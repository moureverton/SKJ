package br.com.evertech.simp.util;

import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;
import java.lang.reflect.Method;

public class ConexaoUtil {

    public static JdbcWrapper getJdbcWrapper() throws Exception {
        Object facade = null;
        try {
            // Tenta o método moderno (1 'F')
            facade = EntityFacadeFactory.getDWFacade();
        } catch (Throwable e) {
            try {
                // Se falhar, tenta o método antigo via Reflexão (2 'F's)
                // Isso engana o compilador e funciona no servidor antigo
                Method metodoAntigo = EntityFacadeFactory.class.getMethod("getDWFFacade");
                facade = metodoAntigo.invoke(null);
            } catch (Exception ex) {
                throw new Exception("Não foi possível conectar ao banco de dados Sankhya. Versão incompatível.", ex);
            }
        }

        // Agora pega o JDBC Wrapper do facade encontrado
        if (facade == null) {
            throw new Exception("Falha fatal: EntityFacade retornou nulo.");
        }
        
        // Usa reflexão de novo para chamar o getJdbcWrapper, garantindo compatibilidade total
        Method getJdbc = facade.getClass().getMethod("getJdbcWrapper");
        return (JdbcWrapper) getJdbc.invoke(facade);
    }
}