package de.pnp.manager;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    private static final File RESOURCES = new File("src/main/resources");

    private static final Pattern MESSAGES_PATTERN = Pattern.compile("messages_(.*).properties");

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final MapType MAP_TYPE = MAPPER.getTypeFactory()
        .constructMapType(HashMap.class, String.class, String.class);


    @ParameterizedTest(name = "{index} {0} of {1} is missing translations")
    @MethodSource("prepareLocalTranslationTest")
    void testLocalesTranslation(String translation, String languageFolder, Collection<String> existingKey,
        Collection<String> allKeys) {
        assertThat(existingKey).describedAs("The translation of '%s' is missing keys in '%s'.",
                languageFolder, translation)
            .containsExactlyInAnyOrderElementsOf(allKeys);
    }

    @ParameterizedTest(name = "{index} {0} is missing translations")
    @MethodSource("prepareMessagesTranslationTest")
    void testMessagesTranslation(String language, Collection<String> existingKey,
        Collection<String> allKeys) {
        assertThat(existingKey).describedAs("The translation of '%s' is missing keys.", language)
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

    private static Stream<Arguments> prepareMessagesTranslationTest() throws IOException {
        File[] files = Objects.requireNonNull(
            RESOURCES.listFiles((file, name) -> MESSAGES_PATTERN.matcher(name).matches()));

        Set<String> keys = new HashSet<>();
        Multimap<File, String> existingKeys = MultimapBuilder.hashKeys().hashSetValues().build();

        for (File file : files) {
            for (String line : Files.readAllLines(file.toPath(), Charset.forName("ISO_8859_1"))) {
                String key = line.split("=", 2)[0].trim();
                keys.add(key);
                existingKeys.put(file, key);
            }
        }

        Collection<Arguments> arguments = new ArrayList<>();

        for (File file : files) {
            Matcher matcher = MESSAGES_PATTERN.matcher(file.getName());
            if (!matcher.matches()) {
                throw new IllegalStateException();
            }
            arguments.add(Arguments.of(matcher.group(1), existingKeys.get(file), keys));
        }

        return arguments.stream();
    }
}
