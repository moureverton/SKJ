package br.com.sankhya.jape.vo;

public interface EntityVO {
    // Métodos essenciais para manipulação de valores
    public Object getProperty(String nome) throws Exception;
    
    public void setProperty(String nome, Object valor) throws Exception;
    
    // Recupera o ID (Primary Key) do registro
    public Object getID() throws Exception;
}