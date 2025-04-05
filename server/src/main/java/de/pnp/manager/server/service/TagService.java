package de.pnp.manager.server.service;

import de.pnp.manager.Tag;
import de.pnp.manager.security.UniverseRead;
import de.pnp.manager.server.database.TagRepository;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to access {@link TagRepository}.
 */
@RestController
@Validated
@RequestMapping("/api/{universe}/tags")
public class TagService {

    /**
     * {@link TagRepository} for this service.
     */
    @Autowired
    private TagRepository repository;


    @GetMapping
    @UniverseRead
    @Operation(summary = "Get all tags from the database", operationId = "getAllTags")
    public Collection<Tag> getAll(@PathVariable String universe) {
        return repository.getAll(universe);
    }
}
