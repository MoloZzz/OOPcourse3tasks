package tasks;

public class SolveSystem {
    public static double[] solveSystem(double[][] coefficients) {
        int rows = coefficients.length;
        int cols = coefficients[0].length - 1;

        for (int i = 0; i < rows; i++) {
            // Нормалізація поточного рядка
            double pivot = coefficients[i][i];
            for (int j = i; j < cols + 1; j++) {
                coefficients[i][j] /= pivot;
            }

            for (int k = 0; k < rows; k++) {
                if (k != i) {
                    double factor = coefficients[k][i];
                    for (int j = i; j < cols + 1; j++) {
                        coefficients[k][j] -= factor * coefficients[i][j];
                    }
                }
            }
        }

        double[] solutions = new double[rows];
        for (int i = 0; i < rows; i++) {
            solutions[i] = coefficients[i][cols];
        }

        return solutions;
    }
}
