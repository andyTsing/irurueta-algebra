/**
 * @file
 * This file contains implementation of
 * com.irurueta.algebra.DecomposerException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 15, 2012
 */
package com.irurueta.algebra;

/**
 * Exception raised when some decomposer algorithm fails
 */
public class DecomposerException extends AlgebraException{
    /**
     * Constructor
     */
    public DecomposerException(){
        super();
    }

    /**
     * Constructor with String containing message
     * @param message Message indicating the cause of the exception
     */
    public DecomposerException(String message){
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message Message describing the cause of the exception
     * @param cause Instance containing the cause of the exception
     */
    public DecomposerException(String message, Throwable cause){
        super(message, cause);
    }

    /**
     * Constructor with cause
     * @param cause Instance containing the cause of the exception
     */
    public DecomposerException(Throwable cause){
        super(cause);
    }        
}
