/**
 * @file
 * This file contains implementation of
 * com.irurueta.algebra.DecomposerHelper
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 16, 2012
 */
package com.irurueta.algebra;

import com.irurueta.statistics.UniformRandomizer;
import java.util.Random;

public class DecomposerHelper {
    
    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 100;
    public static final int MIN_SPD_VALUE = 1;    
    public static final int MAX_SPD_VALUE = 4;
    
    public static final double ERROR = 1e-8;
    
    public DecomposerHelper(){}
    
    //Cholesky Decomposer
    public static Matrix getLeftLowerTriangulatorFactor(int rows) 
            throws WrongSizeException{
        
        //Symmetric positive definite matrices are square symmetric matrices
        //having all their eigenvalues positive or zero.
        //Cholesky decomposition is unique for symmetric positive definite
        //matrices allowing factorization of A into: A = L * L'
        //where L is a lower triangular matrix and the elements in the diagonal
        //of L are the square root of the eigenvalues, as can be seen from eigen
        //decomposition expression of a symmetric matrix: A = Q * D * Q', where
        //Q is an orthogonal matrix and D is a diagonal matrix containing 
        //eigenvalues.
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        //Create random lower triangular matrix ensuring that elements in the
        //diagonal (eigenvalues), are positive to ensure positive definiteness
        Matrix l =  new Matrix(rows, rows);
        
        for(int j = 0; j < rows; j++){
            for(int i = 0; i < rows; i++){
                if(i < j){
                    l.setElementAt(i, j, 0.0);
                }else if(i == j){
                    l.setElementAt(i, j, Math.abs(randomizer.nextDouble(
                            MIN_SPD_VALUE, MAX_SPD_VALUE)));
                }else{
                    l.setElementAt(i, j, randomizer.nextDouble(MIN_SPD_VALUE,
                            MAX_SPD_VALUE));
                }
            }
        }
        return l;
    }
    
    public static Matrix getSymmetricPositiveDefiniteMatrixInstance(Matrix l) 
            throws WrongSizeException{        
        return l.multiplyAndReturnNew(l.transposeAndReturnNew());
    }
    
    //LU, QR and Economy QR decompoers
    public static Matrix getNonSingularMatrixInstance(int rows, int columns) 
            throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        Matrix m = new Matrix(rows, columns);
        double [] rowA;
        double [] rowB;
        boolean ld;
        if(rows < 2 || columns < 2){
            for(int i = 0; i < m.getRows(); i++){
                for(int j = 0; j < m.getColumns(); j++){
                    m.setElementAt(i, j, randomizer.nextDouble(MIN_VALUE, 
                            MAX_VALUE));
                }
            }
        }else{
            rowA = new double[m.getColumns()];
            rowB = new double[m.getColumns()];
            
            for(int i = 0; i < m.getRows(); i++){
                ld = false;
                do{
                    for(int j = 0; j < m.getColumns(); j++){
                        //Assign random values
                        m.setElementAt(i, j, randomizer.nextDouble(MIN_VALUE, 
                                MAX_VALUE));
                    }
                    
                    if(i > 0){
                        //Check that current row is not proportional to previous
                        //This is useful for matrices of size (2, 2)
                        ld = true;
                        for(int j2 = 0; j2 < m.getColumns(); j2++){
                            if(Math.abs(m.getElementAt(i, 0) * 
                                    m.getElementAt(i - 1, j2) - 
                                    m.getElementAt(i - 1, 0) * 
                                    m.getElementAt(i, j2)) > ERROR){
                                ld = false;
                                break;
                            }
                        }
                    }
                    //Check that current row is not LD with any two previous 
                    //ones
                    for(int i2 = 0; i2 < i - 1; i2++){
                        for(int j2 = 0; j2 < m.getColumns(); j2++){
                            rowA[j2] = m.getElementAt(i2 + 1, 0) * 
                                    m.getElementAt(i2, j2) - 
                                    m.getElementAt(i2, 0) * 
                                    m.getElementAt(i2 + 1, j2);
                            rowB[j2] = m.getElementAt(i, 0) * 
                                    m.getElementAt(i2, j2) - 
                                    m.getElementAt(i2, 0) * 
                                    m.getElementAt(i, j2);
                        }
                        
                        //Check whether 2 rows are LD
                        ld = true;
                        for(int j2 = 0; j2 < m.getColumns(); j2++){
                            if(Math.abs(rowA[j2] * rowB[m.getColumns() - 1] -
                                    rowB[j2] * rowA[m.getColumns() - 1]) > 
                                    ERROR){
                                ld = false;
                                break;
                            }
                        }
                    }
                }while(ld);
            }
        }
        return m;
    }
    
    public static Matrix getSingularMatrixInstance(int rows, int columns) 
            throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        Matrix m = new Matrix(rows, columns);
        int length = rows * columns;
        int row1 = randomizer.nextInt(0, rows);
        int row2;
        do{
            row2 = randomizer.nextInt(0, rows);
        }while(row2 == row1);
        
        //Initialize matrix with 2 ld rows
        for(int i = 0; i < length; i++){
            m.setElementAtIndex(i, randomizer.nextDouble(MIN_VALUE, MAX_VALUE), 
                    false);
        }
        
        for(int j = 0; j < m.getColumns(); j++){
            m.setElementAt(row2, j, m.getElementAt(row1, j));
        }
        
        return m;
    }
    
    public static Matrix getOrthonormalMatrix(int rows)
        throws WrongSizeException{
        
        Matrix m = Matrix.identity(rows, rows);
        double value = 1.0 / Math.sqrt(rows);
        m.multiplyByScalar(value);
        
        //now scramble an amount of columns
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int originColumn, destinationColumn;
        double[] column1 = new double[rows];
        double[] column2 = new double[rows];
        
        for(int i = 0; i < rows; i++){
            originColumn = randomizer.nextInt(0, rows);
            destinationColumn = randomizer.nextInt(0, rows);
            m.getSubmatrixAsArray(0, originColumn, rows - 1, originColumn, 
                    column1);
            m.getSubmatrixAsArray(0, destinationColumn, rows - 1, 
                    destinationColumn, column2);
            
            //now swap columns
            m.setSubmatrix(0, originColumn, rows - 1, originColumn, column2);
            m.setSubmatrix(0, destinationColumn, rows - 1, destinationColumn, 
                    column1);
        }
        
        return m;
    }
    
    public static Matrix getSymmetricMatrix(int rows) throws WrongSizeException{
        
        //generate random matrix of size rows x rows (the number of columns can
        //be anything indeed)
        Matrix m = Matrix.createWithUniformRandomValues(rows, rows, 
                MIN_VALUE, MAX_VALUE);
        
        //create a symmetric matrix by multiplying it with its transpose
        m.multiply(m.transposeAndReturnNew());      
        return m;
    }
}
