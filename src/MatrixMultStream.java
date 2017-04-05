/*
 * File:    MatrixMultStream.java
 * Authors: Charlie Beck, Phoebe Hughes, Tiffany Lam, Jenny Lin
 * Date:    April 10, 2017
 * Project: 3
 */

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;

/**
 * This class computes the product of multiplying to matrices using streams.
 */
public class MatrixMultStream implements MatrixMult{

    /**
     * Computes the result of multiplying two 2D arrays
     *
     * @param   A the first 2d array
     * @param   B the second 2d array
     * @return  the resulting array created
     */
    @Override
    public int[][] computeMatrixMult(int[][] A, int[][] B) {
        Stream<IntStream> result = Arrays.stream(A)
                     .map(row -> range(0, B[0].length)
                             .map(rowIndex -> range(0, B.length)
                                     .parallel()
                                     .map(colIndex -> row[colIndex]*B[colIndex][rowIndex])
                                     .sum()));

        return result.parallel().map(IntStream::toArray).toArray(int[][]::new);
    }

    /**
     * Used for testing the computeMatrixMult method
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
        int[][] result = (new MatrixMultStream()).computeMatrixMult(A, B);
        Timer.stop();

        MatrixUtil.printMatrix(result);
        System.out.println("Total time: " + Timer.getRuntime());
    }
}
