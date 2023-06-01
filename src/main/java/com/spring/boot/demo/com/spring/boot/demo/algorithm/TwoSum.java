package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author joe.ly
 * @date 2023/1/4
 */
public class TwoSum {
    public static int[] twoSum(int[] nums, int target) {
        int[] res = new int[2];
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] == target - num) {
                    res[0] = i;
                    res[1] = j;
                    return res;
                }
            }
        }
        return res;
    }

    public static int[] twoSum2(int[] nums, int target) {
        Map<Integer, Integer> numMap = new HashMap<>();
        int[] res = new int[2];
        for (int i = 0; i < nums.length; i++) {
            if (numMap.containsKey(target - nums[i])) {
                res[0] = numMap.get(target - nums[i]);
                res[1] = i;
            }
            numMap.put(nums[i], i);
        }
        return res;
    }

    public static int[] twoSum3(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        int[] res = new int[2];
        Arrays.sort(nums);

        while (lo < hi) {
            int left = nums[lo];
            int right = nums[hi];
            if (left + right > target) {
                hi--;
                continue;
            }
            if (left + right < target) {
                lo++;
                continue;
            }
            res[0] = nums[lo];
            res[1] = nums[hi];
            while (lo < hi && nums[lo] == left) {
                lo++;
            }
            while (lo < hi && nums[hi] == right) {
                hi--;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{2,7,11,5};
        int target = 9;

        int[] res = TwoSum.twoSum3(nums, target);
        for (int num : res) {
            System.out.println(num);
        }
    }
}
