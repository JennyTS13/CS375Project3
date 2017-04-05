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
     *
     * @param   A the first matrix
     * @param   B the second matrix
     * @return  Sum of the two input matrices
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
     *
     * @param   A the first matrix
     * @param   B the second matrix
     * @return  Difference of the two input matrices (A - B)
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
     * Returns the product of the two input matrices
     *
     * @param   A the first matrix
     * @param   B the second matrix
     * @return  Product of the two input matrices (A * B)
     */
    public static int[][] multMatrices(int[][] A, int[][] B) {
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

    /**
     * The smallest 2^n value that is
     * greater than the largest dimension of either of the given two arrays
     *
     * @param   rows the number of rows in the matrix
     * @param   cols the number of cols in the matrix
     * @return  the size of the matrix
     *          that satisfies the 2^n size requirement of Strassen's
     */
    public static int calcSize(int rows, int cols){
        int newRows, newCols;
        double rowExp = Math.log(rows)/Math.log(2);
        double colExp = Math.log(cols)/Math.log(2);
        newRows = (rowExp == (int)rowExp)? rows : (int)Math.pow(2, (int)(rowExp+1));
        newCols = (colExp == (int)colExp)? cols : (int)Math.pow(2, (int)(colExp+1));
        return Math.max(newRows, newCols);
    }
}
