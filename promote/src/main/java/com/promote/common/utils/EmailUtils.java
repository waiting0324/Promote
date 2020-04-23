package com.promote.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Component
public class EmailUtils {
    /**
     * 服務器的SMTP地址
     */
    private static String host;
    /**
     * 服務器的SMTP端口
     */
    private static int port;
    private static String smtp ;
    /**
     * 發信人Email
     */
    private static String from;
    /**
     * 發信人Email密碼
     */
    private static String pwd;
    private static Transport transport;


    @Value("${email.host}")
    public void setHost(String host) {
        EmailUtils.host = host;
    }

    @Value("${email.port}")
    public void setPort(int port) {
        EmailUtils.port = port;
    }

    @Value("${email.smtp}")
    public void setSmtp(String smtp) {
        EmailUtils.smtp = smtp;
    }

    @Value("${email.from}")
    public void setFrom(String from) {
        EmailUtils.from = from;
    }

    @Value("${email.pwd}")
    public void setPwd(String pwd) {
        EmailUtils.pwd = pwd;
    }

    /**
     * 發送Email
     *
     * @param toEmail 收信人Email
     * @throws AddressException
     * @throws MessagingException
     */
    public static void sendEmail(String toEmail, String title, String content) throws AddressException, MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.user", from);
        props.put("mail.password", pwd);
        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pwd);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO,
                new InternetAddress(toEmail));
        message.setSubject(title);

        // message.setText(mailContent);
//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
//        String dateString = formatter.format(date);
        message.setContent(content, "text/html;charset=UTF-8");
        message.setSentDate(new Date());
        message.saveChanges();

        if (transport == null) {
            transport = session.getTransport(smtp);
        }
        if (!transport.isConnected()) {
            transport.connect(host, port, from, pwd);
        }
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}