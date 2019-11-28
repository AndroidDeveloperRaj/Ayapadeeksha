package com.shiva.ayapadeeksha.Fragments;


import android.content.Intent;
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

import com.shiva.ayapadeeksha.Activities.AddNewPostsActivity;
import com.shiva.ayapadeeksha.Adapters.PostsDetailsAdapter;
import com.shiva.ayapadeeksha.POJOClass.PostDetailsPOJOClass;
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


public class WallPostsFragment extends Fragment
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ViewGroup root;
    private FloatingActionButton fabAddPosts;
    private RecyclerView rvPostsDetails;
    private ProgressBar progressBar;
    private TextView tvEmptyText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private PostsDetailsAdapter postsDetailsAdapter;
    private EditText etSearch;
    private String strCurrentPhotoPath, strUserId, StrDescription, StrImage, StrType, StrVideo;


    public WallPostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = (ViewGroup) inflater.inflate(R.layout.fragment_wall_posts, container, false);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppConstants.FRAGMENT_NAME = "Posts ";
        getActivity().setTitle("Posts");

        init();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvPostsDetails.setLayoutManager(linearLayoutManager);

        getPostDetails();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = etSearch.getText().toString().toLowerCase(Locale.getDefault());
                if (text != null || text.equals(""))
                    postsDetailsAdapter.filter(text);
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
        rvPostsDetails = (RecyclerView) root.findViewById(R.id.rvPostsDetails);
        fabAddPosts = (FloatingActionButton) root.findViewById(R.id.fabAddPosts);

        swipeRefreshLayout.setOnRefreshListener(this);
        fabAddPosts.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAddPosts:
                Intent intent = new Intent(getActivity(), AddNewPostsActivity.class);
                startActivity(intent);
                break;
        }
    }


    private void getPostDetails() {

        final ArrayList<PostDetailsPOJOClass> postsModelArrayList = new ArrayList<>();
        if (AppUtils.isOnline(getActivity())) {
            AppUtils.getPosts(AppUrls.ADD_POST, getActivity(),
                    AppConstants.USER_ID, StrDescription, StrImage, StrVideo, StrType, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                swipeRefreshLayout.setRefreshing(false);
                                JSONObject response = new JSONObject(result);

                                if (!response.getBoolean("error")) {
                                    tvEmptyText.setVisibility(View.GONE);
                                    rvPostsDetails.setVisibility(View.VISIBLE);
                                    etSearch.setVisibility(View.VISIBLE);

                                    JSONArray jsonArray = response.getJSONArray("Users List");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        PostDetailsPOJOClass postsModel = new PostDetailsPOJOClass();

                                        postsModel.setDescription(jsonObject.getString("description"));
                                        postsModel.setImage(jsonObject.getString("image"));
                                        postsModel.setPrivacy(jsonObject.getString("privacy"));
                                        postsModel.setVideo(jsonObject.getString("video"));
                                        postsModel.setUser_id(jsonObject.getString("user_id"));

                                        postsModelArrayList.add(postsModel);
                                        postsDetailsAdapter = new PostsDetailsAdapter(getActivity(), postsModelArrayList);
                                        rvPostsDetails.setAdapter(postsDetailsAdapter);

                                    }
                                } else if (response.getString("error").equalsIgnoreCase("true")) {
                                    tvEmptyText.setVisibility(View.VISIBLE);
                                    rvPostsDetails.setVisibility(View.GONE);

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
        getPostDetails();
    }
}