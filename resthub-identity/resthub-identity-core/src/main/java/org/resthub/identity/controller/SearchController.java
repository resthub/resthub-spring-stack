package org.resthub.identity.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.identity.model.AbstractPermissionsOwner;
import org.resthub.identity.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Front controller for performing searches.
 */
@Controller @RequestMapping("/api/search")
public class SearchController {

    // ----------------------------------------------------------------------------------------------------------------
    // Protected attributes

    /**
     * Classe's logger
     */
    protected final static Logger logger = LoggerFactory.getLogger(SearchController.class);

    /**
     * Search service. Injected by Spring.
     */
    @Inject
    @Named("searchService")
    protected SearchService searchService;

    /**
     * Performs a search.
     * 
     * @param query
     *            @QueryParam("query") Search query
     * @param withUsers
     *            @QueryParam("users")@DefaultValue("true") True to search on
     *            users
     * @param withGroups
     *            @QueryParam("groups")@DefaultValue("true") True to search on
     *            groups
     * @return An array of matching users, groups and roles.
     */
    @RequestMapping(method = RequestMethod.GET) @ResponseBody
    public List<AbstractPermissionsOwner> search(@RequestParam("query") String query,
    		@RequestParam(value = "users", required = false) Boolean withUsers,
    		@RequestParam(value = "groups", required = false) Boolean withGroups) {
        logger.debug("[search] Performs a search on " + query);
        
        withUsers = (withUsers == null) ? true : withUsers;
        withGroups = (withGroups == null) ? true : withGroups;
        
        List<AbstractPermissionsOwner> results = searchService.search(query, withUsers, withGroups);
        
        return results;
    }

    /**
     * Reset all search indexes
     */
    @RequestMapping(method = RequestMethod.PUT) @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetIndexes() {
        logger.debug("[resetIndexes] Reset all indexes");
        searchService.resetIndexes();
    }

}