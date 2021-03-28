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
 * Exception thrown if some decomposer algorithm cannot converge
 */
public class NoConvergenceException extends DecomposerException {

    /**
     * Constructor.
     */
    public NoConvergenceException() {
        super();
    }

    /**
     * Constructor with String containing message.
     *
     * @param message Message indicating the cause of the exception.
     */
    public NoConvergenceException(final String message) {
        super(message);
    }

    /**
     * Constructor with message and cause.
     *
     * @param message Message describing the cause of the exception.
     * @param cause   Instance containing the cause of the exception.
     */
    public NoConvergenceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with cause.
     *
     * @param cause Instance containing the cause of the exception.
     */
    public NoConvergenceException(final Throwable cause) {
        super(cause);
    }
}
