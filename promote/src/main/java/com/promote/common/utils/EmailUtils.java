package com.promote.common.utils;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class EmailUtils {
    /** Email服物器的SMTP地址 */
    public static final String HOST = "smtp.gmail.com";
    /** Email服物器的SMTP端口 */
    public static final int PORT = 587;
    public static final String SMTP = "smtp";
    /** 發信人Email */
    public static final String FROM = "dakhpc72@gmail.com";
    /** 發信人Email密碼 */
    public static final String PWD = "r150y9a018n";
    public static final String TITLE = "振興抵用券";
    public static Transport transport;

    public static void main(String[] s){
        try {
            sendEmail("dakhpc72@gmail.com");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     *發送Email
     *
     * @param toEmail 收信人Email
     * @throws AddressException
     * @throws MessagingException
     */
    public static void sendEmail(String toEmail) throws AddressException, MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.user", FROM);
        props.put("mail.password", PWD);
        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PWD);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM));
        message.setRecipient(Message.RecipientType.TO,
                new InternetAddress(toEmail));
        message.setSubject(TITLE);

        // message.setText(mailContent);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        String dateString = formatter.format(date);
        message.setContent("你已经请求了重置密码"
                        + "<br/><span style = 'font-size:12px;color:#C0C0C0'>(为保障您账号的安全性,请您在5分钟内完成重置。)"
                        + "</span><br/>如果你没有请求重置密码,请忽略这份邮件<br/><br/><br/><br/><br/>"
                        + "<br/>*********某某平台<br/>" + dateString,
                "text/html;charset=gbk");
        message.setSentDate(new Date());
        message.saveChanges();

        if (transport == null) {
            transport = session.getTransport(SMTP);
        }
        if (!transport.isConnected()) {
            transport.connect(HOST,PORT,FROM, PWD);
        }
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}