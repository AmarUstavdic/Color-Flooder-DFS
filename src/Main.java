import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Stack;


public class Main {

    private static final int N = 10;
    private static final int M = 10;
    private static int stepCount = 0;


    public static void main(String[] args) {

        Color[] colors = new Color[] {
                new Color(255, 130, 28),
                new Color(255, 203, 31),
                new Color(69, 238, 17),
                new Color(255, 0, 0),
                new Color(9, 64, 239),
                new Color(101, 30, 229),
        };
        int[][] colorMatrix = generateMatrix(colors.length);


        JFrame frame = new JFrame("DFS (Depth-First-Search) Flooder");
        Dimension dimension = new Dimension(35*colorMatrix.length, 35*colorMatrix[0].length);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GridLayout gridLayout = new GridLayout(colorMatrix.length, colorMatrix[0].length);
        JPanel board = new JPanel();
        board.setDoubleBuffered(true);
        board.setLayout(gridLayout);
        board.setSize(dimension);
        board.setPreferredSize(dimension);

        JPanel[][] tiles = new JPanel[colorMatrix.length][colorMatrix[0].length];
        for (int i = 0; i < colorMatrix.length; i++) {
            for (int j = 0; j < colorMatrix[0].length; j++) {
                tiles[i][j] = new JPanel();
                tiles[i][j].setBackground(colors[colorMatrix[i][j]]);
                tiles[i][j].setBorder(new LineBorder(Color.BLACK));
                tiles[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JPanel tile = (JPanel) e.getSource();
                        int c = colorIdx(tile.getBackground(), colors);
                        flood(colorMatrix, c);
                        SwingUtilities.invokeLater(() -> updateBg(colorMatrix, tiles, colors));
                        stepCount++;
                        System.out.println(stepCount);
                    }
                });
                board.add(tiles[i][j]);
            }
        }

        JButton resetBtn = new JButton("New Game");
        resetBtn.setPreferredSize(new Dimension(35*colorMatrix.length, 50));
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });


        frame.add(board, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }


    private static int[][] generateMatrix(int bound) {
        Random random = new Random();
        int[][] mtx = new int[Main.N][Main.M];
        for (int i = 0; i < Main.N; i++) {
            for (int j = 0; j < Main.M; j++) {
                mtx[i][j] = random.nextInt(bound);
            }
        }
        return mtx;
    }

    private static void flood(int[][] colorMatrix, int color) {
        boolean[][] visited = new boolean[colorMatrix.length][colorMatrix[0].length];
        Stack<int[]> stack = new Stack<>();

        int initialColor = colorMatrix[0][0];
        int[] initialPosition = new int[] {0, 0};
        stack.push(initialPosition);

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int i = current[0];
            int j = current[1];
            if (!visited[i][j]) {
                visited[i][j] = true;
                if (i > 0) { if (isValid(i-1, j, visited, colorMatrix, initialColor)) stack.push(new int[] {i-1, j}); }
                if (i < colorMatrix.length-1) { if (isValid(i+1, j, visited, colorMatrix, initialColor)) stack.push(new int[] {i+1, j}); }
                if (j > 0) { if (isValid(i, j-1, visited, colorMatrix, initialColor)) stack.push(new int[] {i, j-1}); }
                if (j < colorMatrix[0].length-1) { if (isValid(i, j+1, visited, colorMatrix, initialColor)) stack.push(new int[] {i, j+1}); }
            }
        }
        for (int i = 0; i < colorMatrix.length; i++) {
            for (int j = 0; j < colorMatrix[0].length; j++) {
                if (visited[i][j]) colorMatrix[i][j] = color;
            }
        }
    }


    private static boolean isValid(int i, int j, boolean[][] visited, int[][] colorMatrix, int initColor) {
        return (colorMatrix[i][j] == initColor) && !visited[i][j];
    }


    private static int colorIdx(Color color, Color[] colors) {
        for (int i = 0; i < colors.length; i++) {
            if (color == colors[i]) return i;
        }
        return 0;
    }


    private static void updateBg(int[][] colorMatrix, JPanel[][] tiles, Color[] colors) {
        for (int i = 0; i < colorMatrix.length; i++) {
            for (int j = 0; j < colorMatrix[0].length; j++) {
                tiles[i][j].setBackground(colors[colorMatrix[i][j]]);
            }
        }
    }
}