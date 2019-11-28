package com.shiva.ayapadeeksha.Fragments;

import android.content.Context;
import android.os.Bundle;
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

import com.shiva.ayapadeeksha.Adapters.AyyapaSwamyHotelsAdapter;
import com.shiva.ayapadeeksha.POJOClass.AyyapaSwamyHotels;
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

public class AyyapaSwamyHotelsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ViewGroup root;
    private ProgressBar progressBar;
    private TextView tvemptyText;
    private RecyclerView rvAyyappaSwamyHotels;
    private AyyapaSwamyHotelsAdapter ayyapaSwamyHotelsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private EditText etSearch;

    public AyyapaSwamyHotelsFragment() {
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
        root = (ViewGroup) inflater.inflate(R.layout.fragment_ayyapa_swamy_hotels, container, false);

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        AppConstants.FRAGMENT_NAME = "Ayyapa Swamy Hotels";
        getActivity().setTitle("Ayyapa Swamy Hotels");

        init();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvAyyappaSwamyHotels.setLayoutManager(linearLayoutManager);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = etSearch.getText().toString().toLowerCase(Locale.getDefault());
                if (text != null)
                    ayyapaSwamyHotelsAdapter.filter(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getAyyappaSwamyHotelsListList("4");
    }

    private void init() {
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        tvemptyText = (TextView) root.findViewById(R.id.tvemptyText);
        rvAyyappaSwamyHotels = (RecyclerView) root.findViewById(R.id.rvAyyappaSwamyHotels);
        etSearch = (EditText) root.findViewById(R.id.etSearch);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        //etSearch.setText("");
        getAyyappaSwamyHotelsListList("4");
    }

    private void getAyyappaSwamyHotelsListList(String strVendorId) {
        final ArrayList<AyyapaSwamyHotels> ayyapaSwamyHotelsArrayList = new ArrayList<>();
        if (AppUtils.isOnline(getActivity())) {
            AppUtils.getLists(AppUrls.All_LIST_URL, progressBar,
                    getActivity(), AppConstants.CURRENT_LOCATION_LATITUDE,
                    AppConstants.CURRENT_LOCATION_LONGITUDE,
                    strVendorId, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                swipeRefreshLayout.setRefreshing(false);
                                JSONObject response = new JSONObject(result);

                                switch (response.getString("error")) {
                                    case "false":
                                        tvemptyText.setVisibility(View.GONE);
                                        rvAyyappaSwamyHotels.setVisibility(View.VISIBLE);
                                        etSearch.setVisibility(View.VISIBLE);
                                        JSONArray jsonArray = response.getJSONArray("Ayyappa Swamy Hotels");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            AyyapaSwamyHotels ayyapaSwamyHotels = new AyyapaSwamyHotels();

                                            ayyapaSwamyHotels.setStrid(jsonObject.getString("id"));
                                            ayyapaSwamyHotels.setStrHotelName(jsonObject.getString("hotel_name"));
                                            ayyapaSwamyHotels.setStrEmail(jsonObject.getString("email"));
                                            ayyapaSwamyHotels.setStrPrice(jsonObject.getString("price"));
                                            ayyapaSwamyHotels.setStrFlatno(jsonObject.getString("flatno"));
                                            ayyapaSwamyHotels.setStrLocality(jsonObject.getString("locality"));
                                            ayyapaSwamyHotels.setStrCity(jsonObject.getString("city"));
                                            ayyapaSwamyHotels.setStrState(jsonObject.getString("state"));
                                            ayyapaSwamyHotels.setStrPincode(jsonObject.getString("pincode"));
                                            ayyapaSwamyHotels.setStrLandmark(jsonObject.getString("landmark"));
                                            ayyapaSwamyHotels.setStrContactNo(jsonObject.getString("contactno"));
                                            ayyapaSwamyHotels.setStrLatitude(jsonObject.getString("latitude"));
                                            ayyapaSwamyHotels.setStrLongitude(jsonObject.getString("longitude"));
                                            ayyapaSwamyHotels.setStrImage(jsonObject.getString("userimage"));

                                            ayyapaSwamyHotelsArrayList.add(ayyapaSwamyHotels);
                                            ayyapaSwamyHotelsAdapter = new AyyapaSwamyHotelsAdapter(getActivity(), ayyapaSwamyHotelsArrayList);
                                            rvAyyappaSwamyHotels.setAdapter(ayyapaSwamyHotelsAdapter);
                                        }
                                        break;
                                    case "true":
                                        tvemptyText.setVisibility(View.VISIBLE);
                                        etSearch.setVisibility(View.GONE);
                                        rvAyyappaSwamyHotels.setVisibility(View.GONE);
                                        tvemptyText.setText(response.getString("error_msg"));
                                        break;
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
