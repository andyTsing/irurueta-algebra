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

import java.security.SecureRandom;
import java.util.Random;

import static org.junit.Assert.*;

public class MatrixTest {

    private static final int MIN_ROWS = 1;
    private static final int MAX_ROWS = 50;
    private static final int MIN_COLUMNS = 1;
    private static final int MAX_COLUMNS = 50;

    private static final double MIN_RANDOM_VALUE = 0.0;
    private static final double MAX_RANDOM_VALUE = 100.0;

    private static final int TIMES = 10000;

    private static final double MEAN = 5;
    private static final double STANDARD_DEVIATION = 100.0;

    private static final double ABSOLUTE_ERROR = 1e-9;
    private static final double RELATIVE_ERROR = 0.1;

    @Test
    public void testConstructorGetRowsAndGetColumns()
            throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        Matrix m = new Matrix(rows, columns);
        assertNotNull(m);
        assertEquals(m.getRows(), rows);
        assertEquals(m.getColumns(), columns);

        // Force WrongSizeException
        m = null;
        try {
            m = new Matrix(0, 0);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        assertNull(m);
    }

    @Test
    public void testCopyConstructor() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m1 = new Matrix(rows, columns);
        assertNotNull(m1);
        assertEquals(m1.getRows(), rows);
        assertEquals(m1.getColumns(), columns);

        randomizer.fill(m1.getBuffer(), MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        final Matrix m2 = new Matrix(m1);

        // check
        assertEquals(m2.getRows(), rows);
        assertEquals(m2.getColumns(), columns);
        assertArrayEquals(m1.getBuffer(), m2.getBuffer(), 0.0);
    }

    @Test
    public void testGetSetElementAt() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m = new Matrix(rows, columns);
        final double[][] tmp = new double[rows][columns];
        double value;

