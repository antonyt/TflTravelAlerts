
package com.tfltravelalerts.common.requests;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.google.common.base.Joiner;
import com.tfltravelalerts.TflApplication;

public class BackendConnection {
    public final static String prefix = "http://192.168.1.104:8080";
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

    public static BackendConnectionResult get(String path) {
        if (!path.startsWith("/")) {
            path = '/'+path;
        }

        AndroidHttpClient httpClient = AndroidHttpClient.newInstance("android");
        HttpGet request = new HttpGet(prefix + path);
        BackendConnectionResult result;
        try {
            HttpResponse response;
            response = httpClient.execute(request);
            InputStream content = response.getEntity().getContent();
            StatusLine statusLine = response.getStatusLine();
            result = new BackendConnectionResult(httpClient, statusLine, content);
        } catch (IOException e) {
            result = new BackendConnectionResult(httpClient, e);
        } finally {
           // httpClient.close();
        }
        return result;
    }

}
