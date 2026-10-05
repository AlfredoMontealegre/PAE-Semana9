package ni.edu.uam.facturacionapp.Service;

import java.math.BigDecimal;

public final class Validacion {
    private Validacion() {}

    public static String texto(String valor, String campo, int max, boolean requerido) {
        String limpio = valor == null ? "" : valor.trim();

        if (requerido && limpio.isEmpty()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }

        if (limpio.length() > max) {
            throw new IllegalArgumentException(campo + ": máximo " + max + " caracteres.");
        }

        return limpio;
    }

    public static BigDecimal precio(String texto) {
        String valor = texto == null ? "" : texto.trim().replace(',', '.');
        final BigDecimal n;

        try {
            n = new BigDecimal(valor);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("El precio debe ser un valor numérico.");
        }

        if (n.signum() <= 0) {
            throw new NumberFormatException("El precio debe ser mayor que cero.");
        }

        if (n.compareTo(new BigDecimal("9999999999.99")) > 0) {
            throw new NumberFormatException("El precio supera el máximo permitido.");
        }

        if (n.stripTrailingZeros().scale() > 2) {
            throw new NumberFormatException("El precio permite como máximo 2 decimales.");
        }

        return n.setScale(2);
    }

    public static int existencia(String texto) {
        String valor = texto == null ? "" : texto.trim();
        final int n;

        try {
            n = Integer.parseInt(valor);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("La existencia debe ser un número entero.");
        }

        if (n < 0) {
            throw new NumberFormatException("La existencia no puede ser negativa.");
        }

        return n;
    }
}
