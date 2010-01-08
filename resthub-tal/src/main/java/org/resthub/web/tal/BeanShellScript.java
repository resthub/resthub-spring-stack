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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * @author <a href="mailto:rossi@webslingerZ.com">Chris Rossi</a>
 * @version $Revision: 1.1 $
 */
public class BeanShellScript {
    private String script;
    
    public BeanShellScript( String script ) {
        this.script = script;
    }
    
    private static final int BLOCK_SIZE = 10000;
    public BeanShellScript( Reader reader ) throws IOException {
        List blocks = null;
        int size = 0;
        
        char[] block = new char[ BLOCK_SIZE ];
        int blockSize = 0;
        while( true ) {
            int read = reader.read( block, blockSize, BLOCK_SIZE - blockSize );
            if ( read == -1 ) {
                if ( blocks != null ) {
                    blocks.add( block );
                }
                break;
            }
            size += read;
            blockSize += read;
            if ( blockSize == BLOCK_SIZE ) {
                if ( blocks == null ) {
                    blocks = new LinkedList();
                }
                blocks.add( block );
                block = new char[ BLOCK_SIZE ];
                blockSize = 0;
            }
        }
        
        if ( blocks == null ) {
            this.script = new String( block, 0, blockSize );
        }
        else {
            StringBuffer buffer = new StringBuffer( size );
            for ( Iterator i = blocks.iterator(); i.hasNext(); ) {
                block = (char[])i.next();
                buffer.append( block, 0, Math.min( BLOCK_SIZE, size ) );
                size -= BLOCK_SIZE;
            }
            this.script = buffer.toString();
        }
    }
    
    public String getScript() {
        return script;
    }
    
    public String toString() {
        return script;
    }
}
