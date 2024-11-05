import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static JLabel display = new JLabel();

    public enum STRING_CONSTANTS {
        //TYPE VALUES: allows you to set celltypes without using direct strings and ensures no compatibility issues
        TYPE_FIELD("tile"),
        TYPE_SNAKE("snake"),
        TYPE_FOOD("food");
        public final String value;

        STRING_CONSTANTS(String type) {
            this.value = type;
        }
    }

    public enum INT_CONSTANTS {
        //INTEGERS: values that make the game work
        BOARD_SIZE(20),
        WINDOW_SIZE(22 * BOARD_SIZE.value),
        CELL_COUNT((int) Math.pow(BOARD_SIZE.value, 2)),
        FPS(150);
        public final int value;

        //constructor for strings (all type vaues)
        INT_CONSTANTS(int value) {
            this.value = value;
        }
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
        } catch (Exception e) {
            System.out.println("error with look and feel!\n------DETAILS------\n" + e.getMessage());
        }

        GameManager game = new GameManager(true); //creates new instance of game manager
        //new Board(); //inits cell values
        //CREATES UI VALUES

        //adds jlabel that does the things
        display.setBounds(0, 0, INT_CONSTANTS.WINDOW_SIZE.value, INT_CONSTANTS.WINDOW_SIZE.value);
        game.panel.add(display);
    }

    //starts and stops game, initializes variables
    protected static class GameManager {
        JPanel panel = new JPanel();
        JFrame frame = new JFrame("text-based snake in java+swing");
        JButton playAgain = new JButton("Play again");
        //auto ends the game if false
        static boolean gameStatus = true;


        //CONSTRUCTOR to start/stop the game depending on the game status
        GameManager(boolean gameStatus) {
            if(gameStatus){
                UIInit();
                runGame();
            }else{stopGame();}
        }

        GameManager() {} //this feels stupid but sometimes they dont want params in a constructor

        private void UIInit(){
            gameStatus = true;

            frame.setSize(INT_CONSTANTS.WINDOW_SIZE.value, INT_CONSTANTS.WINDOW_SIZE.value);

            //adds panel
            frame.add(panel);
            panel.setBounds(0, 0, INT_CONSTANTS.WINDOW_SIZE.value, INT_CONSTANTS.WINDOW_SIZE.value);
            frame.setResizable(false);

            //inits the button to play again
            //inivisible before initialization
            playAgain.setBounds(INT_CONSTANTS.WINDOW_SIZE.value / 2, INT_CONSTANTS.WINDOW_SIZE.value / 2, INT_CONSTANTS.WINDOW_SIZE.value / 3, INT_CONSTANTS.WINDOW_SIZE.value / 5);
            panel.add(playAgain);
            playAgain.setVisible(false);

            frame.setVisible(true);
        }

        //manages game; initializes variables and sets timer
        private void runGame() {
            //UI
            // create a window IM SOOOOOOOOOOO TIRED IDC NO MORE ILL FORMAT THIS STUPID UI BS TOMORROW

            //RUNS GAME METHODS

            //inits all game elements
            //this is not all values that need initialization but it's all values that need it only when the program is booted for the first time
            new Board(false); //inits cells
            Snake.updateMovement(); //inits snake at positiion of 1

            //key listener to obtain player input
            frame.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    Snake.changeDirection(e.getKeyCode());
                }
            });
            final int[] pressedKey = new int[1]; //WHA THE ACTUAL freak IS INTELLIJ SMART SOLUTIONS MAKING MY CODE DO WHI IS THIS A FINAL INT ARRAY???

            Board.createFood(); //initializes food item

            //method that gets called every (milliseconds defined in FPS variable) makes the snake move and shit
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            Runnable snakeMovement = () -> {
                if(gameStatus){Snake.changeDirection(pressedKey[0]);
                }else{
                    scheduler.shutdown();
                    stopGame();
                }
            };

            // Schedule the task with a fixed rate
            scheduler.scheduleAtFixedRate(snakeMovement, 0, INT_CONSTANTS.FPS.value, TimeUnit.MILLISECONDS);
        }

        public void stopGame() {
            gameStatus = false;
            display.setText("GAME OVER!");
            playAgain.setVisible(true);
            playAgain.addActionListener(_ -> runGame()); //if clicked, play the game again
            Snake.position = 1;
            Snake.length = 1;
        }

        public void updateDisplayLabel(StringBuilder toDisplay) {
            display.setText(String.valueOf(toDisplay)); //sets display text to the drawn board
            //System.out.println(display.getText());
            panel.repaint();
            panel.revalidate();
        }
    }
}
