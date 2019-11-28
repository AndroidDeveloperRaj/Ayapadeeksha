package com.shiva.ayapadeeksha.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import com.bumptech.glide.Glide;
import com.shiva.ayapadeeksha.Activities.AyyappaHotelsActivity;
import com.shiva.ayapadeeksha.Activities.GuruSwamyActivity;
import com.shiva.ayapadeeksha.Activities.MandapVendorsActivity;
import com.shiva.ayapadeeksha.Activities.MusicalBandsActivity;
import com.shiva.ayapadeeksha.Activities.NavigationDrawerActivity;
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
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;


public class VendorRegistrationFragment extends Fragment implements View.OnClickListener {

    private ViewGroup root;
    private Toolbar toolbar;
    private Button btnRegister, btnCancel, btnHeaderName, btnDialogCancel, btnDialogSubmit,
            btnVerifyOtp, btnCancelOtp, btnCheck;
    private CoordinatorLayout coordinatorLayout;
    private ProgressBar progressBar, progressBarDialog, progressBarOtpVerification;
    private TextInputLayout tilDeeksha, tilEmail, tilPassword, tilConfirmPassword, tilTypesOfVendors,
            tilDatePicker, tilCity, tilLocality, tilFlatNo, tilPinCode, tilState, tilLandMark,
            tilContactNumber, tilOtp, tilUserName;
    private TextInputEditText tieDeeksha, tieEmail, tiePassword, tieConfirmPassword, tieTypesOfVendors,
            tieDatePicker, tieCity, tieLocality, tieFlatNo, tiePinCode, tieState, tieLandMark,
            tieContactNumber, tieOtp, tieUserName;
    private CircleImageView imgProfile;
    private String strTypeOfDeekshaId, strTypeOfVendorsId, strCityId, strStateId, strOTP,
            strUserId, strOTPStatus, strLaunchScreenId, strUserName, strVendorLoginStatus;
    private TypesOfDeekshaAdapter typesOfDeekshaAdapter;
    private TypesOfVendorsAdapter typesOfVendorsAdapter;
    private String strDeekshaName, strCurrentPhotoPath, strEmail, strPassword, strConfirmPassword,
            strTypeOfVendors, strFlatNo, strLocality, strCity, strState, strPincode,
            strContactNumber, strLandMark;
    private AlertDialog alertDialog, alertDialogOTPVerification;
    private android.app.AlertDialog alertDialogBuilder;
    private ArrayList<States> statesArrayList = new ArrayList<>();
    private ArrayList<Cities> citiesArrayList = new ArrayList<>();
    private RecyclerView rvAddress;
    private EditText etSearch;
    private TextView tvNoResults;
    private CitiesAdapter citiesAdapter;
    private StatesAdapter statesAdapter;
    private long totalSize = 0;
    final List<String> mList = new ArrayList<>();
    final List<String> mListIds = new ArrayList<>();

