//package com.jzz.learn.utils.alipay;
//
//import com.alipay.api.AlipayClient;
//import com.alipay.api.DefaultAlipayClient;
//import com.alipay.api.request.AlipayTradeQueryRequest;
//import com.alipay.api.response.AlipayTradeQueryResponse;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * @author jzz
// * @date 2019/7/31
// */
//@Component
//public class AlipayClientUtil {
//
//    @Bean
//    public AlipayClient alipayClient(){
//        return new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",
//                "2016072200101XXXX","请复制第1步中生成的密钥中的商户应用私钥",
//                "json",
//                "utf-8",
//                "沙箱环境RSA2支付宝公钥",
//                "RSA2");
//
//    }
//
//    @Resource private AlipayClient alipayClient;
//
//    public void createAliOrder(){
//
//    }
//
//
//    public void queryOrder() throws Exception{
//        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
//
//
//        AlipayTradeQueryResponse response = alipayClient.execute(request);
//       // response.getBody()
//
//    }
//}
