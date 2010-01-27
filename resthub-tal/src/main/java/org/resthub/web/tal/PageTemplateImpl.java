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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.cyberneko.html.parsers.SAXParser;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.resthub.web.tal.exception.ExpressionSyntaxException;
import org.resthub.web.tal.exception.NoSuchPathException;
import org.resthub.web.tal.exception.PageTemplateException;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

import bsh.Interpreter;

/**
 * @author <a href="mailto:rossi@webslingerZ.com">Chris Rossi</a>
 * @version $Revision: 1.15 $
 */
public class PageTemplateImpl implements PageTemplate {
    private URI uri;
    private Document template;
    //private String talNamespacePrefix = "tal";
    //private String metalNamespacePrefix = "metal";
    private Resolver userResolver = null;

    // Map of macros contained in this template
    Map macros = null;

    private static SAXReader htmlReader = null;
    static final SAXReader getHTMLReader() throws Exception {
        if ( htmlReader == null ) {
            htmlReader = new SAXReader();
            SAXParser parser = new SAXParser();
            parser.setProperty( "http://cyberneko.org/html/properties/names/elems", "match" );
            parser.setProperty( "http://cyberneko.org/html/properties/names/attrs", "no-change" );
            parser.setProperty( "http://cyberneko.org/html/properties/default-encoding", "UTF-8" );
            htmlReader.setXMLReader( parser );
        }
        return htmlReader;
    }

    private static SAXReader xmlReader = null;
    static final SAXReader getXMLReader() throws Exception {
        if ( xmlReader == null ) {
            xmlReader = new SAXReader();
        }
        return xmlReader;
    }

    /*
    public PageTemplateImpl( String template ) throws PageTemplateException {
        //this( new StringReader( template ), null );
        try {
            this( new ByteArrayInputStream( template.getBytes( "UTF-8" ) ), null );
        } catch( java.io.UnsupportedEncodingException e ) {
            throw new PageTemplateException(e);
        }
    }

    public PageTemplateImpl( String template, Resolver resolver ) throws PageTemplateException {
        //this( new StringReader( template ), resolver );
        try {
            this( new ByteArrayInputStream( template.getBytes( "UTF-8" ) ), resolver );
        } catch( java.io.UnsupportedEncodingException e ) {
            throw new PageTemplateException(e);
        }
    }
    */

    /**
     * Tidy doesn't support Reader
     *
    public PageTemplateImpl( Reader input ) throws PageTemplateException {
        this( input, null );
    }

    public PageTemplateImpl( Reader input, Resolver resolver ) throws PageTemplateException {
        try {
            this.uri = null;
            this.userResolver = resolver;
            
            Tidy tidy = getTidy();
            DOMReader reader = new DOMReader();
            template = reader.read( tidy.parseDOM( input, null) );       
        }
        catch( Exception e ) {
            throw new PageTemplateException(e);
        }
    }
    */

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

    public Resolver getResolver() {
        return this.userResolver;
    }
    
    public void setResolver( Resolver resolver ) {
        this.userResolver = resolver;
    }

    public void process( OutputStream output, Object context )
        throws SAXException, PageTemplateException, IOException
    {
        process( output, context, null );
    }

