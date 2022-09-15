package cn.ictt.zhanghui.springboot_test.base.util.http;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class HttpDelegate {
	private int timeOut = 10000;
	private CloseableHttpClient httpClient;
	private RequestConfig requestConfig;

	public HttpDelegate() {
		httpClient = this.getHttpClient(false);
		this.initRequestConfig();
	}

	public HttpDelegate(Boolean isHttps) {
		httpClient = this.getHttpClient(isHttps);
		this.initRequestConfig();
	}
    
	private String execute(HttpUriRequest method, CloseableHttpClient httpClient) throws RuntimeException, IOException {
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(method);
			HttpEntity entity = response.getEntity();
			if(entity != null) {
			    return EntityUtils.toString(entity, method.getFirstHeader(HttpHeaders.CONTENT_ENCODING).getValue());
			}
			return "";
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}
    
	public String executePost(String url, String contentType, String param) {
		return this.executePost(url, contentType, "UTF-8", param);
	}

	public String executePost(String url, String contentType, String encoding, String param) {
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setConfig(requestConfig);
			this.initMethodParam(httpPost, contentType, encoding);
			if (param != null) {
				/*
				 * List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				 * nvps.add(new BasicNameValuePair("username", "vip"));
				 * nvps.add(new BasicNameValuePair("password", "secret"));
				 * httpPost.setEntity(new UrlEncodedFormEntity(nvps));
				 */
				httpPost.setEntity(new StringEntity(param, encoding));
			}
			return this.execute(httpPost, this.getHttpClient());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			httpPost.releaseConnection();
		}
	}
	
	public String executePostValuePair(String url, String contentType, String encoding, JSONObject param) {
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setConfig(requestConfig);
			this.initMethodParam(httpPost, contentType, encoding);
			if (param != null) {
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				for (String key : param.keySet()) {
					nvps.add(new BasicNameValuePair(key, param.getString(key)));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			}
			return this.execute(httpPost, this.getHttpClient());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			httpPost.releaseConnection();
		}
	}

	public String executePost(String url, String param) {
		return this.executePost(url, null, "UTF-8", param);
	}

	public String executePostJson(String url, JSONObject param) {
		return this.executePostValuePair(url, null, "UTF-8", param);
	}

	public String executeGet(String url, String contentType) {
		return this.executeGet(url, "UTF-8", contentType);
	}

	public String executeGet(String url, String encoding, String contentType) {
		HttpGet httpGet = new HttpGet(url);
		try {
			httpGet.setConfig(requestConfig);
			this.initMethodParam(httpGet, contentType, encoding);
			return this.execute(httpGet, this.getHttpClient());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			httpGet.releaseConnection();
		}
	}

	public String executeGet(String url) {
		return this.executeGet(url, "UTF-8", null);
	}

	private void initMethodParam(HttpUriRequest method, String contentType, String encoding) {
		if (contentType != null) {
			method.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
		}
		method.setHeader(HttpHeaders.CONTENT_ENCODING, encoding);
		method.setHeader(HttpHeaders.TIMEOUT, "60000");
	}

	@SuppressWarnings("deprecation")
	private CloseableHttpClient getHttpClient(Boolean isHttps) {
		CloseableHttpClient httpClient = null;
		try {
			if (isHttps) {

				TrustManager[] trustManagers = new TrustManager[1];
				trustManagers[0] = new X509TrustManager() {
					public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
						// TODO Auto-generated method stub
					}

					public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
						// TODO Auto-generated method stub
					}

					public X509Certificate[] getAcceptedIssuers() {
						// TODO Auto-generated method stub
						return null;
					}

				};
				SSLContext sslContext = SSLContext.getInstance("TLS");
				sslContext.init(new KeyManager[0], trustManagers, new SecureRandom());
				SSLContext.setDefault(sslContext);
				sslContext.init(null, trustManagers, null);
				//设置允许所有主机名称就可以忽略主机名称验证
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			} else {
				httpClient = HttpClients.custom().build();
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return httpClient;
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	public void destroy() {
		try {
			httpClient.close();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
		this.initRequestConfig();
	}

	private void initRequestConfig() {
		requestConfig = RequestConfig.custom().setSocketTimeout(timeOut).setConnectTimeout(timeOut).build();
	}

	
	/*因人脸识别需求header新增*/
	public String executeHeaderGet(String key,String value,String url) {
		return this.executeGet(key,value,url, "UTF-8", null);
	}
	
	public String executeGet(String key,String value,String url, String encoding, String contentType) {
		HttpGet httpGet = new HttpGet(url);
		try {
			httpGet.addHeader(key, value);
			httpGet.setConfig(requestConfig);
			this.initMethodParam(httpGet, contentType, encoding);
			return this.execute(httpGet, this.getHttpClient());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			httpGet.releaseConnection();
		}
	}
	
	public String executeHeaderPost(String key,String value,String url, String param) {
		return this.executeHeaderPost(key,value,url, null, "UTF-8", param);
	}
	
	public String executeHeaderPost(String key,String value,String url, String contentType, String encoding, String param) {
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.addHeader(key, value);
			httpPost.setConfig(requestConfig);
			this.initMethodParam(httpPost, contentType, encoding);
			if (param != null) {
				httpPost.setEntity(new StringEntity(param, encoding));
			}
			return this.execute(httpPost, this.getHttpClient());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			httpPost.releaseConnection();
		}
	}
	
	public String executeDelete(String url, String param) {
		return this.executeHeaderDelete(url, null, "UTF-8", param);
	}
	
	public String executeDelete(String url, String contentType, String encoding, String param) {
		HttpDelete httpDelete = new HttpDelete(url);		 
		try {
			httpDelete.setConfig(requestConfig);
			this.initMethodParam(httpDelete, contentType, encoding);
			return this.execute(httpDelete, this.getHttpClient());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			httpDelete.releaseConnection();
		}
	}
	
	public String executeHeaderDelete(String key,String value,String url, String param) {
		return this.executeHeaderDelete(key,value,url, null, "UTF-8", param);
	}
	
	public String executeHeaderDelete(String key,String value,String url, String contentType, String encoding, String param) {
		HttpDelete httpDelete = new HttpDelete(url);
		 
		try {
			httpDelete.addHeader(key, value);
			httpDelete.setConfig(requestConfig);
			this.initMethodParam(httpDelete, contentType, encoding);
			return this.execute(httpDelete, this.getHttpClient());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			httpDelete.releaseConnection();
		}
	}
	
	public String executePut(String url, String param) {
		return this.executeHeaderPut(url, null, "UTF-8", param);
	}
	
	public String executePut(String url, String contentType, String encoding, String param) {
		HttpPut httpPut = new HttpPut(url);		 
		try {
			httpPut.setConfig(requestConfig);
			this.initMethodParam(httpPut, contentType, encoding);
			if (param != null) {
				httpPut.setEntity(new StringEntity(param, encoding));
			}
			return this.execute(httpPut, this.getHttpClient());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			httpPut.releaseConnection();
		}
	}
	
	public String executeHeaderPut(String key,String value,String url, String param) {
		return this.executeHeaderPut(key,value,url, null, "UTF-8", param);
	}
	
	public String executeHeaderPut(String key,String value,String url, String contentType, String encoding, String param) {
		HttpPut httpPut = new HttpPut(url);
		 
		try {
			httpPut.addHeader(key, value);
			httpPut.setConfig(requestConfig);
			this.initMethodParam(httpPut, contentType, encoding);
			if (param != null) {
				httpPut.setEntity(new StringEntity(param, encoding));
			}
			return this.execute(httpPut, this.getHttpClient());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			httpPut.releaseConnection();
		}
	}
}
