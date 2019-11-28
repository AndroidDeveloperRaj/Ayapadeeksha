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
import com.shiva.ayapadeeksha.Activities.AddNewPadiPujaActivity;
import com.shiva.ayapadeeksha.Activities.DetailsPadipujaActivity;
import com.shiva.ayapadeeksha.POJOClass.GuruSwamys;
import com.shiva.ayapadeeksha.POJOClass.NearByPadiPujaDetailsPOJOClass;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.GrantPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NearByPadiPujaDetailsAdapter extends
        RecyclerView.Adapter<NearByPadiPujaDetailsAdapter.MyViewHolder> {
    public Activity activity;
    public List<NearByPadiPujaDetailsPOJOClass> nearByPadiPujaDetailsPOJOClassList;
    public String strContactNumber;
    public RequestOptions requestOptions;
    private ArrayList<NearByPadiPujaDetailsPOJOClass> arraylist;


    public NearByPadiPujaDetailsAdapter(Activity activity, ArrayList<NearByPadiPujaDetailsPOJOClass> nearByPadiPujaDetailsPOJOClassList) {
        this.activity = activity;
        this.nearByPadiPujaDetailsPOJOClassList = nearByPadiPujaDetailsPOJOClassList;

        arraylist = new ArrayList<NearByPadiPujaDetailsPOJOClass>();
        arraylist.addAll(nearByPadiPujaDetailsPOJOClassList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_near_by_padi_puja_details, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NearByPadiPujaDetailsAdapter.MyViewHolder holder, int i) {

        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ayyappa_sannidhanam);
        requestOptions.error(R.drawable.ayyappa_sannidhanam);

        final NearByPadiPujaDetailsPOJOClass nearByPadiPujaDetailsPOJOClass =
                nearByPadiPujaDetailsPOJOClassList.get(i);
        holder.tvId.setText(nearByPadiPujaDetailsPOJOClass.getStrUserId());
        holder.tvPersonName.setText(nearByPadiPujaDetailsPOJOClass.getStrPersonName());
        if (nearByPadiPujaDetailsPOJOClass.getStrLandmark().equals("")) {
            holder.tvAddress.setText(nearByPadiPujaDetailsPOJOClass.getStrFlatNo() + ", " +
                    nearByPadiPujaDetailsPOJOClass.getStrLocality() + ", " +
                    nearByPadiPujaDetailsPOJOClass.getStrCity() + ", " +
                    nearByPadiPujaDetailsPOJOClass.getStrState() + ", " +
                    nearByPadiPujaDetailsPOJOClass.getStrPincode());
        } else {
            holder.tvAddress.setText(nearByPadiPujaDetailsPOJOClass.getStrFlatNo() + ", " +
                    nearByPadiPujaDetailsPOJOClass.getStrLocality() + ", " +
                    nearByPadiPujaDetailsPOJOClass.getStrLandmark() + ", " +
                    nearByPadiPujaDetailsPOJOClass.getStrCity() + ", " +
                    nearByPadiPujaDetailsPOJOClass.getStrState() + ", " +
                    nearByPadiPujaDetailsPOJOClass.getStrPincode());
        }

        holder.tvDate.setText(nearByPadiPujaDetailsPOJOClass.getStrRegDate());
        holder.tvTime.setText(nearByPadiPujaDetailsPOJOClass.getStrRegTime());

        Glide.with(activity).load(AppUrls.IMAGE_URL + nearByPadiPujaDetailsPOJOClass.getStrImage())
                .thumbnail(0.5f)
                .apply(requestOptions)
                .into(holder.imgUser);

        holder.fabContactNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strContactNumber = nearByPadiPujaDetailsPOJOClass.getStrContactNo();
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

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        nearByPadiPujaDetailsPOJOClassList.clear();
        if (charText.length() == 0) {
            nearByPadiPujaDetailsPOJOClassList.addAll(arraylist);
        } else {
            for (NearByPadiPujaDetailsPOJOClass wp : arraylist) {
                if (wp.getStrPersonName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    nearByPadiPujaDetailsPOJOClassList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return nearByPadiPujaDetailsPOJOClassList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public ImageView imgUser;
        public TextView tvId, tvPersonName, tvAddress, tvDate, tvTime,tvEdit,tvDelete;
        public FloatingActionButton fabContactNumber;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgUser = (ImageView) itemView.findViewById(R.id.imgUser);

            tvId = (TextView) itemView.findViewById(R.id.tvId);
            tvPersonName = (TextView) itemView.findViewById(R.id.tvPersonName);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvDelete = (TextView) itemView.findViewById(R.id.tvDelete);
            tvEdit= (TextView) itemView.findViewById(R.id.tvEdit);
            fabContactNumber = (FloatingActionButton) itemView.findViewById(R.id.fabContactNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, DetailsPadipujaActivity.class);
                    intent.putExtra("USER_ID", tvId.getText().toString());
                    intent.putExtra("PERSON_NAME", tvPersonName.getText().toString());
                    activity.startActivity(intent);
                }
            });

           /* tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, AddNewPadiPujaActivity.class);
                    intent.putExtra("USER_ID", tvId.getText().toString());
                    intent.putExtra("PERSON_NAME", tvPersonName.getText().toString());
                    intent.putExtra("UPDATE_PADIPUJA", true);
                    activity.startActivity(intent);
                }
            });*/
        }


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
}
