package Graphics;

import Logic.Entry;
import Logic.Manager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * JPanel to be added as glasspane to Canvas as an overlay displaying a new
 * Tile on which to dynamically set its number. by typing number keys. With
 * number keys pressed can remove or add Tiles to Canvas through Manager.
 */
public class InputPane extends JPanel {
    /**
     * InputTile being displayed
     */
    private Tile tile;
    /**
     * Keeping track of numbers being typed
     */
    private String numberAsString;
    /**
     * Number received from Canvas giving the next vacant position in the line
     * on the Canvas grid
     */
    private int nextFreeSpace;
    private boolean animating;

    public InputPane(int firstDigit, int nextFreeSpace){
        setFocusable(true);
        addKeyListener(new KeyPadListener());
        setFocusTraversalKeysEnabled(false);
        setLayout(null);
        setBounds(0,0,WIDTH,HEIGHT);
        setBackground(new Color(0,0,0,0));
        numberAsString = "" + firstDigit;
        setOpaque(false);
        this.nextFreeSpace = nextFreeSpace;

        refreshTile(firstDigit);

        setRequestFocusEnabled(true);
        requestFocus();
    }

    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        requestFocus();
    }
    /**
     * Refreshes or creates a new Tile to be displayed
     * @param number of new Tile to be displayed
     */
    private void refreshTile(int number){
        if (tile != null)
            remove(tile);
        tile = new Tile(new Entry(number));
        Dimension screenSize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
        tile.setBounds(
                screenSize.width/2-tile.getWidth()/2,
                screenSize.height/2-tile.getHeight()/2,
                tile.getWidth(),
                tile.getHeight());
        add(tile);
    }
    /**
     * Animate tile from starting position (middle of screen) to destination
     * position (place on Canvas grid)
     * @param after function to be called after animation is finished
     */
    private void animate(Runnable after){
        new Timer(4, new ActionListener() {
            double distX;
            double distY;
            double animSpeed = 2;

            @Override
            public void actionPerformed(ActionEvent event) {
                distX = (Tile.WIDTH+2*Tile.MARGIN)*nextFreeSpace-tile.getX();
                distY = 0-tile.getY();
                tile.setBounds((int)(tile.getX()+((animSpeed*distX)/10)),
                        (int)(tile.getY()+((animSpeed*distY*0.70)/10)),
                        Tile.WIDTH,Tile.HEIGHT);
                repaint();
                //after reaching animation destination
                if (Math.abs(distX) <= 5 && Math.abs(distY) <= 5){
                    cut(event);
                }
            }
            private void cut(ActionEvent event){
                after.run();
                ((Timer)event.getSource()).stop();
            }
        }).start();
    }
    /**
     * Animats after adding by Managers autoNext
     */
    public void autoAdd(){
        animate(() -> {
            Manager.instance.add(Integer.parseInt(numberAsString));
            animating = false;
            setVisible(false);
        });
        animating = true;
    }
    /**
     * Registers typed numbers.
     * If registered typed numbers:
     * 1. Key delete removes Tile from Canvas
     * 2. Key enter adds new Tile (corresponding to numberAsString) to Canvas
     * 3. Key backSpace removes läst digit from inputTile being displayed
     */
    private class KeyPadListener implements KeyListener {
        public void keyPressed(KeyEvent event) {
            if (!animating) {
                if (event.getKeyCode() == KeyEvent.VK_ENTER && !Manager.instance.autoAddOn()) {
                    //Add new entry corresponding to numberAsString

                    //Starting animation with callback to be fired when done
                    animate(() -> {
                        Manager.instance.add(Integer.parseInt(numberAsString));
                        animating = false;
                        setVisible(false);
                    });
                    animating = true;

                } else if (event.getKeyCode() == KeyEvent.VK_DELETE ||
                        event.getKeyCode() == KeyEvent.VK_CLEAR /*Not sure but thingk VK_CLEAR is Mac-version of Delete*/) {
                    //Delete entry corresponding to numberAsString
                    Manager.instance.remove(Integer.parseInt(numberAsString));
                    setVisible(false);
                } else if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    //remove last digit
                    if (numberAsString.length() > 1) {
                        numberAsString = numberAsString.substring(0, numberAsString.length() - 1);
                    } else {
                        setVisible(false);
                    }
                } else {
                    try {
                        //Write input numberAsString and show on InputTile
                        Integer.parseInt(event.getKeyChar()+"");
                        if (numberAsString.length() < 2) {
                            if (numberAsString.startsWith(0 + "")) {
                                numberAsString = event.getKeyChar()+"";
                            }else
                                numberAsString += event.getKeyChar();
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Exception catched: Not a number");
                    }
                }
                refreshTile(Integer.parseInt(numberAsString));
                repaint();
            }
        }
        public void keyReleased(KeyEvent event){

        }
        public void keyTyped(KeyEvent event){

        }
    }

}
