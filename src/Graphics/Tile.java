package Graphics;

import Logic.Entry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class of objects each visually representing an Entry from the list of Entries
 * in Manager.
 */
public class Tile extends JPanel {
    /**
     * Colour of round rect representing the Tile's shadow
     */
    private static final Color SHADOW_COLOUR = new Color(0,0,0,50);
    /**
     * Colour of round rect representing the Tile
     */
    private static final Color TILE_COLOUR = Color.WHITE;
    /**
     * The number printed on the Tile
     */
    private int number;
    public static final int MARGIN = 5;
    private static final int SHADOW_MARGIN = 3;
    public static final int CORNER_SIZE = 20;
    public static final int WIDTH =
            new Dimension(Toolkit.getDefaultToolkit().getScreenSize()).width/ Canvas.SPACES_HORIZONTAL
                    - (2*MARGIN);
    public static final int HEIGHT =
            new Dimension(Toolkit.getDefaultToolkit().getScreenSize()).height/ Canvas.SPACES_VERTICAL
                    - (2*MARGIN);
    /**
     * Tile colour that is used non-destructively (changed while keeping original
     * colour TILE_COLOUR)
     */
    private Color dynamicTileColour = TILE_COLOUR;
    /**
     * Tile colour that is used non-destructively (changed while keeping original
     * colour TEXT_COLOUR)
     */
    private Color dynamicTextColour = Color.BLACK;

    /**
     * Construcs new Tile with transparent background
     * @param entry
     */
    public Tile(Entry entry){
        this.number = entry.getNumber();
        setSize(WIDTH,HEIGHT);
        setBackground(new Color(0,0,0,0));
    }
    /**
     * Renders Tile by in order:
     * 1. Turning on anti aliasing
     * 2. Rendering shadow rect
     * 3. Rendering Tile rect
     * 4. Rendering Tile number
     * @param graphics needed to render graphics
     */
    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);

        Graphics2D g2d = (Graphics2D)graphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        //Render shadow
        graphics.setColor(SHADOW_COLOUR);
        graphics.fillRoundRect(MARGIN-SHADOW_MARGIN,MARGIN-SHADOW_MARGIN,
                WIDTH- CORNER_SIZE /2+(2*SHADOW_MARGIN),
                HEIGHT- CORNER_SIZE /2+(2*SHADOW_MARGIN),
                CORNER_SIZE +(2*SHADOW_MARGIN),
                CORNER_SIZE +(2*SHADOW_MARGIN));
        //Render tile
        graphics.setColor(dynamicTileColour);
        graphics.fillRoundRect(MARGIN,MARGIN,WIDTH-CORNER_SIZE/2,
                HEIGHT-CORNER_SIZE/2, CORNER_SIZE, CORNER_SIZE);

        //Rendering tile number
        int fontSize = 200;
        Font font = new Font("Arial", Font.BOLD, fontSize);
        FontMetrics fontMetric = getFontMetrics(font);
        graphics.setFont(font);
        graphics.setColor(dynamicTextColour);
        graphics.drawString(""+number,(WIDTH-fontMetric.stringWidth(""+number))/2,
                (HEIGHT-fontMetric.getHeight())/2+fontMetric.getAscent());
    }
    /**
     * Starts an animation whereby Tile simultaneously fades to green and fades
     * away and then performing a sent function given as parameter "after"
     * @param after a function to be called after anumation is finished
     */
    public void animateFade(Runnable after){
        new Timer(4, new ActionListener() {
            private int iterations = 40;
            private int MAX_COLOR_VALUE = 255;
            private int step = MAX_COLOR_VALUE/ iterations;
            private boolean pause = false;
            @Override
            public void actionPerformed(ActionEvent event) {
                setOpaque(false);
                double red = dynamicTileColour.getRed();
                double blue = dynamicTileColour.getBlue();
                double alpha = dynamicTileColour.getAlpha();

                if (red <= step || blue <= step || alpha <= step){
                    pause = true;
                    cut(event);
                }
                repaint();

                dynamicTileColour = new Color((int)red- (pause? 0:step),
                        dynamicTileColour.getGreen(),
                        (int)blue- (pause? 0:step),
                        (int)alpha- (pause? 0:step));
                dynamicTextColour = new Color(dynamicTextColour.getRed(),
                        dynamicTextColour.getGreen(),
                        dynamicTextColour.getBlue(),
                        (int)alpha- (pause? 0:step));
            }
            private void cut(ActionEvent event){
                after.run();
                ((Timer)event.getSource()).stop();
            }
        }).start();
    }
    public int getWidth(){
        return WIDTH;
    }
    public int getHeight(){
        return HEIGHT;
    }
    public boolean equals(Object object){
        if (!(object instanceof Tile))
            return false;
        return number == ((Tile)object).number;
    }
}
