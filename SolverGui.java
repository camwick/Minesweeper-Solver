import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

public class SolverGui {
  /**
   * Default method to create and run a Solver object.
   * Creates a new thread for solver to run on.
   * 
   * @param difficulty int, difficulty of the minesweeper board.
   * @param guessing   boolean, guessing turned on when true.
   * @param debug      boolean, debug mode turned on when true
   */
  private static void runSolver(int difficulty, boolean guessing, boolean debug) {
    Thread th = new Thread(new Runnable() {
      @Override
      public void run() {
        Solver game = new Solver(difficulty, guessing, debug);
        game.solve();
      }
    });

    th.start();
  }

  /**
   * Overloaded method to create and run a solver object. Accepts parameters
   * specifying the width, height, and mine count for custom sized boards.
   * Creates a new thread for solver to run on.
   * 
   * @param difficulty int, difficulty of the minesweeper board.
   * @param guessing   boolean, guessing turned on when true.
   * @param debug      boolean, debug mode turned on when true
   * @param width      int, width of minesweeper board.
   * @param height     int, height of minesweeper board.
   * @param mines      int, number of mines on the minesweeper board.
   */
  private static void runSolver(int difficulty, boolean guessing, boolean debug, int width, int height, int mines) {
    Thread th = new Thread(new Runnable() {
      @Override
      public void run() {
        Solver game = new Solver(difficulty, guessing, debug, width, height, mines);
        game.solve();
      }
    });

    th.start();
  }

  /**
   * Create gui
   * 
   * @param args command line argument enables debug mode
   */
  public static void main(String[] args) {
    boolean debug = false;
    if (args.length == 1)
      debug = true;
    final boolean finalDebug = debug;

    Font titleFont = new Font("MS Sans Serif", Font.BOLD, 24);
    Font headerBtnFont = new Font("MS Sans Serif", Font.BOLD, 18);
    Font plainFont = new Font("MS Sans Serif", Font.PLAIN, 16);

    // JFrame
    JFrame mainWindow = new JFrame("Minesweeper Solver");
    mainWindow.setSize(385, 415);
    mainWindow.setResizable(false);
    mainWindow.setLocationRelativeTo(null);
    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainWindow.setLayout(null);

    // title
    JLabel title = new JLabel("<HTML><U>Minesweeper Solver</U></HTML>");
    title.setBounds(70, 10, 242, 40);
    title.setFont(titleFont);
    mainWindow.add(title);

    // Difficulty header
    JLabel diffHeader = new JLabel("Difficulty");
    diffHeader.setBounds(10, 50, 90, 40);
    diffHeader.setFont(headerBtnFont);
    mainWindow.add(diffHeader);

    // Difficulty JRadioButton
    JRadioButton[] diffButtons = new JRadioButton[5];
    String[] diffLabels = { "Easy", "Medium", "Hard/Expert", "Evil", "Custom" };
    ButtonGroup diffGroup = new ButtonGroup();
    for (int i = 0; i < diffButtons.length; ++i) {
      diffButtons[i] = new JRadioButton(diffLabels[i]);
      diffButtons[i].setBounds(10, 90 + (40 * i), 108, 30);
      diffButtons[i].setFont(plainFont);
      diffGroup.add(diffButtons[i]);
      mainWindow.add(diffButtons[i]);
    }

    // Custom Settings
    JTextField[] customSettings = new JTextField[3];
    String[] customLabels = { "Width", "Height", "Mines" };
    for (int i = 0; i < customSettings.length; ++i) {
      customSettings[i] = new JTextField(customLabels[i]);
      customSettings[i].setBounds(120 + (80 * i), 253, 70, 30);
      customSettings[i].setFont(plainFont);
      mainWindow.add(customSettings[i]);
    }

    // Guessing Header
    JLabel guessingHeader = new JLabel("Make Guesses?");
    guessingHeader.setBounds(220, 50, 150, 40);
    guessingHeader.setFont(headerBtnFont);
    mainWindow.add(guessingHeader);

    // Guessing JRadioButton
    JRadioButton[] guessingButtons = new JRadioButton[2];
    String[] guessingLabels = { "Yes", "No" };
    ButtonGroup guessingGroup = new ButtonGroup();
    for (int i = 0; i < guessingButtons.length; ++i) {
      guessingButtons[i] = new JRadioButton(guessingLabels[i]);
      guessingButtons[i].setBounds(220, 95 + (40 * i), 108, 15);
      guessingButtons[i].setFont(plainFont);
      guessingGroup.add(guessingButtons[i]);
      mainWindow.add(guessingButtons[i]);
    }

    // Solve Button
    JButton solveBtn = new JButton("Solve");
    solveBtn.setBounds(10, 300, 350, 60);
    solveBtn.setFont(headerBtnFont);
    mainWindow.add(solveBtn);

    // EVENT HANDLERS

    // focus listeners
    for (int i = 0; i < customSettings.length; ++i) {
      final int index = i;

      customSettings[index].addFocusListener(new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
          if (customSettings[index].getText().trim().equals(customLabels[index]))
            customSettings[index].setText("");
        }

        @Override
        public void focusLost(FocusEvent e) {
          if (customSettings[index].getText().trim().equals(""))
            customSettings[index].setText(customLabels[index]);
        }
      });
    }
    // action listener
    solveBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean guessing = false;
        if (guessingButtons[0].isSelected())
          guessing = true;

        for (int i = 0; i < diffButtons.length; ++i) {
          if (diffButtons[i].isSelected()) {
            switch (i) {
              case 0: // easy
                runSolver(1, guessing, finalDebug);
                break;
              case 1: // medium
                runSolver(2, guessing, finalDebug);
                break;
              case 2: // hard/expert
                runSolver(3, guessing, finalDebug);
                break;
              case 3: // evil
                runSolver(4, guessing, finalDebug);
                break;
              case 4: // custom
                runSolver(5, guessing, finalDebug, Integer.parseInt(customSettings[0].getText()),
                    Integer.parseInt(customSettings[1].getText()), Integer.parseInt(customSettings[2].getText()));
                break;
            }
          }
        }
      }
    });

    mainWindow.setVisible(true);
  }
}