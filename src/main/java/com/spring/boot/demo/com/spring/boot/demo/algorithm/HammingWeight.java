package com.spring.boot.demo.com.spring.boot.demo.algorithm;

/**
 * @author joe.ly
 * @date 2023/2/19
 */
public class HammingWeight {
    public int hammingWeight(int n) {
        int count = 0;
        for (int i = 0; i < 32; i++) {
            if ((n & 1) == 1) {
                count++;
            }
            n >>= 1;
        }
        return count;
    }
    public int hammingWeight1(int n) {
        int count = 0;
        while (n != 0) {
            count++;
            n = n&(n - 1);
        }
        return count;
    }

    public static void main(String[] args) {
        HammingWeight hammingWeight = new HammingWeight();
        int res = hammingWeight.hammingWeight1(-3);
        System.out.println(res);
        System.out.println( 11 & 10);
        System.out.println((1 << 4) - 1);
    }
}
