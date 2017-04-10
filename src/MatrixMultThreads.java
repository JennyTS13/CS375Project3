/*
 * File:    MatrixMultThreads.java
 * Authors: Charlie Beck, Phoebe Hughes, Tiffany Lam, Jenny Lin
 * Date:    April 10, 2017
 * Project: 3
 */


/**
 * This class computes the product of multiplying two matrices together
 *
 * It does so utilizing threads and Strassen's algorithm
 * when applicable to allow for a faster runtime
 */
public class MatrixMultThreads implements MatrixMult {


    /**
     * The number of processors available.
     */
    public static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    /**
     * int threshold indicating when to stop subdivision
     */
    public static final int THRESHOLD = 64;

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

        if (submatrixSize <= THRESHOLD) {
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

            if (NUM_THREADS == 2){

                MatrixThread[] threads = new MatrixThread[2];

                threads[0] = new MatrixThread(a00, b00);
                threads[1] = new MatrixThread(a01, b10);

                this.startThreads(threads);
                this.getResults(threads, subResults, 0);

                threads[0] = new MatrixThread(a00, b01);
                threads[1] = new MatrixThread(a01, b11);

                this.startThreads(threads);
                this.getResults(threads, subResults, 2);

                threads[0] = new MatrixThread(a10, b00);
                threads[1] = new MatrixThread(a11, b10);

                this.startThreads(threads);
                this.getResults(threads, subResults, 4);

                threads[0] = new MatrixThread(a10, b01);
                threads[1] = new MatrixThread(a11, b11);

                this.startThreads(threads);
                this.getResults(threads, subResults, 6);

            } else if (NUM_THREADS == 4) {

                MatrixThread[] threads = new MatrixThread[4];

                threads[0] = new MatrixThread(a00, b00);
                threads[1] = new MatrixThread(a01, b10);
                threads[2] = new MatrixThread(a00, b01);
                threads[3] = new MatrixThread(a01, b11);

                this.startThreads(threads);
                this.getResults(threads, subResults, 0);

                threads[0] = new MatrixThread(a10, b00);
                threads[1] = new MatrixThread(a11, b10);
                threads[2] = new MatrixThread(a10, b01);
                threads[3] = new MatrixThread(a11, b11);

                this.startThreads(threads);
                this.getResults(threads, subResults, 4);


            } else if (NUM_THREADS == 6){

                MatrixThread[] threads = new MatrixThread[6];

                threads[0] = new MatrixThread(a00, b00);
                threads[1] = new MatrixThread(a01, b10);
                threads[2] = new MatrixThread(a00, b01);
                threads[3] = new MatrixThread(a01, b11);
                threads[4] = new MatrixThread(a10, b00);
                threads[5] = new MatrixThread(a11, b10);

                this.startThreads(threads);
                this.getResults(threads, subResults, 0);

                threads = new MatrixThread[2];

                threads[0] = new MatrixThread(a10, b01);
                threads[1] = new MatrixThread(a11, b11);

                this.startThreads(threads);
                this.getResults(threads, subResults, 6);

            } else if (NUM_THREADS == 8){
                MatrixThread[] threads = new MatrixThread[8];

                threads[0] = new MatrixThread(a00, b00);
                threads[1] = new MatrixThread(a01, b10);
                threads[2] = new MatrixThread(a00, b01);
                threads[3] = new MatrixThread(a01, b11);
                threads[4] = new MatrixThread(a10, b00);
                threads[5] = new MatrixThread(a11, b10);
                threads[6] = new MatrixThread(a10, b01);
                threads[7] = new MatrixThread(a11, b11);

                this.startThreads(threads);
                this.getResults(threads, subResults, 0);

            } else {
                return MatrixUtil.multMatrices(A, B);
            }

            d00 = MatrixUtil.addMatrices(subResults[0], subResults[1]);
            d01 = MatrixUtil.addMatrices(subResults[2], subResults[3]);
            d10 = MatrixUtil.addMatrices(subResults[4], subResults[5]);
            d11 = MatrixUtil.addMatrices(subResults[6], subResults[7]);

            // join submatrices to get final multiplication matrix result
            result = MatrixUtil.joinMatrices(d00, d01, d10, d11, result.length);
        }

        return result;
    }

    /**
     * Gets the results of the threads matrices and puts them into the results matrix
     * @param threads the threads that are used to calculate the multiplication
     * @param results the array to put the results in
     * @param startLoc the index to start putting results in, in the results matrix
     */
    private void getResults(MatrixThread[] threads, int[][][] results, int startLoc) {
        //wait for all threads to finish
        for (int i =0; i< threads.length; i++) {
            try {
                threads[i].join();
                int[][] result = threads[i].getResult();
                results[i + startLoc] =  result;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Starts the threads.
     * @param threads the threads it starts.
     */
    private void startThreads(MatrixThread[] threads){
        for (Thread t: threads){
            t.start();
        }
    }

    /**
     * The Thread subclass utilized to allow for parallel processing
     */
    class MatrixThread extends Thread{

        /**
         * The matrices to be multiplied and the results of multiplying them.
         */
        private int[][] A, B, result;

        /**
         * Creates a thread to multiply the given matrices
         * @param a a matrix
         * @param b another matrix
         */
        public MatrixThread(int[][] a, int[][] b){
            A = a;
            B = b;
        }

        /**
         * Multiplies the matrices using standard matrix muliplication
         */
        @Override
        public void run(){
            this.result = MatrixUtil.multMatrices(A, B);
        }

        /**
         * Gets the results
         * @return the results
         */
        public int[][] getResult(){
            return this.result;
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
