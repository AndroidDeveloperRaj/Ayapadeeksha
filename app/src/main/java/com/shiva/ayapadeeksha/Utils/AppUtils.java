package com.shiva.ayapadeeksha.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.shiva.ayapadeeksha.Activities.NavigationDrawerActivity;
import com.shiva.ayapadeeksha.Activities.RegistrationActivity;
import com.shiva.ayapadeeksha.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppUtils {
    private static FusedLocationProviderClient fusedLocationProviderClient;
    private static Geocoder geocoder;
    private static List<Address> addresses;

    private static String strEmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    /* ================ Email Validation ==================*/
    public static boolean isEmailVerified(String strEmail) {
        if (strEmail.matches(strEmailPattern))
            return true;
        else
            return false;
    }

    /* ================ Showing SnackBar If no internet connection ================== */
    public static void showSnackBar(final Activity activity) {
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle("No Internet Connection")
                .setCancelable(false)
                .setMessage("You are offline please check your internet connection !")
                .setPositiveButton("Goto Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /* ================ Checking INTERNET Connection =================== */
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    /* ========================= Checking Runtime Permissions ============================== */



    /* external storage  permissions*/

    public static void checkPermission(final Activity activity, String[] permissionsArray, final GrantPermissions permissions) {
        Permissions.check(activity, permissionsArray, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                permissions.onGranted(true);
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                // permission denied
                Toast.makeText(activity, "Permission Denied", Toast.LENGTH_LONG).show();
                permissions.onGranted(false);
            }
        });
    }

    /* =========================== Type Of deeksha ================================= */
    public static void getTypeOfDeeksha(final String strUrl, final Activity activity,
                                        final ProgressBar progressBar, final VolleyCallback callback) {

        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, strUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* =========================== Type Of Vendors ================================= */
    public static void getTypeOfVendors(final String strUrl, final Activity activity,
                                        final ProgressBar progressBar, final VolleyCallback callback) {

        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, strUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* =========================== Type Of Checked Vendors List ================================= */
    public static void getCheckedVendors(final String strUrl, final Activity activity,
                                         final ProgressBar progressBar, final String strUserId,
                                         final VolleyCallback callback) {

        progressBar.setVisibility(View.VISIBLE);

        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("user_id", strUserId);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* =========================== Open Launch Screen ================================= */
    public static void openLaunchScreen(final String strUrl, final Activity activity,
                                        final ProgressBar progressBar, final String strUserId,
                                        final String strLoginStatus, final VolleyCallback callback) {

        progressBar.setVisibility(View.VISIBLE);

        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("user_id", strUserId);
        builder.appendQueryParameter("status", strLoginStatus);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* =========================== Add Types of vendors List  ================================= */
    public static void getRemainingVendors(final String strUrl, final Activity activity,
                                           final ProgressBar progressBar, final String strEmail,
                                           final VolleyCallback callback) {

        progressBar.setVisibility(View.VISIBLE);

        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("email", strEmail);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* =========================== Delete vendors List  ================================= */
    public static void getDeleteVendors(final String strUrl, final Activity activity,
                                        final String strEmail, final String strVendorId,
                                        final VolleyCallback callback) {


        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("email", strEmail);
        builder.appendQueryParameter("vendor_id", strVendorId);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* ================ Fetching Cities ===================== */
    public static void getCities(final String strUrl, final Activity activity,
                                 final ProgressBar progressBar, final String strStateId,
                                 final VolleyCallback callback) {

        progressBar.setVisibility(View.VISIBLE);

        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("state_id", strStateId);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* ================ Fetching States ===================== */
    public static void getStates(final String strUrl, final Activity activity,
                                 final ProgressBar progressBar, final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, strUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                callback.onSuccess(String.valueOf(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(activity, "Failed to load States", Toast.LENGTH_LONG).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    /* =========================== User Email Login ================================= */
    public static void UserEmailLogin(final String strUrl, final Activity activity,
                                      final ProgressBar progressBar, final String strEmail,
                                      final String strPassword, final String strLoginType,
                                      final VolleyCallback callback) {

        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("email", strEmail);
        builder.appendQueryParameter("password", strPassword);
        builder.appendQueryParameter("login_type", strLoginType);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed to Load Details", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* =========================== User Contact Number Login ================================= */
    public static void UserContactNumberLogin(final String strUrl, final Activity activity,
                                              final ProgressBar progressBar, final String strEmail,
                                              final String strPassword, final String strLoginType,
                                              final VolleyCallback callback) {

        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("contact_no", strEmail);
        builder.appendQueryParameter("password", strPassword);
        builder.appendQueryParameter("login_type", strLoginType);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed to Load Details", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* =========================== User OTP Login ================================= */
    public static void getForgetPassword(final String strUrl, final Activity activity,
                                         final String strMobileNumber, final String strStatus,
                                         final VolleyCallback callback) {

        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("contact_no", strMobileNumber);
        builder.appendQueryParameter("status", strStatus);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(activity, "Failed to Load Details", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* =========================== User OTP Login ================================= */
    public static void getLoginContactNumber(final String strUrl, final Activity activity,
                                             final String strMobileNumber, final String strStatus,
                                             final VolleyCallback callback) {

        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("contact_no", strMobileNumber);
        builder.appendQueryParameter("status", strStatus);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(activity, "Failed to Load Details", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* ======================== Update Profile ======================== */
    public static void getUserDetails(final String strUrl, final Activity activity,
                                      final ProgressBar progressBar, final String strUserId,
                                      final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("id", strUserId);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* ======================== NearBy Padi Puja Details ======================== */
    public static void getNearByPadiPujaDetails(final String strUrl, final Activity activity,
                                                final ProgressBar progressBar, final String strLatitude,
                                                final String strLongitude, final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("latitude", strLatitude);
        builder.appendQueryParameter("longitude", strLongitude);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed to load Details", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* ======================= Save Guru Swamy Experience Details ============================ */
    public static void saveGuruSwamyExperienceDetails(final String strUrl, final ProgressBar progressBar,
                                                      final Activity activity, final String strEmail,
                                                      final String strPersonName, final String strExperience,
                                                      final String strPrice, final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("email", strEmail);
        builder.appendQueryParameter("name", strPersonName);
        builder.appendQueryParameter("experience", strExperience);
        builder.appendQueryParameter("price", strPrice);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* ======================= Save Musical Band Details ============================ */
    public static void saveMusicalBandsDetails(final String strUrl, final ProgressBar progressBar,
                                               final Activity activity, final String strEmail,
                                               final String strPersonName, final String strExperience,
                                               final String strPrice, final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("email", strEmail);
        builder.appendQueryParameter("band_name", strPersonName);
        builder.appendQueryParameter("no_of_persons", strExperience);
        builder.appendQueryParameter("price", strPrice);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* ======================= Save Mandap Vendors Details ============================ */
    public static void saveMandapVendorsDetails(final String strUrl, final ProgressBar progressBar,
                                                final Activity activity, final String strEmail,
                                                final String strPersonName, final String strPrice,
                                                final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("email", strEmail);
        builder.appendQueryParameter("name", strPersonName);
        builder.appendQueryParameter("price", strPrice);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* ======================= Save Ayyappa Hotels Details ============================ */
    public static void saveAyyappaHotelsDetails(final String strUrl, final ProgressBar progressBar,
                                                final Activity activity, final String strEmail,
                                                final String strHotelName, final String strFlatNo,
                                                final String strLocality, final String strLandmark,
                                                final String strState, final String strCity,
                                                final String strPinCode, final String strPrice,
                                                final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("email", strEmail);
        builder.appendQueryParameter("hotel_name", strHotelName);
        builder.appendQueryParameter("price", strPrice);
        builder.appendQueryParameter("flatno", strFlatNo);
        builder.appendQueryParameter("landmark", strLandmark);
        builder.appendQueryParameter("locality", strLocality);
        builder.appendQueryParameter("city", strCity);
        builder.appendQueryParameter("state", strState);
        builder.appendQueryParameter("pincode", strPinCode);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }


    /* ======================== Update Guru Swamy Profile ======================== */
    public static void getGuruSwamyDetails(final String strUrl, final Activity activity,
                                           final ProgressBar progressBar, final String strEmail,
                                           final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("email", strEmail);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed to load Details", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* ======================== Update Musical Band Details ======================== */
    public static void getMusicalBandDetails(final String strUrl, final Activity activity,
                                             final ProgressBar progressBar, final String strEmail,
                                             final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("email", strEmail);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed to load Details", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* ======================== Update Mandap Vendors Details ======================== */
    public static void getMandapVendorsDetails(final String strUrl, final Activity activity,
                                               final ProgressBar progressBar, final String strEmail,
                                               final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("email", strEmail);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed to load Details", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* ======================== Update Ayyappa Swamy Details ======================== */
    public static void getAyyappaSwamyDetails(final String strUrl, final Activity activity,
                                              final ProgressBar progressBar, final String strEmail,
                                              final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("email", strEmail);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed to load Details", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* ======================= Save Guru Swamy Experience Details ============================ */
    public static void getLists(final String strUrl, final ProgressBar progressBar,
                                final Activity activity, final String strLatitude,
                                final String strLongitude, final String strVendorId,
                                final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("vendor_id", strVendorId);
        builder.appendQueryParameter("latitude", strLatitude);
        builder.appendQueryParameter("longitude", strLongitude);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* =========================== Change Password ============================ */
    public static void changePassword(final String strUrl, final ProgressBar progressBar,
                                      final Activity activity, final String strUserId,
                                      final String strOldPassword, final String strNewPassword,
                                      final String strcPassword, final String strStatus,
                                      final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("user_id", strUserId);
        builder.appendQueryParameter("oldpassword", strOldPassword);
        builder.appendQueryParameter("newpassword", strNewPassword);
        builder.appendQueryParameter("cpassword", strcPassword);
        builder.appendQueryParameter("status", strStatus);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* =========================== Send OTP ============================ */
    public static void sendContactOTP(final String strUrl, final ProgressBar progressBar,
                                      final Activity activity, final String strContactNumber,
                                      final String strOtp, final String strStatus,
                                      final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("contact_no", strContactNumber);
        builder.appendQueryParameter("otp", strOtp);
        builder.appendQueryParameter("status", strStatus);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* =========================== Send OTP ============================ */
    public static void sendRegistrationOTP(final String strUrl, final Activity activity,
                                           final String strContactNumber, final VolleyCallback callback) {
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("contact_no", strContactNumber);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* =========================== Guru Swamy All Details ============================ */
    public static void getGuruSwamyAllDetails(final String strUrl, final ProgressBar progressBar,
                                              final Activity activity, final String strEmail,
                                              final String strVendorId, final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("email", strEmail);
        builder.appendQueryParameter("vendor_id", strVendorId);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* =========================== Subscriptions Details ============================ */
    public static void getSubscriptions(final String strUrl, final ProgressBar progressBar,
                                        final Activity activity, final String strEmail,
                                        final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("email", strEmail);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* =========================== Subscriptions Details ============================ */
    public static void getPaymentDetails(final String strUrl, final ProgressBar progressBar,
                                         final Activity activity, final String strUserId,
                                         final String strVendorId, final String strValidity,
                                         final String strPrice, final String strPaymentId,
                                         final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("payment_id", strPaymentId);
        builder.appendQueryParameter("user_id", strUserId);
        builder.appendQueryParameter("vendor_id", strVendorId);
        builder.appendQueryParameter("duration", strValidity);
        builder.appendQueryParameter("amount", strPrice);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* =========================== Padipuja Details Details ============================ */
    public static void getPadiPujaDetails(final String strUrl, final ProgressBar progressBar,
                                          final Activity activity, final String strUserId,
                                          final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("user_id", strUserId);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }


    /* Delete Padi Puja Details*/

    public static void deletePadiPujaDetails(final String strUrl, final ProgressBar progressBar,
                                          final Activity activity, final String strUserId,
                                          final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("post_id", strUserId);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* =========================== Swamy Details ============================ */
    public static void getSwamyDetails(final String strUrl, final ProgressBar progressBar,
                                       final Activity activity, final String strUserId,
                                       final VolleyCallback callback) {
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("id", strUserId);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* =========================== Songs List ================================= */
    public static void getSongs(final String strUrl, final Activity activity,
                                final ProgressBar progressBar, final VolleyCallback callback) {

        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, strUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /* ========================= Current Location ========================= */
    public static void getCurrentLocation(final Activity activity, final CurrentLocation currentLocation) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            location.getLatitude();
                            location.getLongitude();

                            geocoder = new Geocoder(activity, Locale.getDefault());
                            currentLocation.onLocationFeched(geocoder, location);

                        } else {
//                            Toast.makeText(activity, "Cannot find Location",
//                                    Toast.LENGTH_LONG).show();
                            geocoder = new Geocoder(activity, Locale.getDefault());
                            currentLocation.onLocationFeched(geocoder, location);
                        }
                    }
                });
    }

    /*=====Add Posts===========*/
    public static void getPosts(final String strUrl, final Activity activity, final String StrUser_id,
                                final String StrDescription,
                                final String StrPrivacy, final String StrImage, final String StrVideo,
                                final VolleyCallback callback) {
        Uri.Builder builder = Uri.parse(strUrl).buildUpon();
        builder.appendQueryParameter("user_id", StrUser_id);
        builder.appendQueryParameter("description", StrDescription);
        builder.appendQueryParameter("privacy", StrPrivacy);
        builder.appendQueryParameter("image", StrImage);
        builder.appendQueryParameter("video", StrVideo);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

/* GET Request*/
public static void getFriendRequest(final String strUrl, final Activity activity, final String StrUser_id,
                            final String StrStatus,
                            final VolleyCallback callback) {
    Uri.Builder builder = Uri.parse(strUrl).buildUpon();
    builder.appendQueryParameter("user_id", StrUser_id);
    builder.appendQueryParameter("status", StrStatus);


    JsonObjectRequest jsonRequest = new JsonObjectRequest
            (Request.Method.POST, builder.toString(), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    callback.onSuccess(String.valueOf(response));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                }
            });
    jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
            50000,
            0,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    AppController.getInstance().addToRequestQueue(jsonRequest);
}
}

