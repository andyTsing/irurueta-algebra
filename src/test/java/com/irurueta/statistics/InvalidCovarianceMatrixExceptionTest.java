/**
 * @file
 * This file contains unit tests for
 * com.irurueta.statistics.InvalidCovarianceMatrixException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date December 30, 2015
 */
package com.irurueta.statistics;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class InvalidCovarianceMatrixExceptionTest {
    
    public InvalidCovarianceMatrixExceptionTest() {}
    
    @BeforeClass
    public static void setUpClass() {}
    
    @AfterClass
    public static void tearDownClass() {}
    
    @Before
    public void setUp() {}
    
    @After
    public void tearDown() {}
    
    @Test
    public void testConstructor(){
        InvalidCovarianceMatrixException ex;
        assertNotNull(ex = new InvalidCovarianceMatrixException());
        
        ex = null;
        assertNotNull(ex = new InvalidCovarianceMatrixException("message"));
        
        ex = null;
        assertNotNull(ex = new InvalidCovarianceMatrixException(
                new Exception()));
        
        ex = null;
        assertNotNull(ex = new InvalidCovarianceMatrixException("message", 
                new Exception()));        
    }
}
