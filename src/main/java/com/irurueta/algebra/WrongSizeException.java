/**
 * @file
 * This file contains implementation of
 * com.irurueta.algebra.WrongSizeException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 15, 2012
 */
package com.irurueta.algebra;

/**
 * Exception thrown when using matrices having wrong size
 */
public class WrongSizeException extends AlgebraException{
    /**
     * Constructor
     */
    public WrongSizeException(){
        super();
    }

    /**
     * Constructor with String containing message
     * @param message Message indicating the cause of the exception
     */
    public WrongSizeException(String message){
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message Message describing the cause of the exception
     * @param cause Instance containing the cause of the exception
     */
    public WrongSizeException(String message, Throwable cause){
        super(message, cause);
    }

    /**
     * Constructor with cause
     * @param cause Instance containing the cause of the exception
     */
    public WrongSizeException(Throwable cause){
        super(cause);
    }            
}
