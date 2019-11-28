package com.shiva.ayapadeeksha.Activities;

import android.app.Activity;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shiva.ayapadeeksha.Adapters.UpdateAdapter;
import com.shiva.ayapadeeksha.POJOClass.Cities;
import com.shiva.ayapadeeksha.POJOClass.States;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AndroidMultiPartEntity;
import com.shiva.ayapadeeksha.Utils.AppConstants;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.VolleyCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditRegisAyyappaHotelsActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private String strAyyappaHotelsId, strAyyappaHotelsEmail, strCityId, strStateId;
    private Button btnUpdate, btnCancel, btnHeaderName;
    private TextInputLayout tilEmail, tilAyyappaHotelName, tilFlatNo, tilLocality, tilLandMark,
            tilState, tilCity, tilPinCode, tilPrice;
    private TextInputEditText tieEmail, tieAyyappaHotelName, tieFlatNo, tieLocality, tieLandMark,
            tieState, tieCity, tiePinCode, tiePrice;
    private String strEmail, strAyyappaHotelName, strFlatNo, strLocality, strLandMark,
            strState, strCity, strPinCode, strPrice;
    private ProgressBar progressBar, progressBarDialog;
    private ArrayList<States> statesArrayList = new ArrayList<>();
    private ArrayList<Cities> citiesArrayList = new ArrayList<>();
    private TextView tvNoResults, tvAyyappaHotelId;
    private CitiesAdapter citiesAdapter;
    private StatesAdapter statesAdapter;
    private EditText etSearch;
    private android.app.AlertDialog alertDialogBuilder;
    private CheckBox checkBoxFree;
    private RecyclerView rvAddress;
    private long totalSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_regis_ayyappa_hotels);
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

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("Ayyappa Swamy Hotels");
        strAyyappaHotelsId = getIntent().getStringExtra("AYYAPPA_HOTELS_ID");
        strAyyappaHotelsEmail = getIntent().getStringExtra("AYYAPPA_HOTELS_EMAIL");
        init();
        tieEmail.setText(strAyyappaHotelsEmail);
        getAyyappaSwamyDetails(strAyyappaHotelsEmail);

        checkBoxFree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    tiePrice.setText("Free");
                else tiePrice.setText("");
            }
        });

    }

    private void getAyyappaSwamyDetails(String strMusicalBandEmail) {
        if (AppUtils.isOnline(EditRegisAyyappaHotelsActivity.this)) {
            AppUtils.getAyyappaSwamyDetails(AppUrls.EDIT_AYYAPPA_HOTELS_DETAILS_URL, EditRegisAyyappaHotelsActivity.this,
                    progressBar, strMusicalBandEmail, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    JSONArray jsonArray = response.getJSONArray("Ayyappa Swamy Hotel Profile");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        tvAyyappaHotelId.setText(object.getString("id"));
                                        tieAyyappaHotelName.setText(object.getString("hotel_name"));
                                        tieEmail.setText(object.getString("email"));
                                        tieFlatNo.setText(object.getString("flatno"));
                                        tieLocality.setText(object.getString("locality"));
                                        tieCity.setText(object.getString("city"));
                                        tieState.setText(object.getString("state"));
                                        tiePinCode.setText(object.getString("pincode"));
                                        tieLandMark.setText(object.getString("landmark"));

                                        if (object.getString("price").equals("Free")) {
                                            checkBoxFree.setChecked(true);
                                        } else {
                                            checkBoxFree.setChecked(false);
                                            tiePrice.setText(object.getString("price"));
                                        }
                                    }
                                } else {
                                    Toast.makeText(EditRegisAyyappaHotelsActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            AppUtils.showSnackBar(EditRegisAyyappaHotelsActivity.this);
        }
    }

    private void init() {
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        tvAyyappaHotelId = (TextView) findViewById(R.id.tvAyyappaHotelId);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        tilAyyappaHotelName = (TextInputLayout) findViewById(R.id.tilAyyappaHotelName);
        tilFlatNo = (TextInputLayout) findViewById(R.id.tilFlatNo);
        tilLocality = (TextInputLayout) findViewById(R.id.tilLocality);
        tilLandMark = (TextInputLayout) findViewById(R.id.tilLandMark);
        tilState = (TextInputLayout) findViewById(R.id.tilState);
        tilCity = (TextInputLayout) findViewById(R.id.tilCity);
        tilPinCode = (TextInputLayout) findViewById(R.id.tilPinCode);
        tilPrice = (TextInputLayout) findViewById(R.id.tilPrice);

        tieEmail = (TextInputEditText) findViewById(R.id.tieEmail);
        tieAyyappaHotelName = (TextInputEditText) findViewById(R.id.tieAyyappaHotelName);
        tieFlatNo = (TextInputEditText) findViewById(R.id.tieFlatNo);
        tieLocality = (TextInputEditText) findViewById(R.id.tieLocality);
        tieLandMark = (TextInputEditText) findViewById(R.id.tieLandMark);
        tieState = (TextInputEditText) findViewById(R.id.tieState);
        tieCity = (TextInputEditText) findViewById(R.id.tieCity);
        tiePinCode = (TextInputEditText) findViewById(R.id.tiePinCode);
        tiePrice = (TextInputEditText) findViewById(R.id.tiePrice);

        checkBoxFree = (CheckBox) findViewById(R.id.checkBoxFree);

        btnUpdate.setOnClickListener(this);
        tieState.setOnClickListener(this);
        tieCity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        strEmail = tieEmail.getText().toString();
        strAyyappaHotelName = tieAyyappaHotelName.getText().toString();
        strFlatNo = tieFlatNo.getText().toString();
        strLocality = tieLocality.getText().toString();
        strLandMark = tieLandMark.getText().toString();
        strState = tieState.getText().toString();
        strCity = tieCity.getText().toString();
        strPinCode = tiePinCode.getText().toString();
        strPrice = tiePrice.getText().toString();
        switch (v.getId()) {
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
                if (strAyyappaHotelName.isEmpty())
                    setErrorMessage(tilAyyappaHotelName, "Ayyappa Hotel Name Required");
                else if (strFlatNo.isEmpty())
                    setErrorMessage(tilFlatNo, "Flat No Required");
                else if (strLocality.isEmpty())
                    setErrorMessage(tilLocality, "Locality is Required");
                else if (strState.isEmpty())
                    setErrorMessage(tilState, "State Required");
                else if (strCity.isEmpty())
                    setErrorMessage(tilCity, "City Required");
                else if (strPinCode.isEmpty())
                    setErrorMessage(tilPinCode, "Pincode Required");
                else if (strPrice.isEmpty())
                    setErrorMessage(tilPrice, "Price Required");
                else
                    new UpdateUser().execute();
                break;
        }
    }

    /* ==================== Showing Error Message ========================== */
    private void setErrorMessage(TextInputLayout textInputLayout, String strErrorMessage) {
        textInputLayout.setError(strErrorMessage);
    }

    /*============================== Address Dialog =============================*/
    private void openAddressDialog(final String strTitle) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditRegisAyyappaHotelsActivity.this);
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
        if (AppUtils.isOnline(EditRegisAyyappaHotelsActivity.this)) {

            AppUtils.getStates(strStatesUrl, EditRegisAyyappaHotelsActivity.this, progressBarDialog, new VolleyCallback() {
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

                                statesAdapter = new StatesAdapter(EditRegisAyyappaHotelsActivity.this, statesArrayList);
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
            AppUtils.showSnackBar(EditRegisAyyappaHotelsActivity.this);
        }
    }

    /*=============================== Fetching Cities ==================================*/
    private void getCities(String citiesListUrl) {
        if (AppUtils.isOnline(EditRegisAyyappaHotelsActivity.this)) {

            AppUtils.getCities(citiesListUrl, EditRegisAyyappaHotelsActivity.this, progressBar, strStateId, new VolleyCallback() {
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

                                citiesAdapter = new CitiesAdapter(EditRegisAyyappaHotelsActivity.this, citiesArrayList);
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
            AppUtils.showSnackBar(EditRegisAyyappaHotelsActivity.this);
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
            HttpPost httpPost = new HttpPost(AppUrls.UPDATE_AYYAPPA_HOTELS_DETAILS_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });
                entity.addPart("id", new StringBody(strAyyappaHotelsId));
                entity.addPart("hotel_name", new StringBody(strAyyappaHotelName));
                entity.addPart("email", new StringBody(strEmail));
                entity.addPart("flatno", new StringBody(strFlatNo));
                entity.addPart("price", new StringBody(strPrice));
                entity.addPart("locality", new StringBody(strLocality));
                entity.addPart("city", new StringBody(strCity));
                entity.addPart("state", new StringBody(strState));
                entity.addPart("pincode", new StringBody(strPinCode));
                entity.addPart("landmark", new StringBody(strLandMark));


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
                    Toast.makeText(EditRegisAyyappaHotelsActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                    AppConstants.VENDORS_ID = strAyyappaHotelsId;
                    /*Intent intent = new Intent(EditAyyappaHotelsActivity.this, EditProfileActivity.class);
                    intent.putExtra("USER_ID", AppConstants.USER_ID);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                    finish();

                } else if (jsonObject.getBoolean("error")) {
                    Toast.makeText(EditRegisAyyappaHotelsActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
