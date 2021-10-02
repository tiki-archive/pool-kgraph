/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.utilities;

import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.ClockSkewAware;
import com.nimbusds.jwt.proc.JWTClaimsSetVerifier;
import com.nimbusds.jwt.util.DateUtils;
import net.jcip.annotations.ThreadSafe;

import java.util.Date;
import java.util.List;

// Adapted from com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier.java

@ThreadSafe
public class JwtClaimsVerifier<C extends SecurityContext> implements JWTClaimsSetVerifier<C>, ClockSkewAware {
    public static final int DEFAULT_MAX_CLOCK_SKEW_SECONDS = 60;
    private static final String REQUIRED_CUSTOMER_AUD = "https://knowledge.mytiki.com";
    private int maxClockSkew = DEFAULT_MAX_CLOCK_SKEW_SECONDS;

    @Override
    public int getMaxClockSkew() {
        return maxClockSkew;
    }

    @Override
    public void setMaxClockSkew(final int maxClockSkewSeconds) {
        maxClockSkew = maxClockSkewSeconds;
    }

    @Override
    public void verify(JWTClaimsSet claimsSet, C context) throws BadJWTException {
        if(!claimsSet.getIssuer().equals(Constants.CLAIM_ISS_BOUNCER)){
            List<String> audList = claimsSet.getAudience();
            if(audList == null || audList.isEmpty())
                throw new BadJWTException("JWT missing required audience");
            boolean audMatch = false;
            for (String aud : audList) {
                if (aud.equals(REQUIRED_CUSTOMER_AUD)){
                    audMatch = true;
                    break;
                }
            }
            if (!audMatch) {
                throw new BadJWTException("JWT audience rejected: " + audList);
            }
        }

        final Date now = new Date();
        final Date exp = claimsSet.getExpirationTime();
        if (exp != null) {
            if (!DateUtils.isAfter(exp, now, maxClockSkew)) {
                throw new BadJWTException("Expired JWT");
            }
        }

        final Date nbf = claimsSet.getNotBeforeTime();
        if (nbf != null) {
            if (!DateUtils.isBefore(nbf, now, maxClockSkew)) {
                throw new BadJWTException("JWT before use time");
            }
        }
    }
}
