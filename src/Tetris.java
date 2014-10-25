import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
/**
 * Runs a game of Tetris.
 * 
 * @author Eric
 * @author Lin Dong
 * @version 1.0
 */
public class Tetris {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Preview preview = new Preview();
        frame.setPreferredSize(new Dimension(372,500));
        //preview.setPreferredSize(new Dimension(20,1000));

        JPanel framePanel = new JPanel();
        framePanel.setLayout(new GridLayout(1,2));
        
        
        // JPanel framePanel = new JPanel(new GridLayout(2,2));

        // 10 (wide) x 25 (tall) playing grid
        BoardPanel boardPanel = new BoardPanel(10, 25, preview);
        framePanel.add(boardPanel);
        framePanel.add(preview);
        
        frame.add(framePanel);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        boardPanel.start();
    }
}
