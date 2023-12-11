/* Client.java
 * Emily Gai and Christina Zhang
 *
 * This program reads a puzzle.txt file from standard input and stores the relevant
 * data into arrays that are given as parameters for a new Board object which
 * runs gameplay of the crossword through methods from Board.java
 */

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Client implements ActionListener {

    // simulates gameplay of the crossword
    public static void main(String[] args) {
        // 15x15 is the standard size for a crossword puzzle in the New York Times
        int SIZE = 15;
        int numBlack = StdIn.readInt();

        // reads the location of the black squares from standard input and
        // stores them in a 2D array
        char[][] blackSquares = new char[SIZE][SIZE];
        int[] rows = new int[numBlack];
        int[] cols = new int[numBlack];
        StdIn.readString();

        for (int i = 0; i < numBlack; i++) {
            rows[i] = StdIn.readInt();
        }
        StdIn.readLine();
        StdIn.readString();

        for (int i = 0; i < numBlack; i++) {
            cols[i] = StdIn.readInt();
        }
        for (int i = 0; i < numBlack; i++) {
            blackSquares[rows[i]][cols[i]] = '*';
        }

        // reads the "down" and "across" clues from standard input and stores
        // the respective clue for each box in 2D arrays
        String[][] horClues = new String[SIZE][SIZE];
        String[][] vertClues = new String[SIZE][SIZE];
        StdIn.readLine();
        StdIn.readString();

        for (int i = 0; i < SIZE; i++) {
            int total = 0;
            do {
                int numBoxes = StdIn.readInt();
                String clue = StdIn.readLine();
                for (int j = total; j < numBoxes + total; j++) {
                    horClues[i][j] = clue;
                }
                total += numBoxes;
            } while (total < SIZE);
        }
        StdIn.readLine();
        StdIn.readString();

        for (int i = 0; i < SIZE; i++) {
            int total = 0;
            do {
                int numBoxes = StdIn.readInt();
                String clue = StdIn.readLine();
                for (int j = total; j < numBoxes + total; j++) {
                    vertClues[j][i] = clue;
                }
                total += numBoxes;
            } while (total < SIZE);
        }
        StdIn.readLine();
        StdIn.readString();

        // reads the answer for each box from standard input and stores them in
        // a 2D array
        String[][] answers = new String[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                answers[i][j] = StdIn.readString();
            }
        }

        // initializes a Board object for the crossword using the indicated parameters
        JTextField textField = new JTextField(" ");
        textField.setEditable(false);
        Board crossword = new Board(textField, false, 0, 0, blackSquares, horClues, vertClues,
                                    answers);
        crossword.addMouseListener(crossword);
        crossword.addKeyListener(crossword);

        // initializes JFrame objects for the crossword and check button
        JFrame frameObj = new JFrame();
        frameObj.setTitle("Crossword Puzzle");
        JFrame frameObj2 = new JFrame();
        frameObj2.setTitle("Buttons");

        JButton check = new JButton("Check");
        check.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                crossword.check();
            }
        });

        // displays the windows that display the crossword and the check button
        // allows the user to exit the program and game
        frameObj2.add(check);
        frameObj2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frameObj2.setSize(100, 100);
        frameObj2.setLocationRelativeTo(null);
        frameObj2.setVisible(true);

        frameObj.getContentPane().add(BorderLayout.NORTH, textField);
        frameObj.getContentPane().add(BorderLayout.CENTER, crossword);

        frameObj.setSize(900, 900);
        frameObj.setVisible(true);
        frameObj.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    // override
    public void actionPerformed(ActionEvent e) {

    }
}
