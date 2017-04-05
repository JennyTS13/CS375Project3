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
//    final static int[][] A = new int[1500][1000];
//    final static int[][] B = new int[1000][1500];
    final static int[][] A = {{1, 2, 6},
                              {2, 1, 8}};

    final static int[][] B = {{1, 2},
                              {1, 2},
                              {2, 1}};

    static long sequentialRuntime = 0;
    final static int NUM_PROCESSORS = Runtime.getRuntime().availableProcessors();

    private static void initialize(int[][] A, int[][] B) {
        for(int i = 0; i < A.length; i++){
            for(int j = 0; j < A[0].length; j++) {
                A[i][j] = i + j;
                B[j][i] = j + i;
            }
        }
    }
    public static void main(String[] args) throws Exception {
        System.out.println("Number of processors: " + NUM_PROCESSORS);
//        initialize(A, B);
        test("Sequential version", new StrassenSequential());
        test("Parallel streams version", new MatrixMultStream());

    }

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
        MatrixUtil.printMatrix(result);

        // output the time needed to find the product
        System.out.println("Time: " + Timer.getRuntime() + "ms");

        // output the speedup
        if (sequentialRuntime == 0) {
            sequentialRuntime = Timer.getRuntime(); //sequential time
        }
        else {
            System.out.printf("Speed-up: %.2f\n", sequentialRuntime / 1.0 / Timer
                    .getRuntime());
        }
        System.out.println();
    }


}
