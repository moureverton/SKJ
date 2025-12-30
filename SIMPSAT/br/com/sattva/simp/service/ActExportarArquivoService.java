/*      */ package br.com.sattva.simp.service;
/*      */ 
/*      */ import br.com.sankhya.extensions.actionbutton.ContextoAcao;
/*      */ import br.com.sankhya.extensions.actionbutton.Registro;
/*      */ import br.com.sankhya.jape.EntityFacade;
/*      */ import br.com.sankhya.jape.dao.JdbcWrapper;
/*      */ import br.com.sankhya.jape.util.JapeSessionContext;
/*      */ import br.com.sankhya.jape.vo.DynamicVO;
/*      */ import br.com.sankhya.jape.vo.EntityVO;
/*      */ import br.com.sankhya.modelcore.util.EntityFacadeFactory;
/*      */ import com.sankhya.util.TimeUtils;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.RoundingMode;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Date;
/*      */ import java.util.List;
/*      */ import java.util.zip.ZipEntry;
/*      */ import java.util.zip.ZipOutputStream;
/*      */ import javax.xml.bind.DatatypeConverter;
/*      */ import org.apache.commons.lang3.StringUtils;
/*      */ import org.apache.poi.ss.usermodel.CellStyle;
/*      */ import org.apache.poi.xssf.usermodel.XSSFCell;
/*      */ import org.apache.poi.xssf.usermodel.XSSFCellStyle;
/*      */ import org.apache.poi.xssf.usermodel.XSSFDataFormat;
/*      */ import org.apache.poi.xssf.usermodel.XSSFRow;
/*      */ import org.apache.poi.xssf.usermodel.XSSFSheet;
/*      */ import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ActExportarArquivoService
/*      */ {
/*      */   private static final String TABELA_DESTINO = "AD_ESTOQUESIMP";
/*      */   private static final String CAMPO_BIN_TXT = "ARQUIVO";
/*      */   private static final String CAMPO_BIN_XLS = "ARQUIVOXLS";
/*      */   private static final String CAMPO_BIN_ZIP = "ARQUIVOZIP";
/*      */   private static final String CAMPO_PK = "CODESTSIMP";
/*      */   private static final String ENCODING_TXT = "ISO-8859-1";
/*      */   private static final String MIME_TXT = "text/plain";
/*      */   private static final String MIME_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
/*      */   private static final String MIME_ZIP = "application/zip";
/*   62 */   private static final List<String> CAB_DADOS_BASICOS = Arrays.asList(new String[] {
/*   63 */         "IDENTIFICADOR_EXTERNO", 
/*   64 */         "DAT_REFERENCIA", 
/*   65 */         "NOME_CONTATO", 
/*   66 */         "TELEFONE_CONTATO", 
/*   67 */         "EMAIL_CONTATO"
/*      */       });
/*      */ 
/*      */   
/*   71 */   private static final List<String> CAB_MOV_IMPORTADOR = Arrays.asList(new String[] { 
/*   72 */         "CONT_SEQUENCIAL", 
/*   73 */         "COD_OPERACAO", 
/*   74 */         "COD_INSTALACAO1", 
/*   75 */         "COD_INSTALACAO2", 
/*   76 */         "COD_PRODUTO", 
/*   77 */         "QTD_PRODUTO", 
/*   78 */         "QTD_PRODUTO_KG", 
/*   79 */         "COD_MODAL", 
/*   80 */         "COD_VEICULO", 
/*   81 */         "COD_TERCEIRO", 
/*   82 */         "COD_LOCALIDADE", 
/*   83 */         "COD_ATIVIDADE_ECONOMICA", 
/*   84 */         "COD_PAIS", 
/*   85 */         "NUM_LI", 
/*   86 */         "NUM_DI", 
/*   87 */         "NUM_NOTA_FISCAL", 
/*   88 */         "COD_SERIE_NF", 
/*   89 */         "DAT_NOTA_FISCAL", 
/*   90 */         "COD_TIPO_SERVICO", 
/*   91 */         "COD_CARACTERISTICA", 
/*   92 */         "COD_METODO", 
/*   93 */         "MODALIDADE_FRETE", 
/*   94 */         "VAL_CARACTERISTICA", 
/*   95 */         "COD_PROD_OPER_RESULT", 
/*   96 */         "VAL_UNITARIO", 
/*   97 */         "COD_EMBALAGEM_GLP", 
/*   98 */         "COD_NOTA_FISCAL_ELTRA" });
/*      */ 
/*      */ 
/*      */   
/*  102 */   private static final List<String> COLS_RS_ORIG = Arrays.asList(new String[] { 
/*  103 */         "SEQUENCIA", 
/*  104 */         "CODEMP_ANP", 
/*  105 */         "MESREFERENCIA", 
/*  106 */         "CODOPERACAO", 
/*  107 */         "CODINSTALACAO1", 
/*  108 */         "CODINSTALACAO2", 
/*  109 */         "CODPROD_ANP", 
/*  110 */         "QTD_PROD_UN_ANP", 
/*  111 */         "QTD_PROD_OPERADO_KG", 
/*  112 */         "COD_MODAL_MOVIMENTACAO", 
/*  113 */         "COD_OLEODUTO_TRANSPORTE", 
/*  114 */         "CNPJ_PARC", 
/*  115 */         "CODCID_ANP", 
/*  116 */         "COD_ATIV_PARC_ANP", 
/*  117 */         "CODPAIS_ANP", 
/*  118 */         "NUM_LICEN_IMPORT", 
/*  119 */         "NUM_DECLARACAO_IMPORT", 
/*  120 */         "NUMNF", 
/*  121 */         "SERIE_NF", 
/*  122 */         "DAT_OPER_COMERCIAL", 
/*  123 */         "COD_TIP_TARIFA_SERVICO", 
/*  124 */         "CARACTERISTICA", 
/*  125 */         "METODO", 
/*  126 */         "MODALIDADE_FRETE", 
/*  127 */         "NUM_DOC_QUALIDADE", 
/*  128 */         "COD_OPER_RESULTANTE", 
/*  129 */         "VLRUNIT_NF", 
/*  130 */         "RECEPIENTE_GLP", 
/*  131 */         "CHAVE_NFE_CTE" });
/*      */   
/*      */   public static void processar(ContextoAcao ctx) throws Exception {
/*      */     byte[] txtBytes, xlsxBytes;
/*  135 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/*  136 */     JdbcWrapper jdbc = dwf.getJdbcWrapper();
/*  137 */     jdbc.openSession();
/*      */     
/*  139 */     Registro[] linhas = ctx.getLinhas();
/*  140 */     if (linhas == null || linhas.length != 1) {
/*  141 */       ctx.mostraErro("Selecione exatamente um registro para exportar.");
/*      */       
/*      */       return;
/*      */     } 
/*  145 */     Registro registro = linhas[0];
/*      */     
/*  147 */     BigDecimal codSimulaSimp = toBigDecimal(registro.getCampo("CODSIMSIM"));
/*  148 */     String mesReferente = toStringSafe(registro.getCampo("MES_REFERENCIA"));
/*  149 */     BigDecimal anoReferente = toBigDecimal(registro.getCampo("ANO_REFRENCIA"));
/*  150 */     String statusAtual = toStringSafe(registro.getCampo("STATUS"));
/*      */     
/*  152 */     validarStatus(ctx, statusAtual);
/*      */ 
/*      */     
/*  155 */     String yyyyRef = (anoReferente == null) ? "0000" : String.format("%04d", new Object[] { Integer.valueOf(anoReferente.intValue()) });
/*  156 */     String mmRef = pad2(mesReferente);
/*  157 */     String yyyymmRef = String.valueOf(yyyyRef) + mmRef;
/*      */ 
/*      */     
/*  160 */     String datReferencia = String.valueOf(mmRef) + yyyyRef;
/*      */     
/*  162 */     Date now = new Date();
/*  163 */     String yyyyNow = (new SimpleDateFormat("yyyy")).format(now);
/*  164 */     String mmNow = (new SimpleDateFormat("MM")).format(now);
/*  165 */     String ddNow = (new SimpleDateFormat("dd")).format(now);
/*  166 */     String yyyymmNow = String.valueOf(yyyyNow) + mmNow;
/*      */ 
/*      */     
/*  169 */     String nomeTxt = "5777772400_" + yyyymmRef + ".txt";
/*  170 */     String nomeXlsx = "110_57777724_" + yyyymmNow + ddNow + "000000.xlsx";
/*  171 */     String nomeZip = "110_0057777724_" + yyyymmRef + "_" + yyyymmNow + ddNow + "000X.zip";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  176 */     ResultSet rs = null;
/*      */     try {
/*  178 */       rs = gerarArquivo(jdbc, codSimulaSimp);
/*  179 */       txtBytes = gerarTxt(rs);
/*      */       
/*  181 */       closeQuietly(rs);
/*  182 */       rs = gerarArquivo(jdbc, codSimulaSimp);
/*  183 */       xlsxBytes = gerarXlsx(rs, datReferencia);
/*      */     } finally {
/*  185 */       closeQuietly(rs);
/*      */     } 
/*      */ 
/*      */     
/*  189 */     byte[] zipBytes = zipSingleFile(nomeTxt, txtBytes);
/*      */ 
/*      */     
/*  192 */     salvarDiscoSeConfigurado(ctx, txtBytes, nomeTxt);
/*      */ 
/*      */     
/*  195 */     byte[] txtComPH = adicionarPlaceHolderArquivo(txtBytes, nomeTxt, "text/plain");
/*  196 */     byte[] xlsxComPH = adicionarPlaceHolderArquivo(xlsxBytes, nomeXlsx, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
/*  197 */     byte[] zipComPH = adicionarPlaceHolderArquivo(zipBytes, nomeZip, "application/zip");
/*      */ 
/*      */     
/*  200 */     BigDecimal codEst = saveAD_DETSIMULARSIMP(dwf, codSimulaSimp, mesReferente, anoReferente, txtComPH, xlsxComPH, zipComPH);
/*      */ 
/*      */     
/*  203 */     registro.setCampo("STATUS", "1");
/*  204 */     registro.save();
/*      */     
/*  206 */     ctx.setMensagemRetorno(mensagemRetorno(codEst));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void validarStatus(ContextoAcao ctx, String statusAtual) throws Exception {
/*  212 */     if (statusAtual != null && (statusAtual.contains("2") || statusAtual.contains("3"))) {
/*  213 */       ctx.mostraErro("Só pode ser exportado quando o status for \"Simulação\".");
/*      */     }
/*      */   }
/*      */   
/*      */   private static BigDecimal toBigDecimal(Object o) {
/*  218 */     if (o == null) return null; 
/*  219 */     if (o instanceof BigDecimal) return (BigDecimal)o; 
/*  220 */     return new BigDecimal(String.valueOf(o));
/*      */   }
/*      */   
/*      */   private static String toStringSafe(Object o) {
/*  224 */     return (o == null) ? "" : String.valueOf(o);
/*      */   }
/*      */   private static String pad2(String mes) {
/*      */     int m;
/*  228 */     if (mes == null || mes.trim().isEmpty()) return "00"; 
/*  229 */     String digits = mes.replaceAll("\\D+", "");
/*  230 */     if (digits.isEmpty()) return "00"; 
/*      */     
/*  232 */     try { m = Integer.parseInt(digits); } catch (Exception e) { return "00"; }
/*  233 */      if (m < 1 || m > 12) return "00"; 
/*  234 */     return String.valueOf((m < 10) ? "0" : "") + m;
/*      */   }
/*      */   
/*      */   private static void closeQuietly(ResultSet rs) {
/*  238 */     if (rs != null) try { rs.close(); } catch (Exception exception) {}
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   private static byte[] gerarTxt(ResultSet rs) throws Exception {
/*  244 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  245 */     int i = 0;
/*      */     
/*  247 */     while (rs.next()) {
/*  248 */       if (i == 0) {
/*  249 */         BigDecimal totalRegistros = rs.getBigDecimal("CONTADOR");
/*  250 */         if (totalRegistros == null) totalRegistros = BigDecimal.ZERO; 
/*  251 */         totalRegistros = totalRegistros.add(BigDecimal.ONE);
/*      */         
/*  253 */         String codEmpAnp = rs.getString("CODEMP_ANP");
/*  254 */         String referenciaArq = rs.getString("MESREFERENCIA");
/*      */         
/*  256 */         String header = String.valueOf(geraLinha("0", 10)) + 
/*  257 */           geraLinha(codEmpAnp, 10) + 
/*  258 */           geraLinha(referenciaArq, 6) + 
/*  259 */           geraLinha(String.valueOf(totalRegistros), 7);
/*  260 */         baos.write(header.getBytes("ISO-8859-1"));
/*      */       } 
/*      */       
/*  263 */       String linha = escrever(
/*  264 */           rs.getString("SEQUENCIA"), 
/*  265 */           rs.getString("CODEMP_ANP"), 
/*  266 */           rs.getString("MESREFERENCIA"), 
/*  267 */           rs.getString("CODOPERACAO"), 
/*  268 */           rs.getString("CODINSTALACAO1"), 
/*  269 */           rs.getString("CODINSTALACAO2"), 
/*  270 */           rs.getString("CODPROD_ANP"), 
/*  271 */           rs.getString("QTD_PROD_UN_ANP"), 
/*  272 */           rs.getString("QTD_PROD_OPERADO_KG"), 
/*  273 */           rs.getString("COD_MODAL_MOVIMENTACAO"), 
/*  274 */           rs.getString("COD_OLEODUTO_TRANSPORTE"), 
/*  275 */           rs.getString("CNPJ_PARC"), 
/*  276 */           rs.getString("CODCID_ANP"), 
/*  277 */           rs.getString("COD_ATIV_PARC_ANP"), 
/*  278 */           rs.getString("CODPAIS_ANP"), 
/*  279 */           rs.getString("NUM_LICEN_IMPORT"), 
/*  280 */           rs.getString("NUM_DECLARACAO_IMPORT"), 
/*  281 */           rs.getString("NUMNF"), 
/*  282 */           rs.getString("SERIE_NF"), 
/*  283 */           rs.getString("DAT_OPER_COMERCIAL"), 
/*  284 */           rs.getString("COD_TIP_TARIFA_SERVICO"), 
/*  285 */           rs.getString("CARACTERISTICA"), 
/*  286 */           rs.getString("METODO"), 
/*  287 */           rs.getString("MODALIDADE_FRETE"), 
/*  288 */           rs.getString("NUM_DOC_QUALIDADE"), 
/*  289 */           rs.getString("COD_OPER_RESULTANTE"), 
/*  290 */           rs.getString("VLRUNIT_NF"), 
/*  291 */           rs.getString("RECEPIENTE_GLP"), 
/*  292 */           rs.getString("CHAVE_NFE_CTE"));
/*      */       
/*  294 */       baos.write("\r\n".getBytes("ISO-8859-1"));
/*  295 */       baos.write(linha.getBytes("ISO-8859-1"));
/*  296 */       i++;
/*      */     } 
/*  298 */     return baos.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String escrever(String contador, String codEmpAnp, String referenciaArq, String codOperAnp, String codInstalacao1, String codInstalacao2, String codProdAnp, String qtdUnPadrao, String qtdKg, String codModal, String codOleoduto, String cgc_cpf, String codCidAnp, String codCnaeAnpParc, String codPaisAnp, String licencaImportacao, String declaracaoImportacao, String nroNFOper, String codSerieNfAnp, String datOperComercial, String codTipTarifaServ, String caracteristicaInativo, String metodoInativo, String modalidadeFrete, String nroDocQualidade, String codProdResultante, String vlrUnit, String recepienteGlp, String chaveNfeCte) {
/*  309 */     String[] inteiroDecimal = normalizarValorMonetario(vlrUnit);
/*  310 */     String vlrunitinteiro = limitaString(StringUtils.leftPad(inteiroDecimal[0], 3, "0"), 3);
/*  311 */     String vlrunitdecimal = limitaString(StringUtils.rightPad(inteiroDecimal[1], 4, "0"), 4);
/*      */     
/*  313 */     StringBuilder linha = new StringBuilder(256);
/*  314 */     linha.append(geraLinha(contador, 10));
/*  315 */     linha.append(geraLinha(codEmpAnp, 10));
/*  316 */     linha.append(geraLinha(referenciaArq, 6));
/*  317 */     linha.append(geraLinha(codOperAnp, 7));
/*  318 */     linha.append(geraLinha(codInstalacao1, 7));
/*  319 */     linha.append(geraLinha(codInstalacao2, 7));
/*  320 */     linha.append(geraLinha(codProdAnp, 9));
/*  321 */     linha.append(geraLinha(qtdUnPadrao, 15));
/*  322 */     linha.append(geraLinha(qtdKg, 15));
/*  323 */     linha.append(geraLinha(codModal, 1));
/*  324 */     linha.append(geraLinha(codOleoduto, 7));
/*  325 */     linha.append(geraLinha(cgc_cpf, 14));
/*  326 */     linha.append(geraLinha(codCidAnp, 7));
/*  327 */     linha.append(geraLinha(codCnaeAnpParc, 5));
/*  328 */     linha.append(geraLinha(codPaisAnp, 4));
/*  329 */     linha.append(geraLinha(licencaImportacao, 10));
/*  330 */     linha.append(geraLinha(declaracaoImportacao, 10));
/*  331 */     linha.append(geraLinha(nroNFOper, 7));
/*  332 */     linha.append(geraLinha(codSerieNfAnp, 2));
/*  333 */     linha.append(geraLinha(datOperComercial, 8));
/*  334 */     linha.append(geraLinha(codTipTarifaServ, 1));
/*  335 */     linha.append(geraLinha(caracteristicaInativo, 3));
/*  336 */     linha.append(geraLinha(metodoInativo, 3));
/*  337 */     linha.append(geraLinha(modalidadeFrete, 2));
/*  338 */     linha.append(geraLinha(nroDocQualidade, 10));
/*  339 */     linha.append(geraLinha(codProdResultante, 9));
/*  340 */     linha.append(geraLinha(String.valueOf(vlrunitinteiro) + vlrunitdecimal, 7));
/*  341 */     linha.append(geraLinha(recepienteGlp, 2));
/*  342 */     linha.append(geraLinha(chaveNfeCte, 44));
/*  343 */     return linha.toString();
/*      */   }
/*      */   
/*      */   private static String[] normalizarValorMonetario(String vlrUnit) {
/*  347 */     String inteiro = "0";
/*  348 */     String decimal = "0";
/*  349 */     if (vlrUnit == null || "0.0".equalsIgnoreCase(vlrUnit)) {
/*  350 */       return new String[] { inteiro, decimal };
/*      */     }
/*  352 */     String v = vlrUnit.replace(",", ".");
/*  353 */     int idx = v.lastIndexOf('.');
/*  354 */     if (idx >= 0) {
/*  355 */       inteiro = v.substring(0, idx);
/*  356 */       decimal = v.substring(idx + 1);
/*      */     } else {
/*  358 */       inteiro = v;
/*      */     } 
/*  360 */     inteiro = inteiro.replace(".", "");
/*  361 */     decimal = decimal.replace(".", "");
/*  362 */     if (inteiro.isEmpty()) inteiro = "0"; 
/*  363 */     if (decimal.isEmpty()) decimal = "0"; 
/*  364 */     return new String[] { inteiro, decimal };
/*      */   }
/*      */   
/*      */   public static String geraLinha(String texto, int size) {
/*  368 */     if (texto == null) texto = ""; 
/*  369 */     return limitaString(StringUtils.leftPad(texto, size, "0"), size);
/*      */   }
/*      */   
/*      */   public static String limitaString(String texto, int maximo) {
/*  373 */     if (texto == null) return StringUtils.leftPad("", maximo, "0"); 
/*  374 */     return (texto.length() <= maximo) ? texto : texto.substring(0, maximo);
/*      */   }
/*      */   
/*      */   private static void salvarDiscoSeConfigurado(ContextoAcao ctx, byte[] conteudo, String nomeArquivo) {
/*      */     try {
/*  379 */       Object base = ctx.getParametroSistema("DIRBASESIMP");
/*  380 */       if (base != null) {
/*  381 */         String baseDir = String.valueOf(base);
/*  382 */         if (!baseDir.endsWith("/") && !baseDir.endsWith("\\")) {
/*  383 */           baseDir = String.valueOf(baseDir) + "/";
/*      */         }
/*  385 */         String path = String.valueOf(baseDir) + nomeArquivo;
/*  386 */         FileOutputStream fos = null;
/*      */         try {
/*  388 */           fos = new FileOutputStream(path);
/*  389 */           fos.write(conteudo);
/*      */         } finally {
/*  391 */           if (fos != null) try { fos.close(); } catch (Exception exception) {} 
/*      */         } 
/*      */       } 
/*  394 */     } catch (Exception exception) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] gerarXlsx(ResultSet rs, String datReferencia) throws Exception {
/*  402 */     XSSFWorkbook wb = new XSSFWorkbook();
/*      */ 
/*      */     
/*  405 */     XSSFDataFormat xSSFDataFormat = wb.createDataFormat();
/*      */ 
/*      */     
/*  408 */     XSSFCellStyle xSSFCellStyle1 = wb.createCellStyle();
/*  409 */     xSSFCellStyle1.setDataFormat(xSSFDataFormat.getFormat("@"));
/*      */ 
/*      */     
/*  412 */     XSSFCellStyle xSSFCellStyle2 = wb.createCellStyle();
/*  413 */     xSSFCellStyle2.setDataFormat(xSSFDataFormat.getFormat("0.00"));
/*      */ 
/*      */     
/*  416 */     XSSFSheet shBasic = wb.createSheet("DADOS_BASICOS");
/*      */     
/*  418 */     XSSFRow hdrBasic = shBasic.createRow(0);
/*  419 */     for (int i = 0; i < CAB_DADOS_BASICOS.size(); i++) {
/*  420 */       XSSFCell cell = hdrBasic.createCell(i);
/*  421 */       cell.setCellValue(CAB_DADOS_BASICOS.get(i));
/*      */     } 
/*      */     
/*  424 */     List<String> valores = new ArrayList<>();
/*  425 */     valores.add("57777724000163");
/*  426 */     valores.add(datReferencia);
/*  427 */     valores.add("ODAIR");
/*  428 */     valores.add("1126010076");
/*  429 */     valores.add("odair@lubmaster.com.br>");
/*  430 */     XSSFRow rowBasic = shBasic.createRow(1); int j;
/*  431 */     for (j = 0; j < valores.size(); j++) {
/*  432 */       XSSFCell cell = rowBasic.createCell(j);
/*  433 */       cell.setCellValue(valores.get(j));
/*      */     } 
/*  435 */     for (j = 0; j < CAB_DADOS_BASICOS.size(); j++) { 
/*  436 */       try { shBasic.autoSizeColumn(j); } catch (Exception exception) {} }
/*      */ 
/*      */ 
/*      */     
/*  440 */     XSSFSheet sheet = wb.createSheet("MOVIMENTACAO");
/*      */     
/*  442 */     int rowNum = 0;
/*      */     
/*  444 */     XSSFRow header = sheet.createRow(rowNum++); int k;
/*  445 */     for (k = 0; k < CAB_MOV_IMPORTADOR.size(); k++) {
/*  446 */       XSSFCell cell = header.createCell(k);
/*  447 */       cell.setCellValue(CAB_MOV_IMPORTADOR.get(k));
/*      */     } 
/*      */ 
/*      */     
/*  451 */     while (rs.next()) {
/*  452 */       XSSFRow row = sheet.createRow(rowNum++);
/*      */ 
/*      */       
/*  455 */       setCell(row, 0, rs.getString("SEQUENCIA"));
/*      */       
/*  457 */       setCell(row, 1, rs.getString("CODOPERACAO"));
/*      */       
/*  459 */       setCell(row, 2, rs.getString("CODINSTALACAO1"));
/*      */       
/*  461 */       setCell(row, 3, rs.getString("CODINSTALACAO2"));
/*      */       
/*  463 */       setCell(row, 4, rs.getString("CODPROD_ANP"));
/*      */       
/*  465 */       setCell(row, 5, rs.getString("QTD_PROD_UN_ANP"));
/*      */       
/*  467 */       setCell(row, 6, rs.getString("QTD_PROD_OPERADO_KG"));
/*      */       
/*  469 */       setCell(row, 7, rs.getString("COD_MODAL_MOVIMENTACAO"));
/*      */       
/*  471 */       setCell(row, 8, rs.getString("COD_OLEODUTO_TRANSPORTE"));
/*      */       
/*  473 */       setCellText(row, 9, rs.getString("CNPJ_PARC"), (CellStyle)xSSFCellStyle1);
/*      */       
/*  475 */       setCell(row, 10, rs.getString("CODCID_ANP"));
/*      */       
/*  477 */       setCell(row, 11, rs.getString("COD_ATIV_PARC_ANP"));
/*      */       
/*  479 */       setCell(row, 12, rs.getString("CODPAIS_ANP"));
/*      */       
/*  481 */       setCell(row, 13, rs.getString("NUM_LICEN_IMPORT"));
/*      */       
/*  483 */       setCell(row, 14, rs.getString("NUM_DECLARACAO_IMPORT"));
/*      */       
/*  485 */       setCell(row, 15, rs.getString("NUMNF"));
/*      */       
/*  487 */       setCell(row, 16, rs.getString("SERIE_NF"));
/*      */       
/*  489 */       setCellText(row, 17, rs.getString("DAT_OPER_COMERCIAL"), (CellStyle)xSSFCellStyle1);
/*      */       
/*  491 */       setCell(row, 18, rs.getString("COD_TIP_TARIFA_SERVICO"));
/*      */       
/*  493 */       setCell(row, 19, rs.getString("CARACTERISTICA"));
/*      */       
/*  495 */       setCell(row, 20, rs.getString("METODO"));
/*      */       
/*  497 */       setCell(row, 21, rs.getString("MODALIDADE_FRETE"));
/*      */       
/*  499 */       setCell(row, 22, rs.getString("NUM_DOC_QUALIDADE"));
/*      */       
/*  501 */       setCell(row, 23, rs.getString("COD_OPER_RESULTANTE"));
/*      */       
/*  503 */       setCellDecimal2(row, 24, rs.getString("VLRUNIT_NF"), (CellStyle)xSSFCellStyle2);
/*      */       
/*  505 */       setCell(row, 25, rs.getString("RECEPIENTE_GLP"));
/*      */       
/*  507 */       String chave = rs.getString("CHAVE_NFE_CTE");
/*  508 */       setCellText(row, 26, isZeroLike(chave) ? "" : ((chave == null) ? "" : chave), (CellStyle)xSSFCellStyle1);
/*      */     } 
/*      */     
/*  511 */     for (k = 0; k < CAB_MOV_IMPORTADOR.size(); k++) { 
/*  512 */       try { sheet.autoSizeColumn(k); } catch (Exception exception) {} }
/*      */ 
/*      */     
/*  515 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/*      */     
/*  517 */     try { wb.write(out);
/*  518 */       return out.toByteArray(); }
/*      */     finally { 
/*  520 */       try { out.close(); } catch (Exception exception) {} }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void setCell(XSSFRow row, int idx, String val) {
/*  529 */     XSSFCell cell = row.createCell(idx);
/*  530 */     if (val != null && val.matches("^-?\\d+(\\.\\d+)?$")) {
/*      */       try {
/*  532 */         cell.setCellValue(Double.parseDouble(val));
/*      */         return;
/*  534 */       } catch (NumberFormatException numberFormatException) {}
/*      */     }
/*  536 */     cell.setCellValue((val == null) ? "" : val);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setCellText(XSSFRow row, int idx, String val, CellStyle textStyle) {
/*  541 */     XSSFCell cell = row.createCell(idx);
/*  542 */     cell.setCellStyle(textStyle);
/*  543 */     cell.setCellValue((val == null) ? "" : val);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setCellDecimal2(XSSFRow row, int idx, String val, CellStyle decimal2) {
/*  548 */     XSSFCell cell = row.createCell(idx);
/*  549 */     if (val == null || val.trim().isEmpty()) {
/*  550 */       cell.setCellValue("");
/*      */       return;
/*      */     } 
/*      */     try {
/*  554 */       String norm = val.replace(",", ".").trim();
/*  555 */       BigDecimal bd = new BigDecimal(norm);
/*  556 */       bd = bd.setScale(2, RoundingMode.HALF_UP);
/*  557 */       cell.setCellStyle(decimal2);
/*  558 */       cell.setCellValue(bd.doubleValue());
/*  559 */     } catch (Exception ex) {
/*      */       
/*  561 */       cell.setCellValue(val);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isZeroLike(String s) {
/*  567 */     if (s == null) return true; 
/*  568 */     String t = s.trim();
/*  569 */     if (t.isEmpty()) return true;
/*      */     
/*  571 */     if (t.matches("^[0.\\.]+$")) {
/*      */       try {
/*  573 */         BigDecimal bd = new BigDecimal(t.replace(".", ""));
/*  574 */         return (bd.compareTo(BigDecimal.ZERO) == 0);
/*  575 */       } catch (Exception ignore) {
/*  576 */         return "0".equals(t);
/*      */       } 
/*      */     }
/*  579 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] zipSingleFile(String entryName, byte[] content) throws IOException {
/*  585 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  586 */     ZipOutputStream zos = null;
/*      */     try {
/*  588 */       zos = new ZipOutputStream(baos);
/*  589 */       ZipEntry e = new ZipEntry(entryName);
/*  590 */       e.setSize(((content == null) ? 0L : content.length));
/*  591 */       zos.putNextEntry(e);
/*  592 */       if (content != null && content.length > 0) {
/*  593 */         zos.write(content);
/*      */       }
/*  595 */       zos.closeEntry();
/*      */     } finally {
/*  597 */       if (zos != null) try { zos.close(); } catch (Exception exception) {} 
/*      */     } 
/*  599 */     return baos.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static BigDecimal saveAD_DETSIMULARSIMP(EntityFacade dwf, BigDecimal codSimulaSimp, String mesReferente, BigDecimal anoReferente, byte[] binTxtComPH, byte[] binXlsxComPH, byte[] binZipComPH) throws Exception {
/*  611 */     DynamicVO vo = (DynamicVO)dwf.getDefaultValueObjectInstance("AD_ESTOQUESIMP");
/*  612 */     vo.setProperty("CODSIMSIM", codSimulaSimp);
/*  613 */     vo.setProperty("CODUSUINC", JapeSessionContext.getRequiredProperty("usuario_logado"));
/*  614 */     vo.setProperty("DATINC", TimeUtils.getNow());
/*  615 */     vo.setProperty("STATUS", "0");
/*  616 */     vo.setProperty("MES_REFERENCIA", mesReferente);
/*  617 */     vo.setProperty("ANO_REFERENTE", anoReferente);
/*      */     
/*  619 */     vo.setProperty("ARQUIVO", binTxtComPH);
/*  620 */     vo.setProperty("ARQUIVOXLS", binXlsxComPH);
/*  621 */     vo.setProperty("ARQUIVOZIP", binZipComPH);
/*      */     
/*  623 */     dwf.createEntity("AD_ESTOQUESIMP", (EntityVO)vo);
/*  624 */     return vo.asBigDecimal("CODESTSIMP");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] adicionarPlaceHolderArquivo(byte[] arquivo, String nomeArquivo, String mimeType) throws IOException {
/*  630 */     if (arquivo == null) arquivo = new byte[0]; 
/*  631 */     if (mimeType == null || mimeType.trim().isEmpty()) mimeType = "application/octet-stream";
/*      */     
/*  633 */     ByteArrayOutputStream arqByte = new ByteArrayOutputStream();
/*  634 */     BufferedOutputStream bf = null;
/*      */     try {
/*  636 */       bf = new BufferedOutputStream(arqByte);
/*  637 */       byte[] textField = ("__start_fileinformation__{\"name\":\"" + nomeArquivo + "\",\"size\":" + arquivo.length + 
/*  638 */         ",\"type\":\"" + mimeType + "\"}__end_fileinformation__").getBytes("ISO-8859-1");
/*  639 */       bf.write(textField);
/*  640 */       bf.write(arquivo);
/*  641 */       bf.flush();
/*      */     } finally {
/*  643 */       if (bf != null) try { bf.close(); } catch (Exception exception) {} 
/*      */     } 
/*  645 */     return arqByte.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static String mensagemRetorno(BigDecimal codEst) {
/*  651 */     String id = "br.com.sankhya.menu.adicional.AD_ESTOQUESIMP";
/*  652 */     String pk = "{\"CODESTSIMP\"=\"" + codEst + "\"}";
/*  653 */     String caminho = "/mge/system.jsp#app/";
/*  654 */     String idBase64 = DatatypeConverter.printBase64Binary(id.getBytes());
/*  655 */     String paransBase64 = DatatypeConverter.printBase64Binary(pk.replace("=", ":").getBytes());
/*  656 */     String link = String.valueOf(caminho) + idBase64 + "/" + paransBase64;
/*      */     
/*  658 */     return "<div style=\"display:flex;align-items:center;gap:10px;justify-content:center;padding:10px;border:1px solid #e0e0e0;border-radius:8px;background:#fafafa;\">  <img src=\"http://imageshack.com/a/img923/7316/ux573F.png\" style=\"width:22px;height:22px;opacity:.9;\"/>  <span style=\"font-size:14px;color:#333;\">Estoque SIMP gerado com sucesso! Código:</span>  <a href=\"" + 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  663 */       link + "\" target=\"_top\" " + 
/*  664 */       "     style=\"text-decoration:none;font-weight:bold;color:#0a7f3f;background:#e8fff1;" + 
/*  665 */       "            padding:4px 10px;border-radius:14px;border:1px solid #c7f0d8;\">" + 
/*  666 */       codEst + 
/*  667 */       "  </a>" + 
/*  668 */       "</div>";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResultSet gerarArquivo(JdbcWrapper jdbcWrapper, BigDecimal codSimulaSimp) throws Exception {
/*  674 */     String sql = "WITH BASE AS (\r\nSELECT\r\n    'BASE' AS TIPO\r\n    , CODDETSIMSIMP AS SEQUENCIA\r\n    , COALESCE(CODEMP_ANP,'0') AS CODEMP_ANP\r\n    , COALESCE(MESREFERENCIA,'0') AS MESREFERENCIA\r\n    , OPER.CODOPER_ANP AS CODOPERACAO\r\n    , OPER.DESCRICAO\r\n    , OPER.TOTALIZADORGERAL\r\n    , COALESCE(CODINSTALACAO1,'0') AS CODINSTALACAO1\r\n    , COALESCE(CODINSTALACAO2,'0') AS CODINSTALACAO2\r\n    , COALESCE(CODPROD_ANP,'0') AS CODPROD_ANP\r\n    , COALESCE(QTD_PROD_UN_ANP, '0') AS QTD_PROD_UN_ANP\r\n    , COALESCE(QTD_PROD_OPERADO_KG,'0') AS QTD_PROD_OPERADO_KG\r\n    , COALESCE(COD_MODAL_MOVIMENTACAO,'0') AS COD_MODAL_MOVIMENTACAO\r\n    , COALESCE(COD_OLEODUTO_TRANSPORTE,'0') AS COD_OLEODUTO_TRANSPORTE\r\n    , COALESCE(CNPJ_PARC,'0') AS CNPJ_PARC\r\n    , COALESCE(CODCID_ANP,'0') AS CODCID_ANP\r\n    , COALESCE(COD_ATIV_PARC_ANP,'0') AS COD_ATIV_PARC_ANP\r\n    , COALESCE(CODPAIS_ANP,'0') AS CODPAIS_ANP\r\n    , COALESCE(NUM_LICEN_IMPORT,'0') AS NUM_LICEN_IMPORT\r\n    , COALESCE(NUM_DECLARACAO_IMPORT,'0') AS NUM_DECLARACAO_IMPORT\r\n    , COALESCE(NUMNF,'0') AS NUMNF\r\n    , COALESCE(SERIE_NF,'0') AS SERIE_NF\r\n    , COALESCE(DAT_OPER_COMERCIAL,'0') AS DAT_OPER_COMERCIAL\r\n    , COALESCE(COD_TIP_TARIFA_SERVICO,'0') AS COD_TIP_TARIFA_SERVICO\r\n    , COALESCE(CARACTERISTICA,'0') AS CARACTERISTICA\r\n    , COALESCE(METODO,'0') AS METODO\r\n    , COALESCE(MODALIDADE_FRETE,'0') AS MODALIDADE_FRETE\r\n    , COALESCE(NUM_DOC_QUALIDADE,'0') AS NUM_DOC_QUALIDADE\r\n    , COALESCE(COD_OPER_RESULTANTE,'0') AS COD_OPER_RESULTANTE\r\n    , COALESCE(VLRUNIT_NF,'0') AS VLRUNIT_NF\r\n    , COALESCE(RECEPIENTE_GLP,'0') AS RECEPIENTE_GLP\r\n    , COALESCE(CHAVE_NFE_CTE,'0') AS CHAVE_NFE_CTE\r\n    , COALESCE((SELECT TO_CHAR(COUNT(*)) FROM AD_DETSIMULARSIMP DET WHERE DET.CODSIMSIM = A.CODSIMSIM), '0') AS CONTADOR\r\nFROM AD_DETSIMULARSIMP A\r\nJOIN AD_OPERACAOANP OPER ON A.CODOPER = OPER.CODOPER AND OPER.ATIVA = 'S'\r\nWHERE CODSIMSIM = " + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  711 */       codSimulaSimp + "\r\n" + 
/*  712 */       "ORDER BY CODDETSIMSIMP\r\n" + 
/*  713 */       "), \r\n" + 
/*  714 */       "\r\n" + 
/*  715 */       "ESTOQUE_INICIAL AS (\r\n" + 
/*  716 */       "    SELECT * FROM BASE WHERE CODOPERACAO = '3010003'\r\n" + 
/*  717 */       "),\r\n" + 
/*  718 */       "\r\n" + 
/*  719 */       "TOTALIZADOR_OPERACAO AS (\r\n" + 
/*  720 */       "    SELECT\r\n" + 
/*  721 */       "        'TOTALIZADOR OPERACAO' AS TIPO \r\n" + 
/*  722 */       "        , ROWNUM + (SELECT DISTINCT CONTADOR FROM BASE) AS SEQUENCIA\r\n" + 
/*  723 */       "        , AAA.*\r\n" + 
/*  724 */       "    FROM (\r\n" + 
/*  725 */       "            SELECT\r\n" + 
/*  726 */       "                A.CODEMP_ANP\r\n" + 
/*  727 */       "                , A.MESREFERENCIA\r\n" + 
/*  728 */       "                , SUBSTR(A.CODOPERACAO,1,4) || '998' AS CODOPER\r\n" + 
/*  729 */       "                , 'TOTAL DE SAÍDAS COMERCIAIS NACIONAIS' AS DESCRICAO\r\n" + 
/*  730 */       "                , A.TOTALIZADORGERAL\r\n" + 
/*  731 */       "                , A.CODINSTALACAO1\r\n" + 
/*  732 */       "                , '0' AS CODINSTALACAO2\r\n" + 
/*  733 */       "                , A.CODPROD_ANP\r\n" + 
/*  734 */       "                , TO_CHAR(SUM(A.QTD_PROD_UN_ANP)) AS QTD_PROD_UN_ANP \r\n" + 
/*  735 */       "                , '0' AS QTD_PROD_OPERADO_KG\r\n" + 
/*  736 */       "                , '0' AS COD_MODAL_MOVIMENTACAO\r\n" + 
/*  737 */       "                , '0' AS COD_OLEODUTO_TRANSPORTE\r\n" + 
/*  738 */       "                , '0' AS CNPJ_PARC\r\n" + 
/*  739 */       "                , '0' AS CODCID_ANP\r\n" + 
/*  740 */       "                , '0' AS COD_ATIV_PARC_ANP\r\n" + 
/*  741 */       "                , '0' AS COD_PAIS_ANP\r\n" + 
/*  742 */       "                , '0' AS NUM_LICEN_IMPORT\r\n" + 
/*  743 */       "                , '0' AS NUM_DECLARACAO_IMPORT\r\n" + 
/*  744 */       "                , '0' AS NUMNF\r\n" + 
/*  745 */       "                , '0' AS SERIE_NF\r\n" + 
/*  746 */       "                , '0' AS DAT_OPER_COMERCIAL\r\n" + 
/*  747 */       "                , '0' AS COD_TIP_TARIFA_SERVICO\r\n" + 
/*  748 */       "                , '0' AS CARACTERISTICA\r\n" + 
/*  749 */       "                , '0' AS METODO\r\n" + 
/*  750 */       "                , '0' AS MODALIDADE_FRETE\r\n" + 
/*  751 */       "                , '0' AS NUM_DOC_QUALIDADE\r\n" + 
/*  752 */       "                , '0' AS COD_OPER_RESULTANTE\r\n" + 
/*  753 */       "                , '0' AS VLRUNIT_NF\r\n" + 
/*  754 */       "                , '0' AS RECEPIENTE_GLP\r\n" + 
/*  755 */       "                , '0' AS CHAVE_NFE_CTE\r\n" + 
/*  756 */       "                , '0' AS CONTADOR \r\n" + 
/*  757 */       "            FROM BASE A\r\n" + 
/*  758 */       "\t\t\tWHERE SUBSTR(A.CODOPERACAO,1,4) NOT LIKE '3%'\r\n" + 
/*  759 */       "            AND SUBSTR(A.CODOPERACAO,1,4) NOT LIKE '4030%'\r\n" + 
/*  760 */       "            GROUP BY A.CODEMP_ANP, A.MESREFERENCIA, A.TOTALIZADORGERAL, A.CODINSTALACAO1, A.CODPROD_ANP, SUBSTR(A.CODOPERACAO,1,4)\r\n" + 
/*  761 */       "        ) AAA\r\n" + 
/*  762 */       "),\r\n" + 
/*  763 */       "\r\n" + 
/*  764 */       "SEQ_AFTER_TOT AS (\r\n" + 
/*  765 */       "    SELECT MAX(SEQUENCIA) AS CONTADOR FROM TOTALIZADOR_OPERACAO\r\n" + 
/*  766 */       "),\r\n" + 
/*  767 */       "\r\n" + 
/*  768 */       "TOT_GERAL_ENTRADA AS (\r\n" + 
/*  769 */       "    SELECT \r\n" + 
/*  770 */       "        'TOT GER ENT' AS TIPO\r\n" + 
/*  771 */       "        , ROWNUM + (SELECT DISTINCT CONTADOR FROM SEQ_AFTER_TOT) AS SEQUENCIA\r\n" + 
/*  772 */       "        , AAA.*\r\n" + 
/*  773 */       "    FROM (\r\n" + 
/*  774 */       "            SELECT\r\n" + 
/*  775 */       "                A.CODEMP_ANP\r\n" + 
/*  776 */       "                , A.MESREFERENCIA\r\n" + 
/*  777 */       "                , '4011998' AS CODOPER\r\n" + 
/*  778 */       "                , 'TOT GER ENT' AS DESCRICAO\r\n" + 
/*  779 */       "                , A.TOTALIZADORGERAL\r\n" + 
/*  780 */       "                , A.CODINSTALACAO1\r\n" + 
/*  781 */       "                , '0' AS CODINSTALACAO2\r\n" + 
/*  782 */       "                , CODPROD_ANP\r\n" + 
/*  783 */       "                , TO_CHAR(SUM(A.QTD_PROD_UN_ANP)) AS QTD_PROD_UN_ANP \r\n" + 
/*  784 */       "                , '0' AS QTD_PROD_OPERADO_KG\r\n" + 
/*  785 */       "                , '0' AS COD_MODAL_MOVIMENTACAO\r\n" + 
/*  786 */       "                , '0' AS COD_OLEODUTO_TRANSPORTE\r\n" + 
/*  787 */       "                , '0' AS CNPJ_PARC\r\n" + 
/*  788 */       "                , '0' AS CODCID_ANP\r\n" + 
/*  789 */       "                , '0' AS COD_ATIV_PARC_ANP\r\n" + 
/*  790 */       "                , '0' AS COD_PAIS_ANP\r\n" + 
/*  791 */       "                , '0' AS NUM_LICEN_IMPORT\r\n" + 
/*  792 */       "                , '0' AS NUM_DECLARACAO_IMPORT\r\n" + 
/*  793 */       "                , '0' AS NUMNF\r\n" + 
/*  794 */       "                , '0' AS SERIE_NF\r\n" + 
/*  795 */       "                , '0' AS DAT_OPER_COMERCIAL\r\n" + 
/*  796 */       "                , '0' AS COD_TIP_TARIFA_SERVICO\r\n" + 
/*  797 */       "                , '0' AS CARACTERISTICA\r\n" + 
/*  798 */       "                , '0' AS METODO\r\n" + 
/*  799 */       "                , '0' AS MODALIDADE_FRETE\r\n" + 
/*  800 */       "                , '0' AS NUM_DOC_QUALIDADE\r\n" + 
/*  801 */       "                , '0' AS COD_OPER_RESULTANTE\r\n" + 
/*  802 */       "                , '0' AS VLRUNIT_NF\r\n" + 
/*  803 */       "                , '0' AS RECEPIENTE_GLP\r\n" + 
/*  804 */       "                , '0' AS CHAVE_NFE_CTE\r\n" + 
/*  805 */       "                , '0' AS CONTADOR \r\n" + 
/*  806 */       "            FROM BASE A\r\n" + 
/*  807 */       "\t\t\tWHERE SUBSTR(A.CODOPERACAO,1,4) NOT LIKE '3%'\r\n" + 
/*  808 */       "            AND SUBSTR(A.CODOPERACAO,1,4) NOT LIKE '4030%'\r\n" + 
/*  809 */       "            AND A.TOTALIZADORGERAL = 'E'\r\n" + 
/*  810 */       "            GROUP BY A.CODEMP_ANP, A.MESREFERENCIA, A.CODINSTALACAO1, CODPROD_ANP, A.TOTALIZADORGERAL\r\n" + 
/*  811 */       "    ) AAA\r\n" + 
/*  812 */       "),\r\n" + 
/*  813 */       "\r\n" + 
/*  814 */       "TOT_GERAL_ENTRADA_DET AS (\r\n" + 
/*  815 */       "    SELECT \r\n" + 
/*  816 */       "        'TOT GER ENT' AS TIPO\r\n" + 
/*  817 */       "        , ROWNUM + (SELECT DISTINCT CONTADOR FROM SEQ_AFTER_TOT) AS SEQUENCIA\r\n" + 
/*  818 */       "        , AAA.*\r\n" + 
/*  819 */       "    FROM (\r\n" + 
/*  820 */       "            SELECT\r\n" + 
/*  821 */       "                A.CODEMP_ANP\r\n" + 
/*  822 */       "                , A.MESREFERENCIA\r\n" + 
/*  823 */       "                , A.CODOPERACAO\r\n" + 
/*  824 */       "                , A.DESCRICAO\r\n" + 
/*  825 */       "                , A.TOTALIZADORGERAL\r\n" + 
/*  826 */       "                , A.CODINSTALACAO1\r\n" + 
/*  827 */       "                , '0' AS CODINSTALACAO2\r\n" + 
/*  828 */       "                , CODPROD_ANP\r\n" + 
/*  829 */       "                , TO_CHAR(SUM(A.QTD_PROD_UN_ANP)) AS QTD_PROD_UN_ANP \r\n" + 
/*  830 */       "                , '0' AS QTD_PROD_OPERADO_KG\r\n" + 
/*  831 */       "                , '0' AS COD_MODAL_MOVIMENTACAO\r\n" + 
/*  832 */       "                , '0' AS COD_OLEODUTO_TRANSPORTE\r\n" + 
/*  833 */       "                , '0' AS CNPJ_PARC\r\n" + 
/*  834 */       "                , '0' AS CODCID_ANP\r\n" + 
/*  835 */       "                , '0' AS COD_ATIV_PARC_ANP\r\n" + 
/*  836 */       "                , '0' AS COD_PAIS_ANP\r\n" + 
/*  837 */       "                , '0' AS NUM_LICEN_IMPORT\r\n" + 
/*  838 */       "                , '0' AS NUM_DECLARACAO_IMPORT\r\n" + 
/*  839 */       "                , '0' AS NUMNF\r\n" + 
/*  840 */       "                , '0' AS SERIE_NF\r\n" + 
/*  841 */       "                , '0' AS DAT_OPER_COMERCIAL\r\n" + 
/*  842 */       "                , '0' AS COD_TIP_TARIFA_SERVICO\r\n" + 
/*  843 */       "                , '0' AS CARACTERISTICA\r\n" + 
/*  844 */       "                , '0' AS METODO\r\n" + 
/*  845 */       "                , '0' AS MODALIDADE_FRETE\r\n" + 
/*  846 */       "                , '0' AS NUM_DOC_QUALIDADE\r\n" + 
/*  847 */       "                , '0' AS COD_OPER_RESULTANTE\r\n" + 
/*  848 */       "                , '0' AS VLRUNIT_NF\r\n" + 
/*  849 */       "                , '0' AS RECEPIENTE_GLP\r\n" + 
/*  850 */       "                , '0' AS CHAVE_NFE_CTE\r\n" + 
/*  851 */       "                , '0' AS CONTADOR \r\n" + 
/*  852 */       "            FROM BASE A\r\n" + 
/*  853 */       "\t\t\tWHERE SUBSTR(A.CODOPERACAO,1,4) NOT LIKE '3%'\r\n" + 
/*  854 */       "            AND SUBSTR(A.CODOPERACAO,1,4) NOT LIKE '4030%'\r\n" + 
/*  855 */       "            AND A.TOTALIZADORGERAL = 'E'\r\n" + 
/*  856 */       "            GROUP BY A.CODEMP_ANP, A.MESREFERENCIA, A.CODINSTALACAO1, CODPROD_ANP, A.DESCRICAO, A.TOTALIZADORGERAL, A.CODOPERACAO\r\n" + 
/*  857 */       "    ) AAA\r\n" + 
/*  858 */       "),\r\n" + 
/*  859 */       "\r\n" + 
/*  860 */       "SEQ_AFTER_TOT_ENT AS (\r\n" + 
/*  861 */       "    SELECT MAX(SEQUENCIA) AS CONTADOR FROM TOT_GERAL_ENTRADA\r\n" + 
/*  862 */       "),\r\n" + 
/*  863 */       "\r\n" + 
/*  864 */       "TOT_GERAL_SAIDA AS (\r\n" + 
/*  865 */       "    SELECT \r\n" + 
/*  866 */       "        'TOT GER SAI' AS TIPO\r\n" + 
/*  867 */       "        , ROWNUM + (SELECT DISTINCT CONTADOR FROM SEQ_AFTER_TOT_ENT) AS SEQUENCIA\r\n" + 
/*  868 */       "        , AAA.*\r\n" + 
/*  869 */       "    FROM (\r\n" + 
/*  870 */       "            SELECT\r\n" + 
/*  871 */       "                A.CODEMP_ANP\r\n" + 
/*  872 */       "                , A.MESREFERENCIA\r\n" + 
/*  873 */       "                , '4012998' AS CODOPER\r\n" + 
/*  874 */       "                , A.DESCRICAO\r\n" + 
/*  875 */       "                , A.TOTALIZADORGERAL\r\n" + 
/*  876 */       "                , A.CODINSTALACAO1\r\n" + 
/*  877 */       "                , '0' AS CODINSTALACAO2\r\n" + 
/*  878 */       "                , CODPROD_ANP\r\n" + 
/*  879 */       "                , TO_CHAR(-SUM(A.QTD_PROD_UN_ANP)) AS QTD_PROD_UN_ANP \r\n" + 
/*  880 */       "                , '0' AS QTD_PROD_OPERADO_KG\r\n" + 
/*  881 */       "                , '0' AS COD_MODAL_MOVIMENTACAO\r\n" + 
/*  882 */       "                , '0' AS COD_OLEODUTO_TRANSPORTE\r\n" + 
/*  883 */       "                , '0' AS CNPJ_PARC\r\n" + 
/*  884 */       "                , '0' AS CODCID_ANP\r\n" + 
/*  885 */       "                , '0' AS COD_ATIV_PARC_ANP\r\n" + 
/*  886 */       "                , '0' AS COD_PAIS_ANP\r\n" + 
/*  887 */       "                , '0' AS NUM_LICEN_IMPORT\r\n" + 
/*  888 */       "                , '0' AS NUM_DECLARACAO_IMPORT\r\n" + 
/*  889 */       "                , '0' AS NUMNF\r\n" + 
/*  890 */       "                , '0' AS SERIE_NF\r\n" + 
/*  891 */       "                , '0' AS DAT_OPER_COMERCIAL\r\n" + 
/*  892 */       "                , '0' AS COD_TIP_TARIFA_SERVICO\r\n" + 
/*  893 */       "                , '0' AS CARACTERISTICA\r\n" + 
/*  894 */       "                , '0' AS METODO\r\n" + 
/*  895 */       "                , '0' AS MODALIDADE_FRETE\r\n" + 
/*  896 */       "                , '0' AS NUM_DOC_QUALIDADE\r\n" + 
/*  897 */       "                , '0' AS COD_OPER_RESULTANTE\r\n" + 
/*  898 */       "                , '0' AS VLRUNIT_NF\r\n" + 
/*  899 */       "                , '0' AS RECEPIENTE_GLP\r\n" + 
/*  900 */       "                , '0' AS CHAVE_NFE_CTE\r\n" + 
/*  901 */       "                , '0' AS CONTADOR \r\n" + 
/*  902 */       "            FROM BASE A\r\n" + 
/*  903 */       "\t\t\tWHERE SUBSTR(A.CODOPERACAO,1,4) NOT LIKE '3%'\r\n" + 
/*  904 */       "            AND SUBSTR(A.CODOPERACAO,1,4) NOT LIKE '4030%'\r\n" + 
/*  905 */       "            AND A.TOTALIZADORGERAL = 'S'\r\n" + 
/*  906 */       "            GROUP BY A.CODEMP_ANP, A.MESREFERENCIA, A.CODINSTALACAO1, CODPROD_ANP, A.DESCRICAO, A.TOTALIZADORGERAL\r\n" + 
/*  907 */       "    ) AAA\r\n" + 
/*  908 */       "),\r\n" + 
/*  909 */       "\r\n" + 
/*  910 */       "TOT_GERAL_SAIDA_POSITIVO AS (\r\n" + 
/*  911 */       "    SELECT \r\n" + 
/*  912 */       "        'TOT GER SAI' AS TIPO\r\n" + 
/*  913 */       "        , ROWNUM + (SELECT DISTINCT CONTADOR FROM SEQ_AFTER_TOT_ENT) AS SEQUENCIA\r\n" + 
/*  914 */       "        , AAA.*\r\n" + 
/*  915 */       "    FROM (\r\n" + 
/*  916 */       "            SELECT\r\n" + 
/*  917 */       "                A.CODEMP_ANP\r\n" + 
/*  918 */       "                , A.MESREFERENCIA\r\n" + 
/*  919 */       "                , '4012998' AS CODOPER\r\n" + 
/*  920 */       "                , 'TOT GER SAI' AS DESCRICAO\r\n" + 
/*  921 */       "                , A.TOTALIZADORGERAL\r\n" + 
/*  922 */       "                , A.CODINSTALACAO1\r\n" + 
/*  923 */       "                , '0' AS CODINSTALACAO2\r\n" + 
/*  924 */       "                , CODPROD_ANP\r\n" + 
/*  925 */       "                , TO_CHAR(SUM(A.QTD_PROD_UN_ANP)) AS QTD_PROD_UN_ANP \r\n" + 
/*  926 */       "                , '0' AS QTD_PROD_OPERADO_KG\r\n" + 
/*  927 */       "                , '0' AS COD_MODAL_MOVIMENTACAO\r\n" + 
/*  928 */       "                , '0' AS COD_OLEODUTO_TRANSPORTE\r\n" + 
/*  929 */       "                , '0' AS CNPJ_PARC\r\n" + 
/*  930 */       "                , '0' AS CODCID_ANP\r\n" + 
/*  931 */       "                , '0' AS COD_ATIV_PARC_ANP\r\n" + 
/*  932 */       "                , '0' AS COD_PAIS_ANP\r\n" + 
/*  933 */       "                , '0' AS NUM_LICEN_IMPORT\r\n" + 
/*  934 */       "                , '0' AS NUM_DECLARACAO_IMPORT\r\n" + 
/*  935 */       "                , '0' AS NUMNF\r\n" + 
/*  936 */       "                , '0' AS SERIE_NF\r\n" + 
/*  937 */       "                , '0' AS DAT_OPER_COMERCIAL\r\n" + 
/*  938 */       "                , '0' AS COD_TIP_TARIFA_SERVICO\r\n" + 
/*  939 */       "                , '0' AS CARACTERISTICA\r\n" + 
/*  940 */       "                , '0' AS METODO\r\n" + 
/*  941 */       "                , '0' AS MODALIDADE_FRETE\r\n" + 
/*  942 */       "                , '0' AS NUM_DOC_QUALIDADE\r\n" + 
/*  943 */       "                , '0' AS COD_OPER_RESULTANTE\r\n" + 
/*  944 */       "                , '0' AS VLRUNIT_NF\r\n" + 
/*  945 */       "                , '0' AS RECEPIENTE_GLP\r\n" + 
/*  946 */       "                , '0' AS CHAVE_NFE_CTE\r\n" + 
/*  947 */       "                , '0' AS CONTADOR \r\n" + 
/*  948 */       "            FROM BASE A\r\n" + 
/*  949 */       "\t\t\tWHERE SUBSTR(A.CODOPERACAO,1,4) NOT LIKE '3%'\r\n" + 
/*  950 */       "            AND SUBSTR(A.CODOPERACAO,1,4) NOT LIKE '4030%'\r\n" + 
/*  951 */       "            AND A.TOTALIZADORGERAL = 'S'\r\n" + 
/*  952 */       "            GROUP BY A.CODEMP_ANP, A.MESREFERENCIA, A.CODINSTALACAO1, CODPROD_ANP, A.TOTALIZADORGERAL\r\n" + 
/*  953 */       "    ) AAA\r\n" + 
/*  954 */       "),\r\n" + 
/*  955 */       "\r\n" + 
/*  956 */       "TOT_GERAL_SAIDA_DET AS (\r\n" + 
/*  957 */       "    SELECT \r\n" + 
/*  958 */       "        'TOT GER SAI' AS TIPO\r\n" + 
/*  959 */       "        , ROWNUM + (SELECT DISTINCT CONTADOR FROM SEQ_AFTER_TOT_ENT) AS SEQUENCIA\r\n" + 
/*  960 */       "        , AAA.*\r\n" + 
/*  961 */       "    FROM (\r\n" + 
/*  962 */       "            SELECT\r\n" + 
/*  963 */       "                A.CODEMP_ANP\r\n" + 
/*  964 */       "                , A.MESREFERENCIA\r\n" + 
/*  965 */       "                , A.CODOPERACAO\r\n" + 
/*  966 */       "                , A.DESCRICAO\r\n" + 
/*  967 */       "                , A.TOTALIZADORGERAL\r\n" + 
/*  968 */       "                , A.CODINSTALACAO1\r\n" + 
/*  969 */       "                , '0' AS CODINSTALACAO2\r\n" + 
/*  970 */       "                , CODPROD_ANP\r\n" + 
/*  971 */       "                , TO_CHAR(-SUM(A.QTD_PROD_UN_ANP)) AS QTD_PROD_UN_ANP \r\n" + 
/*  972 */       "                , '0' AS QTD_PROD_OPERADO_KG\r\n" + 
/*  973 */       "                , '0' AS COD_MODAL_MOVIMENTACAO\r\n" + 
/*  974 */       "                , '0' AS COD_OLEODUTO_TRANSPORTE\r\n" + 
/*  975 */       "                , '0' AS CNPJ_PARC\r\n" + 
/*  976 */       "                , '0' AS CODCID_ANP\r\n" + 
/*  977 */       "                , '0' AS COD_ATIV_PARC_ANP\r\n" + 
/*  978 */       "                , '0' AS COD_PAIS_ANP\r\n" + 
/*  979 */       "                , '0' AS NUM_LICEN_IMPORT\r\n" + 
/*  980 */       "                , '0' AS NUM_DECLARACAO_IMPORT\r\n" + 
/*  981 */       "                , '0' AS NUMNF\r\n" + 
/*  982 */       "                , '0' AS SERIE_NF\r\n" + 
/*  983 */       "                , '0' AS DAT_OPER_COMERCIAL\r\n" + 
/*  984 */       "                , '0' AS COD_TIP_TARIFA_SERVICO\r\n" + 
/*  985 */       "                , '0' AS CARACTERISTICA\r\n" + 
/*  986 */       "                , '0' AS METODO\r\n" + 
/*  987 */       "                , '0' AS MODALIDADE_FRETE\r\n" + 
/*  988 */       "                , '0' AS NUM_DOC_QUALIDADE\r\n" + 
/*  989 */       "                , '0' AS COD_OPER_RESULTANTE\r\n" + 
/*  990 */       "                , '0' AS VLRUNIT_NF\r\n" + 
/*  991 */       "                , '0' AS RECEPIENTE_GLP\r\n" + 
/*  992 */       "                , '0' AS CHAVE_NFE_CTE\r\n" + 
/*  993 */       "                , '0' AS CONTADOR \r\n" + 
/*  994 */       "            FROM BASE A\r\n" + 
/*  995 */       "\t\t\tWHERE SUBSTR(A.CODOPERACAO,1,4) NOT LIKE '3%'\r\n" + 
/*  996 */       "            AND SUBSTR(A.CODOPERACAO,1,4) NOT LIKE '4030%'\r\n" + 
/*  997 */       "            AND A.TOTALIZADORGERAL = 'S'\r\n" + 
/*  998 */       "            GROUP BY A.CODEMP_ANP, A.MESREFERENCIA, A.CODINSTALACAO1, CODPROD_ANP, A.DESCRICAO, A.TOTALIZADORGERAL, A.CODOPERACAO\r\n" + 
/*  999 */       "    ) AAA\r\n" + 
/* 1000 */       "),\r\n" + 
/* 1001 */       "\r\n" + 
/* 1002 */       "SEQ_AFTER_TOT_SAI AS (\r\n" + 
/* 1003 */       "    SELECT MAX(SEQUENCIA) AS CONTADOR FROM TOT_GERAL_SAIDA_POSITIVO\r\n" + 
/* 1004 */       "), \r\n" + 
/* 1005 */       "\r\n" + 
/* 1006 */       "ESTOQUE_FINAL AS (\r\n" + 
/* 1007 */       "    SELECT\r\n" + 
/* 1008 */       "        'ESTOQUE FINAL' AS TIPO\r\n" + 
/* 1009 */       "        , ROWNUM + (SELECT DISTINCT CONTADOR FROM SEQ_AFTER_TOT_SAI) AS SEQUENCIA\r\n" + 
/* 1010 */       "        , B.*\r\n" + 
/* 1011 */       "    FROM (\r\n" + 
/* 1012 */       "        SELECT A.CODEMP_ANP\r\n" + 
/* 1013 */       "            , A.MESREFERENCIA\r\n" + 
/* 1014 */       "            , '3020003' AS CODOPER\r\n" + 
/* 1015 */       "            , 'ESTOQUE FINAL PRÓPRIO' DESCRICAO\r\n" + 
/* 1016 */       "            , 'N' AS TOTALIZADORGERAL\r\n" + 
/* 1017 */       "            , A.CODINSTALACAO1\r\n" + 
/* 1018 */       "            , '0' AS CODINSTALACAO2\r\n" + 
/* 1019 */       "            , A.CODPROD_ANP\r\n" + 
/* 1020 */       "            , TO_CHAR(SUM(A.QTD_PROD_UN_ANP)) AS QTD_PROD_UN_ANP \r\n" + 
/* 1021 */       "            , '0' AS QTD_PROD_OPERADO_KG\r\n" + 
/* 1022 */       "            , '0' AS COD_MODAL_MOVIMENTACAO\r\n" + 
/* 1023 */       "            , '0' AS COD_OLEODUTO_TRANSPORTE\r\n" + 
/* 1024 */       "            , '0' AS CNPJ_PARC\r\n" + 
/* 1025 */       "            , '0' AS CODCID_ANP\r\n" + 
/* 1026 */       "            , '0' AS COD_ATIV_PARC_ANP\r\n" + 
/* 1027 */       "            , '0' AS COD_PAIS_ANP\r\n" + 
/* 1028 */       "            , '0' AS NUM_LICEN_IMPORT\r\n" + 
/* 1029 */       "            , '0' AS NUM_DECLARACAO_IMPORT\r\n" + 
/* 1030 */       "            , '0' AS NUMNF\r\n" + 
/* 1031 */       "            , '0' AS SERIE_NF\r\n" + 
/* 1032 */       "            , '0' AS DAT_OPER_COMERCIAL\r\n" + 
/* 1033 */       "            , '0' AS COD_TIP_TARIFA_SERVICO\r\n" + 
/* 1034 */       "            , '0' AS CARACTERISTICA\r\n" + 
/* 1035 */       "            , '0' AS METODO\r\n" + 
/* 1036 */       "            , '0' AS MODALIDADE_FRETE\r\n" + 
/* 1037 */       "            , '0' AS NUM_DOC_QUALIDADE\r\n" + 
/* 1038 */       "            , '0' AS COD_OPER_RESULTANTE\r\n" + 
/* 1039 */       "            , '0' AS VLRUNIT_NF\r\n" + 
/* 1040 */       "            , '0' AS RECEPIENTE_GLP\r\n" + 
/* 1041 */       "            , '0' AS CHAVE_NFE_CTE\r\n" + 
/* 1042 */       "            , '0' AS CONTADOR\r\n" + 
/* 1043 */       "        FROM (\r\n" + 
/* 1044 */       "                            SELECT * FROM ESTOQUE_INICIAL\r\n" + 
/* 1045 */       "                UNION ALL   SELECT * FROM TOT_GERAL_ENTRADA\r\n" + 
/* 1046 */       "                UNION ALL   SELECT * FROM TOT_GERAL_SAIDA\r\n" + 
/* 1047 */       "            ) A\r\n" + 
/* 1048 */       "            GROUP BY A.CODEMP_ANP, A.MESREFERENCIA, A.CODINSTALACAO1, A.CODPROD_ANP\r\n" + 
/* 1049 */       "        ) B\r\n" + 
/* 1050 */       "),\r\n" + 
/* 1051 */       "\r\n" + 
/* 1052 */       "ESTOQUE_FINAL_DETALHADO AS (\r\n" + 
/* 1053 */       "    SELECT\r\n" + 
/* 1054 */       "        --'ESTOQUE FINAL' AS TIPO\r\n" + 
/* 1055 */       "         ROWNUM + (SELECT DISTINCT CONTADOR FROM SEQ_AFTER_TOT_SAI) AS SEQUENCIA\r\n" + 
/* 1056 */       "        , B.*\r\n" + 
/* 1057 */       "    FROM (\r\n" + 
/* 1058 */       "        SELECT A.TIPO\r\n" + 
/* 1059 */       "            , A.CODEMP_ANP\r\n" + 
/* 1060 */       "            , A.MESREFERENCIA\r\n" + 
/* 1061 */       "            , A.CODOPERACAO\r\n" + 
/* 1062 */       "            , A.DESCRICAO\r\n" + 
/* 1063 */       "            , A.TOTALIZADORGERAL\r\n" + 
/* 1064 */       "            , A.CODINSTALACAO1\r\n" + 
/* 1065 */       "            , '0' AS CODINSTALACAO2\r\n" + 
/* 1066 */       "            , A.CODPROD_ANP\r\n" + 
/* 1067 */       "            , A.QTD_PROD_UN_ANP AS QTD_PROD_UN_ANP \r\n" + 
/* 1068 */       "            , '0' AS QTD_PROD_OPERADO_KG\r\n" + 
/* 1069 */       "            , '0' AS COD_MODAL_MOVIMENTACAO\r\n" + 
/* 1070 */       "            , '0' AS COD_OLEODUTO_TRANSPORTE\r\n" + 
/* 1071 */       "            , '0' AS CNPJ_PARC\r\n" + 
/* 1072 */       "            , '0' AS CODCID_ANP\r\n" + 
/* 1073 */       "            , '0' AS COD_ATIV_PARC_ANP\r\n" + 
/* 1074 */       "            , '0' AS COD_PAIS_ANP\r\n" + 
/* 1075 */       "            , '0' AS NUM_LICEN_IMPORT\r\n" + 
/* 1076 */       "            , '0' AS NUM_DECLARACAO_IMPORT\r\n" + 
/* 1077 */       "            , '0' AS NUMNF\r\n" + 
/* 1078 */       "            , '0' AS SERIE_NF\r\n" + 
/* 1079 */       "            , '0' AS DAT_OPER_COMERCIAL\r\n" + 
/* 1080 */       "            , '0' AS COD_TIP_TARIFA_SERVICO\r\n" + 
/* 1081 */       "            , '0' AS CARACTERISTICA\r\n" + 
/* 1082 */       "            , '0' AS METODO\r\n" + 
/* 1083 */       "            , '0' AS MODALIDADE_FRETE\r\n" + 
/* 1084 */       "            , '0' AS NUM_DOC_QUALIDADE\r\n" + 
/* 1085 */       "            , '0' AS COD_OPER_RESULTANTE\r\n" + 
/* 1086 */       "            , '0' AS VLRUNIT_NF\r\n" + 
/* 1087 */       "            , '0' AS RECEPIENTE_GLP\r\n" + 
/* 1088 */       "            , '0' AS CHAVE_NFE_CTE\r\n" + 
/* 1089 */       "            , '0' AS CONTADOR\r\n" + 
/* 1090 */       "        FROM (\r\n" + 
/* 1091 */       "                            SELECT * FROM ESTOQUE_INICIAL\r\n" + 
/* 1092 */       "                UNION ALL   SELECT * FROM TOT_GERAL_ENTRADA_DET\r\n" + 
/* 1093 */       "                UNION ALL   SELECT * FROM TOT_GERAL_SAIDA_DET\r\n" + 
/* 1094 */       "            ) A\r\n" + 
/* 1095 */       "            \r\n" + 
/* 1096 */       "        ) B\r\n" + 
/* 1097 */       ")\r\n" + 
/* 1098 */       "\r\n" + 
/* 1099 */       "          SELECT B.*    FROM BASE B\r\n" + 
/* 1100 */       "UNION ALL SELECT TT.*   FROM TOTALIZADOR_OPERACAO TT\r\n" + 
/* 1101 */       "UNION ALL SELECT TTGE.* FROM TOT_GERAL_ENTRADA TTGE\r\n" + 
/* 1102 */       "UNION ALL SELECT TTGS.* FROM TOT_GERAL_SAIDA_POSITIVO TTGS\r\n" + 
/* 1103 */       "UNION ALL SELECT EF.*   FROM ESTOQUE_FINAL EF\r\n" + 
/* 1104 */       "ORDER BY 2";
/*      */     
/* 1106 */     String sqlOld = "WITH BASE AS (\r\nSELECT \r\n    CODDETSIMSIMP AS SEQUENCIA\r\n    , COALESCE(CODEMP_ANP,'0') AS CODEMP_ANP\r\n    , COALESCE(MESREFERENCIA,'0') AS MESREFERENCIA\r\n    , COALESCE((SELECT OPER.CODOPER_ANP FROM AD_OPERACAOANP OPER WHERE OPER.CODOPER = A.CODOPER),'0') AS CODOPERACAO\r\n    , COALESCE(CODINSTALACAO1,'0') AS CODINSTALACAO1\r\n    , COALESCE(CODINSTALACAO2,'0') AS CODINSTALACAO2\r\n    , COALESCE(CODPROD_ANP,'0') AS CODPROD_ANP\r\n    , COALESCE(QTD_PROD_UN_ANP, '0') AS QTD_PROD_UN_ANP \r\n    , COALESCE(QTD_PROD_OPERADO_KG,'0') AS QTD_PROD_OPERADO_KG\r\n    , COALESCE(COD_MODAL_MOVIMENTACAO,'0') AS COD_MODAL_MOVIMENTACAO\r\n    , COALESCE(COD_OLEODUTO_TRANSPORTE,'0') AS COD_OLEODUTO_TRANSPORTE\r\n    , COALESCE(CNPJ_PARC,'0') AS CNPJ_PARC\r\n    , COALESCE(CODCID_ANP,'0') AS CODCID_ANP\r\n    , COALESCE(COD_ATIV_PARC_ANP,'0') AS COD_ATIV_PARC_ANP\r\n    , COALESCE(CODPAIS_ANP,'0') AS CODPAIS_ANP\r\n    , COALESCE(NUM_LICEN_IMPORT,'0') AS NUM_LICEN_IMPORT\r\n    , COALESCE(NUM_DECLARACAO_IMPORT,'0') AS NUM_DECLARACAO_IMPORT\r\n    , COALESCE(NUMNF,'0') AS NUMNF\r\n    , COALESCE(SERIE_NF,'0') AS SERIE_NF\r\n    , COALESCE(DAT_OPER_COMERCIAL,'0') AS DAT_OPER_COMERCIAL\r\n    , COALESCE(COD_TIP_TARIFA_SERVICO,'0') AS COD_TIP_TARIFA_SERVICO\r\n    , COALESCE(CARACTERISTICA,'0') AS CARACTERISTICA\r\n    , COALESCE(METODO,'0') AS METODO\r\n    , COALESCE(MODALIDADE_FRETE,'0') AS MODALIDADE_FRETE\r\n    , COALESCE(NUM_DOC_QUALIDADE,'0') AS NUM_DOC_QUALIDADE\r\n    , COALESCE(COD_OPER_RESULTANTE,'0') AS COD_OPER_RESULTANTE\r\n    , COALESCE(VLRUNIT_NF,'0') AS VLRUNIT_NF\r\n    , COALESCE(RECEPIENTE_GLP,'0') AS RECEPIENTE_GLP\r\n    , COALESCE(CHAVE_NFE_CTE,'0') AS CHAVE_NFE_CTE\r\n    , COALESCE((SELECT TO_CHAR(COUNT(*)) FROM AD_DETSIMULARSIMP DET WHERE DET.CODSIMSIM = A.CODSIMSIM), '0') AS CONTADOR\r\nFROM AD_DETSIMULARSIMP A\r\nWHERE CODSIMSIM = " + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1139 */       codSimulaSimp + " \r\n" + 
/* 1140 */       "ORDER BY CODDETSIMSIMP\r\n" + 
/* 1141 */       "),\r\n" + 
/* 1142 */       "\r\n" + 
/* 1143 */       "ESTOQUE_INICIAL AS (\r\n" + 
/* 1144 */       "    SELECT * FROM BASE B WHERE B.CODOPERACAO = '3010003'\r\n" + 
/* 1145 */       "),\r\n" + 
/* 1146 */       "\r\n" + 
/* 1147 */       "TOT AS (\r\n" + 
/* 1148 */       "    SELECT \r\n" + 
/* 1149 */       "        ROWNUM + (SELECT DISTINCT CONTADOR FROM BASE) AS SEQUENCIA\r\n" + 
/* 1150 */       "        , AAA.*\r\n" + 
/* 1151 */       "    FROM (\r\n" + 
/* 1152 */       "            SELECT\r\n" + 
/* 1153 */       "                A.CODEMP_ANP\r\n" + 
/* 1154 */       "                , A.MESREFERENCIA\r\n" + 
/* 1155 */       "                , SUBSTR(OPER.CODOPER_ANP,1,4) || '998' AS CODOPER\r\n" + 
/* 1156 */       "                , A.CODINSTALACAO1\r\n" + 
/* 1157 */       "                , '0' AS CODINSTALACAO2\r\n" + 
/* 1158 */       "                , CODPROD_ANP\r\n" + 
/* 1159 */       "                , TO_CHAR(SUM(A.QTD_PROD_UN_ANP)) AS QTD_PROD_UN_ANP \r\n" + 
/* 1160 */       "                , '0' AS QTD_PROD_OPERADO_KG\r\n" + 
/* 1161 */       "                , '0' AS COD_MODAL_MOVIMENTACAO\r\n" + 
/* 1162 */       "                , '0' AS COD_OLEODUTO_TRANSPORTE\r\n" + 
/* 1163 */       "                , '0' AS CNPJ_PARC\r\n" + 
/* 1164 */       "                , '0' AS CODCID_ANP\r\n" + 
/* 1165 */       "                , '0' AS COD_ATIV_PARC_ANP\r\n" + 
/* 1166 */       "                , '0' AS COD_PAIS_ANP\r\n" + 
/* 1167 */       "                , '0' AS NUM_LICEN_IMPORT\r\n" + 
/* 1168 */       "                , '0' AS NUM_DECLARACAO_IMPORT\r\n" + 
/* 1169 */       "                , '0' AS NUMNF\r\n" + 
/* 1170 */       "                , '0' AS SERIE_NF\r\n" + 
/* 1171 */       "                , '0' AS DAT_OPER_COMERCIAL\r\n" + 
/* 1172 */       "                , '0' AS COD_TIP_TARIFA_SERVICO\r\n" + 
/* 1173 */       "                , '0' AS CARACTERISTICA\r\n" + 
/* 1174 */       "                , '0' AS METODO\r\n" + 
/* 1175 */       "                , '0' AS MODALIDADE_FRETE\r\n" + 
/* 1176 */       "                , '0' AS NUM_DOC_QUALIDADE\r\n" + 
/* 1177 */       "                , '0' AS COD_OPER_RESULTANTE\r\n" + 
/* 1178 */       "                , '0' AS VLRUNIT_NF\r\n" + 
/* 1179 */       "                , '0' AS RECEPIENTE_GLP\r\n" + 
/* 1180 */       "                , '0' AS CHAVE_NFE_CTE\r\n" + 
/* 1181 */       "                , '0' AS CONTADOR \r\n" + 
/* 1182 */       "            FROM AD_DETSIMULARSIMP A\r\n" + 
/* 1183 */       "            JOIN AD_OPERACAOANP OPER ON A.CODOPER = OPER.CODOPER\r\n" + 
/* 1184 */       "            WHERE CODSIMSIM = " + codSimulaSimp + " \r\n" + 
/* 1185 */       "            AND SUBSTR(OPER.CODOPER_ANP,1,4) NOT LIKE '3%'\r\n" + 
/* 1186 */       "            AND SUBSTR(OPER.CODOPER_ANP,1,4) NOT LIKE '4030%'\r\n" + 
/* 1187 */       "            GROUP BY A.CODEMP_ANP\r\n" + 
/* 1188 */       "                , A.MESREFERENCIA\r\n" + 
/* 1189 */       "                , A.CODINSTALACAO1\r\n" + 
/* 1190 */       "                , CODPROD_ANP\r\n" + 
/* 1191 */       "                , SUBSTR(OPER.CODOPER_ANP,1,4)\r\n" + 
/* 1192 */       "            ORDER BY SUBSTR(OPER.CODOPER_ANP,1,4)\r\n" + 
/* 1193 */       "        ) AAA\r\n" + 
/* 1194 */       "),\r\n" + 
/* 1195 */       "SEQ_AFTER_TOT AS (SELECT MAX(SEQUENCIA) AS CONTADOR FROM TOT), \r\n" + 
/* 1196 */       "\r\n" + 
/* 1197 */       "TOT_GERAL_ENTRADA AS (\r\n" + 
/* 1198 */       "SELECT \r\n" + 
/* 1199 */       "        ROWNUM + (SELECT DISTINCT CONTADOR FROM SEQ_AFTER_TOT) AS SEQUENCIA\r\n" + 
/* 1200 */       "        , AAA.*\r\n" + 
/* 1201 */       "    FROM (\r\n" + 
/* 1202 */       "            SELECT\r\n" + 
/* 1203 */       "                A.CODEMP_ANP\r\n" + 
/* 1204 */       "                , A.MESREFERENCIA\r\n" + 
/* 1205 */       "                , '4011998' AS CODOPER\r\n" + 
/* 1206 */       "                , A.CODINSTALACAO1\r\n" + 
/* 1207 */       "                , '0' AS CODINSTALACAO2\r\n" + 
/* 1208 */       "                , CODPROD_ANP\r\n" + 
/* 1209 */       "                , TO_CHAR(SUM(A.QTD_PROD_UN_ANP)) AS QTD_PROD_UN_ANP \r\n" + 
/* 1210 */       "                , '0' AS QTD_PROD_OPERADO_KG\r\n" + 
/* 1211 */       "                , '0' AS COD_MODAL_MOVIMENTACAO\r\n" + 
/* 1212 */       "                , '0' AS COD_OLEODUTO_TRANSPORTE\r\n" + 
/* 1213 */       "                , '0' AS CNPJ_PARC\r\n" + 
/* 1214 */       "                , '0' AS CODCID_ANP\r\n" + 
/* 1215 */       "                , '0' AS COD_ATIV_PARC_ANP\r\n" + 
/* 1216 */       "                , '0' AS COD_PAIS_ANP\r\n" + 
/* 1217 */       "                , '0' AS NUM_LICEN_IMPORT\r\n" + 
/* 1218 */       "                , '0' AS NUM_DECLARACAO_IMPORT\r\n" + 
/* 1219 */       "                , '0' AS NUMNF\r\n" + 
/* 1220 */       "                , '0' AS SERIE_NF\r\n" + 
/* 1221 */       "                , '0' AS DAT_OPER_COMERCIAL\r\n" + 
/* 1222 */       "                , '0' AS COD_TIP_TARIFA_SERVICO\r\n" + 
/* 1223 */       "                , '0' AS CARACTERISTICA\r\n" + 
/* 1224 */       "                , '0' AS METODO\r\n" + 
/* 1225 */       "                , '0' AS MODALIDADE_FRETE\r\n" + 
/* 1226 */       "                , '0' AS NUM_DOC_QUALIDADE\r\n" + 
/* 1227 */       "                , '0' AS COD_OPER_RESULTANTE\r\n" + 
/* 1228 */       "                , '0' AS VLRUNIT_NF\r\n" + 
/* 1229 */       "                , '0' AS RECEPIENTE_GLP\r\n" + 
/* 1230 */       "                , '0' AS CHAVE_NFE_CTE\r\n" + 
/* 1231 */       "                , '0' AS CONTADOR \r\n" + 
/* 1232 */       "            FROM AD_DETSIMULARSIMP A\r\n" + 
/* 1233 */       "            JOIN AD_OPERACAOANP OPER ON A.CODOPER = OPER.CODOPER\r\n" + 
/* 1234 */       "\t\t\t   WHERE CODSIMSIM = " + codSimulaSimp + " \r\n" + 
/* 1235 */       "            AND SUBSTR(OPER.CODOPER_ANP,1,4) NOT LIKE '3%'\r\n" + 
/* 1236 */       "            AND SUBSTR(OPER.CODOPER_ANP,1,4) NOT LIKE '4030%'\r\n" + 
/* 1237 */       "            AND OPER.TOTALIZADORGERAL = 'E'\r\n" + 
/* 1238 */       "            GROUP BY A.CODEMP_ANP\r\n" + 
/* 1239 */       "                , A.MESREFERENCIA\r\n" + 
/* 1240 */       "                , A.CODINSTALACAO1\r\n" + 
/* 1241 */       "                , CODPROD_ANP\r\n" + 
/* 1242 */       "    ) AAA\r\n" + 
/* 1243 */       "\r\n" + 
/* 1244 */       "),\r\n" + 
/* 1245 */       "\r\n" + 
/* 1246 */       "SEQ_AFTER_TOT_ENT AS (\r\n" + 
/* 1247 */       "    SELECT MAX(SEQUENCIA) AS CONTADOR FROM TOT_GERAL_ENTRADA\r\n" + 
/* 1248 */       "), \r\n" + 
/* 1249 */       "\r\n" + 
/* 1250 */       "TOT_GERAL_SAIDA AS (\r\n" + 
/* 1251 */       "SELECT \r\n" + 
/* 1252 */       "        ROWNUM + (SELECT DISTINCT CONTADOR FROM SEQ_AFTER_TOT_ENT) AS SEQUENCIA\r\n" + 
/* 1253 */       "        , AAA.*\r\n" + 
/* 1254 */       "    FROM (\r\n" + 
/* 1255 */       "            SELECT\r\n" + 
/* 1256 */       "                A.CODEMP_ANP\r\n" + 
/* 1257 */       "                , A.MESREFERENCIA\r\n" + 
/* 1258 */       "                , '4012998' AS CODOPER\r\n" + 
/* 1259 */       "                , A.CODINSTALACAO1\r\n" + 
/* 1260 */       "                , '0' AS CODINSTALACAO2\r\n" + 
/* 1261 */       "                , CODPROD_ANP\r\n" + 
/* 1262 */       "                , TO_CHAR(SUM(A.QTD_PROD_UN_ANP)) AS QTD_PROD_UN_ANP \r\n" + 
/* 1263 */       "                , '0' AS QTD_PROD_OPERADO_KG\r\n" + 
/* 1264 */       "                , '0' AS COD_MODAL_MOVIMENTACAO\r\n" + 
/* 1265 */       "                , '0' AS COD_OLEODUTO_TRANSPORTE\r\n" + 
/* 1266 */       "                , '0' AS CNPJ_PARC\r\n" + 
/* 1267 */       "                , '0' AS CODCID_ANP\r\n" + 
/* 1268 */       "                , '0' AS COD_ATIV_PARC_ANP\r\n" + 
/* 1269 */       "                , '0' AS COD_PAIS_ANP\r\n" + 
/* 1270 */       "                , '0' AS NUM_LICEN_IMPORT\r\n" + 
/* 1271 */       "                , '0' AS NUM_DECLARACAO_IMPORT\r\n" + 
/* 1272 */       "                , '0' AS NUMNF\r\n" + 
/* 1273 */       "                , '0' AS SERIE_NF\r\n" + 
/* 1274 */       "                , '0' AS DAT_OPER_COMERCIAL\r\n" + 
/* 1275 */       "                , '0' AS COD_TIP_TARIFA_SERVICO\r\n" + 
/* 1276 */       "                , '0' AS CARACTERISTICA\r\n" + 
/* 1277 */       "                , '0' AS METODO\r\n" + 
/* 1278 */       "                , '0' AS MODALIDADE_FRETE\r\n" + 
/* 1279 */       "                , '0' AS NUM_DOC_QUALIDADE\r\n" + 
/* 1280 */       "                , '0' AS COD_OPER_RESULTANTE\r\n" + 
/* 1281 */       "                , '0' AS VLRUNIT_NF\r\n" + 
/* 1282 */       "                , '0' AS RECEPIENTE_GLP\r\n" + 
/* 1283 */       "                , '0' AS CHAVE_NFE_CTE\r\n" + 
/* 1284 */       "                , '0' AS CONTADOR \r\n" + 
/* 1285 */       "            FROM AD_DETSIMULARSIMP A\r\n" + 
/* 1286 */       "            JOIN AD_OPERACAOANP OPER ON A.CODOPER = OPER.CODOPER\r\n" + 
/* 1287 */       " \t\t   WHERE CODSIMSIM = " + codSimulaSimp + " \r\n" + 
/* 1288 */       "            AND SUBSTR(OPER.CODOPER_ANP,1,4) NOT LIKE '3%'\r\n" + 
/* 1289 */       "            AND SUBSTR(OPER.CODOPER_ANP,1,4) NOT LIKE '4030%'\r\n" + 
/* 1290 */       "            AND OPER.TOTALIZADORGERAL = 'S'\r\n" + 
/* 1291 */       "            GROUP BY A.CODEMP_ANP\r\n" + 
/* 1292 */       "                , A.MESREFERENCIA\r\n" + 
/* 1293 */       "                , A.CODINSTALACAO1\r\n" + 
/* 1294 */       "                , CODPROD_ANP\r\n" + 
/* 1295 */       "    ) AAA\r\n" + 
/* 1296 */       "\r\n" + 
/* 1297 */       "),\r\n" + 
/* 1298 */       "\r\n" + 
/* 1299 */       "SEQ_AFTER_TOT_SAI AS (\r\n" + 
/* 1300 */       "    SELECT MAX(SEQUENCIA) AS CONTADOR FROM TOT_GERAL_SAIDA\r\n" + 
/* 1301 */       "),\r\n" + 
/* 1302 */       "\r\n" + 
/* 1303 */       "ESTOQUE_FINAL AS (\r\n" + 
/* 1304 */       "    SELECT\r\n" + 
/* 1305 */       "        ROWNUM + (SELECT DISTINCT CONTADOR FROM SEQ_AFTER_TOT_SAI) AS SEQUENCIA\r\n" + 
/* 1306 */       "        , BBB.*\r\n" + 
/* 1307 */       "    FROM (\r\n" + 
/* 1308 */       "            SELECT\r\n" + 
/* 1309 */       "                AAA.CODEMP_ANP\r\n" + 
/* 1310 */       "                , AAA.MESREFERENCIA\r\n" + 
/* 1311 */       "                , '4030001' AS CODOPERACAO\r\n" + 
/* 1312 */       "                , AAA.CODINSTALACAO1\r\n" + 
/* 1313 */       "                , AAA.CODINSTALACAO2\r\n" + 
/* 1314 */       "                , AAA.CODPROD_ANP\r\n" + 
/* 1315 */       "                , TO_CHAR(SUM(AAA.QTD_PROD_UN_ANP)) AS QTD_PROD_UN_ANP\r\n" + 
/* 1316 */       "                , TO_CHAR(SUM(AAA.QTD_PROD_OPERADO_KG)) AS QTD_PROD_OPERADO_KG\r\n" + 
/* 1317 */       "                , AAA.COD_MODAL_MOVIMENTACAO\r\n" + 
/* 1318 */       "                , AAA.COD_OLEODUTO_TRANSPORTE\r\n" + 
/* 1319 */       "                , AAA.CNPJ_PARC\r\n" + 
/* 1320 */       "                , AAA.CODCID_ANP\r\n" + 
/* 1321 */       "                , AAA.COD_ATIV_PARC_ANP\r\n" + 
/* 1322 */       "                , AAA.CODPAIS_ANP\r\n" + 
/* 1323 */       "                , AAA.NUM_LICEN_IMPORT\r\n" + 
/* 1324 */       "                , AAA.NUM_DECLARACAO_IMPORT\r\n" + 
/* 1325 */       "                , AAA.NUMNF\r\n" + 
/* 1326 */       "                , AAA.SERIE_NF\r\n" + 
/* 1327 */       "                , AAA.DAT_OPER_COMERCIAL\r\n" + 
/* 1328 */       "                , AAA.COD_TIP_TARIFA_SERVICO\r\n" + 
/* 1329 */       "                , AAA.CARACTERISTICA\r\n" + 
/* 1330 */       "                , AAA.METODO\r\n" + 
/* 1331 */       "                , AAA.MODALIDADE_FRETE\r\n" + 
/* 1332 */       "                , AAA.NUM_DOC_QUALIDADE\r\n" + 
/* 1333 */       "                , AAA.COD_OPER_RESULTANTE\r\n" + 
/* 1334 */       "                , AAA.VLRUNIT_NF\r\n" + 
/* 1335 */       "                , AAA.RECEPIENTE_GLP\r\n" + 
/* 1336 */       "                , AAA.CHAVE_NFE_CTE\r\n" + 
/* 1337 */       "                , '0' AS CONTADOR\r\n" + 
/* 1338 */       "            FROM (\r\n" + 
/* 1339 */       "                    SELECT \r\n" + 
/* 1340 */       "                        SEQUENCIA\r\n" + 
/* 1341 */       "                        , CODEMP_ANP\r\n" + 
/* 1342 */       "                        , MESREFERENCIA\r\n" + 
/* 1343 */       "                        , CODOPERACAO\r\n" + 
/* 1344 */       "                        , CODINSTALACAO1\r\n" + 
/* 1345 */       "                        , CODINSTALACAO2\r\n" + 
/* 1346 */       "                        , CODPROD_ANP\r\n" + 
/* 1347 */       "                        , TO_NUMBER(QTD_PROD_UN_ANP) QTD_PROD_UN_ANP\r\n" + 
/* 1348 */       "                        , TO_NUMBER(QTD_PROD_OPERADO_KG) AS QTD_PROD_OPERADO_KG\r\n" + 
/* 1349 */       "                        , '0' AS COD_MODAL_MOVIMENTACAO\r\n" + 
/* 1350 */       "                        , COD_OLEODUTO_TRANSPORTE\r\n" + 
/* 1351 */       "                        , CNPJ_PARC\r\n" + 
/* 1352 */       "                        , CODCID_ANP\r\n" + 
/* 1353 */       "                        , COD_ATIV_PARC_ANP\r\n" + 
/* 1354 */       "                        , CODPAIS_ANP\r\n" + 
/* 1355 */       "                        , NUM_LICEN_IMPORT\r\n" + 
/* 1356 */       "                        , NUM_DECLARACAO_IMPORT\r\n" + 
/* 1357 */       "                        , NUMNF\r\n" + 
/* 1358 */       "                        , SERIE_NF\r\n" + 
/* 1359 */       "                        , DAT_OPER_COMERCIAL\r\n" + 
/* 1360 */       "                        , COD_TIP_TARIFA_SERVICO\r\n" + 
/* 1361 */       "                        , CARACTERISTICA\r\n" + 
/* 1362 */       "                        , METODO\r\n" + 
/* 1363 */       "                        , MODALIDADE_FRETE\r\n" + 
/* 1364 */       "                        , NUM_DOC_QUALIDADE\r\n" + 
/* 1365 */       "                        , COD_OPER_RESULTANTE\r\n" + 
/* 1366 */       "                        , VLRUNIT_NF\r\n" + 
/* 1367 */       "                        , RECEPIENTE_GLP\r\n" + 
/* 1368 */       "                        , CHAVE_NFE_CTE\r\n" + 
/* 1369 */       "                    FROM ESTOQUE_INICIAL\r\n" + 
/* 1370 */       "                    UNION ALL \r\n" + 
/* 1371 */       "                    SELECT\r\n" + 
/* 1372 */       "                        SEQUENCIA\r\n" + 
/* 1373 */       "                        , CODEMP_ANP\r\n" + 
/* 1374 */       "                        , MESREFERENCIA\r\n" + 
/* 1375 */       "                        , CODOPER\r\n" + 
/* 1376 */       "                        , CODINSTALACAO1\r\n" + 
/* 1377 */       "                        , CODINSTALACAO2\r\n" + 
/* 1378 */       "                        , CODPROD_ANP\r\n" + 
/* 1379 */       "                        , TO_NUMBER(QTD_PROD_UN_ANP) AS QTD_PROD_UN_ANP\r\n" + 
/* 1380 */       "                        , TO_NUMBER(QTD_PROD_OPERADO_KG) AS QTD_PROD_OPERADO_KG\r\n" + 
/* 1381 */       "                        , '0' AS COD_MODAL_MOVIMENTACAO\r\n" + 
/* 1382 */       "                        , COD_OLEODUTO_TRANSPORTE\r\n" + 
/* 1383 */       "                        , CNPJ_PARC\r\n" + 
/* 1384 */       "                        , CODCID_ANP\r\n" + 
/* 1385 */       "                        , COD_ATIV_PARC_ANP\r\n" + 
/* 1386 */       "                        , COD_PAIS_ANP\r\n" + 
/* 1387 */       "                        , NUM_LICEN_IMPORT\r\n" + 
/* 1388 */       "                        , NUM_DECLARACAO_IMPORT\r\n" + 
/* 1389 */       "                        , NUMNF\r\n" + 
/* 1390 */       "                        , SERIE_NF\r\n" + 
/* 1391 */       "                        , DAT_OPER_COMERCIAL\r\n" + 
/* 1392 */       "                        , COD_TIP_TARIFA_SERVICO\r\n" + 
/* 1393 */       "                        , CARACTERISTICA\r\n" + 
/* 1394 */       "                        , METODO\r\n" + 
/* 1395 */       "                        , MODALIDADE_FRETE\r\n" + 
/* 1396 */       "                        , NUM_DOC_QUALIDADE\r\n" + 
/* 1397 */       "                        , COD_OPER_RESULTANTE\r\n" + 
/* 1398 */       "                        , VLRUNIT_NF\r\n" + 
/* 1399 */       "                        , RECEPIENTE_GLP\r\n" + 
/* 1400 */       "                        , CHAVE_NFE_CTE\r\n" + 
/* 1401 */       "                    FROM TOT_GERAL_ENTRADA\r\n" + 
/* 1402 */       "                    UNION ALL \r\n" + 
/* 1403 */       "                    SELECT\r\n" + 
/* 1404 */       "                        SEQUENCIA\r\n" + 
/* 1405 */       "                        , CODEMP_ANP\r\n" + 
/* 1406 */       "                        , MESREFERENCIA\r\n" + 
/* 1407 */       "                        , CODOPER\r\n" + 
/* 1408 */       "                        , CODINSTALACAO1\r\n" + 
/* 1409 */       "                        , CODINSTALACAO2\r\n" + 
/* 1410 */       "                        , CODPROD_ANP\r\n" + 
/* 1411 */       "                        , -QTD_PROD_UN_ANP\r\n" + 
/* 1412 */       "                        , -QTD_PROD_OPERADO_KG\r\n" + 
/* 1413 */       "                        , '0' AS COD_MODAL_MOVIMENTACAO\r\n" + 
/* 1414 */       "                        , COD_OLEODUTO_TRANSPORTE\r\n" + 
/* 1415 */       "                        , CNPJ_PARC\r\n" + 
/* 1416 */       "                        , CODCID_ANP\r\n" + 
/* 1417 */       "                        , COD_ATIV_PARC_ANP\r\n" + 
/* 1418 */       "                        , COD_PAIS_ANP\r\n" + 
/* 1419 */       "                        , NUM_LICEN_IMPORT\r\n" + 
/* 1420 */       "                        , NUM_DECLARACAO_IMPORT\r\n" + 
/* 1421 */       "                        , NUMNF\r\n" + 
/* 1422 */       "                        , SERIE_NF\r\n" + 
/* 1423 */       "                        , DAT_OPER_COMERCIAL\r\n" + 
/* 1424 */       "                        , COD_TIP_TARIFA_SERVICO\r\n" + 
/* 1425 */       "                        , CARACTERISTICA\r\n" + 
/* 1426 */       "                        , METODO\r\n" + 
/* 1427 */       "                        , MODALIDADE_FRETE\r\n" + 
/* 1428 */       "                        , NUM_DOC_QUALIDADE\r\n" + 
/* 1429 */       "                        , COD_OPER_RESULTANTE\r\n" + 
/* 1430 */       "                        , VLRUNIT_NF\r\n" + 
/* 1431 */       "                        , RECEPIENTE_GLP\r\n" + 
/* 1432 */       "                        , CHAVE_NFE_CTE\r\n" + 
/* 1433 */       "                    FROM TOT_GERAL_SAIDA\r\n" + 
/* 1434 */       "            ) AAA\r\n" + 
/* 1435 */       "            GROUP BY AAA.CODEMP_ANP\r\n" + 
/* 1436 */       "                , AAA.MESREFERENCIA\r\n" + 
/* 1437 */       "                , AAA.CODINSTALACAO1\r\n" + 
/* 1438 */       "                , AAA.CODINSTALACAO2\r\n" + 
/* 1439 */       "                , AAA.CODPROD_ANP\r\n" + 
/* 1440 */       "                , AAA.COD_MODAL_MOVIMENTACAO\r\n" + 
/* 1441 */       "                , AAA.COD_OLEODUTO_TRANSPORTE\r\n" + 
/* 1442 */       "                , AAA.CNPJ_PARC\r\n" + 
/* 1443 */       "                , AAA.CODCID_ANP\r\n" + 
/* 1444 */       "                , AAA.COD_ATIV_PARC_ANP\r\n" + 
/* 1445 */       "                , AAA.CODPAIS_ANP\r\n" + 
/* 1446 */       "                , AAA.NUM_LICEN_IMPORT\r\n" + 
/* 1447 */       "                , AAA.NUM_DECLARACAO_IMPORT\r\n" + 
/* 1448 */       "                , AAA.NUMNF\r\n" + 
/* 1449 */       "                , AAA.SERIE_NF\r\n" + 
/* 1450 */       "                , AAA.DAT_OPER_COMERCIAL\r\n" + 
/* 1451 */       "                , AAA.COD_TIP_TARIFA_SERVICO\r\n" + 
/* 1452 */       "                , AAA.CARACTERISTICA\r\n" + 
/* 1453 */       "                , AAA.METODO\r\n" + 
/* 1454 */       "                , AAA.MODALIDADE_FRETE\r\n" + 
/* 1455 */       "                , AAA.NUM_DOC_QUALIDADE\r\n" + 
/* 1456 */       "                , AAA.COD_OPER_RESULTANTE\r\n" + 
/* 1457 */       "                , AAA.VLRUNIT_NF\r\n" + 
/* 1458 */       "                , AAA.RECEPIENTE_GLP\r\n" + 
/* 1459 */       "                , AAA.CHAVE_NFE_CTE\r\n" + 
/* 1460 */       "        ) BBB\r\n" + 
/* 1461 */       ")\r\n" + 
/* 1462 */       "\r\n" + 
/* 1463 */       "\r\n" + 
/* 1464 */       "SELECT * FROM BASE\r\n" + 
/* 1465 */       "UNION ALL SELECT * FROM TOT\r\n" + 
/* 1466 */       "UNION ALL SELECT * FROM TOT_GERAL_ENTRADA\r\n" + 
/* 1467 */       "UNION ALL SELECT * FROM TOT_GERAL_SAIDA\r\n" + 
/* 1468 */       "UNION ALL SELECT * FROM ESTOQUE_FINAL\r\n" + 
/* 1469 */       "ORDER BY 1";
/*      */     
/* 1471 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/* 1472 */     return pstm.executeQuery();
/*      */   }
/*      */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMPSATTVA.jar!\br\com\sattva\simp\service\ActExportarArquivoService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */