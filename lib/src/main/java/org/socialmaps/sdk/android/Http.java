package org.socialmaps.sdk.android;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

final class Http {
    private static final int TIMEOUT_MS = 15000;

    private Http() {
    }

    public static JSONObject get(String url) throws IOException, JSONException {
        return request("GET", url, null, null);
    }

    public static JSONObject get(String url, Map<String, ?> queryParams) throws IOException, JSONException {
        return request("GET", appendQueryParams(url, queryParams), null, null);
    }

    public static JSONObject post(String url, JSONObject body) throws IOException, JSONException {
        return request("POST", url, body, null);
    }

    public static JSONObject post(String url, JSONObject body, String bearerToken) throws IOException, JSONException {
        return request("POST", url, body, bearerToken);
    }

    public static JSONObject put(String url, JSONObject body, String bearerToken) throws IOException, JSONException {
        return request("PUT", url, body, bearerToken);
    }

    public static JSONObject put(String url, String bearerToken) throws IOException, JSONException {
        return request("PUT", url, null, bearerToken);
    }

    public static JSONObject delete(String url, String bearerToken) throws IOException, JSONException {
        return request("DELETE", url, bearerToken);
    }

    private static JSONObject request(String method, String url, JSONObject body, String bearerToken) throws IOException, JSONException {
        HttpURLConnection connection = null;
        try {
            connection = openConnection(method, url);
            if (bearerToken != null && !bearerToken.isEmpty()) {
                connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
            }

            if (body != null) {
                byte[] bytes = body.toString().getBytes(StandardCharsets.UTF_8);
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setFixedLengthStreamingMode(bytes.length);
                try (OutputStream output = connection.getOutputStream()) {
                    output.write(bytes);
                }
            }

            int status = connection.getResponseCode();
            String response = readBody(status >= 400 ? connection.getErrorStream() : connection.getInputStream());

            if (status < 200 || status >= 300) {
                throw new IOException("HTTP " + status + ": " + response);
            }

            return response.isEmpty() ? new JSONObject() : new JSONObject(response);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static JSONObject request(String method, String url, String bearerToken) throws IOException, JSONException {
        return request(method, url, null, bearerToken);
    }

    private static HttpURLConnection openConnection(String method, String url) throws IOException {
        URL target;
        try {
            target = new URI(url).toURL();
        } catch (IllegalArgumentException | URISyntaxException e) {
            throw new IOException("Invalid URL: " + url, e);
        }

        HttpURLConnection connection = (HttpURLConnection) target.openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(TIMEOUT_MS);
        connection.setReadTimeout(TIMEOUT_MS);
        connection.setRequestProperty("Accept", "application/json");
        return connection;
    }

    private static String appendQueryParams(String url, Map<String, ?> queryParams) throws IOException {
        if (queryParams == null || queryParams.isEmpty()) {
            return url;
        }

        Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
        for (Map.Entry<String, ?> entry : queryParams.entrySet()) {
            if (entry.getKey() == null) {
                throw new IOException("Query parameter name must not be null");
            }
            Object value = entry.getValue();
            uriBuilder.appendQueryParameter(entry.getKey(), value == null ? null : String.valueOf(value));
        }
        return uriBuilder.build().toString();
    }

    private static String readBody(InputStream stream) throws IOException {
        if (stream == null) {
            return "";
        }

        StringBuilder body = new StringBuilder();
        char[] buffer = new char[4096];
        try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            int count;
            while ((count = reader.read(buffer)) != -1) {
                body.append(buffer, 0, count);
            }
        }
        return body.toString();
    }
}
