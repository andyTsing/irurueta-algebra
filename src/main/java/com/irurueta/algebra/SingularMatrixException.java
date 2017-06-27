/**
 * @file
 * This file contains implementation of
 * com.irurueta.algebra.SingularMatrixException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 17, 2012
 */
package com.irurueta.algebra;

/**
 * Exception thrown when a singular matrix is used
 */
public class SingularMatrixException extends AlgebraException{
   /**
     * Constructor
     */
    public SingularMatrixException(){
        super();
    }

    /**
     * Constructor with String containing message
     * @param message Message indicating the cause of the exception
     */
    public SingularMatrixException(String message){
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message Message describing the cause of the exception
     * @param cause Instance containing the cause of the exception
     */
    public SingularMatrixException(String message, Throwable cause){
        super(message, cause);
    }

    /**
     * Constructor with cause
     * @param cause Instance containing the cause of the exception
     */
    public SingularMatrixException(Throwable cause){
        super(cause);
    }                    
}
