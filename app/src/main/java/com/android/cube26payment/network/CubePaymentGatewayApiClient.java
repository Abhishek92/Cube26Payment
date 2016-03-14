package com.android.cube26payment.network;

import retrofit.RestAdapter;

/**
 * Created by hp pc on 12-03-2016.
 */
public final class CubePaymentGatewayApiClient {

    private CubePaymentGatewayApiClient(){}

    private final static String API_URL = "http://hackerearth.0x10.info/";
    private static CubePaymentApiInterface cubePaymentApiInterface;

    private static RestAdapter getRestAdapter()
    {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        return restAdapter;
    }

    public static CubePaymentApiInterface getCubePaymentApiInterface() {
        if (cubePaymentApiInterface == null)
            cubePaymentApiInterface = getRestAdapter().create(CubePaymentApiInterface.class);

        return cubePaymentApiInterface;
    }
}
