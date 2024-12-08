package org.example;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Scanner;

public class Conversor_Principal {
    public static void main(String[] args) {

        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        Map<String, Double> conversionRates = null;

        try {
            // Crear solicitud HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://v6.exchangerate-api.com/v6/ea8901d8cac2574f97db6a5a/latest/USD"))
                    .build();

            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parsear la respuesta JSON
            CambioMonedaConversor exchangeData = gson.fromJson(response.body(), CambioMonedaConversor.class);

            // Obtener tasas de conversión
            conversionRates = exchangeData.getConversionRates();
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al conectar con la API. Verifique su conexión a Internet.");
            return;
        } catch (Exception e) {
            System.out.println("Error al procesar la respuesta de la API. Intente nuevamente más tarde.");
            return;
        }

        // Scanner para entrada de usuario
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            try {
                System.out.println("""
                        
                        ----------------------------------------------
                        
                        Sean Bienvenidos al Conversor de Monedas
                        
                        1) Dolar >> Peso Argentino
                        2) Peso Argentino >> Dolar
                        3) Dolar >> Real Brasileño
                        4) Real Brasileño >> Dolar
                        5) Dolar >> Peso Colombiano
                        6) Peso Colombiano >> Dolar
                        7) SALIR
                        
                        ----------------------------------------------
                        
                        Elija una opción válida:
                        """);

                int opcion = scanner.nextInt();

                if (opcion == 7) {
                    System.out.println("Saliendo del programa...");
                    continuar = false;
                    continue;
                }

                System.out.println("Ingrese el monto a convertir:");
                double monto = scanner.nextDouble();

                switch (opcion) {
                    case 1 -> convertirMoneda(conversionRates, "ARS", monto, "USD", "ARS");
                    case 2 -> convertirMonedaInversa(conversionRates, "ARS", monto, "ARS", "USD");
                    case 3 -> convertirMoneda(conversionRates, "BRL", monto, "USD", "BRL");
                    case 4 -> convertirMonedaInversa(conversionRates, "BRL", monto, "BRL", "USD");
                    case 5 -> convertirMoneda(conversionRates, "COP", monto, "USD", "COP");
                    case 6 -> convertirMonedaInversa(conversionRates, "COP", monto, "COP", "USD");
                    default -> System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (Exception e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número válido.");
                scanner.nextLine(); // Limpiar buffer
            }
        }
    }

    /**
     * Convierte una moneda y muestra el resultado.
     */
    private static void convertirMoneda(Map<String, Double> rates, String moneda, double monto, String from, String to) {
        try {
            double tasa = rates.get(moneda);
            System.out.printf("%s %.2f -> %s %.2f%n", from, monto, to, monto * tasa);
        } catch (NullPointerException e) {
            System.out.println("Error: La tasa de conversión para " + moneda + " no está disponible.");
        }
    }

    /**
     * Convierte una moneda de forma inversa y muestra el resultado.
     */
    private static void convertirMonedaInversa(Map<String, Double> rates, String moneda, double monto, String from, String to) {
        try {
            double tasa = rates.get(moneda);
            System.out.printf("%s %.2f -> %s %.2f%n", from, monto, to, monto / tasa);
        } catch (NullPointerException e) {
            System.out.println("Error: La tasa de conversión para " + moneda + " no está disponible.");
        }
    }
}
