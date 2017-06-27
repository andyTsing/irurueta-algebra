/**
 * @file
 * This file contains implementation of
 * com.irurueta.algebra.Complex
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date May 13, 2012
 */
package com.irurueta.algebra;

import com.irurueta.statistics.UniformRandomizer;
import java.util.Random;
import org.junit.*;
import static org.junit.Assert.*;

public class ComplexTest {
    
    public static final double MIN_RANDOM_VALUE = -100.0;
    public static final double MAX_RANDOM_VALUE = 100.0;
    
    public static final double MIN_MODULUS = 1.0;
    public static final double MAX_MODULUS = 10.0;
    
    public static final double MIN_PHASE = -Math.PI;
    public static final double MAX_PHASE = Math.PI;
    
    public static final double MIN_EXPONENT = -2.0;
    public static final double MAX_EXPONENT = 2.0;
    
    public static final double ABSOLUTE_ERROR = 1e-9;
    
    public ComplexTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testConstructor(){
        Complex c;
        
        //Test 1st constructor
        c = new Complex();
        assertNotNull(c);
        assertEquals(c.getReal(), 0.0, 0.0);
        assertEquals(c.getImaginary(), 0.0, 0.0);
        assertEquals(c.getModulus(), 0.0, ABSOLUTE_ERROR);
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double real = randomizer.nextDouble(
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double imaginary = randomizer.nextDouble(
                MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        //Test 2nd constructor
        c = new Complex(real);
        assertNotNull(c);
        assertEquals(c.getReal(), real, 0.0);
        assertEquals(c.getImaginary(), 0.0, 0.0);
        assertEquals(c.getModulus(), Math.abs(real), ABSOLUTE_ERROR);
        
        //Test 3rd constructor
        c = new Complex(real, imaginary);
        assertNotNull(c);
        assertEquals(c.getReal(), real, 0.0);
        assertEquals(c.getImaginary(), imaginary, 0.0);
        assertEquals(c.getModulus(), 
                Math.sqrt(real * real + imaginary * imaginary), ABSOLUTE_ERROR);
        assertEquals(c.getPhase(), Math.atan2(imaginary, real), ABSOLUTE_ERROR);
        
        //Test 4th constructor
        Complex c2 = new Complex(c);
        assertEquals(c2.getReal(), c.getReal(), 0.0);
        assertEquals(c2.getImaginary(), c.getImaginary(), 0.0);
        assertEquals(c2.getReal(), real, 0.0);
        assertEquals(c2.getImaginary(), imaginary, 0.0);
        assertEquals(c2.getModulus(), c.getModulus(), 0.0);
        assertEquals(c2.getPhase(), c.getPhase(), 0.0);
    }
    
    @Test
    public void testGetSetReal(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double real = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        Complex c = new Complex();
        
        assertEquals(c.getReal(), 0.0, 0.0);
        
        //set new value
        c.setReal(real);
        //check correctness
        assertEquals(c.getReal(), real, 0.0);
    }
    
    @Test
    public void testGetSetImaginary(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double imaginary = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        Complex c = new Complex();
        
        assertEquals(c.getImaginary(), 0.0, 0.0);
        
        //set new value
        c.setImaginary(imaginary);
        //check correctness
        assertEquals(c.getImaginary(), imaginary, 0.0);
    }
    
    @Test
    public void testGetSetRealAndImaginary(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double real = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double imaginary = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        Complex c = new Complex();
        
        assertEquals(c.getReal(), 0.0, 0.0);
        assertEquals(c.getImaginary(), 0.0, 0.0);
        
        //set new value
        c.setRealAndImaginary(real, imaginary);
        //check correctness
        assertEquals(c.getReal(), real, 0.0);
        assertEquals(c.getImaginary(), imaginary, 0.0);
    }
    
    @Test
    public void testGetModulus(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double real = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double imaginary = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        double modulus = Math.sqrt(real * real + imaginary * imaginary);
        Complex c = new Complex(real, imaginary);
        
        assertEquals(c.getModulus(), modulus, ABSOLUTE_ERROR);
    }
    
    @Test
    public void testGetPhase(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double real = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double imaginary = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        double phase = Math.atan2(imaginary, real);
        Complex c = new Complex(real, imaginary);
        
        assertEquals(c.getPhase(), phase, ABSOLUTE_ERROR);
    }
    
    @Test
    public void testSetModulusAndPhase(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double modulus = randomizer.nextDouble(MIN_MODULUS, MAX_MODULUS);
        double phase = randomizer.nextDouble(MIN_PHASE, MAX_PHASE);
        
        double real = modulus * Math.cos(phase);
        double imaginary = modulus * Math.sin(phase);
        Complex c = new Complex();
        
        c.setModulusAndPhase(modulus, phase);
        assertEquals(c.getModulus(), modulus, ABSOLUTE_ERROR);
        assertEquals(c.getPhase(), phase, ABSOLUTE_ERROR);
        assertEquals(c.getReal(), real, ABSOLUTE_ERROR);
        assertEquals(c.getImaginary(), imaginary, ABSOLUTE_ERROR);
    }
    
    @Test
    public void testConjugate(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double modulus = randomizer.nextDouble(MIN_MODULUS, MAX_MODULUS);
        double phase = randomizer.nextDouble(MIN_PHASE, MAX_PHASE);
        
        double real = modulus * Math.cos(phase);
        double imaginary = modulus * Math.sin(phase);

        Complex c = new Complex(real, imaginary);
        assertEquals(c.getReal(), real, 0.0);
        assertEquals(c.getImaginary(), imaginary, 0.0);
        
        Complex result = new Complex();
        assertEquals(result.getReal(), 0.0, 0.0);
        assertEquals(result.getImaginary(), 0.0, 0.0);
        
        //Test conjugate and store in result
        c.conjugate(result);
        assertEquals(result.getReal(), real, 0.0);
        assertEquals(result.getImaginary(), -imaginary, 0.0);
        assertEquals(result.getModulus(), modulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), -phase, ABSOLUTE_ERROR);
        
        //Test conjugate and return new
        result = c.conjugateAndReturnNew();
        assertEquals(result.getReal(), real, 0.0);
        assertEquals(result.getImaginary(), -imaginary, 0.0);
        assertEquals(result.getModulus(), modulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), -phase, ABSOLUTE_ERROR);
        
        
        //Test conjugate itself
        c.conjugate();
        assertEquals(c.getReal(), real, 0.0);
        assertEquals(c.getImaginary(), -imaginary, 0.0);
        assertEquals(c.getModulus(), modulus, ABSOLUTE_ERROR);
        assertEquals(c.getPhase(), -phase, ABSOLUTE_ERROR);        
    }
    
