/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.ejb;

import com.diljeet.myProject.utils.PayChannelOptions;
import com.diljeet.myProject.utils.PaymentOptions;
import com.paytm.pg.merchant.PaytmChecksum;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Named;
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
//    private static final String MID = "FSHcRY11942014623020";
//    private static final String MERCHANT_KEY = "V@lXrclthIc8dSLD";
    private static final String WEBSITE_NAME = "WEBSTAGING";
    private static final String CALLBACK_URL = "http://localhost:8080/myProject/callback.xhtml";
    private static final String CURRENCY = "INR";
    private static final String TOKEN_TYPE = "TXN_TOKEN";

    private String orderId;
    private String transactionToken;
    private String iconBaseUrl;
    private List<PaymentOptions> paymentOptions;
    private List<PayChannelOptions> payChannelOptions;
    private String currency;
    private String balance;

    public PaymentGatewayBean() {
        paymentOptions = new ArrayList<>();
        payChannelOptions = new ArrayList<>();
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

    public void initiateTransaction(String orderId, String payableAmount, String username) {
        this.orderId = orderId;
//        logger.log(Level.SEVERE, "Init trans OrderID is {0}", orderId);
        JSONObject paytmParams = new JSONObject();

        JSONObject body = new JSONObject();
        body.put("requestType", REQUEST_TYPE_INITIATE_TRANSACTION);
        body.put("mid", MID);
        body.put("websiteName", WEBSITE_NAME);
        body.put("orderId", orderId);
        body.put("callbackUrl", CALLBACK_URL);

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
            logger.log(Level.SEVERE, "Checksum in IT {0}", checksum);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JSONObject head = new JSONObject();
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
                System.out.append("Response: " + responseData);
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

    public Response validateOtpAndFetchBalanceInfo(String otp) {
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
    public Response fetchBalanceInfo() {
        JSONObject paytmParams = new JSONObject();

        JSONObject body = new JSONObject();
        body.put("paymentMode", "BALANCE");

        JSONObject head = new JSONObject();
        head.put("txnToken", transactionToken);

        paytmParams.put("body", body);
        paytmParams.put("head", head);

        String post_data = paytmParams.toString();

        /* for Staging */
        URL url = null;
        try {
            url = new URL("https://securegw-stage.paytm.in/userAsset/fetchBalanceInfo?mid=" + MID + "&orderId=" + orderId);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        /* for Production */
// URL url = new URL("https://securegw.paytm.in/userAsset/fetchBalanceInfo?mid=YOUR_MID_HERE&orderId=ORDERID_98765");
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
//                logger.log(Level.SEVERE, "Fetch Balance API Response is {0}", responseData);
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
                            JSONObject balanceInfoObj = ((JSONObject) resObjValue).getJSONObject("balanceInfo");
                            currency = balanceInfoObj.getString("currency");
                            balance = balanceInfoObj.getString("value");
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
//        body.put("walletType", "PAYTMPG");
        if (paymentMode.equals("CREDIT_CARD")) {
            body.put("cardInfo", "|4854980801319205|123|032022");
            body.put("authMode", "otp");
        }

//        logger.log(Level.SEVERE, "transactionToken2 is {0}", transactionToken);
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
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

}
