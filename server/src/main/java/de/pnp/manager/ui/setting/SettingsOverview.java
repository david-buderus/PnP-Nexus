package de.pnp.manager.ui.setting;

import de.pnp.manager.ui.IView;
import de.pnp.manager.ui.ViewPart;
import javafx.application.Application;
import javafx.scene.control.TabPane;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class SettingsOverview extends ViewPart {

    protected BasicSettingsView basicSettingsView;

    public SettingsOverview(IView parent, Application application) {
        super("settings.tabname", parent);

        TabPane root = new TabPane();

        basicSettingsView = new BasicSettingsView(this, application);
        root.getTabs().add(basicSettingsView);

        try {
            List<String> files = IOUtils.readLines(Objects.requireNonNull(getClass().getClassLoader()
                    .getResourceAsStream("settings/")), Charset.defaultCharset());

            for (String file : files) {
                try(
                        InputStream stream = getClass().getClassLoader().getResourceAsStream("settings/" + file);
                        InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(stream));
                        BufferedReader reader = new BufferedReader(streamReader)
                ) {
                    String title = reader.readLine();
                    Collection<String> content = reader.lines().filter(s -> !s.isBlank()).collect(Collectors.toList());

                    root.getTabs().add(new SettingsView(title, content, this));

                }
            }

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        this.setContent(root);
    }

    public void load(File file) {
        basicSettingsView.load(file);
    }
}
