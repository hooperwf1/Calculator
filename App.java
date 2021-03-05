import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import java.lang.Math;
import java.awt.*;
import java.awt.event.*;
import static javax.swing.GroupLayout.Alignment.*;

public class App extends JFrame {

    private String symbolsStrings[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "-", "*", "/", "=", "√", "C"};
    private JButton numbers[] = new JButton[symbolsStrings.length];
    private String expression = "";

    public static void main(String[] args) throws Exception {
        EventQueue.invokeLater(() -> {
            App app = new App();
            app.setVisible(true);
        });
    }

    public App(){
        var pane = getContentPane();
        setSize(500, 550);
        var gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);

        var vertGroup = gl.createSequentialGroup();
        var horiGroup = gl.createSequentialGroup();
        ParallelGroup horizontalGroups[] = new ParallelGroup[6];
        ParallelGroup verticalGroups[] = new ParallelGroup[6];

        for(int i = 0; i < horizontalGroups.length; i++){
            horizontalGroups[i] = gl.createParallelGroup(TRAILING);
            verticalGroups[i] = gl.createParallelGroup(BASELINE);
        }

        JLabel numberText = new JLabel("0");

        //Create buttons
        for(int i = 0; i < numbers.length; i++){
            String sym = symbolsStrings[i];
            JButton button = new JButton(sym);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    calculate(sym, numberText);
                }
            });
            numbers[i] = button;
        }

        horizontalGroups[0].addComponent(numberText);
        verticalGroups[0].addComponent(numberText);
        for(int i = 0; i < numbers.length; i++){
            horizontalGroups[(i%4)].addComponent(numbers[i]);
            verticalGroups[i/4 + 1].addComponent(numbers[i]);
        }
        horizontalGroups[0].addComponent(numbers[numbers.length-1]);
        verticalGroups[5].addComponent(numbers[numbers.length-1]);

        for(int i = 0; i < horizontalGroups.length; i++){
            horiGroup.addGroup(horizontalGroups[i]);
            vertGroup.addGroup(verticalGroups[i]);
        }

        gl.setVerticalGroup(vertGroup);
        gl.setHorizontalGroup(horiGroup);

        setTitle("Calculator");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void calculate(String s, JLabel text){
        if(s.equals("=")){
            String words[] = expression.split("\\s+");
            int ans;
            if(words[0].equals("√")){
                ans = (int) Math.sqrt(Integer.parseInt(words[1]));
            } else {
                ans = Integer.parseInt(words[0]);
            }
            for(int i = 0; i < words.length; i++){
                if(words[i].equals("")){
                    continue;
                }
                String w = words[i];
                char operation = w.charAt(0);
                if(!Character.isDigit(operation)){
                    if(i == 0 || i == words.length - 1 || words[i].equals("√")){
                        continue;
                    } 

                    int nextNum = 0;
                    if(words[i+1].equals("√")){
                        nextNum = (int) Math.sqrt(Integer.parseInt(words[i+2]));
                    } else {
                        if(words[i+1].equals("")){
                            continue;
                        }
                        nextNum = Integer.parseInt(words[i+1]);
                    }

                    switch(operation){
                        case '+':
                            ans += nextNum;
                            break;

                        case '-':
                            ans -= nextNum;
                            break;

                        case '*':
                            ans *= nextNum;
                            break;

                        case '/':
                            ans /= nextNum;
                            break;

                   }
                }
            } 

            text.setText(Integer.toString(ans));
            expression = Integer.toString(ans);
            return;
        }

        if(Character.isDigit(s.charAt(0))){
            expression += s;
        } else {
            if(expression.equals("")){
                expression += s + " ";
            } else {
                expression += " " + s + " ";
            }
        }

        if(s.charAt(0) == 'C'){
            expression = "";
        }

        text.setText(expression);
    }
}
