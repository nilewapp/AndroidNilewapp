package com.mooo.nilewapps.androidnilewapp;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

@SuppressWarnings("serial")
public class HttpException extends Exception {
    HttpPost request;
    HttpResponse response;
    public HttpException(HttpPost request, HttpResponse response) {
        this.request = request;
        this.response = response;
    }
}