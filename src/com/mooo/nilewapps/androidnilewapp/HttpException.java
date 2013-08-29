package com.mooo.nilewapps.androidnilewapp;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

@SuppressWarnings("serial")
public class HttpException extends Exception {
    
    private HttpPost request;
    private HttpResponse response;
    
    public HttpException(HttpPost request, HttpResponse response) {
        this.request = request;
        this.response = response;
    }
    
    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder("HttpException response code: ");
        message.append(response.getStatusLine().getStatusCode());
        return message.toString();
    }

    public HttpPost getRequest() {
        return request;
    }

    public HttpResponse getResponse() {
        return response;
    }
    
}