package de.pnp.manager.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import java.io.IOException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    /**
     * Returns all subclasses of the given baseClass.
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<Class<? extends T>> getAllSubClasses(Class<T> baseClass) {
        return getAllClasses(c -> c != baseClass && baseClass.isAssignableFrom(c)).stream()
            .map(c -> (Class<? extends T>) c)
            .collect(Collectors.toSet());
    }

    /**
     * Returns all classes under `de.pnp.manager` which are matching the given filter. At least one match is expected.
     */
    public static Set<Class<?>> getAllClasses(Predicate<Class<?>> filter) {
        Set<Class<?>> filteredClasses;

        try {
            filteredClasses = ClassPath.from(ClassLoader.getSystemClassLoader())
                .getAllClasses()
                .stream()
                .filter(clazz -> clazz.getPackageName().startsWith("de.pnp.manager"))
                .map(ClassInfo::load)
                .filter(filter)
                .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        // soundness check
        assertThat(filteredClasses).isNotEmpty();
        return filteredClasses;
    }
}
