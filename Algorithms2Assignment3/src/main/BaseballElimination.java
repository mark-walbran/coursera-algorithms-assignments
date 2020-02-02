import java.util.ArrayList;

import java.util.HashSet;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * In the baseball elimination problem, there is a division consisting of n teams. At some point
 * during the season, team i has w[i] wins, l[i] losses, r[i] remaining games, and g[i][j] games
 * left to play against team j. A team is mathematically eliminated if it cannot possibly finish the
 * season in (or tied for) first place. The goal is to determine exactly which teams are
 * mathematically eliminated. For simplicity, we assume that no games end in a tie (as is the case
 * in Major League Baseball) and that there are no rainouts (i.e., every scheduled game is played).
 */
public class BaseballElimination {

  private static final double INFINITY = Double.POSITIVE_INFINITY;

  private final int numberOfTeams;
  private final ArrayList<String> teamNames = new ArrayList<>();
  private final int[] wins;
  private final int[] losses;
  private final int[] remaining;
  private final int[][] remainingMatrix;

  private final ArrayList<Boolean> isEliminatedList;
  private final ArrayList<HashSet<String>> certificateOfEliminationList;


  /**
   * Create a baseball division from given filename.
   *
   * @param filename name of file to load.
   */
  public BaseballElimination(String filename) {
    int teamIndex = 0;

    In input = new In(filename);

    numberOfTeams = Integer.parseInt(input.readLine());
    wins = new int[numberOfTeams];
    losses = new int[numberOfTeams];
    remaining = new int[numberOfTeams];
    remainingMatrix = new int[numberOfTeams][numberOfTeams];

    isEliminatedList = new ArrayList<>(numberOfTeams);
    certificateOfEliminationList = new ArrayList<>(numberOfTeams);
    for (int i = 0; i < numberOfTeams; i++) {
      isEliminatedList.add(i, null);
      certificateOfEliminationList.add(i, null);
    }

    while (input.hasNextLine()) {
      String lineString = input.readLine();
      lineString = lineString.trim();
      String[] line = lineString.split("\\s+");

      teamNames.add(line[0]);
      wins[teamIndex] = Integer.parseInt(line[1]);
      losses[teamIndex] = Integer.parseInt(line[2]);
      remaining[teamIndex] = Integer.parseInt(line[3]);

      for (int i = 0; i < numberOfTeams; i++) {
        remainingMatrix[teamIndex][i] = Integer.parseInt(line[i + 4]);
      }

      teamIndex++;
    }

    if (teamIndex != numberOfTeams) {
      throw new IllegalArgumentException("Input file division must contain " + numberOfTeams
          + " teams, as specified by first line.");
    }
  }

  /**
   * Returns the total number of teams in the division.
   */
  public int numberOfTeams() {
    return numberOfTeams;
  }

  /**
   * Returns an iterator of all the teams in the division.
   */
  public Iterable<String> teams() {
    return teamNames;
  }

  /**
   * Returns the number of wins for given team.
   *
   * @param team to check.
   * @throws IllegalArgumentException if any input is not a valid team.
   */
  public int wins(String team) {
    validateTeamName(team);

    return wins[teamNames.indexOf(team)];
  }

  /**
   * Returns the number of losses for given team.
   *
   * @param team to check.
   * @throws IllegalArgumentException if any input is not a valid team.
   */
  public int losses(String team) {
    validateTeamName(team);

    return losses[teamNames.indexOf(team)];
  }

  /**
   * Returns the number of remaining games for given team.
   *
   * @param team to check.
   * @throws IllegalArgumentException if any input is not a valid team.
   */
  public int remaining(String team) {
    validateTeamName(team);

    return remaining[teamNames.indexOf(team)];
  }

  /**
   * Returns number of remaining games between team1 and team2.
   *
   * @param team1 first team to check.
   * @param team2 second team to check.
   * @throws IllegalArgumentException if any input is not a valid team.
   */
  public int against(String team1, String team2) {
    validateTeamName(team1);
    validateTeamName(team2);

    return remainingMatrix[teamNames.indexOf(team1)][teamNames.indexOf(team2)];
  }

  /**
   * Is given team eliminated?
   *
   * @param team to check.
   * @throws IllegalArgumentException if any input is not a valid team.
   */
  public boolean isEliminated(String team) {
    validateTeamName(team);

    int teamNumber = teamNames.indexOf(team);

    if (isEliminatedList.get(teamNumber) == null) {
      eliminationHelper(teamNumber);
    }

    return isEliminatedList.get(teamNumber);
  }

  /**
   * Returns subset R of teams that eliminates given team; null if not eliminated.
   *
   * @param team to check.
   * @throws IllegalArgumentException if any input is not a valid team.
   */
  public Iterable<String> certificateOfElimination(String team) {
    validateTeamName(team);

    int teamNumber = teamNames.indexOf(team);

    if (isEliminatedList.get(teamNumber) == null) {
      eliminationHelper(teamNumber);
    }

    if (!isEliminatedList.get(teamNumber)) {
      return null;
    } else {
      return certificateOfEliminationList.get(teamNumber);
    }
  }

