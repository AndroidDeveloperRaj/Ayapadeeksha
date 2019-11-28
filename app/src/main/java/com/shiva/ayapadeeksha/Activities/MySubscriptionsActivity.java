package com.shiva.ayapadeeksha.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

public class MySubscriptionsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private RecyclerView rvSubscriptions;
    private LinearLayoutManager linearLayoutManager;
    private String strEmail;
    private TextView tvMessage;
    private MySubscriptionsAdapter mySubscriptionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscriptions);
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
    public void onResume() {
        super.onResume();
        setTitle("My Subscriptions");
        init();
        strEmail = AppConstants.EMAIL;
        linearLayoutManager = new LinearLayoutManager(MySubscriptionsActivity.this);
        rvSubscriptions.setLayoutManager(linearLayoutManager);

        getSubscriptions();
    }

    private void getSubscriptions() {
        final ArrayList<MySubscriptionsPOJOClass> mySubscriptionsPOJOClassArrayList = new ArrayList<>();
        if (AppUtils.isOnline(MySubscriptionsActivity.this)) {
            AppUtils.getSubscriptions(AppUrls.SUBSCRIPTIONS_URL, progressBar,
                    MySubscriptionsActivity.this, strEmail,
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
                                        mySubscriptionsAdapter = new MySubscriptionsAdapter(MySubscriptionsActivity.this, mySubscriptionsPOJOClassArrayList);
                                        rvSubscriptions.setAdapter(mySubscriptionsAdapter);

                                    }
                                } else {
                                    Toast.makeText(MySubscriptionsActivity.this, response.getString("error_msg"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else AppUtils.showSnackBar(MySubscriptionsActivity.this);
    }

    private void init() {
        rvSubscriptions = (RecyclerView) findViewById(R.id.rvSubscriptions);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        tvMessage = (TextView) findViewById(R.id.tvMessage);
    }

}
