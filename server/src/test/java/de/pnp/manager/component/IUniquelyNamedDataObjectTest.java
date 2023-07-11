package de.pnp.manager.component;

import static de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository.NAME_ATTRIBUTE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.reflections.scanners.Scanners.SubTypes;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;
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
        Reflections reflections = new Reflections("de.pnp.manager");

        Set<Class<?>> subTypes =
            reflections.get(SubTypes.of(IUniquelyNamedDataObject.class).asClass());

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
}