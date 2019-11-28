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
import com.shiva.ayapadeeksha.POJOClass.GuruSwamys;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.GrantPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class GuruSwamysAdapter extends
        RecyclerView.Adapter<GuruSwamysAdapter.MyViewHolder> {
    private Activity activity;
    List<GuruSwamys> guruSwamysList;
    private ArrayList<GuruSwamys> arraylist;
    public RequestOptions requestOptions;
    private String strEmail, strPersonName,strContactNumber;

    public GuruSwamysAdapter(Activity activity, List<GuruSwamys> guruSwamysList) {
        this.activity = activity;
        this.guruSwamysList = guruSwamysList;

        arraylist = new ArrayList<GuruSwamys>();
        arraylist.addAll(guruSwamysList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_guru_swamys, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.imgayyapa);
        requestOptions.error(R.drawable.imgayyapa);

        final GuruSwamys guruSwamys = guruSwamysList.get(position);

        holder.tvId.setText(guruSwamys.getStrId());
        holder.tvEmail.setText(guruSwamys.getStrEmail());
        holder.tvPersonName.setText(guruSwamys.getStrName());
        holder.tvExperience.setText("Experience : " + guruSwamys.getStrExperience() + " Years");
        holder.tvAddress.setText(guruSwamys.getStrAddress());

        Glide.with(activity).load(AppUrls.IMAGE_URL + guruSwamys.getStrImage())
                .thumbnail(0.5f)
                .apply(requestOptions)
                .into(holder.imgUser);

        holder.fabContactNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strContactNumber = guruSwamys.getStrContactNo();
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
        guruSwamysList.clear();
        if (charText.length() == 0) {
            guruSwamysList.addAll(arraylist);
        } else {
            for (GuruSwamys wp : arraylist) {
                if (wp.getStrName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    guruSwamysList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return guruSwamysList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imgUser;
        public TextView tvId, tvPersonName, tvExperience, tvEmail, tvAddress;
        public FloatingActionButton fabContactNumber;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tvId);
            tvPersonName = (TextView) itemView.findViewById(R.id.tvPersonName);
            tvExperience = (TextView) itemView.findViewById(R.id.tvExperience);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            imgUser = (CircleImageView) itemView.findViewById(R.id.imgUser);
            fabContactNumber = (FloatingActionButton) itemView.findViewById(R.id.fabContactNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    strEmail = tvEmail.getText().toString();
                    strPersonName = tvPersonName.getText().toString();
                    Intent intent = new Intent(activity, DetailsGuruSwamyActivity.class);
                    intent.putExtra("EMAIL", strEmail);
                    intent.putExtra("PERSON_NAME", strPersonName);
                    activity.startActivity(intent);
                }
            });
        }
    }
}