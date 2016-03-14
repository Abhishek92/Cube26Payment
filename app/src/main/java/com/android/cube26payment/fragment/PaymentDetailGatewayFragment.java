package com.android.cube26payment.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.cube26payment.R;
import com.android.cube26payment.model.PaymentGateway;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentDetailGatewayFragment extends Fragment {


    public static final String TAG = "PaymentDetailGatewayFragment";
    private PaymentGateway mPaymentGatewayData;
    public PaymentDetailGatewayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().containsKey(PaymentGatewayListFragment.PAYMENT_GATEWAY_DATA)){
            mPaymentGatewayData = getArguments().getParcelable(PaymentGatewayListFragment.PAYMENT_GATEWAY_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_detail_gateway, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView)view.findViewById(R.id.rating)).setText("Rating: " + mPaymentGatewayData.getRating());
        ((TextView)view.findViewById(R.id.fee)).setText("Transaction fee: "+mPaymentGatewayData.getTransactionFees());
        String brandingStr = mPaymentGatewayData.getBranding().equalsIgnoreCase("1") ? "Yes": "No";
        ((TextView)view.findViewById(R.id.branding)).setText("Branding: "+brandingStr);
        ((TextView)view.findViewById(R.id.currencies)).setText("Supported currencies: "+mPaymentGatewayData.getCurrencies());
        ((TextView)view.findViewById(R.id.description)).setText(mPaymentGatewayData.getDescription());

        ImageView mImageView = (ImageView) view.findViewById(R.id.payment_gateway_image);
        Picasso.with(getActivity()).load(mPaymentGatewayData.getImage()).fit()
                .error(R.drawable.placeholder)
                .into(mImageView);

    }


}
