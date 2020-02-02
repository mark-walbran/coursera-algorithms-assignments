import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class WordNetTest {

  WordNet wordNet;

  @Test
  void testNonRootedDAGs() {
    assertThrows(IllegalArgumentException.class, () -> wordNet =
        new WordNet("test-input/synsets3.txt", "test-input/hypernyms3InvalidCycle.txt"));

    assertThrows(IllegalArgumentException.class, () -> wordNet =
        new WordNet("test-input/synsets3.txt", "test-input/hypernyms3InvalidTwoRoots.txt"));

    assertThrows(IllegalArgumentException.class, () -> wordNet =
        new WordNet("test-input/synsets6.txt", "test-input/hypernyms6InvalidCycle.txt"));

    assertThrows(IllegalArgumentException.class, () -> wordNet = new WordNet("test-input/synsets6.txt",
        "test-input/hypernyms6InvalidCycle+Path.txt"));

    assertThrows(IllegalArgumentException.class, () -> wordNet =
        new WordNet("test-input/synsets6.txt", "test-input/hypernyms6InvalidTwoRoots.txt"));
  }

  @Test
  void testSmallFiles() {
    wordNet = new WordNet("test-input/synsets6.txt", "test-input/hypernyms6TwoAncestors.txt");
    assertTrue(wordNet.isNoun("a"));
    assertFalse(wordNet.isNoun("one"));

    wordNet = new WordNet("test-input/synsets5000-subgraph.txt",
        "test-input/hypernyms5000-subgraph.txt");
  }

  @Test
  void test() {
    wordNet = new WordNet("test-input/synsets.txt", "test-input/hypernyms.txt");
    assertTrue(wordNet.isNoun("President"));
    assertFalse(wordNet.isNoun("dfsdfsdf"));

    assertEquals(23, wordNet.distance("white_marlin", "mileage"));
    assertEquals(33, wordNet.distance("Black_Plague", "black_marlin"));
    assertEquals(27, wordNet.distance("American_water_spaniel", "histology"));
    assertEquals(29, wordNet.distance("Brown_Swiss", "barrel_roll"));

  }
}
