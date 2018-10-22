/*
 * Copyright (C) 2012 Alberto Irurueta Carro (alberto@irurueta.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.irurueta.algebra;

/**
 * Enumerator indicating the possible methods available for matrix decomposition.
 */
public enum DecomposerType {
    /**
     * Defines LU decomposition, which decomposes a matrix into upper (U) and
     * lower (L) triangular matrices.
     */
    LU_DECOMPOSITION,
    
    /**
     * Defines QR decomposition, which decomposes a matrix into an orthogonal
     * matrix (Q) and an upper triangular matrix (R).
     */
    QR_DECOMPOSITION,
    
    /**
     * Defines Economy QR decomposition, which is a faster version of QR 
     * decomposition. Notiche that economy sized version only works for matrices
     * having rows &lt; columns, while Q is a rows-by-columns and R is 
     * columns-by-columns in size.
     */
    QR_ECONOMY_DECOMPOSITION,
    
    /**
     * Defines RQ decomposition, which decomposes a matrix into an upper 
     * diagonal matrix (R) and an orthogonal matrix (Q). RQ decomposition is
     * very similar to QR and it is related to it through transposition by
     * reversing order of factors R and Q respect to QR decomposition.
     */
    RQ_DECOMPOSITION,
    
    /**
     * Defines Cholesky decomposition, which decomposes a square symmetric and
     * positive definite matrix into a triangular factor (L) and itrs transposed
     * (L').
     */
    CHOLESKY_DECOMPOSITION,
    
    /**
     * Defines Singular Value DEcomposition, which decomposes any rectangular
     * matrix into 2 unary matrices (U, V) and 1 diagonal matrix containing
     * singular values (D).
     */
    SINGULAR_VALUE_DECOMPOSITION
}
