/**
 * @file
 * This file contains unit tests for
 * com.irurueta.algebra.GaussJordanElimination
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date May 20, 2015
 */
package com.irurueta.algebra;

import com.irurueta.statistics.UniformRandomizer;
import java.util.Arrays;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussJordanEliminationTest {
    
    public static final double MIN_RANDOM_VALUE = 0.0;
    public static final double MAX_RANDOM_VALUE = 50.0;
    
    public static final int MIN_COLUMNS = 1;
    public static final int MAX_COLUMNS = 50;

    public static final double ABSOLUTE_ERROR = 1e-6;
    
    public GaussJordanEliminationTest() {}
    
    @BeforeClass
    public static void setUpClass() {}
    
    @AfterClass
    public static void tearDownClass() {}
    
    @Before
    public void setUp() {}
    
    @After
    public void tearDown() {}

    @Test
    public void testProcessMatrix() throws WrongSizeException, 
            SingularMatrixException, RankDeficientMatrixException, 
            DecomposerException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_COLUMNS + 5, MAX_COLUMNS + 5);
        int colsB = randomizer.nextInt(MIN_COLUMNS, MAX_COLUMNS);
                
        Matrix a, b, a2, b2, invA, x;
        
        //Test for non-singular square matrix
        a = DecomposerHelper.getNonSingularMatrixInstance(rows, rows);
        b = Matrix.createWithUniformRandomValues(rows, colsB, MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        
        a2 = a.clone();
        b2 = b.clone();
        
        GaussJordanElimination.process(a2, b2);
        
        //check correctness
        invA = Utils.inverse(a);
        x = Utils.solve(a, b);
        
        assertTrue(a2.equals(invA, ABSOLUTE_ERROR));
        assertTrue(b2.equals(x, ABSOLUTE_ERROR));
        
        //Force WrongSizeException
        
        //non square matrix a
        a = new Matrix(rows, rows + 1);
        b = new Matrix(rows, colsB);        
        try{
            GaussJordanElimination.process(a, b);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        
        //different rows
        a = new Matrix(rows, rows);
        b = new Matrix(rows + 1, colsB);        
        try{
            GaussJordanElimination.process(a, b);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}     
        
        //Force SingularMatrixException
        a = new Matrix(rows, rows);
        b = Matrix.createWithUniformRandomValues(rows, colsB, MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        try{
            GaussJordanElimination.process(a, b);
            fail("SingularMatrixException expected but not thrown");
        }catch(SingularMatrixException e){}             
    }
    
    @Test
    public void testProcessArray() throws WrongSizeException, 
            SingularMatrixException, RankDeficientMatrixException, 
            DecomposerException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_COLUMNS + 5, MAX_COLUMNS + 5);
                
        Matrix a, a2, invA;
        double[] b, b2, x;
        
        //Test for non-singular square matrix
        a = DecomposerHelper.getNonSingularMatrixInstance(rows, rows);
        b = new double[rows];
        randomizer.fill(b, MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        a2 = a.clone();
        b2 = Arrays.copyOf(b, rows);
        
        GaussJordanElimination.process(a2, b2);
        
        //check correctness
        invA = Utils.inverse(a);
        x = Utils.solve(a, b);
        
        assertTrue(a2.equals(invA, ABSOLUTE_ERROR));
        assertArrayEquals(b2, x, ABSOLUTE_ERROR);
        
        //Force WrongSizeException
        
        //non square matrix a
        a = new Matrix(rows, rows + 1);
        b = new double[rows];
        try{
            GaussJordanElimination.process(a, b);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        
        //different lengths
        a = new Matrix(rows, rows);
        b = new double[rows + 1];
        try{
            GaussJordanElimination.process(a, b);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}     
        
        //Force SingularMatrixException
        a = new Matrix(rows, rows);
        b = new double[rows];
        try{
            GaussJordanElimination.process(a, b);
            fail("SingularMatrixException expected but not thrown");
        }catch(SingularMatrixException e){}             
    }
    
    @Test
    public void testInverse() throws WrongSizeException, 
            SingularMatrixException, RankDeficientMatrixException, 
            DecomposerException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_COLUMNS + 5, MAX_COLUMNS + 5);
                
        Matrix a, a2, invA;
        double[] b, b2, x;
        
        //Test for non-singular square matrix
        a = DecomposerHelper.getNonSingularMatrixInstance(rows, rows);
        
        a2 = a.clone();
        
        GaussJordanElimination.inverse(a2);
        
        //check correctness
        invA = Utils.inverse(a);
        
        assertTrue(a2.equals(invA, ABSOLUTE_ERROR));
        
        //Force WrongSizeException
        
        //non square matrix a
        a = new Matrix(rows, rows + 1);
        try{
            GaussJordanElimination.inverse(a);
            fail("WrongSizeException expected but not thrown");
        }catch(WrongSizeException e){}
        
        //Force SingularMatrixException
        a = new Matrix(rows, rows);
        try{
            GaussJordanElimination.inverse(a);
            fail("SingularMatrixException expected but not thrown");
        }catch(SingularMatrixException e){}             
        
    }
}
