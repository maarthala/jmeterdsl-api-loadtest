package perftests.transactions;

import java.io.IOException;
import us.abstracta.jmeter.javadsl.core.controllers.DslTransactionController;

public class  transactions {

    public static DslTransactionController getEntities(int thinktime) throws IOException {
        return entity.getEntity(thinktime);
    }

    public static DslTransactionController newEntity(int thinktime) throws IOException {
        return entity.createEntity(thinktime);
    }

    public static DslTransactionController getEntitById(int thinktime) throws IOException {
        return entity.getEntityById(thinktime);
    }

}
