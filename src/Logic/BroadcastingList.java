package Logic;

import java.util.ArrayList;

/**
 * Extension of ArrayList that broadcasts additions to and removals from
 * BroadcastingList to a list of receivers
 * @param <T> generic type to be contained in BroadcastingList
 */
public class BroadcastingList<T> extends ArrayList<T> {
    /**
     * List of BroadcastingListReceivers to be receiving broadcasts of changes
     * done to BroadcastingList
     */
    ArrayList<BroadcastingListReceiver> receivers = new ArrayList<>();

    /**
     * Adding to BroadcastingList and broadcasting change to all receivers
     * @param item to be added
     * @return if successfully added
     */
    public boolean add(T item){
        boolean success = super.add(item);
        for (BroadcastingListReceiver receiver: receivers)
            receiver.doOnAdd(item);
        return success;
    }
    /**
     * Removing from BroadcastingList and broadcasting change to all receivers
     * @param item to be removed
     * @return if successfully removed
     */
    public boolean remove(Object item){
        boolean success = super.remove(item);
        for (BroadcastingListReceiver receiver: receivers)
            receiver.doOnRemove(item);
        return success;
    }
    /**
     * Adds receiver to be notified upon change done to BroadcastingList
     * @param receiver to be added
     * @return if receiver added successfully
     */
    public boolean addReceiver(BroadcastingListReceiver receiver){
        return receivers.add(receiver);
    }
    /**
     * Removes receiver from list to be notified upon change done to BroadcastingList
     * @param receiver to be removed
     * @return if receiver removed successfully
     */
    public boolean removeReceiver(BroadcastingListReceiver receiver){
        return receivers.remove(receiver);
    }
}
