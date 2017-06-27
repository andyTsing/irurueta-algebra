/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.algebra.AlgebraException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 15, 2012
 */
package com.irurueta.algebra;

import static org.junit.Assert.assertNotNull;
import org.junit.*;

public class AlgebraExceptionTest {
    
    public AlgebraExceptionTest() {
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
        AlgebraException ex;
        assertNotNull(ex = new AlgebraException());
        
        ex = null;
        assertNotNull(ex = new AlgebraException("message"));
        
        ex = null;
        assertNotNull(ex = new AlgebraException(new Exception()));
        
        ex = null;
        assertNotNull(ex = new AlgebraException("message", new Exception()));
        
    }
}
