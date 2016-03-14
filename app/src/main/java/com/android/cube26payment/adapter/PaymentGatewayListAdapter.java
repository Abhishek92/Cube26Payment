package com.android.cube26payment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.cube26payment.R;
import com.android.cube26payment.model.PaymentGateway;
import com.android.cube26payment.model.PaymentGatewayList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp pc on 12-03-2016.
 */
public class PaymentGatewayListAdapter extends RecyclerView.Adapter<PaymentGatewayListAdapter.ViewHolder> {

    private Context mContext;
    private List<PaymentGateway> mPaymentGatewayList;
    private List<PaymentGateway> mBackupList;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;
    private OnItemClickListener mOnItemClickListener;


    public PaymentGatewayListAdapter(Context context, List<PaymentGateway> paymentGatewayList) {
        mContext = context;
        mPaymentGatewayList = new ArrayList<>(paymentGatewayList);
        mBackupList = new ArrayList<>(paymentGatewayList);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.payment_gateway_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PaymentGateway data = mPaymentGatewayList.get(position);
        holder.paymentGatewayName.setText(data.getName());
        holder.paymentGatewayDescription.setText(data.getDescription());
        Picasso.with(mContext).load(data.getImage())
                .error(R.drawable.placeholder)
                .into(holder.paymentGatewayNameThumbnail);
        holder.paymentGatewayRootView.setTag(data);
        setAnimation(holder.paymentGatewayRootView, position);
    }

    @Override
    public int getItemCount() {
        return mPaymentGatewayList.size();
    }

    public void flushFilter(){
        mPaymentGatewayList = new ArrayList<>();
        mPaymentGatewayList.addAll(mBackupList);
        notifyDataSetChanged();
    }

    public void setFilter(String queryText) {

        mPaymentGatewayList = new ArrayList<>();
        queryText = queryText.toLowerCase();
        for (PaymentGateway item: mBackupList) {
            if (item.getName().toLowerCase().contains(queryText))
                mPaymentGatewayList.add(item);
            else if(item.getCurrencies().toLowerCase().contains(queryText))
                mPaymentGatewayList.add(item);
        }
        notifyDataSetChanged();
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void setItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView paymentGatewayNameThumbnail;
        protected TextView paymentGatewayName;
        protected TextView paymentGatewayDescription;
        protected View paymentGatewayRootView;

        public ViewHolder(View itemView) {
            super(itemView);
            paymentGatewayNameThumbnail = (ImageView) itemView.findViewById(R.id.payment_gateway_thumbnail);
            paymentGatewayName = (TextView) itemView.findViewById(R.id.payment_gateway_name);
            paymentGatewayDescription = (TextView) itemView.findViewById(R.id.payment_gateway_description);
            paymentGatewayRootView = itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

}