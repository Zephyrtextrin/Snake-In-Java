import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Objects;

//holds data for snake
public class Snake extends Board{
    static int length = 1;
    static Direction direction = Direction.RIGHT;
    static int position = 1; //thithe position of the cell the snake's head is in
    //static int modifier = direction.value; //how mmany cells the snake will move by (aka: the direction)

    private Snake(boolean init){super(init);} //bc there is no parameterless constructor in board u must use super

    enum Direction{
        //init var
        //sets values for each direction
        UP(-Main.INT_CONSTANTS.BOARD_SIZE.value), DOWN(Main.INT_CONSTANTS.BOARD_SIZE.value), LEFT(-1), RIGHT(1);
        private final int value;

        //sets value according to input
        Direction(int value){this.value=value;}
    }

    public static void updateMovement(){
        position+=direction.value;
        gameStatus = checkBorders();
        Cell targetCell = cellList[position];
        snakeCellsManagement(targetCell); //calls snakeCellsManagement method to add the cell into the list of snake cells
        new Board(true);
    }

    //checks to see if player ran into a wall
    private static boolean checkBorders(){
        Cell targetCell = cellList[position];
        final boolean check = isCheck();
        final boolean ego = Objects.equals(targetCell.type, STRING_CONSTANTS.TYPE_SNAKE); //is snake eating itself
        if (check||ego){
            new Main.GameManager(false);
            return false;
        }
        return true;
    }

    //this is horizontal border check only. vertical check must be performed prior because it causes exception errors due to invalid cell #s
    private static boolean isCheck(){
        int posLocal = position-1;
        int pastPos = posLocal-direction.value;
        final boolean horizontal = direction.equals(Direction.LEFT)||direction.equals(Direction.RIGHT);
        final int row = posLocal/Main.INT_CONSTANTS.BOARD_SIZE.value; //gets the current row of the snake by dividing the position of the board size and truncating any decimal slots
        final int lastRow = pastPos/Main.INT_CONSTANTS.BOARD_SIZE.value; //these lastRow/Col vars are not neccessary you can just use an entire statement for the if-statements but this is more readable

        //VERY LONG DEBUG ]]]STRING DO NOT ENABLE UNLESS TESTING POSITIONING OR GAMEOVER CONDIITONALS
        //System.out.println("----------------------------\nCURRENT ROW: "+row+" PAST ROW: "+ lastRow +"\nCURRENT POS: "+posLocal+" PAST POS:  "+pastPos+"\nDIRECTION: "+direction+" horizontal: "+ horizontal +"\nMODIFIER: "+direction.value+"\n----------------------------");

        return horizontal&&lastRow!=row;
    }


    //contains the opposite direction for each key input (so u dont hit left key while going right and u move inside of yourself and instalose)
    private static final Map<Integer, Direction> directionMap = Map.of(KeyEvent.VK_RIGHT, Direction.RIGHT, KeyEvent.VK_LEFT, Direction.LEFT, KeyEvent.VK_UP, Direction.UP, KeyEvent.VK_DOWN, Direction.DOWN);

    //TODO: oppositedirection is kinda fucky wucky
    static void changeDirection(int key){
        Direction newDirection = directionMap.get(key);
        if (newDirection != null&&!newDirection.equals(oppositeDirection(direction))){direction = newDirection;}
    }

    //CHAT... IM A GENIUS!!
    private static Direction oppositeDirection(Direction direction){return switch (direction){
        case UP -> Direction.DOWN;
        case DOWN -> Direction.UP;
        case LEFT -> Direction.RIGHT;
        case RIGHT -> Direction.LEFT;
    };}

    //adds cells to snakeCell list
    private static void snakeCellsManagement(Cell targetCell){
        targetCell.changeAppearance(true); //changes target cell into its activated appearance (since snake cells are the activated appearance of a shaded-in block
        if (Objects.equals(targetCell.type, STRING_CONSTANTS.TYPE_FOOD)){ //this looks incredibly dumb but you have to have this if statement inside the else
            Snake.length++;
            Board.createFood();
            Main.lengthLabel.setText("Length: "+length);
        }

        targetCell.type = STRING_CONSTANTS.TYPE_SNAKE;
        targetCell.age = Snake.length + 1; //add one because the cells would immediately get depreciated to (length-1)
        snakeCells.add(targetCell);
    }
}