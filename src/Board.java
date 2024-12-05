import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Board extends Main.GameManager{
    //init var
    protected static ArrayList<Cell> snakeCells = new ArrayList<>(); //has all the cellsssss that are part of the snake in them
    public static Cell[][] cellList = new Cell[Main.INT_CONSTANTS.BOARD_SIZE.value][Main.INT_CONSTANTS.BOARD_SIZE.value]; //adds +1 because positions start at 1

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
            for(int row = 1; row < Main.INT_CONSTANTS.BOARD_SIZE.value; row++){for(int col = 1; col < Main.INT_CONSTANTS.BOARD_SIZE.value; col++){new Cell(row, col);}}
            repaintPanels();
        }  //creates a cell object for each row and age of 0
    }

    Board(){} //this looks dumb but its required bc snake inherits board and it needs to have a parameterless constructor

    //decreases age of all cells by 1 and removes any cells with an age of zero
    protected static void cellAgeDeprecation(){
        ArrayList<Cell> cellsToRemove = new ArrayList<>();
        for(Cell currentCell:snakeCells){
            //depreciates age by 1
            currentCell.age--;

            //if 0, turn back into a regular board cell
            if (currentCell.age <= 0) {
                currentCell.type = STRING_CONSTANTS.TYPE_FIELD;
                currentCell.changeAppearance(false); //sets appearance to regular ass cell LOL
                cellsToRemove.add(currentCell);
            }
        }
        //loops through and removes all cells from cellList. u have to do this seperately instead of calling snakeCells.remove(Currentcell) bc that would give an exception cuse u cant edit an array while actively loopin thru it
        for(Cell cell:cellsToRemove){snakeCells.remove(cell);}
    }

    protected static void createFood(){
        Random rand = new Random(); //gets random class to call random cell pos
        int posRow = rand.nextInt(Main.INT_CONSTANTS.BOARD_SIZE.value);
        int posCol = rand.nextInt(Main.INT_CONSTANTS.BOARD_SIZE.value);

        Cell cell = cellList[posRow][posCol]; //inits cell

            while (snakeCells.contains(cell) || posRow ==0 || posCol == 0) { //if selected cell is snake
                posRow = rand.nextInt(Main.INT_CONSTANTS.BOARD_SIZE.value);
                posCol = rand.nextInt(Main.INT_CONSTANTS.BOARD_SIZE.value);
            }

        cell = cellList[posRow][posCol]; //gets atts of cell currently selected

        //changes type to food and changes appearance to activated char
        cell.type = STRING_CONSTANTS.TYPE_FOOD;
        cell.changeAppearance(true);
    }

    //class manages attributes for individual cells
    static class Cell{
        boolean status = false; //if FALSE: cell is represented by an O | if TRUE: cell is an X
        int ROW; //this is the location data and is effectively rows+col. this is used because you have to run the values through a map to sort by an element and running 2 maps for rows and cols is lag-inducing
        int COLUMN;
        int age; //used to determine which cell is cleared when the snake moves
        STRING_CONSTANTS type; //accepted params are in the enum
        JTextField cellField = new JTextField();

        //constructor method used for initialization: sets X/Y position
        private Cell(int row, int col) {
            if (Main.init){
                ROW = row;
                COLUMN = col;
                this.type = STRING_CONSTANTS.TYPE_FIELD;
                this.age = 0;
                this.changeAppearance(false);
                cellField.setEditable(false);

                cellList[row][col] = this;
                setCell(cellField);
            }
        }

        protected void changeAppearance(boolean status){
            this.cellField.setBackground(statusColorsConstants(status));
            this.cellField.repaint();
            this.cellField.revalidate();
        }

        //sets color depending on input status
        private Color statusColorsConstants(boolean status){
            this.status = status;
            //intellij says "ermmm aktually u can just use an if statement here" WRONG if i do an if statement ill have to type return TWICE for both colors and this LOOKS CLEANER
            return switch(status){
                case true -> Color.BLACK;
                default -> Color.WHITE;
            };
        }
    }
}
