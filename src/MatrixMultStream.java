/*
 * File:    MatrixMultStream.java
 * Authors: Charlie Beck, Phoebe Hughes, Tiffany Lam, Jenny Lin
 * Date:    April 10, 2017
 * Project: 3
 */

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static java.util.stream.IntStream.range;

/**
 * This class computes the product of multiplying to matrices using streams.
 */
public class MatrixMultStream implements MatrixMult {

    /**
     * Computes the result of multiplying two 2D arrays
     *
     * @param   A the first 2D array
     * @param   B the second 2D array
     * @return  the resulting multiplication matrix
     */
    @Override
    public int[][] computeMatrixMult(int[][] A, int[][] B) {
        List<IntStream> result = Arrays.stream(A).parallel()
                     .map(row -> range(0, B[0].length)
                             .map(rowIndex -> range(0, B.length)
                                     .map(colIndex -> row[colIndex]*B[colIndex][rowIndex])
                                     .sum())).collect(Collectors.toList());

        return result.parallelStream().map(IntStream::toArray).toArray(int[][]::new);
    }

    /**
     * Used for testing the computeMatrixMult method
     */
    public static void main(String[] args) {
        MatrixUtil.testMatrixMult(new MatrixMultStream());
    }
}
