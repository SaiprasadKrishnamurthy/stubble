package core;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import model.ApiDef;
import org.junit.Test;
import spark.Request;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by sai on 18/09/2015.
 */
public class RouteResolverTest {

    private final List<ApiDef> availableDefinitions = new Repository(RouteResolverTest.class.getClassLoader().getResourceAsStream("test-api-defs.yml")).apiDefinitions();

    @Test
    public void shouldFindRouteConfigWithPathVariablesOnly() throws Exception {

        // Test request
        Request rq = new Request() {
            public String uri() {
                return "/sai/11223344/registration";
            }

            @Override
            public Set<String> headers() {
                return ImmutableSet.of("header1");
            }

            @Override
            public String headers(final String header) {
                return "headerValue1";
            }

            @Override
            public Map<String, String> cookies() {
                return ImmutableMap.of("cookie1", "cookieValue1");
            }

            @Override
            public String requestMethod() {
                return "get";
            }

            @Override
            public String queryString() {
                return null;
            }
        };

        RoutingContext routingContext = Router.context(rq, availableDefinitions);

        assertNotNull(routingContext);
        assertThat(routingContext.getUri(), equalTo(ImmutableMap.of("customer", "sai", "phone", "11223344")));
        assertThat(routingContext.getRequestHeaders(), equalTo(ImmutableMap.of("header1", "headerValue1")));
        assertThat(routingContext.getRequestCookies(), equalTo(ImmutableMap.of("cookie1", "cookieValue1")));
        assertThat(routingContext.getRoutingTemplateName(), equalTo("registrationFile.hb"));
        assertThat(routingContext.getResponseStatus(), equalTo(200));
        assertThat(routingContext.getResponseCookies(), equalTo(ImmutableMap.of("cookie1", "cookievalue1", "cookie2", "cookievalue2")));
        assertThat(routingContext.getResponseHeaders(), equalTo(ImmutableMap.of("header2", "headervalue2", "header3", "headervalue3")));
        assertThat(routingContext.getResponseContentType(), equalTo("application/json"));

    }

    @Test
    public void shouldFindRouteConfigWithPathVariablesAndQueryString() throws Exception {

        // Test request
        Request rq = new Request() {
            public String uri() {
                return "/sai/11223344/billing";
            }

            @Override
            public Set<String> headers() {
                return ImmutableSet.of("header1");
            }

            @Override
            public String headers(final String header) {
                return "headerValue1";
            }

            @Override
            public Map<String, String> cookies() {
                return ImmutableMap.of("cookie1", "cookieValue1");
            }

            @Override
            public String requestMethod() {
                return "get";
            }

            @Override
            public String queryString() {
                return "detailed=yes";
            }
        };

        RoutingContext routingContext = Router.context(rq, availableDefinitions);

        assertNotNull(routingContext);
        assertThat(routingContext.getUri(), equalTo(ImmutableMap.of("customer", "sai", "phone", "11223344")));
        assertThat(routingContext.getRequestHeaders(), equalTo(ImmutableMap.of("header1", "headerValue1")));
        assertThat(routingContext.getRequestCookies(), equalTo(ImmutableMap.of("cookie1", "cookieValue1")));
        assertThat(routingContext.getRoutingTemplateName(), equalTo("registrationFile.hb"));
        assertThat(routingContext.getResponseStatus(), equalTo(200));
        assertThat(routingContext.getResponseCookies(), equalTo(ImmutableMap.of("cookie1", "cookievalue1", "cookie2", "cookievalue2")));
        assertThat(routingContext.getResponseHeaders(), equalTo(ImmutableMap.of("header2", "headervalue2", "header3", "headervalue3")));
        assertThat(routingContext.getRequestParams(), equalTo(ImmutableMap.of("detailedFlag", "yes")));
        assertThat(routingContext.getResponseContentType(), equalTo("application/json"));
    }

    @Test
    public void shouldFindRouteConfigWithPathVariablesPUT() throws Exception {

        // Test request
        Request rq = new Request() {
            public String uri() {
                return "/sai/1234/address";
            }

            @Override
            public String requestMethod() {
                return "put";
            }

            @Override
            public String queryString() {
                return null;
            }
            @Override
            public Set<String> headers() {
                return ImmutableSet.of("header1");
            }

            @Override
            public String headers(final String header) {
                return "headerValue1";
            }

            @Override
            public Map<String, String> cookies() {
                return ImmutableMap.of("cookie1", "cookieValue1");
            }

        };

        RoutingContext routingContext = Router.context(rq, availableDefinitions);

        assertNotNull(routingContext);
        assertThat(routingContext.getUri(), equalTo(ImmutableMap.of("customer", "sai", "phone", "1234")));
        assertThat(routingContext.getResponseStatus(), equalTo(201));
    }
}
