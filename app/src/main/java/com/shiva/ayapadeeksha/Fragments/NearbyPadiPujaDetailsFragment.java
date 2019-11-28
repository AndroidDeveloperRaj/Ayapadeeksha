package com.shiva.ayapadeeksha.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shiva.ayapadeeksha.Activities.AddNewPadiPujaActivity;
import com.shiva.ayapadeeksha.Adapters.NearByPadiPujaDetailsAdapter;
import com.shiva.ayapadeeksha.POJOClass.NearByPadiPujaDetailsPOJOClass;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppConstants;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class NearbyPadiPujaDetailsFragment extends
        Fragment implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    private ViewGroup root;
    private FloatingActionButton fabAddPadiPuja;
    private RecyclerView rvNearByPadiPujaDetails;
    private ProgressBar progressBar;
    private TextView tvEmptyText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private NearByPadiPujaDetailsAdapter nearByPadiPujaDetailsAdapter;
    private EditText etSearch;


    public NearbyPadiPujaDetailsFragment() {
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
        root = (ViewGroup) inflater.inflate(R.layout.fragment_nearby_padi_puja_details, container, false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppConstants.FRAGMENT_NAME = "Nearby Padi Puja Details";
        getActivity().setTitle("Nearby Padi Puja Details");

        init();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvNearByPadiPujaDetails.setLayoutManager(linearLayoutManager);

        getNearByPadiPujaDetails();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = etSearch.getText().toString().toLowerCase(Locale.getDefault());
                if (text != null || text.equals(""))
                    nearByPadiPujaDetailsAdapter.filter(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
       });

    }

    private void init() {
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        tvEmptyText = (TextView) root.findViewById(R.id.tvEmptyText);
        etSearch = (EditText) root.findViewById(R.id.etSearch);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);
        rvNearByPadiPujaDetails = (RecyclerView) root.findViewById(R.id.rvNearByPadiPujaDetails);
        fabAddPadiPuja = (FloatingActionButton) root.findViewById(R.id.fabAddPadiPuja);

        swipeRefreshLayout.setOnRefreshListener(this);
        fabAddPadiPuja.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAddPadiPuja:
                Intent intent = new Intent(getActivity(), AddNewPadiPujaActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getNearByPadiPujaDetails() {
        final ArrayList<NearByPadiPujaDetailsPOJOClass> nearByPadiPujaDetailsPOJOClassArrayList = new ArrayList<>();
        if (AppUtils.isOnline(getActivity())) {
            AppUtils.getNearByPadiPujaDetails(AppUrls.PADI_PUJA_LIST_URL, getActivity(), progressBar,
                    AppConstants.CURRENT_LOCATION_LATITUDE, AppConstants.CURRENT_LOCATION_LONGITUDE, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                swipeRefreshLayout.setRefreshing(false);
                                JSONObject response = new JSONObject(result);

                                if (!response.getBoolean("error")) {
                                    tvEmptyText.setVisibility(View.GONE);
                                    rvNearByPadiPujaDetails.setVisibility(View.VISIBLE);
                                    etSearch.setVisibility(View.VISIBLE);

                                    JSONArray jsonArray = response.getJSONArray("Users List");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        NearByPadiPujaDetailsPOJOClass nearByPadiPujaDetailsPOJOClass = new NearByPadiPujaDetailsPOJOClass();

                                        nearByPadiPujaDetailsPOJOClass.setStrId(jsonObject.getString("id"));
                                        nearByPadiPujaDetailsPOJOClass.setStrPersonName(jsonObject.getString("person_name"));
                                        nearByPadiPujaDetailsPOJOClass.setStrEmail(jsonObject.getString("email"));
                                        nearByPadiPujaDetailsPOJOClass.setStrRegDate(jsonObject.getString("reg_date"));
                                        nearByPadiPujaDetailsPOJOClass.setStrRegTime(jsonObject.getString("reg_time"));
                                        nearByPadiPujaDetailsPOJOClass.setStrCity(jsonObject.getString("city"));
                                        nearByPadiPujaDetailsPOJOClass.setStrLocality(jsonObject.getString("locality"));
                                        nearByPadiPujaDetailsPOJOClass.setStrFlatNo(jsonObject.getString("flat_no"));
                                        nearByPadiPujaDetailsPOJOClass.setStrPincode(jsonObject.getString("pincode"));
                                        nearByPadiPujaDetailsPOJOClass.setStrState(jsonObject.getString("state"));
                                        nearByPadiPujaDetailsPOJOClass.setStrLandmark(jsonObject.getString("landmark"));
                                        nearByPadiPujaDetailsPOJOClass.setStrLatitude(jsonObject.getString("latitude"));
                                        nearByPadiPujaDetailsPOJOClass.setStrLongitude(jsonObject.getString("longitude"));
                                        nearByPadiPujaDetailsPOJOClass.setStrContactNo(jsonObject.getString("contact_no"));
                                        nearByPadiPujaDetailsPOJOClass.setStrUserId(jsonObject.getString("user_id"));
                                        nearByPadiPujaDetailsPOJOClass.setStrImage(jsonObject.getString("image"));

                                        nearByPadiPujaDetailsPOJOClassArrayList.add(nearByPadiPujaDetailsPOJOClass);
                                        nearByPadiPujaDetailsAdapter = new NearByPadiPujaDetailsAdapter(getActivity(), nearByPadiPujaDetailsPOJOClassArrayList);
                                        rvNearByPadiPujaDetails.setAdapter(nearByPadiPujaDetailsAdapter);
                                    }
                                } else if (response.getString("error").equalsIgnoreCase("true")) {
                                    tvEmptyText.setVisibility(View.VISIBLE);
                                    rvNearByPadiPujaDetails.setVisibility(View.GONE);

                                    etSearch.setVisibility(View.GONE);
                                    tvEmptyText.setText(response.getString("error_msg"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        } else {
            AppUtils.showSnackBar(getActivity());
            //Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRefresh() {
        //etSearch.setText("");
        getNearByPadiPujaDetails();
    }
}
