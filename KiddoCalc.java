
/**
 * Lab Exercise 3
 * Name : Nur Aleesya Najwa Binti Nor Azli
 * Student ID : 1211111956
 * 25/12/2024
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * chosen design patterns (MVC) & brief descriptions :
 * 1) Model (calcLogic) - handles the core functionality and data.
 * 2) View (calcView) - manages the user interface.
 * 3) Controller (calcController) - handles user input by coordinating the model and view
 */

// the interface for arithmetic operations
interface Operation {
    double perform(double num1, double num2);
}

// the implementations of arithmetic operations
class add implements Operation { //addition
    public double perform(double num1, double num2) {
        return num1 + num2;
    }
}

class subs implements Operation { //subtraction
    public double perform(double num1, double num2) {
        return num1 - num2;
    }
}

class mul implements Operation { //multiplication
    public double perform(double num1, double num2) {
        return num1 * num2;
    }
}

class div implements Operation { //division
    public double perform(double num1, double num2) {
        if (num2 == 0) {
            throw new ArithmeticException("Oh no! Can't divide by zero!");
        }
        return num1 / num2;
    }
}

// Model - handles the calculation logic (The brains of our calculator)
class calcLogic {
    private Map<String, Operation> operations;
    private double result;
    
    public calcLogic() {
        operations = new HashMap<>();
        operations.put("+", new add());
        operations.put("-", new subs());
        operations.put("×", new mul());
        operations.put("÷", new div());
    }
    
    public void calculate(double num1, double num2, String operator) {
        Operation operation = operations.get(operator);
        if (operation != null) {
            result = operation.perform(num1, num2);
        } else {
            throw new IllegalArgumentException("Invalid operator");
        }
    }
    
    public double getAnswer() {
        return result;
    }
}

// View - handles the GUI components (make the interface pretty)
class calcView extends JFrame {
    private JTextField screen;
    private JButton[] numberPad; // buttons for numbers 0-9
    private JButton[] mathButtons; // buttons for operators (+, -, ×, ÷)
    private JButton equalsButton, clearButton;
    private String[] mathSymbols = {"+", "-", "×", "÷"};
    // custom colors :
    private Color babyGreen = new Color(152, 251, 152);
    private Color babyBlue = new Color(173, 216, 230);
    private Color babyPink = new Color(255, 182, 193);
    private Color peach = new Color(255, 218, 185);

    public calcView() {
        setTitle("✮⋆˙Welcome to Kid-Friendly Calculator  ✧ (๑'ᵕ'๑)⸝*⋆˙✮˙˙");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        screen = new JTextField(); // screen for displaying input/output
        screen.setEditable(false); // to prevent user from typing directly
        screen.setHorizontalAlignment(JTextField.RIGHT);
        screen.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        add(screen, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        numberPad = new JButton[10];
        mathButtons = new JButton[4];

        // make sure the order of the buttons are organized
        String[] buttonOrder = {"7", "8", "9", "+", "4", "5", "6", "-", "1", "2", "3", "×", "C", "0", "=", "÷"};
        for (int i = 0; i < buttonOrder.length; i++) {
            JButton button = new JButton(buttonOrder[i]);
            button.setFont(new Font("Comic Sans MS", Font.BOLD, 18));

            if (buttonOrder[i].matches("[0-9]")) { // check if the button is a number
                int row = i / 4;
                button.setBackground(row % 2 == 0 ? babyBlue : babyPink);
                numberPad[Integer.parseInt(buttonOrder[i])] = button;
            } else if (buttonOrder[i].equals("=") || buttonOrder[i].equals("C")) {
                button.setBackground(peach); // set "=" and "C" button to peach color
                if (buttonOrder[i].equals("=")) equalsButton = button;
                else clearButton = button;
            } else {
                button.setBackground(babyGreen); // set operators button to green color
                mathButtons[i / 4] = button;
            }

            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);
    }

    // sets event listeners for buttons
    public void setNumberButtonListener(ActionListener listener) {
        for (JButton button : numberPad) {
            if (button != null) button.addActionListener(listener);
        }
    }

    public void setMathButtonListener(ActionListener listener) {
        for (JButton button : mathButtons) {
            button.addActionListener(listener);
        }
    }

    public void setEqualsButtonListener(ActionListener listener) {
        equalsButton.addActionListener(listener);
    }

    public void setClearButtonListener(ActionListener listener) {
        clearButton.addActionListener(listener);
    }

    public void updateScreen(String text) {
        screen.setText(text);
    }

    public String readScreen() {
        return screen.getText();
    }
}

// Controller - handles user interactions and updates the model and view
class calcController {
    private calcLogic logic; // model for calculations
    private calcView view; // view for UI
    private double firstNumber, secondNumber;
    private String chosenOperation;

    public calcController(calcLogic logic, calcView view) {
        this.logic = logic;
        this.view = view;

        view.setNumberButtonListener(new NumberButtonListener());
        view.setMathButtonListener(new MathButtonListener());
        view.setEqualsButtonListener(new EqualsButtonListener());
        view.setClearButtonListener(new ClearButtonListener());
    }

    // listeners for buttons :
    class NumberButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String digit = ((JButton) e.getSource()).getText();
            view.updateScreen(view.readScreen() + digit);
        }
    }

    class MathButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            firstNumber = Double.parseDouble(view.readScreen());
            chosenOperation = ((JButton) e.getSource()).getText();
            view.updateScreen("");
        }
    }

    class EqualsButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            secondNumber = Double.parseDouble(view.readScreen());
            try {
                logic.calculate(firstNumber, secondNumber, chosenOperation);
                view.updateScreen(String.valueOf(logic.getAnswer()));
            } catch (ArithmeticException oops) {
                view.updateScreen(oops.getMessage());
            }
        }
    }

    class ClearButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            view.updateScreen("");
            firstNumber = secondNumber = 0;
            chosenOperation = "";
        }
    }
}

public class KiddoCalc {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            calcLogic logic = new calcLogic();
            calcView view = new calcView();
            calcController controller = new calcController(logic, view);
            view.setVisible(true);
        });
    }
}
