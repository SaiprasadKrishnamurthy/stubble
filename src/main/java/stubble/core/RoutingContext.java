package stubble.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sai on 18/09/2015.
 */
public class RoutingContext {
    private Map<String, String> requestParams = new HashMap<>();
    private Map<String, String> uri = new HashMap<>();
    private Map<String, String> requestHeaders = new HashMap<>();
    private Map<String, String> requestCookies = new HashMap<>();
    private Map<String, String> responseCookies = new HashMap<>();
    private Map<String, String> responseHeaders = new HashMap<>();
    private Map<String, String> requestBody = new HashMap<>();
    private int responseStatus = 201;
    private String routingTemplateName;
    private String responseContentType = "application/json";


    public Map<String, String> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(Map<String, String> requestParams) {
        this.requestParams = requestParams;
    }

    public Map<String, String> getUri() {
        return uri;
    }

    public void setUri(Map<String, String> uri) {
        this.uri = uri;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public Map<String, String> getRequestCookies() {
        return requestCookies;
    }

    public void setRequestCookies(Map<String, String> requestCookies) {
        this.requestCookies = requestCookies;
    }

    public Map<String, String> getResponseCookies() {
        return responseCookies;
    }

    public void setResponseCookies(Map<String, String> responseCookies) {
        this.responseCookies = responseCookies;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getRoutingTemplateName() {
        return routingTemplateName;
    }

    public void setRoutingTemplateName(String routingTemplateName) {
        this.routingTemplateName = routingTemplateName;
    }

    public String getResponseContentType() {
        return responseContentType;
    }

    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }

    public Map<String, String> getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Map<String, String> requestBody) {
        this.requestBody = requestBody;
    }
}
