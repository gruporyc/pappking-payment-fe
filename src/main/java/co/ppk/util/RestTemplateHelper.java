/******************************************************************
 *
 * This code is for the Pappking service project.
 *
 *
 * © 2018, Pappking Management All rights reserved.
 *
 *
 ******************************************************************/
package co.ppk.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility for abstract complexity in the interactions with restful services
 * 
 * @author jmunoz
 * @version 1.0
 * 
 */

@Component
public final class RestTemplateHelper {

	private static final Logger LOGGER = LogManager.getLogger(RestTemplateHelper.class.getCanonicalName());
	/* Custom Headers */
	public static final String LANGUAGE_HEADER = "language";
	public static final String PAYMENT_CLIENT_TOKEN = "payment-client-key";
    public static final String PAYMENT_CLIENT_TOKEN_VALUE = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJhNzJjZmJjNi00Nzc0LTQ2NTAtODBjYy04ZjJhNDcxYmQ2ZDIiLCJpYXQiOjE1NDEzNTY3NTAsInN1YiI6IiIsImV4cCI6MTU3Mjg5Mjc1MH0.lY1V6Jh8cFbqLSHGgHjbEoWyKObg9dT7CNwxVqIlpOufD4DsodNHgp7Irvzfx6cPbOe_cHq2ZmYrgQWFvtQ98A";
	/* session objects names */
	public static final String CURRENT_USER_LOCALE = "language";
	public static final String DEFAULT_USER_LOCALE = "es_CO";
	private RestTemplate rt;
	private ObjectMapper om;

