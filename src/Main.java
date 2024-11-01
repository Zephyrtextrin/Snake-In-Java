import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {

    public static JLabel display = new JLabel();

    public enum STRING_CONSTANTS{
        //TYPE VALUES: allows you to set celltypes without using direct strings and ensures no compatibility issues
        TYPE_FIELD("tile"),
        TYPE_SNAKE("snake"),
        TYPE_FOOD("food");
        private final String value;

        STRING_CONSTANTS(String type){this.value = type;}
    }

    public enum INT_CONSTANTS{
        //INTEGERS: values that make the game work
        BOARD_SIZE(20), 
        WINDOW_SIZE(22*BOARD_SIZE.value), 
        CELL_COUNT((int) Math.pow(BOARD_SIZE.value, 2));
        private final int value;

        //constructor for strings (all type vaues)
        INT_CONSTANTS(int value){this.value = value;}
    }

    //sets up frame, initializes some constructors, and runs method that actually makes the game work
    public static void main(String[] args) {
        //changes l&f to windows classic because im a basic bitch like that
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows Classic".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch (Exception e){System.out.println("error with look and feel!\n------DETAILS------\n" + e.getMessage());}

        GameManager game = new GameManager(true); //creates new instance of game manager
        //new Board(); //inits cell values
        //CREATES UI VALUES

        //adds jlabel that does the things
        display.setBounds(0, 0, INT_CONSTANTS.WINDOW_SIZE.value, INT_CONSTANTS.WINDOW_SIZE.value);
        game.panel.add(display);
    }

    //starts and stops game, initializes variables
    protected static class GameManager {
        JPanel panel = new JPanel();
        JFrame frame = new JFrame("text-based snake in java+swing");
        JButton playAgain = new JButton("Play again");

        //plays the game again
        static boolean gameStatus = true;


        //CONSTRUCTOR
        GameManager(boolean gameStatus){

            if(gameStatus) {runGame();
            }else{stopGame();}
        }

        GameManager(){} //this feels stupid but sometimes they dont want params in a constructor

        //manages game; initializes variables and sets timer
        private void runGame() {
            //UI
            // create a window IM SOOOOOOOOOOO TIRED IDC NO MORE ILL FORMAT THIS STUPID UI BS TOMORROW
            gameStatus = true;

            frame.setSize(INT_CONSTANTS.WINDOW_SIZE.value, INT_CONSTANTS.WINDOW_SIZE.value);

            //adds panel
            frame.add(panel);
            panel.setBounds(0, 0, INT_CONSTANTS.WINDOW_SIZE.value, INT_CONSTANTS.WINDOW_SIZE.value);
            frame.setResizable(false);
            new Board(false); //inits cells


            //inits the button to play again
            //inivisible before initialization
            playAgain.setBounds(INT_CONSTANTS.WINDOW_SIZE.value / 2, INT_CONSTANTS.WINDOW_SIZE.value / 2, INT_CONSTANTS.WINDOW_SIZE.value / 3, INT_CONSTANTS.WINDOW_SIZE.value / 5);
            panel.add(playAgain);
            playAgain.setVisible(false);

            frame.setVisible(true);

            //RUNS GAME METHODS
            final int[] pressedKey = new int[1]; //WHA THE ACTUAL freak IS INTELLIJ SMART SOLUTIONS MAKING MY CODE DO WHI IS THIS A FINAL INT ARRAY???
            //IDK WHAT THIS DOES INTELLIJ JUST ADDED IT
            frame.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    Snake.changeDirection(e.getKeyCode());
                }
            });

            final int FPS = 150; //how often the frame refreshes, in MILLISECONDS (*9 is for debug only, usually 150 in normal play)
            Timer timer = new Timer(); //new timer instance
            Snake.updateMovement(); //inits snake at positiion of 1

            Board.createFood(); //initializes food item
            //method that gets called every (milliseconds defined in FPS variable) makes the snake move and shit
            TimerTask snakeMovement = new TimerTask() {
                @Override
                public void run() {

                    if(gameStatus){Snake.changeDirection(pressedKey[0]);
                    }else{stopGame();}
                }
            };

            timer.scheduleAtFixedRate(snakeMovement, 0, FPS);

        }

        public void stopGame() {
            gameStatus = false;
            display.setText("GAME OVER!");
            playAgain.setVisible(true);
            playAgain.addActionListener(_ -> runGame()); //if clicked, play the game again
        }

        public void updateDisplayLabel(StringBuilder toDisplay){
            display.setText(String.valueOf(toDisplay)); //sets display text to the drawn board
            //System.out.println(display.getText());
            panel.repaint();
            panel.revalidate();
        }
    }


    //holds data for snake
    public static class Snake extends Board {
        static int length = 1;
        static Direction direction = Direction.RIGHT;
        static int position = 1; //thithe position of the cell the snake's head is in
        static int modifier = direction.value; //how mmany cells the snake will move by (aka: the direction)

        private enum Direction {
            //init var
            //sets values for each direction
            UP(-INT_CONSTANTS.BOARD_SIZE.value), DOWN(INT_CONSTANTS.BOARD_SIZE.value), LEFT(-1), RIGHT(1);
            private final int value;

            //sets value according to input
            Direction(int value){this.value = value;}
        }

        public static void updateMovement() {
            if(checkBorders()) {
                Cell targetCell=cellList[position];
                //System.out.println(modifier);
                snakeCellsManagement(targetCell); //calls snakeCellsManagement method to add the cell into the list of snake cells
                //position+=modifier; //makes the snake advance by however many tiles the direction needs them to advance in [EDIT: BUGGED DO NOT USE]
                new Board();
            }
        }

        //checks to see if player ran into a wall
        private static boolean checkBorders() {
            Cell targetCell = cellList[position];
            //final boolean check = isCheck();
            final boolean check = isCheck();
            final boolean ego = Objects.equals(targetCell.type, STRING_CONSTANTS.TYPE_SNAKE.value); //is snake eating itself
            if(check||ego){
                //System.out.println("\nIS SNAKE EATING ITSELF "+ego+"\nIS SNAKE HITTING A BORDER "+check);
                new GameManager(false);
                return false;
            }

            return true;
        }

        //idk why this method is here intellij was givin me a warnin like "u could wrap this in a method" and so i clicked quick fix and it did this so idc
        private static boolean isCheck() {
            int posLocal = position-1;
            int pastPos = posLocal-modifier;
            if(pastPos<=0){pastPos=1;}
            if(posLocal<=0){posLocal=1;} //prevent invalid cells or negative values
            final boolean horizontal = direction.equals(Direction.LEFT) || direction.equals(Direction.RIGHT);
            final int column = posLocal%INT_CONSTANTS.BOARD_SIZE.value; //gets the current column of the snake by dividing the position by the board size and getting the remainder
            final int row = posLocal/INT_CONSTANTS.BOARD_SIZE.value; //gets the current row of the snake by dividing the position of the board size and truncating any decimal slots
            final int lastRow = pastPos/INT_CONSTANTS.BOARD_SIZE.value; //these lastRow/Col vars are not neccessary you can just use an entire statement for the if-statements but this is more readable
            final int lastCol = pastPos%INT_CONSTANTS.BOARD_SIZE.value;

            //VERY LONG DEBUG STRING DO NOT ENABLE UNLESS TESTING POSITIONING OR GAMEOVER CONDIITONALS
            System.out.println("----------------------------\nCURRENT ROW: "+row+" PAST ROW: "+ lastRow +"\nCURRENT COLUMN: "+column+" PAST COLUMN: "+ lastCol +"\nCURRENT POS: "+posLocal+" PAST POS:  "+pastPos+"\nDIRECTION: "+direction+" horizontal: "+ horizontal +"\nMODIFIER: "+modifier+"\n----------------------------");

            //this is horizontal border check. vertical check must be performed prior because it causes exception errors due to invalid cell #s
            boolean check = horizontal&&lastRow!=row;
            return check;
        }


        //contains the opposite direction for each key input (so u dont hit left key while going right and u move inside of yourself and instalose)
        private static final Map<Integer, Direction> directionMap = Map.of(
                KeyEvent.VK_RIGHT, Direction.RIGHT,
                KeyEvent.VK_LEFT, Direction.LEFT,
                KeyEvent.VK_UP, Direction.UP,
                KeyEvent.VK_DOWN, Direction.DOWN
        );

        private static void changeDirection(int key) {
            Direction newDirection = directionMap.get(key);
            //System.out.println("NEW DIR: "+newDirection);
            if (newDirection != null && !newDirection.equals(oppositeDirection(direction))) {
                direction = newDirection; //updates to name of direction value
                modifier = direction.value; //updartes to value of direction enum
                //System.out.println("DIRECTION: "+direction);
                //System.out.println("MOD: "+modifier);
            }
            int nextPos = position+modifier;

            if(nextPos<0||nextPos>INT_CONSTANTS.BOARD_SIZE.value){ //ensures the snake will not move if it would result in an invalid cell
                //this check must be done before the bordercheck is performed because otherwise it would cause other issues such as the snake "eating" its own head
                position += modifier; //position must be changed here instead of in the updatemovement method because there was an issue where inputs would be behind by one frame advancement, since they used the modifier from the previous frame
                updateMovement();
            }
        }

        //CHAT... IM A GENIUS!!
        private static Direction oppositeDirection(Direction direction) {
            return switch (direction) {
                case UP -> Direction.DOWN;
                case DOWN -> Direction.UP;
                case LEFT -> Direction.RIGHT;
                case RIGHT -> Direction.LEFT;
            };
        }

        //adds cells to snakeCell list
        private static void snakeCellsManagement(Cell targetCell) {
            targetCell.changeAppearance(true); //changes target cell into its activated appearance (since snake cells are the activated appearance of a shaded-in block
                if (Objects.equals(targetCell.type, STRING_CONSTANTS.TYPE_FOOD.value)) { //this looks incredibly dumb but you have to have this if statement inside the else
                    Snake.length++;
                    Board.createFood();
                }

                targetCell.type = STRING_CONSTANTS.TYPE_SNAKE.value;
                targetCell.age = Snake.length+1; //add one because the cells would immediately get depreciated to (length-1)
                snakeCells.add(targetCell);
            }
        }

    protected static class Board extends GameManager {
        //init var
        //protected ArrayList<Cell> allCells = new ArrayList<>(); //arraylist of all the cells
        protected static ArrayList<Cell> snakeCells = new ArrayList<>(); //has all the cellsssss that are part of the snake in them
        public static Cell[] cellList = new Cell[INT_CONSTANTS.CELL_COUNT.value+1]; //adds +1 because positions start at 1

        private Board(){
            cellAgeDeprecation();
            updateDisplayLabel(drawBoard());
        }

        //constructor that initializes all cell values into the board
        //there's a stupid useless param here that does nothing bc the board already has a paramless method and the snake class needs to call these methods
        protected Board(boolean isInitialized) {
            for (int position = 1; position <= INT_CONSTANTS.CELL_COUNT.value; position++){new Cell(position);} //creates a cell object for each position and age of 0

            updateDisplayLabel(drawBoard());
        }

        //redraws the board tbh //EDIT: bro what was i thinking this is the worst comment of all time
        public StringBuilder drawBoard() {


            StringBuilder toDisplay = new StringBuilder();

            toDisplay.append("<html>"); //NGL I HAF NP IDEA HTML WAS POSSIBLE IN SWING AND I ONLY KNOW IT BECAUSE I HAD TO ADD LINEBREAKS TO THIS JLABEL
            for (int position = 1; position <= INT_CONSTANTS.CELL_COUNT.value; position++) {
                Cell targetCell = cellList[position];
                String input = " " + targetCell.appearance + " ";

                toDisplay.append(input);
                if (position % INT_CONSTANTS.BOARD_SIZE.value == 0) {
                    toDisplay.append("<br>"); //adds new row
                }
            }
            toDisplay.append("</html>");

            return toDisplay;
        }

        //decreases age of all cells by 1 and removes any cells with an age of zero
        protected static void cellAgeDeprecation() {
            ArrayList<Cell> cellsToRemove = new ArrayList<>();
            for(Cell currentCell:snakeCells){
                //depreciates age by 1
                currentCell.age--;

                //if 0, turn back into a regular board cell
                if (currentCell.age <= 0) {
                    currentCell.type = STRING_CONSTANTS.TYPE_FIELD.value;
                    currentCell.changeAppearance(false); //sets appearance to regular ass cell LOL
                    cellsToRemove.add(currentCell);
                }
            }
            //loops through and removes all cells from cellList. u have to do this seperately instead of calling snakeCells.remove(Currentcell) bc that would give an exception cuse u cant edit an array while actively loopin thru it
            for(Cell cell:cellsToRemove){snakeCells.remove(cell);}
        }

        protected static void createFood() {
            Random rand = new Random(); //gets random class to call random cell pos
            Cell targetCell = cellList[rand.nextInt(INT_CONSTANTS.CELL_COUNT.value) + 1]; //inits to placeholder cell

            while (snakeCells.contains(targetCell)) { //if selected cell is snake
                int position = rand.nextInt(INT_CONSTANTS.CELL_COUNT.value) + 1; //must be ++ because rolls start at 0
                targetCell = cellList[position]; //gets atts of cell currently selected
            }

            //changes type to food and changes appearance to activated char
            targetCell.type = STRING_CONSTANTS.TYPE_FOOD.value;
            targetCell.changeAppearance(true);
        }

        //class manages attributes for individual cells
        protected static class Cell {
            boolean status = false; //if FALSE: cell is represented by an O | if TRUE: cell is an X
            char appearance; //actual display of the cell
            int POSITION; //this is the location data and is effectively rows+col. this is used because you have to run the values through a map to sort by an element and running 2 maps for rows and cols is lag-inducing
            int age; //used to determine which cell is cleared when the snake moves
            String type; //accepted params: TILE [regular floor tiles] | SNAKE [snake's body] | FOOD [grow the snake]

            //constructor method used for initialization: sets X/Y position
            private Cell(int position) {
                POSITION = position;
                this.appearance = changeAppearance(false);
                this.type = STRING_CONSTANTS.TYPE_FIELD.value;
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
                if (status) {appearance = ACTIVE;
                }else{appearance = INACTIVE;}

                return appearance;
            }
        }
    }
}
