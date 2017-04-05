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
     * @param   A the first matrix
     * @param   B the second matrix
     * @return  the result of multiplying the two matrices
     */
    public static int[][] computeMatrixMult(int[][] A, int[][] B) {
        int[][] result = new int[A.length][A.length];
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
     * @param args command line arges
     */
    public static void main(String[] args) {
        int[][] A = new int[1500][1000];
        int[][] B = new int[1000][1500];

        for(int i = 0; i < A.length; i++){
            for(int j = 0; j < A[0].length; j++){
                A[i][j] = i + j;
                B[j][i] = j + i;
            }
        }

        Timer.start();
        int[][] result = computeMatrixMult(A, B);
        Timer.stop();

//        MatrixUtil.printMatrix(result);
        System.out.println("Total time: " + Timer.getRuntime());
    }
}
