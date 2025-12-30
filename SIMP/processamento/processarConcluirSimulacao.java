/*     */ package processamento;
/*     */ 
/*     */ import br.com.sankhya.extensions.actionbutton.ContextoAcao;
/*     */ import br.com.sankhya.extensions.actionbutton.Registro;
/*     */ import br.com.sankhya.jape.EntityFacade;
/*     */ import br.com.sankhya.jape.dao.JdbcWrapper;
/*     */ import br.com.sankhya.jape.vo.DynamicVO;
/*     */ import br.com.sankhya.jape.vo.EntityVO;
/*     */ import br.com.sankhya.modelcore.util.EntityFacadeFactory;
/*     */ import buscar.buscarEstoque;
/*     */ import java.math.BigDecimal;
/*     */ import java.sql.ResultSet;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class processarConcluirSimulacao
/*     */ {
/*     */   public static void processar(ContextoAcao arg0) throws Exception {
/*  24 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/*  25 */     JdbcWrapper jdbcWrapper = dwf.getJdbcWrapper();
/*  26 */     jdbcWrapper.openSession();
/*     */     
/*  28 */     Registro[] registros = arg0.getLinhas();
/*  29 */     if (registros.length > 1)
/*  30 */       arg0.mostraErro("S� pode ser conclu�da uma simula��o por vez");  byte b;
/*     */     int i;
/*     */     Registro[] arrayOfRegistro1;
/*  33 */     for (i = (arrayOfRegistro1 = registros).length, b = 0; b < i; ) { Registro registro = arrayOfRegistro1[b];
/*  34 */       Object object = registro.getCampo("STATUS");
/*  35 */       if (!object.equalsIgnoreCase("0")) {
/*  36 */         arg0.mostraErro("S� pode ser cuncluido simula��es com status \"Processado\"");
/*     */       }
/*  38 */       BigDecimal codSimSimp = new BigDecimal((String)registro.getCampo("CODSIMSIM"));
/*     */       
/*  40 */       BigDecimal importEstCab = BigDecimal.ZERO;
/*  41 */       ResultSet query = buscarEstoque.buscarEstoqueFinal(jdbcWrapper, codSimSimp);
/*  42 */       while (query.next()) {
/*     */         
/*  44 */         Date date = new Date();
/*  45 */         SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
/*  46 */         String descricao = "ESTOQUE SIMP " + formatter.format(date);
/*  47 */         String mes = query.getString("MES_REFERENCIA");
/*  48 */         BigDecimal ano = query.getBigDecimal("ANO_REFRENCIA");
/*     */         
/*  50 */         if (importEstCab.intValue() == 0) {
/*  51 */           importEstCab = saveImportEstCab(dwf, descricao, mes, ano, codSimSimp);
/*     */         }
/*     */         
/*  54 */         String codAnp = query.getString("CODPROD_ANP");
/*  55 */         String descricaoProd = query.getString("DESCR_PROD");
/*  56 */         BigDecimal qtd = query.getBigDecimal("QTD_PROD_UN_ANP");
/*  57 */         BigDecimal codEmp = query.getBigDecimal("CODEMP");
/*  58 */         BigDecimal codOper = query.getBigDecimal("OPER_INI");
/*  59 */         saveImportEstDet(dwf, importEstCab, codAnp, descricaoProd, qtd, codEmp, codOper);
/*     */       } 
/*     */       
/*  62 */       registro.setCampo("STATUS", "1");
/*  63 */       registro.save();
/*  64 */       arg0.setMensagemRetorno("Estoque atualizado com sucesso!" + mensagemRetorno(importEstCab));
/*     */       b++; }
/*     */   
/*     */   }
/*     */   
/*     */   public static BigDecimal saveImportEstCab(EntityFacade dwf, String descricao, String mes, BigDecimal ano, BigDecimal codSimulacao) throws Exception {
/*  70 */     DynamicVO simp = (DynamicVO)dwf.getDefaultValueObjectInstance("AD_IMPORTADORESTOQUESIMPCAB");
/*  71 */     simp.setProperty("DESCRIMP", descricao);
/*  72 */     simp.setProperty("MES", mes);
/*  73 */     simp.setProperty("ANO", ano);
/*  74 */     simp.setProperty("CODSIMSIM", codSimulacao);
/*  75 */     dwf.createEntity("AD_IMPORTADORESTOQUESIMPCAB", (EntityVO)simp);
/*     */     
/*  77 */     return simp.asBigDecimalOrZero("CODIMPDOCCAB");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void saveImportEstDet(EntityFacade dwf, BigDecimal codimpestcab, String codAnp, String descricao, BigDecimal qtd, BigDecimal codEmp, BigDecimal codOper) throws Exception {
/*  83 */     DynamicVO simp = (DynamicVO)dwf.getDefaultValueObjectInstance("AD_IMPORTADORESTOQUESIMPDET");
/*  84 */     simp.setProperty("CODIMPDOCCAB", codimpestcab);
/*  85 */     simp.setProperty("CODANP", codAnp);
/*  86 */     simp.setProperty("DESCRITEM", descricao);
/*  87 */     simp.setProperty("QTDE", qtd);
/*  88 */     simp.setProperty("CODEMP", codEmp);
/*  89 */     simp.setProperty("CODOPER", codOper);
/*  90 */     dwf.createEntity("AD_IMPORTADORESTOQUESIMPDET", (EntityVO)simp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String mensagemRetorno(BigDecimal codEst) {
/*  96 */     String id = "br.com.sankhya.menu.adicional.AD_IMPORTADORESTOQUESIMPCAB";
/*  97 */     String mensagemSucesso = "";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     String pk = "{\"CODIMPDOCCAB\"=\"" + codEst + "\"}";
/*     */     
/* 104 */     String caminho = "/mge/system.jsp#app/";
/* 105 */     String idBase64 = DatatypeConverter.printBase64Binary(id.getBytes());
/* 106 */     String paransBase64 = DatatypeConverter.printBase64Binary(pk.toString().replaceAll("=", ":").getBytes());
/* 107 */     String icone = "<p align=\"rigth\"><a href=\"" + caminho + idBase64 + "/" + paransBase64 + 
/* 108 */       "\" target=\"_top\" >" + 
/* 109 */       "<img src=\"http://imageshack.com/a/img923/7316/ux573F.png\" ><font size=\"20\" color=\"#008B45\"><b>" + 
/* 110 */       codEst + "</b></font></a></p>";
/*     */     
/* 112 */     String mensagemRetorno = "<p align=\"left\">" + mensagemSucesso + "</p>" + icone;
/*     */     
/* 114 */     return mensagemRetorno;
/*     */   }
/*     */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\processamento\processarConcluirSimulacao.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */