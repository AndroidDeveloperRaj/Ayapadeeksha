package com.shiva.ayapadeeksha.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shiva.ayapadeeksha.POJOClass.Cities;
import com.shiva.ayapadeeksha.POJOClass.States;
import com.shiva.ayapadeeksha.POJOClass.TypeOfDeeksha;
import com.shiva.ayapadeeksha.POJOClass.TypesOfVendors;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import gun0912.tedbottompicker.TedBottomPicker;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button btnUpdate, btnCancel, btnHeaderName, btnDialogSubmit, btnDialogCancel;
    private ProgressBar progressBar, progressBarDialog;
    private ImageView imgProfile;
    private TextInputLayout tilDeeksha, tilEmail, tilFlatNo,
            tilLocality, tilLandMark, tilState, tilCity, tilPinCode, tilContactNumber, tilUserName;
    private TextInputEditText tieDeeksha, tieEmail, tieFlatNo,
            tieLocality, tieLandMark, tieState, tieCity, tiePinCode, tieContactNumber, tieUserName;
    private TypesOfDeekshaAdapter typesOfDeekshaAdapter;
    private String strUserId, strCurrentPhotoPath, strTypeOfDeekshaId,
            strStateId, strCityId;
    private String strDeekshaName, strEmail, strFlatNo, strLocality,
            strState, strCity, strPincode, strContactNumber, strLandMark, strUserName;
    private AlertDialog alertDialog;
    final List<String> mList = new ArrayList<>();
    final List<String> mListIds = new ArrayList<>();
    private RecyclerView rvAddress;
    private EditText etSearch;
    private TextView tvNoResults, tvUserId;
    private ArrayList<States> statesArrayList = new ArrayList<>();
    private ArrayList<Cities> citiesArrayList = new ArrayList<>();
    private CitiesAdapter citiesAdapter;
    private StatesAdapter statesAdapter;
    private android.app.AlertDialog alertDialogBuilder;
    private long totalSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
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

    private void getUserDetails(final String strUserId) {
        if (AppUtils.isOnline(EditProfileActivity.this)) {
            AppUtils.openLaunchScreen(AppUrls.LAUNCH_SCREEN_URL, EditProfileActivity.this,
                    progressBar, strUserId, "0", new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject object = new JSONObject(result);
                                if (!object.getBoolean("error")) {
                                    //Toast.makeText(EditProfileActivity.this, object.getString("Message"),
                                           // Toast.LENGTH_LONG).show();
                                    tvUserId.setText(object.getString("id"));
                                    tieDeeksha.setText(object.getString("type"));
                                    tieEmail.setText(object.getString("email"));
                                    tieUserName.setText(object.getString("username"));
                                    tieFlatNo.setText(object.getString("flatno"));
                                    tieLocality.setText(object.getString("locality"));
                                    tieLandMark.setText(object.getString("landmark"));
                                    tieState.setText(object.getString("state"));
                                    tieCity.setText(object.getString("city"));
                                    tiePinCode.setText(object.getString("pincode"));
                                    tieContactNumber.setText(object.getString("contactno"));

                                    Glide.with(EditProfileActivity.this)
                                            .load(AppUrls.IMAGE_URL + object.getString("image"))
                                            .thumbnail(0.5f)
                                            .into(imgProfile);

                                } else {
                                    Toast.makeText(EditProfileActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            AppUtils.showSnackBar(EditProfileActivity.this);
        }
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        tilUserName = (TextInputLayout) findViewById(R.id.tilUserName);
        tilDeeksha = (TextInputLayout) findViewById(R.id.tilDeeksha);
        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        tilFlatNo = (TextInputLayout) findViewById(R.id.tilFlatNo);
        tilLocality = (TextInputLayout) findViewById(R.id.tilLocality);
        tilLandMark = (TextInputLayout) findViewById(R.id.tilLandMark);
        tilState = (TextInputLayout) findViewById(R.id.tilState);
        tilCity = (TextInputLayout) findViewById(R.id.tilCity);
        tilPinCode = (TextInputLayout) findViewById(R.id.tilPinCode);
        tilContactNumber = (TextInputLayout) findViewById(R.id.tilContactNumber);

        tvUserId = (TextView) findViewById(R.id.tvUserId);

        tieUserName = (TextInputEditText) findViewById(R.id.tieUserName);
        tieDeeksha = (TextInputEditText) findViewById(R.id.tieDeeksha);
        tieEmail = (TextInputEditText) findViewById(R.id.tieEmail);
        tieFlatNo = (TextInputEditText) findViewById(R.id.tieFlatNo);
        tieLocality = (TextInputEditText) findViewById(R.id.tieLocality);
        tieLandMark = (TextInputEditText) findViewById(R.id.tieLandMark);
        tieState = (TextInputEditText) findViewById(R.id.tieState);
        tieCity = (TextInputEditText) findViewById(R.id.tieCity);
        tiePinCode = (TextInputEditText) findViewById(R.id.tiePinCode);
        tieContactNumber = (TextInputEditText) findViewById(R.id.tieContactNumber);

        tieDeeksha.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
        tieState.setOnClickListener(this);
        tieCity.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("My Profile");
        init();
        strTypeOfDeekshaId = AppConstants.TYPE_OF_DEEKSHA_ID;
        strUserId = getIntent().getStringExtra("USER_ID");
        AppConstants.USER_ID = strUserId;
        getUserDetails(strUserId);
    }

    @Override
    public void onClick(View v) {
        strUserName = tieUserName.getText().toString();
        strDeekshaName = tieDeeksha.getText().toString();
        strEmail = tieEmail.getText().toString();
        strFlatNo = tieFlatNo.getText().toString();
        strLocality = tieLocality.getText().toString();
        strState = tieState.getText().toString();
        strCity = tieCity.getText().toString();
        strPincode = tiePinCode.getText().toString();
        strContactNumber = tieContactNumber.getText().toString();
        strLandMark = tieLandMark.getText().toString();
        switch (v.getId()) {
            case R.id.tieDeeksha:
                openTypeOfDeekshaDialog();
                break;
            case R.id.imgProfile:
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
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
            case R.id.btnUpdate:
                if (strDeekshaName.isEmpty())
                    tilDeeksha.setError("Type Of Deeksha Required");
                else if (strUserName.isEmpty())
                    tilUserName.setError("Enter your Name");
                /*else if (strCurrentPhotoPath == null)
                    Toast.makeText(EditProfileActivity.this, "Please add Your Profile Image", Toast.LENGTH_LONG).show();*/
                else if (strEmail.isEmpty())
                    tilEmail.setError("Email is Required");
                else if (!AppUtils.isEmailVerified(strEmail))
                    tilEmail.setError("Please Enter Valid Email");
                else if (strFlatNo.isEmpty())
                    tilFlatNo.setError("Flat Number / Building Name is Required");
                else if (strLocality.isEmpty())
                    tilLocality.setError("Locality / Area Name / Street Name Required ");
                else if (strState.isEmpty())
                    tilState.setError("State is Required");
                else if (strCity.isEmpty())
                    tilCity.setError("City is Required");
                else if (strPincode.isEmpty())
                    tilPinCode.setError("PinCode Required");
                else if (strContactNumber.isEmpty())
                    tilContactNumber.setError("Contact Number Required");
                else
                    new UpdateUser().execute();
                break;
        }

    }

    /*================================ Dialog type Of Deeksha =============================*/
    private void openTypeOfDeekshaDialog() {
        final AlertDialog.Builder typeOfDeekshaAlertDialog = new AlertDialog.Builder(EditProfileActivity.this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View deekshaDialogView = layoutInflater.inflate(R.layout.dialog_types_of_deeksha, null);
        typeOfDeekshaAlertDialog.setView(deekshaDialogView);
        typeOfDeekshaAlertDialog.setTitle("Type Of deeksha");

        final RecyclerView rVTypesOfDeeksha = (RecyclerView) deekshaDialogView.findViewById(R.id.rVTypesOfDeeksha);
        final ProgressBar progressBar = (ProgressBar) deekshaDialogView.findViewById(R.id.progressBar);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rVTypesOfDeeksha.setLayoutManager(linearLayoutManager);

        final ArrayList<TypeOfDeeksha> typeOfDeekshaArrayList = new ArrayList<>();
        if (AppUtils.isOnline(EditProfileActivity.this)) {
            AppUtils.getTypeOfDeeksha(AppUrls.TYPE_OF_DEEKSHA_URL, EditProfileActivity.this,
                    progressBar, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (response.getString("error").equals("false")) {
                                    JSONArray jsonArray = response.getJSONArray("Deeksha Types");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        TypeOfDeeksha typeOfDeeksha = new TypeOfDeeksha();
                                        typeOfDeeksha.setStrId(jsonObject.getString("id"));
                                        typeOfDeeksha.setStrName(jsonObject.getString("name"));

                                        typeOfDeekshaArrayList.add(typeOfDeeksha);
                                        typesOfDeekshaAdapter = new TypesOfDeekshaAdapter(EditProfileActivity.this, typeOfDeekshaArrayList);
                                        rVTypesOfDeeksha.setAdapter(typesOfDeekshaAdapter);
                                        typesOfDeekshaAdapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            AppUtils.showSnackBar(EditProfileActivity.this);
        }
        typeOfDeekshaAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog = typeOfDeekshaAlertDialog.create();
        alertDialog.show();
    }

    /*=========================== Type Of Deeksha Adapter ==============================*/
    public class TypesOfDeekshaAdapter extends RecyclerView.Adapter<TypesOfDeekshaAdapter.MyViewHolder> {
        private Activity activity;
        private List<TypeOfDeeksha> typeOfDeekshasList;

        public TypesOfDeekshaAdapter(Activity activity, List<TypeOfDeeksha> typeOfDeekshasList) {
            this.activity = activity;
            this.typeOfDeekshasList = typeOfDeekshasList;
        }

        @Override
        public TypesOfDeekshaAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.list_item_type_of_deeksha, viewGroup, false);

            return new TypesOfDeekshaAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TypesOfDeekshaAdapter.MyViewHolder holder, int i) {
            final TypeOfDeeksha typeOfDeeksha = typeOfDeekshasList.get(i);
            holder.tvId.setText(typeOfDeeksha.getStrId());
            holder.tvTypeOfDeekshaName.setText(typeOfDeeksha.getStrName());
        }

        @Override
        public int getItemCount() {
            return typeOfDeekshasList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvId, tvTypeOfDeekshaName;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvId = (TextView) itemView.findViewById(R.id.tvId);
                tvTypeOfDeekshaName = (TextView) itemView.findViewById(R.id.tvTypeOfDeekshaName);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        strTypeOfDeekshaId = tvId.getText().toString();
                        tieDeeksha.setText(tvTypeOfDeekshaName.getText().toString());
                        alertDialog.dismiss();
                    }
                });
            }
        }
    }

    /*============================== Address Dialog =============================*/
    private void openAddressDialog(final String strTitle) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditProfileActivity.this);
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

    /* ============================= Fetching States ================================ */
    private void getStates(String strStatesUrl) {
        if (AppUtils.isOnline(EditProfileActivity.this)) {

            AppUtils.getStates(strStatesUrl, EditProfileActivity.this, progressBarDialog, new VolleyCallback() {
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

                                statesAdapter = new StatesAdapter(EditProfileActivity.this, statesArrayList);
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
            AppUtils.showSnackBar(EditProfileActivity.this);
        }
    }

    /*=============================== Fetching Cities ==================================*/
    private void getCities(String citiesListUrl) {
        if (AppUtils.isOnline(EditProfileActivity.this)) {

            AppUtils.getCities(citiesListUrl, EditProfileActivity.this, progressBar, strStateId, new VolleyCallback() {
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

                                citiesAdapter = new CitiesAdapter(EditProfileActivity.this, citiesArrayList);
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
            AppUtils.showSnackBar(EditProfileActivity.this);
        }
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

    /*========================== askPermissions ================================*/
    private void askPermissions(final String[] permissions) {
        AppUtils.checkPermission(EditProfileActivity.this, permissions, new GrantPermissions() {
            @Override
            public void onGranted(Boolean permissionStatus) {
                if (permissionStatus)
                    openTedBottomPicker();
                else
                    askPermissions(permissions);
            }
        });
    }

    /*============================ TedBottom Picker for images =================================*/
    private void openTedBottomPicker() {
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(EditProfileActivity.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        // here is selected uri
                        strCurrentPhotoPath = uri.getPath();
                        Glide.with(EditProfileActivity.this)
                                .load(strCurrentPhotoPath)
                                .thumbnail(0.5f)
                                .into(imgProfile);
                    }
                })
                .create();

        tedBottomPicker.show(getSupportFragmentManager());
    }

    /*============================= New User Registration Adapter ==========================*/
    private class UpdateUser extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return upLoadFile();
        }

        private String upLoadFile() {
            String responseString = null;

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(AppUrls.PROFILE_UPDATE_URL);

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
                entity.addPart("id", new StringBody(strUserId));
                entity.addPart("username", new StringBody(strUserName));
                entity.addPart("type", new StringBody(strTypeOfDeekshaId));
                entity.addPart("email", new StringBody(strEmail));
                entity.addPart("flat_no", new StringBody(strFlatNo));
                entity.addPart("city", new StringBody(strCity));
                entity.addPart("state", new StringBody(strState));
                entity.addPart("locality", new StringBody(strLocality));
                entity.addPart("pincode", new StringBody(strPincode));
                entity.addPart("landmark", new StringBody(strLandMark));
                entity.addPart("contact_no", new StringBody(strContactNumber));

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
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(EditProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                    getUserDetails(strUserId);

                } else if (jsonObject.getBoolean("error")) {
                    Toast.makeText(EditProfileActivity.this, jsonObject.getString("error_msg"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
