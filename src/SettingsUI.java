import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SettingsUI {

    protected static JFrame frame = new JFrame("snake settings");
    protected static JPanel panel = new JPanel();
    protected static JButton playGame = new JButton("Play!");

    public enum INT_CONSTANTS {
        //INTEGERS: values that make the game work
        WINDOW_SIZE(400);
        public final int value;

        //constructor for strings (all type vaues)
        INT_CONSTANTS(int value) {this.value = value;}
    }

    protected static void UIInit(){
        //changes l&f to windows classic because im a basic bitch like that
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows Classic".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch (Exception e){System.out.println("error with look and feel!\n------DETAILS------\n" + e.getMessage());}

        int WINDOW_SIZE = INT_CONSTANTS.WINDOW_SIZE.value;
        Color snakeColorTemp = Color.BLACK;

        //window
        frame.setSize(WINDOW_SIZE*2, WINDOW_SIZE);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setVisible(true);

        //cellPanel that all the display elements go on
        panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        panel.setLayout(null);
        frame.add(panel);

        //to set snake color
        JLabel snakeColorLabel = new JLabel("Snake Color");
        snakeColorLabel.setBounds((150)/2,0,150,25);
        panel.add(snakeColorLabel);

        JTextField colorOneField = new JTextField("R");
        colorOneField.setBounds(25,25,150,25);
        panel.add(colorOneField);

        JTextField colorTwoField = new JTextField("G");
        colorTwoField.setBounds(25,55,150,26);
        panel.add(colorTwoField);

        JTextField colorThreeField = new JTextField("B");
        colorThreeField.setBounds(25,85,150,25);
        panel.add(colorThreeField);

        String[] colorTypes = {"RGB","Hex"};
        JComboBox colorTypeSelect = new JComboBox(colorTypes);
        colorTypeSelect.setBounds(25,115,150,25);
        panel.add(colorTypeSelect);

        JButton setSnakeColor = new JButton("Set Color");
        setSnakeColor.setBounds(25,145,150,25);
        panel.add(setSnakeColor);

        //button to play again
        playGame.setBounds(50,WINDOW_SIZE,150,50);
        playGame.setFocusable(false); //this cannot be focusable: if it is focusable, you can click on it and steal focus from the frame, and the frame needs to be focused all the time because the input listener only works when the component its applied to is focused
        playGame.setVisible(true);
        panel.add(playGame);


        colorTypeSelect.addActionListener(_->{
            boolean isRgb = Objects.equals(colorTypeSelect.getSelectedItem(), "RGB");
            colorTwoField.setVisible(isRgb);
            colorTwoField.setEnabled(isRgb);

            colorThreeField.setVisible(isRgb);
            colorThreeField.setEnabled(isRgb);

            if(isRgb){
                colorOneField.setText("R");
                colorOneField.setBounds(25,25,150,25);

            }else if(Objects.equals(colorTypeSelect.getSelectedItem(), "Hex")){
                colorOneField.setText("Hex");
                colorOneField.setBounds(25,80,150,25);
            }
        });

        setSnakeColor.addActionListener(_->{
            if(Objects.equals(colorTypeSelect.getSelectedItem(), "RGB")){
                int R = RGBCheckSum(colorOneField);
                oneFieldUpdate("Red",R,colorOneField);

                int G = RGBCheckSum(colorTwoField);
                oneFieldUpdate("Green",G,colorTwoField);

                int B = RGBCheckSum(colorThreeField);
                oneFieldUpdate("Blue",B,colorThreeField);

                Board.SNAKE_COLOR = new Color(R,G,B);
            }else if(Objects.equals(colorTypeSelect.getSelectedItem(), "Hex")){
                String hex = colorOneField.getText();
                if(hex.charAt(0)!='#'){hex = "#"+hex;}
                Board.SNAKE_COLOR = Color.decode(hex);
            }
            colorTypeSelect.setBackground(Board.SNAKE_COLOR);
            colorTypeSelect.setForeground(isDarkColor(Board.SNAKE_COLOR));
            setSnakeColor.setBackground(Board.SNAKE_COLOR);
            setSnakeColor.setForeground(isDarkColor(Board.SNAKE_COLOR));
        });

        playGame.addActionListener(_ -> {
            GameManager.gameStatus = true;
            new GameManager();
            playGame.setVisible(false);
        });

        panel.repaint();
        panel.revalidate();
    }

    private static int RGBCheckSum(JTextField field){
        int value;
        try{
            value = Integer.parseInt(field.getText());
            Color test = new Color(value,value,value); //used purely to test if values are more/less than 255 or 0
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
}
