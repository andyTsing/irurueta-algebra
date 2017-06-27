/**
 * @file
 * This file contains implementation of
 * com.irurueta.statistics.InvalidCovarianceMatrixException
 */
package com.irurueta.statistics;

/**
 * Exception thrown when providing an invalid covariance matrix.
 * Covariance matrices need to be symmetric and non singular (i.e. symmetric
 * and positive definite).
 */
public class InvalidCovarianceMatrixException extends StatisticsException {
    
    /**
     * Constructor.
     */
    public InvalidCovarianceMatrixException(){
        super();
    }
    
    /**
     * Constructor with String containing message.
     * @param message message indicating the cause of the exception.
     */
    public InvalidCovarianceMatrixException(String message){
        super(message);
    }
    
    /**
     * Constructor with message and cause.
     * @param message message describing the cause of the exception.
     * @param cause instance containing the cause of the exception.
     */
    public InvalidCovarianceMatrixException(String message, Throwable cause){
        super(message, cause);
    }
    
    /**
     * Constructor with cause.
     * @param cause instance containing the cause of the exception.
     */
    public InvalidCovarianceMatrixException(Throwable cause){
        super(cause);
    }
}
