package br.com.sankhya.jape;

import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.vo.EntityVO;
import br.com.sankhya.jape.vo.DynamicVO;

public interface EntityFacade {
    public JdbcWrapper getJdbcWrapper() throws Exception;
    
    // Outros métodos comuns que podem ser úteis no futuro
    public void createEntity(String entityName, EntityVO vo) throws Exception;
    
    public DynamicVO findByPK(String entityName, Object pk) throws Exception;
}