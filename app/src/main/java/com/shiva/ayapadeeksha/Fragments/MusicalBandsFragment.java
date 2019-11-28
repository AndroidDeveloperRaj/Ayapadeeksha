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

import com.shiva.ayapadeeksha.Adapters.MusicalBandsAdapter;
import com.shiva.ayapadeeksha.POJOClass.MusicalBands;
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


public class MusicalBandsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ViewGroup root;
    private ProgressBar progressBar;
    private TextView tvemptyText;
    private RecyclerView rvMusicBands;
    private MusicalBandsAdapter musicalBandsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private EditText etSearch;


    public MusicalBandsFragment() {
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
        root = (ViewGroup) inflater.inflate(R.layout.fragment_musical_bands, container, false);

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
        AppConstants.FRAGMENT_NAME = "Musical Bands";
        getActivity().setTitle("Musical Bands");

        init();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvMusicBands.setLayoutManager(linearLayoutManager);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = etSearch.getText().toString().toLowerCase(Locale.getDefault());
                if (text != null)
                    musicalBandsAdapter.filter(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getMusicalBandsList("2");
    }

    private void init() {
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        tvemptyText = (TextView) root.findViewById(R.id.tvemptyText);
        etSearch = (EditText) root.findViewById(R.id.etSearch);
        rvMusicBands = (RecyclerView) root.findViewById(R.id.rvMusicBands);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        //etSearch.setText("");
        getMusicalBandsList("2");
    }

    private void getMusicalBandsList(String strVendorId) {
        final ArrayList<MusicalBands> musicalBandsArrayList = new ArrayList<>();
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
                                        rvMusicBands.setVisibility(View.VISIBLE);
                                        etSearch.setVisibility(View.VISIBLE);
                                        JSONArray jsonArray = response.getJSONArray("Musical Bands");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            MusicalBands musicalBands = new MusicalBands();

                                            musicalBands.setStrId(jsonObject.getString("id"));
                                            musicalBands.setStrBandName(jsonObject.getString("band_name"));
                                            musicalBands.setStrNoOfPersons(jsonObject.getString("no_of_persons"));
                                            musicalBands.setStrPrice(jsonObject.getString("price"));
                                            musicalBands.setStrEmail(jsonObject.getString("email"));
                                            musicalBands.setStrContactNo(jsonObject.getString("contactno"));
                                            musicalBands.setStrAddress(jsonObject.getString("address"));
                                            musicalBands.setStrImage(jsonObject.getString("userimage"));

                                            musicalBandsArrayList.add(musicalBands);
                                            musicalBandsAdapter = new MusicalBandsAdapter(getActivity(), musicalBandsArrayList);
                                            rvMusicBands.setAdapter(musicalBandsAdapter);
                                        }
                                        break;
                                    case "true":
                                        tvemptyText.setVisibility(View.VISIBLE);
                                        rvMusicBands.setVisibility(View.GONE);
                                        etSearch.setVisibility(View.GONE);
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
