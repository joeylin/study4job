package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

/**
 * @author joe.ly
 * @date 2023/1/4
 */
public class ThreeSum {
    public static List<List<Integer>> twoSum(int[] nums, int start, int target) {
        int lo = start, hi = nums.length - 1;
        List<List<Integer>> res = new ArrayList<>();
        while (lo < hi) {
            int left = nums[lo], right = nums[hi];
            int sum = left + right;
            if (sum < target) {
                lo++;
            } else if (sum > target) {
                hi--;
            } else {
                List<Integer> item = new ArrayList<>();
                item.add(left);
                item.add(right);
                res.add(item);
                while (lo < hi && nums[lo] == left) lo++;
                while (lo < hi && nums[hi] == right) hi--;
            }
        }
        return res;
    }
    // 这道题比较特殊的地方在于还需要返回的是具体的值，并且数组的具体指不能重复
    public static List<List<Integer>> threeSum(int[] nums, int target) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < nums.length - 2; i++) {
            List<List<Integer>> tuples = twoSum(nums, i + 1, target - nums[i]);
            for (List<Integer> tuple : tuples) {
                tuple.add(nums[i]);
                res.add(tuple);
            }
            while (i < nums.length - 2 && nums[i] == nums[i + 1]) {
                i++;
            }
        }
        return res;
    }

    public static List<List<Integer>> threeSum1(int[] nums, int target) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {
            int lo = i + 1, hi = nums.length - 1;
            while (lo < hi) {
                int left = nums[lo], right = nums[hi];
                int sum = left + right;
                if (sum < target) {
                    lo++;
                } else if (sum > target) {
                    hi--;
                } else {
                    List<Integer> item = new ArrayList<>();
                    item.add(left);
                    item.add(right);
                    item.add(nums[i]);
                    res.add(item);

                    // 去重
                    while (lo < hi && nums[lo] == left) lo++;
                    while (lo < hi && nums[hi] == right) hi--;
                }
            }
            // 去重
            while (i < nums.length - 2 && nums[i] == nums[i + 1]) i++;
        }
        return res;
    }

    public static List<List<Integer>> threeSum2(int[] nums, int target) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            Map<Integer, Integer> map = new HashMap<>();
            for (int j = i + 1; j < nums.length; j++) {
                if (map.containsKey(target - nums[i] - nums[j])) {
                    if (map.get(target - nums[i] - nums[j]) == 0) {
                        continue;
                    } else {
                        map.put(target - nums[i] - nums[j], 1);
                    }
                    List<Integer> item = Arrays.asList(nums[i], nums[j], target - nums[i] - nums[j]);
                    res.add(item);
                } else {
                    map.put(nums[j], 0);
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{-1,0,1,2,-1,-4};
        List<List<Integer>> res = ThreeSum.threeSum2(nums, 0);
        System.out.println(JSON.toJSONString(res));
    }
}
