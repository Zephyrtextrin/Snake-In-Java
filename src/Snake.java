import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Objects;

//holds data for snake
//todo: make this not extend the board why is it LIKE THIS THIS IS SOOO BAD
public class Snake extends Board{
    private int length = 1;
    private Direction direction = Direction.RIGHT;
    private int row = 1; //thithe position of the cell the snake's head is in
    private int column = 1;
    private int modifier = direction.value;

    Snake() throws Exception {}

    enum Direction{
        //init var
        //sets values for each direction
        UP(-1), DOWN(1), LEFT(-1), RIGHT(1);
        private final int value;

        //sets value according to input
        Direction(int value){this.value=value;}
    }

    protected void updateMovement() throws Exception {
        if(direction.equals(Direction.LEFT)||direction.equals(Direction.RIGHT)){column += modifier;
        }else{row+=modifier;}

        snakeCellsManagement(cellList[row][column]); //calls snakeCellsManagement method to add the cell into the list of snake cells
        cellAgeDeprecation();
    }

    //contains the opposite direction for each key input (so u dont hit left key while going right and u move inside of yourself and instalose)
    private static final Map<Integer, Direction> directionMap = Map.of(KeyEvent.VK_RIGHT, Direction.RIGHT, KeyEvent.VK_LEFT, Direction.LEFT, KeyEvent.VK_UP, Direction.UP, KeyEvent.VK_DOWN, Direction.DOWN);

    void changeDirection(int key) throws Exception {
        Direction newDirection = directionMap.get(key);
        if (newDirection!=null&&!newDirection.equals(oppositeDirection(direction))){
            Direction opposite = oppositeDirection(direction);
            direction = newDirection;
            modifier = direction.value;
            if(direction== opposite){
                throw ErrorPrinter.errorHandler(ErrorPrinter.ERROR_CODE.ERR_SK_OUROBOROS);
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
    private void snakeCellsManagement(Cell targetCell) throws Exception {
        if(Objects.equals(targetCell.type, STRING_CONSTANTS.TYPE_FOOD)){ //this looks incredibly dumb but you have to have this if statement inside the else
            length++;
            Board.createFood();
            GameManager.updateLengthText(length);
        }else if(snakeCells.contains(targetCell)){gameStatus = false;}

        targetCell.changeAppearance(STRING_CONSTANTS.TYPE_SNAKE); //changes target cell into its activated appearance (since snake cells are the activated appearance of a shaded-in block
        targetCell.type = STRING_CONSTANTS.TYPE_SNAKE;
        targetCell.age = length + 1; //add one because the cells would immediately get depreciated to (length-1)
        targetCell.ROW = row;
        targetCell.COLUMN = column;
        snakeCells.add(targetCell);
    }

    //decreases age of all cells by 1 and removes any cells with an age of zero
    private void cellAgeDeprecation(){
        if(snakeCells.size()>length){
            Cell gone = snakeCells.getFirst();
            gone.changeAppearance(STRING_CONSTANTS.TYPE_FIELD); //sets appearance to regular ass cell LOL
            snakeCells.remove(gone);
        }
    }

    public void setDefaultValues() throws Exception {
        length = 1;
        direction = Direction.RIGHT;
        row = 1; //thithe position of the cell the snake's head is in
        column = 1;
        modifier = direction.value;
        GameManager.updateLengthText(length);
    }

    //used exclusively for error handling
    public String getErrorDetails(){return "[CURRENT ROW]: " + this.row + "\n[CURRENT COL]: " + this.column + "\n[MODIFIER]: " + this.modifier;}

    //used exclusively for error handling
    public int[] getPosData(){return new int[]{row,column};}

}