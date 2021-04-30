/*
 * Copyright (C) 2016 Alberto Irurueta Carro (alberto@irurueta.com)
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
package com.irurueta.statistics;

import com.irurueta.algebra.ArrayUtils;
import com.irurueta.algebra.DecomposerException;
import com.irurueta.algebra.DecomposerHelper;
import com.irurueta.algebra.Matrix;
import com.irurueta.algebra.Utils;
import com.irurueta.algebra.WrongSizeException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

public class MultivariateGaussianRandomizerTest {

    private static final int NUM_SAMPLES = 1000000;
    private static final double RELATIVE_ERROR = 0.05;

    private static final double MIN_RANDOM_VALUE = -100.0;
    private static final double MAX_RANDOM_VALUE = 100.0;

    @Test
    public void testConstructor() throws WrongSizeException,
            InvalidCovarianceMatrixException {
        // test empty constructor
        MultivariateGaussianRandomizer randomizer =
                new MultivariateGaussianRandomizer();

        // check correctness
        assertArrayEquals(randomizer.getMean(), new double[]{0.0}, 0.0);
        assertEquals(randomizer.getCovariance(), Matrix.identity(1, 1));


        // test constructor with random
        randomizer = new MultivariateGaussianRandomizer(new Random());

        // check correctness
        assertArrayEquals(randomizer.getMean(), new double[]{0.0}, 0.0);
        assertEquals(randomizer.getCovariance(), Matrix.identity(1, 1));

        // Force NullPointerException
        randomizer = null;
        try {
            randomizer = new MultivariateGaussianRandomizer(null);
            fail("NullPointerException expected but not thrown");
        } catch (final NullPointerException ignore) {
        }
        //noinspection ConstantConditions
        assertNull(randomizer);


        // test constructor with mean and covariance
        final UniformRandomizer uniRandomizer = new UniformRandomizer(new Random());
        final double[] mean = new double[2];
        uniRandomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));

        randomizer = new MultivariateGaussianRandomizer(mean, cov);

        // check correctness
        assertArrayEquals(randomizer.getMean(), mean, 0.0);
        assertEquals(randomizer.getCovariance(), cov);

        // Force WrongSizeException
        randomizer = null;
        try {
            randomizer = new MultivariateGaussianRandomizer(new double[1], cov);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }

        // Force InvalidCovarianceMatrixException
        try {
            randomizer = new MultivariateGaussianRandomizer(mean,
                    new Matrix(2, 2));
            fail("InvalidCovarianceMatrixException expected but not thrown");
        } catch (final InvalidCovarianceMatrixException ignore) {
        }
        assertNull(randomizer);

        // test constructor with random, mean and covariance
        randomizer = new MultivariateGaussianRandomizer(new Random(), mean,
                cov);

        // check correctness
        assertArrayEquals(randomizer.getMean(), mean, 0.0);
        assertEquals(randomizer.getCovariance(), cov);

        // Force WrongSizeException
        randomizer = null;
        try {
            randomizer = new MultivariateGaussianRandomizer(new Random(),
                    new double[1], cov);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }

        // Force InvalidCovarianceMatrixException
        try {
            randomizer = new MultivariateGaussianRandomizer(new Random(), mean,
                    new Matrix(2, 2));
            fail("InvalidCovarianceMatrixException expected but not thrown");
        } catch (final InvalidCovarianceMatrixException ignore) {
        }
        assertNull(randomizer);
    }

    @Test
    public void testGetSetMeanAndCovariance() throws WrongSizeException,
            InvalidCovarianceMatrixException {
        final MultivariateGaussianRandomizer randomizer =
                new MultivariateGaussianRandomizer();

        // check initial values
        assertArrayEquals(randomizer.getMean(), new double[]{0.0}, 0.0);
        assertEquals(randomizer.getCovariance(), Matrix.identity(1, 1));

        // set new values
        final UniformRandomizer uniRandomizer = new UniformRandomizer(new Random());
        final double[] mean = new double[2];
        uniRandomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));

        randomizer.setMeanAndCovariance(mean, cov);

        // check correctness
        assertArrayEquals(randomizer.getMean(), mean, 0.0);
        assertEquals(randomizer.getCovariance(), cov);

        // Force WrongSizeException
        try {
            randomizer.setMeanAndCovariance(new double[1], cov);
            fail("WrongSizeException expected but not thrown");
        } catch (final WrongSizeException ignore) {
        }

        // Force InvalidCovarianceMatrixException
        try {
            randomizer.setMeanAndCovariance(mean, new Matrix(2, 2));
            fail("InvalidCovarianceMatrixException expected but not thrown");
        } catch (final InvalidCovarianceMatrixException ignore) {
        }
    }

    @Test
    public void testNext() throws WrongSizeException,
            InvalidCovarianceMatrixException, DecomposerException {

        final UniformRandomizer uniRandomizer = new UniformRandomizer(new Random());
        final double[] mean = new double[2];
        uniRandomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));

        final MultivariateGaussianRandomizer randomizer =
                new MultivariateGaussianRandomizer(mean, cov);

        final double[] mean1 = new double[2];
        final double[] mean2 = new double[2];

        final double[] values1 = new double[2];
        double[] values2;
        double[] tmp1;
        double[] tmp2;

        final Matrix col1 = new Matrix(2, 1);
        final Matrix row1 = new Matrix(1, 2);
        final Matrix sqr1 = new Matrix(2, 2);

        final Matrix col2 = new Matrix(2, 1);
        final Matrix row2 = new Matrix(1, 2);
        final Matrix sqr2 = new Matrix(2, 2);

        final Matrix sqrSum1 = new Matrix(2, 2);
        final Matrix sqrSum2 = new Matrix(2, 2);
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomizer.next(values1);
            values2 = randomizer.next();

            tmp1 = Arrays.copyOf(values1, 2);
            tmp2 = Arrays.copyOf(values2, 2);

            ArrayUtils.multiplyByScalar(tmp1, 1.0 / (double) NUM_SAMPLES, tmp1);
            ArrayUtils.multiplyByScalar(tmp2, 1.0 / (double) NUM_SAMPLES, tmp2);

            ArrayUtils.sum(mean1, tmp1, mean1);
            ArrayUtils.sum(mean2, tmp2, mean2);

            col1.fromArray(values1);
            row1.fromArray(values1);
            col1.multiply(row1, sqr1);
            sqr1.multiplyByScalar(1.0 / (double) NUM_SAMPLES);

            col2.fromArray(values2);
            row2.fromArray(values2);
            col2.multiply(row2, sqr2);
            sqr2.multiplyByScalar(1.0 / (double) NUM_SAMPLES);

            sqrSum1.add(sqr1);
            sqrSum2.add(sqr2);
        }

        col1.fromArray(mean1);
        row1.fromArray(mean1);
        Matrix sqrMean1 = col1.multiplyAndReturnNew(row1);

        col2.fromArray(mean2);
        row2.fromArray(mean2);
        Matrix sqrMean2 = col2.multiplyAndReturnNew(row2);

        final Matrix cov1 = sqrSum1.subtractAndReturnNew(sqrMean1);
        final Matrix cov2 = sqrSum2.subtractAndReturnNew(sqrMean2);

        // check correctness
        final double maxMean = Math.max(ArrayUtils.max(mean),
                Math.abs(ArrayUtils.min(mean)));
        assertArrayEquals(mean1, mean, RELATIVE_ERROR * maxMean);
        assertArrayEquals(mean2, mean, RELATIVE_ERROR * maxMean);

        final double detCov = Utils.det(cov);
        assertTrue(cov1.equals(cov, RELATIVE_ERROR * detCov));
        assertTrue(cov2.equals(cov, RELATIVE_ERROR * detCov));
    }
}
