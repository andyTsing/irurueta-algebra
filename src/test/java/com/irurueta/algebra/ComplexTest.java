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

import com.irurueta.SerializationHelper;
import com.irurueta.statistics.UniformRandomizer;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.*;

public class ComplexTest {

    private static final double MIN_RANDOM_VALUE = -100.0;
    private static final double MAX_RANDOM_VALUE = 100.0;

    private static final double MIN_MODULUS = 1.0;
    private static final double MAX_MODULUS = 10.0;

    private static final double MIN_PHASE = -Math.PI;
    private static final double MAX_PHASE = Math.PI;

    private static final double MIN_EXPONENT = -2.0;
    private static final double MAX_EXPONENT = 2.0;

    private static final double ABSOLUTE_ERROR = 1e-9;

    @Test
    public void testConstructor() {
        Complex c;

        // Test 1st constructor
        c = new Complex();
        assertNotNull(c);
        assertEquals(c.getReal(), 0.0, 0.0);
        assertEquals(c.getImaginary(), 0.0, 0.0);
        assertEquals(c.getModulus(), 0.0, ABSOLUTE_ERROR);

        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double real = randomizer.nextDouble(
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final double imaginary = randomizer.nextDouble(
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        // Test 2nd constructor
        c = new Complex(real);
        assertNotNull(c);
        assertEquals(c.getReal(), real, 0.0);
        assertEquals(c.getImaginary(), 0.0, 0.0);
        assertEquals(c.getModulus(), Math.abs(real), ABSOLUTE_ERROR);

        // Test 3rd constructor
        c = new Complex(real, imaginary);
        assertNotNull(c);
        assertEquals(c.getReal(), real, 0.0);
        assertEquals(c.getImaginary(), imaginary, 0.0);
        assertEquals(c.getModulus(),
                Math.sqrt(real * real + imaginary * imaginary), ABSOLUTE_ERROR);
        assertEquals(c.getPhase(), Math.atan2(imaginary, real), ABSOLUTE_ERROR);

        // Test 4th constructor
        Complex c2 = new Complex(c);
        assertEquals(c2.getReal(), c.getReal(), 0.0);
        assertEquals(c2.getImaginary(), c.getImaginary(), 0.0);
        assertEquals(c2.getReal(), real, 0.0);
        assertEquals(c2.getImaginary(), imaginary, 0.0);
        assertEquals(c2.getModulus(), c.getModulus(), 0.0);
        assertEquals(c2.getPhase(), c.getPhase(), 0.0);
    }

    @Test
    public void testGetSetReal() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double real = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final Complex c = new Complex();

        assertEquals(c.getReal(), 0.0, 0.0);

        // set new value
        c.setReal(real);
        // check correctness
        assertEquals(c.getReal(), real, 0.0);
    }

    @Test
    public void testGetSetImaginary() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double imaginary = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        final Complex c = new Complex();

        assertEquals(c.getImaginary(), 0.0, 0.0);

        // set new value
        c.setImaginary(imaginary);
        // check correctness
        assertEquals(c.getImaginary(), imaginary, 0.0);
    }

    @Test
    public void testGetSetRealAndImaginary() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double real = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final double imaginary = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        final Complex c = new Complex();

        assertEquals(c.getReal(), 0.0, 0.0);
        assertEquals(c.getImaginary(), 0.0, 0.0);

