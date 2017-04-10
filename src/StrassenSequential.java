/*
 * File:    StrassenSequential.java
 * Authors: Charlie Beck, Phoebe Hughes, Tiffany Lam, Jenny Lin
 * Date:    April 10, 2017
 * Project: 3
 */

/**
 * This class computes the product of multiplying to matrices.
 *
 * The algorithm uses Strassen's Algorithm,
 * recursively dividing the matrices into submatrices,
 * and calculating product arrays to calculate the final product
 * of the two given matrices
 */
public class StrassenSequential implements MatrixMult {

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
    @Override
    public int[][] computeMatrixMult(int[][] A, int[][] B) {
        int[][] result = new int[A.length][A.length];

        //The size of the sub-matrices
        int submatrixSize = MatrixUtil.calcSize(A.length, A[0].length)/2;

        if (submatrixSize <= THRESHOLD) {
            result = MatrixUtil.multMatrices(A, B);
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
            m1 = computeMatrixMult(MatrixUtil.addMatrices(a00, a11),
                    MatrixUtil.addMatrices(b00, b11));
            m2 = computeMatrixMult(MatrixUtil.addMatrices(a10, a11), b00);
            m3 = computeMatrixMult(a00, MatrixUtil.subtractMatrices(b01, b11));
            m4 = computeMatrixMult(a11, MatrixUtil.subtractMatrices(b10, b00));
            m5 = computeMatrixMult(MatrixUtil.addMatrices(a00, a01), b11);
            m6 = computeMatrixMult(MatrixUtil.subtractMatrices(a10, a00),
                    MatrixUtil.addMatrices(b00, b01));
            m7 = computeMatrixMult(MatrixUtil.subtractMatrices(a01, a11),
                    MatrixUtil.addMatrices(b10, b11));

            // resulting submatrices of final multiplication matrix
            c00 = MatrixUtil.addMatrices(MatrixUtil.subtractMatrices (
                    MatrixUtil.addMatrices(m1, m4), m5), m7);
            c01 = MatrixUtil.addMatrices(m3, m5);
            c10 = MatrixUtil.addMatrices(m2, m4);
            c11 = MatrixUtil.addMatrices(MatrixUtil.addMatrices(
                    MatrixUtil.subtractMatrices(m1, m2), m3), m6);

            // join submatrices to get final multiplication matrix result
            result = MatrixUtil.joinMatrices(c00, c01, c10, c11, result.length);
        }

        return result;
    }

    /**
     * Used to test Strassen's method
     *
     * @param args
     */
    public static void main(String[] args) {
        MatrixUtil.testMatrixMult(new StrassenSequential());
    }
}
