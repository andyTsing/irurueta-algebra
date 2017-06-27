/**
 * @file
 * This file contains implementation of
 * com.irurueta.algebra.RQDecomposer
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 19, 2012
 */
package com.irurueta.algebra;

/**
 * This decomposer computes RQ decomposition, which consists on factoring
 * provided input matrix into an upper triangular matrix (R) and an orthogonal
 * matrix (Q). In other words, if input matrix is A, then A = R * Q
 */
public class RQDecomposer extends Decomposer{
    
    /**
     * Internal QR decomposer used behind the scenes to compute RQ 
     * decomposition. Notice that QR and RQ decompositions are related and for
     * that reason QRDecomposer is used
     */
    QRDecomposer internalDecomposer;
    
    /**
     * Constructor of this class.
     */
    public RQDecomposer(){
        super();
        internalDecomposer = new QRDecomposer();
    }
    
    /**
     * Constructor of this class.
     * @param inputMatrix Reference to input matrix to be decomposed.
     */
    public RQDecomposer(Matrix inputMatrix){
        super(inputMatrix);
        internalDecomposer = new QRDecomposer();
    }

    /**
     * Returns decomposer type corresponding to RQ decomposition
     * @return Decomposer type
     */            
    @Override
    public DecomposerType getDecomposerType() {
        return DecomposerType.RQ_DECOMPOSITION;
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
        internalDecomposer.setInputMatrix(inputMatrix);
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
        return internalDecomposer.isDecompositionAvailable();
    }

    /**
     * This method computes RQ matrix decomposition, which consists on factoring
     * provided input matrix into an upper triangular matrix (R) and an 
     * orthogonal matrix (Q).
     * In other words, if input matrix is A, then: A = R * Q
     * Note: During execution of this method, this instance will be locked, and
     * hence attempting to set some parameters might raise a LockedException.
     * Note: After execution of this method, RQ decomposition will be available
     * and operations such as retrieving R and Q matrices will be able to be 
     * done. Attempting to call any of such operations before calling this
     * method wil raise a NotAvailableException because they require computation
     * of QR decomposition first.
     * @throws NotReadyException Exception thrown if attempting to call this
     * method when this instance is not ready (i.e. no input matrix has been
     * provided).
     * @throws LockedException Exception thrown if this decomposer is already
     * locked before calling this method. Notice that this method will actually
     * lock this instance while it is being executed.
     * @throws DecomposerException Exception thrown if for any reason 
     * decomposition fails while being executed, like when provided input matrix
     * has less rows than columns.
     */
    @Override
    public void decompose() throws NotReadyException, LockedException, 
    DecomposerException {
        
        if(!isReady()) throw new NotReadyException();
        if(isLocked()) throw new LockedException();
        
        Matrix tmp = null;
        
        locked = true;
        
        int rows = inputMatrix.getRows();
        int columns = inputMatrix.getColumns();
        
        try{
            tmp = new Matrix(columns, rows);
        }catch(WrongSizeException e){}
        
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                tmp.setElementAt(j, rows - i -1, 
                        inputMatrix.getElementAt(i, j));
            }
        }
        
        internalDecomposer.setInputMatrix(tmp);        
        internalDecomposer.decompose();
        
        locked = false;
    }
    
    /**
     * Returns upper triangular factor matrix.
     * RQ decomposition decomposes input matrix into R (upper triangular 
     * matrix), and Q, which is an orthogonal matrix.
     * @return Upper triangular factor matrix.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing RQ decomposition. To avoid this exception call
     * decompose() method first.
     * @see #decompose()
     */
    public Matrix getR() throws NotAvailableException{
        
        if(!isDecompositionAvailable()) throw new NotAvailableException();
        
        int rows = inputMatrix.getRows();
        int columns = inputMatrix.getColumns();
        
        Matrix r2 = internalDecomposer.getR();
        
        //Left-right flipped identity
        //Instance initialized to zero
        Matrix flipI = null;
        try{
            flipI = new Matrix(rows, rows);
        }catch(WrongSizeException ignore){}
        
        flipI.initialize(0.0);
        
        for(int j = 0; j < rows; j++){
            for(int i = 0; i < rows; i++){
                if(i == rows - j - 1) flipI.setElementAt(i, j, 1.0);
            }
        }
        
        //Big permutation
        Matrix perm = null;
        try{
            perm = Matrix.identity(columns, columns);
        }catch(WrongSizeException ignore){}
        
        //Copy flipped identity into top-left corner
        perm.setSubmatrix(0, 0, rows - 1, rows - 1, flipI);

        try{
            perm.multiply(r2); //perm * r2
            perm.multiply(flipI); //perm * r2 * flipI
            perm.transpose();
        }catch(WrongSizeException ignore){}        
        
        return perm;
    }
    
    /**
     * Returns the economy-sized orthogonal factor matrix.
     * RQ decomposition decomposes input matrix into R, which is an upper
     * triangular matrix and Q (orthogonal matrix)
     * @return Orthogonal factor matrix.
     * @throws NotAvailableException Exception thrown if attempting to call this
     * method before computing RQ decomposition. To avoid this exception call
     * decompose() method first.
     * @see #decompose()
     */
    public Matrix getQ() throws NotAvailableException{
        if(!isDecompositionAvailable()) throw new NotAvailableException();
        
        int rows = inputMatrix.getRows();
        int columns = inputMatrix.getColumns();
        
        Matrix q2 = internalDecomposer.getQ();
        
        //Left-right flipped identity
        //Instance initialized to zero
        Matrix flipI = null;
        try{
            flipI = new Matrix(rows, rows);            
        }catch(WrongSizeException ignore){}
        
        flipI.initialize(0.0);
        
        for(int j = 0; j < rows; j++){
            for(int i = 0; i < rows; i++){
                if(i == rows - j -1) flipI.setElementAt(i, j, 1.0);
            }
        }
        
        //Big permutation
        Matrix perm = null;
        try{
            perm = Matrix.identity(columns, columns);
        }catch(WrongSizeException ignore){}

        //Copy flipped identity into top-left corner
        perm.setSubmatrix(0, 0, rows - 1, rows - 1, flipI);

        Matrix q = null;
        try{
            q = q2.multiplyAndReturnNew(perm); //qTrans = q2 * perm
            q.transpose();
        }catch(WrongSizeException ignore){}        
        
        return q;
    }
    
}
