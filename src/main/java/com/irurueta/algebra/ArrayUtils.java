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

import java.util.Arrays;

/**
 * Class containing utility methods for common operations with arrays of values.
 */
public class ArrayUtils {

    /**
     * Constructor.
     */
    private ArrayUtils() {
    }

    /**
     * Internal method that multiplied by scalar provided input array without
     * comparing length of input array and result array.
     *
     * @param inputArray Array to be multiplied.
     * @param scalar     Scalar used for multiplication.
     * @param result     Array where result is stored.
     * @see #multiplyByScalar(double[], double, double[])
     */
    private static void internalMultiplyByScalar(
            final double[] inputArray, final double scalar, final double[] result) {
        for (int i = 0; i < inputArray.length; ++i) {
            result[i] = scalar * inputArray[i];
        }
    }

    /**
     * Multiplies values in provided input array by provided scalar value
     * and stores the result in provided result array.
     *
     * @param inputArray Array to be multiplied.
     * @param scalar     Scalar used for multiplication.
     * @param result     Array where result is stored.
     * @throws IllegalArgumentException Thrown if inputArray length and result
     *                                  array length are not equal.
     */
    public static void multiplyByScalar(
            final double[] inputArray, final double scalar, final double[] result) {
        if (inputArray.length != result.length) {
            throw new IllegalArgumentException();
        }
        internalMultiplyByScalar(inputArray, scalar, result);
    }

    /**
     * Multiplies values in provided array by provided scalar value and returns
     * the result in a new array.
     *
     * @param inputArray Array to be multiplied.
     * @param scalar     Scalar used for multiplication.
     * @return Result obtained after multiplying input array by provided scalar
     * value.
     */
    public static double[] multiplyByScalarAndReturnNew(
            final double[] inputArray, final double scalar) {
        final double[] result = new double[inputArray.length];
        internalMultiplyByScalar(inputArray, scalar, result);
        return result;
    }

    /**
     * Sums provided operands arrays and stores the result in provided result
     * array (i.e. result = firstOperand + secondOperand). Summation is done in
     * an element by element basis.
     * Provided array result must be initialized.
     * All arrays must have the same length.
     * This method does not check array lengths.
     *
     * @param firstOperand  First operand.
     * @param secondOperand Second operand.
     * @param result        Result of summation.
     */
    private static void internalSum(
            final double[] firstOperand, final double[] secondOperand,
            final double[] result) {
        for (int i = 0; i < firstOperand.length; i++) {
            result[i] = firstOperand[i] + secondOperand[i];
        }
    }

    /**
     * Sums provided operands arrays and stores the result in provided result
     * array (i.e. result = firstOperand + secondOperand). Summation is done
     * in an element by element basis.
     * Provided array result must be initialized.
     * All arrays must have the same length.
     *
     * @param firstOperand  First operand.
     * @param secondOperand Second operand.
     * @param result        Result of summation.
     * @throws IllegalArgumentException Raised if not all arrays have the same
     *                                  length.
     */
    public static void sum(
            final double[] firstOperand, final double[] secondOperand,
            final double[] result) {
        if (firstOperand.length != secondOperand.length ||
                firstOperand.length != result.length) {
            throw new IllegalArgumentException();
        }
        internalSum(firstOperand, secondOperand, result);
    }

    /**
     * Sums provided operands and returns the result as a new array instance.
     * Summation is done in an element by element basis.
     *
     * @param firstOperand  First operand.
     * @param secondOperand Second operand.
     * @return Sum of first and second operands.
     * @throws IllegalArgumentException Raised if first and second operands
     *                                  arrays don't have the same length.
     */
    public static double[] sumAndReturnNew(
            final double[] firstOperand, final double[] secondOperand) {
        if (firstOperand.length != secondOperand.length) {
            throw new IllegalArgumentException();
        }

        final double[] result = new double[firstOperand.length];
        internalSum(firstOperand, secondOperand, result);
        return result;
    }

    /**
     * Subtracts provided operands arrays and stores the result in provided
     * result array (i.e. result = firstOperand - secondOperand). Subtraction is
     * done in an element by element basis.
     * Provided array result must be initialized.
     * All arrays must have the same length.
     * This method does not check array lengths.
     *
     * @param firstOperand  First operand.
     * @param secondOperand Second operand.
     * @param result        Result of subtraction.
     */
    private static void internalSubtract(
            final double[] firstOperand, final double[] secondOperand,
            final double[] result) {
        for (int i = 0; i < firstOperand.length; i++) {
            result[i] = firstOperand[i] - secondOperand[i];
        }
    }

