/**
 * @file
 * This file contains implementation of
 * com.irurueta.algebra.NotAvailableException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 15, 2012
 */
package com.irurueta.algebra;

/**
 * Exception raised when attempting to retrieve something that is not yet
 * available
 */
public class NotAvailableException extends AlgebraException{
    /**
     * Constructor
     */
    public NotAvailableException(){
        super();
    }

    /**
     * Constructor with String containing message
     * @param message Message indicating the cause of the exception
     */
    public NotAvailableException(String message){
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message Message describing the cause of the exception
     * @param cause Instance containing the cause of the exception
     */
    public NotAvailableException(String message, Throwable cause){
        super(message, cause);
    }

    /**
     * Constructor with cause
     * @param cause Instance containing the cause of the exception
     */
    public NotAvailableException(Throwable cause){
        super(cause);
    }            
}
