/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.algebra.LockedException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 15, 2012
 */
package com.irurueta.algebra;

import static org.junit.Assert.assertNotNull;
import org.junit.*;

public class LockedExceptionTest {
    
    public LockedExceptionTest() {
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
        LockedException ex;
        assertNotNull(ex = new LockedException());
        
        ex = null;
        assertNotNull(ex = new LockedException("message"));
        
        ex = null;
        assertNotNull(ex = new LockedException(new Exception()));
        
        ex = null;
        assertNotNull(ex = new LockedException("message", new Exception()));        
    }
}
