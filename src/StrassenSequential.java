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
        int submatrixSize = calcSize(A.length, A[0].length)/2;

        if (submatrixSize <= 100) {
            result = MatrixMultSequential.computeMatrixMult(A, B);
        }
        else {
            int[][] a00 = new int[submatrixSize][submatrixSize];
            int[][] a01 = new int[submatrixSize][submatrixSize];
            int[][] a10 = new int[submatrixSize][submatrixSize];
            int[][] a11 = new int[submatrixSize][submatrixSize];
            int[][] b00 = new int[submatrixSize][submatrixSize];
            int[][] b01 = new int[submatrixSize][submatrixSize];
            int[][] b10 = new int[submatrixSize][submatrixSize];
            int[][] b11 = new int[submatrixSize][submatrixSize];

            int[][] c00, c01, c10, c11;
            int[][] m1, m2, m3, m4, m5, m6, m7;

            if(A.length == submatrixSize && B.length == submatrixSize){
                for (int i = 0; i < submatrixSize; i++) {
                    for (int j = 0; j < submatrixSize; j++) {
                        a00[i][j] = A[i][j];
                        a01[i][j] = A[i][j + submatrixSize];
                        a10[i][j] = A[i + submatrixSize][j];
                        a11[i][j] = A[i + submatrixSize][j + submatrixSize];
                        b00[i][j] = B[i][j];
                        b01[i][j] = B[i][j + submatrixSize];
                        b10[i][j] = B[i + submatrixSize][j];
                        b11[i][j] = B[i + submatrixSize][j + submatrixSize];
                    }
                }
            }
            // padding with 0s
            else {
                for (int i = 0; i < submatrixSize; i++) {
                    for (int j = 0; j < submatrixSize; j++) {
                        a00[i][j] = (i < A.length && j < A[0].length) ? A[i][j] : 0;
                        a01[i][j] = (i < A.length && j + submatrixSize < A[0].length) ?
                                A[i][j + submatrixSize] : 0;
                        a10[i][j] = (i + submatrixSize < A.length && j < A[0].length) ?
                                A[i + submatrixSize][j] : 0;
                        a11[i][j] = (i + submatrixSize < A.length &&
                                j + submatrixSize < A[0].length) ?
                                A[i + submatrixSize][j + submatrixSize] : 0;
                        b00[i][j] = (i < B.length && j < B[0].length) ? B[i][j] : 0;
                        b01[i][j] = (i < B.length && j + submatrixSize < B[0].length) ?
                                B[i][j + submatrixSize] : 0;
                        b10[i][j] = (i + submatrixSize < B.length && j < B[0].length) ?
                                B[i + submatrixSize][j] : 0;
                        b11[i][j] = (i + submatrixSize < B.length &&
                                j + submatrixSize < B[0].length) ?
                                B[i + submatrixSize][j + submatrixSize] : 0;
                    }
                }
            }
            m1 = computeMatrixMult(MatrixUtil.addMatrices(a00, a11),
                    MatrixUtil.addMatrices(b00, b11));
            m2 = computeMatrixMult(MatrixUtil.addMatrices(a10, a11), b00);
            m3 = computeMatrixMult(a00, MatrixUtil.subtractMatrices(b01, b11));
            m4 = computeMatrixMult(a11, MatrixUtil.subtractMatrices(b10, b00));
            m5 = computeMatrixMult(MatrixUtil.addMatrices(a00, a01), b11);
            m6 = computeMatrixMult(MatrixUtil.subtractMatrices(a10, a00),
                    MatrixUtil.addMatrices(b00, b01));
            m7 = computeMatrixMult(MatrixUtil.subtractMatrices(a01, a11),
                    MatrixUtil.addMatrices(b10, b11));

            c00 = MatrixUtil.addMatrices(MatrixUtil.subtractMatrices(
                    MatrixUtil.addMatrices(m1, m4), m5), m7);
            c01 = MatrixUtil.addMatrices(m3, m5);
            c10 = MatrixUtil.addMatrices(m2, m4);
            c11 = MatrixUtil.addMatrices(MatrixUtil.addMatrices(
                    MatrixUtil.subtractMatrices(m1, m2), m3), m6);

            for(int i = 0; i < result.length; i++){
                for(int j = 0; j < result[0].length; j++){
                    if(i < submatrixSize) {
                        if(j < submatrixSize) {
                            result[i][j] = c00[i][j];
                        }
                        else{
                            result[i][j] = c01[i][j-submatrixSize];
                        }
                    }
                    else {
                        if(j < submatrixSize) {
                            result[i][j] = c10[i-submatrixSize][j];
                        }
                        else{
                            result[i][j] = c11[i-submatrixSize][j-submatrixSize];
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
