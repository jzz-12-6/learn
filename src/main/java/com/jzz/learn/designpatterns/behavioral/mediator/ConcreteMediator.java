package com.jzz.learn.designpatterns.behavioral.mediator;

import java.util.ArrayList;
import java.util.List;

/**
 * 具体中介者
 * @author jzz
 * @date 2019/5/16
 */
public class ConcreteMediator extends Mediator{
    private List<Colleague> colleagues=new ArrayList<>();
    @Override
    public void register(Colleague colleague)
    {
        if(!colleagues.contains(colleague))
        {
            colleagues.add(colleague);
            colleague.setMedium(this);
        }
    }
    @Override
    public void relay(Colleague cl)
    {
        for(Colleague ob:colleagues)
        {
            if(!ob.equals(cl))
            {
                ((Colleague)ob).receive();
            }
        }
    }
}
