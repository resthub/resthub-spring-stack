/**
 *  Java Page Templates
 *  Copyright (C) 2004 webslingerZ, inc.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.resthub.web.tal;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.resthub.web.tal.exception.PageTemplateException;

/**
 * @author <a href="mailto:rossi@webslingerZ.com">Chris Rossi</a>
 * @version $Revision: 1.4 $
 */
public abstract class Resolver {
    // Map of resources called by this template
    Map templates = new HashMap();
    Map scripts = new HashMap();
    
    public abstract URL getResource( String path ) 
        throws java.net.MalformedURLException;
    
    public PageTemplate getPageTemplate( String path ) 
        throws PageTemplateException, java.net.MalformedURLException
    {
        PageTemplate template = (PageTemplate)templates.get( path );
        if ( template == null ) {
            URL resource = getResource( path );
            if ( resource != null ) {
                template = new PageTemplateImpl( resource );
                templates.put( path, template );
            }
        }
        return template;
    }
    
    public BeanShellScript getBeanShellScript( String path ) 
        throws java.net.MalformedURLException, java.io.IOException
    {
        BeanShellScript script = (BeanShellScript)scripts.get( path );
        if ( script == null ) {
            URL resource = getResource( path );
            if ( resource != null ) {
                script = new BeanShellScript( new InputStreamReader( resource.openStream() ) );
                scripts.put( path, script );
            }
        }
        return script;
    }
}
