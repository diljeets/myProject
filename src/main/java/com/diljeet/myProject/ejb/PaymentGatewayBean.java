/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.ejb;

import com.diljeet.myProject.utils.CardBinDetails;
import com.diljeet.myProject.utils.PayChannelOptions;
import com.diljeet.myProject.utils.PaymentOptions;
import com.diljeet.myProject.utils.TransactionDetails;
import com.paytm.pg.merchant.PaytmChecksum;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author diljeet
 */
@Named
@Stateless
public class PaymentGatewayBean {

    private static final Logger logger = Logger.getLogger(PaymentGatewayBean.class.getCanonicalName());

    private static final String REQUEST_TYPE_INITIATE_TRANSACTION = "Payment";
    private static final String REQUEST_TYPE_PROCESS_TRANSACTION = "NATIVE";
    private static final String MID = "FyrHkG61747292942551";
    private static final String MERCHANT_KEY = "#vuq3t2aGydWCBW_";
    private static final String WEBSITE_NAME = "WEBSTAGING";
//    private static final String CALLBACK_URL = "/callback.xhtml";
    private static final String CURRENCY = "INR";
    private static final String TOKEN_TYPE = "TXN_TOKEN";

    private String orderId;
    private String payableAmount;
    private String username;
    private String channelId;
    private String callbackUrl;
    private String transactionToken;
    private String iconBaseUrl;
    private List<PaymentOptions> paymentOptions;
    private List<PayChannelOptions> payChannelOptions;
    private String currency;
    private String balance;
    private List<TransactionDetails> transactionDetails;
    private List<CardBinDetails> cardBinDetails;

//    @Inject
//    HttpServletResponse res;
    public PaymentGatewayBean() {
        paymentOptions = new ArrayList<>();
        payChannelOptions = new ArrayList<>();
        transactionDetails = new ArrayList<>();
        cardBinDetails = new ArrayList<>();
    }

