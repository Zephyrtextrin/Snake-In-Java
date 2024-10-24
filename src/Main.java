import java.util.*;

public class Main{
    final static protected int BOARD_SIZE = 10;

    public static void main(String[] args) {
        new Board(); //initializes the cellList

    }


    protected static class Board{
        protected ArrayList<Cell> allCells = new ArrayList<>(); //arraylist of all the cells
        protected Map<Integer, Cell> cellsByPosition = new HashMap<>(); //contains all the cells and their position so u can get a specific cell by finding them in the map via positional value

        //constructor that initializes all cell values into the board
        protected Board(){
            final int CELL_COUNT = (int) Math.pow(BOARD_SIZE, 2);
            for(int position = 1; position <= CELL_COUNT; position++) {
                new Cell(position);//initializes all cells

                System.out.print(" "+0+" ");
                if(position%10==0){
                    System.out.println(); //adds new row
                }
            }
        }

        //constructor to change a certain cell's position
        protected Board(boolean status, int position){
            Cell targetCell = cellsByPosition.get(position);
            targetCell.changeAppearance(status);
        }

        //class manages attributes for individual cells
        protected final class Cell {
            boolean status = false; //if FALSE: cell is represented by an O | if TRUE: cell is an X
            char appearance; //actual display of the cell
            int POSITION; //this is the location data and is effectively rows+col. this is used because you have to run the values through a map to sort by an element and running 2 maps for rows and cols is lag-inducing

            //constructor method used for initialization: sets X/Y pos
            private Cell(int pos){
                POSITION = pos;
                this.appearance = changeAppearance(false);

                allCells.add(this);
                cellsByPosition.put(POSITION, this);
            }

            //method used to change appearance of cell based on status
            private char changeAppearance(boolean status){
                //these vars may not be necessary but it's nice to have them easily configurable
                this.status = status;
                final char ACTIVE = 'X';
                final char INACTIVE = 'O';

                //idk if they should be formatted like this but it looks nicer
                if(status){appearance = ACTIVE;}else{appearance = INACTIVE;}

                return appearance;
            }
        }
    }

    public static class Snake extends Board{
        int length = 1;
        String direction;
        int position; //this could be the pos of the cell the snake's head is in

        //snake head (likely)
        public Snake(){
            int pos = 1;
            new Board(true, pos); //snake turns the tile it's on into it's activated appearance
        }
    }
}