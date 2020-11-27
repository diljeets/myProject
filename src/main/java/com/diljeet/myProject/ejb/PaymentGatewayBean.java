/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.ejb;

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
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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

    private static final String REQUEST_TYPE = "Payment";
    private static final String MID = "FyrHkG61747292942551";
    private static final String MERCHANT_KEY = "#vuq3t2aGydWCBW_";
    private static final String WEBSITE_NAME = "WEBSTAGING";
    private static final String CALLBACK_URL = "https://merchant.com/callback";
    private static final String CURRENCY = "INR";
    private static final String TOKEN_TYPE = "TXN_TOKEN";

//    private Client client;
//
//    @PostConstruct
//    public void init() {
//        client = ClientBuilder.newClient();
//    }
//
//    @PreDestroy
//    public void destroy() {
//        client.close();
//    }
    private String transactionToken;
    private String iconBaseUrl;
    private List<PaymentOptions> paymentOptions;

    public PaymentGatewayBean() {
        paymentOptions = new ArrayList<>();
    }

    public void initiateTransaction(String orderId,
            String payableAmount,
            String username) {

        JSONObject paytmParams = new JSONObject();

        JSONObject body = new JSONObject();
        body.put("requestType", REQUEST_TYPE);
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
//                                System.out.println("transactionToken: " + transactionToken);
                            }
                            if (bodyObjKey.equals("resultInfo") && bodyObjValue instanceof JSONObject) {
                                for (String resultInfoObjKey : ((JSONObject) bodyObjValue).keySet()) {
                                    Object resultInfoObjValue = ((JSONObject) bodyObjValue).get(resultInfoObjKey);
//                                            System.out.println("resultInfoObjKey: " + resultInfoObjKey + " resultInfoObjValue: " + resultInfoObjValue);
                                    if (resultInfoObjKey.equals("resultCode") && resultInfoObjValue.equals("0000")) {
                                        System.out.println("Call Payment Options API");
                                        fetchPaymentOptions(orderId);
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
//        try {
//            Response response = client.target("https://securegw-stage.paytm.in/theia/api/v1/initiateTransaction?mid=" + MID + "&orderId=" + orderId)
//                    .request(MediaType.APPLICATION_JSON)
//                    .post(Entity.json(post_data), Response.class);
//            if (response != null) {
//                if (response.hasEntity()) {
//                    JSONObject jsonObject = response.readEntity(JSONObject.class);
//
//                    for (Object keyStr : jsonObject.keySet()) {
//                        Object keyvalue = jsonObject.get(keyStr);
//                        if (keyStr.equals("body")) {
//                            System.out.println("key: " + keyStr + " value: " + keyvalue);                            
//                        }
//                        
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public void fetchPaymentOptions(String orderId) {
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
//                logger.log(Level.SEVERE, "Payments Options Response {0}", responseData);
                JSONObject resObj = new JSONObject(responseData);
                for (String resObjKey : resObj.keySet()) {
                    Object resObjValue = resObj.get(resObjKey);
                    if (resObjKey.equals("body") && resObjValue instanceof JSONObject) {
//                        System.out.println("resObjKey: " + resObjKey + " resObjValue: " + resObjValue);
                        for (String bodyObjKey : ((JSONObject) resObjValue).keySet()) {
                            Object bodyObjValue = ((JSONObject) resObjValue).get(bodyObjKey);
//                            System.out.println("bodyObjKey: " + bodyObjKey + " bodyObjValue: " + bodyObjValue);
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
                                            logger.log(Level.SEVERE, "Object is {0}", obj.toString());
                                            String paymentMode = obj.getString("paymentMode");
                                            String displayName = obj.getString("displayName");
                                            boolean isHybridDisabled = obj.getBoolean("isHybridDisabled");
                                            boolean onboarding = obj.getBoolean("onboarding");
                                            String priority = obj.getString("priority");
                                            paymentOptions.add(new PaymentOptions(paymentMode, displayName, isHybridDisabled, onboarding, priority));
//                                            logger.log(Level.SEVERE, "List Size is {0}", Integer.toString(paymentOptions.size()));
                                            
//                                            for (String objKey : obj.keySet()) {
//                                                Object objValue = obj.get(objKey);
//                                                System.out.println("objKey: " + objKey + " objValue: " + objValue);   
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

}
