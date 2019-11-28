package com.shiva.ayapadeeksha.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.shiva.ayapadeeksha.Activities.EditProfileActivity;
import com.shiva.ayapadeeksha.Activities.LoginActivity;
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

import static android.app.Activity.RESULT_OK;


public class AddPostFragment extends Fragment  {

    private ImageView imageView_pic;
    private ImageButton imgUserId, button_selectpic, imageVideo;
    private EditText editTextId;
    private Button btnUpload;
    private EditText etxtdesc;

    private ViewGroup root;
    private ProgressDialog dialog = null;
    private Uri filePath;
    private String selectFilePath;
    private String strEdittext, strCurrentPhotoPath;
    private RadioGroup radiGroupPrivacy;
    private RadioButton Rdprivate, Rdpublic;
    public String strUser_id, strDescription, strVideo, strImage, strPrivacy, strPrivate, strPublic;
    private long totalSize = 0;
    private static int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private TextInputEditText tieDescription;


    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    private String selectedPath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = (ViewGroup) inflater.inflate(R.layout.fragment_uploads, container, false);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppConstants.FRAGMENT_NAME = "Add Post";
        getActivity().setTitle("Add Posts");
        init();
    }


    private void init() {

        imgUserId = (ImageButton) root.findViewById(R.id.imgUserId);
        etxtdesc = (EditText) root.findViewById(R.id.etxtdesc);
        radiGroupPrivacy = (RadioGroup) root.findViewById(R.id.radiGroupPrivacy);
        Rdprivate = (RadioButton) root.findViewById(R.id.Rdprivate);
        Rdpublic = (RadioButton) root.findViewById(R.id.Rdpublic);
        imageView_pic = (ImageView) root.findViewById(R.id.imageView_pic);
       // imageVideo = (ImageButton) root.findViewById(R.id.imageVideo);
        btnUpload = (Button) root.findViewById(R.id.uploadButton);
        button_selectpic = (ImageButton) root.findViewById(R.id.button_selectpic);


        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);
        radiGroupPrivacy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.Rdprivate:
                        strPrivate = "1";
                        break;
                    case R.id.Rdpublic:
                        strPublic = "0";
                        break;
                }
            }
        });

        button_selectpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                askPermissions(permissions);


            }


        });

        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                strDescription = etxtdesc.getText().toString();
                //strUser_id = editTextId.getText().toString();
                if (strDescription.isEmpty())
                    etxtdesc.setError("Required Description");
                else if (radiGroupPrivacy.getCheckedRadioButtonId() == -1)
                    Toast.makeText(getContext(), "Select any  Privacy", Toast.LENGTH_SHORT).show();
                /*else if (strUser_id.isEmpty())
                    editTextId.setText("Id Required");*/
                else
                    new UpdateUpload().execute();


            }

        });
    }

    /*========================== askPermissions ================================*/
    private void askPermissions(final String[] permissions) {
        AppUtils.checkPermission(getActivity(), permissions, new GrantPermissions() {
            @Override
            public void onGranted(Boolean permissionStatus) {
                if (permissionStatus)
                    openTedBottomPicker();
                else
                    askPermissions(permissions);
            }
        });
    }

    private void openTedBottomPicker() {
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(getActivity())
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        // here is selected uri
                        strCurrentPhotoPath = uri.getPath();
                        Glide.with(getActivity()).load(strCurrentPhotoPath)
                                .thumbnail(0.5f)
                                .into(imageView_pic);
                    }
                })
                .create();

        tedBottomPicker.show(getActivity().getSupportFragmentManager());
    }

    /**  private void openTedBottomPicker() {
        AddPostFragment addPostFragment = new AddPostFragment().Builder(getContext())
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        // here is selected uri
                        strCurrentPhotoPath = uri.getPath();
                        Glide.with(getActivity())
                                .load(strCurrentPhotoPath)
                                .thumbnail(0.5f)
                                .into(imageView_pic);
                    }
                })
                .create();

        tedBottomPicker.show(getSupportFragmentManager());
    }**/
    //handling the image chooser activity result


    /**
     * private void getUserDetails(final String strUserId) {
     * if (AppUtils.isOnline(EditProfileActivity.this)) {
     * AppUtils.openLaunchScreen(AppUrls.LAUNCH_SCREEN_URL, EditProfileActivity.this,
     * progressBar, strUserId, "0", new VolleyCallback() {
     *
     * @Override public void onSuccess(String result) {
     * try {
     * JSONObject object = new JSONObject(result);
     * if (!object.getBoolean("error")) {
     * //Toast.makeText(EditProfileActivity.this, object.getString("Message"),
     * // Toast.LENGTH_LONG).show();
     * tvUserId.setText(object.getString("id"));
     * tieDeeksha.setText(object.getString("type"));
     * tieEmail.setText(object.getString("email"));
     * tieUserName.setText(object.getString("username"));
     * tieFlatNo.setText(object.getString("flatno"));
     * tieLocality.setText(object.getString("locality"));
     * tieLandMark.setText(object.getString("landmark"));
     * tieState.setText(object.getString("state"));
     * tieCity.setText(object.getString("city"));
     * tiePinCode.setText(object.getString("pincode"));
     * tieContactNumber.setText(object.getString("contactno"));
     * <p>
     * Glide.with(EditProfileActivity.this)
     * .load(AppUrls.IMAGE_URL + object.getString("image"))
     * .thumbnail(0.5f)
     * .into(imgProfile);
     * <p>
     * } else {
     * Toast.makeText(EditProfileActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
     * }
     * } catch (JSONException e) {
     * e.printStackTrace();
     * }
     * }
     * });
     * } else {
     * AppUtils.showSnackBar(EditProfileActivity.this);
     * }
     * }
     **/
    private void getUserDetails() {
        if (AppUtils.isOnline(getContext())) {
            AppUtils.getPosts(AppUrls.ADD_POST, getActivity(),
                    AppConstants.USER_ID, strDescription, "1", strImage, strVideo, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    Toast.makeText(getContext(), response.getString("Message"),
                                            Toast.LENGTH_LONG).show();
                                   // AppConstants.USER_ID = response.getString("user_id");
                                } else {
                                    Toast.makeText(getContext(), response.getString("error_msg"),
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        }
    }

    private class UpdateUpload extends AsyncTask<Void, Integer, String> {

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
               /* if (strCurrentPhotoPath != null) {
                    File sourceFile = new File(strCurrentPhotoPath);
                    entity.addPart("image", new FileBody(sourceFile));
                }*/
                entity.addPart("user_id", new StringBody(AppConstants.USER_ID));
                entity.addPart("description", new StringBody(strDescription));
                entity.addPart("image", new StringBody(strImage));
                entity.addPart("privacy", new StringBody("1"));
                entity.addPart("video", new StringBody(strVideo));


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
                    Toast.makeText(getContext(), jsonObject.getString("Message"), Toast.LENGTH_LONG).show();

                    getUserDetails();

                } else  {
                    Toast.makeText(getContext(), jsonObject.getString("error_msg"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
        }


