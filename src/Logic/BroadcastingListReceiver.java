package Logic;

/**
 * Interface to be implemented by classes that want to receive changes done
 * upon BroadcastingList
 * @param <T> generic type that must match generic type of BroadcastinList to be
 *           received from
 */
public interface BroadcastingListReceiver<T> {
    /**
     * Function called for all BroadcastingListReceivers upon item added to a
     * BroadcastingList
     * @param item added to BroadcastingList
     */
    void doOnAdd(T item);
    /**
     * Function called for all BroadcastingListReceivers upon item removed from a
     * BroadcastingList
     * @param item removed from BroadcastingList
     */
    void doOnRemove(T item);
}
