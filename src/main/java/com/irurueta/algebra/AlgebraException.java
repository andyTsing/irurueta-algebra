/**
 * @file
 * This file contains implementation of
 * com.irurueta.algebra.AlgebraException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 15, 2012
 */
package com.irurueta.algebra;

/**
 * Base exception for all exception in the com.irurueta.algebra package
 */
public class AlgebraException extends Exception{
    
    /**
     * Constructor
     */
    public AlgebraException(){
        super();
    }

    /**
     * Constructor with String containing message
     * @param message Message indicating the cause of the exception
     */
    public AlgebraException(String message){
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message Message describing the cause of the exception
     * @param cause Instance containing the cause of the exception
     */
    public AlgebraException(String message, Throwable cause){
        super(message, cause);
    }

    /**
     * Constructor with cause
     * @param cause Instance containing the cause of the exception
     */
    public AlgebraException(Throwable cause){
        super(cause);
    }        
}
