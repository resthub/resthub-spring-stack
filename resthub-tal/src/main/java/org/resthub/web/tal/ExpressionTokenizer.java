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

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * More or less functions like the standard StringTokenizer
 * except that delimiters which are buried inside parentheses
 * or single quotes are skipped.
 *
 * @author <a href="mailto:rossi@webslingerZ.com">Chris Rossi</a>
 * @version $Revision: 1.2 $
 */
class ExpressionTokenizer {
    String expression;
    Iterator iterator;
    int currIndex = 0;
    int delimiterCount = 0;
    
    ExpressionTokenizer( String expression, char delimiter ) 
        throws PageTemplateException
    {
        this( expression, delimiter, false );
    }
    
    ExpressionTokenizer( String expression, char delimiter, boolean escape ) 
        throws PageTemplateException
    {
        // Go ahead and find delimiters, if any, at construction time
        List delimiters = new ArrayList( 10 );
        
        int parenLevel = 0;
        boolean inQuote = false;
        
        // Scan for delimiters
        int length = expression.length();
        for ( int i = 0; i < length; i++ ) {
            char ch = expression.charAt(i);
            
            if ( ch == delimiter ) {
                // If delimiter is not buried in parentheses or a quote
                if ( parenLevel == 0 && ! inQuote ) {
                    char nextCh = ( i + 1 < length ) ? expression.charAt( i + 1 ) : (char)0;
                    
                    // And if delimiter is not escaped
                    if ( ! ( escape && nextCh == delimiter ) ) {
                        delimiterCount++;
                        delimiters.add( new Integer(i) );
                    }
                    else {
                        // Somewhat inefficient way to pare the
                        // escaped delimiter down to a single
                        // character without breaking our stride
                        expression = expression.substring( 0, i + 1 ) +
                            expression.substring( i + 2 );
                        length--;
                    }
                }
            }
            
            // increment parenthesis level
            else if ( ch == '(' ) {
                parenLevel++;
            }
            
            // decrement parenthesis level
            else if ( ch == ')' ) {
                parenLevel--;
                // If unmatched right parenthesis
                if ( parenLevel < 0 ) {
                    throw new ExpressionSyntaxException
                        ( "syntax error: unmatched right parenthesis: " + expression );
                }
            }
            
            // start or end quote
            else if ( ch == '\'' ) {
                inQuote = ! inQuote;
            }
        }
        
        // If unmatched left parenthesis
        if ( parenLevel > 0 ) {
            throw new ExpressionSyntaxException
                ( "syntax error: unmatched left parenthesis: " + expression );
        }
        
        // If runaway quote
        if ( inQuote ) {
            throw new ExpressionSyntaxException
                ( "syntax error: runaway quotation: " + expression );
        }
        
        this.expression = expression;
        this.iterator = delimiters.iterator();
    }
    
    boolean hasMoreTokens() {
        return currIndex < expression.length();
    }
    
    String nextToken() {
        if ( iterator.hasNext() ) {
            int delim = ((Integer)iterator.next()).intValue();
            String token = expression.substring( currIndex, delim );
            currIndex = delim + 1;
            delimiterCount--;
            return token;
        }
        else {
            String token = expression.substring( currIndex );
            currIndex = expression.length();
            return token;
        }
    }
    
    int countTokens() {
        if ( hasMoreTokens() ) {
            return delimiterCount + 1;
        }
        return 0;
    }
}
