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
 * This decomposer computes economy QR decomposition, which is faster than 
 * typical QR decomposition.
 */
@SuppressWarnings({"WeakerAccess", "Duplicates"})
public class EconomyQRDecomposer extends Decomposer {
    
    /**
     * Constant defining default round error when determining full rank of
     * matrices. This value is zero by default
     */
    public static final double DEFAULT_ROUND_ERROR = 0.0;
    
    /**
     * Constant defining minimum allowed round error value when determining full 
     * rank of matrices.
     */
    public static final double MIN_ROUND_ERROR = 0.0;
    
    /**
     * Internal matrix containing results of decomposition.
     */
    private Matrix qr;
    
    /**
     * Internal array containing diagonal of R.
     */
    private double [] rDiag;

    /**
     * Constructor of this class.
     */
    public EconomyQRDecomposer() {
        super();        
        qr = null;
        rDiag = null;
    }
    
    /**
     * Constructor of this class.
     * @param inputMatrix Reference to input matrix to be decomposed.
     */
    public EconomyQRDecomposer(Matrix inputMatrix) {
        super(inputMatrix);
        qr = null;
        rDiag = null;
    }
    
    /**
     * Returns decomposer type corresponding to Economy QR decomposition.
     * @return Decomposer type.
     */
    @Override
    public DecomposerType getDecomposerType() {
        return DecomposerType.QR_ECONOMY_DECOMPOSITION;
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
        qr = null;
        rDiag = null;
    }    

    /**
     * Returns boolean indicating whether decomposition has been computed and
     * results can be retrieved.
     * Attempting to retrieve decomposition results when not available, will
     * probably raise a NotAvailableException.
     * @return Boolean indicating whether decomposition has been computed and
     * results can be retrieved.
     */        
    @Override
    public boolean isDecompositionAvailable() {
        return qr != null;
    }

    /**
     * This method computes QR matrix decomposition, which consists on factoring
     * provided input matrix into an orthogonal matrix (Q) and an upper 
     * triangular matrix (R).
     * In other words, if input matrix is A, then: A = Q * R
     * Note: During execution of this method, this instance will be locked.
     * Note: After execution of this method, QR decomposition will be available
     * and operations such as retrieving Q and R matrices or solving systems of
     * linear equations will be able to be done. Attempting to call any of such
     * operations before calling this method will raise a NotAvailableException
     * because they require computation of QR decomposition first.
     * @throws NotReadyException Exception thrown if attempting to call this
     * method when this instance is not ready (i.e. no input matrix has been
     * provided).
     * @throws LockedException Exception thrown if this decomposer is already
     * locked before calling this method. Notice that this method will actually
     * lock this instance while it is being executed.
     */
    @Override
    public void decompose() throws NotReadyException, LockedException {
        
        if (!isReady()) {
            throw new NotReadyException();
        }
        if (isLocked()) {
            throw new LockedException();
        }
        
        locked = true;
        
        int rows = inputMatrix.getRows();
        int columns = inputMatrix.getColumns();
        qr = inputMatrix.clone();
        
        rDiag = new double[columns];
        double nrm;
        double s;

        //Main loop
        for (int k = 0; k < columns; k++) {
            //Compute 2-norm of k-th column without under/overflow
            nrm = 0.0;
            
            for (int i = k; i < rows; i++) {
                nrm = Math.sqrt(nrm * nrm + 
                        Math.pow(qr.getElementAt(i, k), 2.0));
            }
            
            if (nrm != 0.0) {
                //Form k-th Householder vector
                if (qr.getElementAt(k, k) < 0.0) {
                    nrm = -nrm;
                }
                
                for (int i = k; i < rows; i++) {
                    qr.setElementAt(i, k, qr.getElementAt(i, k) /nrm);
                }
                qr.setElementAt(k, k, qr.getElementAt(k, k) + 1.0);
                
                //Apply transformation to remaining columns
                for (int j = k + 1; j < columns; j++) {
                    s = 0.0;
                    for (int i = k; i < rows; i++) {
                        s += qr.getElementAt(i, k) * qr.getElementAt(i, j);
                    }
                    s = -s / qr.getElementAt(k, k);
                    for (int i = k; i < rows; i++) {
                        qr.setElementAt(i, j, qr.getElementAt(i, j) + 
                                s * qr.getElementAt(i, k));
                    }
                }
            }
            
            rDiag[k] = -nrm;
        }
        
        locked = false;
    }
    
