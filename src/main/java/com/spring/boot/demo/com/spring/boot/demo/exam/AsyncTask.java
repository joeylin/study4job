package com.spring.boot.demo.com.spring.boot.demo.exam;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

/**
 * @author joe.ly
 * @date 2023/5/17
 */
public class AsyncTask {
    public static void main(String[] args) throws Exception {

        List<Function<String, String>> steps = new ArrayList<>();
        steps.add((key) -> {
            System.out.println("aaaa");
            return "aaaa " + key;
        });
        steps.add((key) -> {
            System.out.println("bbbb");
            return "bbbb " + key;
        });
        steps.add((key) -> {
            System.out.println("cccc");
            return "cccc " + key;
        });
        CompletableFuture<String> future = CompletableFuture.completedFuture("new start");
        for (Function<String, String> step : steps) {
            future = future.thenApplyAsync(step);
        }
        future.thenAccept((result) -> {
            System.out.println("end ");
            System.out.println(result);
        });

        System.out.println("start worker");
        Thread.sleep(1000);
        System.out.println("finish sleep");
    }
    static class TaskSchedule {
        private static final String POLL_TASKS_KEY = "POLL_TASKS_KEY";
        static Map<String, TaskDefinition> taskDefinitionMap = new HashMap<>();
        static Map<String, Operator> operatorMap = new HashMap<>();
        ScheduledExecutorService scheduledExecutorService;
        TaskInstanceRepo taskInstanceRepo = new TaskInstanceRepo();
        StepInstanceRepo stepInstanceRepo = new StepInstanceRepo();
        OperatorHandler handler = new DefaultOperatorHandler();
        RedisUtil redisUtil = new RedisUtil();
        private void init() {
            this.scheduledExecutorService = Executors.newScheduledThreadPool(10);
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                try {
                    List<TaskInstance> tasks = pollTasks();
                    for (TaskInstance task : tasks) {
                        processTask(task);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }, 0, 60, TimeUnit.SECONDS);
        }
        private List<TaskInstance> pollTasks() {
            List<String> statusList = new ArrayList<>();
            statusList.add("Running");
            statusList.add("Ready");
            String lock = redisUtil.tryLock(POLL_TASKS_KEY, 1000);
            try {
                List<TaskInstance> tasks = taskInstanceRepo.queryByStatus(statusList, 100);
                for (TaskInstance task : tasks) {
                    task.setStatus("Processing");
                }
                taskInstanceRepo.save(tasks);
                return tasks;
            } catch (Exception e) {
                throw e;
            } finally {
                redisUtil.unlock(lock);
            }
        }
        private void processTask(TaskInstance taskInstance) {
            List<StepInstance> stepInstances = stepInstanceRepo.queryStepsInTaskByNumOrder(taskInstance.getTaskId());
            for (StepInstance step : stepInstances) {
                if (step.status.equals("Skipped") || step.status.equals("Success")) {
                    if (stepInstances.lastIndexOf(step) == stepInstances.size() - 1) {
                        handleSuccessTask(taskInstance, step);
                        return;
                    }
                    continue;
                }
                if (step.status.equals("Failed")) {
                    handleFailedTask(taskInstance, step);
                    return;
                }
                if (step.status.equals("Running") && hasRunningStepTimeout(step)) {
                    handleFailedTask(taskInstance, step);
                    return;
                }
                executeStep(taskInstance, step, handler);
            }
            taskInstance.setStatus("Running");
            taskInstanceRepo.save(taskInstance);
        }
        private void handleSuccessTask(TaskInstance taskInstance, StepInstance step) {
            taskInstance.setStatus("Success");
            taskInstanceRepo.save(taskInstance);
            TaskDefinition taskDefinition = taskDefinitionMap.get(taskInstance.getTaskName());
            taskDefinition.onSuccess(taskInstance, step);
        }
        private void handleFailedTask(TaskInstance taskInstance, StepInstance step) {
            taskInstance.setStatus("Failed");
            taskInstanceRepo.save(taskInstance);
            TaskDefinition taskDefinition = taskDefinitionMap.get(taskInstance.getTaskName());
            taskDefinition.onFail(taskInstance, step);
        }
        private void executeStep(TaskInstance taskInstance, StepInstance step, OperatorHandler handler) {
            Operator operator = operatorMap.get(step.stepName);
            operator.execStep(taskInstance, step, handler);
        }
        private boolean hasRunningStepTimeout(StepInstance step) {
            return step.config.timeout - System.currentTimeMillis() > 0;
        }
        private void destroy() {
            this.scheduledExecutorService.shutdown();
        }
    }
    static interface Operator {
        String getName();
        void execStep(TaskInstance task, StepInstance step, OperatorHandler handler);
    }
    @Data
    static class TaskInstance {
        private String taskName;
        private String taskId;
        private List<String> stepList;
        private String status;
        private JSONObject context;
    }
    @Data
    static class StepInstance {
        private String stepName;
        private int num;
        private StepConfig config;
        private String taskId;
        private String status;
        private JSONObject inputData;
        private JSONObject outputData;
        private int retries;
    }
    static class StepConfig {
        private long timeout;
        private int maxTries;
    }
    static interface OperatorHandler {
        void onSuccessAndTriggerNext(TaskInstance task, StepInstance current);
        void onSuccessFast(TaskInstance task, StepInstance current);
        void onWaitAndRetry(TaskInstance task, StepInstance current);
        void onFailFast(TaskInstance task, StepInstance current);
    }
    static class DefaultOperatorHandler implements OperatorHandler {
        StepInstanceRepo stepInstanceRepo = new StepInstanceRepo();
        @Override
        public void onSuccessAndTriggerNext(TaskInstance task, StepInstance current) {
            current.setStatus("Success");
            stepInstanceRepo.save(current);
        }
        @Override
        public void onSuccessFast(TaskInstance task, StepInstance current) {
            List<StepInstance> steps = stepInstanceRepo.queryStepsInTaskByNumOrder(task.getTaskId());
            for (StepInstance step : steps) {
                if (step.getNum() < current.getNum()) continue;
                if (step.getNum() == current.getNum()) {
                    current.setStatus("Success");
                    continue;
                }
                step.setStatus("Skipped");
            }
            stepInstanceRepo.save(steps);
        }
        @Override
        public void onWaitAndRetry(TaskInstance task, StepInstance current) {
            if (current.getRetries() < current.getConfig().maxTries) {
                current.setRetries(current.getRetries() + 1);
                current.setStatus("WaitAndRetry");
            } else {
                current.setStatus("Failed");
            }
            stepInstanceRepo.save(current);
        }
        @Override
        public void onFailFast(TaskInstance task, StepInstance current) {
            current.setStatus("Failed");
            stepInstanceRepo.save(current);
        }
    }
    static abstract class TaskDefinition {
        abstract String getName();
        abstract List<String> getStepList();

        void onSuccess(TaskInstance task, StepInstance lastStep) {
            // 异步任务执行结果反馈给上游
        }
        void onFail(TaskInstance task, StepInstance failStep) {
            // 异步任务执行结果反馈给上游
        }
    }
    static class TaskInstanceRepo {
        public void save(TaskInstance task) {}
        public void save(List<TaskInstance> tasks) {}
        public TaskInstance query(String taskId) {
            return new TaskInstance();
        }
        public List<TaskInstance> queryByStatus(List<String> statusList, int limit) {
            return new ArrayList<>();
        }
    }
    static class StepInstanceRepo {
        public void save(List<StepInstance> steps) {}
        public void save(StepInstance step) {}
        public List<StepInstance> queryStepsInTaskByNumOrder(String taskId) {
            return new ArrayList<>();
        }
    }
    static class RedisUtil {
        String tryLock(String key, long timeout) {
            return "";
        }
        void unlock(String lockKey) {
        }
    }
}
