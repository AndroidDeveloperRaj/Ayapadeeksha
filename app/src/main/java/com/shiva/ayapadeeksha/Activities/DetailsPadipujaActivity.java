package com.shiva.ayapadeeksha.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsPadipujaActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private TextView tvId, tvEmail, tvPersonName, tvDate, tvTime, tvContactNumber, tvAddress;
    private String strUserId, strPersonName;
    private ImageView imgPadiPuja;
    private Button edit_post,delete_post;

    private String post_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_padipuja);
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
    protected void onResume() {
        super.onResume();
        strUserId = getIntent().getStringExtra("USER_ID");
        strPersonName = getIntent().getStringExtra("PERSON_NAME");
        setTitle(strPersonName + " Details");
        init();
        getPadiPujaDetails();
    }

    private void getPadiPujaDetails() {
        if (AppUtils.isOnline(DetailsPadipujaActivity.this)) {
            AppUtils.getPadiPujaDetails(AppUrls.NEARBY_PADI_PUJA_DETAILS_URL, progressBar,
                    DetailsPadipujaActivity.this, strUserId, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    JSONArray jsonArray = response.getJSONArray("Users List");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        tvId.setText(object.getString("id"));
                                        post_id = object.getString("id");
                                        tvPersonName.setText(object.getString("person_name"));
                                        tvEmail.setText(object.getString("email"));
                                        tvDate.setText(object.getString("reg_date"));
                                        tvTime.setText(object.getString("reg_time"));
                                        tvContactNumber.setText(object.getString("contact_no"));
                                        if (object.getString("landmark").equals("")) {
                                            tvAddress.setText(object.getString("flat_no") + ", " +
                                                    object.getString("locality") + ", " +
                                                    object.getString("city") + ", " +
                                                    object.getString("state") + ", " +
                                                    object.getString("pincode"));
                                        } else {
                                            tvAddress.setText(object.getString("flat_no") + ", " +
                                                    object.getString("locality") + ", " +
                                                    object.getString("landmark") + ", " +
                                                    object.getString("city") + ", " +
                                                    object.getString("state") + ", " +
                                                    object.getString("pincode"));
                                        }

                                        Glide.with(DetailsPadipujaActivity.this)
                                                .load(AppUrls.IMAGE_URL + object.getString("image"))
                                                .apply(new RequestOptions()
                                                        .placeholder(R.drawable.ayyappa_sannidhanam)
                                                        .error(R.drawable.ayyappa_sannidhanam))
                                                .into(imgPadiPuja);

                                    }
                                } else {
                                    Toast.makeText(DetailsPadipujaActivity.this, response.getString("error_msg"),
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        imgPadiPuja = (ImageView) findViewById(R.id.imgPadiPuja);

        tvId = (TextView) findViewById(R.id.tvId);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPersonName = (TextView) findViewById(R.id.tvPersonName);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvContactNumber = (TextView) findViewById(R.id.tvContactNumber);
        tvAddress = (TextView) findViewById(R.id.tvAddress);

        edit_post = findViewById(R.id.edit_post);
        delete_post = findViewById(R.id.delete_post);

        edit_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DetailsPadipujaActivity.this, AddNewPadiPujaActivity.class);
                intent.putExtra("POST_ID", post_id);
//                intent.putExtra("PERSON_NAME", tvPersonName.getText().toString());
              intent.putExtra("UPDATE_PADIPUJA", true);
                startActivity(intent);

            }
        });


        delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deletePadiPuja();
            }
        });
    }


    private void deletePadiPuja() {
        if (AppUtils.isOnline(DetailsPadipujaActivity.this)) {
            AppUtils.deletePadiPujaDetails(AppUrls.PADI_PUJA_DELETE_URL, progressBar,
                    DetailsPadipujaActivity.this,post_id, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    Toast.makeText(DetailsPadipujaActivity.this, response.getString("Message"),
                                            Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(DetailsPadipujaActivity.this, NavigationDrawerActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(DetailsPadipujaActivity.this, response.getString("error_msg"),
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
