package com.mooo.nilewapps.androidnilewapp;

import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

/**
 * Extends the AsyncTask and performs a Http Post request. After execution
 * a callback method is called with the response string as an argument.
 * 
 * @author nilewapp
 *
 */
public class AsyncHttpGet extends AsyncTask<String, String, String> {
    
    private PostExecuteCallback postExecute = null;
    
    /**
     * Callback interface
     * @author nilewapp
     *
     */
    public interface PostExecuteCallback {
        /**
         * The method is called in {@link AsyncHttpGet#onPostExecute}.
         * @param result Http response string from the post request
         */
        public void call(String result);
    }
    
    /**
     * Constructor
     * @param callback provides the callback method
     */
    public AsyncHttpGet(PostExecuteCallback callback) {
        this.postExecute = callback;
    }
    
    @Override
    protected String doInBackground(String... params) {
        String str = "";
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(params[0]); /* in this case, params[0] is the URL */
        try {
            HttpResponse response = client.execute(get);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
                byte[] result = EntityUtils.toByteArray(response.getEntity());
                str = new String(result, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
    
    @Override
    protected void onPostExecute(String result) {
        postExecute.call(result);
    }
    
}
