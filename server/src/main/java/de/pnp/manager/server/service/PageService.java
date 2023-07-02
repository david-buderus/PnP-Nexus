package de.pnp.manager.server.service;

import de.pnp.manager.server.EJvmFlag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import java.net.MalformedURLException;
import java.net.URI;

@RestController
public class PageService {

    @GetMapping(value = "{destination:(?!.*api).+}", produces = MediaType.TEXT_HTML_VALUE)
    public String getPage() {
        String indexHtml = getIndexHtml();
        if (Boolean.parseBoolean(EJvmFlag.DEV_MODE.getValue())) {
            indexHtml = redirectToDevServer(indexHtml);
        }
        return indexHtml;
    }

    @GetMapping(value = "@fs/**")
    public RedirectView getFromFileSystem() throws MalformedURLException {
        if (!Boolean.parseBoolean(EJvmFlag.DEV_MODE.getValue())) {
            return null;
        }
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
        builder.host("localhost");
        builder.port("5173");
        URI uri = builder.build().toUri();
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(uri.toURL().toString());
        return redirectView;
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
