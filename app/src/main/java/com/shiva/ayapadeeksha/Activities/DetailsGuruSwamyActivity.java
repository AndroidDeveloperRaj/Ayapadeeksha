package com.shiva.ayapadeeksha.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsGuruSwamyActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String strEmail, strPersonName;
    private TextView tvId, tvEmail, tvDeeksha, tvPersonName, tvDateOfBirth, tvExperience, tvPrice,
            tvFlatNo, tvAddress, tvContactNumber;
    private ProgressBar progressBar;
    private ImageView imgViewGuruSwamy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_guru_swamy);
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
        strEmail = getIntent().getStringExtra("EMAIL");
        strPersonName = getIntent().getStringExtra("PERSON_NAME");
        //strVendorId = getIntent().getStringExtra("VENDOR_ID");
        setTitle(strPersonName + " Details");
        init();
        getGuruSwamyAllDetails("1", strEmail);
    }

    private void getGuruSwamyAllDetails(final String strVendorId, final String strEmail) {
        if (AppUtils.isOnline(DetailsGuruSwamyActivity.this)) {
            AppUtils.getGuruSwamyAllDetails(AppUrls.ALL_LIST_DETAILS_URL, progressBar, DetailsGuruSwamyActivity.this,
                    strEmail, strVendorId, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    JSONArray jsonArray = response.getJSONArray("Guru Swamy Profile");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        tvId.setText(object.getString("id"));
                                        tvEmail.setText(object.getString("email"));
                                        tvDeeksha.setText(object.getString("type"));
                                        tvPersonName.setText(object.getString("name"));
                                        tvDateOfBirth.setText(object.getString("dob"));
                                        tvExperience.setText(object.getString("experience"));
                                        tvPrice.setText(object.getString("price"));
                                        if (object.getString("landmark").equals("")) {
                                            tvAddress.setText(object.getString("flatno") + ", " +
                                                    object.getString("locality") + ", " +
                                                    object.getString("city") + ", " +
                                                    object.getString("state") + ", " +
                                                    object.getString("pincode"));
                                        } else {
                                            tvAddress.setText(object.getString("flatno") + ", " +
                                                    object.getString("locality") + ", " +
                                                    object.getString("landmark") + ", " +
                                                    object.getString("city") + ", " +
                                                    object.getString("state") + ", " +
                                                    object.getString("pincode"));
                                        }

                                        tvContactNumber.setText(object.getString("contactno"));


                                        Glide.with(DetailsGuruSwamyActivity.this)
                                                .load(AppUrls.IMAGE_URL + object.getString("image"))
                                                .thumbnail(0.5f)
                                                .into(imgViewGuruSwamy);
                                    }
                                } else {
                                    Toast.makeText(DetailsGuruSwamyActivity.this, response.getString("error_msg"),
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            AppUtils.showSnackBar(DetailsGuruSwamyActivity.this);
        }
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        imgViewGuruSwamy = (ImageView) findViewById(R.id.imgViewGuruSwamy);

        tvId = (TextView) findViewById(R.id.tvId);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvDeeksha = (TextView) findViewById(R.id.tvDeeksha);
        tvPersonName = (TextView) findViewById(R.id.tvPersonName);
        tvDateOfBirth = (TextView) findViewById(R.id.tvDateOfBirth);
        tvExperience = (TextView) findViewById(R.id.tvExperience);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvContactNumber = (TextView) findViewById(R.id.tvContactNumber);

    }
}