	/**
	 * empty constructor that configure the component responsible of the objects
	 * transformations processed by this class
	 * 
	 */
	public RestTemplateHelper() {
		om = new ObjectMapper();
		om.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
		om.setVisibilityChecker(
				VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

		TrustStrategy acceptingTrustStrategy = new TrustStrategy() {

			@Override
			public boolean isTrusted(java.security.cert.X509Certificate[] xcs, String string)
					throws CertificateException {
				return true;
			}
		};

		SSLContext sslContext = null;
		try {
			sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
					.build();
		} catch (NoSuchAlgorithmException ex) {
			LOGGER.error("NoSuchAlgorithmException", ex);
		} catch (KeyStoreException ex) {
			LOGGER.error("KeyStoreException", ex);
		} catch (KeyManagementException ex) {
			LOGGER.error("KeyManagementException", ex);
		}

		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

		requestFactory.setHttpClient(httpClient);

		rt = new RestTemplate(requestFactory);
		

	}

	public <T> ResponseEntity<T> processRequestBase(String url, Map<String, String> obj, Class<T> response,
			HttpMethod method) {



		HttpHeaders headers = getBasicHeaders();
		HttpEntity<String> request = null;
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		try {
			request = new HttpEntity<>(om.writeValueAsString(obj), headers);
		} catch (JsonProcessingException e) {
			LOGGER.error("processRequest", e);
		}

			String uri = (obj == null) ? url : builder.buildAndExpand(obj).toUri().toString();
			return rt.exchange(uri, method, request, response);

	}

	public <T> ResponseEntity<T> processRequestBase(String url, Object request, Class<T> response, HttpMethod method) {



		ResponseEntity<T> resp = null;

		HttpHeaders headers = getBasicHeaders();

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

		HttpEntity<?> req = new HttpEntity<>(request, headers);

		if (req != null) {
			String uri = (request == null) ? url : builder.buildAndExpand(request).toUri().toString();
			resp = rt.exchange(uri, method, req, response);
		}
		return resp;
	}

	public <T> ResponseEntity<T> processRequestBase(String url, Class<T> response, HttpMethod method) {

		HttpHeaders headers = getBasicHeaders();

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

		HttpEntity<?> req = new HttpEntity<>(null, headers);

		return rt.exchange(builder.toUriString(), method, req, response);
	}

	public <T> ResponseEntity<T> processRequestBase(String url, Map<String, String> obj, Class<T> response,
			HttpMethod method, Map<String, String> queryParams) {

		// LOGGER.debug("url=" + url);

		HttpHeaders headers = getBasicHeaders();
		HttpEntity<String> request = null;
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

		for (Map.Entry<String, String> entry : queryParams.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			builder.queryParam(key, value);
		}

		try {
			request = new HttpEntity<>(om.writeValueAsString(obj), headers);
		} catch (JsonProcessingException e) {
			LOGGER.error("processRequest", e);
		} catch (Exception e) {
			LOGGER.error("Exception", e);
		}

		
			String uri = (obj == null) ? builder.toUriString() : builder.buildAndExpand(obj).toUri().toString();
			ResponseEntity<T> RR = rt.exchange(uri, method, request, response);
			return RR;
		
	}

	public <T> ResponseEntity<T> processRequestBase(String url, List<Map<String, String>> obj, Class<T> response,
			HttpMethod method) {

		HttpHeaders headers = getBasicHeaders();
		HttpEntity<String> request = null;
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		try {
			request = new HttpEntity<>(om.writeValueAsString(obj), headers);
		} catch (JsonProcessingException e) {
			LOGGER.error("processRequest", e);
		}
		
			String uri = (obj == null) ? url : builder.buildAndExpand(obj).toUri().toString();
			ResponseEntity<T> T = rt.exchange(uri, method, request, response);

			LOGGER.info("T=" + T.getStatusCode());
			return T;
		
	}

	private HttpHeaders getBasicHeaders() {

		HttpHeaders headers = new HttpHeaders();

		headers.set(PAYMENT_CLIENT_TOKEN, PAYMENT_CLIENT_TOKEN_VALUE);

		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	public <T> ResponseEntity<T> processRequestGet(String url, Class<T> response) {
		return processRequestBase(url, new HashMap<>(), response, HttpMethod.GET);
	}

	public <T> ResponseEntity<T> processRequestPost(String url, Map<String, String> obj, Class<T> response) {
		return processRequestBase(url, obj, response, HttpMethod.POST);
	}
	public <T> ResponseEntity<T> processRequestPostParams(String url, Map<String, String> obj, Class<T> response,Map<String, String> queryParams) {
		return processRequestBase(url, obj, response, HttpMethod.POST,queryParams);
	}

	public <T> ResponseEntity<T> processRequestPostObject(String url, Map<String, Object> obj, Class<T> response) {
		return processRequestBase(url, obj, response, HttpMethod.POST);
	}

	public <T> ResponseEntity<T> processRequestPatch(String url, Map<String, String> obj, Class<T> response) {
		return processRequestBase(url, obj, response, HttpMethod.PATCH);
	}

	public <T> ResponseEntity<T> processRequestGet(String url, Map<String, String> obj, Class<T> response) {
		return processRequestBase(url, obj, response, HttpMethod.GET);
	}

	public <T> ResponseEntity<T> processRequestGet(String url, Map<String, String> obj, Class<T> response,
			Map<String, String> queryParams) {
		return processRequestBase(url, obj, response, HttpMethod.GET, queryParams);
	}

	public <T> ResponseEntity<T> processRequestPut(String url, Map<String, String> obj, Class<T> response) {
		return processRequestBase(url, obj, response, HttpMethod.PUT);
	}

	public <T> ResponseEntity<T> processRequestPost(String url, List<Map<String, String>> obj, Class<T> response) {
		return processRequestBase(url, obj, response, HttpMethod.POST);
	}

	public <T> ResponseEntity<T> processRequestPostExtended(String url, Object body, Class<T> response) {
		return processRequestBase(url, body, response, HttpMethod.POST);
	}

	public <T> ResponseEntity<T> processRequestPatchExtended(String url, Object body, Class<T> response) {
		return processRequestBase(url, body, response, HttpMethod.PATCH);
	}

	public <T> ResponseEntity<T> processRequestDelete(String url, Class<T> response) {
		return processRequestBase(url, response, HttpMethod.DELETE);
	}
}
