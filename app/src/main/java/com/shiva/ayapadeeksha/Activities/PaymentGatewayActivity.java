package com.shiva.ayapadeeksha.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/*import com.payu.india.CallBackHandler.OnetapCallback;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Extras.PayUSdkDetails;
import com.payu.india.Interfaces.OneClickPaymentListener;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.payuui.Activity.PayUBaseActivity;*/
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppConstants;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

/* public class PaymentGatewayActivity extends AppCompatActivity
       implements OneClickPaymentListener {

    private String strSubscriptionName, strUserId, strVendorId, strValidity, strPrice;
    private ProgressBar progressBar;
    private TextView tvemptyText;
    private TextInputLayout tilSubscriptionName, tilValidity, tilAmount;
    private TextInputEditText tieSubscriptionName, tieValidity, tieAmount;
    private String strUserCredentials;
    private Button btnSubscribe;
    private String strMerchantKey = "gtKFFX";// In Production strMerchantKey = "0MQaQP";
    // These will hold all the payment parameters
    private PaymentParams paymentParams;
    // This sets the configuration
    private PayuConfig payuConfig;
    // Used when generating hash from SDK
    private PayUChecksum checksum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        strSubscriptionName = getIntent().getStringExtra("SUBSCRIPTION_NAME");
        strUserId = getIntent().getStringExtra("USER_ID");
        strVendorId = getIntent().getStringExtra("VENDOR_ID");
        strValidity = getIntent().getStringExtra("DURATION");
        strPrice = getIntent().getStringExtra("PRICE");

        setTitle(strSubscriptionName);

        init();

        OnetapCallback.setOneTapCallback(this);

        //TODO Must write below code in your activity to set up initial context for PayU
        Payu.setInstance(this);

        // lets tell the people what version of sdk we are using
        PayUSdkDetails payUSdkDetails = new PayUSdkDetails();

        tieSubscriptionName.setText(strSubscriptionName);
        tieValidity.setText(strValidity);
        tieAmount.setText(strPrice);
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToBaseActivity(view);
            }
        });
    }

    *//* ================================ Payment Details ================================= *//*

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvemptyText = (TextView) findViewById(R.id.tvemptyText);
        tilSubscriptionName = (TextInputLayout) findViewById(R.id.tilSubscriptionName);
        tilValidity = (TextInputLayout) findViewById(R.id.tilValidity);
        tilAmount = (TextInputLayout) findViewById(R.id.tilAmount);

        tieSubscriptionName = (TextInputEditText) findViewById(R.id.tieSubscriptionName);
        tieValidity = (TextInputEditText) findViewById(R.id.tieValidity);
        tieAmount = (TextInputEditText) findViewById(R.id.tieAmount);

        btnSubscribe = (Button) findViewById(R.id.btnSubscribe);
    }

    @Override
    public HashMap<String, String> getAllOneClickHash(String userCredentials) {
        return null;
    }

    @Override
    public void getOneClickHash(String cardToken, String merchantKey, String userCredentials) {

    }

    @Override
    public void saveOneClickHash(String cardToken, String oneClickHash) {

    }

    @Override
    public void deleteOneClickHash(String cardToken, String userCredentials) {

    }

    public void navigateToBaseActivity(View view) {
        String value = "Test";
        int environment;
        String TEST_ENVIRONMENT = getResources().getString(R.string.test);
        if (value.equals(TEST_ENVIRONMENT))
            environment = PayuConstants.STAGING_ENV;
        else
            environment = PayuConstants.PRODUCTION_ENV;

        strUserCredentials = strMerchantKey + ":" + "xyz@gmail.com";

        //TODO Below are mandatory params for hash genetation
        paymentParams = new PaymentParams();
        *//**
         * For Test Environment, merchantKey = please contact mobile.integration@payu.in with your app name and registered email id

         *//*
        paymentParams.setKey(strMerchantKey);
        paymentParams.setAmount(strPrice);
        paymentParams.setProductInfo(strSubscriptionName);
        paymentParams.setFirstName("");
        paymentParams.setEmail("test@gmail.com");
        paymentParams.setPhone("");

        *//*
         * Transaction Id should be kept unique for each transaction.
         * *//*
        paymentParams.setTxnId("" + System.currentTimeMillis());

        *//**
         * Surl --> Success url is where the transaction response is posted by PayU on successful transaction
         * Furl --> Failre url is where the transaction response is posted by PayU on failed transaction
         *//*
        paymentParams.setSurl(" https://payuresponse.firebaseapp.com/success");
        paymentParams.setFurl("https://payuresponse.firebaseapp.com/failure");
        paymentParams.setNotifyURL(paymentParams.getSurl());  //for lazy pay

        *//*
         * udf1 to udf5 are options params where you can pass additional information related to transaction.
         * If you don't want to use it, then send them as empty string like, udf1=""
         * *//*
        paymentParams.setUdf1(strUserId);
        paymentParams.setUdf2(strValidity);
        paymentParams.setUdf3(strVendorId);
        paymentParams.setUdf4("udf4");
        paymentParams.setUdf5("udf5");

        *//**
         * These are used for store card feature. If you are not using it then user_credentials = "default"
         * user_credentials takes of the form like user_credentials = "merchant_key : user_id"
         * here merchant_key = your merchant key,
         * user_id = unique id related to user like, email, phone number, etc.
         * *//*
        paymentParams.setUserCredentials(strUserCredentials);

        //TODO Pass this param only if using offer key
        //mPaymentParams.setOfferKey("cardnumber@8370");

        //TODO Sets the payment environment in PayuConfig object
        payuConfig = new PayuConfig();
        payuConfig.setEnvironment(environment);
        //   payuConfig.setEnvironment(PayuConstants.MOBILE_STAGING_ENV);
        //TODO It is recommended to generate hash from server only. Keep your key and salt in server side hash generation code.
        //  generateHashFromServer(mPaymentParams);

        *//**
         * Below approach for generating hash is not recommended. However, this approach can be used to test in PRODUCTION_ENV
         * if your server side hash generation code is not completely setup. While going live this approach for hash generation
         * should not be used.
         * *//*
        String salt = "eCwWELxi";
        // String salt = "13p0PXZk";
        generateHashFromSDK(paymentParams, salt);
    }

    *//******************************
     * Client hash generation
     ***********************************//*
    // Do not use this, you may use this only for testing.
    // lets generate hashes.
    // This should be done from server side..
    // Do not keep salt anywhere in app.
    public void generateHashFromSDK(PaymentParams mPaymentParams, String salt) {
        PayuHashes payuHashes = new PayuHashes();
        PostData postData = new PostData();

        // payment Hash;
        checksum = null;
        checksum = new PayUChecksum();
        checksum.setAmount(mPaymentParams.getAmount());
        checksum.setKey(mPaymentParams.getKey());
        checksum.setTxnid(mPaymentParams.getTxnId());
        checksum.setEmail(mPaymentParams.getEmail());
        checksum.setSalt(salt);
        checksum.setProductinfo(mPaymentParams.getProductInfo());
        checksum.setFirstname(mPaymentParams.getFirstName());
        checksum.setUdf1(mPaymentParams.getUdf1());
        checksum.setUdf2(mPaymentParams.getUdf2());
        checksum.setUdf3(mPaymentParams.getUdf3());
        checksum.setUdf4(mPaymentParams.getUdf4());
        checksum.setUdf5(mPaymentParams.getUdf5());

        postData = checksum.getHash();
        if (postData.getCode() == PayuErrors.NO_ERROR) {
            payuHashes.setPaymentHash(postData.getResult());
        }

        // checksum for payemnt related details
        // var1 should be either user credentials or default
        String var1 = mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials();
        String key = mPaymentParams.getKey();

        if ((postData = calculateHash(key, PayuConstants.PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // Assign post data first then check for success
            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(postData.getResult());
        //vas
        if ((postData = calculateHash(key, PayuConstants.VAS_FOR_MOBILE_SDK, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
            payuHashes.setVasForMobileSdkHash(postData.getResult());

        // getIbibocodes
        if ((postData = calculateHash(key, PayuConstants.GET_MERCHANT_IBIBO_CODES, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
            payuHashes.setMerchantIbiboCodesHash(postData.getResult());

        if (!var1.contentEquals(PayuConstants.DEFAULT)) {
            // get user card
            if ((postData = calculateHash(key, PayuConstants.GET_USER_CARDS, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // todo rename storedc ard
                payuHashes.setStoredCardsHash(postData.getResult());
            // save user card
            if ((postData = calculateHash(key, PayuConstants.SAVE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setSaveCardHash(postData.getResult());
            // delete user card
            if ((postData = calculateHash(key, PayuConstants.DELETE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setDeleteCardHash(postData.getResult());
            // edit user card
            if ((postData = calculateHash(key, PayuConstants.EDIT_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setEditCardHash(postData.getResult());
        }

        if (mPaymentParams.getOfferKey() != null) {
            postData = calculateHash(key, PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey(), salt);
            if (postData.getCode() == PayuErrors.NO_ERROR) {
                payuHashes.setCheckOfferStatusHash(postData.getResult());
            }
        }

        if (mPaymentParams.getOfferKey() != null && (postData = calculateHash(key, PayuConstants.CHECK_OFFER_STATUS, mPaymentParams.getOfferKey(), salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) {
            payuHashes.setCheckOfferStatusHash(postData.getResult());
        }

        // we have generated all the hases now lest launch sdk's ui
        launchSdkUI(payuHashes);
    }

    // deprecated, should be used only for testing.
    private PostData calculateHash(String key, String command, String var1, String salt) {
        checksum = null;
        checksum = new PayUChecksum();
        checksum.setKey(key);
        checksum.setCommand(command);
        checksum.setVar1(var1);
        checksum.setSalt(salt);
        return checksum.getHash();
    }

    *//**
     * This method adds the Payuhashes and other required params to intent and launches the PayuBaseActivity.java
     *
     * @param payuHashes it contains all the hashes generated from merchant server
     *//*
    public void launchSdkUI(PayuHashes payuHashes) {

        Intent intent = new Intent(this, PayUBaseActivity.class);
        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
        intent.putExtra(PayuConstants.PAYMENT_PARAMS, paymentParams);
        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);

        //Lets fetch all the one click card tokens first
        fetchMerchantHashes(intent);
    }

    *//**
     * This method fetches merchantHash and cardToken already stored on merchant server.
     *//*
    private void fetchMerchantHashes(final Intent intent) {
        // now make the api call.
        final String postParams = "merchant_key=" + strMerchantKey + "&user_credentials=" + strUserCredentials;
        final Intent baseActivityIntent = intent;
        new AsyncTask<Void, Void, HashMap<String, String>>() {

            @Override
            protected HashMap<String, String> doInBackground(Void... params) {
                try {
                    //TODO Replace below url with your server side file url.
                    URL url = new URL("https://payu.herokuapp.com/get_merchant_hashes");

                    byte[] postParamsByte = postParams.getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postParamsByte);

                    InputStream responseInputStream = conn.getInputStream();
                    StringBuffer responseStringBuffer = new StringBuffer();
                    byte[] byteContainer = new byte[1024];
                    for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                        responseStringBuffer.append(new String(byteContainer, 0, i));
                    }

                    JSONObject response = new JSONObject(responseStringBuffer.toString());

                    HashMap<String, String> cardTokens = new HashMap<String, String>();
                    JSONArray oneClickCardsArray = response.getJSONArray("data");
                    int arrayLength;
                    if ((arrayLength = oneClickCardsArray.length()) >= 1) {
                        for (int i = 0; i < arrayLength; i++) {
                            cardTokens.put(oneClickCardsArray.getJSONArray(i).getString(0), oneClickCardsArray.getJSONArray(i).getString(1));
                        }
                        return cardTokens;
                    }
                    // pass these to next activity

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(HashMap<String, String> oneClickTokens) {
                super.onPostExecute(oneClickTokens);

                baseActivityIntent.putExtra(PayuConstants.ONE_CLICK_CARD_TOKENS, oneClickTokens);
                startActivityForResult(baseActivityIntent, PayuConstants.PAYU_REQUEST_CODE);
            }
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (data != null) {
                *//**
                 * Here, data.getStringExtra("payu_response") ---> Implicit response sent by PayU
                 * data.getStringExtra("result") ---> Response received from merchant's Surl/Furl
                 *
                 * PayU sends the same response to merchant server and in app. In response check the value of key "status"
                 * for identifying status of transaction. There are two possible status like, success or failure
                 * *//*
                *//*Log.d("Response PayU", "Payu's Data : " + data.getStringExtra("payu_response")
                        + "\n\n\n Merchant's Data: "
                        + data.getStringExtra("result"));
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Payu's Data : " + data.getStringExtra("payu_response")
                                + "\n\n\n Merchant's Data: "
                                + data.getStringExtra("result"))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();*//*
                try {
                    JSONObject response = new JSONObject(data.getStringExtra("payu_response"));
                    String strTransactionId = response.getString("txnid");

                    if (response.getString("status").equals("success")) {
                        getPaymentDetails(strUserId, strVendorId, strValidity, strPrice, strTransactionId);
                            *//*saveSubscription(strPackageId, AppConstants.USER_ID, strMobileNumber,
                                    strName, strEmail, strSelectedPackageValidity,
                                    strSelectedPackageAmount, strTransactionId);*//*
                    } else {
                        Toast.makeText(PaymentGatewayActivity.this, "Payment Failed", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, getString(R.string.could_not_receive_data), Toast.LENGTH_LONG).show();
            }
        }
    }

    *//*===================== Subscription Details ============================= *//*

    private void getPaymentDetails(final String strUserId, final String strVendorId, final String strValidity,
                                   final String strPrice, final String strTransactionId) {
        if (AppUtils.isOnline(PaymentGatewayActivity.this)) {
            AppUtils.getPaymentDetails(AppUrls.PAYMENT_GATEWAY_URL,
                    progressBar, PaymentGatewayActivity.this, strUserId, strVendorId, strValidity,
                    strPrice, strTransactionId, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                switch (response.getString("error")) {
                                    case "false":
                                        Toast.makeText(PaymentGatewayActivity.this, response.getString("Message"),
                                                Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(PaymentGatewayActivity.this, NavigationDrawerActivity.class);
                                        intent.putExtra("SWAMY_VENDOR_LOGIN_STATUS", AppConstants.LOGIN_STATUS);
                                        startActivity(intent);
                                        break;
                                    case "true":
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else AppUtils.showSnackBar(PaymentGatewayActivity.this);
    }
}

*/