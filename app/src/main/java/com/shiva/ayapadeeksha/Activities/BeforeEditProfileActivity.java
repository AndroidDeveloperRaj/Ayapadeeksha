package com.shiva.ayapadeeksha.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shiva.ayapadeeksha.Adapters.UpdateAdapter;
import com.shiva.ayapadeeksha.POJOClass.TypesOfVendors;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppConstants;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BeforeEditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private CardView cVMyProfile, cVTypesOfVendors;
    private String strUserId, strTypeOfVendorsId, strEmail;
    final List<String> mList = new ArrayList<>();
    final List<String> mListIds = new ArrayList<>();
    private Button btnAddMore, btnCancel;
    private AlertDialog alertDialog, editAlertDialog;
    private ImageButton imgEdit, imgDelete;
    private TypesOfVendorsAdapter typesOfVendorsAdapter;
    private RemainingVendorsAdapter remainingVendorsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_edit_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit Your Profile");
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
        init();
        strUserId = getIntent().getStringExtra("USER_ID");
        strEmail = getIntent().getStringExtra("USER_EMAIL");
    }

    private void init() {
        cVMyProfile = (CardView) findViewById(R.id.cVMyProfile);
        cVTypesOfVendors = (CardView) findViewById(R.id.cVTypesOfVendors);

        imgDelete = (ImageButton) findViewById(R.id.imgDelete);
        imgEdit = (ImageButton) findViewById(R.id.imgEdit);

        cVMyProfile.setOnClickListener(this);
        cVTypesOfVendors.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cVMyProfile:
                Intent intent = new Intent(BeforeEditProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("USER_ID", strUserId);
                startActivity(intent);
                break;
            case R.id.cVTypesOfVendors:
                openTypeOfVendorsDialog();
                break;
        }
    }

    /*================================ Dialog type of vendors ==============================*/
    private void openTypeOfVendorsDialog() {
        final AlertDialog.Builder typeOfVendorsAlertDialog = new AlertDialog.Builder(BeforeEditProfileActivity.this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vendorsDialogView = layoutInflater.inflate(R.layout.dialog_edit_type_of_vendors, null);
        typeOfVendorsAlertDialog.setView(vendorsDialogView);
        typeOfVendorsAlertDialog.setCancelable(false);
        typeOfVendorsAlertDialog.setTitle("Type Of Vendors");

        final RecyclerView rvVendorsList = (RecyclerView) vendorsDialogView.findViewById(R.id.rvVendorsList);
        final ProgressBar progressBar = (ProgressBar) vendorsDialogView.findViewById(R.id.progressBar);
        btnAddMore = (Button) vendorsDialogView.findViewById(R.id.btnAddMore);
        btnCancel = (Button) vendorsDialogView.findViewById(R.id.btnCancel);

        final ArrayList<TypesOfVendors> typesOfVendorsArrayList = new ArrayList<>();

        rvVendorsList.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvVendorsList.setLayoutManager(linearLayoutManager);

        if (AppUtils.isOnline(BeforeEditProfileActivity.this)) {
            AppUtils.getCheckedVendors(AppUrls.CHECKED_LIST_VENDORS_URL, BeforeEditProfileActivity.this,
                    progressBar, strUserId, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    JSONArray jsonArray = response.getJSONArray("Vendor Types");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        TypesOfVendors typesOfVendors = new TypesOfVendors();
                                        typesOfVendors.setStrId(jsonObject.getString("id"));
                                        typesOfVendors.setStrName(jsonObject.getString("name"));

                                        typesOfVendorsArrayList.add(typesOfVendors);
                                        typesOfVendorsAdapter = new TypesOfVendorsAdapter(BeforeEditProfileActivity.this, typesOfVendorsArrayList);
                                        rvVendorsList.setAdapter(typesOfVendorsAdapter);
                                        typesOfVendorsAdapter.notifyDataSetChanged();
                                    }


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            AppUtils.showSnackBar(BeforeEditProfileActivity.this);
        }
        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRemainingVendorsDialog(strEmail);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                alertDialog.cancel();
            }
        });

        alertDialog = typeOfVendorsAlertDialog.create();
        alertDialog.show();
    }

    private void openRemainingVendorsDialog(final String strEmail) {
        final AlertDialog.Builder typeOfVendorsAlertDialog = new AlertDialog.Builder(BeforeEditProfileActivity.this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vendorsDialogView = layoutInflater.inflate(R.layout.dialog_remaining_vendors, null);
        typeOfVendorsAlertDialog.setView(vendorsDialogView);
        typeOfVendorsAlertDialog.setCancelable(false);
        typeOfVendorsAlertDialog.setTitle("Choose your choice");

        final RecyclerView rvRemainingVendorsList = (RecyclerView) vendorsDialogView.findViewById(R.id.rvRemainingVendorsList);
        final ProgressBar progressBar = (ProgressBar) vendorsDialogView.findViewById(R.id.progressBar);
        btnCancel = (Button) vendorsDialogView.findViewById(R.id.btnCancel);

        final ArrayList<TypesOfVendors> typesOfVendorsArrayList = new ArrayList<>();

        rvRemainingVendorsList.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvRemainingVendorsList.setLayoutManager(linearLayoutManager);

        if (AppUtils.isOnline(BeforeEditProfileActivity.this)) {
            AppUtils.getRemainingVendors(AppUrls.ADD_VENDORS_URL, BeforeEditProfileActivity.this,
                    progressBar, strEmail, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    JSONArray jsonArray = response.getJSONArray("Vendor Types");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        TypesOfVendors typesOfVendors = new TypesOfVendors();
                                        typesOfVendors.setStrId(jsonObject.getString("id"));
                                        typesOfVendors.setStrName(jsonObject.getString("name"));

                                        typesOfVendorsArrayList.add(typesOfVendors);
                                        remainingVendorsAdapter = new RemainingVendorsAdapter(BeforeEditProfileActivity.this, typesOfVendorsArrayList);
                                        rvRemainingVendorsList.setAdapter(remainingVendorsAdapter);
                                        remainingVendorsAdapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            AppUtils.showSnackBar(BeforeEditProfileActivity.this);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAlertDialog.dismiss();
            }
        });

        editAlertDialog = typeOfVendorsAlertDialog.create();
        editAlertDialog.show();
    }

    /*=========================== Type Of Vendor Adapter ==============================*/
    class TypesOfVendorsAdapter extends RecyclerView.Adapter<TypesOfVendorsAdapter.MyViewHolder> {
        private Activity activity;
        private ArrayList<TypesOfVendors> typesOfVendorsList;

        public TypesOfVendorsAdapter(Activity activity, ArrayList<TypesOfVendors> typesOfVendorsList) {
            this.activity = activity;
            this.typesOfVendorsList = typesOfVendorsList;
        }

        @Override
        public TypesOfVendorsAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.list_edit_type_of_vendors, viewGroup, false);
            return new TypesOfVendorsAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final TypesOfVendorsAdapter.MyViewHolder holder, final int i) {
            final TypesOfVendors typesOfVendors = typesOfVendorsList.get(i);

            holder.tvVendorId.setText(typesOfVendors.getStrId());
            holder.tvTypeOfVendorName.setText(typesOfVendors.getStrName());

            if (typesOfVendors.getStrId().equals("5")){
                holder.imgEdit.setVisibility(View.GONE);
            }
            holder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (typesOfVendors.getStrId().equals("1")) {
                        Intent intent = new Intent(BeforeEditProfileActivity.this, EditRegisGuruSwamyActivity.class);
                        intent.putExtra("GURU_SWAMY_ID", typesOfVendors.getStrId());
                        intent.putExtra("GURU_SWAMY_EMAIL", strEmail);
                        startActivity(intent);
                    }
                    if (typesOfVendors.getStrId().equals("2")) {
                        Intent intent = new Intent(BeforeEditProfileActivity.this, EditRegisMusicalBandActivity.class);
                        intent.putExtra("MUSICAL_BAND_ID", typesOfVendors.getStrId());
                        intent.putExtra("MUSICAL_BAND_EMAIL", strEmail);
                                /*AppConstants.MUSICAL_BAND_ID = tvVendorId.getText().toString();
                                AppConstants.MUSICAL_BAND_EMAIL = strEmail;*/
                        startActivity(intent);
                    }
                    if (typesOfVendors.getStrId().equals("3")) {
                        Intent intent = new Intent(BeforeEditProfileActivity.this, EditRegisMandapVendorsActivity.class);
                        intent.putExtra("MANDAP_VENDORS_ID", typesOfVendors.getStrId());
                        intent.putExtra("MANDAP_VENDORS_EMAIL", strEmail);
                        startActivity(intent);
                    }
                    if (typesOfVendors.getStrId().equals("4")) {
                        Intent intent = new Intent(BeforeEditProfileActivity.this, EditRegisAyyappaHotelsActivity.class);
                        intent.putExtra("AYYAPPA_HOTELS_ID", typesOfVendors.getStrId());
                        intent.putExtra("AYYAPPA_HOTELS_EMAIL", strEmail);
                        startActivity(intent);
                    }
                    if (typesOfVendors.getStrId().equals("5")) {
                        //imgEdit.setVisibility(View.GONE);
                        alertDialog.dismiss();
                    }
                }
            });

            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    strTypeOfVendorsId = typesOfVendors.getStrId();
                    openDeleteMethod(strTypeOfVendorsId);
                    alertDialog.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return typesOfVendorsList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvVendorId, tvTypeOfVendorName;
            public ImageButton imgEdit, imgDelete;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvVendorId = (TextView) itemView.findViewById(R.id.tvVendorId);
                tvTypeOfVendorName = (TextView) itemView.findViewById(R.id.tvTypeOfVendorName);

                imgEdit = (ImageButton) itemView.findViewById(R.id.imgEdit);
                imgDelete = (ImageButton) itemView.findViewById(R.id.imgDelete);
            }
        }
    }

    private void openDeleteMethod(final String strTypeOfVendorsId) {
        if (AppUtils.isOnline(BeforeEditProfileActivity.this)) {
            AppUtils.getDeleteVendors(AppUrls.DELETE_VENDORS_URL, BeforeEditProfileActivity.this,
                    strEmail, strTypeOfVendorsId, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                if (!response.getBoolean("error")) {
                                    Toast.makeText(BeforeEditProfileActivity.this,
                                            response.getString("Message"), Toast.LENGTH_LONG).show();
                                    //openDeleteMethod(strTypeOfVendorsId);
                                    openTypeOfVendorsDialog();
                                } else {
                                    Toast.makeText(BeforeEditProfileActivity.this,
                                            response.getString("error_msg"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            AppUtils.showSnackBar(BeforeEditProfileActivity.this);
        }
    }

    /*=========================== Type Of Vendor Adapter ==============================*/
    class RemainingVendorsAdapter extends RecyclerView.Adapter<RemainingVendorsAdapter.MyViewHolder> {
        private Activity activity;
        private ArrayList<TypesOfVendors> typesOfVendorsList;

        public RemainingVendorsAdapter(Activity activity, ArrayList<TypesOfVendors> typesOfVendorsList) {
            this.activity = activity;
            this.typesOfVendorsList = typesOfVendorsList;
        }

        @Override
        public RemainingVendorsAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.list_item_remaining_vendors, viewGroup, false);
            return new RemainingVendorsAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RemainingVendorsAdapter.MyViewHolder holder, final int i) {
            final TypesOfVendors typesOfVendors = typesOfVendorsList.get(i);

            holder.tvVendorId.setText(typesOfVendors.getStrId());
            holder.tvTypeOfVendorName.setText(typesOfVendors.getStrName());
        }

        @Override
        public int getItemCount() {
            return typesOfVendorsList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvVendorId, tvTypeOfVendorName;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvVendorId = (TextView) itemView.findViewById(R.id.tvVendorId);
                tvTypeOfVendorName = (TextView) itemView.findViewById(R.id.tvTypeOfVendorName);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*input param email*/
                        alertDialog.dismiss();
                        editAlertDialog.dismiss();
                        if (tvVendorId.getText().toString().equals("1")) {
                            Intent intent = new Intent(BeforeEditProfileActivity.this, EditGuruSwamyActivity.class);
                            intent.putExtra("GURU_SWAMY_ID", tvVendorId.getText().toString());
                            intent.putExtra("GURU_SWAMY_EMAIL", strEmail);
                            startActivity(intent);
                        }
                        if (tvVendorId.getText().toString().equals("2")) {
                            Intent intent = new Intent(BeforeEditProfileActivity.this, EditMusicalBandsActivity.class);
                            intent.putExtra("MUSICAL_BAND_ID", tvVendorId.getText().toString());
                            intent.putExtra("MUSICAL_BAND_EMAIL", strEmail);
                                /*AppConstants.MUSICAL_BAND_ID = tvVendorId.getText().toString();
                                AppConstants.MUSICAL_BAND_EMAIL = strEmail;*/
                            startActivity(intent);
                        }
                        if (tvVendorId.getText().toString().equals("3")) {
                            Intent intent = new Intent(BeforeEditProfileActivity.this, EditMandapVendorsActivity.class);
                            intent.putExtra("MANDAP_VENDORS_ID", tvVendorId.getText().toString());
                            intent.putExtra("MANDAP_VENDORS_EMAIL", strEmail);
                            startActivity(intent);
                        }
                        if (tvVendorId.getText().toString().equals("4")) {
                            Intent intent = new Intent(BeforeEditProfileActivity.this, EditAyyappaHotelsActivity.class);
                            intent.putExtra("AYYAPPA_HOTELS_ID", tvVendorId.getText().toString());
                            intent.putExtra("AYYAPPA_HOTELS_EMAIL", strEmail);
                            startActivity(intent);
                        }
                        if (tvVendorId.getText().toString().equals("5")) {
                            AppConstants.VENDORS_ID = tvVendorId.getText().toString();
                            new UpdateAdapter(BeforeEditProfileActivity.this).execute();
                        }
                    }
                });
            }
        }
    }
}
