/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.algebra.SingularValueDecomposer
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 21, 2012
 */
package com.irurueta.algebra;

import com.irurueta.statistics.UniformRandomizer;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.*;

public class SingularValueDecomposerTest {
    
    public static final int MIN_ROWS = 1;
    public static final int MAX_ROWS = 50;
    public static final int MIN_COLUMNS = 1;
    public static final int MAX_COLUMNS = 50;
    
    public static final double MIN_RANDOM_VALUE = 0.0;
    public static final double MAX_RANDOM_VALUE = 100.0;
    
    public static final int MIN_ITERS = 2;
    public static final int MAX_ITERS = 50;
    
    public static final double RELATIVE_ERROR = 3.0;
    public static final double RELATIVE_ERROR_OVERDETERMINED = 0.35;
    public static final double ABSOLUTE_ERROR = 1e-6;
    public static final double VALID_RATIO = 0.2;
    public static final double ROUND_ERROR = 1e-3;
    
    public static final double EPS = 1e-12;
    
    public SingularValueDecomposerTest() {}

    @BeforeClass
    public static void setUpClass() throws Exception {}

    @AfterClass
    public static void tearDownClass() throws Exception {}
    
    @Before
    public void setUp() {}
    
    @After
    public void tearDown() {}
    
    @Test
    public void testConstructor() throws WrongSizeException, LockedException, 
        NotReadyException, DecomposerException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        SingularValueDecomposer decomposer = new SingularValueDecomposer();
        
        assertFalse(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getDecomposerType(), 
                DecomposerType.SINGULAR_VALUE_DECOMPOSITION);
        
        decomposer.setInputMatrix(m);
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        assertEquals(decomposer.getDecomposerType(), 
                DecomposerType.SINGULAR_VALUE_DECOMPOSITION);
        
        decomposer = new SingularValueDecomposer(m);
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        decomposer.decompose();
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertTrue(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        //when setting a new input matrix, decomposition becomes unavailable
        //must be recomputed
        decomposer.setInputMatrix(m);
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
    }
    
    @Test
    public void testGetSetInputMatrix() throws WrongSizeException, 
        LockedException, NotReadyException, DecomposerException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        SingularValueDecomposer decomposer = new SingularValueDecomposer();
        assertEquals(decomposer.getDecomposerType(), 
                DecomposerType.SINGULAR_VALUE_DECOMPOSITION);
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
        
