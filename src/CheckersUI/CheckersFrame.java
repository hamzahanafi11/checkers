package CheckersUI;

import checkers.Board;
import checkers.CheckerPosition;
import checkers.Coordinate;
import checkers.Move;
import checkers.MoveIterator;
import checkers.MoveJump;
import checkers.MoveList;
import checkers.MoveNormal;
import checkers.GameSearch;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 *
 * @author hamza
 */
public class CheckersFrame extends JFrame implements MouseListener, MouseMotionListener, KeyListener {

    CheckersPanel pan;
    private CheckerPosition multipleJumpsChecker = null;
    ArrayList<Board> boardHistory = new ArrayList<Board>();
    private int clickedHere = 0;
    int FromPawnIndex = 0;
    int ToPawnIndex = 0;
    private static final int FROM = 1;
    private static final int TO = 2;
    private static final int FROM_MULTIPLE = 3;
    private static final int TO_MULTIPLE = 4;
    private static final int COMPUTER_THINKS = 5;
    private static final int NOT_STARTED = 6;
    private int userColor = CheckerPosition.WHITE;
    private int computerColor = CheckerPosition.BLACK;
    private Coordinate from;
    private int thinkDepth = 2;
    private boolean alreadyMoved;
    private boolean moving;
    private int nbrBacks = 0;
    static AudioClip music;
    private int nbrBack = 0;
    private int nbrForward = 0;
    private boolean isBack = false;
    String output = "";
    int currentPositionInBoradHistory = 0;
    private boolean isForward = false;
    static int algorithme = 1;
    boolean playMusic = true;

    JMenuBar menuBar;

