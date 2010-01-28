package org.resthub.web.tal.macro;

import java.io.IOException;
import java.util.Stack;

import org.dom4j.Element;
import org.resthub.web.tal.ElementProcessorImpl;
import org.resthub.web.tal.exception.PageTemplateException;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import bsh.Interpreter;

public class MacroImpl extends ElementProcessorImpl implements Macro {
    private Element element;

    public MacroImpl( Element element ) {
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

