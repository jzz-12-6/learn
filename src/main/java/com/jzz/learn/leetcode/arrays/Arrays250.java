package com.jzz.learn.leetcode.arrays;



import org.junit.Test;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 数组相关题目 200-250题
 * @author jzz
 * @date 2019年3月11日
 */
public class Arrays250 {


    public static void main(String[] args) {

    }
    /**
     * 217
     * 给定一个整数数组，判断是否存在重复元素。
     *
     * 如果任何值在数组中出现至少两次，函数返回 true。如果数组中每个元素都不相同，则返回 false。
     * @param nums
     */
    @Test
    public void containsDuplicate() {
        int[] nums = new int[0];
        Map<Integer,Integer> map = new HashMap<>();
        for (int i:nums) {
            if(map.get(i) != null){
                Assert.isTrue(map.get(i) != null,"重复元素为："+i);
            }
            map.put(i,i);
        }
        List<Integer> list = new ArrayList<>(nums.length);
        for (int i:nums) {
            if(list.contains(i)){
                Assert.isTrue(map.get(i) != null,"重复元素为："+i);
            }
            list.add(i);
        }
    }

    /**
     * 给定两个有序整数数组 nums1 和 nums2，将 nums2 合并到 nums1 中，使得 num1 成为一个有序数组。
     * 输入:
     * nums1 = [1,2,3,0,0,0], m = 3
     * nums2 = [2,5,6],       n = 3
     *
     * 输出: [1,2,2,3,5,6]
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     */
    @Test
    public void merge(int[] nums1, int m, int[] nums2, int n) {
       int[] a = new int[nums1.length];
       for(int i = 0 ; i< m ;i++){
           a[i] = nums1[i];
       }
       for(int i = 0;i<n;i++){
           a[m+i] = nums2[i];
       }
       Arrays.sort(a);
       nums1 = a;
    }
}
