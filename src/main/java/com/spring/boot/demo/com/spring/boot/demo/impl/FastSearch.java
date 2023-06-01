package com.spring.boot.demo.com.spring.boot.demo.impl;

import java.util.Arrays;

/**
 * @author joe.ly
 * @date 2023/5/1
 */
public class FastSearch {
    public static void main(String[] args) {
        // todo: 对比Arrays.sort实现
        int[] arr = new int[]{4,7,6,2,1,3,5};
        int[] newArr = fastSearch(arr, 0, arr.length);
        System.out.println(Arrays.toString(newArr));
    }
    public static int[] fastSearch(int[] arr, int left, int right) {
        if (left >= right) {
            return arr;
        }
        int i =  partition(arr, left, right);
        fastSearch(arr, left, i);
        fastSearch(arr, i + 1, right);
        return arr;
    }
    public static int partition(int[] arr, int left, int right) {
        // 使用同向双指针
        int pivot = arr[left];
        int j = left - 1;
        for (int i = left; i < right; i++) {
            if (arr[i] <= pivot) {
                j++;
                swap(arr, i, j);
            }
        }
        swap(arr, left, j);
        return j;
    }
    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static int partition1(int[] arr, int left, int right) {
        int pivot = arr[left];
        int start = left - 1;
        for (int i = left; i < right; i++) {
            if (arr[left] <= pivot) {
                start++;
                swap(arr, start, i);
            }
        }
        swap(arr, start, left);
        return start;
    }
    public int[] fastSearch1(int[] arr, int left, int right) {
        if (left >= right) {
            return arr;
        }
        int partPos = partition(arr, left, right);
        fastSearch1(arr, left, partPos);
        fastSearch1(arr, partPos + 1, right);
        return arr;
    }
}
