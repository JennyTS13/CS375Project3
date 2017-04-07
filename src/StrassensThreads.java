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
public class StrassensThreads implements MatrixMult {


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
            int[][][] subResults;
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

            subResults = new int[8][a00.length][a00.length];

            int [][][][] pairsOfMatrices = {{a00, b00}, {a01, b10}, {a00, b01}, {a01, b11}, {a10, b00}, {a11, b10}, {a10, b01}, {a11, b11}};

            int numUsableThreads = NUM_THREADS;
            if (NUM_THREADS > 8){
                numUsableThreads = 8;
            }

            MatrixThread[] threads = new MatrixThread[numUsableThreads];
            int remainder = 8 % numUsableThreads;
            int numPerThread = (8 -  remainder)/numUsableThreads;
            int indexFirstUnAssigned = 0;
            for (int i = 0; i< numUsableThreads; i++){
                int endIndex = indexFirstUnAssigned + numPerThread;
                if (i+1 <= remainder){
                    endIndex++;
                }
                threads[i] = new MatrixThread(pairsOfMatrices, subResults, indexFirstUnAssigned, endIndex);
                indexFirstUnAssigned = endIndex ;
            }

            this.runThreads(threads);

            d00 = MatrixUtil.addMatrices(subResults[0], subResults[1]);
            d01 = MatrixUtil.addMatrices(subResults[2], subResults[3]);
            d10 = MatrixUtil.addMatrices(subResults[4], subResults[5]);
            d11 = MatrixUtil.addMatrices(subResults[6], subResults[7]);

            // join submatrices to get final multiplication matrix result
            result = MatrixUtil.joinMatrices(d00, d01, d10, d11, result.length);
        }

        return result;
    }

    private void runThreads(MatrixThread[] threads){
        for (Thread t: threads){
            t.start();
        }
        for (Thread t: threads){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The Thread subclass utilized to allow for parallel processing
     */
    class MatrixThread extends Thread{

        private int[][][][] pairsOfMatrices;
        private int[][][] result;
        private int start, stop;
        private StrassenSequential strassenSequential;

        public MatrixThread(int[][][][] pairsOfMatrices, int[][][] result, int start, int stop){
            this.pairsOfMatrices = pairsOfMatrices;
            this.start = start;
            this.stop = stop;
            this.result = result;
            this.strassenSequential = new StrassenSequential();
        }

        @Override
        public void run(){
            for (int i = this.start; i<this.stop; i++){
                this.result[i] = this.strassenSequential.computeMatrixMult(this.pairsOfMatrices[i][0], this.pairsOfMatrices[i][1]);
            }
        }
    }

    /**
     * Used to test Strassen's method using Fork/Join framework
     *
     * @param args
     */
    public static void main(String[] args) {
        MatrixUtil.testMatrixMult(new StrassensThreads());
    }
}
