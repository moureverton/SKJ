package br.com.evertech.simp.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Map;

public class SimpLayoutManager {

    private JsonArray fieldsConfig;

    public SimpLayoutManager(String jsonPath) {
        try {
            // Lê o arquivo de dentro do JAR
            Reader reader = new InputStreamReader(getClass().getResourceAsStream(jsonPath), "UTF-8");
            JsonObject json = new Gson().fromJson(reader, JsonObject.class);
            this.fieldsConfig = json.getAsJsonArray("fieldsOrder");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar layout SIMP: " + jsonPath, e);
        }
    }

    public String gerarLinhaTxt(Map<String, Object> dados) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < fieldsConfig.size(); i++) {
            JsonObject campo = fieldsConfig.get(i).getAsJsonObject();
            
            String nomeCampo = campo.get("field").getAsString(); // Nome no JSON
            int tamanho = campo.get("length").getAsInt();
            String tipo = campo.has("type") ? campo.get("type").getAsString() : "X";
            String alinhamento = campo.has("align") ? campo.get("align").getAsString() : "L";
            String padChar = campo.has("pad") ? campo.get("pad").getAsString() : " ";
            int scale = campo.has("scale") ? campo.get("scale").getAsInt() : 0;

            Object valorObj = dados.get(nomeCampo); // Busca do Map vindo do banco
            String valorFormatado = formatarValor(valorObj, tamanho, tipo, scale);
            
            sb.append(aplicarPadding(valorFormatado, tamanho, alinhamento, padChar));
        }
        return sb.toString();
    }

    private String formatarValor(Object valor, int tamanho, String tipo, int scale) {
        if (valor == null) return "";
        
        if ("N".equals(tipo) && valor instanceof BigDecimal) {
            BigDecimal v = (BigDecimal) valor;
            // Remove ponto decimal para formato numérico fixo (ex: 10.50 -> 1050)
            return v.movePointRight(scale).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
        }
        return valor.toString();
    }

    private String aplicarPadding(String valor, int tamanho, String align, String padChar) {
        if (valor.length() > tamanho) return valor.substring(0, tamanho); // Corta se for maior
        
        StringBuilder sb = new StringBuilder(valor);
        while (sb.length() < tamanho) {
            if ("R".equals(align)) {
                sb.insert(0, padChar); // Alinha a Direita (enche a esquerda)
            } else {
                sb.append(padChar);    // Alinha a Esquerda (enche a direita)
            }
        }
        return sb.toString();
    }
}