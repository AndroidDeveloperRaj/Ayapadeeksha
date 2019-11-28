package com.shiva.ayapadeeksha.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.shiva.ayapadeeksha.Activities.NavigationDrawerActivity;
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

public class SwamyRegistrationFragment extends Fragment implements View.OnClickListener {
    public ViewGroup root;
    public ProgressBar progressBar;
    public TextInputLayout tilUserName, tilDeeksha, tilContactNumber, tilPassword;
    public TextInputEditText tieUserName, tieDeeksha, tiePassword, tieContactNumber;
    public Button btnRegister;
    private CircleImageView imgProfile;
    public String strUserName, strDeekshaName, strPassword, strContactNumber, strTypeOfDeekshaId,
            strLaunchScreenId, strSwamyLoginStatus, strCurrentPhotoPath;
    private AlertDialog alertDialog;
    private TypesOfDeekshaAdapter typesOfDeekshaAdapter;
    private long totalSize = 0;

    GoogleSignInClient mGoogleSignInClient;

    public SwamyRegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = (ViewGroup) inflater.inflate(R.layout.fragment_swamy_registration, container, false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Registration");
        init();
    }

    private void init() {
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        tilUserName = (TextInputLayout) root.findViewById(R.id.tilUserName);
        tilDeeksha = (TextInputLayout) root.findViewById(R.id.tilDeeksha);
        tilContactNumber = (TextInputLayout) root.findViewById(R.id.tilContactNumber);
        tilPassword = (TextInputLayout) root.findViewById(R.id.tilPassword);

        tieUserName = (TextInputEditText) root.findViewById(R.id.tieUserName);
        tieDeeksha = (TextInputEditText) root.findViewById(R.id.tieDeeksha);
        tiePassword = (TextInputEditText) root.findViewById(R.id.tiePassword);
        tieContactNumber = (TextInputEditText) root.findViewById(R.id.tieContactNumber);

        imgProfile = (CircleImageView) root.findViewById(R.id.imgProfile);

        btnRegister = (Button) root.findViewById(R.id.btnRegister);

      //  strDeekshaName="Ayyappa";
        strTypeOfDeekshaId ="name";
        tieDeeksha.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        strUserName = tieUserName.getText().toString();
        strDeekshaName = tieDeeksha.getText().toString();
        strPassword = tiePassword.getText().toString();
        strContactNumber = tieContactNumber.getText().toString();

        switch (v.getId()) {
            case R.id.tieDeeksha:
                openTypeOfDeekshaDialog();
                break;

            case R.id.imgProfile:
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                askPermissions(permissions);
                break;
            case R.id.btnRegister:
                if (strUserName.isEmpty())
                    tilUserName.setError("Enter your Name");
                else if (strContactNumber.isEmpty())
                    tilContactNumber.setError("Contact Number Required");
                else if (strPassword.isEmpty())
                    tilPassword.setError("Password Required");
                else
                    new RegisterNewUser().execute();
                break;
        }
    }

    private void gooSignInRegistration() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this.getActivity(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this.getActivity());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();


            new RegisterNewUser().execute();
//            nameTV.setText("Name: "+personName);
//            emailTV.setText("Email: "+personEmail);
//            idTV.setText("ID: "+personId);
//            Glide.with(this).load(personPhoto).into(photoIV);
        }
    }

    /*================================ Dialog type Of Deeksha =============================*/
    private void openTypeOfDeekshaDialog() {
        final AlertDialog.Builder typeOfDeekshaAlertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View deekshaDialogView = layoutInflater.inflate(R.layout.dialog_types_of_deeksha, null);
        typeOfDeekshaAlertDialog.setView(deekshaDialogView);
        typeOfDeekshaAlertDialog.setTitle("Type Of deeksha");

        final RecyclerView rVTypesOfDeeksha = (RecyclerView) deekshaDialogView.findViewById(R.id.rVTypesOfDeeksha);
        final ProgressBar progressBar = (ProgressBar) deekshaDialogView.findViewById(R.id.progressBar);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rVTypesOfDeeksha.setLayoutManager(linearLayoutManager);

        final ArrayList<TypeOfDeeksha> typeOfDeekshaArrayList = new ArrayList<>();
        if (AppUtils.isOnline(getActivity())) {
            AppUtils.getTypeOfDeeksha(AppUrls.TYPE_OF_DEEKSHA_URL, getActivity(),
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
                                        typesOfDeekshaAdapter = new TypesOfDeekshaAdapter(getActivity(), typeOfDeekshaArrayList);
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
            AppUtils.showSnackBar(getActivity());
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

    /* ========================== Checking Runtime Permissions for Camera and External Storage ========================= */
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

    /* ======================== Opening Ted Bottom Picker for Pick Image ============================= */
    private void openTedBottomPicker() {
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(getActivity())
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        // here is selected uri
                        strCurrentPhotoPath = uri.getPath();
                        Glide.with(getActivity()).load(strCurrentPhotoPath)
                                .thumbnail(0.5f)
                                .into(imgProfile);
                    }
                })
                .create();

        tedBottomPicker.show(getActivity().getSupportFragmentManager());
    }

    /*============================= New User Registration Adapter ==========================*/
    private class RegisterNewUser extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return upLoadFile();
        }

        private String upLoadFile() {
            String responseString = null;


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(AppUrls.SWAMY_REGISTER_URL);

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
                entity.addPart("username", new StringBody(strUserName));
                entity.addPart("type",new StringBody(strDeekshaName));
                entity.addPart("password", new StringBody(strPassword));
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
                JSONObject response = new JSONObject(s);
                if (!response.getBoolean("error")) {
                    Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();

                    AppConstants.USER_ID = response.getString("id");
                    AppConstants.USER_NAME = response.getString("username");
                    AppConstants.TYPE_OF_DEEKSHA = response.getString("type");
                    AppConstants.CONTACT_NO = response.getString("contactno");
                    AppConstants.TYPE_OF_DEEKSHA_ID = response.getString("type_id");

                    strSwamyLoginStatus = response.getString("status");


                    strLaunchScreenId = response.getString("id");
                    Intent intent = new Intent(getActivity(), NavigationDrawerActivity.class);
                    intent.putExtra("SWAMY_VENDOR_LOGIN_STATUS", strSwamyLoginStatus);
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences
                            ("LoginDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("LaunchScreenId", strLaunchScreenId);
                    // editor.putString("LoginContactNumber", strContactNumber);
                    //editor.putString("LoginPassword", strPassword);

                    editor.commit();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();

                } else if (response.getBoolean("error")) {
                    Toast.makeText(getActivity(), response.getString("error_msg"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
