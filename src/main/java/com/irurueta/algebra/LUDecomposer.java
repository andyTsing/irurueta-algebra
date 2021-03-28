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
 * This class allows decomposition of matrices using LU decomposition, which
 * consists on retrieving two triangular matrices (lower triangular and upper
 * triangular) as a decomposition of provided input matrix.
 * In other words, if input matrix is A, then: A = L * U, where L is lower
 * triangular matrix and U is upper triangular matrix.
 * LU decomposition is a useful and fast way of solving systems of linear
 * equations, computing determinants or finding whether a matrix is singular.
 */
public class LUDecomposer extends Decomposer {

    /**
     * Constant defining default round error when determining singularity of
     * matrices. This value is zero by default.
     */
    public static final double DEFAULT_ROUND_ERROR = 0.0;

    /**
     * Constant defining minimum allowed round error value when determining
     * singularity of matrices.
     */
    public static final double MIN_ROUND_ERROR = 0.0;

    /**
     * Internal matrix containing resuts of decomposition.
     */
    private Matrix lu;

    /**
     * Internal array containing pivotings after decomposition.
     */
    int[] piv;

    /**
     * Member containing pivot sign after decomposition.
     */
    int pivSign;

    /**
     * Constructor of this class.
     */
    public LUDecomposer() {
        super();
        lu = null;
        piv = null;
    }

    /**
     * Constructor of this class.
     *
     * @param inputMatrix Reference to input matrix to be decomposed
     */
    public LUDecomposer(final Matrix inputMatrix) {
        super(inputMatrix);
        lu = null;
        piv = null;
    }

    /**
     * Returns decomposer type corresponding to LU decomposition
     *
     * @return Decomposer type
     */
    @Override
    public DecomposerType getDecomposerType() {
        return DecomposerType.LU_DECOMPOSITION;
    }

    /**
     * Sets reference to input matrix to be decomposed.
     *
     * @param inputMatrix Reference to input matrix to be decomposed.
     * @throws LockedException Exception thrown if attempting to call this
     *                         method while this instance remains locked.
     */
    @Override
    public void setInputMatrix(final Matrix inputMatrix) throws LockedException {
        super.setInputMatrix(inputMatrix);
        lu = null;
        piv = null;
    }

    /**
     * Returns boolean indicating whether decomposition has been computed and
     * results can be retrieved.
     * Attempting to retrieve decomposition results when not available, will
     * probably raise a NotAvailableException
     *
     * @return Boolean indicating whether decomposition has been computed and
     * results can be retrieved.
     */
    @Override
    public boolean isDecompositionAvailable() {
        return lu != null;
    }

