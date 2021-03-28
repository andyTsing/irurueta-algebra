/*
 * Copyright (C) 2015 Alberto Irurueta Carro (alberto@irurueta.com)
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
 * Computes Gauss-Jordan elimination for provided matrix using full pivoting,
 * which provides greater stability.
 * Gauss-Jordan elimination can be used to compute matrix inversion or to
 * solve linear systems of equations of the form A * x = b, where Gauss-Jordan
 * elimination both inverts matrix A and finds solution x at the same time.
 */
public class GaussJordanElimination {

    /**
     * Constructor.
     * Prevents instantiation.
     */
    private GaussJordanElimination() {
    }

    /**
     * Computes Gauss-Jordan elimination by attempting to solve linear system
     * of equations a * x = b. This method computes inverse of matrix a, and
     * modifies provided matrix so that its inverse is stored in it after
     * execution of this method. Likewise, this method modifies b so that
     * solution x is stored on it after execution of this method.
     * This method can only be used on squared a matrices.
     *
     * @param a linear system of equations matrix. Will contain its inverse
     *          after execution of this method.
     * @param b linear system of equations parameters. Will contain the solution
     *          after execution of this method. If null is provided, solution is not
     *          stored but matrix inverse is computed anyway. Each column of b is
     *          considered a new linear system of equations and its solution x is
     *          computed on the corresponding column of b.
     * @throws SingularMatrixException if provided matrix a is found to be
     *                                 singular.
     * @throws WrongSizeException      if provided matrix a is not square.
     */
    public static void process(final Matrix a, final Matrix b)
            throws SingularMatrixException, WrongSizeException {
        if (a.getRows() != a.getColumns()) {
            throw new WrongSizeException();
        }
        if (b != null && b.getRows() != a.getRows()) {
            throw new WrongSizeException();
        }

        int i;
        int icol = 0;
        int irow = 0;
        int j;
        int k;
        int l;
        int ll;
        final int n = a.getRows();
        final int m = b != null ? b.getColumns() : 0;

        double big;
        double dum;
        double pivinv;
        double value;
        final int[] indxc = new int[n];
        final int[] indxr = new int[n];
        final int[] ipiv = new int[n];

        for (j = 0; j < n; j++) {
            ipiv[j] = 0;
        }
        for (i = 0; i < n; i++) {
            big = 0.0;
            for (j = 0; j < n; j++) {
                if (ipiv[j] != 1) {
                    for (k = 0; k < n; k++) {
                        if (ipiv[k] == 0) {
                            value = Math.abs(a.getElementAt(j, k));
                            if (value >= big) {
                                big = value;
                                irow = j;
                                icol = k;
                            }
                        }
                    }
                }
            }
            ++(ipiv[icol]);
            if (irow != icol) {
                for (l = 0; l < n; l++) {
                    swap(a.getBuffer(), a.getBuffer(),
                            a.getIndex(irow, l),
                            a.getIndex(icol, l));
                }
                for (l = 0; l < m; l++) {
                    swap(b.getBuffer(), b.getBuffer(),
                            b.getIndex(irow, l),
                            b.getIndex(icol, l));
                }
            }
            indxr[i] = irow;
            indxc[i] = icol;
            value = a.getElementAt(icol, icol);
            if (value == 0.0) {
                throw new SingularMatrixException();
            }
            pivinv = 1.0 / value;
            a.setElementAt(icol, icol, 1.0);
            for (l = 0; l < n; l++) {
                a.setElementAt(icol, l, a.getElementAt(icol, l) * pivinv);
            }
            for (l = 0; l < m; l++) {
                b.setElementAt(icol, l, b.getElementAt(icol, l) * pivinv);
            }
            for (ll = 0; ll < n; ll++) {
                if (ll != icol) {
                    dum = a.getElementAt(ll, icol);
                    a.setElementAt(ll, icol, 0.0);
                    for (l = 0; l < n; l++) {
                        a.setElementAt(ll, l, a.getElementAt(ll, l) -
                                a.getElementAt(icol, l) * dum);
                    }
                    for (l = 0; l < m; l++) {
                        b.setElementAt(ll, l, b.getElementAt(ll, l) -
                                b.getElementAt(icol, l) * dum);
                    }
                }
            }
        }
        for (l = n - 1; l >= 0; l--) {
            if (indxr[l] != indxc[l]) {
                for (k = 0; k < n; k++) {
                    swap(a.getBuffer(), a.getBuffer(),
                            a.getIndex(k, indxr[l]), a.getIndex(k, indxc[l]));
                }
            }
        }
    }

    /**
     * Computes Gauss-Jordan elimination by attempting to solve linear system
     * of equations a * x = b. This method computes inverse of matrix a, and
     * modifies provided matrix so that its inverse is stored in it after
     * execution of this method. Likewise, this method modifies b so that
     * solution x is stored on it after execution of this method.
     * This method can only be used on squared a matrices.
     *
     * @param a linear system of equations matrix. Will contain its inverse
     *          after execution of this method.
     * @param b linear system of equations parameters. Will contain the solution
     *          after execution of this method. If null is provided, solution is not
     *          stored but matrix inverse is computed anyway.
     * @throws SingularMatrixException if provided matrix a is found to be
     *                                 singular.
     * @throws WrongSizeException      if provided matrix a is not square.
     */
    public static void process(final Matrix a, final double[] b)
            throws SingularMatrixException, WrongSizeException {
        final Matrix mb = b != null ? Matrix.newFromArray(b) : null;
        process(a, mb);
        if (mb != null) {
            final double[] buffer = mb.getBuffer();
            System.arraycopy(buffer, 0, b, 0, b.length);
        }
    }

    /**
     * Computes inverse of matrix a. No solution of a linear system of equations
     * is computed. This method modifies provided matrix storing the inverse
     * on it after execution of this method
     *
     * @param a matrix to be inverted.
     * @throws SingularMatrixException if provided matrix is found to be
     *                                 singular
     * @throws WrongSizeException      if provided matrix is not square
     */
    public static void inverse(final Matrix a)
            throws SingularMatrixException, WrongSizeException {
        process(a, (Matrix) null);
    }

    /**
     * Swaps values in arrays at provided positions
     *
     * @param array1 1st array
     * @param array2 2nd array
     * @param pos1   1st position to be swapped
     * @param pos2   2nd position to be swapped
     */
    private static void swap(final double[] array1, final double[] array2,
                             final int pos1, final int pos2) {
        final double value1 = array1[pos1];
        final double value2 = array2[pos2];

        array1[pos1] = value2;
        array2[pos1] = value1;
    }
}
