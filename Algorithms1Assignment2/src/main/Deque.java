/*
 * Deque class
 *
 * Creator: Mark Walbran
 *
 * Date: 11/02/2019
 *
 * Coursera - Algorithms 1 course: Assignment 2
 *
 * This class will provide functionality for a double-ended queue or "deque" data structure, a
 * generalization of a stack and a queue that supports adding and removing items from either the
 * front or the back of the data structure.
 *
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The {@code Deque} or double-ended queue class is a generalization of a stack and a queue that
 * supports adding and removing items from either the front or the back of the data structure. It
 * supports the usual <em>push</em> and <em>pop</em> operations for both the front and back of the
 * data, along with methods for testing total size, testing if the double-ended queue is empty, and
 * iterating through the items in order from front to end.
 */
public class Deque<Item> implements Iterable<Item> {

  private int size;

  /**
   * Top of the stack.
   */
  private Node<Item> first;

  /**
   * Bottom of the stack.
   */
  private Node<Item> last;

  /**
   * Linked list node.
   */
  private static class Node<Item> {

    private Item item;
    private Node<Item> next;
    private Node<Item> previous;
  }

  /**
   * Initializes an empty double-ended queue.
   */
  public Deque() {
    first = null;
    last = null;
    size = 0;
    assert check();
  }

  /**
   * Returns whether the double-ended queue is empty.
   */
  public boolean isEmpty() {
    return first == null;
  }

  /**
   * Returns the number of items in the double-ended queue.
   */
  public int size() {
    return size;
  }

  /**
   * Adds the item to the front of the double-ended queue.
   *
   * @param item the item to add
   */
  public void addFirst(Item item) {
    if (item == null) {
      throw new IllegalArgumentException("Item must not be null.");
    }

    Node<Item> oldFirst = first;
    first = new Node<>();
    first.item = item;
    first.next = oldFirst;
    if (oldFirst == null) {
      last = first;
    } else {
      oldFirst.previous = first;
    }
    size++;
    assert check();
  }

  /**
   * Adds the item to the back of the double-ended queue.
   *
   * @param item the item to add
   */
  public void addLast(Item item) {
    if (item == null) {
      throw new IllegalArgumentException("Item must not be null.");
    }

    Node<Item> oldlast = last;
    last = new Node<>();
    last.item = item;
    last.previous = oldlast;
    if (oldlast == null) {
      first = last;
    } else {
      oldlast.next = last;
    }
    size++;
    assert check();
  }


  /**
   * Removes and returns the item at the front of the double-ended queue.
   *
   * @return the item at the front
   * @throws java.util.NoSuchElementException if this double-ended queue is empty
   */
  public Item removeFirst() {
    if (isEmpty()) {
      throw new NoSuchElementException("Trying to remove an item from an empty deque.");
    }

    Item item = first.item; // save item to return
    first = first.next; // delete first node
    if (first == null) {
      last = null;
    } else {
      first.previous = null;
    }
    size--;
    if (isEmpty()) {
      last = null;
    }
    assert check();
    return item; // return the saved item
  }

  /**
   * Removes and returns the item at the back of the double-ended queue.
   *
   * @return the item at the back
   * @throws java.util.NoSuchElementException if this double-ended queue is empty
   */
  public Item removeLast() {
    if (isEmpty()) {
      throw new NoSuchElementException("Trying to remove an item from an empty deque.");
    }

    Item item = last.item; // save item to return
    last = last.previous; // delete last node
    if (last == null) {
      first = null;
    } else {
      last.next = null;
    }
    size--;
    if (isEmpty()) {
      first = null;
    }
    assert check();
    return item; // return the saved item
  }

  /**
   * Returns an iterator to this double-ended queue that iterates through the items in LIFO order.
   */
  @Override
  public Iterator<Item> iterator() {
    return new ListIterator();
  }

  /**
   * An iterator for the Deque. Doesn't implement remove() since it's optional.
   */
  private class ListIterator implements Iterator<Item> {

    private Node<Item> current = first;

    @Override
    public boolean hasNext() {
      return current != null;
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
      Item item = current.item;
      current = current.next;
      return item;
    }
  }

  /**
   * Check internal invariants.
   */
  private boolean check() {
    assert size >= 0;
    if (size == 0) {
      assert first == null && last == null : "first and last must be null for empty Deque";
    } else {
      assert first != null && last != null : "first and last must not be null for non-empty Deque.";
      if (size == 1) {
        assert first.next == null
            && last.previous == null : "1-element Deque should not have next or previous nodes.";
      } else {
        assert first.next != null
            && last.previous != null : ">1 element Dequeue should have next and previous nodes.";
      }
    }

    // Check that size field is consistent with counting the linked list, both forwards and
    // backwards.
    int numberOfNodesNext = 0;
    int numberOfNodesPrevious = 0;
    for (Node<Item> node = first; node != null && numberOfNodesNext <= size; node = node.next) {
      numberOfNodesNext++;
    }
    for (Node<Item> node = last; node != null && numberOfNodesPrevious <= size; node =
        node.previous) {
      numberOfNodesPrevious++;
    }
    assert numberOfNodesNext == size;
    assert numberOfNodesPrevious == size;

    return true;
  }
}
