/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.utilities;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

public class ApiKeyFilter extends AbstractPreAuthenticatedProcessingFilter {

    private static final String HEADER = "X-API-Key";

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return request.getHeader(HEADER);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "not implemented";
    }

    public ApiKeyFilter setAuthMgr(ApiKeyAuthMgr mgr){
        this.setAuthenticationManager(mgr);
        return this;
    }
}
