package com.vk.fesswod.articleView.api;


import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharsetUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RestClient {

    private static final String DEBUG_TAG = RestClient.class.getSimpleName();

    public static final String TOKEN = "3507c867e3a240c8f10bc40be3d765b4";

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = generateBoundary(); //"nnnVwRxmJMKcpkWwwwE-DWVRe4CQ0d";
    private final static char[] MULTIPART_CHARS =
            "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    .toCharArray();
    public RestClient() {
    }

    public ApiResponse doGet(String url) {
        return doGet(url, null);
    }

    public ApiResponse doGet(String url, ArrayList<Header> headers) {
        ApiResponse apiResponse = new ApiResponse();

        HttpClient httpClient = getHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet(url);

        httpGet.setParams(getTimeOutParams());

        if (headers != null) {
            for (Header h : headers) {
                httpGet.addHeader(h);
            }
        }

        setDefaultHeaders(httpGet);

        HttpResponse response;
        try {
            response = httpClient.execute(httpGet, localContext);

            apiResponse.status = response.getStatusLine().getStatusCode();
            apiResponse.body = response.getEntity();
            Log.d(DEBUG_TAG, "doGet: " + url);
        } catch (Exception e) {
            Log.e(DEBUG_TAG, Log.getStackTraceString(e));
        }

        return apiResponse;
    }

    public ApiResponse doPost(String url, List<NameValuePair> postParams, String string) {
        Log.d(DEBUG_TAG, "doPost: " + url);
        ApiResponse apiResponse = new ApiResponse();

        HttpClient httpClient = getHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setParams(getTimeOutParams());

        setDefaultHeaders(httpPost);

        try {
            if (postParams != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
            }

            StringEntity stringEntity	= null;
            stringEntity = new StringEntity(string, "UTF-8");
            if (stringEntity != null) {
                httpPost.setEntity(stringEntity);
            }

            HttpResponse response = httpClient.execute(httpPost, localContext);
            apiResponse.status = response.getStatusLine().getStatusCode();
            apiResponse.body = response.getEntity();
            Log.d(DEBUG_TAG, "doPost: end.");
        } catch (final IOException e) {
            Log.e(DEBUG_TAG, Log.getStackTraceString(e));
        }

        return apiResponse;
    }

    public ApiResponse doPut(String url, String string) {
        ApiResponse apiResponse = new ApiResponse();

        HttpClient httpClient = getHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPut httpPut = new HttpPut(url);

        httpPut.setParams(getTimeOutParams());

        setDefaultHeaders(httpPut);

        try {
            StringEntity stringEntity	= null;
            stringEntity = new StringEntity(string, "UTF-8");
            if (stringEntity != null) {
                httpPut.setEntity(stringEntity);
            }

            HttpResponse response = httpClient.execute(httpPut, localContext);
            apiResponse.status = response.getStatusLine().getStatusCode();
            apiResponse.body = response.getEntity();
            Log.d(DEBUG_TAG, "doPut: " + url);
        } catch (final IOException e) {
            Log.e(DEBUG_TAG, Log.getStackTraceString(e));
        }

        return apiResponse;
    }

    public ApiResponse doDelete(String url) {
        ApiResponse apiResponse = new ApiResponse();
        HttpClient httpClient = getHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpDelete httpDelete = new HttpDelete(url);

        httpDelete.setParams(getTimeOutParams());

        setDefaultHeaders(httpDelete);

        HttpResponse response;
        try {
            response = httpClient.execute(httpDelete, localContext);

            apiResponse.status = response.getStatusLine().getStatusCode();
            apiResponse.body = response.getEntity();
            Log.d(DEBUG_TAG, "doDelete: " + url);
        } catch (Exception e) {
            Log.e(DEBUG_TAG, Log.getStackTraceString(e));
        }

        return apiResponse;
    }

    /**
     * Use {@link doMultiPartRequest}
     * @param urlString
     * @param file
     * @param fileParameterName
     * @return
     */
    @Deprecated
    public String doUploadFile(String urlString, File file, String fileParameterName) {
        HttpURLConnection conn				= null;
        DataOutputStream dos				= null;
        DataInputStream dis					= null;
        FileInputStream fileInputStream		= null;
        HashMap<String, String> parameters	= new HashMap<>();
        StringBuilder  stringBuilder		= new StringBuilder();
        byte[] buffer;
        int maxBufferSize = 20 * 1024;
        try {
            // ------------------ CLIENT REQUEST
            fileInputStream = new FileInputStream(file);

            // open a URL connection to the Servlet
            // Open a HTTP connection to the URL
            URL url	= new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            // Allow Inputs
            conn.setDoInput(true);
            // Allow Outputs
            conn.setDoOutput(true);
            // Don't use a cached copy.
            conn.setUseCaches(false);
            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Token token=" + TOKEN);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary + "; charset=UTF-8");
            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\""
                    + fileParameterName + "\"; filename=\"" + file.getName()
                    + "\"" + lineEnd);
            dos.writeBytes("Content-Type:image/jpg" + lineEnd);
            dos.writeBytes(lineEnd);

            // create a buffer of maximum size
            buffer = new byte[Math.min((int) file.length(), maxBufferSize)];
            int length;
            // read file and write it into form...
            while ((length = fileInputStream.read(buffer)) != -1) {
                dos.write(buffer, 0, length);
            }

            for (String name : parameters.keySet()) {
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\""
                        + name + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(parameters.get(name));
            }

            // send multipart form data necessary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            try {
                if (fileInputStream != null){
                    fileInputStream.close();
                }
                if (dos != null)
                    dos.close();
            } catch (Exception e) {}
        }

        // ------------------ read the SERVER RESPONSE
        try {
            int responseCode		= conn.getResponseCode();
            String responseMessage	= conn.getResponseMessage();
            conn.connect();
            dis = new DataInputStream(conn.getInputStream());
            StringBuilder response = new StringBuilder();

            String line;
            while ((line = dis.readLine()) != null) {
                response.append(line).append('\n');
            }

            System.out.println("Upload file responce:"
                    + response.toString());
            return response.toString();
        } catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {}
            }
        }
        return null;
    }

    public String doMultiPartRequest(String urlString, File file, String fileParameterName) {
        HttpURLConnection conn				= null;
        DataOutputStream dos				= null;
        DataInputStream dis					= null;
        FileInputStream fileInputStream		= null;

        MultipartEntityBuilder entity 		= MultipartEntityBuilder.create();
        HttpEntity httpentity;
        String result						= null;

        try {
            entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            try {
                entity.setCharset(CharsetUtils.get("UTF-8"));
            } catch (Exception e) {
               Log.e(DEBUG_TAG," error in doMultiPartRequest ",e);
            }
            entity.addPart(fileParameterName, new FileBody(file, ContentType.create("image/jpeg"), file.getName()));
            httpentity = entity.build();

            // ------------------ CLIENT REQUEST
            fileInputStream = new FileInputStream(file);

            // open a URL connection to the Servlet
            // Open a HTTP connection to the URL
            URL url	= new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            // Allow Inputs
            conn.setDoInput(true);
            // Allow Outputs
            conn.setDoOutput(true);
            // Don't use a cached copy.
            conn.setUseCaches(false);
            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Token token=" + TOKEN);
            conn.setRequestProperty("Content-Type", "application/json");

            conn.addRequestProperty(httpentity.getContentType().getName(), httpentity.getContentType().getValue());
            dos = new DataOutputStream(conn.getOutputStream());
            httpentity.writeTo(dos);
        } catch (IOException e) {
            Log.e(DEBUG_TAG, " error in doMultiPartRequest ", e);
        }
        finally
        {
            try {
                if (fileInputStream != null){
                    fileInputStream.close();
                }
                if (dos != null)
                    dos.close();
            } catch (Exception e) {}
        }

        // ------------------ read the SERVER RESPONSE
        try {
            int responseCode		= conn.getResponseCode();
            StringBuilder response 	= new StringBuilder();
            dis = new DataInputStream(conn.getInputStream());

            String line;
            while ((line = dis.readLine()) != null) {
                response.append(line).append('\n');
            }

            System.out.println("Upload file responce:"
                    + response.toString());
            result					= response.toString();
        } catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {}
            }
        }
        return result;
    }
    private HttpClient getHttpClient() {
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Android");
        return httpClient;
    }

    private HttpParams getTimeOutParams() {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
        HttpConnectionParams.setSoTimeout(httpParams, 50000);
        return httpParams;
    }

    private void setDefaultHeaders(HttpRequestBase httpRequest) {
        httpRequest.setHeader("Authorization", "Token token="+TOKEN);
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setHeader("Accept-Encoding", "utf-8");
    }
    private static String generateBoundary() {
        StringBuilder buffer = new StringBuilder();
        Random rand = new Random();
        int count = rand.nextInt(11) + 30; // a random size from 30 to 40
        for (int i = 0; i < count; i++) {
            buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        return buffer.toString();
    }



}
