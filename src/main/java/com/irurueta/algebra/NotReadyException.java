/**
 * @file
 * This file contains implementation of
 * com.irurueta.algebra.NotReadyException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 15, 2012
 */
package com.irurueta.algebra;

/**
 * Exception raised when attempting to do an action when something is not ready
 */
public class NotReadyException extends AlgebraException{
    
    /**
     * Constructor
     */
    public NotReadyException(){
        super();
    }

    /**
     * Constructor with String containing message
     * @param message Message indicating the cause of the exception
     */
    public NotReadyException(String message){
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message Message describing the cause of the exception
     * @param cause Instance containing the cause of the exception
     */
    public NotReadyException(String message, Throwable cause){
        super(message, cause);
    }

    /**
     * Constructor with cause
     * @param cause Instance containing the cause of the exception
     */
    public NotReadyException(Throwable cause){
        super(cause);
    }                
}
