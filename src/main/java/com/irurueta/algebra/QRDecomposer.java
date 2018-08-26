/**
 * @file
 * This file contains implementation of
 * com.irurueta.algebra.QRDecomposer
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 18, 2012
 */
package com.irurueta.algebra;

/**
 * This decomposer computes QR decomposition, which consists on factoring
 * provided input matrix into an orthogonal matrix (Q) and an upper triangular
 * matrix (R). In other words, if input matrix is A, then:
 * A = Q * R
 */
public class QRDecomposer extends Decomposer{

    /**
     * Constant defining default round error when determining full rank of
     * matrices. This value is zero by default.
     */
    public static final double DEFAULT_ROUND_ERROR = 1e-8;
    
    /**
     * Constant defining minimum allowed round error value when determining full
     * rank of matrices.
     */
    public static final double MIN_ROUND_ERROR = 0.0;
    
    /**
     * Internal matrix containing Q factor
     */
    private Matrix q;
    
    /**
     * Internal matrix containing R factor
     */
    private Matrix r;
    
    /**
     * Boolean indicating whether decomposed matrix is singular.
     */
    boolean sing;
    
    /**
     * Constructor of this class.
     */
    public QRDecomposer(){
        super();
        q = r = null;
        sing = false;
    }
    
    /**
     * Constructor of this class
     * @param inputMatrix Reference to input matrix to be decomposed
     */
    public QRDecomposer(Matrix inputMatrix){
        super(inputMatrix);
        q = r = null;
        sing = false;
    }
    
