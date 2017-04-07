/*
 * File:    MatrixMultThreads.java
 * Authors: Charlie Beck, Phoebe Hughes, Tiffany Lam, Jenny Lin
 * Date:    April 10, 2017
 * Project: 3
 */


/**
 * This class computes the product of multiplying two matrices together
 *
 * It does so utilizing threads when applicable to allow for a faster runtime
 */
public class MatrixMultThreads implements MatrixMult {


    public static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();


    /**
     * Computes the result of multiplying two 2D arrays
     *
     * @param   A the first 2D array
     * @param   B the second 2D array
     * @return  the resulting multiplication matrix
     */
    public int[][] computeMatrixMult(int[][] A, int[][] B){

        int[][] result = new int[A.length][A.length];

        //The size of the sub-matrices
        int submatrixSize = MatrixUtil.calcSize(A.length, A[0].length)/2;

        if (submatrixSize <= 64) {
            result = MatrixUtil.multMatrices(A, B);
        }
        else {
            int[][][] subMatricesA, subMatricesB;
            int[][] a00, a01, a10, a11, b00, b01, b10, b11;
            int[][] c00, c01, c02, c03, c04, c05, c06, c07;
            int[][] d00, d01, d10, d11;

            // matrix is already of size 2^n by 2^n
            if (A.length == submatrixSize * 2 && B.length == submatrixSize * 2) {
                //subdivide each matrix into 4 submatrices
                subMatricesA = MatrixUtil.subDivideMatrix(A);
                subMatricesB = MatrixUtil.subDivideMatrix(B);
            }

            // subdivide each matrix into 4 matrices
            // padding with 0s to obtain matrix of size 2^n by 2^n
            else {
                subMatricesA = MatrixUtil.subDivideMatrix(
                        MatrixUtil.padMatrixZeroes(A, submatrixSize * 2));
                subMatricesB = MatrixUtil.subDivideMatrix(
                        MatrixUtil.padMatrixZeroes(B, submatrixSize * 2));
            }

            a00 = subMatricesA[0];
            a01 = subMatricesA[1];
            a10 = subMatricesA[2];
            a11 = subMatricesA[3];
            b00 = subMatricesB[0];
            b01 = subMatricesB[1];
            b10 = subMatricesB[2];
            b11 = subMatricesB[3];

            c00 = new int[a00.length][a00[0].length];
            c01 = new int[a00.length][a00[0].length];
            c02 = new int[a00.length][a00[0].length];
            c03 = new int[a00.length][a00[0].length];
            c04 = new int[a00.length][a00[0].length];
            c05 = new int[a00.length][a00[0].length];
            c06 = new int[a00.length][a00[0].length];
            c07 = new int[a00.length][a00[0].length];

            if (NUM_THREADS == 4) {

                MatrixThread[] threads = new MatrixThread[4];

                threads[0] = new MatrixThread(a00, b00, c00);
                threads[1] = new MatrixThread(a01, b10, c01);
                threads[2] = new MatrixThread(a00, b01, c02);
                threads[3] = new MatrixThread(a01, b11, c03);

                //wait for all threads to finish
                for (MatrixThread t : threads) {
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                threads[0] = new MatrixThread(a10, b00, c04);
                threads[1] = new MatrixThread(a11, b10, c05);
                threads[2] = new MatrixThread(a10, b01, c06);
                threads[3] = new MatrixThread(a11, b11, c07);

                //wait for all threads to finish
                for (MatrixThread t : threads) {
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (NUM_THREADS == 8){
                MatrixThread[] threads = new MatrixThread[8];

                threads[0] = new MatrixThread(a00, b00, c00);
                threads[1] = new MatrixThread(a01, b10, c01);
                threads[2] = new MatrixThread(a00, b01, c02);
                threads[3] = new MatrixThread(a01, b11, c03);
                threads[4] = new MatrixThread(a10, b00, c04);
                threads[5] = new MatrixThread(a11, b10, c05);
                threads[6] = new MatrixThread(a10, b01, c06);
                threads[7] = new MatrixThread(a11, b11, c07);

                //wait for all threads to finish
                for (MatrixThread t : threads) {
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {

                return MatrixUtil.multMatrices(A, B);
            }

            d00 = MatrixUtil.addMatrices(c00, c01);
            d01 = MatrixUtil.addMatrices(c02, c03);
            d10 = MatrixUtil.addMatrices(c04, c05);
            d11 = MatrixUtil.addMatrices(c06, c07);

            // join submatrices to get final multiplication matrix result
            result = MatrixUtil.joinMatrices(d00, d01, d10, d11, result.length);
        }

        return result;
    }

    /**
     * The Thread subclass utilized to allow for parallel processing
     */
    class MatrixThread extends Thread{

        private int[][] A, B, result;

        public MatrixThread(int[][] a, int[][] b, int[][] c){
            A = a;
            B = b;
            result = c;
        }

        @Override
        public void run(){

            result = MatrixUtil.multMatrices(A, B);

        }
    }

    /**
     * Used to test Strassen's method using Fork/Join framework
     *
     * @param args
     */
    public static void main(String[] args) {
        MatrixUtil.testMatrixMult(new MatrixMultThreads());
    }
}
