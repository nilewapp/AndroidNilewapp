package com.mooo.nilewapps.androidnilewapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.security.KeyStore;
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

/**
 * Helper class that performs http post requests and returns
 * the response as a string
 * @author nilewapp
 *
 */
public class HttpPostString {
    
    public static String request(HttpClient client, String url, List<NameValuePair> params) throws Exception {
        HttpPost request = new HttpPost(url);
        request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            response.getEntity().getContent(), "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = in.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        }
        return null;
        
    }

    /**
     * Performs post request
     * @param trustStore contains a local certificate
     * @param url
     * @param requestEntity http post request parameters
     * @return the response body of the get request
     * @throws Exception
     */
    public static String request(KeyStore trustStore, String url, List<NameValuePair> requestEntity) throws Exception {
        
        /* Register trust store */
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));
        SSLSocketFactory sslSocketFactory = new SSLSocketFactory(trustStore);
        schemeRegistry.register(new Scheme("https", sslSocketFactory, 8443));
        HttpParams params = new BasicHttpParams();
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        
        /* Perform http request */
        HttpClient client = new DefaultHttpClient(cm, params);
        return request(client, url, requestEntity);
    }
    
    /**
     * Performs post request
     * @param url
     * @param params http post request params
     * @return the response body of the get request
     * @throws Exception
     */
    public static String request(String url, List<NameValuePair> params) throws Exception {
        HttpClient client = new DefaultHttpClient();
        return request(client, url, params);
    }
}
