package com.shiva.ayapadeeksha.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shiva.ayapadeeksha.Adapters.MySubscriptionsAdapter;
import com.shiva.ayapadeeksha.POJOClass.MySubscriptionsPOJOClass;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppConstants;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MySubscriptionsFragment extends Fragment {
    private ProgressBar progressBar;
    private RecyclerView rvSubscriptions;
    private ViewGroup root;
    private LinearLayoutManager linearLayoutManager;
    private String strEmail;
    private TextView tvMessage;
    private MySubscriptionsAdapter mySubscriptionsAdapter;

    public MySubscriptionsFragment() {
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
        root = (ViewGroup) inflater.inflate(R.layout.fragment_my_subscriptions, container, false);
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
        getActivity().setTitle("My Subscriptions");
        init();
        strEmail = AppConstants.EMAIL;
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvSubscriptions.setLayoutManager(linearLayoutManager);

        getSubscriptions();
    }

    private void getSubscriptions() {
        final ArrayList<MySubscriptionsPOJOClass> mySubscriptionsPOJOClassArrayList = new ArrayList<>();
        if (AppUtils.isOnline(getActivity())) {
            AppUtils.getSubscriptions(AppUrls.SUBSCRIPTIONS_URL, progressBar, getActivity(), strEmail,
                    new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    JSONArray array = response.getJSONArray("Subscriptions");
                                    tvMessage.setText(response.getString("Message"));
                                    final MySubscriptionsPOJOClass mySubscriptionsPOJOClass1 = new MySubscriptionsPOJOClass();
                                    mySubscriptionsPOJOClass1.setStrUserId(response.getString("user_id"));
                                    for (int i = 0; i <= array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        final MySubscriptionsPOJOClass mySubscriptionsPOJOClass = new MySubscriptionsPOJOClass();
                                        mySubscriptionsPOJOClass.setStrVendorId(object.getString("vendor_id"));
                                        mySubscriptionsPOJOClass.setStrVendorName(object.getString("vendor_name"));
                                        mySubscriptionsPOJOClass.setStrPackageDuration(object.getString("package Duration"));
                                        mySubscriptionsPOJOClass.setStrPrice(object.getString("price"));
                                        mySubscriptionsPOJOClass.setStrDescription(object.getString("description"));
                                        mySubscriptionsPOJOClass.setStrExpiryDate(object.getString("Expiry Date"));

                                        mySubscriptionsPOJOClassArrayList.add(mySubscriptionsPOJOClass);
                                        mySubscriptionsAdapter = new MySubscriptionsAdapter(getActivity(), mySubscriptionsPOJOClassArrayList);
                                        rvSubscriptions.setAdapter(mySubscriptionsAdapter);

                                    }
                                } else {
                                    Toast.makeText(getActivity(), response.getString("error_msg"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else AppUtils.showSnackBar(getActivity());
    }

    private void init() {
        rvSubscriptions = (RecyclerView) root.findViewById(R.id.rvSubscriptions);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        tvMessage = (TextView) root.findViewById(R.id.tvMessage);
    }

}
