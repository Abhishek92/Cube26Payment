package com.android.cube26payment.network;

import com.android.cube26payment.model.PaymentGatewayApiHit;
import com.android.cube26payment.model.PaymentGatewayList;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by hp pc on 12-03-2016.
 */
public interface CubePaymentApiInterface {

    @GET("/api/payment_portals?type=json&query=list_gateway")
    void getPaymentPortalList(Callback<PaymentGatewayList> paymentGatewayListCallback);
    @GET("/api/payment_portals?type=json&query=api_hits")
    void getApiCounter(Callback<PaymentGatewayApiHit> paymentGatewayApiHitCallback);
}
