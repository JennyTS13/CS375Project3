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

        //The size of the sub-matrices
        int matrixSize = calcSize(A.length, A[0].length)/2;

        if (matrixSize <= 8) {
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

            int[][] c11, c12, c21, c22;
            int[][] m1, m2, m3, m4, m5, m6, m7;

            if(A.length == matrixSize && B.length == matrixSize){
                for (int i = 0; i < matrixSize; i++) {
                    for (int j = 0; j < matrixSize; j++) {
                        a11[i][j] = A[i][j];
                        a12[i][j] = A[i][j + matrixSize];
                        a21[i][j] = A[i + matrixSize][j];
                        a22[i][j] = A[i + matrixSize][j + matrixSize];
                        b11[i][j] = B[i][j];
                        b12[i][j] = B[i][j + matrixSize];
                        b21[i][j] = B[i + matrixSize][j];
                        b22[i][j] = B[i + matrixSize][j + matrixSize];
                    }
                }
            }
            // padding with 0s
            else {
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
            }
            m1 = computeMatrixMult(MatrixUtil.addMatrices(a11, a22),
                    MatrixUtil.addMatrices(b11, b22));
            m2 = computeMatrixMult(MatrixUtil.addMatrices(a21, a22), b11);
            m3 = computeMatrixMult(a11, MatrixUtil.subtractMatrices(b12, b22));
            m4 = computeMatrixMult(a22, MatrixUtil.subtractMatrices(b21, b11));
            m5 = computeMatrixMult(MatrixUtil.addMatrices(a11, a12), b22);
            m6 = computeMatrixMult(MatrixUtil.subtractMatrices(a21, a11),
                    MatrixUtil.addMatrices(b11, b12));
            m7 = computeMatrixMult(MatrixUtil.subtractMatrices(a12, a22),
                    MatrixUtil.addMatrices(b21, b22));

            c11 = MatrixUtil.addMatrices(MatrixUtil.subtractMatrices(
                    MatrixUtil.addMatrices(m1, m4), m5), m7);
            c12 = MatrixUtil.addMatrices(m3, m5);
            c21 = MatrixUtil.addMatrices(m2, m4);
            c22 = MatrixUtil.addMatrices(MatrixUtil.addMatrices(
                    MatrixUtil.subtractMatrices(m1, m2), m3), m6);

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

        MatrixUtil.printMatrix(A);
        MatrixUtil.printMatrix(B);

        MatrixUtil.printMatrix(computeMatrixMult(A, B));
    }
}