    /**
     * Subtracts provided operands arrays and stores the result in provided
     * result array (i.e. result = firstOperand - secondOperand). Subtraction is
     * done in an element by element basis.
     * Provided array result must be initialized.
     * All arrays must have the same length.
     *
     * @param firstOperand  First operand.
     * @param secondOperand Second operand.
     * @param result        Result of subtraction.
     * @throws IllegalArgumentException Raised if not all arrays have the same
     *                                  length.
     */
    public static void subtract(
            final double[] firstOperand, final double[] secondOperand,
            final double[] result) {
        if (firstOperand.length != secondOperand.length ||
                firstOperand.length != result.length) {
            throw new IllegalArgumentException();
        }
        internalSubtract(firstOperand, secondOperand, result);
    }

    /**
     * Subtracts provided operands and returns the result as a new array
     * instance.
     * Subtraction is done in an element by element basis.
     *
     * @param firstOperand  First operand
     * @param secondOperand Second operand
     * @return Subtraction of first and second operands
     * @throws IllegalArgumentException Raised if first and second operands
     *                                  arrays don't have the same length
     */
    public static double[] subtractAndReturnNew(
            final double[] firstOperand, final double[] secondOperand) {
        if (firstOperand.length != secondOperand.length) {
            throw new IllegalArgumentException();
        }

        final double[] result = new double[firstOperand.length];
        internalSubtract(firstOperand, secondOperand, result);
        return result;
    }

    /**
     * Computes the dot product of provided arrays as the sum of the product
     * of the elements of both arrays.
     *
     * @param firstOperand  First operand.
     * @param secondOperand Second operand.
     * @return Dot product.
     * @throws IllegalArgumentException Raised if first and second operands
     *                                  arrays don't have the same length.
     */
    public static double dotProduct(
            final double[] firstOperand, final double[] secondOperand) {
        if (firstOperand.length != secondOperand.length) {
            throw new IllegalArgumentException(
                    "both operands must have same length");
        }

        double result = 0.0;
        for (int i = 0; i < firstOperand.length; i++) {
            result += firstOperand[i] * secondOperand[i];
        }
        return result;
    }

    /**
     * Computes the dot product of provided arrays as the sum of the product of
     * the elements of both arrays.
     *
     * @param firstOperand   first operand.
     * @param secondOperand  second operand.
     * @param jacobianFirst  matrix where jacobian of first operand will be
     *                       stored. Must be a column matrix having the same number of rows as the
     *                       first operand length.
     * @param jacobianSecond matrix where jacobian of second operand will be
     *                       stored. Must be a column matrix having the same number of rows as the
     *                       second operand length.
     * @return dot product.
     * @throws IllegalArgumentException if first and second operands don't have
     *                                  the same length or if jacobian matrices are not column vectors having
     *                                  the same length as their respective operands.
     */
    public static double dotProduct(
            final double[] firstOperand, final double[] secondOperand,
            final Matrix jacobianFirst, final Matrix jacobianSecond) {
        if (jacobianFirst != null &&
                (jacobianFirst.getRows() != 1 ||
                        jacobianFirst.getColumns() != firstOperand.length)) {
            throw new IllegalArgumentException("jacobian first must be a " +
                    "row vector having the same number of columns as " +
                    "first operand length");
        }
        if (jacobianSecond != null &&
                (jacobianSecond.getRows() != 1 ||
                        jacobianSecond.getColumns() != secondOperand.length)) {
            throw new IllegalArgumentException("jacobian second must be a " +
                    "row vector havnig the same number of columns as " +
                    "second operand length");
        }

        if (jacobianFirst != null) {
            jacobianFirst.setSubmatrix(0, 0, 0, firstOperand.length - 1,
                    firstOperand);
        }
        if (jacobianSecond != null) {
            jacobianSecond.setSubmatrix(0, 0, 0, secondOperand.length - 1,
                    secondOperand);
        }

        return dotProduct(firstOperand, secondOperand);
    }

    /**
     * Computes the angle between two vectors.
     * The angle is defined between 0 and PI.
     *
     * @param firstOperand  first operand.
     * @param secondOperand second operand.
     * @return angle between arrays.
     * @throws IllegalArgumentException if first and second operands don't have
     *                                  the same length.
     */
    public static double angle(final double[] firstOperand, final double[] secondOperand) {
        final double norm1 = Utils.normF(firstOperand);
        final double norm2 = Utils.normF(secondOperand);
        return Math.acos(Math.min(dotProduct(firstOperand, secondOperand) / norm1 / norm2, 1.0));
    }

