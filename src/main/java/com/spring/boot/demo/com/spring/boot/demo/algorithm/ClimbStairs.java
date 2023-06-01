package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * @author joe.ly
 * @date 2023/2/20
 */
public class ClimbStairs {
    public static int climbStairs(int n) {
        if (n == 0 || n == 1 || n == 2) return n;
        int[] paths = new int[n + 1];
        paths[0] = 0;
        paths[1] = 1;
        paths[2] = 2;
        for (int i = 3; i <= n; i++) {
            paths[i] = paths[i - 1] + paths[i - 2];
        }
        return paths[n];
    }
    public static int minimumTotal(List<List<Integer>> triangle) {
        if (triangle.size() == 0) return 0;
        int start = triangle.get(0).get(0);
        if (triangle.size() == 1) {
            return start;
        }
        backtrace(triangle, 1, 0, start);
        return minTotal;
    }
    private static int minTotal = Integer.MAX_VALUE;
    private static void backtrace(List<List<Integer>> triangle, int level, int pos, int currSum) {
        List<Integer> selectedList = triangle.get(level);
        if (level == triangle.size() - 1) {
            currSum = currSum + Math.min(selectedList.get(pos + 1), selectedList.get(pos));
            minTotal = Math.min(minTotal, currSum);
            return;
        }
        backtrace(triangle, level + 1, pos, currSum + selectedList.get(pos));
        backtrace(triangle, level + 1, pos + 1, currSum + selectedList.get(pos + 1));
    }
    public static int minimumTotalForDp(List<List<Integer>> triangle) {
        int row = triangle.size();
        int maxCol = triangle.get(row - 1).size();
        int[][] dp = new int[row][maxCol];
        for (int k = 0; k < triangle.get(row - 1).size(); k++) {
            dp[row - 1][k] = triangle.get(row - 1).get(k);
        }
        for (int i = row - 2; i >= 0; i--) {
            for (int j = triangle.get(i).size() - 1; j >= 0; j--) {
                dp[i][j] = Math.min(dp[i + 1][j], dp[i + 1][j + 1]) + triangle.get(i).get(j);
            }
        }
        return dp[0][0];
    }
    public static int minimumTotalForDp1(List<List<Integer>> triangle) {
        int m = triangle.size();
        int[] mini = new int[triangle.get(m - 1).size()];
        for (int k = 0; k < triangle.get(m - 1).size(); k++) {
            mini[k] = triangle.get(m - 1).get(k);
        }
        for (int i = m - 2; i >= 0; i--) {
            for (int j = 0; j < triangle.get(i).size(); j++) {
                mini[j] = triangle.get(i).get(j) + Math.min(mini[j], mini[j + 1]);
            }
        }
        return mini[0];
    }

