package com.antevorta.requestlogging;

import com.antevorta.utils.RequestUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

@Component
public class CommonsRequestLoggingFilter extends AbstractRequestLoggingFilter {
    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return logger.isDebugEnabled();
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        logger.debug(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        logger.debug(message);
        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
    }
}
