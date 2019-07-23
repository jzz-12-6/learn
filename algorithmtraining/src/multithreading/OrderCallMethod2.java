package multithreading;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
public class OrderCallMethod2 {
    private Lock lockSecond = new ReentrantLock();
    private Lock lockThird = new ReentrantLock();

    public OrderCallMethod2(){
        lockSecond.lock();
        lockThird.lock();
    }

    public void first(Runnable printFirst) throws InterruptedException {
        printFirst.run();
        lockSecond.unlock();
    }

    public void second(Runnable printSecond) throws InterruptedException {

        printSecond.run();

    }

    public void third(Runnable printThird) throws InterruptedException {

        printThird.run();
    }

    public static void main(String[] args) throws Exception{

        OrderCallMethod2 orderCallMethod = new OrderCallMethod2();
        Thread t = new Thread();
        orderCallMethod.first(t);
        orderCallMethod.second(()-> System.out.println(2));
        orderCallMethod.third(()-> System.out.println(3));
    }
}
