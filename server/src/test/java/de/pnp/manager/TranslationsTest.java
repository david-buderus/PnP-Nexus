package de.pnp.manager;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the translations
 */
public class TranslationsTest {

    private static final File LOCALES = new File(
        Objects.requireNonNull(TranslationsTest.class.getClassLoader().getResource("public/locales")).getFile());

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final MapType MAP_TYPE = MAPPER.getTypeFactory()
        .constructMapType(HashMap.class, String.class, String.class);


    @ParameterizedTest(name = "{index} {0} of {1} is missing translations")
    @MethodSource("prepareLocalTranslationTest")
    void testLocalesTranslation(String translation, String languageFolder, Collection<String> existingKey,
        Collection<String> allKeys) {
        assertThat(existingKey).describedAs("The translation of '%s' is missing key in '%s'.",
                languageFolder, translation)
            .containsExactlyInAnyOrderElementsOf(allKeys);
    }

    private static Stream<Arguments> prepareLocalTranslationTest() throws IOException {
        File[] folders = LOCALES.listFiles(File::isDirectory);
        assertThat(folders).isNotEmpty();

        Set<String> translations = new HashSet<>();
        for (File folder : folders) {
            translations.addAll(Arrays.asList(Objects.requireNonNull(folder.list())));
        }
        assertThat(translations).isNotEmpty();

        Collection<Arguments> arguments = new ArrayList<>();

        for (String translation : translations) {
            Set<String> keys = new HashSet<>();
            Multimap<File, String> existingKeys = MultimapBuilder.hashKeys().hashSetValues().build();

            for (File folder : folders) {
                File translationFile = new File(folder, translation);
                Map<String, String> translationMap;
                if (translationFile.exists()) {
                    translationMap = MAPPER.readValue(translationFile, MAP_TYPE);
                } else {
                    translationMap = Map.of();
                }
                keys.addAll(translationMap.keySet());
                existingKeys.putAll(folder, translationMap.keySet());
            }

            for (File folder : folders) {
                arguments.add(Arguments.of(translation, folder.getName(), existingKeys.get(folder), keys));
            }
        }

        return arguments.stream();
    }
}
