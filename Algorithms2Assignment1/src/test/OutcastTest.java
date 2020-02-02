import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.princeton.cs.algs4.In;

class OutcastTest {

  @Test
  void test() {
    WordNet wordNet = new WordNet("test-input/synsets.txt", "test-input/hypernyms.txt");
    Outcast outcast = new Outcast(wordNet);

    In in5 = new In("test-input/outcast5.txt");
    In in8 = new In("test-input/outcast8.txt");
    In in11 = new In("test-input/outcast11.txt");

    String[] nouns5 = in5.readAllStrings();
    String[] nouns8 = in8.readAllStrings();
    String[] nouns11 = in11.readAllStrings();

    assertEquals("table", outcast.outcast(nouns5));
    assertEquals("bed", outcast.outcast(nouns8));
    assertEquals("potato", outcast.outcast(nouns11));
  }
}
