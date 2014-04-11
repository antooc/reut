
package com.peice.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class ATSHttpUtil {

    public static HttpClient httpClient = new DefaultHttpClient();
    public static final String BASE_URL = "http://piece.com.cn/";

    public static String getRequest(String url) throws Exception {

        HttpGet get = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(get);

        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            String result = EntityUtils.toString(httpResponse.getEntity());
            return result;
        }

        return null;
    }

    public static String postRequest(String url, Map<String, String> rawParams)
        throws Exception {

        HttpPost post = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        for (String key : rawParams.keySet()) {
            params.add(new BasicNameValuePair(key, rawParams.get(key)));
        }

        post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));

        HttpResponse httpResponse = httpClient.execute(post);

        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            String result = EntityUtils.toString(httpResponse.getEntity());
            return result;
        }

        return null;
    }

    // 获得Get请求对象request
    public static HttpGet getHttpGet(String url) {
        HttpGet request = new HttpGet(url);
        return request;
    }

    // 获得Post请求对象request
    public static HttpPost getHttpPost(String url) {
        HttpPost request = new HttpPost(url);
        return request;
    }

    // 根据请求获得响应对象response
    public static HttpResponse getHttpResponse(HttpGet request)
        throws ClientProtocolException, IOException {
        HttpResponse response = new DefaultHttpClient().execute(request);
        return response;
    }

    // 根据请求获得响应对象response
    public static HttpResponse getHttpResponse(HttpPost request)
        throws ClientProtocolException, IOException {
        HttpResponse response = new DefaultHttpClient().execute(request);
        return response;
    }

    // 发送Post请求，获得响应查询结果
    public static String queryStringForPost(String url) {
        // 根据url获得HttpPost对象
        HttpPost request = ATSHttpUtil.getHttpPost(url);
        String result = null;

        try {
            // 获得响应对象
            HttpResponse response = ATSHttpUtil.getHttpResponse(request);
            // 判断是否请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获得响应
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result = "网络异常！";
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result = "网络异常！";
            return result;
        }

        return null;
    }

    // 获得响应查询结果
    public static String queryStringForPost(HttpPost request) {
        String result = null;

        try {
            // 获得响应对象
            HttpResponse response = ATSHttpUtil.getHttpResponse(request);
            // 判断是否请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获得响应
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result = "网络异常！";
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result = "网络异常！";
            return result;
        }

        return null;
    }

    // 发送Get请求，获得响应查询结果
    public static String queryStringForGet(String url) {
        // 获得HttpGet对象
        HttpGet request = ATSHttpUtil.getHttpGet(url);
        String result = null;

        try {
            // 获得响应对象
            HttpResponse response = ATSHttpUtil.getHttpResponse(request);
            // 判断是否请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获得响应
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
            result = "网络异常！";
            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
            result = "网络异常！";
            return result;
        }

        return null;
    }
}

