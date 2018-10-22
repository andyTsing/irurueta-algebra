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
 * Computes Singular Value matrix decomposition, which consists
 * on factoring provided input matrix into three factors consisting of 2
 * unary matrices and 1 diagonal matrix containing singular values,
 * following next expression: A = U * S * V'.
 * Where A is provided input matrix of size m-by-n, U is an m-by-n unary
 * matrix, S is an n-by-n diagonal matrix containing singular values, and V'
 * denotes the transpose/conjugate of V and is an n-by-n unary matrix, for
 * m &lt; n.
 */
@SuppressWarnings({"WeakerAccess", "Duplicates"})
public class SingularValueDecomposer extends Decomposer {

    /**
     * Constant defining default number of iterations to obtain convergence on
     * singular value estimation.
     */
    public static final int DEFAULT_MAX_ITERS = 30;
    
    /**
     * Constant defining minimum number of iterations allowed to obtain 
     * convergence on singular value estimation.
     */
    public static final int MIN_ITERS = 1;
    
    /**
     * Constant defining minimum allowed value as threshold to determine
     * whether a singular value is negligible or not.
     */
    public static final double MIN_THRESH = 0.0;
    
    /**
     * Constant defining machine precision.
     */    
    public static final double EPS = 1e-12;
    
    /**
     * Internal storage of U.
     */
    private Matrix u;
    
    /**
     * Internal storage of V.
     */
    private Matrix v;
    
    /**
     * Internal storage of singular values.
     */
    private double[] w;
    
    /**
     * Contains epsilon value, which indicates an estimation of numerical
     * precision given by this machine.
     */
    private double eps;
    
    /**
     * Contains threshold used to determine whether a singular value can be
     * neglected or not due to numerical precision errors. This can be used to
     * determine effective rank of input matrix.
     */
    private double tsh;
    
    /**
     * Member containing maximum number of iterations to obtain convergence of
     * singular values estimation.
     * If singular values do not converge on provided maximum number of 
     * iterations, then a NoConvergenceExceptin will be thrown when calling
     * decompose() method.
     */
    private int maxIters;
    
    /**
     * Constructor of this class.
     */
    public SingularValueDecomposer() {
        super();
        maxIters = DEFAULT_MAX_ITERS;
        u = v = null;
        w = null;
        eps = EPS;
    }
    
    /**
     * Constructor of this class.
     * @param maxIters Determines maximum number of iterations to be done when
     * decomposing input matrix into singular values so that singular values
     * converge properly.
     */
    public SingularValueDecomposer(int maxIters) {
        super();
        this.maxIters = maxIters;
        u = v = null;
        w = null;    
        eps = EPS;
    }
    
    /**
     * Constructor of this class.
     * @param inputMatrix Reference to input matrix to be decomposed.
     */
    public SingularValueDecomposer(Matrix inputMatrix) {
        super(inputMatrix);
        maxIters = DEFAULT_MAX_ITERS;
        u = v = null;
        w = null;    
        eps = EPS;
    }
    
    /**
     * Constructor of this class.
     * @param inputMatrix Reference to input matrix to be decomposed.
     * @param maxIters Determines maximum number of iterations to be done when
     * decomposing input matrix into singular value so that singular values
     * converge properly.
     */
    public SingularValueDecomposer(Matrix inputMatrix, int maxIters) {
        super(inputMatrix);
        this.maxIters = maxIters;
        u = v = null;
        w = null;  
        eps = EPS;
    }        
    
    /**
     * Returns decomposer type corresponding to Singular Value decomposition.
     * @return Decomposer type.
     */            
    @Override
    public DecomposerType getDecomposerType() {
        return DecomposerType.SINGULAR_VALUE_DECOMPOSITION;
    }

    /**
     * Sets reference to input matrix to be decomposed.
     * @param inputMatrix Reference to input matrix to be decomposed.
     * @throws LockedException Exception thrown if attempting to call this
     * method while this instance remains locked.
     */
    @Override
    public void setInputMatrix(Matrix inputMatrix) throws LockedException {
        super.setInputMatrix(inputMatrix);
        u = v = null;
        w = null;
    }        
    
    /**
     * Returns boolean indicating whether decomposition has been computed and
     * results can be retrieved.
     * Attempting to retrieve decomposition results when not available, will
     * probably raise a NotAvailableException
     * @return Boolean indicating whether decomposition has been computed and
     * results can be retrieved.
     */                    
    @Override
    public boolean isDecompositionAvailable() {
        return u != null || v != null || w != null;
    }

    /**
     * This method computes Singular Value matrix decomposition, which consists
     * on factoring provided input matrix into three factors consisting of 2 
     * unary matrices and 1 diagonal matrix containing singular values, 
     * following next expression: A = U * S * V'.
     * Where A is provided input matrix of size m-by-n, U is an m-by-n unary
     * matrix, S is an n-by-n diagonal matrix containing singular values, and V'
     * denotes the transpose/conjugate of V and is an n-by-n unary matrix, for
     * m &lt; n.
     * Note: Factors U, S and V will be accessible once Singular Value 
     * decomposition has been computed.
     * Note: During execution of this method, Singular Value decomposition will
     * be available and operations such as retrieving matrix factors, or 
     * computing rank of matrices among others will be able to be done. 
     * Attempting to call any of such operations before calling this method will
     * raise a NotAvailableException because they require computation of
     * SingularValue decomposition first.
     * @throws NotReadyException Exception thrown if attempting to call this 
     * method when this instance is not ready (i.e. no input matrix has been
     * provided).
     * @throws LockedException Exception thrown if this decomposer is already
     * locked before calling this method. Notice that this method will actually
     * lock this instance while it is being executed.
     * @throws DecomposerException Exception thrown if for any reason 
     * decomposition fails while being executed, like when convergence of 
     * results cannot be obtained, etc.
     */
    @Override
    public void decompose() throws NotReadyException, LockedException, 
            DecomposerException {
        
        if (!isReady()) {
            throw new NotReadyException();
        }
        if (isLocked()) {
            throw new LockedException();
        }
        
        locked = true;
        
        int m = inputMatrix.getRows();
        int n = inputMatrix.getColumns();
        
        //copy input matrix into U
        u = inputMatrix.clone();
        w = new double[n];
        try {
            v = new Matrix(n, n);
        } catch (WrongSizeException ignore) { }
        try {
            internalDecompose();
            reorder();
            setNegligibleSingularValueThreshold(0.5 * Math.sqrt(m + n + 1.0) * 
                w[0] * eps);
            locked = false;
        } catch (DecomposerException e) {
            u = v = null;
            w = null;
            locked = false;
            throw e;
        }
    }
    
    /**
     * Returns maximum number of iterations to be done in order to obtain
     * convergence of singular values when computing input matrix Singular
     * Value Decomposition.
     * @return Maximum number of iterations to obtain singular values 
     * convergence.
     */
    public int getMaxIterations() {
        return maxIters;
    }
    
