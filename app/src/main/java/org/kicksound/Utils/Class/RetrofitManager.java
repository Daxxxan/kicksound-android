package org.kicksound.Utils.Class;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static RetrofitManager instance = null;
    private String url;
    private Retrofit retrofit;

    private RetrofitManager() {}

    public static RetrofitManager getInstance() {
        if(instance == null) {
            instance = new RetrofitManager();
        }
        return instance;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();
        this.url = url;
        this.retrofit = new Retrofit.Builder()
                .baseUrl(this.url).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
