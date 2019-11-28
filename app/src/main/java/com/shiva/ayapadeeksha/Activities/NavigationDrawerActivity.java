package com.shiva.ayapadeeksha.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shiva.ayapadeeksha.Fragments.AddPostFragment;
import com.shiva.ayapadeeksha.Fragments.AyyapaSwamyHotelsFragment;
import com.shiva.ayapadeeksha.Fragments.FriendFragment;
import com.shiva.ayapadeeksha.Fragments.GuruSwamysListFragment;
import com.shiva.ayapadeeksha.Fragments.ImportanceofDeekshaFragment;
import com.shiva.ayapadeeksha.Fragments.MandapVendorsListFragment;
import com.shiva.ayapadeeksha.Fragments.MusicalBandsFragment;
import com.shiva.ayapadeeksha.Fragments.MySubscriptionsFragment;
import com.shiva.ayapadeeksha.Fragments.NearbyPadiPujaDetailsFragment;
import com.shiva.ayapadeeksha.Fragments.SongsFragment;
import com.shiva.ayapadeeksha.Fragments.WallPostsFragment;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppConstants;
import com.shiva.ayapadeeksha.Utils.AppUpdateChecker;
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

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private TextView tvContactNumber, tvEmail;
    private AlertDialog alertDialog;
    private ProgressBar progressBarChangePassword;
    private TextInputLayout tilOldPassword, tilNewPassword, tilConfirmPassword;
    private TextInputEditText tieOldPassword, tieNewPassword, tieConfirmPassword;
    private Button btnSubmit, btnCancel;
    private String strOldPassword, strNewPassword, strConfirmPassword, strLatitude, strLongitude,
            strLocality, strSwamyLoginStatus;
    private TextView tvLocationSearch;
    private List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new NearbyPadiPujaDetailsFragment()).commit();

        View headerView = navigationView.getHeaderView(0);

        strSwamyLoginStatus = getIntent().getStringExtra("SWAMY_VENDOR_LOGIN_STATUS");
        AppConstants.LOGIN_STATUS = strSwamyLoginStatus;

        tvLocationSearch = (TextView) findViewById(R.id.tvLocationSearch);
        tvContactNumber = (TextView) headerView.findViewById(R.id.tvContactNumber);

        tvContactNumber.setText(AppConstants.CONTACT_NO);

       /*Check For Updates in Google Play*/
        AppUpdateChecker appUpdateChecker=new AppUpdateChecker(this);  //pass the activity in constructure
        appUpdateChecker.checkForUpdate(true); //mannual check false here

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strSwamyLoginStatus.equals("1")) {
                    //tvEmail.setVisibility(View.GONE);
                    tvContactNumber.setPadding(0, 25, 0, 0);
                    tvContactNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit_profile, 0);
                    tvContactNumber.setText(AppConstants.CONTACT_NO);
                    Intent intent = new Intent(NavigationDrawerActivity.this, EditSwamyProfileActivity.class);
                    intent.putExtra("USER_ID", AppConstants.USER_ID);
                    intent.putExtra("USER_CONTACT_NUMBER", AppConstants.CONTACT_NO);
                    startActivity(intent);

                } else if (strSwamyLoginStatus.equals("0")) {
                    //tvEmail.setVisibility(View.VISIBLE);
                    tvContactNumber.setText(AppConstants.CONTACT_NO);
                    //tvEmail.setText(AppConstants.EMAIL);
                    Intent intent = new Intent(NavigationDrawerActivity.this, BeforeEditProfileActivity.class);
                    intent.putExtra("USER_ID", AppConstants.USER_ID);
                    intent.putExtra("USER_EMAIL", AppConstants.EMAIL);
                    //intent.putExtra("USER_CONTACT_NUMBER", AppConstants.CONTACT_NO);
                    startActivity(intent);
                }

            }
        });
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.CALL_PHONE
        };
        askPermissions(permissions);

        tvLocationSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strLatitude == null || strLongitude == null) {
                    tvLocationSearch.setError("Enter Area Name !");
                } else {
                    Intent locationIntent = new Intent(NavigationDrawerActivity.this, LocationActivity.class);
                    locationIntent.putExtra("SWAMY_VENDOR_LOGIN_STATUS", strSwamyLoginStatus);
                    startActivityForResult(locationIntent, 2);// Activity is started with requestCode 2
                }
            }
        });

        Menu nav_menu = navigationView.getMenu();
        if(strSwamyLoginStatus!=null) {
            if (strSwamyLoginStatus.equals("1")) {
                nav_menu.findItem(R.id.nav_Subscriptions).setVisible(false);
            } else if (strSwamyLoginStatus.equals("0")) {
                nav_menu.findItem(R.id.nav_Subscriptions).setVisible(true);
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new NearbyPadiPujaDetailsFragment()).commit();

    }


    private void askPermissions(final String[] permissions) {
        AppUtils.checkPermission(NavigationDrawerActivity.this, permissions, new GrantPermissions() {
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

    private void getCurrentLocation() {
        AppUtils.getCurrentLocation(NavigationDrawerActivity.this, new CurrentLocation() {
            @Override
            public void onLocationFeched(Geocoder geocoder, Location location) {
                geocoder = new Geocoder(NavigationDrawerActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    AppConstants.CURRENT_LOCATION_LATITUDE = String.valueOf(location.getLatitude());
                    AppConstants.CURRENT_LOCATION_LONGITUDE = String.valueOf(location.getLongitude());
                    AppConstants.LOCALITY = addresses.get(0).getAddressLine(0);

                    strLatitude = AppConstants.CURRENT_LOCATION_LATITUDE;
                    strLongitude = AppConstants.CURRENT_LOCATION_LONGITUDE;
                    strLocality = AppConstants.CURRENT_LOCALITY;

                    tvLocationSearch.setText(AppConstants.LOCALITY);
                    strLocality = tvLocationSearch.getText().toString();

                    getLocationLatitudeLongitude(tvLocationSearch.getText().toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        /*menuItem = menu.findItem(R.id.action_select_city);

        if (AppConstants.CURRENT_LOCATION_CITY_NAME == null) {
            menuItem.setTitle(AppConstants.USER_CITY);
        } else {
            menuItem.setTitle(AppConstants.CURRENT_LOCATION_CITY_NAME);
        }*/
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_change_password:
                openChangePasswordDialog();
                break;
            case R.id.action_logout:
                AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Logout !")
                        .setMessage("Are you sure you want to logout from the App ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(NavigationDrawerActivity.this, LaunchScreenActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
                                SharedPreferences.Editor edt = sharedPreferences.edit();
                                edt.clear().commit();

                                startActivity(intent);
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openChangePasswordDialog() {
        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = layoutInflater.inflate(R.layout.dialog_change_password, null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setTitle("Change Password !");
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        progressBarChangePassword = (ProgressBar) dialogView.findViewById(R.id.progressBarChangePassword);

        tilOldPassword = (TextInputLayout) dialogView.findViewById(R.id.tilOldPassword);
        tilNewPassword = (TextInputLayout) dialogView.findViewById(R.id.tilNewPassword);
        tilConfirmPassword = (TextInputLayout) dialogView.findViewById(R.id.tilConfirmPassword);

        tieOldPassword = (TextInputEditText) dialogView.findViewById(R.id.tieOldPassword);
        tieNewPassword = (TextInputEditText) dialogView.findViewById(R.id.tieNewPassword);
        tieConfirmPassword = (TextInputEditText) dialogView.findViewById(R.id.tieConfirmPassword);

        btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);
        btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strOldPassword = tieOldPassword.getText().toString();
                strNewPassword = tieNewPassword.getText().toString();
                strConfirmPassword = tieConfirmPassword.getText().toString();

                if (strOldPassword.isEmpty())
                    tilOldPassword.setError("Old Password Required");
                else if (strNewPassword.isEmpty())
                    tilNewPassword.setError("New Password Required");
                else if (strConfirmPassword.isEmpty())
                    tilConfirmPassword.setError("Confirm Password Required");
                else if (!strNewPassword.equals(strConfirmPassword))
                    tilConfirmPassword.setError("Wrong Password");
                else
                    changePassword();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void changePassword() {
        if (AppUtils.isOnline(NavigationDrawerActivity.this)) {
            AppUtils.changePassword(AppUrls.CHANGE_PASSWORD_URL, progressBarChangePassword, NavigationDrawerActivity.this,
                    AppConstants.USER_ID, strOldPassword, strNewPassword, strConfirmPassword,strSwamyLoginStatus, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    alertDialog.dismiss();
                                    Toast.makeText(NavigationDrawerActivity.this,
                                            response.getString("Message"), Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(NavigationDrawerActivity.this,
                                            response.getString("error_msg"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {

            case R.id.nav_NearByPadiPujaDetails:
                AppConstants.FRAGMENT_NAME = "Nearby Padi Puja Details";
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new NearbyPadiPujaDetailsFragment()).commit();
                tvLocationSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_GuruSwamysList:
                AppConstants.FRAGMENT_NAME = "Guru Swamy's List";
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new GuruSwamysListFragment()).commit();
                tvLocationSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_MusicalBands:
                AppConstants.FRAGMENT_NAME = "Musical Bands";
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new MusicalBandsFragment()).commit();
                tvLocationSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_MandapVendorsList:
                AppConstants.FRAGMENT_NAME = "Mandap Vendors List";
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new MandapVendorsListFragment()).commit();
                tvLocationSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_AyyapaSwamyHotels:
                AppConstants.FRAGMENT_NAME = "Ayyapa Swamy Hotels";
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new AyyapaSwamyHotelsFragment()).commit();
                tvLocationSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_Songs:
                AppConstants.FRAGMENT_NAME = "Songs";
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new SongsFragment()).commit();
                tvLocationSearch.setVisibility(View.GONE);
                break;
            case R.id.nav_Subscriptions:
                AppConstants.FRAGMENT_NAME = "My Subscriptions";
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new MySubscriptionsFragment()).commit();
                tvLocationSearch.setVisibility(View.GONE);
                break;
            case R.id.nav_ImportanceOfDeeksha:
                AppConstants.FRAGMENT_NAME = "Importance of Deeksha";
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new ImportanceofDeekshaFragment()).commit();
                tvLocationSearch.setVisibility(View.GONE);
                break;
            case R.id.nav_friends:
                AppConstants.FRAGMENT_NAME = "Friends";
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FriendFragment()).commit();
                tvLocationSearch.setVisibility(View.GONE);
                break;
            case R.id.nav_wallposts:
                AppConstants.FRAGMENT_NAME = "Wall Posts";
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new WallPostsFragment()).commit();
                tvLocationSearch.setVisibility(View.GONE);
                break;
            default:
                AppConstants.FRAGMENT_NAME = "Nearby Padi Puja Details";
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new NearbyPadiPujaDetailsFragment()).commit();
                tvLocationSearch.setVisibility(View.VISIBLE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            if (toolbar.getTitle().equals("Nearby Padi Puja Details") || AppConstants.FRAGMENT_NAME.equals("Nearby Padi Puja Details")) {
                AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NavigationDrawerActivity.this);
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
            } else {
                AppConstants.FRAGMENT_NAME = "";
                Intent intent = new Intent(NavigationDrawerActivity.this, NavigationDrawerActivity.class);
                intent.putExtra("SWAMY_VENDOR_LOGIN_STATUS", strSwamyLoginStatus);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2) {
            String message = data.getStringExtra("LOCATION");
            tvLocationSearch.setText(message);

            getLocationLatitudeLongitude(tvLocationSearch.getText().toString());
        }
    }

    private void getLocationLatitudeLongitude(String strLocation) {
        Geocoder coder = new Geocoder(NavigationDrawerActivity.this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strLocation, 5);
            Address location = address.get(0);

            AppConstants.CURRENT_LOCATION_LATITUDE = String.valueOf(location.getLatitude());
            AppConstants.CURRENT_LOCATION_LONGITUDE = String.valueOf(location.getLongitude());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
