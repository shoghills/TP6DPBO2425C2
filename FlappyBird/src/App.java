import javax.swing.*;

public class App{
    public static void main(String[] args) {
        JFrame frame = new JFrame ( "Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        Logic logic = new Logic();
        //instansiasi sehingga view bisa berkomunikasi dgn logic
        View view = new View(logic);
        //begitu pula kebalikannya
        logic.setView(view);

        view.requestFocus();
        //instansiasi objek view

        frame.add(view);
        frame.pack();
        frame.setVisible(true);
    }
}