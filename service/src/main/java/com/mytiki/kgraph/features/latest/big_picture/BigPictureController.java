package com.mytiki.kgraph.features.latest.big_picture;

import com.mytiki.common.ApiConstants;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = BigPictureController.PATH_CONTROLLER)
public class BigPictureController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE + "big-picture";

    private final BigPictureService bigPictureService;

    public BigPictureController(BigPictureService bigPictureService) {
        this.bigPictureService = bigPictureService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void webhook(@RequestBody BigPictureAOReqWebhook body){
        bigPictureService.update(body);
    }
}
