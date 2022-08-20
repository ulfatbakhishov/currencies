package az.digirella.assignment.currency;

import org.testcontainers.containers.MSSQLServerContainer;

/**
 * @author Ulphat
 */
public class TestMsSqlContainer extends MSSQLServerContainer<TestMsSqlContainer> {
    private static final String IMAGE_VERSION = "mcr.microsoft.com/mssql/server:2019-latest";
    private static TestMsSqlContainer container;

    private TestMsSqlContainer() {
        super(IMAGE_VERSION);
    }

    public static TestMsSqlContainer getInstance() {
        if (container == null) {
            container = new TestMsSqlContainer().acceptLicense();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }


}
