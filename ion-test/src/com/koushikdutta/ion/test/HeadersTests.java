package com.koushikdutta.ion.test;

import android.test.AndroidTestCase;

import com.koushikdutta.async.http.libcore.RawHeaders;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.koushikdutta.ion.HeadersCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by koush on 6/30/13.
 */
public class HeadersTests extends AndroidTestCase {
    boolean gotHeaders;
    public void testHeaders() throws Exception {
        gotHeaders = false;
        AsyncHttpServer httpServer = new AsyncHttpServer();
        try {
            httpServer.get("/", new HttpServerRequestCallback() {
                @Override
                public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                    response.send("hello");
                }
            });
            httpServer.listen(Ion.getDefault(getContext()).getServer(), 5555);

            Ion.with(getContext())
            .load("http://localhost:5555/")
            .onHeaders(new HeadersCallback() {
                @Override
                public void onHeaders(RawHeaders headers) {
                    assertEquals(headers.getResponseCode(), 200);
                    gotHeaders = true;
                }
            })
            .asString()
            .get();

            assertTrue(gotHeaders);
        }
        finally {
            httpServer.stop();
            Ion.getDefault(getContext()).getServer().stop();
        }
    }
}
