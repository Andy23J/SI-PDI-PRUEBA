package com.programacion.distribuida.rest;

import com.programacion.distribuida.db.Author;
import com.programacion.distribuida.repository.AuthorRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@ApplicationScoped
@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorRest {
    @Inject AuthorRepository repository;

    @GET
    @Path("/{id}")
    @Operation(summary = "Metodo para obtener el author por id")
    @APIResponse(responseCode = "200", description = "OK Author encontrado exitosamente", content =
    @Content(mediaType = "application/json", schema = @Schema(implementation = Author.class)))
    @APIResponse(responseCode = "400", description = "Error author no fue encontrado")
    public Response findById(@PathParam("id") Long id) {
        Author author = repository.findById(id);
        if (author == null) {
            throw new NotFoundException("El autor con el ID " + id + " no se encontró");
        }
        return Response.ok(author).build();
    }

    @GET
    @Operation(summary = "Obtener todos los autores")
    @APIResponse(responseCode = "200", description = "Todos los autores", content =
    @Content(mediaType = "application/json", schema = @Schema(allOf = Author.class)))
    public Collection<Author> getAllAuthors() {
        // Pagination parameters (optional)
        int page = 0;
        int size = 10;

        return repository
                .findAll()
                .stream().skip(page * size)
                .limit(size)
                .toList(); // or use List::of for an immutable list
    }

    @POST
    @Operation(description = "Insertar author ")
    @APIResponse(responseCode = "201", description = "OK author insertado con exito")
    @APIResponse(responseCode = "500", description = "ERROR se pudo insertar el author")
    public void insert(Author obj) {
        repository.persist(obj);
    }

    @PUT
    @Path("/{id}")
    @Operation(description = "Actualizar el author")
    @APIResponse(responseCode = "200", description = "OK author actualizado con exito")
    @APIResponse(responseCode = "500", description = "ERROR no se pudo actualizar el author")
    public void update(Author obj, @PathParam("id") Long id) {

        var author = repository.findById(id);

        author.setFirst_name(obj.getFirst_name());
        author.setLast_name(obj.getLast_name());
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Eliminar un autor")
    @APIResponse(responseCode = "204", description = "El autor fue eliminado correctamente")
    public void deleteAuthor(@PathParam("id") Long id) {
            repository.deleteById(id);
    }
}
