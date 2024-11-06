import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    static boolean init = true;

    public static JLabel display = new JLabel();

    public enum STRING_CONSTANTS {
        //TYPE VALUES: allows you to set celltypes without using direct strings and ensures no compatibility issues
        TYPE_FIELD("tile"),
        TYPE_SNAKE("snake"),
        TYPE_FOOD("food");
        public final String value;

        STRING_CONSTANTS(String type) {this.value = type;}
    }

    public enum INT_CONSTANTS {
        //INTEGERS: values that make the game work
        BOARD_SIZE(20),
        WINDOW_SIZE(22 * BOARD_SIZE.value),
        CELL_COUNT((int) Math.pow(BOARD_SIZE.value, 2)),
        FPS(75);
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

        GameManager game = new GameManager(true); //creates new instance of game manager

        //adds jlabel that does the things EDIT: THIS IS THE WORST COMMENT OF ALL TIME WHAT WAS I THINKING LOL
        display.setBounds(0, 0, INT_CONSTANTS.WINDOW_SIZE.value, INT_CONSTANTS.WINDOW_SIZE.value);
        game.panel.add(display);
    }

    //starts and stops game, initializes variables
    protected static class GameManager {
        JPanel panel = new JPanel();
        JFrame frame = new JFrame("text-based snake in java+swing");
        JButton playAgain = new JButton("Play again");
        static boolean gameStatus = true; //auto ends the game if false

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



            //if this is the first time the game is initialized, make a new frame. if this is  not initialization (ie, hitting the play again button) dont make a new frame and hide the play again button
            if(init){
                frame.setVisible(true);
                playAgain.setBounds(INT_CONSTANTS.WINDOW_SIZE.value / 2, INT_CONSTANTS.WINDOW_SIZE.value / 2, INT_CONSTANTS.WINDOW_SIZE.value / 3, INT_CONSTANTS.WINDOW_SIZE.value / 5);
                panel.add(playAgain);
            }else{playAgain.setVisible(false);}

                playAgain.setVisible(false);
        }

        //manages game; initializes variables and sets timer
        private void runGame(){
            gameStatus = true;
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            new Board(false); //inits board of all cells
            Board.createFood(); //initializes food item

            //key listener to obtain player input
            frame.addKeyListener(new KeyAdapter(){public void keyPressed(KeyEvent e){Snake.changeDirection(e.getKeyCode());}});
            playAgain.addKeyListener(new KeyAdapter(){public void keyPressed(KeyEvent e){Snake.changeDirection(e.getKeyCode());}});

            //inits snake to default positions
            Snake.direction = Snake.Direction.RIGHT;
            Snake.position = 1;
            Snake.updateMovement();

            //method that gets called every (milliseconds defined in FPS variable) makes the snake move and shit
            Runnable snakeMovement = ()->{
                if(gameStatus){
                    try {Snake.updateMovement();
                    }catch(Exception e){gameStatus = false;}
                }else{
                    scheduler.shutdown();
                    stopGame();
                }
            };
            scheduler.scheduleAtFixedRate(snakeMovement, 0, INT_CONSTANTS.FPS.value, TimeUnit.MILLISECONDS);
        }

        public void stopGame(){
            gameStatus = false;
            init = false;
            display.setText("GAME OVER!");
            playAgain.setVisible(true);
            //logic for what happens when u click play again
            //TODO: where did my inputs go bruh imma go insane
            playAgain.addActionListener(_ -> {
                new GameManager(true);
                playAgain.setVisible(false);
            });
        }

        public void updateDisplayLabel(StringBuilder toDisplay) {
            display.setText(String.valueOf(toDisplay)); //sets display text to the drawn board
            panel.repaint();
            panel.revalidate();
        }
    }
}
