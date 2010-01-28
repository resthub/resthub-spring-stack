package org.resthub.web.tal.resolver;

import java.net.URI;
import java.net.URL;

import org.resthub.web.tal.PageTemplate;
import org.resthub.web.tal.exception.PageTemplateException;

public class DefaultResolver extends Resolver {
	private URIResolver uriResolver;
	private Resolver userResolver = null;

    public DefaultResolver(URI uri, Resolver userResolver) {
        if ( uri != null ) {
            uriResolver = new URIResolver( uri );
        }
        
        if(userResolver != null ) {
        	this.userResolver = userResolver;
        }
    }

    public URL getResource( String path ) 
        throws java.net.MalformedURLException
    {
        URL resource = null;
        
        // If user has supplied resolver, use it
        if ( userResolver != null ) {
            resource = userResolver.getResource( path );
        }

        // If resource not found by user resolver
        // fall back to resolving by uri
        if ( resource == null && uriResolver != null ) {
            resource = uriResolver.getResource( path );
        }

        return resource;
    }

    public PageTemplate getPageTemplate( String path )
        throws PageTemplateException, java.net.MalformedURLException
    {
        PageTemplate template = null;

        // If user has supplied resolver, use it
        if ( userResolver != null ) {
            template = userResolver.getPageTemplate( path );
            
            // template inherits user resolver
            template.setResolver( userResolver );
        }

        // If template not found by user resolver
        // fall back to resolving by uri
        if ( template == null && uriResolver != null ) {
            template = uriResolver.getPageTemplate( path );
        }

        return template;
    }
}

