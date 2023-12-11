/* Board.java
 * Emily Gai and Christina Zhang
 *
 * This program uses the Java Swing API to implement a GUI that accepts mouse clicks
 * and key presses for our crossword puzzle as well as the check button for
 * displaying red text for incorrect answers and black for correct ones.
 */

import javax.swing.JTextField;
import javax.swing.event.MouseInputListener;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

public class Board extends Canvas implements KeyListener, MouseInputListener, ActionListener {

    // Dimensions of the square board
    private static final int SIZE = 15;
    // Width of drawn lines
    private static final int LINE = 2;


    // Color of drawn lines
    private static final Color LINE_COLOR = Color.BLACK;
    // Color of black squares
    private static final Color BLACK_SQUARE = Color.BLACK;
    // Background should be white
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    // Color of selected square
    private static final Color HIGHLIGHTED1_COLOR = Color.YELLOW;
    // Color of other squares in the selected row/col
    private static final Color HIGHLIGHTED2_COLOR = Color.BLUE;
    // Color of wrong letters
    private static final Color AUTOCHECK_COLOR = Color.RED;


    // text displayed at the top of the frame
    private JTextField textField;
    // vertical (true) or horizontal (false)
    private boolean direction;
    // on (true) or off (false)
    private boolean autocheck;
    // what row of the crossword the player is in
    private int x;
    // what column of the crossword the player is in
    private int y;

    // array storing the locations of the black squares in the crossword
    private char[][] blackSquares;
    // array storing the locations of the highlighted squares in the crossword
    private char[][] highlighted;
    // array storing the locations and values of the typed letters from the user
    private String[][] letters;
    // array storing the answers to the crossword puzzle
    private String[][] answers;
    // array storing the "across" clue strings that correspond to each box in
    // the crossword
    private String[][] horClues;
    // array storing the "down" clue strings that correspond to each box in
    // the crossword
    private String[][] vertClues;

    // initializes a Board object from the information read from standard input
    public Board(JTextField textField, boolean direction, int x, int y,
                 char[][] blackSquares, String[][] horClues, String[][] vertClues,
                 String[][] answers) {
        this.textField = textField;
        this.direction = direction;
        this.x = x;
        this.y = y;
        letters = new String[SIZE][SIZE];
        highlighted = new char[SIZE][SIZE];
        this.horClues = horClues;
        this.vertClues = vertClues;
        this.answers = answers;
        this.blackSquares = blackSquares;
        autocheck = false;

        for (int i = 0; i < letters.length; i++) {
            for (int j = 0; j < letters.length; j++) {
                letters[i][j] = " ";
            }
        }
    }

