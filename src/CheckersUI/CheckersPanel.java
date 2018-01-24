package CheckersUI;

import checkers.Board;
import checkers.Coordinate;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author hamza
 */
public class CheckersPanel extends JPanel {

    final static String BLACK_PAWN = "/assets/black_pawn.png";
    final static String WHITE_PAWN = "/assets/white_pawn.png";
    final static String BOARD = "/assets/board.png";
    final static String BOARD_1 = "/assets/board1.png";
    final static String BOARD_2 = "/assets/board2.png";
    final static String WHITE_KING = "/assets/white_pawn_king.png";
    final static String BLACK_KING = "/assets/black_pawn_king.png";
    final static String GREEN_INDICATOR = "/assets/green_indicator.png";

    ImageIcon  black_pawn, white_pawn, board, board1, board2, black_king, white_king, indicatePosition;
    boolean newBoard = true;
    ArrayList<Point> blackPositions = new ArrayList<Point>();
    ArrayList<Point> whitePositions = new ArrayList<Point>();
    ArrayList<Pawn> pawns = new ArrayList<Pawn>();
    ArrayList<Point> allBoardPoints = new ArrayList<>();
    ArrayList<Integer> possiblemovesindex = new ArrayList<>();
    ArrayList<Integer> bestmovesfromhelp = new ArrayList<>();
    
    Board boardO = new Board();
    String turn = "your turn";
    String user_move = "";
    String computer_move = "";
    int theme = 1;
    

    public CheckersPanel() {

        // initialiser le damier
        initAllpositions();
        // les images 
        black_pawn = new ImageIcon(getClass().getResource(BLACK_PAWN));
        white_pawn = new ImageIcon(getClass().getResource(WHITE_PAWN));
        board = new ImageIcon(getClass().getResource(BOARD));
        indicatePosition = new ImageIcon(getClass().getResource(GREEN_INDICATOR));
        board1 = new ImageIcon(getClass().getResource(BOARD_1));
        board2 = new ImageIcon(getClass().getResource(BOARD_2));
        white_king = new ImageIcon(getClass().getResource(WHITE_KING));
        black_king = new ImageIcon(getClass().getResource(BLACK_KING));
        boardO.initialize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (theme == 1) {
            board.paintIcon(this, g, 0, 0);
        } else if (theme == 2) {
            board1.paintIcon(this, g, 0, 0);
        } else if (theme == 3) {
            board2.paintIcon(this, g, 0, 0);
        }
        if (newBoard) {
            drawBoard(g);
        }
        drawPawns(g);
        drawPossibleMoves(g);
        drawBestmovesFromHelp(g);
    }

    public void drawInitBoard(Graphics g) {
        for (Point p : allBoardPoints) {
            System.out.println("" + p.getX() + " " + p.getY());
            black_pawn.paintIcon(this, g, (int) p.getX(), (int) p.getY());
        }
    }

    
    private void initAllpositions() {
        int lignes = 0;
        for (int i = 0; i < 32; i++) {
            Point blackpos1 = new Point(5, 5);
            if (i != 0 && i % 4 == 0) {
                lignes++;
            }
            if (lignes % 2 == 0) {
                blackpos1.x = (i % 4) * 75 * 2 + 5;
                blackpos1.y = lignes * 75 + 5;
            }
            else {
                blackpos1.x = (i % 4) * 75 * 2 + 5 + 75;
                blackpos1.y = lignes * 75 + 5;
            }

            allBoardPoints.add(blackpos1);
        }
    }

    public void drawPawnAtPostion(Graphics g, int pos, int player) {
        if (player == 1)// les noires
        {
            Pawn p = new Pawn(allBoardPoints.get(pos), black_pawn);
            p.posindex = pos;
            pawns.add(p);
        } else { // les blancs
            Pawn p = new Pawn(allBoardPoints.get(pos), white_pawn);
            p.posindex = pos;
            pawns.add(p);
        }
    }

    public void drawPawns(Graphics g) {
        for (Pawn p : pawns) {
            p.image.paintIcon(this, g, (int) p.point.getX(), (int) p.point.getY());
        }
    }

    public void drawBoard(Graphics g) {
        pawns.clear();

        for (int i = 1; i < 33; i++) {
            Coordinate c = new Coordinate(i);
            int color = 0;

            if (boardO.getChecker(c) != null) {
                color = boardO.getChecker(c).getColor();
            }
            if (color == 2) {

                Pawn p = null;
                if (boardO.getChecker(c).isKing()) {
                    p = new Pawn(allBoardPoints.get(i - 1), white_king);
                } else {
                    p = new Pawn(allBoardPoints.get(i - 1), white_pawn);
                }

                p.posindex = i;
                pawns.add(p);
            }
            if (color == 1) {
                Pawn p = null;
                if (boardO.getChecker(c).isKing()) {
                    p = new Pawn(allBoardPoints.get(i - 1), black_king);
                } else {
                    p = new Pawn(allBoardPoints.get(i - 1), black_pawn);
                }
                p.posindex = i;
                pawns.add(p);
            }

        }
    }

    private void drawBestmovesFromHelp(Graphics g) {

        for (int i = 0; i < bestmovesfromhelp.size(); i++) {
            indicatePosition.paintIcon(this, g, (int) allBoardPoints.get(bestmovesfromhelp.get(i) - 1).getX() + 5, (int) allBoardPoints.get(bestmovesfromhelp.get(i) - 1).getY() + 5);
        }
    }

    public void drawPossibleMoves(Graphics g) {

        for (int i = 0; i < possiblemovesindex.size(); i++) {
            indicatePosition.paintIcon(this, g, (int) allBoardPoints.get(possiblemovesindex.get(i) - 1).getX() + 5, (int) allBoardPoints.get(possiblemovesindex.get(i) - 1).getY() + 5);
        }

    }
   
    
    public Pawn getPawnOfPosition(int pos) {

        for (Pawn p : pawns) {
            if (p.posindex == pos) {
                return p;
            }
        }
        return null;
    }
}
