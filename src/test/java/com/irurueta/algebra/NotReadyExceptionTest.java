/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.algebra.NotReadyException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 15, 2012
 */
package com.irurueta.algebra;

import static org.junit.Assert.assertNotNull;
import org.junit.*;

public class NotReadyExceptionTest {
    
    public NotReadyExceptionTest() {
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
        NotReadyException ex;
        assertNotNull(ex = new NotReadyException());
        
        ex = null;
        assertNotNull(ex = new NotReadyException("message"));
        
        ex = null;
        assertNotNull(ex = new NotReadyException(new Exception()));
        
        ex = null;
        assertNotNull(ex = new NotReadyException("message", new Exception()));        
    }
}
