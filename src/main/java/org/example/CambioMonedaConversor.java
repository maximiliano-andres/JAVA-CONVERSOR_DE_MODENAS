package org.example;

import java.util.Map;

public class CambioMonedaConversor {
    private String base_code; // Moneda base (ej. USD)
    private Map<String, Double> conversion_rates; // Mapeo de monedas y tasas

    // Getters
    public String getBaseCode() {
        return base_code;
    }

    public Map<String, Double> getConversionRates() {
        return conversion_rates;
    }
}
