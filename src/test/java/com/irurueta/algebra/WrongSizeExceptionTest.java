/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.algebra.WrongSizeException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 15, 2012
 */
package com.irurueta.algebra;

import static org.junit.Assert.assertNotNull;
import org.junit.*;

public class WrongSizeExceptionTest {
    
    public WrongSizeExceptionTest() {
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
        WrongSizeException ex;
        assertNotNull(ex = new WrongSizeException());
        
        ex = null;
        assertNotNull(ex = new WrongSizeException("message"));
        
        ex = null;
        assertNotNull(ex = new WrongSizeException(new Exception()));
        
        ex = null;
        assertNotNull(ex = new WrongSizeException("message", new Exception()));        
    }
}
