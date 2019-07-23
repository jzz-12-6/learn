package queen;

/**
 * @author jzz
 * @date 2019/7/12
 */
public class FIFODemo {

    private Object[] objects;

    private int firstIndex;

    private static final int MAX_SIZE = 10;
    public FIFODemo(){
        objects = new Object[MAX_SIZE];
        firstIndex = 0;
    }
    public boolean enQuene(Object o){
        if(firstIndex>=MAX_SIZE){
            return false;
        }
        objects[firstIndex] = o;
        firstIndex++;
        return true;
    }
    public boolean deQuene(){
        if(firstIndex == 0){
            return false;
        }
        return true;
    }
}
