package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author joe.ly
 * @date 2023/2/28
 */
public class LengthOfLIS {
    public static int sum(int a, int b) {
        return a + b;
    }
    // 功能：求最长递增子序列的长度
    public static int lengthOfLIS(int[] nums) {
        int max = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            int count = 1;
            int curr = nums[i];
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] > curr) {
                    curr = nums[j];
                    count++;
                }
            }
            max = Math.max(max, count);
        }
        return max;
    }
    private static int max = 0;

    public static void main(String[] args) {
        int[] nums = new int[]{10,9,2,5,3,7,101,18};
        int[] nums1 = new int[] {0,1,0,3,2,3};
        int res = lengthOfLIS(nums1);
        System.out.println(res);

        //String[] strs = new String[]{"flower","flow","flight"};
        //Map<String, String> map = new HashMap<>();
        //Arrays.stream(strs).reduce(map, (v, k) -> {
        //    System.out.println(v);
        //    System.out.println(k);
        //    return v;
        //});
    }
}
