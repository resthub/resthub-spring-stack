package org.resthub.web.tal;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.resthub.web.tal.exception.PageTemplateException;
import org.resthub.web.tal.resolver.Resolver;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public interface ElementProcessor {
	
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