    //The same for Complex arrays

    /**
     * Internal method that multiplied by scalar provided input array without
     * comparing length of input array and result array.
     *
     * @param inputArray Array to be multiplied.
     * @param scalar     Scalar used for multiplication.
     * @param result     Array where result is stored.
     * @see #multiplyByScalar(double[], double, double[])
     */
    private static void internalMultiplyByScalar(
            final Complex[] inputArray, final double scalar, final Complex[] result) {
        for (int i = 0; i < inputArray.length; ++i) {
            result[i].setReal(inputArray[i].getReal() * scalar);
            result[i].setImaginary(inputArray[i].getImaginary() * scalar);
        }
    }

    /**
     * Multiplies values in provided input array by provided scalar value
     * and stores the result in provided result array.
     *
     * @param inputArray Array to be multiplied.
     * @param scalar     Scalar used for multiplication.
     * @param result     Array where result is stored.
     * @throws IllegalArgumentException Thrown if inputArray length and result
     *                                  array length are not equal.
     */
    public static void multiplyByScalar(
            final Complex[] inputArray, final double scalar, final Complex[] result) {
        if (inputArray.length != result.length) {
            throw new IllegalArgumentException();
        }
        internalMultiplyByScalar(inputArray, scalar, result);
    }

    /**
     * Multiplies values in provided array by provided scalar value and returns
     * the result in a new array.
     *
     * @param inputArray Array to be multiplied.
     * @param scalar     Scalar used for multiplication.
     * @return Result obtained after multiplying input array by provided scalar
     * value.
     */
    public static Complex[] multiplyByScalarAndReturnNew(
            final Complex[] inputArray, final double scalar) {
        final Complex[] result = new Complex[inputArray.length];
        // instantiate Complex instances in result array containing values of
        // provided array multiplied by scalar value
        for (int i = 0; i < inputArray.length; i++) {
            result[i] = new Complex(inputArray[i].getReal() * scalar,
                    inputArray[i].getImaginary() * scalar);
        }
        return result;
    }

    /**
     * Sums provided operands arrays and stores the result in provided result
     * array (i.e. result = firstOperand + secondOperand). Summation is done in
     * an element by element basis.
     * Provided array result must be initialized.
     * All arrays must have the same length.
     * This method does not check array lengths.
     *
     * @param firstOperand  First operand.
     * @param secondOperand Second operand.
     * @param result        Result of summation.
     */
    private static void internalSum(
            final Complex[] firstOperand, final Complex[] secondOperand,
            final Complex[] result) {
        for (int i = 0; i < firstOperand.length; i++) {
            result[i].setReal(firstOperand[i].getReal() +
                    secondOperand[i].getReal());
            result[i].setImaginary(firstOperand[i].getImaginary() +
                    secondOperand[i].getImaginary());
        }
    }

    /**
     * Sums provided operands arrays and stores the result in provided result
     * array (i.e. result = firstOperand + secondOperand). Summation is done
     * in an element by element basis.
     * Provided array result must be initialized.
     * All arrays must have the same length.
     *
     * @param firstOperand  First operand.
     * @param secondOperand Second operand.
     * @param result        Result of summation.
     * @throws IllegalArgumentException Raised if not all arrays have the same
     *                                  length.
     */
    public static void sum(
            final Complex[] firstOperand, final Complex[] secondOperand,
            final Complex[] result) {
        if (firstOperand.length != secondOperand.length ||
                firstOperand.length != result.length) {
            throw new IllegalArgumentException();
        }
        internalSum(firstOperand, secondOperand, result);
    }

    /**
     * Sums provided operands and returns the result as a new array instance.
     * Summation is done in an element by element basis
     *
     * @param firstOperand  First operand
     * @param secondOperand Second operand
     * @return Sum of first and second operands
     * @throws IllegalArgumentException Raised if first and second operands
     *                                  arrays don't have the same length
     */
    public static Complex[] sumAndReturnNew(
            final Complex[] firstOperand, final Complex[] secondOperand) {
        if (firstOperand.length != secondOperand.length) {
            throw new IllegalArgumentException();
        }

        final Complex[] result = new Complex[firstOperand.length];
        // initialize each element of result matrix
        for (int i = 0; i < firstOperand.length; i++)
            result[i] = firstOperand[i].addAndReturnNew(secondOperand[i]);
        return result;
    }

