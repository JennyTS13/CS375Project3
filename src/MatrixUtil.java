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

    /**
     * Return 4 2D matrices in the form of a 3D array of the 4 quadrants of the input
     * Assumption: input matrix is a square matrix with even dimensions
     *
     * @param   A 2D matrix to divide into fourths
     * @return  3D array holding the 4 quarters of the input matrix
     */
    public static int[][][] subDivideMatrix(int[][] A){
        int subMatrixSize = A.length/2;
        int [][][] subMatrices = new int[4][subMatrixSize][subMatrixSize];

        for (int i = 0; i < subMatrixSize; i++) {
            for (int j = 0; j < subMatrixSize; j++) {
                subMatrices[0][i][j] = A[i][j];
                subMatrices[1][i][j] = A[i][j + subMatrixSize];
                subMatrices[2][i][j] = A[i + subMatrixSize][j];
                subMatrices[3][i][j] = A[i + subMatrixSize][j + subMatrixSize];
            }
        }
        return subMatrices;
    }

    /**
     * Return matrix padded with 0's at the end of the input
     * until we've reach the desired dimension (dimension x dimension matrix)
     *
     * @param   A 2D matrix to padd with 0's
     * @param   dimension integer of desired matrix dimension
     * @return  2D matrix of the input matrix padded with 0's
     */
    public static int[][] padMatrixZeroes(int[][] A, int dimension){
        int[][] result = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                result[i][j] = (i < A.length && j < A[0].length) ? A[i][j] : 0;
            }
        }
        return result;
    }

    /**
     * Returns a 2D matrix of the specified dimension made up of values from the
     * 4 input matrices.
     * @param   a00 2D matrix to copy into final matrix
     * @param   a01 2D matrix to copy into final matrix
     * @param   a10 2D matrix to copy into final matrix
     * @param   a11 2D matrix to copy into final matrix
     * @param   dimension integer of desired matrix dimension
     * @return  2D matrix of the 4 matrices joined together,
     *          cutting off at specified dimension
     */
    public static int[][] joinMatrices(int[][] a00, int[][] a01, int[][] a10, int[][] a11,
                                       int dimension) {
        int[][] result = new int[dimension][dimension];
        int subMatrixSize = a00.length;
        for(int i = 0; i < result.length; i++) {
            for(int j = 0; j < result[0].length; j++) {
                if(i < subMatrixSize) {
                    if(j < subMatrixSize) {
                        result[i][j] = a00[i][j];
                    }
                    else {
                        result[i][j] = a01[i][j-subMatrixSize];
                    }
                }
                else {
                    if(j < subMatrixSize) {
                        result[i][j] = a10[i-subMatrixSize][j];
                    }
                    else {
                        result[i][j] = a11[i-subMatrixSize][j-subMatrixSize];
                    }
                }
            }
        }
        return result;
    }
}
