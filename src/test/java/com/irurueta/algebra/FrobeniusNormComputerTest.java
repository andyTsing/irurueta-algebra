/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.algebra.FrobeniusNormComputer
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

public class FrobeniusNormComputerTest {
    
    public static final int MIN_LIMIT = 0;
    public static final int MAX_LIMIT = 50;
    public static final int MIN_ROWS = 1;
    public static final int MAX_ROWS = 50;
    public static final int MIN_COLUMNS = 1;
    public static final int MAX_COLUMNS = 50;
    public static final int MIN_LENGTH = 1;
    public static final int MAX_LENGTH = 100;
    public static final double RELATIVE_ERROR = 1.0;
    public static final double ABSOLUTE_ERROR = 1e-6;
    
    public FrobeniusNormComputerTest() {}

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
        FrobeniusNormComputer normComputer = new FrobeniusNormComputer();
        assertEquals(normComputer.getNormType(), NormType.FROBENIUS_NORM);
    }
    
    @Test
    public void testGetNormMatrix() throws WrongSizeException{
        FrobeniusNormComputer normComputer = new FrobeniusNormComputer();
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        int minSize = (rows < columns) ? rows : columns;
        double sum = 0.0, norm;
        double initValue = randomizer.nextDouble(MIN_COLUMNS, MAX_COLUMNS);
        double value;
        
        //For random non-initialized matrix
        Matrix m = new Matrix(rows, columns);
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                value = randomizer.nextDouble(MIN_LIMIT, MAX_LIMIT);
                m.setElementAt(i, j, value);
                sum += value * value;
            }
        }      
        
        norm = Math.sqrt(sum);
        
        assertEquals(normComputer.getNorm(m), norm, ABSOLUTE_ERROR);
        assertEquals(FrobeniusNormComputer.norm(m), norm, ABSOLUTE_ERROR);
        
        //For initialized matrix
        m = new Matrix(rows, columns);
        m.initialize(initValue);
        
        norm = initValue * Math.sqrt(rows * columns);
        
        assertEquals(normComputer.getNorm(m), norm, ABSOLUTE_ERROR);
        assertEquals(FrobeniusNormComputer.norm(m), norm, ABSOLUTE_ERROR);
        
        //For identity matrix
        m = Matrix.identity(rows, columns);
        assertEquals(normComputer.getNorm(m), Math.sqrt(minSize), 
                ABSOLUTE_ERROR);
        assertEquals(FrobeniusNormComputer.norm(m), Math.sqrt(minSize),
                ABSOLUTE_ERROR);
    }
    
    @Test
    public void testGetNormArray(){
        FrobeniusNormComputer normComputer = new FrobeniusNormComputer();
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        double sum = 0.0, norm;
        double initValue = randomizer.nextDouble(MIN_LIMIT, MAX_LIMIT);
        double value;
        
        double[] v = new double[length];
        
        for(int i = 0; i < length; i++){
            value = randomizer.nextDouble(MIN_LIMIT, MAX_LIMIT);
            v[i] = value;
            sum += value * value;
        }
        
        norm = Math.sqrt(sum);
        
        assertEquals(normComputer.getNorm(v), norm, ABSOLUTE_ERROR);
        assertEquals(FrobeniusNormComputer.norm(v), norm, ABSOLUTE_ERROR);
        
        Arrays.fill(v, initValue);
        
        norm = initValue * Math.sqrt(length);
        
        assertEquals(normComputer.getNorm(v), norm, ABSOLUTE_ERROR);
        assertEquals(FrobeniusNormComputer.norm(v), norm, ABSOLUTE_ERROR);
    }
    
    @Test
    public void testNormWithJacobian() throws AlgebraException{
        FrobeniusNormComputer normComputer = new FrobeniusNormComputer();
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_LENGTH, MAX_LENGTH);
        double sum = 0.0, norm;
        double value;
        
        double[] v = new double[length];
        
        for(int i = 0; i < length; i++){
            value = randomizer.nextDouble(MIN_LIMIT, MAX_LIMIT);
            v[i] = value;
            sum += value * value;
        }
        
        norm = Math.sqrt(sum);    
        
        Matrix jacobian = new Matrix(1, length);        
        assertEquals(FrobeniusNormComputer.norm(v, jacobian), norm, 
                ABSOLUTE_ERROR);        
        assertEquals(jacobian, Matrix.newFromArray(v).
                multiplyByScalarAndReturnNew(1.0 / norm).
                transposeAndReturnNew());
        
        //Force WrongSizeException
        try{
            FrobeniusNormComputer.norm(v, new Matrix(2,length));
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
