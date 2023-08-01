package de.pnp.manager.server.service;

import de.pnp.manager.server.EJvmFlag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service which returns html documents.
 */
@RestController
public class PageService {

    /**
     * Returns the base index.html.
     */
    @GetMapping(value = "{destination:(?!.*api).+}", produces = MediaType.TEXT_HTML_VALUE)
    public String getPage(@PathVariable String destination) {
        String indexHtml = getIndexHtml();
        if (Boolean.parseBoolean(EJvmFlag.DEV_MODE.getValue())) {
            indexHtml = redirectToDevServer(indexHtml);
        }
        return indexHtml;
    }

    private String redirectToDevServer(String indexHtml) {
        return """
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
    }

    private String getIndexHtml() {
        return null;
    }
}
