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
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class EconomyQRDecomposerTest {

    private static final int MIN_ROWS = 3;
    private static final int MAX_ROWS = 50;
    private static final int MIN_COLUMNS = 3;
    private static final int MAX_COLUMNS = 50;
    private static final double MIN_RANDOM_VALUE = 0.0;
    private static final double MIN_RANDOM_VALUE2 = 1.0;
    private static final double MAX_RANDOM_VALUE = 100.0;
    private static final double ROUND_ERROR = 1e-3;
    private static final double ABSOLUTE_ERROR = 1e-6;
    private static final double RELATIVE_ERROR_OVERDETERMINED = 0.35;
    private static final double VALID_RATIO = 0.25;

    @Test
    public void testConstructor() throws WrongSizeException, LockedException {

        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m = Matrix.createWithUniformRandomValues(rows, columns,
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        // Test 1st constructor
        EconomyQRDecomposer decomposer = new EconomyQRDecomposer();
        assertFalse(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getDecomposerType(),
                DecomposerType.QR_ECONOMY_DECOMPOSITION);

        decomposer.setInputMatrix(m);
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        assertEquals(decomposer.getDecomposerType(),
                DecomposerType.QR_ECONOMY_DECOMPOSITION);

        // Test 2nd constructor
        decomposer = new EconomyQRDecomposer(m);
        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
        assertEquals(decomposer.getDecomposerType(),
                DecomposerType.QR_ECONOMY_DECOMPOSITION);
    }

    @Test
    public void testGetSetInputMatrixAndIsReady() throws WrongSizeException,
            LockedException, NotReadyException {

        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m = Matrix.createWithUniformRandomValues(rows, columns,
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        final EconomyQRDecomposer decomposer = new EconomyQRDecomposer();

        assertEquals(decomposer.getDecomposerType(),
                DecomposerType.QR_ECONOMY_DECOMPOSITION);
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

        // When setting a new input matrix, decomposition becomes unavailable and
        // must be recomputed
        decomposer.setInputMatrix(m);

        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);
    }

    @Test
    public void testDecompose() throws WrongSizeException, NotReadyException,
            LockedException, NotAvailableException {

        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        final int rows = randomizer.nextInt(columns, MAX_ROWS + 1);

        final Matrix m;
        final Matrix q;
        final Matrix r;
        final Matrix m2;

        m = Matrix.createWithUniformRandomValues(rows, columns,
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        EconomyQRDecomposer decomposer = new EconomyQRDecomposer(m);

        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertFalse(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);

        decomposer.decompose();

        assertTrue(decomposer.isReady());
        assertFalse(decomposer.isLocked());
        assertTrue(decomposer.isDecompositionAvailable());
        assertEquals(decomposer.getInputMatrix(), m);

        // Check decomposition
        q = decomposer.getQ();
        r = decomposer.getR();

        m2 = q.multiplyAndReturnNew(r);

        assertEquals(m.getRows(), m2.getRows());
        assertEquals(m.getColumns(), m2.getColumns());
        for (int j = 0; j < m2.getColumns(); j++) {
            for (int i = 0; i < m2.getRows(); i++) {
                assertEquals(m.getElementAt(i, j), m2.getElementAt(i, j),
                        ROUND_ERROR);
            }
        }

        // Force NotReadyException
        decomposer = new EconomyQRDecomposer();
        try {
            decomposer.decompose();
            fail("NotReadyException expected but not thrown");
        } catch (final NotReadyException ignore) {
        }
    }

    @Test
    public void testIsFullRank() throws WrongSizeException, LockedException,
            NotReadyException, NotAvailableException {

        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        final int rows = randomizer.nextInt(columns + 1, MAX_ROWS + 3);

        Matrix m;
        final EconomyQRDecomposer decomposer = new EconomyQRDecomposer();

        // Test for any rectangular or square matrix that a matrix has full rank
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        decomposer.setInputMatrix(m);

        // Force NotAvailableException
        try {
            assertTrue(decomposer.isFullRank());
            fail("NotAvailableException expected but not thrown");
        } catch (final NotAvailableException ignore) {
        }

        decomposer.decompose();

        // Force IllegalArgumentException with a negative round error
        try {
            decomposer.isFullRank(-1.0);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        assertTrue(decomposer.isFullRank(ROUND_ERROR));

        // Test false case only for square matrix, for other sizes unreliable
        // results might be obtained because of rounding error
        m = DecomposerHelper.getSingularMatrixInstance(rows, rows);
        decomposer.setInputMatrix(m);
        decomposer.decompose();

        assertFalse(decomposer.isFullRank(ROUND_ERROR));

        // Try for a matrix having less rows than columns to force
        // WrongSizeException
        m = DecomposerHelper.getNonSingularMatrixInstance(columns, rows);
        decomposer.setInputMatrix(m);
        decomposer.decompose();
        try {
            decomposer.isFullRank();
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
    }

    @Test
    public void testGetH() throws WrongSizeException, LockedException,
            NotReadyException, NotAvailableException {

        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        final int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);

        final Matrix m;
        final Matrix h;

        final EconomyQRDecomposer decomposer = new EconomyQRDecomposer();

        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        decomposer.setInputMatrix(m);

        // Force NotAvailableException
        try {
            decomposer.getH();
            fail("NotAvailableException expected but not thrown");
        } catch (final NotAvailableException ignore) {
        }

        decomposer.decompose();
        h = decomposer.getH();

        assertEquals(h.getRows(), rows);
        assertEquals(h.getColumns(), columns);

        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                if (i < j) {
                    assertEquals(h.getElementAt(i, j), 0.0, 0.0);
                }
            }
        }
    }

    @Test
    public void testGetR() throws WrongSizeException, LockedException,
            NotReadyException, NotAvailableException {

        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        final int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);

        final Matrix m;
        final Matrix r;

        final EconomyQRDecomposer decomposer = new EconomyQRDecomposer();

        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        decomposer.setInputMatrix(m);

        // Force NotAvailableException
        try {
            decomposer.getR();
            fail("NotAvailableException expected but not thrown");
        } catch (final NotAvailableException ignore) {
        }

        decomposer.decompose();
        r = decomposer.getR();

        assertEquals(r.getRows(), columns);
        assertEquals(r.getColumns(), columns);

        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < columns; i++) {
                if (i > j) {
                    assertEquals(r.getElementAt(i, j), 0.0, ROUND_ERROR);
                }
            }
        }
    }

    @Test
    public void testGetQ() throws WrongSizeException, LockedException,
            NotReadyException, NotAvailableException {

        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        final int rows = randomizer.nextInt(columns + 1, MAX_ROWS + 3);

        Matrix m;
        Matrix q;
        Matrix qTransposed;
        Matrix test;

        final EconomyQRDecomposer decomposer = new EconomyQRDecomposer();

        // Test for non-square matrix having rows > columns
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        decomposer.setInputMatrix(m);

        // Force NotAvailableException
        try {
            decomposer.getQ();
            fail("NotAvailableException expected but not thrown");
        } catch (final NotAvailableException ignore) {
        }

        decomposer.decompose();
        q = decomposer.getQ();

        assertEquals(q.getRows(), rows);
        assertEquals(q.getColumns(), columns);

        // Q is an orthogonal matrix, which means that Q * Q' = I
        qTransposed = q.transposeAndReturnNew();

        test = qTransposed.multiplyAndReturnNew(q);

        assertEquals(test.getRows(), columns);
        assertEquals(test.getColumns(), columns);

        // Check that test is similar to identity
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < columns; i++) {
                if (i == j) {
                    assertEquals(test.getElementAt(i, j), 1.0, ROUND_ERROR);
                } else {
                    assertEquals(test.getElementAt(i, j), 0.0, ROUND_ERROR);
                }
            }
        }

        // Test for square matrix
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, rows);
        decomposer.setInputMatrix(m);

        // Force NotAvailableException
        try {
            decomposer.getQ();
            fail("NotAvailableException expected but not thrown");
        } catch (final NotAvailableException ignore) {
        }

        decomposer.decompose();
        q = decomposer.getQ();

        assertEquals(q.getRows(), rows);
        assertEquals(q.getColumns(), rows);

        // Q is an orthogonal matrix, which means that Q * Q' = I
        qTransposed = q.transposeAndReturnNew();

        test = qTransposed.multiplyAndReturnNew(q);

        assertEquals(test.getRows(), rows);
        assertEquals(test.getRows(), rows);

        // Check that test is similar to identity
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < rows; i++) {
                if (i == j) {
                    assertEquals(test.getElementAt(i, j), 1.0, ROUND_ERROR);
                } else {
                    assertEquals(test.getElementAt(i, j), 0.0, ROUND_ERROR);
                }
            }
        }


        // Test for matrix having rows < columns (Throws WrongSizeException)
        m = DecomposerHelper.getNonSingularMatrixInstance(columns, rows);
        decomposer.setInputMatrix(m);

        // Force NotAvailableException
        try {
            decomposer.getQ();
            fail("NotAvailableException expected but not thrown");
        } catch (final NotAvailableException ignore) {
        }

        decomposer.decompose();
        // Force WrongSizeException
        try {
            decomposer.getQ();
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
    }

    @Test
    public void testSolve() throws WrongSizeException,
            RankDeficientMatrixException, NotReadyException, LockedException,
            NotAvailableException {

        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS + 3, MAX_ROWS + 3);
        final int columns = randomizer.nextInt(MIN_COLUMNS, rows - 1);
        final int columns2 = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        Matrix m;
        Matrix b;
        Matrix s;
        Matrix b2;
        double relError;

        // Try for square matrix
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, rows);
        b = Matrix.createWithUniformRandomValues(rows, columns2,
                MIN_RANDOM_VALUE2, MAX_RANDOM_VALUE);

        final EconomyQRDecomposer decomposer = new EconomyQRDecomposer(m);

        // Force NotAvailableException
        try {
            decomposer.solve(b);
            fail("NotAvailableException expected but not thrown");
        } catch (final NotAvailableException ignore) {
        }

        decomposer.decompose();

        // Force IllegalArgumentException
        try {
            decomposer.solve(b, -1.0);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        s = decomposer.solve(b);

        // check that solution after calling solve matches following equation:
        // m * s = b
        b2 = m.multiplyAndReturnNew(s);

        assertEquals(b2.getRows(), b.getRows());
        assertEquals(b2.getColumns(), b.getColumns());
        for (int j = 0; j < b2.getColumns(); j++) {
            for (int i = 0; i < b2.getRows(); i++) {
                assertEquals(b.getElementAt(i, j), b2.getElementAt(i, j),
                        ABSOLUTE_ERROR);
            }
        }

        // Try for overdetermined system (rows > columns)
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        b = Matrix.createWithUniformRandomValues(rows, columns2,
                MIN_RANDOM_VALUE2, MAX_RANDOM_VALUE);

        decomposer.setInputMatrix(m);
        decomposer.decompose();

        // Force IllegalArgumentException
        try {
            decomposer.solve(b, -1.0);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        s = decomposer.solve(b);

        // check that solution after calling solve matches following equation:
        // m * s = b
        b2 = m.multiplyAndReturnNew(s);

        assertEquals(b2.getRows(), b.getRows());
        assertEquals(b2.getColumns(), b.getColumns());
        int valid = 0;
        final int total = b2.getColumns() * b2.getRows();
        for (int j = 0; j < b2.getColumns(); j++) {
            for (int i = 0; i < b2.getRows(); i++) {
                relError = Math.abs(RELATIVE_ERROR_OVERDETERMINED *
                        b2.getElementAt(i, j));
                if (Math.abs(b2.getElementAt(i, j) - b.getElementAt(i, j)) <
                        relError) {
                    valid++;
                }
            }
        }

        assertTrue((double) valid / (double) total > VALID_RATIO);

        // Try for matrix having rows < columns (Trhows WrongSizeException)
        m = DecomposerHelper.getNonSingularMatrixInstance(columns, rows);
        b = Matrix.createWithUniformRandomValues(columns, columns2,
                MIN_RANDOM_VALUE2, MAX_RANDOM_VALUE);
        decomposer.setInputMatrix(m);
        decomposer.decompose();

        // Force IllegalArgumentException
        try {
            decomposer.solve(b, -1.0);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        // Force WrongSizeException
        try {
            decomposer.solve(b);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }


        // Try for b matrix having different number of rows than m
        // (Throws WrongSizeException)
        m = DecomposerHelper.getNonSingularMatrixInstance(rows, columns);
        b = Matrix.createWithUniformRandomValues(columns, columns2,
                MIN_RANDOM_VALUE2, MAX_RANDOM_VALUE);
        decomposer.setInputMatrix(m);
        decomposer.decompose();
        try {
            decomposer.solve(b);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }

        // Test for rank deficient matrix only for squared matrices
        // (for other sizes, rank deficiency might not be detected and solve
        // method would execute)
        m = DecomposerHelper.getSingularMatrixInstance(rows, rows);
        b = Matrix.createWithUniformRandomValues(rows, columns2,
                MIN_RANDOM_VALUE2, MAX_RANDOM_VALUE);
        decomposer.setInputMatrix(m);
        decomposer.decompose();
        try {
            decomposer.solve(b, ROUND_ERROR);
            fail("RankDeficientMatrixException expected but not thrown");
        } catch (final RankDeficientMatrixException ignore) {
        }
    }
}
