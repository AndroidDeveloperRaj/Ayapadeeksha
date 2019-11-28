package com.shiva.ayapadeeksha.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppConstants;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendRequestActivity extends AppCompatActivity {


    private ProgressBar pbFriendRequest;
    private RecyclerView rvFriendRequest;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String StrUser_id, StrStatus;

   //private List<User> LISTOFFRIENDREQUESTS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();


    }

    private void init() {

        pbFriendRequest = findViewById(R.id.pb_friend_request);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_friend_request);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //LISTOFFRIENDREQUESTS.clear();
                getPendingFriendRequest();
            }


        });


        //LISTOFFRIENDREQUESTS = new ArrayList<>();
        rvFriendRequest = findViewById(R.id.rv_friend_requests);
        rvFriendRequest.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FriendRequestActivity.this);
        rvFriendRequest.setLayoutManager(layoutManager);
        getPendingFriendRequest();
    }


    private void getPendingFriendRequest() {
        if (AppUtils.isOnline(FriendRequestActivity.this)) {
            AppUtils.getFriendRequest(AppUrls.FOLLOW_UNFOLLOW, FriendRequestActivity.this,
                    StrUser_id, StrStatus, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {


                                try {
                                    JSONObject response = new JSONObject(result);
                                    if (!response.getBoolean("error")) {
                                        Toast.makeText(FriendRequestActivity.this, response.getString("Message"),
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(FriendRequestActivity.this, response.getString("error_msg"),
                                                Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });



        }
    }
}