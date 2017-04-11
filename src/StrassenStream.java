/*
 * File:    StrassenStream.java
 * Authors: Charlie Beck, Phoebe Hughes, Tiffany Lam, Jenny Lin
 * Date:    April 10, 2017
 * Project: 3
 */

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class computes the product of multiplying to matrices using streams and strassen's
 * algorithm.
 */
public class StrassenStream implements MatrixMult {

    MatrixMultStream matrixMultStream = new MatrixMultStream();

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

        //The size of the sub-matrices
        int submatrixSize = MatrixUtil.calcSize(A.length, A[0].length)/2;

        if (submatrixSize <= 64) {
            //uses streams/standard matrix multiplication to compute
            result = this.matrixMultStream.computeMatrixMult(A, B);
        }
        else {
            int[][][] subMatricesA, subMatricesB;
            int[][] a00, a01, a10, a11, b00, b01, b10, b11;
            int[][] c00, c01, c10, c11;
            int[][] m1, m2, m3, m4, m5, m6, m7;

            // matrix is already of size 2^n by 2^n
            if(A.length == submatrixSize*2 && B.length == submatrixSize*2) {
                //subdivide each matrix into 4 submatrices
                subMatricesA = MatrixUtil.subDivideMatrix(A);
                subMatricesB = MatrixUtil.subDivideMatrix(B);
            }
            // subdivide each matrix into 4 matrices
            // padding with 0s to obtain matrix of size 2^n by 2^n
            else {
                subMatricesA = MatrixUtil.subDivideMatrix(
                        MatrixUtil.padMatrixZeroes(A, submatrixSize*2));
                subMatricesB = MatrixUtil.subDivideMatrix(
                        MatrixUtil.padMatrixZeroes(B, submatrixSize*2));
            }

            a00 = subMatricesA[0];
            a01 = subMatricesA[1];
            a10 = subMatricesA[2];
            a11 = subMatricesA[3];
            b00 = subMatricesB[0];
            b01 = subMatricesB[1];
            b10 = subMatricesB[2];
            b11 = subMatricesB[3];

            // calculates product arrays
            int [][][][] pairsOfMatrices = {
                    {MatrixUtil.addMatrices(a00, a11), MatrixUtil.addMatrices(b00, b11)},
                    {MatrixUtil.addMatrices(a10, a11), b00},
                    {a00, MatrixUtil.subtractMatrices(b01, b11)},
                    {a11, MatrixUtil.subtractMatrices(b10, b00)},
                    {MatrixUtil.addMatrices(a00, a01), b11},
                    {MatrixUtil.subtractMatrices(a10, a00), MatrixUtil.addMatrices(b00, b01)},
                    {MatrixUtil.subtractMatrices(a01, a11), MatrixUtil.addMatrices(b10, b11)}};


            List<int[][]> subResults = Arrays.stream(pairsOfMatrices)
                    .parallel()
                    .map(pair -> computeMatrixMult(pair[0], pair[1]))
                    .collect(Collectors.toList());

            c00 = MatrixUtil.addMatrices(MatrixUtil.subtractMatrices (
                    MatrixUtil.addMatrices(subResults.get(0), subResults.get(3)),
                    subResults.get(4)), subResults.get(6));
            c01 = MatrixUtil.addMatrices(subResults.get(2), subResults.get(4));
            c10 = MatrixUtil.addMatrices(subResults.get(1), subResults.get(3));
            c11 = MatrixUtil.addMatrices(MatrixUtil.addMatrices(
                    MatrixUtil.subtractMatrices(subResults.get(0), subResults.get(1)),
                    subResults.get(2)), subResults.get(5));

            // join submatrices to get final multiplication matrix result
            result = MatrixUtil.joinMatrices(c00, c01, c10, c11, result.length);
        }

        return result;
    }
    /**
     * Used for testing the computeMatrixMult method
     */
    public static void main(String[] args) {
        MatrixUtil.testMatrixMult(new StrassenStream());
    }
}