    public VendorRegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = (ViewGroup) inflater.inflate(R.layout.fragment_vendor_registration, container, false);
        getActivity().setTitle("Registration");
        init();
        return root;
    }

    private void init() {
        coordinatorLayout = (CoordinatorLayout) root.findViewById(R.id.coordinatorLayout);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        btnCheck = (Button) root.findViewById(R.id.btnCheck);

        tilUserName = (TextInputLayout) root.findViewById(R.id.tilUserName);
        tilDeeksha = (TextInputLayout) root.findViewById(R.id.tilDeeksha);
        tilEmail = (TextInputLayout) root.findViewById(R.id.tilEmail);
        tilPassword = (TextInputLayout) root.findViewById(R.id.tilPassword);
        tilConfirmPassword = (TextInputLayout) root.findViewById(R.id.tilConfirmPassword);
        tilTypesOfVendors = (TextInputLayout) root.findViewById(R.id.tilTypesOfVendors);
        tilCity = (TextInputLayout) root.findViewById(R.id.tilCity);
        tilLocality = (TextInputLayout) root.findViewById(R.id.tilLocality);
        tilFlatNo = (TextInputLayout) root.findViewById(R.id.tilFlatNo);
        tilPinCode = (TextInputLayout) root.findViewById(R.id.tilPinCode);
        tilState = (TextInputLayout) root.findViewById(R.id.tilState);
        tilLandMark = (TextInputLayout) root.findViewById(R.id.tilLandMark);
        tilContactNumber = (TextInputLayout) root.findViewById(R.id.tilContactNumber);

        tieUserName = (TextInputEditText) root.findViewById(R.id.tieUserName);
        tieDeeksha = (TextInputEditText) root.findViewById(R.id.tieDeeksha);
        tieEmail = (TextInputEditText) root.findViewById(R.id.tieEmail);
        tiePassword = (TextInputEditText) root.findViewById(R.id.tiePassword);
        tieConfirmPassword = (TextInputEditText) root.findViewById(R.id.tieConfirmPassword);
        tieTypesOfVendors = (TextInputEditText) root.findViewById(R.id.tieTypesOfVendors);
        tieCity = (TextInputEditText) root.findViewById(R.id.tieCity);
        tieLocality = (TextInputEditText) root.findViewById(R.id.tieLocality);
        tieFlatNo = (TextInputEditText) root.findViewById(R.id.tieFlatNo);
        tiePinCode = (TextInputEditText) root.findViewById(R.id.tiePinCode);
        tieState = (TextInputEditText) root.findViewById(R.id.tieState);
        tieLandMark = (TextInputEditText) root.findViewById(R.id.tieLandMark);
        tieContactNumber = (TextInputEditText) root.findViewById(R.id.tieContactNumber);

        imgProfile = (CircleImageView) root.findViewById(R.id.imgProfile);
        btnRegister = (Button) root.findViewById(R.id.btnRegister);

        tieDeeksha.setOnClickListener(this);
        btnCheck.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
        tieTypesOfVendors.setOnClickListener(this);
        tieState.setOnClickListener(this);
        tieCity.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        strUserName = tieUserName.getText().toString();
        strDeekshaName = tieDeeksha.getText().toString();
        strEmail = tieEmail.getText().toString();
        strPassword = tiePassword.getText().toString();
        strConfirmPassword = tieConfirmPassword.getText().toString();
        strTypeOfVendors = tieTypesOfVendors.getText().toString();
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
            case R.id.btnCheck:
                strContactNumber = tieContactNumber.getText().toString();
                if (strContactNumber.isEmpty())
                    tilContactNumber.setError("Contact Number Required");
                else {
                    sendRegistrationOTP(strContactNumber);
                }
                break;
            case R.id.imgProfile:
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                askPermissions(permissions);
                break;
            case R.id.tieTypesOfVendors:
                if (strEmail.isEmpty())
                    tilEmail.setError("Email Required");
                else if (!AppUtils.isEmailVerified(strEmail))
                    tilEmail.setError("Please Enter Valid Email");
                else
                    openTypeOfVendorsDialog();
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
            case R.id.btnRegister:
                if (strUserName.isEmpty())
                    tilUserName.setError("Enter your Name");
                else if (strDeekshaName.isEmpty())
                    tilDeeksha.setError("Type Of Deeksha Required");
                else if (strCurrentPhotoPath == null)
                    Toast.makeText(getActivity(), "Please add Your Profile Image", Toast.LENGTH_LONG).show();
                else if (strEmail.isEmpty())
                    tilEmail.setError("Email is Required");
                else if (!AppUtils.isEmailVerified(strEmail))
                    tilEmail.setError("Please Enter Valid Email");
                else if (strPassword.isEmpty())
                    tilPassword.setError("Password Required");
                else if (strConfirmPassword.isEmpty())
                    tilConfirmPassword.setError("Confirm Password Required");
                else if (!strPassword.equals(strConfirmPassword))
                    Toast.makeText(getActivity(), "Passwords does not Matched", Toast.LENGTH_LONG).show();
                else if (strTypeOfVendors.isEmpty())
                    tilTypesOfVendors.setError("Please select from above Preferences");
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
                else if (strOTPStatus == null || !strOTPStatus.equals("Verified"))
                    tilContactNumber.setError("Verify your contact number by clicking check");
                else
                    new RegisterNewUser().execute();
                break;
        }
    }

    private void sendRegistrationOTP(final String strContactNumber) {
        if (AppUtils.isOnline(getActivity())) {
            AppUtils.sendRegistrationOTP(AppUrls.MOBILE_VERIFICATION_URL, getActivity(),
                    strContactNumber, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    Toast.makeText(getActivity(), response.getString("Message"),
                                            Toast.LENGTH_LONG).show();
                                    openOtpVerificationDialog(strContactNumber);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    /*============================== Address Dialog =============================*/
    private void openAddressDialog(final String strTitle) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
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
        if (AppUtils.isOnline(getActivity())) {

            AppUtils.getStates(strStatesUrl, getActivity(), progressBarDialog, new VolleyCallback() {
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

                                statesAdapter = new StatesAdapter(getActivity(), statesArrayList);
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
            AppUtils.showSnackBar(getActivity());
        }
    }

    /*=============================== Fetching Cities ==================================*/
    private void getCities(String citiesListUrl) {
        if (AppUtils.isOnline(getActivity())) {

            AppUtils.getCities(citiesListUrl, getActivity(), progressBar, strStateId, new VolleyCallback() {
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

                                citiesAdapter = new CitiesAdapter(getActivity(), citiesArrayList);
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
            AppUtils.showSnackBar(getActivity());
        }
    }

    /*================================ Dialog type of vendors ==============================*/
    private void openTypeOfVendorsDialog() {
        mList.clear();
        mListIds.clear();
        final AlertDialog.Builder typeOfVendorsAlertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vendorsDialogView = layoutInflater.inflate(R.layout.dialog_types_of_vendors, null);
        typeOfVendorsAlertDialog.setView(vendorsDialogView);
        typeOfVendorsAlertDialog.setCancelable(false);
        typeOfVendorsAlertDialog.setTitle("Type Of Vendors");

        final RecyclerView rvVendorsList = (RecyclerView) vendorsDialogView.findViewById(R.id.rvVendorsList);
        final ProgressBar progressBar = (ProgressBar) vendorsDialogView.findViewById(R.id.progressBar);
        btnDialogCancel = (Button) vendorsDialogView.findViewById(R.id.btnDialogCancel);
        btnDialogSubmit = (Button) vendorsDialogView.findViewById(R.id.btnDialogSubmit);

        final ArrayList<TypesOfVendors> typesOfVendorsArrayList = new ArrayList<>();

        rvVendorsList.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvVendorsList.setLayoutManager(linearLayoutManager);

        if (AppUtils.isOnline(getActivity())) {
            AppUtils.getTypeOfVendors(AppUrls.VENDORS_LIST_URL, getActivity(),
                    progressBar, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    JSONArray jsonArray = response.getJSONArray("Vendor Types");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        TypesOfVendors typesOfVendors = new TypesOfVendors();
                                        typesOfVendors.setStrId(jsonObject.getString("id"));
                                        typesOfVendors.setStrName(jsonObject.getString("name"));

                                        typesOfVendorsArrayList.add(typesOfVendors);
                                        typesOfVendorsAdapter = new TypesOfVendorsAdapter(getActivity(), typesOfVendorsArrayList);
                                        rvVendorsList.setAdapter(typesOfVendorsAdapter);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            AppUtils.showSnackBar(getActivity());
        }
        btnDialogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.isEmpty()) {
                    alertDialog.show();
                    Toast.makeText(getActivity(), "Nothing is Selected", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(RegistrationActivity.this, String.valueOf(mList), Toast.LENGTH_LONG).show();
                    //int[] array=String.valueOf(mList);

                    tieTypesOfVendors.setText(Arrays.toString(new String[]{String.valueOf(mList)})
                            .replace("[", "").replace("]", ""));

                    //Toast.makeText(RegistrationActivity.this, String.valueOf(mListIds), Toast.LENGTH_LONG).show();

                    strTypeOfVendorsId = Arrays.toString(new String[]{String.valueOf(mListIds)})
                            .replace("[", "").replace("]", "");

                    Log.d("SelectedVendorNamesList", String.valueOf(mList));
                    Log.d("SelectedVendorIdsList", String.valueOf(mListIds));
                    alertDialog.dismiss();
                }
            }
        });
        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        alertDialog = typeOfVendorsAlertDialog.create();
        alertDialog.show();
    }

    /* ========================== Checking Runtime Permissions for Camera and External Storage ========================= */
    private void askPermissions(final String[] permissions) {
        AppUtils.checkPermission(getActivity(), permissions, new GrantPermissions() {
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
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(getActivity())
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        // here is selected uri
                        strCurrentPhotoPath = uri.getPath();
                        Glide.with(getActivity()).load(strCurrentPhotoPath)
                                .thumbnail(0.5f)
                                .into(imgProfile);
                    }
                })
                .create();

        tedBottomPicker.show(getActivity().getSupportFragmentManager());
    }

    /*================================ Dialog type Of Deeksha =============================*/
    private void openTypeOfDeekshaDialog() {
        final AlertDialog.Builder typeOfDeekshaAlertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View deekshaDialogView = layoutInflater.inflate(R.layout.dialog_types_of_deeksha, null);
        typeOfDeekshaAlertDialog.setView(deekshaDialogView);
        typeOfDeekshaAlertDialog.setTitle("Type Of deeksha");

        final RecyclerView rVTypesOfDeeksha = (RecyclerView) deekshaDialogView.findViewById(R.id.rVTypesOfDeeksha);
        final ProgressBar progressBar = (ProgressBar) deekshaDialogView.findViewById(R.id.progressBar);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rVTypesOfDeeksha.setLayoutManager(linearLayoutManager);

        final ArrayList<TypeOfDeeksha> typeOfDeekshaArrayList = new ArrayList<>();
        if (AppUtils.isOnline(getActivity())) {
            AppUtils.getTypeOfDeeksha(AppUrls.TYPE_OF_DEEKSHA_URL, getActivity(),
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
                                        typesOfDeekshaAdapter = new TypesOfDeekshaAdapter(getActivity(), typeOfDeekshaArrayList);
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
            AppUtils.showSnackBar(getActivity());
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

    /*=========================== Type Of Vendor Adapter ==============================*/
    class TypesOfVendorsAdapter extends RecyclerView.Adapter<TypesOfVendorsAdapter.MyViewHolder> {
        private Activity activity;
        private ArrayList<TypesOfVendors> typesOfVendorsList;

        public TypesOfVendorsAdapter(Activity activity, ArrayList<TypesOfVendors> typesOfVendorsList) {
            this.activity = activity;
            this.typesOfVendorsList = typesOfVendorsList;
        }

        @Override
        public TypesOfVendorsAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.list_type_of_vendors, viewGroup, false);
            return new TypesOfVendorsAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final TypesOfVendorsAdapter.MyViewHolder holder, final int i) {
            final TypesOfVendors typesOfVendors = typesOfVendorsList.get(i);

            holder.tvVendorId.setText(typesOfVendors.getStrId());
            holder.tvTypeOfVendorName.setText(typesOfVendors.getStrName());

            if (typesOfVendors.isChecked()) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }

        }

        @Override
        public int getItemCount() {
            return typesOfVendorsList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvVendorId, tvTypeOfVendorName;
            public CheckBox checkBox;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvVendorId = (TextView) itemView.findViewById(R.id.tvVendorId);
                tvTypeOfVendorName = (TextView) itemView.findViewById(R.id.tvTypeOfVendorName);
                checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                           /* Toast.makeText(VendorsAdapter.this.activity,
                                    "selected brand is " + tvVendorName.getText(),
                                    Toast.LENGTH_LONG).show();
*/
                            mList.add(tvTypeOfVendorName.getText().toString());
                            mListIds.add(tvVendorId.getText().toString());
                            if (tvVendorId.getText().toString().equals("1")) {
                                Intent intent = new Intent(getContext(), GuruSwamyActivity.class);
                                intent.putExtra("GURU_SWAMY_ID", tvVendorId.getText().toString());
                                intent.putExtra("GURU_SWAMY_EMAIL", strEmail);
                                startActivity(intent);
                            }
                            if (tvVendorId.getText().toString().equals("2")) {
                                Intent intent = new Intent(getContext(), MusicalBandsActivity.class);
                                intent.putExtra("MUSICAL_BAND_ID", tvVendorId.getText().toString());
                                intent.putExtra("MUSICAL_BAND_EMAIL", strEmail);
                                startActivity(intent);
                            }
                            if (tvVendorId.getText().toString().equals("3")) {
                                Intent intent = new Intent(getContext(), MandapVendorsActivity.class);
                                intent.putExtra("MANDAP_VENDORS_ID", tvVendorId.getText().toString());
                                intent.putExtra("MANDAP_VENDORS_EMAIL", strEmail);
                                startActivity(intent);
                            }
                            if (tvVendorId.getText().toString().equals("4")) {
                                Intent intent = new Intent(getContext(), AyyappaHotelsActivity.class);
                                intent.putExtra("AYYAPPA_HOTELS_ID", tvVendorId.getText().toString());
                                intent.putExtra("AYYAPPA_HOTELS_EMAIL", strEmail);
                                startActivity(intent);
                            }

                        } else {
                           /* Toast.makeText(VendorsAdapter.this.activity,
                                    "Unselected brand is " + tvVendorName.getText(),
                                    Toast.LENGTH_LONG).show();
*/

                            mList.remove(tvTypeOfVendorName.getText().toString());
                            mListIds.remove(tvVendorId.getText().toString());
                        }
                    }
                });
            }
        }
    }

   /* @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Registration");
        init();
    }*/

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

    /*============================= New User Registration Adapter ==========================*/
    private class RegisterNewUser extends AsyncTask<Void, Integer, String> {

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
            HttpPost httpPost = new HttpPost(AppUrls.VENDOR_REGISTER_URL);

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
                entity.addPart("username", new StringBody(strUserName));
                entity.addPart("type", new StringBody(strTypeOfDeekshaId));
                entity.addPart("email", new StringBody(strEmail));
                entity.addPart("password", new StringBody(strPassword));
                entity.addPart("confirm_pswd", new StringBody(strConfirmPassword));
                entity.addPart("flat_no", new StringBody(strFlatNo));
                entity.addPart("city", new StringBody(strCity));
                entity.addPart("state", new StringBody(strState));
                entity.addPart("locality", new StringBody(strLocality));
                entity.addPart("pincode", new StringBody(strPincode));
                entity.addPart("landmark", new StringBody(strLandMark));
                entity.addPart("contact_no", new StringBody(strContactNumber));
                entity.addPart("vendors", new StringBody(strTypeOfVendorsId));
                entity.addPart("otp_status", new StringBody(strOTPStatus));

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
                JSONObject response = new JSONObject(s);
                if (!response.getBoolean("error")) {
                    Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();

                    AppConstants.USER_ID = response.getString("id");
                    AppConstants.USER_NAME = response.getString("username");
                    AppConstants.EMAIL = response.getString("email");
                    AppConstants.TYPE_OF_DEEKSHA = response.getString("type");
                    AppConstants.TYPE_OF_DEEKSHA_ID = response.getString("type_id");
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
                    Intent intent = new Intent(getActivity(), NavigationDrawerActivity.class);
                    intent.putExtra("SWAMY_VENDOR_LOGIN_STATUS", strVendorLoginStatus);
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences
                            ("LoginDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("LaunchScreenId", strLaunchScreenId);
                    // editor.putString("LoginContactNumber", strContactNumber);
                    //editor.putString("LoginPassword", strPassword);

                    editor.commit();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();

                } else if (response.getBoolean("error")) {
                    Toast.makeText(getActivity(), response.getString("error_msg"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /* ======================= Opening OTP Verification Dialog ============================== */
    private void openOtpVerificationDialog(final String strContactNumber) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    sendOTP(strContactNumber, strOTP);
                }

            }
        });
        alertDialogOTPVerification = builder.create();
        alertDialogOTPVerification.show();
    }

    /* ========================== Sending OTP to mMobile Number ========================= */
    private void sendOTP(String strContactNumber, String strOTP) {
        if (AppUtils.isOnline(getActivity())) {
            AppUtils.sendContactOTP(AppUrls.OTP_SEND_URL, progressBar, getActivity(), strContactNumber,
                    strOTP, "0", new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (!jsonObject.getBoolean("error")) {
                                    Toast.makeText(getActivity(), jsonObject.getString("Message"), Toast.LENGTH_LONG).show();
                                    strOTPStatus = jsonObject.getString("Status");
                                    alertDialogOTPVerification.dismiss();

                                } else {
                                    Toast.makeText(getActivity(), jsonObject.getString("error_msg"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            AppUtils.showSnackBar(getActivity());
        }
    }
}
