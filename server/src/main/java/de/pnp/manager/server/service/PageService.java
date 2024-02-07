package de.pnp.manager.server.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import de.pnp.manager.EJvmFlag;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service which returns html documents.
 */
@RestController
public class PageService {

    private static final String DEV_MODE_INDEX = """
        <html>
        <head>
            <script type="module">
              import RefreshRuntime from 'http://localhost:5173/@react-refresh'
              RefreshRuntime.injectIntoGlobalHook(window)
              window.$RefreshReg$ = () => {}
              window.$RefreshSig$ = () => (type) => type
              window.__vite_plugin_react_preamble_installed__ = true
            </script>
            <script type="module" src="http://localhost:5173/src/Index.tsx"></script>
        </head>
        <body>
        <div id="app"></div>
        </body>
        </html>
        """;

    @Value("classpath:static/index.html")
    private Resource resource;

    /**
     * Returns the base index.html.
     */
    @GetMapping(value = "{destination:(?!.*api)(?!.*favicon.ico).+}", produces = MediaType.TEXT_HTML_VALUE)
    public String getPage(@PathVariable String destination) {
        if (EJvmFlag.DEV_MODE.isEnabled()) {
            return DEV_MODE_INDEX;
        }
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
