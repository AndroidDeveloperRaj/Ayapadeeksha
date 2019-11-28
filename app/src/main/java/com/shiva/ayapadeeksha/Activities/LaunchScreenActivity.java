package com.shiva.ayapadeeksha.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppConstants;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.CurrentLocation;
import com.shiva.ayapadeeksha.Utils.GrantPermissions;
import com.shiva.ayapadeeksha.Utils.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LaunchScreenActivity extends AppCompatActivity {
    public static final long LAUNCH_SCREEN_TIMEOUT = 3000;
    private MediaPlayer mp;
    public SharedPreferences sharedPreferences;
    private String strLaunchScreenId, strLoginStatus;
    private ProgressBar progressBar;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Geocoder geocoder;
    private List<Address> addresses;
    private String strStateName, strCityName, strLaunchScreenLoginStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch_screen);
        getSupportActionBar().hide();
    }

    private void askPermissions(final String[] permissions) {
        AppUtils.checkPermission(LaunchScreenActivity.this, permissions, new GrantPermissions() {
            @Override
            public void onGranted(Boolean status) {
                if (status) {
                    getCurrentLocation();
                } else {
                    askPermissions(permissions);
                }
            }
        });
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();

        geocoder = new Geocoder(LaunchScreenActivity.this, Locale.getDefault());
        sharedPreferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
        strLaunchScreenId = sharedPreferences.getString("LaunchScreenId", null);
        strLoginStatus = sharedPreferences.getString("LoginStatus", null);
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (AppUtils.isOnline(LaunchScreenActivity.this)) {
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET};
                askPermissions(permissions);
            } else {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("GPS Settings");
                alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu ..?");

                alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        } else {
            AppUtils.showSnackBar(LaunchScreenActivity.this);
        }

    }

    private void getCurrentLocation() {
        AppUtils.getCurrentLocation(LaunchScreenActivity.this, new CurrentLocation() {
            @Override
            public void onLocationFeched(Geocoder geocoder, Location location) {
                geocoder = new Geocoder(LaunchScreenActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);

                    strCityName = addresses.get(0).getLocality();
                    strStateName = addresses.get(0).getFeatureName();

                    AppConstants.CURRENT_LOCATION_LATITUDE = String.valueOf(location.getLatitude());
                    AppConstants.CURRENT_LOCATION_LONGITUDE = String.valueOf(location.getLongitude());
                    AppConstants.CURRENT_LOCATION_CITY_NAME = strCityName;
                    AppConstants.CURRENT_LOCATION_STATE = strStateName;
                    AppConstants.LOCALITY = addresses.get(0).getAddressLine(0);

                    if (strLaunchScreenId != null && strLoginStatus != null) {
                        openLaunchScreen(strLaunchScreenId, strLoginStatus);
                    } else {
                        startActivity(new Intent(LaunchScreenActivity.this, LoginActivity.class));
                        finish();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void openLaunchScreen(final String strLaunchScreenId, final String strLoginStatus) {
        if (AppUtils.isOnline(LaunchScreenActivity.this)) {
            AppUtils.openLaunchScreen(AppUrls.LAUNCH_SCREEN_URL, LaunchScreenActivity.this, progressBar,
                    strLaunchScreenId, strLoginStatus, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                final JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    Toast.makeText(LaunchScreenActivity.this, response.getString("Message"),
                                            Toast.LENGTH_LONG).show();
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
                                        strLaunchScreenLoginStatus = response.getString("status");

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    if (response.getString("Subscription_status").equals("1")) {
                                                        //openLaunchScreen(strLaunchScreenId);
                                                        Intent intent = new Intent(LaunchScreenActivity.this, NavigationDrawerActivity.class);
                                                        intent.putExtra("SWAMY_VENDOR_LOGIN_STATUS", strLaunchScreenLoginStatus);
                                                        SharedPreferences sharedPreferences = getSharedPreferences
                                                                ("LoginDetails", Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                                        editor.putString("LaunchScreenId", strLaunchScreenId);
                                                        editor.putString("LoginStatus", strLoginStatus);

                                                        editor.commit();
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
                                                        finish();
                                                    } else if (response.getString("Subscription_status").equals("0")) {
                                                        Intent intent = new Intent(LaunchScreenActivity.this, MySubscriptionsActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, LAUNCH_SCREEN_TIMEOUT);
                                    } else if (response.getString("status").equals("1")) {
                                        AppConstants.USER_ID = response.getString("id");
                                        AppConstants.TYPE_OF_DEEKSHA = response.getString("type");
                                        AppConstants.USER_NAME = response.getString("user_name");
                                        AppConstants.CONTACT_NO = response.getString("contact_no");
                                        strLaunchScreenLoginStatus = response.getString("status");
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(LaunchScreenActivity.this, NavigationDrawerActivity.class);
                                                intent.putExtra("SWAMY_VENDOR_LOGIN_STATUS", strLaunchScreenLoginStatus);

                                                SharedPreferences sharedPreferences = getSharedPreferences
                                                        ("LoginDetails", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("LaunchScreenId", strLaunchScreenId);
                                                editor.putString("LoginStatus", strLoginStatus);
                                                editor.commit();
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }, LAUNCH_SCREEN_TIMEOUT);
                                    }


                                } else {
                                    Toast.makeText(LaunchScreenActivity.this, response.getString("error_msg"),
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            AppUtils.showSnackBar(LaunchScreenActivity.this);
        }
    }

}

