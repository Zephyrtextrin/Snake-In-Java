import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {
    protected static JLabel display = new JLabel(); //jlabel that displays the game
    protected static JPanel panel = new JPanel(); //panel that the jlabel goes on
    protected static boolean gameStatus = true; //is the game running
    protected static int BOARD_SIZE = 20; //how many rowsXcol the board is
    static final int WINDOW_SIZE = (22 * BOARD_SIZE); //controls size of all panels and frames
    private static final int CELL_COUNT = (int) Math.pow(BOARD_SIZE, 2); //total amount of cells

    static final JButton playAgain = new JButton("Play again"); //plays the game again


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
        } catch (Exception e) {System.out.println("error with look and feel!\n------DETAILS------\n" + e.getMessage());}


        // create a window
        JFrame frame = new JFrame("text-based snake in java+swing");
        frame.setSize(WINDOW_SIZE, WINDOW_SIZE);

        //adds panel
        frame.add(panel);
        panel.setBounds(0, 0, WINDOW_SIZE, WINDOW_SIZE);
        frame.setResizable(false);

        //adds jlabel that does the things
        display.setBounds(0, 0, WINDOW_SIZE, WINDOW_SIZE);
        panel.add(display);

        new Board(); //inits cell values

        //inits the button to play again
        //inivisible before initialization
        playAgain.setBounds(WINDOW_SIZE / 2, WINDOW_SIZE / 2, WINDOW_SIZE / 3, WINDOW_SIZE / 5);
        panel.add(playAgain);
        playAgain.setVisible(false);
        playAgain.addActionListener(e -> runGame(frame)); //if clicked, play the game again

        runGame(frame); //runs the method that actually makrs the gamr work
        frame.setVisible(true);

    }

    //manages game
    private static void runGame(JFrame frame) {
        //init var
        final int[] pressedKey = new int[1]; //WHA THE ACTUAL FUCK IS INTELLIJ SMART SOLUTIONS MAKING MY CODE DO WHI IS THIS A FINAL INT ARRAY???
        Board.createFood(); //initializes food item
        final int FPS = 250; //how often the frame refreshes, in MILLISECONDS
        Timer timer = new Timer(); //new timer instance

        //IDK WHAT THIS DOES INTELLIJ JUST ADDED IT
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                Snake.changeDirection(keyCode);
            }
        });


        //Snake.updateMovement(); //inits snake at positiion of 1

        //method that gets called every (milliseconds defined in FPS variable) makes the snake move and shit
        TimerTask snakeMovement = new TimerTask() {
            public void run() {

                if(gameStatus){Snake.changeDirection(pressedKey[0]);
                }else{stopGame();}
            }
        };

        timer.scheduleAtFixedRate(snakeMovement, 0, FPS);

    }

    public static void stopGame() {
        gameStatus = false;
        display.setText("GAME OVER!");
        playAgain.setVisible(true);
    }

    //holds data for snake
    public static class Snake extends Board {
        static int length = 1;
        static String direction = "RIGHT";
        static int position = 1; //thithe position of the cell the snake's head is in
        static int modifier =1; //how mmany cells the snake will move by (aka: the direction)
        private static int nextPos; //predicts next position via modifier


        public static void updateMovement() {
            if (nextPos>CELL_COUNT&&nextPos<= 0){stopGame(); //ERR HANDLER: if the next position would be out-of-bounds or otherwise invalid, stop the game

            }else{
                position += modifier; //makes the snake advance by however many tiles the direction needs them to advance in
                Cell targetCell = cellsByPosition.get(position);  //this is the current cell that the snake's head is "hovering" over
                nextPos = position + modifier;
                checkBorders(targetCell); //calls snakeCellsManagement method to add the cell into the list of snake cells
                cellAgeDeprecation();
                drawBoard(panel, display);
            }
        }

        //manages the cells that are a part of the snake, and other functions such as increasing length when food is eaten or causing a gameover when hitting the border or yourself
        private static void checkBorders(Cell headCell) {
            //init var
            final int column = position % BOARD_SIZE; //gets the current column of the snake by dividing the position by the board size and getting the remainder
            final int row = position / BOARD_SIZE; //gets the current row of the snake by dividing the position of the board size and truncating any decimal slots
            final int nextRow = nextPos / BOARD_SIZE; //these nextRow/Col vars, CHANGE var, and the HORIZONTAL var are not necessary you can just use an entire statement for the if-statements but this is way more readable
            final int nextCol = nextPos % BOARD_SIZE;
            final boolean horizontal = direction.equals("LEFT") || direction.equals("RIGHT");
            final boolean unauthChange = (horizontal &&nextRow!=row)&&(!horizontal &&nextCol!=column); //so how this works is that if there was no gameover condition when you hit a wall you would appear on the other side of the board on the next row/column so it's checking to see if you're appearing on a new row/col when your current direction logically wouldn't do that (so you had to have hit a border)

            //System.out.println("----------------------------\nCURRENT ROW: "+row+" NEXT ROW: "+nextRow+"\nCURRENT COLUMN: "+column+" NEXT COLUMN: "+nextCol+"\nCURRENT POS: "+position+" NEXT POS:  "+nextPos+"\nDIRECTION: "+direction+" HORIZONTAL: "+HORIZONTAL+"\nMODIFIER: "+modifier+"\n----------------------------");
            //^^^^^^^^ VERY LONG DEBUG MESSAGE ONLY ENABLE TO DEBUG POSITIONING AND GAMEOVER

            //intellij here says that the unath_change var will always be false but it is a liar
            //if you hit yourself or a border, quit the game and force-exit the current method
            if(Objects.equals(headCell.type, "snake")|| unauthChange){stopGame();}

            headCell.changeAppearance(true); //changes target cell into its activated appearance (since snake cells are the activated appearance of a shaded-in block
            if (Objects.equals(headCell.type, "food")) { //this looks incredibly dumb but you have to have this if statement inside the else
                length++;
                Board.createFood();
            }

            headCell.type = "snake";
            headCell.age = Snake.length + 1; //add one because the cells would immediately get depreciated to (length-1)
            snakeCells.add(headCell);

            }



        private static void changeDirection(int key){

            if(gameStatus) {
                //me when code stolen off stackoverflow
                //this feels like it could be improved tbh but idk what
                if (key==KeyEvent.VK_RIGHT&&!Objects.equals(direction, "LEFT")) {
                    direction = "RIGHT";
                    modifier = 1;
                } else if (key==KeyEvent.VK_LEFT&&!Objects.equals(direction, "RIGHT")) {
                    direction = "LEFT";
                    modifier = -1;

                } else if (key==KeyEvent.VK_UP&&!Objects.equals(direction, "DOWN")) {
                    direction = "UP";
                    modifier = -BOARD_SIZE;
                } else if (key==KeyEvent.VK_DOWN&&!Objects.equals(direction, "UP")) {
                    direction = "DOWN";
                    modifier = BOARD_SIZE;
                }

                updateMovement();

            }else{stopGame();}

        }
    }


    //class manages, holds, and initializes data for the entire board
    protected static class Board extends Main {
        //init var
        protected static Map<Integer, Cell> cellsByPosition = new HashMap<>(); //contains all the cells and their position so u can get a specific cell by finding them in the map via positional value
        protected static ArrayList<Cell> snakeCells = new ArrayList<>(); //has all the cellsssss that are part of the snake in them

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
                String input = " " + targetCell.appearance + " ";

                toDisplay.append(input);
                if (position % BOARD_SIZE == 0) {toDisplay.append("<br>");} //adds new row

            }

            toDisplay.append("</html>");

            display.setText(String.valueOf(toDisplay)); //sets display text to the drawn board
            panel.repaint();
            panel.revalidate();
        }

        //decreases age of all cells by 1 and removes any cells with an age of zero
        protected static void cellAgeDeprecation() {
            for (int i = 0; i < snakeCells.size(); i++) {
                Cell currentCell = snakeCells.get(i);
                //depreciates age by 1
                currentCell.age--;

                //if 0, turn back into a regular board cell
                if (currentCell.age <= 0) {
                    currentCell.type = "tile";
                    currentCell.changeAppearance(false); //sets appearance to regular ass cell LOL
                    snakeCells.remove(i);
                }
            }
        }

        protected static void createFood() {
            Random rand = new Random(); //gets random class to call random cell pos
            Cell targetCell = cellsByPosition.get(rand.nextInt(CELL_COUNT) + 1); //inits to placeholder cell

            while (!Objects.equals(targetCell.type, "tile")) { //if selected cell is snake
                int RNGPos = rand.nextInt(CELL_COUNT) + 1; //must be ++ because rolls start at 0
                targetCell = cellsByPosition.get(RNGPos); //gets atts of cell currently selected
            }

            //changes type to food and changes appearance to activated char
            targetCell.type = "food";
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
                this.type = "tile";
                this.age = 0;

                //allCells.add(this);
                cellsByPosition.put(POSITION, this);
            }

            //method used to change appearance of cell based on status
            private char changeAppearance(boolean status) { //true if snake false if no\t
                //active/inactive vars may not be necessary but it's nice to have them easily configurable
                final char ACTIVE = '■'; //active/inactive appearances for each cell (active is if there's a snake/food on that tile)
                final char INACTIVE = '☐';
                this.status = status;

                //idk if they should be formatted like this but it looks nicer
                if (status) {appearance = ACTIVE;
                }else{appearance = INACTIVE;}

                return appearance;
            }

            //adds cells to snakeCell list
            /*private void snakeCellsManagement(Cell targetCell) {
                targetCell.changeAppearance(true); //changes target cell into its activated appearance (since snake cells are the activated appearance of a shaded-in block
                if (Objects.equals(targetCell.type, "snake")) {stopGame();} //ends game if you run into yourself
                else {
                    if (Objects.equals(targetCell.type, "food")) { //this looks incredibly dumb but you have to have this if statement inside the else
                        Snake.length++;
                        createFood();
                    }

                    targetCell.type = "snake";
                    targetCell.age = Snake.length + 1; //add one because the cells would immediately get depreciated to (length-1)
                    snakeCells.add(this);
                }
            }*/
        }
    }
}