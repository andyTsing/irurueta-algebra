/**
 * @file
 * This file contains implementation of
 * com.irurueta.algebra.NoConvergenceException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 21, 2012
 */
package com.irurueta.algebra;

/**
 * Exception thrown if some decomposer algorithm cannot converge
 */
public class NoConvergenceException extends DecomposerException{
    /**
     * Constructor
     */
    public NoConvergenceException(){
        super();
    }

    /**
     * Constructor with String containing message
     * @param message Message indicating the cause of the exception
     */
    public NoConvergenceException(String message){
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message Message describing the cause of the exception
     * @param cause Instance containing the cause of the exception
     */
    public NoConvergenceException(String message, Throwable cause){
        super(message, cause);
    }

    /**
     * Constructor with cause
     * @param cause Instance containing the cause of the exception
     */
    public NoConvergenceException(Throwable cause){
        super(cause);
    }            
}
