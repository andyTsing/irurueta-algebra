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

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

public class ArrayUtilsTest {
    
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 50;
    
    private static final double MIN_RANDOM_VALUE = -100.0;
    private static final double MAX_RANDOM_VALUE = 100.0;
    
    private static final double ABSOLUTE_ERROR = 1e-6;

    private static final int TIMES = 100;
    
    public ArrayUtilsTest() { }

    @BeforeClass
    public static void setUpClass() { }

    @AfterClass
    public static void tearDownClass() { }
    
    @Before
    public void setUp() { }
    
    @After
    public void tearDown() { }
    
    @Test
    public void testMultiplyByScalar() {
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        double scalar = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        
        double[] input = new double[length];
        randomizer.fill(input, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        double[] expectedResult = new double[length];
        for (int i = 0; i < length; i++) {
            expectedResult[i] = input[i] * scalar;
        }
        
        double[] result1 = ArrayUtils.multiplyByScalarAndReturnNew(input, 
                scalar);
        
        double[] result2 = new double[length];
        ArrayUtils.multiplyByScalar(input, scalar, result2);
        
        //check correctness
        assertEquals(result1.length, length);
        for (int i = 0; i < length; i++) {
            assertEquals(result1[i], expectedResult[i], 0.0);
            assertEquals(result2[i], expectedResult[i], 0.0);
        }
        
        //Force IllegalArgumentException
        double[] wrongResult = new double[length + 1];
        try {
            ArrayUtils.multiplyByScalar(input, scalar, wrongResult);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testSum() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        
        double[] input1 = new double[length];
        double[] input2 = new double[length];
        randomizer.fill(input1, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        randomizer.fill(input2, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        double[] expectedResult = new double[length];
        for (int i = 0; i < length; i++) {
            expectedResult[i] = input1[i] + input2[i];
        }
        
        double[] result1 = ArrayUtils.sumAndReturnNew(input1, input2);
        
        double[] result2 = new double[length];
        ArrayUtils.sum(input1, input2, result2);
        
        //check correctness
        assertEquals(result1.length, length);
        for (int i = 0; i < length; i++) {
            assertEquals(result1[i], expectedResult[i], 0.0);
            assertEquals(result2[i], expectedResult[i], 0.0);
        }
        
        //Force IllegalArgumentException
        double[] wrongArray = new double[length + 1];
        try {
            ArrayUtils.sum(input1, input2, wrongArray);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            ArrayUtils.sumAndReturnNew(input1, wrongArray);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testSubtract() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        
        double[] input1 = new double[length];
        double[] input2 = new double[length];
        randomizer.fill(input1, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        randomizer.fill(input2, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        double[] expectedResult = new double[length];
        for (int i = 0; i < length; i++) {
            expectedResult[i] = input1[i] - input2[i];
        }
        
        double[] result1 = ArrayUtils.subtractAndReturnNew(input1, input2);
        
        double[] result2 = new double[length];
        ArrayUtils.subtract(input1, input2, result2);
        
        //check correctness
        assertEquals(result1.length, length);
        for (int i = 0; i < length; i++) {
            assertEquals(result1[i], expectedResult[i], 0.0);
            assertEquals(result2[i], expectedResult[i], 0.0);
        }
        
        //Force IllegalArgumentException
        double[] wrongArray = new double[length + 1];
        try {
            ArrayUtils.subtract(input1, input2, wrongArray);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            ArrayUtils.subtractAndReturnNew(input1, wrongArray);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }    
    
    @Test
    public void testDotProduct() throws WrongSizeException {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        
        double[] input1 = new double[length];
        double[] input2 = new double[length];
        randomizer.fill(input1, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        randomizer.fill(input2, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        double expectedResult = 0.0;
        for (int i = 0; i < length; i++) {
            expectedResult += input1[i] * input2[i];
        }
        
        double result = ArrayUtils.dotProduct(input1, input2);
                
        //check correctness
        assertEquals(result, expectedResult, 0.0);
        
        //Force IllegalArgumentException
        double[] wrongArray = new double[length + 1];
        try {
            //noinspection all
            ArrayUtils.dotProduct(input1, wrongArray);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            //noinspection all
            ArrayUtils.dotProduct(wrongArray, input2);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        
        //Test with jacobians
        Matrix jacobian1 = new Matrix(1, length);
        Matrix jacobian2 = new Matrix(1, length);
        result = ArrayUtils.dotProduct(input1, input2, jacobian1, jacobian2);
        
        //check correctness
        assertEquals(result, expectedResult, 0.0);
        
        assertArrayEquals(jacobian1.getBuffer(), input1, 0.0);
        assertArrayEquals(jacobian2.getBuffer(), input2, 0.0);
        
        //Force IllegalArgumentException
        try {
            ArrayUtils.dotProduct(wrongArray, input2, jacobian1, jacobian2);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            ArrayUtils.dotProduct(input1, wrongArray, jacobian1, jacobian2);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            ArrayUtils.dotProduct(input1, input2, new Matrix(1,1), jacobian2);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            ArrayUtils.dotProduct(input1, input2, jacobian1, new Matrix(1,1));
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testAngle() {
        int numValid = 0;
        for (int t = 0; t < TIMES; t++) {
            final UniformRandomizer randomizer = new UniformRandomizer(new Random());
            final int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);

            final double[] input1 = new double[length];
            final double[] input2 = new double[length];
            randomizer.fill(input1, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
            randomizer.fill(input2, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

            final double norm1 = Utils.normF(input1);
            final double norm2 = Utils.normF(input2);

            if (norm1 < ABSOLUTE_ERROR || norm2 < ABSOLUTE_ERROR) {
                continue;
            }

            double dotProduct = 0.0;
            for (int i = 0; i < length; i++) {
                dotProduct += input1[i] * input2[i];
            }

            double expectedResult = Math.acos(dotProduct / norm1 / norm2);

            double result = ArrayUtils.angle(input1, input2);

            //check correctness
            assertEquals(result, expectedResult, ABSOLUTE_ERROR);
            assertEquals(ArrayUtils.angle(input2, input1), expectedResult, ABSOLUTE_ERROR);
            assertEquals(ArrayUtils.angle(input1, input1), 0.0, ABSOLUTE_ERROR);
            assertEquals(ArrayUtils.angle(input2, input2), 0.0, ABSOLUTE_ERROR);

            numValid++;
            break;
        }

        assertTrue(numValid > 0);
    }
    
    @Test
    public void testMultiplyByScalarComplex() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        double scalar = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        
        Complex[] input = new Complex[length];
        //fill array with random values
        for (int i = 0; i < length; i++) {
            input[i] = new Complex(randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE), randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE));
        }
        
        Complex[] expectedResult = new Complex[length];
        for (int i = 0; i < length; i++) {
            expectedResult[i] = input[i].multiplyByScalarAndReturnNew(scalar);
        }
        
        Complex[] result1 = ArrayUtils.multiplyByScalarAndReturnNew(input, 
                scalar);
        
        Complex[] result2 = new Complex[length];
        //Force NullPointerException (because result2 hasn't been initialized
        //with instances in the array
        try {
            ArrayUtils.multiplyByScalar(input, scalar, result2);        
            fail("NullPointerException expected but not thrown");
        } catch (NullPointerException ignore) { }
        
        //initialize array with instances (otherwise null pointer exception will
        //be raised
        for (int i = 0; i < length; i++) {
            result2[i] = new Complex();
        }
        ArrayUtils.multiplyByScalar(input, scalar, result2);
        
        //check correctness
        assertEquals(result1.length, length);
        for (int i = 0; i < length; i++) {
            assertEquals(result1[i], expectedResult[i]);
            assertEquals(result2[i], expectedResult[i]);
        }
        
        //Force IllegalArgumentException
        Complex[] wrongResult = new Complex[length + 1];
        for (int i = 0; i < length; i++) {
            wrongResult[i] = new Complex();
        }
        try {
            ArrayUtils.multiplyByScalar(input, scalar, wrongResult);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testSumComplex() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        
        Complex[] input1 = new Complex[length];
        Complex[] input2 = new Complex[length];
        //fill array with random values
        for (int i = 0; i < length; i++) {
            input1[i] = new Complex(randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE), randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE));
            input2[i] = new Complex(randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE), randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE));            
        }
        
        Complex[] expectedResult = new Complex[length];
        for (int i = 0; i < length; i++) {
            expectedResult[i] = input1[i].addAndReturnNew(input2[i]);
        }
        
        Complex[] result1 = ArrayUtils.sumAndReturnNew(input1, input2);
        
        Complex[] result2 = new Complex[length];
        //Force NullPointerException (because result2 hasn't been initialized
        //with instances in the array)
        try {
            ArrayUtils.sum(input1, input2, result2);
            fail("NullPointerException expected but not thrown");
        } catch (NullPointerException ignore) { }
        
        //initialize array with instances (otherwise null pointer exception will
        //be raised
        for (int i = 0; i < length; i++) {
            result2[i] = new Complex();
        }
        ArrayUtils.sum(input1, input2, result2);
        
        //check correctness
        assertEquals(result1.length, length);
        for (int i = 0; i < length; i++) {
            assertEquals(result1[i], expectedResult[i]);
            assertEquals(result2[i], expectedResult[i]);
        }
        
        //Force IllegalArgumentException
        Complex[] wrongArray = new Complex[length + 1];
        try {
            ArrayUtils.sum(input1, input2, wrongArray);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            ArrayUtils.sumAndReturnNew(input1, wrongArray);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testSubtractComplex() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        
        Complex[] input1 = new Complex[length];
        Complex[] input2 = new Complex[length];
        //fill array with random values
        for (int i = 0; i < length; i++) {
            input1[i] = new Complex(randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE), randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE));
            input2[i] = new Complex(randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE), randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE));            
        }
        
        Complex[] expectedResult = new Complex[length];
        for (int i = 0; i < length; i++) {
            expectedResult[i] = input1[i].subtractAndReturnNew(input2[i]);
        }
        
        Complex[] result1 = ArrayUtils.subtractAndReturnNew(input1, input2);
        
        Complex[] result2 = new Complex[length];
        //Force NullPointerException (because result2 hasn't been initialized
        //with instances in the array)
        try {
            ArrayUtils.subtract(input1, input2, result2);
            fail("NullPointerException expected but not thrown");
        } catch (NullPointerException ignore) { }
        
        //initialize array with instances (otherwise null pointer exception will
        //be raised
        for (int i = 0; i < length; i++) {
            result2[i] = new Complex();
        }
        ArrayUtils.subtract(input1, input2, result2);
        
        //check correctness
        assertEquals(result1.length, length);
        for (int i = 0; i < length; i++) {
            assertEquals(result1[i], expectedResult[i]);
            assertEquals(result2[i], expectedResult[i]);
        }
        
        //Force IllegalArgumentException
        Complex[] wrongArray = new Complex[length + 1];
        try {
            ArrayUtils.subtract(input1, input2, wrongArray);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            ArrayUtils.subtractAndReturnNew(input1, wrongArray);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }    
    
    @Test
    public void testDotProductComplex() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        
        Complex[] input1 = new Complex[length];
        Complex[] input2 = new Complex[length];
        //fill array with random values
        for (int i = 0; i < length; i++) {
            input1[i] = new Complex(randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE), randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE));
            input2[i] = new Complex(randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE), randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE));            
        }
        
        Complex expectedResult = new Complex(0.0);
        for (int i = 0; i < length; i++) {
            expectedResult.add(input1[i].multiplyAndReturnNew(input2[i]));
        }
                
        Complex result = ArrayUtils.dotProduct(input1, input2);
                
        //check correctness
        assertEquals(result, expectedResult);
        
        //Force IllegalArgumentException
        Complex[] wrongArray = new Complex[length + 1];
        try {
            ArrayUtils.dotProduct(input1, wrongArray);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            ArrayUtils.dotProduct(wrongArray, input2);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }  
    
    @Test
    public void testNormalize() throws AlgebraException {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        
        double[] v = new double[length];
        randomizer.fill(v);
        
        double[] result = new double[length];
        Matrix jacobian = new Matrix(length, length);
        
        ArrayUtils.normalize(v, result, jacobian);
        
        //check correctness
        double norm = Utils.normF(v);
        double[] result2 = ArrayUtils.multiplyByScalarAndReturnNew(v, 
                1.0 / norm);
        assertArrayEquals(result, result2, ABSOLUTE_ERROR);
        
        Matrix jacobian2 = Matrix.identity(length, length);
        jacobian2.multiplyByScalar(norm * norm);
        jacobian2.subtract(Matrix.newFromArray(v,true).multiplyAndReturnNew(
                Matrix.newFromArray(v,false)));
        jacobian2.multiplyByScalar(1.0 / (norm*norm*norm));
        
        assertTrue(jacobian.equals(jacobian2, ABSOLUTE_ERROR));
        
        //Force IllegalArgumentException
        try {
            ArrayUtils.normalize(new double[length + 1], result, jacobian);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            ArrayUtils.normalize(v, new double[length + 1], jacobian);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            ArrayUtils.normalize(v, result, new Matrix(length + 1, length));
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        
        
        //test normalize and return new with jacobian
        jacobian = new Matrix(length, length);
        result = ArrayUtils.normalizeAndReturnNew(v, jacobian);
                       
        //check correctness
        assertArrayEquals(result, result2, ABSOLUTE_ERROR);
        assertTrue(jacobian.equals(jacobian2, ABSOLUTE_ERROR));
        
        //Force IllegalArgumentException
        try {
            ArrayUtils.normalizeAndReturnNew(new double[length + 1], jacobian);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            ArrayUtils.normalizeAndReturnNew(v, new Matrix(length + 1, length));
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        
        
        //test normalize and update with jacobian
        double[] v2 = Arrays.copyOf(v, length);
        jacobian = new Matrix(length, length);
        ArrayUtils.normalize(v2, jacobian);
        
        //check correctness
        assertArrayEquals(result, v2, ABSOLUTE_ERROR);
        assertTrue(jacobian.equals(jacobian2, ABSOLUTE_ERROR));
        
        //Force IllegalArgumentException
        try {
            ArrayUtils.normalize(new double[length + 1], jacobian);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            ArrayUtils.normalize(v, new Matrix(length + 1, length));
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        
        //test normalize and return new
        result = ArrayUtils.normalizeAndReturnNew(v);
        
        //check correctness
        assertArrayEquals(result, result2, ABSOLUTE_ERROR);  
        
        //test normalize and update
        v2 = Arrays.copyOf(v, length);
        ArrayUtils.normalize(v2);
        
        //check correctness
        assertArrayEquals(result, v2, ABSOLUTE_ERROR);


        //test for zero norm
        v = new double[length];
        result = new double[length];
        jacobian = new Matrix(length, length);

        ArrayUtils.normalize(v, result, jacobian);

        //check
        Arrays.fill(result2, Double.MAX_VALUE);
        assertArrayEquals(result, result2, 0.0);

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                assertEquals(jacobian.getElementAt(i, j),
                        Double.MAX_VALUE, 0.0);
            }
        }
    }
    
    @Test
    public void testReverse() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        
        double[] v = new double[length];
        randomizer.fill(v);
        
        double[] result = new double[length];
        ArrayUtils.reverse(v, result);
        
        //check correctness
        double[] result2 = new double[length];
        for (int i = 0; i < length; i++) {
            result2[length - 1 -i] = v[i];
        }
        
        assertArrayEquals(result, result2, ABSOLUTE_ERROR);
        
        ArrayUtils.reverse(v);
        
        //check correctness
        assertArrayEquals(v, result2, ABSOLUTE_ERROR);

        
        //test for odd/even case
        length++; //if length was even it will be now odd, and viceversa
        
        v = new double[length];
        randomizer.fill(v);
        
        result = new double[length];
        ArrayUtils.reverse(v, result);
        
        //check correctness
        result2 = new double[length];
        for (int i = 0; i < length; i++) {
            result2[length - 1 -i] = v[i];
        }
        
        assertArrayEquals(result, result2, ABSOLUTE_ERROR);
        
        ArrayUtils.reverse(v);
        
        //check correctness
        assertArrayEquals(v, result2, ABSOLUTE_ERROR);        
    }
    
    @Test
    public void testReverseAndReturnNew() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        
        double[] v = new double[length];
        randomizer.fill(v);
        
        double[] result = ArrayUtils.reverseAndReturnNew(v);
        
        //check correctness
        double[] result2 = new double[length];
        for (int i = 0; i < length; i++) {
            result2[length - 1 -i] = v[i];
        }
        
        assertArrayEquals(result, result2, ABSOLUTE_ERROR);
        
        
        //test for odd/even case
        length++; //if length was even it will be now odd, and viceversa
        
        v = new double[length];
        randomizer.fill(v);
        
        result = ArrayUtils.reverseAndReturnNew(v);
        
        //check correctness
        result2 = new double[length];
        for (int i = 0; i < length; i++) {
            result2[length - 1 -i] = v[i];
        }
        
        assertArrayEquals(result, result2, ABSOLUTE_ERROR);        
    }
    
    @Test
    public void testReverseComplex() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        
        Complex[] v = new Complex[length];
        //fill array with random values
        for (int i = 0; i < length; i++) {
            v[i] = new Complex(randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE), randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE));
        }        
        
        Complex[] result = new Complex[length];
        ArrayUtils.reverse(v, result);
        
        //check correctness        
        assertEquals(result.length, length);
        for (int i = 0; i < length; i++) {
            assertEquals(result[length - 1 - i], v[i]);
        }
        
        //copy and reverse
        Complex[] result2 = new Complex[length];
        for (int i = 0; i < length; i++) {
            result2[i] = result[i].clone();
        }
        ArrayUtils.reverse(v);
        
        //check correctness
        assertEquals(v.length, result2.length);
        for (int i = 0; i < length; i++) {
            assertEquals(v[i], result2[i]);
        }
        
        //test for odd/even case
        length++; //if length was even it will be now odd, and viceversa
        
        v = new Complex[length];
        //fill array with random values
        for (int i = 0; i < length; i++) {
            v[i] = new Complex(randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE), randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE));
        }        
        
        result = new Complex[length];
        ArrayUtils.reverse(v, result);
        
        //check correctness
        assertEquals(result.length, length);
        for (int i = 0; i < length; i++) {
            assertEquals(result[length - 1 - i], v[i]);
        }
        
        //copy and reverse
        result2 = new Complex[length];
        for (int i = 0; i < length; i++) {
            result2[i] = result[i].clone();
        }        
        ArrayUtils.reverse(v);
        
        //check correctness
        assertEquals(v.length, result2.length);
        for (int i = 0; i < length; i++) {
            assertEquals(v[i], result2[i]);
        }
    }
    
    @Test
    public void testReverseAndReturnNewComplex() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        
        Complex[] v = new Complex[length];
        //fill array with random values
        for (int i = 0; i < length; i++) {
            v[i] = new Complex(randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE), randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE));
        }        
        
        Complex[] result = ArrayUtils.reverseAndReturnNew(v);
        
        //check correctness
        assertEquals(result.length, length);
        for (int i = 0; i < length; i++) {
            assertEquals(result[length - 1 - i], v[i]);
        }
        
        
        //test for odd/even case
        length++; //if length was even it will be now odd, and viceversa
        
        v = new Complex[length];
        //fill array with random values
        for (int i = 0; i < length; i++) {
            v[i] = new Complex(randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE), randomizer.nextDouble(MIN_RANDOM_VALUE,
                    MAX_RANDOM_VALUE));
        }        
        
        result = ArrayUtils.reverseAndReturnNew(v);
        
        //check correctness
        assertEquals(result.length, length);
        for (int i = 0; i < length; i++) {
            assertEquals(result[length - 1 - i], v[i]);
        }
    }
    
    @Test
    public void testSqrt() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);

        double[] v = new double[length];
        double[] sqrt = new double[length];
        for (int i = 0; i < length; i++) {
            v[i] = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
            sqrt[i] = Math.sqrt(v[i]);
        }
        
        //check correctness
        double[] sqrt2 = new double[length];
        ArrayUtils.sqrt(v, sqrt2);
        
        assertArrayEquals(sqrt, sqrt2, ABSOLUTE_ERROR);
        
        sqrt2 = ArrayUtils.sqrtAndReturnNew(v);
        assertArrayEquals(sqrt, sqrt2, ABSOLUTE_ERROR);
        
        ArrayUtils.sqrt(v);
        assertArrayEquals(v, sqrt2, ABSOLUTE_ERROR);
    }
    
    @Test
    public void testMinMax() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        
        double[] v = new double[length];
        double minValue = Double.MAX_VALUE;
        double maxValue = -Double.MAX_VALUE;
        int minPos = -1;
        int maxPos = -1;
        for (int i = 0; i < length; i++) {
            v[i] = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
            if (v[i] < minValue) {
                minValue = v[i];
                minPos = i;
            }
            if (v[i] > maxValue) {
                maxValue = v[i];
                maxPos = i;
            }
        }
        
        int[] pos = new int[1];
        assertEquals(ArrayUtils.min(v, pos), minValue, 0.0);
        assertEquals(pos[0], minPos);
        
        assertEquals(ArrayUtils.min(v), minValue, 0.0);
        
        assertEquals(ArrayUtils.max(v, pos), maxValue, 0.0);
        assertEquals(pos[0], maxPos);
        
        assertEquals(ArrayUtils.max(v), maxValue, 0.0);
        
        double[] result = new double[2];
        pos = new int[2];
        ArrayUtils.minMax(v, result, pos);
        assertEquals(result[0], minValue, 0.0);
        assertEquals(result[1], maxValue, 0.0);
        assertEquals(pos[0], minPos);
        assertEquals(pos[1], maxPos);
    }
}
