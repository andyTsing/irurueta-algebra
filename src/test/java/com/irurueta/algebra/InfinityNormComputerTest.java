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

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class InfinityNormComputerTest {

    private static final int MIN_LIMIT = 0;
    private static final int MAX_LIMIT = 50;
    private static final int MIN_ROWS = 1;
    private static final int MAX_ROWS = 50;
    private static final int MIN_COLUMNS = 1;
    private static final int MAX_COLUMNS = 50;
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 100;
    private static final double MIN_RANDOM_VALUE = 0;
    private static final double MAX_RANDOM_VALUE = 100;
    private static final double ABSOLUTE_ERROR = 1e-6;

    @Test
    public void testGetNormType() {
        final InfinityNormComputer normComputer = new InfinityNormComputer();
        assertEquals(normComputer.getNormType(), NormType.INFINITY_NORM);
    }

    @Test
    public void testGetNormMatrix() throws WrongSizeException {
        final InfinityNormComputer normComputer = new InfinityNormComputer();
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        final int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        double rowSum;
        double maxRowSum = 0.0;
        final double norm;
        final double initValue = randomizer.nextDouble(MIN_COLUMNS, MAX_COLUMNS);
        double value;

        // For random non-initialized matrix
        Matrix m = new Matrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            rowSum = 0.0;
            for (int j = 0; j < columns; j++) {
                value = randomizer.nextInt(MIN_LIMIT, MAX_LIMIT);
                m.setElementAt(i, j, value);
                rowSum += Math.abs(value);
            }

            maxRowSum = Math.max(rowSum, maxRowSum);
        }

        assertEquals(normComputer.getNorm(m), maxRowSum, ABSOLUTE_ERROR);
        assertEquals(InfinityNormComputer.norm(m), maxRowSum, ABSOLUTE_ERROR);

        // For initialized matrix
        m = new Matrix(rows, columns);
        m.initialize(initValue);

        norm = initValue * columns;

        assertEquals(normComputer.getNorm(m), norm, ABSOLUTE_ERROR);
        assertEquals(InfinityNormComputer.norm(m), norm, ABSOLUTE_ERROR);

        // For identity matrix
        m = Matrix.identity(rows, columns);
        assertEquals(normComputer.getNorm(m), 1.0, ABSOLUTE_ERROR);
        assertEquals(InfinityNormComputer.norm(m), 1.0, ABSOLUTE_ERROR);
    }

    @Test
    public void testGetNormArray() {
        final InfinityNormComputer normComputer = new InfinityNormComputer();
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        double norm;
        final double initValue = randomizer.nextDouble(MIN_LIMIT, MAX_LIMIT);

        final double[] v = new double[length];

        // randomly initialize vector
        for (int i = 0; i < length; i++) {
            v[i] = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        }

        norm = Math.abs(v[0]);

        assertEquals(normComputer.getNorm(v), norm, ABSOLUTE_ERROR);
        assertEquals(InfinityNormComputer.norm(v), norm, ABSOLUTE_ERROR);

        Arrays.fill(v, initValue);

        norm = initValue;

        assertEquals(normComputer.getNorm(v), norm, ABSOLUTE_ERROR);
        assertEquals(InfinityNormComputer.norm(v), norm, ABSOLUTE_ERROR);
    }

    @Test
    public void testNormWithJacobian() throws AlgebraException {
        final InfinityNormComputer normComputer = new InfinityNormComputer();
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        double norm;

        final double[] v = new double[length];

        // randomly initialize vector
        for (int i = 0; i < length; i++) {
            v[i] = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        }

        norm = Math.abs(v[0]);

        Matrix jacobian = new Matrix(1, length);
        assertEquals(InfinityNormComputer.norm(v, jacobian), norm,
                ABSOLUTE_ERROR);
        assertEquals(jacobian, Matrix.newFromArray(v).
                multiplyByScalarAndReturnNew(1.0 / norm).
                transposeAndReturnNew());

        // Force WrongSizeException
        try {
            InfinityNormComputer.norm(v, new Matrix(2, length));
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }


        jacobian = new Matrix(1, length);
        assertEquals(normComputer.getNorm(v, jacobian), norm, ABSOLUTE_ERROR);
        assertEquals(jacobian, Matrix.newFromArray(v).
                multiplyByScalarAndReturnNew(1.0 / norm).
                transposeAndReturnNew());

        // Force WrongSizeException
        try {
            normComputer.getNorm(v, new Matrix(2, length));
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }
    }
}
