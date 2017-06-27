/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.algebra.RankDeficientMatrixException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 17, 2012
 */
package com.irurueta.algebra;

import static org.junit.Assert.assertNotNull;
import org.junit.*;

public class RankDeficientMatrixExceptionTest {
    
    public RankDeficientMatrixExceptionTest() {
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
        RankDeficientMatrixException ex;
        assertNotNull(ex = new RankDeficientMatrixException());
        
        ex = null;
        assertNotNull(ex = new RankDeficientMatrixException(
                "message"));
        
        ex = null;
        assertNotNull(ex = new RankDeficientMatrixException(
                new Exception()));
        
        ex = null;
        assertNotNull(ex = new RankDeficientMatrixException(
                "message", new Exception()));        
    }    
}
