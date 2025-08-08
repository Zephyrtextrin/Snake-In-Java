import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

//holds data for snake
//todo: make this not extend the board why is it LIKE THIS
public class Snake extends Board{
    private static int length = 1;
    private static Direction direction = Direction.RIGHT;
    private static int row = 1; //thithe position of the cell the snake's head is in
    private static int column = 1;
    private static int modifier = direction.value;
    public static Direction opposite;

    Snake() throws IOException {}

    enum Direction{
        //init var
        //sets values for each direction
        UP(-1), DOWN(1), LEFT(-1), RIGHT(1);
        private final int value;

        //sets value according to input
        Direction(int value){this.value=value;}
    }

    protected static void updateMovement() throws IOException{
        if(direction.equals(Direction.LEFT)||direction.equals(Direction.RIGHT)){column += modifier;
        }else{row+=modifier;}

        snakeCellsManagement(cellList[row][column]); //calls snakeCellsManagement method to add the cell into the list of snake cells
        cellAgeDeprecation();
    }

    //contains the opposite direction for each key input (so u dont hit left key while going right and u move inside of yourself and instalose)
    private static final Map<Integer, Direction> directionMap = Map.of(KeyEvent.VK_RIGHT, Direction.RIGHT, KeyEvent.VK_LEFT, Direction.LEFT, KeyEvent.VK_UP, Direction.UP, KeyEvent.VK_DOWN, Direction.DOWN);

    static void changeDirection(int key){
        Direction newDirection = directionMap.get(key);
        if (newDirection!=null&&!newDirection.equals(oppositeDirection(direction))){
            opposite = oppositeDirection(direction);
            direction = newDirection;
            modifier = direction.value;
            if(direction==opposite){
                ErrorPrinter.errorHandler(ErrorPrinter.ERROR_CODE.ERR_SK_OUROBOROS,null);
            }
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
        if(Objects.equals(targetCell.type, STRING_CONSTANTS.TYPE_FOOD)){ //this looks incredibly dumb but you have to have this if statement inside the else
            Snake.length++;
            Board.createFood();
            GameManager.updateLengthText(length);
        }else if(snakeCells.contains(targetCell)){gameStatus = false;}

        targetCell.changeAppearance(STRING_CONSTANTS.TYPE_SNAKE); //changes target cell into its activated appearance (since snake cells are the activated appearance of a shaded-in block
        targetCell.type = STRING_CONSTANTS.TYPE_SNAKE;
        targetCell.age = Snake.length + 1; //add one because the cells would immediately get depreciated to (length-1)
        targetCell.ROW = Snake.row;
        targetCell.COLUMN = Snake.column;
        snakeCells.add(targetCell);
    }

    //decreases age of all cells by 1 and removes any cells with an age of zero
    private static void cellAgeDeprecation(){
        if(snakeCells.size()>Snake.length){
            Cell gone = snakeCells.getFirst();
            gone.changeAppearance(STRING_CONSTANTS.TYPE_FIELD); //sets appearance to regular ass cell LOL
            snakeCells.remove(gone);
        }
    }

    public static void setDefaultValues() throws IOException {
        length = 1;
        direction = Direction.RIGHT;
        row = 1; //thithe position of the cell the snake's head is in
        column = 1;
        modifier = direction.value;
        GameManager.updateLengthText(length);
    }

    //used exclusively for error handling
    public static String getErrorDetails(){return "[CURRENT ROW]: " + Snake.row + "\n[CURRENT COL]: " + Snake.column + "\n[MODIFIER]: " + Snake.modifier;}

    //used exclusively for error handling
    public static int[] getPosData(){return new int[]{row,column};}

}