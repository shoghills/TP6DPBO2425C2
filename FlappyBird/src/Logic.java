import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Logic implements ActionListener, KeyListener {
    int frameWidth = 360; int frameHeight = 640;

    int playerStartPosX = frameWidth / 2;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight =24;

    // tambahkan atribut posisis dan ukuran pipa
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    //  Atribut untuk logika game
    int score = 0;
    boolean gameOver = false;

    //  Atribut untuk posisi awal pipa
    int initialPipeCooldown = 1500; // 1.5 detik

    View view;
    Image birdImage;
    Player player;
    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;

    int pipeVelocityX = -2;

    Image lowerPipeImage;
    Image upperPipeImage;
    ArrayList<Pipe> pipes;

    public Logic(){
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);

        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();
        pipes = new ArrayList<Pipe>();

        // Memanggil resetGame untuk inisialisasi awal
        resetGame();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pipa");
                placePipes();
            }
        });
        pipesCooldown.start();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    // Method untuk mereset semua variabel game ke kondisi awal
    public void resetGame() {
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<Pipe>();
        score = 0;
        gameOver = false;
        if (gameLoop != null) gameLoop.start();
        if (pipesCooldown != null) pipesCooldown.start();
        if(view != null) view.updateScoreLabel();
    }

    public void setView(View view) {
        this.view = view;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Pipe> getPipes() { return pipes;}

    // Getter untuk skor
    public int getScore() {
        return score;
    }

    // Getter untuk status game over
    public boolean isGameOver() {
        return gameOver;
    }

    public void placePipes() {
        int randomposY = (int) (pipeStartPosY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = frameHeight / 4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomposY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, (randomposY + openingSpace + pipeHeight), pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);

    }

    // Method untuk deteksi tabrakan
    public boolean collision(Player a, Pipe b) {
        Rectangle playerRect = a.getBounds(); // Gunakan getBounds yang baru ditambahkan di Player
        Rectangle pipeRect = b.getBounds();
        return playerRect.intersects(pipeRect);
    }

    // Method untuk cek apakah burung sudah melewati pipa
    public void checkScore() {
        for(int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            // Cek skor hanya untuk pipa atas (atau pipa bawah) dan hanya sekali (pipe.passed)
            if (pipe.getPosX() + pipe.getWidth() < player.getPosX() && !pipe.passed) {
                // Pastikan ini adalah pipa atas (atau hanya proses sekali per pasangan)
                // Kita asumsikan pipa berpasangan (atas dan bawah). Pipa atas genap, pipa bawah ganjil (indeks 0, 2, 4...)
                if (i % 2 == 0) {
                    score++;
                    view.updateScoreLabel(); // NEW: Perbarui label skor
                }
                pipe.passed = true;
            }
        }
    }

    public void move() {
        // 1. HENTIKAN gerakan jika Game Over
        if (gameOver) return;

        // 2. Gerakan Player (Gravitasi & Batas Atas)
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        // 3. Gerakan Pipa
        for(int i = 0;i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);

            // 4. Deteksi TABRAKAN (Collision Check)
            if (collision(player, pipe)) {
                gameOver = true;
            }
        }

        // 5. Deteksi Tabrakan di Bawah (Jatuh ke Tanah)
        if (player.getPosY() + player.getHeight() > frameHeight) {
            gameOver = true;
        }

        // 6. Perhitungan Skor
        if (!gameOver) {
            checkScore();
        }

        // 7. Logika Game Over
        if (gameOver) {
            gameLoop.stop();
            pipesCooldown.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        if(view != null) {
            view.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
//            player.setVelocityY(-10);
            if (!gameOver) {
                player.setVelocityY(-10);
            }
        }
        // Restart game saat menekan 'R'
        if(e.getKeyCode() == KeyEvent.VK_R) {
            if (gameOver) {
                resetGame();
            }
        }
    }
    public void keyReleased(KeyEvent e) {}
}
