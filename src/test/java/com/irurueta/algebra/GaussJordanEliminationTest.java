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
package com.irurueta.algebra;

import com.irurueta.statistics.UniformRandomizer;
import org.junit.*;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

public class GaussJordanEliminationTest {
    
    public static final double MIN_RANDOM_VALUE = 0.0;
    public static final double MAX_RANDOM_VALUE = 50.0;
    
    public static final int MIN_COLUMNS = 1;
    public static final int MAX_COLUMNS = 50;

    public static final double ABSOLUTE_ERROR = 1e-6;
    
    public GaussJordanEliminationTest() { }
    
    @BeforeClass
    public static void setUpClass() { }
    
    @AfterClass
    public static void tearDownClass() { }
    
    @Before
    public void setUp() { }
    
    @After
    public void tearDown() { }

    @Test
    public void testProcessMatrix() throws WrongSizeException, 
            SingularMatrixException, RankDeficientMatrixException, 
            DecomposerException {
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
        try {
            GaussJordanElimination.process(a, b);
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
        
        //different rows
        a = new Matrix(rows, rows);
        b = new Matrix(rows + 1, colsB);        
        try {
            GaussJordanElimination.process(a, b);
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
        
        //Force SingularMatrixException
        a = new Matrix(rows, rows);
        b = Matrix.createWithUniformRandomValues(rows, colsB, MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        try {
            GaussJordanElimination.process(a, b);
            fail("SingularMatrixException expected but not thrown");
        } catch (SingularMatrixException ignore) { }
    }
    
    @Test
    public void testProcessArray() throws WrongSizeException, 
            SingularMatrixException, RankDeficientMatrixException, 
            DecomposerException {
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
        try {
            GaussJordanElimination.process(a, b);
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
        
        //different lengths
        a = new Matrix(rows, rows);
        b = new double[rows + 1];
        try {
            GaussJordanElimination.process(a, b);
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
        
        //Force SingularMatrixException
        a = new Matrix(rows, rows);
        b = new double[rows];
        try {
            GaussJordanElimination.process(a, b);
            fail("SingularMatrixException expected but not thrown");
        } catch (SingularMatrixException ignore) { }
    }
    
    @Test
    public void testInverse() throws WrongSizeException, 
            SingularMatrixException, RankDeficientMatrixException, 
            DecomposerException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        int rows = randomizer.nextInt(MIN_COLUMNS + 5, MAX_COLUMNS + 5);
                
        Matrix a, a2, invA;
        
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
        try {
            GaussJordanElimination.inverse(a);
            fail("WrongSizeException expected but not thrown");
        } catch (WrongSizeException ignore) { }
        
        //Force SingularMatrixException
        a = new Matrix(rows, rows);
        try {
            GaussJordanElimination.inverse(a);
            fail("SingularMatrixException expected but not thrown");
        } catch (SingularMatrixException ignore) { }
        
    }
}