    /**
     * Subtracts provided operands arrays and stores the result in provided
     * result array (i.e. result = firstOperand - secondOperand). Subtraction is
     * done in an element by element basis
     * Provided array result must be initialized.
     * All arrays must have the same length
     * This method does not check array lengths
     *
     * @param firstOperand  First operand
     * @param secondOperand Second operand
     * @param result        Result of subtraction
     */
    private static void internalSubtract(
            final Complex[] firstOperand, final Complex[] secondOperand,
            final Complex[] result) {
        for (int i = 0; i < firstOperand.length; i++) {
            result[i].setReal(firstOperand[i].getReal() -
                    secondOperand[i].getReal());
            result[i].setImaginary(firstOperand[i].getImaginary() -
                    secondOperand[i].getImaginary());
        }
    }

    /**
     * Subtracts provided operands arrays and stores the result in provided
     * result array (i.e. result = firstOperand - secondOperand). Subtraction is
     * done in an element by element basis
     * Provided array result must be initialized.
     * All arrays must have the same length
     *
     * @param firstOperand  First operand
     * @param secondOperand Second operand
     * @param result        Result of subtraction
     * @throws IllegalArgumentException Raised if not all arrays have the same
     *                                  length
     */
    public static void subtract(
            final Complex[] firstOperand, final Complex[] secondOperand,
            final Complex[] result) {
        if (firstOperand.length != secondOperand.length ||
                firstOperand.length != result.length) {
            throw new IllegalArgumentException();
        }
        internalSubtract(firstOperand, secondOperand, result);
    }

    /**
     * Subtracts provided operands and returns the result as a new array
     * instance.
     * Subtraction is done in an element by element basis
     *
     * @param firstOperand  First operand
     * @param secondOperand Second operand
     * @return Subtraction of first and second operands
     * @throws IllegalArgumentException Raised if first and second operands
     *                                  arrays don't have the same length
     */
    public static Complex[] subtractAndReturnNew(
            final Complex[] firstOperand, final Complex[] secondOperand) {
        if (firstOperand.length != secondOperand.length) {
            throw new IllegalArgumentException();
        }

        final Complex[] result = new Complex[firstOperand.length];
        // initialize each element of result matrix
        for (int i = 0; i < firstOperand.length; i++) {
            result[i] = firstOperand[i].subtractAndReturnNew(secondOperand[i]);
        }
        return result;
    }

    /**
     * Computes the dot product of provided arrays as the sum of the product
     * of the elements of both arrays
     *
     * @param firstOperand  First operand
     * @param secondOperand Second operand
     * @return Dot product
     * @throws IllegalArgumentException Raised if first and second operands
     *                                  arrays don't have the same length
     */
    public static Complex dotProduct(
            final Complex[] firstOperand, final Complex[] secondOperand) {
        if (firstOperand.length != secondOperand.length) {
            throw new IllegalArgumentException();
        }

        final Complex result = new Complex(0.0);
        for (int i = 0; i < firstOperand.length; i++) {
            result.add(firstOperand[i].multiplyAndReturnNew(secondOperand[i]));
        }
        return result;
    }

    /**
     * Normalizes provided array and computes corresponding jacobian.
     *
     * @param v        array to be normalized.
     * @param result   array where result of normalized array will be stored.
     * @param jacobian matrix where jacobian will be stored.
     * @throws IllegalArgumentException if provided arrays don't have the same
     *                                  length or if provided jacobian is not NxN, where N is length of arrays.
     */
    public static void normalize(final double[] v, final double[] result, final Matrix jacobian) {
        final double s = v.length;

        if (s != result.length) {
            throw new IllegalArgumentException(
                    "both arrays must have the same length");
        }

        if (jacobian != null && (jacobian.getRows() != s ||
                jacobian.getColumns() != s)) {
            throw new IllegalArgumentException(
                    "provided jacobian is not NxN where N is length of v");
        }

        final double n2 = dotProduct(v, v);
        final double n = Math.sqrt(n2);

        if (jacobian != null) {
            try {
                final double n3 = n * n2;

                // VN_v = (n2*eye(s) - v*v') / n3
                Matrix.identity(jacobian);
                jacobian.multiplyByScalar(n2);
                jacobian.subtract(Matrix.newFromArray(v, true).
                        multiplyAndReturnNew(Matrix.newFromArray(v, false)));
                if (n3 != 0.0) {
                    jacobian.multiplyByScalar(1.0 / n3);
                } else {
                    jacobian.initialize(Double.MAX_VALUE);
                }
            } catch (final WrongSizeException ignore) {
                // never thrown
            }
        }

        if (n != 0.0) {
            internalMultiplyByScalar(v, 1.0 / n, result);
        } else {
            Arrays.fill(result, Double.MAX_VALUE);
        }
    }

