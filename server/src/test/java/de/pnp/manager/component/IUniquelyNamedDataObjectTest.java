package de.pnp.manager.component;

import static de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository.NAME_ATTRIBUTE;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Tests for {@link IUniquelyNamedDataObject}
 */
class IUniquelyNamedDataObjectTest {

    @ParameterizedTest
    @MethodSource("provideIUniquelyNamedDataObjectClasses")
    void testCorrectImplementations(Class<? extends IUniquelyNamedDataObject> implClass) {
        Field field = getDeclaredField(implClass, NAME_ATTRIBUTE);
        assertThat(field).describedAs(implClass.getSimpleName() + " does not has the necessary field '"
            + NAME_ATTRIBUTE + "'.").isNotNull();

        Indexed annotation = Objects.requireNonNull(field).getAnnotation(Indexed.class);
        assertThat(annotation).describedAs("The field '" + NAME_ATTRIBUTE + "' needs to be annotated with 'Indexed'.")
            .isNotNull();
        assertThat(annotation.unique()).describedAs("The field '" + NAME_ATTRIBUTE + "' has to be unique.").isTrue();
    }

    private static Stream<Arguments> provideIUniquelyNamedDataObjectClasses() {
        Set<Class<?>> subTypes;

        try {
            subTypes = ClassPath.from(ClassLoader.getSystemClassLoader())
                .getAllClasses()
                .stream()
                .filter(clazz -> clazz.getPackageName().startsWith("de.pnp.manager"))
                .map(ClassInfo::load)
                .filter(IUniquelyNamedDataObjectTest::implementsIUniquelyNamedDataObject)
                .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        // soundness check
        assertThat(subTypes).isNotEmpty();
        return subTypes.stream().map(clazz -> Arguments.of(Named.of(clazz.getSimpleName(), clazz)));
    }

    private static Field getDeclaredField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                return getDeclaredField(superclass, name);
            }
        }
        return null;
    }

    private static boolean implementsIUniquelyNamedDataObject(Class<?> clazz) {
        if (Arrays.asList(clazz.getInterfaces()).contains(IUniquelyNamedDataObject.class)) {
            return true;
        }
        return clazz.getSuperclass() != null && implementsIUniquelyNamedDataObject(clazz.getSuperclass());
    }
}