    static final SAXTransformerFactory factory = (SAXTransformerFactory)TransformerFactory.newInstance();
    public void process( OutputStream output, Object context, Map dictionary )
        throws SAXException, PageTemplateException, IOException
    {
        try {
            TransformerHandler handler = factory.newTransformerHandler();
            Transformer transformer = handler.getTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "html");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            handler.setResult( new StreamResult( output ) );
            process( handler, handler, context, dictionary );
        } catch( TransformerConfigurationException e ) {
            throw new PageTemplateException(e);
        }
    }

    public void process( ContentHandler contentHandler, 
                         LexicalHandler lexicalHandler, 
                         Object context, 
                         Map dictionary )
        throws SAXException, PageTemplateException, IOException 
    {
        try {
            // Initialize the bean shell
            Interpreter beanShell = new Interpreter();
            if ( dictionary != null ) {
                for ( Iterator i = dictionary.entrySet().iterator(); i.hasNext(); ) {
                    Map.Entry entry = (Map.Entry)i.next();
                    beanShell.set( (String)entry.getKey(), entry.getValue() );
                }
            }
            beanShell.set( "here", context );
            beanShell.set( "template", this );
            beanShell.set( "resolver", new DefaultResolver() );
            beanShell.set( "bool", new BoolHelper() );
            beanShell.set( "math", new MathHelper() );
            beanShell.set( "date", new DateHelper() );
            Element root = template.getRootElement();
            contentHandler.startDocument();
            processElement( root, contentHandler, lexicalHandler, beanShell, new Stack() );
            contentHandler.endDocument();
        } catch( bsh.EvalError e ) {
            throw new PageTemplateException(e);
        }
    }

    private Map namespaces = new TreeMap();
    private String getNamespaceURIFromPrefix( String prefix ) {
        String uri = (String)namespaces.get( prefix );
        return uri == null ? "" : uri;
    }

    private void processElement( Element element, 
                                 ContentHandler contentHandler, 
                                 LexicalHandler lexicalHandler, 
                                 Interpreter beanShell, 
                                 Stack slotStack )
        throws SAXException, PageTemplateException, IOException
    {   
        // Get attributes
        Expressions expressions = new Expressions();
        AttributesImpl attributes = getAttributes( element, expressions );
        
        // Process instructions

        // use macro
        if ( expressions.useMacro != null ) {
            processMacro( expressions.useMacro, element, contentHandler, lexicalHandler, beanShell, slotStack );
            return;
        }

        // fill slot
        if ( expressions.defineSlot != null ) {
            //System.err.println( "fill slot: " + expressions.defineSlot );
            if ( ! slotStack.isEmpty() ) {
                Map slots = (Map)slotStack.pop();
                Slot slot = (Slot)slots.get( expressions.defineSlot );
                //System.err.println( "slot: " + slot );
                if ( slot != null ) {
                    slot.process( contentHandler, lexicalHandler, beanShell, slotStack );
                    slotStack.push( slots );
                    return;
                }
                // else { use content in macro }
                slotStack.push( slots );
            }
            else {
                throw new PageTemplateException( "slot definition not allowed outside of macro" );
            }
        }

        // define
        if ( expressions.define != null ) {
            processDefine( expressions.define, beanShell );
        }

        // condition
        if ( expressions.condition != null &&
             ! Expression.evaluateBoolean( expressions.condition, beanShell ) ) {
            // Skip this element (and children)
            return;
        }

        // repeat
        Loop loop = new Loop( expressions.repeat, beanShell );
        while( loop.repeat( beanShell ) ) {
            // content or replace
            Object jptContent = null;
            if ( expressions.content != null ) {
                jptContent = processContent( expressions.content, beanShell );
            }
            
            // attributes
            if ( expressions.attributes != null ) {
                processAttributes( attributes, expressions.attributes, beanShell );
            }
            
            // omit-tag
            boolean jptOmitTag = false;
            if ( expressions.omitTag != null ) {
                if ( expressions.omitTag.equals( "" ) ) {
                    jptOmitTag = true;
                }
                else {
                    jptOmitTag = Expression.evaluateBoolean( expressions.omitTag, beanShell );
                }
            }
            
            // Declare element
            Namespace namespace = element.getNamespace();
            if ( ! jptOmitTag ) {
                contentHandler.startElement( namespace.getURI(), element.getName(), element.getQualifiedName(), attributes );
            }
            
            // Process content
            if ( jptContent != null ) {
                // Content for this element has been generated dynamically
                if ( jptContent instanceof HTMLFragment ) {
                    HTMLFragment html = (HTMLFragment)jptContent;
                    html.toXhtml( contentHandler, lexicalHandler );
                } 
                
                // plain text
                else {
                    char[] text = ((String)jptContent).toCharArray();
                    contentHandler.characters( text, 0, text.length );
                }
            }
            else {
                defaultContent( element, contentHandler, lexicalHandler, beanShell, slotStack );
            }
   
            // End element
            if ( ! jptOmitTag ) {
                contentHandler.endElement( namespace.getURI(), element.getName(), element.getQualifiedName() );
            }
        }
    }

    private void defaultContent( Element element, 
                                 ContentHandler contentHandler, 
                                 LexicalHandler lexicalHandler, 
                                 Interpreter beanShell, 
                                 Stack slotStack )
        throws SAXException, PageTemplateException, IOException
    {   
        // Use default template content
        for ( Iterator i = element.nodeIterator(); i.hasNext(); ) {
            Node node = (Node)i.next();
            switch( node.getNodeType() ) {
            case Node.ELEMENT_NODE:
                processElement( (Element)node, contentHandler, lexicalHandler, beanShell, slotStack );
                break;
                
            case Node.TEXT_NODE:
                char[] text = node.getText().toCharArray();
                contentHandler.characters( text, 0, text.length );
                break;
                
            case Node.COMMENT_NODE:
                char[] comment = node.getText().toCharArray();
                lexicalHandler.comment( comment, 0, comment.length );
                break;
                
            case Node.CDATA_SECTION_NODE:
                lexicalHandler.startCDATA();
                char[] cdata = node.getText().toCharArray();
                contentHandler.characters( cdata, 0, cdata.length );
                lexicalHandler.endCDATA();
                break;
                
            case Node.NAMESPACE_NODE:
                Namespace declared = (Namespace)node;
                //System.err.println( "Declared namespace: " + declared.getPrefix() + ":" + declared.getURI() );
                namespaces.put( declared.getPrefix(), declared.getURI() );
                //if ( declared.getURI().equals( TAL_NAMESPACE_URI ) ) {
                //    this.talNamespacePrefix = declared.getPrefix();
                //} 
                //else if (declared.getURI().equals( METAL_NAMESPACE_URI ) ) {
                //    this.metalNamespacePrefix = declared.getPrefix();
                //}
                break;
                
            case Node.ATTRIBUTE_NODE:
                // Already handled
                break;
                
            case Node.DOCUMENT_TYPE_NODE:
            case Node.ENTITY_REFERENCE_NODE:
            case Node.PROCESSING_INSTRUCTION_NODE:
            default:
                //System.err.println( "WARNING: Node type not supported: " + node.getNodeTypeName() );       
            }
        }
    }

    AttributesImpl getAttributes( Element element, Expressions expressions ) 
        throws PageTemplateException
    {
        AttributesImpl attributes = new AttributesImpl();
        for ( Iterator i = element.attributeIterator(); i.hasNext(); ) {
            Attribute attribute = (Attribute)i.next();
            Namespace namespace = attribute.getNamespace();
            //String prefix = namespace.getPrefix();
            //System.err.println( "attribute: name=" + attribute.getName() + "\t" +
            //                    "qualified name=" + attribute.getQualifiedName() + "\t" +
            //                    "ns prefix=" + namespace.getPrefix() + "\t" +
            //                    "ns uri=" + namespace.getURI() );
            //String qualifiedName = attribute.getName();
            //String name = qualifiedName;
            //if ( qualifiedName.startsWith( prefix + ":" ) ) {
            //    name = qualifiedName.substring( prefix.length() + 1 );
            //}
            String name = attribute.getName();
            
            // Handle JPT attributes
            //if ( prefix.equals( talNamespacePrefix ) ) {
            if ( TAL_NAMESPACE_URI.equals( namespace.getURI() ) ) {
                // tal:define
                if ( name.equals( "define" ) ) {
                    expressions.define = attribute.getValue();
                }

                // tal:condition
                else if ( name.equals( "condition" ) ) {
                    expressions.condition = attribute.getValue();
                }

                // tal:repeat
                else if ( name.equals( "repeat" ) ) {
                    expressions.repeat = attribute.getValue();
                }

                // tal:content
                else if ( name.equals( "content" ) ) {
                    expressions.content = attribute.getValue();
                }

                // tal:replace
                else if ( name.equals( "replace" ) ) {
                    if ( expressions.omitTag == null ) {
                        expressions.omitTag = "";
                    }
                    expressions.content = attribute.getValue();
                }

                // tal:attributes
                else if ( name.equals( "attributes" ) ) {
                    expressions.attributes = attribute.getValue();
                }

                // tal:omit-tag
                else if ( name.equals( "omit-tag" ) ) {
                    expressions.omitTag = attribute.getValue();
                }

                // error
                else {
                    throw new PageTemplateException( "unknown tal attribute: " + name );
                }
            }
            //else if ( prefix.equals( metalNamespacePrefix ) ) 
            else if ( METAL_NAMESPACE_URI.equals( namespace.getURI() ) ) {
                // metal:use-macro
                if ( name.equals( "use-macro" ) ) {
                    expressions.useMacro = attribute.getValue();
                }
                
                // metal:define-slot
                else if ( name.equals( "define-slot" ) ) {
                    expressions.defineSlot = attribute.getValue();
                }

                // metal:define-macro
                // metal:fill-slot
                else if ( name.equals( "define-macro" ) ||
                          name.equals( "fill-slot" ) ) {
                    // these are ignored here, as they don't affect processing of current
                    // template, but are called from other templates
                }

                // error
                else {
                    throw new PageTemplateException( "unknown metal attribute: " + name );
                }
            }

            // Pass on all other attributes
            else {
                //String qualifiedName = namespace.getPrefix() + ":" + name;
                attributes.addAttribute( namespace.getURI(), name, attribute.getQualifiedName(), 
                                         "CDATA", attribute.getValue() );
                //attributes.addAttribute( getNamespaceURIFromPrefix(prefix), name, qualifiedName, "CDATA", attribute.getValue() );
            }
        }
        return attributes;
    }

    private Object processContent( String expression, Interpreter beanShell )
        throws PageTemplateException
    {
        // Structured text, preserve xml structure
        if ( expression.startsWith( "structure " ) ) {
            expression = expression.substring( "structure ".length() );
            Object content = Expression.evaluate( expression, beanShell );
            if ( ! ( content instanceof HTMLFragment ) ) {
                content = new HTMLFragment( String.valueOf( content ) );
            }
            return content;
        }
        else if ( expression.startsWith( "text " ) ) {
            expression = expression.substring( "text ".length() );
        }
        return String.valueOf( Expression.evaluate( expression, beanShell ) );
    }

    private void processDefine( String expression, Interpreter beanShell )
        throws PageTemplateException
    {
        try {
            ExpressionTokenizer tokens = new ExpressionTokenizer( expression, ';', true );
            while( tokens.hasMoreTokens() ) {
                String variable = tokens.nextToken().trim();
                int space = variable.indexOf( ' ' );
                if ( space == -1 ) {
                    throw new ExpressionSyntaxException( "bad variable definition: " + variable );
                }
                String name = variable.substring( 0, space );
                String valueExpression = variable.substring( space + 1 ).trim();
                Object value = Expression.evaluate( valueExpression, beanShell );
                beanShell.set( name, value );
            }
        } catch( bsh.EvalError e ) {
            throw new PageTemplateException(e);
        }
    }

    private void processAttributes( AttributesImpl attributes, String expression, Interpreter beanShell )
        throws PageTemplateException
    {
        ExpressionTokenizer tokens = new ExpressionTokenizer( expression, ';', true );
        while( tokens.hasMoreTokens() ) {
            String attribute = tokens.nextToken().trim();
            int space = attribute.indexOf( ' ' );
            if ( space == -1 ) {
                throw new ExpressionSyntaxException( "bad attributes expression: " + attribute );
            }
            String qualifiedName = attribute.substring( 0, space );
            String valueExpression = attribute.substring( space + 1 ).trim();
            Object value = Expression.evaluate( valueExpression, beanShell );
            //System.err.println( "attribute:\n" + qualifiedName + "\n" + valueExpression + "\n" + value );
            removeAttribute( attributes, qualifiedName );
            if ( value != null ) {
                String name = "";
                String uri = "";
                int colon = qualifiedName.indexOf( ":" );
                if ( colon != -1 ) {
                    String prefix = qualifiedName.substring( 0, colon );
                    name = qualifiedName.substring( colon + 1 );
                    uri = getNamespaceURIFromPrefix( prefix );
                }
                attributes.addAttribute( uri, name, qualifiedName, "CDATA", String.valueOf( value ) );
            }
        }
    }

    private void removeAttribute( AttributesImpl attributes, String qualifiedName ) {
        int index = attributes.getIndex( qualifiedName );
        if ( index != -1 ) {
            attributes.removeAttribute( index );
        }
    }

    private void processMacro( String expression, 
                               Element element, 
                               ContentHandler contentHandler,
                               LexicalHandler lexicalHandler,
                               Interpreter beanShell,
                               Stack slotStack )
        throws SAXException, PageTemplateException, IOException
    {
        Object object = Expression.evaluate( expression, beanShell );
        if ( object == null ) {
            throw new NoSuchPathException( "could not find macro: " + expression );
        }

        if ( object instanceof Macro ) {
            // Find slots to fill inside this macro call
            Map slots = new HashMap();
            findSlots( element, slots );

            // Slots filled in later templates (processed earlier) 
            // Take precedence over slots filled in intermediate
            // templates.
            if ( ! slotStack.isEmpty() ) {
                Map laterSlots = (Map)slotStack.peek();
                slots.putAll( laterSlots );
            }
            slotStack.push( slots );
            //System.err.println( "found slots: " + slots.keySet() );
            
            // Call macro
            Macro macro = (Macro)object;
            macro.process( contentHandler, lexicalHandler, beanShell, slotStack );
        }
        else {
            throw new PageTemplateException( "expression '" + expression + "' does not evaluate to macro: " + 
                                             object.getClass().getName() );
        }
    }

    /**
     * With all of our namespace woes, getting an XPath expression
     * to work has proven futile, so we'll recurse through the tree
     * ourselves to find what we need.
     */
    private void findSlots( Element element, Map slots ) {
        //System.err.println( "element: " + element.getName() );
        for ( Iterator i = element.attributes().iterator(); i.hasNext(); ) {
            Attribute attribute = (Attribute)i.next();
            //System.err.println( "\t" + attribute.getName() + "\t" + attribute.getQualifiedName() );
        }
        
        // Look for our attribute
        //String qualifiedAttributeName = this.metalNamespacePrefix + ":fill-slot";
        //String name = element.attributeValue( qualifiedAttributeName );
        String name = element.attributeValue( "fill-slot" );
        if ( name != null ) {
            slots.put( name, new SlotImpl( element ) );
        }

        // Recurse into child elements
        for ( Iterator i = element.elementIterator(); i.hasNext(); ) {
            findSlots( (Element)i.next(), slots );
        }
    }
    
    /**
     * With all of our namespace woes, getting an XPath expression
     * to work has proven futile, so we'll recurse through the tree
     * ourselves to find what we need.
     */
    private void findMacros( Element element, Map macros )
    {
        // Process any declared namespaces
        for ( Iterator i = element.declaredNamespaces().iterator(); i.hasNext(); ) {
            Namespace namespace = (Namespace)i.next();
            namespaces.put( namespace.getPrefix(), namespace.getURI() );
            //if ( namespace.getURI().equals( TAL_NAMESPACE_URI ) ) {
            //    this.talNamespacePrefix = namespace.getPrefix();
            //}
            //else if ( namespace.getURI().equals( METAL_NAMESPACE_URI ) ) {
            //    this.metalNamespacePrefix = namespace.getPrefix();
            //}
        }
        
        // Look for our attribute
        //String qualifiedAttributeName = this.metalNamespacePrefix + ":define-macro";
        //String name = element.attributeValue( qualifiedAttributeName );
        String name = element.attributeValue( "define-macro" );
        //if ( name == null ) {
        //    name = element.attributeValue
        //        ( new QName( "define-macro", new Namespace( metalNamespacePrefix, METAL_NAMESPACE_URI ) ) );
        //}
        if ( name != null ) {
            macros.put( name, new MacroImpl( element ) );
        }

        // Recurse into child elements
        for ( Iterator i = element.elementIterator(); i.hasNext(); ) {
            findMacros( (Element)i.next(), macros );
        }
    }

    public String toLetter( int n ) {
        return Loop.formatLetter( n );
    }

    public String toCapitalLetter( int n ) {
        return Loop.formatCapitalLetter( n );
    }

    public String toRoman( int n ) {
        return Loop.formatRoman( n );
    }

    public String toCapitalRoman( int n ) {
        return Loop.formatCapitalRoman( n );
    }

    /*
    public List getDependencies() {
        Set templatePaths = new HashSet();
        List nodes = template.selectNodes( "//@*[contains[.,'resolver/getPageTemplate']" );
        for ( Iterator i = nodes.iterator(); i.hasNext(); ) {
            String att = ((Attribute)i.next()).getValue();
            int index = att.indexOf( "resolver/getPageTemplate(" );
            while( index != -1 ) {
      */
          
    public Map getMacros() {
        if ( this.macros == null ) {
            this.macros = new HashMap();
            findMacros( template.getRootElement(), this.macros );
        }
        return this.macros;
    }

    class DefaultResolver extends Resolver {
        URIResolver uriResolver;

        DefaultResolver() {
            if ( uri != null ) {
                uriResolver = new URIResolver( uri );
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

    class MacroImpl implements Macro {
        Element element;

        MacroImpl( Element element ) {
            this.element = element;
        }

        public void process( ContentHandler contentHandler, 
                             LexicalHandler lexicalHandler, 
                             Interpreter beanShell,
                             Stack slotStack )
            throws SAXException, PageTemplateException, IOException
        {
            processElement( element, contentHandler, lexicalHandler, beanShell, slotStack );
        }
    }

    class SlotImpl implements Slot {
        Element element;

        SlotImpl( Element element ) {
            this.element = element;
        }

        public void process( ContentHandler contentHandler, 
                             LexicalHandler lexicalHandler, 
                             Interpreter beanShell,
                             Stack slotStack )
            throws SAXException, PageTemplateException, IOException
        {
            processElement( element, contentHandler, lexicalHandler, beanShell, slotStack );
        }
    }
}

class Expressions {
    String define = null;
    String condition = null;
    String repeat = null;
    String content = null;
    String attributes = null;
    String omitTag = null;
    String useMacro = null;
    String defineSlot = null;
}
    

class BitBucket extends OutputStream {
    static java.io.PrintWriter getBitBucketPrintWriter() {
        return new java.io.PrintWriter( new java.io.OutputStreamWriter( new BitBucket() ) );
    }

    public void write( int b ) {
        // off you go to bit heaven
    }
}

class BoolHelper {
    public static boolean and( boolean a, boolean b ) {
        return a && b;
    }

    public static boolean or( boolean a, boolean b ) {
        return a || b;
    }

    public static Object cond( boolean b, Object x, Object y ) {
        return b ? x : y;
    }
}

class MathHelper {
    public final static int add( int x, int y ) {
        return x + y;
    }

    public final static int sub( int x, int y ) {
        return x - y;
    }

    public final static int mult( int x, int y ) {
        return x * y;
    }

    public final static int div( int x, int y ) {
        return x / y;
    }

    public final static int mod( int x, int y ) {
        return x % y;
    }
}

class DateHelper {
    static final Map dateFormats = new TreeMap();
    
    public static final String format( String format, Date date ) {
        SimpleDateFormat dateFormat = (SimpleDateFormat)dateFormats.get( format );
        if ( dateFormat == null ) {
            dateFormat = new SimpleDateFormat( format );
            dateFormats.put( format, dateFormat );
        }
        return dateFormat.format( date );
    }
}
