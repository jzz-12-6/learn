package arrays;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 递归
 * @author jzz
 * @date 2019/7/4
 */
public class ArraysProgram {


    public static void main(String[] args) {
        System.out.println(fib(4));
    }

    public static int fib(int N) {
        if(N == 0){
            return 0;
        }
        if(N == 1){
            return 1;
        }
        return fib(N-2) + fib(N-1);
    }
    public static int heightChecker(int[] heights) {
        int[] copy = Arrays.copyOf(heights, heights.length);
        Arrays.sort(copy);
        int length = copy.length;
        int count = 0;
        for (int i = 0; i < length; i++) {
            if(copy[i] != heights[i]){
                count++;
            }
        }
        return count;
    }
    /**
     * 832. 翻转图像
     * 给定一个二进制矩阵 A，我们想先水平翻转图像，然后反转图像并返回结果。
     *
     * 水平翻转图片就是将图片的每一行都进行翻转，即逆序。例如，水平翻转 [1, 1, 0] 的结果是 [0, 1, 1]。
     *
     * 反转图片的意思是图片中的 0 全部被 1 替换， 1 全部被 0 替换。例如，反转 [0, 1, 1] 的结果是 [1, 0, 0]。
     * 输入: [[1,1,0],[1,0,1],[0,0,0]]
     * 输出: [[1,0,0],[0,1,0],[1,1,1]]
     * 解释: 首先翻转每一行: [[0,1,1],[1,0,1],[0,0,0]]；
     *      然后反转图片: [[1,0,0],[0,1,0],[1,1,1]]
     * @param A 指定数组
     * @return int[][]
     */
    public static int[][] flipAndInvertImage(int[][] A) {
        int length = A.length;
        for(int i = 0;i<length;i++){
            swapAndReverse(A[i]);
        }
        return A;
    }
    private static void swapAndReverse(int[] a){
        int length = a.length;
        for(int i = 0;i<length/2;i++){
            int temp = a[length-i-1] == 0 ? 1 : 0;
            int temp2 = a[i]  == 0 ? 1 : 0;
            a[i] = temp;
            a[length-i-1] = temp2;
        }
        int half = length / 2;
        if(half * 2 != length  ){
            int temp2 = a[half]  == 0 ? 1 : 0;
            a[half] = temp2;
        }
    }
    /**
     * 119. 杨辉三角 II
     * 给定一个非负索引 k，其中 k ≤ 33，返回杨辉三角的第 k 行。
     * 输入: 3
     * 输出: [1,3,3,1]
     * @param rowIndex 行数
     * @return List<Integer>
     */
    public List<Integer> getRow(int rowIndex) {
        if (rowIndex == 1){
            return Collections.singletonList(1);
        }else if(rowIndex == 2){
            return Arrays.asList(1,1);
        }else {
            List<Integer> list = new ArrayList<>(rowIndex);
            list.add(1);
            int temp = rowIndex -1;
            int length = rowIndex / 2;
            if(length * 2 != rowIndex){
                length++;
            }
            for (int i = 1;i<length;i++){
                list.add(temp);

            }
            return list;
        }
    }
    /**
     * 189. 旋转数组
     * 给定一个数组，将数组中的元素向右移动 k 个位置，其中 k 是非负数。
     * 输入: [1,2,3,4,5,6,7] 和 k = 3
     * 输出: [5,6,7,1,2,3,4]
     * 解释:
     * 向右旋转 1 步: [7,1,2,3,4,5,6]
     * 向右旋转 2 步: [6,7,1,2,3,4,5]
     * 向右旋转 3 步: [5,6,7,1,2,3,4]
     * @param nums 数组
     * @param k 移动的位置
     */
    public static void rotate(int[] nums, int k) {
        if(k<=0){
            return;
        }
        int length = nums.length;
        for(int j =0;j<k;j++){
            int last = nums[length-1];
            for(int i = length -1;i>0;i--){
                nums[i] = nums[i-1];
            }
            nums[0] = last;
        }
        System.out.println(Arrays.toString(nums));
    }