    public List<PaymentOptions> getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(List<PaymentOptions> paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public List<PayChannelOptions> getPayChannelOptions() {
        return payChannelOptions;
    }

    public void setPayChannelOptions(List<PayChannelOptions> payChannelOptions) {
        this.payChannelOptions = payChannelOptions;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<TransactionDetails> getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(List<TransactionDetails> transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public List<CardBinDetails> getCardBinDetails() {
        return cardBinDetails;
    }

    public void setCardBinDetails(List<CardBinDetails> cardBinDetails) {
        this.cardBinDetails = cardBinDetails;
    }

    public void initiateTransaction(String orderId, String payableAmount, String username, String channelId, String callbackUrl) {
        this.orderId = orderId;
        this.payableAmount = payableAmount;
        this.username = username;
        this.channelId = channelId;
        this.callbackUrl = callbackUrl;
//        logger.log(Level.SEVERE, "ChannelId is {0}", channelId);
        JSONObject paytmParams = new JSONObject();

        JSONObject body = new JSONObject();
        body.put("requestType", REQUEST_TYPE_INITIATE_TRANSACTION);
        body.put("mid", MID);
        body.put("websiteName", WEBSITE_NAME);
        body.put("orderId", orderId);
        body.put("callbackUrl", callbackUrl);

        JSONObject txnAmount = new JSONObject();
        txnAmount.put("value", payableAmount);
        txnAmount.put("currency", CURRENCY);

        JSONObject userInfo = new JSONObject();
        userInfo.put("custId", username);

        body.put("txnAmount", txnAmount);
        body.put("userInfo", userInfo);

        /*
* Generate checksum by parameters we have in body
* You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
* Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
         */
        String checksum = null;
        try {
            checksum = PaytmChecksum.generateSignature(body.toString(), MERCHANT_KEY);
//            logger.log(Level.SEVERE, "Checksum in IT {0}", checksum);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JSONObject head = new JSONObject();
        head.put("channelId", channelId);
        head.put("signature", checksum);

        paytmParams.put("body", body);
        paytmParams.put("head", head);

        String post_data = paytmParams.toString();

        /* for Staging */
        URL url = null;
        try {
            url = new URL("https://securegw-stage.paytm.in/theia/api/v1/initiateTransaction?mid=" + MID + "&orderId=" + orderId);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        /* for Production */
// URL url = new URL("https://securegw.paytm.in/theia/api/v1/initiateTransaction?mid=YOUR_MID_HERE&orderId=ORDERID_98765");
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
//                System.out.append("Response: " + responseData);
                JSONObject resObj = new JSONObject(responseData);
                for (String resObjKey : resObj.keySet()) {
                    Object resObjValue = resObj.get(resObjKey);
                    if (resObjKey.equals("body") && resObjValue instanceof JSONObject) {
//                        System.out.println("key: " + keyStr + " value: " + keyvalue);  
                        for (String bodyObjKey : ((JSONObject) resObjValue).keySet()) {
                            Object bodyObjValue = ((JSONObject) resObjValue).get(bodyObjKey);
//                                System.out.println("bodyObjKey: " + bodyObjKey + " bodyObjValue: " + bodyObjValue);
                            if (bodyObjKey.equals("txnToken")) {
                                transactionToken = (String) bodyObjValue;
//                                logger.log(Level.SEVERE, "transactionToken1 is {0}", transactionToken);                                
                            }
                            if (paymentOptions.isEmpty()) {
                                if (bodyObjKey.equals("resultInfo") && bodyObjValue instanceof JSONObject) {
                                    for (String resultInfoObjKey : ((JSONObject) bodyObjValue).keySet()) {
                                        Object resultInfoObjValue = ((JSONObject) bodyObjValue).get(resultInfoObjKey);
//                                            System.out.println("resultInfoObjKey: " + resultInfoObjKey + " resultInfoObjValue: " + resultInfoObjValue);
                                        if (resultInfoObjKey.equals("resultCode") && resultInfoObjValue.equals("0000")) {
//                                        System.out.println("Call Payment Options API");
                                            fetchPaymentOptions(orderId);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void fetchPaymentOptions(String orderId) {
//        paymentOptions.clear();
//        payChannelOptions.clear();
        boolean isResponseSuccess = false;
        JSONObject paytmParams = new JSONObject();

        JSONObject body = new JSONObject();
        body.put("mid", MID);
        JSONObject head = new JSONObject();
        head.put("tokenType", TOKEN_TYPE);
        head.put("token", transactionToken);
//        head.put("txnToken", transactionToken);

        paytmParams.put("body", body);
        paytmParams.put("head", head);

        String post_data = paytmParams.toString();

        /* for Staging */
        URL url = null;
        try {
            url = new URL("https://securegw-stage.paytm.in/theia/api/v2/fetchPaymentOptions?mid=" + MID + "&orderId=" + orderId);
//            url = new URL("https://securegw-stage.paytm.in/fetchPaymentOptions?mid=" + MID + "&orderId=" + orderId);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        /* for Production */
// URL url = new URL("https://securegw.paytm.in/theia/api/v2/fetchPaymentOptions?mid=YOUR_MID_HERE&orderId=ORDERID_98765");
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                logger.log(Level.SEVERE, "Payments Options Response {0}", responseData);
//                boolean isUserLoggedIn = false;
                JSONObject resObj = new JSONObject(responseData);
                for (String resObjKey : resObj.keySet()) {
                    Object resObjValue = resObj.get(resObjKey);
                    if (resObjKey.equals("body") && resObjValue instanceof JSONObject) {
//                        System.out.println("resObjKey: " + resObjKey + " resObjValue: " + resObjValue);
                        for (String bodyObjKey : ((JSONObject) resObjValue).keySet()) {
                            Object bodyObjValue = ((JSONObject) resObjValue).get(bodyObjKey);
//                            System.out.println("bodyObjKey: " + bodyObjKey + " bodyObjValue: " + bodyObjValue);                            
//                            if (bodyObjKey.equals("loginInfo") && bodyObjValue instanceof JSONObject) {                                
//                                isUserLoggedIn = ((JSONObject) bodyObjValue).getBoolean("userLoggedIn");
//                                logger.log(Level.SEVERE, "is user logged in {0}", Boolean.toString(isUserLoggedIn));
//                            }                            
                            if (bodyObjKey.equals("iconBaseUrl")) {
                                iconBaseUrl = (String) bodyObjValue;
//                                System.out.println("iconBaseUrl: " + iconBaseUrl);
                            }
                            if (bodyObjKey.equals("resultInfo") && bodyObjValue instanceof JSONObject) {
                                for (String resultInfoObjKey : ((JSONObject) bodyObjValue).keySet()) {
                                    Object resultInfoObjValue = ((JSONObject) bodyObjValue).get(resultInfoObjKey);
                                    if (resultInfoObjKey.equals("resultCode") && resultInfoObjValue.equals("0000")) {
                                        isResponseSuccess = true;
                                    }
                                }
                            }
                            if (isResponseSuccess && bodyObjKey.equals("merchantPayOption") && bodyObjValue instanceof JSONObject) {
                                for (String merchantPayOptionObjKey : ((JSONObject) bodyObjValue).keySet()) {
                                    Object merchantPayOptionObjValue = ((JSONObject) bodyObjValue).get(merchantPayOptionObjKey);
//                                    System.out.println("merchantPayOptionObjKey: " + merchantPayOptionObjKey + " merchantPayOptionObjValue: " + merchantPayOptionObjValue);
                                    if (merchantPayOptionObjKey.equals("paymentModes") && merchantPayOptionObjValue instanceof JSONArray) {
                                        JSONArray jsonArray = (JSONArray) merchantPayOptionObjValue;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject obj = jsonArray.getJSONObject(i);
//                                            logger.log(Level.SEVERE, "Object is {0}", obj.toString());
                                            String paymentMode = obj.getString("paymentMode");
                                            String displayName = obj.getString("displayName");
                                            boolean isHybridDisabled = obj.getBoolean("isHybridDisabled");
                                            boolean onboarding = obj.getBoolean("onboarding");
                                            String priority = obj.getString("priority");

                                            paymentOptions.add(new PaymentOptions(paymentMode, displayName, isHybridDisabled, onboarding, priority));

//                                            if ((obj.getString("paymentMode")).equals("BALANCE")) {
//                                                logger.log(Level.SEVERE, "I'm here for fetch");
//                                                fetchBalanceInfo();
//                                            }
//                                            if ((obj.getString("paymentMode")).equals("BALANCE") && (obj.get("payChannelOptions") instanceof JSONArray)) {
//                                                JSONArray payChannelOptionsJsonArray = (JSONArray) obj.get("payChannelOptions");
//                                                for (int j = 0; j < payChannelOptionsJsonArray.length(); j++) {
//                                                    JSONObject jsonObject = payChannelOptionsJsonArray.getJSONObject(j);
////                                                    logger.log(Level.SEVERE, "Object in payChannelOptionsJsonArray is {0}", jsonObject.toString());
//                                                    JSONObject balanceInfoObject = (JSONObject) jsonObject.get("balanceInfo");
//                                                    JSONObject accountBalanceObject = (JSONObject) balanceInfoObject.get("accountBalance");
//                                                    currency = accountBalanceObject.getString("currency");
//                                                    balance = accountBalanceObject.getString("value");
//                                                    logger.log(Level.SEVERE, "currency is {0}", currency);
//                                                    logger.log(Level.SEVERE, "balance is {0}", balance);
//
////                                                    logger.log(Level.SEVERE, "PayChannelOptions List Size is {0}", Integer.toString(payChannelOptions.size()));
//                                                }
//                                            }
                                            if ((obj.getString("paymentMode")).equals("NET_BANKING") && (obj.get("payChannelOptions") instanceof JSONArray)) {
                                                JSONArray jsonArray1 = (JSONArray) obj.get("payChannelOptions");
                                                for (int j = 0; j < jsonArray1.length(); j++) {
                                                    JSONObject obj1 = jsonArray1.getJSONObject(j);
//                                                    logger.log(Level.SEVERE, "Object1 is {0}", obj1.toString());
                                                    boolean isChannelOptionHybridDisabled = obj1.getBoolean("isHybridDisabled");
                                                    String channelName = obj1.getString("channelName");
                                                    String iconUrl = obj1.getString("iconUrl");
                                                    String channelCode = obj1.getString("channelCode");

                                                    payChannelOptions.add(new PayChannelOptions(isChannelOptionHybridDisabled, channelName, iconUrl, channelCode));
//                                                    logger.log(Level.SEVERE, "PayChannelOptions List Size is {0}", Integer.toString(payChannelOptions.size()));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public Response sendOTP(String paytmMobile) {
//        logger.log(Level.SEVERE, "send OTP OrderID is {0}", orderId);
        JSONObject paytmParams = new JSONObject();

        JSONObject body = new JSONObject();
        body.put("mobileNumber", paytmMobile);

        JSONObject head = new JSONObject();
        head.put("txnToken", transactionToken);

        paytmParams.put("body", body);
        paytmParams.put("head", head);
        String post_data = paytmParams.toString();

        /* for Staging */
        URL url = null;
        try {
            url = new URL("https://securegw-stage.paytm.in/login/sendOtp?mid=" + MID + "&orderId=" + orderId);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        /* for Production */
// URL url = new URL("https://securegw.paytm.in/login/sendOtp?mid=YOUR_MID_HERE&orderId=ORDERID_98765");
//        JSONObject resultInfoObj = null;
//        String resultCode = null;
        String resultMsg = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
//                System.out.append("Send OTP API Response: " + responseData);
                JSONObject resObj = new JSONObject(responseData);
                for (String resObjKey : resObj.keySet()) {
                    Object resObjValue = resObj.get(resObjKey);
                    if (resObjKey.equals("body") && resObjValue instanceof JSONObject) {
                        JSONObject resultInfoObj = ((JSONObject) resObjValue).getJSONObject("resultInfo");
                        String resultCode = resultInfoObj.getString("resultCode");
                        resultMsg = resultInfoObj.getString("resultMsg");
                        if (!resultCode.equals("01")) {
                            return Response.status(Response.Status.NOT_ACCEPTABLE)
                                    .header("resultMsg", resultMsg)
                                    .build();
                        }
                    }
                }
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Response.status(Response.Status.OK)
                .header("resultMsg", resultMsg)
                .build();
    }

    public Response validateOtpAndFetchPaytmBalance(String otp) {
        JSONObject paytmParams = new JSONObject();

        JSONObject body = new JSONObject();
        body.put("otp", otp);

        JSONObject head = new JSONObject();
        head.put("txnToken", transactionToken);

        paytmParams.put("body", body);
        paytmParams.put("head", head);

        String post_data = paytmParams.toString();

        /* for Staging */
        URL url = null;
        try {
            url = new URL("https://securegw-stage.paytm.in/login/validateOtp?mid=" + MID + "&orderId=" + orderId);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PaymentGatewayBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        /* for Production */
// URL url = new URL("https://securegw.paytm.in/login/validateOtp?mid=YOUR_MID_HERE&orderId=ORDERID_98765");
//        boolean isAuthenticated = false;
        String resultMsg = null;
        String resultStatus = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
//                System.out.append("ValidateOTP API Response: " + responseData);
                JSONObject resObj = new JSONObject(responseData);
                for (String resObjKey : resObj.keySet()) {
                    Object resObjValue = resObj.get(resObjKey);
                    if (resObjKey.equals("body") && resObjValue instanceof JSONObject) {
                        JSONObject resultInfoObj = ((JSONObject) resObjValue).getJSONObject("resultInfo");
//                        logger.log(Level.SEVERE, "resultInfoObj in Valid OTP {0}", resultInfoObj.toString());
                        String resultCode = resultInfoObj.getString("resultCode");
                        resultStatus = resultInfoObj.getString("resultStatus");
                        if (!resultCode.equals("01")) {
                            resultMsg = resultInfoObj.getString("resultMsg");
                            return Response.status(Response.Status.NOT_ACCEPTABLE)
                                    .header("resultMsg", resultMsg)
                                    .build();
                        } else {
//                            isAuthenticated = ((JSONObject) resObjValue).getBoolean("authenticated");
//                            fetchPaymentOptions(orderId);
                            fetchPaytmBalance();
//                            return fetchBalanceInfo();

                        }
                    }
                }
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Response.status(Response.Status.OK)
                .header("resultMsg", resultStatus)
                .build();
    }

    public void fetchPaytmBalance() {
//        paymentOptions.clear();
//        payChannelOptions.clear();
        boolean isResponseSuccess = false;
        JSONObject paytmParams = new JSONObject();

        JSONObject body = new JSONObject();
        body.put("mid", MID);

        JSONObject head = new JSONObject();
        head.put("tokenType", TOKEN_TYPE);
        head.put("token", transactionToken);

        paytmParams.put("body", body);
        paytmParams.put("head", head);

        String post_data = paytmParams.toString();

        /* for Staging */
        URL url = null;
        try {
            url = new URL("https://securegw-stage.paytm.in/theia/api/v2/fetchPaymentOptions?mid=" + MID + "&orderId=" + orderId);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        /* for Production */
// URL url = new URL("https://securegw.paytm.in/theia/api/v2/fetchPaymentOptions?mid=YOUR_MID_HERE&orderId=ORDERID_98765");
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                logger.log(Level.SEVERE, "fetchPaytmBalance Response {0}", responseData);
//                boolean isUserLoggedIn = false;
                JSONObject resObj = new JSONObject(responseData);
                for (String resObjKey : resObj.keySet()) {
                    Object resObjValue = resObj.get(resObjKey);
                    if (resObjKey.equals("body") && resObjValue instanceof JSONObject) {
//                        System.out.println("resObjKey: " + resObjKey + " resObjValue: " + resObjValue);
                        for (String bodyObjKey : ((JSONObject) resObjValue).keySet()) {
                            Object bodyObjValue = ((JSONObject) resObjValue).get(bodyObjKey);
//                            System.out.println("bodyObjKey: " + bodyObjKey + " bodyObjValue: " + bodyObjValue);                            
//                            if (bodyObjKey.equals("loginInfo") && bodyObjValue instanceof JSONObject) {                                
//                                isUserLoggedIn = ((JSONObject) bodyObjValue).getBoolean("userLoggedIn");
//                                logger.log(Level.SEVERE, "is user logged in {0}", Boolean.toString(isUserLoggedIn));
//                            }                            
//                            if (bodyObjKey.equals("iconBaseUrl")) {
//                                iconBaseUrl = (String) bodyObjValue;
//                            }
                            if (bodyObjKey.equals("resultInfo") && bodyObjValue instanceof JSONObject) {
                                for (String resultInfoObjKey : ((JSONObject) bodyObjValue).keySet()) {
                                    Object resultInfoObjValue = ((JSONObject) bodyObjValue).get(resultInfoObjKey);
                                    if (resultInfoObjKey.equals("resultCode") && resultInfoObjValue.equals("0000")) {
                                        isResponseSuccess = true;
                                    }
                                }
                            }
                            if (isResponseSuccess && bodyObjKey.equals("merchantPayOption") && bodyObjValue instanceof JSONObject) {
                                for (String merchantPayOptionObjKey : ((JSONObject) bodyObjValue).keySet()) {
                                    Object merchantPayOptionObjValue = ((JSONObject) bodyObjValue).get(merchantPayOptionObjKey);
//                                    System.out.println("merchantPayOptionObjKey: " + merchantPayOptionObjKey + " merchantPayOptionObjValue: " + merchantPayOptionObjValue);
                                    if (merchantPayOptionObjKey.equals("paymentModes") && merchantPayOptionObjValue instanceof JSONArray) {
                                        JSONArray jsonArray = (JSONArray) merchantPayOptionObjValue;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject obj = jsonArray.getJSONObject(i);
//                                            logger.log(Level.SEVERE, "Object is {0}", obj.toString());
//                                            String paymentMode = obj.getString("paymentMode");
//                                            String displayName = obj.getString("displayName");
//                                            boolean isHybridDisabled = obj.getBoolean("isHybridDisabled");
//                                            boolean onboarding = obj.getBoolean("onboarding");
//                                            String priority = obj.getString("priority");
//
//                                            paymentOptions.add(new PaymentOptions(paymentMode, displayName, isHybridDisabled, onboarding, priority));

//                                            if ((obj.getString("paymentMode")).equals("BALANCE")) {
//                                                logger.log(Level.SEVERE, "I'm here for fetch");
//                                                fetchBalanceInfo();
//                                            }
                                            if ((obj.getString("paymentMode")).equals("BALANCE") && (obj.get("payChannelOptions") instanceof JSONArray)) {
                                                JSONArray payChannelOptionsJsonArray = (JSONArray) obj.get("payChannelOptions");
                                                for (int j = 0; j < payChannelOptionsJsonArray.length(); j++) {
                                                    JSONObject jsonObject = payChannelOptionsJsonArray.getJSONObject(j);
//                                                    logger.log(Level.SEVERE, "Object in payChannelOptionsJsonArray is {0}", jsonObject.toString());
                                                    JSONObject balanceInfoObject = (JSONObject) jsonObject.get("balanceInfo");
                                                    JSONObject accountBalanceObject = (JSONObject) balanceInfoObject.get("accountBalance");
                                                    currency = accountBalanceObject.getString("currency");
                                                    balance = accountBalanceObject.getString("value");
                                                    logger.log(Level.SEVERE, "currency is {0}", currency);
                                                    logger.log(Level.SEVERE, "balance is {0}", balance);

//                                                    logger.log(Level.SEVERE, "PayChannelOptions List Size is {0}", Integer.toString(payChannelOptions.size()));
                                                }
                                            }
//                                            if ((obj.getString("paymentMode")).equals("NET_BANKING") && (obj.get("payChannelOptions") instanceof JSONArray)) {
//                                                JSONArray jsonArray1 = (JSONArray) obj.get("payChannelOptions");
//                                                for (int j = 0; j < jsonArray1.length(); j++) {
//                                                    JSONObject obj1 = jsonArray1.getJSONObject(j);
////                                                    logger.log(Level.SEVERE, "Object1 is {0}", obj1.toString());
//                                                    boolean isChannelOptionHybridDisabled = obj1.getBoolean("isHybridDisabled");
//                                                    String channelName = obj1.getString("channelName");
//                                                    String iconUrl = obj1.getString("iconUrl");
//                                                    String channelCode = obj1.getString("channelCode");
//
//                                                    payChannelOptions.add(new PayChannelOptions(isChannelOptionHybridDisabled, channelName, iconUrl, channelCode));
////                                                    logger.log(Level.SEVERE, "PayChannelOptions List Size is {0}", Integer.toString(payChannelOptions.size()));
//                                                }
//                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

//    public Response fetchBalanceInfo() {
//        JSONObject paytmParams = new JSONObject();
//
//        JSONObject body = new JSONObject();
//        body.put("paymentMode", "BALANCE");
//
//        JSONObject head = new JSONObject();
//        head.put("txnToken", transactionToken);
//
//        paytmParams.put("body", body);
//        paytmParams.put("head", head);
//
//        String post_data = paytmParams.toString();
//
//        /* for Staging */
//        URL url = null;
//        try {
//            url = new URL("https://securegw-stage.paytm.in/userAsset/fetchBalanceInfo?mid=" + MID + "&orderId=" + orderId);
//        } catch (MalformedURLException ex) {
//            ex.printStackTrace();
//        }
//
//        /* for Production */
//// URL url = new URL("https://securegw.paytm.in/userAsset/fetchBalanceInfo?mid=YOUR_MID_HERE&orderId=ORDERID_98765");
//        String resultMsg = null;
//        try {
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setDoOutput(true);
//
//            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
//            requestWriter.writeBytes(post_data);
//            requestWriter.close();
//            String responseData = "";
//            InputStream is = connection.getInputStream();
//            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
//            if ((responseData = responseReader.readLine()) != null) {
////                logger.log(Level.SEVERE, "Fetch Balance API Response is {0}", responseData);
//                JSONObject resObj = new JSONObject(responseData);
//                for (String resObjKey : resObj.keySet()) {
//                    Object resObjValue = resObj.get(resObjKey);
//                    if (resObjKey.equals("body") && resObjValue instanceof JSONObject) {
//                        JSONObject resultInfoObj = ((JSONObject) resObjValue).getJSONObject("resultInfo");
//                        String resultCode = resultInfoObj.getString("resultCode");
//                        resultMsg = resultInfoObj.getString("resultMsg");
//                        if (!resultCode.equals("0000")) {
//                            return Response.status(Response.Status.NOT_ACCEPTABLE)
//                                    .header("resultMsg", resultMsg)
//                                    .build();
//                        } else {
//                            JSONObject balanceInfoObj = ((JSONObject) resObjValue).getJSONObject("balanceInfo");
//                            currency = balanceInfoObj.getString("currency");
//                            balance = balanceInfoObj.getString("value");
//                        }
//                    }
//                }
//            }
//            responseReader.close();
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//        return Response.status(Response.Status.OK)
//                .header("resultMsg", resultMsg)
//                .build();
//    }
    public Response fetchBinDetails(String firstSixCardDigits) {
        JSONObject paytmParams = new JSONObject();

        JSONObject body = new JSONObject();
        body.put("bin", firstSixCardDigits);

        JSONObject head = new JSONObject();
        head.put("tokenType", TOKEN_TYPE);
        head.put("token", transactionToken);

        paytmParams.put("body", body);
        paytmParams.put("head", head);

        String post_data = paytmParams.toString();

        /* for Staging */
        URL url = null;
        try {
            url = new URL("https://securegw-stage.paytm.in/fetchBinDetail?mid=" + MID + "&orderId=" + orderId);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        /* for Production */
// URL url = new URL("https://securegw.paytm.in/fetchBinDetail?mid=YOUR_MID_HERE&orderId=ORDERID_98765");
        String resultMsg = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                logger.log(Level.SEVERE, "Fetch Bin Response is {0}", responseData);
                JSONObject resObj = new JSONObject(responseData);
                for (String resObjKey : resObj.keySet()) {
                    Object resObjValue = resObj.get(resObjKey);
                    if (resObjKey.equals("body") && resObjValue instanceof JSONObject) {
                        JSONObject resultInfoObj = ((JSONObject) resObjValue).getJSONObject("resultInfo");
                        String resultCode = resultInfoObj.getString("resultCode");
                        resultMsg = resultInfoObj.getString("resultMsg");
                        if (!resultCode.equals("0000")) {
                            return Response.status(Response.Status.NOT_ACCEPTABLE)
                                    .header("resultMsg", resultMsg)
                                    .build();
                        } else {
                            //Get attributes from binDetail Object
                            JSONObject binDetailObj = ((JSONObject) resObjValue).getJSONObject("binDetail");
                            String issuingBank = binDetailObj.getString("issuingBank");
                            String issuingBankCode = binDetailObj.getString("issuingBankCode");
                            String paymentMode = binDetailObj.getString("paymentMode");
                            String channelName = binDetailObj.getString("channelName");
                            String channelCode = binDetailObj.getString("channelCode");
                            String isCvvRequired = binDetailObj.getString("cvvR");
                            String isExpRequired = binDetailObj.getString("expR");
                            String isActive = binDetailObj.getString("isActive");

                            //Get attributes from hasLowSuccessRate Object
                            JSONObject hasLowSuccessRateObj = ((JSONObject) resObjValue).getJSONObject("hasLowSuccessRate");
                            String hasLowSuccessRateStatus = hasLowSuccessRateObj.getString("status");
                            String hasLowSuccessRateMsg = hasLowSuccessRateObj.getString("msg");

                            //Get iconUrl Object
                            String cardIconUrl = ((JSONObject) resObjValue).getString("iconUrl");

                            cardBinDetails.add(new CardBinDetails(issuingBank, issuingBankCode, paymentMode, channelName, channelCode, isCvvRequired, isExpRequired, isActive, hasLowSuccessRateStatus, hasLowSuccessRateMsg, cardIconUrl));

                        }
                    }
                }
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Response.status(Response.Status.OK)
                .header("resultMsg", resultMsg)
                .build();
    }

    public Response processTransaction(String paymentMode) {
//        logger.log(Level.SEVERE, "In PT methos paymentModei is {0}", paymentMode);
        JSONObject paytmParams = new JSONObject();

        JSONObject body = new JSONObject();
        body.put("requestType", REQUEST_TYPE_PROCESS_TRANSACTION);
        body.put("mid", MID);
        body.put("orderId", orderId);
        body.put("paymentMode", paymentMode);
//        body.put("authMode", "otp");
//        body.put("walletType", "PAYTMPG");
        if (paymentMode.equals("CREDIT_CARD")) {
//            body.put("cardInfo", "|4854980801319205|123|032022");
            body.put("cardInfo", "|4111111111111111|111|032032");
            body.put("authMode", "otp");
        }

        JSONObject head = new JSONObject();
        head.put("txnToken", transactionToken);

        paytmParams.put("body", body);
        paytmParams.put("head", head);

        String post_data = paytmParams.toString();

        /* for Staging */
        URL url = null;
        try {
            url = new URL("https://securegw-stage.paytm.in/theia/api/v1/processTransaction?mid=" + MID + "&orderId=" + orderId);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        /* for Production */
// URL url = new URL("https://securegw.paytm.in/theia/api/v1/processTransaction?mid=YOUR_MID_HERE&orderId=ORDERID_98765");
        String resultMsg = null;
        String callBackUrl = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                logger.log(Level.SEVERE, "PT ResponseData {0}", responseData);
                JSONObject resObj = new JSONObject(responseData);
                for (String resObjKey : resObj.keySet()) {
                    Object resObjValue = resObj.get(resObjKey);
                    if (resObjKey.equals("body") && resObjValue instanceof JSONObject) {
                        JSONObject resultInfoObj = ((JSONObject) resObjValue).getJSONObject("resultInfo");
                        String resultCode = resultInfoObj.getString("resultCode");
//                        resultMsg = resultInfoObj.getString("resultMsg");
                        if (!resultCode.equals("0000")) {
//                            return Response.status(Response.Status.NOT_ACCEPTABLE)
//                                    .header("resultMsg", resultMsg)
//                                    .build();
                        } else {
//                            JSONObject bodyObjValue = (JSONObject) resObjValue;
//                            verifyChecksumHash(bodyObjValue);

                            //Get callbackURL from response
                            callBackUrl = ((JSONObject) resObjValue).getString("callBackUrl");
                            logger.log(Level.SEVERE, "Callback URL is {0}", callBackUrl);

                            //Get txnInfo Object from response
                            JSONObject txnInfoObj = ((JSONObject) resObjValue).getJSONObject("txnInfo");
                            String paytmChecksum = txnInfoObj.getString("CHECKSUMHASH");
                            String BANKNAME = txnInfoObj.getString("BANKNAME");
                            String BANKTXNID = txnInfoObj.getString("BANKTXNID");
                            String _CURRENCY = txnInfoObj.getString("CURRENCY");
                            String GATEWAYNAME = txnInfoObj.getString("GATEWAYNAME");
                            String _MID = txnInfoObj.getString("MID");
                            String ORDERID = txnInfoObj.getString("ORDERID");
                            String PAYMENTMODE = txnInfoObj.getString("PAYMENTMODE");
                            String RESPCODE = txnInfoObj.getString("RESPCODE");
                            String RESPMSG = txnInfoObj.getString("RESPMSG");
                            String STATUS = txnInfoObj.getString("STATUS");
                            String TXNAMOUNT = txnInfoObj.getString("TXNAMOUNT");
                            String TXNDATE = txnInfoObj.getString("TXNDATE");
                            String TXNID = txnInfoObj.getString("TXNID");

                            transactionDetails.add(new TransactionDetails(BANKNAME, BANKTXNID, _CURRENCY, GATEWAYNAME, ORDERID, PAYMENTMODE, RESPCODE, RESPMSG, TXNAMOUNT, TXNDATE, TXNID));

                            return Response.temporaryRedirect(URI.create(callBackUrl+"?respCode="+RESPCODE)).build();

                        }
                    }
                }
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public void verifyChecksumHash(JSONObject bodyObjValue) {
        JSONObject resultInfoObj = bodyObjValue.getJSONObject("resultInfo");
        String callBackUrl = bodyObjValue.getString("callBackUrl");
        JSONObject riskContentObj = bodyObjValue.getJSONObject("riskContent");
//        logger.log(Level.SEVERE, "callBackUrl is {0}", callBackUrl);
        JSONObject txnInfoObj = bodyObjValue.getJSONObject("txnInfo");
        String paytmChecksum = txnInfoObj.getString("CHECKSUMHASH");
        String BANKNAME = txnInfoObj.getString("BANKNAME");
        String BANKTXNID = txnInfoObj.getString("BANKTXNID");
        String _CURRENCY = txnInfoObj.getString("CURRENCY");
        String GATEWAYNAME = txnInfoObj.getString("GATEWAYNAME");
        String _MID = txnInfoObj.getString("MID");
        String ORDERID = txnInfoObj.getString("ORDERID");
        String PAYMENTMODE = txnInfoObj.getString("PAYMENTMODE");
        String RESPCODE = txnInfoObj.getString("RESPCODE");
        String RESPMSG = txnInfoObj.getString("RESPMSG");
        String STATUS = txnInfoObj.getString("STATUS");
        String TXNAMOUNT = txnInfoObj.getString("TXNAMOUNT");
        String TXNDATE = txnInfoObj.getString("TXNDATE");
        String TXNID = txnInfoObj.getString("TXNID");

        JSONObject body = new JSONObject();

        JSONObject resultInfo = new JSONObject();
        resultInfo.put("resultStatus", resultInfoObj.getString("resultStatus"));
        resultInfo.put("resultCode", resultInfoObj.getString("resultCode"));
        resultInfo.put("resultMsg", resultInfoObj.getString("resultMsg"));
        resultInfo.put("retry", resultInfoObj.getBoolean("retry"));

        JSONObject txnInfo = new JSONObject();
        txnInfo.put("BANKNAME", BANKNAME);
        txnInfo.put("BANKTXNID", BANKTXNID);
//        txnInfo.put("CHECKSUMHASH", paytmChecksum);
        txnInfo.put("CURRENCY", _CURRENCY);
        txnInfo.put("GATEWAYNAME", GATEWAYNAME);
        txnInfo.put("MID", _MID);
        txnInfo.put("ORDERID", ORDERID);
        txnInfo.put("PAYMENTMODE", PAYMENTMODE);
        txnInfo.put("RESPCODE", RESPCODE);
        txnInfo.put("RESPMSG", RESPMSG);
        txnInfo.put("STATUS", STATUS);
        txnInfo.put("TXNAMOUNT", TXNAMOUNT);
        txnInfo.put("TXNDATE", TXNDATE);
        txnInfo.put("TXNID", TXNID);

        JSONObject riskContent = new JSONObject();
        riskContent.put("eventLinkId", riskContentObj.getString("eventLinkId"));

//        body.put("resultInfo", resultInfo);
//        body.put("txnInfo", txnInfo);
//        body.put("callBackUrl", callBackUrl);
//        body.put("riskContent", riskContent);
        Boolean isVerifySignature = null;
        try {
            isVerifySignature = PaytmChecksum.verifySignature(body.toString(), MERCHANT_KEY, paytmChecksum);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (isVerifySignature) {
            logger.log(Level.SEVERE, "Checksum Matched");
        } else {
            logger.log(Level.SEVERE, "Checksum MisMatched");
        }
    }

    public void transactionStatus(String paytmChecksum) {
        /* initialize an object */
        JSONObject paytmParams = new JSONObject();

        /* body parameters */
        JSONObject body = new JSONObject();

        /* Find your MID in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys */
        body.put("mid", MID);

        /* Enter your order id which needs to be check status for */
        body.put("orderId", orderId);

        /**
         * Generate checksum by parameters we have in body You can get Checksum
         * JAR from https://developer.paytm.com/docs/checksum/ Find your
         * Merchant Key in your Paytm Dashboard at
         * https://dashboard.paytm.com/next/apikeys
         */
        String checksum = null;
        try {
            checksum = PaytmChecksum.generateSignature(body.toString(), MERCHANT_KEY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        /* head parameters */
        JSONObject head = new JSONObject();

        /* put generated checksum value here */
        head.put("signature", checksum);

        /* prepare JSON string for request */
        paytmParams.put("body", body);
        paytmParams.put("head", head);
        String post_data = paytmParams.toString();

        /* for Staging */
        URL url = null;
        try {
            url = new URL("https://securegw-stage.paytm.in/v3/order/status");
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        /* for Production */
// URL url = new URL("https://securegw.paytm.in/v3/order/status");
        String signature = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                logger.log(Level.SEVERE, "Transaction Status Response is {0}", responseData);
                JSONObject resObj = new JSONObject(responseData);
//                JSONObject headObj = resObj.getJSONObject("head");
//                signature = headObj.getString("signature");
                verifyChecksumHash2(resObj, paytmChecksum);

            }
            // System.out.append("Request: " + post_data);
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
//        return signature;
    }

    public void verifyChecksumHash2(JSONObject resObj, String paytmChecksum) {
        JSONObject bodyObj = resObj.getJSONObject("body");
        JSONObject resultInfoObj = bodyObj.getJSONObject("resultInfo");

        JSONObject body = new JSONObject();

        JSONObject txnInfo = new JSONObject();
        txnInfo.put("txnId", bodyObj.getString("txnId"));
        txnInfo.put("bankTxnId", bodyObj.getString("bankTxnId"));
        txnInfo.put("orderId", bodyObj.getString("orderId"));
        txnInfo.put("txnAmount", bodyObj.getString("txnAmount"));
        txnInfo.put("txnType", bodyObj.getString("txnType"));
        txnInfo.put("gatewayName", bodyObj.getString("gatewayName"));
        txnInfo.put("bankName", bodyObj.getString("bankName"));
        txnInfo.put("mid", bodyObj.getString("mid"));
        txnInfo.put("paymentMode", bodyObj.getString("paymentMode"));
        txnInfo.put("refundAmt", bodyObj.getString("refundAmt"));
        txnInfo.put("txnDate", bodyObj.getString("txnDate"));

        JSONObject resultInfo = new JSONObject();
        resultInfo.put("resultStatus", resultInfoObj.getString("resultStatus"));
        resultInfo.put("resultCode", resultInfoObj.getString("resultCode"));
        resultInfo.put("resultMsg", resultInfoObj.getString("resultMsg"));

        body.put("txnInfo", txnInfo);
//        body.put("resultInfo", resultInfo);

        Boolean isVerifySignature = null;
        try {
            isVerifySignature = PaytmChecksum.verifySignature(body.toString(), MERCHANT_KEY, paytmChecksum);
        } catch (Exception ex) {
            Logger.getLogger(PaymentGatewayBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (isVerifySignature) {
            logger.log(Level.SEVERE, "Checksum Matched");
        } else {
            logger.log(Level.SEVERE, "Checksum MisMatched");
        }
    }
}
