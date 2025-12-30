/*     */ package processamento;
/*     */ 
/*     */ import br.com.sankhya.extensions.actionbutton.ContextoAcao;
/*     */ import br.com.sankhya.extensions.actionbutton.Registro;
/*     */ import br.com.sankhya.jape.EntityFacade;
/*     */ import br.com.sankhya.jape.dao.JdbcWrapper;
/*     */ import br.com.sankhya.jape.util.JapeSessionContext;
/*     */ import br.com.sankhya.jape.vo.DynamicVO;
/*     */ import br.com.sankhya.jape.vo.EntityVO;
/*     */ import br.com.sankhya.modelcore.util.EntityFacadeFactory;
/*     */ import buscar.buscarEstoque;
/*     */ import com.sankhya.util.TimeUtils;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.sql.ResultSet;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class processarExportarArquivo
/*     */ {
/*     */   public static void processar(ContextoAcao arg0) throws Exception {
/*  31 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/*  32 */     JdbcWrapper jdbcWrapper = dwf.getJdbcWrapper();
/*  33 */     jdbcWrapper.openSession();
/*     */     
/*  35 */     Registro[] registros = arg0.getLinhas();
/*  36 */     if (registros.length > 1)
/*  37 */       arg0.mostraErro("S� pode ser exportado um arquivo por vez!");  byte b;
/*     */     int i;
/*     */     Registro[] arrayOfRegistro1;
/*  40 */     for (i = (arrayOfRegistro1 = registros).length, b = 0; b < i; ) { Registro registro = arrayOfRegistro1[b];
/*  41 */       BigDecimal codSimulaSimp = new BigDecimal((String)registro.getCampo("CODSIMSIM"));
/*  42 */       Object object1 = registro.getCampo("MES_REFERENCIA");
/*  43 */       BigDecimal anoReferente = new BigDecimal((String)registro.getCampo("ANO_REFRENCIA"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  52 */       Object object2 = registro.getCampo("STATUS");
/*  53 */       if (object2.contains("2") || object2.contains("3")) {
/*  54 */         arg0.mostraErro("S� pode ser exportados simula��es com status \"Simula��o\"");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  62 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*     */ 
/*     */       
/*  65 */       int j = 0;
/*  66 */       ResultSet query = buscarEstoque.gerarArquivo(jdbcWrapper, codSimulaSimp);
/*  67 */       while (query.next()) {
/*  68 */         String contador = query.getString("ROWNUM");
/*  69 */         String codEmpAnp = query.getString("CODEMP_ANP");
/*  70 */         String referenciaArq = query.getString("MESREFERENCIA");
/*  71 */         String codOperAnp = query.getString("CODOPERACAO");
/*  72 */         String codInstalacao1 = query.getString("CODINSTALACAO1");
/*  73 */         String codInstalacao2 = query.getString("CODINSTALACAO2");
/*  74 */         String codProdAnp = query.getString("CODPROD_ANP");
/*  75 */         String qtdUnPadrao = query.getString("QTD_PROD_UN_ANP");
/*  76 */         String qtdKg = query.getString("QTD_PROD_OPERADO_KG");
/*  77 */         String codModal = query.getString("COD_MODAL_MOVIMENTACAO");
/*  78 */         String codOleoduto = query.getString("COD_OLEODUTO_TRANSPORTE");
/*  79 */         String cgc_cpf = query.getString("CNPJ_PARC");
/*  80 */         String codCidAnp = query.getString("CODCID_ANP");
/*  81 */         String codCnaeAnpParc = query.getString("COD_ATIV_PARC_ANP");
/*  82 */         String codPaisAnp = query.getString("CODPAIS_ANP");
/*  83 */         String licencaImportacao = query.getString("NUM_LICEN_IMPORT");
/*  84 */         String declaracaoImportacao = query.getString("NUM_DECLARACAO_IMPORT");
/*  85 */         String nroNFOper = query.getString("NUMNF");
/*  86 */         String codSerieNfAnp = query.getString("SERIE_NF");
/*  87 */         String datOperComercial = query.getString("DAT_OPER_COMERCIAL");
/*  88 */         String codTipTarifaServ = query.getString("COD_TIP_TARIFA_SERVICO");
/*  89 */         String caracteristicaInativo = query.getString("CARACTERISTICA");
/*  90 */         String metodoInativo = query.getString("METODO");
/*  91 */         String modalidadeFrete = query.getString("MODALIDADE_FRETE");
/*  92 */         String nroDocQualidade = query.getString("NUM_DOC_QUALIDADE");
/*  93 */         String codProdResultante = query.getString("COD_OPER_RESULTANTE");
/*  94 */         String vlrUnit = query.getString("VLRUNIT_NF");
/*  95 */         String recepienteGlp = query.getString("RECEPIENTE_GLP");
/*  96 */         String chaveNfeCte = query.getString("CAHVE_NFE_CTE");
/*     */         
/*  98 */         if (j == 0) {
/*  99 */           BigDecimal totalRegistros = query.getBigDecimal("CONTADOR").add(BigDecimal.ONE);
/* 100 */           String str = String.valueOf(geraLinha("0", 10)) + geraLinha(codEmpAnp, 10) + geraLinha(referenciaArq, 6) + geraLinha((String)totalRegistros, 7);
/*     */           
/* 102 */           baos.write(str.getBytes());
/*     */         } 
/*     */         
/* 105 */         String linha = escrever(contador, codEmpAnp, referenciaArq, codOperAnp, codInstalacao1, codInstalacao2, codProdAnp, qtdUnPadrao, 
/* 106 */             qtdKg, codModal, codOleoduto, cgc_cpf, codCidAnp, codCnaeAnpParc, codPaisAnp, licencaImportacao, 
/* 107 */             declaracaoImportacao, nroNFOper, codSerieNfAnp, datOperComercial, codTipTarifaServ, 
/* 108 */             caracteristicaInativo, metodoInativo, modalidadeFrete, nroDocQualidade, codProdResultante, 
/* 109 */             vlrUnit, recepienteGlp, chaveNfeCte);
/* 110 */         baos.write("\r\n".getBytes());
/* 111 */         baos.write(linha.getBytes());
/* 112 */         j++;
/*     */       } 
/*     */       
/* 115 */       byte[] arq = baos.toByteArray();
/* 116 */       Date date = new Date();
/* 117 */       SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
/* 118 */       String formattedDate = formatter.format(date);
/*     */       
/* 120 */       String path = arg0.getParametroSistema("DIRBASESIMP") + "simp-" + formattedDate + ".txt";
/* 121 */       FileOutputStream fileOutputStream = new FileOutputStream(path);
/* 122 */       fileOutputStream.write(arq);
/* 123 */       fileOutputStream.close();
/*     */       
/* 125 */       arq = adicionarPlaceHolderArquivo(arq, "SIMP-" + formattedDate + ".txt");
/*     */ 
/*     */       
/* 128 */       BigDecimal codEst = saveAD_DETSIMULARSIMP(dwf, codSimulaSimp, arq, (String)object1, anoReferente);
/*     */       
/* 130 */       registro.setCampo("STATUS", "1");
/* 131 */       registro.save();
/* 132 */       arg0.setMensagemRetorno("Estoque SIMP gerado com Sucesso!<br> C�digo do estoque gerado:" + mensagemRetorno(codEst));
/*     */       b++; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escrever(String contador, String codEmpAnp, String referenciaArq, String codOperAnp, String codInstalacao1, String codInstalacao2, String codProdAnp, String qtdUnPadrao, String qtdKg, String codModal, String codOleoduto, String cgc_cpf, String codCidAnp, String codCnaeAnpParc, String codPaisAnp, String licencaImportacao, String declaracaoImportacao, String nroNFOper, String codSerieNfAnp, String datOperComercial, String codTipTarifaServ, String caracteristicaInativo, String metodoInativo, String modalidadeFrete, String nroDocQualidade, String codProdResultante, String vlrUnit, String recepienteGlp, String chaveNfeCte) {
/* 146 */     String vlrunitinteiro = "0";
/* 147 */     String vlrunitdecimal = "0";
/* 148 */     if (vlrUnit.equalsIgnoreCase("0.0")) {
/* 149 */       vlrunitinteiro = "0";
/*     */     } else {
/*     */       
/* 152 */       String[] valores = vlrUnit.split("\\.");
/* 153 */       if (valores.length > 1) {
/* 154 */         vlrunitinteiro = valores[0];
/* 155 */         vlrunitdecimal = valores[1];
/*     */       } else {
/*     */         
/* 158 */         String[] valores1 = vlrUnit.split("\\,");
/* 159 */         if (valores1.length > 1) {
/* 160 */           vlrunitinteiro = valores1[0];
/* 161 */           vlrunitdecimal = valores1[1];
/*     */         } else {
/* 163 */           vlrunitinteiro = vlrUnit;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 168 */     vlrunitinteiro = vlrunitinteiro.replace(".", "");
/* 169 */     vlrunitdecimal = vlrunitdecimal.replace(".", "");
/* 170 */     vlrunitinteiro = vlrunitinteiro.replace(",", "");
/* 171 */     vlrunitdecimal = vlrunitdecimal.replace(",", "");
/*     */     
/* 173 */     vlrunitinteiro = limitaString(StringUtils.leftPad(vlrunitinteiro, 3, "0"), 3);
/* 174 */     vlrunitdecimal = limitaString(StringUtils.rightPad(vlrunitdecimal, 4, "0"), 4);
/*     */ 
/*     */     
/* 177 */     StringBuilder linha = new StringBuilder();
/* 178 */     linha.append(geraLinha(contador, 10));
/* 179 */     linha.append(geraLinha(codEmpAnp, 10));
/* 180 */     linha.append(geraLinha(referenciaArq, 6));
/* 181 */     linha.append(geraLinha(codOperAnp, 7));
/* 182 */     linha.append(geraLinha(codInstalacao1, 7));
/* 183 */     linha.append(geraLinha(codInstalacao2, 7));
/* 184 */     linha.append(geraLinha(codProdAnp, 9));
/* 185 */     linha.append(geraLinha(qtdUnPadrao, 15));
/* 186 */     linha.append(geraLinha(qtdKg, 15));
/* 187 */     linha.append(geraLinha(codModal, 1));
/* 188 */     linha.append(geraLinha(codOleoduto, 7));
/* 189 */     linha.append(geraLinha(cgc_cpf, 14));
/* 190 */     linha.append(geraLinha(codCidAnp, 7));
/* 191 */     linha.append(geraLinha(codCnaeAnpParc, 5));
/* 192 */     linha.append(geraLinha(codPaisAnp, 4));
/* 193 */     linha.append(geraLinha(licencaImportacao, 10));
/* 194 */     linha.append(geraLinha(declaracaoImportacao, 10));
/* 195 */     linha.append(geraLinha(nroNFOper, 7));
/* 196 */     linha.append(geraLinha(codSerieNfAnp, 2));
/* 197 */     linha.append(geraLinha(datOperComercial, 8));
/* 198 */     linha.append(geraLinha(codTipTarifaServ, 1));
/* 199 */     linha.append(geraLinha(caracteristicaInativo, 3));
/* 200 */     linha.append(geraLinha(metodoInativo, 3));
/* 201 */     linha.append(geraLinha(modalidadeFrete, 2));
/* 202 */     linha.append(geraLinha(nroDocQualidade, 10));
/* 203 */     linha.append(geraLinha(codProdResultante, 9));
/* 204 */     linha.append(geraLinha(String.valueOf(vlrunitinteiro) + vlrunitdecimal, 7));
/* 205 */     linha.append(geraLinha(recepienteGlp, 2));
/* 206 */     linha.append(geraLinha(chaveNfeCte, 44));
/* 207 */     return linha.toString();
/*     */   }
/*     */   
/*     */   public static BigDecimal saveAD_DETSIMULARSIMP(EntityFacade dwf, BigDecimal codSimulaSimp, byte[] arq, String mesReferente, BigDecimal anoReferente) throws Exception {
/* 211 */     DynamicVO simp = (DynamicVO)dwf.getDefaultValueObjectInstance("AD_ESTOQUESIMP");
/* 212 */     simp.setProperty("CODSIMSIM", codSimulaSimp);
/* 213 */     simp.setProperty("ARQUIVO", arq);
/* 214 */     simp.setProperty("CODUSUINC", JapeSessionContext.getRequiredProperty("usuario_logado"));
/* 215 */     simp.setProperty("DATINC", TimeUtils.getNow());
/* 216 */     simp.setProperty("STATUS", "0");
/* 217 */     simp.setProperty("MES_REFERENCIA", mesReferente);
/* 218 */     simp.setProperty("ANO_REFERENTE", anoReferente);
/* 219 */     dwf.createEntity("AD_ESTOQUESIMP", (EntityVO)simp);
/*     */     
/* 221 */     return simp.asBigDecimal("CODESTSIMP");
/*     */   }
/*     */ 
/*     */   
/*     */   public static String geraLinha(String texto, int size) {
/* 226 */     StringBuilder linha = new StringBuilder("");
/* 227 */     return limitaString(StringUtils.leftPad(texto, size, "0"), size);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String limitaString(String texto, int maximo) {
/* 232 */     if (texto.length() <= maximo) {
/* 233 */       return texto;
/*     */     }
/* 235 */     return texto.substring(0, maximo);
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] adicionarPlaceHolderArquivo(byte[] arquivo, String caminho) throws IOException {
/* 240 */     Date date = new Date();
/* 241 */     SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
/* 242 */     String formattedDate = formatter.format(date);
/*     */     
/* 244 */     String nomeArquivo = caminho.split("/")[(caminho.split("/")).length - 1];
/*     */     
/* 246 */     byte[] textField = ("__start_fileinformation__{\"name\":\"" + nomeArquivo + "\",\"size\":" + arquivo.length + 
/* 247 */       ",\"type\":\"application/pdf\"}__end_fileinformation__").getBytes();
/* 248 */     ByteArrayOutputStream arqByte = new ByteArrayOutputStream();
/* 249 */     BufferedOutputStream bf = new BufferedOutputStream(arqByte);
/*     */     
/* 251 */     bf.write(textField);
/* 252 */     bf.write(arquivo);
/* 253 */     bf.flush();
/* 254 */     bf.close();
/* 255 */     arqByte.toByteArray();
/* 256 */     byte[] arquivoComPlaceHolder = arqByte.toByteArray();
/* 257 */     return arquivoComPlaceHolder;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String mensagemRetorno(BigDecimal codEst) {
/* 262 */     String id = "br.com.sankhya.menu.adicional.AD_ESTOQUESIMP";
/* 263 */     String mensagemSucesso = "";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 268 */     String pk = "{\"CODESTSIMP\"=\"" + codEst + "\"}";
/*     */     
/* 270 */     String caminho = "/mge/system.jsp#app/";
/* 271 */     String idBase64 = DatatypeConverter.printBase64Binary(id.getBytes());
/* 272 */     String paransBase64 = DatatypeConverter.printBase64Binary(pk.toString().replaceAll("=", ":").getBytes());
/* 273 */     String icone = "<p align=\"rigth\"><a href=\"" + caminho + idBase64 + "/" + paransBase64 + 
/* 274 */       "\" target=\"_top\" >" + 
/* 275 */       "<img src=\"http://imageshack.com/a/img923/7316/ux573F.png\" ><font size=\"20\" color=\"#008B45\"><b>" + 
/* 276 */       codEst + "</b></font></a></p>";
/*     */     
/* 278 */     String mensagemRetorno = "<p align=\"left\">" + mensagemSucesso + "</p>" + icone;
/*     */     
/* 280 */     return mensagemRetorno;
/*     */   }
/*     */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\processamento\processarExportarArquivo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */