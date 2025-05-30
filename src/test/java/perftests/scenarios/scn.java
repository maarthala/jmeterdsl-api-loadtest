package perftests.scenarios;
import java.io.IOException;
import java.time.Duration;
import perftests.transactions.*;
import us.abstracta.jmeter.javadsl.core.threadgroups.BaseThreadGroup.SampleErrorAction;
import us.abstracta.jmeter.javadsl.core.threadgroups.DslDefaultThreadGroup;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;


public class scn {

    public static DslDefaultThreadGroup  scnEntities(int threads, int rampup_in_seconds,int test_duration_in_sec)  throws IOException  {
      return threadGroup()
        .sampleErrorAction(SampleErrorAction.START_NEXT_ITERATION)
        .rampToAndHold(threads, Duration.ofSeconds(rampup_in_seconds),Duration.ofSeconds(test_duration_in_sec))
        .children(
          transactions.getEntities(2),
          transactions.newEntity(2),
          transactions.getEntitById(2)
        );
    }

}
