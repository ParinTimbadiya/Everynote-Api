package com.everynote.api.helpers;

import com.google.gson.Gson;

public class JsonResponse {
    
    private String message;
    
    public JsonResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