    @Test
    public void testAdd(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double real1 = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        double imaginary1 = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        double real2 = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        double imaginary2 = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        
        Complex c1 = new Complex(real1, imaginary1);
        Complex c2 = new Complex(real2, imaginary2);
        Complex result = new Complex();
        
        assertEquals(c1.getReal(), real1, 0.0);
        assertEquals(c1.getImaginary(), imaginary1, 0.0);
        assertEquals(c2.getReal(), real2, 0.0);
        assertEquals(c2.getImaginary(), imaginary2, 0.0);
        assertEquals(result.getReal(), 0.0, 0.0);
        assertEquals(result.getImaginary(), 0.0, 0.0);
        
        //Add and store in result
        c1.add(c2, result);
        //check correctness
        assertEquals(result.getReal(), real1 + real2, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), imaginary1 + imaginary2, 
                ABSOLUTE_ERROR);
        
        //Add and return result
        result = c1.addAndReturnNew(c2);
        //check correctness
        assertEquals(result.getReal(), real1 + real2, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), imaginary1 + imaginary2, 
                ABSOLUTE_ERROR);

        //Add and store result on same instance
        c1.add(c2);
        //check correctness
        assertEquals(c1.getReal(), real1 + real2, ABSOLUTE_ERROR);
        assertEquals(c1.getImaginary(), imaginary1 + imaginary2, 
                ABSOLUTE_ERROR);        
    }
    
    @Test
    public void testSubtract(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double real1 = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        double imaginary1 = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        double real2 = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        double imaginary2 = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        
        Complex c1 = new Complex(real1, imaginary1);
        Complex c2 = new Complex(real2, imaginary2);
        Complex result = new Complex();
        
        assertEquals(c1.getReal(), real1, 0.0);
        assertEquals(c1.getImaginary(), imaginary1, 0.0);
        assertEquals(c2.getReal(), real2, 0.0);
        assertEquals(c2.getImaginary(), imaginary2, 0.0);
        assertEquals(result.getReal(), 0.0, 0.0);
        assertEquals(result.getImaginary(), 0.0, 0.0);
        
        //Subtract and store in result
        c1.subtract(c2, result);
        //check correctness
        assertEquals(result.getReal(), real1 - real2, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), imaginary1 - imaginary2, 
                ABSOLUTE_ERROR);
        
        //Subtract and return result
        result = c1.subtractAndReturnNew(c2);
        //check correctness
        assertEquals(result.getReal(), real1 - real2, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), imaginary1 - imaginary2, 
                ABSOLUTE_ERROR);

        //Subtract and store result on same instance
        c1.subtract(c2);
        //check correctness
        assertEquals(c1.getReal(), real1 - real2, ABSOLUTE_ERROR);
        assertEquals(c1.getImaginary(), imaginary1 - imaginary2, 
                ABSOLUTE_ERROR);                
    }
    
    @Test
    public void testMultiply(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double modulus1 = randomizer.nextDouble(MIN_MODULUS, MAX_MODULUS);
        double phase1 = randomizer.nextDouble(MIN_PHASE, MAX_PHASE);
        double modulus2 = randomizer.nextDouble(MIN_MODULUS, MAX_MODULUS);
        double phase2 = randomizer.nextDouble(MIN_PHASE, MAX_PHASE);
        
        double real1 = modulus1 * Math.cos(phase1);
        double imaginary1 = modulus1 * Math.sin(phase1);
        double real2 = modulus2 * Math.cos(phase2);
        double imaginary2 = modulus2 * Math.sin(phase2);

        Complex c1 = new Complex(real1, imaginary1);
        Complex c2 = new Complex(real2, imaginary2);
        Complex result = new Complex();
        
        assertEquals(c1.getReal(), real1, 0.0);
        assertEquals(c1.getImaginary(), imaginary1, 0.0);
        assertEquals(c2.getReal(), real2, 0.0);
        assertEquals(c2.getImaginary(), imaginary2, 0.0);
        assertEquals(result.getReal(), 0.0, 0.0);
        assertEquals(result.getImaginary(), 0.0, 0.0);
        
        //multiply and store in result
        c1.multiply(c2, result);
        //check correctness
        double resultModulus = modulus1 * modulus2;
        double resultPhase = phase1 + phase2;
        double resultReal = resultModulus * Math.cos(resultPhase);
        double resultImaginary = resultModulus * Math.sin(resultPhase);
        resultPhase = Math.atan2(resultImaginary, resultReal);
        assertEquals(result.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(result.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), resultImaginary, ABSOLUTE_ERROR);

        
        //multiply and return result
        result = c1.multiplyAndReturnNew(c2);
        //check correctness
        assertEquals(result.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(result.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), resultImaginary, ABSOLUTE_ERROR);

        //multiply and store result on same instance
        c1.multiply(c2);
        //check correctness
        assertEquals(c1.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(c1.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(c1.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(c1.getImaginary(), resultImaginary, ABSOLUTE_ERROR);        
    }
    
    @Test
    public void testDivide(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double modulus1 = randomizer.nextDouble(MIN_MODULUS, MAX_MODULUS);
        double phase1 = randomizer.nextDouble(MIN_PHASE, MAX_PHASE);
        double modulus2 = randomizer.nextDouble(MIN_MODULUS, MAX_MODULUS);
        double phase2 = randomizer.nextDouble(MIN_PHASE, MAX_PHASE);
        
        double real1 = modulus1 * Math.cos(phase1);
        double imaginary1 = modulus1 * Math.sin(phase1);
        double real2 = modulus2 * Math.cos(phase2);
        double imaginary2 = modulus2 * Math.sin(phase2);

        Complex c1 = new Complex(real1, imaginary1);
        Complex c2 = new Complex(real2, imaginary2);
        Complex result = new Complex();
        
        assertEquals(c1.getReal(), real1, 0.0);
        assertEquals(c1.getImaginary(), imaginary1, 0.0);
        assertEquals(c2.getReal(), real2, 0.0);
        assertEquals(c2.getImaginary(), imaginary2, 0.0);
        assertEquals(result.getReal(), 0.0, 0.0);
        assertEquals(result.getImaginary(), 0.0, 0.0);
        
        //divide and store in result
        c1.divide(c2, result);
        //check correctness
        double resultModulus = modulus1 / modulus2;
        double resultPhase = phase1 - phase2;
        double resultReal = resultModulus * Math.cos(resultPhase);
        double resultImaginary = resultModulus * Math.sin(resultPhase);
        resultPhase = Math.atan2(resultImaginary, resultReal);
        assertEquals(result.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(result.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), resultImaginary, ABSOLUTE_ERROR);

        
        //divide and return result
        result = c1.divideAndReturnNew(c2);
        //check correctness
        assertEquals(result.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(result.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), resultImaginary, ABSOLUTE_ERROR);

        //divide and store result on same instance
        c1.divide(c2);
        //check correctness
        assertEquals(c1.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(c1.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(c1.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(c1.getImaginary(), resultImaginary, ABSOLUTE_ERROR);        
    }    
    
    @Test
    public void testMultiplyByScalar(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double real = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        double imaginary = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        double scalar = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        
        Complex c = new Complex(real, imaginary);
        Complex result = new Complex();
        
        assertEquals(c.getReal(), real, 0.0);
        assertEquals(c.getImaginary(), imaginary, 0.0);
        assertEquals(result.getReal(), 0.0, 0.0);
        assertEquals(result.getImaginary(), 0.0, 0.0);
        
        //multiply by scalar and store in result
        c.multiplyByScalar(scalar, result);
        //check correctness
        assertEquals(result.getReal(), scalar * real, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), scalar * imaginary, ABSOLUTE_ERROR);
        
        //multiply by scalar and return result
        result = c.multiplyByScalarAndReturnNew(scalar);
        //check correctness
        assertEquals(result.getReal(), scalar * real, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), scalar * imaginary, ABSOLUTE_ERROR);

        //multiply by scalar and store result on same instance
        c.multiplyByScalar(scalar);
        //check correctness
        assertEquals(c.getReal(), scalar * real, ABSOLUTE_ERROR);
        assertEquals(c.getImaginary(), scalar * imaginary, ABSOLUTE_ERROR);
    }
    
    @Test
    public void testPow(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double modulus = randomizer.nextDouble(MIN_MODULUS, MAX_MODULUS);
        double phase = randomizer.nextDouble(MIN_PHASE, MAX_PHASE);
        double exponent = randomizer.nextDouble(MIN_EXPONENT, MAX_EXPONENT);
        
        double real = modulus * Math.cos(phase);
        double imaginary = modulus * Math.sin(phase);

        Complex c = new Complex(real, imaginary);
        Complex result = new Complex();
        
        assertEquals(c.getReal(), real, 0.0);
        assertEquals(c.getImaginary(), imaginary, 0.0);
        assertEquals(result.getReal(), 0.0, 0.0);
        assertEquals(result.getImaginary(), 0.0, 0.0);
        
        //compute power and store in result
        c.pow(exponent, result);
        //check correctness
        double resultModulus = Math.pow(modulus, exponent);
        double resultPhase = exponent * phase;
        double resultReal = resultModulus * Math.cos(resultPhase);
        double resultImaginary = resultModulus * Math.sin(resultPhase);
        resultPhase = Math.atan2(resultImaginary, resultReal);
        assertEquals(result.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(result.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), resultImaginary, ABSOLUTE_ERROR);

        
        //multiply and return result
        result = c.powAndReturnNew(exponent);
        //check correctness
        assertEquals(result.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(result.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), resultImaginary, ABSOLUTE_ERROR);

        //multiply and store result on same instance
        c.pow(exponent);
        //check correctness
        assertEquals(c.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(c.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(c.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(c.getImaginary(), resultImaginary, ABSOLUTE_ERROR);                
    }
    
    @Test
    public void testSqrt(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double modulus = randomizer.nextDouble(MIN_MODULUS, MAX_MODULUS);
        double phase = randomizer.nextDouble(MIN_PHASE, MAX_PHASE);
        
        double real = modulus * Math.cos(phase);
        double imaginary = modulus * Math.sin(phase);

        Complex c = new Complex(real, imaginary);
        Complex result = new Complex();
        
        assertEquals(c.getReal(), real, 0.0);
        assertEquals(c.getImaginary(), imaginary, 0.0);
        assertEquals(result.getReal(), 0.0, 0.0);
        assertEquals(result.getImaginary(), 0.0, 0.0);
        
        //compute power and store in result
        c.sqrt(result);
        //check correctness
        double resultModulus = Math.sqrt(modulus);
        double resultPhase = 0.5 * phase;
        double resultReal = resultModulus * Math.cos(resultPhase);
        double resultImaginary = resultModulus * Math.sin(resultPhase);
        resultPhase = Math.atan2(resultImaginary, resultReal);
        assertEquals(result.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(result.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), resultImaginary, ABSOLUTE_ERROR);

        
        //multiply and return result
        result = c.sqrtAndReturnNew();
        //check correctness
        assertEquals(result.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(result.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(result.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(result.getImaginary(), resultImaginary, ABSOLUTE_ERROR);

        //multiply and store result on same instance
        c.sqrt();
        //check correctness
        assertEquals(c.getModulus(), resultModulus, ABSOLUTE_ERROR);
        assertEquals(c.getPhase(), resultPhase, ABSOLUTE_ERROR);
        assertEquals(c.getReal(), resultReal, ABSOLUTE_ERROR);
        assertEquals(c.getImaginary(), resultImaginary, ABSOLUTE_ERROR);                        
    }
    
    @Test
    public void testEquals(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double real1 = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        double real2 = 1.0 + real1;
        double imaginary1 = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        double imaginary2 = 1.0 + imaginary1;
        
        Complex c1 = new Complex(real1, imaginary1);
        Complex c2 = new Complex(real1, imaginary1);
        Complex c3 = new Complex(real2, imaginary2);
        
        assertTrue(c1.equals(c1));
        assertTrue(c1.equals(c2));
        assertFalse(c1.equals(c3));
        assertFalse(c1.equals(new Object()));
    }
    
    @Test
    public void testClone(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double real = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double imaginary = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        Complex c1 = new Complex(real, imaginary);
        Complex c2 = c1.clone();
        
        assertEquals(c1, c2);
    }
    
    @Test
    public void testCopyFrom(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double real = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double imaginary = randomizer.nextDouble(MIN_RANDOM_VALUE,
                MAX_RANDOM_VALUE);
        Complex c1 = new Complex(real, imaginary);
        Complex c2 = new Complex();
        
        assertEquals(c1.getReal(), real, 0.0);
        assertEquals(c1.getImaginary(), imaginary, 0.0);
        
        assertEquals(c2.getReal(), 0.0, 0.0);
        assertEquals(c2.getImaginary(), 0.0, 0.0);
        
        //copy c1 into c2
        c2.copyFrom(c1);
        
        //check correctness
        assertEquals(c2.getReal(), real, 0.0);
        assertEquals(c2.getImaginary(), imaginary, 0.0);
    }
}
