package br.com.evertech.simp.action;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.evertech.simp.service.SimpService;
import br.com.evertech.simp.util.ConexaoUtil; // <--- Importante!
import java.math.BigDecimal;

public class GeraSimpAction implements AcaoRotinaJava {

    @Override
    public void doAction(ContextoAcao ctx) throws Exception {
        // Pega o código da linha selecionada (Ajuste conforme sua grade)
        Registro[] linhas = ctx.getLinhas();
        if (linhas == null || linhas.length == 0) {
            ctx.mostraErro("Selecione uma linha na grade.");
            return;
        }
        
        Registro linha = linhas[0];
        // O campo pode vir como String, BigDecimal ou Integer dependendo do banco. 
        // Vamos garantir a conversão segura:
        Object codObj = linha.getCampo("CODSIMSIM");
        BigDecimal codSimp = new BigDecimal(codObj.toString());
        
        JdbcWrapper jdbc = null;
        try {
            // USANDO A NOVA CLASSE UTILITÁRIA BLINDADA
            jdbc = ConexaoUtil.getJdbcWrapper(); 
            
            jdbc.openSession();
            
            SimpService service = new SimpService();
            byte[] arquivo = service.gerarArquivoTxt(jdbc, codSimp);
            
            // Retorna o arquivo (versão compatível com download)
            // Se ctx.setArquivoRetorno der erro de compilação, use a lógica antiga de salvar em tabela
            try {
                 ctx.setArquivoRetorno("SIMP_" + codSimp + ".txt", "text/plain", arquivo);
                 ctx.setMensagemRetorno("Arquivo gerado com sucesso! (" + arquivo.length + " bytes)");
            } catch (Throwable t) {
                 // Fallback se o servidor for muito antigo e não tiver setArquivoRetorno
                 ctx.setMensagemRetorno("Arquivo gerado! (Funcionalidade de download não suportada nesta versão do Sankhya). Bytes: " + arquivo.length);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            // Mostra o erro real na tela
            String msgErro = e.getMessage();
            if (e.getCause() != null) msgErro += " | " + e.getCause().getMessage();
            ctx.mostraErro("Erro ao gerar SIMP: " + msgErro);
            
        } finally {
            if(jdbc != null) jdbc.closeSession();
        }
    }
}