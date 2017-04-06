/*
 * File:    StrassenForkJoin.java
 * Authors: Charlie Beck, Phoebe Hughes, Tiffany Lam, Jenny Lin
 * Date:    April 10, 2017
 * Project: 3
 */

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * This class computes the product of multiplying to matrices.
 *
 * The algorithm uses Java's Fork/Join framework to compute the
 * product of two matrices using the Strassen's Algorithm.
 */
public class StrassenForkJoin implements MatrixMult {
    /**
     * Computes the result of multiplying two 2D arrays
     *
     * @param   A the first 2D array
     * @param   B the second 2D array
     * @return  the resulting multiplication matrix
     */
    @Override
    public int[][] computeMatrixMult(int[][] A, int[][] B) {
        int[][] result = new int[A.length][A.length];
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new MatrixMultAction(A, B, result));
        return result;
    }

    class MatrixMultAction extends RecursiveAction {
        private static final int THRESHOLD = 64;
        private int[][] a, b, result;

        public MatrixMultAction(int[][] A, int[][] B, int[][] result) {
            a = A;
            b = B;
            this.result = result;
        }

        @Override
        protected void compute() {
            //The size of the sub-matrices
            int submatrixSize = MatrixUtil.calcSize(a.length, a[0].length)/2;

            if (submatrixSize <= THRESHOLD) {   // do it sequentially
                MatrixUtil.multMatrices(a, b, result);
            }
            else {
                int[][][] subMatricesA, subMatricesB;
                // matrix is already of size 2^n by 2^n
                if(a.length == submatrixSize*2 && b.length == submatrixSize*2) {
                    //subdivide each matrix into 4 submatrices
                    subMatricesA = MatrixUtil.subDivideMatrix(a);
                    subMatricesB = MatrixUtil.subDivideMatrix(b);
                }
                // subdivide each matrix into 4 matrices
                // padding with 0s to obtain matrix of size 2^n by 2^n
                else {
                    subMatricesA = MatrixUtil.subDivideMatrix(MatrixUtil.padMatrixZeroes(a, submatrixSize*2));
                    subMatricesB = MatrixUtil.subDivideMatrix(MatrixUtil.padMatrixZeroes(b, submatrixSize*2));
                }

                int[][] a00 = subMatricesA[0];
                int[][] a01 = subMatricesA[1];
                int[][] a10 = subMatricesA[2];
                int[][] a11 = subMatricesA[3];
                int[][] b00 = subMatricesB[0];
                int[][] b01 = subMatricesB[1];
                int[][] b10 = subMatricesB[2];
                int[][] b11 = subMatricesB[3];

                int[][] m1Result = new int[a.length][a.length];
                int[][] m2Result = new int[a.length][a.length];
                int[][] m3Result = new int[a.length][a.length];
                int[][] m4Result = new int[a.length][a.length];
                int[][] m5Result = new int[a.length][a.length];
                int[][] m6Result = new int[a.length][a.length];
                int[][] m7Result = new int[a.length][a.length];


                MatrixMultAction m1 = new MatrixMultAction(MatrixUtil.addMatrices(a00, a11),
                        MatrixUtil.addMatrices(b00, b11), m1Result);
                MatrixMultAction m2 = new MatrixMultAction(MatrixUtil.addMatrices(a10, a11), b00, m2Result);
                MatrixMultAction m3 = new MatrixMultAction(a00, MatrixUtil.subtractMatrices(b01, b11), m3Result);
                MatrixMultAction m4 = new MatrixMultAction(a11, MatrixUtil.subtractMatrices(b10, b00), m4Result);
                MatrixMultAction m5 = new MatrixMultAction(MatrixUtil.addMatrices(a00, a01), b11, m5Result);
                MatrixMultAction m6 = new MatrixMultAction(MatrixUtil.subtractMatrices(a10, a00),
                        MatrixUtil.addMatrices(b00, b01), m6Result);
                MatrixMultAction m7 = new MatrixMultAction(MatrixUtil.subtractMatrices(a01, a11),
                        MatrixUtil.addMatrices(b10, b11), m7Result);

                invokeAll(m1, m2, m3, m4, m5, m6, m7);


                // resulting submatrices of final multiplication matrix
                int[][] c00 = MatrixUtil.addMatrices(MatrixUtil.subtractMatrices(
                        MatrixUtil.addMatrices(m1Result, m4Result), m5Result), m7Result);
                int[][] c01 = MatrixUtil.addMatrices(m3Result, m5Result);
                int[][] c10 = MatrixUtil.addMatrices(m2Result, m4Result);
                int[][] c11 = MatrixUtil.addMatrices(MatrixUtil.addMatrices(
                        MatrixUtil.subtractMatrices(m1Result, m2Result), m3Result), m6Result);

                // join submatrices to get final multiplication matrix result
                MatrixUtil.joinMatrices(c00, c01, c10, c11, result);
            }
        }
    }

    /**
     * Used to test Strassen's method using Fork/Join framework
     *
     * @param args
     */
    public static void main(String[] args) {
        MatrixUtil.testMatrixMult(new StrassenForkJoin());
    }

}
