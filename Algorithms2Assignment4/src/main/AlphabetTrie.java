/**
 * 26-way Trie Implementation.
 *
 * Some code taken from the TrieST class from the algs4 library provided by Princeton University for their Algorithms and Data Structures courses.
 */
public class AlphabetTrie {
  private static final int R = 26; // Alphabet
  private Node root; // root of trie
  private int n; // number of keys in trie

  // R-way trie node
  private static class Node {
    private int val = -1;
    private Node[] next = new Node[R];
  }

  /**
     * Initializes an empty string symbol table.
   */
  public AlphabetTrie() {

  }

  /**
   * Returns the value associated with the given key.
   * @param key the key
   * @return the value associated with the given key if the key is in the symbol table
   *     and {@code null} if the key is not in the symbol table
   * @throws IllegalArgumentException if {@code key} is {@code null}
   */
  public int get(String key) {
    if (key == null) {
      throw new IllegalArgumentException("argument to get() is null");
    }

    Node node = getHelper(key);

    if (node == null) {
      return -1;
    }

    return node.val;
  }

  /**
   * Does this symbol table contain the given key?
   * @param key the key
   * @return {@code true} if this symbol table contains {@code key} and
   *     {@code false} otherwise
   * @throws IllegalArgumentException if {@code key} is {@code null}
   */
  public boolean contains(String key) {
    if (key == null) {
      throw new IllegalArgumentException("argument to contains() is null");
    }

    Node node = getHelper(key);
    return node != null && node.val != -1;
  }

  /**
   * Does this symbol table contain the given key prefix?
   * @param prefix the key prefix
   * @return {@code true} if this symbol table contains {@code prefix} and
   *     {@code false} otherwise
   * @throws IllegalArgumentException if {@code prefix} is {@code null}
   */
  public boolean containsPrefix(String prefix) {
    if (prefix == null) {
      throw new IllegalArgumentException("argument to wordsHavePrefix() is null");
    }

    return getHelper(prefix) != null;
  }

  private Node getHelper(String key) {
    Node node = root;
    int d = 0;

    while (d < key.length()) {
      if (node == null) {
        return null;
      }

      char c = (char) (key.charAt(d) - 65);
      node = node.next[c];
      d++;
    }

    return node;
  }

  /**
   * Inserts the key-value pair into the symbol table, overwriting the old value
   * with the new value if the key is already in the symbol table.
   * If the value is {@code null}, this effectively deletes the key from the symbol table.
   * @param key the key
   * @param val the value
   * @throws IllegalArgumentException if {@code key} is {@code null}
   */
  public void put(String key, int val) {
    if (key == null) {
      throw new IllegalArgumentException("first argument to put() is null");
    }

    if (val == -1) {
      delete(key);
    } else {
      int d = 0;

      if (this.root == null) {
        this.root = new Node();
      }
      Node node = this.root;

      while (d < key.length()) {
        char c = key.charAt(d);
        d++;

        int index = c - 'A';
        if (node.next[index] == null) {
          node.next[index] = new Node();
        }
        node = node.next[index];
      }

      if (node.val == -1) {
        n++;
      }

      node.val = val;
    }
  }

  /**
   * Returns the number of key-value pairs in this symbol table.
   * @return the number of key-value pairs in this symbol table
   */
  public int size() {
    return n;
  }

  /**
   * Is this symbol table empty?
   * @return {@code true} if this symbol table is empty and {@code false} otherwise
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Removes the key from the set if the key is present.
   * @param key the key
   * @throws IllegalArgumentException if {@code key} is {@code null}
   */
  public void delete(String key) {
    if (key == null) {
      throw new IllegalArgumentException("argument to delete() is null");
    }

    root = delete(root, key, 0);
  }

  private Node delete(Node x, String key, int d) {
    if (x == null) {
      return null;
    }

    if (d == key.length()) {
      if (x.val != -1) {
        n--;
      }

      x.val = -1;
    } else {
      char c = key.charAt(d);
      x.next[c - 65] = delete(x.next[c - 65], key, d + 1);
    }

    // remove subtrie rooted at x if it is completely empty
    if (x.val != -1) {
      return x;
    }

    for (int c = 0; c < R; c++) {
      if (x.next[c - 65] != null) {
        return x;
      }
    }

    return null;
  }
}