    // determines which squares should be highlighted after every click
    public void getHighlighted() {
        int a = x;
        int b = y;

        // vertical
        if (direction) {
            int minY = y;
            int maxY = y;

            while (blackSquares[minY][a] != '*' && minY > 0)
                minY--;

            while (blackSquares[maxY][a] != '*' && maxY < SIZE - 1)
                maxY++;

            for (int i = y; i <= maxY; i++) {
                if (blackSquares[i][x] != '*') {
                    highlighted[i][x] = '*';
                }
            }
            for (int i = y; i >= minY; i--) {
                if (blackSquares[i][x] != '*') {
                    highlighted[i][x] = '*';
                }
            }
            System.out.print("Highlighted squares: ");
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (highlighted[j][i] == '*') {
                        System.out.printf("(%d, %d) ", i + 1, j + 1);
                    }
                }
            }
            System.out.println();
        }

        // horizontal
        if (!direction) {

            int minX = x;
            int maxX = x;

            while (blackSquares[b][minX] != '*' && minX > 0)
                minX--;

            while (blackSquares[b][maxX] != '*' && maxX < SIZE - 1)
                maxX++;

            for (int i = x; i <= maxX; i++) {
                if (blackSquares[y][i] != '*') {
                    highlighted[y][i] = '*';
                }
            }
            for (int i = x; i >= minX; i--) {
                if (blackSquares[y][i] != '*') {
                    highlighted[y][i] = '*';
                }
            }
            System.out.print("Highlighted squares: ");
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (highlighted[i][j] == '*') {
                        System.out.printf("(%d, %d) ", j + 1, i + 1);
                    }
                }
            }
            System.out.println();
        }

    }

    // resets the squares back to white
    public void resetHighlighted() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                highlighted[i][j] = ' ';
            }
        }
    }

    // turns check on/off when the button is pressed each time
    public void check() {
        autocheck = !autocheck;
    }

    // empty body because methods do not return any values
    public static void main(String[] args) {
        // most if not all methods can be tested/confirmed visually through the
        // JFrame
    }

    // registers the location the player clicks on the board
    public void mouseClicked(MouseEvent e) {
        x = Math.min(SIZE - 1, e.getX() / (getWidth() / SIZE));
        y = Math.min(SIZE - 1, e.getY() / (getHeight() / SIZE));

        // test: prints out the location of the mouse click
        System.out.printf("You have clicked at (%d, %d)\n", x + 1, y + 1);

        // changes the textField to the corresponding clue depending on the
        // direction the player is inputting letters in
        if (direction) {
            textField.setText(vertClues[y][x]);
        }
        else {
            textField.setText(horClues[y][x]);
        }

        // updates the board
        resetHighlighted();
        getHighlighted();
        super.repaint();
    }

    // registers key presses from the player and displays them on the board
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() >= KeyEvent.VK_A && e.getKeyCode() <= KeyEvent.VK_Z) {
            // test: prints out the key the player has pressed
            System.out.printf("You have pressed the %c key\n", e.getKeyCode());
            
            letters[x][y] = Character.toString((char) e.getKeyCode());
            super.repaint();

            // horizontal
            // automatically moves on to next square after each key press
            // loops back to the first row after editing the last row
            if (!direction) {
                x++;
                if (x == 15) {
                    x = 0;
                    y++;
                }
                if (y == 15) {
                    y = 0;
                }
                resetHighlighted();
                getHighlighted();
                super.repaint();
                textField.setText(horClues[y][x]);
            }

            // vertical
            // automatically moves on to the next square after each key press
            // loops back to the first column after editing the last column
            if (direction) {
                y++;
                if (y == 15) {
                    y = 0;
                    x++;
                }
                if (x == 15) {
                    x = 0;
                }
                resetHighlighted();
                getHighlighted();
                super.repaint();
                textField.setText(vertClues[y][x]);
            }
        }

        // changes the direction of input when the player hits the ENTER key
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            direction = !direction;
        }

        // moves on to the next square when the player hits the SPACE key
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // horizontal
            if (!direction) {
                x++;
                if (x == 15) {
                    x = 0;
                    y++;
                }
                if (y == 15) {
                    y = 0;
                }
                resetHighlighted();
                getHighlighted();
                super.repaint();
                textField.setText(horClues[y][x]);
            }

            // vertical
            if (direction) {
                y++;
                if (y == 15) {
                    y = 0;
                    x++;
                }
                if (x == 15) {
                    x = 0;
                }
                resetHighlighted();
                getHighlighted();
                super.repaint();
                textField.setText(horClues[y][x]);
            }
        }
    }

    // Draws the board to a JFrame
    public void paint(Graphics crossword) {

        // Sets font and size of inputted letters
        int fontSize = 50;
        crossword.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));

        // Draws the gridlines
        crossword.setColor(BACKGROUND_COLOR);
        crossword.fillRect(0, 0, getWidth(), getHeight());
        crossword.setColor(LINE_COLOR);
        int squareWidth = getWidth() / SIZE;
        int squareHeight = getHeight() / SIZE;
        for (int i = 1; i < SIZE; i++) {
            crossword.fillRect(i * squareWidth, 0, LINE, getHeight());
            crossword.fillRect(0, i * squareHeight, getWidth(), LINE);
        }

        // colors black squares at the indicated positions
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (blackSquares[i][j] == '*') {
                    crossword.setColor(BLACK_SQUARE);
                    crossword.fillRect(j * squareWidth, i * squareHeight, squareWidth,
                                       squareHeight);
                }

            }
        }

        // highlights squares in the same row/col of the clicked square blue
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (highlighted[i][j] == '*') {
                    crossword.setColor(HIGHLIGHTED2_COLOR);
                    crossword.fillRect(j * squareWidth, i * squareHeight, squareWidth,
                                       squareHeight);
                }
            }
        }

        // highlights the selected square yellow
        crossword.setColor(HIGHLIGHTED1_COLOR);
        crossword.fillRect(x * squareWidth, y * squareHeight, squareWidth,
                           squareHeight);

        // displays the typed letters from the user in their indicated positions
        crossword.setColor(BLACK_SQUARE);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                crossword.setColor(BLACK_SQUARE);
                if (blackSquares[j][i] != '*') {
                    // displays wrong answers in red text
                    if (autocheck && !(answers[j][i].equals(letters[i][j]))) {
                        crossword.setColor(AUTOCHECK_COLOR);
                    }
                    else crossword.setColor(BLACK_SQUARE);
                    crossword.drawString(letters[i][j], i * squareWidth +
                            squareHeight / 4, j * squareHeight
                                                 + 3 * squareHeight / 4);
                }
            }
        }
    }

    // override
    public void actionPerformed(ActionEvent e) {

    }

    // override
    public void keyTyped(KeyEvent e) {

    }

    // override
    public void keyReleased(KeyEvent e) {

    }

    // override
    public void mouseReleased(MouseEvent e) {

    }

    // override
    public void mouseEntered(MouseEvent e) {

    }

    // override
    public void mouseExited(MouseEvent e) {

    }

    // override
    public void mouseDragged(MouseEvent e) {

    }

    // override
    public void mouseMoved(MouseEvent e) {

    }

    // override
    public void mousePressed(MouseEvent e) {

    }

}
