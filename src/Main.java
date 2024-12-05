import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends JFrame{
    public static boolean init = true;
    private static int highScore = 0;

    public static JButton playAgain = new JButton("Play again");
    public static JLabel lengthLabel = new JLabel("Length: 1");

    public enum INT_CONSTANTS {
        //INTEGERS: values that make the game work
        BOARD_SIZE(20),
        WINDOW_SIZE(35 * BOARD_SIZE.value),
        CELL_COUNT((int) Math.pow(BOARD_SIZE.value, 2)),
        FPS(75);
        public final int value;

        //constructor for strings (all type vaues)
        INT_CONSTANTS(int value) {this.value = value;}
    }

    //sets up frame, initializes some constructors, and runs method that actually makes the game work
    public static void main(String[] args) throws IOException {
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

        //cellPanel that displays the length and stuff
        game.lengthPanel.setBounds(0, game.frame.getHeight()-game.frame.getHeight()/6, game.frame.getWidth(), game.frame.getHeight()/6);
        //game.lengthPanel.setFocusable(false);
        game.lengthPanel.setVisible(true);
        game.frame.add(game.lengthPanel);
        System.out.println(game.lengthPanel.isShowing());
        //i dont usually like relying on static vars but this is necessary for some reason they will not show up if they're not static wtf lmao

        //key listener to obtain player input
        game.frame.addKeyListener(new KeyAdapter(){public void keyPressed(KeyEvent e){Snake.changeDirection(e.getKeyCode());}});
    }

    //starts and stops game, initializes variables
    protected static class GameManager {
        static JPanel cellPanel = new JPanel();
        static JPanel lengthPanel = new JPanel();
        JFrame frame = new JFrame("text-based snake in java+swing");

        static boolean gameStatus = true; //auto ends the game if false

        //CONSTRUCTOR to start/stop the game depending on the game status
        GameManager(boolean gameStatus) throws IOException {
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
            frame.setFocusable(true);

            //cellPanel that all the display elements go on
            cellPanel.setBounds(30, 0,INT_CONSTANTS.WINDOW_SIZE.value-60, INT_CONSTANTS.WINDOW_SIZE.value-INT_CONSTANTS.WINDOW_SIZE.value/6);
            cellPanel.setFocusable(false); //i dont think anyone will ever focus on the cellPanel but this is just in case yk (explanation under the play again button comments)
            cellPanel.setLayout(new GridLayout(INT_CONSTANTS.BOARD_SIZE.value, INT_CONSTANTS.BOARD_SIZE.value));
            frame.add(cellPanel);

            //label to display length
            lengthLabel.setBounds(0, frame.getHeight()-frame.getHeight()/6, frame.getWidth(), frame.getHeight()/6);
            lengthLabel.setFocusable(false);
            lengthPanel.add(lengthLabel);

            //button to play again
            playAgain.setFocusable(false); //this cannot be focusable: if it is focusable, you can click on it and steal focus from the frame, and the frame needs to be focused all the time because the input listener only works when the component its applied to is focused
            playAgain.setVisible(false);
            lengthPanel.add(playAgain);

            //display label that displays all the cubes
            //display.setFocusable(false); //this is likely not needed but its worth doing just in case (explanation under the play again button comments)
            //display.setSize(INT_CONSTANTS.BOARD_SIZE.value, INT_CONSTANTS.BOARD_SIZE.value);

            //if this is the first time the game is initialized, make a new frame (bc u dont want a new window openign every time u play again cause the game's already on the previous window)
            if(init){
                frame.setVisible(true);
                new Board(true); //inits board of all cells
            }

            repaintPanels();
            lengthPanel.repaint();
            lengthPanel.revalidate();
        }

        //manages game; initializes variables and sets timer
        private void runGame() throws IOException {
            gameStatus = true;
            Board.createFood(); //initializes food item
            cellPanel.setEnabled(true);
            cellPanel.setVisible(true);


            //inits snake to default positions
            Snake.direction = Snake.Direction.RIGHT;
            Snake.row = 1;
            Snake.column = 1;
            Snake.length = 1;
            Snake.updateMovement();

            if(init){frameAdvancement();}
        }

        private void stopGame(){
            gameStatus = false;
            init = false;
            cellPanel.setEnabled(false);
            cellPanel.setVisible(false);

            //display.setText("GAME OVER! Length: "+Snake.length);
            playAgain.setVisible(true);

            //logic for what happens when u click play again
            playAgain.addActionListener(_ -> {
                try {runGame();
                }catch(IOException e){throw new RuntimeException(e);}
                playAgain.setVisible(false);
            });

            repaintPanels();

        }

        private static void frameAdvancement(){
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            //method that gets called every (milliseconds defined in FPS variable) makes the snake move and shit
            Runnable snakeMovement = ()->{
                if(gameStatus){
                    //you have to try/catch for an exception here because executorservices just hang the program instead of throwing an exception even tho i kinda need it to not do that cause of the gameover logic relying on the snake being out of bounds
                    try {Snake.updateMovement();
                    }catch(Exception e){gameStatus = false;}
                }else{
                    try{new GameManager(false);
                        System.out.println("MAIN LINE 158");
                    }catch(IOException e) {throw new RuntimeException(e);
                    }
                }
            };
            //scheduler.scheduleAtFixedRate(snakeMovement, 0, INT_CONSTANTS.FPS.value, TimeUnit.MILLISECONDS);
            scheduler.scheduleAtFixedRate(snakeMovement, 1, INT_CONSTANTS.FPS.value, TimeUnit.MILLISECONDS);
            init = false;
        }

        public void repaintPanels(){
            cellPanel.repaint();
            cellPanel.revalidate();
        }

        public static void setCell(JTextField cell){cellPanel.add(cell);}

        public static void highScoreUpdater(JLabel lengthLabel) throws IOException{
            highScore = DataReadingInterface.readFile();

            if(Snake.length>highScore){
                highScore = Snake.length;
                DataReadingInterface.writeFile(String.valueOf(highScore));
            }
            lengthLabel.setText(lengthTextTemplate());
        }

        private static String lengthTextTemplate(){return "Length: "+Snake.length+" || High Score: "+highScore;}
    }
}
