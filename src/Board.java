import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Board extends GameManager {
    //init var
    private static final int BOARD_SIZE = 20;
    public static Color FIELD_COLOR = Color.WHITE;
    public static Color SNAKE_COLOR = Color.BLACK;
    public static Color FOOD_COLOR = Color.RED;
    final protected static ArrayList<Cell> snakeCells = new ArrayList<>(); //has all the cellsssss that are part of the snake in them
    public static Cell[][] cellList = new Cell[BOARD_SIZE][BOARD_SIZE];

    public enum STRING_CONSTANTS {
        //TYPE VALUES: allows you to set celltypes without using direct strings and ensures no compatibility issues
        TYPE_FIELD,
        TYPE_SNAKE,
        TYPE_FOOD
    }

    public static void initCells(){
        cellList = new Cell[BOARD_SIZE][BOARD_SIZE];
        int row;
        int col;
        final String errorMethodTraceBack = "initCells();";
        final int boardSize = BOARD_SIZE;
        try{
            //im so used to one-line methods that it physically pains me to see this as 5 LINES (incl. brackets) but it's for the sake of "readability" SMH MY HEAD BRO
            for (row = 1; row < boardSize; row++) {
                for (col = 1; col < boardSize; col++) {
                    new Cell(row, col, errorMethodTraceBack);
                }
            }
        }catch (Exception e){
            ErrorPrinter.setDetails("[METHOD]: "+ errorMethodTraceBack,false);
            ErrorPrinter.errorHandler("ERR_BR_GENERIC", e); //if error is not related to positioning
        }
        GameUI.repaintPanels();
    }

    protected static void createFood(){
        Cell cell;
        int posRow = 0;
        int posCol = 0;

        try {
            do {
                Random rand = new Random(); //gets random class to call random cell pos
                posRow = rand.nextInt(BOARD_SIZE);
                posCol = rand.nextInt(BOARD_SIZE);

            }while(snakeCells.contains(cellList[posRow][posCol]) || posRow == 0 || posCol == 0); //if selected cell is snake

            cell = cellList[posRow][posCol]; //gets atts of cell currently selected
            //changes type to food and changes appearance to activated char
            cell.type = STRING_CONSTANTS.TYPE_FOOD;
            cell.changeAppearance(STRING_CONSTANTS.TYPE_FOOD);
        }catch(Exception e){
            ErrorPrinter.setDetails("[METHOD]: createFood();",false);

            //checks if the issue is a general error or a positioning one specifically so the error output changes
            try{System.out.println(cellList[posRow][posCol]);
            }catch(Exception ex){
                ErrorPrinter.setDetails("\n[ROW]: "+posRow+"\n[COL]: "+posCol,true);
                ErrorPrinter.errorHandler("ERR_BR_CELL_OOB", e);
            }

            ErrorPrinter.errorHandler("ERR_BR_GENERIC", e);
        }
    }

    public static int getBoardSize(){return BOARD_SIZE;}

    //class manages attributes for individual cells
    public static final class Cell{
        int ROW; //this is the location data and is effectively rows+col. this is used because you have to run the values through a map to sort by an element and running 2 maps for rows and cols is lag-inducing
        int COLUMN;
        int age; //used to determine which cell is cleared when the snake moves
        STRING_CONSTANTS type; //accepted params are in the enum
        JTextField cellField = new JTextField();

        //constructor method used for initialization: sets X/Y position
        //"method" is used for more accurate error handling. u wanna know what method tried to make a cell
        private Cell(int row, int col, String method){
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
                ErrorPrinter.setDetails("[METHOD]: "+method,false);
                ErrorPrinter.printCellAtts(this);

                //a nested try/catch is required to check if the issue is that the cell is out-of-bounds or if it is a more general issue
                try{System.out.println(cellList[row][col]);  //we print cellList[row][cell] here for the try (to check if accessing a cell at that index would crash) because it requires nothing to be initialized
                }catch(Exception ex){ErrorPrinter.errorHandler("ERR_BR_CELL_OOB", e);}

                ErrorPrinter.errorHandler("ABN_BR_CELL_UNDER_CONSTRUXION", e);
            }
        }

        void changeAppearance(STRING_CONSTANTS type){
            this.cellField.setBackground(statusColorsConstants(type));
            this.cellField.repaint();
            this.cellField.revalidate();
        }

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
