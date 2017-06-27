/**
 * @file
 * This file contains implementation of
 * com.irurueta.algebra.NonSymmetricPositiveDefiniteMatrixException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 15, 2012
 */
package com.irurueta.algebra;

/**
 * Exception thrown when requiring symmetric positive definite matrices
 */
public class NonSymmetricPositiveDefiniteMatrixException extends 
        AlgebraException{
    /**
     * Constructor
     */
    public NonSymmetricPositiveDefiniteMatrixException(){
        super();
    }

    /**
     * Constructor with String containing message
     * @param message Message indicating the cause of the exception
     */
    public NonSymmetricPositiveDefiniteMatrixException(String message){
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message Message describing the cause of the exception
     * @param cause Instance containing the cause of the exception
     */
    public NonSymmetricPositiveDefiniteMatrixException(String message, 
            Throwable cause){
        super(message, cause);
    }

    /**
     * Constructor with cause
     * @param cause Instance containing the cause of the exception
     */
    public NonSymmetricPositiveDefiniteMatrixException(Throwable cause){
        super(cause);
    }            
}
