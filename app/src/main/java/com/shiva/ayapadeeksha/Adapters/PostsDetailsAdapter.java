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
import com.shiva.ayapadeeksha.Activities.DetailsPadipujaActivity;
import com.shiva.ayapadeeksha.POJOClass.NearByPadiPujaDetailsPOJOClass;
import com.shiva.ayapadeeksha.POJOClass.PostDetailsPOJOClass;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppUrls;
import com.shiva.ayapadeeksha.Utils.AppUtils;
import com.shiva.ayapadeeksha.Utils.GrantPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostsDetailsAdapter extends
    RecyclerView.Adapter<PostsDetailsAdapter.MyViewHolder> {
    public Activity activity;
    public List<PostDetailsPOJOClass> postDetailsPOJOClassesList;
    public String strContactNumber;
    public RequestOptions requestOptions;
    private ArrayList<PostDetailsPOJOClass> arraylist;


    public PostsDetailsAdapter(Activity activity, ArrayList<PostDetailsPOJOClass> postDetailsPOJOClassesList) {
        this.activity = activity;
        this.postDetailsPOJOClassesList = postDetailsPOJOClassesList;

        arraylist = new ArrayList<PostDetailsPOJOClass>();
        arraylist.addAll(postDetailsPOJOClassesList);
    }

    @Override
    public PostsDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_wall_posts, viewGroup, false);
        return new PostsDetailsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PostsDetailsAdapter.MyViewHolder holder, int i) {

        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ayyappa_sannidhanam);
        requestOptions.error(R.drawable.ayyappa_sannidhanam);

        final PostDetailsPOJOClass postDetailsPOJOClass =
                postDetailsPOJOClassesList.get(i);
        holder.tvUserId.setText(postDetailsPOJOClass.getUser_id());
        holder.tvDescription.setText(postDetailsPOJOClass.getDescription());
        holder.tvImage.setText(postDetailsPOJOClass.getImage());
        holder.tvVideo.setText(postDetailsPOJOClass.getVideo());
        holder.tvprivacy.setText(postDetailsPOJOClass.getPrivacy());

        Glide.with(activity).load(AppUrls.IMAGE_URL + postDetailsPOJOClass.getImage())
                .thumbnail(0.5f)
                .apply(requestOptions)
                .into(holder.imgViewUser);

    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        postDetailsPOJOClassesList.clear();
        if (charText.length() == 0) {
            postDetailsPOJOClassesList.addAll(arraylist);
        } else {
            for (PostDetailsPOJOClass wp : arraylist) {
                if (wp.getImage().toLowerCase(Locale.getDefault()).contains(charText)) {
                    postDetailsPOJOClassesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return postDetailsPOJOClassesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgViewUser;
        public TextView tvUserId, tvImage, tvVideo, tvprivacy, tvDescription;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgViewUser = (ImageView) itemView.findViewById(R.id.imgViewUser);

            tvUserId = (TextView) itemView.findViewById(R.id.tvUserId);
            tvprivacy = (TextView) itemView.findViewById(R.id.tvprivacy);
            tvVideo = (TextView) itemView.findViewById(R.id.tvVideo);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
        }
    }

}