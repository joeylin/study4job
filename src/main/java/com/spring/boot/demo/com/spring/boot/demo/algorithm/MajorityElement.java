package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author joe.ly
 * @date 2023/1/11
 */
public class MajorityElement {
    public static int majorityElement(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], map.getOrDefault(nums[i], 0) + 1);
        }
        for (Map.Entry entry : map.entrySet()) {
            if ((Integer)entry.getValue() > nums.length / 2) {
                return (Integer)entry.getKey();
            }
        }
        return 0;
    }
    public static int majorityElement2(int[] nums) {
        if (nums.length == 1) return nums[0];
        Arrays.sort(nums);
        int max = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1]) {
                max++;
                if (max > nums.length / 2) return nums[i];
            } else {
                max = 1;
            }
        }
        return 0;
    }
    // 使用分治思想：优先思考是否可以使用分治，因为可以并行计算
    public static int majorityElement3(int[] nums) {

        return 0;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{2,2,1,1,1,2,2};
        int res =majorityElement2(nums);
        System.out.println(res);
    }
}
