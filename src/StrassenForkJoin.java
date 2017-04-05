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
        pool.invoke(new MatrixMultAction(A, B));
        return result;
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
            //The size of the sub-matrices
            int submatrixSize = MatrixUtil.calcSize(a.length, a[0].length)/2;

            if (submatrixSize <= THRESHOLD) {   // do it sequentially
                result = new StrassenSequential().computeMatrixMult(a, b);
            }
            else {

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
