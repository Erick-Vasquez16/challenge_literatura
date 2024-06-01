package com.example.alura.cursos.literatura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {
    public String obtenerDatos(String url){
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)  // Configurar el cliente para seguir redirecciones
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RuntimeException("Error al obtener los datos. Código de estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al realizar la solicitud HTTP", e);
        }
    }
}
