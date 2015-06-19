package com.vk.fesswod.articleView.rest;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

 public class PhotoMultipartRequest extends Request<String> {

        private MultipartEntity entity = new MultipartEntity();

        private static final String FILE_PART_NAME = "photo[image]";
        private static final String STRING_PART_NAME = "text";

        private final Response.Listener<String> mListener;
        private final File mFilePart;

        public PhotoMultipartRequest(String url, Response.ErrorListener errorListener, Response.Listener<String> listener, File file)
        {
            super(Request.Method.POST, url, errorListener);

            mListener = listener;
            mFilePart = file;
            buildMultipartEntity();
        }

        private void buildMultipartEntity()
        {
            entity.addPart(FILE_PART_NAME, new FileBody(mFilePart));

        }

        @Override
        public String getBodyContentType()
        {
            return entity.getContentType().getValue();
        }

        @Override
        public byte[] getBody() throws AuthFailureError
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try
            {
                entity.writeTo(bos);
            }
            catch (IOException e)
            {
                VolleyLog.e("IOException writing to ByteArrayOutputStream");
            }
            return bos.toByteArray();
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response)
        {
            return Response.success("Uploaded", getCacheEntry());
        }

        @Override
        protected void deliverResponse(String response)
        {
            mListener.onResponse(response);
        }
    }