package com.android.cube26payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hp pc on 12-03-2016.
 */
public class PaymentGatewayApiHit {

    @SerializedName("api_hits")
    @Expose
    private String apiHits;

    /**
     *
     * @return
     * The apiHits
     */
    public String getApiHits() {
        return apiHits;
    }

    /**
     *
     * @param apiHits
     * The api_hits
     */
    public void setApiHits(String apiHits) {
        this.apiHits = apiHits;
    }

}
