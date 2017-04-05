/*
 * File:    MatrixUtil.java
 * Authors: Charlie Beck, Phoebe Hughes, Tiffany Lam, Jenny Lin
 * Date:    April 10, 2017
 * Project: 3
 */

/**
 * This utility class for 2D matrices
 */
public class MatrixUtil {

    /**
     * Returns the sum of the two input matrices
     * @param A the first matrix
     * @param B the second matrix
     * @return Sum of the two input matrices
     */
    public static int[][] addMatrices(int[][] A, int[][] B){
        int[][] result = new int[A.length][A[0].length];
        for(int i = 0; i < A.length; i++){
            for(int j = 0; j < A[0].length; j++){
                result[i][j] = A[i][j] + B[i][j];
            }
        }
        return result;
    }

    /**
     * Returns the difference of the two input matrices
     * @param A the first matrix
     * @param B the second matrix
     * @return Difference of the two input matrices (A - B)
     */
    public static int[][] subtractMatrices(int[][] A, int[][] B){
        int[][] result = new int[A.length][A[0].length];
        for(int i = 0; i < A.length; i++){
            for(int j = 0; j < A[0].length; j++){
                result[i][j] = A[i][j] - B[i][j];
            }
        }
        return result;
    }

    /**
     * Prints out the matrix
     *
     * @param matrix the given matrix
     */
    public static void printMatrix(int[][] matrix){
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
