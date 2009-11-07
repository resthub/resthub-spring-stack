/**
 *  This file is part of Resthub.
 *
 *  Resthub is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *   Resthub is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Resthub.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.resthub.core.exception;

/**
 * InvalidTypeException should occur when you try to persist Object in the JCR in a node that have
 * some constraints of type
 * @author Bouiaw
 */
public class InvalidTypeException extends ResthubException {

    /**
     * uid used for serialization
     */
	private static final long serialVersionUID = 5326639689232818051L;

    /**
     * Default constructor 
     */
    public InvalidTypeException() {
        super();
    }

    /**
     * Create a new InvalidTypeException from a message
     */
    public InvalidTypeException(String message) {
        super(message);
    }

    /**
     * Create a new InvalidTypeException from a message and a base throwable exception
     */
    public InvalidTypeException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
}
