package com.shiva.ayapadeeksha.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shiva.ayapadeeksha.POJOClass.TypeOfDeeksha;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;

public class EditSwamyProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button btnUpdate;
    private TextInputLayout tilUserName, tilDeeksha, tilContactNumber;
    private TextInputEditText tieUserName, tieDeeksha, tieContactNumber;
    private ProgressBar progressBar;
    private String strTypeOfDeekshaId, strUserId, strUserName, strDeekshaName, strContactNumber, strCurrentPhotoPath;
    private AlertDialog alertDialog;
    private TypesOfDeekshaAdapter typesOfDeekshaAdapter;
    private long totalSize = 0;
    private CircleImageView imgProfile;
    public RequestOptions requestOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_swamy_profile);
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
        setTitle("My Swamy Profile");
        init();
        strTypeOfDeekshaId = AppConstants.TYPE_OF_DEEKSHA_ID;
        strUserId = getIntent().getStringExtra("USER_ID");
        AppConstants.USER_ID = strUserId;
        getSwamyDetails(strUserId);

        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_img_person);
        requestOptions.error(R.drawable.ic_img_person);
    }

    private void getSwamyDetails(final String strUserId) {
        if (AppUtils.isOnline(EditSwamyProfileActivity.this)) {
            AppUtils.openLaunchScreen(AppUrls.LAUNCH_SCREEN_URL, EditSwamyProfileActivity.this,
                    progressBar, strUserId, "1", new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                   /* Toast.makeText(EditSwamyProfileActivity.this, response.getString("Message"),
                                            Toast.LENGTH_LONG).show();*/

                                    tieUserName.setText(response.getString("user_name"));
                                    tieDeeksha.setText(response.getString("type"));
                                    tieContactNumber.setText(response.getString("contact_no"));

                                    Glide.with(EditSwamyProfileActivity.this)
                                            .load(AppUrls.IMAGE_URL + response.getString("userimage"))
                                            .thumbnail(0.5f)
                                            .apply(requestOptions)
                                            .into(imgProfile);
                                } else {
                                    Toast.makeText(EditSwamyProfileActivity.this, response.getString("error_msg"),
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else AppUtils.showSnackBar(EditSwamyProfileActivity.this);
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        imgProfile = (CircleImageView) findViewById(R.id.imgProfile);

        tilUserName = (TextInputLayout) findViewById(R.id.tilUserName);
        tilDeeksha = (TextInputLayout) findViewById(R.id.tilDeeksha);
        tilContactNumber = (TextInputLayout) findViewById(R.id.tilContactNumber);

        tieUserName = (TextInputEditText) findViewById(R.id.tieUserName);
        tieDeeksha = (TextInputEditText) findViewById(R.id.tieDeeksha);
        tieContactNumber = (TextInputEditText) findViewById(R.id.tieContactNumber);

        tieDeeksha.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        strUserName = tieUserName.getText().toString();
        strDeekshaName = tieDeeksha.getText().toString();
        strContactNumber = tieContactNumber.getText().toString();

        switch (v.getId()) {
            case R.id.tieDeeksha:
                openTypeOfDeekshaDialog();
                break;
            case R.id.imgProfile:
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                askPermissions(permissions);
                break;
            case R.id.btnUpdate:
                if (strDeekshaName.isEmpty())
                    tilDeeksha.setError("Type Of Deeksha Required");
                else if (strUserName.isEmpty())
                    tilUserName.setError("Enter your Name");
                else if (strContactNumber.isEmpty())
                    tilContactNumber.setError("Contact Number Required");
                else
                    new UpdateUser().execute();
                break;
        }
    }

    /*================================ Dialog type Of Deeksha =============================*/
    private void openTypeOfDeekshaDialog() {
        final AlertDialog.Builder typeOfDeekshaAlertDialog = new AlertDialog.Builder(EditSwamyProfileActivity.this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View deekshaDialogView = layoutInflater.inflate(R.layout.dialog_types_of_deeksha, null);
        typeOfDeekshaAlertDialog.setView(deekshaDialogView);
        typeOfDeekshaAlertDialog.setTitle("Type Of deeksha");

        final RecyclerView rVTypesOfDeeksha = (RecyclerView) deekshaDialogView.findViewById(R.id.rVTypesOfDeeksha);
        final ProgressBar progressBar = (ProgressBar) deekshaDialogView.findViewById(R.id.progressBar);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rVTypesOfDeeksha.setLayoutManager(linearLayoutManager);

        final ArrayList<TypeOfDeeksha> typeOfDeekshaArrayList = new ArrayList<>();
        if (AppUtils.isOnline(EditSwamyProfileActivity.this)) {
            AppUtils.getTypeOfDeeksha(AppUrls.TYPE_OF_DEEKSHA_URL, EditSwamyProfileActivity.this,
                    progressBar, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (response.getString("error").equals("false")) {
                                    JSONArray jsonArray = response.getJSONArray("Deeksha Types");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        TypeOfDeeksha typeOfDeeksha = new TypeOfDeeksha();
                                        typeOfDeeksha.setStrId(jsonObject.getString("id"));
                                        typeOfDeeksha.setStrName(jsonObject.getString("name"));

                                        typeOfDeekshaArrayList.add(typeOfDeeksha);
                                        typesOfDeekshaAdapter = new TypesOfDeekshaAdapter(EditSwamyProfileActivity.this, typeOfDeekshaArrayList);
                                        rVTypesOfDeeksha.setAdapter(typesOfDeekshaAdapter);
                                        typesOfDeekshaAdapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            AppUtils.showSnackBar(EditSwamyProfileActivity.this);
        }
        typeOfDeekshaAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog = typeOfDeekshaAlertDialog.create();
        alertDialog.show();
    }

    /*=========================== Type Of Deeksha Adapter ==============================*/
    public class TypesOfDeekshaAdapter extends RecyclerView.Adapter<TypesOfDeekshaAdapter.MyViewHolder> {
        private Activity activity;
        private List<TypeOfDeeksha> typeOfDeekshasList;

        public TypesOfDeekshaAdapter(Activity activity, List<TypeOfDeeksha> typeOfDeekshasList) {
            this.activity = activity;
            this.typeOfDeekshasList = typeOfDeekshasList;
        }

        @Override
        public TypesOfDeekshaAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.list_item_type_of_deeksha, viewGroup, false);

            return new TypesOfDeekshaAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TypesOfDeekshaAdapter.MyViewHolder holder, int i) {
            final TypeOfDeeksha typeOfDeeksha = typeOfDeekshasList.get(i);
            holder.tvId.setText(typeOfDeeksha.getStrId());
            holder.tvTypeOfDeekshaName.setText(typeOfDeeksha.getStrName());
        }

        @Override
        public int getItemCount() {
            return typeOfDeekshasList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvId, tvTypeOfDeekshaName;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvId = (TextView) itemView.findViewById(R.id.tvId);
                tvTypeOfDeekshaName = (TextView) itemView.findViewById(R.id.tvTypeOfDeekshaName);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        strTypeOfDeekshaId = tvId.getText().toString();
                        tieDeeksha.setText(tvTypeOfDeekshaName.getText().toString());
                        alertDialog.dismiss();
                    }
                });
            }
        }
    }

    /*========================== askPermissions ================================*/
    private void askPermissions(final String[] permissions) {
        AppUtils.checkPermission(EditSwamyProfileActivity.this, permissions, new GrantPermissions() {
            @Override
            public void onGranted(Boolean permissionStatus) {
                if (permissionStatus)
                    openTedBottomPicker();
                else
                    askPermissions(permissions);
            }
        });
    }

    /*============================ TedBottom Picker for images =================================*/
    private void openTedBottomPicker() {
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(EditSwamyProfileActivity.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        // here is selected uri
                        strCurrentPhotoPath = uri.getPath();
                        Glide.with(EditSwamyProfileActivity.this)
                                .load(strCurrentPhotoPath)
                                .thumbnail(0.5f)
                                .into(imgProfile);
                    }
                })
                .create();

        tedBottomPicker.show(getSupportFragmentManager());
    }

    /*============================= New User Registration Adapter ==========================*/
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
            HttpPost httpPost = new HttpPost(AppUrls.UPDATE_SWAMY_PROFILE_URL);

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
                entity.addPart("id", new StringBody(strUserId));
                entity.addPart("username", new StringBody(strUserName));
                entity.addPart("deeksha_type", new StringBody(strDeekshaName));
                entity.addPart("contact_no", new StringBody(strContactNumber));

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
                    Toast.makeText(EditSwamyProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                    getSwamyDetails(strUserId);

                } else if (jsonObject.getBoolean("error")) {
                    Toast.makeText(EditSwamyProfileActivity.this, jsonObject.getString("error_msg"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
