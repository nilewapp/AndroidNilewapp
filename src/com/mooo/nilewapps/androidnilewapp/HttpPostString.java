/**
 *  Copyright 2013 Robert Welin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mooo.nilewapps.androidnilewapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.util.Base64;

/**
 * Helper class that performs HTTP post requests and returns
 * the response as a string
 * @author nilewapp
 *
 */
public class HttpPostString {
    
    /**
     * Performs an HTTP post request.
     * @param client
     * @param request
     * @param requestEntity
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    public static <T extends NameValuePair> String request(HttpClient client, HttpPost request, List<T> requestEntity)
            throws IllegalStateException, IOException, HttpException {
        request.setEntity(new UrlEncodedFormEntity(requestEntity));
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            response.getEntity().getContent()), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = in.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } else {
            throw new HttpException(request, response);
        }
    }

    /**
     * Performs a basic HTTP post request.
     * @param client
     * @param url
     * @param requestEntity
     * @return the response body of the request
     * @throws IllegalStateException
     * @throws IOException
     * @throws HttpException 
     */
    public static <T extends NameValuePair> String request(HttpClient client, String url, List<T> requestEntity)
            throws IllegalStateException, IOException, HttpException {
        HttpPost request = new HttpPost(url);
        return request(client, request, requestEntity);
    }

    /**
     * Performs an HTTP post request.
     * @param trustStore contains a local certificate
     * @param url
     * @param requestEntity http post request parameters
     * @return the response body of the post request
     * @throws KeyStoreException 
     * @throws NoSuchAlgorithmException 
     * @throws UnrecoverableKeyException 
     * @throws KeyManagementException 
     * @throws IOException 
     * @throws IllegalStateException 
     * @throws HttpException 
     * @throws Exception
     */
    public static <T extends NameValuePair> String request(KeyStore trustStore, String url, List<T> requestEntity)
            throws KeyManagementException,
                   UnrecoverableKeyException,
                   NoSuchAlgorithmException,
                   KeyStoreException,
                   IllegalStateException,
                   IOException,
                   HttpException {
        return request(trustStore, new HttpPost(url), requestEntity);
    }

    /**
     * Registers a truststore and performs an HTTP post request.
     * @param trustStore
     * @param request
     * @param requestEntity
     * @return
     * @throws KeyManagementException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IllegalStateException
     * @throws IOException
     * @throws HttpException
     */
    public static <T extends NameValuePair> String request(KeyStore trustStore, HttpPost request, List<T> requestEntity)
            throws KeyManagementException,
                UnrecoverableKeyException,
                NoSuchAlgorithmException,
                KeyStoreException,
                IllegalStateException,
                IOException, HttpException {

        /* Register trust store */
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));
        SSLSocketFactory sslSocketFactory = new SSLSocketFactory(trustStore);
        schemeRegistry.register(new Scheme("https", sslSocketFactory, 8443));
        HttpParams params = new BasicHttpParams();
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        
        /* Perform http request */
        HttpClient client = new DefaultHttpClient(cm, params);
        return request(client, request, requestEntity);
    }
    
    /**
     * Performs an HTTP post request.
     * @param url
     * @param params http post request params
     * @return the response body of the post request
     * @throws IOException 
     * @throws IllegalStateException 
     * @throws HttpException 
     * @throws Exception
     */
    public static <T extends NameValuePair> String request(String url, List<T> params)
            throws IllegalStateException, IOException, HttpException {
        HttpClient client = new DefaultHttpClient();
        return request(client, url, params);
    }
    
    /**
     * Performs an HTTP post request with a given Authorization header.
     * @param trustStore
     * @param url
     * @param authorizationHeader
     * @param requestEntity
     * @return
     * @throws KeyManagementException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IllegalStateException
     * @throws IOException
     * @throws HttpException
     */
    public static <T extends NameValuePair> String request(
                KeyStore trustStore, 
                String url, 
                AuthorizationHeader authorizationHeader, 
                List<T> requestEntity)
            throws KeyManagementException,
                UnrecoverableKeyException,
                NoSuchAlgorithmException,
                KeyStoreException,
                IllegalStateException,
                IOException,
                HttpException {
        HttpPost request = new HttpPost(url);
        request.setHeader("Authorization", authorizationHeader.toString());
        return request(trustStore, request, requestEntity);
    }
    
    /**
     * Performs an HTTP post request. Sets the Authorization header with 
     * the Basic authentication scheme.
     * @param trustStore
     * @param url
     * @param username
     * @param password
     * @param requestEntity
     * @return
     * @throws KeyManagementException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IllegalStateException
     * @throws IOException
     * @throws HttpException 
     */
    public static <T extends NameValuePair> String request(
                KeyStore trustStore, 
                String url, 
                String username, 
                String password, 
                List<T> requestEntity)
            throws KeyManagementException,
                UnrecoverableKeyException,
                NoSuchAlgorithmException,
                KeyStoreException,
                IllegalStateException,
                IOException,
                HttpException {
        String credentials = username + ":" + password;
        AuthorizationHeader auth = 
                new AuthorizationHeader("Basic", 
                        Base64.encodeToString(
                                credentials.getBytes(),
                                Base64.URL_SAFE|Base64.NO_WRAP)); 
        return request(trustStore, url, auth, requestEntity);
    }
    
}
