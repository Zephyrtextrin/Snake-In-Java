import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameManager extends GameUI{

    public static final boolean DEBUG = false;

    //starts and stops game, initializes variables
    static int highScore = 0;
    static boolean gameStatus = true; //auto ends the game if false

    //sets up frame, initializes some constructors, and runs method that actually makes the game work
    public static void main(String[] args) throws IOException{

        ErrorPrinter.initialize();
        SettingsUI.UIInit();

        if(DEBUG){
            System.out.println("[DEBUG MODE ENABLED]");
            Debug.displayOptions();
        }
    }

    //CONSTRUCTOR to start/stop the game depending on the game status
    GameManager() throws Exception {
        UIInit();
        runGame();
    }

    //manages game; initializes variables and sets timer
    private static void runGame() throws Exception {
        cellPanel.removeAll();
        Board.initCells();
        Board.snakeCells.clear();
        Board.createFood(); //initializes food item
        cellPanel.setEnabled(true);
        cellPanel.setVisible(true);
        gameStatus = true;
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

    public static void frameAdvancement(Snake snake){
        final int FPS = 75;
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        //key listener to obtain player input
        frame.addKeyListener(new KeyAdapter(){public void keyPressed(KeyEvent e){

            try {snake.changeDirection(e.getKeyCode());
            }catch (Exception ex){throw new RuntimeException(ex);}

            if(DEBUG){
                try{Debug.runDebugFunctions(e.getKeyChar());
                }catch (Exception ex) {throw new RuntimeException(ex);}
            }
        }});
        //method that gets called every (milliseconds defined in FPS variable) makes the snake move and shit
        Runnable snakeMovement = ()-> {
            //you have to try/catch for an exception on everything because executorservices just hangs the program instead of throwing an exception and i CANNOT figure OUT what the ISSUE IS unless it throws something
            try{
                if(gameStatus){
                    try{snake.updateMovement();
                    }catch(Exception e){
                        int[] pos = snake.getPosData();
                        if(pos[0]>Board.getBoardSize()-1||pos[1]>Board.getBoardSize()-1||pos[0]<=0||pos[1]<=0) {
                            gameStatus = false;
                        }else{throw ErrorPrinter.errorHandler(ErrorPrinter.ERROR_CODE.ERR_GM_EXECUTOR_SERVICE_FAULT);} //throws error if an exception happens for any other reason
                    }
                }else{stopGame();}//stops game is gamestatus is false
            }catch(Exception e){
                //idk why java is making me do this.. why do i need to try/catch an error throw
                try {throw ErrorPrinter.errorHandler(ErrorPrinter.ERROR_CODE.ERR_GM_EXECUTOR_SERVICE_FAULT);
                }catch (Exception ex){throw new RuntimeException(ex);}
            }
        };

        scheduler.scheduleAtFixedRate(snakeMovement, 1, FPS, TimeUnit.MILLISECONDS);
    }

    //you have to use a method here instead of shutting the game down natively using system.exit because you need to disable the game functions but keep the console running (to print stack trace)
    public static void emergencyShutdown(){
        GameUI.frame.setEnabled(false);
        GameUI.frame.setVisible(false);
        gameStatus = false;
    }

    public static void updateLengthText(int length) throws Exception {
        highScore = DataReadingInterface.readFile();

        if(length>highScore){
            highScore = length;
            DataReadingInterface.writeFile(String.valueOf(highScore));
        }

        GameUI.lengthLabel.setText("Length: "+length+" || High Score: "+highScore);
    }
}
