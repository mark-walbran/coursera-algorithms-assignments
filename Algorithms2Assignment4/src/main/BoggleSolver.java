import java.util.HashSet;
import java.util.Set;

/**
 * Boggle is a word game designed by Allan Turoff and distributed by Hasbro. It involves a board
 * made up of 16 cubic dice, where each die has a letter printed on each of its 6 sides. At the
 * beginning of the game, the 16 dice are shaken and randomly distributed into a 4-by-4 tray, with
 * only the top sides of the dice visible. The players compete to accumulate points by building
 * valid words from the dice, according to these rules:
 * <p>
 * A valid word must be composed by following a sequence of adjacent dice, two dice are adjacent if
 * they are horizontal, vertical, or diagonal neighbours.
 * <p>
 * A valid word can use each die at most once.
 * <p>
 * A valid word must contain at least 3 letters.
 * <p>
 * A valid word must be in the dictionary (which typically does not contain proper nouns).
 */
public class BoggleSolver {

  private final AlphabetTrie trie = new AlphabetTrie();

  /**
   * Initializes the data structure using the given array of strings as the dictionary.
   * <p>
   * <p>
   * It is assumed that each word in the dictionary contains only the uppercase letters A through
   * Z.
   *
   * @param dictionary the array of strings representing the dictionary to read in.
   */
  public BoggleSolver(String[] dictionary) {
    for (String word : dictionary) {
      trie.put(word, scoreOfHelper(word));
    }
  }

  /**
   * Returns the set of all valid words in the given Boggle board, as an Iterable.
   *
   * @param board the Boggle board to search for words.
   */
  public Iterable<String> getAllValidWords(BoggleBoard board) {
    Set<String> validWords = new HashSet<>();

    // Go through each cell in the 2D board and consider words starting with the letter of that cell
    for (int row = 0; row < board.rows(); row++) {
      for (int col = 0; col < board.cols(); col++) {
        boolean[][] usedCells = new boolean[board.rows()][board.cols()];
        usedCells[row][col] = true;
        validWords = validWords(board, usedCells, row, col,
            letterToString(board.getLetter(row, col)), validWords);
      }
    }

    return validWords;
  }

  /**
   * Returns the letter character as a string, except for the special case 'q' which is returned as
   * "qu".
   */
  private String letterToString(char letter) {
    String word;
    if (letter == 'Q') {
      word = "QU";
    } else {
      word = Character.toString(letter);
    }
    return word;
  }

  private Set<String> validWords(BoggleBoard board, boolean[][] usedCells, int activeRow,
                                 int activeCol, String word, Set<String> validWords) {
    if (!trie.containsPrefix(word)) {
      return validWords;
    }

    if (word.length() >= 3 && trie.contains(word)) {
      validWords.add(word);
    }

    for (int row = activeRow - 1; row <= activeRow + 1; row++) {
      for (int col = activeCol - 1; col <= activeCol + 1; col++) {
        if (row == activeRow && col == activeCol) {
          continue;
        }

        if (row < 0 || row >= usedCells.length || col < 0 || col >= usedCells[0].length) {
          continue;
        }

        if (usedCells[row][col]) {
          continue;
        }

        usedCells[row][col] = true;
        validWords = validWords(board, usedCells, row, col,
            word + letterToString(board.getLetter(row, col)), validWords);
        usedCells[row][col] = false;
      }
    }

    return validWords;
  }

  /**
   * Returns the score of the given word if it is in the dictionary, zero otherwise.
   * <p>
   * <p>
   * Assumed that the word contains only the uppercase letters A through Z.
   * <p>
   *
   * <table>
   *   <tr>
   *     <td> <b> Word Length </b> </td> <td> <b> Points </b> </td>
   *   </tr>
   *   <tr>
   *     <td> 3-4 </td> <td> 1 </td>
   *   </tr>
   *   <tr>
   *     <td> 5 </td> <td> 2 </td>
   *   </tr>
   *   <tr>
   *     <td> 6 </td> <td> 3 </td>
   *   </tr>
   *   <tr>
   *     <td> 7 </td> <td> 5 </td>
   *   </tr>
   *   <tr>
   *     <td> 8+ </td> <td> 11 </td>
   *   </tr>
   * </table>
   *
   * @param word the word to test.
   */
  public int scoreOf(String word) {
    if (!trie.contains(word)) {
      return 0;
    }

    return trie.get(word);
  }

  private int scoreOfHelper(String word) {
    int length = word.length();

    if (length < 3) {
      return 0;
    } else if (length < 5) {
      return 1;
    } else if (length < 6) {
      return 2;
    } else if (length < 7) {
      return 3;
    } else if (length < 8) {
      return 5;
    } else {
      return 11;
    }
  }
}
