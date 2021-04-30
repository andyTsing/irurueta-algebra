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

public abstract class Decomposer {

    /**
     * Reference to input matrix to be decomposed.
     */
    protected Matrix inputMatrix;

    /**
     * Member indicating whether this decomposer instance is locked or not.
     * When locked, attempting to change parameters of this instance might
     * raise a com.algebra.LockedException.
     */
    protected boolean locked;

    /**
     * Constructor of this class.
     */
    protected Decomposer() {
        this.inputMatrix = null;
        locked = false;
    }

    /**
     * Constructor of this class.
     *
     * @param inputMatrix Reference to input matrix to be decomposed.
     */
    protected Decomposer(final Matrix inputMatrix) {
        this.inputMatrix = inputMatrix;
        locked = false;
    }

    /**
     * Returns decomposer type of this instance. Decomposer type determines the
     * algorithm being used for matrix decomposition. Depending on this type,
     * after calling decompose() method, different object or matrices will be
     * available for retrieval.
     *
     * @return Decomposer type of this instance.
     */
    public abstract DecomposerType getDecomposerType();

    /**
     * Returns a reference to input matrix to be decomposed.
     *
     * @return Reference to input matrix to be decomposed.
     */
    public Matrix getInputMatrix() {
        return inputMatrix;
    }

    /**
     * Sets reference to input matrix to be decomposed.
     *
     * @param inputMatrix Reference to input matrix to be decomposed.
     * @throws LockedException Exception thrown if attempting to call this
     *                         method while this instance remains locked.
     */
    public void setInputMatrix(final Matrix inputMatrix) throws LockedException {
        if (isLocked()) {
            throw new LockedException();
        }
        this.inputMatrix = inputMatrix;
    }

    /**
     * Returns boolean indicating whether this instance is ready for
     * decomposition computation.
     * Attempting to call decompose() method when this instance is not ready
     * will result on a NotReadyException being thrown.
     *
     * @return Boolean indicating whether this instance is ready for
     * decomposition computation.
     */
    public boolean isReady() {
        return inputMatrix != null;
    }

    /**
     * Returns boolean indicating whether this instance is locked or not. When
     * locked, attempting to change some parameters of this instance might raise
     * a LockedException.
     *
     * @return Boolean indicating whether this instance is locked or not.
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Returns boolean indicating whether decomposition has been computed and
     * results can be retrieved.
     * Attempting to retrieve decomposition results when not available, will
     * probably raise a NotAvailableException.
     *
     * @return Boolean indicating whether decomposition has been computed and
     * results can be retrieved.
     */
    public abstract boolean isDecompositionAvailable();

    /**
     * This method computes matrix decomposition for each decomposer type.
     * After execution of this method, different objects might be generated
     * depending on decomposition type and decomposition results availability.
     * Note: during execution of this method, this instance might become locked
     * depending on subclass implementation.
     *
     * @throws NotReadyException   Exception thrown if attempting to call this
     *                             method when this instance is not yet ready for decomposition, usually
     *                             because some data or parameter is missing.
     * @throws LockedException     Exception thrown if this decomposer is already
     *                             locked before calling this method. Notice that this method will actually
     *                             lock this instance while it is being executed.
     * @throws DecomposerException Exception thrown if for any other reason
     *                             decomposition fails while being executed, like when convergence or
     *                             results cannot be obtained, etc.
     */
    public abstract void decompose() throws NotReadyException, LockedException,
            DecomposerException;
}
