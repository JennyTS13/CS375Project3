/*
 * File:    StrassenForkJoin.java
 * Authors: Charlie Beck, Phoebe Hughes, Tiffany Lam, Jenny Lin
 * Date:    April 10, 2017
 * Project: 3
 */

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
        return result;
    }

    /**
     * Used to test Strassen's method using Fork/Join framework
     *
     * @param args
     */
    public static void main(String[] args) {
        int[][] A = {{1, 2, 6},
                     {2, 1, 8}};

        int[][] B = {{1, 2},
                     {1, 2},
                     {2, 1}};

        Timer.start();
        int[][] result = (new StrassenForkJoin()).computeMatrixMult(A, B);
        Timer.stop();

        MatrixUtil.printMatrix(result);
        System.out.println("Total time: " + Timer.getRuntime());
    }

}
