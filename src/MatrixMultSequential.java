/*
 * File:    MatrixMultSequential.java
 * Authors: Charlie Beck, Phoebe Hughes, Tiffany Lam, Jenny Lin
 * Date:    April 10, 2017
 * Project: 3
 */

/**
 * This class computes the product of multiplying to matrices.
 *
 * The algorithm is naive, going through each element of each matrix sequentially
 * to calculate the product
 */
public class MatrixMultSequential {
    /**
     * Calculates the product of multiplying two matrices
     *
     * @param A the first matrix
     * @param B the second matrix
     * @return  the result of multiplying the two matrices
     */
    public int[][] computeMatrixMult(int[][] A, int[][] B) {
        int[][] result = new int[A.length][B[0].length];
        for(int i = 0; i < A.length; i++) {
            for(int j = 0; j < B[0].length; j++) {
                result[i][j] = 0;
                for(int k = 0; k < B.length; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return result;
    }

    /**
     * Used for testing the mult method
     *
     * @param args
     */
    public static void main(String[] args) {
        int[][] A = {{1, 2},
                     {2, 1}};

        int[][] B = {{1, 2},
                     {2, 1}};

        int[][] result = new MatrixMultSequential().computeMatrixMult(A, B);

        for(int i = 0; i < result.length; i++) {
            for(int j = 0; j < result[i].length; j++) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }
    }
}