    public CheckersFrame() {

        Toolkit toolkit = getToolkit();
        setTitle("Checkers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        pan = new CheckersPanel();
        Dimension size = toolkit.getScreenSize();
        setSize(605, 650);//605
        setResizable(false);
        setLocationRelativeTo(null);
        createMenu();
        add(pan);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setVisible(true);

        URL url = this.getClass().getResource("/assets/musicBackground.wav");
        music = Applet.newAudioClip(url);
        if (playMusic) {
            music.loop();
        }
    }

    public void createMenu() {
        //Create the menu bar.
        menuBar = new JMenuBar();

        // create Algorithmes menu item
        JMenu algorithme = new JMenu("algorithmes");
        ButtonGroup algorithmeGroup = new ButtonGroup(); // group for radio buttons
        
        JRadioButtonMenuItem rbMiniMax = new JRadioButtonMenuItem("MiniMax");        
        rbMiniMax.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("miniMax");
                CheckersFrame.algorithme = 1;
            }
        });
        JRadioButtonMenuItem rbMiniMaxAB = new JRadioButtonMenuItem("MiniMaxAB");
        rbMiniMaxAB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("miniMaxAB");
                CheckersFrame.algorithme = 2; 
            }
        });
        // add radio buttons to the group, to check only one of them
        algorithmeGroup.add(rbMiniMax);
        algorithmeGroup.add(rbMiniMaxAB);
        // set one of the radio buttons checked 
        if(CheckersFrame.algorithme == 1){
            rbMiniMax.setSelected(true);
            rbMiniMaxAB.setSelected(false);
        }
        else{
            rbMiniMax.setSelected(false);
            rbMiniMaxAB.setSelected(true);
        }
        // add radio buttons to the menu algorithme onglet
        algorithme.add(rbMiniMax);
        algorithme.add(rbMiniMaxAB);
        // add algorithme item to the menu bar
        menuBar.add(algorithme);
        JMenu levels = new JMenu("levels");
        menuBar.add(levels);
        ButtonGroup levelsGroup = new ButtonGroup(); // group for radio buttons
        
        JRadioButtonMenuItem hardLevel = new JRadioButtonMenuItem("hardLevel");        
        hardLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("hardLevel");
                
            }
        });
        
        JRadioButtonMenuItem mediumLevel = new JRadioButtonMenuItem("mediumLevel");
        mediumLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("mediumLevel");
                
            }
        });
        
        JRadioButtonMenuItem easyLevel = new JRadioButtonMenuItem("easyLevel");
        easyLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("easyLevel");
                
            }
        });
        // add radio buttons to the group, to check only one of them
        levelsGroup.add(hardLevel);
        levelsGroup.add(mediumLevel);
        levelsGroup.add(easyLevel);
        
        levels.add(hardLevel);
        levels.add(mediumLevel);
        levels.add(easyLevel);
        
        if(thinkDepth == 8){
            hardLevel.setSelected(true);
            mediumLevel.setSelected(false);
            easyLevel.setSelected(false);
        }
        else if(thinkDepth == 5){
            hardLevel.setSelected(false);
            mediumLevel.setSelected(true);
            easyLevel.setSelected(false);
        }else if(thinkDepth == 2){
            hardLevel.setSelected(false);
            mediumLevel.setSelected(false);
            easyLevel.setSelected(true);
        }
        // les styles & music
        JMenu options = new JMenu("Options");
        menuBar.add(options);
        // options
        JMenuItem styleAndMusic = new JMenuItem("style And Music", null);
        styleAndMusic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("styles And Music");
                String[] themes = {"style 1", "style 2", "style 3"};
                String[] Algorithems = {"MiniMax", "MiniMax AB"};

                JCheckBox music = new JCheckBox("Music");
                JComboBox ThemesCombo = new JComboBox(themes);

                if (playMusic == true) {
                    music.setSelected(true);
                } else {
                    music.setSelected(false);
                }

                if (pan.theme == 1) {
                    ThemesCombo.setSelectedIndex(0);
                } else if (pan.theme == 2) {
                    ThemesCombo.setSelectedIndex(1);
                } else if (pan.theme == 3) {
                    ThemesCombo.setSelectedIndex(2);
                }
                final JComponent[] inputs = new JComponent[]{
                    new JLabel("Theme "),
                    ThemesCombo,
                    new JLabel("ON/OFF music"), music
                };
                JOptionPane.showMessageDialog(CheckersFrame.this, inputs, "Styles and music ", JOptionPane.PLAIN_MESSAGE);

                if (ThemesCombo.getSelectedItem().toString().equals("style 1")) {
                    pan.theme = 1;
                    pan.repaint();
                } else if (ThemesCombo.getSelectedItem().toString().equals("style 2")) {
                    pan.theme = 2;
                    pan.repaint();
                } else if (ThemesCombo.getSelectedItem().toString().equals("style 3")) {
                    pan.theme = 3;
                    pan.repaint();
                }
                if (music.isSelected()) {
                    playMusic = true;
                    CheckersFrame.music.loop();
                } else {
                    playMusic = false;
                    CheckersFrame.music.stop();
                }
            }
        });
        JMenuItem backward = new JMenuItem("backward", null);
        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Do backward ");
                System.out.println("The Current Position when i will Back " + currentPositionInBoradHistory);
                if ((currentPositionInBoradHistory - (++nbrBack)) >= 0) {
                    isBack = true;
                    pan.boardO = boardHistory.get(currentPositionInBoradHistory - nbrBack);
                    currentPositionInBoradHistory = currentPositionInBoradHistory - nbrBack;
                    pan.repaint();
                    nbrBack = 0;
                } else {
                    nbrBack = 0;
                }
            }
        });
        JMenuItem forward = new JMenuItem("forward", null);
        forward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Do forward ");
                if (currentPositionInBoradHistory + (++nbrForward) < boardHistory.size()) {
                    pan.boardO = boardHistory.get(currentPositionInBoradHistory + nbrForward);
                    currentPositionInBoradHistory = currentPositionInBoradHistory + nbrForward;
                    nbrForward = 0;
                }else {
                    nbrForward = 0;
                }
                pan.repaint();
                
            }
        });
        JMenuItem resetBoard = new JMenuItem("reset board", null);
        resetBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Do resetBoard ");
                 pan.pawns.clear();
                pan.boardO.initialize();
                boardHistory.clear();
                currentPositionInBoradHistory = 0;
                isBack = false;
                pan.repaint();
                
            }
        });
        JMenuItem help = new JMenuItem("help", null);
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Do help ");

                    Board tmpBoard = null;
                    if (CheckersFrame.algorithme == 1) {
                        tmpBoard = GameSearch.minimax(pan.boardO, thinkDepth, userColor);
                    } else {
                        tmpBoard = GameSearch.minimaxAB(pan.boardO, thinkDepth, userColor, GameSearch.minusInfinityBoard(), GameSearch.plusInfinityBoard());
                    }

                    Move move = tmpBoard.getHistory().first().getNext();

                    pan.bestmovesfromhelp.add(move.getChecker().getPosition().get());
                    pan.bestmovesfromhelp.add(move.getDestination().get());
                    pan.repaint();
            }
        });
        options.add(backward);
        options.add(forward);
        options.add(help);
        options.add(resetBoard);
        options.add(styleAndMusic);

        // import
        JMenu importEport = new JMenu("import/export");
        menuBar.add(importEport);
        JMenuItem importGame = new JMenuItem("import game", null);
        importGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("import Game");

                File folder = new File("savegames");
                File[] listOfFiles = folder.listFiles();
                int nbrfile = 0;
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        nbrfile++;
                    }
                }

                String[] comboTypes = new String[nbrfile];

                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        comboTypes[i] = listOfFiles[i].getName();
                    } else if (listOfFiles[i].isDirectory()) {
                        System.out.println("Directory " + listOfFiles[i].getName());
                    }
                }

                ArrayList<String> filename = new ArrayList<>();

                JComboBox comboTypesList = new JComboBox(comboTypes);
                final JComponent[] inputs = new JComponent[]{
                    new JLabel("Nom de fichier"),
                    comboTypesList
                };
                JOptionPane.showMessageDialog(CheckersFrame.this, inputs, "Import a game", JOptionPane.PLAIN_MESSAGE);
                importGamefromSavegames(comboTypesList.getSelectedItem().toString());
            }
        });
        JMenuItem exportGame = new JMenuItem("export game", null);
        exportGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("export Game");
                JTextField fileName = new JTextField();
                final JComponent[] inputs = new JComponent[]{
                    new JLabel("Nom de fichier"),
                    fileName
                };
                JOptionPane.showMessageDialog(CheckersFrame.this, inputs, "Export a game", JOptionPane.PLAIN_MESSAGE);
                if(fileName.getText().toString().compareTo("")!=0){
                    try {
                        SaveHistoryOfGame(fileName.getText().toString());
                    } catch (IOException ex) {
                        Logger.getLogger(CheckersFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    System.out.println("no name file entred");
                }
            }
        });
        importEport.add(importGame);
        importEport.add(exportGame);
        this.setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        new CheckersFrame();
    }

    @Override
    public void mouseClicked(MouseEvent e) {


        int test = 0;
        for (int i = 0; i < pan.allBoardPoints.size(); i++) {
            if (e.getX() > (int) pan.allBoardPoints.get(i).getX()
                    && e.getX() < (int) (pan.allBoardPoints.get(i).getX() + 75)
                    && e.getY() - 40 < (int) (pan.allBoardPoints.get(i).getY() + 75)
                    && e.getY() - 40 > (int) (pan.allBoardPoints.get(i).getY())) {
                test = (i + 1);
                break;
            }
        }

        for (int i = 0; i < pan.pawns.size(); i++) {

            if (e.getX() > (int) pan.pawns.get(i).point.getX() && e.getX() < (int) (pan.pawns.get(i).point.getX() + 75)
                    && e.getY() - 27 < (int) (pan.pawns.get(i).point.getY() + 75) && e.getY() - 27 > (int) (pan.pawns.get(i).point.getY())) {
                clickedHere = i;
                break;
            }
        }

        MoveList validMoves;
        validMoves = GameSearch.findAllValidMoves(pan.boardO, userColor);
        pan.possiblemovesindex.clear();
        for (int i = 0; i < validMoves.size(); i++) {
            if ((test - 1) >= 0 && pan.boardO.getChecker(new Coordinate(test)) != null && validMoves.get(i).getChecker().getPosition() == pan.boardO.getChecker(new Coordinate(test)).getPosition()) {
                pan.possiblemovesindex.add(validMoves.get(i).getDestination().get());
                pan.repaint();
            }

        }

        if (e.getX() > 690 && e.getX() < 690 + 54 && e.getY() - 27 > 530 && e.getY() - 27 < 530 + 54) {
            pan.pawns.clear();
            pan.boardO.initialize();
            boardHistory.clear();
            currentPositionInBoradHistory = 0;
            isBack = false;
            pan.repaint();
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (alreadyMoved) {
            FromPawnIndex = clickedHere + 1;
            for (int i = 0; i < pan.allBoardPoints.size(); i++) {

                if (e.getX() > (int) pan.allBoardPoints.get(i).getX() && e.getX() < (int) (pan.allBoardPoints.get(i).getX() + 75)
                        && e.getY() - 27 < (int) (pan.allBoardPoints.get(i).getY() + 75) && e.getY() - 27 > (int) (pan.allBoardPoints.get(i).getY())) {
                    ToPawnIndex = i + 1;
                    break;
                }
            }

            if (clickedHere >= 0) {
                moveUser(new Coordinate((pan.pawns.get(clickedHere).posindex)), new Coordinate(ToPawnIndex));
            }
            pan.newBoard = true;
            pan.repaint();
            clickedHere = -48;
            setCursor(Cursor.DEFAULT_CURSOR);
            alreadyMoved = false;
            moving = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        alreadyMoved = true;
        setCursor(Cursor.CROSSHAIR_CURSOR);
        pan.possiblemovesindex.clear();
        pan.bestmovesfromhelp.clear();

        if (!moving) {
            for (int i = 0; i < pan.pawns.size(); i++) {
                if (e.getX() > (int) pan.pawns.get(i).point.getX()
                        && e.getX() < (int) (pan.pawns.get(i).point.getX() + 75)
                        && e.getY() - 27 < (int) (pan.pawns.get(i).point.getY() + 75)
                        && e.getY() - 27 > (int) (pan.pawns.get(i).point.getY())) {
                    clickedHere = i;
                    break;
                }
            }
        }

        if (clickedHere >= 0) {
            pan.newBoard = false;
            moving = true;
            pan.pawns.get(clickedHere).setP(new Point(e.getX() - 75 / 2, e.getY() - 40 - 75 / 2));
            pan.repaint();

        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }

    public void moveUser(Coordinate from, Coordinate to) {
        pan.turn = "your turn";

        Move move = validateUserMove(from, to);
        if (move == null) {
            System.out.println(" The Move is not Valid ");
            outputText("Invalid move.");
        } else if (move.isJump()) {

            if (isBack) {
                int currentBoard = 0;
                currentBoard = currentPositionInBoradHistory;
                removeBordsAfter(currentPositionInBoradHistory + 1);
                isBack = false;
                currentPositionInBoradHistory = boardHistory.size() + 1;
            } else if (boardHistory.size() == 0) {
                currentPositionInBoradHistory = 0;
                boardHistory.add(pan.boardO);
            }
            pan.boardO = GameSearch.executeUserJump(move, pan.boardO);
            multipleJumpsChecker = pan.boardO.getChecker(move.getDestination());
            if (mandatoryJump(multipleJumpsChecker, pan.boardO)) {
                outputText("A multiple jump must be completed.");
            } else {
                computerMoves();
            }
        } else // Normal move.
         if (GameSearch.existJump(pan.boardO, userColor)) {
                outputText("Invalid move. If you can jump, you must.");
            } else {

                if (isBack) {
                    int currentBoard = 0;
                    currentBoard = currentPositionInBoradHistory;
                    removeBordsAfter(currentPositionInBoradHistory + 1);
                    isBack = false;

                    currentPositionInBoradHistory = boardHistory.size() + 1;
                } else if (boardHistory.size() == 0) {
                    currentPositionInBoradHistory = 0;
                    boardHistory.add(pan.boardO);
                }
                pan.boardO = GameSearch.executeMove(move, pan.boardO);
                pan.user_move = move.toString();
                computerMoves();
            }
    }

    public Move validateUserMove(Coordinate from, Coordinate to) {
        Move move = null;
        CheckerPosition checker = pan.boardO.getChecker(from);
        if (checker == null) {
        }
        if (checker != null) {
            if (userColor == CheckerPosition.WHITE) {
                if (checker.getColor() == CheckerPosition.WHITE) {
                    if (checker.getValue() == CheckerPosition.WHITE_VALUE_KING) {
                        if (Math.abs(from.row() - to.row()) == 1) {

                            if (GameSearch.validKingMove(from, to, pan.boardO)) {
                                move = new MoveNormal(checker, to);
                            }
                        } else if (GameSearch.validKingJump(from, to, pan.boardO)) {

                            move = new MoveJump(checker, to);
                        }
                    } else // Normal white checker. 
                     if (from.row() - to.row() == 1) {

                            if (GameSearch.validWhiteMove(from, to, pan.boardO)) {
                                move = new MoveNormal(checker, to);
                            }
                        } else if (GameSearch.validWhiteJump(from, to, pan.boardO)) {
                            move = new MoveJump(checker, to);
                        }
                }
            } else // User is black.
             if (checker.getColor() == CheckerPosition.BLACK) {
                    if (checker.getValue() == CheckerPosition.BLACK_VALUE_KING) {
                        if (Math.abs(from.row() - to.row()) == 1) {
                            if (GameSearch.validKingMove(from, to, pan.boardO)) {
                                move = new MoveNormal(checker, to);
                            }
                        } else if (GameSearch.validKingJump(from, to, pan.boardO)) {
                            move = new MoveJump(checker, to);
                        }
                    } else // Normal black checker.
                     if (to.row() - from.row() == 1) {
                            if (GameSearch.validBlackMove(from, to, pan.boardO)) {
                                move = new MoveNormal(checker, to);
                            }
                        } else if (GameSearch.validBlackJump(from, to, pan.boardO)) {
                            move = new MoveJump(checker, to);
                        }
                }
        }
        return move;
    }

    private void outputText(String s) {
        output = "\n>>> " + s;
        System.out.println("" + (output));
    }

    // Returns true if checker can make a jump.
    private boolean mandatoryJump(CheckerPosition checker, Board board) {
        MoveList movelist = new MoveList();
        checker.findValidJumps(movelist, board);
        if (movelist.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    // The computer thinks....
    public void computerMoves() {
        pan.turn = " Computer turn ";
        MoveList validMoves = GameSearch.findAllValidMoves(pan.boardO, computerColor);
        if (validMoves.size() == 0) {
            JOptionPane.showMessageDialog((Component) this, "\nCongratulations!"
                    + "You win\n", "Checkers", JOptionPane.INFORMATION_MESSAGE);
            outputText("You win.");

        } else {

            pan.boardO.getHistory().reset();
            Board comBoard = null;
            if (algorithme == 2) {
                comBoard = GameSearch.minimaxAB(pan.boardO, thinkDepth, computerColor,
                        GameSearch.minusInfinityBoard(),
                        GameSearch.plusInfinityBoard());
            }
            if (algorithme == 1) {
                comBoard = GameSearch.minimax(pan.boardO, thinkDepth, computerColor);
            }
            Move move = comBoard.getHistory().first();

            pan.boardO = GameSearch.executeMove(move, pan.boardO);

            if (!isBack && !isForward) {
                boardHistory.add(pan.boardO);
                currentPositionInBoradHistory = boardHistory.size() - 1;
            }
            MoveIterator iterator = pan.boardO.getHistory().getIterator();
            String moves = "";
            while (iterator.hasNext()) {
                moves = moves + iterator.next();
                if (iterator.hasNext()) {
                    moves = moves + " , ";
                }
            }
            pan.computer_move = moves;
            int s = moves.indexOf("(");
            int ss = moves.indexOf(")");
            String[] values = moves.substring(s + 1, ss).split("-");
            outputText("the computer make this move : " + moves);
            validMoves = GameSearch.findAllValidMoves(pan.boardO, userColor);
            if (validMoves.size() == 0) {
                JOptionPane.showMessageDialog((Component) this, "Computer wins!", "Checkers", JOptionPane.INFORMATION_MESSAGE);
                outputText("Sorry. The computer wins.");
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void removeBordsAfter(int i) {
        int taille = boardHistory.size();
        for (int k = taille - 1; k >= i; k--) {
            boardHistory.remove(k);
        }
    }

    private void importGamefromSavegames(String toString) {
        ArrayList<Board> importedHistory = new ArrayList<>();
        try {
            FileInputStream fileIn = new FileInputStream("savegames/" + toString);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            importedHistory = (ArrayList<Board>) in.readObject();
            boardHistory = importedHistory;
            currentPositionInBoradHistory = boardHistory.size() - 1;
            pan.boardO = boardHistory.get(boardHistory.size() - 1);
            pan.repaint();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            return;
        }
    }

    private void SaveHistoryOfGame(String toString) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream("savegames/" + toString + ".game");

        // création d'un "flux objet" avec le flux fichier
        ObjectOutputStream os = new ObjectOutputStream(fos);
        try {
            //écriture de l'objet dans le flux de sortie
            os.writeObject(boardHistory);
            // vider le tampon
            os.flush();
        } finally {
            //fermeture des flux
            try {
                os.close();
            } finally {
                fos.close();
            }
        }
    }

}
