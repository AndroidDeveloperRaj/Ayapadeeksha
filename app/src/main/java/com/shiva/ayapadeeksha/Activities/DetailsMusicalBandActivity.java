package com.shiva.ayapadeeksha.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class DetailsMusicalBandActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String strEmail, strBandName;
    private TextView tvId, tvEmail, tvDeeksha, tvBandName, tvNoOfPersons, tvPrice,
            tvAddress, tvContactNumber;
    private ProgressBar progressBar;
    private ImageView imgViewMusicalBand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_musical_band);
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
        strBandName = getIntent().getStringExtra("BAND_NAME");
        //strVendorId = getIntent().getStringExtra("VENDOR_ID");
        setTitle(strBandName + " Details");
        init();
        getGuruSwamyAllDetails("2", strEmail);
    }

    private void getGuruSwamyAllDetails(final String strVendorId, final String strEmail) {
        if (AppUtils.isOnline(DetailsMusicalBandActivity.this)) {
            AppUtils.getGuruSwamyAllDetails(AppUrls.ALL_LIST_DETAILS_URL, progressBar, DetailsMusicalBandActivity.this,
                    strEmail, strVendorId, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    JSONArray jsonArray = response.getJSONArray("Musical Bands Profile");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        tvId.setText(object.getString("id"));
                                        tvEmail.setText(object.getString("email"));
                                        tvDeeksha.setText(object.getString("type"));
                                        tvBandName.setText(object.getString("band_name"));
                                        tvNoOfPersons.setText(object.getString("no_of_persons"));
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

                                        Glide.with(DetailsMusicalBandActivity.this)
                                                .load(AppUrls.IMAGE_URL + object.getString("image"))
                                                .thumbnail(0.5f)
                                                .into(imgViewMusicalBand);
                                    }
                                } else {
                                    Toast.makeText(DetailsMusicalBandActivity.this, response.getString("error_msg"),
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            AppUtils.showSnackBar(DetailsMusicalBandActivity.this);
        }
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        imgViewMusicalBand = (ImageView) findViewById(R.id.imgViewMusicalBand);

        tvId = (TextView) findViewById(R.id.tvId);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvDeeksha = (TextView) findViewById(R.id.tvDeeksha);
        tvBandName = (TextView) findViewById(R.id.tvBandName);
        tvNoOfPersons = (TextView) findViewById(R.id.tvNoOfPersons);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvContactNumber = (TextView) findViewById(R.id.tvContactNumber);

    }
}
