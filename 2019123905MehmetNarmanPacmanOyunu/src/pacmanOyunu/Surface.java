package pacmanOyunu;

/**
 * 2019123905 Mehmet NARMAN
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Surface extends JPanel implements ActionListener { //Surface sınıfımı oluşturdum ve sınıfımı JPanelden miras alarak Panelimi oluşturdum Timer'ı kullanacağım için ActionListener'ı implement ettim

    private Dimension d;            //Dimesion türünde d değişkenimi tanımladım Panelimde width ve height değerlerimi tanımlayacağım.
    private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);

    private Image img;          //Image tipinde img değişkenimi tanımladım
    private final Color noktaColor = new Color(255, 0, 0);  //noktaların rengini tanımladım
    private Color labirentColor;            //labirent rengini oluşturabilmek için labirent color değişken tipinde labirentColor değişkenini tanımladım.

    private boolean inGame = false;         //Oyunun durması veya devam etmesini kontrol edecek değişkenimi tanımladım
    private boolean dying = false;          //

    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int PAC_ANIM_DELAY = 2;
    private final int PACMAN_ANIM_COUNT = 4;
    private final int MAX_CANAVAR = 12;
    private final int PACMAN_SPEED = 6;

    private int pacAnimCount = PAC_ANIM_DELAY;
    private int pacAnimDir = 1;
    private int pacmanAnimPos = 0;
    private int N_CANAVAR = 6;
    private int can, puan;
    private int[] dx, dy;
    private int[] canavar_x, canavar_y, canavar_dx, canavar_dy, CanavarSpeed;

    private Image canavar;                                                          //değişken konumlardaki pacmanın ve canavarların görüntülerini tanımlamak için değişken isimlerini oluşturdum
    private Image pacman1, pacman2up, pacman2left, pacman2right, pacman2down;
    private Image pacman3up, pacman3down, pacman3left, pacman3right;
    private Image pacman4up, pacman4down, pacman4left, pacman4right;

    private int pacman_x, pacman_y, pacmand_x, pacmand_y;
    private int req_dx, req_dy, view_dx, view_dy;

    private final short levelData[] = { //noktaların, sol , sağ,yukarı aşağı duvarların yerlerini matriste ayarladım(sol duvar 1,üst duvar 2,sağ duvar 4, alt duvar 8, nokta 16).
        19, 18, 26, 18, 18, 18, 26, 18, 26, 18, 18, 26, 18, 18, 22,
        17, 20, 0, 25, 16, 28, 0, 21, 0, 25, 20, 0, 17, 16, 20,
        17, 20, 0, 0, 29, 0, 0, 21, 0, 0, 29, 0, 17, 16, 20,
        17, 20, 0, 23, 0, 23, 0, 21, 0, 23, 0, 0, 17, 16, 20,
        17, 20, 0, 17, 18, 20, 0, 21, 0, 17, 22, 0, 17, 16, 20,
        17, 16, 18, 16, 16, 16, 18, 16, 18, 16, 16, 18, 16, 16, 20,
        25, 16, 16, 16, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 20,
        1, 17, 16, 20, 0, 0, 0, 0, 0, 0, 0, 17, 16, 16, 20,
        1, 17, 16, 16, 18, 18, 22, 0, 19, 18, 18, 16, 16, 16, 20,
        1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 20,
        1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 20,
        1, 17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 16, 20,
        1, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        1, 25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 16, 20,
        9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 25, 24, 24, 24, 28
    };

    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};  //hızları dizi halinde tanımladım
    private final int maxSpeed = 6;

    private int oyunHizi = 3;           //oyun hızını tanımladım
    private short[] screenData;
    private Timer timer;                //ekranımın hangi aralıkta tetikleneceğini kontrol etmek için timer imi tanımladım

    public Surface() {                  //new Surface komutu çalıştığında çalışacak Surface Constructor' ımı uluşturdum

        loadImages();                   //görselleri yüklemek için load image metodumu çalıştırdım
        initVariables();                
        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());         //klavyeyi dinliyorum
        setFocusable(true);
        setBackground(Color.black);             //arka plan rengini oluşturdum
    }

    private void initVariables() {

        screenData = new short[N_BLOCKS * N_BLOCKS];
        labirentColor = new Color(255, 255, 255);       //Labirent Rengini ayarladım
        d = new Dimension(400, 400);                    //width ve height yi ayarladım
        canavar_x = new int[MAX_CANAVAR];
        canavar_dx = new int[MAX_CANAVAR];
        canavar_y = new int[MAX_CANAVAR];
        canavar_dy = new int[MAX_CANAVAR];
        CanavarSpeed = new int[MAX_CANAVAR];
        dx = new int[4];
        dy = new int[4];

        timer = new Timer(40, this);                //timerımı ayarladım
        timer.start();                              //timerımı başlattım
    }

    @Override
    public void addNotify() {               
        super.addNotify();                  //üst sınıftan addNotify sınıfını ekliyorum
        initGame();                         //init game metodunu çalıştırdım
    }

    private void doAnim() {

        pacAnimCount--;

        if (pacAnimCount <= 0) {
            pacAnimCount = PAC_ANIM_DELAY;
            pacmanAnimPos = pacmanAnimPos + pacAnimDir;

            if (pacmanAnimPos == (PACMAN_ANIM_COUNT - 1) || pacmanAnimPos == 0) {
                pacAnimDir = -pacAnimDir;
            }
        }
    }

    private void playGame(Graphics2D g2d) {             //oyun kontrolü sağlayan metodumu tanımladım

        if (dying) {                                    

            death();

        } else {

            movePacman();
            drawPacman(g2d);
            canavarYap(g2d);
            LabCont();
        }
    }
    private void showIntroScreen(Graphics2D g2d) {
        g2d.setColor(new Color(0, 32, 48));
        g2d.fillRect(50, SCREEN_SIZE / 2 - 30, SCREEN_SIZE - 100, 50);
        g2d.setColor(Color.white);
        g2d.drawRect(50, SCREEN_SIZE / 2 - 30, SCREEN_SIZE - 100, 50);

        String s = "Başlamak için s tuşuna bas";       //Oyunu başlatmak için kullanıcının yapması gereken mesajı bstırdım
        Font small = new Font("Helvetica", Font.BOLD, 14);    // yazı tipini ayarladım
        FontMetrics metr = this.getFontMetrics(small);

        g2d.setColor(Color.white);
        g2d.setFont(small);
        g2d.drawString(s, (SCREEN_SIZE - metr.stringWidth(s)) / 2, SCREEN_SIZE / 2);
    }
    
    private void drawScore(Graphics2D g) {   //puan durumunu yazdıracak metodum
        int i;
        String s;
        g.setFont(smallFont);
        g.setColor(new Color(96, 128, 255));
        s = "Puan: " + puan;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);

        for (i = 0; i < can; i++) {
            g.drawImage(pacman3left, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
    }

    private void LabCont() {                //Labirent Kontrol

        short i = 0;
        boolean finished = true;

        while (i < N_BLOCKS * N_BLOCKS && finished) {

            if ((screenData[i] & 48) != 0) {
                finished = false;
            }

            i++;
        }

        if (finished) {

            puan += 50;

            if (N_CANAVAR < MAX_CANAVAR) {
                N_CANAVAR++;
            }

            if (oyunHizi < maxSpeed) {
                oyunHizi++;
            }

            initLevel();
        }
    }

    private void death() {              //Pacman can hesabı ve oyunu can bitince bitirme

        can--;

        if (can == 0) {
            inGame = false;
        }

        continueLevel();
    }

    private void canavarYap(Graphics2D g2d) {       //canavar Kontrol

        short i;
        int pos;
        int count;

        for (i = 0; i < N_CANAVAR; i++) {
            if (canavar_x[i] % BLOCK_SIZE == 0 && canavar_y[i] % BLOCK_SIZE == 0) {
                pos = canavar_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (canavar_y[i] / BLOCK_SIZE);

                count = 0;

                if ((screenData[pos] & 1) == 0 && canavar_dx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 2) == 0 && canavar_dy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }

                if ((screenData[pos] & 4) == 0 && canavar_dx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 8) == 0 && canavar_dy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                if (count == 0) {

                    if ((screenData[pos] & 15) == 15) {
                        canavar_dx[i] = 0;
                        canavar_dy[i] = 0;
                    } else {
                        canavar_dx[i] = -canavar_dx[i];
                        canavar_dy[i] = -canavar_dy[i];
                    }

                } else {

                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    canavar_dx[i] = dx[count];
                    canavar_dy[i] = dy[count];
                }

            }

            canavar_x[i] = canavar_x[i] + (canavar_dx[i] * CanavarSpeed[i]);
            canavar_y[i] = canavar_y[i] + (canavar_dy[i] * CanavarSpeed[i]);
            cnavarCiz(g2d, canavar_x[i] + 1, canavar_y[i] + 1);

            if (pacman_x > (canavar_x[i] - 12) && pacman_x < (canavar_x[i] + 12)
                    && pacman_y > (canavar_y[i] - 12) && pacman_y < (canavar_y[i] + 12)
                    && inGame) {

                dying = true;
            }
        }
    }

    private void cnavarCiz(Graphics2D g2d, int x, int y) {  //Canavar görüntüsünü çizdir

        g2d.drawImage(canavar, x, y, this);
    }

    private void movePacman() {             //Pacman Kontrol

        int pos;
        short ch;

        if (req_dx == -pacmand_x && req_dy == -pacmand_y) {
            pacmand_x = req_dx;
            pacmand_y = req_dy;
            view_dx = pacmand_x;
            view_dy = pacmand_y;
        }

        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);
            ch = screenData[pos];

            if ((ch & 16) != 0) {
                screenData[pos] = (short) (ch & 15);
                puan++;
            }

            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                    view_dx = pacmand_x;
                    view_dy = pacmand_y;
                }
            }

            if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
                    || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
                    || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
                    || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
                pacmand_x = 0;
                pacmand_y = 0;
            }
        }
        pacman_x = pacman_x + PACMAN_SPEED * pacmand_x;
        pacman_y = pacman_y + PACMAN_SPEED * pacmand_y;
    }

    private void drawPacman(Graphics2D g2d) {       //pacmani yönlerine göre metotlara gönderecek ve çizdirecek metodumu oluşturdum

        if (view_dx == -1) {                        
            drawPacnanLeft(g2d);
        } else if (view_dx == 1) {
            drawPacmanRight(g2d);
        } else if (view_dy == -1) {
            drawPacmanUp(g2d);
        } else {
            drawPacmanDown(g2d);
        }
    }

    private void drawPacmanUp(Graphics2D g2d) {         //pacman yukarı yönlü olduğu zaman ekrana basılacak görsellleri ayarladım

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2up, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3up, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4up, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    private void drawPacmanDown(Graphics2D g2d) {       //pacman aşağı yönlü olduğu zaman ekrana basılacak görsellleri ayarladım

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2down, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3down, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4down, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    private void drawPacnanLeft(Graphics2D g2d) {       //pacman sol yönlü olduğu zaman ekrana basılacak görsellleri ayarladım

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2left, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3left, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4left, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    private void drawPacmanRight(Graphics2D g2d) {          //pacman sağ yönlü olduğu zaman ekrana basılacak görsellleri ayarladım

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2right, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3right, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4right, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    private void LabirentCiz(Graphics2D g2d) {          //labirent ayarlamalarını yaptım

        short i = 0;
        int x, y;

        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

                g2d.setColor(labirentColor);
                g2d.setStroke(new BasicStroke(2));

                if ((screenData[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }

                if ((screenData[i] & 4) != 0) {
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 8) != 0) {
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 16) != 0) {
                    g2d.setColor(noktaColor);
                    g2d.fillRect(x + 11, y + 11, 2, 2);
                }

                i++;
            }
        }
    }
    private void initLevel() {

        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }

        continueLevel();
    }
    private void initGame() {

        can = 3;            //canı tanımladım
        puan = 0;           //puan başlangıç
        initLevel();
        N_CANAVAR = 6;      //canavar sayısını tanımladım
        oyunHizi = 3;       //oyun hızını tanımladım
    }

    private void continueLevel() {

        short i;
        int dx = 1;
        int random;

        for (i = 0; i < N_CANAVAR; i++) {

            canavar_y[i] = 4 * BLOCK_SIZE;
            canavar_x[i] = 4 * BLOCK_SIZE;
            canavar_dy[i] = 0;
            canavar_dx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (oyunHizi + 1));

            if (random > oyunHizi) {
                random = oyunHizi;
            }

            CanavarSpeed[i] = validSpeeds[random];
        }

        pacman_x = 7 * BLOCK_SIZE;
        pacman_y = 11 * BLOCK_SIZE;
        pacmand_x = 0;
        pacmand_y = 0;
        req_dx = 0;
        req_dy = 0;
        view_dx = -1;
        view_dy = 0;
        dying = false;
    }

    private void loadImages() {             //Görsellerimi tanımladım

        canavar = new ImageIcon("images/canavar.png").getImage();
        pacman1 = new ImageIcon("images/pacman.png").getImage();
        pacman2up = new ImageIcon("images/up1.png").getImage();
        pacman3up = new ImageIcon("images/up2.png").getImage();
        pacman4up = new ImageIcon("images/up3.png").getImage();
        pacman2down = new ImageIcon("images/down1.png").getImage();
        pacman3down = new ImageIcon("images/down2.png").getImage();
        pacman4down = new ImageIcon("images/down3.png").getImage();
        pacman2left = new ImageIcon("images/left1.png").getImage();
        pacman3left = new ImageIcon("images/left2.png").getImage();
        pacman4left = new ImageIcon("images/left3.png").getImage();
        pacman2right = new ImageIcon("images/right1.png").getImage();
        pacman3right = new ImageIcon("images/right2.png").getImage();
        pacman4right = new ImageIcon("images/right3.png").getImage();

    }

    @Override
    public void paintComponent(Graphics g) {            //Otomatik olarak çalışacak paint component sınıfımı 
        super.paintComponent(g);                        //bir üst sınıftan boş bir Panel açtım

        doDrawing(g);                                   //doDrawing metodumu çağırarak ekrana basılacak görsel ve metinlerin basılmasını başlattım
    }

    private void doDrawing(Graphics g) {                //ekrana basılacak görsel ve metinlerin basılmasını başlattım

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        LabirentCiz(g2d);
        drawScore(g2d);
        doAnim();

        if (inGame) {
            playGame(g2d);
        } else {
            showIntroScreen(g2d);
        }

        g2d.drawImage(img, 5, 5, this);
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    class TAdapter extends KeyAdapter {             //Klavyeden gelen bilgilere göre oyunumu başlatmak durdurmak pacmanı yönlenriemek için Klavye kontrolü sağladım

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (inGame) {
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false;
                } else if (key == KeyEvent.VK_PAUSE) {
                    if (timer.isRunning()) {
                        timer.stop();
                    } else {
                        timer.start();
                    }
                }
            } else {
                if (key == 's' || key == 'S') {
                    inGame = true;
                    initGame();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == Event.LEFT || key == Event.RIGHT
                    || key == Event.UP || key == Event.DOWN) {
                req_dx = 0;
                req_dy = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {   //timere mağlı olarak programlamın tetiklenmesi metodu

        repaint();
    }
}
