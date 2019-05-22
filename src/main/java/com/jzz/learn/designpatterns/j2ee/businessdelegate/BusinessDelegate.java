package com.jzz.learn.designpatterns.j2ee.businessdelegate;

/**
 * 业务代表
 * @author jzz
 * @date 2019/5/22
 */
public class BusinessDelegate {
    private BusinessLookUp lookupService = new BusinessLookUp();
    private BusinessService businessService;
    private String serviceType;

    public void setServiceType(String serviceType){
        this.serviceType = serviceType;
    }

    public void doTask(){
        businessService = lookupService.getBusinessService(serviceType);
        businessService.doProcessing();
    }
}
