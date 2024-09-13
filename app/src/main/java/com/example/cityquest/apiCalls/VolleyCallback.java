package com.example.cityquest.apiCalls;

import org.json.JSONArray;

public interface VolleyCallback {
    void onSuccess(JSONArray result);
    void onError(String error);
}
