package com.shiva.ayapadeeksha.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shiva.ayapadeeksha.Activities.DetailsGuruSwamyActivity;
import com.shiva.ayapadeeksha.Activities.DetailsMusicalBandActivity;
import com.shiva.ayapadeeksha.POJOClass.GuruSwamys;
import com.shiva.ayapadeeksha.POJOClass.MusicalBands;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.GrantPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MusicalBandsAdapter extends RecyclerView.Adapter<MusicalBandsAdapter.MyViewHolder> {
    private Activity activity;
    List<MusicalBands> musicalBandsList;
    private ArrayList<MusicalBands> arraylist;
    private String strEmail, strBandName, strContactNumber;
    public RequestOptions requestOptions;

    public MusicalBandsAdapter(Activity activity, List<MusicalBands> musicalBandsList) {
        this.activity = activity;
        this.musicalBandsList = musicalBandsList;

        arraylist = new ArrayList<MusicalBands>();
        arraylist.addAll(musicalBandsList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_musical_bands, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.imgayyapa);
        requestOptions.error(R.drawable.imgayyapa);
        final MusicalBands musicalBands = musicalBandsList.get(position);

        holder.tvId.setText(musicalBands.getStrId());
        holder.tvEmail.setText(musicalBands.getStrEmail());
        holder.tvBandName.setText(musicalBands.getStrBandName());
        holder.tvNoOfPersons.setText("No. of Persons : " + musicalBands.getStrNoOfPersons());
        holder.tvAddress.setText(musicalBands.getStrAddress());

        Glide.with(activity).load(AppUrls.IMAGE_URL + musicalBands.getStrImage())
                .thumbnail(0.5f)
                .apply(requestOptions)
                .into(holder.imgUser);
        holder.fabContactNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strContactNumber = musicalBands.getStrContactNo();
                String[] permissions = {Manifest.permission.CALL_PHONE};
                askPermissions(permissions);
            }
        });
    }

    /*======================= run time permissions ==================================*/
    private void askPermissions(final String[] permissions) {
        AppUtils.checkPermission(activity, permissions, new GrantPermissions() {
            @Override
            public void onGranted(Boolean status) {
                if (status) {
                    callIntent(strContactNumber);
                } else {
                    askPermissions(permissions);
                }
            }
        });
    }

    /*======================================== call intent ========================*/
    private void callIntent(final String strContactNo) {
        Intent intentCall = new Intent(Intent.ACTION_CALL);
        intentCall.setData(Uri.parse("tel:" + strContactNo));
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        intentCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intentCall);
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        musicalBandsList.clear();
        if (charText.length() == 0) {
            musicalBandsList.addAll(arraylist);
        } else {
            for (MusicalBands wp : arraylist) {
                if (wp.getStrBandName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    musicalBandsList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return musicalBandsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgUser;
        public TextView tvId, tvBandName, tvNoOfPersons, tvEmail, tvAddress;
        public FloatingActionButton fabContactNumber;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tvId);
            tvBandName = (TextView) itemView.findViewById(R.id.tvBandName);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            tvNoOfPersons = (TextView) itemView.findViewById(R.id.tvNoOfPersons);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            imgUser = (ImageView) itemView.findViewById(R.id.imgUser);

            fabContactNumber = (FloatingActionButton) itemView.findViewById(R.id.fabContactNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    strEmail = tvEmail.getText().toString();
                    strBandName = tvBandName.getText().toString();

                    Intent intent = new Intent(activity, DetailsMusicalBandActivity.class);
                    intent.putExtra("EMAIL", strEmail);
                    intent.putExtra("BAND_NAME", strBandName);
                    activity.startActivity(intent);

                }
            });
        }
    }
}