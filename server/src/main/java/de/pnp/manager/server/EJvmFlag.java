package de.pnp.manager.server;

public enum EJvmFlag {

    DEV_MODE("pnp-nexus.dev-mode", Boolean.FALSE.toString());

    private final String flag;
    private final String defaultValue;

    EJvmFlag(String flag, String defaultValue) {
        this.flag = flag;
        this.defaultValue = defaultValue;
    }

    public String getValue() {
        System.out.println(System.getenv());
        return System.getProperty(flag, defaultValue);
    }
}
