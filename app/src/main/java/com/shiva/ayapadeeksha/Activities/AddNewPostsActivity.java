package com.shiva.ayapadeeksha.Activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shiva.ayapadeeksha.Fragments.AddPostFragment;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AndroidMultiPartEntity;
import com.shiva.ayapadeeksha.Utils.AppConstants;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.GrantPermissions;
import com.shiva.ayapadeeksha.Utils.VolleyCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import gun0912.tedbottompicker.TedBottomPicker;

public class AddNewPostsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView rvAddress;
    private ImageButton imgUserId;
    private ImageView imageView_pic;
 //   private TextInputEditText textDescription;
    private EditText Etxtdesc;
    private RadioButton Rdprivate, Rdpublic;
    private RadioGroup radiGroupPrivacy;
    private Button uploadButton, selectButton;
    private String strCurrentPhotoPath,strUserId, StrDescription, StrImage, StrType,StrVideo;
    private long totalSize = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_posts);
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


    private void init() {

        uploadButton = (Button) findViewById(R.id.uploadButton);
        selectButton = (Button) findViewById(R.id.button_selectpic);
        Etxtdesc = (EditText) findViewById(R.id.Etxtdesc);
        radiGroupPrivacy = (RadioGroup) findViewById(R.id.radiGroupPrivacy);
        Rdprivate = (RadioButton) findViewById(R.id.Rdprivate);
        Rdpublic = (RadioButton) findViewById(R.id.Rdpublic);

        imageView_pic=findViewById(R.id.imageView_pic);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                askPermissions(permissions);
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StrDescription = Etxtdesc.getText().toString();
                //strUser_id = editTextId.getText().toString();
                if (StrDescription.isEmpty())
                    Etxtdesc.setError("Required Description");
                else if (radiGroupPrivacy.getCheckedRadioButtonId() == -1)
                    Toast.makeText(AddNewPostsActivity.this, "Select any  Privacy", Toast.LENGTH_SHORT).show();
                /*else if (strUser_id.isEmpty())
                    editTextId.setText("Id Required");*/
                else
                    new UpdateUpload().execute();


            }

        });

        radiGroupPrivacy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.Rdprivate:
                        StrType = "1";
                        break;
                    case R.id.Rdpublic:
                        StrType = "0";
                        break;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("My Profile");
        strUserId = AppConstants.USER_ID;
        init();
    }



    /* permissions*/

    private void askPermissions(final String[] permissions) {
        AppUtils.checkPermission(AddNewPostsActivity.this, permissions, new GrantPermissions() {
            @Override
            public void onGranted(Boolean permissionStatus) {
                if (permissionStatus)
                    openTedBottomPicker();
                else
                    askPermissions(permissions);
            }
        });
    }
/* pick Image*/
private void openTedBottomPicker() {
    TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(AddNewPostsActivity.this)
            .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                @Override
                public void onImageSelected(Uri uri) {
                    // here is selected uri
                    strCurrentPhotoPath = uri.getPath();
                    Glide.with(AddNewPostsActivity.this).load(strCurrentPhotoPath)
                            .thumbnail(0.5f)
                            .into(imageView_pic);
                }
            })
            .create();

    tedBottomPicker.show(getSupportFragmentManager());



}


    /**private void getUserDetails() {
        if (AppUtils.isOnline(AddNewPostsActivity.this)) {
            AppUtils.getPosts(AppUrls.ADD_POST, (AddNewPostsActivity.this),
                    AppConstants.USER_ID, StrDescription,StrType, StrImage,StrVideo, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    Toast.makeText(AddNewPostsActivity.this, response.getString("Message"),
                                            Toast.LENGTH_LONG).show();
                                    // AppConstants.USER_ID = response.getString("user_id");
                                } else {
                                    Toast.makeText(AddNewPostsActivity.this, response.getString("error_msg"),
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        }
    }**/
    /* Update Uploads*/
    private class UpdateUpload extends AsyncTask<Void, Integer, String>

        {

            @Override
            protected void onPreExecute() {

            super.onPreExecute();
        }

            @Override
            protected String doInBackground(Void... voids) {
            return upLoadFile();
        }

            private String upLoadFile() {
            String responseString = null;

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(AppUrls.ADD_POST);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });
                if (strCurrentPhotoPath != null) {
                    File sourceFile = new File(strCurrentPhotoPath);
                    entity.addPart("image", new FileBody(sourceFile));
                }
                entity.addPart("user_id", new StringBody(AppConstants.USER_ID));
                entity.addPart("description", new StringBody(StrDescription));
               // entity.addPart("image", new StringBody(strCurrentPhotoPath));
              /*  if (strCurrentPhotoPath != null) {
                    File sourceFile = new File(strCurrentPhotoPath);
                    entity.addPart("image", new FileBody(sourceFile));
                }*/
                entity.addPart("privacy", new StringBody(StrType));



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
                    Toast.makeText(AddNewPostsActivity.this, jsonObject.getString("Message"), Toast.LENGTH_LONG).show();


                    Intent intent = new Intent(AddNewPostsActivity.this, NavigationDrawerActivity.class);
                    intent.putExtra("Add POSTS", AppConstants.USER_ID);
                    startActivity(intent);

                    //getUserDetails();

                } else  {
                    Toast.makeText(AddNewPostsActivity.this, jsonObject.getString("error_msg"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        }
    }


