import javax.swing.*;
import java.awt.*;

public class GameUI {

    protected static JFrame frame = new JFrame("game");
    protected static JPanel lengthPanel = new JPanel();
    protected static JPanel cellPanel = new JPanel();
    protected static JLabel lengthLabel = new JLabel("Length: 1");
    protected static JButton playAgain = new JButton("Play again");
    protected static JButton settingsButton = new JButton("Change settings");
    public static int boardSize = 20; //temp solution
    public static int cellCount = 400; //temp
    
    protected static void UIInit(){
        final int WINDOW_SIZE = 860;
        boardSize = SettingsUI.getBoardSize();
        //changes l&f to windows classic because im a basic bitch like that
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows Classic".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch (Exception e){System.out.println("error with look and feel!\n------DETAILS------\n" + e.getMessage());}

        //panel that displays the length
        lengthPanel.setBounds(0, WINDOW_SIZE, frame.getWidth(), WINDOW_SIZE/6);
        lengthPanel.setFocusable(false);
        lengthPanel.setVisible(true);
        frame.add(lengthPanel);

        //cellPanel that all the display elements go on
        cellPanel.setBounds(30, 0, WINDOW_SIZE-60, WINDOW_SIZE);
        cellPanel.setFocusable(false); //i dont think anyone will ever focus on the cellPanel but this is just in case yk (explanation under the play again button comments)
        cellPanel.setLayout(new GridLayout(boardSize, boardSize));
        frame.add(cellPanel);

        //label to display length
        lengthLabel.setBounds(0, WINDOW_SIZE, frame.getWidth(), WINDOW_SIZE/6);
        lengthLabel.setFocusable(false);
        lengthPanel.add(lengthLabel);

        //label to display length
        settingsButton.setBounds(0, WINDOW_SIZE+50, 150, 50);
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
            SettingsUI.enableDisableSettings(true);
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
