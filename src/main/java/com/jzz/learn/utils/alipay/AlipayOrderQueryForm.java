package com.jzz.learn.utils.alipay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @author jzz
 * @date 2019/7/31
 */
@Data
public class AlipayOrderQueryForm {
    /**
     * 支付时传入的商户订单号，与trade_no必填一个
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    /**
     * 支付时返回的支付宝交易号，与out_trade_no必填一个
     */
    @JSONField(name = "trade_no")
    private String tradeNo;


    public static String jsonString(String outTradeNo,String tradeNo){
        AlipayOrderQueryForm form = new AlipayOrderQueryForm();
        form.setOutTradeNo(outTradeNo);
        form.setTradeNo(tradeNo);
        return JSON.toJSONString(form);
    }

    public static void main(String[] args) {
        System.out.println(jsonString("",""));
    }
}
