/*
 * Copyright (C) 2016 Alberto Irurueta Carro (alberto@irurueta.com)
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

package com.irurueta.statistics;

import com.irurueta.algebra.AlgebraException;
import com.irurueta.algebra.CholeskyDecomposer;
import com.irurueta.algebra.Matrix;
import com.irurueta.algebra.WrongSizeException;

import java.util.Random;

/**
 * Generates pseudo-random values following a multivariate Gaussian distribution 
 * having the specified mean and covariance. By default mean is equal to zero 
 * and the covariance is equal to the identity (unitary standard deviation).
 */
@SuppressWarnings("WeakerAccess")
public class MultivariateGaussianRandomizer {
    
    /**
     * Contains mean values to be used for random value generation.
     */
    private double[] mMean;
    
    /**
     * Covariance matrix.
     */
    private Matrix mCovariance;
    
    /**
     * Lower triangular Cholesky decomposition of covariance matrix.
     */
    private Matrix mL;
    
    /**
     * Instance in charge of generating pseudo-random values. Secure instances
     * can be used if the generated values need to be ensured "more" random at
     * the expense of higher computational cost.
     */
    private Random mInternalRandom;
    
    /**
     * Constructor.
     */
    public MultivariateGaussianRandomizer() {
        this(new Random());
    }
    
    /**
     * Constructor.
     * Because neither mean or covariance are provided, default values will be
     * used instead.
     * @param internalRandom internal random instance in charge of generating
     * pseudo-random values.
     * @throws NullPointerException thrown if provided internal random is null.
     */
    public MultivariateGaussianRandomizer(Random internalRandom) {
        if (internalRandom == null) {
            throw new NullPointerException();
        }
        
        mInternalRandom = internalRandom;
        mMean = new double[1];
        try {
            mCovariance = mL = Matrix.identity(1, 1);
        } catch (WrongSizeException ignore) { /* never thrown */ }
    }
    
    /**
     * Constructor.
     * @param mean mean to be set.
     * @param covariance covariance to be set.
     * @throws WrongSizeException if mean length is not compatible with 
     * covariance size. Mean length must be equal to size of square covariance 
     * matrix.
     * @throws InvalidCovarianceMatrixException if provided covariance matrix is
     * not symmetric positive definite.
     */
    public MultivariateGaussianRandomizer(double[] mean, Matrix covariance) 
            throws WrongSizeException, InvalidCovarianceMatrixException {
        this(new Random(), mean, covariance);
    }
    
    /**
     * Constructor.
     * @param internalRandom internal random instance in charge of generating
     * pseudo-random values.
     * @param mean mean to be set.
     * @param covariance covariance to be set.
     * @throws WrongSizeException if mean length is not compatible with 
     * covariance size. Mean length must be equal to size of square covariance 
     * matrix.
     * @throws InvalidCovarianceMatrixException if provided covariance matrix is
     * not symmetric positive definite.
     * @throws NullPointerException thrown if provided internal random is null.
     */
    public MultivariateGaussianRandomizer(Random internalRandom, double[] mean,
            Matrix covariance) throws WrongSizeException, 
            InvalidCovarianceMatrixException {
        if (internalRandom == null) {
            throw new NullPointerException();
        }
        
        mInternalRandom = internalRandom;
        setMeanAndCovariance(mean, covariance);
    }
    
    /**
     * Returns mean value to be used for Gaussian random value generation.
     * @return mean value.
     */
    public double[] getMean() {
        return mMean;
    }
    
    /**
     * Returns covariance to be used for Gaussian random value generation.
     * @return covariance.
     */
    public Matrix getCovariance() {
        return mCovariance;
    }
    
    /**
     * Sets mean and covariance to generate multivariate Gaussian random values.
     * @param mean mean to be set.
     * @param covariance covariance to be set.
     * @throws WrongSizeException if mean length is not compatible with 
     * covariance size. Mean length must be equal to size of square covariance 
     * matrix.
     * @throws InvalidCovarianceMatrixException if provided covariance matrix is
     * not symmetric positive definite.
     */
    public final void setMeanAndCovariance(double[] mean, Matrix covariance) 
            throws WrongSizeException, InvalidCovarianceMatrixException {
        int length = mean.length;
        if (covariance.getRows() != length ||
                covariance.getColumns() != length) {
            throw new WrongSizeException("mean must have same covariance size");
        }
                
        try {
            CholeskyDecomposer decomposer = new CholeskyDecomposer(covariance);
            decomposer.decompose();
            if (!decomposer.isSPD()) {
                throw new InvalidCovarianceMatrixException(
                        "covariance matrix must be symmetric positive " + 
                        "definite (non singular)");                
            }
            
            mMean = mean;            
            mCovariance = covariance;
            
            mL = decomposer.getL();
        } catch (AlgebraException e) {
            throw new InvalidCovarianceMatrixException(
                    "covariance matrix must be square", e);            
        }
    }
    
    /**
     * Generate next set of multivariate Gaussian random values having current
     * mean and covariance of this instance.
     * @param values array where generated random values will be stored.
     * @throws IllegalArgumentException if provided array length does not have the same length
     * as provided mean array.
     */
    public void next(double[] values) {
        int n = values.length;
        
        if (n != mMean.length) {
            throw new IllegalArgumentException("values must have mean length");
        }
        
        //generate initial Gaussian values with zero mean and unitary standard 
        //deviation
        double[] tmp = new double[n];
        for (int i = 0; i < n; i++) {
            tmp[i] = mInternalRandom.nextGaussian();
        }
        
        //multiply square root of covariance (its Lower triangular Cholesky 
        //decomposition) by the generated values and add mean
        for (int i = 0; i < n; i++) {
            values[i] = 0.0;
            
            for (int j = 0; j < n; j++) {
                if (i >= j) {
                    //only evaluate lower triangular portion
                    values[i] += mL.getElementAt(i, j) * tmp[j];
                }
            }
            
            //add mean
            values[i] += mMean[i];
        }
    }
    
    /**
     * Generate next set of multivariate Gaussian random values having current
     * mean and covariance of this instance.
     * @return a new array containing generated random values.
     */
    public double[] next() {
        double[] values = new double[mMean.length];
        next(values);
        return values;
    }
}
