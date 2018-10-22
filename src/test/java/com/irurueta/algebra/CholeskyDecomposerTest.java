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

import com.irurueta.statistics.UniformRandomizer;
import org.junit.*;

import java.util.Random;

import static org.junit.Assert.*;

public class CholeskyDecomposerTest {
    
    public static final int MIN_ROWS = 2;
    public static final int MAX_ROWS = 10;
    public static final int MIN_COLUMNS = 2;
    public static final int MAX_COLUMNS = 10;
    
    public static final double MIN_RANDOM_VALUE = 1.0;
    public static final double MAX_RANDOM_VALUE = 100.0;
    
    public static final double ABSOLUTE_ERROR = 1e-6;
    
    public CholeskyDecomposerTest() { }

    @BeforeClass
    public static void setUpClass() { }

    @AfterClass
    public static void tearDownClass() { }
    
    @Before
    public void setUp() { }
    
    @After
    public void tearDown() { }
    
    @Test
    public void testConstructor() throws WrongSizeException, LockedException {
            UniformRandomizer randomizer = new UniformRandomizer(new Random());

        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        //Test 1st constructor
        CholeskyDecomposer decomposer = new CholeskyDecomposer();
        assertFalse(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getDecomposerType(), 
                DecomposerType.CHOLESKY_DECOMPOSITION);
        
        decomposer.setInputMatrix(m);
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        assertEquals(decomposer.getDecomposerType(),
                DecomposerType.CHOLESKY_DECOMPOSITION);
        
        //Test 2nd constructor
        decomposer = new CholeskyDecomposer(m);
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        assertEquals(decomposer.getDecomposerType(),
                DecomposerType.CHOLESKY_DECOMPOSITION);
    }
    
    @Test
    public void testGetSetInputMatrixAndIsReady() throws WrongSizeException, 
            LockedException, NotReadyException, DecomposerException {
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, rows, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        CholeskyDecomposer decomposer = new CholeskyDecomposer();
        
        assertEquals(decomposer.getDecomposerType(), 
                DecomposerType.CHOLESKY_DECOMPOSITION);
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
        
        //When setting a new input matrix, decomposition becomes unavailable and
        //must be reocmputed
        decomposer.setInputMatrix(m);
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
    }
    
