/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.algebra.NoConvergeException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 21, 2012
 */
package com.irurueta.algebra;

import org.junit.*;
import static org.junit.Assert.*;

public class NoConvergenceExceptionTest {
    
    public NoConvergenceExceptionTest() {
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
        NoConvergenceException ex;
        assertNotNull(ex = new NoConvergenceException());
        
        ex = null;
        assertNotNull(ex = new NoConvergenceException("message"));
        
        ex = null;
        assertNotNull(ex = new NoConvergenceException(new Exception()));
        
        ex = null;
        assertNotNull(ex = new NoConvergenceException("message", 
                new Exception()));        
    }    
}
