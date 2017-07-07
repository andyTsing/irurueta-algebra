/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.algebra.Matrix
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 9, 2012
 */
package com.irurueta.algebra;

import com.irurueta.statistics.UniformRandomizer;
import java.security.SecureRandom;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.*;

public class MatrixTest {
    
    public static final int MIN_ROWS = 1;
    public static final int MAX_ROWS = 50;
    public static final int MIN_COLUMNS = 1;
    public static final int MAX_COLUMNS = 50;
    
    public static final double MIN_RANDOM_VALUE = 0.0;
    public static final double MAX_RANDOM_VALUE = 100.0;
    
    public static final int TIMES = 10000;
    
    public static final double MEAN = 5;
    public static final double STANDARD_DEVIATION = 100.0;
    
    public static final double ABSOLUTE_ERROR = 1e-9;
    public static final double RELATIVE_ERROR = 0.1;
    
    public MatrixTest() {
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
    public void testConstructorGetRowsAndGetColumns() 
            throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = new Matrix(rows, columns);
        assertNotNull(m);
        assertEquals(m.getRows(), rows);
        assertEquals(m.getColumns(), columns);
        
        //Force WrongSizeException
        m = null;
        try{
            m = new Matrix(0, 0);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        assertNull(m);
    }
    
    @Test
    public void testGetSetElementAt() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = new Matrix(rows, columns);
        double[][] tmp = new double[rows][columns];
        double value;
                
        //initialize matrix and array to random values
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                value = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE);
                tmp[i][j] = value;
                m.setElementAt(i, j, value);
            }
        }
        
        //check that matrix contains same values in array
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                value = tmp[i][j];
                assertEquals(m.getElementAt(i, j), value, 0.0);
            }
        }
    }
    
    @Test
    public void testGetIndex() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = new Matrix(rows, columns);
        double[][] tmp = new double[rows][columns];
        double value;
                
        //initialize matrix and array to random values
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                value = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE);
                tmp[i][j] = value;
                m.setElementAt(i, j, value);
            }
        }

        //check that matrix contains same values in array and that it 
        //corresponds to computed index
        int index;
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                index = j * rows + i;
                assertEquals(m.getIndex(i, j), index);
                value = tmp[i][j];
                assertEquals(m.getElementAt(i, j), value, 0.0);
                assertEquals(m.getElementAtIndex(index), value, 0.0);
            }
        }
    }
    
    @Test
    public void testGetSetElementAtIndex() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int length = rows * columns;
        int index;
        
        Matrix m1 = new Matrix(rows, columns);
        Matrix m2 = new Matrix(rows, columns);
        
        double[] tmp = new double[length];
        double value;
        
        //initialize matrix and array to random value using column order
        for(int i = 0; i < length; i++){
            value = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
            tmp[i] = value;
            m1.setElementAtIndex(i, value, true);
            m2.setElementAtIndex(i, value);
        }
        
        //check that matrices have the same values contained in array using
        //column order
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                index = j * rows + i;
                value = tmp[index];
                assertEquals(m1.getElementAt(i, j), value, 0.0);
                assertEquals(m1.getElementAtIndex(index), value, 0.0);
                assertEquals(m1.getElementAtIndex(index, true), value, 0.0);
                
                assertEquals(m2.getElementAt(i, j), value, 0.0);
                assertEquals(m2.getElementAtIndex(index), value, 0.0);
                assertEquals(m2.getElementAtIndex(index, true), value, 0.0);                
            }
        }
        
        //initialize matrix m1 and array to random values using row order
        for(int i = 0; i < length; i++){
            value = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
            tmp[i] = value;
            m1.setElementAtIndex(i, value, false);
        }
        
        //checks that matrix contains same values in array using row order
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                index = i * columns + j;
                value = tmp[index];
                assertEquals(m1.getElementAt(i, j), value, 0.0);
                assertEquals(m1.getElementAtIndex(index, false), value, 0.0);
            }
        }
    }
    
    @Test
    public void testClone() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        //instantiate matrix and fill with random values
        Matrix m1 = new Matrix(rows, columns);
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                m1.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE));
            }
        }
        
        //clone matrix
        Matrix m2 = m1.clone();
        
        //check correctness
        assertEquals(m2.getRows(), rows);
        assertEquals(m2.getColumns(), columns);
        
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                assertEquals(m1.getElementAt(i, j), m2.getElementAt(i, j), 0.0);
            }
        }
    }
    
    @Test
    public void testCopyTo() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        //instantiate matrix and fill with random values
        Matrix m = new Matrix(rows, columns);
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                m.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE));
            }
        }
        
        //instantiate destination matrix
        Matrix destination = new Matrix(1, 1);
        assertEquals(destination.getRows(), 1);
        assertEquals(destination.getColumns(), 1);
        
        //copy to destination
        m.copyTo(destination);
        
        //check correctness
        assertEquals(destination.getRows(), rows);
        assertEquals(destination.getColumns(), columns);
        
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                assertEquals(m.getElementAt(i, j), 
                        destination.getElementAt(i, j), 0.0);
            }
        }
        
        //Force NullPointerException
        try{
            m.copyTo(null);
            fail("NullPointerException expected but not thrown");
        }catch(NullPointerException e){}
    }
    
    @Test
    public void testCopyFrom() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);

        Matrix source = new Matrix(rows, columns);
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                source.setElementAt(i, j, randomizer.nextDouble(
                        MIN_RANDOM_VALUE, MAX_RANDOM_VALUE));
            }
        }
        
        //instantiate destination matrix
        Matrix destination = new Matrix(1, 1);
        assertEquals(destination.getRows(), 1);
        assertEquals(destination.getColumns(), 1);
        
        //copy from source
        destination.copyFrom(source);
        
        //check correctness
        assertEquals(destination.getRows(), rows);
        assertEquals(destination.getColumns(), columns);
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                assertEquals(destination.getElementAt(i, j), 
                        source.getElementAt(i, j), 0.0);
            }
        }
        
        //Force NullPointerException
        try{
            destination.copyFrom(null);
            fail("NullPointerException expected but not thrown");
        }catch(NullPointerException e){}
    }
    
    @Test
    public void testAddAndReturnNew() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m1 = new Matrix(rows, columns);
        Matrix m2 = new Matrix(rows, columns);
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                m1.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
                m2.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE));
            }
        }
        
        Matrix m3 = m1.addAndReturnNew(m2);
        
        //check correctness
        assertEquals(m3.getRows(), rows);
        assertEquals(m3.getColumns(), columns);
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                assertEquals(m3.getElementAt(i, j), m1.getElementAt(i, j) +
                        m2.getElementAt(i, j), ABSOLUTE_ERROR);
            }
        }
        
        //Force WrongSizeException
        Matrix wrong = new Matrix(rows + 1, columns + 1);
        m3 = null;
        try{
            m3 = m1.addAndReturnNew(wrong);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        assertNull(m3);
        
        //Force NullPointerException        
        try{
            m3 = m1.addAndReturnNew(null);
            fail("NullPointerException expected but not thrown");
        }catch(NullPointerException e){}
        assertNull(m3);
    }
    
    @Test
    public void testAdd() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m1 = new Matrix(rows, columns);
        Matrix m2 = new Matrix(rows, columns);
        double[][] tmp = new double[rows][columns];
        double value;
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                value = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE);
                m1.setElementAt(i, j, value);
                tmp[i][j] = value;
                m2.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE));
            }
        }
        
        m1.add(m2);
        
        //check correctness
        assertEquals(m1.getRows(), rows);
        assertEquals(m1.getColumns(), columns);
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                assertEquals(m1.getElementAt(i, j), tmp[i][j] +
                        m2.getElementAt(i, j), ABSOLUTE_ERROR);
            }
        }
        
        //Force WrongSizeException
        Matrix wrong = new Matrix(rows + 1, columns + 1);
        try{
            m1.add(wrong);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        
        //Force NullPointerException        
        try{
            m1.add(null);
            fail("NullPointerException expected but not thrown");
        }catch(NullPointerException e){}
    }    
    
    @Test
    public void testSubtractAndReturnNew() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m1 = new Matrix(rows, columns);
        Matrix m2 = new Matrix(rows, columns);
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                m1.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE,
                        MAX_RANDOM_VALUE));
                m2.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE));
            }
        }
        
        Matrix m3 = m1.subtractAndReturnNew(m2);
        
        //check correctness
        assertEquals(m3.getRows(), rows);
        assertEquals(m3.getColumns(), columns);
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                assertEquals(m3.getElementAt(i, j), m1.getElementAt(i, j) -
                        m2.getElementAt(i, j), ABSOLUTE_ERROR);
            }
        }
        
        //Force WrongSizeException
        Matrix wrong = new Matrix(rows + 1, columns + 1);
        m3 = null;
        try{
            m3 = m1.subtractAndReturnNew(wrong);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        assertNull(m3);
        
        //Force NullPointerException        
        try{
            m3 = m1.subtractAndReturnNew(null);
            fail("NullPointerException expected but not thrown");
        }catch(NullPointerException e){}
        assertNull(m3);
    }
    
    @Test
    public void testSubtract() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m1 = new Matrix(rows, columns);
        Matrix m2 = new Matrix(rows, columns);
        double[][] tmp = new double[rows][columns];
        double value;
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                value = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE);
                m1.setElementAt(i, j, value);
                tmp[i][j] = value;
                m2.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE));
            }
        }
        
        m1.subtract(m2);
        
        //check correctness
        assertEquals(m1.getRows(), rows);
        assertEquals(m1.getColumns(), columns);
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                assertEquals(m1.getElementAt(i, j), tmp[i][j] -
                        m2.getElementAt(i, j), ABSOLUTE_ERROR);
            }
        }
        
        //Force WrongSizeExceptionException
        Matrix wrong = new Matrix(rows + 1, columns + 1);
        try{
            m1.add(wrong);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        
        //Force NullPointerException        
        try{
            m1.add(null);
            fail("NullPointerException expected but not thrown");
        }catch(NullPointerException e){}
    }    
    
    @Test
    public void testMultiplyAndReturnNew() throws WrongSizeException{
        int rows1 = 4, columns1 = 3, rows2 = 3, columns2 = 2;
        Matrix m1 = new Matrix(rows1, columns1);
        Matrix m2 = new Matrix(rows2, columns2);
        
        Matrix result;
        
        //fill m1 and m2 with predefined values
        m1.setElementAt(0, 0, 1.0);
        m1.setElementAt(0, 1, 2.0);
        m1.setElementAt(0, 2, 3.0);
        m1.setElementAt(1, 0, 4.0);
        m1.setElementAt(1, 1, 5.0);
        m1.setElementAt(1, 2, 6.0);
        m1.setElementAt(2, 0, 6.0);
        m1.setElementAt(2, 1, 5.0);
        m1.setElementAt(2, 2, 4.0);
        m1.setElementAt(3, 0, 3.0);
        m1.setElementAt(3, 1, 2.0);
        m1.setElementAt(3, 2, 1.0);        

        m2.setElementAt(0, 0, 1.0);
        m2.setElementAt(0, 1, 2.0);
        m2.setElementAt(1, 0, 3.0);
        m2.setElementAt(1, 1, 4.0);
        m2.setElementAt(2, 0, 5.0);
        m2.setElementAt(2, 1, 6.0);
		
        //make matrix product
        result = m1.multiplyAndReturnNew(m2);
		
        //we know result for provided set of matrices m1 and m2, check it is
	//correct
        assertEquals(result.getRows(), rows1);
        assertEquals(result.getColumns(), columns2);
        
        assertEquals(result.getElementAt(0, 0), 22.0, ABSOLUTE_ERROR);
        assertEquals(result.getElementAt(0, 1), 28.0, ABSOLUTE_ERROR);
        assertEquals(result.getElementAt(1, 0), 49.0, ABSOLUTE_ERROR);
        assertEquals(result.getElementAt(1, 1), 64.0, ABSOLUTE_ERROR);
        assertEquals(result.getElementAt(2, 0), 41.0, ABSOLUTE_ERROR);
        assertEquals(result.getElementAt(2, 1), 56.0, ABSOLUTE_ERROR);
        assertEquals(result.getElementAt(3, 0), 14.0, ABSOLUTE_ERROR);
        assertEquals(result.getElementAt(3, 1), 20.0, ABSOLUTE_ERROR);
        
		
	//Force IllegalArgumentException
        Matrix m3 = new Matrix(columns1, rows1);
        Matrix m4 = new Matrix(columns2, rows2);
        
        result = null;
        try{
            result = m3.multiplyAndReturnNew(m4);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        assertNull(result);
        
        //Force NullPointerException
        try{
            result = m3.multiplyAndReturnNew(null);
            fail("NullPointerException expected but not thrown");
        }catch(NullPointerException e){}
        assertNull(result);
    }
    
    @Test
    public void testMultiply() throws WrongSizeException{
        int rows1 = 4, columns1 = 3, rows2 = 3, columns2 = 2;
        Matrix m1 = new Matrix(rows1, columns1);
        Matrix m2 = new Matrix(rows2, columns2);
                
        //fill m1 and m2 with predefined values
        m1.setElementAt(0, 0, 1.0);
        m1.setElementAt(0, 1, 2.0);
        m1.setElementAt(0, 2, 3.0);
        m1.setElementAt(1, 0, 4.0);
        m1.setElementAt(1, 1, 5.0);
        m1.setElementAt(1, 2, 6.0);
        m1.setElementAt(2, 0, 6.0);
        m1.setElementAt(2, 1, 5.0);
        m1.setElementAt(2, 2, 4.0);
        m1.setElementAt(3, 0, 3.0);
        m1.setElementAt(3, 1, 2.0);
        m1.setElementAt(3, 2, 1.0);        

        m2.setElementAt(0, 0, 1.0);
        m2.setElementAt(0, 1, 2.0);
        m2.setElementAt(1, 0, 3.0);
        m2.setElementAt(1, 1, 4.0);
        m2.setElementAt(2, 0, 5.0);
        m2.setElementAt(2, 1, 6.0);
		
        //make matrix product
        m1.multiply(m2);
		
        //we know result for provided set of matrices m1 and m2, check it is
	//correct
        assertEquals(m1.getRows(), rows1);
        assertEquals(m1.getColumns(), columns2);
        
        assertEquals(m1.getElementAt(0, 0), 22.0, ABSOLUTE_ERROR);
        assertEquals(m1.getElementAt(0, 1), 28.0, ABSOLUTE_ERROR);
        assertEquals(m1.getElementAt(1, 0), 49.0, ABSOLUTE_ERROR);
        assertEquals(m1.getElementAt(1, 1), 64.0, ABSOLUTE_ERROR);
        assertEquals(m1.getElementAt(2, 0), 41.0, ABSOLUTE_ERROR);
        assertEquals(m1.getElementAt(2, 1), 56.0, ABSOLUTE_ERROR);
        assertEquals(m1.getElementAt(3, 0), 14.0, ABSOLUTE_ERROR);
        assertEquals(m1.getElementAt(3, 1), 20.0, ABSOLUTE_ERROR);
        
		
	//Force IllegalArgumentException
        Matrix m3 = new Matrix(columns1, rows1);
        Matrix m4 = new Matrix(columns2, rows2);
        
        try{
            m3.multiply(m4);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        
        //Force NullPointerException
        try{
            m3.multiply(null);
            fail("NullPointerException expected but not thrown");
        }catch(NullPointerException e){}
    }    
    
    @Test
    public void testMultiplyKroneckerAndReturnNew() throws WrongSizeException{
        Matrix m1 = new Matrix(2,2);
        Matrix m2 = new Matrix(2,2);
        
        m1.setSubmatrix(0, 0, 1, 1, new double[]{1,3,2,1});
        m2.setSubmatrix(0, 0, 1, 1, new double[]{0,2,3,1});
        
        Matrix m3 = m1.multiplyKroneckerAndReturnNew(m2);
        
        //check correctness
        assertEquals(m3.getRows(), 4);
        assertEquals(m3.getColumns(), 4);
        
        Matrix m3b = new Matrix(4,4);
        m3b.setSubmatrix(0, 0, 3, 3, new double[]{
            1*0, 1*2, 3*0, 3*2,
            1*3, 1*1, 3*3, 3*1,
            2*0, 2*2, 1*0, 1*2,
            2*3, 2*1, 1*3, 1*1
        });
        
        assertEquals(m3, m3b);
    }
    
    @Test
    public void testMultiplyKronecker() throws WrongSizeException{
        Matrix m1 = new Matrix(2,2);
        Matrix m2 = new Matrix(2,2);
        
        m1.setSubmatrix(0, 0, 1, 1, new double[]{1,3,2,1});
        m2.setSubmatrix(0, 0, 1, 1, new double[]{0,2,3,1});
        
        Matrix m3 = new Matrix(2,2);
        m1.multiplyKronecker(m2, m3);
        m1.multiplyKronecker(m2);
        
        //check correctness
        assertEquals(m3.getRows(), 4);
        assertEquals(m3.getColumns(), 4);
        
        assertEquals(m1.getRows(), 4);
        assertEquals(m1.getColumns(), 4);
        
        Matrix m3b = new Matrix(4,4);
        m3b.setSubmatrix(0, 0, 3, 3, new double[]{
            1*0, 1*2, 3*0, 3*2,
            1*3, 1*1, 3*3, 3*1,
            2*0, 2*2, 1*0, 1*2,
            2*3, 2*1, 1*3, 1*1
        });
        
        assertEquals(m1, m3b);
        assertEquals(m3, m3b);
    }    
    
    @Test
    public void testMultiplyByScalarAndReturnNew() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = new Matrix(rows, columns);
        //fill matrix
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                m.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE));
            }
        }
        
        double scalar = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        
        Matrix result = m.multiplyByScalarAndReturnNew(scalar);
        
        //check correctness
        assertEquals(result.getRows(), rows);
        assertEquals(result.getColumns(), columns);
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                assertEquals(result.getElementAt(i, j),
                        m.getElementAt(i, j) * scalar, ABSOLUTE_ERROR);
            }
        }                
    }
    
    @Test
    public void testMultiplyByScalar() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = new Matrix(rows, columns);
        double[][] tmp = new double[rows][columns];
        double value;
        //fill matrix
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                value = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE);
                m.setElementAt(i, j, value);
                tmp[i][j] = value;
            }
        }
        
        double scalar = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        
        m.multiplyByScalar(scalar);
        
        //check correctness
        assertEquals(m.getRows(), rows);
        assertEquals(m.getColumns(), columns);
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                assertEquals(m.getElementAt(i, j),
                        tmp[i][j] * scalar, ABSOLUTE_ERROR);
            }
        }
    }
    
    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void testEqualsAndHashCode() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = new Matrix(rows, columns);
        Matrix equal = new Matrix(rows, columns);
        Matrix different1 = new Matrix(rows + 1, columns + 1);
        Matrix different2 = new Matrix(rows, columns);
        Object different3 = new Object();
        
        double value;
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                value = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE);
                m.setElementAt(i, j, value);
                equal.setElementAt(i, j, value);
                different1.setElementAt(i, j, value);
                different2.setElementAt(i, j, value + 1.0);
            }
        }
        
        //check correctness
        assertTrue(m.equals(m));
        assertTrue(m.equals(equal));
        assertFalse(m.equals(different1));
        assertFalse(m.equals(different2));
        assertFalse(m.equals(different3));
        assertFalse(m.equals(null));
        
        assertEquals(m.hashCode(), equal.hashCode());
        
        //check with threshold
        assertTrue(m.equals(m, ABSOLUTE_ERROR));
        assertTrue(m.equals(equal, ABSOLUTE_ERROR));
        assertFalse(m.equals(different1, ABSOLUTE_ERROR));
        assertFalse(m.equals(different2, ABSOLUTE_ERROR));        
        assertFalse(m.equals(null, ABSOLUTE_ERROR));
    }
    
    @Test
    public void testElementByElementProductAndReturnNew() 
            throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        Matrix m1 = new Matrix(rows, columns);
        Matrix m2 = new Matrix(rows, columns);
        
        //fill matrices
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                m1.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE));
                m2.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE));                
            }
        }
        
        Matrix m3 = m1.elementByElementProductAndReturnNew(m2);
        
        //check correctness
        assertEquals(m3.getRows(), rows);
        assertEquals(m3.getColumns(), columns);
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                assertEquals(m3.getElementAt(i, j),
                        m1.getElementAt(i, j) * m2.getElementAt(i, j),
                        ABSOLUTE_ERROR);
            }
        }
        
        //Force WrongSizeException
        Matrix wrong = new Matrix(rows + 1, columns + 1);
        m3 = null;
        try{
            m3 = m1.elementByElementProductAndReturnNew(wrong);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        assertNull(m3);
        
        //Force NullPointerException
        try{
            m3 = m1.elementByElementProductAndReturnNew(null);
            fail("NullPointerException expected but not thrown");
        }catch(NullPointerException e){}
        assertNull(m3);
    }
    
    @Test
    public void testElementByElementProduct() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        Matrix m1 = new Matrix(rows, columns);
        Matrix m2 = new Matrix(rows, columns);
        double[][] tmp = new double[rows][columns];
        double value;
        
        //fill matrices
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                value = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE);
                m1.setElementAt(i, j, value);
                tmp[i][j] = value;
                m2.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE));                
            }
        }
        
        m1.elementByElementProduct(m2);
        
        //check correctness
        assertEquals(m1.getRows(), rows);
        assertEquals(m1.getColumns(), columns);
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                assertEquals(m1.getElementAt(i, j),
                        tmp[i][j] * m2.getElementAt(i, j), ABSOLUTE_ERROR);
            }
        }
        
        //Force WrongSizeException
        Matrix wrong = new Matrix(rows + 1, columns + 1);
        try{
            m1.elementByElementProduct(wrong);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        
        //Force NullPointerException
        try{
            m1.elementByElementProduct(null);
            fail("NullPointerException expected but not thrown");
        }catch(NullPointerException e){}
    }
    
    @Test
    public void testTransposeAndReturnNEw() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m1 = new Matrix(rows, columns);
        //fill matrix
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                m1.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE));
            }
        }
        
        Matrix m2 = m1.transposeAndReturnNew();
        
        //check correctness
        assertEquals(m2.getRows(), columns);
        assertEquals(m2.getColumns(), rows);
        
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                assertEquals(m1.getElementAt(i, j), m2.getElementAt(j, i), 0.0);
            }
        }
    }
    
    @Test
    public void testTranspose() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = new Matrix(rows, columns);
        double[][] tmp = new double[rows][columns];
        double value;
        
        //fill matrix
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                value = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE);
                m.setElementAt(i, j, value);
                tmp[i][j] = value;
            }
        }
        
        m.transpose();
        
        //check correctness
        assertEquals(m.getRows(), columns);
        assertEquals(m.getColumns(), rows);
        
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                assertEquals(tmp[i][j], m.getElementAt(j, i), 0.0);
            }
        }
    }
    
    @Test
    public void testInitialize() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = new Matrix(rows, columns);
        //fill with random values
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                m.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE));
            }
        }
        
        //pick an init value
        double value = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        
        m.initialize(value);
        
        //check correctness
        assertEquals(m.getRows(), rows);
        assertEquals(m.getColumns(), columns);
        
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                assertEquals(m.getElementAt(i, j), value, 0.0);
            }
        }
    }
    
    @Test
    public void testResetAndResize() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows1 = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int rows2 = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns1 = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int columns2 = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = new Matrix(rows1, columns1);
        assertEquals(m.getRows(), rows1);
        assertEquals(m.getColumns(), columns1);
        
        //reset to new size
        m.resize(rows2, columns2);
        
        //check correctness
        assertEquals(m.getRows(), rows2);
        assertEquals(m.getColumns(), columns2);
        
        //Force WrongSizeException
        try{
            m.resize(0, columns2);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        try{
            m.resize(rows2, 0);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        try{
            m.resize(0, 0);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        
        
        //reset to new size and value
        double initValue = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        
        //reset to new size and value
        m.reset(rows1, columns1, initValue);
        
        //check correctness
        assertEquals(m.getRows(), rows1);
        assertEquals(m.getColumns(), columns1);
        
        for(int j = 0; j < columns1; j++){
            for(int i = 0; i < rows1; i++){
                assertEquals(m.getElementAt(i, j), initValue, 0.0);
            }
        }
        
        //Force WrongSizeException
        try{
            m.reset(0, columns1, initValue);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        try{
            m.reset(rows1, 0, initValue);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        try{
            m.reset(0, 0, initValue);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
    }
    
    @Test
    public void testToArray() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = new Matrix(rows, columns);
        double[] array = new double[rows * columns];
        double value;
        int counter = 0;
        
        //fill matrix
        if(Matrix.DEFAULT_USE_COLUMN_ORDER){
            //use column order
            for(int j = 0; j < columns; j++){
                for(int i = 0; i < rows; i++){
                    value = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                            MAX_RANDOM_VALUE);
                    m.setElementAt(i, j, value);
                    array[counter] = value;
                    counter++;
                }
            }
        }else{
            //use row order
            for(int i = 0; i < rows; i++){
                for(int j = 0; j < columns; j++){
                    value = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                            MAX_RANDOM_VALUE);
                    m.setElementAt(i, j, value);
                    array[counter] = value;
                    counter++;
                }
            }
        }
        
        double[] array2 = m.toArray();
        double[] array3 = new double[array.length];
        m.toArray(array3);
        
        //check correctness
        for(int i = 0; i < rows * columns; i++){
            assertEquals(array[i], array2[i], 0.0);
            assertEquals(array[i], array3[i], 0.0);
        }
    }
    
    @Test
    public void testToArrayWithColumnOrder() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = new Matrix(rows, columns);
        double[] array = new double[rows * columns];
        double value;
        int counter = 0;
        
        //fill with column order                
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                value = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE);
                m.setElementAt(i, j, value);
                array[counter] = value;
                counter++;
            }
        }
        
        double[] array2 = m.toArray(true);
        double[] array3 = new double[array.length];
        m.toArray(array3, true);        
        
        //check correctness
        for(int i = 0; i < rows * columns; i++){
            assertEquals(array[i], array2[i], 0.0);
            assertEquals(array[i], array3[i], 0.0);
        }        
    }
    
    @Test
    public void testToArrayWithRowOrder() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = new Matrix(rows, columns);
        double[] array = new double[rows * columns];
        double value;
        int counter = 0;
        
        //fill with row order
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                value = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE);
                m.setElementAt(i, j, value);
                array[counter] = value;
                counter++;
            }
        }
        
        double[] array2 = m.toArray(false);
        double[] array3 = new double[array.length];
        m.toArray(array3, false);
        
        //check correctness
        for(int i = 0; i < rows * columns; i++){
            assertEquals(array[i], array2[i], 0.0);
            assertEquals(array[i], array3[i], 0.0);
        }        
    }
    
    @Test
    public void testGetSubmatrix() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        
        int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);
        
        int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);
        
        
        Matrix m = new Matrix(rows, columns);
        
        //fill matrix with random values
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                m.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE));
            }
        }
        
        Matrix submatrix = m.getSubmatrix(topLeftRow, topLeftColumn, 
                bottomRightRow, bottomRightColumn);
        
        //check correctness
        assertEquals(submatrix.getRows(), bottomRightRow - topLeftRow + 1);
        assertEquals(submatrix.getColumns(), 
                bottomRightColumn - topLeftColumn + 1);
        
        for(int j = 0; j < submatrix.getColumns(); j++){
            for(int i = 0; i < submatrix.getRows(); i++){
                assertEquals(submatrix.getElementAt(i, j),
                        m.getElementAt(i + topLeftRow, j + topLeftColumn), 0.0);
            }
        }
        
        //Force IllegalArgumentException
        submatrix = null;
        try{
            submatrix = m.getSubmatrix(rows, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(submatrix);
        try{
            submatrix = m.getSubmatrix(topLeftRow, columns,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(submatrix);
        try{
            submatrix = m.getSubmatrix(topLeftRow, topLeftColumn, rows, 
                    bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(submatrix);
        try{
            submatrix = m.getSubmatrix(topLeftRow, topLeftColumn,
                    bottomRightRow, columns);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(submatrix);
        try{
            submatrix = m.getSubmatrix(topLeftRow + 1, topLeftColumn, 
                    topLeftRow, topLeftColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(submatrix);
        try{
            submatrix = m.getSubmatrix(topLeftRow, topLeftColumn + 1,
                    topLeftRow, topLeftColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(submatrix);
        try{
            submatrix = m.getSubmatrix(topLeftRow + 1, topLeftColumn + 1,
                    topLeftRow, topLeftColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(submatrix);
    }
    
    @Test
    public void testGetSubmatrixAsArray() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        
        int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);
        
        int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);
        
        
        Matrix m = new Matrix(rows, columns);
        
        //fill matrix with random values
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                m.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE));
            }
        }
        
        double[] array  = m.getSubmatrixAsArray(topLeftRow, topLeftColumn, 
                    bottomRightRow, bottomRightColumn);
        assertEquals(array.length, (bottomRightRow - topLeftRow + 1) * 
                (bottomRightColumn - topLeftColumn + 1));
        int counter = 0;
        
        if(Matrix.DEFAULT_USE_COLUMN_ORDER){
            //column order
            for(int j = 0; j < (bottomRightColumn - topLeftColumn + 1); j++){
                for(int i = 0; i < (bottomRightRow - topLeftRow + 1); i++){
                    assertEquals(array[counter],
                            m.getElementAt(i + topLeftRow, j + topLeftColumn), 
                            0.0);
                    counter++;
                }
            }
        }else{
            //row order
            for(int i = 0; i < (bottomRightRow - topLeftRow + 1); i++){
                for(int j = 0; j < (bottomRightColumn - topLeftColumn + 1); j++){
                    assertEquals(array[counter],
                            m.getElementAt(i + topLeftRow, j + topLeftColumn),
                            0.0);
                    counter++;
                }
            }
        }
                
        //Force IllegalArgumentException
        array = null;
        try{
            array = m.getSubmatrixAsArray(rows, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow, columns,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn, rows, 
                    bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn,
                    bottomRightRow, columns);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow + 1, topLeftColumn, 
                    topLeftRow, topLeftColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn + 1,
                    topLeftRow, topLeftColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow + 1, topLeftColumn + 1,
                    topLeftRow, topLeftColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);        
    }
    
    @Test
    public void testGetSubmatrixAsArrayWithColumnOrder() 
            throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        
        int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);
        
        int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);
        
        
        Matrix m = new Matrix(rows, columns);
        
        //fill matrix with random values
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                m.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE));
            }
        }
        
        double[] array  = m.getSubmatrixAsArray(topLeftRow, topLeftColumn, 
                    bottomRightRow, bottomRightColumn, true);
        assertEquals(array.length, (bottomRightRow - topLeftRow + 1) * 
                (bottomRightColumn - topLeftColumn + 1));
        int counter = 0;
        
        //column order
        for(int j = 0; j < (bottomRightColumn - topLeftColumn + 1); j++){
            for(int i = 0; i < (bottomRightRow - topLeftRow + 1); i++){
                assertEquals(array[counter],
                        m.getElementAt(i + topLeftRow, j + topLeftColumn), 0.0);
                counter++;
            }
        }
                
        //Force IllegalArgumentException
        array = null;
        try{
            array = m.getSubmatrixAsArray(rows, topLeftColumn,
                    bottomRightRow, bottomRightColumn, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow, columns,
                    bottomRightRow, bottomRightColumn, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn, rows, 
                    bottomRightColumn, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn,
                    bottomRightRow, columns, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow + 1, topLeftColumn, 
                    topLeftRow, topLeftColumn, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn + 1,
                    topLeftRow, topLeftColumn, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow + 1, topLeftColumn + 1,
                    topLeftRow, topLeftColumn, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);        
    }
    
    @Test
    public void testGetSubmatrixAsArrayWithRowOrder() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        
        int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);
        
        int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);
        
        
        Matrix m = new Matrix(rows, columns);
        
        //fill matrix with random values
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                m.setElementAt(i, j, randomizer.nextDouble(MIN_RANDOM_VALUE, 
                        MAX_RANDOM_VALUE));
            }
        }
        
        double[] array  = m.getSubmatrixAsArray(topLeftRow, topLeftColumn, 
                    bottomRightRow, bottomRightColumn, false);
        assertEquals(array.length, (bottomRightRow - topLeftRow + 1) * 
                (bottomRightColumn - topLeftColumn + 1));
        int counter = 0;
        
        //row order
        for(int i = 0; i < (bottomRightRow - topLeftRow + 1); i++){
            for(int j = 0; j < (bottomRightColumn - topLeftColumn + 1); j++){
                assertEquals(array[counter],
                        m.getElementAt(i + topLeftRow, j + topLeftColumn), 0.0);
                counter++;
            }
        }
                
        //Force IllegalArgumentException
        array = null;
        try{
            array = m.getSubmatrixAsArray(rows, topLeftColumn,
                    bottomRightRow, bottomRightColumn, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow, columns,
                    bottomRightRow, bottomRightColumn, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn, rows, 
                    bottomRightColumn, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn,
                    bottomRightRow, columns, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow + 1, topLeftColumn, 
                    topLeftRow, topLeftColumn, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow, topLeftColumn + 1,
                    topLeftRow, topLeftColumn, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);
        try{
            array = m.getSubmatrixAsArray(topLeftRow + 1, topLeftColumn + 1,
                    topLeftRow, topLeftColumn, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(array);        
    }
    
    @Test
    public void testSetSubmatrix() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        
        int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);
        
        int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);
        
        Matrix m = new Matrix(rows, columns);
        
        int submatrixRows = bottomRightRow - topLeftRow + 1;
        int submatrixColumns = bottomRightColumn - topLeftColumn + 1;
        Matrix submatrix = new Matrix(submatrixRows, submatrixColumns);
        
        //fill submatrix with random values
        for(int j = 0; j < submatrixColumns; j++){
            for(int i = 0; i < submatrixRows; i++){
                submatrix.setElementAt(i, j, randomizer.nextDouble(
                        MIN_RANDOM_VALUE, MAX_RANDOM_VALUE));
            }
        }
        
        //set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                bottomRightColumn, submatrix);
        
        //check correctness
        for(int j = 0; j < submatrixColumns; j++){
            for(int i = 0; i < submatrixRows; i++){
                assertEquals(m.getElementAt(i + topLeftRow, j + topLeftColumn),
                        submatrix.getElementAt(i, j), 0.0);
            }
        }
        
        //Force IllegalArgumentException
        try{
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, submatrix);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, submatrix);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow, 
                    bottomRightColumn, submatrix);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, columns, bottomRightRow, 
                    bottomRightColumn, submatrix);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow, 
                    bottomRightColumn, submatrix);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn, 
                    submatrix);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    -bottomRightColumn, submatrix);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns, 
                    submatrix);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow, 
                    bottomRightColumn, submatrix);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow, 
                    topLeftColumn, submatrix);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        Matrix wrong = new Matrix(submatrixRows + 1, submatrixColumns);
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, wrong);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        wrong = new Matrix(submatrixRows, submatrixColumns + 1);
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, wrong);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        wrong = new Matrix(submatrixRows + 1, submatrixColumns + 1);
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, wrong);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}        
    }
    
    @Test
    public void testSetSubmatrix2() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        
        int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);
        
        int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);
        
        Matrix m = new Matrix(rows, columns);
        
        int submatrixRows = bottomRightRow - topLeftRow + 1;
        int submatrixColumns = bottomRightColumn - topLeftColumn + 1;
        Matrix submatrix = new Matrix(rows, columns);
        
        //fill submatrix with random values
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                submatrix.setElementAt(i, j, randomizer.nextDouble(
                        MIN_RANDOM_VALUE, MAX_RANDOM_VALUE));
            }
        }
        
        //set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                bottomRightRow, bottomRightColumn);
        
        //check correctness
        for(int j = topLeftColumn; j <= bottomRightColumn; j++){
            for(int i = topLeftRow; i < bottomRightRow; i++){
                assertEquals(m.getElementAt(i, j),
                        submatrix.getElementAt(i, j), 0.0);
            }
        }
        
        //Force IllegalArgumentException
        try{
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow, 
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, columns, bottomRightRow, 
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow, 
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn, 
                    submatrix, topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    -bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns, 
                    submatrix, topLeftRow, topLeftColumn, bottomRightRow,
                    bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow, 
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow, 
                    topLeftColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
                

        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, submatrix, -topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, submatrix, rows, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, submatrix, topLeftRow, -topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, submatrix, topLeftRow, columns,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    -bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn, 
                    rows, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, -bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn, 
                    bottomRightRow, columns);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow, 
                    bottomRightColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow, 
                    topLeftColumn, submatrix, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, m, topLeftRow, topLeftColumn,
                    bottomRightRow + 1, bottomRightColumn);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, m, topLeftRow, topLeftColumn,
                    bottomRightRow, bottomRightColumn + 1);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, m, topLeftRow, topLeftColumn,
                    bottomRightRow + 1, bottomRightColumn + 1);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}                
    }   
    
    @Test
    public void testSetSubmatrixWithValue() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        
        int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);
        
        int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);
        
        double value = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        
        Matrix m = new Matrix(rows, columns);
        
        //fil matrix with random values
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                m.setElementAt(i, j, value + 1.0);
            }
        }
        
        //set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                bottomRightColumn, value);
        
        //check correctness
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                if(i >= topLeftRow && i <= bottomRightRow && 
                        j >= topLeftColumn && j <= bottomRightColumn){
                    assertEquals(m.getElementAt(i, j), value, 0.0);
                }else{
                    assertEquals(m.getElementAt(i, j), value + 1.0, 0.0);
                }
            }
        }
        
        //Force IllegalArgumentException
        try{
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, value);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, value);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow, 
                    bottomRightColumn, value);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, columns, bottomRightRow, 
                    bottomRightColumn, value);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow, 
                    bottomRightColumn, value);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn, 
                    value);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    -bottomRightColumn, value);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns, 
                    value);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow, 
                    bottomRightColumn, value);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow, 
                    topLeftColumn, value);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}        
    }
    
    @Test
    public void testSetSubmatrixWithArray() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        
        int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);
        
        int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);
        
        Matrix m = new Matrix(rows, columns);
        
        int submatrixRows = bottomRightRow - topLeftRow + 1;
        int submatrixColumns = bottomRightColumn - topLeftColumn + 1;
        int length = submatrixRows * submatrixColumns;
        double[] array = new double[length];
        
        //fill array with random values
        for(int i = 0; i < length; i++){
            array[i] = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE);
        }
        
        //set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                bottomRightColumn, array);
        int counter = 0;
        
        //check correctness
        if(Matrix.DEFAULT_USE_COLUMN_ORDER){
            //column order
            for(int j = 0; j < submatrixColumns; j++){
                for(int i = 0; i < submatrixRows; i++){
                    assertEquals(m.getElementAt(i + topLeftRow, 
                            j + topLeftColumn), array[counter], 0.0);
                    counter++;
                }
            }
            
        }else{
            //row order
            for(int i = 0; i < submatrixRows; i++){
                for(int j = 0; j < submatrixColumns; j++){
                    assertEquals(m.getElementAt(i + topLeftRow, 
                            j + topLeftColumn), array[counter], 0.0);
                    counter++;
                }
            }
        }
        
        //Force IllegalArgumentException
        try{
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, columns, bottomRightRow, 
                    bottomRightColumn, array);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow, 
                    bottomRightColumn, array);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn, 
                    array);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    -bottomRightColumn, array);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns, 
                    array);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow, 
                    bottomRightColumn, array);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow, 
                    topLeftColumn, array);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        double[] wrong = new double[length + 1];
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, wrong);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}        
    }
    
    @Test
    public void testSetSubmatrixWithArrayColumnOrder() 
            throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        
        int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);
        
        int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);
        
        Matrix m = new Matrix(rows, columns);
        
        int submatrixRows = bottomRightRow - topLeftRow + 1;
        int submatrixColumns = bottomRightColumn - topLeftColumn + 1;
        int length = submatrixRows * submatrixColumns;
        double[] array = new double[length];
        
        //fill array with random values
        for(int i = 0; i < length; i++){
            array[i] = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE);
        }
        
        //set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                bottomRightColumn, array, true);
        int counter = 0;
        
        //check correctness with column order
        for(int j = 0; j < submatrixColumns; j++){
            for(int i = 0; i < submatrixRows; i++){
                assertEquals(m.getElementAt(i + topLeftRow, 
                        j + topLeftColumn), array[counter], 0.0);
                counter++;
            }
        }
        
        //Force IllegalArgumentException
        try{
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, columns, bottomRightRow, 
                    bottomRightColumn, array, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow, 
                    bottomRightColumn, array, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn, 
                    array, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    -bottomRightColumn, array, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns, 
                    array, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow, 
                    bottomRightColumn, array, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow, 
                    topLeftColumn, array, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        double[] wrong = new double[length + 1];
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, wrong, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}        
    }
    
    @Test
    public void testSetSubmatrixWithArrayRowOrder() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        
        int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);
        
        int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);
        
        Matrix m = new Matrix(rows, columns);
        
        int submatrixRows = bottomRightRow - topLeftRow + 1;
        int submatrixColumns = bottomRightColumn - topLeftColumn + 1;
        int length = submatrixRows * submatrixColumns;
        double[] array = new double[length];
        
        //fill array with random values
        for(int i = 0; i < length; i++){
            array[i] = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE);
        }
        
        //set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                bottomRightColumn, array, false);
        int counter = 0;
        
        //check correctness with row order
        for(int i = 0; i < submatrixRows; i++){
            for(int j = 0; j < submatrixColumns; j++){
                assertEquals(m.getElementAt(i + topLeftRow, 
                        j + topLeftColumn), array[counter], 0.0);
                counter++;
            }
        }
        
        //Force IllegalArgumentException
        try{
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, columns, bottomRightRow, 
                    bottomRightColumn, array, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow, 
                    bottomRightColumn, array, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn, 
                    array, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    -bottomRightColumn, array, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns, 
                    array, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow, 
                    bottomRightColumn, array, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow, 
                    topLeftColumn, array, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        double[] wrong = new double[length + 1];
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, wrong, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}        
    }   
    
    @Test
    public void testSetSubmatrixWithArray2() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        
        int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);
        
        int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);
        
        int offset = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        Matrix m = new Matrix(rows, columns);
        
        int submatrixRows = bottomRightRow - topLeftRow + 1;
        int submatrixColumns = bottomRightColumn - topLeftColumn + 1;
        int length = submatrixRows * submatrixColumns;
        double[] array = new double[length + offset];
        
        //fill array with random values
        for(int i = 0; i < length; i++){
            array[i + offset] = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE);
        }
        
        //set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                bottomRightColumn, array, offset, offset + length - 1);
        int counter = offset;
        
        //check correctness
        if(Matrix.DEFAULT_USE_COLUMN_ORDER){
            //column order
            for(int j = 0; j < submatrixColumns; j++){
                for(int i = 0; i < submatrixRows; i++){
                    assertEquals(m.getElementAt(i + topLeftRow, 
                            j + topLeftColumn), array[counter], 0.0);
                    counter++;
                }
            }
            
        }else{
            //row order
            for(int i = 0; i < submatrixRows; i++){
                for(int j = 0; j < submatrixColumns; j++){
                    assertEquals(m.getElementAt(i + topLeftRow, 
                            j + topLeftColumn), array[counter], 0.0);
                    counter++;
                }
            }
        }
        
        //Force IllegalArgumentException
        try{
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, columns, bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn, 
                    array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    -bottomRightColumn, array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns, 
                    array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow, 
                    bottomRightColumn, array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow, 
                    topLeftColumn, array, offset, offset + length - 1);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length );
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}        
        
    }    
    
    @Test
    public void testSetSubmatrixWithArrayColumnOrder2() 
            throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        
        int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);
        
        int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);
        
        int offset = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        Matrix m = new Matrix(rows, columns);
        
        int submatrixRows = bottomRightRow - topLeftRow + 1;
        int submatrixColumns = bottomRightColumn - topLeftColumn + 1;
        int length = submatrixRows * submatrixColumns;
        double[] array = new double[length + offset];
        
        //fill array with random values
        for(int i = 0; i < length; i++){
            array[i + offset] = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE);
        }
        
        //set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                bottomRightColumn, array, offset, offset + length - 1, true);
        int counter = offset;
        
        //check correctness with column order
        for(int j = 0; j < submatrixColumns; j++){
            for(int i = 0; i < submatrixRows; i++){
                assertEquals(m.getElementAt(i + topLeftRow, 
                        j + topLeftColumn), array[counter], 0.0);
                counter++;
            }
        }
        
        //Force IllegalArgumentException
        try{
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length - 1, 
                    true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length - 1, 
                    true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length - 1, 
                    true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, columns, bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length - 1, 
                    true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length - 1, 
                    true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn, 
                    array, offset, offset + length - 1, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    -bottomRightColumn, array, offset, offset + length - 1, 
                    true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns, 
                    array, offset, offset + length - 1, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow, 
                    bottomRightColumn, array, offset, offset + length - 1, 
                    true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow, 
                    topLeftColumn, array, offset, offset + length - 1, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length, true);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}        
        
    }        
    
    @Test
    public void testSetSubmatrixWithArrayRowOrder2() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS + 2, MAX_ROWS + 2);
        int columns = randomizer.nextInt(MIN_COLUMNS + 2, MAX_COLUMNS + 2);
        
        int topLeftColumn = randomizer.nextInt(MIN_COLUMNS, columns - 1);
        int topLeftRow = randomizer.nextInt(MIN_ROWS, rows - 1);
        
        int bottomRightColumn = randomizer.nextInt(topLeftColumn, columns - 1);
        int bottomRightRow = randomizer.nextInt(topLeftRow, rows - 1);
        
        int offset = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        Matrix m = new Matrix(rows, columns);
        
        int submatrixRows = bottomRightRow - topLeftRow + 1;
        int submatrixColumns = bottomRightColumn - topLeftColumn + 1;
        int length = submatrixRows * submatrixColumns;
        double[] array = new double[length + offset];
        
        //fill array with random values
        for(int i = 0; i < length; i++){
            array[i + offset] = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE);
        }
        
        //set submatrix
        m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                bottomRightColumn, array, offset, offset + length - 1, false);
        int counter = offset;
        
        //check correctness with row order
        for(int i = 0; i < submatrixRows; i++){
            for(int j = 0; j < submatrixColumns; j++){
                assertEquals(m.getElementAt(i + topLeftRow, 
                        j + topLeftColumn), array[counter], 0.0);
                counter++;
            }
        }
        
        //Force IllegalArgumentException
        try{
            m.setSubmatrix(-topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length - 1, 
                    false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(rows, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length - 1, 
                    false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, -topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length - 1, 
                    false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, columns, bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length - 1, 
                    false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, -bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length - 1, 
                    false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, rows, bottomRightColumn, 
                    array, offset, offset + length - 1, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    -bottomRightColumn, array, offset, offset + length - 1, 
                    false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, columns, 
                    array, offset, offset + length - 1, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(bottomRightRow + 1, topLeftColumn, topLeftRow, 
                    bottomRightColumn, array, offset, offset + length - 1, 
                    false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            m.setSubmatrix(topLeftRow, bottomRightColumn + 1, bottomRightRow, 
                    topLeftColumn, array, offset, offset + length - 1, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
        try{
            m.setSubmatrix(topLeftRow, topLeftColumn, bottomRightRow, 
                    bottomRightColumn, array, offset, offset + length, false);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}        
    }        
    
    @Test
    public void testIdentity() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m = Matrix.identity(rows, columns);
        
        //check correctness
        assertEquals(m.getRows(), rows);
        assertEquals(m.getColumns(), columns);
        
        for(int j = 0; j < columns; j++){
            for(int i = 0; i < rows; i++){
                if(i == j) assertEquals(m.getElementAt(i, j), 1.0, 0.0);
                else assertEquals(m.getElementAt(i, j), 0.0, 0.0);
            }
        }
        
        //force WrongSizeException
        try{
            Matrix.identity(0, columns);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        try{
            Matrix.identity(rows, 0);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        try{
            Matrix.identity(0, 0);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        
    }
    
    @Test
    public void testCreateWithUniformRandomValues() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m;
        double value, sum = 0.0, sqrSum = 0.0;
        for(int k = 0; k < TIMES; k++){
            m = Matrix.createWithUniformRandomValues(rows, columns, 
                    MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
            //check correctness
            assertEquals(m.getRows(), rows);
            assertEquals(m.getColumns(), columns);
            
            for(int j = 0; j < columns; j++){
                for(int i = 0; i < rows; i++){
                    value = m.getElementAt(i, j);
                    
                    assertTrue(value >= MIN_RANDOM_VALUE);
                    assertTrue(value <= MAX_RANDOM_VALUE);
                    
                    sum += value;
                    sqrSum += value * value;
                }
            }
        }
        
        int numSamples = rows * columns * TIMES;
        double estimatedMeanValue = sum / (double)(numSamples);
        double estimatedVariance = (sqrSum - (double)numSamples * 
                estimatedMeanValue * estimatedMeanValue) / 
                ((double)numSamples - 1.0);
        
        //mean and variance of uniform distribution
        double meanValue = 0.5 * (MIN_RANDOM_VALUE + MAX_RANDOM_VALUE);
        double variance = (MAX_RANDOM_VALUE - MIN_RANDOM_VALUE) * 
                (MAX_RANDOM_VALUE - MIN_RANDOM_VALUE) / 12.0;
        
        //check correctness of results
        assertEquals(meanValue, estimatedMeanValue, 
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);
        
        //Force WrongSizeException
        try{
            Matrix.createWithUniformRandomValues(0, columns, MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        try{
            Matrix.createWithUniformRandomValues(rows, 0, MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        //Force IllegalArgumentException
        try{
            Matrix.createWithUniformRandomValues(rows, columns, 
                    MAX_RANDOM_VALUE, MIN_RANDOM_VALUE);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
    }
    
    @Test
    public void testCreateWithUniformRandomValues2() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m;
        double value, sum = 0.0, sqrSum = 0.0;
        for(int k = 0; k < TIMES; k++){
            m = Matrix.createWithUniformRandomValues(rows, columns, 
                    MIN_RANDOM_VALUE, MAX_RANDOM_VALUE, new SecureRandom());
            //check correctness
            assertEquals(m.getRows(), rows);
            assertEquals(m.getColumns(), columns);
            
            for(int j = 0; j < columns; j++){
                for(int i = 0; i < rows; i++){
                    value = m.getElementAt(i, j);
                    
                    assertTrue(value >= MIN_RANDOM_VALUE);
                    assertTrue(value <= MAX_RANDOM_VALUE);
                    
                    sum += value;
                    sqrSum += value * value;
                }
            }
        }
        
        int numSamples = rows * columns * TIMES;
        double estimatedMeanValue = sum / (double)(numSamples);
        double estimatedVariance = (sqrSum - (double)numSamples * 
                estimatedMeanValue * estimatedMeanValue) / 
                ((double)numSamples - 1.0);
        
        //mean and variance of uniform distribution
        double meanValue = 0.5 * (MIN_RANDOM_VALUE + MAX_RANDOM_VALUE);
        double variance = (MAX_RANDOM_VALUE - MIN_RANDOM_VALUE) * 
                (MAX_RANDOM_VALUE - MIN_RANDOM_VALUE) / 12.0;
        
        //check correctness of results
        assertEquals(meanValue, estimatedMeanValue, 
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);
        
        //Force WrongSizeException
        try{
            Matrix.createWithUniformRandomValues(0, columns, MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        try{
            Matrix.createWithUniformRandomValues(rows, 0, MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        //Force IllegalArgumentException
        try{
            Matrix.createWithUniformRandomValues(rows, columns, 
                    MAX_RANDOM_VALUE, MIN_RANDOM_VALUE);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        
    }    
    
    @Test
    public void testCreateWithGaussianRandomValues() throws WrongSizeException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int columns = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        
        Matrix m;
        double value, mean = 0.0, sqrSum = 0.0;
        int numSamples = rows * columns * TIMES;
        for(int k = 0; k < TIMES; k++){
            m = Matrix.createWithGaussianRandomValues(rows, columns, 
                    MEAN, STANDARD_DEVIATION);
            //check correctness
            assertEquals(m.getRows(), rows);
            assertEquals(m.getColumns(), columns);
            
            for(int j = 0; j < columns; j++){
                for(int i = 0; i < rows; i++){
                    value = m.getElementAt(i, j);
                    
                    mean += value / (double)numSamples;
                    sqrSum += value * value / (double)numSamples;
                }
            }
        }
        
        double standardDeviation = Math.sqrt(sqrSum - mean);
        
                
        //check correctness of results
        assertEquals(mean, MEAN, mean * RELATIVE_ERROR);
        assertEquals(standardDeviation, STANDARD_DEVIATION,
                standardDeviation * RELATIVE_ERROR);
        
        //Force WrongSizeException
        try{
            Matrix.createWithGaussianRandomValues(0, columns, MEAN, 
                    STANDARD_DEVIATION);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        try{
            Matrix.createWithGaussianRandomValues(rows, 0, MEAN, 
                    STANDARD_DEVIATION);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        try{
            Matrix.createWithGaussianRandomValues(rows, columns, 
                    MEAN, -STANDARD_DEVIATION);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
    }    
    
    @Test
    public void testDiagonal(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int length = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        
        double[] diagonal = new double[length];
        
        //fill diagonal with random values
        for(int i = 0; i < length; i++){
            diagonal[i] = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                    MAX_RANDOM_VALUE);
        }
        
        Matrix m = Matrix.diagonal(diagonal);
        
        //check correctness
        assertEquals(m.getRows(), length);
        assertEquals(m.getColumns(), length);
        
        for(int j = 0; j < length; j++){
            for(int i = 0; i < length; i++){
                if(i == j) assertEquals(m.getElementAt(i, j), diagonal[i], 0.0);
                else assertEquals(m.getElementAt(i, j), 0.0, 0.0);
            }
        }
    }
    
    @Test
    public void testNewFromArray() throws AlgebraException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int cols = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int length = rows * cols;
        
        double[] array = new double[length];
        randomizer.fill(array, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        //use default column order        
        Matrix m = Matrix.newFromArray(array);        
        double[] array2 = m.toArray();
        
        //check correctness
        assertEquals(m.getRows(), length);
        assertEquals(m.getColumns(), 1);
        assertArrayEquals(array, array2, 0.0);
        
        //use column order
        m = Matrix.newFromArray(array, true);
        array2 = m.toArray(true);
        
        //check correctness
        assertEquals(m.getRows(), length);
        assertEquals(m.getColumns(), 1);        
        assertArrayEquals(array, array2, 0.0);
        
        //use row order
        m = Matrix.newFromArray(array, false);
        array2 = m.toArray(false);
        
        //check correctness
        assertEquals(m.getRows(), 1);
        assertEquals(m.getColumns(), length);
        assertArrayEquals(array, array2, 0.0);        
    }
    
    @Test
    public void testFromArray() throws AlgebraException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);
        int cols = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
        int length = rows * cols;
        
        double[] array = new double[length];
        randomizer.fill(array, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        Matrix m = new Matrix(rows, cols);
        
        //use default column order
        m.fromArray(array);        
        double[] array2 = m.toArray();
        
        //check correctness
        assertEquals(m.getRows(), rows);
        assertEquals(m.getColumns(), cols);
        assertArrayEquals(array, array2, 0.0);
        
        //Force WrongSizeException
        try{
            m.fromArray(new double[length + 1]);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        
        
        //use column order
        m.fromArray(array, true);
        array2 = m.toArray(true);
        
        //check correctness
        assertEquals(m.getRows(), rows);
        assertEquals(m.getColumns(), cols);        
        assertArrayEquals(array, array2, 0.0);
        
        //Force WrongSizeException
        try{
            m.fromArray(new double[length + 1], true);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        
        
        //use row order
        m.fromArray(array, false);
        array2 = m.toArray(false);
        
        //check correctness
        assertEquals(m.getRows(), rows);
        assertEquals(m.getColumns(), cols);        
        assertArrayEquals(array, array2, 0.0);
        
        //Force WrongSizeException
        try{
            m.fromArray(new double[length + 1], false);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}        
    }
    
    @Test
    public void testSymmetrize() throws AlgebraException{
        int numValid = 0;
        for (int t = 0; t < TIMES; t++) {
            UniformRandomizer randomizer = new UniformRandomizer(new Random());
            int rows = randomizer.nextInt(MIN_ROWS, MAX_ROWS);

            Matrix symmetric = DecomposerHelper.getSymmetricMatrix(rows);

            Matrix nonSymmetric = new Matrix(rows, rows);
            nonSymmetric.copyFrom(symmetric);
            nonSymmetric.setElementAt(0, rows - 1,
                    nonSymmetric.getElementAt(0, rows - 1) + 1.0);


            //symmetrize
            Matrix symmetric2 = new Matrix(rows, rows);
            symmetric.symmetrize(symmetric2);

            Matrix nonSymmetric2 = new Matrix(rows, rows);
            nonSymmetric.symmetrize(nonSymmetric2);

            //check correctness
            if (!Utils.isSymmetric(symmetric)) {
                continue;
            }
            assertTrue(Utils.isSymmetric(symmetric));
            if (Utils.isSymmetric(nonSymmetric)) {
                continue;
            }
            assertFalse(Utils.isSymmetric(nonSymmetric));

            if (!Utils.isSymmetric(symmetric2)) {
                continue;
            }
            assertTrue(Utils.isSymmetric(symmetric2));
            if (!Utils.isSymmetric(nonSymmetric2)) {
                continue;
            }
            assertTrue(Utils.isSymmetric(nonSymmetric2));

            boolean failed = false;
            for (int i = 0; i < symmetric2.getColumns(); i++) {
                for (int j = 0; j < symmetric2.getRows(); j++) {
                    if (Math.abs(symmetric2.getElementAt(i, j) -
                            0.5 * (symmetric.getElementAt(j, i) + symmetric.getElementAt(j, i))) > ABSOLUTE_ERROR) {
                        failed = true;
                        break;
                    }
                    assertEquals(symmetric2.getElementAt(i, j),
                            0.5 * (symmetric.getElementAt(i, j) +
                                    symmetric.getElementAt(j, i)), ABSOLUTE_ERROR);
                }
            }

            if (failed) {
                continue;
            }

            for (int i = 0; i < nonSymmetric2.getColumns(); i++) {
                for (int j = 0; j < nonSymmetric2.getRows(); j++) {
                    if (Math.abs(nonSymmetric2.getElementAt(i, j) -
                            0.5 * (nonSymmetric.getElementAt(i, j) + nonSymmetric.getElementAt(j, i))) > ABSOLUTE_ERROR) {
                        failed = true;
                        break;
                    }
                    assertEquals(nonSymmetric2.getElementAt(i, j),
                            0.5 * (nonSymmetric.getElementAt(i, j) +
                                    nonSymmetric.getElementAt(j, i)), ABSOLUTE_ERROR);
                }
            }

            if (failed) {
                continue;
            }


            //Force WrongSizeException
            Matrix wrong = new Matrix(1, 2);
            try {
                wrong.symmetrize(wrong);
                fail("WrongSizeException expected but not thrown");
            } catch (WrongSizeException e) { }
            try {
                symmetric.symmetrize(wrong);
                fail("WrongSizeException expected but not thrown");
            } catch (WrongSizeException e) { }


            //symmetrize and return new
            Matrix symmetric3 = symmetric.symmetrizeAndReturnNew();
            Matrix nonSymmetric3 = nonSymmetric.symmetrizeAndReturnNew();

            //check correctness
            if (!Utils.isSymmetric(symmetric)) {
                continue;
            }
            assertTrue(Utils.isSymmetric(symmetric));
            if (Utils.isSymmetric(nonSymmetric)) {
                continue;
            }
            assertFalse(Utils.isSymmetric(nonSymmetric));

            if (!Utils.isSymmetric(symmetric3)) {
                continue;
            }
            assertTrue(Utils.isSymmetric(symmetric3));
            if (!Utils.isSymmetric(nonSymmetric3)) {
                continue;
            }
            assertTrue(Utils.isSymmetric(nonSymmetric3));

            //Force WrongSizeException
            try {
                wrong.symmetrizeAndReturnNew();
                fail("WrongSizeException expected but not thrown");
            } catch (WrongSizeException e) { }


            //symmetrize and update
            symmetric.symmetrize();
            nonSymmetric.symmetrize();

            //check correctness
            if (!Utils.isSymmetric(symmetric)) {
                continue;
            }
            assertTrue(Utils.isSymmetric(symmetric));
            if (!Utils.isSymmetric(nonSymmetric)) {
                continue;
            }
            assertTrue(Utils.isSymmetric(nonSymmetric));

            //Force WrongSizeException
            try {
                wrong.symmetrize();
                fail("WrongSizeException expected but not thrown");
            } catch (WrongSizeException e) { }

            numValid++;
            break;
        }

        assertTrue(numValid > 0);
    }
}
