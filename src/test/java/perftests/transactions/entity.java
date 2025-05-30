package perftests.transactions;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import java.io.IOException;
import java.time.Duration;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import us.abstracta.jmeter.javadsl.core.assertions.DslResponseAssertion.TargetField;
import us.abstracta.jmeter.javadsl.core.controllers.DslTransactionController;
import us.abstracta.jmeter.javadsl.core.testelements.DslScopedTestElement.Scope;



public class entity {

    public static DslTransactionController getEntity(int thinktime) throws IOException {

        String path = "/objects";

        return transaction("get-entities", 
            httpSampler(path)
                .followRedirects(true)
                .header("accept", "application/json")
                .children(
                responseAssertion()
                  .scope(Scope.ALL_SAMPLES)
                  .fieldToTest(TargetField.RESPONSE_BODY)
                  .containsSubstrings("name")
              ),threadPause(Duration.ofSeconds(thinktime))
        );
    };


    public static DslTransactionController createEntity(int thinktime) throws IOException {

        String path = "/objects";
        String newEntity = payloads.newEntity();

        return transaction("createNewEntity", 
            httpSampler(path)
                .method(HTTPConstants.POST)
                .body(newEntity)
                .followRedirects(true)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .children(
                responseAssertion()
                  .scope(Scope.ALL_SAMPLES)
                  .fieldToTest(TargetField.RESPONSE_BODY)
                  .containsSubstrings("name")
              ),threadPause(Duration.ofSeconds(thinktime))
        );
    };

    public static DslTransactionController getEntityById(int thinktime) throws IOException {

        String path = "/objects/${id}";

        return transaction("get-entity-by-id", 
            httpSampler(path)
                .followRedirects(true)
                .header("accept", "application/json")
                .children(
                responseAssertion()
                  .scope(Scope.ALL_SAMPLES)
                  .fieldToTest(TargetField.RESPONSE_BODY)
                  .containsSubstrings("name")
              ),threadPause(Duration.ofSeconds(thinktime))
        );
    };

}
