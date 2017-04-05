/*
 * File:    MatrixMult.java
 * Authors: Charlie Beck, Phoebe Hughes, Tiffany Lam, Jenny Lin
 * Date:    April 10, 2017
 * Project: 3
 */

/**
 * An interface used for matrix multiplication
 */
public interface MatrixMult {

    /**
     * Computes the result of multiplying two 2D arrays
     *
     * @param   A the first 2D array
     * @param   B the second 2D array
     * @return  the resulting multiplication matrix
     */
    public int[][] computeMatrixMult(int[][] A, int[][] B);
}
