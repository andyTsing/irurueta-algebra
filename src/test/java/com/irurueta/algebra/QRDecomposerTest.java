/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.algebra.QRDecomposer
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 19, 2012
 */
package com.irurueta.algebra;

import com.irurueta.statistics.UniformRandomizer;
import java.util.Random;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class QRDecomposerTest {
    
    public static final int MIN_ROWS = 1;
    public static final int MAX_ROWS = 50;
    public static final int MIN_COLUMNS = 1;
    public static final int MAX_COLUMNS = 50;
    public static final double MIN_RANDOM_VALUE = 0.0;
    public static final double MIN_RANDOM_VALUE2 = 1.0;
    public static final double MAX_RANDOM_VALUE = 100.0;
    
    public static final double ABSOLUTE_ERROR = 1e-6;
    public static final double RELATIVE_ERROR = 0.35;
    public static final double VALID_RATIO = 0.25;
    
    public QRDecomposerTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testConstructor() throws WrongSizeException, LockedException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        //Test 1st constructor
        QRDecomposer decomposer = new QRDecomposer();
        
        assertFalse(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getDecomposerType(), 
                DecomposerType.QR_DECOMPOSITION);
        
        decomposer.setInputMatrix(m);
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        assertEquals(decomposer.getDecomposerType(), 
                DecomposerType.QR_DECOMPOSITION);
        
        //Test 2nd constructor
        decomposer = new QRDecomposer(m);
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        assertEquals(decomposer.getDecomposerType(), 
                DecomposerType.QR_DECOMPOSITION);
    }
    
    @Test
    public void testGetSetInputMatrixAndIsReady() throws WrongSizeException, 
        LockedException, NotReadyException, DecomposerException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int rows = randomizer.nextInt(columns + 1, MAX_ROWS + 2);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        QRDecomposer decomposer = new QRDecomposer();
        assertEquals(decomposer.getDecomposerType(), 
                DecomposerType.QR_DECOMPOSITION);
        assertFalse(decomposer.isReady());
        
        decomposer.setInputMatrix(m);
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        decomposer.decompose();
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertTrue(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        //when setting a new input matrix, decomposition becomes unavailable and
        //must be recomputed
        decomposer.setInputMatrix(m);
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
    }
    
    @Test
    public void testDecompose() throws WrongSizeException, NotReadyException, 
        LockedException, DecomposerException, NotAvailableException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int rows = randomizer.nextInt(columns + 1, MAX_ROWS + 2);
        
        Matrix m, q, r, m2;
        
        m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        QRDecomposer decomposer = new QRDecomposer(m);
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        decomposer.decompose();
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertTrue(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        //Check decomposition
        q = decomposer.getQ();
        r = decomposer.getR();
        
        m2 = q.multiplyAndReturnNew(r);
        
        assertEquals(m.getRows(), m2.getRows());
        assertEquals(m.getColumns(), m2.getColumns());
        for(int j = 0; j < m2.getColumns(); j++){
            for(int i = 0; i < m2.getRows(); i++){
                assertEquals(m.getElementAt(i, j), m2.getElementAt(i, j),
                        ABSOLUTE_ERROR);
            }
        }
        
        //Force DecomposerException
        m = Matrix.createWithUniformRandomValues(columns, rows, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        decomposer.setInputMatrix(m);
        try{
            decomposer.decompose();
            fail("DecomposerException expected but not thrown");
        }catch(DecomposerException e){}
        
        //Force NotReadyException
        decomposer = new QRDecomposer();
        try{
            decomposer.decompose();
            fail("NotReadyException expected but not thrown");
        }catch(NotReadyException e){}        
    }
    
    @Test
    public void testIsFullRank() throws WrongSizeException, LockedException, 
        NotReadyException, DecomposerException, NotAvailableException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS);
        int rows = randomizer.nextInt(columns, MAX_ROWS + 3);
        
        Matrix m;
        
        QRDecomposer decomposer = new QRDecomposer();
        
        //Test for any rectangular or square matrix that a matrix has full rank
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        decomposer.setInputMatrix(m);
        
        //Force NotAvailableException
        try{
            decomposer.isFullRank();
            fail("NotAvailableException expected but not thrown");
        }catch(NotAvailableException e){}
        
        decomposer.decompose();
        
        //Force IllegalArgumentException with a negative round error
        try{
            decomposer.isFullRank(-1.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        assertTrue(decomposer.isFullRank(ABSOLUTE_ERROR));
        
        //Test false case only for square matrix, for other sizes unreliable
        //results might be obtained because of rounding error
        m = DecomposerHelper.getSingularMatrixInstance(rows, rows);
        decomposer.setInputMatrix(m);
        decomposer.decompose();
        
        assertFalse(decomposer.isFullRank(ABSOLUTE_ERROR));
    }
    
    @Test
    public void testGetH() throws WrongSizeException, LockedException, 
        NotReadyException, DecomposerException, NotAvailableException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        int rows = randomizer.nextInt(columns + 1, MAX_ROWS + 4);
        
        Matrix m, r;
        
        QRDecomposer decomposer = new QRDecomposer();
        
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        decomposer.setInputMatrix(m);
        
        //Force NotAvailableException
        try{
            decomposer.getR();
            fail("NotAvailableException expected but not thrown");
        }catch(NotAvailableException e){}
        
        decomposer.decompose();
        r = decomposer.getR();
        
        assertEquals(r.getRows(), rows);
        assertEquals(r.getColumns(), columns);
        
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                if(i > j){
                    assertEquals(r.getElementAt(i, j), 0.0, 
                            ABSOLUTE_ERROR);
                }
            }
        }
    }
    
    @Test
    public void testGetR() throws WrongSizeException, LockedException, 
        NotReadyException, DecomposerException, NotAvailableException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        int rows = randomizer.nextInt(columns + 1, MAX_ROWS + 4);
        
        Matrix m, q, qTransposed, test;
        
        QRDecomposer decomposer = new QRDecomposer();
        
        //Test for non-square matrix having rows > columns
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        decomposer.setInputMatrix(m);
        
        //Force NotAvcailableException
        try{
            decomposer.getQ();
            fail("NotAvailableException expected but not thrown");
        }catch(NotAvailableException e){}
        
        decomposer.decompose();
        
        q = decomposer.getQ();
        
        assertEquals(q.getRows(), rows);
        assertEquals(q.getColumns(), rows);
        
        //Q is an orthogonal matrix, which means that Q * Q' = I
        qTransposed = q.transposeAndReturnNew();
        
        test = qTransposed.multiplyAndReturnNew(q);
        
        assertEquals(test.getRows(), rows);
        assertEquals(test.getColumns(), rows);
        
        //Check that test is similar to identity
        assertTrue(test.equals(Matrix.identity(rows, rows), ABSOLUTE_ERROR));
        
        //Test for square matrix
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, rows);
        decomposer.setInputMatrix(m);
        
        //Force NotAvailableException
        try{
            decomposer.getQ();
            fail("NotAvailableException expected but not thrown");
        }catch(NotAvailableException e){}
        
        decomposer.decompose();
        
        q = decomposer.getQ();
        
        assertEquals(q.getRows(), rows);
        assertEquals(q.getColumns(), rows);
        
        //Q is an orthogonal matrix, which means that Q * Q' = I
        qTransposed = q.transposeAndReturnNew();
        
        test = qTransposed.multiplyAndReturnNew(q);
        
        assertEquals(test.getRows(), rows);
        assertEquals(test.getColumns(), rows);
        
        //Check that test is similar to identity
        assertTrue(test.equals(Matrix.identity(rows, rows), ABSOLUTE_ERROR));
    }
    
    @Test
    public void testSolve() throws WrongSizeException, 
        RankDeficientMatrixException, NotReadyException, LockedException, 
        DecomposerException, NotAvailableException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS + 4, MAX_ROWS + 2);
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, rows - 1);
        int columns2 = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m, b, s, b2;
        double relError;
        
        //Try for square matrix
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, rows);
        b = Matrix.createWithUniformRandomValues(rows, columns2, 
                MIN_RANDOM_VALUE2, MAX_RANDOM_VALUE);
        
        QRDecomposer decomposer = new QRDecomposer(m);
        
        //Force NotAvailableException
        try{
            decomposer.solve(b);
            fail("NotAvailableException expected but not thrown");
        }catch(NotAvailableException e){}
        decomposer.decompose();
        
        //Force IllegalArgumentException
        try{
            decomposer.solve(b, -1.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        s = decomposer.solve(b);
        
        //Check that solution after calling solve matches following equation:
        //m * s = b
        b2 = m.multiplyAndReturnNew(s);
        
        assertEquals(b2.getRows(), b.getRows());
        assertEquals(b2.getColumns(), b.getColumns());
        
        assertTrue(b.equals(b, ABSOLUTE_ERROR));
        
        
        //Try for overdetermined system (rows > columns)
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        b = Matrix.createWithUniformRandomValues(rows, columns2, 
                MIN_RANDOM_VALUE2, MAX_RANDOM_VALUE);
        
        decomposer.setInputMatrix(m);
        decomposer.decompose();
        
        //Force IllegalArgumentException
        try{
            decomposer.solve(b, -1.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        s = decomposer.solve(b);
        
        //check that solution after calling solve matches following equation:
        //m * s = b
        b2 = m.multiplyAndReturnNew(s);
        
        assertEquals(b2.getRows(), b.getRows());
        assertEquals(b2.getColumns(), b.getColumns());
        
        int valid = 0, total = b2.getColumns() * b2.getRows();
        for(int j = 0; j < b2.getColumns(); j++){
            for(int i = 0; i < b2.getRows(); i++){
                relError = Math.abs(RELATIVE_ERROR * b2.getElementAt(i, j));
                if(Math.abs(b2.getElementAt(i, j) - b.getElementAt(i, j)) < 
                        relError) valid++;
            }
        }
        
        assertTrue(((double)valid / (double)total) > VALID_RATIO);
        
        //Try for b matrix having different number of rows than m
        //(Throws WrongSizeException)
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        b = Matrix.createWithUniformRandomValues(columns, columns2, 
                MIN_RANDOM_VALUE2, MAX_RANDOM_VALUE);
        decomposer.setInputMatrix(m);
        decomposer.decompose();
        try{
            decomposer.solve(b);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        
        //Test for Rank deficient matrix only for squared matrices
        //(for other sizes, rank deficiency might not be detected and solve
        //method would execute)
        m = DecomposerHelper.getSingularMatrixInstance(rows, rows);
        b = Matrix.createWithUniformRandomValues(rows, columns2, 
                MIN_RANDOM_VALUE2, MAX_RANDOM_VALUE);
        decomposer.setInputMatrix(m);
        decomposer.decompose();
        try{
            decomposer.solve(b, ABSOLUTE_ERROR);
            fail("RankDeficientMatrixException expected but not thrown");
        }catch(RankDeficientMatrixException e){}
    }
}
