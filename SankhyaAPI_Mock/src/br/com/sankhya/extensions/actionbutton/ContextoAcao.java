package br.com.sankhya.extensions.actionbutton;

public interface ContextoAcao {
    // Agora são apenas assinaturas de métodos (sem chaves {} e sem return)
    public Registro[] getLinhas();
    
    public void setMensagemRetorno(String msg);
    
    public void mostraErro(String msg) throws Exception;
    
    public Object getParam(String nome);
    
    public void setArquivoRetorno(String nome, String tipo, byte[] conteudo);
}