    /**
     * Returns decomposer type corresponding to QR decomposition
     * @return Decomposer type
     */        
    @Override
    public DecomposerType getDecomposerType() {
        return DecomposerType.QR_DECOMPOSITION;
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
        q = r = null;
        sing = false;
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
        return q != null | r != null;
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
     * @throws NotReadyException Exception thrown if attempting to call this
     * method when this instance is not ready (i.e. no input matrix has been
     * provided).
     * @throws LockedException Exception thrown if attempting to call this
     * method when this instance is not ready (i.e. no input matrix has been 
     * provided).
     * @throws DecomposerException Exception thrown if for any reason
     * decomposition fails while executing, like when convergence of results
     * can not be obtained, etc.
     */    
    @Override
    public void decompose() throws NotReadyException, LockedException, 
        DecomposerException {
        
        if(!isReady()) throw new NotReadyException();
        if(isLocked()) throw new LockedException();
        
        locked = true;
        
        int rows = inputMatrix.getRows();
        int columns = inputMatrix.getColumns();
        if(rows < columns) throw new DecomposerException();
        
        double norm, prod;
        try{
            //initialize Q with some random values (within range of 1 to keep
            //high accuracy
            q = Matrix.createWithUniformRandomValues(rows, rows, 0.5, 1.0);
            r = new Matrix(rows, columns);
            //initialize factor R to zero (because it will be diagonal)
            r.initialize(0.0);
        }catch(WrongSizeException e){}
        
        //Copy contents of input matrix to Q matrix (which is bigger than input
        //matrix) on top-left area.
        q.setSubmatrix(0, 0, rows - 1, columns - 1, inputMatrix);
        
        //Construct QR decomposition by means of Gram-Schimidt
        for(int j = 0; j < rows; j++){
            //Find orthogonal base by means of Gram-Schmidt of all rows of Q
            //Previous columns of Q will contain already normalized vectors,
            //while column J must be made orthogonal and then normalized
            for(int k = 0; k < j; k++){
                //compute scalar product of previous k-th orthonormal Q column
                //with current input matrix j-th column
                prod = 0.0;
                for(int i = 0; i < rows; i++)
                    prod += q.getElementAt(i, k) * q.getElementAt(i, j);
                
                //Update R factor with obtained dot product at location (k, j)
                //(only within r limits when j < columns)
                if(j < columns) r.setElementAt(k, j, prod);
                
                //Update j-th column of Q by subtracting obtained dot product
                //respect to previous k-th orthonormal column, on the diretion
                //of this latter column.
                for(int i = 0; i < rows; i++)
                    q.setElementAt(i, j, q.getElementAt(i, j) - 
                            prod * q.getElementAt(i, k));
            }
            //Normalize column j of Q after computing orthogonal Q column to
            //make it orthonormal
            norm = 0.0;
            for(int i = 0; i < rows; i++)
                norm += Math.pow(q.getElementAt(i, j), 2.0); //compute norm
            norm = Math.sqrt(norm);
            for(int i = 0; i < rows; i++)
                q.setElementAt(i, j, q.getElementAt(i, j) / norm); //normalize
            
            //update R factor diagonal with obtained norm (only within r limits
            //when j < columns)
            if(j < columns) r.setElementAt(j, j, norm);            
        }
        
        locked = false;
    }
    
    /**
     * Returns boolean indicating whether provided input matrix has full rank
     * or not.
     * Squared matrices having full rank also have determinant different from
     * zero.
     * Note: Because of rounding errors testing whether a matrix has full rank
     * or not might obtain unreliable results especially for non-squared 
     * matrices. In such cases, matrices usually tend to be considered as full
     * rank even when they are not.
     * Note: DEFAULT_ROUND_ERROR is used to determine whether matrix is full
     * rank or not
     * @return Boolean indicating whether provided input matrix has full rank or
     * not.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing QR decomposition. To avoid this exception call
     * decompose() method first.
     * @throws IllegalArgumentException Exception thrown if provided rounding 
     * error is lower than minimum allowed value (MIN_ROUND_ERROR)
     * @see #decompose()
     */    
    public boolean isFullRank() throws NotAvailableException{
        
        return isFullRank(DEFAULT_ROUND_ERROR);
    }
    
    /**
     * Returns boolean indicating whether provided input matrix has full rank
     * or not.
     * Squared matrices having full rank also have determinant different from
     * zero.
     * Note: Because of rounding errors testing whether a matrix has full rank
     * or not might obtain unreliable results especially for non-squared 
     * matrices. In such cases, matrices usually tend to be considered as full
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
     * @throws IllegalArgumentException Exception thrown if provided rounding 
     * error is lower than minimum allowed value (MIN_ROUND_ERROR)
     * @see #decompose()
     */
    public boolean isFullRank(double roundingError) 
            throws NotAvailableException, IllegalArgumentException{
        
        if(!isDecompositionAvailable()) throw new NotAvailableException();
        if(roundingError < MIN_ROUND_ERROR) 
            throw new IllegalArgumentException();
        
        int rows = inputMatrix.getRows();
        int columns = inputMatrix.getColumns();
        int minSize = Math.min(rows, columns);
        
        for(int j = 0; j < minSize; j++){
            if(Math.abs(r.getElementAt(j, j)) <= roundingError) return false;
        }
        return true;
    }
    
    
    /**
     * Returns upper triangular factor matrix.
     * QR decomposition decomposes input matrix into Q (orthogonal matrix) and
     * R, which is an upper triangular matrix.
     * @return Upper triangular factor matrix.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing QR decomposition. To avoid this exception call
     * decompose() method first.
     * @see #decompose()
     */
    public Matrix getR() throws NotAvailableException{
        if(!isDecompositionAvailable()) throw new NotAvailableException();
        
        return r;
    }
    
    /**
     * Returns the economy-sized orthogonal factor matrix.
     * QR decomposition decomposes input matrix into Q, which is an orthogonal
     * matrix and R (upper triangular matrix).
     * @return Orthogonal factor matrix.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing QR decomposition. To avoid this exception call
     * decompose() method first.
     * @see #decompose()
     */
    public Matrix getQ() throws NotAvailableException{
        if(!isDecompositionAvailable()) throw new NotAvailableException();
        
        return q;
    }
    
    /**
     * Solves a linear system of equations of the following form: A * X = B.
     * Where A is the input matrix provided for QR decomposition, X is the
     * solution to the system of equations, and B is the parameters vector/
     * matrix.
     * Note: This method can be reused for different b vectors/matrices without
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
     * Note: To solve the linear system of equations DEFAULT_ROUND_ERROR is used
     * Note: Solution of linear system of equations is stored in result matrix,
     * and result matrix is resized if needed.
     * @param b Parameters matrix that determines a linear system of equations.
     * Provided matrix must have the same number of rows as provided input
     * matrix for QR decomposition. Besides, each column on parameters matrix
     * will represent a new system of equations, whose solution will be returned
     * on appropriate column as an output of this method.
     * @param result Matrix containing solution of linear system of equations on
     * each column for each column of provided parameters matrix b.
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
     * @see #decompose()
     */        
    public void solve(Matrix b, Matrix result) throws NotAvailableException, 
            WrongSizeException, RankDeficientMatrixException{
        solve(b, DEFAULT_ROUND_ERROR, result);
    }
    
    /**
     * Solves a linear system of equations of the following form: A * X = B.
     * Where A is the input matrix provided for QR decomposition, X is the
     * solution to the system of equations, and B is the parameters vector/
     * matrix.
     * Note: This method can be reused for different b vectors/matrices without
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
     * Note: Solution of linear system of equations is stored in result matrix,
     * and result matrix is resized if needed.
     * @param b Parameters matrix that determines a linear system of equations.
     * Provided matrix must have the same number of rows as provided input
     * matrix for QR decomposition. Besides, each column on parameters matrix
     * will represent a new system of equations, whose solution will be returned
     * on appropriate column as an output of this method.
     * @param roundingError Determines the amount of margin given to determine
     * whether a matrix has full rank or not due to rounding errors. If not
     * provided, by default rounding error is set to zero, but this value can be
     * relaxed if needed.
     * @param result Matrix containing solution of linear system of equations on
     * each column for each column of provided parameters matrix b.
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
     * @throws IllegalArgumentException Exception thrown if provided rounging
     * error is lower than minimum allowed value (MIN_ROUND_ERROR).
     * @see #decompose()
     */    
    public void solve(Matrix b, double roundingError, Matrix result) 
            throws NotAvailableException, WrongSizeException, 
            RankDeficientMatrixException{
        
        if(!isDecompositionAvailable()) throw new NotAvailableException();
        
        int rows = inputMatrix.getRows();
        int columns = inputMatrix.getColumns();
        int rowsB = b.getRows();
        int colsB = b.getColumns();
        double sum;
        
        if(rowsB != rows) throw new WrongSizeException();
        if(roundingError < MIN_ROUND_ERROR) 
            throw new IllegalArgumentException();
        if(rows < columns) throw new WrongSizeException();
        if(!isFullRank(roundingError)) throw new RankDeficientMatrixException();
        
        //Compute Y = Q' * B
        Matrix Y = q.transposeAndReturnNew().multiplyAndReturnNew(b);
        
        //resize result matrix if needed
        if(result.getRows() != columns || result.getColumns() != colsB){
            result.resize(columns, colsB);
        }
        
        //Solve R * X = Y
        //Each column of B will be a column of out (i.e. a solution of the 
        //linear system of equations)
        for(int j2 = 0; j2 < colsB; j2++){
            //for everdetermined systems R has rows > columns, so we use only
            //first columns rows, which contain upper diagonal data of R, the
            //remaining rows of R are just zero.
            for(int i = columns - 1; i >= 0; i--){
                sum = Y.getElementAt(i, j2);
                for(int j = i + 1; j < columns; j++)
                    sum -= r.getElementAt(i, j) * result.getElementAt(j, j2);
                result.setElementAt(i, j2, sum / r.getElementAt(i, i));
            }
        }
    }
    
    /**
     * Solves a linear system of equations of the following form: A * X = B.
     * Where A is the input matrix provided for QR decomposition, X is the
     * solution to the system of equations, and B is the parameters vector/
     * matrix.
     * Note: This method can be reused for different b vectors/matrices without
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
     * Note: To solve the linear system of equations DEFAULT_ROUND_ERROR is used
     * @param b Parameters matrix that determines a linear system of equations.
     * Provided matrix must have the same number of rows as provided input
     * matrix for QR decomposition. Besides, each column on parameters matrix
     * will represent a new system of equations, whose solution will be returned
     * on appropriate column as an output of this method.
     * @return Matrix containing solution of linear system of equations on each
     * column for each column of provided parameters matrix b.
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
     * @see #decompose()
     */    
    public Matrix solve(Matrix b) throws NotAvailableException, 
            WrongSizeException, RankDeficientMatrixException{
        return solve(b, DEFAULT_ROUND_ERROR);
    }
    
    /**
     * Solves a linear system of equations of the following form: A * X = B.
     * Where A is the input matrix provided for QR decomposition, X is the
     * solution to the system of equations, and B is the parameters vector/
     * matrix.
     * Note: This method can be reused for different b vectors/matrices without
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
     * @param b Parameters matrix that determines a linear system of equations.
     * Provided matrix must have the same number of rows as provided input
     * matrix for QR decomposition. Besides, each column on parameters matrix
     * will represent a new system of equations, whose solution will be returned
     * on appropriate column as an output of this method.
     * @param roundingError Determines the amount of margin given to determine
     * whether a matrix has full rank or not due to rounding errors. If not
     * provided, by default rounding error is set to zero, but this value can be
     * relaxed if needed.
     * @return Matrix containing solution of linear system of equations on each
     * column for each column of provided parameters matrix b.
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
     * @throws IllegalArgumentException Exception thrown if provided rounging
     * error is lower than minimum allowed value (MIN_ROUND_ERROR).
     * @see #decompose()
     */
    public Matrix solve(Matrix b, double roundingError) 
            throws NotAvailableException, WrongSizeException, 
            RankDeficientMatrixException, IllegalArgumentException{
        
        if(!isDecompositionAvailable()) throw new NotAvailableException();
        
        int columns = inputMatrix.getColumns();        
        int colsB = b.getColumns();
        Matrix out = new Matrix(columns, colsB);
        solve(b, roundingError, out);
        return out;
    }
    
    /**
     * Returns a or -a depending on b sign. If b is positive, this method 
     * returns a, otherwise it returns -a.
     * @param a 1st value
     * @param b 2nd value
     * @return  a or -a depending on b sign
     */
    private double sign(double a, double b){
        return (b >= 0.0 ? (a >= 0.0 ? a : -a) : (a >= 0.0 ? -a : a));
    }
}
