import javax.swing.*;
import java.awt.*;

public class SegmentDisplay extends JPanel {

    final static String ZERO = "zero.jpg";
    final static String ONE = "one.jpg";
    final static String TWO = "two.jpg";
    final static String THREE = "three.jpg";
    final static String FOUR = "four.jpg";
    final static String FIVE = "five.jpg";
    final static String SIX = "six.jpg";
    final static String SEVEN = "seven.jpg";
    final static String EIGHT = "eight.jpg";
    final static String NINE = "nine.jpg";
    final static String N = "N.jpg";
    final static String O = "O.jpg";
    final static String E = "E.jpg";
    final static String R = "R.jpg";
    final static String S = "S.jpg";
    final static String U = "U.jpg";
    final static String F = "F.jpg";
    final static String I = "I.jpg";
    final static String T = "T.jpg";
    final static String H = "H.jpg";
    final static String W = "W.jpg";
    final static String A = "A.jpg";
    final static String M = "M.jpg";
    final static String D = "D.jpg";
    final static String Y = "Y.jpg";
    final static String blank = "blank.jpg";
    final static String ten = "ten.jpg";

    ImageIcon segmentImage;
    JLabel segmentLabel;

    public SegmentDisplay(int x, int y, int width, int height, char c) {
        setVisible(true);
        setLayout(null);
        setBounds(x, y, width, height);

        if (c == '0')
            segmentImage = new ImageIcon(getClass().getResource(ZERO));
        else if (c == '1')
            segmentImage = new ImageIcon(getClass().getResource(ONE));
        else if (c == '2')
            segmentImage = new ImageIcon(getClass().getResource(TWO));
        else if (c == '3')
            segmentImage = new ImageIcon(getClass().getResource(THREE));
        else if (c == '4')
            segmentImage = new ImageIcon(getClass().getResource(FOUR));
        else if (c == '5')
            segmentImage = new ImageIcon(getClass().getResource(FIVE));
        else if (c == '6')
            segmentImage = new ImageIcon(getClass().getResource(SIX));
        else if (c == '7')
            segmentImage = new ImageIcon(getClass().getResource(SEVEN));
        else if (c == '8')
            segmentImage = new ImageIcon(getClass().getResource(EIGHT));
        else if (c == '9')
            segmentImage = new ImageIcon(getClass().getResource(NINE));
        else if (c == 'N')
            segmentImage = new ImageIcon(getClass().getResource(N));
        else if (c == 'O')
            segmentImage = new ImageIcon(getClass().getResource(O));
        else if (c == 'E')
            segmentImage = new ImageIcon(getClass().getResource(E));
        else if (c == 'R')
            segmentImage = new ImageIcon(getClass().getResource(R));
        else if (c == 'U')
            segmentImage = new ImageIcon(getClass().getResource(U));
        else if (c == 'S')
            segmentImage = new ImageIcon(getClass().getResource(S));
        else if (c == 'F')
            segmentImage = new ImageIcon(getClass().getResource(F));
        else if (c == 'I')
            segmentImage = new ImageIcon(getClass().getResource(I));
        else if (c == 'T')
            segmentImage = new ImageIcon(getClass().getResource(T));
        else if (c == 'H')
            segmentImage = new ImageIcon(getClass().getResource(H));
        else if (c == 'W')
            segmentImage = new ImageIcon(getClass().getResource(W));
        else if (c == 'A')
            segmentImage = new ImageIcon(getClass().getResource(A));
        else if (c == 'M')
            segmentImage = new ImageIcon(getClass().getResource(M));
        else if (c == 'D')
            segmentImage = new ImageIcon(getClass().getResource(D));
        else if (c == 'Y')
            segmentImage = new ImageIcon(getClass().getResource(Y));
        else if (c == 't')
            segmentImage = new ImageIcon(getClass().getResource(ten));
        else
            segmentImage = new ImageIcon(getClass().getResource(blank));

        segmentImage = new ImageIcon(segmentImage.getImage()
                .getScaledInstance(width, height, Image.SCALE_SMOOTH));
        segmentLabel = new JLabel(segmentImage);
        segmentLabel.setBounds(0, 0, width, height);
        segmentLabel.setVisible(true);

        add(segmentLabel);
    }
}
