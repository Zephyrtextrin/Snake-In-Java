import javax.swing.*;
import java.util.*;
import java.util.Timer;

public class Main{
    protected static JLabel display = new JLabel();
    protected static JPanel panel = new JPanel();

    //sets up frame, initializes some constructors, and runs method that actually makes the game work
    public static void main(String[] args) {
        final int WINDOW_SIZE = 220; //controls size of all panels and frames
        //changes l&f to windows classic because im a basic bitch like that
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows Classic".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {System.out.println("error with look and feel!\n------DETAILS------\n"+e.getMessage());}


        // create a window
        JFrame frame = new JFrame("text-based snake in java+swing");
        frame.setSize(WINDOW_SIZE, WINDOW_SIZE);

        //adds panel
        frame.add(panel);
        panel.setBounds(0, 0, WINDOW_SIZE, WINDOW_SIZE);
        frame.setResizable(false);

        //adds jlabel that does the things
        display.setBounds(0,0,WINDOW_SIZE,WINDOW_SIZE);
        panel.add(display);

        new Board(); //inits cell values

        runGame(panel); //runs the method that actually makrs the gamr work
        frame.setVisible(true);


    }

    //manages game
    private static void runGame(JPanel panel){
        final int FPS = 1000/4; //how often the frame refreshes, in MILLISECONDS (this is 1000/4 instead of just 250 bc its easier to work with)
        Timer timer = new Timer(); //new timer instance
        Snake.updateMovement(); //inits snake at positiion of 1

        /*timer that goes off every frame or evry 2 frames idk
        //if()else{
        //if player input is up set snake.direction to up and obv do that for all directions
        //if direction is left/right change MODIFIER by 1 but if direction is up/down change by BOARD_SIZE
        //end ifelse
        //snake.position += MODIFIER*/

        //method that gets called every (milliseconds defined in FPS variable) makes the snake move and shit
        TimerTask snakeMovement = new TimerTask() {
            @Override
            public void run() {
                Snake.position++;
                Snake.updateMovement();
                Board.cellAgeDeprecation();
            }
        };

        timer.scheduleAtFixedRate(snakeMovement,0,FPS);

    }

    protected static class Board extends Main{
        //init var
        final static protected int BOARD_SIZE = 10;
        //protected ArrayList<Cell> allCells = new ArrayList<>(); //arraylist of all the cells
        protected static Map<Integer, Cell> cellsByPosition = new HashMap<>(); //contains all the cells and their position so u can get a specific cell by finding them in the map via positional value
        protected static ArrayList<Cell> snakeCells = new ArrayList<>(); //has all the cellsssss that are part of the snake in them
        private static final int CELL_COUNT = (int) Math.pow(BOARD_SIZE, 2);

        //constructor that initializes all cell values into the board
        protected Board() {
            for (int position = 1; position <= CELL_COUNT; position++) {new Cell(position);} //creates a cell object for each position and age of 0

            drawBoard(panel, display);
        }

        //redraws the board tbh //EDIT: bro what was i thinking this is the worst comment of all time
        public static void drawBoard(JPanel panel, JLabel display) {
            StringBuilder toDisplay = new StringBuilder();

            toDisplay.append("<html>"); //NGL I HAF NP IDEA HTML WAS POSSIBLE IN SWING AND I ONLY KNOW IT BECAUSE I HAD TO ADD LINEBREAKS TO THIS JLABEL
            for (int position = 1; position <= CELL_COUNT; position++) {
                Cell targetCell = cellsByPosition.get(position);
                String input = " "+targetCell.appearance+" ";

                toDisplay.append(input);
                if (position % 10 == 0) {
                    toDisplay.append("<br>"); //adds new row
                }

                //delete youngest cell

            }
            toDisplay.append("</html>"); //NGL I HAF NP IDEA HTML WAS POSSIBLE IN SWING AND I ONLY KNOW IT BECAUSE I HAD TO ADD LINEBREAKS TO THIS JLABEL

            display.setText(String.valueOf(toDisplay)); //sets display text to
            panel.repaint();
            panel.revalidate();
        }

        //updates a specific cell based on specified status and position
        protected static void updateCell (boolean status, int position, boolean type){
            Cell targetCell = cellsByPosition.get(position);
            targetCell.changeAppearance(status);
            if(type){targetCell.snakeCellsManagement(targetCell);} //calls snakeCellsManagement method if the changeAppearance method's being called in regards to the snake class
        }

        //decreases age of all cells by 1 and removes any cells with an age of zero
        protected static void cellAgeDeprecation(){
            for(int i = 0; i<snakeCells.size(); i++){
                Cell currentCell = snakeCells.get(i);
                //depreciates age by 1
                currentCell.age--;

                //if 0, turn back into a regular board cell
                if(currentCell.age<=0){
                    currentCell.type = false;
                    currentCell.changeAppearance(false); //sets appearance to regular ass cell LOL
                    snakeCells.remove(i);
                    System.out.println("POS "+ currentCell.POSITION+"AGE "+currentCell.age);
                }
            }
        }

        //class manages attributes for individual cells
        protected static final class Cell {
            boolean status = false; //if FALSE: cell is represented by an O | if TRUE: cell is an X
            char appearance; //actual display of the cell
            int POSITION; //this is the location data and is effectively rows+col. this is used because you have to run the values through a map to sort by an element and running 2 maps for rows and cols is lag-inducing
            int age; //used to determine which cell is cleared when the snake moves
            boolean type; //true if snake cell, false if else

            //constructor method used for initialization: sets X/Y position
            private Cell(int position){
                POSITION = position;
                this.appearance = changeAppearance(false);
                this.type = false;
                this.age = 0;

                //allCells.add(this);
                cellsByPosition.put(POSITION, this);
            }

            //method used to change appearance of cell based on status
            private char changeAppearance(boolean status){ //true if snake false if no\t
                //active/inactive vars may not be necessary but it's nice to have them easily configurable
                final char ACTIVE = 'X'; //active/inactive appearances for each cell (active is if there's a snake/food on that tile)
                final char INACTIVE = 'O';
                this.status = status;

                //idk if they should be formatted like this but it looks nicer
                if(status){appearance = ACTIVE;}else{appearance = INACTIVE;}

                return appearance;
            }

            //adds cells to snakeCell list
            private void snakeCellsManagement(Cell targetCell){
                targetCell.type = true;
                targetCell.age = Snake.length;
                snakeCells.add(this);
            }
        }
    }

    //holds data for snake
    public static class Snake extends Board{
        static int length = 4;
        String direction = "RIGHT";
        static int position = 1; //thithe position of the cell the snake's head is in

        public static void updateMovement(){
            Board.updateCell(true, position, true); //snake turns the tile it's on into it's activated appearance
            drawBoard(panel,display); //updates board
        }
    }
}