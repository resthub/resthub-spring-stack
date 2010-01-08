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


import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.junit.Test;

public class PageTemplateTest {

    @Test
    public void testStatements() throws Exception {
        testPageTemplate( "statements", null );
        testPageTemplate( "statements", null );
    }

    @Test
    public void testExpressions() throws Exception {
        Map dictionary = new HashMap();
        dictionary.put( "opinions", "everybodysgotone" );
        dictionary.put( "helper", new TestObject() );
        dictionary.put( "acquaintance", "friend" );

        testPageTemplate( "expressions", dictionary );
        testPageTemplate( "expressions", dictionary );
    }

    @Test
    public void testNamespace() throws Exception {
        testPageTemplate( "namespace", null );
    }

    @Test
    public void testStyle() throws Exception {
        testPageTemplate( "style", null );
    }

    @Test
    public void testMacros() throws Exception {
        testPageTemplate( "macros", null );
        testPageTemplate( "macros", null );
    }

    @Test
    public void testMacros2() throws Exception {
        testPageTemplate( "macros2", null );
        testPageTemplate( "macros2", null );
    }
    
    private void testPageTemplate( String test, Map dictionary ) throws Exception {
        String jpt = test + ".jpt";
        long start = System.currentTimeMillis();
        PageTemplate template = new PageTemplateImpl( getClass().getClassLoader().getResource( jpt ) );
        long elapsed = System.currentTimeMillis() - start;
        System.err.println( test + ": constructed template in " + elapsed + " ms" );

        TestObject testObject = new TestObject();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        start = System.currentTimeMillis();
        if ( dictionary == null ) {
            template.process( buffer, testObject );
        } else {
            template.process( buffer, testObject, dictionary );
        }
        elapsed = System.currentTimeMillis() - start;
        System.err.println( test + ": processed template in " + elapsed + " ms" );

        String html = test + ".html";
        String newHtml = html + ".new";
        byte[] resultBinary = buffer.toByteArray();
        String result = fixCRLF( new String( resultBinary, "UTF-8" ) );
        String expected = fixCRLF( loadTextFile( getClass().getClassLoader().getResourceAsStream( html ) ) );
        if ( ! result.equals( expected ) ) {
            FileOutputStream out = new FileOutputStream( newHtml );
            out.write( resultBinary );
            out.close();
        }
        assertTrue( "unexpected results: see " + newHtml, result.equals( expected ) );
    }

    static final String loadTextFile( InputStream input ) throws Exception {
        InputStreamReader reader = new InputStreamReader( input );
        StringWriter buffer = new StringWriter();
        char cbuf[] = new char[ 1024 ];
        int len = reader.read( cbuf );
        while ( len != -1 ) {
            buffer.write( cbuf, 0, len );
            len = reader.read( cbuf );
        }
        buffer.close();
        reader.close();
        return buffer.toString();
    }

    static final String fixCRLF( String source ) {
        StringBuffer buf = new StringBuffer( source.length() );
        StringTokenizer chunks = new StringTokenizer( source, "\r\n" );
        while ( chunks.hasMoreTokens() ) {
            buf.append( chunks.nextToken() );
            if ( buf.charAt( buf.length() - 1 ) != '\n' ) {
                buf.append( '\n' );
            }
        }
        return buf.toString();
    }

    public class TestObjectSuperClass {
        public int getNumberTwo() {
            return 2;
        }
    }
    
    public class TestObject extends TestObjectSuperClass {
        List people;
        String[] animals = {
            "horse", 
            "dog",
            "cat",
            "pig",
            "crocodile"
        };
        
        int[][] table = {
            { 1, 2, 3, 4 },
            { 5, 6, 7, 8 },
            { 9, 10 }
        };
        
        TestObject() {
            people = new ArrayList(5);
            people.add( "Chris" );
            people.add( "Karen" );
            people.add( "Mike" );
            people.add( "Marsha" );
            people.add( "Christiane" );
        }

        public List getPeople() {
            return people;
        }

        public String[] getAnimals() {
            return animals;
        }

        public int[][] getTable() {
            return table;
        }
        
        public String getFavoriteColor() {
            return "red";
        }

        public boolean isGoodLooking() {
            return true;
        }

        public boolean isDumb() {
            return false;
        }

        public int add( int x, int y ) {
            return x + y;
        }

        public int multiply( int x, int y ) {
            return x * y;
        }

        private final TestObject2 friend = new TestObject2();
        public TestObject2 getFriend() {
            return friend;
        }

        public TestObject2 getEnemy() {
            return null;
        }

        public String getDiatribe() {
            return "<b>The cabinet</b> has <i>usurped</i> the authority of the <h3>president</h3><p>";
        }

        public String getDiatribe2() {
            return "<b>The cabinet</b> has <i>usurped</i> the authority of the <h3>president</h3><p/>";
        }
        
        public HTMLFragment getDiatribe3() throws Exception {
            return new HTMLFragment( 
                "<b>The cabinet</b> has <i>usurped</i> the authority of the <h3>president</h3><p>" 
            );
        }
        
        public List getNumbers() {
            List numbers = new ArrayList(100);
            for ( int i = 100; i > 0; i-- ) {
                numbers.add( new Integer( i ) );
            }
            return numbers;
        }
        
        public Date getBirthday() {
            Calendar calendar = Calendar.getInstance();
            calendar.set( 1975, 6, 7, 7, 57, 23 );
            return calendar.getTime();
        }
    }
    
    public class TestObject2 {
        Map map = new HashMap();

        TestObject2() {
            map.put( "friend", "kevin" );
            map.put( "enemy", "mc2" );
            map.put( "hello", new Long(99l) );
        }

        public int getNumber() {
            return 9;
        }

        public long getZero() {
            return 0l;
        }

        public Map getMap() {
            return map;
        }

        public String toString() {
            return "Albert";
        }
    }

    public static int subtract( int x, int y ) {
        return x - y;
    }
}

