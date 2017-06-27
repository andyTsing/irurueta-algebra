/**
 * @file
 * This file contains implementation of
 * com.irurueta.algebra.LockedException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 15, 2012
 */
package com.irurueta.algebra;

public class LockedException extends AlgebraException{
    
    /**
     * Constructor
     */
    public LockedException(){
        super();
    }

    /**
     * Constructor with String containing message
     * @param message Message indicating the cause of the exception
     */
    public LockedException(String message){
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message Message describing the cause of the exception
     * @param cause Instance containing the cause of the exception
     */
    public LockedException(String message, Throwable cause){
        super(message, cause);
    }

    /**
     * Constructor with cause
     * @param cause Instance containing the cause of the exception
     */
    public LockedException(Throwable cause){
        super(cause);
    }            
}
