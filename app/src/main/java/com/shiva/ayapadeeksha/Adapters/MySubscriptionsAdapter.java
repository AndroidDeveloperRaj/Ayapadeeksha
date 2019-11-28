package com.shiva.ayapadeeksha.Adapters;

import android.app.Activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/*
import com.shiva.ayapadeeksha.Activities.PaymentGatewayActivity;
*/
import com.shiva.ayapadeeksha.POJOClass.MySubscriptionsPOJOClass;
import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class MySubscriptionsAdapter extends RecyclerView.Adapter<MySubscriptionsAdapter.MyViewHolder> {
    private Activity activity;
    private List<MySubscriptionsPOJOClass> mySubscriptionsList;

    public MySubscriptionsAdapter(Activity activity, List<MySubscriptionsPOJOClass> mySubscriptionsList) {
        this.activity = activity;
        this.mySubscriptionsList = mySubscriptionsList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_my_subscriptions, viewGroup, false);
        return new MySubscriptionsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {
        final MySubscriptionsPOJOClass mySubscriptionsPOJOClass = mySubscriptionsList.get(i);

        holder.tvVendorId.setText(mySubscriptionsPOJOClass.getStrVendorId());
        holder.tvUserId.setText(mySubscriptionsPOJOClass.getStrUserId());
        holder.tvSubscriptionName.setText(mySubscriptionsPOJOClass.getStrVendorName());
        holder.tvRupee.setText(mySubscriptionsPOJOClass.getStrPrice());
        holder.tvPrice.setText("₹ " + mySubscriptionsPOJOClass.getStrPrice());
        holder.tvDuration.setText(mySubscriptionsPOJOClass.getStrPackageDuration());
        holder.tvValidity.setText("Validity : " + mySubscriptionsPOJOClass.getStrPackageDuration() + " Months");
        holder.btnSubscribe.setText(mySubscriptionsPOJOClass.getStrDescription());

    }


    @Override
    public int getItemCount() {
        return mySubscriptionsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSubscriptionName, tvPrice, tvValidity, tvVendorId, tvUserId, tvRupee, tvDuration;
        public Button btnSubscribe;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvVendorId = (TextView) itemView.findViewById(R.id.tvVendorId);
            tvUserId = (TextView) itemView.findViewById(R.id.tvUserId);
            tvSubscriptionName = (TextView) itemView.findViewById(R.id.tvSubscriptionName);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvValidity = (TextView) itemView.findViewById(R.id.tvValidity);
            tvRupee = (TextView) itemView.findViewById(R.id.tvRupee);
            tvDuration = (TextView) itemView.findViewById(R.id.tvDuration);

            btnSubscribe = (Button) itemView.findViewById(R.id.btnSubscribe);

            btnSubscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*Intent intent = new Intent(activity, PaymentGatewayActivity.class);
                    intent.putExtra("SUBSCRIPTION_NAME", tvSubscriptionName.getText().toString());
                    intent.putExtra("USER_ID", AppConstants.USER_ID);
                    intent.putExtra("VENDOR_ID", tvVendorId.getText().toString());
                    intent.putExtra("DURATION", tvDuration.getText().toString());
                    intent.putExtra("PRICE", tvRupee.getText().toString());
                    activity.startActivity(intent);*/
                }
            });
        }
    }
}
