/**
 * @file
 * This file contains implementation of
 * com.irurueta.algebra.RankDeficientMatrixException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 17, 2012
 */
package com.irurueta.algebra;

/**
 * Exception thrown when a matrix does not have full rank
 */
public class RankDeficientMatrixException extends AlgebraException{
    /**
     * Constructor
     */
    public RankDeficientMatrixException(){
        super();
    }

    /**
     * Constructor with String containing message
     * @param message Message indicating the cause of the exception
     */
    public RankDeficientMatrixException(String message){
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message Message describing the cause of the exception
     * @param cause Instance containing the cause of the exception
     */
    public RankDeficientMatrixException(String message, 
            Throwable cause){
        super(message, cause);
    }

    /**
     * Constructor with cause
     * @param cause Instance containing the cause of the exception
     */
    public RankDeficientMatrixException(Throwable cause){
        super(cause);
    }                
}
