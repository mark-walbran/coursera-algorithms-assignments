import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

/**
 * An immutable word-net digraph data-type that represents the semantic relationship between various
 * synsets (sets of word synonyms). For use on Coursera, Algorithms Part II programming assignment.
 */
public class WordNet {

  private final ArrayList<String> synsetList = new ArrayList<>();
  private final HashMap<String, HashSet<Integer>> nounList = new HashMap<>();
  private final SAP sap;

  /**
   * Reads in the synsets and hypernyms files and creates a WordNet directed acyclical graph based
   * on them.
   *
   * @param synsets   a list of all noun synsets in WordNet, one per line.
   * @param hypernyms a list containg all of the hypernym relationships.
   * @throws IllegalArgumentException if null argument or if either argument does not correspond to
   *                                  a rooted DAG.
   */
  public WordNet(String synsets, String hypernyms) {
    if ((synsets == null) || (hypernyms == null)) {
      throw new IllegalArgumentException("Null input.");
    }

    // Read in the synsets and make them into a list
    int synsetCounter = 0;
    In synsetsIn = new In(synsets);

    while (!synsetsIn.isEmpty()) {
      String line = synsetsIn.readLine();
      String[] splitStrings = line.split(",");

      if (splitStrings.length < 3) {
        throw new IllegalArgumentException(
            "Synset input file line " + synsetCounter + " must have 3 fields.");
      }

      int id = Integer.parseInt(splitStrings[0]);
      if (id != synsetCounter) {
        throw new IllegalArgumentException("ID at synset input file line " + synsetCounter
            + " should be the same as line number.");
      }

      synsetList.add(splitStrings[1]);
      synsetCounter++;
    }

    // Read the hypernyms file and build a Digraph.
    final Digraph rootedDAG = new Digraph(synsetCounter);

    int hypernymCounter = 0;
    In hypernymsIn = new In(hypernyms);
    while (!hypernymsIn.isEmpty()) {
      String line = hypernymsIn.readLine();
      String[] splitStrings = line.split(",");

      if (splitStrings.length == 0) {
        throw new IllegalArgumentException(
            "Hypernym input file line " + hypernymCounter + " cannot be empty.");
      }

      int id = Integer.parseInt(splitStrings[0]);

      if (splitStrings.length > 1) {
        for (String s : Arrays.copyOfRange(splitStrings, 1, splitStrings.length)) {
          rootedDAG.addEdge(id, Integer.parseInt(s));
        }

        hypernymCounter++;
      }
    }

    // Ensure that the input is a rooted DAG
    // DAG if no cycles present (i.e. has topological order)
    DirectedCycle directedCycle = new DirectedCycle(rootedDAG);
    if (directedCycle.hasCycle()) {
      throw new IllegalArgumentException("Input not a DAG.");
    }

    // Rooted DAG if only one node without parent
    int rootsNumber = synsetCounter - hypernymCounter;
    if (rootsNumber != 1) {
      throw new IllegalArgumentException("Input not a rooted DAG.");
    }

    // Calculate noun list
    int i = 0;
    for (String synset : synsetList) {
      for (String noun : synset.split(" ")) {
        nounList.computeIfAbsent(noun, k -> new HashSet<>());
        nounList.get(noun).add(i);
      }
      i++;
    }

    sap = new SAP(rootedDAG);
  }

  /**
   * Returns all wordNet nouns in an iterator.
   *
   * @throws IllegalArgumentException if null argument.
   */
  public Iterable<String> nouns() {
    return nounList.keySet();
  }

  /**
   * Is the word a WordNet noun?
   *
   * @param word the word to test.
   * @throws IllegalArgumentException if null argument.
   */
  public boolean isNoun(String word) {
    if (word == null) {
      throw new IllegalArgumentException("Null input.");
    }

    return nounList.containsKey(word);
  }

  /**
   * Returns the distance between nounA and nounB. The distance is the shortest path (directed or
   * undirected) between the words in the wordMap digraph.
   *
   * @param nounA the first noun
   * @param nounB the second noun
   * @throws IllegalArgumentException if null argument or argument is not WordNet noun.
   */
  public int distance(String nounA, String nounB) {
    // Find noun in synset list and return index number
    Iterable<Integer> indexA = findIndex(nounA);
    Iterable<Integer> indexB = findIndex(nounB);

    return sap.length(indexA, indexB);
  }

  /**
   * Returns a synset that is the common ancestor of nounA and nounB in a shortest ancestral path.
   *
   * @param nounA the first noun
   * @param nounB the second noun
   * @throws IllegalArgumentException if null argument or argument is not WordNet noun.
   */
  public String sap(String nounA, String nounB) {
    Iterable<Integer> indexA = findIndex(nounA);
    Iterable<Integer> indexB = findIndex(nounB);

    int ancestorIndex = sap.ancestor(indexA, indexB);

    return synsetList.get(ancestorIndex);
  }

  private Iterable<Integer> findIndex(String noun) {
    if (noun == null) {
      throw new IllegalArgumentException("Null input.");
    }

    if (!isNoun(noun)) {
      throw new IllegalArgumentException("Input not a word net noun.");
    }

    // Find noun in synset list and return index number
    return nounList.get(noun);
  }
}