    /**
     * Returns boolean indicating whether provided input matrix has full rank or
     * not.
     * Squared matrices having full rank also have determinant different from
     * zero.
     * Note: Because of rounding errors, testing whether a matrix has full rank
     * or not, might obtain unreliable results especially for non-square 
     * matrices. In such cases matrices usually tend to be considered as full
     * rank even when they are not.
     * @return Boolean indicating whether provided input matrix has full rank or
     * not.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing QR decomposition. To avoid this exception call
     * decompose() method first.
     * @throws WrongSizeException Exception thrown if provided rounding error is
     * lower than minimum allowed value (MIN_ROUND_ERROR).
     * @see #decompose()
     */
    public boolean isFullRank() throws NotAvailableException, 
            WrongSizeException {
        return isFullRank(DEFAULT_ROUND_ERROR);
    }
    
    /**
     * Returns boolean indicating whether provided input matrix has full rank or
     * not.
     * Squared matrices having full rank also have determinant different from
     * zero.
     * Note: Because of rounding errors, testing whether a matrix has full rank
     * or not, might obtain unreliable results especially for non-square 
     * matrices. In such cases matrices usually tend to be considered as full
     * rank even when they are not.
     * @param roundingError Determines the amount of margin given to determine
     * whether a matrix has full rank or not due to rounding errors. If not
     * provided, by default rounding error is set to zero, but this value can
     * be relaxed if needed.
     * @return Boolean indicating whether provided input matrix has full rank or
     * not.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing QR decomposition. To avoid this exception call
     * decompose() method first.
     * @throws WrongSizeException Exception thrown if provided input matrix has
     * less rows than columns.
     * @throws IllegalArgumentException Exception thrown if provided rounding 
     * error is lower than minimum allowed value (MIN_ROUND_ERROR).
     * @see #decompose()
     */
    public boolean isFullRank(double roundingError) 
            throws NotAvailableException, WrongSizeException {
        
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        if (roundingError < MIN_ROUND_ERROR) {
            throw new IllegalArgumentException();
        }
        
        int rows = inputMatrix.getRows();
        int columns = inputMatrix.getColumns();
        
        if (rows < columns) {
            throw new WrongSizeException();
        }
        
        for (int j = 0; j < columns; j++) {
            if (Math.abs(rDiag[j]) < roundingError) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Computes the Householder vectors and store them in provided matrix.
     * Provided matrix will be resized if needed
     * @param h Matrix where Householder vectors will be stored
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing QR decoposition. To avoid this exception call
     * decompose() method first.
     * @see #decompose()
     */
    public void getH(Matrix h) throws NotAvailableException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        
        int rows = inputMatrix.getRows();
        int columns = inputMatrix.getColumns();
        if (h.getRows() != rows || h.getColumns() != columns) {
            try {
                h.resize(rows, columns);
            } catch (WrongSizeException ignore) {
                //never happens
            }
        }
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i >= j) {
                    h.setElementAt(i, j, qr.getElementAt(i, j));
                } else {
                    h.setElementAt(i, j, 0.0);
                }
            }
        }
    }
    
    /**
     * Returns the Householder vectors.
     * @return Lower trapezoidal matrix whose columns define the reflections.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing QR decomposition. To avoid this exception call
     * decompose() method first.
     * @see #decompose()
     */
    public Matrix getH() throws NotAvailableException {
        int rows = inputMatrix.getRows();
        int columns = inputMatrix.getColumns();
        Matrix out = null;
        try {
            out = new Matrix(rows, columns);
        } catch (WrongSizeException ignore) {
            //never happens
        }
        getH(out);
        return out;
    }
    
    /**
     * Computes upper triangular factor matrix and stores it into provided 
     * matrix.
     * QR decomposition decomposes input matrix into Q (orthogonal matrix) and 
     * R, which is an upper triangular matrix.
     * @param r Upper triangular factor matrix
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing QR decomposition. To avoid this exception call
     * decompose() method first.
     * @see #decompose()
     */    
    public void getR(Matrix r) throws NotAvailableException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        int columns = inputMatrix.getColumns();
        int rows = inputMatrix.getRows();
        
        if (r.getRows() != columns || r.getColumns() != columns) {
            try {
                r.resize(columns, columns);
            } catch (WrongSizeException ignore) {
                //never happens
            }
        }
        
        for (int i = 0; i < columns; i++) {
            if (i < rows) {
                for (int j = 0; j < columns; j++) {
                    if (i < j) {
                        r.setElementAt(i, j, qr.getElementAt(i, j));
                    } else if (i == j) {
                        r.setElementAt(i, j, rDiag[i]);
                    } else {
                        r.setElementAt(i, j, 0.0);
                    }
                }
            } else {
                for (int j = 0; j < columns; j++) {
                    r.setElementAt(i, j, 0.0);
                }
            }
        }      
    }
    
    /**
     * Return upper triangular factor matrix.
     * QR decomposition decomposes input matrix into Q (orthogonal matrix) and 
     * R, which is an upper triangular matrix.
     * @return Upper triangular factor matrix
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing QR decomposition. To aviod this exception call
     * decompose() method first.
     * @see #decompose()
     */
    public Matrix getR() throws NotAvailableException {
        int columns = inputMatrix.getColumns();
        Matrix out = null;
        try {
            out = new Matrix(columns, columns);
        } catch (WrongSizeException ignore) {
            //never happens
        }
        getR(out);
        return out;
    }
    
    /**
     * Computes the economy-sized orthogonal factor matrix and stores it into
     * provided matrix.
     * QR decomposition decomposes input matrix into Q, which is an orthogonal
     * matrix and R (upper triangular matrix).
     * @param q Orthogonal factor matrix.
     * @throws NotAvailableException Exception thrown if attempting to call 
     * @throws WrongSizeException Exception thrown if provided input matrix has 
     * less rows than columns.
     * @see #decompose()
     */    
    public void getQ(Matrix q) throws NotAvailableException, WrongSizeException {
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        
        int rows = inputMatrix.getRows();
        int columns = inputMatrix.getColumns();
        double s;
        
        if (rows < columns) {
            throw new WrongSizeException();
        }
        
        if (q.getRows() != rows || q.getColumns() != columns) {
            try {
                q.resize(rows, columns);
            } catch (WrongSizeException ignore) {
                //never happens
            }
        }
                
        for (int k = columns - 1; k >= 0; k--) {
            for (int i = 0; i < rows; i++) {
                q.setElementAt(i, k, 0.0);
            }
            q.setElementAt(k, k, 1.0);
            for (int j = k; j < columns; j++) {
                if (qr.getElementAt(k, k) != 0) {
                    s = 0.0;
                    for (int i = k; i < rows; i++) {
                        s += qr.getElementAt(i, k) * q.getElementAt(i, j);
                    }
                    
                    s = -s / qr.getElementAt(k, k);
                    for (int i = k; i < rows; i++) {
                        q.setElementAt(i, j, q.getElementAt(i, j) + 
                                s * qr.getElementAt(i, k));
                    }
                }
            }
        }
    }
    
    /**
     * Return the economy-sized orthogonal factor matrix.
     * QR decomposition decomposes input matrix into Q, which is an orthogonal
     * matrix and R (upper triangular matrix).
     * @return Orthogonal factor matrix.
     * @throws NotAvailableException Exception thrown if attempting to call 
     * @throws WrongSizeException Exception thrown if provided input matrix has 
     * less rows than columns.
     * @see #decompose()
     */
    public Matrix getQ() throws NotAvailableException, WrongSizeException {

        int rows = inputMatrix.getRows();
        int columns = inputMatrix.getColumns();

        Matrix out = new Matrix(rows, columns);
        getQ(out);
        return out;
    }
    
    /**
     * Solves a linear system of equations of the following form:
     * A * X = B, where A is the input matrix provided for QR decomposition,
     * X is the solution to the system of equations, and B is the parameters
     * vector/matrix.
     * Note: This method can be reused for different b vector/matrices without
     * having to recompute QR decomposition on the same input matrix.
     * Note: Provided b matrix must have the same number of rows as provided
     * input matrix A, otherwise a WrongSizeException will be raised.
     * Note: Provided input matrix A must have at least as many rows as columns,
     * otherwise a WrongSizeException will be raised as well. For input matrices
     * having a higher number of rows than columns, the system of equations will
     * be overdetermined and a least squares solution will be found.
     * Note: If provided input matrix A is rank deficient, a 
     * RankDeficientMatrixException will be thrown.
     * Note: In order to execute this method, a QR decomposition must be 
     * available, otherwise a NotAvailableException will be raised. In order to
     * avoid this exception call decompose() method first.
     * @param b Parameters matrix that determine a linear system of equations.
     * Provided matrix must have the same number of rows as provided input 
     * matrix for QR decomposition. Besides, each column on parameters matrix
     * will represent a new system of equations, whose solution will be returned
     * on appropriate column as an output of this method.
     * @param result Matrix containing solution of linear system of equations on
     * each column for each column of provided matrix of parameters b. Provided
     * matrix will be resized if needed
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing QR decomposition. To avoid this exception call
     * decompose() method first.
     * @throws WrongSizeException Exception thrown if attempting to call this 
     * method using an input matrix with less rows than columns; or if provided
     * parameters matrix (b) does not have the same number of rows as input 
     * matrix being QR decomposed.
     * @throws RankDeficientMatrixException Exception thrown if provided input
     * matrix to be QR decomposed is rank deficient. In this case linear system
     * of equations cannot be solved.
     */    
    public void solve(Matrix b, Matrix result) throws NotAvailableException, 
            WrongSizeException, RankDeficientMatrixException {
        solve(b, DEFAULT_ROUND_ERROR, result);
    }

    /**
     * Solves a linear system of equations of the following form:
     * A * X = B, where A is the input matrix provided for QR decomposition,
     * X is the solution to the system of equations, and B is the parameters
     * vector/matrix.
     * Note: This method can be reused for different b vector/matrices without
     * having to recompute QR decomposition on the same input matrix.
     * Note: Provided b matrix must have the same number of rows as provided
     * input matrix A, otherwise a WrongSizeException will be raised.
     * Note: Provided input matrix A must have at least as many rows as columns,
     * otherwise a WrongSizeException will be raised as well. For input matrices
     * having a higher number of rows than columns, the system of equations will
     * be overdetermined and a least squares solution will be found.
     * Note: If provided input matrix A is rank deficient, a
     * RankDeficientMatrixException will be thrown.
     * Note: In order to execute this method, a QR decomposition must be
     * available, otherwise a NotAvailableException will be raised. In order to
     * avoid this exception call decompose() method first.
     * @param b Parameters matrix that determine a linear system of equations.
     * Provided matrix must have the same number of rows as provided input
     * matrix for QR decomposition. Besides, each column on parameters matrix
     * will represent a new system of equations, whose solution will be returned
     * on appropriate column as an output of this method.
     * @param roundingError threshold to determine whether matrix b has full rank or not.
     *                      By default this is typically a tiny value close to zero.
     * @param result Matrix containing solution of linear system of equations on
     * each column for each column of provided matrix of parameters b. Provided
     * matrix will be resized if needed
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing QR decomposition. To avoid this exception call
     * decompose() method first.
     * @throws WrongSizeException Exception thrown if attempting to call this
     * method using an input matrix with less rows than columns; or if provided
     * parameters matrix (b) does not have the same number of rows as input
     * matrix being QR decomposed.
     * @throws RankDeficientMatrixException Exception thrown if provided input
     * matrix to be QR decomposed is rank deficient. In this case linear system
     * of equations cannot be solved.
     * @throws IllegalArgumentException if provided rounding error is negative.
     */
    public void solve(Matrix b, double roundingError, Matrix result) 
            throws NotAvailableException, WrongSizeException, 
            RankDeficientMatrixException {
        
        if (!isDecompositionAvailable()) {
            throw new NotAvailableException();
        }
        
        int rows = inputMatrix.getRows();
        int columns = inputMatrix.getColumns();
        int rowsB = b.getRows();
        int colsB = b.getColumns();
        double s;
        
        if (rowsB != rows) {
            throw new WrongSizeException();
        }
        
        if (roundingError < MIN_ROUND_ERROR) {
            throw new IllegalArgumentException();
        }
        
        if (rows < columns) {
            throw new WrongSizeException();
        }
        if (!isFullRank(roundingError)) {
            throw new RankDeficientMatrixException();
        }
        
        //Copy b into X
        Matrix x = b.clone();
        
        //Compute Y = transpose(Q) * B
        for (int k = 0; k < columns; k++) {
            for (int j = 0; j < colsB; j++) {
                s = 0.0;
                for (int i = k; i < rows; i++) {
                    s += qr.getElementAt(i, k) * x.getElementAt(i, j);
                }
                s = -s / qr.getElementAt(k, k);
                for (int i = k; i < rows; i++) {
                    x.setElementAt(i, j, x.getElementAt(i, j) + 
                            s * qr.getElementAt(i, k));
                }
            }
        }
        
        //Solve R * X = Y
        for (int k = columns - 1; k >= 0; k--) {
            for (int j = 0; j < colsB; j++) {
                x.setElementAt(k, j, x.getElementAt(k, j) / rDiag[k]);
            }
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < colsB; j++) {
                    x.setElementAt(i, j, x.getElementAt(i, j) - 
                            x.getElementAt(k, j) * qr.getElementAt(i, k));
                }
            }
        }
        
        //Pick only first columns rows of X in case of everdetermined systems
        //(where rows > columns), othersise rows == columns and we pick them all
        if (result.getRows() != columns || result.getColumns() != colsB) {
            //resize result
            result.resize(columns, colsB);
        }
        result.setSubmatrix(0, 0, columns - 1, colsB - 1, x, 0, 0, columns - 1, 
                colsB - 1);
    }    
    
    /**
     * Solves a linear system of equations of the following form:
     * A * X = B, where A is the input matrix provided for QR decomposition,
     * X is the solution to the system of equations, and B is the parameters
     * vector/matrix.
     * Note: This method can be reused for different b vector/matrices without
     * having to recompute QR decomposition on the same input matrix.
     * Note: Provided b matrix must have the same number of rows as provided
     * input matrix A, otherwise a WrongSizeException will be raised.
     * Note: Provided input matrix A must have at least as many rows as columns,
     * otherwise a WrongSizeException will be raised as well. For input matrices
     * having a higher number of rows than columns, the system of equations will
     * be overdetermined and a least squares solution will be found.
     * Note: If provided input matrix A is rank deficient, a 
     * RankDeficientMatrixException will be thrown.
     * Note: In order to execute this method, a QR decomposition must be 
     * available, otherwise a NotAvailableException will be raised. In order to
     * avoid this exception call decompose() method first.
     * @param b Parameters matrix that determine a linear system of equations.
     * Provided matrix must have the same number of rows as provided input 
     * matrix for QR decomposition. Besides, each column on parameters matrix
     * will represent a new system of equations, whose solution will be returned
     * on appropriate column as an output of this method.
     * @return Matrix containing solution of linear system of equations on each
     * column for each column of provided matrix of parameters b.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing QR decomposition. To avoid this exception call
     * decompose() method first.
     * @throws WrongSizeException Exception thrown if attempting to call this 
     * method using an input matrix with less rows than columns; or if provided
     * parameters matrix (b) does not have the same number of rows as input 
     * matrix being QR decomposed.
     * @throws RankDeficientMatrixException Exception thrown if provided input
     * matrix to be QR decomposed is rank deficient. In this case linear system
     * of equations cannot be solved.
     */
    public Matrix solve(Matrix b) throws NotAvailableException, 
            WrongSizeException, RankDeficientMatrixException {
        return solve(b, DEFAULT_ROUND_ERROR);
    }
    
    /**
     * Solves a linear system of equations of the following form:
     * A * X = B, where A is the input matrix provided for QR decomposition,
     * X is the solution to the system of equations, and B is the parameters
     * vector/matrix.
     * Note: This method can be reused for different b vector/matrices without
     * having to recompute QR decomposition on the same input matrix.
     * Note: Provided b matrix must have the same number of rows as provided
     * input matrix A, otherwise a WrongSizeException will be raised.
     * Note: Provided input matrix A must have at least as many rows as columns,
     * otherwise a WrongSizeException will be raised as well. For input matrices
     * having a higher number of rows than columns, the system of equations will
     * be overdetermined and a least squares solution will be found.
     * Note: If provided input matrix A is rank deficient, a 
     * RankDeficientMatrixException will be thrown.
     * Note: In order to execute this method, a QR decomposition must be 
     * available, otherwise a NotAvailableException will be raised. In order to
     * avoid this exception call decompose() method first.
     * @param b Parameters matrix that determine a linear system of equations.
     * Provided matrix must have the same number of rows as provided input 
     * matrix for QR decomposition. Besides, each column on parameters matrix
     * will represent a new system of equations, whose solution will be returned
     * on appropriate column as an output of this method.
     * @param roundingError Determines the amount of margin given to determine
     * whether a matrix has full rank or not due to rounding errors. If not 
     * provided, by default rounding error is set to zero, but this value can be 
     * relaxed if needed.
     * @return Matrix containing solution of linear system of equations on each
     * column for each column of provided matrix of parameters b.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing QR decomposition. To avoid this exception call
     * decompose() method first.
     * @throws WrongSizeException Exception thrown if attempting to call this 
     * method using an input matrix with less rows than columns; or if provided
     * parameters matrix (b) does not have the same number of rows as input 
     * matrix being QR decomposed.
     * @throws RankDeficientMatrixException Exception thrown if provided input
     * matrix to be QR decomposed is rank deficient. In this case linear system
     * of equations cannot be solved.
     * @throws IllegalArgumentException Exception thrown if provided rounding
     * error is lower than minimum allowed value (MIN_ROUND_ERROR)
     * @see #decompose()
     */    
    public Matrix solve(Matrix b, double roundingError) 
            throws NotAvailableException, WrongSizeException, 
            RankDeficientMatrixException {
        
        int columns = inputMatrix.getColumns();
        int colsB = b.getColumns();
        Matrix out = new Matrix(columns, colsB);
        solve(b, roundingError, out);
        return out;
    }
}
