package cn.hengyumo.humor.utils;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * HttpClientUtil
 *
 * @author hengyumo
 * @since 2019/6/16
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class HttpClientUtil {

    public static String get(String url, Object param) throws URISyntaxException, IOException {

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(param);

        String resultString = "";
        CloseableHttpResponse response = null;

        // 创建uri
        URIBuilder builder = new URIBuilder(url);
        if (param != null) {
            for (String key : jsonObject.keySet()) {
                builder.addParameter(key, (String) jsonObject.get(key));
            }
        }
        URI uri = builder.build();

        // 创建http GET请求
        HttpGet httpGet = new HttpGet(uri);

        // 执行请求
        response = httpclient.execute(httpGet);
        // 判断返回状态是否为200
        if (response.getStatusLine().getStatusCode() == 200) {
            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        }
        System.out.println(resultString);

        response.close();
        httpclient.close();

        return resultString;
    }

    public static String get(String url) throws IOException, URISyntaxException {
        return get(url, null);
    }

    public static <T> T get(String url, Object param, Class<T> returnClass) throws IOException, URISyntaxException {
        return JSONObject.parseObject(get(url, param), returnClass);
    }

    public static <T> T getAndParse(String url, Class<T> returnClass) throws IOException, URISyntaxException {
        return JSONObject.parseObject(get(url), returnClass);
    }

    public static String post(String url, Object param) throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(param);

        String resultString = "";
        // 创建Http Post请求
        HttpPost httpPost = new HttpPost(url);
        // 创建参数列表
        if (param != null) {
            List<NameValuePair> paramList = new ArrayList<>();
            for (String key : jsonObject.keySet()) {
                paramList.add(new BasicNameValuePair(key, (String) jsonObject.get(key)));
            }
            // 模拟表单
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
            httpPost.setEntity(entity);
        }
        // 执行http请求
        response = httpClient.execute(httpPost);
        resultString = EntityUtils.toString(response.getEntity(), "utf-8");

        response.close();
        return resultString;
    }

    public static String post(String url) throws IOException {
        return post(url, null);
    }

    public static <T> T post(String url, Object param, Class<T> returnClass) throws IOException {
        return JSONObject.parseObject(post(url, param), returnClass);
    }

    public static <T> T postAndParse(String url, Class<T> returnClass) throws IOException {
        return JSONObject.parseObject(post(url), returnClass);
    }

    public static String postJson(String url, Object object) throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        String json = JSONObject.toJSONString(object);

        String resultString = "";

        // 创建Http Post请求
        HttpPost httpPost = new HttpPost(url);
        // 创建请求内容
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        // 执行http请求
        response = httpClient.execute(httpPost);
        resultString = EntityUtils.toString(response.getEntity(), "utf-8");

        response.close();

        return resultString;
    }

    public static <T> T postJsonAndParse(String url, Object param, Class<T> returnClass) throws IOException {
        return JSONObject.parseObject(postJson(url, param), returnClass);
    }
}
