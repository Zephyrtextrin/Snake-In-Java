import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*

  [[[---KNOWN-ISSUES---]]]

    *PROBLEM: unable to lose via hitting game edges (you can lose by hitting urself tho)
    *CAUSE: i havent programmed it yet and i genuinely dont know where to start for that lol (unless i reprogrammed how position values are stored and read to rely off row/col instead of one num for the pos)

    *PROBLEM: game is very laggy
    *CAUSE: it's not very well optimized and there's a lot of methods running every frame cause i had bigger fish to fry for a while

    *PROBLEM: the method for making the game end rn feels weird coding-wise and is weird from a gameplay perspective bc u can still make inputs and its weird and glitchy
    *CAUSE: i cant think of any other solution for it rn so it was the only thing i could think of (unless i close the window but that feels weird

  [[[---POSSIBLE-FEATURES---]]]
    add a play again button wheb u lose
    and stats about how long u are
    maybe a high score too where it writes to a txt file your high score every time u lose and then it reads it to set the highscore variable but also that feels like someon can just open it and set it to 999999999999999999 so there would need to be an errchk

 */
public class Main{
    protected static JLabel display = new JLabel();
    protected static JPanel panel = new JPanel();
    protected static boolean gameStatus = true;
    protected static int BOARD_SIZE = 20;
    static final int WINDOW_SIZE = (22*BOARD_SIZE); //controls size of all panels and frames

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

        runGame(frame); //runs the method that actually makrs the gamr work
        frame.setVisible(true);


    }

    //manages game
    private static void runGame(JFrame frame){
        final int[] pressedKey = new int[1]; //WHA THE ACTUAL FUCK IS INTELLIJ MAKING MY CODE DO WHI IS THIS A FINAL INT ARRAY???
        //IDK WHAT THIS DOES INTELLIJ JUST ADDED IT
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                Snake.changeDirection(keyCode);
            }
        });

        final int FPS = 1000/4; //how often the frame refreshes, in MILLISECONDS (this is 1000/4 instead of just 250 bc its easier to work with)
        Timer timer = new Timer(); //new timer instance
        Snake.updateMovement(); //inits snake at positiion of 1

        Board.Cell.createFood(); //initializes food item
        //method that gets called every (milliseconds defined in FPS variable) makes the snake move and shit
            TimerTask snakeMovement = new TimerTask() {
                @Override
                public void run() {
                    if(gameStatus) {Snake.changeDirection(pressedKey[0]);}

                    else{
                        display.setText("GAME OVER!");
                        display.setBounds(WINDOW_SIZE/2, WINDOW_SIZE/2, WINDOW_SIZE, WINDOW_SIZE);
                        //System.exit(0);
                    }

                    //TODO: make a better solution for stopping the game (also u can still input when in game-over)
                }
            };

            timer.scheduleAtFixedRate(snakeMovement, 0, FPS);

    }

    protected static class Board extends Main{
        //init var
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
                if (position % BOARD_SIZE == 0) {
                    toDisplay.append("<br>"); //adds new row
                }
            }
            toDisplay.append("</html>");

            display.setText(String.valueOf(toDisplay)); //sets display text to the drawn board
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
                    currentCell.type = "tile";
                    currentCell.changeAppearance(false); //sets appearance to regular ass cell LOL
                    snakeCells.remove(i);
                }
            }
        }

        //class manages attributes for individual cells
        protected static  class Cell {
            boolean status = false; //if FALSE: cell is represented by an O | if TRUE: cell is an X
            char appearance; //actual display of the cell
            int POSITION; //this is the location data and is effectively rows+col. this is used because you have to run the values through a map to sort by an element and running 2 maps for rows and cols is lag-inducing
            int age; //used to determine which cell is cleared when the snake moves
            String type; //accepted params: TILE [regular floor tiles] | SNAKE [snake's body] | FOOD [grow the snake]

            //constructor method used for initialization: sets X/Y position
            private Cell(int position){
                POSITION = position;
                this.appearance = changeAppearance(false);
                this.type = "tile";
                this.age = 0;

                //allCells.add(this);
                cellsByPosition.put(POSITION, this);
            }

            //method used to change appearance of cell based on status
            private char changeAppearance(boolean status){ //true if snake false if no\t
                //active/inactive vars may not be necessary but it's nice to have them easily configurable
                final char ACTIVE = '■'; //active/inactive appearances for each cell (active is if there's a snake/food on that tile)
                final char INACTIVE = '☐';
                this.status = status;

                //idk if they should be formatted like this but it looks nicer
                if(status){appearance = ACTIVE;}else{appearance = INACTIVE;}

                return appearance;
            }

            //adds cells to snakeCell list
            private void snakeCellsManagement(Cell targetCell) {
                if (Objects.equals(targetCell.type, "snake")) {gameStatus = false;}
                else {
                    if (Objects.equals(targetCell.type, "food")) { //this looks incredibly dumb but you have to have this if statement inside the else
                        Snake.length++;
                        createFood();
                    }

                    targetCell.type = "snake";
                    targetCell.age = Snake.length+1; //add one because the cells would immediately get depreciated to (length-1)
                    snakeCells.add(this);
                }
            }

            protected static void createFood(){
                Random rand = new Random(); //gets random class to call random cell pos
                Cell targetCell = cellsByPosition.get(rand.nextInt(CELL_COUNT)+1); //inits to placeholder cell

                while(!Objects.equals(targetCell.type, "tile")){ //if selected cell is snake
                    int position = rand.nextInt(CELL_COUNT)+1; //must be ++ because rolls start at 0
                    targetCell = cellsByPosition.get(position); //gets atts of cell currently selected
                }

                //changes type to food and changes appearance to activated char
                targetCell.type = "food";
                targetCell.changeAppearance(true);
            }
        }
    }

    //holds data for snake
    public static class Snake extends Board{
        static int length = 1;
        static String direction = "RIGHT";
        static int position = 1; //thithe position of the cell the snake's head is in
        static int modifier = 1; //how mmany cells the snake will move by (aka: the direction)


        public static void updateMovement(){
            position+=modifier;
            Board.updateCell(true, position, true); //snake turns the tile it's on into it's activated appearance
            cellAgeDeprecation();
            drawBoard(panel,display); //updates board
        }

        private static void changeDirection(int key){
            //me when code stolen off stackoverflow
            if(key == KeyEvent.VK_RIGHT&&!Objects.equals(direction, "LEFT")){
                direction = "RIGHT";
                modifier = 1;

            }else if(key == KeyEvent.VK_LEFT&&!Objects.equals(direction, "RIGHT")){
                direction = "LEFT";
                modifier = -1;

            }else if(key == KeyEvent.VK_UP&&!Objects.equals(direction, "DOWN")){
                direction = "UP";
                modifier = -BOARD_SIZE;

            }else if(key == KeyEvent.VK_DOWN&&!Objects.equals(direction, "UP")){
                direction = "DOWN";
                modifier = BOARD_SIZE;

            }

            updateMovement();
        }
    }
}