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
 * Enumerator defining all possible ways of computing norms for matrices and
 * arrays.
 */
public enum NormType {
    /**
     * Defines Frobenius norm type.
     */
    FROBENIUS_NORM,
    
    /**
     * Defines one norm, which is the maximum column sum on a matrix.
     */
    ONE_NORM,
    
    /**
     * Defines Infinity norm type, which is the maximum row sum on a matrix.
     */
    INFINITY_NORM
}
