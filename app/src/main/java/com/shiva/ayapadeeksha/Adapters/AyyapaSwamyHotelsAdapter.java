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
import com.shiva.ayapadeeksha.Activities.DetailsAyyappaHotelsActivity;
import com.shiva.ayapadeeksha.Activities.DetailsMandapVendorsActivity;
import com.shiva.ayapadeeksha.POJOClass.AyyapaSwamyHotels;
import com.shiva.ayapadeeksha.POJOClass.GuruSwamys;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.GrantPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AyyapaSwamyHotelsAdapter extends
        RecyclerView.Adapter<AyyapaSwamyHotelsAdapter.MyViewHolder> {
    private Activity activity;
    List<AyyapaSwamyHotels> ayyapaSwamyHotelsList;
    private ArrayList<AyyapaSwamyHotels> arraylist;
    private String strEmail, strAyyappaSwamyHotelName,strContactNumber;
    public RequestOptions requestOptions;

    public AyyapaSwamyHotelsAdapter(Activity activity, List<AyyapaSwamyHotels> ayyapaSwamyHotelsList) {
        this.activity = activity;
        this.ayyapaSwamyHotelsList = ayyapaSwamyHotelsList;

        arraylist = new ArrayList<AyyapaSwamyHotels>();
        arraylist.addAll(ayyapaSwamyHotelsList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_ayyappa_swamy_hotels, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.imgayyapa);
        requestOptions.error(R.drawable.imgayyapa);
        final AyyapaSwamyHotels ayyapaSwamyHotels = ayyapaSwamyHotelsList.get(position);

        holder.tvId.setText(ayyapaSwamyHotels.getStrid());
        holder.tvHotelName.setText(ayyapaSwamyHotels.getStrHotelName());
        holder.tvEmail.setText(ayyapaSwamyHotels.getStrEmail());

        if (ayyapaSwamyHotels.getStrLandmark().equals("")) {
            holder.tvLocation.setText(ayyapaSwamyHotels.getStrFlatno() + "," +
                    ayyapaSwamyHotels.getStrLocality() + "," +
                    ayyapaSwamyHotels.getStrCity() + "," +
                    ayyapaSwamyHotels.getStrState() + "," +
                    ayyapaSwamyHotels.getStrPincode());
        } else {
            holder.tvLocation.setText(ayyapaSwamyHotels.getStrFlatno() + "," +
                    ayyapaSwamyHotels.getStrLocality() + "," +
                    ayyapaSwamyHotels.getStrLandmark() + "," +
                    ayyapaSwamyHotels.getStrCity() + "," +
                    ayyapaSwamyHotels.getStrState() + "," +
                    ayyapaSwamyHotels.getStrPincode());
        }

        Glide.with(activity).load(AppUrls.IMAGE_URL + ayyapaSwamyHotels.getStrImage())
                .thumbnail(0.5f)
                .apply(requestOptions)
                .into(holder.imgUser);
        holder.fabContactNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strContactNumber = ayyapaSwamyHotels.getStrContactNo();
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
        ayyapaSwamyHotelsList.clear();
        if (charText.length() == 0) {
            ayyapaSwamyHotelsList.addAll(arraylist);
        } else {
            for (AyyapaSwamyHotels wp : arraylist) {
                if (wp.getStrHotelName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    ayyapaSwamyHotelsList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ayyapaSwamyHotelsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgUser;
        public TextView tvId, tvHotelName, tvLocation, tvEmail;
        public FloatingActionButton fabContactNumber;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tvId);
            tvHotelName = (TextView) itemView.findViewById(R.id.tvHotelName);
            tvLocation = (TextView) itemView.findViewById(R.id.tvLocation);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            imgUser = (ImageView) itemView.findViewById(R.id.imgUser);

            fabContactNumber = (FloatingActionButton) itemView.findViewById(R.id.fabContactNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    strEmail = tvEmail.getText().toString();
                    strAyyappaSwamyHotelName = tvHotelName.getText().toString();

                    Intent intent = new Intent(activity, DetailsAyyappaHotelsActivity.class);
                    intent.putExtra("EMAIL", strEmail);
                    intent.putExtra("AYYAPPA_HOTEL_NAME", strAyyappaSwamyHotelName);
                    activity.startActivity(intent);

                }
            });
        }
    }
}