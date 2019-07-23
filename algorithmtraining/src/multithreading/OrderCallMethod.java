package multithreading;

/**
 * 顺序调用方法
 * 线程 A 将会调用 one() 方法
 * 线程 B 将会调用 two() 方法
 * 线程 C 将会调用 three() 方法
 * 请设计修改程序，以确保 two() 方法在 one() 方法之后被执行，three() 方法在 two() 方法之后被执行。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/print-in-order
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @author jzz
 * @date 2019/7/15
 */
public class OrderCallMethod {

    private int flag;
    public OrderCallMethod(){
        flag = 1;
    }

    public void first(Runnable printFirst) throws InterruptedException {
        while (flag !=1){}
        printFirst.run();
        flag =2;
        // printFirst.run() outputs "first". Do not change or remove this line.

    }

    public void second(Runnable printSecond) throws InterruptedException {

        // printSecond.run() outputs "second". Do not change or remove this line.
        while (flag !=2){}
        printSecond.run();
        flag = 3;
    }

    public void third(Runnable printThird) throws InterruptedException {

        // printThird.run() outputs "third". Do not change or remove this line.
        while (flag !=3){}
        printThird.run();
    }

    public static void main(String[] args) throws Exception{

        OrderCallMethod orderCallMethod = new OrderCallMethod();
        Thread thread = new Thread(()->
        {try {
            orderCallMethod.third(()-> System.out.println(1));
        }catch (Exception e){
            System.out.println(e);
        }
        });
        orderCallMethod.first(()-> System.out.println(3));

        thread.start();
        orderCallMethod.second(()-> System.out.println(2));

    }
}
