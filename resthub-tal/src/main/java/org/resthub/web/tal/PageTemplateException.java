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

/**
 * @author <a href="mailto:rossi@webslingerZ.com">Chris Rossi</a>
 * @version $Revision: 1.2 $
 */
public class PageTemplateException extends Exception {
    public PageTemplateException() {
        super();
    }

    public PageTemplateException( String message ) {
        super( message );
    }

    public PageTemplateException( String message, Throwable cause ) {
        super( message, cause );
    }

    public PageTemplateException( Throwable cause ) {
        super( cause );
    }

    /* 1.3 is old
    Exception cause = null;
    String message = "";

    public PageTemplateException( String message ) {
        this.message  = message;
    }

    public PageTemplateException( String message, Exception cause ) {
        this.message = message;
        this.cause = cause;
    }

    public PageTemplateException( Exception cause ) {
        this.cause = cause;
    }

    public String getMessage() {
        if ( cause != null ) {
            return message + ": " +  cause.getMessage();
        }
        return message;
    }

    public void printStackTrace() {
        if ( cause != null ) {
            cause.printStackTrace();
        }
        super.printStackTrace();
    }

    public void printStackTrace( java.io.PrintStream ps ) {
        if ( cause != null ) {
            cause.printStackTrace(ps);
        }
        super.printStackTrace(ps);
    }

    public void printStackTrace( java.io.PrintWriter pw ) {
        if ( cause != null ) {
            cause.printStackTrace(pw);
        }
        super.printStackTrace();
    }
    */
}
