/*
 * File:    MatrixMultSequential.java
 * Authors: Charlie Beck, Phoebe Hughes, Tiffany Lam, Jenny Lin
 * Date:    April 10, 2017
 * Project: 3
 */

/**
 * Multiplies two matrices using the standard form of multiplication.
 */
public class MatrixMultSequential implements MatrixMult {

    /**
     * Computes the result of multiplying two 2D arrays
     *
     * @param   A the first 2D array
     * @param   B the second 2D array
     * @return  the resulting multiplication matrix
     */
    public int[][] computeMatrixMult(int[][] A, int[][] B){

        return MatrixUtil.multMatrices(A, B);
    }

}