        //when setting a new input matrix, decomposition becomes unavailable
        //and must be recomputed
        decomposer.setInputMatrix(m);
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
    }
    
    @Test
    public void testDecompose() throws WrongSizeException, LockedException, 
        DecomposerException, NotReadyException, NotAvailableException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int rows = randomizer.nextInt(columns, MAX_ROWS + 1);
        
        Matrix m, u, w, v, vTrans, m2;
        SingularValueDecomposer decomposer = new SingularValueDecomposer();
        
        //randomly initialize m
        m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        //Force NotReadyException
        try{
            decomposer.decompose();
            fail("NotReadyException expected but not thrown");
        }catch(NotReadyException e){}
        
        decomposer.setInputMatrix(m);
        decomposer.decompose();
        
        u = decomposer.getU();
        w = decomposer.getW();
        v = decomposer.getV();
        vTrans = v.transposeAndReturnNew();
        
        //check that w is diagonal with descending singular values
        assertEquals(w.getRows(), columns);
        assertEquals(w.getColumns(), columns);
        double prevSingularValue = Double.MAX_VALUE;
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < columns; i++){
                if(i == j){
                    assertTrue(w.getElementAt(i, j) <= prevSingularValue);
                    prevSingularValue = w.getElementAt(i, j);
                }else{
                    assertEquals(w.getElementAt(i, j), 0.0, 0.0);
                }
            }
        }
        
        
        m2 = u.multiplyAndReturnNew(w.multiplyAndReturnNew(vTrans ));
        
        //check that m2 is equal (except for rounding errors to m
        assertEquals(m2.getRows(), rows);
        assertEquals(m2.getColumns(), columns);
        assertTrue(m.equals(m2, ABSOLUTE_ERROR));
    }
    
    @Test
    public void testGetSetMaxIterations() throws WrongSizeException, 
        LockedException, 
        NotReadyException, 
        DecomposerException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int maxIters = randomizer.nextInt(MIN_ITERS, MAX_ITERS);
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        SingularValueDecomposer decomposer = new SingularValueDecomposer(m);
        
        //Check default value
        assertEquals(decomposer.getMaxIterations(),
                SingularValueDecomposer.DEFAULT_MAX_ITERS);
        
        //Try before decomposing
        decomposer.setMaxIterations(maxIters);
        assertEquals(decomposer.getMaxIterations(), maxIters);
        try{
            decomposer.decompose();
        }catch(DecomposerException e){}
        
        //Try after decomposing
        maxIters = randomizer.nextInt(MIN_ITERS, MAX_ITERS);
        decomposer.setMaxIterations(maxIters);
        assertEquals(decomposer.getMaxIterations(), maxIters);
        
        //Try on constructor
        decomposer = new SingularValueDecomposer(m, maxIters);
        assertEquals(decomposer.getMaxIterations(), maxIters);
        
        //Force IllegalArgumentException
        try{
            decomposer.setMaxIterations(0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
    
    @Test
    public void testGetU() throws WrongSizeException, NotReadyException, 
        LockedException, DecomposerException, NotAvailableException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        //Works for any matrix size
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int rows = randomizer.nextInt(columns, MAX_ROWS + 1);
        
        //Randomly initialize m
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        SingularValueDecomposer decomposer = new SingularValueDecomposer(m);
        
        decomposer.decompose();
        
        Matrix u = decomposer.getU();
        Matrix uTrans = u.transposeAndReturnNew();
        
        //Check that U is orthogonal: U' * U = I
        Matrix ident = uTrans.multiplyAndReturnNew(u);
        assertEquals(u.getRows(), rows);
        assertEquals(u.getColumns(), columns);
        for(int j = 0; j < ident.getColumns(); j++){
            for(int i = 0; i < ident.getRows(); i++){
                if(i == j){
                    assertEquals(ident.getElementAt(i, j), 1.0, RELATIVE_ERROR);
                }else{
                    assertEquals(ident.getElementAt(i, j), 0.0, ROUND_ERROR);
                }
            }
        }
    }
    
    @Test
    public void testGetV() throws WrongSizeException, NotReadyException, 
        LockedException, DecomposerException, NotAvailableException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        //Works for any matrix size
        int row = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        //Randomly initialize m
        Matrix m = Matrix.createWithUniformRandomValues(row, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        Matrix v, vTrans;
        
        SingularValueDecomposer decomposer = new SingularValueDecomposer(m);
        
        decomposer.decompose();
        
        v = decomposer.getV();
        vTrans = v.transposeAndReturnNew();
        
        //Check that V is orthogonal: V' * V = I
        Matrix ident = vTrans.multiplyAndReturnNew(v);
        assertEquals(v.getRows(), columns);
        assertEquals(v.getColumns(), columns);
        for(int j = 0; j < ident.getColumns(); j++){
            for(int i = 0; i < ident.getRows(); i++){
                if(i == j){
                    assertEquals(ident.getElementAt(i, j), 1.0, RELATIVE_ERROR);
                }else{
                    assertEquals(ident.getElementAt(i, j), 0.0, ROUND_ERROR);
                }
            }
        }
    }
    
    @Test
    public void testGetSingularValues() throws WrongSizeException, 
        NotReadyException, LockedException, DecomposerException, 
        NotAvailableException{
        
        //Works for any matrix size
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        //Randomly initialize m
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double[] singularValues;
        SingularValueDecomposer decomposer = new SingularValueDecomposer(m);
        
        decomposer.decompose();
        
        singularValues = decomposer.getSingularValues();
        
        //Check that singular values are ordered from largest to smallest
        assertEquals(singularValues.length, columns);
        for(int i = 1; i < columns; i++){
            assertTrue(singularValues[i] <= singularValues[i - 1]);
            //Algorithm computes positive singular values
            assertTrue(singularValues[i] >= 0.0);
            assertTrue(singularValues[i - 1] >= 0.0);
        }
    }
    
    @Test
    public void testGetW() throws WrongSizeException, NotReadyException, 
        LockedException, DecomposerException, NotAvailableException{
        
        //Works for any matrix size
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        //Randomly initialize m
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        Matrix w;
        SingularValueDecomposer decomposer = new SingularValueDecomposer(m);
        
        decomposer.decompose();
        
        w = decomposer.getW();
        
        //Check that singular values are ordered from largest to smallest and
        //that W is diagonal
        assertEquals(w.getRows(), columns);
        assertEquals(w.getColumns(), columns);
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < columns; i++){
                if(i == j){
                    if(i >= 1){
                        assertTrue(w.getElementAt(i, j) <= 
                                w.getElementAt(i - 1, j - 1));
                        //Algorithm computes positive singular values
                        assertTrue(w.getElementAt(i, j) >= 0.0);
                        assertTrue(w.getElementAt(i - 1, j - 1) >= 0.0);
                    }
                }else{
                    assertEquals(w.getElementAt(i, j), 0.0, ROUND_ERROR);
                }
            }
        }
    }
    
    @Test
    public void testGetNorm2() throws WrongSizeException, NotReadyException, 
        LockedException, DecomposerException, NotAvailableException{
        
        FrobeniusNormComputer normComputer = new FrobeniusNormComputer();
        
        //works for any matrix size
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        //Randomly initialize m
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        Matrix mTrans, m2;
        double normFro, norm2;
        
        SingularValueDecomposer decomposer = new SingularValueDecomposer(m);
        
        mTrans = m.transposeAndReturnNew();
        m2 = mTrans.multiplyAndReturnNew(m);
        normFro = Math.sqrt(normComputer.getNorm(m2));
        
        decomposer.decompose();
        norm2 = decomposer.getNorm2();
        assertEquals(normFro, norm2, norm2 * RELATIVE_ERROR_OVERDETERMINED);
    }
    
    @Test
    public void testGetConditionNumber() throws WrongSizeException, 
        NotReadyException, LockedException, DecomposerException, 
        NotAvailableException{
        
        //Works for any matrix size
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        SingularValueDecomposer decomposer = new SingularValueDecomposer(m);
        double[] w;
        double condNumber, recCondNumber;
        
        decomposer.decompose();
        condNumber = decomposer.getConditionNumber();
        recCondNumber = decomposer.getReciprocalConditionNumber();
        w = decomposer.getSingularValues();
        
        if(recCondNumber > EPS){
            assertEquals(1.0 / recCondNumber, condNumber, 
                    condNumber * RELATIVE_ERROR);
        }
        
        if(w[0] >= EPS && w[columns - 1] >= EPS){
            assertEquals(recCondNumber, w[columns - 1] / w[0], ABSOLUTE_ERROR);
        }
    }
    
    @Test
    public void testGetRank() throws WrongSizeException, NotReadyException, 
        LockedException, DecomposerException, NotAvailableException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int rows = randomizer.nextInt(columns, MAX_ROWS + 1);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        SingularValueDecomposer decomposer = new SingularValueDecomposer(m);
        Matrix u, v, vTrans, w, m2;
        
        decomposer.decompose();
        u = decomposer.getU();
        w = decomposer.getW();
        v = decomposer.getV();
        vTrans = v.transposeAndReturnNew();
        
        //Randomly set some singular values to zero
        int rank = columns;
        for(int i = 0; i < columns; i++){
            if(randomizer.nextInt(0, 2) == 0){
                //Set singular value with 50% probability
                w.setElementAt(i, i, 0.0);
                rank--;
            }
        }
        
        m2 = u.multiplyAndReturnNew(w.multiplyAndReturnNew(vTrans));
        decomposer = new SingularValueDecomposer(m2);
        decomposer.decompose();
        
        assertEquals(rank, decomposer.getRank());
    }
    
    @Test
    public void testGetNullity() throws WrongSizeException, NotReadyException, 
        LockedException, DecomposerException, NotAvailableException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int rows = randomizer.nextInt(columns, MAX_ROWS + 1);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        SingularValueDecomposer decomposer = new SingularValueDecomposer(m);
        Matrix u, v, vTrans, w, m2;
        
        decomposer.decompose();
        u = decomposer.getU();
        w = decomposer.getW();
        v = decomposer.getV();
        vTrans = v.transposeAndReturnNew();
        
        //Randomly set some singular values to zero
        int nullity = 0;
        for(int i = 0; i < columns; i++){
            if(randomizer.nextInt(0, 2) == 0){
                //Set singular value with 50% probability
                w.setElementAt(i, i, 0.0);
                nullity++;
            }
        }
        
        m2 = u.multiplyAndReturnNew(w.multiplyAndReturnNew(vTrans));
        decomposer = new SingularValueDecomposer(m2);
        decomposer.decompose();
        
        assertEquals(nullity, decomposer.getNullity());
    }
    
    @Test
    public void testGetRange() throws WrongSizeException, NotReadyException, 
        LockedException, DecomposerException, NotAvailableException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS + 3, MAX_COLUMNS + 3);
        int rows = randomizer.nextInt(columns, MAX_ROWS + 4);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        SingularValueDecomposer decomposer = new SingularValueDecomposer(m);
        Matrix u, v, vTrans, w, m2, r, rTrans, ident;
        
        decomposer.decompose();
        u = decomposer.getU();
        w = decomposer.getW();
        v = decomposer.getV();
        vTrans = v.transposeAndReturnNew();
        
        //Randomly set some singular values to zero
        int rank = columns;
        for(int i = 0; i < columns; i++){
            if(randomizer.nextInt(0, 2) == 0){
                //Set singular value with 50% probability
                w.setElementAt(i, i, 0.0);
                rank--;
            }
        }
        
        m2 = u.multiplyAndReturnNew(w.multiplyAndReturnNew(vTrans));
        decomposer = new SingularValueDecomposer(m2);
        decomposer.decompose();
        
        if(rank == 0){
            try{
                decomposer.getRange();
                fail("NotAvailableException expected but not thrown");
            }catch(NotAvailableException e){}
        }else{
            r = decomposer.getRange();
            rTrans = r.transposeAndReturnNew();
            ident = rTrans.multiplyAndReturnNew(r);
            assertEquals(r.getColumns(), rank);
            assertEquals(r.getRows(), rows);
            
            for(int j = 0; j < ident.getColumns(); j++){
                for(int i = 0; i < ident.getRows(); i++){
                    if(i == j){
                        assertEquals(ident.getElementAt(i, j), 1.0, 
                                RELATIVE_ERROR);
                    }else{
                        assertEquals(ident.getElementAt(i, j), 0.0, 
                                ROUND_ERROR);
                    }
                }
            }
        }
    }
    
    @Test
    public void testGetNullspace() throws WrongSizeException, NotReadyException,
        LockedException, DecomposerException, NotAvailableException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        int rows = randomizer.nextInt(columns, MAX_ROWS + 3);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        SingularValueDecomposer decomposer = new SingularValueDecomposer(m);
        Matrix u, v, vTrans, w, m2, ns, nsTrans, ident;
        
        decomposer.decompose();
        u = decomposer.getU();
        w = decomposer.getW();
        v = decomposer.getV();
        vTrans = v.transposeAndReturnNew();
        
        //Randomly set some singular values to zero
        int nullity = 0;
        for(int i = 0; i < columns; i++){
            if(randomizer.nextInt(0, 2) == 0){
                //Set singular value with 50% probability
                w.setElementAt(i, i, 0.0);
                nullity++;
            }
        }
        
        m2 = u.multiplyAndReturnNew(w.multiplyAndReturnNew(vTrans));
        
        decomposer = new SingularValueDecomposer(m2);
        decomposer.decompose();
        
        if(nullity == 0){
            try{
                decomposer.getNullspace();
                fail("NotAvailableException expected but not thrown");
            }catch(NotAvailableException e){}
        }else{
            ns = decomposer.getNullspace();
            nsTrans = ns.transposeAndReturnNew();
            ident = nsTrans.multiplyAndReturnNew(ns);
            assertEquals(ns.getColumns(), nullity);
            assertEquals(ns.getRows(), columns);
            
            for(int j = 0; j < ident.getColumns(); j++){
                for(int i = 0; i < ident.getRows(); i++){
                    if(i == j){
                        assertEquals(ident.getElementAt(i, j), 1.0, 
                                RELATIVE_ERROR);
                    }else{
                        assertEquals(ident.getElementAt(i, j), 0.0, 
                                ROUND_ERROR);
                    }
                }
            }
        }
    }
    
    @Test
    public void testSolve() throws WrongSizeException, NotReadyException, 
        LockedException, DecomposerException, NotAvailableException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS + 4, MAX_ROWS + 4);
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, rows - 1);
        int columns2 = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m, b, s, b2;
        double relError;
        
        //Try for square marix
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, rows);
        b = Matrix.createWithUniformRandomValues(rows, columns2, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        SingularValueDecomposer decomposer = new SingularValueDecomposer(m);
        
        //Force NotAvailableException
        try{
            decomposer.solve(b);
            fail("NotAvailableException expected but not thrown");
        }catch(NotAvailableException e){}
        
        decomposer.decompose();
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
        assertTrue(b2.equals(b, ROUND_ERROR));
        
        //Try for overdetermined system (rows > columns)
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        b = Matrix.createWithUniformRandomValues(rows, columns2, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        decomposer.setInputMatrix(m);
        decomposer.decompose();
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
                relError = Math.abs(RELATIVE_ERROR_OVERDETERMINED * 
                        b2.getElementAt(i, j));
                if(Math.abs(b2.getElementAt(i, j) - b.getElementAt(i, j)) < 
                        relError) valid++;
            }
        }
        
        assertTrue(((double)valid / (double)total) > VALID_RATIO);
        
        //Try for b matrix having differnt number of rows than m (Throws
        //WrongSizeException
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        b = Matrix.createWithUniformRandomValues(columns, columns2, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        decomposer.setInputMatrix(m);
        decomposer.decompose();
        try{
            decomposer.solve(b);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
    }
}
