import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameUI {

    protected static JFrame frame = new JFrame("game");
    protected static JPanel lengthPanel = new JPanel();
    protected static JPanel cellPanel = new JPanel();
    protected static JLabel lengthLabel = new JLabel("Length: 1");
    protected static JButton playAgain = new JButton("Play again");
    protected static JButton settingsButton = new JButton("Change settings");

    public enum INT_CONSTANTS {
        //INTEGERS: values that make the game work
        BOARD_SIZE(20),
        WINDOW_SIZE(35 * BOARD_SIZE.value),
        CELL_COUNT((int) Math.pow(BOARD_SIZE.value, 2));
        public final int value;

        //constructor for strings (all type vaues)
        INT_CONSTANTS(int value) {this.value = value;}
    }
    
    protected static void UIInit(){
        //changes l&f to windows classic because im a basic bitch like that
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows Classic".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch (Exception e){System.out.println("error with look and feel!\n------DETAILS------\n" + e.getMessage());}


        //window
        frame.setSize(INT_CONSTANTS.WINDOW_SIZE.value, INT_CONSTANTS.WINDOW_SIZE.value*7/6);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setFocusable(true);

        //panel that displays the length
        lengthPanel.setBounds(0, INT_CONSTANTS.WINDOW_SIZE.value, frame.getWidth(), INT_CONSTANTS.WINDOW_SIZE.value/6);
        lengthPanel.setFocusable(false);
        lengthPanel.setVisible(true);
        frame.add(lengthPanel);

        //cellPanel that all the display elements go on
        cellPanel.setBounds(30, 0, INT_CONSTANTS.WINDOW_SIZE.value-60, INT_CONSTANTS.WINDOW_SIZE.value);
        cellPanel.setFocusable(false); //i dont think anyone will ever focus on the cellPanel but this is just in case yk (explanation under the play again button comments)
        cellPanel.setLayout(new GridLayout(INT_CONSTANTS.BOARD_SIZE.value, INT_CONSTANTS.BOARD_SIZE.value));
        frame.add(cellPanel);

        //label to display length
        lengthLabel.setBounds(0, INT_CONSTANTS.WINDOW_SIZE.value, frame.getWidth(), INT_CONSTANTS.WINDOW_SIZE.value/6);
        lengthLabel.setFocusable(false);
        lengthPanel.add(lengthLabel);

        //label to display length
        settingsButton.setBounds(0, INT_CONSTANTS.WINDOW_SIZE.value+50, 150, 50);
        settingsButton.setFocusable(false);
        lengthPanel.add(settingsButton);

        //button to play again
        playAgain.setFocusable(false); //this cannot be focusable: if it is focusable, you can click on it and steal focus from the frame, and the frame needs to be focused all the time because the input listener only works when the component its applied to is focused
        playAgain.setVisible(false);
        lengthPanel.add(playAgain);

        //if this is the first time the game is initialized, make a new frame (bc u dont want a new window openign every time u play again cause the game's already on the previous window)

        cellPanel.repaint();
        cellPanel.revalidate();
        lengthPanel.repaint();
        lengthPanel.revalidate();

        playAgain.addActionListener(_ -> {
            GameManager.gameStatus = true;
            new GameManager();
            playAgain.setVisible(false);
        });

        settingsButton.addActionListener(_ -> {
            GameManager.gameStatus = false;
            frame.setVisible(false);
            SettingsUI.frame.setVisible(true);
        });
    }

    public static void repaintPanels(){
        cellPanel.repaint();
        cellPanel.revalidate();
        lengthPanel.repaint();
        lengthPanel.revalidate();
    }

    public static void setCell(JTextField cell){cellPanel.add(cell);}
}
