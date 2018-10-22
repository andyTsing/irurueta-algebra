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

@SuppressWarnings("Duplicates")
public class RQDecomposerTest {
    
    private static final int MIN_ROWS = 1;
    private static final int MAX_ROWS = 50;
    
    private static final int MIN_COLUMNS = 1;
    private static final int MAX_COLUMNS = 50;
    
    private static final int MIN_RANDOM_VALUE = 0;
    private static final int MAX_RANDOM_VALUE = 100;
    
    private static final double RELATIVE_ERROR = 1.0;
    private static final double ROUND_ERROR = 1e-3;
    
    public RQDecomposerTest() { }

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
        
        RQDecomposer decomposer = new RQDecomposer();
        
        assertFalse(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getDecomposerType(), 
                DecomposerType.RQ_DECOMPOSITION);
        
        decomposer.setInputMatrix(m);
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        assertEquals(decomposer.getDecomposerType(), 
                DecomposerType.RQ_DECOMPOSITION);
        
        decomposer = new RQDecomposer(m);
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        assertEquals(decomposer.getDecomposerType(), 
                DecomposerType.RQ_DECOMPOSITION);
    }
    
    @Test
    public void testGetSetInputMatrix() throws WrongSizeException, 
            LockedException, NotReadyException, DecomposerException {
        
        //RQ decomposition works for any rectangular matrix size
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS + 1, MAX_COLUMNS + 1);
        int rows = randomizer.nextInt(MIN_ROWS, columns);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        RQDecomposer decomposer = new RQDecomposer();
        assertEquals(decomposer.getDecomposerType(), 
                DecomposerType.RQ_DECOMPOSITION);
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
        
        //When setting a new input matrix, decomposition becomes unavailable
        //and must be recomputed
        decomposer.setInputMatrix(m);
        
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
    }
    
    @Test
    public void testDecompose() throws WrongSizeException, LockedException,
            DecomposerException, NotReadyException, NotAvailableException {
        //Works for any rectangular matrix size having rows < columns (it also
        //works for square matrices)
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        int rows = randomizer.nextInt(MIN_ROWS, columns - 1);
        
        Matrix m, q, r, m2;
        
        m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        RQDecomposer decomposer = new RQDecomposer();
        
        //Force NotReadyException
        try {
            decomposer.decompose();
            fail("NotReadyException expected but not thrown");
        } catch (NotReadyException ignore) { }
        
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
        
        //Check decomposition
        r = decomposer.getR();
        q = decomposer.getQ();
        
        m2 = r.multiplyAndReturnNew(q);
        
        assertEquals(m.getRows(), m2.getRows());
        assertEquals(m.getColumns(), m2.getColumns());
        assertTrue(m.equals(m2, ROUND_ERROR));
        
        //Force DecomposerException
        m = Matrix.createWithUniformRandomValues(columns, rows, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        decomposer.setInputMatrix(m);
        
        try {
            decomposer.decompose();
            fail("DecomposerException expected but not thrown");
        } catch (DecomposerException ignore) { }
    }
    
    @Test
    public void testGetR() throws WrongSizeException, LockedException,
            NotReadyException, DecomposerException, NotAvailableException {

        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        int rows = randomizer.nextInt(MIN_ROWS, columns - 1);
        
        Matrix m, r;
        
        RQDecomposer decomposer = new RQDecomposer();
        
        m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        decomposer.setInputMatrix(m);
        
        //Force NotAvailableException
        try {
            decomposer.getR();
            fail("NotAvailableException expected but not thrown");
        } catch (NotAvailableException ignore) { }
        
        decomposer.decompose();
        r = decomposer.getR();
        
        assertEquals(r.getRows(), rows);
        assertEquals(r.getColumns(), columns);
        
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                if (i > j) {
                    assertEquals(r.getElementAt(i, j), 0.0, ROUND_ERROR);
                }
            }
        }
    }
    
    @Test
    public void testGetQ() throws WrongSizeException, LockedException,
            NotReadyException, DecomposerException, NotAvailableException {

        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        int rows = randomizer.nextInt(MIN_ROWS, columns - 1);
        
        Matrix m, q, qTransposed, test;
        
        RQDecomposer decomposer = new RQDecomposer();
        
        //Test for non-square matrix having rows <= columns
        m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        decomposer.setInputMatrix(m);
        
        //Force NotAvailableException
        try {
            decomposer.getQ();
            fail("NotAvailableException expected but not thrown");
        } catch (NotAvailableException ignore) { }
        
        decomposer.decompose();
        q = decomposer.getQ();
        
        assertEquals(q.getRows(), columns);
        assertEquals(q.getColumns(), columns);
        
        //Q is an orthogonal matrix, which mean that Q * Q' = I
        qTransposed = q.transposeAndReturnNew();
        
        test = qTransposed.multiplyAndReturnNew(q);
        
        assertEquals(test.getRows(), columns);
        assertEquals(test.getColumns(), columns);
        
        //Check that test is similar to identity
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < rows; i++) {
                if (i == j) {
                    assertEquals(Math.abs(test.getElementAt(i, j)), 1.0, 
                            RELATIVE_ERROR);
                } else {
                    assertEquals(test.getElementAt(i, j), 0.0, ROUND_ERROR);
                }
            }
        }
        
        //Test for square matrix
        m = Matrix.createWithUniformRandomValues(rows, rows, MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        decomposer.setInputMatrix(m);
        
        //Force NotAvailableException
        try {
            decomposer.getQ();
            fail("NotAvailableException expected but not thrown");
        } catch (NotAvailableException ignore) { }
        
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
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < rows; i++) {
                if (i == j) {
                    assertEquals(Math.abs(test.getElementAt(i, j)), 1.0, 
                            RELATIVE_ERROR);
                } else {
                    assertEquals(test.getElementAt(i, j), 0.0, ROUND_ERROR);
                }
            }
        }
    }
}
