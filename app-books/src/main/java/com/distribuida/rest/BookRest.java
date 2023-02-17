package com.distribuida.rest;

import com.distribuida.db.Book;
import com.distribuida.servicios.BookService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
@Path("/books")
public class BookRest {
    @Inject
    private BookService bookService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener libro")
    @APIResponse(responseCode = "200", description = "OK libro encontrado", content =
    @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @APIResponse(responseCode = "400", description = "Error!! libro no encontrado")
    public Response findById(@Parameter(description = "ID BOOK", required = true) @PathParam("id") Integer id)  {
        Book book = bookService.getBookById(id);
        if (book == null) {
            throw new NotFoundException("Libro no encontrado");
        }

        return Response.ok(book).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Buscar libros", description = "Obtiene todos los libros disponibles en la biblioteca")
    @APIResponse(responseCode = "200", description = "Operación exitosa", content =
    @Content(mediaType = "application/json", schema = @Schema(allOf = Book.class)))
    @APIResponse(responseCode = "500", description = "Error en el servidor")
    public Response findAll() {

        try {
            List<Book> books = bookService.getBooks();
            return Response.ok(books).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Crear un libro")
    @APIResponse(responseCode = "201", description = "El libro fue creado con éxito")
    @APIResponse(responseCode = "400", description = "La solicitud es inválida")
    @APIResponse(responseCode = "500", description = "Hubo un problema al momento de crear el libro")
    public Response create(
            @RequestBody(description = "Escriba el libro que va ha insertar", required = true,
                    content = @Content(schema = @Schema(implementation = Book.class)))
            Book b) {
        if (b == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El objeto Book es nulo")
                    .build();
        }
        try {
            bookService.createBook(b);
            return Response.status(Response.Status.CREATED)
                    .entity("El libro fue creado con éxito")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Hubo un problema al crear el libro: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Operation(description = "Actualizar un libro")
    @APIResponse(responseCode = "200", description = "El libro fue actualizado con éxito")
    @APIResponse(responseCode = "400", description = "La solicitud es inválida")
    @APIResponse(responseCode = "404", description = "El libro no se encuentra")
    @APIResponse(responseCode = "500", description = "Hubo un problema al actualizar el libro")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(
            @RequestBody(description = "Digite los datos a actualizar", required = true,
                    content = @Content(schema = @Schema(implementation = Book.class)))
            Book b,
            @Parameter(description = "ID BOOK", required = true)
            @PathParam("id") Integer id) {
        if (b == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El objeto Book es nulo")
                    .build();
        }
        try {
            Book existingBook = bookService.getBookById(id);
            if (existingBook == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontró el libro con ID " + id)
                        .build();
            }
            bookService.updateBook(id, b);
            return Response.status(Response.Status.OK)
                    .entity("El libro fue actualizado con éxito")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Hubo un problema al actualizar el libro: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(description = "Eliminar libro")
    @APIResponse(responseCode = "204", description = "El libro fue eliminado")
    @APIResponse(responseCode = "404", description = "El libro no se encontró")
    @APIResponse(responseCode = "500", description = "Hubo un problema con el servidor para eliminar el libro")
    public Response delete(@Parameter(description = "ID BOOK", required = true) @PathParam("id") Integer id) {
        try {
            bookService.delete(id);
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("El libro con id " + id + " ha sido eliminado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("El libro con id " + id + " no se encontró")
                    .build();

        }
    }

}
