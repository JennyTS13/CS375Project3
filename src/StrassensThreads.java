/*
 * File:    StrassensThreads.java
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

    /**
     * The number of processors available.
     */
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

            //individual parts of subMatricesA and B
            int[][] a00, a01, a10, a11, b00, b01, b10, b11;
            int[][][] subResults;
            //the parts of the results
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

            subResults = new int[7][a00.length][a00.length];

            //pairs of matrices that must be multiplied
            int [][][][] pairsOfMatrices = {
                    {MatrixUtil.addMatrices(a00, a11), MatrixUtil.addMatrices(b00, b11)},
                    {MatrixUtil.addMatrices(a10, a11), b00},
                    {a00, MatrixUtil.subtractMatrices(b01, b11)},
                    {a11, MatrixUtil.subtractMatrices(b10, b00)},
                    {MatrixUtil.addMatrices(a00, a01), b11},
                    {MatrixUtil.subtractMatrices(a10, a00), MatrixUtil.addMatrices(b00, b01)},
                    {MatrixUtil.subtractMatrices(a01, a11), MatrixUtil.addMatrices(b10, b11)}};

            int numUsableThreads = NUM_THREADS;
            if (NUM_THREADS > 7){
                numUsableThreads = 7;
            }

            MatrixThread[] threads = new MatrixThread[numUsableThreads];
            int remainder = 7 % numUsableThreads;
            int numPerThread = (7 -  remainder)/numUsableThreads;
            int indexFirstUnAssigned = 0;
            //assigns to each thread which matrices from pairsOfMatrices to multiply
            for (int i = 0; i< numUsableThreads; i++){
                int endIndex = indexFirstUnAssigned + numPerThread;
                if (i+1 <= remainder){
                    endIndex++;
                }
                threads[i] = new MatrixThread(pairsOfMatrices, subResults,
                        indexFirstUnAssigned, endIndex);
                indexFirstUnAssigned = endIndex ;
            }

            this.runThreads(threads);

            // resulting submatrices of final multiplication matrix
            d00 = MatrixUtil.addMatrices(MatrixUtil.subtractMatrices (
                    MatrixUtil.addMatrices(subResults[0], subResults[3]),
                    subResults[4]), subResults[6]);
            d01 = MatrixUtil.addMatrices(subResults[2], subResults[4]);
            d10 = MatrixUtil.addMatrices(subResults[1], subResults[3]);
            d11 = MatrixUtil.addMatrices(MatrixUtil.addMatrices(
                    MatrixUtil.subtractMatrices(subResults[0], subResults[1]),
                    subResults[2]), subResults[5]);

            // join submatrices to get final multiplication matrix result
            result = MatrixUtil.joinMatrices(d00, d01, d10, d11, result.length);
        }

        return result;
    }

    /**
     * Starts and then joins the threads.
     * @param threads the threads to be run.
     */
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

        /**
         * All pairs of matrices that must be multiplied.
         */
        private int[][][][] pairsOfMatrices;

        /**
         * The result of multiplying the matrices.
         */
        private int[][][] result;

        /**
         * Which matrix pairs to multiply in pairsOfMatrices.
         */
        private int start, stop;

        /**
         * The MatrixMult which multiplies the matrices.
         */
        private StrassenSequential strassenSequential;


        /**
         * Creates a matrix thread which multiples matrices using Strassen's.
         * @param pairsOfMatrices Pairs of matrices that must be multiplied
         * @param result The matrix to put the result into
         * @param start The starting index for which pairs of matrices to multiply
         * @param stop the stopping index for which pairs of matrices to multiply
         */
        public MatrixThread(int[][][][] pairsOfMatrices,
                            int[][][] result, int start, int stop){
            this.pairsOfMatrices = pairsOfMatrices;
            this.start = start;
            this.stop = stop;
            this.result = result;
            this.strassenSequential = new StrassenSequential();
        }

        /**
         * Calculates the multiplication of the matrices that it is assigned and puts
         * it into the results
         */
        @Override
        public void run(){
            for (int i = this.start; i<this.stop; i++){
                this.result[i] = this.strassenSequential.computeMatrixMult(
                        this.pairsOfMatrices[i][0], this.pairsOfMatrices[i][1]);
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
