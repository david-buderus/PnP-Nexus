package de.pnp.manager.server.service.backup;

import de.pnp.manager.server.controller.backup.BackupExportController;
import de.pnp.manager.server.controller.backup.BackupImportController;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * Handles backups
 */
@RestController
@RequestMapping("/api/backup")
public class BackupService {

    @Autowired
    private BackupExportController exportController;

    @Autowired
    private BackupImportController importController;

    @GetMapping(path = "export", produces = "application/zip")
    @Operation(summary = "Creates a backup of the nexus", operationId = "exportBackup")
    public StreamingResponseBody exportBackup(HttpServletResponse response,
        @RequestParam(required = false) List<String> universes) {

        response.setHeader(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment;filename=\"PnP-Nexus-" + System.currentTimeMillis() + ".zip\"");

        return outputStream -> exportController.export(outputStream, universes);
    }

    @PostMapping(path = "import", consumes = "application/zip")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Import a backup to the nexus", operationId = "importBackup")
    public void importBackup(MultipartFile backup) {
        try (InputStream inputStream = backup.getInputStream()) {
            importController.importBackup(inputStream);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                e.getMessage(), e);
        }
    }
}
