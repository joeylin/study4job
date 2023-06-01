package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author joe.ly
 * @date 2023/1/11
 */
public class MaxProfit {
    public static int maxProfit(int[] prices) {
        int maxProfit = 0;
        for (int i = 0; i < prices.length - 1; i++) {
            int buyPoint = prices[i];
            while (i < prices.length - 1 && prices[i] < prices[i + 1]) {
                i++;
            }
            if (buyPoint < prices[i]) {
                maxProfit += (prices[i] - buyPoint);
            }
        }
        return maxProfit;
    }
    // 使用动态规划算法，
    public static int maxProfit1(int[] prices) {
        if (prices.length == 0) return 0;
        int len = prices.length;
        int[][] dp = new int[len][2];
        // dp[i][0]表示第i天不持有股票的收支最大值, dp[i][1]表示第i天持有股票的收支最大值
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        for (int i = 1; i < len; i++) {
            dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i]);
            dp[i][1] = Math.max(dp[i-1][1], dp[i-1][0] - prices[i]);
        }
        return dp[len - 1][0];
    }
    // 使用递归的方法
    private static List<Integer> maxProfitValue = new ArrayList<>();
    public static void maxProfitRecursion(int[] prices, int i, boolean isHave, int profit) {
        if (i == prices.length - 1) {
            maxProfitValue.add(profit);
            return;
        }
        if (isHave) {
            // 当前持有
            maxProfitRecursion(prices, i + 1, true, profit);
            maxProfitRecursion(prices, i + 1, false, profit + prices[i]);
        } else {
            // 当前未持有
            maxProfitRecursion(prices, i + 1, true, profit - prices[i]);
            maxProfitRecursion(prices, i + 1, false, profit);
        }
    }
    public static int maxProfit2(int[] prices) {
        maxProfitRecursion(prices, 0, false, 0);
        return maxProfitValue.stream().max((a, b) -> a > b ? a : b).orElse(0);
    }


    public static void main(String[] args) {
        int[] prices = new int[]{7,1,5,3,6,4};
        int res = maxProfit1(prices);
        System.out.println(res);

        int res1 = maxProfit1(new int[]{1,2,3,4,5});
        System.out.println(res1);

        int res2 = maxProfit1(new int[]{7,6,4,3,1});
        System.out.println(res2);
    }
}
