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
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXWriter;
import org.resthub.web.tal.exception.PageTemplateException;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/**
 * @author <a href="mailto:rossi@webslingerZ.com">Chris Rossi</a>
 * @version $Revision: 1.1 $
 */
public class HTMLFragment implements java.io.Serializable {
    private String html;
    private transient Element dom = null;
    
    private HTMLFragment() {}
    
    public HTMLFragment( String html) throws PageTemplateException {
        this.html = html;
        parseFragment();
    }
    
    public String getHtml() {
        return html;
    }
    
    public void setHtml( String html ) {
        this.html = html;
    }
    
    public String getXhtml() throws PageTemplateException {
        try {
            StringWriter buffer = new StringWriter();
            toXhtml( buffer );
            buffer.close();
            return buffer.toString();
        } catch( IOException e ) {
            throw new RuntimeException(e);
        }
    }
    
    public void toXhtml( ContentHandler contentHandler, LexicalHandler lexicalHandler ) 
        throws PageTemplateException, SAXException
    {
        if ( dom == null ) {
            parseFragment();
        }
        SAXWriter writer = new SAXWriter( contentHandler, lexicalHandler );
        for ( Iterator i = dom.nodeIterator(); i.hasNext(); ) {
            Node node = (Node)i.next();
            writer.write( node );
        }
    }
    
    public void toXhtml( Writer writer )
        throws PageTemplateException, IOException
    {
        if ( dom == null ) {
            parseFragment();
        }
        for ( Iterator i = dom.nodeIterator(); i.hasNext(); ) {
            Node node = (Node)i.next();
            node.write( writer );
        }
    }
    
    private void parseFragment() throws PageTemplateException {
        try {
            StringBuffer fragment = new StringBuffer( html.length() + 26 );
            fragment.append( "<html><body>" );
            fragment.append( html );
            fragment.append( "</body></html>" );
            
            Reader input = new StringReader( fragment.toString() );
            SAXReader reader = PageTemplateImpl.getXMLReader();
            try {
                dom = reader.read( input ).getRootElement().element( "body" );
            } catch( DocumentException e ) {
                try {
                    reader = PageTemplateImpl.getHTMLReader();
                    input.close();
                    input = new StringReader( fragment.toString() );
                    dom = reader.read( input ).getRootElement().element( "body" );
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
    
    public String toString() {
        return getHtml();
    }
}
