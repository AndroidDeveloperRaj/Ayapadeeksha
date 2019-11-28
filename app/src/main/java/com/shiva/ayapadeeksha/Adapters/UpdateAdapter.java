package com.shiva.ayapadeeksha.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.shiva.ayapadeeksha.Activities.BeforeEditProfileActivity;
import com.shiva.ayapadeeksha.Utils.AndroidMultiPartEntity;
import com.shiva.ayapadeeksha.Utils.AppConstants;
import com.shiva.ayapadeeksha.Utils.AppUrls;

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

public class UpdateAdapter extends AsyncTask<Void, Integer, String> {
    public Context context;
    public long totalSize = 0;
    public String strTypeOfVendorsId, strUserId;

    public UpdateAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        // progressBar.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        return upLoadFile();
    }

    private String upLoadFile() {
        String responseString = null;
        strTypeOfVendorsId = AppConstants.VENDORS_ID;
        strUserId = AppConstants.USER_ID;

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(AppUrls.PROFILE_UPDATE_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                @Override
                public void transferred(long num) {
                    publishProgress((int) ((num / (float) totalSize) * 100));
                }
            });

            entity.addPart("id", new StringBody(strUserId));
            entity.addPart("vendors", new StringBody(strTypeOfVendorsId));

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
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (!jsonObject.getBoolean("error")) {
                Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                /*Intent intent = new Intent(context, BeforeEditProfileActivity.class);
                intent.putExtra("USER_ID", AppConstants.USER_ID);
                context.startActivity(intent);*/


            } else if (jsonObject.getBoolean("error")) {
                Toast.makeText(context, jsonObject.getString("error_msg"), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
