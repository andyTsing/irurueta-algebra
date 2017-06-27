/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.algebra.OneNormComputer
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 22, 2012
 */
package com.irurueta.algebra;

import com.irurueta.statistics.UniformRandomizer;
import java.util.Arrays;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.*;

public class OneNormComputerTest {
    
    public static final int MIN_LIMIT = 0;
    public static final int MAX_LIMIT = 50;
    public static final int MIN_ROWS = 1;
    public static final int MAX_ROWS = 50;
    public static final int MIN_COLUMNS = 1;
    public static final int MAX_COLUMNS = 50;
    public static final int MIN_LENGTH = 1;
    public static final int MAX_LENGTH = 100;
    public static final double ABSOLUTE_ERROR = 1e-6;
    
    public OneNormComputerTest() {}

    @BeforeClass
    public static void setUpClass() throws Exception {}

    @AfterClass
    public static void tearDownClass() throws Exception {}
    
    @Before
    public void setUp() {}
    
    @After
    public void tearDown() {}
    
    @Test
    public void testGetNormType(){
        OneNormComputer normComputer = new OneNormComputer();
        assertNotNull(normComputer);
        assertEquals(normComputer.getNormType(), NormType.ONE_NORM);
    }
    
    @Test
    public void testGetNormMatrix() throws WrongSizeException{
        OneNormComputer normComputer = new OneNormComputer();
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        double colSum, maxColSum = 0.0, norm;
        double value;
        
        Matrix m = new Matrix(rows, columns);
        for(int j = 0; j < columns; j++){
            colSum = 0.0;
            for(int i = 0; i < rows; i++){
                value = randomizer.nextDouble(MIN_LIMIT, MAX_LIMIT);
                m.setElementAt(i, j, value);
                colSum += Math.abs(value);
            }
            
            maxColSum = (colSum > maxColSum) ? colSum : maxColSum;
        }
        
        assertEquals(normComputer.getNorm(m), maxColSum, ABSOLUTE_ERROR);
        assertEquals(OneNormComputer.norm(m), maxColSum, ABSOLUTE_ERROR);
        
        //For initialized matrix
        double initValue = randomizer.nextDouble(MIN_LIMIT, MAX_LIMIT);
        m.initialize(initValue);
        
        norm = initValue * rows;
        assertEquals(normComputer.getNorm(m), norm, ABSOLUTE_ERROR);
        assertEquals(OneNormComputer.norm(m), norm, ABSOLUTE_ERROR);
        
        //For identity matrix
        m = Matrix.identity(rows, columns);
        assertEquals(normComputer.getNorm(m), 1.0, ABSOLUTE_ERROR);
        assertEquals(OneNormComputer.norm(m), 1.0, ABSOLUTE_ERROR);
    }
    
    @Test
    public void testGetNormArray(){
        OneNormComputer normComputer = new OneNormComputer();
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        double sum = 0.0, norm;
        double initValue = randomizer.nextDouble(MIN_LIMIT, MAX_LIMIT);
        double value;
        
        double[] v = new double[length];
        for(int i = 0; i < length; i++){
            value = randomizer.nextDouble(MIN_LIMIT, MAX_LIMIT);
            v[i] = value;
            sum += Math.abs(value);
        }
        
        norm = sum;
        assertEquals(normComputer.getNorm(v), norm, ABSOLUTE_ERROR);
        assertEquals(OneNormComputer.norm(v), norm, ABSOLUTE_ERROR);
        
        Arrays.fill(v, initValue);
        
        norm = initValue * length;
        
        assertEquals(normComputer.getNorm(v), norm, ABSOLUTE_ERROR);
        assertEquals(OneNormComputer.norm(v), norm, ABSOLUTE_ERROR);
    }
    
    @Test
    public void testNormWithJacobian() throws AlgebraException{
        OneNormComputer normComputer = new OneNormComputer();
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        double sum = 0.0, norm;
        double value;
        
        double[] v = new double[length];
        for(int i = 0; i < length; i++){
            value = randomizer.nextDouble(MIN_LIMIT, MAX_LIMIT);
            v[i] = value;
            sum += Math.abs(value);
        }
        
        norm = sum;

        Matrix jacobian = new Matrix(1, length);
        assertEquals(OneNormComputer.norm(v, jacobian), norm, 
                ABSOLUTE_ERROR);        
        assertEquals(jacobian, Matrix.newFromArray(v).
                multiplyByScalarAndReturnNew(1.0 / norm).
                transposeAndReturnNew());  
        
        //Force WrongSizeException
        try{
            OneNormComputer.norm(v, new Matrix(2, length));
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        
        jacobian = new Matrix(1, length);
        assertEquals(normComputer.getNorm(v, jacobian), norm, ABSOLUTE_ERROR);
        assertEquals(jacobian, Matrix.newFromArray(v).
                multiplyByScalarAndReturnNew(1.0 / norm).
                transposeAndReturnNew()); 
        
        //Force WrongSizeException
        try{
            normComputer.getNorm(v, new Matrix(2, length));
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}        
    }
}
