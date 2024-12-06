import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

//holds data for snake
public class Snake extends Board{
    static int length = 1;
    static Direction direction = Direction.RIGHT;
    static int row = 1; //thithe position of the cell the snake's head is in
    static int column = 1;
    static int modifier = direction.value;
    static int pastRow;
    static int pastCol;
    static int placeHolder;

    enum Direction{
        //init var
        //sets values for each direction
        UP(-1), DOWN(1), LEFT(-1), RIGHT(1);
        private final int value;

        //sets value according to input
        Direction(int value){this.value=value;}
    }

    public static void updateMovement() throws IOException{
        pastCol = column;
        pastRow = row;

        if(direction.equals(Direction.LEFT)||direction.equals(Direction.RIGHT)){
            column += modifier;
            if(column!=pastCol+modifier){ErrorPrinter.errorHandler("ABN_SK_IRREGULAR_MOVEMENT");} //error handler if ur movement is different from ur directional modifier
        }else{
            row+=modifier;
            if(row!=pastRow+modifier){ErrorPrinter.errorHandler("ABN_SK_IRREGULAR_MOVEMENT");} //error handler if ur movement is different from ur directional modifier
        }

        if(pastRow!=row&&pastCol!=column){ErrorPrinter.errorHandler("ABN_SK_IRREGULAR_MOVEMENT");} //error handlers if u somehow move diagonally(these should always be false but its nice to have a handler)

        snakeCellsManagement(cellList[row][column]); //calls snakeCellsManagement method to add the cell into the list of snake cells
        new Board(false);
    }

    //contains the opposite direction for each key input (so u dont hit left key while going right and u move inside of yourself and instalose)
    private static final Map<Integer, Direction> directionMap = Map.of(KeyEvent.VK_RIGHT, Direction.RIGHT, KeyEvent.VK_LEFT, Direction.LEFT, KeyEvent.VK_UP, Direction.UP, KeyEvent.VK_DOWN, Direction.DOWN);

    static void changeDirection(int key){
        Direction newDirection = directionMap.get(key);
        if (!newDirection.equals(oppositeDirection(direction))){
            direction = newDirection;
            modifier = direction.value;
        }
    }

    //CHAT... IM A GENIUS!!
    private static Direction oppositeDirection(Direction direction){return switch (direction){
        case UP -> Direction.DOWN;
        case DOWN -> Direction.UP;
        case LEFT -> Direction.RIGHT;
        case RIGHT -> Direction.LEFT;
    };}

    //adds cells to snakeCell list
    private static void snakeCellsManagement(Cell targetCell) throws IOException{
        targetCell.changeAppearance(true); //changes target cell into its activated appearance (since snake cells are the activated appearance of a shaded-in block
        if(Objects.equals(targetCell.type, STRING_CONSTANTS.TYPE_FOOD)){ //this looks incredibly dumb but you have to have this if statement inside the else
            Snake.length++;
            Board.createFood();
            Main.GameManager.highScoreUpdater(Main.lengthLabel);
        }else if(snakeCells.contains(targetCell)){
            gameStatus = false;
        }

        targetCell.type = STRING_CONSTANTS.TYPE_SNAKE;
        targetCell.age = Snake.length + 1; //add one because the cells would immediately get depreciated to (length-1)
        targetCell.ROW = Snake.row;
        targetCell.COLUMN = Snake.column;
        snakeCells.add(targetCell);
    }
}