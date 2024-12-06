import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends GameUI{
    public static boolean init = true;
    private static int highScore = 0;

    //sets up frame, initializes some constructors, and runs method that actually makes the game work
    public static void main(String[] args) throws IOException{
        new ErrorPrinter();
        new Board(true);
        UIInit();
        new GameManager(true); //creates new instance of game manager
        frame.addKeyListener(new KeyAdapter(){public void keyPressed(KeyEvent e){Snake.changeDirection(e.getKeyCode());}}); //key listener to obtain player input
        frame.setVisible(true);
    }

    //starts and stops game, initializes variables
    protected static class GameManager {

        static boolean gameStatus = true; //auto ends the game if false

        //CONSTRUCTOR to start/stop the game depending on the game status
        GameManager(boolean gameStatus) throws IOException {
            if(gameStatus){
                UIInit();
                runGame();
            }else{stopGame();}
        }

        GameManager(){} //this feels stupid but sometimes they dont want params in a constructor

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
            final int FPS = 75;
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            //method that gets called every (milliseconds defined in FPS variable) makes the snake move and shit
            Runnable snakeMovement = ()-> {

                try {
                    if (gameStatus){
                        //you have to try/catch for an exception here because executorservices just hangs the program instead of throwing an exception even tho i kinda need it to not do that cause of the gameover logic relying on the snake being out of bounds
                        try {Snake.updateMovement();
                        }catch(Exception e){gameStatus = false;}
                    }else{new GameManager(false);}

                }catch(Exception e){
                    ErrorPrinter.errorHandler("ERR_GG_EXECUTOR_SERVICE_FAULT");
                    throw new RuntimeException(e);
                }
            };

            scheduler.scheduleAtFixedRate(snakeMovement, 1, FPS, TimeUnit.MILLISECONDS);
            init = false;
        }

        public static void highScoreUpdater() throws IOException{
            highScore = DataReadingInterface.readFile();

            if(Snake.length>highScore){
                highScore = Snake.length;
                DataReadingInterface.writeFile(String.valueOf(highScore));
            }
            GameUI.lengthLabel.setText("Length: "+Snake.length+" || High Score: "+highScore);
        }
    }
}
