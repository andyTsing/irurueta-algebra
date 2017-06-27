/**
 * @file
 * This file contains unit tests for
 * com.irurueta.statistics.MultivariateNormalDist
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date December 30, 2015
 */
package com.irurueta.statistics;

import com.irurueta.algebra.AlgebraException;
import com.irurueta.algebra.ArrayUtils;
import com.irurueta.algebra.DecomposerException;
import com.irurueta.algebra.DecomposerHelper;
import com.irurueta.algebra.Matrix;
import com.irurueta.algebra.NotReadyException;
import com.irurueta.algebra.Utils;
import com.irurueta.algebra.WrongSizeException;
import com.irurueta.statistics.MultivariateNormalDist.JacobianEvaluator;
import java.util.Arrays;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistTest {
    
    public static final double MIN_RANDOM_VALUE = -100.0;
    public static final double MAX_RANDOM_VALUE = 100.0;
    
    public static final double ABSOLUTE_ERROR = 1e-6;
    public static final double LARGE_ABSOLUTE_ERROR = 1e-3;
    
    public static final int N_SAMPLES = 1000000;
    public static final double RELATIVE_ERROR = 0.05;
    
    public MultivariateNormalDistTest() {}
    
    @BeforeClass
    public static void setUpClass() {}
    
    @AfterClass
    public static void tearDownClass() {}
    
    @Before
    public void setUp() {}
    
    @After
    public void tearDown() {}

    @Test
    public void testConstructor() throws AlgebraException, 
            InvalidCovarianceMatrixException{
        //empty constructor
        MultivariateNormalDist dist = new MultivariateNormalDist();
        
        //check correctness
        assertArrayEquals(dist.getMean(), new double[1], 0.0);
        assertEquals(dist.getCovariance(), Matrix.identity(1, 1));
        assertTrue(dist.isReady());
        assertNull(dist.getCovarianceBasis());
        assertNull(dist.getVariances());
        
        //constructor with dimensions
        dist = new MultivariateNormalDist(2);
        
        //check correctness
        assertArrayEquals(dist.getMean(), new double[2], 0.0);
        assertEquals(dist.getCovariance(), Matrix.identity(2,2));
        assertTrue(dist.isReady());
        assertNull(dist.getCovarianceBasis());
        assertNull(dist.getVariances());
        
        //Force IllegalArgumentException
        dist = null;
        try{
            dist = new MultivariateNormalDist(0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(dist);
        
        
        //constructor with mean and covariance
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));
        
        dist = new MultivariateNormalDist(mean, cov);
        
        //check correctness
        assertArrayEquals(dist.getMean(), mean, 0.0);
        assertEquals(dist.getCovariance(), cov);
        assertTrue(dist.isReady());
        assertNull(dist.getCovarianceBasis());
        assertNull(dist.getVariances());

        
        //Force IllegalArgumentException
        dist = null;
        try{
            dist = new MultivariateNormalDist(new double[0], cov);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            dist = new MultivariateNormalDist(new double[3], cov);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        Matrix wrong = DecomposerHelper.getLeftLowerTriangulatorFactor(2);
        Matrix wrong2 = DecomposerHelper.getSingularMatrixInstance(2, 2);
        Matrix wrong3 = new Matrix(2, 3);
        try{
            dist = new MultivariateNormalDist(mean, wrong);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        }catch(InvalidCovarianceMatrixException e){}
        try{
            dist = new MultivariateNormalDist(mean, wrong2);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        }catch(InvalidCovarianceMatrixException e){}
        try{
            dist = new MultivariateNormalDist(mean, wrong3);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        }catch(InvalidCovarianceMatrixException e){}        
        assertNull(dist);
        
        dist = new MultivariateNormalDist(mean, cov, false);
        
        //check correctness
        assertArrayEquals(dist.getMean(), mean, 0.0);
        assertEquals(dist.getCovariance(), cov);
        assertTrue(dist.isReady());
        assertNull(dist.getCovarianceBasis());
        assertNull(dist.getVariances());        
    }
    
    @Test
    public void testGetSetMean(){
        MultivariateNormalDist dist = new MultivariateNormalDist();
        
        //check default value
        assertArrayEquals(dist.getMean(), new double[1], 0.0);
        
        //set new value
        double[] mean = new double[2];
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        dist.setMean(mean);
        
        //check correctness
        assertArrayEquals(dist.getMean(), mean, 0.0);
        
        
        //Force IllegalArgumentException
        try{
            dist.setMean(new double[0]);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
    
    @Test
    public void testGetSetCovariance() throws AlgebraException, 
            InvalidCovarianceMatrixException{
        MultivariateNormalDist dist = new MultivariateNormalDist();
        
        //check default value
        assertEquals(dist.getCovariance(), Matrix.identity(1, 1));
        
        //set new value
        Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));

        dist.setCovariance(cov);
        
        //check correctnes
        assertEquals(dist.getCovariance(), cov);
        
        Matrix cov2 = new Matrix(2,2);
        dist.getCovariance(cov2);
        assertEquals(cov, cov2);
        
        dist.setCovariance(cov, false);
        
        //check correctnes
        assertEquals(dist.getCovariance(), cov);        
        
        //Force InvalidCovarianceMatrixException
        Matrix wrong = DecomposerHelper.getLeftLowerTriangulatorFactor(2);
        Matrix wrong2 = DecomposerHelper.getSingularMatrixInstance(2, 2);
        Matrix wrong3 = new Matrix(3, 2);
        try{
            dist.setCovariance(wrong);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        }catch(InvalidCovarianceMatrixException e){}
        try{
            dist.setCovariance(wrong2);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        }catch(InvalidCovarianceMatrixException e){}
        try{
            dist.setCovariance(wrong3);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        }catch(InvalidCovarianceMatrixException e){}        
    }
    
    @Test
    public void testSetMeanAndCovariance() throws AlgebraException, 
            InvalidCovarianceMatrixException{
        MultivariateNormalDist dist = new MultivariateNormalDist();
        
        //check default values
        assertArrayEquals(dist.getMean(), new double[1], 0.0);
        assertEquals(dist.getCovariance(), Matrix.identity(1, 1));
        
        //set new values
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));
        
        dist.setMeanAndCovariance(mean, cov);
        
        //check correctness
        assertArrayEquals(dist.getMean(), mean, 0.0);
        assertEquals(dist.getCovariance(), cov);
        
        dist.setMeanAndCovariance(mean, cov, false);

        //check correctness
        assertArrayEquals(dist.getMean(), mean, 0.0);
        assertEquals(dist.getCovariance(), cov);
        
        //Force IllegalArgumentException
        try{
            dist.setMeanAndCovariance(new double[0], cov);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            dist.setMeanAndCovariance(new double[3], cov);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        Matrix wrong = DecomposerHelper.getLeftLowerTriangulatorFactor(2);
        Matrix wrong2 = DecomposerHelper.getSingularMatrixInstance(2, 2);
        Matrix wrong3 = new Matrix(2, 3);
        try{
            dist.setMeanAndCovariance(mean, wrong);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        }catch(InvalidCovarianceMatrixException e){}
        try{
            dist.setMeanAndCovariance(mean, wrong2);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        }catch(InvalidCovarianceMatrixException e){}
        try{
            dist.setMeanAndCovariance(mean, wrong3);
            fail("InvalidCovarianceMatrixException expected but not thrown");
        }catch(InvalidCovarianceMatrixException e){}        
    }
    
    @Test
    public void testIsValidCovariance() throws AlgebraException{
        Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));        
        
        Matrix wrong = DecomposerHelper.getLeftLowerTriangulatorFactor(2);
        Matrix wrong2 = DecomposerHelper.getSingularMatrixInstance(2, 2);
        Matrix wrong3 = new Matrix(2, 3);

        assertTrue(MultivariateNormalDist.isValidCovariance(cov));
        assertFalse(MultivariateNormalDist.isValidCovariance(wrong));
        assertFalse(MultivariateNormalDist.isValidCovariance(wrong2));
        assertFalse(MultivariateNormalDist.isValidCovariance(wrong3));
    }
    
    @Test
    public void testIsReady(){
        MultivariateNormalDist dist = new MultivariateNormalDist();
        
        //check initial value
        assertTrue(dist.isReady());
        
        //force not ready
        dist.setMean(new double[2]);
        assertFalse(dist.isReady());
    }
    
    @Test
    public void testP() throws AlgebraException, 
            InvalidCovarianceMatrixException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double[] x = new double[2];
        randomizer.fill(x, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));
        
        MultivariateNormalDist dist = new MultivariateNormalDist(mean, cov);
        
        assertEquals(dist.p(x), 1.0/(Math.sqrt(Math.pow(2.0*Math.PI, 2.0)*
                Utils.det(cov)))*Math.exp(-0.5*((Matrix.newFromArray(x).
                subtractAndReturnNew(Matrix.newFromArray(mean))).
                transposeAndReturnNew().multiplyAndReturnNew(
                Utils.inverse(cov)).multiplyAndReturnNew(
                Matrix.newFromArray(x).subtractAndReturnNew(
                Matrix.newFromArray(mean)))).getElementAtIndex(0)), 
                ABSOLUTE_ERROR);
        
        //Force IllegalArgumentException
        try{
            dist.p(new double[1]);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        //Force NotReadyException
        dist.setMean(new double[1]);
        try{
            dist.p(x);
            fail("NotReadyException expected but not thrown");
        }catch(NotReadyException e){}
    }
    
    @Test
    public void testCdf() throws AlgebraException, 
            InvalidCovarianceMatrixException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));
        
        MultivariateNormalDist dist = new MultivariateNormalDist(mean, cov);
        
        //check that for 2 dimensions
        assertEquals(dist.cdf(mean), 0.25, ABSOLUTE_ERROR);
        
        //Force IllegalArgumentException
        try{
            dist.cdf(new double[1]);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        Matrix basis = new Matrix(2,2);
        assertEquals(dist.cdf(mean, basis), 0.25, ABSOLUTE_ERROR);
        assertEquals(dist.getCovarianceBasis(), basis);
        
        //Force IllegalArgumentException
        try{
            dist.cdf(new double[3], basis);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        double[] variances = dist.getVariances();
        assertNotNull(variances);
        
        double[] v1 = basis.getSubmatrixAsArray(0, 0, 1, 0);
        double[] v2 = basis.getSubmatrixAsArray(0, 1, 1, 1);
        
        //check in basis v1
        
        //-3 std away from mean on basis v1
        double[] x = ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v1, 
                -3.0 * Math.sqrt(variances[0])));        
        assertEquals(dist.cdf(x), 0.00135 * 0.5, LARGE_ABSOLUTE_ERROR);
        
        //-2 std away from mean on basis v1
        x = ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v1, 
                -2.0 * Math.sqrt(variances[0])));        
        assertEquals(dist.cdf(x), 0.02275 * 0.5, LARGE_ABSOLUTE_ERROR);
        
        //-1 std away from mean on basis v1
        x = ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v1, 
                - Math.sqrt(variances[0])));        
        assertEquals(dist.cdf(x), 0.15866 * 0.5, LARGE_ABSOLUTE_ERROR);
        
        //on mean value
        x = mean;
        assertEquals(dist.cdf(x), 0.5 * 0.5, LARGE_ABSOLUTE_ERROR);
        
        //+1 std away from mean on basis v1
        x = ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v1, 
                Math.sqrt(variances[0])));        
        assertEquals(dist.cdf(x), 0.84134 * 0.5, LARGE_ABSOLUTE_ERROR);
        
        //+2 std away from mean on basis v1
        x = ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v1, 
                 + 2.0 * Math.sqrt(variances[0])));        
        assertEquals(dist.cdf(x), 0.97725 * 0.5, LARGE_ABSOLUTE_ERROR);
        
        //+3 std away from mean on basis v1
        x = ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v1, 
                + 3.0 * Math.sqrt(variances[0])));        
        assertEquals(dist.cdf(x), 0.99865 * 0.5, LARGE_ABSOLUTE_ERROR);


        //check in basis v2
        
        //-3 std away from mean on basis v2
        x = ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v2, 
                -3.0 * Math.sqrt(variances[1])));        
        assertEquals(dist.cdf(x), 0.5 * 0.00135, LARGE_ABSOLUTE_ERROR);
        
        //-2 std away from mean on basis v2
        x = ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v2, 
                -2.0 * Math.sqrt(variances[1])));        
        assertEquals(dist.cdf(x), 0.5 * 0.02275, LARGE_ABSOLUTE_ERROR);
        
        //-1 std away from mean on basis v2
        x = ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v2, 
                        - Math.sqrt(variances[1])));        
        assertEquals(dist.cdf(x), 0.5 * 0.15866, LARGE_ABSOLUTE_ERROR);
        
        //on mean value
        x = mean;
        assertEquals(dist.cdf(x), 0.5 * 0.5, LARGE_ABSOLUTE_ERROR);
        
        //+1 std away from mean on basis v2
        x = ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v2, 
                        Math.sqrt(variances[1])));        
        assertEquals(dist.cdf(x), 0.5 * 0.84134, LARGE_ABSOLUTE_ERROR);
        
        //+2 std away from mean on basis v2
        x = ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v2, 
                        + 2.0 * Math.sqrt(variances[1])));        
        assertEquals(dist.cdf(x), 0.5 * 0.97725, LARGE_ABSOLUTE_ERROR);
        
        //+3 std away from mean on basis v2
        x = ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v2, 
                        + 3.0 * Math.sqrt(variances[1])));        
        assertEquals(dist.cdf(x), 0.5 * 0.99865, LARGE_ABSOLUTE_ERROR);
        
        
        //check in both basis
        
        //-3 std away from mean on basis v1 and v2
        x = ArrayUtils.sumAndReturnNew(ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v1, 
                -3.0 * Math.sqrt(variances[0]))), 
                ArrayUtils.multiplyByScalarAndReturnNew(v2, 
                -3.0 * Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.00135 * 0.00135, LARGE_ABSOLUTE_ERROR);
        
        //-2 std away from mean on basis v1 and v2
        x = ArrayUtils.sumAndReturnNew(ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v1, 
                -2.0 * Math.sqrt(variances[0]))), 
                ArrayUtils.multiplyByScalarAndReturnNew(v2, 
                -2.0 * Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.02275 * 0.02275, LARGE_ABSOLUTE_ERROR);
        
        //-1 std away from mean on basis v1 and v2
        x = ArrayUtils.sumAndReturnNew(ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v1, 
                - Math.sqrt(variances[0]))), 
                ArrayUtils.multiplyByScalarAndReturnNew(v2, 
                - Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.15866 * 0.15866, LARGE_ABSOLUTE_ERROR);
        
        //on mean value
        x = mean;
        assertEquals(dist.cdf(x), 0.5 * 0.5, LARGE_ABSOLUTE_ERROR);
        
        //+1 std away from mean on basis v1 and v2
        x = ArrayUtils.sumAndReturnNew(ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v1, 
                Math.sqrt(variances[0]))), 
                ArrayUtils.multiplyByScalarAndReturnNew(v2, 
                Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.84134 * 0.84134, LARGE_ABSOLUTE_ERROR);
        
        //+2 std away from mean on basis v1 and v2
        x = ArrayUtils.sumAndReturnNew(ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v1, 
                +2.0 * Math.sqrt(variances[0]))), 
                ArrayUtils.multiplyByScalarAndReturnNew(v2, 
                +2.0 * Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.97725 * 0.97725, LARGE_ABSOLUTE_ERROR);

        //+3 std away from mean on basis v1 and v2
        x = ArrayUtils.sumAndReturnNew(ArrayUtils.sumAndReturnNew(mean, 
                ArrayUtils.multiplyByScalarAndReturnNew(v1, 
                +3.0 * Math.sqrt(variances[0]))), 
                ArrayUtils.multiplyByScalarAndReturnNew(v2, 
                +3.0 * Math.sqrt(variances[1])));
        assertEquals(dist.cdf(x), 0.99865 * 0.99865, LARGE_ABSOLUTE_ERROR);        
        
        
        //Force NotReadyException
        dist.setMean(new double[1]);
        try{
            dist.cdf(x);
            fail("NotReadyException expected but not thrown");
        }catch(NotReadyException e){}        
        try{
            dist.cdf(x, basis);
            fail("NotReadyException expected but not thrown");
        }catch(NotReadyException e){}                
    }
    
    @Test
    public void testJointProbability(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double[] p = new double[2];
        randomizer.fill(p);
        
        double jointProbability = MultivariateNormalDist.jointProbability(p);
        
        assertTrue(jointProbability >= 0.0 && jointProbability <= 1.0);
        assertEquals(jointProbability, p[0]*p[1], 0.0);
    }
    
    @Test
    public void testInvcdf() throws AlgebraException, 
            InvalidCovarianceMatrixException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));
        
        double[] p = new double[2];
        randomizer.fill(p); //values between 0.0 and 1.0
        
        MultivariateNormalDist dist = new MultivariateNormalDist(mean, cov);
        
        assertEquals(dist.cdf(dist.invcdf(p)), 
                MultivariateNormalDist.jointProbability(p), ABSOLUTE_ERROR);
        
        //Force IllegalArgumentException
        try{
            dist.invcdf(new double[1]);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        Matrix basis = new Matrix(2,2);
        assertEquals(dist.cdf(dist.invcdf(p, basis)), 
                MultivariateNormalDist.jointProbability(p), ABSOLUTE_ERROR);
        assertEquals(basis, dist.getCovarianceBasis());
        
        //Force IllegalArgumentException
        try{
            dist.invcdf(new double[1], basis);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        double[] result = new double[2];
        dist.invcdf(p, result);
        assertEquals(dist.cdf(result),
                MultivariateNormalDist.jointProbability(p), ABSOLUTE_ERROR);
        assertEquals(basis, dist.getCovarianceBasis());
        
        //Force IllegalArgumentException
        try{
            dist.invcdf(new double[1], result);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            dist.invcdf(p, new double[1]);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
                
        dist.invcdf(p, result, basis);
        assertEquals(dist.cdf(result),
                MultivariateNormalDist.jointProbability(p), ABSOLUTE_ERROR);
        assertEquals(basis, dist.getCovarianceBasis());
        
        //Force IllegalArgumentException        
        try{
            dist.invcdf(new double[1], result, basis);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            dist.invcdf(p, new double[1], basis);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        
        //Force NotReadyException
        dist.setMean(new double[1]); 
        try{
            dist.invcdf(p);
            fail("NotReadyException expected but not thrown");
        }catch(NotReadyException e){}     
        try{
            dist.invcdf(p, basis);
            fail("NotReadyException expected but not thrown");
        }catch(NotReadyException e){}
        try{
            dist.invcdf(p, result);
            fail("NotReadyException expected but not thrown");
        }catch(NotReadyException e){}
        try{
            dist.invcdf(p, result, basis);
            fail("NotReadyException expected but not thrown");
        }catch(NotReadyException e){}
    }
    
    @Test
    public void testInvcdfJointProbability() throws AlgebraException, 
            InvalidCovarianceMatrixException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));
        
        double jointP = randomizer.nextDouble();
        double singleP = Math.sqrt(jointP);
        double[] p = new double[]{singleP, singleP};
        
        MultivariateNormalDist dist = new MultivariateNormalDist(mean, cov);
        
        double[] x = dist.invcdf(p);
        
        assertArrayEquals(dist.invcdf(dist.cdf(x)), x, ABSOLUTE_ERROR);
        
        //Force IllegalArgumentException
        try{
            dist.invcdf(0.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            dist.invcdf(1.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        
        Matrix basis = new Matrix(2,2);
        assertArrayEquals(dist.invcdf(dist.cdf(x), basis), x, ABSOLUTE_ERROR);
        assertEquals(basis, dist.getCovarianceBasis());
        
        //Force IllegalArgumentException
        try{
            dist.invcdf(0.0, basis);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            dist.invcdf(1.0, basis);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        double[] result = new double[2];
        dist.invcdf(dist.cdf(x), result);
        assertArrayEquals(result, x, ABSOLUTE_ERROR);
        assertEquals(basis, dist.getCovarianceBasis());
        
        //Force IllegalArgumentException
        try{
            dist.invcdf(dist.cdf(x), new double[1]);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
                
        dist.invcdf(dist.cdf(x), result, basis);
        assertArrayEquals(result, x, ABSOLUTE_ERROR);
        assertEquals(basis, dist.getCovarianceBasis());
        
        //Force IllegalArgumentException        
        try{
            dist.invcdf(dist.cdf(x), new double[1], basis);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        
        //Force NotReadyException
        dist.setMean(new double[1]); 
        try{
            dist.invcdf(dist.cdf(x));
            fail("NotReadyException expected but not thrown");
        }catch(NotReadyException e){}     
        try{
            dist.invcdf(dist.cdf(x), basis);
            fail("NotReadyException expected but not thrown");
        }catch(NotReadyException e){}
        try{
            dist.invcdf(dist.cdf(x), result);
            fail("NotReadyException expected but not thrown");
        }catch(NotReadyException e){}
        try{
            dist.invcdf(dist.cdf(x), result, basis);
            fail("NotReadyException expected but not thrown");
        }catch(NotReadyException e){}
    }   
    
    @Test
    public void testMahalanobisDistance() throws AlgebraException, 
            InvalidCovarianceMatrixException{
        //check for 2 dimensions
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double[] x = new double[2];
        randomizer.fill(x, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));
        
        MultivariateNormalDist dist = new MultivariateNormalDist(mean, cov);
        
        Matrix meanMatrix = Matrix.newFromArray(mean);
        Matrix xMatrix = Matrix.newFromArray(x);
        Matrix diffMatrix = xMatrix.subtractAndReturnNew(meanMatrix);
        Matrix transDiffMatrix = diffMatrix.transposeAndReturnNew();
        Matrix invCov = Utils.inverse(cov);
        
        Matrix value = transDiffMatrix.multiplyAndReturnNew(invCov).
                multiplyAndReturnNew(diffMatrix);
        assertEquals(value.getRows(), 1);
        assertEquals(value.getColumns(), 1);
        
        assertEquals(dist.squaredMahalanobisDistance(x), 
                value.getElementAtIndex(0), ABSOLUTE_ERROR);
        assertEquals(dist.mahalanobisDistance(x), 
                Math.sqrt(value.getElementAt(0, 0)), ABSOLUTE_ERROR);
        
        //check for 1 dimension
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
            InvalidCovarianceMatrixException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double[] x = new double[2];
        randomizer.fill(x, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));
        
        MultivariateNormalDist dist = new MultivariateNormalDist(mean, cov);        
        
        //check default values
        assertNull(dist.getCovarianceBasis());
        assertNull(dist.getVariances());
        
        //process
        dist.processCovariance();
        
        //check correctness
        assertNotNull(dist.getCovarianceBasis());
        assertNotNull(dist.getVariances());
        
        //check that basis is orthonormal (its transpose is its inverse)
        Matrix basis = dist.getCovarianceBasis();
        assertTrue(Matrix.identity(2, 2).equals(
                basis.multiplyAndReturnNew(basis.transposeAndReturnNew()),
                ABSOLUTE_ERROR));
        
        //check that covariance can be expressed as: basis * variances * basis'
        double[] variances = dist.getVariances();
        Matrix cov2 = new Matrix(2, 2);
        cov2.copyFrom(basis);
        cov2.multiply(Matrix.diagonal(variances));
        cov2.multiply(basis.transposeAndReturnNew());
        
        assertTrue(cov.equals(cov2, ABSOLUTE_ERROR));
    }
    
    @Test
    public void testPropagate() throws WrongSizeException, 
            InvalidCovarianceMatrixException,
            DecomposerException {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double[] mean = new double[2];
        randomizer.fill(mean, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        Matrix cov = DecomposerHelper.
                getSymmetricPositiveDefiniteMatrixInstance(
                        DecomposerHelper.getLeftLowerTriangulatorFactor(2));
        cov.multiplyByScalar(1e-4);
        
        
        MultivariateNormalDist dist = new MultivariateNormalDist(mean, cov);
        JacobianEvaluator evaluator = 
                new MultivariateNormalDist.JacobianEvaluator() {
            @Override
            public void evaluate(double[] x, double[] y, Matrix jacobian) {
                y[0] = x[0]*x[0]*x[1];
                y[1] = 5*x[0] + Math.sin(x[1]);
                
                jacobian.setElementAt(0, 0, 2*x[0]*x[1]);
                jacobian.setElementAt(0, 1, x[0]*x[0]);
                
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
        
        //check correctness
        double[] evaluation =new double[2];
        Matrix jacobian = new Matrix(2,2);
        evaluator.evaluate(mean, evaluation, jacobian);
        assertArrayEquals(result.getMean(), evaluation, ABSOLUTE_ERROR);
        assertTrue(result.getCovariance().equals(
                jacobian.multiplyAndReturnNew(cov).multiplyAndReturnNew(
                jacobian.transposeAndReturnNew()), ABSOLUTE_ERROR));
        
        
        result = MultivariateNormalDist.propagate(evaluator, mean, cov);
        
        //check correctness
        assertArrayEquals(result.getMean(), evaluation, ABSOLUTE_ERROR);
        assertTrue(result.getCovariance().equals(
                jacobian.multiplyAndReturnNew(cov).multiplyAndReturnNew(
                jacobian.transposeAndReturnNew()), ABSOLUTE_ERROR));
        
        
        result = new MultivariateNormalDist();
        MultivariateNormalDist.propagate(evaluator, dist, result);
        
        //check correctness
        assertArrayEquals(result.getMean(), evaluation, ABSOLUTE_ERROR);
        assertTrue(result.getCovariance().equals(
                jacobian.multiplyAndReturnNew(cov).multiplyAndReturnNew(
                jacobian.transposeAndReturnNew()), ABSOLUTE_ERROR));

        
        result = MultivariateNormalDist.propagate(evaluator, dist);

        //check correctness
        assertArrayEquals(result.getMean(), evaluation, ABSOLUTE_ERROR);
        assertTrue(result.getCovariance().equals(
                jacobian.multiplyAndReturnNew(cov).multiplyAndReturnNew(
                jacobian.transposeAndReturnNew()), ABSOLUTE_ERROR)); 
        
        
        result = new MultivariateNormalDist();
        dist.propagateThisDistribution(evaluator, result);

        //check correctness
        assertArrayEquals(result.getMean(), evaluation, ABSOLUTE_ERROR);
        assertTrue(result.getCovariance().equals(
                jacobian.multiplyAndReturnNew(cov).multiplyAndReturnNew(
                jacobian.transposeAndReturnNew()), ABSOLUTE_ERROR));

                
        result = dist.propagateThisDistribution(evaluator);
        
        //check correctness
        assertArrayEquals(result.getMean(), evaluation, ABSOLUTE_ERROR);
        assertTrue(result.getCovariance().equals(
                jacobian.multiplyAndReturnNew(cov).multiplyAndReturnNew(
                jacobian.transposeAndReturnNew()), ABSOLUTE_ERROR));
        
        
        //generate a large number of Gaussian random samples and propagate
        //through function.
        MultivariateGaussianRandomizer multiRandomizer = 
                new MultivariateGaussianRandomizer(mean, cov);
        double[] x = new double[2];
        double[] y = new double[2];
        jacobian = new Matrix(2,2);
        
        double[] resultMean = new double[2];
        Matrix row = new Matrix(1,2);
        Matrix col = new Matrix(2,1);
        Matrix sqr = new Matrix(2, 2);
        Matrix sqrSum = new Matrix(2,2);
        double[] tmp;
        for(int i = 0; i < N_SAMPLES; i++) {
            multiRandomizer.next(x);
            evaluator.evaluate(x, y, jacobian);
            
            tmp = Arrays.copyOf(y, 2);
            ArrayUtils.multiplyByScalar(tmp, 1.0 / (double)N_SAMPLES, tmp);
            ArrayUtils.sum(resultMean, tmp, resultMean);
            
            col.fromArray(y);
            row.fromArray(y);
            col.multiply(row, sqr);
            sqr.multiplyByScalar(1.0 / (double)N_SAMPLES);
            
            sqrSum.add(sqr);
        }
        
        col.fromArray(resultMean);
        row.fromArray(resultMean);
        Matrix sqrMean = col.multiplyAndReturnNew(row);
        
        Matrix resultCov = sqrSum.subtractAndReturnNew(sqrMean);
        
        double maxMean = Math.max(ArrayUtils.max(mean), 
                Math.abs(ArrayUtils.min(mean)));
        assertArrayEquals(result.getMean(), resultMean, 
                RELATIVE_ERROR * maxMean);
                
        double maxCov = Math.max(
                ArrayUtils.max(result.getCovariance().getBuffer()), 
                Math.abs(ArrayUtils.min(result.getCovariance().getBuffer())));
        assertTrue(resultCov.equals(result.getCovariance(), 
                RELATIVE_ERROR * maxCov));        
    }
}
