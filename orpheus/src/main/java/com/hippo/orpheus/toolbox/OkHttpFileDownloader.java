package com.hippo.orpheus.toolbox;

import android.content.Context;
import android.util.Log;

import com.hippo.orpheus.FileDownloader;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Hubert on 15/10/24.
 */
public class OkHttpFileDownloader implements FileDownloader {

    private static final String TAG = "Orpheus";

    private OkHttpClient client;

    public OkHttpFileDownloader(Context context) {
        File cacheDir = Utils.createDefaultCacheDir(context);
        try {
            client.setCache(new com.squareup.okhttp.Cache(cacheDir, Utils.calculateDiskCacheSize(cacheDir)));
        } catch (Exception ignored) {
        }
    }

    @Override
    public File download(String url) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        try {
            Response response = client.newCall(requestBuilder.build()).execute();
        } catch (Exception e) {
            Log.e(TAG, "Download file error", e);
        }
        return null;
    }

    public synchronized OkHttpClient defaultOkHttpClient() {
        if (client == null) {
            client = new OkHttpClient();
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_NONE);
            client.setCookieHandler(cookieManager);
            client.setConnectTimeout(30, TimeUnit.SECONDS);
            client.setWriteTimeout(60, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);

            // Ignore the SSL
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore
                        .getDefaultType());
                trustStore.load(null, null);
                SSLContext sslContext = SSLContext.getInstance("TLS");
                TrustManager tm = new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] chain, String authType)
                            throws java.security.cert.CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] chain, String authType)
                            throws java.security.cert.CertificateException {
                    }
                };
                sslContext.init(null, new TrustManager[]{tm}, null);
                client.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });
                client.setSslSocketFactory(sslContext.getSocketFactory());

            } catch (KeyStoreException | CertificateException | IOException | KeyManagementException
                    | NoSuchAlgorithmException e) {
                Log.d(TAG, e.getMessage(), e);
            }

        }
        return client;
    }
}
