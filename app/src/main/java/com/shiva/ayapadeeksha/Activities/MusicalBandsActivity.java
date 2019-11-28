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
import android.widget.Toast;

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

public class MusicalBandsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String strMusicalBandId,strMusicalBandEmail;
    private TextInputLayout tilEmail, tilBandName, tilNoOfPersons, tilPrice;
    private TextInputEditText tieEmail, tieBandName, tieNoOfPersons, tiePrice;
    private String strEmail, strBandName, strNoOfPersons, strPrice;
    private CheckBox checkBoxFree;
    private Button btnSave;
    private ProgressBar progressBar;
    private long totalSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musical_bands);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicalBandsActivity.this, RegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("Musical Band Details");
        strMusicalBandId = getIntent().getStringExtra("MUSICAL_BAND_ID");
        strMusicalBandEmail = getIntent().getStringExtra("MUSICAL_BAND_EMAIL");
        init();
        tieEmail.setText(strMusicalBandEmail);
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
                strBandName = tieBandName.getText().toString();
                strNoOfPersons = tieNoOfPersons.getText().toString();
                strPrice = tiePrice.getText().toString();

                if (strEmail.isEmpty())
                    setErrorMessage(tilEmail, "Email Required");
                else if (strBandName.isEmpty())
                    setErrorMessage(tilBandName, "Person Name Required");
                else if (strNoOfPersons.isEmpty())
                    setErrorMessage(tilNoOfPersons, "Experience Required");
                else if (strPrice.isEmpty())
                    setErrorMessage(tilPrice, "Price Required");
                else
                    new UpdateUser().execute();
                    //saveMusicalBandsDetails();
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

        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        tilBandName = (TextInputLayout) findViewById(R.id.tilBandName);
        tilNoOfPersons = (TextInputLayout) findViewById(R.id.tilNoOfPersons);
        tilPrice = (TextInputLayout) findViewById(R.id.tilPrice);

        tieEmail = (TextInputEditText) findViewById(R.id.tieEmail);
        tieBandName = (TextInputEditText) findViewById(R.id.tieBandName);
        tieNoOfPersons = (TextInputEditText) findViewById(R.id.tieNoOfPersons);
        tiePrice = (TextInputEditText) findViewById(R.id.tiePrice);

        checkBoxFree = (CheckBox) findViewById(R.id.checkBoxFree);
        btnSave = (Button) findViewById(R.id.btnSave);
    }

    private void saveMusicalBandsDetails() {
        if (AppUtils.isOnline(MusicalBandsActivity.this)) {
            AppUtils.saveMusicalBandsDetails(AppUrls.MUSIC_BAND_DETAILS_URL,
                    progressBar, MusicalBandsActivity.this,
                    strEmail, strBandName, strNoOfPersons,
                    strPrice, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                switch (response.getString("error")) {
                                    case "false":
                                        Toast.makeText(MusicalBandsActivity.this,
                                                response.getString("message"),
                                                Toast.LENGTH_LONG).show();
                                        finish();
                                        break;
                                    case "true":
                                        Toast.makeText(MusicalBandsActivity.this,
                                                response.getString("error_msg"),
                                                Toast.LENGTH_LONG).show();
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else AppUtils.showSnackBar(MusicalBandsActivity.this);
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
            HttpPost httpPost = new HttpPost(AppUrls.UPDATE_MUSICAL_BAND_DETAILS_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });
                //entity.addPart("id", new StringBody(strMusicalBandId));
                entity.addPart("band_name", new StringBody(strBandName));
                entity.addPart("email", new StringBody(strEmail));
                entity.addPart("no_of_persons", new StringBody(strNoOfPersons));
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
                    Toast.makeText(MusicalBandsActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                    AppConstants.VENDORS_ID = strMusicalBandId;
                    /*Intent intent = new Intent(EditMusicalBandsActivity.this, EditProfileActivity.class);
                     *//*intent.putExtra("USER_ID", AppConstants.USER_ID);
                    intent.putExtra("ID", jsonObject.getString("id"));*//*
                    intent.putExtra("EMAIL", jsonObject.getString("email"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/

                    finish();

                } else if (jsonObject.getBoolean("error")) {
                    Toast.makeText(MusicalBandsActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
