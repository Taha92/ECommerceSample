package com.example.ecommerceapp.http;

import com.example.ecommerceapp.interfaces.iWebServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestService {

    private static iWebServices service;
    private static iWebServices serviceSync;
    private static OkHttpClient httpClient;
    private static OkHttpClient httpSyncClient;

    //region Helper methods for Retrofit default
    public static void initializeHttpService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpUtils.BASE_URL)
                .client(getDefaultHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        if (service == null) {
            service = retrofit.create(iWebServices.class);
        }

    }

    private static OkHttpClient getDefaultHttpClient() {

        if (httpClient == null) {

            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder().addHeader("Apikey", "F*S0pL0MoWmpYF8SAOFv&cER").build();
                    return chain.proceed(request);
                }
            });

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(interceptor);
            client.readTimeout(HttpUtils.READ_TIME_OUT, TimeUnit.SECONDS);
            client.connectTimeout(HttpUtils.CONNECTION_TIME_OUT, TimeUnit.SECONDS);

            httpClient = client.build();
        }

        return httpClient;
    }

    public static iWebServices getHttpService() {

        if (service == null) {
            throw new HttpServiceNotInitialized();
        }

        return service;
    }

    //endregion

    //region Helper method for Retrofit Sync
    public static void initializeSyncHttpService() {

        Gson gson = new GsonBuilder()
                .setLenient()
                //.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofitSync = new Retrofit.Builder()
                .baseUrl(HttpUtils.BASE_URL)
                .client(getSyncHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        if (serviceSync == null) {
            serviceSync = retrofitSync.create(iWebServices.class);
        }

    }

    private static OkHttpClient getSyncHttpClient() {
        if (httpSyncClient == null) {
            OkHttpClient.Builder b = new OkHttpClient.Builder();
            b.readTimeout(HttpUtils.READ_TIME_OUT, TimeUnit.SECONDS);
            b.connectTimeout(HttpUtils.CONNECTION_TIME_OUT, TimeUnit.SECONDS);
            b.interceptors().add(new LoggingInterceptor());
            b.interceptors().add(new SyncRequestInterceptor());
            httpSyncClient = b.build();
        }
        return httpSyncClient;
    }

    //endregion

}
