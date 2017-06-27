/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.algebra.NonSymmetricPositiveDefiniteMatrixException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 */
package com.irurueta.algebra;

import static org.junit.Assert.assertNotNull;
import org.junit.*;

public class NonSymmetricPositiveDefiniteMatrixExceptionTest {
    
    public NonSymmetricPositiveDefiniteMatrixExceptionTest(){}
    
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
        NonSymmetricPositiveDefiniteMatrixException ex;
        assertNotNull(ex = new NonSymmetricPositiveDefiniteMatrixException());
        
        ex = null;
        assertNotNull(ex = new NonSymmetricPositiveDefiniteMatrixException(
                "message"));
        
        ex = null;
        assertNotNull(ex = new NonSymmetricPositiveDefiniteMatrixException(
                new Exception()));
        
        ex = null;
        assertNotNull(ex = new NonSymmetricPositiveDefiniteMatrixException(
                "message", new Exception()));        
    }
}
