import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Board extends GameManager {
    //init var
    final protected static ArrayList<Cell> snakeCells = new ArrayList<>(); //has all the cellsssss that are part of the snake in them
    final public static Cell[][] cellList = new Cell[GameUI.INT_CONSTANTS.BOARD_SIZE.value][GameUI.INT_CONSTANTS.BOARD_SIZE.value]; //adds +1 because positions start at 1
    public static Color FIELD_COLOR = Color.WHITE;
    public static Color SNAKE_COLOR = Color.BLACK;
    public static Color FOOD_COLOR = Color.RED;
    private static String errorDetails; //used exclusively for error handlers

    public enum STRING_CONSTANTS {
        //TYPE VALUES: allows you to set celltypes without using direct strings and ensures no compatibility issues
        TYPE_FIELD,
        TYPE_SNAKE,
        TYPE_FOOD
    }

    public static void initCells(){
        int row = 0;
        int col = 0;
        try{
            //im so used to one-line methods that it physically pains me to see this as 5 LINES (incl. brackets) but it's for the sake of "readability" SMH MY HEAD BRO
            for (row = 1; row < GameUI.INT_CONSTANTS.BOARD_SIZE.value+2; row++) {
                for (col = 1; col < GameUI.INT_CONSTANTS.BOARD_SIZE.value; col++) {
                    new Cell(row, col);
                }
            }
        }catch (Exception e){
            errorDetails = "initCells";

            //used to check if the error is with the cell position specifically or is a more abstruse error
            try{new Cell(row, col);}catch (Exception f){ErrorPrinter.errorHandler("ERR_BR_CELL_OOB");}

            ErrorPrinter.errorHandler("ERR_BR_GENERIC"); //if error is not related to positioning
        }
        GameUI.repaintPanels();
    }

    protected static void createFood(){
        Cell cell;
        int posRow = 0;
        int posCol = 0;
        try {
            Random rand = new Random(); //gets random class to call random cell pos
            posRow = rand.nextInt(GameUI.INT_CONSTANTS.BOARD_SIZE.value);
            posCol = rand.nextInt(GameUI.INT_CONSTANTS.BOARD_SIZE.value);

            while (snakeCells.contains(cellList[posRow][posCol]) || posRow == 0 || posCol == 0) { //if selected cell is snake
                posRow = rand.nextInt(GameUI.INT_CONSTANTS.BOARD_SIZE.value);
                posCol = rand.nextInt(GameUI.INT_CONSTANTS.BOARD_SIZE.value);
            }

            cell = cellList[posRow][posCol]; //gets atts of cell currently selected

            //changes type to food and changes appearance to activated char
            cell.type = STRING_CONSTANTS.TYPE_FOOD;
            cell.changeAppearance(STRING_CONSTANTS.TYPE_FOOD);
        }catch(Exception e){
            errorDetails = "initCells";
            //used to check if the error is with the cell position specifically or is a more generic error since itd suck if it said "cell dont exist" and the error was something else
            try{new Cell(posRow, posCol);}catch(Exception f){ErrorPrinter.errorHandler("ERR_BR_OOB");}
            ErrorPrinter.errorHandler("ERR_BR_GENERIC");
        }
    }

    //we're in the zone where all the remaining methods are used exclusively for error handling
    public static String getLine(){return errorDetails;}

    public static void printCellAtts(Cell cell){errorDetails = "[ROW]: "+cell.ROW+"\n[COLUMN]: "+cell.COLUMN+"\n[AGE]: "+cell.age+"\n[TYPE]: "+cell.type;}

    //class manages attributes for individual cells
    static final class Cell{
        int ROW; //this is the location data and is effectively rows+col. this is used because you have to run the values through a map to sort by an element and running 2 maps for rows and cols is lag-inducing
        int COLUMN;
        int age; //used to determine which cell is cleared when the snake moves
        STRING_CONSTANTS type; //accepted params are in the enum
        JTextField cellField = new JTextField();

        //constructor method used for initialization: sets X/Y position
        private Cell(int row, int col){
            try {
                ROW = row;
                COLUMN = col;
                this.type = STRING_CONSTANTS.TYPE_FIELD;
                this.age = 0;
                this.changeAppearance(STRING_CONSTANTS.TYPE_FIELD);
                cellField.setEditable(false);
                cellField.setFocusable(false);

                cellList[row][col] = this;
                GameUI.setCell(cellField);
            }catch(Exception e){
                printCellAtts(this);
                ErrorPrinter.errorHandler("ABN_BR_UNDER_CONSTRUXION");
            }
        }

        void changeAppearance(STRING_CONSTANTS type){
            this.cellField.setBackground(statusColorsConstants(type));
            this.cellField.repaint();
            this.cellField.revalidate();
        }

        private Cell(){}

        //sets color depending on input status
        private Color statusColorsConstants(STRING_CONSTANTS type){
            this.type = type;
            return switch(type){
                case TYPE_FOOD -> FOOD_COLOR;
                case TYPE_SNAKE -> SNAKE_COLOR;
                case TYPE_FIELD -> FIELD_COLOR;
            };
        }
    }
}
