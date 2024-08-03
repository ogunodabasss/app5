package com.example.app5.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@Slf4j
public class RequestUtil {

    public static String getRequest(String uri, Map<String, String> parameters, Map<String, String> inputHeaders, Map<String, String> outputHeaders) {
        URL url;
        try {
            if (!parameters.isEmpty()) uri = uri + "?" + ParameterStringBuilder.getParamsString(parameters);
            url = new URL(uri);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(HttpMethod.GET.name());

            for (String key : inputHeaders.keySet()) {
                String value = inputHeaders.get(key);
                con.setRequestProperty(key, value);
            }

            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setDoOutput(true);

            int status = con.getResponseCode();

            if (status == 200 || status == 201) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                if (outputHeaders != null && !outputHeaders.isEmpty())
                    for (String key : outputHeaders.keySet()) {
                        outputHeaders.put(key, con.getHeaderField(key));
                    }

                return content.toString();
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                String errMessage = "err status: " + status + "\nMessage: " + content;
                log.error(errMessage);
                throw new RuntimeException(errMessage);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) con.disconnect();
        }
    }

    public static String getRequest(String uri, Map<String, String> parameters) {
        URL url;
        try {
            if (!parameters.isEmpty()) uri = uri + "?" + ParameterStringBuilder.getParamsString(parameters);
            url = new URL(uri);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(HttpMethod.GET.name());
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Connection", "keep-alive");
            con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            con.setRequestProperty("Accept", "*/*");

            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setDoOutput(true);

            int status = con.getResponseCode();

            if (status == 200 || status == 201) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                return content.toString();
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                String errMessage = "err status: " + status + "\nMessage: " + content;
                log.error(errMessage);
                throw new RuntimeException(errMessage);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) con.disconnect();
        }
    }

    public static String getRequest(String uri) {

        URL url;
        try {
            url = new URL(uri);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(HttpMethod.GET.name());
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Connection", "keep-alive");
            con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            con.setRequestProperty("Accept", "*/*");

            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setDoOutput(true);

            int status = con.getResponseCode();
            if (status == 200 || status == 201) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                return content.toString();
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                String errMessage = "err status: " + status + "\nMessage: " + content;
                log.error(errMessage);
                throw new RuntimeException(errMessage);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) con.disconnect();
        }
    }

    public record Response(String body, Map<String, String> header) {
    }
}