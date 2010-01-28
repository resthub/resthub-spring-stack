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

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.resthub.web.tal.exception.PageTemplateException;
import org.resthub.web.tal.resolver.Resolver;

/**
 * @author <a href="mailto:rossi@webslingerZ.com">Chris Rossi</a>
 * @version $Revision: 1.15 $
 */
public class PageTemplateImpl extends ElementProcessorImpl implements PageTemplate {

    public PageTemplateImpl( InputStream input ) throws PageTemplateException {
        this( input, null );
    }

    public PageTemplateImpl( InputStream input, Resolver resolver ) throws PageTemplateException {
        try {
            this.uri = null;
            this.userResolver = resolver;
            
            SAXReader reader = getXMLReader();
            try {
                template = reader.read( input );
            } catch( DocumentException e ) {
                try {
                    reader = getHTMLReader();
                    template = reader.read( input );
                } catch( NoClassDefFoundError ee ) {
                    // Allow user to omit nekohtml package
                    // to disable html parsing
                    //System.err.println( "Warning: no nekohtml" );
                    //ee.printStackTrace();
                    throw e;
                }
            }
        }
        catch( Exception e ) {
            throw new PageTemplateException(e);
        }
    }

    public PageTemplateImpl( URL url ) throws PageTemplateException {
        try {
            this.uri = new URI( url.toString() );
            SAXReader reader = getXMLReader();
            try {
                template = reader.read( url );
            } catch( DocumentException e ) {
                try {
                    reader = getHTMLReader();
                    template = reader.read( url );
                } catch( NoClassDefFoundError ee ) {
                    // Allow user to omit nekohtml package
                    // to disable html parsing
                    //System.err.println( "Warning: no nekohtml" );
                    //ee.printStackTrace();
                    throw e;
                }
            }
        }
        catch( RuntimeException e ) {
            throw e;
        }
        catch( Exception e ) {
            throw new PageTemplateException(e);
        }
    }

}


    






