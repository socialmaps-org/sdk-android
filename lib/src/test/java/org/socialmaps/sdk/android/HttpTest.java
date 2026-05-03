package org.socialmaps.sdk.android;

import mockwebserver3.MockResponse;
import mockwebserver3.RecordedRequest;
import mockwebserver3.junit4.MockWebServerRule;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class HttpTest {
    @Rule
    public final MockWebServerRule serverRule = new MockWebServerRule();

    @Test
    public void get_returnsParsedJson() throws Exception {
        enqueueJson(200, "{\"ok\":true,\"name\":\"Dublin\"}");

        JSONObject json = Http.get(url("/places"));
        RecordedRequest request = takeRequest();

        assertTrue(json.getBoolean("ok"));
        assertEquals("Dublin", json.getString("name"));
        assertEquals("GET", request.getMethod());
        assertEquals("/places", request.getUrl().encodedPath());
        assertEquals("application/json", request.getHeaders().get("Accept"));
    }

    @Test
    public void get_withQueryParamsSendsEncodedParamsAndReturnsParsedJson() throws Exception {
        enqueueJson(200, "{\"ok\":true,\"name\":\"Dublin\"}");
        Map<String, Object> queryParams = new LinkedHashMap<>();
        queryParams.put("name", "Izz Cafe");
        queryParams.put("lat", 51.8952597);
        queryParams.put("lon", -8.4715779);

        JSONObject json = Http.get(url("/places"), queryParams);
        RecordedRequest request = takeRequest();

        assertTrue(json.getBoolean("ok"));
        assertEquals("Dublin", json.getString("name"));
        assertEquals("GET", request.getMethod());
        assertEquals("/places", request.getUrl().encodedPath());
        assertEquals("Izz Cafe", request.getUrl().queryParameter("name"));
        assertEquals("51.8952597", request.getUrl().queryParameter("lat"));
        assertEquals("-8.4715779", request.getUrl().queryParameter("lon"));
        assertEquals("application/json", request.getHeaders().get("Accept"));
    }

    @Test
    public void post_sendsJsonBodyAndReturnsParsedJson() throws Exception {
        enqueueJson(201, "{\"created\":true}");

        JSONObject json = Http.post(url("/places"), new JSONObject().put("name", "Bristol"));
        RecordedRequest request = takeRequest();

        assertTrue(json.getBoolean("created"));
        assertEquals("POST", request.getMethod());
        assertEquals("/places", request.getUrl().encodedPath());
        assertEquals("application/json; charset=utf-8", request.getHeaders().get("Content-Type"));
        assertEquals("application/json", request.getHeaders().get("Accept"));
        assertNotNull(request.getBody());
        assertEquals("Bristol", new JSONObject(request.getBody().utf8()).getString("name"));
    }

    @Test
    public void post_withBearerTokenSendsAuthorizationHeader() throws Exception {
        enqueueJson(201, "{\"created\":true}");

        Http.post(url("/places"), new JSONObject().put("name", "Bristol"), "test-token");
        RecordedRequest request = takeRequest();

        assertEquals("POST", request.getMethod());
        assertEquals("Bearer test-token", request.getHeaders().get("Authorization"));
    }

    @Test
    public void get_emptySuccessBodyReturnsEmptyJsonObject() throws Exception {
        enqueueJson(204, "");

        JSONObject json = Http.get(url("/empty"));
        RecordedRequest request = takeRequest();

        assertEquals(0, json.length());
        assertEquals("GET", request.getMethod());
        assertEquals("/empty", request.getUrl().encodedPath());
    }

    @Test
    public void get_errorStatusThrowsIOExceptionWithResponseBody() throws Exception {
        enqueueJson(404, "{\"error\":\"not found\"}");

        IOException error = assertThrows(IOException.class, () -> Http.get(url("/fail")));
        RecordedRequest request = takeRequest();

        assertTrue(error.getMessage().contains("HTTP 404"));
        assertTrue(error.getMessage().contains("not found"));
        assertEquals("GET", request.getMethod());
        assertEquals("/fail", request.getUrl().encodedPath());
    }

    @Test
    public void get_invalidUrlThrowsIOException() {
        IOException error = assertThrows(IOException.class, () -> Http.get("http:// bad-url"));

        assertTrue(error.getMessage().contains("Invalid URL"));
    }

    private void enqueueJson(int code, String body) {
        serverRule.getServer().enqueue(new MockResponse.Builder()
                .code(code)
                .addHeader("Content-Type", "application/json")
                .body(body)
                .build());
    }

    private String url(String path) {
        return serverRule.getServer().url(path).toString();
    }

    private RecordedRequest takeRequest() throws InterruptedException {
        RecordedRequest request = serverRule.getServer().takeRequest(5, TimeUnit.SECONDS);
        assertNotNull(request);
        return request;
    }
}
