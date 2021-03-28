/*
 * Copyright (C) 2015 Alberto Irurueta Carro (alberto@irurueta.com)
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

import com.irurueta.algebra.AlgebraException;
import com.irurueta.algebra.ArrayUtils;
import com.irurueta.algebra.DecomposerHelper;
import com.irurueta.algebra.Matrix;
import com.irurueta.algebra.NotReadyException;
import com.irurueta.algebra.Utils;
import com.irurueta.algebra.WrongSizeException;
import com.irurueta.statistics.MultivariateNormalDist.JacobianEvaluator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

public class MultivariateNormalDistTest {

    private static final double MIN_RANDOM_VALUE = -100.0;
    private static final double MAX_RANDOM_VALUE = 100.0;

    private static final double ABSOLUTE_ERROR = 1e-6;
    private static final double LARGE_ABSOLUTE_ERROR = 1e-3;

    private static final int N_SAMPLES = 1000000;
    private static final double RELATIVE_ERROR = 0.05;

    @Test
    public void testConstructor() throws AlgebraException,
            InvalidCovarianceMatrixException {
        // empty constructor
        MultivariateNormalDist dist = new MultivariateNormalDist();

        // check correctness
        assertArrayEquals(dist.getMean(), new double[1], 0.0);
        assertEquals(dist.getCovariance(), Matrix.identity(1, 1));
        assertTrue(dist.isReady());
        assertNull(dist.getCovarianceBasis());
        assertNull(dist.getVariances());

        // constructor with dimensions
        dist = new MultivariateNormalDist(2);

        // check correctness
        assertArrayEquals(dist.getMean(), new double[2], 0.0);
        assertEquals(dist.getCovariance(), Matrix.identity(2, 2));
        assertTrue(dist.isReady());
        assertNull(dist.getCovarianceBasis());
        assertNull(dist.getVariances());

        // Force IllegalArgumentException
        dist = null;
        try {
            dist = new MultivariateNormalDist(0);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(dist);


        // constructor with mean and covariance
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));

        dist = new MultivariateNormalDist(mean, cov);

        // check correctness
        assertArrayEquals(dist.getMean(), mean, 0.0);
        assertEquals(dist.getCovariance(), cov);
        assertTrue(dist.isReady());
        assertNull(dist.getCovarianceBasis());
        assertNull(dist.getVariances());


        // Force IllegalArgumentException
        dist = null;
        try {
            dist = new MultivariateNormalDist(new double[0], cov);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            dist = new MultivariateNormalDist(new double[3], cov);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        final Matrix wrong = DecomposerHelper.getLeftLowerTriangulatorFactor(2);
        final Matrix wrong2 = DecomposerHelper.getSingularMatrixInstance(2, 2);
        final Matrix wrong3 = new Matrix(2, 3);
        try {
            dist = new MultivariateNormalDist(mean, wrong);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        } catch (final InvalidCovarianceMatrixException ignore) {
        }
        try {
            dist = new MultivariateNormalDist(mean, wrong2);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        } catch (final InvalidCovarianceMatrixException ignore) {
        }
        try {
            dist = new MultivariateNormalDist(mean, wrong3);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        } catch (final InvalidCovarianceMatrixException ignore) {
        }
        assertNull(dist);

        dist = new MultivariateNormalDist(mean, cov, false);

        // check correctness
        assertArrayEquals(dist.getMean(), mean, 0.0);
        assertEquals(dist.getCovariance(), cov);
        assertTrue(dist.isReady());
        assertNull(dist.getCovarianceBasis());
        assertNull(dist.getVariances());
    }

    @Test
    public void testGetSetMean() {
        final MultivariateNormalDist dist = new MultivariateNormalDist();

        // check default value
        assertArrayEquals(dist.getMean(), new double[1], 0.0);

        // set new value
        final double[] mean = new double[2];
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        dist.setMean(mean);

        // check correctness
        assertArrayEquals(dist.getMean(), mean, 0.0);


        // Force IllegalArgumentException
        try {
            dist.setMean(new double[0]);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testGetSetCovariance() throws AlgebraException,
            InvalidCovarianceMatrixException {
        final MultivariateNormalDist dist = new MultivariateNormalDist();

        // check default value
        assertEquals(dist.getCovariance(), Matrix.identity(1, 1));

        // set new value
        final Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));

        dist.setCovariance(cov);

        // check correctness
        assertEquals(dist.getCovariance(), cov);

        final Matrix cov2 = new Matrix(2, 2);
        dist.getCovariance(cov2);
        assertEquals(cov, cov2);

        dist.setCovariance(cov, false);

        // check correctness
        assertEquals(dist.getCovariance(), cov);

        // Force InvalidCovarianceMatrixException
        final Matrix wrong = DecomposerHelper.getLeftLowerTriangulatorFactor(2);
        final Matrix wrong2 = DecomposerHelper.getSingularMatrixInstance(2, 2);
        final Matrix wrong3 = new Matrix(3, 2);
        try {
            dist.setCovariance(wrong);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        } catch (final InvalidCovarianceMatrixException ignore) {
        }
        try {
            dist.setCovariance(wrong2);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        } catch (final InvalidCovarianceMatrixException ignore) {
        }
        try {
            dist.setCovariance(wrong3);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        } catch (final InvalidCovarianceMatrixException ignore) {
        }
    }

    @Test
    public void testSetMeanAndCovariance() throws AlgebraException,
            InvalidCovarianceMatrixException {
        final MultivariateNormalDist dist = new MultivariateNormalDist();

        // check default values
        assertArrayEquals(dist.getMean(), new double[1], 0.0);
        assertEquals(dist.getCovariance(), Matrix.identity(1, 1));

        // set new values
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));

        dist.setMeanAndCovariance(mean, cov);

        // check correctness
        assertArrayEquals(dist.getMean(), mean, 0.0);
        assertEquals(dist.getCovariance(), cov);

        dist.setMeanAndCovariance(mean, cov, false);

        // check correctness
        assertArrayEquals(dist.getMean(), mean, 0.0);
        assertEquals(dist.getCovariance(), cov);

        // Force IllegalArgumentException
        try {
            dist.setMeanAndCovariance(new double[0], cov);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            dist.setMeanAndCovariance(new double[3], cov);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        final Matrix wrong = DecomposerHelper.getLeftLowerTriangulatorFactor(2);
        final Matrix wrong2 = DecomposerHelper.getSingularMatrixInstance(2, 2);
        final Matrix wrong3 = new Matrix(2, 3);
        try {
            dist.setMeanAndCovariance(mean, wrong);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        } catch (final InvalidCovarianceMatrixException ignore) {
        }
        try {
            dist.setMeanAndCovariance(mean, wrong2);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        } catch (final InvalidCovarianceMatrixException ignore) {
        }
        try {
            dist.setMeanAndCovariance(mean, wrong3);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        } catch (final InvalidCovarianceMatrixException ignore) {
        }
    }

    @Test
    public void testIsValidCovariance() throws AlgebraException {
        final Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));

        final Matrix wrong = DecomposerHelper.getLeftLowerTriangulatorFactor(2);
        final Matrix wrong2 = DecomposerHelper.getSingularMatrixInstance(2, 2);
        final Matrix wrong3 = new Matrix(2, 3);

        assertTrue(MultivariateNormalDist.isValidCovariance(cov));
        assertFalse(MultivariateNormalDist.isValidCovariance(wrong));
        assertFalse(MultivariateNormalDist.isValidCovariance(wrong2));
        assertFalse(MultivariateNormalDist.isValidCovariance(wrong3));
    }

    @Test
    public void testIsReady() {
        final MultivariateNormalDist dist = new MultivariateNormalDist();

        // check initial value
        assertTrue(dist.isReady());

        // force not ready
        dist.setMean(new double[2]);
        assertFalse(dist.isReady());
    }

    @Test
    public void testP() throws AlgebraException,
            InvalidCovarianceMatrixException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final double[] x = new double[2];
        randomizer.fill(x, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));

        final MultivariateNormalDist dist = new MultivariateNormalDist(mean, cov);

        assertEquals(dist.p(x), 1.0 / (Math.sqrt(Math.pow(2.0 * Math.PI, 2.0) *
                        Utils.det(cov))) * Math.exp(-0.5 * ((Matrix.newFromArray(x).
                        subtractAndReturnNew(Matrix.newFromArray(mean))).
                        transposeAndReturnNew().multiplyAndReturnNew(
                Utils.inverse(cov)).multiplyAndReturnNew(
                Matrix.newFromArray(x).subtractAndReturnNew(
                        Matrix.newFromArray(mean)))).getElementAtIndex(0)),
                ABSOLUTE_ERROR);

        // Force IllegalArgumentException
        try {
            dist.p(new double[1]);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        // Force NotReadyException
        dist.setMean(new double[1]);
        try {
            dist.p(x);
            fail("NotReadyException expected but not thrown");
        } catch (final NotReadyException ignore) {
        }
    }

    @Test
    public void testCdf() throws AlgebraException,
            InvalidCovarianceMatrixException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));

        final MultivariateNormalDist dist = new MultivariateNormalDist(mean, cov);

        // check that for 2 dimensions
        assertEquals(dist.cdf(mean), 0.25, ABSOLUTE_ERROR);

        // Force IllegalArgumentException
        try {
            dist.cdf(new double[1]);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        final Matrix basis = new Matrix(2, 2);
        assertEquals(dist.cdf(mean, basis), 0.25, ABSOLUTE_ERROR);
        assertEquals(dist.getCovarianceBasis(), basis);

        // Force IllegalArgumentException
        try {
            dist.cdf(new double[3], basis);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        final double[] variances = dist.getVariances();
        assertNotNull(variances);

        final double[] v1 = basis.getSubmatrixAsArray(0, 0, 1, 0);
        final double[] v2 = basis.getSubmatrixAsArray(0, 1, 1, 1);

        // check in basis v1

        // -3 std away from mean on basis v1
        double[] x = ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v1,
                        -3.0 * Math.sqrt(variances[0])));
        assertEquals(dist.cdf(x), 0.00135 * 0.5, LARGE_ABSOLUTE_ERROR);

        // -2 std away from mean on basis v1
        x = ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v1,
                        -2.0 * Math.sqrt(variances[0])));
        assertEquals(dist.cdf(x), 0.02275 * 0.5, LARGE_ABSOLUTE_ERROR);

        // -1 std away from mean on basis v1
        x = ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v1,
                        -Math.sqrt(variances[0])));
        assertEquals(dist.cdf(x), 0.15866 * 0.5, LARGE_ABSOLUTE_ERROR);

        // on mean value
        x = mean;
        assertEquals(dist.cdf(x), 0.5 * 0.5, LARGE_ABSOLUTE_ERROR);

        // +1 std away from mean on basis v1
        x = ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v1,
                        Math.sqrt(variances[0])));
        assertEquals(dist.cdf(x), 0.84134 * 0.5, LARGE_ABSOLUTE_ERROR);

        // +2 std away from mean on basis v1
        x = ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v1,
                        +2.0 * Math.sqrt(variances[0])));
        assertEquals(dist.cdf(x), 0.97725 * 0.5, LARGE_ABSOLUTE_ERROR);

        // +3 std away from mean on basis v1
        x = ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v1,
                        +3.0 * Math.sqrt(variances[0])));
        assertEquals(dist.cdf(x), 0.99865 * 0.5, LARGE_ABSOLUTE_ERROR);


        // check in basis v2

        // -3 std away from mean on basis v2
        x = ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v2,
                        -3.0 * Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.5 * 0.00135, LARGE_ABSOLUTE_ERROR);

        // -2 std away from mean on basis v2
        x = ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v2,
                        -2.0 * Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.5 * 0.02275, LARGE_ABSOLUTE_ERROR);

        // -1 std away from mean on basis v2
        x = ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v2,
                        -Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.5 * 0.15866, LARGE_ABSOLUTE_ERROR);

        // on mean value
        x = mean;
        assertEquals(dist.cdf(x), 0.5 * 0.5, LARGE_ABSOLUTE_ERROR);

        // +1 std away from mean on basis v2
        x = ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v2,
                        Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.5 * 0.84134, LARGE_ABSOLUTE_ERROR);

        // +2 std away from mean on basis v2
        x = ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v2,
                        +2.0 * Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.5 * 0.97725, LARGE_ABSOLUTE_ERROR);

        // +3 std away from mean on basis v2
        x = ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v2,
                        +3.0 * Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.5 * 0.99865, LARGE_ABSOLUTE_ERROR);


        // check in both basis

        // -3 std away from mean on basis v1 and v2
        x = ArrayUtils.sumAndReturnNew(ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v1,
                        -3.0 * Math.sqrt(variances[0]))),
                ArrayUtils.multiplyByScalarAndReturnNew(v2,
                        -3.0 * Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.00135 * 0.00135, LARGE_ABSOLUTE_ERROR);

        // -2 std away from mean on basis v1 and v2
        x = ArrayUtils.sumAndReturnNew(ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v1,
                        -2.0 * Math.sqrt(variances[0]))),
                ArrayUtils.multiplyByScalarAndReturnNew(v2,
                        -2.0 * Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.02275 * 0.02275, LARGE_ABSOLUTE_ERROR);

        // -1 std away from mean on basis v1 and v2
        x = ArrayUtils.sumAndReturnNew(ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v1,
                        -Math.sqrt(variances[0]))),
                ArrayUtils.multiplyByScalarAndReturnNew(v2,
                        -Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.15866 * 0.15866, LARGE_ABSOLUTE_ERROR);

        // on mean value
        x = mean;
        assertEquals(dist.cdf(x), 0.5 * 0.5, LARGE_ABSOLUTE_ERROR);

        // +1 std away from mean on basis v1 and v2
        x = ArrayUtils.sumAndReturnNew(ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v1,
                        Math.sqrt(variances[0]))),
                ArrayUtils.multiplyByScalarAndReturnNew(v2,
                        Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.84134 * 0.84134, LARGE_ABSOLUTE_ERROR);

        // +2 std away from mean on basis v1 and v2
        x = ArrayUtils.sumAndReturnNew(ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v1,
                        +2.0 * Math.sqrt(variances[0]))),
                ArrayUtils.multiplyByScalarAndReturnNew(v2,
                        +2.0 * Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.97725 * 0.97725, LARGE_ABSOLUTE_ERROR);

        // +3 std away from mean on basis v1 and v2
        x = ArrayUtils.sumAndReturnNew(ArrayUtils.sumAndReturnNew(mean,
                ArrayUtils.multiplyByScalarAndReturnNew(v1,
                        +3.0 * Math.sqrt(variances[0]))),
                ArrayUtils.multiplyByScalarAndReturnNew(v2,
                        +3.0 * Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.99865 * 0.99865, LARGE_ABSOLUTE_ERROR);


        // Force NotReadyException
        dist.setMean(new double[1]);
        try {
            dist.cdf(x);
            fail("NotReadyException expected but not thrown");
        } catch (final NotReadyException ignore) {
        }
        try {
            dist.cdf(x, basis);
            fail("NotReadyException expected but not thrown");
        } catch (final NotReadyException ignore) {
        }
    }

    @Test
    public void testJointProbability() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double[] p = new double[2];
        randomizer.fill(p);

        final double jointProbability = MultivariateNormalDist.jointProbability(p);

        assertTrue(jointProbability >= 0.0 && jointProbability <= 1.0);
        assertEquals(jointProbability, p[0] * p[1], 0.0);
    }

    @Test
    public void testInvcdf() throws AlgebraException,
            InvalidCovarianceMatrixException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));

        final double[] p = new double[2];
        randomizer.fill(p); //values between 0.0 and 1.0

        final MultivariateNormalDist dist = new MultivariateNormalDist(mean, cov);

        assertEquals(dist.cdf(dist.invcdf(p)),
                MultivariateNormalDist.jointProbability(p), ABSOLUTE_ERROR);

        // Force IllegalArgumentException
        try {
            dist.invcdf(new double[1]);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        final Matrix basis = new Matrix(2, 2);
        assertEquals(dist.cdf(dist.invcdf(p, basis)),
                MultivariateNormalDist.jointProbability(p), ABSOLUTE_ERROR);
        assertEquals(basis, dist.getCovarianceBasis());

        // Force IllegalArgumentException
        try {
            dist.invcdf(new double[1], basis);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        final double[] result = new double[2];
        dist.invcdf(p, result);
        assertEquals(dist.cdf(result),
                MultivariateNormalDist.jointProbability(p), ABSOLUTE_ERROR);
        assertEquals(basis, dist.getCovarianceBasis());

        // Force IllegalArgumentException
        try {
            dist.invcdf(new double[1], result);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            dist.invcdf(p, new double[1]);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        dist.invcdf(p, result, basis);
        assertEquals(dist.cdf(result),
                MultivariateNormalDist.jointProbability(p), ABSOLUTE_ERROR);
        assertEquals(basis, dist.getCovarianceBasis());

        // Force IllegalArgumentException
        try {
            dist.invcdf(new double[1], result, basis);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            dist.invcdf(p, new double[1], basis);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }


        // Force NotReadyException
        dist.setMean(new double[1]);
        try {
            dist.invcdf(p);
            fail("NotReadyException expected but not thrown");
        } catch (final NotReadyException ignore) {
        }
        try {
            dist.invcdf(p, basis);
            fail("NotReadyException expected but not thrown");
        } catch (final NotReadyException ignore) {
        }
        try {
            dist.invcdf(p, result);
            fail("NotReadyException expected but not thrown");
        } catch (final NotReadyException ignore) {
        }
        try {
            dist.invcdf(p, result, basis);
            fail("NotReadyException expected but not thrown");
        } catch (final NotReadyException ignore) {
        }
    }

    @Test
    public void testInvcdfJointProbability() throws AlgebraException,
            InvalidCovarianceMatrixException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));

        final double jointP = randomizer.nextDouble();
        final double singleP = Math.sqrt(jointP);
        final double[] p = new double[]{singleP, singleP};

        final MultivariateNormalDist dist = new MultivariateNormalDist(mean, cov);

        final double[] x = dist.invcdf(p);

        assertArrayEquals(dist.invcdf(dist.cdf(x)), x, ABSOLUTE_ERROR);

        // Force IllegalArgumentException
        try {
            dist.invcdf(0.0);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            dist.invcdf(1.0);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }


        final Matrix basis = new Matrix(2, 2);
        assertArrayEquals(dist.invcdf(dist.cdf(x), basis), x, ABSOLUTE_ERROR);
        assertEquals(basis, dist.getCovarianceBasis());

        // Force IllegalArgumentException
        try {
            dist.invcdf(0.0, basis);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            dist.invcdf(1.0, basis);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        final double[] result = new double[2];
        dist.invcdf(dist.cdf(x), result);
        assertArrayEquals(result, x, ABSOLUTE_ERROR);
        assertEquals(basis, dist.getCovarianceBasis());

        // Force IllegalArgumentException
        try {
            dist.invcdf(dist.cdf(x), new double[1]);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }

        dist.invcdf(dist.cdf(x), result, basis);
        assertArrayEquals(result, x, ABSOLUTE_ERROR);
        assertEquals(basis, dist.getCovarianceBasis());

        // Force IllegalArgumentException
        try {
            dist.invcdf(dist.cdf(x), new double[1], basis);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }


        // Force NotReadyException
        dist.setMean(new double[1]);
        try {
            dist.invcdf(dist.cdf(x));
            fail("NotReadyException expected but not thrown");
        } catch (final NotReadyException ignore) {
        }
        try {
            dist.invcdf(dist.cdf(x), basis);
            fail("NotReadyException expected but not thrown");
        } catch (final NotReadyException ignore) {
        }
        try {
            dist.invcdf(dist.cdf(x), result);
            fail("NotReadyException expected but not thrown");
        } catch (final NotReadyException ignore) {
        }
        try {
            dist.invcdf(dist.cdf(x), result, basis);
            fail("NotReadyException expected but not thrown");
        } catch (final NotReadyException ignore) {
        }
    }

    @Test
    public void testMahalanobisDistance() throws AlgebraException,
            InvalidCovarianceMatrixException {
        // check for 2 dimensions
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double[] x = new double[2];
        randomizer.fill(x, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));

        MultivariateNormalDist dist = new MultivariateNormalDist(mean, cov);

        final Matrix meanMatrix = Matrix.newFromArray(mean);
        final Matrix xMatrix = Matrix.newFromArray(x);
        final Matrix diffMatrix = xMatrix.subtractAndReturnNew(meanMatrix);
        final Matrix transDiffMatrix = diffMatrix.transposeAndReturnNew();
        final Matrix invCov = Utils.inverse(cov);

        final Matrix value = transDiffMatrix.multiplyAndReturnNew(invCov).
                multiplyAndReturnNew(diffMatrix);
        assertEquals(value.getRows(), 1);
        assertEquals(value.getColumns(), 1);

        assertEquals(dist.squaredMahalanobisDistance(x),
                value.getElementAtIndex(0), ABSOLUTE_ERROR);
        assertEquals(dist.mahalanobisDistance(x),
                Math.sqrt(value.getElementAt(0, 0)), ABSOLUTE_ERROR);

        // check for 1 dimension
        mean = new double[1];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        x = new double[1];
        randomizer.fill(x, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        cov = DecomposerHelper.getSymmetricMatrix(1);

        dist = new MultivariateNormalDist(mean, cov);

        assertEquals(dist.squaredMahalanobisDistance(x),
                Math.pow(NormalDist.mahalanobisDistance(x[0], mean[0],
                        Math.sqrt(cov.getElementAtIndex(0))), 2.0), ABSOLUTE_ERROR);
        assertEquals(dist.mahalanobisDistance(x),
                NormalDist.mahalanobisDistance(x[0], mean[0],
                        Math.sqrt(cov.getElementAtIndex(0))), ABSOLUTE_ERROR);
    }

    @Test
    public void testProcessCovariance() throws AlgebraException,
            InvalidCovarianceMatrixException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final double[] x = new double[2];
        randomizer.fill(x, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));

        final MultivariateNormalDist dist = new MultivariateNormalDist(mean, cov);

        // check default values
        assertNull(dist.getCovarianceBasis());
        assertNull(dist.getVariances());

        // process
        dist.processCovariance();

        // check correctness
        assertNotNull(dist.getCovarianceBasis());
        assertNotNull(dist.getVariances());

        // check that basis is orthonormal (its transpose is its inverse)
        final Matrix basis = dist.getCovarianceBasis();
        assertTrue(Matrix.identity(2, 2).equals(
                basis.multiplyAndReturnNew(basis.transposeAndReturnNew()),
                ABSOLUTE_ERROR));

        // check that covariance can be expressed as: basis * variances * basis'
        final double[] variances = dist.getVariances();
        final Matrix cov2 = new Matrix(2, 2);
        cov2.copyFrom(basis);
        cov2.multiply(Matrix.diagonal(variances));
        cov2.multiply(basis.transposeAndReturnNew());

        assertTrue(cov.equals(cov2, ABSOLUTE_ERROR));
    }

    @Test
    public void testPropagate() throws WrongSizeException,
            InvalidCovarianceMatrixException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));
        cov.multiplyByScalar(1e-4);


        final MultivariateNormalDist dist = new MultivariateNormalDist(mean, cov);
        final JacobianEvaluator evaluator =
                new MultivariateNormalDist.JacobianEvaluator() {
                    @Override
                    public void evaluate(final double[] x, final double[] y, final Matrix jacobian) {
                        y[0] = x[0] * x[0] * x[1];
                        y[1] = 5 * x[0] + Math.sin(x[1]);

                        jacobian.setElementAt(0, 0, 2 * x[0] * x[1]);
                        jacobian.setElementAt(0, 1, x[0] * x[0]);

                        jacobian.setElementAt(1, 0, 5.0);
                        jacobian.setElementAt(1, 1, Math.cos(x[1]));
                    }

                    @Override
                    public int getNumberOfVariables() {
                        return 2;
                    }
                };

        MultivariateNormalDist result = new MultivariateNormalDist();
        MultivariateNormalDist.propagate(evaluator, mean, cov, result);

        // check correctness
        final double[] evaluation = new double[2];
        Matrix jacobian = new Matrix(2, 2);
        evaluator.evaluate(mean, evaluation, jacobian);
        assertArrayEquals(result.getMean(), evaluation, ABSOLUTE_ERROR);
        assertTrue(result.getCovariance().equals(
                jacobian.multiplyAndReturnNew(cov).multiplyAndReturnNew(
                        jacobian.transposeAndReturnNew()), ABSOLUTE_ERROR));


        result = MultivariateNormalDist.propagate(evaluator, mean, cov);

        // check correctness
        assertArrayEquals(result.getMean(), evaluation, ABSOLUTE_ERROR);
        assertTrue(result.getCovariance().equals(
                jacobian.multiplyAndReturnNew(cov).multiplyAndReturnNew(
                        jacobian.transposeAndReturnNew()), ABSOLUTE_ERROR));


        result = new MultivariateNormalDist();
        MultivariateNormalDist.propagate(evaluator, dist, result);

        // check correctness
        assertArrayEquals(result.getMean(), evaluation, ABSOLUTE_ERROR);
        assertTrue(result.getCovariance().equals(
                jacobian.multiplyAndReturnNew(cov).multiplyAndReturnNew(
                        jacobian.transposeAndReturnNew()), ABSOLUTE_ERROR));


        result = MultivariateNormalDist.propagate(evaluator, dist);

        // check correctness
        assertArrayEquals(result.getMean(), evaluation, ABSOLUTE_ERROR);
        assertTrue(result.getCovariance().equals(
                jacobian.multiplyAndReturnNew(cov).multiplyAndReturnNew(
                        jacobian.transposeAndReturnNew()), ABSOLUTE_ERROR));


        result = new MultivariateNormalDist();
        dist.propagateThisDistribution(evaluator, result);

        // check correctness
        assertArrayEquals(result.getMean(), evaluation, ABSOLUTE_ERROR);
        assertTrue(result.getCovariance().equals(
                jacobian.multiplyAndReturnNew(cov).multiplyAndReturnNew(
                        jacobian.transposeAndReturnNew()), ABSOLUTE_ERROR));


        result = dist.propagateThisDistribution(evaluator);

        // check correctness
        assertArrayEquals(result.getMean(), evaluation, ABSOLUTE_ERROR);
        assertTrue(result.getCovariance().equals(
                jacobian.multiplyAndReturnNew(cov).multiplyAndReturnNew(
                        jacobian.transposeAndReturnNew()), ABSOLUTE_ERROR));


        // generate a large number of Gaussian random samples and propagate
        // through function.
        final MultivariateGaussianRandomizer multiRandomizer =
                new MultivariateGaussianRandomizer(mean, cov);
        final double[] x = new double[2];
        final double[] y = new double[2];
        jacobian = new Matrix(2, 2);

        final double[] resultMean = new double[2];
        final Matrix row = new Matrix(1, 2);
        final Matrix col = new Matrix(2, 1);
        final Matrix sqr = new Matrix(2, 2);
        final Matrix sqrSum = new Matrix(2, 2);
        double[] tmp;
        for (int i = 0; i < N_SAMPLES; i++) {
            multiRandomizer.next(x);
            evaluator.evaluate(x, y, jacobian);

            tmp = Arrays.copyOf(y, 2);
            ArrayUtils.multiplyByScalar(tmp, 1.0 / (double) N_SAMPLES, tmp);
            ArrayUtils.sum(resultMean, tmp, resultMean);

            col.fromArray(y);
            row.fromArray(y);
            col.multiply(row, sqr);
            sqr.multiplyByScalar(1.0 / (double) N_SAMPLES);

            sqrSum.add(sqr);
        }

        col.fromArray(resultMean);
        row.fromArray(resultMean);
        final Matrix sqrMean = col.multiplyAndReturnNew(row);

        final Matrix resultCov = sqrSum.subtractAndReturnNew(sqrMean);

        final double maxMean = Math.max(ArrayUtils.max(mean),
                Math.abs(ArrayUtils.min(mean)));
        assertArrayEquals(result.getMean(), resultMean,
                RELATIVE_ERROR * maxMean);

        final double maxCov = Math.max(
                ArrayUtils.max(result.getCovariance().getBuffer()),
                Math.abs(ArrayUtils.min(result.getCovariance().getBuffer())));
        assertTrue(resultCov.equals(result.getCovariance(),
                RELATIVE_ERROR * maxCov));
    }
}
