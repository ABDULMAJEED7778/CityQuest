package com.example.cityquest.olaMap;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AccessTokenInterceptor implements Interceptor {
    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY_VALUE = "ozhzO2oQ5p84lMQTaaQ9r8ikZLM2aEJ2xUqrwYvG";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Modify the URL to include the API key as a query parameter
        HttpUrl urlWithApiKey = originalRequest.url().newBuilder()
                .addQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                .build();

        Request newRequest = originalRequest.newBuilder()
                .url(urlWithApiKey)
                .build();

        return chain.proceed(newRequest);
    }
}
