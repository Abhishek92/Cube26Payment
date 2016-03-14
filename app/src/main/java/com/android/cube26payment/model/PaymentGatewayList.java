package com.android.cube26payment.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp pc on 12-03-2016.
 */
public class PaymentGatewayList {

    @SerializedName("payment_gateways")
    @Expose
    private List<PaymentGateway> paymentGateways = new ArrayList<PaymentGateway>();

    /**
     *
     * @return
     * The paymentGateways
     */
    public List<PaymentGateway> getPaymentGateways() {
        return paymentGateways;
    }

    /**
     *
     * @param paymentGateways
     * The payment_gateways
     */
    public void setPaymentGateways(List<PaymentGateway> paymentGateways) {
        this.paymentGateways = paymentGateways;
    }


}
