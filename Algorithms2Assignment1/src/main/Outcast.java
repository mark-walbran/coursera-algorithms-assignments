import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Identifies an oucast from a list of WordNet nouns. For use on Coursera, Algorithms Part II
 * programming assignment.
 */
public class Outcast {

  private final WordNet wordNet;

  /**
   * Creates a word net object to check outcasts against.
   *
   * @param wordNet the word net
   */
  public Outcast(WordNet wordNet) {
    this.wordNet = wordNet;
  }

  /**
   * Given an array of WordNet nouns, return an outcast. It is assumed that argument to outcast()
   * contains only valid wordnet nouns (and that it contains at least two such nouns).
   *
   * @param nouns the array of nouns
   */
  public String outcast(String[] nouns) {
    // Find combined distance between each noun and all of the others
    int[] d = new int[nouns.length];
    int i = 0;
    for (String noun1 : nouns) {
      for (String noun2 : nouns) {
        d[i] += wordNet.distance(noun1, noun2);
      }
      i++;
    }

    // Find noun with max combined distance
    int maxIndex = 0;

    for (i = 0; i < d.length; i++) {
      maxIndex = d[i] > d[maxIndex] ? i : maxIndex;
    }

    return nouns[maxIndex];
  }

  /**
   * The following test client takes from the command line the name of a synset file the name of a
   * hypernym file, followed by the names of outcast files, and prints out an outcast in each file.
   *
   * @param args the file names.
   */
  public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++) {
      In in = new In(args[t]);
      String[] nouns = in.readAllStrings();
      StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }
  }
}
