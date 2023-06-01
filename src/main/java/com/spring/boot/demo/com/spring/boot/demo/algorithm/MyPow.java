package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * @author joe.ly
 * @date 2023/1/10
 */
public class MyPow {
    public static double myPow(double x, int n) {
        if (n == 0) return 1;
        if (n > 0) {
            return x * myPow(x, n - 1);
        } else {
            return 1/x * myPow(x, n + 1);
        }
    }
    public static double myPow1(double x, int n) {
        double res = 1.0;
        while (n != 0) {
            if (n > 0) {
                res = x * res;
                n--;
            } else {
                res = 1/x * res;
                n++;
            }
        }
        return res;
    }

    static Map<String, Long> map = new HashMap<>();
    public static double myPow2(double x, long n) {
        if (n == 0) return 1;

        if (n < 0) {
            return 1 / myPow2(x, -n);
        }
        if (n % 2 == 0) {
            return myPow2(x * x, n / 2);
        } else {
            return x * myPow2(x, n - 1);
        }
    }
    public static double myPow3(double x, int n) {
        double res = 1.0;
        for (int i = n; i != 0; i /= 2) {
            if (i % 2 != 0) {
                res *= x;
            }
            x *= x;
        }
        return  n < 0 ? 1/res : res;
    }

    public static void main(String[] args) {
        //double res = myPow2(1.0, -2147483648);
        System.out.println(-3 % 2 == 1);
    }
}