        // initialize matrix and array to random values
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                value = randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE);
                tmp[i][j] = value;
                m.setElementAt(i, j, value);
            }
        }

        // check that matrix contains same values in array
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                value = tmp[i][j];
                assertEquals(m.getElementAt(i, j), value, 0.0);
            }
        }
    }

    @Test
    public void testGetIndex() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m = new Matrix(rows, columns);
        final double[][] tmp = new double[rows][columns];
        double value;

        // initialize matrix and array to random values
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                value = randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE);
                tmp[i][j] = value;
                m.setElementAt(i, j, value);
            }
        }

        // check that matrix contains same values in array and that it
        // corresponds to computed index
        int index;
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                index = j * rows + i;
                assertEquals(m.getIndex(i, j), index);
                value = tmp[i][j];
                assertEquals(m.getElementAt(i, j), value, 0.0);
                assertEquals(m.getElementAtIndex(index), value, 0.0);
            }
        }
    }

    @Test
    public void testGetSetElementAtIndex() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        final int length = rows * columns;
        int index;

        final Matrix m1 = new Matrix(rows, columns);
        final Matrix m2 = new Matrix(rows, columns);

        final double[] tmp = new double[length];
        double value;

        // initialize matrix and array to random value using column order
        for (int i = 0; i < length; i++) {
            value = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
            tmp[i] = value;
            m1.setElementAtIndex(i, value, true);
            m2.setElementAtIndex(i, value);
        }

        // check that matrices have the same values contained in array using
        // column order
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                index = j * rows + i;
                value = tmp[index];
                assertEquals(m1.getElementAt(i, j), value, 0.0);
                assertEquals(m1.getElementAtIndex(index), value, 0.0);
                assertEquals(m1.getElementAtIndex(index, true), value, 0.0);

                assertEquals(m2.getElementAt(i, j), value, 0.0);
                assertEquals(m2.getElementAtIndex(index), value, 0.0);
                assertEquals(m2.getElementAtIndex(index, true), value, 0.0);
            }
        }

        // initialize matrix m1 and array to random values using row order
        for (int i = 0; i < length; i++) {
            value = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
            tmp[i] = value;
            m1.setElementAtIndex(i, value, false);
        }

        // checks that matrix contains same values in array using row order
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                index = i * columns + j;
                value = tmp[index];
                assertEquals(m1.getElementAt(i, j), value, 0.0);
                assertEquals(m1.getElementAtIndex(index, false), value, 0.0);
            }
        }
    }

    @Test
    public void testClone() throws WrongSizeException, CloneNotSupportedException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        // instantiate matrix and fill with random values
        final Matrix m1 = new Matrix(rows, columns);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                m1.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
            }
        }

        // clone matrix
        final Matrix m2 = m1.clone();

        // check correctness
        assertEquals(m2.getRows(), rows);
        assertEquals(m2.getColumns(), columns);

        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                assertEquals(m1.getElementAt(i, j), m2.getElementAt(i, j), 0.0);
            }
        }
    }

    @Test
    public void testCopyTo() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        // instantiate matrix and fill with random values
        final Matrix m = new Matrix(rows, columns);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                m.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
            }
        }

        // instantiate destination matrix
        final Matrix destination = new Matrix(1, 1);
        assertEquals(destination.getRows(), 1);
        assertEquals(destination.getColumns(), 1);

        // copy to destination
        m.copyTo(destination);

        // check correctness
        assertEquals(destination.getRows(), rows);
        assertEquals(destination.getColumns(), columns);

        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                assertEquals(m.getElementAt(i, j),
                        destination.getElementAt(i, j), 0.0);
            }
        }

        // Force NullPointerException
        try {
            //noinspection ConstantConditions
            m.copyTo(null);
            fail("NullPointerException expected but not thrown");
        } catch (final NullPointerException ignore) {
        }
    }

    @Test
    public void testCopyFrom() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix source = new Matrix(rows, columns);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                source.setElementAt(i, j, randomizer.nextDouble(
                        MIN_RANDOM_VALUE, MAX_RANDOM_VALUE));
            }
        }

        // instantiate destination matrix
        final Matrix destination = new Matrix(1, 1);
        assertEquals(destination.getRows(), 1);
        assertEquals(destination.getColumns(), 1);

        // copy from source
        destination.copyFrom(source);

        // check correctness
        assertEquals(destination.getRows(), rows);
        assertEquals(destination.getColumns(), columns);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                assertEquals(destination.getElementAt(i, j),
                        source.getElementAt(i, j), 0.0);
            }
        }

        // Force NullPointerException
        try {
            //noinspection ConstantConditions
            destination.copyFrom(null);
            fail("NullPointerException expected but not thrown");
        } catch (final NullPointerException ignore) {
        }
    }

    @Test
    public void testAddAndReturnNew() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m1 = new Matrix(rows, columns);
        final Matrix m2 = new Matrix(rows, columns);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                m1.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
                m2.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
            }
        }

        Matrix m3 = m1.addAndReturnNew(m2);

        // check correctness
        assertEquals(m3.getRows(), rows);
        assertEquals(m3.getColumns(), columns);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                assertEquals(m3.getElementAt(i, j), m1.getElementAt(i, j) +
                        m2.getElementAt(i, j), ABSOLUTE_ERROR);
            }
        }

        // Force WrongSizeException
        final Matrix wrong = new Matrix(rows + 1, columns + 1);
        m3 = null;
        try {
            m3 = m1.addAndReturnNew(wrong);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        assertNull(m3);

        // Force NullPointerException
        try {
            //noinspection ConstantConditions
            m3 = m1.addAndReturnNew(null);
            fail("NullPointerException expected but not thrown");
        } catch (final NullPointerException ignore) {
        }
        assertNull(m3);
    }

    @Test
    public void testAdd() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m1 = new Matrix(rows, columns);
        final Matrix m2 = new Matrix(rows, columns);
        final double[][] tmp = new double[rows][columns];
        double value;
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                value = randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE);
                m1.setElementAt(i, j, value);
                tmp[i][j] = value;
                m2.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
            }
        }

        m1.add(m2);

        // check correctness
        assertEquals(m1.getRows(), rows);
        assertEquals(m1.getColumns(), columns);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                assertEquals(m1.getElementAt(i, j), tmp[i][j] +
                        m2.getElementAt(i, j), ABSOLUTE_ERROR);
            }
        }

        // Force WrongSizeException
        final Matrix wrong = new Matrix(rows + 1, columns + 1);
        try {
            m1.add(wrong);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }

        // Force NullPointerException
        try {
            //noinspection ConstantConditions
            m1.add(null);
            fail("NullPointerException expected but not thrown");
        } catch (final NullPointerException ignore) {
        }
    }

    @Test
    public void testSubtractAndReturnNew() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m1 = new Matrix(rows, columns);
        final Matrix m2 = new Matrix(rows, columns);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                m1.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
                m2.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
            }
        }

        Matrix m3 = m1.subtractAndReturnNew(m2);

        // check correctness
        assertEquals(m3.getRows(), rows);
        assertEquals(m3.getColumns(), columns);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                assertEquals(m3.getElementAt(i, j), m1.getElementAt(i, j) -
                        m2.getElementAt(i, j), ABSOLUTE_ERROR);
            }
        }

        // Force WrongSizeException
        Matrix wrong = new Matrix(rows + 1, columns + 1);
        m3 = null;
        try {
            m3 = m1.subtractAndReturnNew(wrong);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        assertNull(m3);

        // Force NullPointerException
        try {
            //noinspection ConstantConditions
            m3 = m1.subtractAndReturnNew(null);
            fail("NullPointerException expected but not thrown");
        } catch (final NullPointerException ignore) {
        }
        assertNull(m3);
    }

    @Test
    public void testSubtract() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m1 = new Matrix(rows, columns);
        final Matrix m2 = new Matrix(rows, columns);
        final double[][] tmp = new double[rows][columns];
        double value;
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                value = randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE);
                m1.setElementAt(i, j, value);
                tmp[i][j] = value;
                m2.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
            }
        }

        final Matrix m3 = new Matrix(rows, columns);
        m1.subtract(m2, m3);
        m1.subtract(m2);

        // check correctness
        assertEquals(m1.getRows(), rows);
        assertEquals(m1.getColumns(), columns);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                assertEquals(m1.getElementAt(i, j), tmp[i][j] -
                        m2.getElementAt(i, j), ABSOLUTE_ERROR);
            }
        }
        assertEquals(m1, m3);

        // Force WrongSizeExceptionException
        final Matrix wrong = new Matrix(rows + 1, columns + 1);
        try {
            m1.add(wrong);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }

        // Force NullPointerException
        try {
            //noinspection ConstantConditions
            m1.add(null);
            fail("NullPointerException expected but not thrown");
        } catch (final NullPointerException ignore) {
        }
    }

    @Test
    public void testMultiplyAndReturnNew() throws WrongSizeException {
        final int rows1 = 4;
        final int columns1 = 3;
        final int rows2 = 3;
        final int columns2 = 2;
        final Matrix m1 = new Matrix(rows1, columns1);
        final Matrix m2 = new Matrix(rows2, columns2);

        Matrix result;

        // fill m1 and m2 with predefined values
        m1.setElementAt(0, 0, 1.0);
        m1.setElementAt(0, 1, 2.0);
        m1.setElementAt(0, 2, 3.0);
        m1.setElementAt(1, 0, 4.0);
        m1.setElementAt(1, 1, 5.0);
        m1.setElementAt(1, 2, 6.0);
        m1.setElementAt(2, 0, 6.0);
        m1.setElementAt(2, 1, 5.0);
        m1.setElementAt(2, 2, 4.0);
        m1.setElementAt(3, 0, 3.0);
        m1.setElementAt(3, 1, 2.0);
        m1.setElementAt(3, 2, 1.0);

        m2.setElementAt(0, 0, 1.0);
        m2.setElementAt(0, 1, 2.0);
        m2.setElementAt(1, 0, 3.0);
        m2.setElementAt(1, 1, 4.0);
        m2.setElementAt(2, 0, 5.0);
        m2.setElementAt(2, 1, 6.0);

        // make matrix product
        result = m1.multiplyAndReturnNew(m2);

        // we know result for provided set of matrices m1 and m2, check it is
        // correct
        assertEquals(result.getRows(), rows1);
        assertEquals(result.getColumns(), columns2);

        assertEquals(result.getElementAt(0, 0), 22.0, ABSOLUTE_ERROR);
        assertEquals(result.getElementAt(0, 1), 28.0, ABSOLUTE_ERROR);
        assertEquals(result.getElementAt(1, 0), 49.0, ABSOLUTE_ERROR);
        assertEquals(result.getElementAt(1, 1), 64.0, ABSOLUTE_ERROR);
        assertEquals(result.getElementAt(2, 0), 41.0, ABSOLUTE_ERROR);
        assertEquals(result.getElementAt(2, 1), 56.0, ABSOLUTE_ERROR);
        assertEquals(result.getElementAt(3, 0), 14.0, ABSOLUTE_ERROR);
        assertEquals(result.getElementAt(3, 1), 20.0, ABSOLUTE_ERROR);


        // Force IllegalArgumentException
        final Matrix m3 = new Matrix(columns1, rows1);
        final Matrix m4 = new Matrix(columns2, rows2);

        result = null;
        try {
            result = m3.multiplyAndReturnNew(m4);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        assertNull(result);

        // Force NullPointerException
        try {
            //noinspection ConstantConditions
            result = m3.multiplyAndReturnNew(null);
            fail("NullPointerException expected but not thrown");
        } catch (final NullPointerException ignore) {
        }
        assertNull(result);
    }

    @Test
    public void testMultiply() throws WrongSizeException {
        final int rows1 = 4;
        final int columns1 = 3;
        final int rows2 = 3;
        final int columns2 = 2;
        final Matrix m1 = new Matrix(rows1, columns1);
        final Matrix m2 = new Matrix(rows2, columns2);

        // fill m1 and m2 with predefined values
        m1.setElementAt(0, 0, 1.0);
        m1.setElementAt(0, 1, 2.0);
        m1.setElementAt(0, 2, 3.0);
        m1.setElementAt(1, 0, 4.0);
        m1.setElementAt(1, 1, 5.0);
        m1.setElementAt(1, 2, 6.0);
        m1.setElementAt(2, 0, 6.0);
        m1.setElementAt(2, 1, 5.0);
        m1.setElementAt(2, 2, 4.0);
        m1.setElementAt(3, 0, 3.0);
        m1.setElementAt(3, 1, 2.0);
        m1.setElementAt(3, 2, 1.0);

        m2.setElementAt(0, 0, 1.0);
        m2.setElementAt(0, 1, 2.0);
        m2.setElementAt(1, 0, 3.0);
        m2.setElementAt(1, 1, 4.0);
        m2.setElementAt(2, 0, 5.0);
        m2.setElementAt(2, 1, 6.0);

        // make matrix product
        m1.multiply(m2);

        // we know result for provided set of matrices m1 and m2, check it is
        // correct
        assertEquals(m1.getRows(), rows1);
        assertEquals(m1.getColumns(), columns2);

        assertEquals(m1.getElementAt(0, 0), 22.0, ABSOLUTE_ERROR);
        assertEquals(m1.getElementAt(0, 1), 28.0, ABSOLUTE_ERROR);
        assertEquals(m1.getElementAt(1, 0), 49.0, ABSOLUTE_ERROR);
        assertEquals(m1.getElementAt(1, 1), 64.0, ABSOLUTE_ERROR);
        assertEquals(m1.getElementAt(2, 0), 41.0, ABSOLUTE_ERROR);
        assertEquals(m1.getElementAt(2, 1), 56.0, ABSOLUTE_ERROR);
        assertEquals(m1.getElementAt(3, 0), 14.0, ABSOLUTE_ERROR);
        assertEquals(m1.getElementAt(3, 1), 20.0, ABSOLUTE_ERROR);


        // Force IllegalArgumentException
        final Matrix m3 = new Matrix(columns1, rows1);
        final Matrix m4 = new Matrix(columns2, rows2);

        try {
            m3.multiply(m4);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }

        // Force NullPointerException
        try {
            //noinspection ConstantConditions
            m3.multiply(null);
            fail("NullPointerException expected but not thrown");
        } catch (NullPointerException ignore) {
        }
    }

    @Test
    public void testMultiplyKroneckerAndReturnNew() throws WrongSizeException {
        final Matrix m1 = new Matrix(2, 2);
        final Matrix m2 = new Matrix(2, 2);

        m1.setSubmatrix(0, 0, 1, 1, new double[]{1, 3, 2, 1});
        m2.setSubmatrix(0, 0, 1, 1, new double[]{0, 2, 3, 1});

        final Matrix m3 = m1.multiplyKroneckerAndReturnNew(m2);

        // check correctness
        assertEquals(m3.getRows(), 4);
        assertEquals(m3.getColumns(), 4);

        final Matrix m3b = new Matrix(4, 4);
        //noinspection all
        m3b.setSubmatrix(0, 0, 3, 3, new double[]{
                1 * 0, 1 * 2, 3 * 0, 3 * 2,
                1 * 3, 1 * 1, 3 * 3, 3 * 1,
                2 * 0, 2 * 2, 1 * 0, 1 * 2,
                2 * 3, 2 * 1, 1 * 3, 1 * 1
        });

        assertEquals(m3, m3b);
    }

    @Test
    public void testMultiplyKronecker() throws WrongSizeException {
        final Matrix m1 = new Matrix(2, 2);
        final Matrix m2 = new Matrix(2, 2);

        m1.setSubmatrix(0, 0, 1, 1, new double[]{1, 3, 2, 1});
        m2.setSubmatrix(0, 0, 1, 1, new double[]{0, 2, 3, 1});

        final Matrix m3 = new Matrix(2, 2);
        m1.multiplyKronecker(m2, m3);
        m1.multiplyKronecker(m2);

        // check correctness
        assertEquals(m3.getRows(), 4);
        assertEquals(m3.getColumns(), 4);

        assertEquals(m1.getRows(), 4);
        assertEquals(m1.getColumns(), 4);

        final Matrix m3b = new Matrix(4, 4);
        //noinspection all
        m3b.setSubmatrix(0, 0, 3, 3, new double[]{
                1 * 0, 1 * 2, 3 * 0, 3 * 2,
                1 * 3, 1 * 1, 3 * 3, 3 * 1,
                2 * 0, 2 * 2, 1 * 0, 1 * 2,
                2 * 3, 2 * 1, 1 * 3, 1 * 1
        });

        assertEquals(m1, m3b);
        assertEquals(m3, m3b);
    }

    @Test
    public void testMultiplyByScalarAndReturnNew() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m = new Matrix(rows, columns);
        // fill matrix
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                m.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
            }
        }

        final double scalar = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);

        final Matrix result = m.multiplyByScalarAndReturnNew(scalar);

        // check correctness
        assertEquals(result.getRows(), rows);
        assertEquals(result.getColumns(), columns);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                assertEquals(result.getElementAt(i, j),
                        m.getElementAt(i, j) * scalar, ABSOLUTE_ERROR);
            }
        }
    }

    @Test
    public void testMultiplyByScalar() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m = new Matrix(rows, columns);
        final double[][] tmp = new double[rows][columns];
        double value;
        // fill matrix
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                value = randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE);
                m.setElementAt(i, j, value);
                tmp[i][j] = value;
            }
        }

        final double scalar = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);

        m.multiplyByScalar(scalar);

        // check correctness
        assertEquals(m.getRows(), rows);
        assertEquals(m.getColumns(), columns);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                assertEquals(m.getElementAt(i, j),
                        tmp[i][j] * scalar, ABSOLUTE_ERROR);
            }
        }
    }

    @Test
    public void testEqualsAndHashCode() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m = new Matrix(rows, columns);
        final Matrix equal = new Matrix(rows, columns);
        final Matrix different1 = new Matrix(rows + 1, columns + 1);
        final Matrix different2 = new Matrix(rows, columns);
        final Object different3 = new Object();

        double value;
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                value = randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE);
                m.setElementAt(i, j, value);
                equal.setElementAt(i, j, value);
                different1.setElementAt(i, j, value);
                different2.setElementAt(i, j, value + 1.0);
            }
        }

        // check correctness
        //noinspection EqualsWithItself
        assertTrue(m.equals(m));
        assertTrue(m.equals(equal));
        assertFalse(m.equals(different1));
        assertFalse(m.equals(different2));
        assertNotEquals(m, different3);
        assertFalse(m.equals(null));

        assertEquals(m.hashCode(), equal.hashCode());

        // check with threshold
        assertTrue(m.equals(m, ABSOLUTE_ERROR));
        assertTrue(m.equals(equal, ABSOLUTE_ERROR));
        assertFalse(m.equals(different1, ABSOLUTE_ERROR));
        assertFalse(m.equals(different2, ABSOLUTE_ERROR));
        assertFalse(m.equals(null, ABSOLUTE_ERROR));
    }

    @Test
    public void testElementByElementProductAndReturnNew()
            throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_ROWS, MAX_ROWS);

        final Matrix m1 = new Matrix(rows, columns);
        final Matrix m2 = new Matrix(rows, columns);

        // fill matrices
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                m1.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
                m2.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
            }
        }

        Matrix m3 = m1.elementByElementProductAndReturnNew(m2);

        // check correctness
        assertEquals(m3.getRows(), rows);
        assertEquals(m3.getColumns(), columns);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                assertEquals(m3.getElementAt(i, j),
                        m1.getElementAt(i, j) * m2.getElementAt(i, j),
                        ABSOLUTE_ERROR);
            }
        }

        // Force WrongSizeException
        final Matrix wrong = new Matrix(rows + 1, columns + 1);
        m3 = null;
        try {
            m3 = m1.elementByElementProductAndReturnNew(wrong);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        assertNull(m3);

        // Force NullPointerException
        try {
            //noinspection ConstantConditions
            m3 = m1.elementByElementProductAndReturnNew(null);
            fail("NullPointerException expected but not thrown");
        } catch (final NullPointerException ignore) {
        }
        assertNull(m3);
    }

    @Test
    public void testElementByElementProduct() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_ROWS, MAX_ROWS);

        final Matrix m1 = new Matrix(rows, columns);
        final Matrix m2 = new Matrix(rows, columns);
        final double[][] tmp = new double[rows][columns];
        double value;

        // fill matrices
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                value = randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE);
                m1.setElementAt(i, j, value);
                tmp[i][j] = value;
                m2.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
            }
        }

        final Matrix m3 = new Matrix(rows, columns);
        m1.elementByElementProduct(m2, m3);
        m1.elementByElementProduct(m2);

        // check correctness
        assertEquals(m1.getRows(), rows);
        assertEquals(m1.getColumns(), columns);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                assertEquals(m1.getElementAt(i, j),
                        tmp[i][j] * m2.getElementAt(i, j), ABSOLUTE_ERROR);
            }
        }
        assertEquals(m1, m3);

        // Force WrongSizeException
        final Matrix wrong = new Matrix(rows + 1, columns + 1);
        try {
            m1.elementByElementProduct(wrong);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }

        // Force NullPointerException
        try {
            //noinspection ConstantConditions
            m1.elementByElementProduct(null);
            fail("NullPointerException expected but not thrown");
        } catch (final NullPointerException ignore) {
        }
    }

    @Test
    public void testTransposeAndReturnNEw() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m1 = new Matrix(rows, columns);
        // fill matrix
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                m1.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
            }
        }

        final Matrix m2 = m1.transposeAndReturnNew();

        // check correctness
        assertEquals(m2.getRows(), columns);
        assertEquals(m2.getColumns(), rows);

        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                assertEquals(m1.getElementAt(i, j), m2.getElementAt(j, i), 0.0);
            }
        }
    }

    @Test
    public void testTranspose() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m = new Matrix(rows, columns);
        final double[][] tmp = new double[rows][columns];
        double value;

        // fill matrix
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                value = randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE);
                m.setElementAt(i, j, value);
                tmp[i][j] = value;
            }
        }

        m.transpose();

        // check correctness
        assertEquals(m.getRows(), columns);
        assertEquals(m.getColumns(), rows);

        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                assertEquals(tmp[i][j], m.getElementAt(j, i), 0.0);
            }
        }
    }

    @Test
    public void testInitialize() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m = new Matrix(rows, columns);
        // fill with random values
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                m.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
            }
        }

        // pick an init value
        final double value = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);

        m.initialize(value);

        // check correctness
        assertEquals(m.getRows(), rows);
        assertEquals(m.getColumns(), columns);

        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                assertEquals(m.getElementAt(i, j), value, 0.0);
            }
        }
    }

    @Test
    public void testResetAndResize() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows1 = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int rows2 = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns1 = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        final int columns2 = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m = new Matrix(rows1, columns1);
        assertEquals(m.getRows(), rows1);
        assertEquals(m.getColumns(), columns1);

        // reset to new size
        m.resize(rows2, columns2);

        // check correctness
        assertEquals(m.getRows(), rows2);
        assertEquals(m.getColumns(), columns2);

        // Force WrongSizeException
        try {
            m.resize(0, columns2);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        try {
            m.resize(rows2, 0);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        try {
            m.resize(0, 0);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }


        // reset to new size and value
        final double initValue = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);

        // reset to new size and value
        m.reset(rows1, columns1, initValue);

        // check correctness
        assertEquals(m.getRows(), rows1);
        assertEquals(m.getColumns(), columns1);

        for (int j = 0; j < columns1; j++) {
            for (int i = 0; i < rows1; i++) {
                assertEquals(m.getElementAt(i, j), initValue, 0.0);
            }
        }

        // Force WrongSizeException
        try {
            m.reset(0, columns1, initValue);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        try {
            m.reset(rows1, 0, initValue);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        try {
            m.reset(0, 0, initValue);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
    }

    @Test
    public void testToArray() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m = new Matrix(rows, columns);
        final double[] array = new double[rows * columns];
        double value;
        int counter = 0;

        // fill matrix
        if (Matrix.DEFAULT_USE_COLUMN_ORDER) {
            // use column order
            for (int j = 0; j < columns; j++) {
                for (int i = 0; i < rows; i++) {
                    value = randomizer.nextDouble(MIN_RANDOM_VALUE,
                            MAX_RANDOM_VALUE);
                    m.setElementAt(i, j, value);
                    array[counter] = value;
                    counter++;
                }
            }
        }

        final double[] array2 = m.toArray();
        final double[] array3 = new double[array.length];
        m.toArray(array3);

        // check correctness
        for (int i = 0; i < rows * columns; i++) {
            assertEquals(array[i], array2[i], 0.0);
            assertEquals(array[i], array3[i], 0.0);
        }
    }

    @Test
    public void testToArrayWithColumnOrder() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m = new Matrix(rows, columns);
        final double[] array = new double[rows * columns];
        double value;
        int counter = 0;

        // fill with column order
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                value = randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE);
                m.setElementAt(i, j, value);
                array[counter] = value;
                counter++;
            }
        }

        final double[] array2 = m.toArray(true);
        final double[] array3 = new double[array.length];
        m.toArray(array3, true);

        // check correctness
        for (int i = 0; i < rows * columns; i++) {
            assertEquals(array[i], array2[i], 0.0);
            assertEquals(array[i], array3[i], 0.0);
        }
    }

    @Test
    public void testToArrayWithRowOrder() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m = new Matrix(rows, columns);
        final double[] array = new double[rows * columns];
        double value;
        int counter = 0;

        // fill with row order
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                value = randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE);
                m.setElementAt(i, j, value);
                array[counter] = value;
                counter++;
            }
        }

        final double[] array2 = m.toArray(false);
        final double[] array3 = new double[array.length];
        m.toArray(array3, false);

        // check correctness
        for (int i = 0; i < rows * columns; i++) {
            assertEquals(array[i], array2[i], 0.0);
            assertEquals(array[i], array3[i], 0.0);
        }
    }

    @Test
    public void testGetSubmatrix() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        final int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);

        final int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        final int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);

        final int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        final int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);

        final Matrix m = new Matrix(rows, columns);

        // fill matrix with random values
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                m.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
            }
        }

        Matrix submatrix = m.getSubmatrix(topLeftRow, topLeftColumn,
                bottomRightRow, bottomRightColumn);

        // check correctness
        assertEquals(submatrix.getRows(), bottomRightRow - topLeftRow + 1);
        assertEquals(submatrix.getColumns(),
                bottomRightColumn - topLeftColumn + 1);

        for (int j = 0; j < submatrix.getColumns(); j++) {
            for (int i = 0; i < submatrix.getRows(); i++) {
                assertEquals(submatrix.getElementAt(i, j),
                        m.getElementAt(i + topLeftRow, j + topLeftColumn), 0.0);
            }
        }

        // Force IllegalArgumentException
        submatrix = null;
        try {
            submatrix = m.getSubmatrix(rows, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(submatrix);
        try {
            submatrix = m.getSubmatrix(topLeftRow, columns,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(submatrix);
        try {
            submatrix = m.getSubmatrix(topLeftRow, topLeftColumn, rows,
                    bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(submatrix);
        try {
            submatrix = m.getSubmatrix(topLeftRow, topLeftColumn,
                    bottomRightRow, columns);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(submatrix);
        try {
            submatrix = m.getSubmatrix(topLeftRow + 1, topLeftColumn,
                    topLeftRow, topLeftColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(submatrix);
        try {
            submatrix = m.getSubmatrix(topLeftRow, topLeftColumn + 1,
                    topLeftRow, topLeftColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(submatrix);
        try {
            submatrix = m.getSubmatrix(topLeftRow + 1, topLeftColumn + 1,
                    topLeftRow, topLeftColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(submatrix);
    }

    @Test
    public void testGetSubmatrixAsArray() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        final int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);

        final int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        final int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);

        final int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        final int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);


        final Matrix m = new Matrix(rows, columns);

        // fill matrix with random values
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                m.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
            }
        }

        double[] array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn,
                bottomRightRow, bottomRightColumn);
        assertEquals(array.length, (bottomRightRow - topLeftRow + 1) *
                (bottomRightColumn - topLeftColumn + 1));
        int counter = 0;

        if (Matrix.DEFAULT_USE_COLUMN_ORDER) {
            // column order
            for (int j = 0; j < (bottomRightColumn - topLeftColumn + 1); j++) {
                for (int i = 0; i < (bottomRightRow - topLeftRow + 1); i++) {
                    assertEquals(array[counter],
                            m.getElementAt(i + topLeftRow, j + topLeftColumn),
                            0.0);
                    counter++;
                }
            }
        }

        // Force IllegalArgumentException
        array = null;
        try {
            array = m.getSubmatrixAsArray(rows, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow, columns,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn, rows,
                    bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn,
                    bottomRightRow, columns);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow + 1, topLeftColumn,
                    topLeftRow, topLeftColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn + 1,
                    topLeftRow, topLeftColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow + 1, topLeftColumn + 1,
                    topLeftRow, topLeftColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
    }

    @Test
    public void testGetSubmatrixAsArrayWithColumnOrder()
            throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        final int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);

        final int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        final int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);

        final int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        final int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);

        final Matrix m = new Matrix(rows, columns);

        // fill matrix with random values
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                m.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
            }
        }

        double[] array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn,
                bottomRightRow, bottomRightColumn, true);
        assertEquals(array.length, (bottomRightRow - topLeftRow + 1) *
                (bottomRightColumn - topLeftColumn + 1));
        int counter = 0;

        // column order
        for (int j = 0; j < (bottomRightColumn - topLeftColumn + 1); j++) {
            for (int i = 0; i < (bottomRightRow - topLeftRow + 1); i++) {
                assertEquals(array[counter],
                        m.getElementAt(i + topLeftRow, j + topLeftColumn), 0.0);
                counter++;
            }
        }

        // Force IllegalArgumentException
        array = null;
        try {
            array = m.getSubmatrixAsArray(rows, topLeftColumn,
                    bottomRightRow, bottomRightColumn, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow, columns,
                    bottomRightRow, bottomRightColumn, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn, rows,
                    bottomRightColumn, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn,
                    bottomRightRow, columns, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow + 1, topLeftColumn,
                    topLeftRow, topLeftColumn, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn + 1,
                    topLeftRow, topLeftColumn, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow + 1, topLeftColumn + 1,
                    topLeftRow, topLeftColumn, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
    }

    @Test
    public void testGetSubmatrixAsArrayWithRowOrder() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        final int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);

        final int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        final int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);

        final int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        final int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);


        final Matrix m = new Matrix(rows, columns);

        // fill matrix with random values
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                m.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
            }
        }

        double[] array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn,
                bottomRightRow, bottomRightColumn, false);
        assertEquals(array.length, (bottomRightRow - topLeftRow + 1) *
                (bottomRightColumn - topLeftColumn + 1));
        int counter = 0;

        // row order
        for (int i = 0; i < (bottomRightRow - topLeftRow + 1); i++) {
            for (int j = 0; j < (bottomRightColumn - topLeftColumn + 1); j++) {
                assertEquals(array[counter],
                        m.getElementAt(i + topLeftRow, j + topLeftColumn), 0.0);
                counter++;
            }
        }

        // Force IllegalArgumentException
        array = null;
        try {
            array = m.getSubmatrixAsArray(rows, topLeftColumn,
                    bottomRightRow, bottomRightColumn, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow, columns,
                    bottomRightRow, bottomRightColumn, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn, rows,
                    bottomRightColumn, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn,
                    bottomRightRow, columns, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow + 1, topLeftColumn,
                    topLeftRow, topLeftColumn, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn + 1,
                    topLeftRow, topLeftColumn, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
        try {
            array = m.getSubmatrixAsArray(topLeftRow + 1, topLeftColumn + 1,
                    topLeftRow, topLeftColumn, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(array);
    }

    @Test
    public void testSetSubmatrix() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        final int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);

        final int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        final int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);

        final int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        final int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);

        final Matrix m = new Matrix(rows, columns);

        final int submatrixRows = bottomRightRow - topLeftRow + 1;
        final int submatrixColumns = bottomRightColumn - topLeftColumn + 1;
        final Matrix submatrix = new Matrix(submatrixRows, submatrixColumns);

        // fill submatrix with random values
        for (int j = 0; j < submatrixColumns; j++) {
            for (int i = 0; i < submatrixRows; i++) {
                submatrix.setElementAt(i, j, randomizer.nextDouble(
                        MIN_RANDOM_VALUE, MAX_RANDOM_VALUE));
            }
        }

        // set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                bottomRightColumn, submatrix);

        // check correctness
        for (int j = 0; j < submatrixColumns; j++) {
            for (int i = 0; i < submatrixRows; i++) {
                assertEquals(m.getElementAt(i + topLeftRow, j + topLeftColumn),
                        submatrix.getElementAt(i, j), 0.0);
            }
        }

        // Force IllegalArgumentException
        try {
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, submatrix);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow,
                    bottomRightColumn, submatrix);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow,
                    bottomRightColumn, submatrix);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, columns, bottomRightRow,
                    bottomRightColumn, submatrix);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow,
                    bottomRightColumn, submatrix);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn,
                    submatrix);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    -bottomRightColumn, submatrix);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns,
                    submatrix);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow,
                    bottomRightColumn, submatrix);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow,
                    topLeftColumn, submatrix);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        Matrix wrong = new Matrix(submatrixRows + 1, submatrixColumns);
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, wrong);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        wrong = new Matrix(submatrixRows, submatrixColumns + 1);
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, wrong);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        wrong = new Matrix(submatrixRows + 1, submatrixColumns + 1);
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, wrong);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testSetSubmatrix2() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        final int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);

        final int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        final int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);

        final int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        final int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);

        final Matrix m = new Matrix(rows, columns);

        final Matrix submatrix = new Matrix(rows, columns);

        // fill submatrix with random values
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                submatrix.setElementAt(i, j, randomizer.nextDouble(
                        MIN_RANDOM_VALUE, MAX_RANDOM_VALUE));
            }
        }

        // set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                bottomRightRow, bottomRightColumn);

        // check correctness
        for (int j = topLeftColumn; j <= bottomRightColumn; j++) {
            for (int i = topLeftRow; i < bottomRightRow; i++) {
                assertEquals(m.getElementAt(i, j),
                        submatrix.getElementAt(i, j), 0.0);
            }
        }

        // Force IllegalArgumentException
        try {
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow,
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow,
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, columns, bottomRightRow,
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow,
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn,
                    submatrix, topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    -bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns,
                    submatrix, topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow,
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow,
                    topLeftColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, submatrix, -topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, submatrix, rows, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, submatrix, topLeftRow, -topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, submatrix, topLeftRow, columns,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    -bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    rows, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, -bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, columns);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow,
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow,
                    topLeftColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, m, topLeftRow, topLeftColumn,
                    bottomRightRow + 1, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, m, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn + 1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, m, topLeftRow, topLeftColumn,
                    bottomRightRow + 1, bottomRightColumn + 1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testSetSubmatrixWithValue() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        final int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);

        final int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        final int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);

        final int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        final int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);

        final double value = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);

        final Matrix m = new Matrix(rows, columns);

        // fil matrix with random values
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                m.setElementAt(i, j, value + 1.0);
            }
        }

        // set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                bottomRightColumn, value);

        // check correctness
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                if (i >= topLeftRow && i <= bottomRightRow &&
                        j >= topLeftColumn && j <= bottomRightColumn) {
                    assertEquals(m.getElementAt(i, j), value, 0.0);
                } else {
                    assertEquals(m.getElementAt(i, j), value + 1.0, 0.0);
                }
            }
        }

        // Force IllegalArgumentException
        try {
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, value);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow,
                    bottomRightColumn, value);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow,
                    bottomRightColumn, value);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, columns, bottomRightRow,
                    bottomRightColumn, value);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow,
                    bottomRightColumn, value);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn,
                    value);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    -bottomRightColumn, value);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns,
                    value);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow,
                    bottomRightColumn, value);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow,
                    topLeftColumn, value);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testSetSubmatrixWithArray() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        final int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);

        final int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        final int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);

        final int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        final int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);

        final Matrix m = new Matrix(rows, columns);

        final int submatrixRows = bottomRightRow - topLeftRow + 1;
        final int submatrixColumns = bottomRightColumn - topLeftColumn + 1;
        final int length = submatrixRows * submatrixColumns;
        final double[] array = new double[length];

        // fill array with random values
        for (int i = 0; i < length; i++) {
            array[i] = randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE);
        }

        // set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                bottomRightColumn, array);
        int counter = 0;

        // check correctness
        if (Matrix.DEFAULT_USE_COLUMN_ORDER) {
            // column order
            for (int j = 0; j < submatrixColumns; j++) {
                for (int i = 0; i < submatrixRows; i++) {
                    assertEquals(m.getElementAt(i + topLeftRow,
                            j + topLeftColumn), array[counter], 0.0);
                    counter++;
                }
            }
        }

        // Force IllegalArgumentException
        try {
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, array);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow,
                    bottomRightColumn, array);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow,
                    bottomRightColumn, array);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, columns, bottomRightRow,
                    bottomRightColumn, array);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow,
                    bottomRightColumn, array);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn,
                    array);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    -bottomRightColumn, array);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns,
                    array);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow,
                    bottomRightColumn, array);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow,
                    topLeftColumn, array);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        final double[] wrong = new double[length + 1];
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, wrong);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testSetSubmatrixWithArrayColumnOrder()
            throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        final int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);

        final int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        final int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);

        final int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        final int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);

        final Matrix m = new Matrix(rows, columns);

        final int submatrixRows = bottomRightRow - topLeftRow + 1;
        final int submatrixColumns = bottomRightColumn - topLeftColumn + 1;
        final int length = submatrixRows * submatrixColumns;
        final double[] array = new double[length];

        // fill array with random values
        for (int i = 0; i < length; i++) {
            array[i] = randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE);
        }

        // set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                bottomRightColumn, array, true);
        int counter = 0;

        // check correctness with column order
        for (int j = 0; j < submatrixColumns; j++) {
            for (int i = 0; i < submatrixRows; i++) {
                assertEquals(m.getElementAt(i + topLeftRow,
                        j + topLeftColumn), array[counter], 0.0);
                counter++;
            }
        }

        // Force IllegalArgumentException
        try {
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, columns, bottomRightRow,
                    bottomRightColumn, array, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow,
                    bottomRightColumn, array, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn,
                    array, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    -bottomRightColumn, array, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns,
                    array, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow,
                    bottomRightColumn, array, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow,
                    topLeftColumn, array, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        final double[] wrong = new double[length + 1];
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, wrong, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testSetSubmatrixWithArrayRowOrder() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        final int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);

        final int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        final int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);

        final int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        final int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);

        final Matrix m = new Matrix(rows, columns);

        final int submatrixRows = bottomRightRow - topLeftRow + 1;
        final int submatrixColumns = bottomRightColumn - topLeftColumn + 1;
        final int length = submatrixRows * submatrixColumns;
        final double[] array = new double[length];

        // fill array with random values
        for (int i = 0; i < length; i++) {
            array[i] = randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE);
        }

        // set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                bottomRightColumn, array, false);
        int counter = 0;

        // check correctness with row order
        for (int i = 0; i < submatrixRows; i++) {
            for (int j = 0; j < submatrixColumns; j++) {
                assertEquals(m.getElementAt(i + topLeftRow,
                        j + topLeftColumn), array[counter], 0.0);
                counter++;
            }
        }

        // Force IllegalArgumentException
        try {
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, columns, bottomRightRow,
                    bottomRightColumn, array, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow,
                    bottomRightColumn, array, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn,
                    array, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    -bottomRightColumn, array, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns,
                    array, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow,
                    bottomRightColumn, array, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow,
                    topLeftColumn, array, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        final double[] wrong = new double[length + 1];
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, wrong, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testSetSubmatrixWithArray2() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        final int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);

        final int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        final int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);

        final int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        final int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);

        final int offset = randomizer.nextInt(MIN_ROWS, MAX_ROWS);

        final Matrix m = new Matrix(rows, columns);

        final int submatrixRows = bottomRightRow - topLeftRow + 1;
        final int submatrixColumns = bottomRightColumn - topLeftColumn + 1;
        final int length = submatrixRows * submatrixColumns;
        final double[] array = new double[length + offset];

        // fill array with random values
        for (int i = 0; i < length; i++) {
            array[i + offset] = randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE);
        }

        // set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                bottomRightColumn, array, offset, offset + length - 1);
        int counter = offset;

        // check correctness
        if (Matrix.DEFAULT_USE_COLUMN_ORDER) {
            // column order
            for (int j = 0; j < submatrixColumns; j++) {
                for (int i = 0; i < submatrixRows; i++) {
                    assertEquals(m.getElementAt(i + topLeftRow,
                            j + topLeftColumn), array[counter], 0.0);
                    counter++;
                }
            }
        }

        // Force IllegalArgumentException
        try {
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, columns, bottomRightRow,
                    bottomRightColumn, array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow,
                    bottomRightColumn, array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn,
                    array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    -bottomRightColumn, array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns,
                    array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow,
                    bottomRightColumn, array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow,
                    topLeftColumn, array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, offset, offset + length);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testSetSubmatrixWithArrayColumnOrder2()
            throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        final int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);

        final int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        final int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);

        final int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        final int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);

        final int offset = randomizer.nextInt(MIN_ROWS, MAX_ROWS);

        final Matrix m = new Matrix(rows, columns);

        final int submatrixRows = bottomRightRow - topLeftRow + 1;
        final int submatrixColumns = bottomRightColumn - topLeftColumn + 1;
        final int length = submatrixRows * submatrixColumns;
        final double[] array = new double[length + offset];

        // fill array with random values
        for (int i = 0; i < length; i++) {
            array[i + offset] = randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE);
        }

        // set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                bottomRightColumn, array, offset, offset + length - 1, true);
        int counter = offset;

        // check correctness with column order
        for (int j = 0; j < submatrixColumns; j++) {
            for (int i = 0; i < submatrixRows; i++) {
                assertEquals(m.getElementAt(i + topLeftRow,
                        j + topLeftColumn), array[counter], 0.0);
                counter++;
            }
        }

        // Force IllegalArgumentException
        try {
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, offset, offset + length - 1,
                    true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, offset, offset + length - 1,
                    true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, offset, offset + length - 1,
                    true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, columns, bottomRightRow,
                    bottomRightColumn, array, offset, offset + length - 1,
                    true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow,
                    bottomRightColumn, array, offset, offset + length - 1,
                    true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn,
                    array, offset, offset + length - 1, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    -bottomRightColumn, array, offset, offset + length - 1,
                    true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns,
                    array, offset, offset + length - 1, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow,
                    bottomRightColumn, array, offset, offset + length - 1,
                    true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow,
                    topLeftColumn, array, offset, offset + length - 1, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, offset, offset + length, true);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testSetSubmatrixWithArrayRowOrder2() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        final int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);

        final int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        final int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);

        final int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        final int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);

        final int offset = randomizer.nextInt(MIN_ROWS, MAX_ROWS);

        final Matrix m = new Matrix(rows, columns);

        final int submatrixRows = bottomRightRow - topLeftRow + 1;
        final int submatrixColumns = bottomRightColumn - topLeftColumn + 1;
        final int length = submatrixRows * submatrixColumns;
        final double[] array = new double[length + offset];

        // fill array with random values
        for (int i = 0; i < length; i++) {
            array[i + offset] = randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE);
        }

        // set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                bottomRightColumn, array, offset, offset + length - 1, false);
        int counter = offset;

        // check correctness with row order
        for (int i = 0; i < submatrixRows; i++) {
            for (int j = 0; j < submatrixColumns; j++) {
                assertEquals(m.getElementAt(i + topLeftRow,
                        j + topLeftColumn), array[counter], 0.0);
                counter++;
            }
        }

        // Force IllegalArgumentException
        try {
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, offset, offset + length - 1,
                    false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, offset, offset + length - 1,
                    false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, offset, offset + length - 1,
                    false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, columns, bottomRightRow,
                    bottomRightColumn, array, offset, offset + length - 1,
                    false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow,
                    bottomRightColumn, array, offset, offset + length - 1,
                    false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn,
                    array, offset, offset + length - 1, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    -bottomRightColumn, array, offset, offset + length - 1,
                    false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns,
                    array, offset, offset + length - 1, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow,
                    bottomRightColumn, array, offset, offset + length - 1,
                    false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow,
                    topLeftColumn, array, offset, offset + length - 1, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn, array, offset, offset + length, false);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testIdentity() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        final Matrix m = Matrix.identity(rows, columns);

        // check correctness
        assertEquals(m.getRows(), rows);
        assertEquals(m.getColumns(), columns);

        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                if (i == j) {
                    assertEquals(m.getElementAt(i, j), 1.0, 0.0);
                } else {
                    assertEquals(m.getElementAt(i, j), 0.0, 0.0);
                }
            }
        }

        // force WrongSizeException
        try {
            Matrix.identity(0, columns);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        try {
            Matrix.identity(rows, 0);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        try {
            Matrix.identity(0, 0);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }

    }

    @Test
    public void testFillWithUniformRandomValues() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        Matrix m;
        double value;
        double sum = 0.0;
        double sqrSum = 0.0;
        for (int k = 0; k < TIMES; k++) {
            m = new Matrix(rows, columns);
            Matrix.fillWithUniformRandomValues(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE, m);

            // check correctness
            assertEquals(m.getRows(), rows);
            assertEquals(m.getColumns(), columns);

            for (int j = 0; j < columns; j++) {
                for (int i = 0; i < rows; i++) {
                    value = m.getElementAt(i, j);

                    assertTrue(value >= MIN_RANDOM_VALUE);
                    assertTrue(value <= MAX_RANDOM_VALUE);

                    sum += value;
                    sqrSum += value * value;
                }
            }
        }

        final int numSamples = rows * columns * TIMES;
        final double estimatedMeanValue = sum / (double) (numSamples);
        final double estimatedVariance = (sqrSum - (double) numSamples *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) numSamples - 1.0);

        // mean and variance of uniform distribution
        final double meanValue = 0.5 * (MIN_RANDOM_VALUE + MAX_RANDOM_VALUE);
        final double variance = (MAX_RANDOM_VALUE - MIN_RANDOM_VALUE) *
                (MAX_RANDOM_VALUE - MIN_RANDOM_VALUE) / 12.0;

        // check correctness of results
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        // Force WrongSizeException
        try {
            Matrix.createWithUniformRandomValues(0, columns, MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        try {
            Matrix.createWithUniformRandomValues(rows, 0, MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        // Force IllegalArgumentException
        try {
            Matrix.createWithUniformRandomValues(rows, columns,
                    MAX_RANDOM_VALUE, MIN_RANDOM_VALUE);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

    }

    @Test
    public void testCreateWithUniformRandomValues() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        Matrix m;
        double value;
        double sum = 0.0;
        double sqrSum = 0.0;
        for (int k = 0; k < TIMES; k++) {
            m = Matrix.createWithUniformRandomValues(rows, columns,
                    MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
            // check correctness
            assertEquals(m.getRows(), rows);
            assertEquals(m.getColumns(), columns);

            for (int j = 0; j < columns; j++) {
                for (int i = 0; i < rows; i++) {
                    value = m.getElementAt(i, j);

                    assertTrue(value >= MIN_RANDOM_VALUE);
                    assertTrue(value <= MAX_RANDOM_VALUE);

                    sum += value;
                    sqrSum += value * value;
                }
            }
        }

        final int numSamples = rows * columns * TIMES;
        final double estimatedMeanValue = sum / (double) (numSamples);
        final double estimatedVariance = (sqrSum - (double) numSamples *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) numSamples - 1.0);

        // mean and variance of uniform distribution
        final double meanValue = 0.5 * (MIN_RANDOM_VALUE + MAX_RANDOM_VALUE);
        final double variance = (MAX_RANDOM_VALUE - MIN_RANDOM_VALUE) *
                (MAX_RANDOM_VALUE - MIN_RANDOM_VALUE) / 12.0;

        // check correctness of results
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        // Force WrongSizeException
        try {
            Matrix.createWithUniformRandomValues(0, columns, MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        try {
            Matrix.createWithUniformRandomValues(rows, 0, MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        // Force IllegalArgumentException
        try {
            Matrix.createWithUniformRandomValues(rows, columns,
                    MAX_RANDOM_VALUE, MIN_RANDOM_VALUE);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

    }

    @Test
    public void testCreateWithUniformRandomValues2() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        Matrix m;
        double value;
        double sum = 0.0;
        double sqrSum = 0.0;
        for (int k = 0; k < TIMES; k++) {
            m = Matrix.createWithUniformRandomValues(rows, columns,
                    MIN_RANDOM_VALUE, MAX_RANDOM_VALUE, new SecureRandom());
            // check correctness
            assertEquals(m.getRows(), rows);
            assertEquals(m.getColumns(), columns);

            for (int j = 0; j < columns; j++) {
                for (int i = 0; i < rows; i++) {
                    value = m.getElementAt(i, j);

                    assertTrue(value >= MIN_RANDOM_VALUE);
                    assertTrue(value <= MAX_RANDOM_VALUE);

                    sum += value;
                    sqrSum += value * value;
                }
            }
        }

        final int numSamples = rows * columns * TIMES;
        final double estimatedMeanValue = sum / (double) (numSamples);
        final double estimatedVariance = (sqrSum - (double) numSamples *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) numSamples - 1.0);

        // mean and variance of uniform distribution
        final double meanValue = 0.5 * (MIN_RANDOM_VALUE + MAX_RANDOM_VALUE);
        final double variance = (MAX_RANDOM_VALUE - MIN_RANDOM_VALUE) *
                (MAX_RANDOM_VALUE - MIN_RANDOM_VALUE) / 12.0;

        // check correctness of results
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        // Force WrongSizeException
        try {
            Matrix.createWithUniformRandomValues(0, columns, MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        try {
            Matrix.createWithUniformRandomValues(rows, 0, MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        //Force IllegalArgumentException
        try {
            Matrix.createWithUniformRandomValues(rows, columns,
                    MAX_RANDOM_VALUE, MIN_RANDOM_VALUE);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testFillWithGaussianRandomValues() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        Matrix m;
        double value;
        double mean = 0.0;
        double sqrSum = 0.0;
        final int numSamples = rows * columns * TIMES;
        for (int k = 0; k < TIMES; k++) {
            m = new Matrix(rows, columns);
            Matrix.fillWithGaussianRandomValues(MEAN, STANDARD_DEVIATION, m);

            // check correctness
            assertEquals(m.getRows(), rows);
            assertEquals(m.getColumns(), columns);

            for (int j = 0; j < columns; j++) {
                for (int i = 0; i < rows; i++) {
                    value = m.getElementAt(i, j);

                    mean += value / (double) numSamples;
                    sqrSum += value * value / (double) numSamples;
                }
            }
        }

        final double standardDeviation = Math.sqrt(sqrSum - mean);


        // check correctness of results
        assertEquals(mean, MEAN, mean * RELATIVE_ERROR);
        assertEquals(standardDeviation, STANDARD_DEVIATION,
                standardDeviation * RELATIVE_ERROR);

        // Force WrongSizeException
        try {
            Matrix.createWithGaussianRandomValues(0, columns, MEAN,
                    STANDARD_DEVIATION);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        try {
            Matrix.createWithGaussianRandomValues(rows, 0, MEAN,
                    STANDARD_DEVIATION);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        try {
            Matrix.createWithGaussianRandomValues(rows, columns,
                    MEAN, -STANDARD_DEVIATION);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testCreateWithGaussianRandomValues() throws WrongSizeException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        Matrix m;
        double value;
        double mean = 0.0;
        double sqrSum = 0.0;
        final int numSamples = rows * columns * TIMES;
        for (int k = 0; k < TIMES; k++) {
            m = Matrix.createWithGaussianRandomValues(rows, columns,
                    MEAN, STANDARD_DEVIATION);
            // check correctness
            assertEquals(m.getRows(), rows);
            assertEquals(m.getColumns(), columns);

            for (int j = 0; j < columns; j++) {
                for (int i = 0; i < rows; i++) {
                    value = m.getElementAt(i, j);

                    mean += value / (double) numSamples;
                    sqrSum += value * value / (double) numSamples;
                }
            }
        }

        final double standardDeviation = Math.sqrt(sqrSum - mean);


        // check correctness of results
        assertEquals(mean, MEAN, mean * RELATIVE_ERROR);
        assertEquals(standardDeviation, STANDARD_DEVIATION,
                standardDeviation * RELATIVE_ERROR);

        // Force WrongSizeException
        try {
            Matrix.createWithGaussianRandomValues(0, columns, MEAN,
                    STANDARD_DEVIATION);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        try {
            Matrix.createWithGaussianRandomValues(rows, 0, MEAN,
                    STANDARD_DEVIATION);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
        try {
            Matrix.createWithGaussianRandomValues(rows, columns,
                    MEAN, -STANDARD_DEVIATION);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testDiagonal() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int length = randomizer.nextInt(MIN_ROWS, MAX_ROWS);

        final double[] diagonal = new double[length];

        // fill diagonal with random values
        for (int i = 0; i < length; i++) {
            diagonal[i] = randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE);
        }

        final Matrix m = Matrix.diagonal(diagonal);

        // check correctness
        assertEquals(m.getRows(), length);
        assertEquals(m.getColumns(), length);

        for (int j = 0; j < length; j++) {
            for (int i = 0; i < length; i++) {
                if (i == j) {
                    assertEquals(m.getElementAt(i, j), diagonal[i], 0.0);
                } else {
                    assertEquals(m.getElementAt(i, j), 0.0, 0.0);
                }
            }
        }
    }

    @Test
    public void testNewFromArray() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int cols = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        final int length = rows * cols;

        final double[] array = new double[length];
        randomizer.fill(array, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        // use default column order
        Matrix m = Matrix.newFromArray(array);
        double[] array2 = m.toArray();

        // check correctness
        assertEquals(m.getRows(), length);
        assertEquals(m.getColumns(), 1);
        assertArrayEquals(array, array2, 0.0);

        // use column order
        m = Matrix.newFromArray(array, true);
        array2 = m.toArray(true);

        // check correctness
        assertEquals(m.getRows(), length);
        assertEquals(m.getColumns(), 1);
        assertArrayEquals(array, array2, 0.0);

        // use row order
        m = Matrix.newFromArray(array, false);
        array2 = m.toArray(false);

        // check correctness
        assertEquals(m.getRows(), 1);
        assertEquals(m.getColumns(), length);
        assertArrayEquals(array, array2, 0.0);
    }

    @Test
    public void testFromArray() throws AlgebraException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int cols = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        final int length = rows * cols;

        final double[] array = new double[length];
        randomizer.fill(array, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        final Matrix m = new Matrix(rows, cols);

        // use default column order
        m.fromArray(array);
        double[] array2 = m.toArray();

        // check correctness
        assertEquals(m.getRows(), rows);
        assertEquals(m.getColumns(), cols);
        assertArrayEquals(array, array2, 0.0);

        // Force WrongSizeException
        try {
            m.fromArray(new double[length + 1]);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }


        // use column order
        m.fromArray(array, true);
        array2 = m.toArray(true);

        // check correctness
        assertEquals(m.getRows(), rows);
        assertEquals(m.getColumns(), cols);
        assertArrayEquals(array, array2, 0.0);

        // Force WrongSizeException
        try {
            m.fromArray(new double[length + 1], true);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }


        // use row order
        m.fromArray(array, false);
        array2 = m.toArray(false);

        // check correctness
        assertEquals(m.getRows(), rows);
        assertEquals(m.getColumns(), cols);
        assertArrayEquals(array, array2, 0.0);

        // Force WrongSizeException
        try {
            m.fromArray(new double[length + 1], false);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
    }

    @Test
    public void testSymmetrize() throws AlgebraException {
        int numValid = 0;
        for (int t = 0; t < TIMES; t++) {
            final UniformRandomizer randomizer = new UniformRandomizer(new Random());
            final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);

            final Matrix symmetric = DecomposerHelper.getSymmetricMatrix(rows);

            final Matrix nonSymmetric = new Matrix(rows, rows);
            nonSymmetric.copyFrom(symmetric);
            nonSymmetric.setElementAt(0, rows - 1,
                    nonSymmetric.getElementAt(0, rows - 1) + 1.0);


            // symmetrize
            final Matrix symmetric2 = new Matrix(rows, rows);
            symmetric.symmetrize(symmetric2);

            final Matrix nonSymmetric2 = new Matrix(rows, rows);
            nonSymmetric.symmetrize(nonSymmetric2);

            // check correctness
            if (!Utils.isSymmetric(symmetric)) {
                continue;
            }
            assertTrue(Utils.isSymmetric(symmetric));
            if (Utils.isSymmetric(nonSymmetric)) {
                continue;
            }
            assertFalse(Utils.isSymmetric(nonSymmetric));

            if (!Utils.isSymmetric(symmetric2)) {
                continue;
            }
            assertTrue(Utils.isSymmetric(symmetric2));
            if (!Utils.isSymmetric(nonSymmetric2)) {
                continue;
            }
            assertTrue(Utils.isSymmetric(nonSymmetric2));

            boolean failed = false;
            for (int i = 0; i < symmetric2.getColumns(); i++) {
                for (int j = 0; j < symmetric2.getRows(); j++) {
                    if (Math.abs(symmetric2.getElementAt(i, j) -
                            0.5 * (symmetric.getElementAt(j, i) + symmetric.getElementAt(j, i))) > ABSOLUTE_ERROR) {
                        failed = true;
                        break;
                    }
                    assertEquals(symmetric2.getElementAt(i, j),
                            0.5 * (symmetric.getElementAt(i, j) +
                                    symmetric.getElementAt(j, i)), ABSOLUTE_ERROR);
                }
            }

            if (failed) {
                continue;
            }

            for (int i = 0; i < nonSymmetric2.getColumns(); i++) {
                for (int j = 0; j < nonSymmetric2.getRows(); j++) {
                    if (Math.abs(nonSymmetric2.getElementAt(i, j) -
                            0.5 * (nonSymmetric.getElementAt(i, j) + nonSymmetric.getElementAt(j, i))) > ABSOLUTE_ERROR) {
                        failed = true;
                        break;
                    }
                    assertEquals(nonSymmetric2.getElementAt(i, j),
                            0.5 * (nonSymmetric.getElementAt(i, j) +
                                    nonSymmetric.getElementAt(j, i)), ABSOLUTE_ERROR);
                }
            }

            if (failed) {
                continue;
            }


            // Force WrongSizeException
            final Matrix wrong = new Matrix(1, 2);
            try {
                wrong.symmetrize(wrong);
                fail("WrongSizeException expected but not thrown");
            } catch (final WrongSizeException ignore) {
            }
            try {
                symmetric.symmetrize(wrong);
                fail("WrongSizeException expected but not thrown");
            } catch (final WrongSizeException ignore) {
            }


            // symmetrize and return new
            final Matrix symmetric3 = symmetric.symmetrizeAndReturnNew();
            final Matrix nonSymmetric3 = nonSymmetric.symmetrizeAndReturnNew();

            // check correctness
            if (!Utils.isSymmetric(symmetric)) {
                continue;
            }
            assertTrue(Utils.isSymmetric(symmetric));
            if (Utils.isSymmetric(nonSymmetric)) {
                continue;
            }
            assertFalse(Utils.isSymmetric(nonSymmetric));

            if (!Utils.isSymmetric(symmetric3)) {
                continue;
            }
            assertTrue(Utils.isSymmetric(symmetric3));
            if (!Utils.isSymmetric(nonSymmetric3)) {
                continue;
            }
            assertTrue(Utils.isSymmetric(nonSymmetric3));

            // Force WrongSizeException
            try {
                wrong.symmetrizeAndReturnNew();
                fail("WrongSizeException expected but not thrown");
            } catch (final WrongSizeException ignore) {
            }


            // symmetrize and update
            symmetric.symmetrize();
            nonSymmetric.symmetrize();

            // check correctness
            if (!Utils.isSymmetric(symmetric)) {
                continue;
            }
            assertTrue(Utils.isSymmetric(symmetric));
            if (!Utils.isSymmetric(nonSymmetric)) {
                continue;
            }
            assertTrue(Utils.isSymmetric(nonSymmetric));

            // Force WrongSizeException
            try {
                wrong.symmetrize();
                fail("WrongSizeException expected but not thrown");
            } catch (final WrongSizeException ignore) {
            }

            numValid++;
            break;
        }

        assertTrue(numValid > 0);
    }
}
