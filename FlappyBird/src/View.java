import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View extends JPanel{
    int width = 360;
    int height = 640;

    // JLabel untuk menampilkan skor
    private JLabel scoreLabel;

    private Logic logic;

    //constructor
    public View(Logic logic){
        this.logic = logic;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.cyan);

        // Tambahkan layout manager untuk menempatkan JLabel
        setLayout(new BorderLayout());

        //Inisialisasi JLabel skor
        scoreLabel = new JLabel("Score : 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setForeground(Color.BLACK);
        // Tambahkan JLabel ke bagian atas panel
        add(scoreLabel, BorderLayout.NORTH);

        //tambahkan fokus dan Key Listener
        //untuk menerima input keyboard
        setFocusable(true);
        addKeyListener(logic);
    }

    // Method untuk memperbarui teks pada JLabel skor
    public void updateScoreLabel() {
        scoreLabel.setText("Score: " + logic.getScore());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

        // Tampilkan pesan Game Over
        if (logic.isGameOver()) {
            drawGameOver(g);
        }
    }

    public void draw(Graphics g){
        Player player = logic.getPlayer();
        if (player != null) {
            g.drawImage(player.getImage(), player.getPosX(), player.getPosY(),
            player.getWidth(), player.getHeight(), null);
        }

        ArrayList<Pipe> pipes = logic.getPipes();
        if(pipes != null) {
            for(int i = 0; i < pipes.size(); i++){
                Pipe pipe = pipes.get(i);
                g.drawImage(pipe.getImage(), pipe.getPosX(), pipe. getPosY(),
                        pipe.getWidth(), pipe.getHeight(), null);
            }
        }
    }

    // Method untuk menggambar teks Game Over
    public void drawGameOver(Graphics g) {
        // Kotak semi-transparan
        Color semiTransparentRed = new Color(255, 0, 0, 150);
        g.setColor(semiTransparentRed);
        g.fillRect(0, height/3, width, height/3);

        // Teks "GAME OVER"
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        String gameOverText = "GAME OVER";
        FontMetrics fm = g.getFontMetrics();
        int x = (width - fm.stringWidth(gameOverText)) / 2;
        int y = height / 2 - 20;
        g.drawString(gameOverText, x, y);

        // Teks instruksi restart
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String restartText = "Press 'R' to Restart";
        fm = g.getFontMetrics();
        x = (width - fm.stringWidth(restartText)) / 2;
        y = height / 2 + 20;
        g.drawString(restartText, x, y);
    }
}