    /**
     * Normalizes provided array and computes corresponding jacobian.
     *
     * @param v        array to be normalized.
     * @param jacobian matrix where jacobian will be stored.
     * @return a new array instance containing normalized array.
     * @throws IllegalArgumentException if provided jacobian is not NxN, where N
     *                                  is length of arrays.
     */
    public static double[] normalizeAndReturnNew(final double[] v, final Matrix jacobian) {
        final double[] result = new double[v.length];
        normalize(v, result, jacobian);
        return result;
    }

    /**
     * Normalizes provided array, updates its values and computes corresponding
     * jacobian.
     *
     * @param v        array to be normalized and updated.
     * @param jacobian matrix where jacobian will be stored.
     * @throws IllegalArgumentException if provided jacobian is not NxN, where N
     *                                  is length of arrays.
     */
    public static void normalize(final double[] v, final Matrix jacobian) {
        normalize(v, v, jacobian);
    }

    /**
     * Normalizes provided array.
     *
     * @param v      array to be normalized.
     * @param result array where result of normalized array will be stored.
     * @throws IllegalArgumentException if provided arrays don't have the same
     *                                  length.
     */
    public static void normalize(final double[] v, final double[] result) {
        normalize(v, result, null);
    }

    /**
     * Normalizes provided array.
     *
     * @param v array to be normalized.
     * @return a new array instance containing normalized array.
     */
    public static double[] normalizeAndReturnNew(final double[] v) {
        return normalizeAndReturnNew(v, null);
    }

    /**
     * Normalizes provided array and updates its values.
     *
     * @param v array to be normalized and updated.
     */
    public static void normalize(final double[] v) {
        normalize(v, (Matrix) null);
    }

    /**
     * Reverses the order of elements in the array.
     *
     * @param v      array to be reversed.
     * @param result instance where results will be stored.
     * @throws IllegalArgumentException if provided arrays don't have the same
     *                                  length.
     */
    public static void reverse(final double[] v, final double[] result) {

        final int length = v.length;

        if (result.length != length) {
            throw new IllegalArgumentException();
        }

        final int halfLength = length / 2;
        double tmp;
        for (int i = 0, j = length - 1; i < halfLength; i++, j--) {
            tmp = v[i];
            result[i] = v[j];
            result[j] = tmp;
        }

        if (length % 2 != 0) {
            // if length is odd, copy central value
            result[halfLength] = v[halfLength];
        }
    }

    /**
     * Reverses provided array. This method updates provided array to contain
     * its reversed values.
     *
     * @param v array to be reversed.
     */
    public static void reverse(final double[] v) {
        reverse(v, v);
    }

    /**
     * Returns a new array containing provided array having its elements in
     * reversed order.
     *
     * @param v array to be reversed.
     * @return a new instance containing reversed array.
     */
    public static double[] reverseAndReturnNew(final double[] v) {
        final double[] result = new double[v.length];
        reverse(v, result);
        return result;
    }

    /**
     * Reverses the order of elements in the array.
     *
     * @param v      array to be reversed.
     * @param result instance where results will be stored.
     * @throws IllegalArgumentException if provided arrays don't have the same
     *                                  length.
     */
    public static void reverse(final Complex[] v, final Complex[] result) {

        final int length = v.length;

        if (result.length != length) {
            throw new IllegalArgumentException();
        }

        final int halfLength = length / 2;
        Complex tmp;
        if (v != result) {
            // for different instances, copy values
            for (int i = 0, j = length - 1; i < halfLength; i++, j--) {
                tmp = new Complex(v[i]);
                result[i] = new Complex(v[j]);
                result[j] = tmp;
            }

            if (length % 2 != 0) {
                // if length is odd, copy central value
                result[halfLength] = new Complex(v[halfLength]);
            }
        } else {
            // for same instances, rearrange values
            for (int i = 0, j = length - 1; i < halfLength; i++, j--) {
                tmp = v[i];
                result[i] = v[j];
                result[j] = tmp;
            }

            if (length % 2 != 0) {
                // if length is odd, copy central value
                result[halfLength] = v[halfLength];
            }
        }
    }

