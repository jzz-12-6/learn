package com.jzz.learn.utils.email;

/**
 * 邮件服务
 * @author jzz
 * @date 2019年3月8日
 */
public interface EmailService {
    /**
     * 发送邮件
     * @param to 收件人邮箱
     * @param title 邮件标题
     * @param content 邮件内容
     */
    void sendEmail(String to,String title,String content);
}
