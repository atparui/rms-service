package com.atparui.rmsservice.web.rest;

import com.atparui.rmsservice.service.AppMenuAccessService;
import com.atparui.rmsservice.service.dto.AppMenuTreeDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller to fetch menu tree filtered by permissions.
 */
@RestController
@RequestMapping("/api/app-menus")
public class AppMenuAccessResource {

    private static final Logger LOG = LoggerFactory.getLogger(AppMenuAccessResource.class);

    private final AppMenuAccessService appMenuAccessService;

    public AppMenuAccessResource(AppMenuAccessService appMenuAccessService) {
        this.appMenuAccessService = appMenuAccessService;
    }

    /**
     * GET /api/app-menus/tree : get permitted menu tree for current user.
     *
     * @param appKey optional application key to filter menus for specific client
     */
    @GetMapping("/tree")
    public ResponseEntity<List<AppMenuTreeDTO>> getMenuTree(@RequestParam(value = "appKey", required = false) String appKey) {
        LOG.debug("REST request to get app menu tree for appKey {}", appKey);
        return ResponseEntity.ok(appMenuAccessService.getMenuTreeForCurrentUser(appKey));
    }
}
