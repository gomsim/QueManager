package Graphics;

import Logic.BroadcastingListReceiver;
import Logic.Entry;
import Logic.Manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;

public class Canvas extends JFrame implements BroadcastingListReceiver<Entry> {

    /**
     * Desired number of Tiles to fit along screen width
     */
    public static final int SPACES_HORIZONTAL = 5;
    /**
     * Desired number of Tiles to fint along screen height
     */
    public static final int SPACES_VERTICAL = 4;
    /**
     * List containing Tiles not fitting on screen space
     */
    private LinkedList<Tile> tileBuffer = new LinkedList<Tile>();
    /**
     * List containing Tiles displayed on screen space
     */
    private ArrayList<Tile> displayedTiles = new ArrayList<Tile>();
    /**
     * Class responsible for displaying added Tiles on a grid defined by
     * SPACES_HORIZONTAL and SPACES_VERTICAL. Creates and displays InputPane
     * when any number is typed.
     * Implements BroadcastingListReceiver
     */
    public Canvas(){
        setLayout(new GridLayout(0, SPACES_HORIZONTAL));
        setFocusable(true);
        setTitle("QueManager");
        setIconImage(new ImageIcon("Resources/QueManagerSmall.png").getImage());

        setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setBackground(new Color(0,0,0,0));
        addKeyListener(new KeyPadListener());

        requestFocus();
        setVisible(true);
    }
    /**
     * Adds new Tile, then revalidates and repaints
     * @param item to be added
     */
    @Override
    public void doOnAdd(Entry item) {
        //Update tiles
        addTile(new Tile(item));
        validate();
        repaint();
    }
    /**
     * Removes Tile corresponding to item, then revalidates and repaints.
     * @param item to be removed
     */
    @Override
    public void doOnRemove(Entry item) {
        //Update tiles
        Tile toRemove = new Tile(item);
        if (displayedTiles.contains(toRemove))
            displayedTiles.get(displayedTiles.indexOf(toRemove)).animateFade(()->{
                removeTile(toRemove);
                validate();
                repaint();
            });
    }
    /**
     * Adds new Tile to displayedTiles or tileBuffer based on available screen
     * space
     * @param tile to be added
     */
    private void addTile(Tile tile){
        if (displayedTiles.size() < SPACES_HORIZONTAL){
            add(tile);
            displayedTiles.add(tile);
        }
        else{
            tileBuffer.add(tile);
        }
    }
    /**
     * Removes Tile corresponding to tile. Tile is removed from displayedTiles
     * or tileBuffer depending on where it's located. If located in displayedTiles
     * displayedTiles is supplied with the next Tile from tileBuffer adter removal.
     * @param tile to be removed
     */
    private void removeTile(Tile tile){
        if (!displayedTiles.contains(tile)){
            tileBuffer.remove(tile);
        }else{
            try {
                Tile toRemove = displayedTiles.get(displayedTiles.indexOf(tile));
                displayedTiles.remove(toRemove);
                remove(toRemove);
                if (!tileBuffer.isEmpty()) {
                    Tile next = tileBuffer.removeFirst();
                    add(next);
                    displayedTiles.add(next);
                }
            }catch(IndexOutOfBoundsException e){
                System.out.println("Exception catched: Invalid tile number");
            }
        }
    }
    /**
     * Creates and displays new InputPane, a transparent glasspane on top of
     * this Canvas.
     * @param firstDigit of Tile to be shown on InputPane
     * @return created InputPane
     */
    public InputPane showInputPane(int firstDigit){
        InputPane glassPanel = new InputPane(firstDigit, displayedTiles.size());
        setGlassPane(glassPanel);
        glassPanel.setVisible(true);
        return glassPanel;
    }
    /**
     * Keylistener listening for digit input, ignoring everything else. Upon
     * digit input a new InputPane is displayed using
     * showInputPane(digitPressed).
     */
    private class KeyPadListener implements KeyListener{
        public void keyPressed(KeyEvent event){
            //Write input number
            try{
                int number = Integer.parseInt(event.getKeyChar()+"");
                showInputPane(number);

            }catch(NumberFormatException e){
                System.out.println("Exception catched: Not a number");
            }
            if (Manager.instance.autoAddOn()){
                if (event.getKeyCode() == KeyEvent.VK_ENTER){
                    showInputPane(Manager.instance.autoNext()).autoAdd();
                }
            }
        }
        public void keyReleased(KeyEvent event){

        }
        public void keyTyped(KeyEvent event){

        }
    }
}
