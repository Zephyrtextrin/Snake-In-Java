import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Board extends GameManager{
    //init var
    final protected static ArrayList<Cell> snakeCells = new ArrayList<>(); //has all the cellsssss that are part of the snake in them
    final public static Cell[][] cellList = new Cell[GameUI.INT_CONSTANTS.BOARD_SIZE.value][GameUI.INT_CONSTANTS.BOARD_SIZE.value]; //adds +1 because positions start at 1
    final private static ArrayList<Cell> cellsToRemove = new ArrayList<Cell>();
    public enum STRING_CONSTANTS {
        //TYPE VALUES: allows you to set celltypes without using direct strings and ensures no compatibility issues
        TYPE_FIELD,
        TYPE_SNAKE,
        TYPE_FOOD
    }

    //inits values or updates snake depending on bool
    Board(boolean init){
        if(!init){cellAgeDeprecation();
        }else{ //inits all cells into board
            for(int row = 1; row < GameUI.INT_CONSTANTS.BOARD_SIZE.value; row++){for(int col = 1; col < GameUI.INT_CONSTANTS.BOARD_SIZE.value; col++){new Cell(row, col);}}
            GameUI.repaintPanels();
        }  //creates a cell object for each row and age of 0
    }

    Board(){} //this looks dumb but its required bc snake inherits board and it needs to have a parameterless constructor

    //decreases age of all cells by 1 and removes any cells with an age of zero
    protected static void cellAgeDeprecation(){
        if(snakeCells.size()>Snake.length){
            Cell gone = snakeCells.get(0);
            gone.changeAppearance(STRING_CONSTANTS.TYPE_FIELD); //sets appearance to regular ass cell LOL
            snakeCells.remove(gone);
        }
    }

    protected static void createFood(){
        Random rand = new Random(); //gets random class to call random cell pos
        int posRow = rand.nextInt(GameUI.INT_CONSTANTS.BOARD_SIZE.value);
        int posCol = rand.nextInt(GameUI.INT_CONSTANTS.BOARD_SIZE.value);

        Cell cell = cellList[posRow][posCol]; //inits cell

            while (snakeCells.contains(cell) || posRow ==0 || posCol == 0) { //if selected cell is snake
                posRow = rand.nextInt(GameUI.INT_CONSTANTS.BOARD_SIZE.value);
                posCol = rand.nextInt(GameUI.INT_CONSTANTS.BOARD_SIZE.value);
            }

        cell = cellList[posRow][posCol]; //gets atts of cell currently selected

        //changes type to food and changes appearance to activated char
        cell.type = STRING_CONSTANTS.TYPE_FOOD;
        cell.changeAppearance(STRING_CONSTANTS.TYPE_FOOD);
    }

    //class manages attributes for individual cells
    static class Cell{
        boolean status = false; //if FALSE: cell is represented by an O | if TRUE: cell is an X
        int ROW; //this is the location data and is effectively rows+col. this is used because you have to run the values through a map to sort by an element and running 2 maps for rows and cols is lag-inducing
        int COLUMN;
        int age; //used to determine which cell is cleared when the snake moves
        STRING_CONSTANTS type; //accepted params are in the enum
        JTextField cellField = new JTextField();
        private Color FIELD_COLOR = Color.WHITE;
        private Color SNAKE_COLOR = Color.BLACK;
        private Color FOOD_COLOR = Color.RED;

        //constructor method used for initialization: sets X/Y position
        private Cell(int row, int col) {
            ROW = row;
            COLUMN = col;
            this.type = STRING_CONSTANTS.TYPE_FIELD;
            this.age = 0;
            this.changeAppearance(STRING_CONSTANTS.TYPE_FIELD);
            cellField.setEditable(false);

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
                case TYPE_FOOD -> Color.RED;
                case TYPE_SNAKE -> Color.BLACK;
                case TYPE_FIELD -> Color.WHITE;
            };
        }
    }
}
