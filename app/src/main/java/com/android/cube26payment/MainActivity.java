package com.android.cube26payment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cube26payment.fragment.PaymentGatewayListFragment;
import com.android.cube26payment.model.PaymentGatewayApiHit;
import com.android.cube26payment.network.CubePaymentGatewayApiClient;
import com.android.cube26payment.utils.AppUtils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private TextView mTotalApiCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTotalApiCount = (TextView) findViewById(R.id.total_api_count);
        getNoOfApiHits();
        attachPaymentListFragment();

    }

    private void attachPaymentListFragment(){
        PaymentGatewayListFragment fragment = new PaymentGatewayListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment, PaymentGatewayListFragment.TAG).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void getNoOfApiHits()
    {
        if(AppUtils.isNetworkConnected(this)) {
            CubePaymentGatewayApiClient.getCubePaymentApiInterface().getApiCounter(new Callback<PaymentGatewayApiHit>() {
                @Override
                public void success(PaymentGatewayApiHit paymentGatewayApiHit, Response response) {
                    if(response != null && paymentGatewayApiHit != null)
                        mTotalApiCount.setText("Total api hit count: "+paymentGatewayApiHit.getApiHits());
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }
}
