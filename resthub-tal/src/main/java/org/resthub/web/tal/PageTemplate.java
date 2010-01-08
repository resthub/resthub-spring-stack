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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/**
 * @author <a href="mailto:rossi@webslingerZ.com">Chris Rossi</a>
 * @version $Revision: 1.5 $
 */
public interface PageTemplate {
    static final String TAL_NAMESPACE_URI = "http://xml.zope.org/namespaces/tal";
    static final String METAL_NAMESPACE_URI = "http://xml.zope.org/namespaces/metal";

    void process( ContentHandler contentHandler, LexicalHandler lexicalHandler, Object context, Map dictionary )
        throws SAXException, PageTemplateException, IOException;
    void process( OutputStream output, Object context )
        throws SAXException, PageTemplateException, IOException;
    void process( OutputStream output, Object context, Map dictionary )
        throws SAXException, PageTemplateException, IOException;

    Resolver getResolver();
    void setResolver( Resolver resolver );
    Map getMacros();
    //List getDependencies();
    
    String toLetter( int n );
    String toCapitalLetter( int n );
    String toRoman( int n );
    String toCapitalRoman( int n );
}
