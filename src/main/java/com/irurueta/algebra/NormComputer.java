/**
 * @file
 * This file contains definition of
 * com.irurueta.algebra.NormComputer
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 22, 2012
 */
package com.irurueta.algebra;

/**
 * Class in charge of computing norms of arrays and matrices.
 * Norms can be computed in different ways depending on the desired norm
 * measure. For that purpose subclass implementations of this class attempt to
 * work on different norm types.
 * By default Frobenius norm is used for arrays and matrices, which consists
 * on computing the square root of summation of all the squared elements within
 * a given matrix or array.
 */
public abstract class NormComputer {
    /**
     * Constant defining default norm type to be used.
     */
    public static final NormType DEFAULT_NORM_TYPE = NormType.FROBENIUS_NORM;
    
    /**
     * Constructor of this class.
     */
    public NormComputer(){}
    
    /**
     * Returns norm type being used by this class.
     * @return Norm type being used by this class.
     */
    public abstract NormType getNormType();
    
    /**
     * Computes norm of provided matrix.
     * @param m Matrix being used for norm computation.
     * @return Norm of provided matrix.
     */
    public abstract double getNorm(Matrix m);
    
    /**
     * Computes norm of provided array.
     * @param array Array being used for norm computation.
     * @return Norm of provided vector.
     */
    public abstract double getNorm(double[] array);
    
    /**
     * Computes norm of provided array and stores the jacobian into provided
     * instance.
     * @param array array being used for norm computation.
     * @param jacobian instance where jacobian will be stored. Must be 1xN, 
     * where N is length of array.
     * @return norm of provided vector.
     * @throws WrongSizeException if provided jacobian is not 1xN, where N is 
     * length of array.
     */
    public double getNorm(double[] array, Matrix jacobian) 
            throws WrongSizeException{
        if(jacobian != null && (jacobian.getRows() != 1 || 
                jacobian.getColumns() != array.length)){
            throw new WrongSizeException("jacobian must be 1xN, where " + 
                    "N is length of array");
        }
        
        double norm = getNorm(array);
        
        if(jacobian != null){
            jacobian.fromArray(array);
            jacobian.multiplyByScalar(1.0 / norm);
        }
        
        return norm;
    }
    
    /**
     * Factory method. Returns a new instance of NormComputer prepared to
     * compute norms using provided NormType.
     * @param normType Norm type to be used by returned instance.
     * @return New instance of NormComputer.
     */
    public static NormComputer create(NormType normType){
        switch(normType){
            case INFINITY_NORM:
                return new InfinityNormComputer();
            case ONE_NORM:
                return new OneNormComputer();
            case FROBENIUS_NORM:
            default:
                return new FrobeniusNormComputer();                
        }
    }
    
    /**
     * Factory method. Returns a new instance of NormComputer prepared to
     * compute norms using provided NormType.
     * @return New instance of NormComputer.
     */
    public static NormComputer create(){
        return create(DEFAULT_NORM_TYPE);
    }
}
