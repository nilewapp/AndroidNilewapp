package com.mooo.nilewapps.androidnilewapp;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

/**
 * Extends the AsyncTask and performs a Http Post request. After execution
 * a callback method is called with the response string as an argument.
 * 
 * @author nilewapp
 *
 */
public class AsyncHttpPost extends AsyncTask<String, String, String> {
    
    private Map<String, String> data = null;
    private PostExecuteCallback postExecute = null;
    
    /**
     * Callback interface
     * @author nilewapp
     *
     */
    public interface PostExecuteCallback {
        /**
         * The method is called in {@link AsyncHttpPost#onPostExecute}.
         * @param result Http response string from the post request
         */
        public void call(String result);
    }
    
    /**
     * Constructor
     * @param data parameters for the Http post request
     * @param callback provides the callback method
     */
    public AsyncHttpPost(Map<String, String> data, PostExecuteCallback callback) {
        this.data = data;
        this.postExecute = callback;
    }
    
    @Override
    protected String doInBackground(String... params) {
        String str = "";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(params[0]); /* in this case, params[0] is the URL */
        try {
            /* Set up post data */
            if (data != null) {
                ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                Iterator<String> it = data.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    nameValuePair.add(new BasicNameValuePair(key, data.get(key)));
                }
                post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
            }

            HttpResponse response = client.execute(post);
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
