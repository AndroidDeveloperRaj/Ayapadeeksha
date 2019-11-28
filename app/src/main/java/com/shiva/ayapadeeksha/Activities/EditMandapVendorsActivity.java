package com.shiva.ayapadeeksha.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shiva.ayapadeeksha.Adapters.UpdateAdapter;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AndroidMultiPartEntity;
import com.shiva.ayapadeeksha.Utils.AppConstants;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.VolleyCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class EditMandapVendorsActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private String strMandapVendorsId, strMandapVendorsEmail;
    private TextInputLayout tilEmail, tilMandapVendorName, tilPrice;
    private TextInputEditText tieEmail, tieMandapVendorName, tiePrice;
    private String strEmail, strMandapVendorName, strPrice;
    private CheckBox checkBoxFree;
    private Button btnUpdate;
    private TextView tvMandapVendorId;
    private ProgressBar progressBar;
    private long totalSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mandap_vendors);
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
        setTitle("Mandap Vendors Details");
        init();
        strMandapVendorsId = getIntent().getStringExtra("MANDAP_VENDORS_ID");
        strMandapVendorsEmail = getIntent().getStringExtra("MANDAP_VENDORS_EMAIL");
        tieEmail.setText(strMandapVendorsEmail);
        getMandapVendorsDetails(strMandapVendorsEmail);
        checkBoxFree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    tiePrice.setText("Free");
                else tiePrice.setText("");
            }
        });

        tiePrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tiePrice.getText().toString().equals(""))
                    checkBoxFree.setChecked(false);
            }
        });

        if (tiePrice.getText().toString().equals(""))
            checkBoxFree.setChecked(false);
    }

    /* ==================== Showing Error Message ========================== */
    private void setErrorMessage(TextInputLayout textInputLayout, String strErrorMessage) {
        textInputLayout.setError(strErrorMessage);
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvMandapVendorId = (TextView) findViewById(R.id.tvMandapVendorId);

        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        tilMandapVendorName = (TextInputLayout) findViewById(R.id.tilMandapVendorName);
        tilPrice = (TextInputLayout) findViewById(R.id.tilPrice);

        tieEmail = (TextInputEditText) findViewById(R.id.tieEmail);
        tieMandapVendorName = (TextInputEditText) findViewById(R.id.tieMandapVendorName);
        tiePrice = (TextInputEditText) findViewById(R.id.tiePrice);

        checkBoxFree = (CheckBox) findViewById(R.id.checkBoxFree);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(this);
    }

    private void getMandapVendorsDetails(String strMandapVendorsEmail) {
        if (AppUtils.isOnline(EditMandapVendorsActivity.this)) {
            AppUtils.getMandapVendorsDetails(AppUrls.EDIT_MANDAP_VENDORS_DETAILS_URL, EditMandapVendorsActivity.this,
                    progressBar, strMandapVendorsEmail, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    JSONArray jsonArray = response.getJSONArray("Mandap Vendors Profile");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        tvMandapVendorId.setText(object.getString("id"));
                                        tieMandapVendorName.setText(object.getString("name"));
                                        tieEmail.setText(object.getString("email"));
                                        tiePrice.setText(object.getString("price"));

                                        if (object.getString("price").equals("Free")) {
                                            checkBoxFree.setChecked(true);
                                        } else {
                                            checkBoxFree.setChecked(false);
                                            tiePrice.setText(object.getString("price"));
                                        }
                                    }
                                } else {
                                    Toast.makeText(EditMandapVendorsActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            AppUtils.showSnackBar(EditMandapVendorsActivity.this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate:
                strEmail = tieEmail.getText().toString();
                strMandapVendorName = tieMandapVendorName.getText().toString();
                strPrice = tiePrice.getText().toString();

                if (strEmail.isEmpty())
                    setErrorMessage(tilEmail, "Email Required");
                else if (strMandapVendorName.isEmpty())
                    setErrorMessage(tilMandapVendorName, "Person Name Required");
                else if (strPrice.isEmpty())
                    setErrorMessage(tilPrice, "Price Required");
                else
                    new UpdateUser().execute();
                break;
        }
    }

    private class UpdateUser extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return upLoadFile();
        }

        private String upLoadFile() {
            String responseString = null;

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(AppUrls.UPDATE_MANDAP_VENDORS_DETAILS_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });
                entity.addPart("id", new StringBody(strMandapVendorsId));
                entity.addPart("name", new StringBody(strMandapVendorName));
                entity.addPart("email", new StringBody(strEmail));
                entity.addPart("price", new StringBody(strPrice));

                totalSize = entity.getContentLength();
                httpPost.setEntity(entity);

                // Making Server Call
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    //Server Response
                    responseString = EntityUtils.toString(httpEntity);
                    Log.d("responseString", responseString);
                } else {
                    responseString = "Error Occurred.!Http status Code:" + statusCode;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(EditMandapVendorsActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                    AppConstants.VENDORS_ID = strMandapVendorsId;
                    /*Intent intent = new Intent(EditMandapVendorsActivity.this, EditProfileActivity.class);
                    intent.putExtra("USER_ID", AppConstants.USER_ID);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/

                    new UpdateAdapter(EditMandapVendorsActivity.this).execute();
                    finish();

                } else if (jsonObject.getBoolean("error")) {
                    Toast.makeText(EditMandapVendorsActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
