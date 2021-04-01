package bg.geist.init.adapter;

public class ArrayToMatrixAdapter {
    private ArrayToMatrixAdapter() {}

    public static String[][] toMatrix(String[] source, int cols) {
        int rows = source.length / cols;
        String[][] result = new String[rows][cols];
        int i = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result[r][c] = source[i++];
            }
        }
        return result;
    }
}