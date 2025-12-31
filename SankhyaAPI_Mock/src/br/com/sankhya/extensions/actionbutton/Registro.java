package br.com.sankhya.extensions.actionbutton;

public interface Registro {
    // Apenas a assinatura do método, sem corpo
    public Object getCampo(String nome) throws Exception;
    
    // Adicionei este também, pois é comum ser usado
    public void setCampo(String nome, Object valor) throws Exception;
}