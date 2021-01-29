package com.thetechnovator.common.java.utils;

import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thetechnovator.common.java.exceptions.BusinessException;
import com.thetechnovator.common.java.exceptions.RestException;

public class RestUtil {
    private static Logger logger = LoggerFactory.getLogger(RestUtil.class);
    private static HttpClientContext httpContext;
	private static String technicalUserToken;
	
    static {
        // Cookies are enabled for session management, which is needed to avoid forcing
        // auth with every request
        CookieStore cookieStore = new BasicCookieStore();
        httpContext = HttpClientContext.create();
        httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
    }

    public static String getTechnicalUserToken() {
		return technicalUserToken;
	}
	public static void setTechnicalUserToken(String technicalUserToken) {
		RestUtil.technicalUserToken = technicalUserToken;
	}
	public static RestException getRestException(HttpResponse response) {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return new RestException("Unknown error", null, response.getStatusLine().getStatusCode());
        }
        String message, errorCode;
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(entity.getContent());
            message = getNodeText(node.get("message"));
            errorCode = getNodeText(node.get("erroCode"));
            if (message == null) {
            	message=HttpUtil.getResponseAsString(response);
            }
        } catch (Throwable e) {
            message = HttpUtil.getResponseAsString(response);
            errorCode=null;
        }
        return new RestException(message, errorCode, response.getStatusLine().getStatusCode());
    }
    private static String getNodeText(JsonNode node) {
    	if (node == null) {
    		return null;
    	}else {
    		return node.asText();
    	}
    }
    public static <T> T find(String url, Class<T> returnType) {
        HttpEntity entity = findEntity(url);
        if (entity == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        T obj;
        try {
            if (logger.isDebugEnabled()) {
                JsonNode json = mapper.readTree(entity.getContent());
                // logger.debug("RETURN: "+json.toString());
                obj = mapper.readValue(json.toString(), returnType);
            } else {
                obj = mapper.readValue(entity.getContent(), returnType);
            }
        } catch (Throwable e) {
            throw new RestException(e);
        }
        return obj;
    }

    public static JsonNode find(String url) {
        HttpEntity entity = findEntity(url);
        if (entity == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();

        JsonNode json;
        try {
            json = mapper.readTree(entity.getContent());
        } catch (Throwable e) {
            throw new RestException(e);
        }
        // logger.debug("RETURN: "+json.toString());
        return json;
    }
    public static HttpEntity findEntity(String url) {
        HttpClient client = createHttpClient(url);
        HttpGet request = new HttpGet(url);
        setAuthenticationHeader(request);

        HttpResponse response;
        try {
            logger.info("Calling REST: " + url);
            response = client.execute(request, httpContext);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_NOT_FOUND) {
					throw getHttpResponseException(url, response);						
				}
                return null;
            }
            HttpEntity entity = response.getEntity();
            return entity;

        } catch (Throwable e) {
            throw new RestException(e);
        }
    }
	private static RestException getHttpResponseException(String url, HttpResponse response) {
		String msg;
		String errorCode=null;
		HttpEntity entity = response.getEntity();
		if (entity == null || entity.getContentLength()==0) {
			msg = response.getStatusLine().getReasonPhrase();
		}else {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = null;
			try {
				String contents = new String(IOUtils.readFully(entity.getContent(), (int)entity.getContentLength()));
				if (contents.length() > 0) {
					node = mapper.readTree(contents);
				}
				if (node != null) {
					msg = getNodeText(node.get("message"));
					errorCode = getNodeText(node.get("errorCode"));
					if (msg == null) {
						msg = contents;
					}
				}else {
					msg = contents;
				}
			} catch (UnsupportedOperationException e) {
				msg=HttpUtil.getResponseAsString(entity);
			} catch (IOException e) {
				msg = response.getStatusLine().getReasonPhrase();
			}
		}
		return new RestException(msg+". HttpStatus: "+response.getStatusLine().getStatusCode(), errorCode,response.getStatusLine().getStatusCode());
	}
	
    public static void printCookies(String msg) {
        logger.info(msg);
        for (Cookie cookie : httpContext.getCookieStore().getCookies()) {
            logger.info(cookie.toString());
        }

    }

    public static JsonNode get(String url) {
        HttpClient client = createHttpClient(url);
        return get(url, client);
    }

    public static <T> T getObjectFromJson(String json, Class<T> clz) {
    	ObjectMapper mapper = new ObjectMapper();
		try {
			T obj = mapper.readValue(json, clz);
			
			return obj;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		
    }
    public static JsonNode get(String url, HttpClient client) {
        HttpGet request = new HttpGet(url);
        setAuthenticationHeader(request);
        HttpResponse response;

        try {
            logger.debug("Calling REST: " + url);
            response = client.execute(request);

            logger.info(response.toString());

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            	RestException e = getRestException(response);
                logger.warn("Rest call to " + url + " failed: " + e.getMessage());
                throw e;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new IllegalStateException("No result from call to " + url);
            }
            ObjectMapper mapper = new ObjectMapper();

            JsonNode json = mapper.readTree(entity.getContent());
            // logger.debug("RETURN: "+json.toString());
            return json;
        } catch (Throwable e) {
            throw new RestException(e);
        }
    }

    public static String post(String url, Object object) {
        return internalPost(url, object);
    }

    public static HttpResponse httpsCallBasicAuth(String url, String user, String password) {

        try {
            HttpClient client = HttpUtil.getHttpsClient();
            HttpGet request = new HttpGet(url);

            String authString = user + ":" + password;
            String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());
            //String authStringEnc = new BASE64Encoder().encode(authString.getBytes());

            request.setHeader("Authorization", "Basic " + authStringEnc);

            HttpResponse response = client.execute(request);

            return response;
        } catch (Throwable e) {
            throw new RestException(e);
        }
    }

    private static String internalPost(String url, Object object) {
        HttpClient client = createHttpClient(url);
        return internalPost(url, object, client);
    }

    private static  String internalPost(String url, Object object, HttpClient client) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(object);
            // logger.debug(json);
            HttpPost httpPost = new HttpPost(url);
            setAuthenticationHeader(httpPost);
            httpPost.setEntity(new StringEntity(json, "UTF-8"));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json;charset=UTF-8");
            // logger.debug("Calling REST: "+url);
            HttpResponse response = client.execute(httpPost, httpContext);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                return response.getFirstHeader("Location").getValue();
            } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return HttpUtil.getResponseAsString(response);
            } else {
            	RestException e = getRestException(response);
                logger.warn("Rest call to " + url + " failed: " + e.getMessage());
                throw e;
            }
        } catch (Throwable e) {
            throw new RestException(e);
        }
    }

    public static void setAuthenticationHeader(HttpRequest request) {
    	if (technicalUserToken != null) {
    		logger.info("Setting Bearer token");
    		request.setHeader("Authorization", "Bearer " + technicalUserToken );
    	}
    }

    private static HttpClient createHttpClient(String url) {
    	if (url.startsWith("https")) {
    		try {
				return HttpUtil.getHttpsClient();
			} catch (BusinessException e) {
				throw new IllegalStateException(e);
			}
    	}else {
    		return HttpUtil.getHttpClient();
    	}

    }
    
    public static String delete(String url) {
        HttpClient client = createHttpClient(url);

        return internalDeletet(url, client);
    }
    
    private static  String internalDeletet(String url, HttpClient client) {
        try {
            HttpDelete httpDelete = new HttpDelete(url);
            setAuthenticationHeader(httpDelete);
            httpDelete.setHeader("Accept", "application/json");
            httpDelete.setHeader("Content-type", "application/json;charset=UTF-8");
            // logger.debug("Calling REST: "+url);
            HttpResponse response = client.execute(httpDelete, httpContext);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return HttpUtil.getResponseAsString(response);
            } else {
            	RestException e = getRestException(response);
                logger.warn("Rest call to " + url + " failed: " +e.getMessage()); 
                throw e;
            }
        } catch (Throwable e) {
            throw new RestException(e);
        }
    }

}
