package com.shiva.ayapadeeksha.Fragments;

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
import com.shiva.ayapadeeksha.POJOClass.GuruSwamys;
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

public class GuruSwamysListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ViewGroup root;
    private ProgressBar progressBar;
    private TextView tvemptyText;
    private RecyclerView rvGuruSwamys;
    private GuruSwamysAdapter guruSwamysAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private EditText etSearch;

    public GuruSwamysListFragment() {
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
        root = (ViewGroup) inflater.inflate(R.layout.fragment_guru_swamys_list, container, false);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppConstants.FRAGMENT_NAME = "Guru Swamy's List";
        getActivity().setTitle("Guru Swamy's List");

        init();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvGuruSwamys.setLayoutManager(linearLayoutManager);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = etSearch.getText().toString().toLowerCase(Locale.getDefault());
                if (text != null)
                    guruSwamysAdapter.filter(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getGuruSwamysList("1");
    }

    private void getGuruSwamysList(String strVendorId) {
        final ArrayList<GuruSwamys> guruSwamysArrayList = new ArrayList<>();
        if (AppUtils.isOnline(getActivity())) {
            AppUtils.getLists(AppUrls.All_LIST_URL, progressBar,
                    getActivity(), AppConstants.CURRENT_LOCATION_LATITUDE,
                    AppConstants.CURRENT_LOCATION_LONGITUDE, strVendorId, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                swipeRefreshLayout.setRefreshing(false);
                                JSONObject response = new JSONObject(result);

                                switch (response.getString("error")) {
                                    case "false":
                                        tvemptyText.setVisibility(View.GONE);
                                        rvGuruSwamys.setVisibility(View.VISIBLE);
                                        etSearch.setVisibility(View.VISIBLE);
                                        JSONArray jsonArray = response.getJSONArray("Guru Swamy Users");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            GuruSwamys guruSwamys = new GuruSwamys();

                                            guruSwamys.setStrId(jsonObject.getString("id"));
                                            guruSwamys.setStrName(jsonObject.getString("name"));
                                            guruSwamys.setStrEmail(jsonObject.getString("email"));
                                            guruSwamys.setStrExperience(jsonObject.getString("experience"));
                                            guruSwamys.setStrPrice(jsonObject.getString("price"));
                                            guruSwamys.setStrContactNo(jsonObject.getString("contactno"));
                                            guruSwamys.setStrAddress(jsonObject.getString("address"));
                                            guruSwamys.setStrImage(jsonObject.getString("userimage"));

                                            guruSwamysArrayList.add(guruSwamys);
                                            guruSwamysAdapter = new GuruSwamysAdapter(getActivity(), guruSwamysArrayList);
                                            rvGuruSwamys.setAdapter(guruSwamysAdapter);
                                        }
                                        break;
                                    case "true":
                                        tvemptyText.setVisibility(View.VISIBLE);
                                        rvGuruSwamys.setVisibility(View.GONE);
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

    private void init() {
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        tvemptyText = (TextView) root.findViewById(R.id.tvemptyText);
        rvGuruSwamys = (RecyclerView) root.findViewById(R.id.rvGuruSwamys);
        etSearch = (EditText) root.findViewById(R.id.etSearch);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        //etSearch.setText("");
        getGuruSwamysList("1");
    }
}
