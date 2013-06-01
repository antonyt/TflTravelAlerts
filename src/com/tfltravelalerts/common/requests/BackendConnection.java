
package com.tfltravelalerts.common.requests;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.util.Log;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.CharStreams;
import com.tfltravelalerts.TflApplication;

public class BackendConnection {
    private final static String LOG_TAG = "BackendConnection";
    public final static String prefix = "http://tfl-travel-alerts.appspot.com";
    public final static String userAgent; // sample: android 4 1.5 / client
                                          // 1.0.0

    static {
        userAgent = Joiner.on(' ').join("android", Build.VERSION.SDK_INT, Build.VERSION.RELEASE,
                '/', "client", getVersionName());
    }

    private static String getVersionName() {
        Context context = TflApplication.getLastInstance();
        if (context == null) {
            return "NullContext";
        }
        PackageInfo pInfo;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (NameNotFoundException e) {
            return "VersionNotFound";
        }
    }

    private static BackendConnectionResult doRequest(HttpUriRequest request) {
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance(userAgent);
        BackendConnectionResult result;
        try {
            HttpResponse response = httpClient.execute(request);
            InputStream inputStream = response.getEntity().getContent();
            StatusLine statusLine = response.getStatusLine();
            
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charsets.UTF_8);
            String contentString = CharStreams.toString(inputStreamReader);
            inputStreamReader.close();
            
            httpClient.close();
            result = new BackendConnectionResult(statusLine, contentString);
        } catch (IOException e) {
            result = new BackendConnectionResult(e);
        }
        return result;
    }

    public static BackendConnectionResult post(String path, NameValuePair... data) {
        if (!path.startsWith("/")) {
            path = '/' + path;
        }
        HttpPost request = new HttpPost(prefix + path);
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(Arrays.asList(data), "UTF-8");
            request.setEntity(entity);
        } catch (UnsupportedEncodingException e) {
            Log.wtf(LOG_TAG, "failed to encode data", e);
        }
        return doRequest(request);
    }

    public static BackendConnectionResult get(String path) {
        if (!path.startsWith("/")) {
            path = '/' + path;
        }
        HttpGet request = new HttpGet(prefix + path);
        return doRequest(request);
    }

}
