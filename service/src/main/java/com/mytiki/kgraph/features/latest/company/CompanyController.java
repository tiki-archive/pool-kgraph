package com.mytiki.kgraph.features.latest.company;

import com.mytiki.common.ApiConstants;
import com.mytiki.common.reply.ApiReplyAO;
import com.mytiki.common.reply.ApiReplyAOFactory;
import com.mytiki.kgraph.utilities.Constants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping(value = CompanyController.PATH_CONTROLLER)
public class CompanyController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE + "vertex/company";

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RolesAllowed(Constants.ROLE_USER)
    @RequestMapping(method = RequestMethod.GET)
    public ApiReplyAO<CompanyAO> getDomain(@RequestParam String domain){
        return ApiReplyAOFactory.ok(companyService.findByDomain(domain));
    }
}
