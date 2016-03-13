package com.cs160.joleary.catnip;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Marcus on 3/12/16.
 */
public class CensusRestClient {
    private static final String BASE_URL = "http://data.fcc.gov/api/block/find";

    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        Log.d("absolute URL", BASE_URL + relativeUrl);
        return BASE_URL + relativeUrl;
    }
}