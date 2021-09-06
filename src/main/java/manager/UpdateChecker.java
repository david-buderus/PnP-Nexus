package manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.web.Release;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Comparator;

public abstract class UpdateChecker {

    private static final String url = "https://api.github.com/repos/david-buderus/PnP-Manager/releases";

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final VersionComparator comparator = new VersionComparator();

    private static Configuration versionProperties = null;


    static {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(params.properties()
                                .setFileName("Version.properties")
                                .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));

        try {
            versionProperties = builder.getConfiguration();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static UpdateResponse checkForUpdates() {

        HttpRequest request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Release[] releases = objectMapper.readValue(response.body(), Release[].class);

            var optNewestRelease = Arrays.stream(releases).min(Comparator.comparing(release -> release.tag_name, comparator));

            if (optNewestRelease.isPresent()) {
                Release newestRelease = optNewestRelease.get();

                if (!newestRelease.tag_name.equals(Utility.getConfig().getString("version.skip"))) {
                    if (comparator.compare(versionProperties.getString("version"), newestRelease.tag_name) > 0) {
                        return new UpdateResponse(true, newestRelease.tag_name, newestRelease.body, newestRelease.html_url);
                    }
                }
            }

        } catch (IOException | InterruptedException ignored) { }

        return UpdateResponse.NoNewUpdate;
    }

    public static class UpdateResponse {

        public final static UpdateResponse NoNewUpdate = new UpdateResponse(false, null, null, null);

        public final boolean updateDoesExists;
        public final String newVersion;
        public final String info;
        public final String url;

        public UpdateResponse(boolean updateDoesExists, String newVersion, String info, String url) {
            this.updateDoesExists = updateDoesExists;
            this.newVersion = newVersion;
            this.info = info;
            this.url = url;
        }
    }

    private static class VersionComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            o1 = o1.trim();
            o2 = o2.trim();

            if (!o1.matches("v[0-9]+.[0-9]+.[0-9]+") || !o2.matches("v[0-9]+.[0-9]+.[0-9]+")) {
                throw new IllegalArgumentException("The versions are not in the right format");
            }

            o1 = o1.substring(1);
            o2 = o2.substring(1);

            int[] versions1 = Arrays.stream(o1.split("\\.")).mapToInt(Integer::parseInt).toArray();
            int[] versions2 = Arrays.stream(o2.split("\\.")).mapToInt(Integer::parseInt).toArray();

            if (versions1[0] > versions2[0]) {
                return -1;
            }
            if (versions1[0] == versions2[0]) {

                if (versions1[1] > versions2[1]) {
                    return -1;
                }
                if (versions1[1] == versions2[1]) {

                    if (versions1[2] > versions2[2]) {
                        return -1;
                    }
                    if (versions1[2] == versions2[2]) {
                        return 0;
                    }
                }
            }

            return 1;
        }
    }
}