        // set new value
        c.setRealAndImaginary(real, imaginary);
        // check correctness
        assertEquals(c.getReal(), real, 0.0);
        assertEquals(c.getImaginary(), imaginary, 0.0);
    }

    @Test
    public void testGetModulus() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double real = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final double imaginary = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        final double modulus = Math.sqrt(real * real + imaginary * imaginary);
        final Complex c = new Complex(real, imaginary);

        assertEquals(c.getModulus(), modulus, ABSOLUTE_ERROR);
    }

    @Test
    public void testGetPhase() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double real = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final double imaginary = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        final double phase = Math.atan2(imaginary, real);
        final Complex c = new Complex(real, imaginary);

        assertEquals(c.getPhase(), phase, ABSOLUTE_ERROR);
    }

    @Test
    public void testSetModulusAndPhase() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double modulus = randomizer.nextDouble(MIN_MODULUS, MAX_MODULUS);
        final double phase = randomizer.nextDouble(MIN_PHASE, MAX_PHASE);

        final double real = modulus * Math.cos(phase);
        final double imaginary = modulus * Math.sin(phase);
        final Complex c = new Complex();

        c.setModulusAndPhase(modulus, phase);
        assertEquals(c.getModulus(), modulus, ABSOLUTE_ERROR);
        assertEquals(c.getPhase(), phase, ABSOLUTE_ERROR);
        assertEquals(c.getReal(), real, ABSOLUTE_ERROR);
        assertEquals(c.getImaginary(), imaginary, ABSOLUTE_ERROR);
    }

    @Test
    public void testConjugate() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double modulus = randomizer.nextDouble(MIN_MODULUS, MAX_MODULUS);
        final double phase = randomizer.nextDouble(MIN_PHASE, MAX_PHASE);

        final double real = modulus * Math.cos(phase);
        final double imaginary = modulus * Math.sin(phase);

        final Complex c = new Complex(real, imaginary);
        assertEquals(c.getReal(), real, 0.0);
        assertEquals(c.getImaginary(), imaginary, 0.0);

        Complex result = new Complex();
        assertEquals(result.getReal(), 0.0, 0.0);
        assertEquals(result.getImaginary(), 0.0, 0.0);

        // Test conjugate and store in result
        c.conjugate(result);
        assertEquals(result.getReal(), real, 0.0);
        assertEquals(result.getImaginary(), -imaginary, 0.0);
        assertEquals(result.getModulus(), modulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), -phase, ABSOLUTE_ERROR);

        // Test conjugate and return new
        result = c.conjugateAndReturnNew();
        assertEquals(result.getReal(), real, 0.0);
        assertEquals(result.getImaginary(), -imaginary, 0.0);
        assertEquals(result.getModulus(), modulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), -phase, ABSOLUTE_ERROR);


        // Test conjugate itself
        c.conjugate();
        assertEquals(c.getReal(), real, 0.0);
        assertEquals(c.getImaginary(), -imaginary, 0.0);
        assertEquals(c.getModulus(), modulus, ABSOLUTE_ERROR);
        assertEquals(c.getPhase(), -phase, ABSOLUTE_ERROR);
    }

    @Test
    public void testAdd() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double real1 = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        final double imaginary1 = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        final double real2 = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        final double imaginary2 = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);

        final Complex c1 = new Complex(real1, imaginary1);
        final Complex c2 = new Complex(real2, imaginary2);
        Complex result = new Complex();

        assertEquals(c1.getReal(), real1, 0.0);
        assertEquals(c1.getImaginary(), imaginary1, 0.0);
        assertEquals(c2.getReal(), real2, 0.0);
        assertEquals(c2.getImaginary(), imaginary2, 0.0);
        assertEquals(result.getReal(), 0.0, 0.0);
        assertEquals(result.getImaginary(), 0.0, 0.0);

        // Add and store in result
        c1.add(c2, result);
        // check correctness
        assertEquals(result.getReal(), real1 + real2, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), imaginary1 + imaginary2,
                ABSOLUTE_ERROR);

        // Add and return result
        result = c1.addAndReturnNew(c2);
        // check correctness
        assertEquals(result.getReal(), real1 + real2, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), imaginary1 + imaginary2,
                ABSOLUTE_ERROR);

        // Add and store result on same instance
        c1.add(c2);
        // check correctness
        assertEquals(c1.getReal(), real1 + real2, ABSOLUTE_ERROR);
        assertEquals(c1.getImaginary(), imaginary1 + imaginary2,
                ABSOLUTE_ERROR);
    }

    @Test
    public void testSubtract() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double real1 = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        final double imaginary1 = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        final double real2 = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        final double imaginary2 = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);

        final Complex c1 = new Complex(real1, imaginary1);
        final Complex c2 = new Complex(real2, imaginary2);
        Complex result = new Complex();

        assertEquals(c1.getReal(), real1, 0.0);
        assertEquals(c1.getImaginary(), imaginary1, 0.0);
        assertEquals(c2.getReal(), real2, 0.0);
        assertEquals(c2.getImaginary(), imaginary2, 0.0);
        assertEquals(result.getReal(), 0.0, 0.0);
        assertEquals(result.getImaginary(), 0.0, 0.0);

        // Subtract and store in result
        c1.subtract(c2, result);
        // check correctness
        assertEquals(result.getReal(), real1 - real2, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), imaginary1 - imaginary2,
                ABSOLUTE_ERROR);

        // Subtract and return result
        result = c1.subtractAndReturnNew(c2);
        // check correctness
        assertEquals(result.getReal(), real1 - real2, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), imaginary1 - imaginary2,
                ABSOLUTE_ERROR);

        // Subtract and store result on same instance
        c1.subtract(c2);
        // check correctness
        assertEquals(c1.getReal(), real1 - real2, ABSOLUTE_ERROR);
        assertEquals(c1.getImaginary(), imaginary1 - imaginary2,
                ABSOLUTE_ERROR);
    }

    @Test
    public void testMultiply() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double modulus1 = randomizer.nextDouble(MIN_MODULUS, MAX_MODULUS);
        final double phase1 = randomizer.nextDouble(MIN_PHASE, MAX_PHASE);
        final double modulus2 = randomizer.nextDouble(MIN_MODULUS, MAX_MODULUS);
        final double phase2 = randomizer.nextDouble(MIN_PHASE, MAX_PHASE);

        final double real1 = modulus1 * Math.cos(phase1);
        final double imaginary1 = modulus1 * Math.sin(phase1);
        final double real2 = modulus2 * Math.cos(phase2);
        final double imaginary2 = modulus2 * Math.sin(phase2);

        final Complex c1 = new Complex(real1, imaginary1);
        final Complex c2 = new Complex(real2, imaginary2);
        Complex result = new Complex();

        assertEquals(c1.getReal(), real1, 0.0);
        assertEquals(c1.getImaginary(), imaginary1, 0.0);
        assertEquals(c2.getReal(), real2, 0.0);
        assertEquals(c2.getImaginary(), imaginary2, 0.0);
        assertEquals(result.getReal(), 0.0, 0.0);
        assertEquals(result.getImaginary(), 0.0, 0.0);

        // multiply and store in result
        c1.multiply(c2, result);
        // check correctness
        final double resultModulus = modulus1 * modulus2;
        double resultPhase = phase1 + phase2;
        final double resultReal = resultModulus * Math.cos(resultPhase);
        final double resultImaginary = resultModulus * Math.sin(resultPhase);
        resultPhase = Math.atan2(resultImaginary, resultReal);
        assertEquals(result.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(result.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), resultImaginary, ABSOLUTE_ERROR);


        // multiply and return result
        result = c1.multiplyAndReturnNew(c2);
        // check correctness
        assertEquals(result.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(result.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), resultImaginary, ABSOLUTE_ERROR);

        // multiply and store result on same instance
        c1.multiply(c2);
        // check correctness
        assertEquals(c1.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(c1.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(c1.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(c1.getImaginary(), resultImaginary, ABSOLUTE_ERROR);
    }

    @Test
    public void testDivide() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double modulus1 = randomizer.nextDouble(MIN_MODULUS, MAX_MODULUS);
        final double phase1 = randomizer.nextDouble(MIN_PHASE, MAX_PHASE);
        final double modulus2 = randomizer.nextDouble(MIN_MODULUS, MAX_MODULUS);
        final double phase2 = randomizer.nextDouble(MIN_PHASE, MAX_PHASE);

        final double real1 = modulus1 * Math.cos(phase1);
        final double imaginary1 = modulus1 * Math.sin(phase1);
        final double real2 = modulus2 * Math.cos(phase2);
        final double imaginary2 = modulus2 * Math.sin(phase2);

        final Complex c1 = new Complex(real1, imaginary1);
        final Complex c2 = new Complex(real2, imaginary2);
        Complex result = new Complex();

        assertEquals(c1.getReal(), real1, 0.0);
        assertEquals(c1.getImaginary(), imaginary1, 0.0);
        assertEquals(c2.getReal(), real2, 0.0);
        assertEquals(c2.getImaginary(), imaginary2, 0.0);
        assertEquals(result.getReal(), 0.0, 0.0);
        assertEquals(result.getImaginary(), 0.0, 0.0);

        // divide and store in result
        c1.divide(c2, result);
        // check correctness
        final double resultModulus = modulus1 / modulus2;
        double resultPhase = phase1 - phase2;
        final double resultReal = resultModulus * Math.cos(resultPhase);
        final double resultImaginary = resultModulus * Math.sin(resultPhase);
        resultPhase = Math.atan2(resultImaginary, resultReal);
        assertEquals(result.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(result.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), resultImaginary, ABSOLUTE_ERROR);


        // divide and return result
        result = c1.divideAndReturnNew(c2);
        // check correctness
        assertEquals(result.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(result.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), resultImaginary, ABSOLUTE_ERROR);

        // divide and store result on same instance
        c1.divide(c2);
        // check correctness
        assertEquals(c1.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(c1.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(c1.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(c1.getImaginary(), resultImaginary, ABSOLUTE_ERROR);
    }

    @Test
    public void testMultiplyByScalar() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double real = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        final double imaginary = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        final double scalar = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);

        final Complex c = new Complex(real, imaginary);
        Complex result = new Complex();

        assertEquals(c.getReal(), real, 0.0);
        assertEquals(c.getImaginary(), imaginary, 0.0);
        assertEquals(result.getReal(), 0.0, 0.0);
        assertEquals(result.getImaginary(), 0.0, 0.0);

        // multiply by scalar and store in result
        c.multiplyByScalar(scalar, result);
        // check correctness
        assertEquals(result.getReal(), scalar * real, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), scalar * imaginary, ABSOLUTE_ERROR);

        // multiply by scalar and return result
        result = c.multiplyByScalarAndReturnNew(scalar);
        // check correctness
        assertEquals(result.getReal(), scalar * real, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), scalar * imaginary, ABSOLUTE_ERROR);

        // multiply by scalar and store result on same instance
        c.multiplyByScalar(scalar);
        // check correctness
        assertEquals(c.getReal(), scalar * real, ABSOLUTE_ERROR);
        assertEquals(c.getImaginary(), scalar * imaginary, ABSOLUTE_ERROR);
    }

    @Test
    public void testPow() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double modulus = randomizer.nextDouble(MIN_MODULUS, MAX_MODULUS);
        final double phase = randomizer.nextDouble(MIN_PHASE, MAX_PHASE);
        final double exponent = randomizer.nextDouble(MIN_EXPONENT, MAX_EXPONENT);

        final double real = modulus * Math.cos(phase);
        final double imaginary = modulus * Math.sin(phase);

        final Complex c = new Complex(real, imaginary);
        Complex result = new Complex();

        assertEquals(c.getReal(), real, 0.0);
        assertEquals(c.getImaginary(), imaginary, 0.0);
        assertEquals(result.getReal(), 0.0, 0.0);
        assertEquals(result.getImaginary(), 0.0, 0.0);

        // compute power and store in result
        c.pow(exponent, result);
        // check correctness
        final double resultModulus = Math.pow(modulus, exponent);
        double resultPhase = exponent * phase;
        final double resultReal = resultModulus * Math.cos(resultPhase);
        final double resultImaginary = resultModulus * Math.sin(resultPhase);
        resultPhase = Math.atan2(resultImaginary, resultReal);
        assertEquals(result.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(result.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), resultImaginary, ABSOLUTE_ERROR);


        // multiply and return result
        result = c.powAndReturnNew(exponent);
        // check correctness
        assertEquals(result.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(result.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), resultImaginary, ABSOLUTE_ERROR);

        // multiply and store result on same instance
        c.pow(exponent);
        // check correctness
        assertEquals(c.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(c.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(c.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(c.getImaginary(), resultImaginary, ABSOLUTE_ERROR);
    }

    @Test
    public void testSqrt() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double modulus = randomizer.nextDouble(MIN_MODULUS, MAX_MODULUS);
        final double phase = randomizer.nextDouble(MIN_PHASE, MAX_PHASE);

        final double real = modulus * Math.cos(phase);
        final double imaginary = modulus * Math.sin(phase);

        final Complex c = new Complex(real, imaginary);
        Complex result = new Complex();

        assertEquals(c.getReal(), real, 0.0);
        assertEquals(c.getImaginary(), imaginary, 0.0);
        assertEquals(result.getReal(), 0.0, 0.0);
        assertEquals(result.getImaginary(), 0.0, 0.0);

        // compute power and store in result
        c.sqrt(result);
        // check correctness
        final double resultModulus = Math.sqrt(modulus);
        double resultPhase = 0.5 * phase;
        final double resultReal = resultModulus * Math.cos(resultPhase);
        final double resultImaginary = resultModulus * Math.sin(resultPhase);
        resultPhase = Math.atan2(resultImaginary, resultReal);
        assertEquals(result.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(result.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), resultImaginary, ABSOLUTE_ERROR);


        // multiply and return result
        result = c.sqrtAndReturnNew();
        // check correctness
        assertEquals(result.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(result.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), resultImaginary, ABSOLUTE_ERROR);

        // multiply and store result on same instance
        c.sqrt();
        // check correctness
        assertEquals(c.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(c.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(c.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(c.getImaginary(), resultImaginary, ABSOLUTE_ERROR);
    }

    @Test
    public void testEqualsAndHashCode() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double real1 = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        final double real2 = 1.0 + real1;
        final double imaginary1 = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        final double imaginary2 = 1.0 + imaginary1;

        final Complex c1 = new Complex(real1, imaginary1);
        final Complex c2 = new Complex(real1, imaginary1);
        final Complex c3 = new Complex(real2, imaginary2);

        assertEquals(c1, c1);
        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c1, new Object());

        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    public void testClone() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double real = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final double imaginary = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        final Complex c1 = new Complex(real, imaginary);
        final Complex c2 = new Complex(c1);

        assertEquals(c1, c2);
    }

    @Test
    public void testCopyFrom() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double real = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final double imaginary = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        final Complex c1 = new Complex(real, imaginary);
        final Complex c2 = new Complex();

        assertEquals(c1.getReal(), real, 0.0);
        assertEquals(c1.getImaginary(), imaginary, 0.0);

        assertEquals(c2.getReal(), 0.0, 0.0);
        assertEquals(c2.getImaginary(), 0.0, 0.0);

        // copy c1 into c2
        c2.copyFrom(c1);

        // check correctness
        assertEquals(c2.getReal(), real, 0.0);
        assertEquals(c2.getImaginary(), imaginary, 0.0);
    }

    @Test
    public void testSerializeDeserialize() throws IOException, ClassNotFoundException {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        final double real = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final double imaginary = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        final Complex c1 = new Complex(real, imaginary);

        final byte[] bytes = SerializationHelper.serialize(c1);
        final Complex c2 = SerializationHelper.deserialize(bytes);

        assertEquals(c1, c2);
        assertNotSame(c1, c2);
    }
}