    /**
     * SSets maximum number of iterations to be done in order to obtain
     * convergence of singular values when computing input matrix Singular
     * Value Decomposition.
     * Note: This parameter should rarely be modified because default value
     * is usually good enough.
     * Note: If convergence of singular values is not achieved within provided
     * maximum number of iterations, a DecomposerException will be thrown when
     * calling decompose();
     * @param maxIters Maximum number of iterations to obtain convergence of
     * singular values. Provided value must be 1 or greater, otherwise an
     * IllegalArgumentException will be thrown.
     * @throws LockedException Exception thrown if attempting to call this
     * method while this instance remains locked.
     * @throws IllegalArgumentException Exception thrown if provided value
     * for maxIters is out of valid range of values.
     */
    public void setMaxIterations(int maxIters) throws LockedException,
            IllegalArgumentException {
        if (isLocked()) {
            throw new LockedException();
        }
        if (maxIters < MIN_ITERS) {
            throw new IllegalArgumentException();
        }
        
        this.maxIters = maxIters;
    }
    
    /**
     * Returns threshold to be used for determining whether a singular value is
     * negligible or not.
     * This threshold can be used to consider a singular value as zero or not,
     * since small singular values might appear in places where they should be
     * zero because of rounding errors and machine precision.
     * Singular values considered as zero determine aspects such as rank,
     * nullability, nullspace or range space.
     * @return Threshold to be used for determining whether a singular value
     * is negligible or not.
     * @throws NotAvailableException Exception thrown if attempting to call
     * this method before computing Singular Value decomposition.
     * To avoid this exception call decompose() method first.
     */
    public double getNegligibleSingularValueThreshold() 
            throws NotAvailableException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        return tsh;
    }
    
    /**
     * Returns a new matrix instance containing the left singular vector (U
     * factor) from Singular Value matrix decomposition, which consists
     * on decomposing a matrix using the following expression:
     * A = U * S * V'.
     * Where A is provided input matrix of size m-by-n and U is an m-by-n
     * unary matrix for m &lt; n.
     * @return Matrix instance containing the left singular vectors from a
     * Singular Value decomposition.
     * @throws NotAvailableException Exception thrown if attempting to call
     * this method before computing Singular Value decomposition. To avoid
     * this exception call decompose() method first.
     * @see #decompose()
     */
    public Matrix getU() throws NotAvailableException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        return u;
    }
    
    /**
     * Returns a new matrix instance containing the right singular vectors
     * (V factor) from Singular Value matrix decomposition, which consists on
     * decomposing a matrix using the following expression:
     * A = U * S * V',
     * Where A is provided input matrix of size m-by-n and V' denotes the
     * transpose/conjugate of V, which is an n-by-n unary matrix for m &lt; n.
     * @return Matrix instance containing the right singular vectors from a
     * Singular Value decomposition.
     * @throws NotAvailableException Exception thrown if attempting to call
     * this method before computing SIngular Value decomposition. To avoid
     * this exception call decompose() method first.
     * @see #decompose()
     */
    public Matrix getV() throws NotAvailableException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        return v;
    }
    
    /**
     * Returns a new vector instance containing all singular values after
     * decomposition.
     * Returned vector is equal to the diagonal of S matrix within expression:
     * A = U * S * V' where A is provided input matrix and S is a diagonal
     * matrix containing singular values on its diagonal.
     * @return singular values.
     * @throws NotAvailableException if decomposition has not yet been computed.
     */
    public double[] getSingularValues() throws NotAvailableException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        return w;
    }
    
    /**
     * Copies diagonal matrix into provided instance containing all singular 
     * values on its diagonal after Singular Value matrix decomposition, which 
     * consists on decomposing a matrix using the following expression: 
     * A = U * S * V'.
     * Where A is provided input matrix of size m-by-n and S is a diagonal
     * matrix of size n-by-n for m &lt; n.
     * @param m matrix instance containing all singular values on its
     * diagonal after execution of this method.
     * @throws NotAvailableException Exception thrown if attempting to call
     * this method before computing Singular Value decomposition. To avoid 
     * this exception call decompose() method first.
     * @see #decompose()
     */    
    public void getW(Matrix m) throws NotAvailableException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        Matrix.diagonal(w, m);
    }
    
    /**
     * Returns a new diagonal matrix instance containing all singular values on
     * its diagonal after Singular Value matrix decomposition, which consists
     * on decomposing a matrix using the following expression: A = U * S * V'.
     * Where A is provided input matrix of size m-by-n and S is a diagonal
     * matrix of size n-by-n for m &lt; n.
     * @return Returned matrix instance containing all singular values on its
     * diagonal.
     * @throws NotAvailableException Exception thrown if attempting to call
     * this method before computing Singular Value decomposition. To avoid 
     * this exception call decompose() method first.
     * @see #decompose()
     */
    public Matrix getW() throws NotAvailableException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        
        //copy S array into a new diagonal matrix
        return Matrix.diagonal(w);
    }
    
    /**
     * Returns the 2-norm of provided input matrix, which is equal to the highest
     * singular value found after decomposition. This is also called the Ky Fan
     * 1-norm.
     * This norm is also equal to the square root of Frobenius norm of the
     * squared of provided input matrix. In other words:
     * sqrt(norm(A' * A, 'fro')) in Matlab notation.
     * Where A is provided input matrix and A' is its transpose, and hence
     * A' * A can be considered the squared matrix of A, and Frobenius norm
     * is defined as the square root of the sum of the squared elements of a
     * matrix: sqr(sum(A(:).^2))
     * @return The 2-norm of provided input matrix, which is equal to the 
     * highest singular value.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing Singular Value decomposition. To avoid this
     * exception call decompose() method first.
     * @see #decompose()
     */
    public double getNorm2() throws NotAvailableException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        return w[0];
    }
    
    /**
     * Returns the condition number of provided input matrix found after
     * decomposition.
     * The condition number of a matrix measures the sensitivity of the
     * solution of a system of linear equations to errors in the data.
     * It gives an indication of the accuracy of the results from matrix
     * inversion and the solution of a linear system of equations.
     * A problem with a low condition number is said to be well-conditioned,
     * whereas a problem with a high condition number is said to be 
     * ill-conditioned.
     * The condition number is a property of a matrix and is not related to the
     * algorithm or floating point accuracy of a machine to solve a linear
     * system of equations or make matrix inversion.
     * When solving a linear system of equations (A * X = b), one should think
     * of the condition number as being (very roughly) the rate at which the
     * solution x will change with respect to a change in b.
     * Thus, if the condition number is large, even a small error in b may
     * cause a large error in x. On the other hand, if the condition number is
     * small then the error in x will not be much bigger than the error in b.
     * One way to find the condition number is by using the ration of the
     * maximal and minimal singular values of a matrix, which is what this
     * method returns.
     * @return The condition number of provided input matrix.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing Singular Value decomposition. To avoid this
     * exception call decompose() method first.
     * @see #decompose()
     */
    public double getConditionNumber() throws NotAvailableException {
        return 1.0 / getReciprocalConditionNumber();
    }
    
    /**
     * Returns the inverse of the condition number, i.e. 1.0 / condition number.
     * Hence, when reciprocal condition number is close to zero, input matrix
     * will be ill-conditioned.
     * For more information see getConditionNumber()
     * @return Inverse of the condition number.
     * @throws NotAvailableException  Exception thrown if attempting to call
     * this method before computing Singular Value decomposition. To avoid this
     * exception call decompose() method first.
     * @see #decompose()
     * @see #getConditionNumber()
     */
    public double getReciprocalConditionNumber() throws NotAvailableException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        
        int columns = inputMatrix.getColumns();
        return (w[0] <= 0.0 || w[columns - 1] <= 0.0) ? 0.0 : 
                w[columns - 1] / w[0];
    }
    
    /**
     * Returns effective numerical matrix rank.
     * By definition rank of a matrix can be found as the number of non-zero
     * singular values of such matrix found after decomposition.
     * However, rounding error and machine precision may lead to small but non-
     * zero, singular values in a rank deficient matrix.
     * This method tries to cope with such rounding errors by taking into 
     * account only those non-negligible singular values to determine input
     * matrix rank.
     * The Rank-nullity theorem states that for a matrix A of size m-by-n then:
     * rank(A) + nullity(A) = n
     * Where A is input matrix and n is the number of columns of such matrix.
     * @param singularValueThreshold Threshold used to determine whether a
     * singular value is negligible or not.
     * @return Effective numerical matrix rank.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing Singular Value decomposition. To avoid this
     * exception call decompose() method first.
     * @throws IllegalArgumentException Exception thrown if provided singular
     * value threshold is negative. Returned singular values after decomposition
     * are always positive, and hence, provided threshold should be a positive
     * value close to zero.
     * @see #decompose()
     */
    public int getRank(double singularValueThreshold) 
            throws NotAvailableException, IllegalArgumentException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        if (singularValueThreshold < MIN_THRESH) {
            throw new IllegalArgumentException();
        }
        
        int r = 0;
        for (double aW : w) {
            if (aW > singularValueThreshold) {
                r++;
            }
        }
        return r;
    }
    
    /**
     * Returns effective numerical matrix rank.
     * By definition rank of a matrix can be found as the number of non-zero
     * singular values of such matrix found after decomposition.
     * However, rounding error and machine precision may lead to small but non-
     * zero singular values in a rank deficient matrix.
     * This method tries to cope with such rounding error by taking into account
     * only those non-negligible singular values to determine input matrix rank.
     * The Rank-nullity theorem states that for a matrix A of size m-by-n then:
     * rank(A) + nullity(A) = n
     * Where A is input matrix and n is number of columns of such matrix.
     * Note: This method makes same actions as int getRank(double)
     * except that singular value threshold is automatically computed by taking
     * into account input matrix size, maximal singular value and machine 
     * precision. This threshold is good enough for most situations, and hence
     * we discourage setting it manually.
     * @return Effective numerical matrix rank.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing Singular Value decomposition. To avoid this
     * exception call decompose() method first.
     * @see #decompose()
     */
    public int getRank() throws NotAvailableException {
        return getRank(getNegligibleSingularValueThreshold());
    }
    
    /**
     * Returns effective numerical matrix nullity.
     * By definition nullity of a matrix can be found as the number of zero or
     * negligible singular values of provided input matrix after decomposition.
     * ROunding error and machine precision may lead to small but non-zero
     * singular values in a rank deficient matrix.
     * This method tries to cope with such rounding error by taking into account
     * only those negligible singular values to determine input matrix nullity.
     * The Rank-nullity theorem states that for a matrix A of size m-by-n then:
     * rank(A) + nullity(A) = n
     * Where A is input matrix and n is number of columns of such matrix.
     * @param singularValueThreshold Threshold used to determine whether a
     * singular value is negligible or not.
     * @return Effective numerical matrix nullity.
     * @throws NotAvailableException Exception thrown if attempting to call
     * this method before computing Singular Value decomposition.
     * @throws IllegalArgumentException Exception thrown if provided singular
     * value threshold is negative. Returned singular values after decomposition
     * are always positive, and hence, provided threshold should be a positive
     * value close to zero.
     * @see #decompose()
     */
    public int getNullity(double singularValueThreshold) 
            throws NotAvailableException, IllegalArgumentException {
        int n = inputMatrix.getColumns();
        return n - getRank(singularValueThreshold);
    }
    
    /**
     * Returns effective numerical matrix nullity.
     * By definition nullity of a matrix can be found as the number of zero or
     * negligible singular values of provided input matrix after decomposition.
     * Rounding error and machine precision may lead to small but non-zero
     * singular values in a rank deficient matrix.
     * This method tries to cope with such rounding error by taking into account
     * only those negligible singular values to determine input matrix nullity.
     * The Rank-nullity theorem states that for a matrix A of size m-by-n then:
     * rank(A) + nulity(A) = n
     * Where A is input matrix and n is number of columns of such matrix.
     * Note: This method makes the same actions as int getNullity(double) except
     * that singular value threshold is automatically computed by taking into 
     * account input matrix size, maximal singular value and machine precision. 
     * This threshold is good enough for most situations, and hence we 
     * discourage setting it manually.
     * @return Effective numerical matrix nullity.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing Singular Value decomposition. To avoid this
     * exception call decompose() method first.
     * @see #decompose()
     */
    public int getNullity() throws NotAvailableException {
        return getNullity(getNegligibleSingularValueThreshold());
    }
    
    /**
     * Internal method to copy range space vector values into provided matrix.
     * Provided matrix will be resized if needed
     * @param rank Rank of range space
     * @param singularValueThreshold Threshold to determine whether a singular
     * value is null
     * @param range Matrix where range space vector values are stored.
     */
    private void internalGetRange(int rank, double singularValueThreshold,
            Matrix range) {
        int rows = inputMatrix.getRows();
        int columns = inputMatrix.getColumns();
        
        if (range.getRows() != rows || range.getColumns() != rank) {
            try {
                range.resize(rows, rank);
            } catch (WrongSizeException ignore) { }
        }
        
        int nr = 0;
        for (int j = 0; j < columns; j++) {
            if (w[j] > singularValueThreshold) {
                //copy column j of U matrix into column nr of out matrix
                range.setSubmatrix(0, nr, rows - 1, nr, u, 0, j, rows - 1, j);
                nr++;
            }
        }        
    }
    
    /**
     * Sets into provided range matrix the Range space of provided input matrix, 
     * which spans a subspace of dimension equal to the rank of input matrix.
     * Range space is equal to the columns of U corresponding to non-negligible
     * singular values.
     * @param singularValueThreshold Threshold used to determine whether a 
     * singular value is negligible or not.
     * @param range Matrix containing Range space of provided input matrix.
     * @throws NotAvailableException Exception thrown if input matrix has rank
     * zero or also if attempting to call this method before computing Singular
     * Value decomposition. To avoid this exception call decompose() method 
     * first and make sure that input matrix has non-zero rank.
     * @throws IllegalArgumentException Exception thrown if provided singular
     * value threshold is negative. Returned singular values after decomposition
     * are always positive, and hence, provided threshols should be a positive
     * near to zero value.
     */    
    public void getRange(double singularValueThreshold, Matrix range)
            throws NotAvailableException, IllegalArgumentException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        int rank = getRank(singularValueThreshold);
        internalGetRange(rank, singularValueThreshold, range);
    }
    
    /**
     * Sets into provided range matrix the Range space of provided input matrix, 
     * which spans a subspace of dimension equal to the rank of input matrix.
     * Range space is equal to the columns of U corresponding to non-negligible
     * singular values.
     * This method performs same actions as getRange(double, Matrix) except that
     * singular value threshold is automatically computed by taking into account
     * input matrix size, maximal singular value and machine precision.
     * This threshold is good enough for most situations, and hence we 
     * discourage setting it manually.
     * @param range Matrix containing Range space of provided input matrix.
     * @throws NotAvailableException Exception thrown if input matrix has rank
     * zero or also if attempting to call this method before computing Singular
     * Value decomposition. To avoid this exception call decompose() method 
     * first and make sure that input matrix has non-zero rank.
     * @throws IllegalArgumentException Exception thrown if provided singular
     * value threshold is negative. Returned singular values after decomposition
     * are always positive, and hence, provided threshols should be a positive
     * near to zero value.
     */        
    public void getRange(Matrix range) throws NotAvailableException,
            IllegalArgumentException {
        getRange(getNegligibleSingularValueThreshold(), range);
    }
    
    /**
     * Returns matrix containing Range space of provided input matrix, which
     * spans a subspace of dimension equal to the rank of input matrix.
     * Range space is equal to the columns of U corresponding to non-negligible
     * singular values.
     * @param singularValueThreshold Threshold used to determine whether a 
     * singular value is negligible or not.
     * @return Matrix containing Range space of provided input matrix.
     * @throws NotAvailableException Exception thrown if input matrix has rank
     * zero or also if attempting to call this method before computing Singular
     * Value decomposition. To avoid this exception call decompose() method 
     * first and make sure that input matrix has non-zero rank.
     * @throws IllegalArgumentException Exception thrown if provided singular
     * value threshold is negative. Returned singular values after decomposition
     * are always positive, and hence, provided threshols should be a positive
     * near to zero value.
     */
    public Matrix getRange(double singularValueThreshold) 
            throws NotAvailableException, IllegalArgumentException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        int rows = inputMatrix.getRows();
        int rank = getRank(singularValueThreshold);
        
        Matrix out;
        try {
            out = new Matrix(rows, rank);
        } catch (WrongSizeException e) {
            throw new NotAvailableException(e);
        }
        internalGetRange(rank, singularValueThreshold, out);
        return out;
    }
        
    /**
     * Return matrix containing Range space of provided input matrix, which
     * spans a subspace of dimension equal to the rank of input matrix.
     * Range space is equal to the columns of U corresponding to non-negligible
     * singular values.
     * This method performs same actions as Matrix getRange(double) except that
     * singular value threshold is automatically computed by taking into account
     * input matrix size, maximal singular value and machine precision.
     * This threshold is good enough for most situations, and hence we 
     * discourage setting it manually.
     * @return Matrix containing Range space of provided input matrix
     * @throws NotAvailableException Exception thrown if input matrix has rank
     * zero or also if attempting to call this method before computing Singular
     * Value decomposition. To avoid this exception call decompose() method 
     * first and make sure that input matrix has non-zero rank.
     */
    public Matrix getRange() throws NotAvailableException {
        return getRange(getNegligibleSingularValueThreshold());
    }
    
    /**
     * Internal method to copy nullspace vector values into provided matrix.
     * Provided matrix will be resized if needed
     * @param nullity Nullity of nullspace
     * @param singularValueThreshold Threshold to determine whether a singular
     * value is null
     * @param nullspace Matrix where nullspace vector values are stored.
     */
    public void internalGetNullspace(int nullity, double singularValueThreshold,
            Matrix nullspace) {
        int columns = inputMatrix.getColumns();
        
        if (nullspace.getRows() != columns || nullspace.getColumns() != nullity){
            try {
                nullspace.resize(columns, nullity);
            } catch (WrongSizeException ignore){ }
        }
        
        int nn = 0;
        for (int j = 0; j < columns; j++) {
            if (w[j] <= singularValueThreshold) {
                //copy column j of U matrix into column nn of out matrix
                nullspace.setSubmatrix(0, nn, columns - 1, nn, v, 0, j, 
                        columns - 1, j);
                nn++;
            }
        }
    }
    
    /**
     * Sets into provided matrix Nullspace of provided input matrix, which spans
     * a subspace of dimension equal to the nullity of input matrix. Nullspace 
     * is equal to the columns of V corresponding to negligible singular values.
     * @param singularValueThreshold Threshold used to determine whether a 
     * singular value is negligible or not.
     * @param nullspace Matrix containing nullspace of provided input matrix.
     * @throws NotAvailableException Exception thrown if input matrix has full
     * rank, and hence its nullity is zero, or also if attempting to call this
     * method before computing Singular Value decomposition. To avoid this
     * exception call decompose() method first and make sure that input matrix
     * is rank deficient.
     * @throws IllegalArgumentException Exception thrown if provided singular
     * value threshold is negative. Returned singular values after decomposition
     * are always positive, and hence, provided threshold should be a positive 
     * near to zero value.
     */    
    public void getNullspace(double singularValueThreshold, Matrix nullspace)
            throws NotAvailableException, IllegalArgumentException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        int nullity = getNullity(singularValueThreshold);
        internalGetNullspace(nullity, singularValueThreshold, nullspace);
    }

    /**
     * Sets into provided matrix Nullspace of provided input matrix, which spans
     * a subspace of dimension equal to the nullity of input matrix. Nullspace 
     * is equal to the columns of V corresponding to negligible singular values.
     * This method performs same actions as getNullspace(double, Matrix) except
     * that singular value threshold is automatically computed by taking into
     * account input matrix size, maximal singular value and machine precision.
     * This threshold is good enough for most situations, and hence we 
     * discourage setting it manually.
     * @param nullspace Matrix containing nullspace of provided input matrix.
     * @throws NotAvailableException Exception thrown if input matrix has full
     * rank, and hence its nullity is zero, or also if attempting to call this
     * method before computing Singular Value decomposition. To avoid this
     * exception call decompose() method first and make sure that input matrix
     * is rank deficient.
     * @throws IllegalArgumentException Exception thrown if provided singular
     * value threshold is negative. Returned singular values after decomposition
     * are always positive, and hence, provided threshold should be a positive 
     * near to zero value.
     */        
    public void getNullspace(Matrix nullspace) throws NotAvailableException,
            IllegalArgumentException {
        getNullspace(getNegligibleSingularValueThreshold(), nullspace);
    }
    
    /**
     * Returns matrix containing Nullspace of provided input matrix, which spans
     * a subspace of dimension equal to the nullity of input matrix. Nullspace 
     * is equal to the columns of V corresponding to negligible singular values.
     * @param singularValueThreshold Threshold used to determine whether a 
     * singular value is negligible or not.
     * @return Matrix containing nullspace of provided input matrix.
     * @throws NotAvailableException Exception thrown if input matrix has full
     * rank, and hence its nullity is zero, or also if attempting to call this
     * method before computing Singular Value decomposition. To avoid this
     * exception call decompose() method first and make sure that input matrix
     * is rank deficient.
     * @throws IllegalArgumentException Exception thrown if provided singular
     * value threshold is negative. Returned singular values after decomposition
     * are always positive, and hence, provided threshold should be a positive 
     * near to zero value.
     */
    public Matrix getNullspace(double singularValueThreshold) 
            throws NotAvailableException, IllegalArgumentException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        
        int columns = inputMatrix.getColumns();
        int nullity = getNullity(singularValueThreshold);
        Matrix out;
        try {
            out = new Matrix(columns, nullity);
        } catch (WrongSizeException e) {
            throw new NotAvailableException(e);
        }
        internalGetNullspace(nullity, singularValueThreshold, out);
        return out;
    }       
    
    /**
     * Returns matrix containing Nullspace of provided input matrix, which
     * spans a subspace of dimension equal to the nullity of input matrix.
     * Nullspace is equal to the columns of V corresponding to negligible 
     * singular values.
     * @return Matrix containing nullspace of provided input matrix.
     * This method performs same actions as Matrix getNullspace(double) except
     * that singular value threshold is automatically computed by taking into
     * account input matrix size, maximal singular value and machine precision.
     * This threshold is good enough for most situations, and hence we 
     * discourage setting it manually.
     * @throws NotAvailableException Exception thrown if input matrix has full
     * rank, and hence its nullity is zero, or also if attempting to call this
     * method before computing Singular Value decomposition. To avoid this
     * exception call decompose() method first and make sure that input matrix
     * is rank deficient.
     */
    public Matrix getNullspace() throws NotAvailableException {
        return getNullspace(getNegligibleSingularValueThreshold());
    }
    
    /**
     * Solves a linear system of equations of the following form: A * X = B
     * using the pseudo-inverse to find a least squares solution.
     * Where A is the input matrix provided for Singular Value decomposition,
     * X is the solution to the system of equations, and B is the parameters
     * matrix.
     * Note: This method can be reused for different b matrices without having
     * to recompute Singular Value decomposition on the same input matrix.
     * Note: Provided b matrix must have the same number of rows as provided
     * input matrix A, otherwise a WrongSizeException will be raised.
     * Note: In order to execute this method, a Singular Value decomposition
     * must be available, otherwise a NotAvailableException will be raised. In
     * order to avoid this exception call decompose() method first.
     * Note: Provided result matrix will be resized if needed
     * @param b Parameters matrix that determine a linear system of equations.
     * Provided matrix must have the same number of rows as provided input 
     * matrix for Singular Value decomposition. Besides, each column on 
     * parameters matrix will represent a new system of equations, whose 
     * solution will be returned on appropriate column as an output of this 
     * method.
     * @param singularValueThreshold Threshold used to determine whether a 
     * singular value is negligible or not.
     * @param result Matrix containing least squares solution of linear system 
     * of equations on each column for each column of provided parameters matrix
     * b.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing SIngular Value decomposition. To avoid this
     * exception call decompose() method first.
     * @throws WrongSizeException Exception thrown if provided parameters matrix
     * (b) does not have the same number of rows as input matrix being Singular
     * Value decomposed.
     * @throws IllegalArgumentException Exception thrown if provided singular
     * value threshold is lower than minimum allowed value (MIN_THRESH).
     * @see #decompose()
     */    
    public void solve(Matrix b, double singularValueThreshold, Matrix result)
            throws NotAvailableException, WrongSizeException, 
            IllegalArgumentException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        
        if (b.getRows() != inputMatrix.getRows()) {
            throw new WrongSizeException();
        }
        
        if (singularValueThreshold < MIN_THRESH) {
            throw new IllegalArgumentException();
        }
        
        int m = inputMatrix.getRows();
        int n = inputMatrix.getColumns();
        int p = b.getColumns();
        
        double[] bcol = new double[m];
        double[] xx;
        
        //resize result matrix if needed
        if (result.getRows() != n || result.getColumns() != p) {
            result.resize(n, p);
        }
        
        for (int j = 0; j < p; j++) {
            for (int i = 0; i < m; i++) {
                bcol[i] = b.getElementAt(i, j);
            }
            
            xx = solve(bcol, singularValueThreshold);
            //set column j of X using values in vector xx
            result.setSubmatrix(0, j, n - 1, j, xx);
            
        }       
    }
    
    /**
     * Solves a linear system of equations of the following form: A * X = B
     * using the pseudo-inverse to find a least squares solution.
     * Where A is the input matrix provided for Singular Value decomposition,
     * X is the solution to the system of equations, and B is the parameters
     * matrix.
     * Note: This method can be reused for different b matrices without having
     * to recompute Singular Value decomposition on the same input matrix.
     * Note: Provided b matrix must have the same number of rows as provided
     * input matrix A, otherwise a WrongSizeException will be raised.
     * Note: In order to execute this method, a Singular Value decomposition
     * must be available, otherwise a NotAvailableException will be raised. In
     * order to avoid this exception call decompose() method first.
     * Note: This method performs same actions as Matrix solve(Matrix, double)
     * except that singular value threshold is automatically computed by taking
     * into account input matrix size, maximal singular value and machine 
     * precision.
     * This threshold is good enough for most situations, and hence we 
     * discourage setting it manually.
     * Note: Provided result matrix will be resized if needed
     * @param b Parameters matrix that determines a linear system of equations.
     * Provided matrix must have the same number of rows as provided input 
     * matrix for Singular Value decomposition. Besides, each column on 
     * parameters matrix will represent a new system of equations, whose 
     * solution will be returned on appropriate column as an output of this method.
     * @param result Matrix containing least squares solution of linear system 
     * of equations on each column for each column of provided parameters matrix
     * b.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing Singular Value decomposition. To avoid this
     * exception call decompose() method first.
     * @throws WrongSizeException Exception thrown if provided parameters matrix
     * (b) does not have the same number of rows as input matrix being Singular
     * Value decomposed.
     * @see #decompose()
     */    
    public void solve(Matrix b, Matrix result) throws NotAvailableException,
            WrongSizeException, IllegalArgumentException {
        solve(b, getNegligibleSingularValueThreshold(), result);
    }
    
    /**
     * Solves a linear system of equations of the following form: A * X = B
     * using the pseudo-inverse to find a least squares solution.
     * Where A is the input matrix provided for Singular Value decomposition,
     * X is the solution to the system of equations, and B is the parameters
     * matrix.
     * Note: This method can be reused for different b matrices without having
     * to recompute Singular Value decomposition on the same input matrix.
     * Note: Provided b matrix must have the same number of rows as provided
     * input matrix A, otherwise a WrongSizeException will be raised.
     * Note: In order to execute this method, a Singular Value decomposition
     * must be available, otherwise a NotAvailableException will be raised. In
     * order to avoid this exception call decompose() method first.
     * @param b Parameters matrix that determine a linear system of equations.
     * Provided matrix must have the same number of rows as provided input 
     * matrix for Singular Value decomposition. Besides, each column on 
     * parameters matrix will represent a new system of equations, whose 
     * solution will be returned on appropriate column as an output of this 
     * method.
     * @param singularValueThreshold Threshold used to determine whether a 
     * singular value is negligible or not.
     * @return Matrix containing least squares solution of linear system of
     * equations on each column for each column of provided parameters matrix b.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing SIngular Value decomposition. To avoid this
     * exception call decompose() method first.
     * @throws WrongSizeException Exception thrown if provided parameters matrix
     * (b) does not have the same number of rows as input matrix being Singular
     * Value decomposed.
     * @throws IllegalArgumentException Exception thrown if provided singular
     * value threshold is lower than minimum allowed value (MIN_THRESH).
     * @see #decompose()
     */
    public Matrix solve(Matrix b, double singularValueThreshold)
            throws NotAvailableException, WrongSizeException, 
            IllegalArgumentException {
        int n = inputMatrix.getColumns();
        int p = b.getColumns();
        Matrix x = new Matrix(n, p);
        solve(b, singularValueThreshold, x);
        return x;
    }
    
    /**
     * Solves a linear system of equations of the following form: A * X = B
     * using the pseudo-inverse to find a least squares solution.
     * Where A is the input matrix provided for Singular Value decomposition,
     * X is the solution to the system of equations, and B is the parameters
     * matrix.
     * Note: This method can be reused for different b matrices without having
     * to recompute Singular Value decomposition on the same input matrix.
     * Note: Provided b matrix must have the same number of rows as provided
     * input matrix A, otherwise a WrongSizeException will be raised.
     * Note: In order to execute this method, a Singular Value decomposition
     * must be available, otherwise a NotAvailableException will be raised. In
     * order to avoid this exception call decompose() method first.
     * Note: This method performs same actions as Matrix solve(Matrix, double)
     * except that singular value threshold is automatically computed by taking
     * into account input matrix size, maximal singular value and machine 
     * precision.
     * This threshold is good enough for most situations, and hence we 
     * discourage setting it manually.
     * @param b Parameters matrix that determines a linear system of equations.
     * Provided matrix must have the same number of rows as provided input 
     * matrix for Singular Value decomposition. Besides, each column on 
     * parameters matrix will represent a new system of equations, whose 
     * solution will be returned on appropriate column as an output of this method.
     * @return Matrix containing least squares solution of linear system of
     * equations on each column for each column of provided parameters matrix b.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing Singular Value decomposition. To avoid this
     * exception call decompose() method first.
     * @throws WrongSizeException Exception thrown if provided parameters matrix
     * (b) does not have the same number of rows as input matrix being Singular
     * Value decomposed.
     * @see #decompose()
     */
    public Matrix solve(Matrix b) throws NotAvailableException, 
            WrongSizeException {
        return solve(b, getNegligibleSingularValueThreshold());
    }
    
    /**
     * Solves a linear system of equations of the following form: A * X = B
     * using the pseudo-inverse to find a least squares solution.
     * Where A i s the input matrix provided for Singular Value decomposition,
     * X is the solution to the system of equations, and B is the parameters
     * array.
     * Note: This method can be reused for different b arrays without having
     * to recompute Singular Value decomposition on the same input matrix.
     * Note: Provided b array must have the same length as the number of rows
     * on provided input matrix A, otherwise a WrongSizeException will be
     * raised.
     * Note: In order to execute this method, a Singular Value decomposition
     * must be available, otherwise a NotAvailableException will be raised. In
     * order to avoid this exception call decompose() method first.
     * @param b Parameters array that determines a linear system of equations.
     * Provided array must have the same length as number of rows on provided
     * input matrix for Singular Value decomposition.
     * @param singularValueThreshold Threshold used to determine whether a
     * singular value is negligible or not.
     * @param result Vector where least squares solution of linear system of
     * equations for provided parameters array b will be stored.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing SingularValue decomposition.
     * To avoid this exception call decompose() method first.
     * @throws WrongSizeException Exception thrown if provided parameters array
     * (b) does not have the same length as number of rows on input matrix being
     * Singular Value decomposed or if provided result array does not have the
     * same length as the number of columns on input matrix.
     * @throws IllegalArgumentException Exception thrown if provided singular
     * value threshold is lower than minimum allowed value (MIN_THRESH).
     * @see #decompose()
     */    
    public void solve(double[] b, double singularValueThreshold, 
            double[] result) throws NotAvailableException, WrongSizeException,
            IllegalArgumentException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        
        if (b.length != inputMatrix.getRows()) {
            throw new WrongSizeException();
        }
        
        int m = inputMatrix.getRows();
        int n = inputMatrix.getColumns();
        
        if (result.length != n) {
            throw new WrongSizeException();
        }
                
        double s;
        double[] tmp = new double[n];
        
        for (int j = 0; j < n; j++) {
            s = 0.0;
            if (w[j] > singularValueThreshold) {
                for (int i = 0; i < m; i++) {
                    s += u.getElementAt(i, j) * b[i];
                }
                s /= w[j];
            }
            tmp[j] = s;
        }
        for (int j = 0; j < n; j++) {
            s = 0.0;
            for (int jj = 0; jj < n; jj++) {
                s += v.getElementAt(j, jj) * tmp[jj];
            }
            result[j] = s;
        }       
    }
    
    /**
     * Solves a linear system of equations of the following form: A * X = B
     * using the pseudo-inverse to find a least squares solution.
     * Where A i s the input matrix provided for Singular Value decomposition,
     * X is the solution to the system of equations, and B is the parameters
     * array.
     * Note: This method can be reused for different b arrays without having
     * to recompute Singular Value decomposition on the same input matrix.
     * Note: Provided b array must have the same length as the number of rows
     * on provided input matrix A, otherwise a WrongSizeException will be
     * raised.
     * Note: In order to execute this method, a Singular Value decomposition
     * must be available, otherwise a NotAvailableException will be raised. In
     * order to avoid this exception call decompose() method first.
     * Note: this method performs same actions as double[] solve(double[], 
     * double) except that singular value threshold is automatically computed by
     * taking into account input matrix size, maximal singular value and machine
     * precision.
     * This threshold is good enough for most situations, and hence we discourage
     * setting it manually.
     * @param b Parameters array that determines a linear system of equations.
     * Provided array must have the same length as number of rows on provided
     * input matrix for Singular Value decomposition.
     * @param result Vector where least squares solution of linear system of
     * equations for provided parameters array b will be stored.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing SingularValue decomposition.
     * To avoid this exception call decompose() method first.
     * @throws WrongSizeException Exception thrown if provided parameters array
     * (b) does not have the same length as number of rows on input matrix being
     * Singular Value decomposed or if provided result array does not have the
     * same length as the number of columns on input matrix.
     * @throws IllegalArgumentException Exception thrown if provided singular
     * value threshold is lower than minimum allowed value (MIN_THRESH).
     * @see #decompose()
     */        
    public void solve(double[] b, double[] result) throws NotAvailableException,
            WrongSizeException, IllegalArgumentException {
        solve(b, getNegligibleSingularValueThreshold(), result);
    }
    
    /**
     * Solves a linear system of equations of the following form: A * X = B
     * using the pseudo-inverse to find a least squares solution.
     * Where A i s the input matrix provided for Singular Value decomposition,
     * X is the solution to the system of equations, and B is the parameters
     * array.
     * Note: This method can be reused for different b arrays without having
     * to recompute Singular Value decomposition on the same input matrix.
     * Note: Provided b array must have the same length as the number of rows
     * on provided input matrix A, otherwise a WrongSizeException will be
     * raised.
     * Note: In order to execute this method, a Singular Value decomposition
     * must be available, otherwise a NotAvailableException will be raised. In
     * order to avoid this exception call decompose() method first.
     * @param b Parameters array that determines a linear system of equations.
     * Provided array must have the same length as number of rows on provided
     * input matrix for Singular Value decomposition.
     * @param singularValueThreshold Threshold used to determine whether a
     * singular value is negligible or not.
     * @return Vector containing least squares solution of linear system of
     * equations for provided parameters array b.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing SingularValue decomposition.
     * To avoid this exception call decompose() method first.
     * @throws WrongSizeException Exception thrown if provided parameters array
     * (b) does not have the same length as number of rows on input matrix being
     * Singular Value decomposed.
     * @throws IllegalArgumentException Exception thrown if provided singular
     * value threshold is lower than minimum allowed value (MIN_THRESH).
     * @see #decompose()
     */
    public double[] solve(double[] b, double singularValueThreshold)
            throws NotAvailableException, WrongSizeException, 
            IllegalArgumentException {
        int n = inputMatrix.getColumns();
        
        double[] x = new double[n];
        solve(b, singularValueThreshold, x);
        return x;
    }

    /**
     * Solves a linear system of equations of the following form: A * X = B
     * using the pseudo-inverse to find a least squares solution.
     * Where A i s the input matrix provided for Singular Value decomposition,
     * X is the solution to the system of equations, and B is the parameters
     * array.
     * Note: This method can be reused for different b arrays without having
     * to recompute Singular Value decomposition on the same input matrix.
     * Note: Provided b array must have the same length as the number of rows
     * on provided input matrix A, otherwise a WrongSizeException will be
     * raised.
     * Note: In order to execute this method, a Singular Value decomposition
     * must be available, otherwise a NotAvailableException will be raised. In
     * order to avoid this exception call decompose() method first.
     * Note: this method performs same actions as double[] solve(double[], 
     * double) except that singular value threshold is automatically computed by
     * taking into account input matrix size, maximal singular value and machine
     * precision.
     * This threshold is good enough for most situations, and hence we discourage
     * setting it manually.
     * @param b Parameters array that determines a linear system of equations.
     * Provided array must have the same length as number of rows on provided
     * input matrix for Singular Value decomposition.
     * @return Vector containing least squares solution of linear system of
     * equations for provided parameters array b.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing SingularValue decomposition.
     * To avoid this exception call decompose() method first.
     * @throws WrongSizeException Exception thrown if provided parameters array
     * (b) does not have the same length as number of rows on input matrix being
     * Singular Value decomposed.
     * @see #decompose()
     */    
    public double[] solve(double[] b) throws NotAvailableException,
            WrongSizeException {
        return solve(b, getNegligibleSingularValueThreshold());
    }
    
    /**
     * This method is called internally by decompose(), and actually computes
     * Singular Value Decomposition.
     * However, algorithm implemented in this algorithm does not ensure that
     * singular values are ordered from maximal to minimal, and hence reorder()
     * method is called next within decompose() as well.
     * @throws NoConvergenceException Exception thrown if singular value 
     * estimation does not converge within provided number of maximum 
     * iterations.
     */
    private void internalDecompose() throws NoConvergenceException {
        int m = inputMatrix.getRows();
        int n = inputMatrix.getColumns();
		
		
	    boolean flag;
	    int i, its, j, jj, k, l = 0, nm = 0;
	    double anorm, c, f, g, h, s, scale, x, y, z;
	    double[] rv1 = new double[n];
		
	    //Householder reduction to bidiagonal form
	    g = scale = anorm = 0.0;
	    for (i = 0; i < n; i++) {
            l = i + 2;
            rv1[i] = scale * g;
            g = s = scale = 0.0;
            if (i < m) {
		        for (k = i; k < m; k++) {
                    scale += Math.abs(u.getElementAt(k, i));
                }
                if (scale != 0.0) {
                    for (k = i; k < m; k++) {
                        u.setElementAt(k, i, u.getElementAt(k,i) / scale);
                        s += Math.pow(u.getElementAt(k,i), 2.0);
                    }
                    f = u.getElementAt(i,i);
                    g = -sign(Math.sqrt(s), f);
                    h = f * g - s;
                    u.setElementAt(i,i, f - g);
                    for (j = l - 1; j < n; j++) {
                        for (s = 0.0, k = i; k < m; k++) {
                            s += u.getElementAt(k, i) * u.getElementAt(k, j);
                        }
                        f = s / h;
                        for (k = i; k < m; k++) {
                            u.setElementAt(k, j, u.getElementAt(k, j) +
                                    f * u.getElementAt(k, i));
                        }
                    }
                    for (k = i; k < m; k++) {
                        u.setElementAt(k, i, u.getElementAt(k, i) * scale);
                    }
		        }
            }
            w[i] = scale * g;
            g = s = scale = 0.0;
            if (i + 1 <= m && i + 1 != n) {
                for (k = l - 1; k < n; k++) {
                    scale += Math.abs(u.getElementAt(i, k));
                }
                if (scale != 0.0) {
                    for (k = l - 1; k < n; k++) {
                        u.setElementAt(i, k, u.getElementAt(i,k) / scale);
                        s += Math.pow(u.getElementAt(i,k), 2.0);
                    }
                    f = u.getElementAt(i, l - 1);
                    g = -sign(Math.sqrt(s), f);
                    h = f * g - s;
                    u.setElementAt(i, l - 1, f - g);
                    for (k = l - 1; k < n; k++) {
                        rv1[k] = u.getElementAt(i, k) / h;
                    }
                    for (j = l - 1; j < m; j++) {
                        for (s = 0.0, k = l - 1; k < n; k++) {
                            s += u.getElementAt(j, k) * u.getElementAt(i, k);
                        }
                        for (k = l - 1; k < n; k++) {
                            u.setElementAt(j, k,
                                    u.getElementAt(j, k) + s * rv1[k]);
                        }
                    }
                    for (k = l - 1; k < n; k++) {
                        u.setElementAt(i, k, u.getElementAt(i, k) * scale);
                    }
                }
            }
            anorm = Math.max(anorm, Math.abs(w[i]) + Math.abs(rv1[i]));			
	    }
		
	    //Accumulation of right-hand transformations
        for (i = n - 1; i >= 0; i--) {
            if (i < (n - 1)) {
                if (g != 0.0) {
                    //Double division to avoid possible underflow.
                    for (j = l; j < n; j++) {
                        v.setElementAt(j, i, u.getElementAt(i, j) /
                                u.getElementAt(i, l) / g);
                    }
                    for (j = l; j < n; j++) {
                        for (s = 0.0, k = l; k < n; k++) {
                            s += u.getElementAt(i, k) * v.getElementAt(k, j);
                        }
                        for (k = l; k < n; k++) {
                            v.setElementAt(k, j, v.getElementAt(k, j) +
                                    s * v.getElementAt(k, i));
                        }
                    }
		        }
		        for (j = l; j < n; j++) {
                    v.setElementAt(i,j, 0.0);
                    v.setElementAt(j,i, 0.0);
                }
            }
            v.setElementAt(i,i, 1.0);
            g = rv1[i];
            l = i;			
	    }
		
	    //Accumulation of left-hand transformations
	    for (i = Math.min(m,n) - 1; i >= 0; i--) {
            l = i + 1;
            g = w[i];
            for (j = l; j < n; j++) {
                u.setElementAt(i,j, 0.0);
            }
            if (g != 0.0) {
                g = 1.0 / g;
                for (j = l; j < n; j++) {
                    for (s = 0.0, k = l; k < m; k++) {
                        s += u.getElementAt(k,i) * u.getElementAt(k,j);
                    }
                    f = (s / u.getElementAt(i,i)) * g;
                    for (k = i; k < m; k++) {
                        u.setElementAt(k,j, u.getElementAt(k,j) +
                                f * u.getElementAt(k,i));
                    }
                }
                for (j = i; j < m; j++) {
                    u.setElementAt(j, i, u.getElementAt(j, i) * g);
                }
            } else {
                for (j = i; j < m; j++) {
                    u.setElementAt(j,i, 0.0);
                }
            }
            u.setElementAt(i,i, u.getElementAt(i,i) + 1.0);
	    }
		
	    //Diagonalization of the bidiagonal form: Loop over singular values and
	    //over allowed iterations.
	    for (k = n - 1; k >= 0; k--) {
            for (its = 0; its < maxIters; its++) {
		        flag = true;
		        //Test for splitting
		        //Note that rrv1[0] is always zero
		        for (l = k; l >= 0; l--) {
                    nm = l - 1;
                    if (l == 0 || Math.abs(rv1[l]) <= eps * anorm) {
			            flag = false;
			            break;
                    }
                    if (Math.abs(w[nm]) <= eps * anorm) {
                        break;
                    }
		        }
		        //Cancellation of rv1[0] if l > 1
		        if (flag) {
                    c = 0.0;
                    s = 1.0;
                    for (i = l; i < k + 1; i++) {
			            f = s * rv1[i];
			            rv1[i] = c * rv1[i];
			            if (Math.abs(f) <= eps * anorm) {
			                break;
                        }
			            g = w[i];
			            h = pythag(f, g);
			            w[i] = h;
                        h = 1.0 / h;
			            c = g * h;
			            s = -f * h;
			            for (j = 0; j < m; j++) {
                            y = u.getElementAt(j,nm);
                            z = u.getElementAt(j,i);
                            u.setElementAt(j,nm, y * c + z * s);
                            u.setElementAt(j,i, z * c - y * s);
			            }
                    }
		        }
		        z = w[k];
		        //Convergence.
		        if (l == k) {
                    //Singular value is made nonnegative
                    if (z < 0.0) {
			            w[k] = -z;
			            for (j = 0; j < n; j++) {
                            v.setElementAt(j, k, -v.getElementAt(j, k));
                        }
                    }
                    break;
		        }
		        if (its == maxIters - 1) {
		            throw new NoConvergenceException();
                }
				
		        //Shift from bottom 2-by-2 minor.
		        x = w[l];
		        nm = k - 1;
		        y = w[nm];
		        g = rv1[nm];
		        h = rv1[k];
		        f = ((y - z) * (y + z) + (g - h) * (g + h)) / (2.0 * h * y);
		        g = pythag(f, 1.0);
		        f = ((x - z) * (x + z) + h * ((y / (f + sign(g, f))) - h)) / x;
		        c = s = 1.0;
		        //Next QR transformation
		        for (j = l; j <= nm; j++) {
                    i = j + 1;
                    g = rv1[i];
                    y = w[i];
                    h = s * g;
                    g = c * g;
                    z = pythag(f, h);
                    rv1[j] = z;
                    c = f / z;
                    s = h / z;
                    f = x * c + g * s;
                    g = g * c - x * s;
                    h = y * s;
                    y *= c;
                    for (jj = 0; jj < n; jj++) {
			            x = v.getElementAt(jj,j);
			            z = v.getElementAt(jj,i);
			            v.setElementAt(jj,j, x * c + z * s);
			            v.setElementAt(jj,i, z * c - x * s);
                    }
                    z = pythag(f, h);
                    //Rotation can be arbitrary if z = 0
                    w[j] = z;
                    if (z != 0.0) {
			            z = 1.0 / z;
			            c = f * z;
			            s = h * z;
                    }
                    f = c * g + s * y;
                    x = c * y - s * g;
                    for (jj = 0; jj < m; jj++) {
			            y = u.getElementAt(jj,j);
                        z = u.getElementAt(jj,i);
			            u.setElementAt(jj,j, y * c + z * s);
			            u.setElementAt(jj,i, z * c - y * s);
                    }
		        }
		        rv1[l] = 0.0;
		        rv1[k] = f;
		        w[k] = x;
            }
        }
    }
    
    /**
     * Reorders singular values from maximal to minimal, and also reorders
     * columns and rows of U and V to ensure that Singular Value Decomposition
     * still remains valid.
     */
    private void reorder() {
        int m = inputMatrix.getRows();
	    int n = inputMatrix.getColumns();
		
	    int i, j, k, s, inc = 1;
	    double sw;
	    double[] su = new double[m];
	    double[] sv = new double[n];
		
	    do {
	        inc *= 3; inc++;
	    } while(inc <= n);
        do {
            inc /= 3;
            for (i = inc; i < n; i++) {
                sw = w[i];
		        for (k = 0; k < m; k++) {
                    su[k] = u.getElementAt(k, i);
                }
		        for (k = 0; k < n; k++) {
                    sv[k] = v.getElementAt(k, i);
                }
		        j = i;
		        while (w[j - inc] < sw) {
                    w[j] = w[j - inc];
                    for (k = 0; k < m; k++) {
                        u.setElementAt(k, j, u.getElementAt(k, j - inc));
                    }
                    for (k = 0; k < n; k++) {
                        v.setElementAt(k, j, v.getElementAt(k, j - inc));
                    }
                    j -= inc;
                    if (j < inc) {
                        break;
                    }
                }
                w[j] = sw;
                for (k = 0; k < m; k++) {
                    u.setElementAt(k, j, su[k]);
                }
                for (k = 0; k < n; k++) {
                    v.setElementAt(k, j, sv[k]);
                }
            }
	    } while (inc > 1);
		
	    for (k = 0; k < n; k++) {
            s = 0;
            for (i = 0; i < m; i++) {
                if (u.getElementAt(i, k) < 0.0) {
                    s++;
                }
            }
            for (j = 0; j < n; j++) {
                if (v.getElementAt(j, k) < 0.0) {
                    s++;
                }
            }
            if (s > (m + n) / 2) {
		        for (i = 0; i < m; i++) {
                    u.setElementAt(i, k, -u.getElementAt(i, k));
                }
		        for (j = 0; j < n; j++) {
                    v.setElementAt(j, k, -v.getElementAt(j, k));
                }
            }
	    }
    }
    
    /**
     * Sets threshold to be used to determine whether a singular value is
     * negligible or not.
     * This threshold can be used to consider a singular value as zero or not,
     * since small singular values might appear in places where they should be
     * zero because of rounding errors and machine precision.
     * Singular values considered as zero determine aspects such as rank,
     * nullability, nullspace or range space.
     * @param threshold Threshold to be used to determine whether a singular
     * value is negligible or not.
     */
    private void setNegligibleSingularValueThreshold(double threshold) {
        tsh = threshold;
    }
    
    /**
     * Computes norm of a vector of 2 components 'a' and 'b' as
     * sqrt(pow(a, 2.0)  + pow(b, 2.0)) without destructive underflow or
     * overflow, that is when a or b are close to maximum or minimum values
     * allowed by machine precision, computing the previous expression might
     * lead to highly inaccurate results.
     * This method implements previous expression to avoid this effect as
     * much as possible and increase accuracy.
     * @param a 1st value
     * @param b 2nd value
     * @return Norm of (a, b).
     */
    private double pythag(double a, double b) {
        double absa, absb;
	    absa = Math.abs(a);
	    absb = Math.abs(b);
		
	    if (absa > absb) {
	        return absa * Math.sqrt(1.0 + (absb/absa)*(absb/absa));
        } else {
	        return (absb == 0.0 ?
                    0.0 : absb * Math.sqrt(1.0 + (absa/absb)*(absa/absb)));
        }
    }
    
    /**
     * Returns a or -a depending on b sign. If b is positive, this method
     * returns a, otherwise it returns -a
     * @param a 1st value
     * @param b 2nd value
     * @return a or -a depending on b sign.
     */
    private double sign(double a, double b) {
        return (b >= 0.0 ? (a >= 0.0 ? a : -a) : (a >= 0.0 ? -a : a));
    }
}
