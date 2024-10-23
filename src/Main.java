import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        final int BOARD_SIZE = 10;
        for(int column = 0; column <= BOARD_SIZE; column++) {
            for (int row = 0; row <= BOARD_SIZE; row++) {
                new Cell(column, row); //initializes and prints required cells
                //bruh turn this into a method in the board class or something
            }
            System.out.println();//makes new line
        }
    }


        protected class Board{
            protected ArrayList<Cell> allCells = new ArrayList<>();


            protected final class Cell { //each individual cell in the board
                boolean status = false; //if FALSE: cell is represented by an O | if TRUE: cell is an X
                int ROW = 0; //this feels like they should be final tbh but idk how that would work
                int COLUMN = 0; //this feels like they should be final tbh but idk how that would work
                char appearance; //actual display of the cell

                //constructor method used for initialization: sets X/Y pos
                protected Cell(int X, int Y){
                    ROW = Y;
                    COLUMN = X;
                    this.appearance = changeAppearance(false);

                    allCells.add(this);
                }

                //constructor used to change appearance of cell based on status
                private char changeAppearance(boolean status){
                    this.status = status;
                    final char ACTIVE = 'X';
                    final char INACTIVE = 'O';

                    if(status){appearance = ACTIVE;
                    }else{appearance = INACTIVE;}
                    return appearance;
                }
        }
    }
}