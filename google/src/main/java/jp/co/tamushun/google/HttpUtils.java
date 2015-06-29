package jp.co.tamushun.google;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class HttpUtils {

	public static String getContents(String url) {

		HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.DEFAULT).build();
		String body = null;

		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept", "application/json");
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() >= 200 && httpResponse.getStatusLine().getStatusCode() < 300)
				if (httpResponse.getEntity() != null)
					return IOUtils.toString(httpResponse.getEntity().getContent());
		} catch (IOException e) {
			return null;
		}

		return null;

	}

	public static String postContents(String url, Map<String, Object> params) {

		HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.DEFAULT).build();
		String body = null;

		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setEntity(new UrlEncodedFormEntity(convertNameValuePairs(params), Consts.UTF_8));
		try {
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() >= 200 && httpResponse.getStatusLine().getStatusCode() < 300)
				if (httpResponse.getEntity() != null)
					return IOUtils.toString(httpResponse.getEntity().getContent());
		} catch (IOException e) {
			return null;
		}

		return null;

	}

	private static List<NameValuePair> convertNameValuePairs(Map<String, Object> params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, Object> entry : params.entrySet())
			nameValuePairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
		return nameValuePairs;
	}
}
