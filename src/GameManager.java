import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameManager extends GameUI{

    public static final boolean DEBUG = true;

    //starts and stops game, initializes variables
    static int highScore = 0;
    static boolean gameStatus = true; //auto ends the game if false

    //CONSTRUCTOR to start/stop the game depending on the game status
    GameManager(){
        UIInit();
        runGame();
    }

    //manages game; initializes variables and sets timer
    private static void runGame(){
        cellPanel.removeAll();
        Board.initCells();
        Board.snakeCells.clear();
        Snake.setDefaultValues();
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

    public static void frameAdvancement(){
        final int FPS = 75;
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        //key listener to obtain player input
        frame.addKeyListener(new KeyAdapter(){public void keyPressed(KeyEvent e){
            Snake.changeDirection(e.getKeyCode());
            if(DEBUG){Debug.runDebugFunctions(e.getKeyChar());}
        }});
        //method that gets called every (milliseconds defined in FPS variable) makes the snake move and shit
        Runnable snakeMovement = ()-> {
            //you have to try/catch for an exception on everything because executorservices just hangs the program instead of throwing an exception and i CANNOT figure OUT what the ISSUE IS unless it throws something
            try{
                if(gameStatus){
                    if(getFoodPosition()==null){
                        ErrorPrinter.handler("ABN_GM_FOOD_DNE", null);
                        Board.createFood();}
                    try{Snake.updateMovement();
                    }catch(Exception e){
                        int[] pos = Snake.getPosData();
                        if(pos[0]>Board.getBoardSize()-1||pos[1]>Board.getBoardSize()-1||pos[0]<=0||pos[1]<=0) {
                            gameStatus = false;
                        }else{ErrorPrinter.handler("ERR_GM_EXECUTOR_SERVICE_FAULT", e);} //throws error if an exception happens for any other reason
                    }
                }else{stopGame();} //stops game is gamestatus is false
            }catch(Exception e){
                ErrorPrinter.handler("ERR_GM_EXECUTOR_SERVICE_FAULT", e);
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

    //you have to use a method here instead of shutting the game down natively using system.exit because you need to disable the game functions but keep the console running (to print stack trace)
    public static void emergencyShutdown(){
        GameUI.frame.setEnabled(false);
        GameUI.frame.setVisible(false);
        gameStatus = false;
    }

    private static Board.Cell getFoodPosition(){
        int food = 0;

        try{
            if(Board.cellList!=null){
                for (int row = 1; row < Board.getBoardSize(); row++) {
                    for (int col = 1; col < Board.getBoardSize(); col++) {
                        Board.Cell cell = Board.cellList[row][col];
                        if (cell.type == Board.STRING_CONSTANTS.TYPE_FOOD) {
                            return cell;
                        }
                    }
                }
            }else{ErrorPrinter.handler("ERR_BR_DNE",null);}
            if(food==0){
                return null;
            }else if(food!=1&&food!=0){
                Exception e = new Exception();
                ErrorPrinter.handler("ABN_GM_DEBUG_GENERIC_EXCEPTION",e);
            }
        }catch(Exception e){ErrorPrinter.handler("ABN_GM_DEBUG_GENERIC_EXCEPTION", e);}
        return null;
    }
}
