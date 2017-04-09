/*
 * File:    Driver.java
 * Authors: Charlie Beck, Phoebe Hughes, Tiffany Lam, Jenny Lin
 * Date:    April 10, 2017
 * Project: 3
 */

/**
 * A driver for testing different algorithms of multiplying two matrices.
 */
public class Driver {

    /**
     * The matrices to be multiplied.
     */
    final static int[][] A = new int[1000][1000];
    final static int[][] B = new int[1000][1000];


    /** The time to multiply the matrices using MatrixMultSequential*/
    static long sequentialRuntime = 0;

    /** The time to multiply the matrices using StrassensSequential*/
    static long strassenRuntime = 0;

    /** The number of processors available */
    final static int NUM_PROCESSORS = Runtime.getRuntime().availableProcessors();

    /**
     * Initializes matrices A and B with arbitrary values
     * @param A a 2D matrix
     * @param B another 2D matrix
     */
    private static void initialize(int[][] A, int[][] B) {
        for(int i = 0; i < A.length; i++){
            for(int j = 0; j < A[0].length; j++) {
                A[i][j] = i + j;
                B[j][i] = j + i;
            }
        }
    }

    /**
     * Tests a MatrixMult, finding the time and speed ups
     * @param version the name of the MatrixMult being tested
     * @param m the MatrixMult
     * @throws Exception
     */
    private static void test(String version, MatrixMult m) throws Exception {
        // warm up
        m.computeMatrixMult(A, B);
        m.computeMatrixMult(A, B);

        // compute the product of multiplying two matrices
        Timer.start();
        int[][] result = m.computeMatrixMult(A, B);
        Timer.stop();

        // output the results
        System.out.println("--------" + version + "----------");

        // output the multiplication matrix
//        MatrixUtil.printMatrix(result);

        // output the time needed to find the product
        System.out.println("Time: " + Timer.getRuntime() + "ms");

        // output the speedup
        if (version.equals("Standard Sequential Version")) {
            sequentialRuntime = Timer.getRuntime(); //sequential time
        } else if (version.equals("Sequential Strassens Version")){
            strassenRuntime = Timer.getRuntime();
            System.out.printf("Speed-up vs. Standard Sequential: %.2f\n",
                    sequentialRuntime / 1.0 / Timer.getRuntime());
        }
        else {
            System.out.printf("Speed-up vs. Standard Sequential: %.2f\n",
                    sequentialRuntime / 1.0 / Timer.getRuntime());
            System.out.printf("Speed-up vs. Strassen's Sequential: %.2f\n",
                    strassenRuntime / 1.0 / Timer.getRuntime());
        }
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Number of Processors: " + NUM_PROCESSORS);
        initialize(A, B);
        test("Standard Sequential Version", new MatrixMultSequential());
        test("Sequential Strassens Version", new StrassenSequential());
        test("Threaded Version", new MatrixMultThreads());
        test("Strassens Threaded Version", new StrassensThreads());
        test("Parallel Streams Version", new MatrixMultStream());
        test("Fork-Join Version", new StrassenForkJoin());
    }

}
