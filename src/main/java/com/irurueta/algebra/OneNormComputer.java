/*
 * Copyright (C) 2012 Alberto Irurueta Carro (alberto@irurueta.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.irurueta.algebra;

/**
 * Class in charge of computing one norms of arrays and matrices.
 * One norm is defined as the maximum column sum on a matrix or array.
 * For the case of arrays, this library considers arrays as column matrices
 * with one column and array length rows.
 */
@SuppressWarnings({"WeakerAccess", "Duplicates"})
public class OneNormComputer extends NormComputer {

    /**
     * Constructor of this class.
     */
    public OneNormComputer() {
        super();
    }
    
    /**
     * Returns norm type being used by this class.
     * @return Norm type being used by this class.
     */    
    @Override
    public NormType getNormType() {
        return NormType.ONE_NORM;
    }
    
    /**
     * Computes norm of provided matrix.
     * @param m matrix being used for norm computation.
     * @return norm of provided matrix.
     */
    public static double norm(Matrix m) {
        int rows = m.getRows();
        int columns = m.getColumns();
        double colSum, maxColSum = 0.0;
        
        for (int j = 0; j < columns; j++) {
            colSum = 0.0;
            for (int i = 0; i < rows; i++) {
                colSum += Math.abs(m.getElementAt(i, j));
            }
            
            maxColSum = colSum > maxColSum ? colSum : maxColSum;
        }
        
        return maxColSum;        
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
    public static double norm(double[] array) {
        double colSum = 0.0;

        for (double value : array) {
            colSum += Math.abs(value);
        }
        
        return colSum;        
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
            throws WrongSizeException {
        if (jacobian != null && (jacobian.getRows() != 1 ||
                jacobian.getColumns() != array.length)) {
            throw new WrongSizeException("jacobian must be 1xN, where " + 
                    "N is length of array");
        }
        
        double norm = norm(array);
        
        if (jacobian != null) {
            jacobian.fromArray(array);
            jacobian.multiplyByScalar(1.0 / norm);
        }
        
        return norm;        
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
