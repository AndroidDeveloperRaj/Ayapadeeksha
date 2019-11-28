package com.shiva.ayapadeeksha.Activities;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppConstants;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.CurrentLocation;
import com.shiva.ayapadeeksha.Utils.GrantPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private CardView cvCurrentLocation;
    private AutoCompleteTextView aCTVLocation;
    private String strLocation, strLatitude, strLongitude, strLoginStatus;
    private int MY_REQUEST_ID = 1;
    private Geocoder geocoder;
    private List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNavigationActivity();
            }
        });
    }

    private void startNavigationActivity() {
        Intent intent = new Intent(LocationActivity.this, NavigationDrawerActivity.class);
        intent.putExtra("SWAMY_VENDOR_LOGIN_STATUS", strLoginStatus);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void init() {
        cvCurrentLocation = (CardView) findViewById(R.id.cvCurrentLocation);
        aCTVLocation = (AutoCompleteTextView) findViewById(R.id.aCTVLocation);

        cvCurrentLocation.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startNavigationActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvCurrentLocation:
                String[] permissions = {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                };
                        askPermissions(permissions);
                break;
        }
    }

    private void askPermissions(final String[] permissions) {
        AppUtils.checkPermission(LocationActivity.this, permissions, new GrantPermissions() {
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
        AppUtils.getCurrentLocation(LocationActivity.this, new CurrentLocation() {
            @Override
            public void onLocationFeched(Geocoder geocoder, Location location) {
                geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    AppConstants.CURRENT_LOCATION_LATITUDE = String.valueOf(location.getLatitude());
                    AppConstants.CURRENT_LOCATION_LONGITUDE = String.valueOf(location.getLongitude());
                    AppConstants.LOCALITY = addresses.get(0).getAddressLine(0);

                    Intent intent = new Intent();
                    intent.putExtra("LOCATION", addresses.get(0).getAddressLine(0));
                    setResult(2, intent);
                    finish();//finishing activity

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("Enter your Location");
        init();
        strLoginStatus = getIntent().getStringExtra("LOGIN_STATUS");
        aCTVLocation.setThreshold(1);
        aCTVLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new AreaLocationTask().execute(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        aCTVLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strLocation = aCTVLocation.getText().toString();
                AppConstants.CURRENT_LOCALITY = strLocation;
                Intent intent = new Intent();
                intent.putExtra("LOCATION", strLocation);
                setResult(2, intent);
                finish();//finishing activity
            }
        });
    }

    /*=========================== Async Task for Location =============================*/
    private class AreaLocationTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=" + getResources().getString(R.string.google_maps_key);

            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input + "&" + types + "&" + sensor + "&" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                // Fetching the data from we service
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject response = new JSONObject(result);
                JSONArray jsonArray = response.getJSONArray("predictions");

                List<String> stringArrayList = new ArrayList<String>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String name = jsonObject.getString("description");
                    stringArrayList.add(name);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(LocationActivity.this,
                            android.R.layout.select_dialog_item, stringArrayList);
                    aCTVLocation.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

          /*  // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);*/
        }
    }

    /*A method to download json data from url*/

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("error", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