    /**
     * This method computes LU matrix decomposition, which consists on
     * retrieving two triangular matrices (Lower triangular and Upper
     * triangular) as a decomposition of provided input matrix.
     * In other words, if input matrix is A, then: A = L * U
     * Note: During execution of this method, this instance will be locked,
     * and hence attempting to set some parameters might raise a
     * LockedException.
     * Note: After execution of this method, LU decomposition will be
     * available and operations such as retrieving L and U matrices or
     * computing determinants among others will be able to be done.
     * Attempting to call any of such operations before calling this method
     * will raise a NotAvailableException because they require computation of
     * LU decomposition first.
     *
     * @throws NotReadyException   Exception thrown if attempting to call this
     *                             method when this instance is not ready (i.e. no input matrix has been
     *                             provided).
     * @throws LockedException     Exception thrown if attempting to call this
     *                             method when this instance is not ready (i.e. no input matrix has been
     *                             provided).
     * @throws DecomposerException Exception thrown if for any reason
     *                             decomposition fails while executing, like when convergence of results
     *                             can not be obtained, etc.
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

        final int rows = inputMatrix.getRows();
        final int columns = inputMatrix.getColumns();

        if (columns > rows) {
            throw new DecomposerException();
        }

        locked = true;

        // copy matrix contents
        lu = new Matrix(inputMatrix);

        piv = new int[rows];
        for (int i = 0; i < rows; i++) {
            piv[i] = i;
        }

        pivSign = 1;

        // Main loop
        for (int k = 0; k < columns; k++) {
            // Find pivot
            int p = k;
            for (int i = k + 1; i < rows; i++) {
                p = Math.abs(lu.getElementAt(i, k)) >
                        Math.abs(lu.getElementAt(p, k)) ? i : p;
            }
            // Exchange if necessary
            if (p != k) {
                for (int j = 0; j < columns; j++) {
                    final double t = lu.getElementAt(p, j);
                    lu.setElementAt(p, j, lu.getElementAt(k, j));
                    lu.setElementAt(k, j, t);
                }
                final int t = piv[p];
                piv[p] = piv[k];
                piv[k] = t;
                pivSign = -pivSign;
            }
            // Compute multipliers and eliminate k-th column
            if (lu.getElementAt(k, k) != 0.0) {
                for (int i = k + 1; i < rows; i++) {
                    lu.setElementAt(i, k, lu.getElementAt(i, k) /
                            lu.getElementAt(k, k));
                    for (int j = k + 1; j < columns; j++) {
                        lu.setElementAt(i, j, lu.getElementAt(i, j) -
                                lu.getElementAt(i, k) * lu.getElementAt(k, j));
                    }
                }
            }
        }

        locked = false;
    }

    /**
     * Return boolean indicating whether provided input matrix is singular
     * or not after computing LU decomposition. Returns true if singular and
     * false otherwise.
     * A matrix is defined as singular if its determinant is zero, hence
     * provided input matrix must be square (even though LU decomposition can be
     * computed for non-square matrices), otherwise a WrongSizeException will
     * be raised when calling this method. LU decomposition can be used to avoid
     * determinant computation by means of pivoting, because LU decomposition
     * obtains two triangular matrices, and the determinant of a triangular
     * matrix is just the product of the diagonal elements. Hence, if any
     * element on the diagonal of LU decomposition is zero, determinant will be
     * zero and input matrix will be singular.
     *
     * @return Boolean indicating whether provided input matrix is singular
     * or not.
     * @throws NotAvailableException    Exception thrown if attempting to call this
     *                                  method before computing LU decomposition. To avoid this exception call
     *                                  decompose() method first.
     * @throws WrongSizeException       Exception thrown if attempting to call this
     *                                  method using a non-square input matrix.
     * @throws IllegalArgumentException Exception thrown if provided rounding
     *                                  error is lower than minimum allowed value (MIN_ROUND_ERROR)
     * @see #decompose()
     */
    public boolean isSingular() throws NotAvailableException,
            WrongSizeException {
        return isSingular(DEFAULT_ROUND_ERROR);
    }

