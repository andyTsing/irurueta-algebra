/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.algebra.LUDecomposer
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 18, 2012
 */
package com.irurueta.algebra;

import com.irurueta.statistics.UniformRandomizer;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.*;

public class LUDecomposerTest {
    
    public static final int MIN_ROWS = 3;
    public static final int MAX_ROWS = 50;
    public static final int MIN_COLUMNS = 3;
    public static final int MAX_COLUMNS = 50;
    
    public static final double MIN_RANDOM_VALUE = 0.0;
    public static final double MIN_RANDOM_VALUE2 = 1.0;
    public static final double MAX_RANDOM_VALUE = 100.0;
    
    public static final double RELATIVE_ERROR = 1.0;
    public static final double ROUND_ERROR = 1e-3;
    
    public static final double EPSILON = 1e-10;
    
    public LUDecomposerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
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
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int rows = columns + randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        //Test 1st constructor
        LUDecomposer decomposer = new LUDecomposer();        
        assertFalse(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getDecomposerType(), 
                DecomposerType.LU_DECOMPOSITION);
        
        decomposer.setInputMatrix(m);
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        assertEquals(decomposer.getDecomposerType(), 
                DecomposerType.LU_DECOMPOSITION);
        
        //Test 2nd constructor
        decomposer = new LUDecomposer(m);
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        assertEquals(decomposer.getDecomposerType(), 
                DecomposerType.LU_DECOMPOSITION);
    }
    
    @Test
    public void testGetSetInputMatrixAndIsReady() throws WrongSizeException, 
        LockedException, 
        NotReadyException, 
        DecomposerException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int rows = columns + randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        LUDecomposer decomposer = new LUDecomposer();
        assertEquals(decomposer.getDecomposerType(), 
                DecomposerType.LU_DECOMPOSITION);
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
    public void testDecomposer() throws WrongSizeException, NotReadyException, 
        LockedException, DecomposerException, NotAvailableException{
        
        //works for any rectangular matrix size with rows >= columns
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int rows = columns + randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        int length;
        Matrix m, l, u, m2;
        int[] pivot;
        
        m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        length = m.getRows() * m.getColumns();
        
        LUDecomposer decomposer = new LUDecomposer(m);
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        decomposer.decompose();
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertTrue(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        //Check using pivotted L
        l = decomposer.getPivottedL();
        u = decomposer.getU();
        pivot = decomposer.getPivot();
        
        m2 = l.multiplyAndReturnNew(u);
        
        int pivotIndex;
        assertEquals(m.getRows(), m2.getRows());
        assertEquals(m.getColumns(), m2.getColumns());
        for(int j = 0; j < m2.getColumns(); j++){
            for(int i = 0; i < m2.getRows(); i++){
                pivotIndex = pivot[i];
                if(!Double.isNaN(m2.getElementAt(i, j))){
                    assertEquals(m.getElementAt(pivotIndex, j), 
                            m2.getElementAt(i, j), ROUND_ERROR);
                }
            }
        }
        
        
        //Check using L : A = L * U
        l = decomposer.getL();
        
        m2 = l.multiplyAndReturnNew(u);
        
        assertEquals(m.getRows(), m2.getRows());
        assertEquals(m.getColumns(), m2.getColumns());
        for(int j = 0; j < m2.getColumns(); j++){
            for(int i = 0; i < m2.getRows(); i++){
                if(!Double.isNaN(m2.getElementAt(i, j))){
                    assertEquals(m.getElementAt(i, j), m2.getElementAt(i, j), 
                            ROUND_ERROR);
                }
            }
        }
        
        //Force NotReadyException
        decomposer = new LUDecomposer();
        try{
            decomposer.decompose();
            fail("NotReadyException expected but not thrown");
        }catch(NotReadyException e){}
    }
    
    @Test
    public void testIsSingular() throws WrongSizeException, LockedException, 
        NotReadyException, DecomposerException, NotAvailableException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int rows = columns + randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        Matrix m;
        
        LUDecomposer decomposer = new LUDecomposer();
        
        //Test for square matrix
        m = DecomposerHelper.getSingularMatrixInstance(rows, rows);
        
        decomposer.setInputMatrix(m);
        
        //Force NotAvailableException
        try{
            decomposer.isSingular();
            fail("NotAvailableException expected but not thrown");
        }catch(NotAvailableException e){}
        
        decomposer.decompose();
        
        //Force IllegalArgumentException
        try{
            decomposer.isSingular(-1.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        assertTrue(decomposer.isSingular());
        
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, rows);
        decomposer.setInputMatrix(m);
        decomposer.decompose();
        
        assertFalse(decomposer.isSingular());
        
        //Test for non-square matrix (Force WrongSizeException
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        decomposer.setInputMatrix(m);
        decomposer.decompose();
        
        try{
            decomposer.isSingular();
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
    }
    
    @Test
    public void testGetPivottedL() throws WrongSizeException, NotReadyException,
        LockedException, DecomposerException, NotAvailableException{
                
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int rows = columns + randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        int length;
        Matrix m, l;
        
        m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        length = m.getRows() * m.getColumns();
        
        LUDecomposer decomposer = new LUDecomposer(m);
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        //Force NotAvailableException
        try{
            decomposer.getPivottedL();
            fail("NotAvailableException expected but not thrown");
        }catch(NotAvailableException e){}
        
        decomposer.decompose();
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertTrue(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        l = decomposer.getPivottedL();
        
        //Ensure size of l is correct
        assertEquals(l.getRows(), m.getRows());
        assertEquals(l.getColumns(), m.getColumns());
        
        for(int j = 0; j < l.getColumns(); j++){
            for(int i = 0; i < l.getRows(); i++){
                if(j > i){
                    assertEquals(l.getElementAt(i, j), 0.0, ROUND_ERROR);
                }else{
                    assertEquals(Math.abs(l.getElementAt(i, j)), 1.0, RELATIVE_ERROR);
                }
            }
        }
    }
    
    @Test
    public void testGetL() throws WrongSizeException, NotReadyException, 
        LockedException, DecomposerException, NotAvailableException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int rows = columns + randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        int length;
        Matrix m, pivottedL, l;
        int[] pivot;
        
        m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        length = m.getRows() * m.getColumns();
        
        LUDecomposer decomposer = new LUDecomposer(m);
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        //Force NotAvailableException
        try{
            decomposer.getL();
            fail("NotAvailableException expected but not thrown");
        }catch(NotAvailableException e){}
        
        decomposer.decompose();
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertTrue(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        pivottedL = decomposer.getPivottedL();
        l = decomposer.getL();
        pivot = decomposer.getPivot();
        
        assertEquals(pivottedL.getRows(), l.getRows());
        assertEquals(pivottedL.getColumns(), l.getColumns());
        
        int pivotIndex;
        for(int j = 0; j < l.getColumns(); j++){
            for(int i = 0; i < l.getRows(); i++){
                pivotIndex = pivot[i];
                assertEquals(pivottedL.getElementAt(i, j), 
                        l.getElementAt(pivotIndex, j), 0.0);
            }
        }
    }
    
    @Test
    public void testGetU() throws WrongSizeException, NotReadyException, 
        LockedException, DecomposerException, NotAvailableException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int rows = columns + randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        int length;
        Matrix m, u;
        
        m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        length = m.getRows() * m.getColumns();
        
        LUDecomposer decomposer = new LUDecomposer(m);
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        //Force NotAvailableException
        try{
            decomposer.getU();
            fail("NotAvailableException expected but not thrown");
        }catch(NotAvailableException e){}
        
        decomposer.decompose();
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertTrue(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        u = decomposer.getU();
        
        //Ensure size of l is correct
        assertEquals(u.getRows(), m.getColumns());
        assertEquals(u.getColumns(), m.getColumns());
        
        for(int j = 0; j < u.getColumns(); j++){
            for(int i = 0; i < u.getRows(); i++){
                if(i > j){
                    assertEquals(u.getElementAt(i, j), 0.0, ROUND_ERROR);
                }
            }
        }
    }
    
    @Test
    public void testGetPivot() throws WrongSizeException, NotReadyException, 
        LockedException, DecomposerException, NotAvailableException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int rows = columns + randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        int length;
        Matrix m;
        int[] pivot;
        
        m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);       
        
        LUDecomposer decomposer = new LUDecomposer(m);
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        //Force NotAvailableException
        try{
            decomposer.getPivot();
            fail("NotAvailableException expected but not thrown");
        }catch(NotAvailableException e){}
        
        decomposer.decompose();
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertTrue(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        pivot = decomposer.getPivot();
        
        length = pivot.length;
        assertEquals(length, m.getRows());
        
        for(int i = 0; i < length; i++){
            assertTrue(pivot[i] < length);
        }
    }
    
    @Test
    public void testDeterminant() throws WrongSizeException, NotReadyException,
        LockedException, DecomposerException, NotAvailableException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int rows = columns + randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        int length;
        Matrix m;
        double determinant;
        
        //TEST FOR ONE ELEMENT MATRIX (DETERMINANT EQUAL TO THE ELEMENT)
        //Check for matrix of size (1, 1)
        m = new Matrix(1, 1);
        m.initialize(randomizer.nextDouble(MIN_RANDOM_VALUE + 1.0, 
                MAX_RANDOM_VALUE + 1.0));
        
        LUDecomposer decomposer = new LUDecomposer(m);
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        //Force NotAvailableException
        try{
            decomposer.determinant();
            fail("NotAvailableException expected but not thrown");
        }catch(NotAvailableException e){}
        
        decomposer.decompose();
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertTrue(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        
        determinant = decomposer.determinant();
        
        //Check determinant is equal to element located at (0, 0)
        assertEquals(determinant, m.getElementAt(0, 0), ROUND_ERROR);
        
        //Square matrix
        //TEST FOR NON LD MATRIX (NON_ZERO DETERMINANT)
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, rows);
        length = m.getRows() * m.getColumns();
        
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
        
        determinant = decomposer.determinant();
        
        //Check that determinant is different of zero (we give a margin of
        //episilon to take into account possible rounding error
        assertTrue(Math.abs(determinant) > EPSILON);
        
        //TEST FOR LD MATRIX (ZERO DETERMINANT)
        //Initialize matrix with 2 ld rows
        m = DecomposerHelper.getSingularMatrixInstance(rows, rows);
        
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
        
        determinant = decomposer.determinant();
        
        //Check that determinant is equal to zero (we give a margin of epsilon
        //to take into account possible rounding error
        assertEquals(determinant, 0.0, ROUND_ERROR);
        
        //Test for non square matrix (Force WrongSizeException)
        m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE + 1.0, MAX_RANDOM_VALUE + 1.0);
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
        
        //Force WrongSizeException
        try{
            decomposer.determinant();
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
    }
    
    @Test
    public void testSolve() throws WrongSizeException, NotReadyException, 
        LockedException, DecomposerException, NotAvailableException, 
        SingularMatrixException{
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int rows = columns + randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        int columns2 = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m, b, s, b2;
        
        //Try for square matrix
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, rows);
        b = Matrix.createWithUniformRandomValues(rows, columns2, 
                MIN_RANDOM_VALUE2, MAX_RANDOM_VALUE);
        
        LUDecomposer decomposer = new LUDecomposer(m);
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
        for(int j = 0; j < b2.getColumns(); j++){
            for(int i = 0; i < b2.getRows(); i++){
                assertEquals(b2.getElementAt(i, j), b.getElementAt(i, j), 
                        ROUND_ERROR);
            }
        }
        
        //Try for non-square matrix (Throw WrongSizeException)
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        b = Matrix.createWithUniformRandomValues(rows, columns2, 
                MIN_RANDOM_VALUE2, MAX_RANDOM_VALUE);
        
        decomposer.setInputMatrix(m);
        decomposer.decompose();
        
        //Force WrongSizeException
        try{
            decomposer.solve(b);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        
        //Test for singular square matrix (Throw SingularMatrixException)
        m = DecomposerHelper.getSingularMatrixInstance(rows, rows);
        b = Matrix.createWithUniformRandomValues(rows, columns2, 
                MIN_RANDOM_VALUE2, MAX_RANDOM_VALUE);
        decomposer.setInputMatrix(m);
        decomposer.decompose();
        
        try{
            decomposer.solve(b);
            fail("SingularMatrixException expected but not thrown");
        }catch(SingularMatrixException e){}
    }
}
