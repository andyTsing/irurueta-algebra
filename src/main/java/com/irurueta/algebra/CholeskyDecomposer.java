/**
 * @file
 * This file contains implementation of
 * com.irurueta.algebra.CholeskyDecomposer
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 15, 2012
 */
package com.irurueta.algebra;

/**
 * This class allows decomposition of matrices using Cholesky decomposition,
 * which consists on retrieving a lower or upper triangular matrix so that input
 * matrix can be decomposed as: A = L * L' = R' * R, where A is provided input
 * matrix, L is a lower triangular matrix and R is an upper triangular matrix.
 * Note: Cholesky decomposition can only be correctly computed on positive 
 * definite matrices.
 */
public class CholeskyDecomposer extends Decomposer{
    
    /**
     * Internal storage of Cholesky decomposition for provided input matrix
     */
    private Matrix r;
    
    /**
     * Boolean indicating whether provided input matrix is symmetric and 
     * positive definite
     */
    boolean spd;

    /**
     * Constructor of this class.
     */
    public CholeskyDecomposer(){
        super();
        r = null;
        spd = false;
    }
    
    /**
     * Constructor of this class.
     * @param inputMatrix Reference to input matrix to be decomposed
     */
    public CholeskyDecomposer(Matrix inputMatrix){
        super(inputMatrix);
        r = null;
        spd = false;
    }
    
    /**
     * Returns decomposer type corresponding to Cholesky decomposition
     * @return Decomposer type
     */
    @Override
    public DecomposerType getDecomposerType() {
        return DecomposerType.CHOLESKY_DECOMPOSITION;
    }
    
    /**
     * Sets reference to input matrix to be decomposed.
     * @param inputMatrix Reference to input matrix to be decomposed.
     * @throws LockedException Exception thrown if attempting to call this
     * method while this instance remains locked.
     */
    @Override
    public void setInputMatrix(Matrix inputMatrix) throws LockedException{
        super.setInputMatrix(inputMatrix);
        r = null;
        spd = false;
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
        return r != null;
    }

    /**
     * This method computes Cholesky matrix decomposition, which consists on
     * factoring provided input matrix whenever it is square, symmetric and
     * positive definite into a lower triangulator factor such that it follows
     * next expression: A = L * L'
     * where A is input matrix and L is lower triangular factor (L' is its
     * transposed).
     * Cholesky decomposition can also be computed using Right Cholesky 
     * decomposition, in which case A = R' * R, where R is an upper triangular
     * factor equal to L'.
     * Both factors L and R will be accessible once Cholesky decomposition has
     * been computed.
     * Note: During execution of this method, this instance will remain locked,
     * and hence attempting to set some parameters might raise a LockedException
     * Note: After execution of this method, Cholesky decomposition will be
     * available and operations such as retrieving L matrix factor or solving
     * systems of linear equations will be able to be done. Attempting to call 
     * any of such operations before calling this method will raise a
     * NotAvailableException because they require computation of Cholesky
     * decomposition first.
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
        
        if(!isReady()) throw new NotReadyException();
        
        int rows = inputMatrix.getRows();
        int columns = inputMatrix.getColumns();
        
        if(rows != columns) throw new DecomposerException();
        
        locked = true;
        
        Matrix localR = null;
        try{
            localR = new Matrix(columns, columns);
        }catch(WrongSizeException e){}
        boolean localSpd = (rows == columns);
        double d, s;
        
        //Main loop
        for(int j = 0; j < columns; j++){
            d = 0.0;
            for(int k = 0; k < j; k++){
                s = inputMatrix.getElementAt(k, j);
                for(int i = 0; i < k; i++){
                    s = s - localR.getElementAt(i, k) * 
                            localR.getElementAt(i, j);
                }
                s /= localR.getElementAt(k, k);
                localR.setElementAt(k, j, s);
                d += s * s;
                localSpd &= inputMatrix.getElementAt(k, j) == 
                        inputMatrix.getElementAt(j, k);
            }
            d = inputMatrix.getElementAt(j, j) - d;
            localSpd &= d > 0.0;
            //sqrt of max(d, 0.0)
            localR.setElementAt(j, j, Math.sqrt((d > 0.0) ? d : 0.0)); 
            for(int k = j + 1; k < columns; k++){
                localR.setElementAt(k, j, 0.0);
            }
        }
        
        this.spd = localSpd;
        this.r = localR;
        
        locked = false;
    }
    
    /**
     * Returns Cholesky matrix factor corresponding to a Lower triangular matrix
     * following this expression: A = L * L'. Where A is provided input matrix
     * that has been decomposed and L is the left lower triangular matrix 
     * factor.
     * @return Returns Cholesky Lower triangular matrix
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before actually computing Cholesky decomposition. To avoid this
     * exception call decompose() method first.
     * @see #decompose()
     */
    public Matrix getL() throws NotAvailableException{
        if(!isDecompositionAvailable()) throw new NotAvailableException();
        
        return r.transposeAndReturnNew();
    }
    
    /**
     * Returns Cholesky matrix factor corresponding to an upper triangular 
     * matrix following this expression: A = R' * R. Where A is provided input
     * matrix that has been decomposed and R is the right upper triangular 
     * matrix factor.
     * @return Returns Cholesky upper triangular matrix
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before actually computing Cholesky decomposition. To avoid this
     * exception call decompose() method first.
     * @see #decompose()
     */
    public Matrix getR() throws NotAvailableException{
        if(!isDecompositionAvailable()) throw new NotAvailableException();
        
        return r;
    }
    