  private void eliminationHelper(int teamNumber) {
    // Check for trivial elimination (i.e if w(x) + r(x) < w[i] for some i)
    int maxPossibleWins = wins[teamNumber] + remaining[teamNumber];
    for (int i = 0; i < numberOfTeams; i++) {
      if (maxPossibleWins < wins[i]) {
        isEliminatedList.set(teamNumber, Boolean.TRUE);

        HashSet<String> certificate = new HashSet<>(1);
        certificateOfEliminationList.set(teamNumber, certificate);
        certificate.add(teamNames.get(i));

        return;
      }
    }

    int numberRemainingGameCombinations = integerSeriesSum(numberOfTeams - 2);
    int numberVertices = numberOfTeams + numberRemainingGameCombinations + 1;

    FlowNetwork flowNetwork =
        flowNetworkCreator(teamNumber, numberRemainingGameCombinations, numberVertices);

    FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, flowNetwork.V() - 1);
    int maxFlow = (int) fordFulkerson.value();

    // Calculate if is eliminated or not
    int flowCapacity = 0;

    for (int team1 = 0; team1 < numberOfTeams; team1++) {
      if (team1 == teamNumber) {
        continue;
      }

      for (int team2 = team1 + 1; team2 < numberOfTeams; team2++) {
        if (team2 == teamNumber) {
          continue;
        }

        flowCapacity += remainingMatrix[team1][team2];
      }
    }

    if (maxFlow == flowCapacity) {
      isEliminatedList.set(teamNumber, Boolean.FALSE);
    } else {
      isEliminatedList.set(teamNumber, Boolean.TRUE);

      // For all team vertices, check if in certificate and add to ArrayList of indices if so
      ArrayList<Integer> certificateIndices = new ArrayList<>();

      // Add team to sink edges
      int teamSkip = 0;
      for (int team = 0; team < numberOfTeams; team++) {
        if (team == teamNumber) {
          teamSkip = 1;
          continue;
        }

        if (fordFulkerson.inCut((team - teamSkip) + numberRemainingGameCombinations + 1)) {
          certificateIndices.add(team);
        }
      }

      validateCertificate(certificateIndices, teamNumber);

      // Add certificate names to hash set
      int certificateSize = certificateIndices.size();
      HashSet<String> certificate = new HashSet<>(certificateSize);
      certificateOfEliminationList.set(teamNumber, certificate);
      for (Integer certificateIndex : certificateIndices) {
        certificate.add(teamNames.get(certificateIndex));
      }
    }
  }

  private FlowNetwork flowNetworkCreator(int teamNumber, int numberRemainingGameCombinations,
                                         int numberVertices) {
    FlowNetwork flowNetwork = new FlowNetwork(numberVertices);

    int edgeCounter = 1;
    int team1Skip = 0;

    for (int team1 = 0; team1 < numberOfTeams; team1++) {
      if (team1 == teamNumber) {
        team1Skip = 1;
        continue;
      }

      int team2Skip = 0;
      for (int team2 = team1 + 1; team2 < numberOfTeams; team2++) {
        if (team2 == teamNumber) {
          continue;
        }

        if (team2 >= teamNumber) {
          team2Skip = 1;
        }

        // Source to remaining game edges
        flowNetwork.addEdge(new FlowEdge(0, edgeCounter, remainingMatrix[team1][team2]));

        // Remaining game to team edges
        flowNetwork.addEdge(new FlowEdge(edgeCounter,
            numberRemainingGameCombinations + (team1 - team1Skip) + 1, INFINITY));
        flowNetwork.addEdge(new FlowEdge(edgeCounter,
            numberRemainingGameCombinations + (team2 - team2Skip) + 1, INFINITY));

        edgeCounter++;
      }
    }

    // Add team to sink edges
    int teamSkip = 0;
    for (int team = 0; team < numberOfTeams; team++) {
      if (team == teamNumber) {
        teamSkip = 1;
        continue;
      }
      flowNetwork.addEdge(new FlowEdge(numberRemainingGameCombinations + (team - teamSkip) + 1,
          numberOfTeams + numberRemainingGameCombinations,
          wins[teamNumber] + remaining[teamNumber] - wins[team]));

    }

    return flowNetwork;
  }

  private int integerSeriesSum(int n) {
    int sum = 0;

    for (int i = 1; i <= n; i++) {
      sum += i;
    }

    return sum;
  }

  private void validateCertificate(ArrayList<Integer> certificateIndices, int teamNumber) {
    double averageFinalWins = 0;

    int certificateSize = certificateIndices.size();
    for (Integer certificateIndex : certificateIndices) {
      averageFinalWins += wins[certificateIndex] + remaining[certificateIndex];
    }

    averageFinalWins = averageFinalWins / certificateSize;

    if (averageFinalWins <= (wins[teamNumber] + remaining[teamNumber])) {
      throw new RuntimeException(
          "Average final number of wins ((total current # wins + total # remaining games) / total number teams) for certificate of elimination should be greater than the maximum number of games the eliminated team can win.");
    }
  }

  private void validateTeamName(String team) {
    if (!teamNames.contains(team)) {
      throw new IllegalArgumentException(
          "Team name " + team + " is not in the valid list of teams for the division.");
    }
  }

  /**
   * Reads in a file and prints out all of the teams that are mathematically eliminated, along with
   * the certificates of elimination that prove this.
   *
   * <p>
   * NOTE: This comes from the assignment description.
   *
   * @param args file to read in containing baseball division information.
   */
  public static void main(String[] args) {
    BaseballElimination division = new BaseballElimination(args[0]);
    for (String team : division.teams()) {
      if (division.isEliminated(team)) {
        StdOut.print(team + " is eliminated by the subset R = { ");
        for (String t : division.certificateOfElimination(team)) {
          StdOut.print(t + " ");
        }
        StdOut.println("}");
      } else {
        StdOut.println(team + " is not eliminated");
      }
    }
  }
}
