package com.shiva.ayapadeeksha.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppConstants;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener {
    private Button btnRegistration, btnLogin, btnVerifyOtp, btnCancelOtp, btnOtpLogin, btnForgotPassword;
    private TextInputLayout tilMobileNumberOrEmail, tilPassword, tilOtp;
    private TextInputEditText tieMobileNumberOrEmail, tiePassword, tieOtp;
    private String strMobileNumberOrEmail, strPassword, strOTP, strLaunchScreenId, strLoginType,
            strSwamyLoginStatus, strVendorLoginStatus;
    private ProgressBar progressBar, progressBarOtpVerification;
    private AlertDialog alertDialogOTPVerification;
    private RadioGroup radiGroupLogin;
    private RadioButton rBLoginSwamy, rBLoginVendor;

    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("Login");
        init();
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        tilMobileNumberOrEmail = (TextInputLayout) findViewById(R.id.tilMobileNumberOrEmail);
        tilPassword = (TextInputLayout) findViewById(R.id.tilPassword);

        radiGroupLogin = (RadioGroup) findViewById(R.id.radiGroupLogin);
        radiGroupLogin = (RadioGroup) findViewById(R.id.radiGroupLogin);
        rBLoginSwamy = (RadioButton) findViewById(R.id.rBLoginSwamy);
        rBLoginVendor = (RadioButton) findViewById(R.id.rBLoginVendor);

        tieMobileNumberOrEmail = (TextInputEditText) findViewById(R.id.tieMobileNumberOrEmail);
        tiePassword = (TextInputEditText) findViewById(R.id.tiePassword);

        btnRegistration = (Button) findViewById(R.id.btnRegistration);
        btnOtpLogin = (Button) findViewById(R.id.btnOtpLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnForgotPassword = (Button) findViewById(R.id.btnForgotPassword);

        signInButton = findViewById(R.id.sign_in_button);
        googleSignInOptionClicked();

        radiGroupLogin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rBLoginSwamy:
                        strLoginType = "1";
                        break;
                    case R.id.rBLoginVendor:
                        strLoginType = "0";
                        break;
                }
            }
        });
        btnRegistration.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnOtpLogin.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });
    }


    public void googleSignInOptionClicked() {


            //Initializing Views


            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn();
                }
            });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

               AppConstants.USER_NAME = acct.getDisplayName();
               AppConstants.EMAIL = acct.getEmail();
               AppConstants.USER_ID = acct.getId();
                strVendorLoginStatus="1";
          //     AppConstants.
            }

            // Signed in successfully, show authenticated UI.
           Intent intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
            intent.putExtra("SWAMY_VENDOR_LOGIN_STATUS", strVendorLoginStatus);
            SharedPreferences sharedPreferences = getSharedPreferences
                    ("LoginDetails", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("LaunchScreenId", strLaunchScreenId);
            editor.putString("LoginStatus", strVendorLoginStatus);
            //editor.putString("LoginContactNumber", strMobileNumberOrEmail);
            // editor.putString("LoginPassword", strPassword);

            editor.commit();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            startActivity(intent);
            finish();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            startActivity(new Intent(LoginActivity.this, NavigationDrawerActivity.class));
        }
        super.onStart();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegistration:
                Intent intentRegistration = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intentRegistration);
                break;
            case R.id.btnOtpLogin:
                strMobileNumberOrEmail = tieMobileNumberOrEmail.getText().toString();
                if (strMobileNumberOrEmail.isEmpty())
                    tilMobileNumberOrEmail.setError("Mobile Number Required");
                else if (radiGroupLogin.getCheckedRadioButtonId() == -1)
                    Toast.makeText(LoginActivity.this, "Select any RadioButton", Toast.LENGTH_LONG).show();
                else if (strMobileNumberOrEmail == null || AppUtils.isEmailVerified(strMobileNumberOrEmail))
                    tilMobileNumberOrEmail.setError("Enter your Mobile Number only");
                else {
                    getLoginContactNumber(strMobileNumberOrEmail);
                }
                break;
            case R.id.btnForgotPassword:
                strMobileNumberOrEmail = tieMobileNumberOrEmail.getText().toString();
                if (strMobileNumberOrEmail.isEmpty())
                    tilMobileNumberOrEmail.setError("Enter your Mobile Number only");
                else if (radiGroupLogin.getCheckedRadioButtonId() == -1)
                    Toast.makeText(LoginActivity.this, "Select any RadioButton", Toast.LENGTH_LONG).show();
                else if (strMobileNumberOrEmail == null || AppUtils.isEmailVerified(strMobileNumberOrEmail)) {
                    tilMobileNumberOrEmail.setError("Enter your Mobile Number only");
                } else {
                    getForgetPassword(strMobileNumberOrEmail);
                }
                break;
            case R.id.btnLogin:
                strMobileNumberOrEmail = tieMobileNumberOrEmail.getText().toString();
                strPassword = tiePassword.getText().toString();

                if (strMobileNumberOrEmail.isEmpty())
                    tilMobileNumberOrEmail.setError("Mobile Number or Email Required");
                else if (strPassword.isEmpty())
                    tilPassword.setError("Password Required");
                else if (radiGroupLogin.getCheckedRadioButtonId() == -1)
                    Toast.makeText(LoginActivity.this, "Select any RadioButton", Toast.LENGTH_LONG).show();
                else if (AppUtils.isEmailVerified(strMobileNumberOrEmail)) {
                    getLoginDetails("strEmail");
                } else {
                    getLoginDetails("strContactNumber");
                }
                break;
        }
    }

    private void getLoginContactNumber(final String strContactNumber) {
        if (AppUtils.isOnline(LoginActivity.this)) {
            AppUtils.getLoginContactNumber(AppUrls.LOGIN_WITH_OTP_URL, LoginActivity.this,
                    strContactNumber, strLoginType, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    Toast.makeText(LoginActivity.this, response.getString("Message"), Toast.LENGTH_LONG).show();
                                    if (response.getString("status").equals("0")) {
                                        AppConstants.USER_ID = response.getString("id");
                                        AppConstants.EMAIL = response.getString("email");
                                        AppConstants.USER_NAME = response.getString("username");
                                        AppConstants.TYPE_OF_DEEKSHA = response.getString("type");
                                        AppConstants.CITY = response.getString("city");
                                        AppConstants.LOCALITY = response.getString("locality");
                                        AppConstants.FLAT_NO = response.getString("flatno");
                                        AppConstants.STATE = response.getString("state");
                                        AppConstants.PINCODE = response.getString("pincode");
                                        AppConstants.LANDMARK = response.getString("landmark");
                                        AppConstants.CONTACT_NO = response.getString("contactno");
                                        AppConstants.VENDORS = response.getString("vendors");
                                        AppConstants.TYPE_OF_DEEKSHA_ID = response.getString("type_id");
                                        AppConstants.VENDORS_ID = response.getString("vendors_id");

                                        if (response.getString("Subscription_status").equals("1")) {
                                            strVendorLoginStatus = response.getString("status");
                                            openOtpDialog(strVendorLoginStatus);
                                        } else if (response.getString("Subscription_status").equals("0")) {
                                            Intent intent = new Intent(LoginActivity.this, MySubscriptionsActivity.class);
                                            startActivity(intent);
                                        }
                                    } else if (response.getString("status").equals("1")) {
                                        AppConstants.USER_ID = response.getString("id");
                                        AppConstants.TYPE_OF_DEEKSHA = response.getString("type");
                                        AppConstants.TYPE_OF_DEEKSHA_ID = response.getString("type_id");
                                        AppConstants.USER_NAME = response.getString("user_name");
                                        AppConstants.CONTACT_NO = response.getString("contact_no");

                                        strSwamyLoginStatus = response.getString("status");
                                        openOtpDialog(strSwamyLoginStatus);
                                    }


                                } else {
                                    Toast.makeText(LoginActivity.this, response.getString("error_msg"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            AppUtils.showSnackBar(LoginActivity.this);
        }

    }

    private void getForgetPassword(final String strMobileNumberOrEmail) {
        if (AppUtils.isOnline(LoginActivity.this)) {
            AppUtils.getForgetPassword(AppUrls.FORGET_PASSWORD_URL, LoginActivity.this,
                    strMobileNumberOrEmail,strLoginType, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    Toast.makeText(LoginActivity.this, response.getString("Message"),
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, response.getString("error_msg"),
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            AppUtils.showSnackBar(LoginActivity.this);
        }
    }

    private void openOtpDialog(final String strLoginStatus) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = layoutInflater.inflate(R.layout.dialog_otp_verification, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        progressBarOtpVerification = (ProgressBar) dialogView.findViewById(R.id.progressBarOtpVerification);

        tilOtp = (TextInputLayout) dialogView.findViewById(R.id.tilOtp);
        tieOtp = (TextInputEditText) dialogView.findViewById(R.id.tieOtp);

        btnVerifyOtp = (Button) dialogView.findViewById(R.id.btnVerifyOtp);
        btnCancelOtp = (Button) dialogView.findViewById(R.id.btnCancelOtp);


        btnCancelOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogOTPVerification.dismiss();
            }
        });

        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strOTP = tieOtp.getText().toString();
                if (strOTP.isEmpty()) {
                    tilOtp.setError("Enter OTP");
                } else {
                    sendContactOTP(strMobileNumberOrEmail, strOTP, strLoginStatus);
                }
            }
        });
        alertDialogOTPVerification = builder.create();
        alertDialogOTPVerification.show();
    }

    private void getLoginDetails(final String strTitle) {
        switch (strTitle) {
            case "strEmail":
                sendEmailLogin();
                break;
            case "strContactNumber":
                sendContactNumberLogin();
                break;
        }
    }

    private void sendContactNumberLogin() {
        if (AppUtils.isOnline(LoginActivity.this)) {
            AppUtils.UserContactNumberLogin(AppUrls.LOGIN_URL, LoginActivity.this, progressBar, strMobileNumberOrEmail,
                    strPassword, strLoginType, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    Toast.makeText(LoginActivity.this, response.getString("Message"), Toast.LENGTH_LONG).show();

                                    if (response.getString("status").equals("0")) {
                                        AppConstants.USER_ID = response.getString("id");
                                        AppConstants.EMAIL = response.getString("email");
                                        AppConstants.USER_NAME = response.getString("username");
                                        AppConstants.TYPE_OF_DEEKSHA = response.getString("type");
                                        AppConstants.TYPE_OF_DEEKSHA_ID = response.getString("type_id");
                                        AppConstants.DATE_OF_BIRTH = response.getString("dob");
                                        AppConstants.CITY = response.getString("city");
                                        AppConstants.LOCALITY = response.getString("locality");
                                        AppConstants.FLAT_NO = response.getString("flatno");
                                        AppConstants.STATE = response.getString("state");
                                        AppConstants.PINCODE = response.getString("pincode");
                                        AppConstants.LANDMARK = response.getString("landmark");
                                        AppConstants.CONTACT_NO = response.getString("contactno");
                                        AppConstants.VENDORS = response.getString("vendors");
                                        AppConstants.VENDORS_ID = response.getString("vendors_id");

                                        strVendorLoginStatus = response.getString("status");
                                        strLaunchScreenId = response.getString("id");

                                        if (response.getString("Subscription_status").equals("1")) {

                                            Intent intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                                            intent.putExtra("SWAMY_VENDOR_LOGIN_STATUS", strVendorLoginStatus);
                                            SharedPreferences sharedPreferences = getSharedPreferences
                                                    ("LoginDetails", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();

                                            editor.putString("LaunchScreenId", strLaunchScreenId);
                                            editor.putString("LoginStatus", strVendorLoginStatus);
                                            //editor.putString("LoginContactNumber", strMobileNumberOrEmail);
                                            // editor.putString("LoginPassword", strPassword);

                                            editor.commit();
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        } else if (response.getString("Subscription_status").equals("0")) {
                                            Intent intent = new Intent(LoginActivity.this, MySubscriptionsActivity.class);
                                            startActivity(intent);
                                        }
                                    } else if (response.getString("status").equals("1")) {
                                        AppConstants.USER_ID = response.getString("id");
                                        AppConstants.TYPE_OF_DEEKSHA = response.getString("type");
                                        AppConstants.CONTACT_NO = response.getString("contact_no");
                                        AppConstants.USER_NAME = response.getString("user_name");
                                        AppConstants.TYPE_OF_DEEKSHA_ID = response.getString("type_id");

                                        strSwamyLoginStatus = response.getString("status");
                                        strLaunchScreenId = response.getString("id");

                                        Intent intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                                        intent.putExtra("SWAMY_VENDOR_LOGIN_STATUS", strSwamyLoginStatus);
                                        SharedPreferences sharedPreferences = getSharedPreferences
                                                ("LoginDetails", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putString("LaunchScreenId", strLaunchScreenId);
                                        editor.putString("LoginStatus", strSwamyLoginStatus);

                                        //editor.putString("LoginContactNumber", strMobileNumberOrEmail);
                                        // editor.putString("LoginPassword", strPassword);

                                        editor.commit();
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();

                                    }


                                } else if (response.getString("error").equals("true")) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, response.getString("error_msg"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else
            AppUtils.showSnackBar(LoginActivity.this);
    }

    private void sendEmailLogin() {
        if (AppUtils.isOnline(LoginActivity.this)) {
            AppUtils.UserEmailLogin(AppUrls.LOGIN_URL, LoginActivity.this, progressBar, strMobileNumberOrEmail,
                    strPassword, strLoginType, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    Toast.makeText(LoginActivity.this, response.getString("Message"), Toast.LENGTH_LONG).show();

                                    if (response.getString("status").equals("0")) {
                                        AppConstants.USER_ID = response.getString("id");
                                        AppConstants.EMAIL = response.getString("email");
                                        AppConstants.TYPE_OF_DEEKSHA = response.getString("type");
                                        AppConstants.TYPE_OF_DEEKSHA_ID = response.getString("type_id");
                                        AppConstants.DATE_OF_BIRTH = response.getString("dob");
                                        AppConstants.CITY = response.getString("city");
                                        AppConstants.LOCALITY = response.getString("locality");
                                        AppConstants.FLAT_NO = response.getString("flatno");
                                        AppConstants.STATE = response.getString("state");
                                        AppConstants.PINCODE = response.getString("pincode");
                                        AppConstants.LANDMARK = response.getString("landmark");
                                        AppConstants.CONTACT_NO = response.getString("contactno");
                                        AppConstants.VENDORS = response.getString("vendors");
                                        AppConstants.VENDORS_ID = response.getString("vendors_id");

                                        strVendorLoginStatus = response.getString("status");
                                        strLaunchScreenId = response.getString("id");
                                        if (response.getString("Subscription_status").equals("1")) {

                                            Intent intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                                            intent.putExtra("SWAMY_VENDOR_LOGIN_STATUS", strVendorLoginStatus);
                                            SharedPreferences sharedPreferences = getSharedPreferences
                                                    ("LoginDetails", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();

                                            editor.putString("LaunchScreenId", strLaunchScreenId);
                                            editor.putString("LoginStatus", strVendorLoginStatus);
                                            //editor.putString("LoginPassword", strPassword);

                                            editor.commit();
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        } else if (response.getString("Subscription_status").equals("0")) {
                                            Intent intent = new Intent(LoginActivity.this, MySubscriptionsActivity.class);
                                            startActivity(intent);
                                        }
                                    } else if (response.getString("status").equals("1")) {
                                        AppConstants.USER_ID = response.getString("id");
                                        AppConstants.TYPE_OF_DEEKSHA = response.getString("type");
                                        AppConstants.CONTACT_NO = response.getString("contact_no");
                                        AppConstants.USER_NAME = response.getString("user_name");
                                        AppConstants.TYPE_OF_DEEKSHA_ID = response.getString("type_id");

                                        strSwamyLoginStatus = response.getString("status");
                                        strLaunchScreenId = response.getString("id");

                                        Intent intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                                        intent.putExtra("SWAMY_VENDOR_LOGIN_STATUS", strSwamyLoginStatus);
                                        SharedPreferences sharedPreferences = getSharedPreferences
                                                ("LoginDetails", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putString("LaunchScreenId", strLaunchScreenId);
                                        editor.putString("LoginStatus", strSwamyLoginStatus);
                                        //editor.putString("LoginContactNumber", strMobileNumberOrEmail);
                                        // editor.putString("LoginPassword", strPassword);

                                        editor.commit();
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();

                                    }
                                } else if (response.getString("error").equals("true")) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, response.getString("error_msg"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else
            AppUtils.showSnackBar(LoginActivity.this);
    }

    /* ========================== Sending OTP to mMobile Number ========================= */
    private void sendContactOTP(final String strContactNumber, final String strOTP, final String strLoginStatus) {
        if (AppUtils.isOnline(LoginActivity.this)) {
            AppUtils.sendContactOTP(AppUrls.LOGIN_WITH_OTP_CHECK_URL, progressBar, LoginActivity.this,
                    strContactNumber, strOTP, strLoginStatus, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (!jsonObject.getBoolean("error")) {
                                    //Toast.makeText(LoginActivity.this, jsonObject.getString("Message"), Toast.LENGTH_LONG).show();
                                    //AppConstants.CONTACT_NO = jsonObject.getString("contactno");
                                    if (jsonObject.getString("status").equals("0")){
                                        strLaunchScreenId = jsonObject.getString("user_id");
                                        if (jsonObject.getString("Subscription_status").equals("1")) {
                                            Intent intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                                            intent.putExtra("SWAMY_VENDOR_LOGIN_STATUS", "0");
                                            SharedPreferences sharedPreferences = getSharedPreferences
                                                    ("LoginDetails", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();

                                            editor.putString("LaunchScreenId", strLaunchScreenId);
                                            //editor.putString("LoginPassword", strPassword);

                                            editor.commit();
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        } else if (jsonObject.getString("Subscription_status").equals("0")) {
                                            Intent intent = new Intent(LoginActivity.this, MySubscriptionsActivity.class);
                                            startActivity(intent);
                                        }
                                    }else if (jsonObject.getString("status").equals("1")){
                                        strLaunchScreenId = jsonObject.getString("id");
                                        Intent intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                                        intent.putExtra("SWAMY_VENDOR_LOGIN_STATUS", "1");
                                        SharedPreferences sharedPreferences = getSharedPreferences
                                                ("LoginDetails", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putString("LaunchScreenId", strLaunchScreenId);
                                        //editor.putString("LoginPassword", strPassword);

                                        editor.commit();
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                    alertDialogOTPVerification.dismiss();

                                } else {
                                    Toast.makeText(LoginActivity.this, jsonObject.getString("error_msg"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            AppUtils.showSnackBar(LoginActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Exit App !")
                .setMessage("Are you sure you want to Exit from the App?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                        finish();
                    }
                });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
