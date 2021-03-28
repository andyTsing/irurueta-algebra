/*
 * Copyright (C) 2021 Alberto Irurueta Carro (alberto@irurueta.com)
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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NormComputerTest {

    @Test
    public void testCreate() {
        NormComputer normComputer = NormComputer.create(NormType.FROBENIUS_NORM);
        assertEquals(NormType.FROBENIUS_NORM, normComputer.getNormType());

        normComputer = NormComputer.create(NormType.ONE_NORM);
        assertEquals(NormType.ONE_NORM, normComputer.getNormType());

        normComputer = NormComputer.create(NormType.INFINITY_NORM);
        assertEquals(NormType.INFINITY_NORM, normComputer.getNormType());

        normComputer = NormComputer.create();
        assertEquals(NormComputer.DEFAULT_NORM_TYPE, normComputer.getNormType());
    }
}
