/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.add;

import com.mytiki.common.ApiConstants;
import com.mytiki.common.reply.ApiReplyAO;
import com.mytiki.common.reply.ApiReplyAOFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = AddController.PATH_CONTROLLER)
public class AddController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE + "add";

    private final AddService addService;

    public AddController(AddService addService) {
        this.addService = addService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ApiReplyAO<AddAO> post(@RequestBody AddAO body) {
        return ApiReplyAOFactory.ok(addService.execute(body));
    }
}