    public static int maxProduct(int[] nums) {
        if (nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            int subArrMax = nums[i];
            int value = nums[i];
            for (int j = i + 1; j < nums.length; j++) {
                value = value * nums[j];
                if (value > subArrMax) {
                    subArrMax = value;
                }
            }
            max = Math.max(max, subArrMax);
        }
        return max;
    }
    public static int maxProductForDp(int[] nums) {
        int[][] dp = new int[nums.length][2];
        dp[0][0] = nums[0];
        dp[0][1] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > 0) {
                dp[i][0] = Math.max(dp[i - 1][0] * nums[i], nums[i]);
                dp[i][1] = Math.min(dp[i - 1][1] * nums[i], nums[i]);
            } else {
                dp[i][0] = Math.max(dp[i - 1][1] * nums[i], nums[i]);
                dp[i][1] = Math.min(dp[i - 1][0] * nums[i], nums[i]);
            }
        }
        int max = Integer.MIN_VALUE;
        for (int[] ints : dp) {
            max = Math.max(ints[0], max);
        }
        return max;
    }

    public static int maxProfit(int[] prices) {
        int maxProfitValue = 0;
        for (int i = 0; i < prices.length - 1; i++) {
            for (int j = i + 1; j < prices.length; j++) {
                maxProfitValue = Math.max(prices[j] - prices[i], maxProfitValue);
            }
        }
        return maxProfitValue;
    }
    public static int maxProfitForDp(int[] prices) {
        int[][] dp = new int[prices.length][2];
        dp[0][0] = 0;
        dp[0][1] = prices[0];
        for (int i = 1; i < prices.length; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], prices[i] - dp[i - 1][1]);
            dp[i][1] = Math.min(dp[i - 1][1], prices[i]);
        }
        return dp[prices.length - 1][0];
    }

    public static int maxProfit1(int[] prices) {
        int maxProfitValue = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                maxProfitValue += prices[i] - prices[i - 1];
            }
        }
        return maxProfitValue;
    }
    public static int maxProfit1ForDp(int[] prices) {
        int[][] dp = new int[prices.length][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
            dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i]);
        }
        return dp[prices.length - 1][0];
    }

    public static int maxProfit1Two(int[] prices) {
        return maxProfit1TwoBacktrace(prices, 0, 0, 0);
    }

    /**
     * 暴力递归的语义：从i天开始考虑，手头有股票或者没股票的场景下能赚的最大利润
     * 交易的定义：买入时记作交易一次，卖出时不记录
     * @param prices 股票价格
     * @param i 第i天
     * @param hasStock 是否持有股票 0表示手上没有股票，1表示手上有股票
     * @param count 交易的次数
     * @return 最大的利润
     */
    public static int maxProfit1TwoBacktrace(int[] prices, int i, int hasStock, int count) {
        if (i == prices.length || (count >= 2 && hasStock == 0)) {
            return 0;
        }
        if (hasStock == 1) {
            return Math.max(prices[i] + maxProfit1TwoBacktrace(prices, i + 1, 0, count),
                maxProfit1TwoBacktrace(prices, i + 1, hasStock, count));
        } else {
            return Math.max(-prices[i] + maxProfit1TwoBacktrace(prices, i + 1, 1, count + 1),
                maxProfit1TwoBacktrace(prices, i + 1, hasStock, count));
        }
    }
    public static int maxProfit2ForDpTwo(int[] prices) {
        // 每一天可能包含的状态：
        // 0: 不操作
        // 1: 第一次买入
        // 2: 第一次卖出（当天买入之后再卖出）
        // 3: 第二次买入（当天买入卖出，再第二次买入）
        // 4: 第二次卖出（当天买入卖出，再买入之后卖出）
        int[][] dp = new int[prices.length][5];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        dp[0][2] = 0;
        dp[0][3] = -prices[0];
        dp[0][4] = 0;
        for (int i = 1; i < prices.length; i++) {
            dp[i][0] = dp[i - 1][0];
            dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i]);
            dp[i][2] = Math.max(dp[i - 1][2], dp[i - 1][1] + prices[i]);
            dp[i][3] = Math.max(dp[i - 1][3], dp[i - 1][2] - prices[i]);
            dp[i][4] = Math.max(dp[i - 1][4], dp[i - 1][3] + prices[i]);
        }
        return dp[prices.length - 1][4];
    }

    public static int maxProfit1ForDpTwo(int[] prices) {
        // 三维状态的DP：
        // 1. 代表第几天
        // 2. 代表当天之前总共交易了几次
        // 3. 当天是否持有股票: 0不持有，1持有
        // 交易的理解：一次交易指的是买入 + 卖出（在买入的时候交易加1）
        int[][][] dp = new int[prices.length][3][2];
        int len = prices.length;
        int k = 2;
        for (int i = 0; i <= k; i++) {
            dp[0][i][0] = 0;
            dp[0][i][1] = -prices[0];
        }

        for (int i = 1; i < len; i++) {
            for (int j = 1; j <= k; j++) {
                dp[i][j][0] = Math.max(dp[i - 1][j][0], dp[i - 1][j][1] + prices[i]);
                dp[i][j][1] = Math.max(dp[i - 1][j][1], dp[i - 1][j - 1][0] - prices[i]);
            }
        }
        return dp[prices.length - 1][k][0];
    }

    public static int maxProfitWithCoolDown(int[] prices) {
        // 每天的状态分析（状态之间要正交）：
        // 0. 今天处于买入状态（今天买入了，或者之前买入了）
        // 1. 今天处于卖出状态，且2天前卖出了股票
        // 2. 今天刚卖出股票
        // 3. 今天处于冷冻期
        int[][] dp = new int[prices.length][4];
        dp[0][0] = -prices[0];
        dp[0][1] = 0;
        dp[0][2] = 0;
        dp[0][3] = 0;
        for (int i = 1; i < prices.length; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], Math.max(dp[i - 1][1] - prices[i], dp[i - 1][3] - prices[i]));
            dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][3]);
            dp[i][2] = dp[i - 1][0] + prices[i];
            dp[i][3] = dp[i - 1][2];
        }
        return Math.max(Math.max(dp[prices.length - 1][1], dp[prices.length - 1][2]), dp[prices.length - 1][3]);
    }

    public static int maxProfit1ForDpK(int[] prices, int k) {
        if (prices.length == 0) return 0;
        // 0表示不持有股票
        // 1表示持有股票
        int[][][] dp = new int[prices.length][k + 1][2];
        for (int i = 0; i <= k; i++) {
            dp[0][i][0] = 0;
            dp[0][i][1] = -prices[0];
        }
        for (int i = 1; i < prices.length; i++) {
            for (int j = 1; j <= k; j++) {
                dp[i][j][0] = Math.max(dp[i - 1][j][0], dp[i - 1][j][1] + prices[i]);
                dp[i][j][1] = Math.max(dp[i - 1][j][1], dp[i - 1][j - 1][0] - prices[i]);
            }
        }
        return dp[prices.length - 1][k][0];
    }

    public static int maxProfitWithFee(int[] prices, int fee) {
        int[][] dp = new int[prices.length][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0] - fee;
        for (int i = 1; i < prices.length; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
            dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i] - fee);
        }
        return dp[prices.length - 1][0];
    }

    public static void main(String[] args) {
        //int res = climbStairs(4);
        //System.out.println(res);

        // [[-1],[2,3],[1,-1,-3]]
        List<List<Integer>> triangle = new ArrayList<>();
        List<Integer> one = new ArrayList<>();
        one.add(-1);
        triangle.add(one);
        List<Integer> two = new ArrayList<>();
        two.add(2);
        two.add(3);
        triangle.add(two);
        List<Integer> three = new ArrayList<>();
        three.add(1);
        three.add(-1);
        three.add(-3);
        triangle.add(three);
        int res0 = minimumTotalForDp1(triangle);
        //System.out.println(res0);

        int[] nums = new int[] {2,3,-2, 4};
        int res = maxProductForDp(nums);
        //System.out.println(res);

        int[] prices = new int[] {7,1,5,3,6,4};
        int[] prices1 = new int[] {7,6,4,3,1};
        int res1 = maxProfitForDp(prices1);
        //System.out.println(res1);

        int[] prices2 = new int[] {1,2,3,4,5};
        int res2 = maxProfit1ForDp(prices);
        //System.out.println(res2);

        int[] prices3 = new int[] {3,3,5,0,0,3,1,4};
        int res3 = maxProfit1ForDpTwo(prices3);
        //System.out.println(res3);

        int[] prices4 = new int[] {1,2,3,0,2};
        int res4 = maxProfitWithCoolDown(prices4);
        //System.out.println(res4);

        int[] prices5 = new int[] {2,4,1};
        int k = 2;
        int res5 = maxProfit1ForDpK(prices5, k);
        System.out.println(res5);

        int[] prices6 = new int[] {1,3,2,8,4,9};
        int fee = 2;
        int res6 = maxProfitWithFee(prices6, fee);
        System.out.println(res6);
    }
}