    /**
     * Reverses provided array. This method updates provided array to contain
     * its reversed values.
     *
     * @param v array to be reversed.
     */
    public static void reverse(final Complex[] v) {
        reverse(v, v);
    }

    /**
     * Returns a new array containing provided array having its elements in
     * reversed order.
     *
     * @param v array to be reversed.
     * @return a new instance containing reversed array.
     */
    public static Complex[] reverseAndReturnNew(final Complex[] v) {
        final Complex[] result = new Complex[v.length];
        reverse(v, result);
        return result;
    }

    /**
     * Computes the squared root of each element of provided array and sets the
     * result into provided result array.
     *
     * @param v      input array to compute the squared root of each element.
     * @param result array where results will be stored.
     * @throws IllegalArgumentException if provided arrays don't have the same
     *                                  length.
     */
    public static void sqrt(final double[] v, final double[] result) {
        final int length = v.length;

        if (result.length != length) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < length; i++) {
            result[i] = Math.sqrt(v[i]);
        }
    }

    /**
     * Computes the squared root of each element of provided array and returns
     * the result as a new array.
     *
     * @param v input array to compute the squared root of each element.
     * @return a new array containing the squared root of each element of input
     * array.
     */
    public static double[] sqrtAndReturnNew(final double[] v) {
        final double[] result = new double[v.length];
        sqrt(v, result);
        return result;
    }

    /**
     * Updates provided array by setting on each element its squared root.
     *
     * @param v input array to be updated with its squared root elements.
     */
    public static void sqrt(final double[] v) {
        sqrt(v, v);
    }

    /**
     * Finds the minimum value into provided array.
     *
     * @param v   array where minimum must be found.
     * @param pos position where minimum was found. Position will be stored at
     *            position zero of the array, if provided.
     * @return minimum value.
     */
    public static double min(final double[] v, final int[] pos) {
        final int length = v.length;
        double min = Double.MAX_VALUE;
        int foundPos = -1;
        for (int i = 0; i < length; i++) {
            if (v[i] < min) {
                min = v[i];
                foundPos = i;
            }
        }

        if (pos != null && pos.length > 0) {
            pos[0] = foundPos;
        }

        return min;
    }

    /**
     * Finds the minimum value into provided array.
     *
     * @param v array where minimum must be found.
     * @return minimum value.
     */
    public static double min(final double[] v) {
        return min(v, null);
    }

    /**
     * Finds the maximum value into provided array.
     *
     * @param v   array where maximum must be found.
     * @param pos position where maximum was found. Position will be stored at
     *            position zero of the array, if provided.
     * @return maximum value.
     */
    public static double max(final double[] v, final int[] pos) {
        final int length = v.length;
        double max = -Double.MAX_VALUE;
        int foundPos = -1;
        for (int i = 0; i < length; i++) {
            if (v[i] > max) {
                max = v[i];
                foundPos = i;
            }
        }

        if (pos != null && pos.length > 0) {
            pos[0] = foundPos;
        }

        return max;
    }

    /**
     * Finds the maximum value into provided array.
     *
     * @param v array where maximum must be found.
     * @return maximum value.
     */
    public static double max(final double[] v) {
        return max(v, null);
    }

    /**
     * Finds the minimum and maximum values into provided array and finds their
     * positions.
     *
     * @param v      array where minimum and maximum must be found.
     * @param result array of length 2 containing found minimum and maximum
     *               values at positions 0 and 1 respectively.
     * @param pos    array of length 2 containing positions where minimum and
     *               maximum where found in v array. Position 0 will contain minimum position,
     *               position 1 will contain maximum position.
     * @throws IllegalArgumentException if provided result or pos array don't
     *                                  have length 2.
     */
    public static void minMax(final double[] v, final double[] result, final int[] pos) {
        if (result.length != 2) {
            throw new IllegalArgumentException("result must have length 2");
        }
        if (pos != null && pos.length != 2) {
            throw new IllegalArgumentException("pos must have length 2");
        }

        final int length = v.length;
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        int minPos = -1;
        int maxPos = -1;
        for (int i = 0; i < length; i++) {
            if (v[i] < min) {
                min = v[i];
                minPos = i;
            }
            if (v[i] > max) {
                max = v[i];
                maxPos = i;
            }
        }

        result[0] = min;
        result[1] = max;
        if (pos != null) {
            pos[0] = minPos;
            pos[1] = maxPos;
        }
    }
}
