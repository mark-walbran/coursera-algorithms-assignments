import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

class BaseballEliminationTest {

  @Test
  void constructorTest() {
    BaseballElimination baseballElimination4 = new BaseballElimination("test-input/teams4.txt");
    BaseballElimination baseballElimination5 = new BaseballElimination("test-input/teams5.txt");

    // Assert wins correct
    ArrayList<String> expectedNames4 = new ArrayList<>();
    expectedNames4.add("Atlanta");
    expectedNames4.add("Philadelphia");
    expectedNames4.add("New_York");
    expectedNames4.add("Montreal");

    ArrayList<String> expectedNames5 = new ArrayList<>();
    expectedNames5.add("New_York");
    expectedNames5.add("Baltimore");
    expectedNames5.add("Boston");
    expectedNames5.add("Toronto");
    expectedNames5.add("Detroit");

    int[] expectedWins4 = {83, 80, 78, 77};
    int[] expectedWins5 = {75, 71, 69, 63, 49};
    int[] expectedLosses4 = {71, 79, 78, 82};
    int[] expectedLosses5 = {59, 63, 66, 72, 86};
    int[] expectedRemaining4 = {8, 3, 6, 3};
    int[] expectedRemaining5 = {28, 28, 27, 27, 27};
    int[][] expectedRemainingMatrix4 = {{0, 1, 6, 1}, {1, 0, 0, 2}, {6, 0, 0, 0}, {1, 2, 0, 0}};
    int[][] expectedRemainingMatrix5 =
        {{0, 3, 8, 7, 3}, {3, 0, 2, 7, 7}, {8, 2, 0, 0, 3}, {7, 7, 0, 0, 3}, {3, 7, 3, 3, 0}};

    assertEquals(4, baseballElimination4.numberOfTeams());
    assertEquals(expectedNames4, baseballElimination4.teams());

    for (int i = 0; i < 4; i++) {
      assertEquals(expectedWins4[i], baseballElimination4.wins(expectedNames4.get(i)));
      assertEquals(expectedLosses4[i], baseballElimination4.losses(expectedNames4.get(i)));
      assertEquals(expectedRemaining4[i], baseballElimination4.remaining(expectedNames4.get(i)));

      for (int j = 0; j < 4; j++) {
        assertEquals(expectedRemainingMatrix4[i][j],
            baseballElimination4.against(expectedNames4.get(i), expectedNames4.get(j)));
      }
    }

    assertEquals(5, baseballElimination5.numberOfTeams());
    assertEquals(expectedNames5, baseballElimination5.teams());

    for (int i = 0; i < 5; i++) {
      assertEquals(expectedWins5[i], baseballElimination5.wins(expectedNames5.get(i)));
      assertEquals(expectedLosses5[i], baseballElimination5.losses(expectedNames5.get(i)));
      assertEquals(expectedRemaining5[i], baseballElimination5.remaining(expectedNames5.get(i)));

      for (int j = 0; j < 5; j++) {
        assertEquals(expectedRemainingMatrix5[i][j],
            baseballElimination5.against(expectedNames5.get(i), expectedNames5.get(j)));
      }
    }
  }

  @Test
  void eliminationTest() {
    BaseballElimination baseballElimination4 = new BaseballElimination("test-input/teams4.txt");
    BaseballElimination baseballElimination5 = new BaseballElimination("test-input/teams5.txt");

    assertFalse(baseballElimination4.isEliminated("Atlanta"));
    assertTrue(baseballElimination4.isEliminated("Philadelphia"));
    assertFalse(baseballElimination4.isEliminated("New_York"));
    assertTrue(baseballElimination4.isEliminated("Montreal"));

    assertNull(baseballElimination4.certificateOfElimination("Atlanta"));
    assertEquals(new HashSet<>(Arrays.asList("Atlanta", "New_York")),
        baseballElimination4.certificateOfElimination("Philadelphia"));
    assertNull(baseballElimination4.certificateOfElimination("New_York"));
    assertEquals(new HashSet<>(Collections.singletonList("Atlanta")),
        baseballElimination4.certificateOfElimination("Montreal"));

    assertFalse(baseballElimination5.isEliminated("New_York"));
    assertFalse(baseballElimination5.isEliminated("Baltimore"));
    assertFalse(baseballElimination5.isEliminated("Boston"));
    assertFalse(baseballElimination5.isEliminated("Toronto"));
    assertTrue(baseballElimination5.isEliminated("Detroit"));

    assertNull(baseballElimination5.certificateOfElimination("New_York"));
    assertNull(baseballElimination5.certificateOfElimination("Baltimore"));
    assertNull(baseballElimination5.certificateOfElimination("Boston"));
    assertNull(baseballElimination5.certificateOfElimination("Toronto"));
    assertEquals(new HashSet<>(Arrays.asList("New_York", "Baltimore", "Boston", "Toronto")),
        baseballElimination5.certificateOfElimination("Detroit"));
  }
}
