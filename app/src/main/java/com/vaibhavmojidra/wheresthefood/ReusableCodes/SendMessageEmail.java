package com.vaibhavmojidra.wheresthefood.ReusableCodes;

import android.content.Context;
import android.os.AsyncTask;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMessageEmail extends AsyncTask<Void,Void,Void> {
    private String senderEmail;
    private String messagebody;
    private String subject;
    public SendMessageEmail(String email, String subject, String messagebody){
        this.senderEmail = email;
        this.subject=subject;
        this.messagebody=messagebody;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            Session sess = Session.getDefaultInstance(props,new javax.mail.Authenticator(){protected PasswordAuthentication getPasswordAuthentication() { return new PasswordAuthentication("NotifyMeMobileApp@gmail.com","VaibhavP2311");}});

            MimeMessage message = new MimeMessage(sess);
            message.setFrom(new InternetAddress("NotifyMeMobileApp@gmail.com"));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(senderEmail));
            message.setSubject(subject);
            message.setText(messagebody);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
