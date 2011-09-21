package org.resthub.identity.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.resthub.identity.model.AbstractPermissionsOwner;
import org.resthub.identity.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Front controller for performing searches.
 */
@Path("/search")
@Named("searchController")
@RolesAllowed({ "IM_SEARCH" })
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
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

    // ----------------------------------------------------------------------------------------------------------------
    // Public methods

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
    @GET
    public Response search(@QueryParam("query") String query,
            @QueryParam("users") @DefaultValue("true") Boolean withUsers,
            @QueryParam("groups") @DefaultValue("true") Boolean withGroups) {
        logger.debug("[search] Performs a search on " + query);
        ResponseBuilder response = Response.serverError();
        try {
            List<AbstractPermissionsOwner> results = searchService.search(query, withUsers, withGroups);
            // GenericEntity allows us to return a list of resource, tackling
            // the type erasure problem.
            GenericEntity<List<AbstractPermissionsOwner>> entity = new GenericEntity<List<AbstractPermissionsOwner>>(
                    results) {
            };
            response = Response.ok(entity);
        } catch (Exception exc) {
            response.entity(exc.getMessage());
        }
        return response.build();
    } // search().

    /**
     * Reset all search indexes
     */
    @PUT
    public void resetIndexes() {
        logger.debug("[resetIndexes] Reset all indexes");
        searchService.resetIndexes();
    } // resetIndexes().

} // Class SearchController.
