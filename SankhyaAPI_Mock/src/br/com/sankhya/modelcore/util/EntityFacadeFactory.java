package br.com.sankhya.modelcore.util;
import br.com.sankhya.jape.EntityFacade;

public class EntityFacadeFactory {
    
    // Método usado nas versões mais novas do Sankhya
    public static EntityFacade getDWFacade() { return null; }
    
    // Método usado nas versões antigas (Legacy) - É este que corrige o erro dos arquivos antigos
    public static EntityFacade getDWFFacade() { return null; }
}