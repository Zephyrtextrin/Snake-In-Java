import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends JFrame{
    static boolean init = true;

    public static JLabel display = new JLabel();
    public static JButton playAgain = new JButton("Play again");
    public static JLabel lengthLabel = new JLabel("Length: 1");
    public static JPanel lengthPanel = new JPanel();

    public enum INT_CONSTANTS {
        //INTEGERS: values that make the game work
        BOARD_SIZE(20),
        WINDOW_SIZE(25 * BOARD_SIZE.value),
        CELL_COUNT((int) Math.pow(BOARD_SIZE.value, 2)),
        FPS(900);
        public final int value;

        //constructor for strings (all type vaues)
        INT_CONSTANTS(int value) {this.value = value;}
    }

    //sets up frame, initializes some constructors, and runs method that actually makes the game work
    public static void main(String[] args) {
        //changes l&f to windows classic because im a basic bitch like that
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows Classic".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch (Exception e){System.out.println("error with look and feel!\n------DETAILS------\n" + e.getMessage());}

        final GameManager game = new GameManager(true); //creates new instance of game manager

        //i dont usually like relying on static vars but this is necessary for some reason they will not show up if they're not static wtf lmao
        //game.panel.add(display);
        game.frame.add(lengthPanel);
        GameManager.panel.add(playAgain);
        lengthLabel.setBounds(0,0,100,100);
        lengthPanel.add(lengthLabel);
    }

    //starts and stops game, initializes variables
    protected static class GameManager {
        static JPanel panel = new JPanel();

        JFrame frame = new JFrame("text-based snake in java+swing");
        static boolean gameStatus = true; //auto ends the game if false

        //CONSTRUCTOR to start/stop the game depending on the game status
        GameManager(boolean gameStatus) {
            if(gameStatus){
                UIInit();
                runGame();
            }else{stopGame();}
        }

        GameManager(){} //this feels stupid but sometimes they dont want params in a constructor

        private void UIInit(){
            gameStatus = true;

            //window
            frame.setSize(INT_CONSTANTS.WINDOW_SIZE.value, INT_CONSTANTS.WINDOW_SIZE.value);
            frame.setResizable(false);

            //panel that all the display elements go on
            panel.setSize(INT_CONSTANTS.WINDOW_SIZE.value, INT_CONSTANTS.WINDOW_SIZE.value-INT_CONSTANTS.WINDOW_SIZE.value/6);
            panel.setFocusable(false); //i dont think anyone will ever focus on the panel but this is just in case yk (explanation under the play again button comments)
            panel.setLayout(new GridLayout(INT_CONSTANTS.BOARD_SIZE.value, INT_CONSTANTS.BOARD_SIZE.value));
            frame.add(panel);

            //panel that displays the length and stuff
            lengthPanel.setBounds(0, INT_CONSTANTS.WINDOW_SIZE.value-INT_CONSTANTS.WINDOW_SIZE.value/6, INT_CONSTANTS.WINDOW_SIZE.value, INT_CONSTANTS.WINDOW_SIZE.value/6);
            lengthPanel.setLayout(null);
            lengthPanel.setFocusable(false);
            frame.add(lengthPanel);

            //label to display length
            lengthLabel.setBounds(0,0,100,100);
            lengthLabel.setFocusable(false);

            //button to play again
            playAgain.setFocusable(false); //this cannot be focusable: if it is focusable, you can click on it and steal focus from the frame, and the frame needs to be focused all the time because the input listener only works when the component its applied to is focused
            playAgain.setVisible(false);

            //display label that displays all the cubes
            display.setFocusable(false); //this is likely not needed but its worth doing just in case (explanation under the play again button comments)
            display.setSize(INT_CONSTANTS.BOARD_SIZE.value, INT_CONSTANTS.BOARD_SIZE.value);

            //if this is the first time the game is initialized, make a new frame (bc u dont want a new window openign every time u play again cause the game's already on the previous window)
            if(init){frame.setVisible(true);}

            //reloads panels
            panel.repaint();
            panel.revalidate();

            lengthPanel.repaint();
            lengthPanel.revalidate();
        }

        //manages game; initializes variables and sets timer
        private void runGame(){
            gameStatus = true;
            new Board(true); //inits board of all cells
            Board.createFood(); //initializes food item

            //key listener to obtain player input
            frame.addKeyListener(new KeyAdapter(){public void keyPressed(KeyEvent e){Snake.changeDirection(e.getKeyCode());}});

            //inits snake to default positions
            Snake.direction = Snake.Direction.RIGHT;
            Snake.position = 1;
            Snake.length = 1;
            Snake.updateMovement();

            if(init){frameAdvancement();}
        }

        private void stopGame(){
            gameStatus = false;
            init = false;

            display.setText("GAME OVER! Length: "+Snake.length);
            playAgain.setVisible(true);

            //logic for what happens when u click play again
            playAgain.addActionListener(_ -> {
                runGame();
                playAgain.setVisible(false);
            });
        }

        private static void frameAdvancement(){
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            //method that gets called every (milliseconds defined in FPS variable) makes the snake move and shit
            Runnable snakeMovement = ()->{
                if(gameStatus){
                    //you have to try/catch for an exception here because executorservices just hang the program instead of throwing an exception even tho i kinda need it to not do that cause of the gameover logic relying on the snake being out of bounds
                    try {Snake.updateMovement();
                    }catch(Exception e){gameStatus = false;}
                }else{new GameManager(false);}
            };
            //scheduler.scheduleAtFixedRate(snakeMovement, 0, INT_CONSTANTS.FPS.value, TimeUnit.MILLISECONDS);
            scheduler.scheduleAtFixedRate(snakeMovement, 1, INT_CONSTANTS.FPS.value, TimeUnit.MILLISECONDS);

        }

        public void repaintPanels(){
            panel.repaint();
            panel.revalidate();
            lengthPanel.repaint();
            lengthPanel.revalidate();
        }

        public static void setCell(JTextField cell){panel.add(cell);}
    }
}
