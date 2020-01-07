/**
 * @author Simon Gombrii
 * @version 1.0
 * @since 2019-10-30
 *
 * Note: Some of this code was written by the company of wine (read "all").
 */

package Logic;

import Graphics.Canvas;

/**
 * The main class. It manages all additions to and removals from the list of
 * entries and manages the unofficial AutoAdd feature. An instance of this class
 * is available to all other classes for adding and removal of entries.
 */
public class Manager {

    /**
     * List of current entries
     */
    private BroadcastingList<Entry> entries = new BroadcastingList<>();
    /**
     * Instance of this Manager available to other classes to add and remove
     * new entries
     */
    public static Manager instance;
    private static final int START_AUTO = 0;
    private static int autoCounter = START_AUTO;
    public static final int MAX_ENTRY_DIGITS = 2;
    private static final boolean AUTO_ADD = false;

    public static void main(String[] args){
        instance = new Manager();
        instance.run();
    }
    private void run(){
        entries.addReceiver(new Canvas());
    }
    /**
     * Adding new Entry to list of entries.
     * @param entryNumber the number given to the entry being added
     * @return if added successfully
     */
    public boolean add(int entryNumber){
        return entries.add(new Entry(entryNumber));
    }
    /**
     * Removing Entry from list of entries.
     * @param entryNumber the number of the Entry to be removed
     * @return if added successfully
     */
    public boolean remove(int entryNumber){
        return entries.remove(new Entry(entryNumber));
    }
    /**
     * Supplies a number between variable START_AUTO and MAX_ENTRY_DIGITS
     * @return autoCounter + 1
     */
    public int autoNext(){
        return autoCounter++%(int)(Math.pow(10, MAX_ENTRY_DIGITS));
    }
    public boolean autoAddOn(){
        return AUTO_ADD;
    }
}
