package com.programacion.distribuida;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import javax.ws.rs.core.Application;


@OpenAPIDefinition(info = @Info(
        title = "app-authors",
        version = "1.0.0"

))
public class RestApplication extends Application {
}
