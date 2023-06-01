package com.spring.boot.demo.com.spring.boot.demo.algorithm;

/**
 * @author joe.ly
 * @date 2023/2/13
 */
public class Sqrt {
    public int mySqrt(int x) {
        if (x == 0 || x == 1) return x;
        int left = 0, right = x, res = 0;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (mid == x/mid) {
                return mid;
            } else if (mid < x/mid) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
            res = Math.min(left, right);
        }
        return res;
    }

    public static void main(String[] args) {
        Sqrt sqrt = new Sqrt();
        int res = sqrt.mySqrt(15);
        System.out.println(res);
    }
}
