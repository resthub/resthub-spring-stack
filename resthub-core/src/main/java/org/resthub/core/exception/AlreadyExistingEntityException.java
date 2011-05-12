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
 * Base class for all custom exception thrown in Resthub 
 * 
 * @author sdeleuze
 */
public class AlreadyExistingEntityException extends ResthubException {

    /**
     * uid used for serialization
     */
    private static final long serialVersionUID = 2010307013874058143L;

    /**
     * Default constructor 
     */
    public AlreadyExistingEntityException() {
        super();
    }

    
    /**
     * Create a new AlreadyExistingEntityException from a message which explain the nature of the Exception
     */
    public AlreadyExistingEntityException(String message) {
        super(message);
    }

    /**
     * Create a new AlreadyExistingEntityException from a message and a base throwable exception 
     */
    public AlreadyExistingEntityException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
