package com.shiva.ayapadeeksha.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shiva.ayapadeeksha.Fragments.NearbyPadiPujaDetailsFragment;
import com.shiva.ayapadeeksha.POJOClass.Cities;
import com.shiva.ayapadeeksha.POJOClass.States;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AndroidMultiPartEntity;
import com.shiva.ayapadeeksha.Utils.AppConstants;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.GrantPermissions;
import com.shiva.ayapadeeksha.Utils.VolleyCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import gun0912.tedbottompicker.TedBottomPicker;

public class AddNewPadiPujaActivity extends AppCompatActivity
        implements View.OnClickListener {
    private Toolbar toolbar;
    private Button btnSubmit, btnCancel, btnHeaderName;
    private ProgressBar progressBar, progressBarDialog;
    private EditText etSearch;
    private TextView tvNoResults;
    private RecyclerView rvAddress;
    private StatesAdapter statesAdapter;
    private CitiesAdapter citiesAdapter;
    private ArrayList<States> statesArrayList = new ArrayList<>();
    private ArrayList<Cities> citiesArrayList = new ArrayList<>();
    private int intYear, intMonth, intDay, intHour, intMinute;
    private android.app.AlertDialog alertDialogBuilder;
    private ImageView imgProfile;
    private TextInputLayout tilPersonName, tilEmail, tilDate, tilTime, tilCity, tilLocality,
            tilFlatNo, tilPinCode, tilState, tilLandMark, tilContactNumber;
    private TextInputEditText tiePersonName, tieEmail, tieDate, tieTime, tieCity, tieLocality,
            tieFlatNo, tiePinCode, tieState, tieLandMark, tieContactNumber;
    private String strCurrentPhotoPath, strPersonName, strEmail, strDate, strTime, strFlatNo,
            strLocality, strCity, strState, strPincode, strContactNumber, strLandMark;
    private String strStateId, strCityId, strUserId;
    private long totalSize = 0;
    private Intent intent;
   private Boolean update_value;
   private String post_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_padi_puja);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);

        tilPersonName = (TextInputLayout) findViewById(R.id.tilPersonName);
        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        tilDate = (TextInputLayout) findViewById(R.id.tilDate);
        tilTime = (TextInputLayout) findViewById(R.id.tilTime);
        tilCity = (TextInputLayout) findViewById(R.id.tilCity);
        tilLocality = (TextInputLayout) findViewById(R.id.tilLocality);
        tilFlatNo = (TextInputLayout) findViewById(R.id.tilFlatNo);
        tilPinCode = (TextInputLayout) findViewById(R.id.tilPinCode);
        tilState = (TextInputLayout) findViewById(R.id.tilState);
        tilLandMark = (TextInputLayout) findViewById(R.id.tilLandMark);
        tilContactNumber = (TextInputLayout) findViewById(R.id.tilContactNumber);

        tiePersonName = (TextInputEditText) findViewById(R.id.tiePersonName);
        tieEmail = (TextInputEditText) findViewById(R.id.tieEmail);
        tieDate = (TextInputEditText) findViewById(R.id.tieDate);
        tieTime = (TextInputEditText) findViewById(R.id.tieTime);
        tieCity = (TextInputEditText) findViewById(R.id.tieCity);
        tieLocality = (TextInputEditText) findViewById(R.id.tieLocality);
        tieFlatNo = (TextInputEditText) findViewById(R.id.tieFlatNo);
        tiePinCode = (TextInputEditText) findViewById(R.id.tiePinCode);
        tieState = (TextInputEditText) findViewById(R.id.tieState);
        tieLandMark = (TextInputEditText) findViewById(R.id.tieLandMark);
        tieContactNumber = (TextInputEditText) findViewById(R.id.tieContactNumber);

        imgProfile.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        tieCity.setOnClickListener(this);
        tieState.setOnClickListener(this);
        tieDate.setOnClickListener(this);
        tieTime.setOnClickListener(this);

        intent =getIntent();
        if(intent.hasExtra("UPDATE_PADIPUJA") && intent.hasExtra("POST_ID") ) {
            update_value= intent.getBooleanExtra("UPDATE_PADIPUJA",false);
            post_id = intent.getStringExtra("POST_ID");
            btnSubmit.setText("Update");
        } else {
            btnSubmit.setText("Submit");

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("My Profile");
        strUserId = AppConstants.USER_ID;
        init();
    }

    @Override
    public void onClick(View v) {
        strPersonName = tiePersonName.getText().toString();
        strEmail = tieEmail.getText().toString();
        strDate = tieDate.getText().toString();
        strTime = tieTime.getText().toString();
        strCity = tieCity.getText().toString();
        strLocality = tieLocality.getText().toString();
        strFlatNo = tieFlatNo.getText().toString();
        strPincode = tiePinCode.getText().toString();
        strState = tieState.getText().toString();
        strLandMark = tieLandMark.getText().toString();
        strContactNumber = tieContactNumber.getText().toString();

        switch (v.getId()) {
            case R.id.imgProfile:
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                askPermissions(permissions);
                break;
            case R.id.tieState:
                openAddressDialog("Select State");
                break;
            case R.id.tieCity:
                if (strState.isEmpty())
                    tilState.setError("Select Your State");
                else
                    openAddressDialog("Select City");
                break;
            case R.id.tieDate:
                openDatePickerDialog();
                break;
            case R.id.tieTime:
                openTimePickerDialog();
                break;
            case R.id.btnSubmit:
                if (strPersonName.isEmpty())
                    tilPersonName.setError("Person Name Required");
//                else if (strEmail.isEmpty())
//                    tilEmail.setError("Email Required");
//                else if (!AppUtils.isEmailVerified(strEmail))
//                    tilEmail.setError("Enter Valid Email");
                else if (strDate.isEmpty())
                    tilDate.setError("Date is Required");
                else if (strTime.isEmpty())
                    tilTime.setError("Time Required");
                else if (strFlatNo.isEmpty())
                    tilFlatNo.setError("Flat Number / Building Name is Required");
                else if (strLocality.isEmpty())
                    tilLocality.setError("Locality / Area Name / Street Name Required ");
                else if (strState.isEmpty())
                    tilState.setError("State Required");
                else if (strCity.isEmpty())
                    tilCity.setError("City Required");
//                else if (strPincode.isEmpty())
//                    tilPinCode.setError("Pincode Required");
                else if (strContactNumber.isEmpty())
                    tilContactNumber.setError("Contact Number Required");
                else
                    new NewAddPadiPujaDetails().execute();
                break;
        }
    }

    /* ========================== Checking Runtime Permissions for Camera and External Storage ========================= */
    private void askPermissions(final String[] permissions) {
        AppUtils.checkPermission(AddNewPadiPujaActivity.this, permissions, new GrantPermissions() {
            @Override
            public void onGranted(Boolean permissionStatus) {
                if (permissionStatus)
                    openTedBottomPicker();
                else
                    askPermissions(permissions);
            }
        });
    }

    /* ======================== Opening Ted Bottom Picker for Pick Image ============================= */
    private void openTedBottomPicker() {
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(AddNewPadiPujaActivity.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        // here is selected uri
                        strCurrentPhotoPath = uri.getPath();
                        Glide.with(AddNewPadiPujaActivity.this).load(strCurrentPhotoPath)
                                .thumbnail(0.5f)
                                .into(imgProfile);
                    }
                })
                .create();

        tedBottomPicker.show(getSupportFragmentManager());
    }


    /*============================== Address Dialog =============================*/
    private void openAddressDialog(final String strTitle) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddNewPadiPujaActivity.this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = layoutInflater.inflate(R.layout.dialog_check_address, null);
        builder.setView(dialogView);

        progressBarDialog = (ProgressBar) dialogView.findViewById(R.id.progressBarDialog);
        rvAddress = (RecyclerView) dialogView.findViewById(R.id.rvAddress);
        btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        btnHeaderName = (Button) dialogView.findViewById(R.id.btnHeaderName);
        etSearch = (EditText) dialogView.findViewById(R.id.etSearch);
        tvNoResults = (TextView) dialogView.findViewById(R.id.tvNoResults);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = etSearch.getText().toString().toLowerCase(Locale.getDefault());
                switch (strTitle) {
                    case "Select State":
                        statesAdapter.filter(text);
                        break;
                    case "Select City":
                        citiesAdapter.filter(text);
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnHeaderName.setText(strTitle);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBuilder.dismiss();
            }
        });

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvAddress.setLayoutManager(linearLayoutManager);

        switch (strTitle) {
            case "Select State":
                statesArrayList.clear();
                getStates(AppUrls.STATES_LIST_URL);
                tieCity.setText("");

                break;
            case "Select City":
                citiesArrayList.clear();
                getCities(AppUrls.CITIES_LIST_URL);
                break;
        }

        alertDialogBuilder = builder.create();
        alertDialogBuilder.show();

    }

    /* ==================== Cities Adapter ====================== */
    public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.MyViewHolder> {
        private Activity activity;
        private List<Cities> citiesList;
        private ArrayList<Cities> arraylist;

        public CitiesAdapter(Activity activity, List<Cities> citiesList) {
            this.activity = activity;
            this.citiesList = citiesList;

            arraylist = new ArrayList<Cities>();
            arraylist.addAll(citiesList);
        }

        @Override
        public CitiesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(activity).inflate(R.layout.list_item_states_cities, parent, false);
            return new CitiesAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final CitiesAdapter.MyViewHolder holder, int position) {
            final Cities cities = citiesList.get(position);
            holder.tvCityId.setText(cities.getStrCityId());
            holder.tvCityName.setText(cities.getStrCityName());
        }

        @Override
        public int getItemCount() {
            return citiesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvCityId, tvCityName;

            public MyViewHolder(View itemView) {
                super(itemView);

                tvCityId = (TextView) itemView.findViewById(R.id.tvCityId);
                tvCityName = (TextView) itemView.findViewById(R.id.tvCityName);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        strCityId = tvCityId.getText().toString();
                        tieCity.setText(tvCityName.getText().toString());
                        alertDialogBuilder.dismiss();
                    }
                });

            }
        }

        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            citiesList.clear();
            if (charText.length() == 0) {
                citiesList.addAll(arraylist);
            } else {
                for (Cities wp : arraylist) {
                    if (wp.getStrCityName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        citiesList.add(wp);
                    } else {
                        tvNoResults.setVisibility(View.VISIBLE);
                        tvNoResults.setText("No Results Found");
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    /* ==================== States Adapter ====================== */
    public class StatesAdapter extends RecyclerView.Adapter<StatesAdapter.MyViewHolder> {
        private Activity activity;
        private List<States> statesList;
        private ArrayList<States> arraylist;

        public StatesAdapter(Activity activity, List<States> statesList) {
            this.activity = activity;
            this.statesList = statesList;

            arraylist = new ArrayList<States>();
            arraylist.addAll(statesList);
        }

        @Override
        public StatesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(activity).inflate(R.layout.list_item_states_cities, parent, false);
            return new StatesAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final StatesAdapter.MyViewHolder holder, int position) {
            final States states = statesList.get(position);
            holder.tvStateId.setText(states.getStrStateId());
            holder.tvStateName.setText(states.getStrStateName());
        }

        @Override
        public int getItemCount() {
            return statesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvStateId, tvStateName;

            public MyViewHolder(View itemView) {
                super(itemView);

                tvStateId = (TextView) itemView.findViewById(R.id.tvCityId);
                tvStateName = (TextView) itemView.findViewById(R.id.tvCityName);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        strStateId = tvStateId.getText().toString();
                        tieState.setText(tvStateName.getText().toString());
                        alertDialogBuilder.dismiss();
                    }
                });

            }
        }

        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            statesList.clear();
            if (charText.length() == 0) {
                statesList.addAll(arraylist);
            } else {
                for (States wp : arraylist) {
                    if (wp.getStrStateName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        statesList.add(wp);
                    } else {
                        tvNoResults.setVisibility(View.VISIBLE);
                        tvNoResults.setText("No Results Found");
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    /* ============================= Fetching States ================================ */
    private void getStates(String strStatesUrl) {
        if (AppUtils.isOnline(AddNewPadiPujaActivity.this)) {

            AppUtils.getStates(strStatesUrl, AddNewPadiPujaActivity.this, progressBarDialog, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject response = new JSONObject(result);
                        if (!response.getBoolean("error")) {
                            JSONArray jsonArray = response.getJSONArray("States List");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                States states = new States();

                                states.setStrStateId(jsonObject.getString("id"));
                                states.setStrStateName(jsonObject.getString("state"));

                                statesArrayList.add(states);

                                statesAdapter = new StatesAdapter(AddNewPadiPujaActivity.this, statesArrayList);
                                rvAddress.setAdapter(statesAdapter);
                                statesAdapter.notifyDataSetChanged();
                            }
                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            AppUtils.showSnackBar(AddNewPadiPujaActivity.this);
        }
    }

    /*=============================== Fetching Cities ==================================*/
    private void getCities(String citiesListUrl) {
        if (AppUtils.isOnline(AddNewPadiPujaActivity.this)) {

            AppUtils.getCities(citiesListUrl, AddNewPadiPujaActivity.this, progressBar, strStateId, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject response = new JSONObject(result);
                        if (!response.getBoolean("error")) {
                            JSONArray jsonArray = response.getJSONArray("Cities List");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                Cities cities = new Cities();
                                cities.setStrCityId(jsonObject.getString("id"));
                                cities.setStrCityName(jsonObject.getString("city_name"));
                                cities.setStrStateName(jsonObject.getString("state_id"));

                                citiesArrayList.add(cities);

                                citiesAdapter = new CitiesAdapter(AddNewPadiPujaActivity.this, citiesArrayList);
                                rvAddress.setAdapter(citiesAdapter);
                                citiesAdapter.notifyDataSetChanged();

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            AppUtils.showSnackBar(AddNewPadiPujaActivity.this);
        }
    }

    /*============================== Date Of Birth Dialog ================================*/
    private void openDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        intYear = c.get(Calendar.YEAR);
        intMonth = c.get(Calendar.MONTH);
        intDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        tieDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, intYear, intMonth, intDay);
        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    /* ================== Opening Time Picker Dialog =========================== */
    private void openTimePickerDialog() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        intHour = c.get(Calendar.HOUR_OF_DAY);
        intMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        tieTime.setText(hourOfDay + ":" + minute);
                    }
                }, intHour, intMinute, true);
        timePickerDialog.show();
    }

    /*============================= New User Registration Adapter ==========================*/
    private class NewAddPadiPujaDetails extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return upLoadFile();
        }

        private String upLoadFile() {
            String responseString = null;

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost;
            if(update_value!=null && update_value) {

                httpPost = new HttpPost(AppUrls.PADI_PUJA_UPDATE_URL);
                try {
                    AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                        @Override
                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });
                    if (strCurrentPhotoPath != null) {
                        File sourceFile = new File(strCurrentPhotoPath);
                        entity.addPart("image", new FileBody(sourceFile));
                    }

                    entity.addPart("post_id", new StringBody(post_id));
                    entity.addPart("user_id", new StringBody(strUserId));
                    entity.addPart("name", new StringBody(strPersonName));
                    entity.addPart("email", new StringBody(strEmail));
                    entity.addPart("flat_no", new StringBody(strFlatNo));
                    entity.addPart("city", new StringBody(strCity));
                    entity.addPart("state", new StringBody(strState));
              //      entity.addPart("locality", new StringBody(strLocality));
                    entity.addPart("pincode", new StringBody(strPincode));
              //      entity.addPart("landmark", new StringBody(strLandMark));
                    entity.addPart("contact_no", new StringBody(strContactNumber));
                    entity.addPart("date", new StringBody(strDate));
                    entity.addPart("time", new StringBody(strTime));


                    totalSize = entity.getContentLength();
                    httpPost.setEntity(entity);

                    // Making Server Call
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();

                    int statusCode = httpResponse.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        //Server Response
                        responseString = EntityUtils.toString(httpEntity);
                        Log.d("responseString", responseString);
                    } else {
                        responseString = "Error Occurred.!Http status Code:" + statusCode;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                httpPost = new HttpPost(AppUrls.PADI_PUJA_DETAILS_URL);

                try {
                    AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                        @Override
                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });
                    if (strCurrentPhotoPath != null) {
                        File sourceFile = new File(strCurrentPhotoPath);
                        entity.addPart("image", new FileBody(sourceFile));
                    }

                    entity.addPart("user_id", new StringBody(strUserId));
                    entity.addPart("name", new StringBody(strPersonName));
                    entity.addPart("email", new StringBody(strEmail));
                    entity.addPart("flat_no", new StringBody(strFlatNo));
                    entity.addPart("city", new StringBody(strCity));
                    entity.addPart("state", new StringBody(strState));
                    entity.addPart("locality", new StringBody(strLocality));
                    entity.addPart("pincode", new StringBody(strPincode));
                    entity.addPart("landmark", new StringBody(strLandMark));
                    entity.addPart("contact_no", new StringBody(strContactNumber));
                    entity.addPart("date", new StringBody(strDate));
                    entity.addPart("time", new StringBody(strTime));


                    totalSize = entity.getContentLength();
                    httpPost.setEntity(entity);

                    // Making Server Call
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();

                    int statusCode = httpResponse.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        //Server Response
                        responseString = EntityUtils.toString(httpEntity);
                        Log.d("responseString", responseString);
                    } else {
                        responseString = "Error Occurred.!Http status Code:" + statusCode;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(AddNewPadiPujaActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                    AppConstants.USER_ID = jsonObject.getString("user_id");

                    Intent intent = new Intent(AddNewPadiPujaActivity.this, NavigationDrawerActivity.class);
                    intent.putExtra("SWAMY_VENDOR_LOGIN_STATUS", AppConstants.LOGIN_STATUS);
                    startActivity(intent);
                    finish();

                } else if (jsonObject.getBoolean("error")) {
                    Toast.makeText(AddNewPadiPujaActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
