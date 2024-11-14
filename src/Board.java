import java.util.ArrayList;
import java.util.Random;

public class Board extends Main.GameManager{
    //init var
    protected static ArrayList<Cell> snakeCells = new ArrayList<>(); //has all the cellsssss that are part of the snake in them
    public static Cell[] cellList = new Cell[Main.INT_CONSTANTS.CELL_COUNT.value+1]; //adds +1 because positions start at 1

    public enum STRING_CONSTANTS {
        //TYPE VALUES: allows you to set celltypes without using direct strings and ensures no compatibility issues
        TYPE_FIELD,
        TYPE_SNAKE,
        TYPE_FOOD
    }

    //inits values or updates snake depending on bool
    Board(boolean init){
        if(init){ //frame-by-frame snake management and redraws
            cellAgeDeprecation();
            updateDisplayLabel(drawBoard());
        }else{ //inits all cells into board
            for(int position = 1; position <= Main.INT_CONSTANTS.CELL_COUNT.value; position++){new Cell(position);} //creates a cell object for each position and age of 0
            updateDisplayLabel(drawBoard());
        }
    }

    //redraws the board tbh //EDIT: bro what was i thinking this is the worst comment of all time
    public StringBuilder drawBoard(){

        StringBuilder toDisplay = new StringBuilder();

        toDisplay.append("<html>"); //NGL I HAF NP IDEA HTML WAS POSSIBLE IN SWING AND I ONLY KNOW IT BECAUSE I HAD TO ADD LINEBREAKS TO THIS JLABEL
        for(int position = 1; position <= Main.INT_CONSTANTS.CELL_COUNT.value; position++) {
            Cell targetCell = cellList[position];
            String input = " " + targetCell.appearance + " ";

            toDisplay.append(input);
            if(position%Main.INT_CONSTANTS.BOARD_SIZE.value == 0){toDisplay.append("<br>");} //adds new row
        }
        toDisplay.append("</html>");

        return toDisplay;
    }

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
        Cell targetCell = cellList[rand.nextInt(Main.INT_CONSTANTS.CELL_COUNT.value) + 1]; //inits to placeholder cell

        while (snakeCells.contains(targetCell)) { //if selected cell is snake
            int position = rand.nextInt(Main.INT_CONSTANTS.CELL_COUNT.value) + 1; //must be ++ because rolls start at 0
            targetCell = cellList[position]; //gets atts of cell currently selected
        }

        //changes type to food and changes appearance to activated char
        targetCell.type = STRING_CONSTANTS.TYPE_FOOD;
        targetCell.changeAppearance(true);
    }

    //class manages attributes for individual cells
    protected static class Cell{
        boolean status = false; //if FALSE: cell is represented by an O | if TRUE: cell is an X
        char appearance; //actual display of the cell
        int POSITION; //this is the location data and is effectively rows+col. this is used because you have to run the values through a map to sort by an element and running 2 maps for rows and cols is lag-inducing
        int age; //used to determine which cell is cleared when the snake moves
        STRING_CONSTANTS type; //accepted params are in the enum

        //constructor method used for initialization: sets X/Y position
        private Cell(int position){
            POSITION = position;
            this.appearance = changeAppearance(false);
            this.type = STRING_CONSTANTS.TYPE_FIELD;
            this.age = 0;

            //allCells.add(this);
            cellList[position] = this;
        }

        //method used to change appearance of cell based on status
        char changeAppearance(boolean status) { //true if snake false if no\t
            //active/inactive vars may not be necessary but it's nice to have them easily configurable
            final char ACTIVE = '■'; //active/inactive appearances for each cell (active is if there's a snake/food on that tile)
            final char INACTIVE = '☐';
            this.status = status;

            //idk if they should be formatted like this but it looks nicer
            if(status){appearance = ACTIVE;
            }else{appearance = INACTIVE;}

            return appearance;
        }
    }
}
