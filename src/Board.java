import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Board extends GameManager{
    //init var
    final protected static ArrayList<Cell> snakeCells = new ArrayList<>(); //has all the cellsssss that are part of the snake in them
    final public static Cell[][] cellList = new Cell[GameUI.INT_CONSTANTS.BOARD_SIZE.value][GameUI.INT_CONSTANTS.BOARD_SIZE.value]; //adds +1 because positions start at 1
    public static Color FIELD_COLOR = Color.WHITE;
    public static Color SNAKE_COLOR = Color.BLACK;
    public static Color FOOD_COLOR = Color.RED;

    public enum STRING_CONSTANTS {
        //TYPE VALUES: allows you to set celltypes without using direct strings and ensures no compatibility issues
        TYPE_FIELD,
        TYPE_SNAKE,
        TYPE_FOOD
    }

    public static void initCells(){
        for(int row = 1; row < GameUI.INT_CONSTANTS.BOARD_SIZE.value; row++){for(int col = 1; col < GameUI.INT_CONSTANTS.BOARD_SIZE.value; col++){new Cell(row, col);}}
        GameUI.repaintPanels();
        }

    protected static void createFood(){
        Cell cell;
        try {
            Random rand = new Random(); //gets random class to call random cell pos
            int posRow = rand.nextInt(GameUI.INT_CONSTANTS.BOARD_SIZE.value);
            int posCol = rand.nextInt(GameUI.INT_CONSTANTS.BOARD_SIZE.value);

            while (snakeCells.contains(cellList[posRow][posCol]) || posRow == 0 || posCol == 0) { //if selected cell is snake
                posRow = rand.nextInt(GameUI.INT_CONSTANTS.BOARD_SIZE.value);
                posCol = rand.nextInt(GameUI.INT_CONSTANTS.BOARD_SIZE.value);
            }

            cell = cellList[posRow][posCol]; //gets atts of cell currently selected

            //changes type to food and changes appearance to activated char
            cell.type = STRING_CONSTANTS.TYPE_FOOD;
            cell.changeAppearance(STRING_CONSTANTS.TYPE_FOOD);
        }catch(Exception e){
            ErrorPrinter.errorHandler("ERR_BR_FOOD_OOB");
        }
    }

    //class manages attributes for individual cells
    static class Cell{
        int ROW; //this is the location data and is effectively rows+col. this is used because you have to run the values through a map to sort by an element and running 2 maps for rows and cols is lag-inducing
        int COLUMN;
        int age; //used to determine which cell is cleared when the snake moves
        STRING_CONSTANTS type; //accepted params are in the enum
        JTextField cellField = new JTextField();

        //constructor method used for initialization: sets X/Y position
        private Cell(int row, int col){
            ROW = row;
            COLUMN = col;
            this.type = STRING_CONSTANTS.TYPE_FIELD;
            this.age = 0;
            this.changeAppearance(STRING_CONSTANTS.TYPE_FIELD);
            cellField.setEditable(false);
            cellField.setFocusable(false);

            cellList[row][col] = this;
            GameUI.setCell(cellField);
        }

        protected void changeAppearance(STRING_CONSTANTS type){
            this.cellField.setBackground(statusColorsConstants(type));
            this.cellField.repaint();
            this.cellField.revalidate();
        }

        //sets color depending on input status
        private Color statusColorsConstants(STRING_CONSTANTS type){
            this.type = type;
            //intellij says "ermmm aktually u can just use an if statement here" WRONG if i do an if statement ill have to type return TWICE for both colors and this LOOKS CLEANER
            return switch(type){
                case TYPE_FOOD -> FOOD_COLOR;
                case TYPE_SNAKE -> SNAKE_COLOR;
                case TYPE_FIELD -> FIELD_COLOR;
            };
        }
    }
}