    /**
     * Return boolean indicating whether provided input matrix is singular
     * or not after computing LU decomposition. Returns true if singular and
     * false otherwise.
     * A matrix is defined as singular if its determinant is zero, hence
     * provided input matrix must be square (even though LU decomposition can be
     * computed for non-square matrices), otherwise a WrongSizeException will
     * be raised when calling this method. LU decomposition can be used to avoid
     * determinant computation by means of pivoting, because LU decomposition
     * obtains two triangular matrices, and the determinant of a triangular
     * matrix is just the product of the diagonal elements. Hence, if any
     * element on the diagonal of LU decomposition is zero, determinant will be
     * zero and input matrix will be singular.
     *
     * @param roundingError Determines the amount of margin given to determine
     *                      whether a matrix is singular or not due to rounding errors. If not
     *                      provided, by default rounding error is set to zero, but this value can be
     *                      relaxed if needed.
     * @return Boolean indicating whether provided input matrix is singular
     * or not.
     * @throws NotAvailableException    Exception thrown if attempting to call this
     *                                  method before computing LU decomposition. To avoid this exception call
     *                                  decompose() method first.
     * @throws WrongSizeException       Exception thrown if attempting to call this
     *                                  method using a non-square input matrix.
     * @throws IllegalArgumentException Exception thrown if provided rounding
     *                                  error is lower than minimum allowed value (MIN_ROUND_ERROR)
     * @see #decompose()
     */
    public boolean isSingular(final double roundingError)
            throws NotAvailableException, WrongSizeException {

        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        if (roundingError < MIN_ROUND_ERROR) {
            throw new IllegalArgumentException();
        }

        // A matrix is singular when its determinant is zero. Hence, in order to
        // compute singularity matrix must be square
        final int rows = inputMatrix.getRows();
        final int columns = inputMatrix.getColumns();
        if (rows != columns) {
            throw new WrongSizeException();
        }

        // Since we have computed LU decomposition into triangular matrices. The
        // determinant of a triangular matrix is the product of its diagonal
        // elements. Hence, if any element in the diagonal is zero, input matrix
        // will be singular.
        for (int j = 0; j < columns; j++) {
            if (lu.getElementAt(j, j) == 0.0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Fills provided matrix instance with the Lower triangular matrix
     * resulting from LU decomposition before correcting any possible pivots.
     * Hence, this matrix is only ensured to be Lower triangular.
     * In other words, this matrix does not ensure the product A = L * U, to
     * achieve this, we need to apply pivot correction.
     * A pivot corrected version of this matrix can be obtained by calling
     * method getL().
     *
     * @param pivottedL Lower triangular matrix.
     * @throws NotAvailableException Exception thrown if attempting to call
     *                               this method before computing LU decomposition. To avoid this exception
     *                               call decompose() method first.
     * @see #getL()
     * @see #decompose()
     */
    public void getPivottedL(final Matrix pivottedL) throws NotAvailableException {

        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }

        final int rows = lu.getRows();
        final int columns = lu.getColumns();

        if (pivottedL.getRows() != rows || pivottedL.getColumns() != columns) {
            try {
                pivottedL.resize(rows, columns);
            } catch (final WrongSizeException ignore) {
                // never happens
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i > j) {
                    pivottedL.setElementAt(i, j, lu.getElementAt(i, j));
                } else if (i == j) {
                    pivottedL.setElementAt(i, j, 1.0);
                } else {
                    pivottedL.setElementAt(i, j, 0.0);
                }
            }
        }
    }

    /**
     * Returns a new matrix instance containing the Lower triangular matrix
     * resulting from LU decomposition before correcting any possible pivots.
     * Hence, this matrix is only ensured to be Lower triangular.
     * In other words, this matrix does not ensure the product A = L * U, to
     * achieve this, we need to apply pivot correction.
     * A pivot corrected version of this matrix can be obtained by calling
     * method getL().
     *
     * @return Lower triangular matrix.
     * @throws NotAvailableException Exception thrown if attempting to call
     *                               this method before computing LU decomposition. To avoid this exception
     *                               call decompose() method first.
     * @see #getL()
     * @see #decompose()
     */
    public Matrix getPivottedL() throws NotAvailableException {

        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }

        final int rows = lu.getRows();
        final int columns = lu.getColumns();

        Matrix out = null;
        try {
            out = new Matrix(rows, columns);
        } catch (final WrongSizeException ignore) {
            // never happens
        }
        getPivottedL(out);
        return out;
    }

