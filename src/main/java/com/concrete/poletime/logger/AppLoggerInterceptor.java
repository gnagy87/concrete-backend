package com.concrete.poletime.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collections;

@Component
@Async
@Transactional
public class AppLoggerInterceptor extends HandlerInterceptorAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(AppLoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String postData;
        HttpServletRequest requestCacheWrapperObject = null;

        try {
            requestCacheWrapperObject = new ContentCachingRequestWrapper(request);
            requestCacheWrapperObject.getParameterMap();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postData = readPayload(requestCacheWrapperObject);
            LOGGER.info("REQUEST DATA: " + postData);
            for (String r : Collections.list(request.getHeaderNames())) {
                LOGGER.info(r + " : " + request.getHeader(r));
            }
        }
        return true;
    }



    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LOGGER.info("RESPONSE: " + response.getStatus());
    }

    private String readPayload(final HttpServletRequest request) throws IOException {
        String payloadData = null;
        ContentCachingRequestWrapper contentCachingRequestWrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (null != contentCachingRequestWrapper) {
            byte[] buf = contentCachingRequestWrapper.getContentAsByteArray();
            if (buf.length > 0) {
                payloadData = new String(buf, 0, buf.length, contentCachingRequestWrapper.getCharacterEncoding());
            }
        }
        return payloadData;
    }
}
