/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.algebra.DecomposerException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 15, 2012
 */
package com.irurueta.algebra;

import static org.junit.Assert.assertNotNull;
import org.junit.*;

public class DecomposerExceptionTest {
    
    public DecomposerExceptionTest() {
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
        DecomposerException ex;
        assertNotNull(ex = new DecomposerException());
        
        ex = null;
        assertNotNull(ex = new DecomposerException("message"));
        
        ex = null;
        assertNotNull(ex = new DecomposerException(new Exception()));
        
        ex = null;
        assertNotNull(ex = new DecomposerException("message", new Exception()));        
    }
}
