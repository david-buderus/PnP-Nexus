package de.pnp.manager;

/**
 * Enumeration for any JVM flags which configure PnP-Nexus.
 */
public enum EJvmFlag {

    /**
     * Enum value for the development mode. Configures the web frontend to run embedded, i.e. any html and ts files are
     * served by Spring.
     */
    DEV_MODE("pnp-nexus.dev-mode", false);

    private final String flag;
    private final String defaultValue;

    EJvmFlag(String flag, String defaultValue) {
        this.flag = flag;
        this.defaultValue = defaultValue;
    }

    EJvmFlag(String flag, boolean defaultValue) {
        this(flag, Boolean.toString(defaultValue));
    }

    /**
     * Returns the value of the flag.
     */
    public String getValue() {
        return System.getProperty(flag, defaultValue);
    }

    /**
     * Returns whether the flag is active. For this, the value needs to be <code>true</code>, case ignored.
     */
    public boolean isEnabled() {
        return Boolean.TRUE.toString().equals(getValue().toLowerCase());
    }
}
