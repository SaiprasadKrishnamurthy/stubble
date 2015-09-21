package stubble.model;

/**
 * Created by sai on 18/09/2015.
 */
public class ApiDefinition {

    private String api;
    private String httpverb;
    private String uri;
    private Integer responseStatus;
    private String responseCookies;
    private String responseHeaders;
    private String responseTemplateFile;

    /**
     * @return The api
     */
    public String getApi() {
        return api;
    }

    /**
     * @param api The api
     */
    public void setApi(String api) {
        this.api = api;
    }

    /**
     * @return The httpverb
     */
    public String getHttpverb() {
        return httpverb;
    }

    /**
     * @param httpverb The httpverb
     */
    public void setHttpverb(String httpverb) {
        this.httpverb = httpverb;
    }

    /**
     * @return The uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri The uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return The responseStatus
     */
    public Integer getResponseStatus() {
        return responseStatus;
    }

    /**
     * @param responseStatus The responseStatus
     */
    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    /**
     * @return The responseCookies
     */
    public String getResponseCookies() {
        return responseCookies;
    }

    /**
     * @param responseCookies The responseCookies
     */
    public void setResponseCookies(String responseCookies) {
        this.responseCookies = responseCookies;
    }

    /**
     * @return The responseHeaders
     */
    public String getResponseHeaders() {
        return responseHeaders;
    }

    /**
     * @param responseHeaders The responseHeaders
     */
    public void setResponseHeaders(String responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    /**
     * @return The responseTemplateFile
     */
    public String getResponseTemplateFile() {
        return responseTemplateFile;
    }

    /**
     * @param responseTemplateFile The responseTemplateFile
     */
    public void setResponseTemplateFile(String responseTemplateFile) {
        this.responseTemplateFile = responseTemplateFile;
    }
}
