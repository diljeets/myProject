/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.ejb;

import com.diljeet.myProject.entities.CustomerTransaction;
import com.diljeet.myProject.utils.CardDetails;
import com.diljeet.myProject.utils.PayChannelOptionsNetBanking;
import com.diljeet.myProject.utils.PayChannelOptionsPaytmBalance;
import com.diljeet.myProject.utils.PaymentOptions;
import com.diljeet.myProject.utils.PaymentRequestDetails;
import com.diljeet.myProject.utils.SavedInstruments;
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
    private static final String WEBSITE_NAME = "WEBSTAGING";
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
    private List<PayChannelOptionsPaytmBalance> payChannelOptionsPaytmBalance;
    private List<SavedInstruments> savedInstruments;
    private List<PayChannelOptionsNetBanking> payChannelOptionsNetBanking;
    private List<CardDetails> cardDetails;
    private CustomerTransaction customerTransaction;

    public PaymentGatewayBean() {
        paymentOptions = new ArrayList<>();
        payChannelOptionsPaytmBalance = new ArrayList<>();
        savedInstruments = new ArrayList<>();
        payChannelOptionsNetBanking = new ArrayList<>();
        cardDetails = new ArrayList<>();
    }

    public List<PaymentOptions> getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(List<PaymentOptions> paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public List<PayChannelOptionsPaytmBalance> getPayChannelOptionsPaytmBalance() {
        return payChannelOptionsPaytmBalance;
    }

    public void setPayChannelOptionsPaytmBalance(List<PayChannelOptionsPaytmBalance> payChannelOptionsPaytmBalance) {
        this.payChannelOptionsPaytmBalance = payChannelOptionsPaytmBalance;
    }

    public List<SavedInstruments> getSavedInstruments() {
        return savedInstruments;
    }

    public void setSavedInstruments(List<SavedInstruments> savedInstruments) {
        this.savedInstruments = savedInstruments;
    }

    public List<PayChannelOptionsNetBanking> getPayChannelOptionsNetBanking() {
        return payChannelOptionsNetBanking;
    }

    public void setPayChannelOptionsNetBanking(List<PayChannelOptionsNetBanking> payChannelOptionsNetBanking) {
        this.payChannelOptionsNetBanking = payChannelOptionsNetBanking;
    }

    public List<CardDetails> getCardDetails() {
        return cardDetails;
    }

    public void setCardDetails(List<CardDetails> cardDetails) {
        this.cardDetails = cardDetails;
    }

    public CustomerTransaction getCustomerTransaction() {
        return customerTransaction;
    }

    public void setCustomerTransaction(CustomerTransaction customerTransaction) {
        this.customerTransaction = customerTransaction;
    }

    public Response initiateTransaction(String orderId, String payableAmount, String username, String channelId, String callbackUrl) {
        this.orderId = orderId;
        this.payableAmount = payableAmount;
        this.username = username;
        this.channelId = channelId;
        this.callbackUrl = callbackUrl;
        logger.log(Level.SEVERE, "orderId in IT is {0}", orderId);
        paymentOptions.clear();
        payChannelOptionsPaytmBalance.clear();
        savedInstruments.clear();
        payChannelOptionsNetBanking.clear();
        cardDetails.clear();
        customerTransaction = null;

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
                JSONObject resObj = new JSONObject(responseData);
                JSONObject bodyObj = resObj.getJSONObject("body");
                //Get Transaction Token from body object and save in variable to be used in successive API calls
                transactionToken = bodyObj.getString("txnToken");
                //Get Result Info object from body Object
                JSONObject resultInfoObj = bodyObj.getJSONObject("resultInfo");
                String resultCode = resultInfoObj.getString("resultCode");
                if (!resultCode.equals("0000")) {
                    String resultMsg = resultInfoObj.getString("resultMsg");
                    return Response
                            .status(Response.Status.NOT_ACCEPTABLE)
                            .header("resultMsg", resultMsg)
                            .build();
                } else {
                    return fetchPaymentOptions(orderId);
                }
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Response fetchPaymentOptions(String orderId) {

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
                JSONObject resObj = new JSONObject(responseData);
                JSONObject bodyObj = resObj.getJSONObject("body");
                iconBaseUrl = bodyObj.getString("iconBaseUrl");
                JSONObject resultInfoObj = bodyObj.getJSONObject("resultInfo");
                String resultCode = resultInfoObj.getString("resultCode");
                if (!resultCode.equals("0000")) {
                    String resultMsg = resultInfoObj.getString("resultMsg");
                    return Response
                            .status(Response.Status.NOT_ACCEPTABLE)
                            .header("resultMsg", resultMsg)
                            .build();
                } else {
                    JSONObject merchantPayOptionObj = bodyObj.getJSONObject("merchantPayOption");
                    JSONArray paymentModesJsonArray = merchantPayOptionObj.getJSONArray("paymentModes");
                    for (int i = 0; i < paymentModesJsonArray.length(); i++) {
                        JSONObject obj = paymentModesJsonArray.getJSONObject(i);
                        String paymentMode = obj.getString("paymentMode");
                        String displayName = obj.getString("displayName");
                        boolean isHybridDisabled = obj.getBoolean("isHybridDisabled");
                        boolean onboarding = obj.getBoolean("onboarding");
                        String priority = obj.getString("priority");

                        paymentOptions.add(new PaymentOptions(
                                paymentMode,
                                displayName,
                                isHybridDisabled,
                                onboarding,
                                priority
                        ));

                        boolean isChannelOptionHybridDisabled;
                        String channelName;
                        String iconUrl;
                        String channelCode;
                        if ((obj.getString("paymentMode")).equals("NET_BANKING") && (obj.get("payChannelOptions") instanceof JSONArray)) {
                            JSONArray payChannelOptionsJsonArray = (JSONArray) obj.get("payChannelOptions");
                            for (int j = 0; j < payChannelOptionsJsonArray.length(); j++) {
                                JSONObject obj1 = payChannelOptionsJsonArray.getJSONObject(j);
                                isChannelOptionHybridDisabled = obj1.getBoolean("isHybridDisabled");
                                channelName = obj1.getString("channelName");
                                iconUrl = iconBaseUrl + obj1.getString("iconUrl");
                                channelCode = obj1.getString("channelCode");

                                payChannelOptionsNetBanking.add(new PayChannelOptionsNetBanking(
                                        isChannelOptionHybridDisabled,
                                        channelName,
                                        iconUrl,
                                        channelCode
                                ));
                            }
                            payChannelOptionsNetBanking.add(new PayChannelOptionsNetBanking(
                                    "Other Banks",
                                    "OTHERS"
                            ));
                        }
                    }
                }
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Response
                .ok()
                .build();
    }

    public Response sendOTP(String paytmMobile) {

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
                logger.log(Level.SEVERE, "Send OTP response is {0}", responseData);
                JSONObject resObj = new JSONObject(responseData);
                JSONObject bodyObj = resObj.getJSONObject("body");
                JSONObject resultInfoObj = bodyObj.getJSONObject("resultInfo");
                String resultCode = resultInfoObj.getString("resultCode");
                resultMsg = resultInfoObj.getString("resultMsg");
                if (!resultCode.equals("01")) {
                    return Response
                            .status(Response.Status.NOT_ACCEPTABLE)
                            .header("resultMsg", resultMsg)
                            .build();
                }

            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Response
                .status(Response.Status.OK)
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
            ex.printStackTrace();
        }

        /* for Production */
// URL url = new URL("https://securegw.paytm.in/login/validateOtp?mid=YOUR_MID_HERE&orderId=ORDERID_98765");
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
                logger.log(Level.SEVERE, "Validate OTP response is {0}", responseData);
                JSONObject resObj = new JSONObject(responseData);
                JSONObject bodyObj = resObj.getJSONObject("body");
                JSONObject resultInfoObj = bodyObj.getJSONObject("resultInfo");
                String resultCode = resultInfoObj.getString("resultCode");
                resultStatus = resultInfoObj.getString("resultStatus");
                if (!resultCode.equals("01")) {
                    resultMsg = resultInfoObj.getString("resultMsg");
                    return Response
                            .status(Response.Status.NOT_ACCEPTABLE)
                            .header("resultMsg", resultMsg)
                            .build();
                } else {
                    return fetchPaytmBalance();
                }
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Response
                .status(Response.Status.OK)
                .build();
    }

    public Response fetchPaytmBalance() {

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
                JSONObject resObj = new JSONObject(responseData);
                JSONObject bodyObj = resObj.getJSONObject("body");
                JSONObject resultInfoObj = bodyObj.getJSONObject("resultInfo");
                String resultCode = resultInfoObj.getString("resultCode");
                if (!resultCode.equals("0000")) {
                    String resultMsg = resultInfoObj.getString("resultMsg");
                    return Response
                            .status(Response.Status.NOT_ACCEPTABLE)
                            .header("resultMsg", resultMsg)
                            .build();
                } else {
                    JSONObject merchantPayOptionObj = bodyObj.getJSONObject("merchantPayOption");
                    //fetch balance in Paytm Wallet
                    JSONArray paymentModesJsonArray = merchantPayOptionObj.getJSONArray("paymentModes");
                    for (int i = 0; i < paymentModesJsonArray.length(); i++) {
                        JSONObject obj = paymentModesJsonArray.getJSONObject(i);
                        if ((obj.getString("paymentMode")).equals("BALANCE") && (obj.get("payChannelOptions") instanceof JSONArray)) {
                            String paymentMode = obj.getString("paymentMode");
                            JSONArray payChannelOptionsJsonArray = (JSONArray) obj.get("payChannelOptions");
                            for (int j = 0; j < payChannelOptionsJsonArray.length(); j++) {
                                JSONObject jsonObject = payChannelOptionsJsonArray.getJSONObject(j);
                                JSONObject balanceInfoObject = (JSONObject) jsonObject.get("balanceInfo");
                                JSONArray subWalletDetailsJsonArray = balanceInfoObject.getJSONArray("subWalletDetails");
                                for (int k = 0; k < subWalletDetailsJsonArray.length(); k++) {
                                    JSONObject objsInSubWalletDetailsJsonArray = subWalletDetailsJsonArray.getJSONObject(k);
                                    String balance = objsInSubWalletDetailsJsonArray.getString("balance");
                                    String displayName = objsInSubWalletDetailsJsonArray.getString("displayName");
                                    String imageUrl = objsInSubWalletDetailsJsonArray.getString("imageUrl");

                                    payChannelOptionsPaytmBalance.add(new PayChannelOptionsPaytmBalance(
                                            paymentMode,
                                            balance,
                                            displayName,
                                            imageUrl
                                    ));
                                }
                            }
                        }
                    }
                    // fetch Saved Cards by user if any
                    JSONArray instruments = merchantPayOptionObj.getJSONArray("savedInstruments");
                    for (Object instrument : instruments) {
                        JSONObject savedCard = (JSONObject) instrument;
                        //Misc Card Info
                        String iconUrl = savedCard.getString("iconUrl");
                        String issuingBank = savedCard.getString("issuingBank");
                        String displayName = savedCard.getString("displayName");
                        String priority = savedCard.getString("priority");
//                        String paymentOfferDetails = (String) savedCard.get("paymentOfferDetails");
//                        String savedCardEmisubventionDetail = (String) savedCard.get("savedCardEmisubventionDetail");
                        String channelCode = savedCard.getString("channelCode");
                        String channelName = savedCard.getString("channelName");
                        boolean oneClickSupported = savedCard.getBoolean("oneClickSupported");
                        boolean isEmiAvailable = savedCard.getBoolean("isEmiAvailable");
                        boolean isHybridDisabled = savedCard.getBoolean("isHybridDisabled");
                        boolean isEmiHybridDisabled = savedCard.getBoolean("isEmiHybridDisabled");
                        //Auth Mode
                        JSONArray authModes = savedCard.getJSONArray("authModes");
                        String authMode = authModes.getString(0);
                        //Card Details
                        JSONObject savedCardDetails = savedCard.getJSONObject("cardDetails");
                        String cardId = savedCardDetails.getString("cardId");
                        String cardType = savedCardDetails.getString("cardType");
                        String expiryDate = savedCardDetails.getString("expiryDate");
                        String firstSixDigit = savedCardDetails.getString("firstSixDigit");
                        String lastFourDigit = savedCardDetails.getString("lastFourDigit");
                        String status = savedCardDetails.getString("status");
                        String cvvLength = savedCardDetails.getString("cvvLength");
                        boolean cvvRequired = savedCardDetails.getBoolean("cvvRequired");
                        boolean indian = savedCardDetails.getBoolean("indian");

                        savedInstruments.add(new SavedInstruments(
                                iconUrl,
                                issuingBank,
                                displayName,
                                priority,
                                //                                paymentOfferDetails,
                                //                                savedCardEmisubventionDetail,
                                channelCode,
                                channelName,
                                oneClickSupported,
                                isEmiAvailable,
                                isHybridDisabled,
                                isEmiHybridDisabled,
                                authMode,
                                cardId,
                                cardType,
                                expiryDate,
                                firstSixDigit,
                                lastFourDigit,
                                status,
                                cvvLength,
                                cvvRequired,
                                indian
                        ));
                    }
                }
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Response
                .status(Response.Status.OK)
                .build();
    }

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
                JSONObject bodyObj = resObj.getJSONObject("body");
                JSONObject resultInfoObj = bodyObj.getJSONObject("resultInfo");
                String resultCode = resultInfoObj.getString("resultCode");
                if (!resultCode.equals("0000")) {
                    String resultMsg = resultInfoObj.getString("resultMsg");
                    return Response
                            .status(Response.Status.NOT_ACCEPTABLE)
                            .header("resultMsg", resultMsg)
                            .build();
                } else {
                    //Get attributes from binDetail Object
                    JSONObject binDetailObj = bodyObj.getJSONObject("binDetail");
                    String issuingBank = binDetailObj.getString("issuingBank");
                    String issuingBankCode = binDetailObj.getString("issuingBankCode");
                    String paymentMode = binDetailObj.getString("paymentMode");
                    String channelName = binDetailObj.getString("channelName");
                    String channelCode = binDetailObj.getString("channelCode");
                    String isCvvRequired = binDetailObj.getString("cvvR");
                    String isExpRequired = binDetailObj.getString("expR");
                    String isActive = binDetailObj.getString("isActive");

                    //Get attributes from hasLowSuccessRate Object
                    JSONObject hasLowSuccessRateObj = bodyObj.getJSONObject("hasLowSuccessRate");
                    String hasLowSuccessRateStatus = hasLowSuccessRateObj.getString("status");
                    String hasLowSuccessRateMsg = hasLowSuccessRateObj.getString("msg");

                    //Get iconUrl Object
                    String cardIconUrl = bodyObj.getString("iconUrl");

                    cardDetails.add(new CardDetails(
                            issuingBank,
                            issuingBankCode,
                            paymentMode,
                            channelName,
                            channelCode,
                            isCvvRequired,
                            isExpRequired,
                            isActive,
                            hasLowSuccessRateStatus,
                            hasLowSuccessRateMsg,
                            cardIconUrl
                    ));

                }
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Response
                .status(Response.Status.OK)
                .build();
    }

    public void fetchOtherNetBankingPaymentChannels() {
        //Clear payChannelOptionsNetBanking List
        payChannelOptionsNetBanking.clear();

        JSONObject paytmParams = new JSONObject();

        JSONObject body = new JSONObject();
        body.put("type", "MERCHANT");

        JSONObject head = new JSONObject();
        head.put("tokenType", TOKEN_TYPE);
        head.put("token", transactionToken);

        paytmParams.put("body", body);
        paytmParams.put("head", head);

        String post_data = paytmParams.toString();

        /* for Staging */
        URL url = null;
        try {
            url = new URL("https://securegw-stage.paytm.in/theia/api/v1/fetchNBPaymentChannels?mid=" + MID + "&orderId=" + orderId);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        /* for Production */
// URL url = new URL("https://securegw.paytm.in/theia/api/v1/fetchNBPaymentChannels?mid=YOUR_MID_HERE&orderId=ORDERID_98765");
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
                logger.log(Level.SEVERE, "fetch NB Payment Channel Response: {0}", responseData);
                JSONObject resObj = new JSONObject(responseData);
                JSONObject bodyObj = resObj.getJSONObject("body");
                JSONObject resultInfoObj = bodyObj.getJSONObject("resultInfo");

                String resultCode = resultInfoObj.getString("resultCode");
                if (resultCode.equals("0000")) {
                    JSONObject nbPayOptionObj = bodyObj.getJSONObject("nbPayOption");
                    JSONArray payChannelOptionsJsonArray = nbPayOptionObj.getJSONArray("payChannelOptions");
                    for (int i = 0; i < payChannelOptionsJsonArray.length(); i++) {
                        JSONObject bankObj = payChannelOptionsJsonArray.getJSONObject(i);
                        boolean isChannelOptionHybridDisabled = bankObj.getBoolean("isHybridDisabled");
                        String channelName = bankObj.getString("channelName");
                        String iconUrl = iconBaseUrl + bankObj.getString("iconUrl");
                        String channelCode = bankObj.getString("channelCode");
                        payChannelOptionsNetBanking.add(new PayChannelOptionsNetBanking(
                                isChannelOptionHybridDisabled,
                                channelName,
                                iconUrl,
                                channelCode
                        ));
                    }
                }
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Response processTransaction(PaymentRequestDetails paymentRequestDetails) {
        String paymentMode = paymentRequestDetails.getPaymentMode();
//        logger.log(Level.SEVERE, "paymentMode in PT is {0}", paymentMode);
        JSONObject paytmParams = new JSONObject();

        JSONObject body = new JSONObject();
        body.put("requestType", REQUEST_TYPE_PROCESS_TRANSACTION);
        body.put("mid", MID);
        body.put("orderId", orderId);
        body.put("paymentMode", paymentMode);
//        body.put("authMode", "otp");
//        body.put("walletType", "PAYTMPG");
        if (paymentMode.equals("CREDIT_CARD") || paymentMode.equals("DEBIT_CARD")) {
            String cardInfo;
            if (paymentRequestDetails.getCardId() != null) {                
                cardInfo = paymentRequestDetails.getCardId() + "||" + paymentRequestDetails.getCvv() + "|";
            } else {
                cardInfo = "|" + paymentRequestDetails.getCardNumber() + "|" + paymentRequestDetails.getCvv() + "|" + paymentRequestDetails.getExpiryDate();
                body.put("storeInstrument", paymentRequestDetails.getSaveCard());
                logger.log(Level.SEVERE, "storeInstrument {0}", paymentRequestDetails.getSaveCard());
            }
            logger.log(Level.SEVERE, "cardInfo is {0}", cardInfo);            
            body.put("cardInfo", cardInfo);
//            body.put("cardInfo", "|4854980801319205|123|032022");
//            body.put("cardInfo", "|4111111111111111|111|032032");
            body.put("authMode", "otp");
        }
        if (paymentMode.equals("NET_BANKING")) {
            body.put("channelCode", paymentRequestDetails.getChannelCode());
        }
        JSONObject head = new JSONObject();
        head.put("txnToken", transactionToken);
        logger.log(Level.SEVERE, "paymentMode in body Object is {0}", body.getString("paymentMode"));
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
//        Response response = null;
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
                JSONObject bodyObj = resObj.getJSONObject("body");
                JSONObject resultInfoObj = bodyObj.getJSONObject("resultInfo");

                String resultCode = resultInfoObj.getString("resultCode");
                if (!resultCode.equals("0000")) {
                    String resultMsg = resultInfoObj.getString("resultMsg");
                    return Response
                            .status(Response.Status.NOT_ACCEPTABLE)
                            .header("resultMsg", resultMsg)
                            .build();
                } else {
                    if (paymentMode.equals("BALANCE")) {
//                        String callBackUrl = bodyObj.getString("callBackUrl");
                        JSONObject txnInfoObj = bodyObj.getJSONObject("txnInfo");
                        getCustomerTransactionStatus(txnInfoObj.toString());
                        return Response
                                .ok()
                                .build();
                    }
                    if (paymentMode.equals("CREDIT_CARD") || paymentMode.equals("DEBIT_CARD") || paymentMode.equals("NET_BANKING")) {
                        return Response
                                .ok()
                                .entity(bodyObj.toString())
                                .build();
                    }
                }
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void getCustomerTransactionStatus(String txnInfo) {
        JSONObject txnInfoObj = new JSONObject(txnInfo);
        String BANKNAME = txnInfoObj.getString("BANKNAME");
        String BANKTXNID = txnInfoObj.getString("BANKTXNID");
        String _CURRENCY = txnInfoObj.getString("CURRENCY");
        String GATEWAYNAME = txnInfoObj.getString("GATEWAYNAME");
//                String MID = txnInfoObj.getString("MID");
        String ORDERID = txnInfoObj.getString("ORDERID");
        String PAYMENTMODE = txnInfoObj.getString("PAYMENTMODE");
        logger.log(Level.SEVERE, "PAYMENTMODE in getCustomerTransactionStatus is {0}", PAYMENTMODE);
        String RESPCODE = txnInfoObj.getString("RESPCODE");
        String RESPMSG = txnInfoObj.getString("RESPMSG");
//                String STATUS = txnInfoObj.getString("STATUS");
        String TXNAMOUNT = txnInfoObj.getString("TXNAMOUNT");
        String TXNDATE = txnInfoObj.getString("TXNDATE");
        String TXNID = txnInfoObj.getString("TXNID");

        if (RESPCODE.equals("01")) {
            Response response = transactionStatus(ORDERID);
            if (response.getStatus() == Response.Status.OK.getStatusCode()
                    && response.hasEntity()) {
                String stringEntity = response.readEntity(String.class);
                JSONObject bodyObj = new JSONObject(stringEntity);
                JSONObject resultInfoObj = bodyObj.getJSONObject("resultInfo");
                String resultCode = resultInfoObj.getString("resultCode");
                if (resultCode.equals("01")) {
                    //Create CustomerTransaction Object in case Transaction is Successful
                    customerTransaction = new CustomerTransaction(
                            BANKNAME,
                            BANKTXNID,
                            _CURRENCY,
                            GATEWAYNAME,
                            ORDERID,
                            PAYMENTMODE,
                            RESPCODE,
                            RESPMSG,
                            TXNAMOUNT,
                            TXNDATE,
                            TXNID
                    );
                    logger.log(Level.SEVERE, "customerTransaction object created for Success {0}", customerTransaction.getOrderId());
                }
            }
        } else {
            //Create CustomerTransaction Object in case Transaction is Failure
            customerTransaction = new CustomerTransaction(
                    BANKNAME,
                    BANKTXNID,
                    _CURRENCY,
                    GATEWAYNAME,
                    ORDERID,
                    PAYMENTMODE,
                    RESPCODE,
                    RESPMSG,
                    TXNAMOUNT,
                    TXNDATE,
                    TXNID
            );
            logger.log(Level.SEVERE, "customerTransaction object created for Failure {0}", customerTransaction.getOrderId());
        }
    }

    public Response transactionStatus(String orderId) {
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
                JSONObject bodyObj = resObj.getJSONObject("body");
                return Response
                        .ok()
                        .entity(bodyObj.toString())
                        .build();
            }

            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

}
