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

import com.shiva.ayapadeeksha.Adapters.GuruSwamysAdapter;
import com.shiva.ayapadeeksha.Adapters.MandapVendorsAdapter;
import com.shiva.ayapadeeksha.POJOClass.MandapVendors;
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

public class MandapVendorsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ViewGroup root;
    private ProgressBar progressBar;
    private TextView tvemptyText;
    private RecyclerView rvMandapVendors;
    private MandapVendorsAdapter mandapVendorsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private EditText etSearch;

    public MandapVendorsListFragment() {
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
        root = (ViewGroup) inflater.inflate(R.layout.fragment_mandap_vendors_list, container, false);

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
        AppConstants.FRAGMENT_NAME = "Mandap Vendors List";
        getActivity().setTitle("Mandap Vendors List");

        init();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvMandapVendors.setLayoutManager(linearLayoutManager);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = etSearch.getText().toString().toLowerCase(Locale.getDefault());
                if (text != null)
                    mandapVendorsAdapter.filter(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getMandapVendorssList("3");
    }

    private void init() {
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        tvemptyText = (TextView) root.findViewById(R.id.tvemptyText);
        rvMandapVendors = (RecyclerView) root.findViewById(R.id.rvMandapVendors);
        etSearch = (EditText) root.findViewById(R.id.etSearch);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        //etSearch.setText("");
        getMandapVendorssList("3");
    }

    private void getMandapVendorssList(String strVendorId) {
        final ArrayList<MandapVendors> mandapVendorsArrayList = new ArrayList<>();
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
                                        rvMandapVendors.setVisibility(View.VISIBLE);
                                        etSearch.setVisibility(View.VISIBLE);
                                        JSONArray jsonArray = response.getJSONArray("Mandap Vendors");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            MandapVendors mandapVendors = new MandapVendors();

                                            mandapVendors.setStrId(jsonObject.getString("id"));
                                            mandapVendors.setStrName(jsonObject.getString("name"));
                                            mandapVendors.setStrEmail(jsonObject.getString("email"));
                                            mandapVendors.setStrPrice(jsonObject.getString("price"));
                                            mandapVendors.setStrContactNo(jsonObject.getString("contactno"));
                                            mandapVendors.setStrAddress(jsonObject.getString("address"));
                                            mandapVendors.setStrImage(jsonObject.getString("userimage"));

                                            mandapVendorsArrayList.add(mandapVendors);
                                            mandapVendorsAdapter = new MandapVendorsAdapter(getActivity(), mandapVendorsArrayList);
                                            rvMandapVendors.setAdapter(mandapVendorsAdapter);
                                        }
                                        break;
                                    case "true":
                                        tvemptyText.setVisibility(View.VISIBLE);
                                        etSearch.setVisibility(View.GONE);
                                        rvMandapVendors.setVisibility(View.GONE);
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
