/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.utilities;

import com.mytiki.common.reply.ApiReplyAO;
import com.mytiki.common.reply.ApiReplyAOFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

public class ApiExceptionHandler extends com.mytiki.common.exception.ApiExceptionHandler {
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiReplyAO<?>> handleAccessDenied(AccessDeniedException e, HttpServletRequest request) {
        logger.trace("Request: " + request.getRequestURI() + "caused {}", e);
        ApiReplyAO<?> reply = ApiReplyAOFactory.error(HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(reply);
    }
}
