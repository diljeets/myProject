/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.utils;

import com.diljeet.myProject.entities.Cart;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author diljeet
 */
public class MyProjectUtils {

    private static final Logger logger = Logger.getLogger(MyProjectUtils.class.getCanonicalName());

//    @Resource(lookup = "java:jboss/mail/gmailSession")
//    public Session mailSession;
    public MyProjectUtils() {
    }

    public static void sendActivationLinkOnMail(Session mailSession, String username) {
        try {

            Encoder encoder = Base64.getEncoder();
            String encodedEmail = encoder.encodeToString(username.getBytes());

            MimeMessage m = new MimeMessage(mailSession);
            Address from = new InternetAddress("rayatdiljeet1983@gmail.com");
            Address[] to = new InternetAddress[]{new InternetAddress(username)};

            m.setFrom(from);
            m.setRecipients(Message.RecipientType.TO, to);
            m.setSubject("DO NOT REPLY: Account Activation");
            m.setSentDate(new java.util.Date());
//            m.setContent("Thank you for registering with us.<br/>"
//                    + "Kindly click on the below link to activate your account.<br/>"
//                    + "<a href='http://localhost:8080/test/webapi/TestUsers?username="+username+"'>http://localhost:8080/test/webapi/TestUsers?username="+username+"</a>", "text/html");
            m.setContent("Thank you for registering with us.<br/>"
                    + "Kindly click on the below link to activate your account.<br/>"
                    + "<a href='http://localhost:8080/myProject/webapi/RegisteredUsers/activate-account/" + encodedEmail + "'>http://localhost:8080/myProject/webapi/RegisteredUsers/activate-account/" + encodedEmail + "</a>", "text/html");
            Transport.send(m);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public static void sendForgotPasswordOnMail(Session mailSession, String username, String password) {
        try {
            MimeMessage m = new MimeMessage(mailSession);
            Address from = new InternetAddress("rayatdiljeet1983@gmail.com");
            Address[] to = new InternetAddress[]{new InternetAddress(username)};

            m.setFrom(from);
            m.setRecipients(Message.RecipientType.TO, to);
            m.setSubject("DO NOT REPLY: Reset Password");
            m.setSentDate(new java.util.Date());
            m.setContent("Dear User,<br/>"
                    + "Kindly use the below 10 digit password in bold letters to login into your account.<br/>"
                    + "<strong>" + password + "</strong><br/>"
                    + "Post login it is advised you change your password for security reasons.",
                    "text/html");
            Transport.send(m);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public static void sendChangePasswordConsentLinkOnMail(Session mailSession, String username, byte[] password) {
        try {

            Encoder encoder = Base64.getEncoder();
            String encodedEmail = encoder.encodeToString(username.getBytes());
            String encodedPassword = encoder.encodeToString(password);

            if (encodedPassword.contains("/")) {
                encodedPassword = manipulateEncodedPassword(encodedPassword, "/", "_");
            }

            MimeMessage m = new MimeMessage(mailSession);
            Address from = new InternetAddress("rayatdiljeet1983@gmail.com");
            Address[] to = new InternetAddress[]{new InternetAddress(username)};

            m.setFrom(from);
            m.setRecipients(Message.RecipientType.TO, to);
            m.setSubject("DO NOT REPLY: Consent to Change Password");
            m.setSentDate(new java.util.Date());
            m.setContent("Dear Customer;<br/>"
                    + "We have received a Password change Request for your Account.<br/>"
                    + "Kindly click on the below link to proceed with the request.<br/>"
                    + "<a href='http://localhost:8080/myProject/webapi/RegisteredUsers/change-password/" + encodedEmail + "/" + encodedPassword + "'>http://localhost:8080/myProject/webapi/RegisteredUsers/change-password/" + encodedEmail + "/" + encodedPassword + "</a>", "text/html");
            Transport.send(m);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public static String generateForgotPassword() {
        ArrayList<String> passwordList = new ArrayList<>();
        ArrayList<String> replaceByte = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            replaceByte.add(Integer.toString(random.nextInt(10)));
        }
        byte[] bytes = new byte[10];
        random.nextBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] >= 35 && bytes[i] <= 38)
                    || (bytes[i] >= 63 && bytes[i] <= 90)
                    || (bytes[i] >= 97 && bytes[i] <= 122)) {
                passwordList.add(new String(bytes, i, 1));
//                        logger.log(Level.SEVERE, Byte.toString(bytes[i]));
            } else {
                passwordList.add(replaceByte.get(i));
            }
        }

        StringBuilder password = new StringBuilder();
        Iterator<String> itr = passwordList.iterator();
        while (itr.hasNext()) {
            password.append(itr.next());
        }
//                String utfPassword = new String(bytes, StandardCharsets.UTF_8);
//                logger.log(Level.SEVERE, "UTF Password is {0}", utfPassword);
//                logger.log(Level.SEVERE, "Password is {0}", passwordList.toString());
//                logger.log(Level.SEVERE, "Password is {0}", password);
        return password.toString();
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[64];
        random.nextBytes(salt);
//        String encodedSalt = Util.encodeBase16(salt);
        return salt;
    }

    public static String manipulateEncodedPassword(String encodedPassword, CharSequence toReplace, CharSequence replaceWith) {
        for (int i = 0; i < encodedPassword.length(); i++) {
            encodedPassword = encodedPassword.replace(toReplace, replaceWith);
        }
        return encodedPassword;
    }

    public static Cart calculateTotalMealPlanRate(Cart cartItem) {
        Double totalMealPlanRate = Math.round((cartItem.getMealPlanRate() * cartItem.getMealPlanQuantity()) * 100.0) / 100.0;
//        BigDecimal bd = new BigDecimal(totalMealPlanRate);
//        BigDecimal roundOff = bd.setScale(2, BigDecimal.ROUND_HALF_EVEN);
//        logger.log(Level.SEVERE, "roundOff val is {0}", roundOff.toString());
//        Double roundOffTotalMealPlanRate = roundOff.doubleValue();
//        logger.log(Level.SEVERE, "roundOffTotalMealPlanRate val is {0}", roundOffTotalMealPlanRate.toString());
        cartItem.setTotalMealPlanRate(totalMealPlanRate);
        return cartItem;
    }

    public static Double calculatePayableAmount(ArrayList<Cart> cartItems) {
        Double payableAmount = (double) 0;
        for (Cart item : cartItems) {
            payableAmount = Math.round((payableAmount + item.getTotalMealPlanRate()) * 100.0) / 100.0;
        }
        return payableAmount;
    }

    public static String createOrderId() {
        Random random = new Random();
        int randomInt = random.nextInt();
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMYYYY"));
        return "ORDERID" + date + Integer.toString(randomInt);
    }

}