    /**
     * Fills provided matrix instance with the pivot corrected Lower
     * triangular matrix resulting from LU decomposition for provided input
     * matrix.
     * Since this matrix is pivot corrected, it might not be completely
     * triangular, except for some row pivotting.
     * Notice that LU decomposition obtains matrices in the form of
     * A = L * U, where A is provided input matrix, L is lower triangular
     * matrix and U is upper triangular matrix.
     *
     * @param l the Lower triangular matrix resulting from LU
     *          decomposition for provided input matrix.
     * @throws NotAvailableException Exception thrown if attempting to call
     *                               this method before computing LU decomposition. To avoid this exception
     *                               call decompose() method first.
     * @see #decompose()
     */
    public void getL(final Matrix l) throws NotAvailableException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }

        final int rows = lu.getRows();
        final int columns = lu.getColumns();

        if (l.getRows() != rows || l.getColumns() != columns) {
            try {
                l.resize(rows, columns);
            } catch (final WrongSizeException ignore) {
                // never happens
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i > j) {
                    l.setElementAt(piv[i], j, lu.getElementAt(i, j));
                } else if (i == j) {
                    l.setElementAt(piv[i], j, 1.0);
                } else {
                    l.setElementAt(piv[i], j, 0.0);
                }
            }
        }
    }

    /**
     * Returns a new matrix instance containing the pivot corrected Lower
     * triangular matrix resulting from LU decomposition for provided input
     * matrix.
     * Since this matrix is pivot corrected, it might not be completely
     * triangular, except for some row pivotting.
     * Notice that LU decomposition obtains matrices in the form of
     * A = L * U, where A is provided input matrix, L is lower triangular
     * matrix and U is upper triangular matrix.
     *
     * @return Returns the Lower triangular matrix resulting from LU
     * decomposition for provided input matrix.
     * @throws NotAvailableException Exception thrown if attempting to call
     *                               this method before computing LU decomposition. To avoid this exception
     *                               call decompose() method first.
     * @see #decompose()
     */
    public Matrix getL() throws NotAvailableException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }

        final int rows = lu.getRows();
        final int columns = lu.getColumns();
        Matrix out = null;
        try {
            out = new Matrix(rows, columns);
        } catch (final WrongSizeException ignore) {
            //never happens
        }
        getL(out);
        return out;
    }

    /**
     * Fills provided matrix instance with the Upper triangular matrix
     * resulting from LU decomposition for provided input matrix.
     * Notice that LU decomposition obtains matrices in the form A = L * U,
     * where A is provided input matrix, L is lower triangular matrix and U
     * is upper triangular matrix.
     *
     * @param u Returns the Upper triangular matrix resulting from LU
     *          decomposition for provided input matrix.
     * @throws NotAvailableException Exception thrown if attempting to call
     *                               this method before computing LU decomposition. To avoid this exception
     *                               call decompose() method first.
     * @see #decompose()
     */
    public void getU(final Matrix u) throws NotAvailableException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }

        final int columns = lu.getColumns();

        if (u.getRows() != columns || u.getColumns() != columns) {
            try {
                u.resize(columns, columns);
            } catch (final WrongSizeException ignore) {
                // never happens
            }
        }
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < columns; j++) {
                if (i <= j) {
                    u.setElementAt(i, j, lu.getElementAt(i, j));
                } else {
                    u.setElementAt(i, j, 0.0);
                }
            }
        }
    }

    /**
     * Returns a new matrix instance containing the Upper triangular matrix
     * resulting from LU decomposition for provided input matrix.
     * Notice that LU decomposition obtains matrices in the form A = L * U,
     * where A is provided input matrix, L is lower triangular matrix and U
     * is upper triangular matrix.
     *
     * @return Returns the Upper triangular matrix resulting from LU
     * decomposition for provided input matrix.
     * @throws NotAvailableException Exception thrown if attempting to call
     *                               this method before computing LU decomposition. To avoid this exception
     *                               call decompose() method first.
     * @see #decompose()
     */
    public Matrix getU() throws NotAvailableException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }

        final int columns = lu.getColumns();
        Matrix out = null;
        try {
            out = new Matrix(columns, columns);
        } catch (final WrongSizeException ignore) {
            // never happens
        }
        getU(out);
        return out;
    }

    /**
     * Returns pivot permutation vector.
     *
     * @return Pivot permutation vector.
     * @throws NotAvailableException Exception thrown if attempting to call
     *                               this method before computing LU decomposition. To avoid this exception
     *                               call decompose() method first.
     */
    public int[] getPivot() throws NotAvailableException {

        if (!this.isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        return piv;
    }

    /**
     * Returns determinant of provided input matrix using LU decomposition as
     * means to obtian it.
     * Provided input matrix must be square (even though LU decomposition can
     * be computed for non-square matrices), otherwise a WrongSizeException will
     * be raised when calling this method.
     * LU decomposition can be used to avoid determinant computation using other
     * slow methods, because LU decomposition obtains two triangular matrices,
     * and the determinant of a triangular matris is just the product of the
     * diagonal elements.
     * Since the determinant of a matrix product is the product of determinants,
     * then determinant of input matrix can be computed as the product of
     * determinants of L and U.
     * Finally, since L has ones on its diagonal, its determinant will be +-1,
     * depending on the amount of pivots done on L, and determinant of U will be
     * just the product of its diagonal elements.
     *
     * @return Determinant of provided input matrix.
     * @throws NotAvailableException Exception thrown if attempting to call
     *                               this method before computing LU decomposition. To avoid this exception
     *                               call decompose() method first.
     * @throws WrongSizeException    Exception thrown if attempting to call this
     *                               method using a non-square input matrix.
     * @see #decompose()
     */
    public double determinant() throws NotAvailableException,
            WrongSizeException {

        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }

        // Determinants can only be computed on squared matrices
        final int rows = inputMatrix.getRows();
        final int columns = inputMatrix.getColumns();

        if (rows != columns) {
            throw new WrongSizeException();
        }

        double d = pivSign;
        for (int j = 0; j < columns; j++) {
            d *= lu.getElementAt(j, j);
        }

        return d;
    }


    /**
     * Solves a linear system of equations of the following form:
     * A * X = B.
     * Where A is the input matrix provided for LU decomposition, X is the
     * solution to the system of equations, and B is the parameters
     * vector/matrix.
     * Note: This method can be reused for different b vectors/matrices without
     * having to recompute LU decomposition on the same input matrix.
     * Note: Provided b matrix must have the same number of rows as provided
     * input matrix A, otherwise a WrongSizeException will be raised.
     * Note: Provided input matrix A must be square, otherwise a
     * WrongSizeException will be raised as well.
     * Note: If provided input matrix A is singular, a SingularMatrixException
     * will be thrown.
     * Note: In order to execute this method, an LU decomposition must be
     * available, otherwise a NotAvailableException will be raised. In order
     * to avoid this exception call decompose() method first.
     * Note: DEFAULT_ROUND_ERROR is used as rounding error
     * Note: Solution of linear system of equations is stored in provided result
     * matrix
     *
     * @param b      Parameters matrix that determine a linear system of equations.
     *               Provided matrix must have the same number of rows as provided input
     *               matrix for LU decomposition. Besides, each column on parameters matrix
     *               will represent a new system of equations, whose solution will be returned
     *               on appropriate column as an output of this method.
     * @param result Matrix containing solution of linear system of equations on
     *               each column for each column of provided parameters matrix b.
     * @throws NotAvailableException    Exception thrown if attempting to call this
     *                                  method before computing LU decomposition. To avoid this exception call
     *                                  decompose() method first.
     * @throws WrongSizeException       Exception thrown if attempting to call this
     *                                  method using a non-square input matrix; or if provided parameters matrix
     *                                  (b) does not have the same number of rows as input matrix being LU
     *                                  decomposed.
     * @throws SingularMatrixException  Exception thrown if provided input matrix
     *                                  to be LU decomposed is singular. In this case linear system of equations
     *                                  cannot be solved.
     * @throws IllegalArgumentException Exception thrown if provided rounding
     *                                  error is lower than minimum allowed value (MIN_ROUND_ERROR).
     * @see #decompose()
     */
    public void solve(final Matrix b, final Matrix result) throws NotAvailableException,
            WrongSizeException, SingularMatrixException {
        solve(b, DEFAULT_ROUND_ERROR, result);
    }

    /**
     * Solves a linear system of equations of the following form:
     * A * X = B.
     * Where A is the input matrix provided for LU decomposition, X is the
     * solution to the system of equations, and B is the parameters
     * vector/matrix.
     * Note: This method can be reused for different b vectors/matrices without
     * having to recompute LU decomposition on the same input matrix.
     * Note: Provided b matrix must have the same number of rows as provided
     * input matrix A, otherwise a WrongSizeException will be raised.
     * Note: Provided input matrix A must be square, otherwise a
     * WrongSizeException will be raised as well.
     * Note: If provided input matrix A is singular, a SingularMatrixException
     * will be thrown.
     * Note: In order to execute this method, an LU decomposition must be
     * available, otherwise a NotAvailableException will be raised. In order
     * to avoid this exception call decompose() method first.
     * Note: Solution of linear system of equations is stored in provided result
     * matrix
     *
     * @param b             Parameters matrix that determine a linear system of equations.
     *                      Provided matrix must have the same number of rows as provided input
     *                      matrix for LU decomposition. Besides, each column on parameters matrix
     *                      will represent a new system of equations, whose solution will be returned
     *                      on appropriate column as an output of this method.
     * @param roundingError Determines the amount of margin given to determine
     *                      whether a matrix is singular or not due to rounding errors. If not
     *                      provided, by default rounding error is set to zero, but this value can
     *                      be relaxed if needed.
     * @param result        Matrix containing solution of linear system of equations on
     *                      each column for each column of provided parameters matrix b.
     * @throws NotAvailableException    Exception thrown if attempting to call this
     *                                  method before computing LU decomposition. To avoid this exception call
     *                                  decompose() method first.
     * @throws WrongSizeException       Exception thrown if attempting to call this
     *                                  method using a non-square input matrix; or if provided parameters matrix
     *                                  (b) does not have the same number of rows as input matrix being LU
     *                                  decomposed.
     * @throws SingularMatrixException  Exception thrown if provided input matrix
     *                                  to be LU decomposed is singular. In this case linear system of equations
     *                                  cannot be solved.
     * @throws IllegalArgumentException Exception thrown if provided rounding
     *                                  error is lower than minimum allowed value (MIN_ROUND_ERROR).
     * @see #decompose()
     */
    public void solve(final Matrix b, final double roundingError, final Matrix result)
            throws NotAvailableException, WrongSizeException,
            SingularMatrixException {

        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }

        if (b.getRows() != inputMatrix.getRows()) {
            throw new WrongSizeException();
        }

        if (roundingError < MIN_ROUND_ERROR) {
            throw new IllegalArgumentException();
        }

        // Copy right hand side with pivoting
        final int rows = lu.getRows();
        final int columns = lu.getColumns();
        final int colsB = b.getColumns();

        if (rows != columns) {
            throw new WrongSizeException();
        }
        if (isSingular(roundingError)) {
            throw new SingularMatrixException();
        }

        // resize result matrix if needed
        if (result.getRows() != columns || result.getColumns() != colsB) {
            result.resize(columns, colsB);
        }
        result.initialize(0.0);

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < colsB; j++) {
                result.setElementAt(i, j, b.getElementAt(piv[i], j));
            }
        }

        // Solve L * Y = b(piv, :)
        for (int k = 0; k < columns; k++) {
            for (int i = k + 1; i < columns; i++) {
                for (int j = 0; j < colsB; j++) {
                    result.setElementAt(i, j, result.getElementAt(i, j) -
                            result.getElementAt(k, j) * lu.getElementAt(i, k));
                }
            }
        }

        // Solve U * X = Y
        for (int k = columns - 1; k >= 0; k--) {
            for (int j = 0; j < colsB; j++) {
                result.setElementAt(k, j, result.getElementAt(k, j) /
                        lu.getElementAt(k, k));
            }
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < colsB; j++) {
                    result.setElementAt(i, j, result.getElementAt(i, j) -
                            result.getElementAt(k, j) * lu.getElementAt(i, k));
                }
            }
        }
    }

    /**
     * Solves a linear system of equations of the following form:
     * A * X = B.
     * Where A is the input matrix provided for LU decomposition, X is the
     * solution to the system of equations, and B is the parameters
     * vector/matrix.
     * Note: This method can be reused for different b vectors/matrices without
     * having to recompute LU decomposition on the same input matrix.
     * Note: Provided b matrix must have the same number of rows as provided
     * input matrix A, otherwise a WrongSizeException will be raised.
     * Note: Provided input matrix A must be square, otherwise a
     * WrongSizeException will be raised as well.
     * Note: If provided input matrix A is singular, a SingularMatrixException
     * will be thrown.
     * Note: In order to execute this method, an LU decomposition must be
     * available, otherwise a NotAvailableException will be raised. In order
     * to avoid this exception call decompose() method first.
     * Note: DEFAULT_ROUND_ERROR is used as rounding error
     *
     * @param b Parameters matrix that determine a linear system of equations.
     *          Provided matrix must have the same number of rows as provided input
     *          matrix for LU decomposition. Besides, each column on parameters matrix
     *          will represent a new system of equations, whose solution will be returned
     *          on appropriate column as an output of this method.
     * @return Matrix containing solution of linear system of equations on each
     * column for each column of provided parameters matrix b.
     * @throws NotAvailableException    Exception thrown if attempting to call this
     *                                  method before computing LU decomposition. To avoid this exception call
     *                                  decompose() method first.
     * @throws WrongSizeException       Exception thrown if attempting to call this
     *                                  method using a non-square input matrix; or if provided parameters matrix
     *                                  (b) does not have the same number of rows as input matrix being LU
     *                                  decomposed.
     * @throws SingularMatrixException  Exception thrown if provided input matrix
     *                                  to be LU decomposed is singular. In this case linear system of equations
     *                                  cannot be solved.
     * @throws IllegalArgumentException Exception thrown if provided rounding
     *                                  error is lower than minimum allowed value (MIN_ROUND_ERROR).
     * @see #decompose()
     */
    public Matrix solve(final Matrix b) throws NotAvailableException,
            WrongSizeException, SingularMatrixException {
        return solve(b, DEFAULT_ROUND_ERROR);
    }

    /**
     * Solves a linear system of equations of the following form:
     * A * X = B.
     * Where A is the input matrix provided for LU decomposition, X is the
     * solution to the system of equations, and B is the parameters
     * vector/matrix.
     * Note: This method can be reused for different b vectors/matrices without
     * having to recompute LU decomposition on the same input matrix.
     * Note: Provided b matrix must have the same number of rows as provided
     * input matrix A, otherwise a WrongSizeException will be raised.
     * Note: Provided input matrix A must be square, otherwise a
     * WrongSizeException will be raised as well.
     * Note: If provided input matrix A is singular, a SingularMatrixException
     * will be thrown.
     * Note: In order to execute this method, an LU decomposition must be
     * available, otherwise a NotAvailableException will be raised. In order
     * to avoid this exception call decompose() method first.
     *
     * @param b             Parameters matrix that determine a linear system of equations.
     *                      Provided matrix must have the same number of rows as provided input
     *                      matrix for LU decomposition. Besides, each column on parameters matrix
     *                      will represent a new system of equations, whose solution will be returned
     *                      on appropriate column as an output of this method.
     * @param roundingError Determines the amount of margin given to determine
     *                      whether a matrix is singular or not due to rounding errors. If not
     *                      provided, by default rounding error is set to zero, but this value can
     *                      be relaxed if needed.
     * @return Matrix containing solution of linear system of equations on each
     * column for each column of provided parameters matrix b.
     * @throws NotAvailableException    Exception thrown if attempting to call this
     *                                  method before computing LU decomposition. To avoid this exception call
     *                                  decompose() method first.
     * @throws WrongSizeException       Exception thrown if attempting to call this
     *                                  method using a non-square input matrix; or if provided parameters matrix
     *                                  (b) does not have the same number of rows as input matrix being LU
     *                                  decomposed.
     * @throws SingularMatrixException  Exception thrown if provided input matrix
     *                                  to be LU decomposed is singular. In this case linear system of equations
     *                                  cannot be solved.
     * @throws IllegalArgumentException Exception thrown if provided rounding
     *                                  error is lower than minimum allowed value (MIN_ROUND_ERROR).
     * @see #decompose()
     */
    public Matrix solve(final Matrix b, final double roundingError)
            throws NotAvailableException, WrongSizeException,
            SingularMatrixException {

        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }

        final int columns = lu.getColumns();
        final int colsB = b.getColumns();
        final Matrix out = new Matrix(columns, colsB);
        solve(b, roundingError, out);
        return out;
    }
}
