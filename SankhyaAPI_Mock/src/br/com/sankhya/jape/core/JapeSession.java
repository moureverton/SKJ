package br.com.sankhya.jape.core;

import java.math.BigDecimal;

public class JapeSession {
    
    // Métodos estáticos comuns usados para pegar usuário logado, etc.
    public static Object getProperty(String name) { return null; }
    public static void putProperty(String name, Object value) {}
    
    // Às vezes usado para pegar ID do usuário logado
    public static BigDecimal getCodUsu() { return BigDecimal.ZERO; }
}