/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.fingerprint;

import com.mytiki.common.ApiConstants;
import com.mytiki.common.reply.ApiReplyAO;
import com.mytiki.common.reply.ApiReplyAOFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = FingerprintController.PATH_CONTROLLER)
public class FingerprintController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE + "fingerprint";

    private final FingerprintService fingerprintService;

    public FingerprintController(FingerprintService fingerprintService) {
        this.fingerprintService = fingerprintService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ApiReplyAO<FingerprintAORsp> post(@RequestBody FingerprintAOReq body) {
        return ApiReplyAOFactory.ok(fingerprintService.find(body));
    }
}
