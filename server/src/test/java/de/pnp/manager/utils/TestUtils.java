package de.pnp.manager.utils;

/**
 * Utility for tests.
 */
public class TestUtils {

    /**
     * Returns whether the tests are currently running in a CI pipeline.
     */
    public static boolean isRunningInCI() {
        return Boolean.parseBoolean(System.getenv("RUNNING_IN_CI"));
    }
}
