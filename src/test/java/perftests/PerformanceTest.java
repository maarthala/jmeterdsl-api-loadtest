package perftests;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import org.junit.jupiter.api.Test;
import perftests.config.traits;
import perftests.scenarios.*;
import perftests.utils.*;
import us.abstracta.jmeter.javadsl.core.engines.EmbeddedJmeterEngine;

import static org.assertj.core.api.Assertions.assertThat;

public class PerformanceTest {

  @Test
  public void testPerformance() throws Exception {
    utils.getEnv();
    // traits.jedisConnect();

    testPlan(
      csvDataSet(String.format("src/test/resources/entities.csv", traits.DATA_FILE_INDEX))
        .stopThreadOnEOF(false),
      
      httpDefaults()
            .url(traits.BASE_URL)
            ,scn.scnEntities(traits.USERS,traits.RAMPUP_DURATION_IN_SEC,traits.TEST_DURATION_IN_SEC)
            ,htmlReporter(traits.RESULT_DIR ,traits.TEST_ID)
            // ,resultsTreeVisualizer()
    )
    .children(
          httpCache().disable())
    .runIn(new EmbeddedJmeterEngine().propertiesFile("src/test/resources/my.properties"));
  }
}
