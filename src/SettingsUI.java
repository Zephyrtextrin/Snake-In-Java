import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SettingsUI {


    private static boolean firstPlay = true;
    private static final JFrame frame = new JFrame("snake settings");

    protected static void UIInit(){
        final int WINDOW_SIZE = 300;
        //changes l&f to windows classic because im a basic bitch like that
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows Classic".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch (Exception e){System.out.println("error with look and feel!\n------DETAILS------\n" + e.getMessage());}

        //window
        frame.setSize(WINDOW_SIZE*2, WINDOW_SIZE);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setVisible(true);

        //cellPanel that all the display elements go on
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        panel.setLayout(null);
        frame.add(panel);

        //to set snake color
        JLabel snakeColorLabel = new JLabel("Snake Color");
        snakeColorLabel.setBounds((150)/2,0,150,25);
        panel.add(snakeColorLabel);

        JTextField snakeColorOneField = new JTextField("R");
        snakeColorOneField.setBounds(25,25,150,25);
        panel.add(snakeColorOneField);

        JTextField snakeColorTwoField = new JTextField("G");
        snakeColorTwoField.setBounds(25,55,150,26);
        panel.add(snakeColorTwoField);

        JTextField snakeColorThreeField = new JTextField("B");
        snakeColorThreeField.setBounds(25,85,150,25);
        panel.add(snakeColorThreeField);

        String[] colorTypes = {"RGB","Hex"};
        JComboBox<String> snakeColorTypeSelect = new JComboBox<>(colorTypes);
        snakeColorTypeSelect.setBounds(25,115,150,25);
        panel.add(snakeColorTypeSelect);

        JButton setSnakeColor = new JButton("Set Color");
        setSnakeColor.setBounds(25,145,150,25);
        panel.add(setSnakeColor);




        //to set field color
        JLabel backgroundColorLabel = new JLabel("Field Color");
        backgroundColorLabel.setBounds(((150)/2)+200,0,150,25);
        panel.add(backgroundColorLabel);

        JTextField backgroundColorOneField = new JTextField("255");
        backgroundColorOneField.setBounds(225,25,150,25);
        panel.add(backgroundColorOneField);

        JTextField backgroundColorTwoField = new JTextField("255");
        backgroundColorTwoField.setBounds(225,55,150,26);
        panel.add(backgroundColorTwoField);

        JTextField backgroundColorThreeField = new JTextField("255");
        backgroundColorThreeField.setBounds(225,85,150,25);
        panel.add(backgroundColorThreeField);

        JComboBox<String> backgroundColorTypeSelect = new JComboBox<>(colorTypes);
        backgroundColorTypeSelect.setBounds(225,115,150,25);
        panel.add(backgroundColorTypeSelect);

        JButton setFieldColor = new JButton("Set Color");
        setFieldColor.setBounds(225,145,150,25);
        panel.add(setFieldColor);





        //to set food color
        JLabel foodColorLabel = new JLabel("Food Color");
        foodColorLabel.setBounds(((150)/2)+400,0,150,25);
        panel.add(foodColorLabel);

        JTextField foodColorOneField = new JTextField("255");
        foodColorOneField.setBounds(425,25,150,25);
        panel.add(foodColorOneField);

        JTextField foodColorTwoField = new JTextField("G");
        foodColorTwoField.setBounds(425,55,150,26);
        panel.add(foodColorTwoField);

        JTextField foodColorThreeField = new JTextField("B");
        foodColorThreeField.setBounds(425,85,150,25);
        panel.add(foodColorThreeField);

        JComboBox<String> foodColorTypeSelect = new JComboBox<>(colorTypes);
        foodColorTypeSelect.setBounds(425,115,150,25);
        panel.add(foodColorTypeSelect);

        JButton setFoodColor = new JButton("Set Color");
        setFoodColor.setBounds(425,145,150,25);
        panel.add(setFoodColor);

        //button to play again
        JButton playGame = new JButton("Play!");
        playGame.setBounds((frame.getWidth()/2)-150/2,frame.getHeight()-100,150,50);
        playGame.setFocusable(false); //this cannot be focusable: if it is focusable, you can click on it and steal focus from the frame, and the frame needs to be focused all the time because the input listener only works when the component its applied to is focused
        playGame.setVisible(true);
        panel.add(playGame);


        snakeColorTypeSelect.addActionListener(_-> setColorType(snakeColorOneField,snakeColorTwoField,snakeColorThreeField,snakeColorTypeSelect));

        setSnakeColor.addActionListener(_->{
            Board.SNAKE_COLOR = setColor(snakeColorOneField,snakeColorTwoField,snakeColorThreeField,snakeColorTypeSelect);
            setSnakeColor.setBackground(Board.SNAKE_COLOR);
            setSnakeColor.setForeground(isDarkColor(Board.SNAKE_COLOR));
        });


        backgroundColorTypeSelect.addActionListener(_-> setColorType(backgroundColorOneField,backgroundColorTwoField,backgroundColorThreeField,backgroundColorTypeSelect));

        setFieldColor.addActionListener(_->{
            Board.FIELD_COLOR = setColor(backgroundColorOneField,backgroundColorTwoField,backgroundColorThreeField,backgroundColorTypeSelect);
            setFieldColor.setBackground(Board.FIELD_COLOR);
            setFieldColor.setForeground(isDarkColor(Board.FIELD_COLOR));
        });

        foodColorTypeSelect.addActionListener(_-> setColorType(foodColorOneField,foodColorTwoField,foodColorThreeField,foodColorTypeSelect));

        setFoodColor.addActionListener(_->{
            Board.FOOD_COLOR = setColor(foodColorOneField,foodColorTwoField,foodColorThreeField,foodColorTypeSelect);
            setFoodColor.setBackground(Board.FOOD_COLOR);
            setFoodColor.setForeground(isDarkColor(Board.FOOD_COLOR));
        });

        playGame.addActionListener(_ -> {
            Board.initCells();

            if(firstPlay) {
                GameManager.gameStatus = true;
                new GameManager();
                GameUI.frame.setVisible(true);
                GameManager.frameAdvancement();
                frame.setVisible(false);
                firstPlay = false;
                GameUI.lengthPanel.add(GameUI.settingsButton);
            }else{
                GameUI.frame.setVisible(true);
                frame.setVisible(false);
            }
        });

        panel.repaint();
        panel.revalidate();
    }

    private static void setColorType(JTextField RField, JTextField GField, JTextField BField, JComboBox<String> typeSelection){
        boolean isRgb = Objects.equals(typeSelection.getSelectedItem(), "RGB");
        GField.setVisible(isRgb);
        GField.setEnabled(isRgb);

        BField.setVisible(isRgb);
        BField.setEnabled(isRgb);

        if(isRgb){
            RField.setText("R");
            RField.setBounds(RField.getX(),25,RField.getWidth(),RField.getHeight());

        }else if(Objects.equals(typeSelection.getSelectedItem(), "Hex")){
            RField.setText("Hex");
            RField.setBounds(RField.getX(),80,RField.getWidth(),RField.getHeight());
        }
    }

    private static Color setColor(JTextField RField, JTextField GField, JTextField BField, JComboBox<String> typeSelection){
        Color result = Color.BLACK;
        if(Objects.equals(typeSelection.getSelectedItem(), "RGB")){
            int R = RGBCheckSum(RField);
            oneFieldUpdate("Red",R, RField);

            int G = RGBCheckSum(GField);
            oneFieldUpdate("Green",G, GField);

            int B = RGBCheckSum(BField);
            oneFieldUpdate("Blue",B, BField);

            result = new Color(R,G,B);
        }else if(Objects.equals(typeSelection.getSelectedItem(), "Hex")){
            String hex = RField.getText(); //RField is used both for the R in RGB and for hex because there's no reaspn to make 2 seperate fields
            if(hex.charAt(0)!='#'){hex = "#"+hex;}
            result = Color.decode(hex);
        }
        typeSelection.setBackground(result);
        typeSelection.setForeground(isDarkColor(result));
        return result;
    }

    private static int RGBCheckSum(JTextField field){
        int value;
        try{
            value = Integer.parseInt(field.getText());
            new Color(value,value,value); //used purely to test if values are more/less than 255 or 0
        }catch (Exception e){value = 0;}
        return value;
    }

    private static void oneFieldUpdate(String colorType, int value, JTextField field){
        Color color = switch (colorType) {
            case "Red" -> new Color(value, 0, 0);
            case "Green" -> new Color(0, value, 0);
            case "Blue" -> new Color(0, 0, value);
            default -> Color.BLACK;
        };

        field.setBackground(color);
        field.setForeground(isDarkColor(color));
        field.setText(String.valueOf(value));
        if(colorType.equals("Blue")){field.setForeground(Color.WHITE);} //even blue at 255 is way too dark LOL
    }

    private static Color isDarkColor(Color color){
        int R = color.getRed();
        int G = color.getGreen();
        int B = color.getBlue();
        int darkThreshold = 167;

        if(R<darkThreshold&&G<darkThreshold&&B<darkThreshold){return Color.WHITE;
        }else{return Color.BLACK;}
    }

    protected static void enableSettings(){frame.setVisible(true);}
}
