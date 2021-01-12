/*
 *  2019123905
 *  Mehmet Narman
 */
package pacmanOyunu;        // paketi tanımladık

import java.awt.EventQueue;   
import javax.swing.JFrame;

public class Pacman extends JFrame {     //Pacman sınıfını Jframe sınıfından miras aldık yani Frame oluşturduk

    public Pacman() {                     //sınıfımızın boş bir Constructorını tanımladık bu sınıftan yeni bir nesne çalışırsa yapılandırıcı metot çalışacak ve içerisinde bunulan initUI metodu çalışacak.

        initUI();                          //Pacman sınıfından nesne tanımladığı zaman çalışacak metot
    }

    private void initUI() {                 // bu metot içerisinde sayfa yapısını ayarladım

        add(new Surface());                 //JPanelimi oluşturacak metodu çağırdım

        setTitle("Pacman");                 //Frame e başlık verdim
        setSize(380, 420);                  //Sayfa Boyutumu belirledim
        setDefaultCloseOperation(EXIT_ON_CLOSE);    //Frame in ne zaman sonlanacağını ayarladım
        setLocationRelativeTo(null);                //Ekranımın konumunu null olarak ayarladım
        
    }

    public static void main(String[] args) {        //Programı tetikleyecek Main metodumu oluşturdum

        EventQueue.invokeLater(() -> {              //Tüm güncellemelerin eşzamanlı olarak güvenli olmasını sağlayacak metodu çağırdum

            var ex = new Pacman();                  //JFrame'imden yeni bir nesne oluşturdum
            ex.setVisible(true);                    //Çalışan çizimlerimin yada görsellerimin ön planda çalışması için setVisible metoduna true değeri gönderdim
        });
    }
}
