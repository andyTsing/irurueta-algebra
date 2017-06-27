/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.algebra.NotAvailableException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 15, 2012
 */
package com.irurueta.algebra;

import static org.junit.Assert.assertNotNull;
import org.junit.*;

public class NotAvailableExceptionTest {
    
    public NotAvailableExceptionTest() {
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
        NotAvailableException ex;
        assertNotNull(ex = new NotAvailableException());
        
        ex = null;
        assertNotNull(ex = new NotAvailableException("message"));
        
        ex = null;
        assertNotNull(ex = new NotAvailableException(new Exception()));
        
        ex = null;
        assertNotNull(ex = new NotAvailableException("message", 
                new Exception()));        
    }
}
