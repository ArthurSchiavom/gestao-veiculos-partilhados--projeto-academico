package lapr.project.data;

import com.sun.mail.smtp.SMTPTransport;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Class that takes care of sending emails
 */
public class Emailer {
    private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final String USERNAME = "ridesharinglapr3@gmail.com";
    private static final String PASSWORD = "melhorgrupoole";

    private static final String EMAIL_FROM = "ridesharinglapr3@gmail.com";

    /**
     * Sends an email
     * @param recipient the email of the target
     * @param subject title of the email
     * @param text main text of the email
     */
    public static void sendEmail(String recipient, String subject, String text) {

        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", SMTP_SERVER); //optional, defined in SMTPTransport
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "587"); // default port 25
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);

        try {

            // from
            msg.setFrom(new InternetAddress(EMAIL_FROM));

            // to
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient, false));

            // subject
            msg.setSubject(subject);

            // content
            msg.setText(text);

            msg.setSentDate(new Date());

            // Get SMTPTransport
            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

            // connect
            t.connect(SMTP_SERVER, USERNAME, PASSWORD);

            // send
            t.sendMessage(msg, msg.getAllRecipients());

            t.close();

        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }


    }

}