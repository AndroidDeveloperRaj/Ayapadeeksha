package com.shiva.ayapadeeksha.Activities;

import android.app.Fragment;
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
import android.widget.Toast;

import com.shiva.ayapadeeksha.Fragments.VendorRegistrationFragment;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class GuruSwamyActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String strGuruSwamyId, strGuruSwamyEmail;
    private TextInputLayout tilEmail, tilPersonName, tilExperience, tilPrice;
    private TextInputEditText tieEmail, tiePersonName, tieExperience, tiePrice;
    private String strEmail, strPersonName, strExperience, strPrice;
    private CheckBox checkBoxFree;
    private Button btnSave;
    private ProgressBar progressBar;
    private long totalSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru_swamy);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuruSwamyActivity.this, RegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("Guru Swamy Details");

        strGuruSwamyId = getIntent().getStringExtra("GURU_SWAMY_ID");
        strGuruSwamyEmail = getIntent().getStringExtra("GURU_SWAMY_EMAIL");

        init();
        tieEmail.setText(strGuruSwamyEmail);
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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = tieEmail.getText().toString();
                strPersonName = tiePersonName.getText().toString();
                strExperience = tieExperience.getText().toString();
                strPrice = tiePrice.getText().toString();

                if (strEmail.isEmpty())
                    setErrorMessage(tilEmail, "Email Required");
                else if (strPersonName.isEmpty())
                    setErrorMessage(tilPersonName, "Person Name Required");
                else if (strExperience.isEmpty())
                    setErrorMessage(tilExperience, "Experience Required");
                else if (strPrice.isEmpty())
                    setErrorMessage(tilPrice, "Price Required");
                else
                    new UpdateUser().execute();
                    //saveGuruSwamyExperienceDetails();
            }
        });
        if (tiePrice.getText().toString().equals(""))
            checkBoxFree.setChecked(false);

    }

    private void saveGuruSwamyExperienceDetails() {
        if (AppUtils.isOnline(GuruSwamyActivity.this)) {
            AppUtils.saveGuruSwamyExperienceDetails(AppUrls.GURU_SWAMY_EXPERINCE_DETAILS_URL,
                    progressBar, GuruSwamyActivity.this,
                    strEmail, strPersonName, strExperience,
                    strPrice, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                switch (response.getString("error")) {
                                    case "false":
                                        Toast.makeText(GuruSwamyActivity.this,
                                                response.getString("message"),
                                                Toast.LENGTH_LONG).show();
                                        finish();
                                        break;
                                    case "true":
                                        Toast.makeText(GuruSwamyActivity.this,
                                                response.getString("error_msg"),
                                                Toast.LENGTH_LONG).show();
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else AppUtils.showSnackBar(GuruSwamyActivity.this);
    }

    /* ==================== Showing Error Message ========================== */
    private void setErrorMessage(TextInputLayout textInputLayout, String strErrorMessage) {
        textInputLayout.setError(strErrorMessage);
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        tilPersonName = (TextInputLayout) findViewById(R.id.tilPersonName);
        tilExperience = (TextInputLayout) findViewById(R.id.tilExperience);
        tilPrice = (TextInputLayout) findViewById(R.id.tilPrice);

        tieEmail = (TextInputEditText) findViewById(R.id.tieEmail);
        tiePersonName = (TextInputEditText) findViewById(R.id.tiePersonName);
        tieExperience = (TextInputEditText) findViewById(R.id.tieExperience);
        tiePrice = (TextInputEditText) findViewById(R.id.tiePrice);

        checkBoxFree = (CheckBox) findViewById(R.id.checkBoxFree);
        btnSave = (Button) findViewById(R.id.btnSave);
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
            HttpPost httpPost = new HttpPost(AppUrls.UPDATE_GURU_SWAMY_DETAILS_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });
                //entity.addPart("id", new StringBody(strGuruSwamyId));
                entity.addPart("name", new StringBody(strPersonName));
                entity.addPart("email", new StringBody(strEmail));
                entity.addPart("experience", new StringBody(strExperience));
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
                    Toast.makeText(GuruSwamyActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                   /* Intent intent = new Intent(GuruSwamyActivity.this, VendorRegistrationFragment.class);
                    //intent.putExtra("USER_ID", AppConstants.USER_ID);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                    finish();

                } else if (jsonObject.getBoolean("error")) {
                    Toast.makeText(GuruSwamyActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