    public static int[] intersect(int[] nums1, int[] nums2) {
        int length1 = nums1.length;
        Map<Integer,Integer> map1 = new HashMap<>(length1);
        for(int i : nums1){
            map1.merge(i,1,(x,y)->x+y);
        }
        int length2 = nums2.length;
        Map<Integer,Integer> map2 = new HashMap<>(length2);
        for(int i : nums2){
            map2.merge(i,1,(x,y)->x+y);
        }
        List<Integer> list = new ArrayList<>();
        map1.forEach((k,v)->{
            Integer i = map2.get(k);
            if(i!=null){
               int num = Math.min(i,v);
                for(int j =0;j<num;j++){
                    list.add(k);
                }
            }
        });
        int[] resultArray = new int[list.size()];
        int index = 0;
        for(Integer i : list){
            resultArray[index] = i;
            index++;
        }
        return resultArray;
    }

    public int removeDuplicates(int[] nums) {
        int firstIndex = 0;
        int lastIndex = 1;
        int length = nums.length;
        while (true){
            if(lastIndex < length){
                break;
            }
            if(nums[firstIndex]!=nums[lastIndex]){
                firstIndex++;
                lastIndex++;
            }else {
                //确定下一个不等的下标
                int i = firstIndex;
                int j = lastIndex;
                for(;i<length;i++){

                }
                //移动数组


            }
        }
        return 0;
    }
    /**
     * 557 反转字符串中的单词 III
     * 给定一个字符串，你需要反转字符串中每个单词的字符顺序，同时仍保留空格和单词的初始顺序。
     * 输入: "Let's take LeetCode contest"
     * 输出: "s'teL ekat edoCteeL tsetnoc"
     * @param s
     * @return
     */
    public static String reverseWords3(String s) {
        String[] strings = s.split(" ");
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<strings.length;i++){
            sb.append(reverseSingle(strings[i])).append(' ');
        }
        System.out.println(sb.toString().trim());
        return sb.toString().trim();
    }

    public static String reverseSingle(String s){
        char[] chars = s.toCharArray();
        int length = chars.length;
        char[] newChars = new char[length];
        for(int i = length-1,j=0;i>=0;i--,j++){
           newChars[j] = chars[i];
        }
        return new String(newChars);
    }
    /**
     * 151. 翻转字符串里的单词
     * 给定一个字符串，逐个翻转字符串中的每个单词。
     * 输入: "  hello world!  "
     * 输出: "world! hello"
     * 解释: 输入字符串可以在前面或者后面包含多余的空格，但是反转后的字符不能包括。
     * @param s
     * @return
     */
    public static String reverseWords(String s) {
        char[] chars = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        //去除空格
        for(int i=0,j=i+1;i<chars.length;i++,j++){
            char c = chars[i];
            if(filter(c)){
                sb.append(c);
                if(j<chars.length-1 && !filter(chars[j])){
                    sb.append(' ');
                }
            }
        }
        //去除尾部空格
        String[] strings = sb.toString().trim().split(" ");
        sb = new StringBuilder();
        //头尾交换
        for(int i=0,j=strings.length-1;i<strings.length/2;i++,j--){
            String temp = strings[i];
            strings[i] = strings[j];
            strings[j] = temp;
        }
        for(String string : strings){
            sb.append(string).append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * 排除两种空格
     * @param c
     * @return
     */
    private static boolean filter(char c){
        return c != 32 && c != 160;
    }



    private static final String[] wordsArray = new String[]{".-","-...","-.-.","-..",".","..-.","--.","....","..",".---","-.-",".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...-",".--","-..-","-.--","--.."};

    /**
     * 804. 唯一摩尔斯密码词
     * @param words
     * @return
     */
    public static int uniqueMorseRepresentations(String[] words) {
        Stream<String> stream = Arrays.stream(words);
        Set<String> collect = stream.map(s -> {
            char[] chars = s.toLowerCase().toCharArray();
            StringBuilder sb = new StringBuilder();
            for (char c : chars) {
                sb.append(wordsArray[c - 'a']);
            }
            return sb.toString();
        }).collect(Collectors.toSet());
        return collect.size();
    }

    /**
     * 977 有序数组的平方
     * 给定一个按非递减顺序排序的整数数组 A，返回每个数字的平方组成的新数组，要求也按非递减顺序排序。
     * 输入：[-4,-1,0,3,10]
     * 输出：[0,1,9,16,100]
     * @param A
     * @return
     */
    public int[] sortedSquares(int[] A) {
        for(int i =0;i<A.length;i++){
            A[i] = A[i] * A[i];
        }
        Arrays.sort(A);
        return A;
    }
    /**
     * 771
     * 给定字符串J 代表石头中宝石的类型，和字符串 S代表你拥有的石头。 S 中每个字符代表了一种你拥有的石头的类型，你想知道你拥有的石头中有多少是宝石。
     *
     * J 中的字母不重复，J 和 S中的所有字符都是字母。字母区分大小写，因此"a"和"A"是不同类型的石头。
     * 输入: J = "aA", S = "aAAbbbb"
     * 输出: 3
     * @param J
     * @param S
     * @return
     */
    public int numJewelsInStones(String J, String S) {
        int count = 0;
        char[] gem = J.toCharArray();
        char[] stone = S.toCharArray();
        Set<Character> set = new HashSet<>(stone.length);
        for(char c : gem){
            set.add(c);
        }
        for(char s :stone){
            if(set.contains(s)){
                count++;
            }
        }
        return count;
    }
    /**
     * 1108
     * 给你一个有效的 IPv4 地址 address，返回这个 IP 地址的无效化版本。
     * 所谓无效化 IP 地址，其实就是用 "[.]" 代替了每个 "."。
     * 输入：address = "1.1.1.1"
     * 输出："1[.]1[.]1[.]1"
     * @param address
     * @return
     */
    public String defangIPaddr(String address) {
        char[] chars = address.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char c : chars){
            if(c == '.'){
                sb.append("[").append(c).append("]");
            }else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 给定一个非负整数数组 A，返回一个数组，在该数组中， A 的所有偶数元素之后跟着所有奇数元素。
     * 你可以返回满足此条件的任何数组作为答案。
     * 题目905
     * @param A 数组
     * @return 数组
     */
    public static int[] sortArrayByParity(int[] A) {
        //新建一个数组，偶数放左边，奇数放右边
        int length = A.length-1;
        int[] evenArrays = new int[A.length];
        int evenArraysIndex = 0;
        for(int i : A){
            if(i % 2 == 0){
                evenArrays[evenArraysIndex] = i;
                evenArraysIndex++;
            }else {
                evenArrays[length] = i;
                length--;
            }
        }
        //直接对原数组进行修改
        int firstIndex = 0;
        int lastIndex = A.length-1;
        while (firstIndex<lastIndex){
            int i = A[firstIndex];
            //偶数
            if(i % 2 == 0){
                firstIndex ++;
            }else {
                while (lastIndex>firstIndex){
                    int j = A[lastIndex];
                    //找到一个偶数并交换
                    if(j % 2 == 0){
                        int temp = A[lastIndex] ;
                        A[lastIndex] = A[firstIndex];
                        A[firstIndex] =temp;
                        break;
                    }else {
                        lastIndex--;
                    }
                }
            }
        }
        return A;
    }

    /**
     * 给定一个含有 n 个正整数的数组和一个正整数 s ，找出该数组中满足其和 ≥ s 的长度最小的连续子数组。如果不存在符合条件的连续子数组，返回 0。
     * 输入: s = 7, nums = [2,3,1,2,4,3]
     * 输出: 2
     * 解释: 子数组 [4,3] 是该条件下的长度最小的连续子数组。
     * @param s 和
     * @param nums 数组
     * @return
     */
    public static int minSubArrayLen(int s, int[] nums) {
        Map.of(1,1);
        int length = nums.length;
        if(length==1){
            if(nums[0] == s){
                return 1;
            }else {
                return 0;
            }
        }
        for(int i = 0;i<length;i++){
            int sum = nums[i];
            if(sum == s){
                return 1;
            }
            for(int j = i+1; j<length;j++){
                int x = nums[j] - nums[j-1];
                if (Math.abs(x) == 1){
                    sum = sum  + nums[j];
                    if(sum == s){
                        return j-i+1;
                    }
                }else {
                    break;
                }
            }
        }
        return 0;
    }
}
