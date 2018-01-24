package CheckersUI;

import java.awt.Point;
import javax.swing.ImageIcon;

/**
 *
 * @author hamza
 */
public class Pawn {

    Point point;
    int posindex;

    ImageIcon image;
    int type;

    public Pawn(Point p, ImageIcon image) {
        this.point = p;
        this.image = image;
        posindex=0;
    }

    public Point getP() {
        return point;
    }

    public void setP(Point p) {
        this.point = p;
    }

    public ImageIcon getImage() {
        return image;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public  void movePawn(int from , int to){
        
    }

}
