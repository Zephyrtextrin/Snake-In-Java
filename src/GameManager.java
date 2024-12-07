import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameManager extends GameUI{

    //starts and stops game, initializes variables
    static int highScore = 0;

    static boolean gameStatus = true; //auto ends the game if false

    //CONSTRUCTOR to start/stop the game depending on the game status
    GameManager(){
        UIInit();
        runGame();
        frameAdvancement();
    }
    //manages game; initializes variables and sets timer
    private static void runGame(){
        gameStatus = true;
        Snake.setDefaultValues();
        Board.createFood(); //initializes food item
        cellPanel.setEnabled(true);
        cellPanel.setVisible(true);
    }

    private static void stopGame(){
        gameStatus = false;
        cellPanel.setEnabled(false);
        cellPanel.setVisible(false);

        //display.setText("GAME OVER! Length: "+Snake.length);
        playAgain.setVisible(true);
        //logic for what happens when u click play again

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
                }else{stopGame();}

            }catch(Exception e){
                ErrorPrinter.errorHandler("ERR_GM_EXECUTOR_SERVICE_FAULT");
                throw new RuntimeException(e);
            }
        };

        scheduler.scheduleAtFixedRate(snakeMovement, 1, FPS, TimeUnit.MILLISECONDS);
    }

    public static void highScoreUpdater(int length) throws IOException{
        highScore = DataReadingInterface.readFile();

        if(length>highScore){
            highScore = length;
            DataReadingInterface.writeFile(String.valueOf(highScore));
        }
        GameUI.lengthLabel.setText("Length: "+length+" || High Score: "+highScore);
    }
}