    @Test
    public void testDecompose() throws WrongSizeException, LockedException, 
            NotReadyException, DecomposerException, NotAvailableException {
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        Matrix initL, initR, m;
        Matrix l, r, m2, l2, r2;
        
        CholeskyDecomposer decomposer = new CholeskyDecomposer();
        
        //Initialize initL as a lower triangular matrix of size (rows, rows)
        //and initR as its transpose. Cholesky decomposition is unique, hence
        //obtained factors will be exactly the same initL and initR
        //Compute a new random symmetric positive definite matrix instance as
        //well as its Cholesky left Lower triangular factor. Notice that
        //Cholesky decomposition is unique for a symmetric positive definite
        //matrix.
        
        initL = DecomposerHelper.getLeftLowerTriangulatorFactor(rows);
        m = DecomposerHelper.getSymmetricPositiveDefiniteMatrixInstance(initL);
        
        //Right Upper triangular factor of Cholesky decomposition is equal to
        //the transpose of initL
        initR = initL.transposeAndReturnNew();
        
        //Set input matrix to decompose
        assertFalse(decomposer.isReady());
        decomposer.setInputMatrix(m);
        assertTrue(decomposer.isReady());
        
        assertFalse(decomposer.isDecompositionAvailable());
        decomposer.decompose();
        assertTrue(decomposer.isDecompositionAvailable());
        l = decomposer.getL();
        r = decomposer.getR();
        assertTrue(decomposer.isSPD());
        //Ensure that obtained decomposition factors are equal to the initial
        //ones since Cholesky decomposition is unique for symmetric positive
        //definite matrices
        assertEquals(l.getRows(), initL.getRows());
        assertEquals(l.getColumns(), initL.getColumns());
        
        assertEquals(r.getRows(), initR.getRows());
        assertEquals(r.getColumns(), initR.getColumns());
        
        //Check that factors are equal (initL == l and initR == r) because
        //Cholesky decomposition is unique
        for (int j = 0; j < l.getColumns(); j++) {
            for (int i = 0; i < l.getRows(); i++) {
                assertEquals(initL.getElementAt(i, j),
                        l.getElementAt(i, j), ABSOLUTE_ERROR);
            }
        }
        
        for (int j = 0; j < r.getColumns(); j++) {
            for (int i = 0; i < r.getRows(); i++) {
                assertEquals(initR.getElementAt(i, j),
                        r.getElementAt(i, j), ABSOLUTE_ERROR);
            }
        }
        
        l2 = l.transposeAndReturnNew();
        r2 = r.transposeAndReturnNew();
        
        //ensure that left and right factors are just their respective
        //transposed ones
        assertTrue(l.equals(r2, ABSOLUTE_ERROR));
        assertTrue(r.equals(l2, ABSOLUTE_ERROR));
        
        //compute new matrix using computed factors
        m2 = l.multiplyAndReturnNew(r);
        
        //ensure that new obtained matrix is equal to the original symmetric
        //positive definite matrix
        assertEquals(m2.getRows(), m.getRows());
        assertEquals(m2.getColumns(), m.getColumns());
        
        for (int j = 0; j < m2.getColumns(); j++) {
            for (int i = 0; i < m2.getRows(); i++) {
                assertEquals(m.getElementAt(i, j), m2.getElementAt(i, j), 
                        ABSOLUTE_ERROR);
            }
        }
        
        //Force NotReadyException
        decomposer = new CholeskyDecomposer();
        try {
            decomposer.decompose();
            fail("NotReadyException expected but not thrown");
        } catch (NotReadyException ignore) { }
        
        //Force DecomposerException
        m = new Matrix(rows, rows + 1);
        decomposer.setInputMatrix(m);
        try {
            decomposer.decompose();
            fail("DecomposerException expected but not thrown");
        } catch (DecomposerException ignore) { }
    }
    
    @Test
    public void testGetL() throws WrongSizeException, LockedException, 
            NotReadyException, DecomposerException, NotAvailableException {
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        Matrix initL, m, l, r, l2, r2;
        
        CholeskyDecomposer decomposer = new CholeskyDecomposer();
        
        initL = DecomposerHelper.getLeftLowerTriangulatorFactor(rows);
        m = DecomposerHelper.getSymmetricPositiveDefiniteMatrixInstance(initL);
        
        decomposer.setInputMatrix(m);
        //Force NotAvailableException
        try {
            decomposer.getL();
            fail("NotAvailableException expected but not thrown");
        } catch (NotAvailableException ignore) { }
        
        decomposer.decompose();
        
        l = decomposer.getL();
        r = decomposer.getR();
        assertTrue(decomposer.isSPD());
        
        l2 = r.transposeAndReturnNew();
        r2 = l.transposeAndReturnNew();
        
        assertTrue(l.equals(l2, ABSOLUTE_ERROR));
        assertTrue(r.equals(r2, ABSOLUTE_ERROR));
        
        assertEquals(l.getRows(), rows);
        assertEquals(r.getColumns(), rows);
        
        //Check that factors l and initL are equal
        for (int j = 0; j < l.getColumns(); j++) {
            for (int i = 0; i < l.getRows(); i++) {
                assertEquals(initL.getElementAt(i, j),
                        l.getElementAt(i, j), ABSOLUTE_ERROR);
            }
        }
    }
    
