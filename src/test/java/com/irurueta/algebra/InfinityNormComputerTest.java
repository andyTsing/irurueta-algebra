/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.algebra.InfinityNormComputer;
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 22, 2012
 */
package com.irurueta.algebra;

import com.irurueta.statistics.UniformRandomizer;
import java.util.Arrays;
import java.util.Random;
import org.junit.*;
import static org.junit.Assert.*;

public class InfinityNormComputerTest {
    
    public static final int MIN_LIMIT = 0;
    public static final int MAX_LIMIT = 50;
    public static final int MIN_ROWS = 1;
    public static final int MAX_ROWS = 50;
    public static final int MIN_COLUMNS = 1;
    public static final int MAX_COLUMNS = 50;
    public static final int MIN_LENGTH = 1;
    public static final int MAX_LENGTH = 100;
    public static final double MIN_RANDOM_VALUE = 0;
    public static final double MAX_RANDOM_VALUE = 100;
    public static final double RELATIVE_ERROR = 1.0;
    public static final double ABSOLUTE_ERROR = 1e-6;
    
    public InfinityNormComputerTest() {}

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
        InfinityNormComputer normComputer = new InfinityNormComputer();
        assertEquals(normComputer.getNormType(), NormType.INFINITY_NORM);
    }
    
    @Test
    public void testGetNormMatrix() throws WrongSizeException{
        InfinityNormComputer normComputer = new InfinityNormComputer();
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        double rowSum, maxRowSum = 0.0, norm;
        double initValue = randomizer.nextDouble(MIN_COLUMNS, MAX_COLUMNS);
        double value;
        
        //For random non-initialized matrix
        Matrix m = new Matrix(rows, columns);
        for(int i = 0; i < rows; i++){
            rowSum = 0.0;
            for(int j = 0; j < columns; j++){
                value = randomizer.nextInt(MIN_LIMIT, MAX_LIMIT);
                m.setElementAt(i, j, value);
                rowSum += Math.abs(value);
            }
            
            maxRowSum = (rowSum > maxRowSum) ? rowSum : maxRowSum;
        }
        
        assertEquals(normComputer.getNorm(m), maxRowSum, ABSOLUTE_ERROR);
        assertEquals(InfinityNormComputer.norm(m), maxRowSum, ABSOLUTE_ERROR);
        
        //For initialized matrix
        m = new Matrix(rows, columns);
        m.initialize(initValue);
        
        norm = initValue * columns;
        
        assertEquals(normComputer.getNorm(m), norm, ABSOLUTE_ERROR);
        assertEquals(InfinityNormComputer.norm(m), norm, ABSOLUTE_ERROR);
        
        //For identity matrix
        m = Matrix.identity(rows, columns);
        assertEquals(normComputer.getNorm(m), 1.0, ABSOLUTE_ERROR);
        assertEquals(InfinityNormComputer.norm(m), 1.0, ABSOLUTE_ERROR);
    }
    
    @Test
    public void testGetNormArray(){
        InfinityNormComputer normComputer = new InfinityNormComputer();
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        double norm;
        double initValue = randomizer.nextDouble(MIN_LIMIT, MAX_LIMIT);
        
        double[] v = new double[length];
        
        //randomly initialize vector
        for(int i = 0; i < length; i++){
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
    public void testNormWithJacobian() throws AlgebraException{
        InfinityNormComputer normComputer = new InfinityNormComputer();
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        double norm;
        
        double[] v = new double[length];
        
        //randomly initialize vector
        for(int i = 0; i < length; i++){
            v[i] = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        }
        
        norm = Math.abs(v[0]);
        
        Matrix jacobian = new Matrix(1, length);
        assertEquals(InfinityNormComputer.norm(v, jacobian), norm, 
                ABSOLUTE_ERROR);        
        assertEquals(jacobian, Matrix.newFromArray(v).
                multiplyByScalarAndReturnNew(1.0 / norm).
                transposeAndReturnNew());     
        
        //Force WrongSizeException
        try{
            InfinityNormComputer.norm(v, new Matrix(2,length));
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
