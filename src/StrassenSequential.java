/*
 * File:    StrassenSequential.java
 * Authors: Charlie Beck, Phoebe Hughes, Tiffany Lam, Jenny Lin
 * Date:    April 10, 2017
 * Project: 3
 */

/**
 * This class computes the product of multiplying to matrices.
 *
 * The algorithm uses Strassen's Method,
 * recursively dividing the matrices into submatrices,
 * and calculating product arrays to calculate the final product
 * of the two given matrices
 */
public class StrassenSequential {

    /**
     * Calculates the product of multiplying two matrices
     *
     * @param A the first matrix
     * @param B the second matrix
     * @return  the result of multiplying the two matrices
     */
    public int[][] computeMatrixMult(int[][] A, int[][] B) {

        int[][] result = new int[A.length][A.length];

        /**
         * The size of the sub-matrices
         */
        int matrixSize = calcSize(A.length, A[0].length)/2;

        if (A.length == 1) {
            result[0][0] = A[0][0] * B[0][0];
        }
        else {
            int[][] a11 = new int[matrixSize][matrixSize];
            int[][] a12 = new int[matrixSize][matrixSize];
            int[][] a21 = new int[matrixSize][matrixSize];
            int[][] a22 = new int[matrixSize][matrixSize];
            int[][] b11 = new int[matrixSize][matrixSize];
            int[][] b12 = new int[matrixSize][matrixSize];
            int[][] b21 = new int[matrixSize][matrixSize];
            int[][] b22 = new int[matrixSize][matrixSize];

            // padding with 0s
            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    a11[i][j] = (i < A.length && j < A[0].length) ? A[i][j] : 0;
                    a12[i][j] = (i < A.length && j + matrixSize < A[0].length) ?
                            A[i][j + matrixSize] : 0;
                    a21[i][j] = (i + matrixSize < A.length && j < A[0].length) ?
                            A[i + matrixSize][j] : 0;
                    a22[i][j] = (i + matrixSize < A.length && j + matrixSize < A[0].length) ?
                            A[i + matrixSize][j + matrixSize] : 0;
                    b11[i][j] = (i < B.length && j < B[0].length) ? B[i][j] : 0;
                    b12[i][j] = (i < B.length && j + matrixSize < B[0].length) ?
                            B[i][j + matrixSize] : 0;
                    b21[i][j] = (i + matrixSize < B.length && j < B[0].length) ?
                            B[i + matrixSize][j] : 0;
                    b22[i][j] = (i + matrixSize < B.length && j + matrixSize < B[0].length) ?
                            B[i + matrixSize][j + matrixSize] : 0;
                }
            }
            printMatrix(a11);
            printMatrix(a12);
            printMatrix(a21);
            printMatrix(a22);
            printMatrix(b11);
            printMatrix(b12);
            printMatrix(b21);
            printMatrix(b22);
        }

        return result;
    }

    /**
     * The smallest 2^n value that is
     * greater than the largest dimension of either of the given two arrays
     *
     * @param rows the number of rows in the matrix
     * @param cols the number of cols in the matrix
     * @return the size of the matrix
     */
    public int calcSize(int rows, int cols){
        int newRows, newCols;
        double rowExp = Math.log(rows)/Math.log(2);
        double colExp = Math.log(cols)/Math.log(2);
        newRows = (rowExp == (int)rowExp)? rows : (int)Math.pow(2, (int)(rowExp+1));
        newCols = (colExp == (int)colExp)? cols : (int)Math.pow(2, (int)(colExp+1));
        return Math.max(newRows, newCols);
    }

    /**
     * Prints out the matrix
     *
     * @param matrix the given matrix
     */
    public void printMatrix(int[][] matrix){
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Used to test Strassen's method
     *
     * @param args
     */
    public static void main(String[] args){
        StrassenSequential mult = new StrassenSequential();
        int[][] A = {{1, 2},
                     {2, 1},
                     {3, 4}};

        int[][] B = {{1, 2, 4},
                     {2, 1, 2}};

        mult.printMatrix(A);
        mult.printMatrix(B);

        mult.computeMatrixMult(A, B);
    }
}
