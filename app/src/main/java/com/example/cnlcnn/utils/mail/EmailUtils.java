package com.example.cnlcnn.utils.mail;

import com.example.cnlcnn.entity.Constants;
import com.sun.mail.util.MailSSLSocketFactory;

import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by cnlcnn on 2017/5/29.
 */

public class EmailUtils {
    private String mailContext;

    public EmailUtils(String context){
        this.mailContext = context;
    }

    public void sendMail() throws MessagingException, GeneralSecurityException {
        Properties props = new Properties();
        //使用smtp代理，且使用网易163邮箱
        props.put("mail.smtp.host", "smtp.163.com");
        //设置验证
        props.put("mail.smtp.auth", "true");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);

        MyAuthenticator myauth = new MyAuthenticator(Constants.SYSTEM_EMAIN_ADRESS, Constants.SYSTEM_EMAIN_ADRESS_AUTHORIZE_CODE);
        Session session = Session.getInstance(props, myauth);
        //打开调试开关
        session.setDebug(true);

        MimeMessage message = new MimeMessage(session);
        InternetAddress fromAddress = null;
        //发件人邮箱地址
        fromAddress = new InternetAddress(Constants.SYSTEM_EMAIN_ADRESS);
        message.setFrom(fromAddress);

        InternetAddress toAddress = new InternetAddress(Constants.SYSTEM_EMAIN_ADRESS);
        message.addRecipient(Message.RecipientType.TO, toAddress);
        message.setSubject(Constants.EMAIL_SUBJECT);
        message.setText(mailContext);// 设置邮件内容
        //message.setFileName("邮件附件");
        message.saveChanges(); //存储信息

        Transport transport = null;
        transport = session.getTransport("smtp");
//        transport.connect("smtp.163.com", "发件人邮箱@163.com", "密码");
        transport.connect("smtp.163.com", Constants.SYSTEM_EMAIN_ADRESS, Constants.SYSTEM_EMAIN_ADRESS_AUTHORIZE_CODE);
        transport.sendMessage(message, message.getAllRecipients());

        transport.close();
    }
}
