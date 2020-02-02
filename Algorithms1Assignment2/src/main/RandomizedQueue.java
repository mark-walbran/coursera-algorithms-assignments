/*
 * Randomized Queue class
 *
 * Creator: Mark Walbran
 *
 * Date: 23/02/2019
 *
 * Coursera - Algorithms 1 course: Assignment 2
 *
 * This class will provide functionality for a randomized queue data structure, similar to a stack
 * or queue, except that the item removed is chosen uniformly at random from items in the data
 * structure.
 *
 */

import edu.princeton.cs.algs4.StdRandom;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The {@code RandomizedQueue} class is similar to a stack or queue, except that the item removed is
 * chosen uniformly at random from items in the data structure. It supports the usual
 * <em>enqueue</em> and <em>dequeue</em> operations, along with methods for peeking at a random
 * item, testing if the stack is empty, and iterating through the items in random order.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

  /**
   * Array to hold items. May be bigger than the current size.
   */
  private Item[] items;

  /**
   * Number of items currently in the randomized queue.
   */
  private int size;

  /**
   * Initializes an empty randomized queue.
   */
  public RandomizedQueue() {
    items = (Item[]) new Object[1];
    size = 0;
  }

  /**
   * Returns whether the randomized queue is empty.
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns the number of items in the randomized queue.
   */
  public int size() {
    return size;
  }

  /**
   * Adds the item to this randomized queue.
   *
   * @param item the item to add
   */
  public void enqueue(Item item) {
    if (item == null) {
      throw new IllegalArgumentException("Null argument");
    }

    if (size == items.length) {
      resize(2 * items.length); // double size of array if necessary
    }
    items[size++] = item; // add item
  }

  /**
   * Removes and returns a random item.
   *
   * @return random item
   * @throws java.util.NoSuchElementException if this stack is empty
   */
  public Item dequeue() {
    if (isEmpty()) {
      throw new NoSuchElementException("Tried to deque from an empty queue.");
    }

    // Return random item and move last item to previous position of said random item.
    int index = StdRandom.uniform(size);
    Item item = items[index];
    items[index] = items[size - 1];
    items[size - 1] = null; // Don't keep an unnecessary reference.
    size--;

    // Shrink size of array if necessary.
    if (size > 0 && size == items.length / 4) {
      resize(items.length / 2);
    }

    return item;
  }

  /**
   * Return a random item (but do not remove it).
   *
   * @return random item
   * @throws java.util.NoSuchElementException if this stack is empty
   */
  public Item sample() {
    if (isEmpty()) {
      throw new NoSuchElementException("Stack underflow");
    }

    return items[StdRandom.uniform(size)];
  }

  /**
   * Returns an independent iterator over items in random order.
   *
   * @return an iterator to this randomized queue that iterates through the items in independently
   * random order.
   */
  @Override
  public Iterator<Item> iterator() {
    Item[] itemsCopy = Arrays.copyOf(items, size);
    StdRandom.shuffle(itemsCopy);
    return new ArrayIterator<>(itemsCopy);
  }

  /**
   * An iterator over a given array.
   *
   * <p>Doesn't implement {@link #remove()} since it's optional.
   */
  private static class ArrayIterator<Item> implements Iterator<Item> {

    private int nextIndex;
    private final Item[] items;

    public ArrayIterator(Item[] items) {
      this.items = items;
      nextIndex = items.length - 1;
    }

    @Override
    public boolean hasNext() {
      return nextIndex >= 0;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Item next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      return items[nextIndex--];
    }
  }

  /**
   * Resize the underlying array holding the elements to the given new capacity.
   */
  private void resize(int capacity) {
    assert capacity >= size;

    items = Arrays.copyOf(items, capacity);
  }
}
