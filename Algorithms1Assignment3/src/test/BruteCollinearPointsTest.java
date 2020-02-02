/**
 * Unit tests for {@link BruteCollinearPoints}.
 */
class BruteCollinearPointsTest extends CollinearPointsTest {

  @Override
  CollinearPoints getInstance() {
    return new BruteCollinearPoints();
  }
}
