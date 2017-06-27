/**
 * @file
 * This file contains implementation of
 * com.irurueta.algebra.FrobeniusNormComputer
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 22, 2012.
 */
package com.irurueta.algebra;

/**
 * Class in charge of coputing Frobenius norms of vectors and matrices
 */
public class FrobeniusNormComputer extends NormComputer{

    /**
     * Constructor of this class.
     */
    public FrobeniusNormComputer(){
        super();
    }
    
    /**
     * Returns norm type being used by this class.
     * @return Norm type being used by this class.
     */    
    @Override
    public NormType getNormType() {
        return NormType.FROBENIUS_NORM;
    }
    
    /**
     * Computes norm of provided matrix.
     * @param m matrix being used for norm computation.
     * @return norm of provided matrix.
     */
    public static double norm(Matrix m){
        int rows = m.getRows();
        int columns = m.getColumns();
        int length = rows * columns;
        double sum = 0.0, value;
        for(int i = 0; i < length; i++){
            value = m.getElementAtIndex(i);
            sum += value * value;
        }
        return Math.sqrt(sum);        
    }
    
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
    public static double norm(double[] array, Matrix jacobian)
            throws WrongSizeException{
        if(jacobian != null && (jacobian.getRows() != 1 || 
                jacobian.getColumns() != array.length)){
            throw new WrongSizeException("jacobian must be 1xN, where " + 
                    "N is length of array");
        }
        
        double norm = norm(array);
        
        if(jacobian != null){
            jacobian.fromArray(array);
            jacobian.multiplyByScalar(1.0 / norm);
        }
        
        return norm;        
    }    

    /**
     * Computes norm of provided matrix.
     * @param m Matrix being used for norm computation.
     * @return Norm of provided matrix.
     */    
    @Override
    public double getNorm(Matrix m) {
        return norm(m);
    }
    
    /**
     * Computes norm of provided array.
     * @param array array being used for norm computation.
     * @return norm of provided vector.
     */
    public static double norm(double[] array){
        int length = array.length;
        double sum = 0.0, value;
        for(int i = 0; i < length; i++){
            value = array[i];
            sum += value * value;
        }
        return Math.sqrt(sum);        
    }

    /**
     * Computes norm of provided array.
     * @param array Array being used for norm computation.
     * @return Norm of provided vector.
     */    
    @Override
    public double getNorm(double[] array) {
        return norm(array);
    }
}
