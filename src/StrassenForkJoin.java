/*
 * File:    StrassenForkJoin.java
 * Authors: Charlie Beck, Phoebe Hughes, Tiffany Lam, Jenny Lin
 * Date:    April 10, 2017
 * Project: 3
 */

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
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
        MatrixMultAction multAction = new MatrixMultAction(A, B);

        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(multAction);
        return multAction.getResult();
    }

    class MatrixMultAction extends RecursiveAction {
        private static final int THRESHOLD = 100;
        private int[][] a, b, result;

        public MatrixMultAction(int[][] A, int[][] B) {
            a = A;
            b = B;
        }

        @Override
        protected void compute() {
            result = new int[a.length][a.length];

            //The size of the sub-matrices
            int submatrixSize = MatrixUtil.calcSize(a.length, a[0].length)/2;

            if (submatrixSize <= THRESHOLD) {   // do it sequentially
                result = new StrassenSequential().computeMatrixMult(a, b);
            }
            else {
                MatrixMultAction[] mVals = new MatrixMultAction[7];
                int[][][] subMatricesA, subMatricesB;
                int[][] a00, a01, a10, a11, b00, b01, b10, b11;
                int[][] c00, c01, c10, c11;

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

                a00 = subMatricesA[0];
                a01 = subMatricesA[1];
                a10 = subMatricesA[2];
                a11 = subMatricesA[3];
                b00 = subMatricesB[0];
                b01 = subMatricesB[1];
                b10 = subMatricesB[2];
                b11 = subMatricesB[3];

                // calculates product arrays
                mVals[0] = new MatrixMultAction(MatrixUtil.addMatrices(a00, a11),
                        MatrixUtil.addMatrices(b00, b11));
                mVals[1] = new MatrixMultAction(MatrixUtil.addMatrices(a10, a11), b00);
                mVals[2] = new MatrixMultAction(a00, MatrixUtil.subtractMatrices(b01, b11));
                mVals[3] = new MatrixMultAction(a11, MatrixUtil.subtractMatrices(b10, b00));
                mVals[4] = new MatrixMultAction(MatrixUtil.addMatrices(a00, a01), b11);
                mVals[5] = new MatrixMultAction(MatrixUtil.subtractMatrices(a10, a00),
                        MatrixUtil.addMatrices(b00, b01));
                mVals[6] = new MatrixMultAction(MatrixUtil.subtractMatrices(a01, a11),
                        MatrixUtil.addMatrices(b10, b11));

                invokeAll(mVals);

                // resulting submatrices of final multiplication matrix
                c00 = MatrixUtil.addMatrices(MatrixUtil.subtractMatrices(
                        MatrixUtil.addMatrices(mVals[0].getResult(), mVals[3].getResult()),
                        mVals[4].getResult()), mVals[6].getResult());
                c01 = MatrixUtil.addMatrices(mVals[2].getResult(), mVals[4].getResult());
                c10 = MatrixUtil.addMatrices(mVals[1].getResult(), mVals[3].getResult());
                c11 = MatrixUtil.addMatrices(MatrixUtil.addMatrices(
                        MatrixUtil.subtractMatrices(mVals[0].getResult(),
                                mVals[1].getResult()), mVals[2].getResult()),
                        mVals[5].getResult());

                // join submatrices to get final multiplication matrix result
                result = MatrixUtil.joinMatrices(c00, c01, c10, c11, result.length);
            }
        }

        /**
         * Returns the result of the computation
         *
         * @return 2D array of result of matrix multiplication
         */
        private int[][] getResult(){
            return result;
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
