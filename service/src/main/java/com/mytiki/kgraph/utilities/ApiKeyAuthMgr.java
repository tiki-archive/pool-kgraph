/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.utilities;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class ApiKeyAuthMgr implements AuthenticationManager {

    private final String key;

    public ApiKeyAuthMgr(String key) {
        this.key = key;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String principal = (String) authentication.getPrincipal();
        if (!key.equals(principal))
            throw new BadCredentialsException("The API key was not found or not the expected value.");
        authentication.setAuthenticated(true);
        return authentication;
    }
}
