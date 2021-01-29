package com.thetechnovator.common.java.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thetechnovator.common.java.exceptions.BusinessException;

public class HttpUtil {
	private static final Logger LOG = LoggerFactory.getLogger(HttpUtil.class);
	private static CookieStore cookieStore = new BasicCookieStore();
	public static CloseableHttpClient getHttpClient() {
		return HttpClients.custom().build();
	}

	public static CloseableHttpClient getAuthenticatedHttpClient(String userName, String password) {
		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		CloseableHttpClient client = HttpClients.custom() //
				.setDefaultCredentialsProvider(provider) //
				.setDefaultCookieStore(cookieStore) //
				.setRedirectStrategy(new LaxRedirectStrategy()) //
				.build();

		return client;
	}
	public static CloseableHttpClient getAuthenticatedHttpsClient(String userName, String password)
			throws BusinessException {
		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		SSLContext sslContext;
		try {
			sslContext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy() {

				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}

			}).build();

			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
			Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("https", socketFactory).build();
			HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(reg);
			CloseableHttpClient httpClient = HttpClients.custom() //
					.setConnectionManager(cm)//
					.setDefaultCredentialsProvider(provider)//
					.setDefaultCookieStore(cookieStore)//
					.setRedirectStrategy(new LaxRedirectStrategy())//
					.build();

			return httpClient;
		} catch (KeyManagementException e) {
			throw new BusinessException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new BusinessException(e);
		} catch (KeyStoreException e) {
			throw new BusinessException(e);
		}

	}

	public static CloseableHttpClient getHttpsClient() throws BusinessException {
		SSLContext sslContext;
		try {
			sslContext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy() {

				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}

			}).build();
			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
			Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("https", socketFactory).build();
			HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(reg);
			CloseableHttpClient httpClient = HttpClients.custom() //
					.setConnectionManager(cm) //
					.setDefaultCookieStore(cookieStore) //
					.setRedirectStrategy(new LaxRedirectStrategy())
					.build();
		
			return httpClient;
		} catch (KeyManagementException e) {
			throw new BusinessException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new BusinessException(e);
		} catch (KeyStoreException e) {
			throw new BusinessException(e);
		}

	}

	public static HttpClient getSelfSignedStrategyClient() {
		SSLContext context = null;
		try {
			context = SSLContexts.custom()
			        .loadTrustMaterial(TrustSelfSignedStrategy.INSTANCE)
			        .build();
		} catch (KeyManagementException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		} catch (KeyStoreException e) {
			throw new IllegalStateException(e);
		}

	    Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
	            .register("http", PlainConnectionSocketFactory.INSTANCE)
	            .register("https", new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE))
	            .build();

	    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);

	    CloseableHttpClient httpClient = HttpClients.custom()
	            .setConnectionManager(connectionManager) //
	            .setRedirectStrategy(new LaxRedirectStrategy()) //
	            .build();		
		
	    return httpClient;
	}
	public static void downloadFile(URL fileUrl, File outputFile) throws BusinessException {
		HttpClient client = null;
		if (fileUrl.getProtocol().equals("https")) {
			client = getHttpsClient();
		} else {
			client = getHttpClient();
		}
		downloadFile(client, fileUrl, outputFile);
	}

	public static void downloadFile(URL fileUrl, String userName, String password, File outputFile)
			throws BusinessException {
		HttpClient client = null;
		if (fileUrl.getProtocol().equals("https")) {
			client = getAuthenticatedHttpsClient(userName, password);
		} else {
			client = getAuthenticatedHttpClient(userName, password);
		}
		downloadFile(client, fileUrl, outputFile);
	}

	private static void downloadFile(HttpClient client, URL fileUrl, File outputFile) throws BusinessException {
		HttpGet request = new HttpGet(fileUrl.toString());
		LOG.info("Downloading file: " + request.getURI());
		HttpResponse response;
		try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();

			int responseCode = response.getStatusLine().getStatusCode();
			if (responseCode != HttpStatus.SC_OK) {
				LOG.warn("Response Code: " + responseCode);
				throw new BusinessException("Error downloading file: " + request.getURI() + ". Response Code: "
						+ responseCode + ". Message:" + getResponseAsString(response));
			}

			InputStream is = entity.getContent();

			FileOutputStream fos = new FileOutputStream(outputFile);

			int inByte;
			while ((inByte = is.read()) != -1) {
				fos.write(inByte);
			}

			is.close();
			fos.close();
			LOG.info("File downloaded: " + request.getURI() + " to " + outputFile.getAbsolutePath());
		} catch (ClientProtocolException e) {
			throw new BusinessException(e);
		} catch (IOException e) {
			throw new BusinessException(e);
		}
	}

	public static String getResponseAsString(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		if (entity == null)
			return "";
		return getResponseAsString(entity);
	}
	public static String getResponseAsString(HttpEntity entity) {
		try {
			InputStream inputStream = entity.getContent();
			byte[] buffer = new byte[1024];
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			int bytesRead = 0;
			StringBuilder sb = new StringBuilder();
			while ((bytesRead = bis.read(buffer)) != -1) {
				String chunk = new String(buffer, 0, bytesRead);
				sb.append(chunk);
				;
			}

			return sb.toString();
		} catch (UnsupportedOperationException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	public static String postForm(String url, List<NameValuePair> params, HttpClient client, HttpClientContext httpContext) {
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));
			HttpResponse response = client.execute(httpPost, httpContext);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return getResponseAsString(response);
			} else {
				throw new IllegalStateException("Rest call to " + url + " failed: " + getResponseAsString(response));
			}
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		} catch (ClientProtocolException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}


}