    /**
     * Returns boolean indicating whether provided input matrix is
     * Symmetric Positive Definite or not.
     * Notice that if returned value is false, then Cholesky decomposition
     * should be ignored, as Cholesky decomposition can only be computed on
     * symmetric positive definite matrices.
     * @return Boolean indicating whether provided input matrix is symmetric
     * positive definite or not.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing Cholesky decomposition. To avoid this exception
     * call decompose() method first.
     * @see #decompose()
     */
    public boolean isSPD() throws NotAvailableException{
        if(!isDecompositionAvailable()) throw new NotAvailableException();
        
        return spd;
    }

    /**
     * Solves a linear system of equations of the following form: A * X = B.
     * Where A is the input matrix provided for Cholesky decomposition, X is the
     * solution to the system of equations, and B is the parameters 
     * vector/matrix.
     * Note: This method can be reused for different b vectors/matrices without
     * having to recompute Cholesky decomposition on the same input matrix.
     * Note: Provided b matrix must have the same number of rows as provided
     * input matrix A, otherwise an IllegalArgumentException will be raised
     * Note: Provided input matrix A must be square, otherwise a 
     * WrongSizeException will be raised.
     * Note: If provided input matrix A is not symmetric positive definite, a
     * NonSymmetricPositiveDefiniteMatrixException will be thrown.
     * Note: In order to be able to execute this method, a Cholesky 
     * decomposition must be available, otherwise a NotAvailableException will
     * be raised. In order to avoid this exception call decompose() method first
     * Note: result matrix contains solution of linear system of equations. It
     * will be resized if provided matrix does not have proper size
     * @param b Parameters of linear system of equations
     * @param result instance where solution X will be stored.
     * @throws com.irurueta.algebra.NotAvailableException if decomposition has 
     * not yet been computed.
     * @throws com.irurueta.algebra.WrongSizeException if the number of rows of 
     * b matrix is not equal to the number of rows of input matrix provided to
     * Cholesky decomposer.
     * @throws com.irurueta.algebra.NonSymmetricPositiveDefiniteMatrixException 
     * if input matrix provided to Cholesky decomposer is not positive definite.
     */    
    public void solve(Matrix b, Matrix result) throws NotAvailableException,
            WrongSizeException, NonSymmetricPositiveDefiniteMatrixException{
        
        if(!isDecompositionAvailable()) throw new NotAvailableException();
        
        int rows = inputMatrix.getRows();
        int columns = inputMatrix.getColumns();
        int rowsB = b.getRows();
        int colsB = b.getColumns();
        
        if(rowsB != rows) throw new WrongSizeException();
        
        if(!isSPD()) throw new NonSymmetricPositiveDefiniteMatrixException();
        
        //resize result matrix if needed
        if(result.getRows() != rowsB || result.getColumns() != colsB){
            result.resize(rowsB, colsB);
        }
        
        //Copy b into result matrix
        result.copyFrom(b);
        
        Matrix l = getL();
        
        //Solve L * Y = B
        for(int k = 0; k < columns; k++){
            for(int j = 0; j < colsB; j++){
                for(int i = 0; i < k; i++){
                    result.setElementAt(k, j, result.getElementAt(k, j) -
                            result.getElementAt(i, j) * l.getElementAt(k, i));
                }
                result.setElementAt(k, j, 
                        result.getElementAt(k, j) / l.getElementAt(k, k));
            }
        }
        
        //Solv L' * X = Y
        int k2;
        for(int k = columns - 1; k >= 0; k--){
            k2 = k;
            for(int j = 0; j < colsB; j++){
                for(int i = k2 + 1; i < columns; i++){
                    result.setElementAt(k2, j, result.getElementAt(k2, j) -
                            result.getElementAt(i, j) * l.getElementAt(i, k2));
                }
                result.setElementAt(k2, j, 
                        result.getElementAt(k2, j) / l.getElementAt(k2, k2));
            }
        }
    }
    
    /**
     * Solves a linear system of equations of the following form: A * X = B.
     * Where A is the input matrix provided for Cholesky decomposition, X is the
     * solution to the system of equations, and B is the parameters 
     * vector/matrix.
     * Note: This method can be reused for different b vectors/matrices without
     * having to recompute Cholesky decomposition on the same input matrix.
     * Note: Provided b matrix must have the same number of rows as provided
     * input matrix A, otherwise an IllegalArgumentException will be raised
     * Note: Provided input matrix A must be square, otherwise a 
     * WrongSizeException will be raised.
     * Note: If provided input matrix A is not symmetric positive definite, a
     * NonSymmetricPositiveDefiniteMatrixException will be thrown.
     * Note: In order to be able to execute this method, a Cholesky 
     * decomposition must be available, otherwise a NotAvailableException will
     * be raised. In order to avoid this exception call decompose() method first
     * @param b Parameters of linear system of equations
     * @return a new matrix containing solution X.
     * @throws com.irurueta.algebra.NotAvailableException if decomposition has 
     * not yet been computed.
     * @throws com.irurueta.algebra.WrongSizeException if the number of rows of 
     * b matrix is not equal to the number of rows of input matrix provided to
     * Cholesky decomposer.
     * @throws com.irurueta.algebra.NonSymmetricPositiveDefiniteMatrixException 
     * if input matrix provided to Cholesky decomposer is not positive definite.
     */
    public Matrix solve(Matrix b) throws NotAvailableException,
            WrongSizeException, NonSymmetricPositiveDefiniteMatrixException{
        
        int columns = inputMatrix.getColumns();
        int colsB = b.getColumns();
        Matrix out = new Matrix(columns, colsB);
        solve(b, out);
        return out;
    } 
}
