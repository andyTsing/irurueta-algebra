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
public class UtilsTest {
    
    private static final double MIN_RANDOM_VALUE = 0.0;
    private static final double MAX_RANDOM_VALUE = 50.0;
    private static final int MIN_ROWS = 1;
    private static final int MAX_ROWS = 50;
    private static final int MIN_COLUMNS = 1;
    private static final int MAX_COLUMNS = 50;
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 100;

    private static final double ROUND_ERROR = 1e-3;
    private static final double BIG_ROUND_ERROR = 1.0;
    private static final double ABSOLUTE_ERROR = 1e-6;

    private static final int TIMES = 10;
    
    public UtilsTest() { }

    @BeforeClass
    public static void setUpClass() { }

    @AfterClass
    public static void tearDownClass() { }
    
    @Before
    public void setUp() { }
    
    @After
    public void tearDown() { }
    
    @Test
    public void testTrace() throws WrongSizeException {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double trace = 0.0;
        
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                if (i == j) {
                    trace += m.getElementAt(i, j);
                }
            }
        }
        
        assertEquals(trace, Utils.trace(m), ABSOLUTE_ERROR);
    }
    
    @Test
    public void testCond() throws WrongSizeException, NotReadyException, 
            LockedException, DecomposerException, NotAvailableException {
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        SingularValueDecomposer decomposer = new SingularValueDecomposer(m);
        double condNumber;
        
        decomposer.decompose();
        
        condNumber = decomposer.getConditionNumber();
        
        assertEquals(condNumber, Utils.cond(m), ABSOLUTE_ERROR);
    }
    
    @Test
    public void testRank() throws WrongSizeException, NotReadyException, 
            LockedException, DecomposerException, NotAvailableException {
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        SingularValueDecomposer decomposer = new SingularValueDecomposer(m);
        decomposer.decompose();
        
        int rank = decomposer.getRank();
        
        assertEquals(rank, Utils.rank(m), ABSOLUTE_ERROR);
    }
    
    @Test
    public void testDet() throws WrongSizeException, NotReadyException,
            LockedException, DecomposerException, NotAvailableException {

        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns;
        do {
            columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        } while (rows == columns);
        
        //Test for square matrix
        Matrix m = Matrix.createWithUniformRandomValues(rows, rows, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        LUDecomposer decomposer = new LUDecomposer(m);
        decomposer.decompose();
        
        double det = decomposer.determinant();
        
        assertEquals(det, Utils.det(m), ABSOLUTE_ERROR);
        
        //Test for non-square matrix (Force WrongSizeException)
        m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        try {
            Utils.det(m);
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
    }
    
    @Test
    public void testSolve() throws WrongSizeException, NotReadyException, 
            LockedException, DecomposerException, NotAvailableException,
            SingularMatrixException, RankDeficientMatrixException {
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_COLUMNS + 5, MAX_COLUMNS + 5);
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, rows - 1);
        int colsB = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
                
        Matrix m, b, s, s2;
        
        //Test for non-singular square matrix
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, rows);
        b = Matrix.createWithUniformRandomValues(rows, colsB, MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        
        LUDecomposer decomposer = new LUDecomposer(m);
        decomposer.decompose();
        
        s = decomposer.solve(b);
        s2 = Utils.solve(m, b);
        
        assertTrue(s.equals(s2, ABSOLUTE_ERROR));
        
        //Test for singular square matrix (Force RankDeficientMatrixException)
        m = DecomposerHelper.getSingularMatrixInstance(rows, rows);
        try {
            Utils.solve(m, b);
            fail("RankDeficientMatrixException expected but not thrown");
        } catch (RankDeficientMatrixException ignore) { }
        
        //Test for non-square (rows > columns) non-rank deficient matrix
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        b = Matrix.createWithUniformRandomValues(rows, colsB, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        EconomyQRDecomposer decomposer2 = new EconomyQRDecomposer(m);
        decomposer2.decompose();
        
        s = decomposer2.solve(b);
        s2 = Utils.solve(m, b);
        
        assertTrue(s.equals(s2, ABSOLUTE_ERROR));
        
        //Test for non-square (rows < columns) matrix (Force WrongSizeException)
        m = DecomposerHelper.getSingularMatrixInstance(columns, rows);
        b = Matrix.createWithUniformRandomValues(columns, colsB, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        try {
            Utils.solve(m, b);
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
        
        //Test for b having different number of rows than m
        m = DecomposerHelper.getSingularMatrixInstance(rows, columns);
        b = Matrix.createWithUniformRandomValues(columns, colsB, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        try {
            Utils.solve(m, b);
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
    }
    
    @Test
    public void testNormF() throws WrongSizeException {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        FrobeniusNormComputer normComputer = new FrobeniusNormComputer();
        
        double norm = normComputer.getNorm(m);
        assertEquals(norm, Utils.normF(m), ABSOLUTE_ERROR);
    }
    
    @Test
    public void testNormInf() throws WrongSizeException {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        InfinityNormComputer normComputer = new InfinityNormComputer();
        
        double norm = normComputer.getNorm(m);
        assertEquals(norm, Utils.normInf(m), ABSOLUTE_ERROR);
    }
    
    @Test
    public void testNorm2() throws WrongSizeException, NotReadyException, 
            LockedException, DecomposerException, NotAvailableException {
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        double norm2;
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        SingularValueDecomposer decomposer = new SingularValueDecomposer(m);
        decomposer.decompose();
        norm2 = decomposer.getNorm2();
        
        assertEquals(norm2, Utils.norm2(m), ABSOLUTE_ERROR);
    }
    
    @Test
    public void testNorm1() throws WrongSizeException {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = Matrix.createWithUniformRandomValues(rows, columns, 
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        OneNormComputer normComputer = new OneNormComputer();
        
        double norm = normComputer.getNorm(m);
        assertEquals(norm, Utils.norm1(m), ABSOLUTE_ERROR);
    }
    
    @Test
    public void testInverse() throws WrongSizeException, 
        RankDeficientMatrixException, DecomposerException {
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        int rows = randomizer.nextInt(columns + 1, MAX_ROWS + 3);
        
        Matrix m = DecomposerHelper.getNonSingularMatrixInstance(rows, rows);
        Matrix inverse = Utils.inverse(m);
        Matrix identity = m.multiplyAndReturnNew(inverse);
        //Check identity is correct
        assertTrue(identity.equals(Matrix.identity(rows, rows), ROUND_ERROR));
        
        //Test for singular square matrix (Force RankDeficientMatrixException)
        m = DecomposerHelper.getSingularMatrixInstance(rows, rows);
        try {
            Utils.inverse(m);
            fail("RankDeficientMatrixException expected but not thrown");
        } catch (RankDeficientMatrixException ignore) { }
        
        //Test for non-square (rows > columns) non-singular matrix to find
        //pseudoinverse, hence we use BIG_RELATIVE_ERROR to test correctness
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        inverse = Utils.inverse(m);
        
        identity = m.multiplyAndReturnNew(inverse);
        //Check identity is correct
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < rows; i++) {
                if (i == j) {
                    assertEquals(identity.getElementAt(i, j), 1.0, 
                            BIG_ROUND_ERROR);
                } else {
                    assertEquals(identity.getElementAt(i, j), 0.0, 
                            BIG_ROUND_ERROR);
                }
            }
        }
        
        //Test for non-square (rows < columns) matrix (Force WrongSizeException)
        m = DecomposerHelper.getSingularMatrixInstance(columns, rows);
        try {
            Utils.inverse(m);
        } catch (WrongSizeException ignore) { }
    }

    @Test
    public void testInverse2() throws WrongSizeException, 
            RankDeficientMatrixException, DecomposerException {
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        int rows = randomizer.nextInt(columns + 1, MAX_ROWS + 3);
        
        Matrix m = DecomposerHelper.getNonSingularMatrixInstance(rows, rows);
        Matrix inverse = m.clone();
        Utils.inverse(inverse, inverse);
        Matrix identity = m.multiplyAndReturnNew(inverse);
        //Check identity is correct
        assertTrue(identity.equals(Matrix.identity(rows, rows), ROUND_ERROR));
        
        //Test for singular square matrix (Force RankDeficientMatrixException)
        m = DecomposerHelper.getSingularMatrixInstance(rows, rows);
        try {
            Utils.inverse(m, inverse);
            fail("RankDeficientMatrixException expected but not thrown");
        } catch (RankDeficientMatrixException ignore) { }
        
        //Test for non-square (rows > columns) non-singular matrix to find
        //pseudoinverse, hence we use BIG_RELATIVE_ERROR to test correctness
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        inverse = new Matrix(rows, columns);
        Utils.inverse(m, inverse);
        
        identity = m.multiplyAndReturnNew(inverse);
        //Check identity is correct
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < rows; i++) {
                if (i == j) {
                    assertEquals(identity.getElementAt(i, j), 1.0, 
                            BIG_ROUND_ERROR);
                } else {
                    assertEquals(identity.getElementAt(i, j), 0.0, 
                            BIG_ROUND_ERROR);
                }
            }
        }
        
        //Test for non-square (rows < columns) matrix (Force WrongSizeException)
        m = DecomposerHelper.getSingularMatrixInstance(columns, rows);
        try {
            Utils.inverse(m, inverse);
        } catch (WrongSizeException ignore) { }
    }
    
    @Test
    public void testPseudoInverse() throws WrongSizeException, 
            DecomposerException {
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        int rows = randomizer.nextInt(columns + 1, MAX_ROWS + 3);
        
        Matrix m = DecomposerHelper.getNonSingularMatrixInstance(rows, rows);
        Matrix inverse = Utils.pseudoInverse(m);
        Matrix identity = m.multiplyAndReturnNew(inverse);
        //Check identity is correct and that pseudo-inverse is equal to the 
        //inverse
        assertTrue(identity.equals(Matrix.identity(rows, rows), ROUND_ERROR));
        
        //Test for singular square matrix
        m = DecomposerHelper.getSingularMatrixInstance(rows, rows);
        inverse = Utils.pseudoInverse(m);
        identity = m.multiplyAndReturnNew(inverse);
        //Check identity is correct and that pseudo-inverse is equal to the 
        //inverse
        assertTrue(identity.equals(Matrix.identity(rows, rows), 
                BIG_ROUND_ERROR));
        
        //Test for non-square (rows < columns) non-singular matrix
        m = DecomposerHelper.getNonSingularMatrixInstance(columns, rows);
        inverse = Utils.pseudoInverse(m);
        identity = m.multiplyAndReturnNew(inverse);
        assertTrue(identity.equals(Matrix.identity(columns, columns), 
                BIG_ROUND_ERROR));        
    }
    
    @Test
    public void testSkew1() throws WrongSizeException {

        double[] array = new double[3];
        array[0] = 1.0;
        array[1] = 4.0;
        array[2] = 2.0;
        
        Matrix m = Utils.skewMatrix(array);
        
        assertEquals(m.getElementAt(0, 1), -array[2], ABSOLUTE_ERROR);
        assertEquals(m.getElementAt(0, 2), array[1], ABSOLUTE_ERROR);
        assertEquals(m.getElementAt(1, 0), array[2], ABSOLUTE_ERROR);
        assertEquals(m.getElementAt(1, 2), -array[0], ABSOLUTE_ERROR);
        assertEquals(m.getElementAt(2, 0), -array[1], ABSOLUTE_ERROR);
        assertEquals(m.getElementAt(2, 1), array[0], ABSOLUTE_ERROR);
        
        Matrix m2 = new Matrix(3,3);
        Utils.skewMatrix(array, m2);
        
        assertEquals(m, m2);
        
        Matrix jacobian = new Matrix(9,3);
        Matrix m3 = new Matrix(3,3);
        Utils.skewMatrix(array, m3, jacobian);
        
        assertEquals(m, m3);
        
        assertEquals(jacobian.getElementAt(0, 0), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(1, 0), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(2, 0), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(3, 0), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(4, 0), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(5, 0), 1.0, 0.0);
        assertEquals(jacobian.getElementAt(6, 0), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(7, 0), -1.0, 0.0);
        assertEquals(jacobian.getElementAt(8, 0), 0.0, 0.0);
        
        assertEquals(jacobian.getElementAt(0, 1), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(1, 1), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(2, 1), -1.0, 0.0);
        assertEquals(jacobian.getElementAt(3, 1), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(4, 1), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(5, 1), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(6, 1), 1.0, 0.0);
        assertEquals(jacobian.getElementAt(7, 1), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(8, 1), 0.0, 0.0);
        
        assertEquals(jacobian.getElementAt(0, 2), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(1, 2), 1.0, 0.0);
        assertEquals(jacobian.getElementAt(2, 2), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(3, 2), -1.0, 0.0);
        assertEquals(jacobian.getElementAt(4, 2), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(5, 2), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(6, 2), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(7, 2), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(8, 2), 0.0, 0.0);
        
        //Force WrongSizeException
        try {
            Utils.skewMatrix(array, m3, new Matrix(1,1));
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
    }
    
    @Test
    public void testSkew2() throws WrongSizeException {
        Matrix m = new Matrix(3,1);
        m.setElementAt(0, 0, 1.0);
        m.setElementAt(1, 0, 4.0);
        m.setElementAt(2, 0, 3.0);
        
        Matrix mSkew = Utils.skewMatrix(m);
        
        assertEquals(mSkew.getElementAt(0, 1), -m.getElementAt(2, 0), ABSOLUTE_ERROR);
        assertEquals(mSkew.getElementAt(0, 2), m.getElementAt(1, 0), ABSOLUTE_ERROR);
        assertEquals(mSkew.getElementAt(1, 0), m.getElementAt(2, 0), ABSOLUTE_ERROR);
        assertEquals(mSkew.getElementAt(1, 2), -m.getElementAt(0, 0), ABSOLUTE_ERROR);
        assertEquals(mSkew.getElementAt(2, 0), -m.getElementAt(1, 0), ABSOLUTE_ERROR);
        assertEquals(mSkew.getElementAt(2, 1), m.getElementAt(0, 0), ABSOLUTE_ERROR);

        
        Matrix m2 = new Matrix(3,3);
        Utils.skewMatrix(m, m2);
        
        assertEquals(mSkew, m2);
        
        Matrix jacobian = new Matrix(9,3);
        Matrix m3 = new Matrix(3,3);
        Utils.skewMatrix(m, m3, jacobian);
        
        assertEquals(mSkew, m3);
        
        assertEquals(jacobian.getElementAt(0, 0), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(1, 0), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(2, 0), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(3, 0), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(4, 0), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(5, 0), 1.0, 0.0);
        assertEquals(jacobian.getElementAt(6, 0), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(7, 0), -1.0, 0.0);
        assertEquals(jacobian.getElementAt(8, 0), 0.0, 0.0);
        
        assertEquals(jacobian.getElementAt(0, 1), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(1, 1), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(2, 1), -1.0, 0.0);
        assertEquals(jacobian.getElementAt(3, 1), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(4, 1), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(5, 1), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(6, 1), 1.0, 0.0);
        assertEquals(jacobian.getElementAt(7, 1), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(8, 1), 0.0, 0.0);
        
        assertEquals(jacobian.getElementAt(0, 2), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(1, 2), 1.0, 0.0);
        assertEquals(jacobian.getElementAt(2, 2), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(3, 2), -1.0, 0.0);
        assertEquals(jacobian.getElementAt(4, 2), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(5, 2), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(6, 2), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(7, 2), 0.0, 0.0);
        assertEquals(jacobian.getElementAt(8, 2), 0.0, 0.0);  
        
        //Force WrongSizeException
        try {
            Utils.skewMatrix(m, m3, new Matrix(1,1));
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
    }
    
    @Test
    public void testCrossProduct1() throws WrongSizeException {

        double[] output;
        double[] array1 = new double[3];
        double[] array2 = new double[3];
        
        array1[0] = 1.0;
        array1[1] = 4.0;
        array1[2] = 2.0;
        
        array2[0] = 4.0;
        array2[1] = 2.0;
        array2[2] = 3.0;
        
        output = Utils.crossProduct(array1, array2);
        double[] output2 = new double[3];
        Utils.crossProduct(array1, array2, output2);
                
        assertEquals(output[0], 8.0, ABSOLUTE_ERROR);
        assertEquals(output[1], 5.0, ABSOLUTE_ERROR);
        assertEquals(output[2], -14.0, ABSOLUTE_ERROR);

        assertArrayEquals(output, output2, 0.0);
        
        Matrix jacobian1 = new Matrix(3,3);
        Matrix jacobian2 = new Matrix(3,3);
        
        Utils.crossProduct(array1, array2, output2, jacobian1, jacobian2);
        
        assertEquals(jacobian1, Utils.skewMatrix(array1).
                multiplyByScalarAndReturnNew(-1.0));
        assertEquals(jacobian2, Utils.skewMatrix(array2));
        
        //Force WrongSizeException
        try {
            Utils.crossProduct(array1, array2, new Matrix(1,1), jacobian2);
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
        try {
            Utils.crossProduct(array1, array2, jacobian1, new Matrix(1,1));
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
        
        try {
            Utils.crossProduct(array1, array2, new double[1], jacobian1, 
                    jacobian2);
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
        try {
            Utils.crossProduct(array1, array2, output2, new Matrix(1,1), 
                    jacobian2);
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
        try {
            Utils.crossProduct(array1, array2, output2, jacobian1, 
                    new Matrix(1,1));
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
        
    }
    
    @Test
    public void testCrossProduct2() throws WrongSizeException {

        Matrix output;
        double[] array = new double[3];
        Matrix m = new Matrix(3,3);

        array[0] = 1.0;
        array[1] = 4.0;
        array[2] = 2.0;

        // first row
        m.setElementAt(0,0, 4.0);
        m.setElementAt(1,0, 2.0);
        m.setElementAt(2,0, 3.0);

        // second row
        m.setElementAt(0,1, 3.0);
        m.setElementAt(1,1, 1.0);
        m.setElementAt(2,1, 7.0);

        // third row
        m.setElementAt(0,2, 8.0);
        m.setElementAt(1,2, 1.0);
        m.setElementAt(2,2, 3.0);

        output = Utils.crossProduct(array, m);

        assertEquals(output.getElementAt(0,0), 8.0, ABSOLUTE_ERROR);
        assertEquals(output.getElementAt(1,0), 5.0, ABSOLUTE_ERROR);
        assertEquals(output.getElementAt(2,0), -14.0, ABSOLUTE_ERROR);

        assertEquals(output.getElementAt(0,1), 26.0, ABSOLUTE_ERROR);
        assertEquals(output.getElementAt(1,1), -1.0, ABSOLUTE_ERROR);
        assertEquals(output.getElementAt(2,1), -11.0, ABSOLUTE_ERROR);

        assertEquals(output.getElementAt(0,2), 10.0, ABSOLUTE_ERROR);
        assertEquals(output.getElementAt(1,2), 13.0, ABSOLUTE_ERROR);
        assertEquals(output.getElementAt(2,2), -31.0, ABSOLUTE_ERROR);
    }    
    
    @Test
    public void testIsSymmetric() throws WrongSizeException {
        int numValid = 0;
        for (int t = 0; t < TIMES; t++) {
            UniformRandomizer randomizer = new UniformRandomizer(new Random());
            int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);

            Matrix m = DecomposerHelper.getSymmetricMatrix(rows);

            assertTrue(Utils.isSymmetric(m));
            assertTrue(Utils.isSymmetric(m, ABSOLUTE_ERROR));

            //now make matrix non symmetric
            m.setElementAt(0, rows - 1, m.getElementAt(0, rows - 1) + 1.0);

            if (Utils.isSymmetric(m)) {
                continue;
            }
            assertFalse(Utils.isSymmetric(m));
            assertFalse(Utils.isSymmetric(m, ABSOLUTE_ERROR));

            //but if we provide a threshold large enough, matrix will still be
            //considered to be symmetric
            assertTrue(Utils.isSymmetric(m, 1.0));

            numValid++;
            break;
        }

        assertTrue(numValid > 0);
    }
    
    @Test
    public void testIsOrthonormalAndIsOrthogonal() throws WrongSizeException {
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        Matrix m = DecomposerHelper.getOrthonormalMatrix(rows);
        
        assertTrue(Utils.isOrthonormal(m));
        assertTrue(Utils.isOrthonormal(m, ABSOLUTE_ERROR));
        assertTrue(Utils.isOrthogonal(m));
        assertTrue(Utils.isOrthogonal(m, ABSOLUTE_ERROR));
        
        //if we scale the matrix it will no longer will be orthonormal, but it
        //will continue to be orthogonal
        m.multiplyByScalar(2.0);
        
        assertFalse(Utils.isOrthonormal(m));
        assertFalse(Utils.isOrthonormal(m, ABSOLUTE_ERROR));
        assertTrue(Utils.isOrthogonal(m));
        assertTrue(Utils.isOrthogonal(m, ABSOLUTE_ERROR));
        //unless threshold is large enough, in which case matrix will still be
        //considered as orthonormal
        assertTrue(Utils.isOrthonormal(m, 2.0));
        
        //a singular matrix won't be either orthogonal or orthonormal
        m = DecomposerHelper.getSingularMatrixInstance(rows, rows);
        assertFalse(Utils.isOrthogonal(m));
        assertFalse(Utils.isOrthogonal(m, ABSOLUTE_ERROR));
        assertFalse(Utils.isOrthonormal(m));
        assertFalse(Utils.isOrthonormal(m, ABSOLUTE_ERROR));
        
        //A non-square matrix won't be orthogonal or orthonormal
        m = new Matrix(rows, rows + 1);
        assertFalse(Utils.isOrthogonal(m));
        assertFalse(Utils.isOrthogonal(m, ABSOLUTE_ERROR));
        assertFalse(Utils.isOrthonormal(m));
        assertFalse(Utils.isOrthonormal(m, ABSOLUTE_ERROR));

        //Force IllegalArgumentException (by setting negative threshold)
        try {
            Utils.isOrthogonal(m, -ABSOLUTE_ERROR);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            Utils.isOrthonormal(m, -ABSOLUTE_ERROR);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testDotProduct() throws WrongSizeException {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH + 1, MAX_LENGTH);
        
        double[] input1 = new double[length];
        double[] input2 = new double[length];
        randomizer.fill(input1, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        randomizer.fill(input2, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        double expectedResult = 0.0;
        for (int i = 0; i < length; i++) {
            expectedResult += input1[i] * input2[i];
        }
        
        double result = Utils.dotProduct(input1, input2);
        
        //check correctness
        assertEquals(result, expectedResult, 0.0);
        
        //Force IllegalArgumentException
        double[] wrongArray = new double[length + 1];
        try {
            //noinspection all
            Utils.dotProduct(input1, wrongArray);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try{
            //noinspection all
            Utils.dotProduct(wrongArray, input2);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        
        //test with jacobians
        Matrix jacobian1 = new Matrix(1,length);
        Matrix jacobian2 = new Matrix(1,length);
        result = Utils.dotProduct(input1, input2, jacobian1, jacobian2);
        
        //check correctness
        assertEquals(result, expectedResult, 0.0);
        
        assertArrayEquals(jacobian1.getBuffer(), input1, 0.0);
        assertArrayEquals(jacobian2.getBuffer(), input2, 0.0);
        
        //Force IllegalArgumentException
        try {
            Utils.dotProduct(wrongArray, input2, jacobian1, jacobian2);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            Utils.dotProduct(input1, wrongArray, jacobian1, jacobian2);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore){ }
        try {
            Utils.dotProduct(input1, input2, new Matrix(1,1), jacobian2);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            Utils.dotProduct(input1, input2, jacobian1, new Matrix(1,1));
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        
        
        //test with matrices
        Matrix m1 = Matrix.newFromArray(input1, false);
        Matrix m2 = Matrix.newFromArray(input2, true);
        
        result = Utils.dotProduct(m1, m2);
        
        //check correctness
        assertEquals(result, expectedResult, 0.0);        
        
        //Force WrongSizeException
        Matrix wrongMatrix = new Matrix(length + 1, 1);
        try {
            Utils.dotProduct(m1, wrongMatrix);
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
        try {
            Utils.dotProduct(wrongMatrix, m2);
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
        
        //test with jacobians
        result = Utils.dotProduct(m1, m2, jacobian1, jacobian2);
        
        //check correctness
        assertEquals(result, expectedResult, 0.0);
        
        assertArrayEquals(jacobian1.getBuffer(), m1.getBuffer(), 0.0);
        assertArrayEquals(jacobian2.getBuffer(), m2.getBuffer(), 0.0);
        
        //Force WrongSizeException
        try {
            Utils.dotProduct(wrongMatrix, m2, jacobian1, jacobian2);
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
        try {
            Utils.dotProduct(m1, wrongMatrix, jacobian1, jacobian2);
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
        
        //Force IllegalArgumentException
        try {
            Utils.dotProduct(m1, m2, new Matrix(1,1), jacobian2);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            Utils.dotProduct(m1, m2, jacobian1, new Matrix(1,1));
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testSchurc() throws WrongSizeException, DecomposerException,
            RankDeficientMatrixException {

        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int size = randomizer.nextInt(MIN_ROWS + 1, MAX_ROWS);
        int pos = randomizer.nextInt(1, size);
        
        Matrix m = DecomposerHelper.getSymmetricPositiveDefiniteMatrixInstance(
                DecomposerHelper.getLeftLowerTriangulatorFactor(size));

        //as defined in https://en.wikipedia.org/wiki/Schur_complement
        Matrix A = m.getSubmatrix(0, 0, pos - 1, pos - 1);
        Matrix B = m.getSubmatrix(0, pos, pos - 1, size - 1);
        Matrix C = m.getSubmatrix(pos, 0, size - 1, pos - 1);
        Matrix D = m.getSubmatrix(pos, pos, size - 1, size - 1);
        
        assertTrue(B.equals(C.transposeAndReturnNew(), ABSOLUTE_ERROR));
        
        
        //test 1st schurc method        
        
        //test with pos from start, and sqrt
        Matrix result = new Matrix(size - pos, size - pos);
        Matrix iA = new Matrix(pos, pos);
        Utils.schurc(m, pos, true, true, result, iA);
        
        //check correctness
        //(result is the sqr root of the Schur complement of A)
        assertEquals(result.getRows(), size - pos);
        assertEquals(result.getColumns(), size - pos);
        
        //Schur complement of A is: M/A = D - C*A^-1*B,
        //but result is the sqr root of that (an upper triangle matrix)
        Matrix result2 = D.subtractAndReturnNew(C.multiplyAndReturnNew(
                Utils.inverse(A).multiplyAndReturnNew(B)));
        Matrix schurc = result.transposeAndReturnNew().
                multiplyAndReturnNew(result);
        assertTrue(schurc.equals(result2, ABSOLUTE_ERROR));
        
        //iA is the inverse of A
        assertEquals(iA.getRows(), pos);
        assertEquals(iA.getColumns(), pos);
        assertTrue(A.multiplyAndReturnNew(iA).equals(Matrix.identity(pos, pos), 
                ABSOLUTE_ERROR));
                        
        //test with pos from start, and no sqrt
        result = new Matrix(size - pos, size - pos);
        iA = new Matrix(pos, pos);
        Utils.schurc(m, pos, true, false, result, iA);
        
        //check correctness
        assertEquals(result.getRows(), size - pos);
        assertEquals(result.getColumns(), size - pos);        
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        //iA is the inverse of A
        assertEquals(iA.getRows(), pos);
        assertEquals(iA.getColumns(), pos);
        assertTrue(A.multiplyAndReturnNew(iA).equals(Matrix.identity(pos, pos), 
                ABSOLUTE_ERROR));

        
        //test with pos from end, with sqrt
        result = new Matrix(pos, pos);
        iA = new Matrix(size - pos, size - pos);
        Utils.schurc(m, pos, false, true, result, iA);
        
        //check correctness
        assertEquals(result.getRows(), pos);
        assertEquals(result.getColumns(), pos);
        
        //Schur complement of D is: M/D = A - B*D^-1*C
        //but result is the sqrt root of that (an upper triangle matrix)
        result2 = A.subtractAndReturnNew(B.multiplyAndReturnNew(
                Utils.inverse(D).multiplyAndReturnNew(C)));
        schurc = result.transposeAndReturnNew().multiplyAndReturnNew(result);
        assertTrue(schurc.equals(result2, ABSOLUTE_ERROR));
        
        //iA is the inverse of D
        assertEquals(iA.getRows(), size - pos);
        assertEquals(iA.getColumns(), size - pos);
        assertTrue(D.multiplyAndReturnNew(iA).equals(
                Matrix.identity(size - pos, size - pos), ABSOLUTE_ERROR));
        
        
        //test with pos from end, and no sqrt
        result = new Matrix(pos, pos);
        iA = new Matrix(size - pos, size - pos);
        Utils.schurc(m, pos, false, false, result, iA);
        
        //check correctness
        assertEquals(result.getRows(), pos);
        assertEquals(result.getColumns(), pos);
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        //iA is the inverse of D
        assertEquals(iA.getRows(), size - pos);
        assertEquals(iA.getColumns(), size - pos);
        assertTrue(D.multiplyAndReturnNew(iA).equals(
                Matrix.identity(size - pos, size - pos), ABSOLUTE_ERROR));

        //Force IllegalArgumentException
        Matrix wrong = new Matrix(size, size + 1);
        try {
            Utils.schurc(wrong, pos, true, true, result, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            Utils.schurc(m, size, true, true, result, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            Utils.schurc(m, 0, true, true, result, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        
        //Force RankDeficientMatrixException
        Matrix m2 = new Matrix(size, size);
        try {
            Utils.schurc(m2, pos, true, true, result, iA);
            fail("RankDeficientMatrixException expected but not thrown");
        } catch (RankDeficientMatrixException ignore) { }

        
        //test 2nd schurc method
        
        //test with pos from start and no sqrt
        result = new Matrix(size - pos, size - pos);
        iA = new Matrix(pos, pos);
        Utils.schurc(m, pos, true, result, iA);
        
        //check correctness
        assertEquals(result.getRows(), size - pos);
        assertEquals(result.getColumns(), size - pos);
        
        //Schur complement of A is: M/A = D - C*A^-1*B,
        result2 = D.subtractAndReturnNew(C.multiplyAndReturnNew(
                Utils.inverse(A).multiplyAndReturnNew(B)));
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));

        //iA is the inverse of A
        assertEquals(iA.getRows(), pos);
        assertEquals(iA.getColumns(), pos);
        assertTrue(A.multiplyAndReturnNew(iA).equals(Matrix.identity(pos, pos), 
                ABSOLUTE_ERROR));
        
        
        //test with pos from end and no sqrt
        result = new Matrix(pos, pos);
        iA = new Matrix(size - pos, size - pos);
        Utils.schurc(m, pos, false, result, iA);
        
        //check correctness
        assertEquals(result.getRows(), pos);
        assertEquals(result.getColumns(), pos);
        
        //Schur complement of D is: M/D = A - B*D^-1*C
        result2 = A.subtractAndReturnNew(B.multiplyAndReturnNew(
                Utils.inverse(D).multiplyAndReturnNew(C)));
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        //iA is the inverse of D
        assertEquals(iA.getRows(), size - pos);
        assertEquals(iA.getColumns(), size - pos);
        assertTrue(D.multiplyAndReturnNew(iA).equals(
                Matrix.identity(size - pos, size - pos), ABSOLUTE_ERROR));        
        
        //Force IllegalArgumentException
        try {
            Utils.schurc(wrong, pos, false, result, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            Utils.schurc(m, size, true, result, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            Utils.schurc(m, 0, false, result, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }

        //Force RankDeficientMatrixException
        try {
            Utils.schurc(m2, pos, true, result, iA);
            fail("RankDeficientMatrixException expected but not thrown");
        } catch (RankDeficientMatrixException ignore) { }

        
        //test 3rd schurc method
        
        //test with pos from start, and no sqrt
        result = new Matrix(size - pos, size - pos);
        iA = new Matrix(pos, pos);
        Utils.schurc(m, pos, result, iA);
        
        //check correctness
        assertEquals(result.getRows(), size - pos);
        assertEquals(result.getColumns(), size - pos);        
        
        //Schur complement of A is: M/A = D - C*A^-1*B,
        result2 = D.subtractAndReturnNew(C.multiplyAndReturnNew(
                Utils.inverse(A).multiplyAndReturnNew(B))); 
        
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        //iA is the inverse of A
        assertEquals(iA.getRows(), pos);
        assertEquals(iA.getColumns(), pos);
        assertTrue(A.multiplyAndReturnNew(iA).equals(Matrix.identity(pos, pos), 
                ABSOLUTE_ERROR));       
        
        //Force IllegalArgumentException
        try {
            Utils.schurc(wrong, pos, result, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            Utils.schurc(m, size, result, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            Utils.schurc(m, 0, result, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        
        //Force RankDeficientMatrixException
        try {
            Utils.schurc(m2, pos, result, iA);
            fail("RankDeficientMatrixException expected but not thrown");
        } catch (RankDeficientMatrixException ignore) { }

        
        
        //test 4th schurc method 
        //(which returns new instance)
        
        //test with pos from start, and sqrt
        iA = new Matrix(pos, pos);
        result = Utils.schurcAndReturnNew(m, pos, true, true, iA);
        
        //check correctness
        //(result is the sqrt root of the Schur complement of A)
        assertEquals(result.getRows(), size - pos);
        assertEquals(result.getColumns(), size - pos);
        
        //Schur complement of A is: M/A = D - C*A^-1*B,
        //but result is the sqrt root of that (an upper triangle matrix)
        result2 = D.subtractAndReturnNew(C.multiplyAndReturnNew(
                Utils.inverse(A).multiplyAndReturnNew(B)));
        schurc = result.transposeAndReturnNew().multiplyAndReturnNew(result);
        assertTrue(schurc.equals(result2, ABSOLUTE_ERROR));
        
        //iA is the inverse of A
        assertEquals(iA.getRows(), pos);
        assertEquals(iA.getColumns(), pos);
        assertTrue(A.multiplyAndReturnNew(iA).equals(Matrix.identity(pos, pos), 
        ABSOLUTE_ERROR));
        
        //test with pos from start, and no sqrt
        iA = new Matrix(pos, pos);
        result = Utils.schurcAndReturnNew(m, pos, true, false, iA);
        
        //check correctness
        assertEquals(result.getRows(), size - pos);
        assertEquals(result.getColumns(), size - pos);
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        //iA is the inverse of A
        assertEquals(iA.getRows(), pos);
        assertEquals(iA.getColumns(), pos);
        assertTrue(A.multiplyAndReturnNew(iA).equals(Matrix.identity(pos, pos),
                ABSOLUTE_ERROR));
        
        
        //test with pos from end, with sqrt
        iA = new Matrix(size - pos, size - pos);
        result = Utils.schurcAndReturnNew(m, pos, false, true, iA);
        
        //check correctness
        assertEquals(result.getRows(), pos);
        assertEquals(result.getColumns(), pos);
        
        //Schur complement of D is: M/D = A - B*D^-1*C
        //but result is the sqrt root of that (an upper triangle matrix)
        result2 = A.subtractAndReturnNew(B.multiplyAndReturnNew(
                Utils.inverse(D).multiplyAndReturnNew(C)));
        schurc = result.transposeAndReturnNew().multiplyAndReturnNew(result);
        assertTrue(schurc.equals(result2, ABSOLUTE_ERROR));
        
        //iA is the inverse of D
        assertEquals(iA.getRows(), size - pos);
        assertEquals(iA.getColumns(), size - pos);
        assertTrue(D.multiplyAndReturnNew(iA).equals(
                Matrix.identity(size - pos, size - pos), ABSOLUTE_ERROR));
        
        
        //test with pos from end, and no sqrt
        iA = new Matrix(size - pos, size - pos);
        result = Utils.schurcAndReturnNew(m, pos, false, false, iA);
        
        //check correctness
        assertEquals(result.getRows(), pos);
        assertEquals(result.getColumns(), pos);
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        //iA is the inverse of D
        assertEquals(iA.getRows(), size - pos);
        assertEquals(iA.getColumns(), size - pos);
        assertTrue(D.multiplyAndReturnNew(iA).equals(
                Matrix.identity(size - pos, size - pos), ABSOLUTE_ERROR));
        
        //Force IllegalArgumentException
        result = null;
        try {
            result = Utils.schurcAndReturnNew(wrong, pos, true, true, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            result = Utils.schurcAndReturnNew(m, size, true, true, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            result = Utils.schurcAndReturnNew(m, 0, true, true, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        
        //Force RankDeficientMatrixException
        try {
            result = Utils.schurcAndReturnNew(m2, pos, true, true, iA);
            fail("RankDeficientMatrixException expected but not thrown");
        }catch (RankDeficientMatrixException ignore) { }
        assertNull(result);
        
        
        //test 5th schurc method
        //(which returns new instance)
        
        //test with pos from start and no sqrt
        iA = new Matrix(pos, pos);
        result = Utils.schurcAndReturnNew(m, pos, true, iA);
        
        //check correctness
        assertEquals(result.getRows(), size - pos);
        assertEquals(result.getColumns(), size - pos);
        
        //Schur complement of A is: M/A = D - C*A^-1*B
        result2 = D.subtractAndReturnNew(C.multiplyAndReturnNew(
                Utils.inverse(A)).multiplyAndReturnNew(B));
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        //iA is the inverse of A
        assertEquals(iA.getRows(), pos);
        assertEquals(iA.getColumns(), pos);
        assertTrue(A.multiplyAndReturnNew(iA).equals(Matrix.identity(pos, pos),
                ABSOLUTE_ERROR));
        
        
        //test with pos from end and no sqrt
        iA = new Matrix(size - pos, size - pos);
        result = Utils.schurcAndReturnNew(m, pos, false, iA);
        
        //check correctness
        assertEquals(result.getRows(), pos);
        assertEquals(result.getColumns(), pos);
        
        //Schur complement of D is: M/D = A - B*D^-1*C
        result2 = A.subtractAndReturnNew(B.multiplyAndReturnNew(
                Utils.inverse(D).multiplyAndReturnNew(C)));
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        //iA is the inverse of D
        assertEquals(iA.getRows(), size - pos);
        assertEquals(iA.getColumns(), size - pos);
        assertTrue(D.multiplyAndReturnNew(iA).equals(
                Matrix.identity(size - pos, size - pos), ABSOLUTE_ERROR));
        
        //Force IllegalArgumentException
        result = null;
        try {
            result = Utils.schurcAndReturnNew(wrong, pos, false, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            result = Utils.schurcAndReturnNew(m, size, true, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            result = Utils.schurcAndReturnNew(m, 0, false, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        
        //Force RankDeficientMatrixException
        try {
            result = Utils.schurcAndReturnNew(m2, pos, true, iA);
            fail("RankDeficientMatrixException expected but not thrown");
        } catch (RankDeficientMatrixException ignore) { }
        assertNull(result);
        
        
        //test 6th schurc method
        //(which returns new instance)
        
        //test with pos from start, and no sqrt
        iA = new Matrix(pos, pos);
        result = Utils.schurcAndReturnNew(m, pos, iA);
        
        //check correctness
        assertEquals(result.getRows(), size - pos);
        assertEquals(result.getColumns(), size - pos);
        
        //Schur complement of A is: M/A = D - C*A^-1*B
        result2 = D.subtractAndReturnNew(C.multiplyAndReturnNew(
                Utils.inverse(A).multiplyAndReturnNew(B)));
        
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        //iA is the inverse of A
        assertEquals(iA.getRows(), pos);
        assertEquals(iA.getColumns(), pos);
        assertTrue(A.multiplyAndReturnNew(iA).equals(Matrix.identity(pos, pos), 
                ABSOLUTE_ERROR));
        
        //Force IllegalArgumentException
        result = null;
        try {
            result = Utils.schurcAndReturnNew(wrong, pos, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            result = Utils.schurcAndReturnNew(m, size, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            result = Utils.schurcAndReturnNew(m, 0, iA);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        
        //Force RankDeficientMatrixException
        try {
            result = Utils.schurcAndReturnNew(m2, pos, iA);
            fail("RankDeficientMatrixException expected but not thrown");
        } catch (RankDeficientMatrixException ignore) { }
        assertNull(result);
        
        
        //test 7th schurc method
        
        //test with pos from start, and sqrt
        result = new Matrix(size - pos, size - pos);
        Utils.schurc(m, pos, true, true, result);
        
        //check correctness
        //(result is the sqrt root of the Schur complement of A)
        assertEquals(result.getRows(), size - pos);
        assertEquals(result.getColumns(), size - pos);
        
        //Schur complement of A is: M/A = D - C*A^-1*B
        //but result is the sqrt root of that (an upper triangle matrix)
        result2 = D.subtractAndReturnNew(C.multiplyAndReturnNew(
                Utils.inverse(A).multiplyAndReturnNew(B)));
        schurc = result.transposeAndReturnNew().multiplyAndReturnNew(result);
        assertTrue(schurc.equals(result2, ABSOLUTE_ERROR));
        
        
        //test with pos from start, and no sqrt
        result = new Matrix(size - pos, size - pos);
        Utils.schurc(m, pos, true, false, result);
        
        //check correctness
        assertEquals(result.getRows(), size - pos);
        assertEquals(result.getColumns(), size - pos);
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        
        //test with pos from end, with sqrt
        result = new Matrix(pos, pos);
        Utils.schurc(m, pos, false, true, result);
        
        //check correctness
        assertEquals(result.getRows(), pos);
        assertEquals(result.getColumns(), pos);
        
        //Schur complement of D is: M/D = A - B*D^-1*C
        //but result is the sqrt root of that (an upper triangle matrix)
        result2 = A.subtractAndReturnNew(B.multiplyAndReturnNew(
                Utils.inverse(D).multiplyAndReturnNew(C)));
        schurc = result.transposeAndReturnNew().multiplyAndReturnNew(result);
        assertTrue(schurc.equals(result2, ABSOLUTE_ERROR));
        
        
        //test with pos from end, and no sqrt
        result = new Matrix(pos, pos);
        Utils.schurc(m, pos, false, false, result);
        
        //check correctness
        assertEquals(result.getRows(), pos);
        assertEquals(result.getColumns(), pos);
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        //Force IllegalArgumentException
        try {
            Utils.schurc(wrong, pos, true, true, result);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            Utils.schurc(m, size, true, true, result);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            Utils.schurc(m, 0, true, true, result);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        
        
        //test 8th schurc method
        
        //test with pos from start and no sqrt
        result = new Matrix(size - pos, size - pos);
        Utils.schurc(m, pos, true, result);
        
        //check correctness
        assertEquals(result.getRows(), size - pos);
        assertEquals(result.getColumns(), size - pos);
        
        //Schur complement of A is: M/A = D - C*A^-1*B
        result2 = D.subtractAndReturnNew(C.multiplyAndReturnNew(
                Utils.inverse(A).multiplyAndReturnNew(B)));
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        
        //test with pos from end and no sqrt
        result = new Matrix(pos, pos);
        Utils.schurc(m, pos, false, result);
        
        //check correctness
        assertEquals(result.getRows(), pos);
        assertEquals(result.getColumns(), pos);
        
        //Schur complement of D is: M/D = A - B*D^-1*C
        result2 = A.subtractAndReturnNew(B.multiplyAndReturnNew(
                Utils.inverse(D).multiplyAndReturnNew(C)));
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        //Force IllegalArgumentException
        try {
            Utils.schurc(wrong, pos, false, result);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            Utils.schurc(m, size, true, result);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            Utils.schurc(m, 0, false, result);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        
        
        //test 9th schurc method
        
        //test with pos from start, and no sqrt
        result = new Matrix(size - pos, size - pos);
        Utils.schurc(m, pos, result);
        
        //check correctness
        assertEquals(result.getRows(), size - pos);
        assertEquals(result.getColumns(), size - pos);
        
        //Schur complement of A is: M/A = D - C*A^-1*B
        result2 = D.subtractAndReturnNew(C.multiplyAndReturnNew(
                Utils.inverse(A).multiplyAndReturnNew(B)));
        
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        //Force IllegalArgumentException
        try {
            Utils.schurc(wrong, pos, result);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            Utils.schurc(m, size, result);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            Utils.schurc(m, 0, result);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        
        
        //test 10th schurc method
        //(which returns new instance)
        
        //test with pos from start, and sqrt
        result = Utils.schurcAndReturnNew(m, pos, true, true);
        
        //check correctness
        //(result is the sqrt root of the Schur complement of A)
        assertEquals(result.getRows(), size - pos);
        assertEquals(result.getColumns(), size - pos);
        
        //Schur complement of A is: M/A = D - C*A^-1*B
        //but result is the sqrt root of that (an upper triangle matrix)
        result2 = D.subtractAndReturnNew(C.multiplyAndReturnNew(
                Utils.inverse(A).multiplyAndReturnNew(B)));
        schurc = result.transposeAndReturnNew().multiplyAndReturnNew(result);
        assertTrue(schurc.equals(result2, ABSOLUTE_ERROR));
        
        
        //test with pos from start, and no sqrt
        result = Utils.schurcAndReturnNew(m, pos, true, false);
        
        //check correctness
        assertEquals(result.getRows(), size - pos);
        assertEquals(result.getColumns(), size - pos);
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        
        //test with pos from end, with sqrt
        result = Utils.schurcAndReturnNew(m, pos, false, true);
        
        //check correctness
        assertEquals(result.getRows(), pos);
        assertEquals(result.getColumns(), pos);
        
        //Schur complement of D is: M/D = A - B*D^-1*C
        //but result is the sqrt root of that (an upper triangle matrix)
        result2 = A.subtractAndReturnNew(B.multiplyAndReturnNew(
                Utils.inverse(D).multiplyAndReturnNew(C)));
        schurc = result.transposeAndReturnNew().multiplyAndReturnNew(result);
        assertTrue(schurc.equals(result2, ABSOLUTE_ERROR));
        
        
        //test with pos from end, and no sqrt
        result = Utils.schurcAndReturnNew(m, pos, false, false);
        
        //check correctness
        assertEquals(result.getRows(), pos);
        assertEquals(result.getColumns(), pos);
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        //Force IllegalArgumentException
        result = null;
        try {
            result = Utils.schurcAndReturnNew(wrong, pos, true, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            result = Utils.schurcAndReturnNew(m, size, true, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            result = Utils.schurcAndReturnNew(m, 0, true, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        assertNull(result);
        
        
        //test 11th schurc method
        //(which returns new instance)
        
        //test with pos from start and no sqrt
        result = Utils.schurcAndReturnNew(m, pos, true);
        
        //check correctness
        assertEquals(result.getRows(), size - pos);
        assertEquals(result.getColumns(), size - pos);
        
        //Schur complement of A is: M/A = D - C*A^-1*B
        result2 = D.subtractAndReturnNew(C.multiplyAndReturnNew(
                Utils.inverse(A).multiplyAndReturnNew(B)));
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        
        //test with pos from end and no sqrt
        result = Utils.schurcAndReturnNew(m, pos, false);
        
        //check correctness
        assertEquals(result.getRows(), pos);
        assertEquals(result.getColumns(), pos);
        
        //Schur complement of D is: M/D = A - B*D^-1*C
        result2 = A.subtractAndReturnNew(B.multiplyAndReturnNew(
                Utils.inverse(D).multiplyAndReturnNew(C)));
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        //Force IllegalArgumentException
        result = null;
        try {
            result = Utils.schurcAndReturnNew(wrong, pos, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            result = Utils.schurcAndReturnNew(m, size, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            result = Utils.schurcAndReturnNew(m, 0, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        assertNull(result);
        
        
        //test 12th schurc method
        //(which returns new instance)
        
        //test with pos from start, and no sqrt
        result = Utils.schurcAndReturnNew(m, pos);
        
        //check correctness
        assertEquals(result.getRows(), size - pos);
        assertEquals(result.getColumns(), size - pos);
        
        //Schur complement of A is: M/A = D - C*A^-1*B
        result2 = D.subtractAndReturnNew(C.multiplyAndReturnNew(
                Utils.inverse(A).multiplyAndReturnNew(B)));
        
        assertTrue(result.equals(result2, ABSOLUTE_ERROR));
        
        //Force IllegalArgumentException
        result = null;
        try {
            result = Utils.schurcAndReturnNew(wrong, pos);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            result = Utils.schurcAndReturnNew(m, size);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            result = Utils.schurcAndReturnNew(m, 0);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        assertNull(result);
    }
}
