package com.jzz.learn.designpatterns.j2ee.businessdelegate;

/**
 * 查询服务
 * @author jzz
 * @date 2019/5/22
 */
public class BusinessLookUp {
    public BusinessService getBusinessService(String serviceType){
        if(serviceType.equalsIgnoreCase("EJB")){
            return new EJBService();
        }else {
            return new JMSService();
        }
    }
}
