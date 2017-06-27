/**
 * @file
 * This file contains implementation of
 * com.irurueta.algebra.NormType
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 22, 2012
 */
package com.irurueta.algebra;

/**
 * Enumerator defining all possible ways of computing norms for matrices and
 * arrays
 */
public enum NormType {
    /**
     * Defines Frobenius norm type
     */
    FROBENIUS_NORM,
    
    /**
     * Defines one norm, which is the maximum column sum on a matrix.
     */
    ONE_NORM,
    
    /**
     * Defines Infinity norm type, which is the maximum row sum on a matrix
     */
    INFINITY_NORM
}
