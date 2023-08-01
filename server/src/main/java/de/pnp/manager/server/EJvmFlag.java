package de.pnp.manager.server;

/**
 * Enumeration for any JVM flags which configure PnP-Nexus.
 */
public enum EJvmFlag {

    /**
     * Enum value for the development mode.
     * Configures the web frontend to run embedded, i.e. any html and ts files are served by Spring.
     */
    DEV_MODE("pnp-nexus.dev-mode", Boolean.FALSE.toString());

    private final String flag;
    private final String defaultValue;

    EJvmFlag(String flag, String defaultValue) {
        this.flag = flag;
        this.defaultValue = defaultValue;
    }

    /**
     * Returns the value of the flag.
     *
     * @return
     */
    public String getValue() {
        return System.getProperty(flag, defaultValue);
    }
}
