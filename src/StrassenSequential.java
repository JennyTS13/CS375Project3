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
    public static int[][] computeMatrixMult(int[][] A, int[][] B) {

        int[][] result = new int[A.length][A.length];

        /**
         * The size of the sub-matrices
         */
        int matrixSize = calcSize(A.length, A[0].length)/2;

        if (A.length == 1) {
            result = MatrixMultSequential.computeMatrixMult(A, B);
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
            int[][] c11 = new int[matrixSize][matrixSize];
            int[][] c12 = new int[matrixSize][matrixSize];
            int[][] c21 = new int[matrixSize][matrixSize];
            int[][] c22 = new int[matrixSize][matrixSize];
            int[][] m1 = new int[matrixSize][matrixSize];
            int[][] m2 = new int[matrixSize][matrixSize];
            int[][] m3 = new int[matrixSize][matrixSize];
            int[][] m4 = new int[matrixSize][matrixSize];
            int[][] m5 = new int[matrixSize][matrixSize];
            int[][] m6 = new int[matrixSize][matrixSize];
            int[][] m7 = new int[matrixSize][matrixSize];

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
            m1 = MatrixMultSequential.
                    computeMatrixMult(addMatrices(a11, a22), addMatrices(b11, b22));
            m2 = MatrixMultSequential.computeMatrixMult(addMatrices(a21, a22), b11);
            m3 = MatrixMultSequential.computeMatrixMult(a11, subtractMatrices(b12, b22));
            m4 = MatrixMultSequential.computeMatrixMult(a22, subtractMatrices(b21, b11));
            m5 = MatrixMultSequential.computeMatrixMult(addMatrices(a11, a12), b22);
            m6 = MatrixMultSequential.
                    computeMatrixMult(subtractMatrices(a21, a11), addMatrices(b11, b12));
            m7 = MatrixMultSequential.
                    computeMatrixMult(subtractMatrices(a12, a22), addMatrices(b21, b22));

            c11 = addMatrices(subtractMatrices(addMatrices(m1, m4), m5), m7);
            c12 = addMatrices(m3, m5);
            c21 = addMatrices(m2, m4);
            c22 = addMatrices(addMatrices(subtractMatrices(m1, m2), m3), m6);

            for(int i = 0; i < result.length; i++){
                for(int j = 0; j < result[0].length; j++){
                    if(i < matrixSize) {
                        if(j < matrixSize) {
                            result[i][j] = c11[i][j];
                        }
                        else{
                            result[i][j] = c12[i][j-matrixSize];
                        }
                    }
                    else {
                        if(j < matrixSize) {
                            result[i][j] = c21[i-matrixSize][j];
                        }
                        else{
                            result[i][j] = c22[i-matrixSize][j-matrixSize];
                        }
                    }
                }
            }
        }

        return result;
    }

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
     * The smallest 2^n value that is
     * greater than the largest dimension of either of the given two arrays
     *
     * @param rows the number of rows in the matrix
     * @param cols the number of cols in the matrix
     * @return the size of the matrix
     *         that satisfies the 2^n size requirement of Strassen's
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
     * Used to test Strassen's method
     *
     * @param args
     */
    public static void main(String[] args){
        int[][] A = {{1, 2},
                     {2, 1},
                     {3, 4}};

        int[][] B = {{1, 2, 4},
                     {2, 1, 2}};

        printMatrix(A);
        printMatrix(B);

        printMatrix(computeMatrixMult(A, B));
    }
}
