package com.tfltravelalerts.analytics;

import org.apache.http.client.methods.HttpUriRequest;


public class TflAnalytics {

    public static RequestAnalytics forRequests(HttpUriRequest request) {
        return new RequestAnalytics("requests", request.getURI().getPath());
    }
    
    public static ParsingAnalytics forParser(String name) {
        return new ParsingAnalytics("parsing", name);
    }

}
