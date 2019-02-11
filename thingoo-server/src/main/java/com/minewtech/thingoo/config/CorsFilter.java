package com.minewtech.thingoo.config;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


// When @EnableWebSecurity is on then @Component shouldnt be used else the filter will be registered twice
@Component
public class CorsFilter implements Filter {

    protected static final Logger LOG = LoggerFactory.getLogger(CorsFilter.class);

    @Value("#{'${access-control-allowed-origins:*}'.replaceAll(\"\\s*\",\"\").split(',')}")
    private List<String> accessControlAllowedOrigins;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        String reqOrigin = ((HttpServletRequest) req).getHeader("Origin");
//        if (accessControlAllowedOrigins.contains(reqOrigin)) {
        if (reqOrigin != null && !reqOrigin.isEmpty()) {
        res.setHeader("Access-Control-Allow-Origin", reqOrigin);
        res.setHeader("Access-Control-Allow-Credentials", "true");
        } else {
            res.setHeader("Access-Control-Allow-Origin", "*");
//            res.setHeader("Access-Control-Allow-Credentials", "false");
//            res.setHeader("Access-Control-Allow-Credentials", "true");
        }
        LOG.debug("Origin: " + reqOrigin +"; Access-Control-Allow-Origin: " + res.getHeader("Access-Control-Allow-Origin"));

        res.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
//        res.setHeader("Access-Control-Max-Age", "3600");
        res.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Accept-Encoding, Accept-Language, Host, Referer, Connection, User-Agent, authorization, sw-useragent, sw-version");
//        res.setHeader("X-XSS-Protection", "0");

        // Just REPLY OK if request method is OPTIONS for CORS (pre-flight)
        if (req.getMethod().equals("OPTIONS")) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}