    @Test
    public void testGetR() throws WrongSizeException, LockedException, 
            NotReadyException, DecomposerException, NotAvailableException {
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        Matrix initL, initR, m, l, r, l2, r2;
        
        CholeskyDecomposer decomposer = new CholeskyDecomposer();
        
        initL = DecomposerHelper.getLeftLowerTriangulatorFactor(rows);
        initR = initL.transposeAndReturnNew();
        
        m = DecomposerHelper.getSymmetricPositiveDefiniteMatrixInstance(initL);
        
        decomposer.setInputMatrix(m);
        
        //Force NotAvailableException
        try {
            decomposer.getR();
            fail("NotAvailableException expected but not thrown");
        } catch (NotAvailableException ignore) { }
        
        decomposer.decompose();
        
        l = decomposer.getL();
        r = decomposer.getR();
        assertTrue(decomposer.isSPD());
        
        l2 = r.transposeAndReturnNew();
        r2 = l.transposeAndReturnNew();
        
        assertTrue(l.equals(l2, ABSOLUTE_ERROR));
        assertTrue(r.equals(r2, ABSOLUTE_ERROR));
        
        assertEquals(r.getRows(), rows);
        assertEquals(r.getColumns(), rows);
        
        //Check that factors l and initL are equal
        for (int j = 0; j < r.getColumns(); j++) {
            for (int i = 0; i < r.getRows(); i++) {
                assertEquals(initR.getElementAt(i, j),
                        r.getElementAt(i, j), ABSOLUTE_ERROR);
            }
        }
    }
    
    @Test
    public void testIsSPD() throws WrongSizeException, LockedException, 
            NotAvailableException, NotReadyException, DecomposerException {
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        Matrix initL, m;
        
        CholeskyDecomposer decomposer = new CholeskyDecomposer();
        
        //Test for symmetric positive definite matrix
        initL = DecomposerHelper.getLeftLowerTriangulatorFactor(rows);
        m = DecomposerHelper.getSymmetricPositiveDefiniteMatrixInstance(initL);
        
        decomposer.setInputMatrix(m);
        
        //Force NotAvailableException
        try {
            decomposer.isSPD();
            fail("NotAvailableException expected but not thrown");
        } catch (NotAvailableException ignore) { }
        
        decomposer.decompose();
        
        assertTrue(decomposer.isSPD());
        
        //we build a non symmetric positive definite matrix using m
        for (int v = 0; v < rows; v++) {
            for (int u = 0; u < v; u++) {
                m.setElementAt(u, v, m.getElementAt(v, u) + 1.0);
            }
        }
        
        decomposer.setInputMatrix(m);
        decomposer.decompose();
        
        assertFalse(decomposer.isSPD());
    }
    
    @Test
    public void testSolve() throws WrongSizeException, LockedException, 
            NonSymmetricPositiveDefiniteMatrixException, NotReadyException,
            DecomposerException, NotAvailableException {
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix initL, m, b, s, b2;
        
        CholeskyDecomposer decomposer = new CholeskyDecomposer();
        
        //Test for symmetric positive definite matrix
        initL = DecomposerHelper.getLeftLowerTriangulatorFactor(rows);
        m = DecomposerHelper.getSymmetricPositiveDefiniteMatrixInstance(initL);
                
        b = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        decomposer.setInputMatrix(m);
        
        //Force NotAvailableException
        try {
            decomposer.solve(b);
            fail("NotAvailableException expected but not thrown");
        } catch (NotAvailableException ignore) { }
        
        decomposer.decompose();
        
        s = decomposer.solve(b);
        assertTrue(decomposer.isSPD());
        
        //Check that solution after calling solve matches following equation
        //m * s = b
        b2 = m.multiplyAndReturnNew(s);
        
        assertEquals(b2.getRows(), b.getRows());
        assertEquals(b2.getColumns(), b.getColumns());
        //check they are equal expect for machine accuracy
        assertTrue(b.equals(b2, ABSOLUTE_ERROR));
        
        
        //Force NonSymmetricPositiveDefiniteMatrixException
        
        //Test for non-symmetric positive definite matrices
        //we build a non symmetric positive definite matrix using m
        for (int v = 0; v < rows; v++) {
            for (int u = 0; u < v; u++) {
                m.setElementAt(u, v, m.getElementAt(v, u) + 1.0);
            }
        }
        
        decomposer.setInputMatrix(m);
        decomposer.decompose();        
        try {
            decomposer.solve(b);
            fail("NonSymmetricPositiveDefiniteMatrixException expected but not thrown");
        } catch (NonSymmetricPositiveDefiniteMatrixException ignore) { }
        
        //Force WrongsizeException
        
        //Test for parameters matrix b with different number of rows than input
        //matrix m
        b = Matrix.createWithUniformRandomValues(rows + 1, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        //since m has already been decomposed, we can directly solve
        try {
            decomposer.solve(b);
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
    }
}
