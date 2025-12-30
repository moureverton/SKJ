/*     */ package br.com.sattva.lubmaster.simp.erp.service;
/*     */ 
/*     */ import br.com.sankhya.extensions.actionbutton.ContextoAcao;
/*     */ import br.com.sankhya.extensions.actionbutton.Registro;
/*     */ import br.com.sankhya.jape.EntityFacade;
/*     */ import br.com.sankhya.jape.sql.NativeSql;
/*     */ import br.com.sankhya.jape.vo.DynamicVO;
/*     */ import br.com.sankhya.jape.vo.EntityVO;
/*     */ import br.com.sankhya.modelcore.util.EntityFacadeFactory;
/*     */ import br.com.sattva.lubmaster.simp.config.OperationCatalog;
/*     */ import br.com.sattva.lubmaster.simp.config.ResourceLoader;
/*     */ import br.com.sattva.lubmaster.simp.config.SimpSettings;
/*     */ import br.com.sattva.lubmaster.simp.erp.dto.SimpExportResult;
/*     */ import br.com.sattva.lubmaster.simp.infra.db.NativeSqlHelper;
/*     */ import java.math.BigDecimal;
/*     */ import java.sql.Date;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SimpExportServiceOnline
/*     */ {
/*  43 */   private static final Logger LOG = Logger.getLogger(SimpExportServiceOnline.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpExportResult executar(ContextoAcao ctx, int codEmp, Date dtIni, Date dtFim, List<String> _ignoredOpCodes, String charset, boolean somenteCSV, boolean gravarNoBanco, BigDecimal codSimSimp) throws Exception {
/*  49 */     long t0 = System.currentTimeMillis();
/*  50 */     SimpSettings cfg = (new SimpSettings.Builder()).codEmp(codEmp).period(dtIni, dtFim).outDir("/home/mgeweb/repositorio/SIMP/").charset((charset != null) ? charset : "ISO-8859-1").debug(false).matrixVersion("v1_11").offline(false).build();
/*     */     
/*  52 */     NativeSqlHelper helper = new NativeSqlHelper(cfg);
/*     */     
/*  54 */     OperationCatalog catalog = null;
/*     */     
/*     */     try {
/*  57 */       ResourceLoader rl = new ResourceLoader(cfg);
/*  58 */       catalog = OperationCatalog.load(rl, null);
/*  59 */     } catch (Exception var58) {
/*  60 */       catalog = null;
/*     */     } 
/*     */     
/*  63 */     OnlineOperationRepository repo = new OnlineOperationRepository(cfg);
/*  64 */     List<OnlineOperationRepository.OpDef> ops = repo.loadActiveOps();
/*  65 */     Calendar cal = Calendar.getInstance();
/*  66 */     cal.setTime((dtFim != null) ? dtFim : new Date());
/*  67 */     int mesRef = cal.get(2) + 1;
/*  68 */     int anoRef = cal.get(1);
/*     */ 
/*     */ 
/*     */     
/*  72 */     for (int i = 0; i < ops.size(); i++) {
/*  73 */       OnlineOperationRepository.OpDef op = ops.get(i);
/*  74 */       Integer countRS = null;
/*     */       
/*     */       try {
/*  77 */         tryCountRows(helper, op.sql, codEmp, dtIni, dtFim, mesRef, anoRef);
/*  78 */       } catch (Throwable var57) {
/*  79 */         LOG.fine("[SIMP] Contagem (COUNT(*)) indisponível para op=" + op.code + ": " + var57.getMessage());
/*  80 */         countRS = null;
/*     */       } 
/*     */       
/*  83 */       ResultSet rs = repo.executeOpSql(helper, op.sql, codEmp, dtIni, dtFim, mesRef, anoRef);
/*     */       
/*  85 */       while (rs.next()) {
/*  86 */         String codEmpAnp = rs.getString("AD_CODEMP_ANP");
/*  87 */         String referenciaArq = rs.getString("MES_REF");
/*  88 */         String codInstalacao1 = rs.getString("COD_INST_EMP");
/*  89 */         String codInstalacao2 = rs.getString("COD_INST_PAR");
/*  90 */         String codProdAnp = rs.getString("CODPROD_ANP");
/*  91 */         BigDecimal qtdUnPadrao = rs.getBigDecimal("QTD_UN_PADRAO");
/*  92 */         BigDecimal qtdKg = rs.getBigDecimal("QTD_CONVERTIDO_KG");
/*  93 */         String codModal = rs.getString("CODMODAL_SIMP");
/*  94 */         String codOleoduto = rs.getString("COD_OLEODUTO");
/*  95 */         String cgc_cpf = rs.getString("CNPJ_PARC");
/*  96 */         String codCidAnp = rs.getString("CODCID_ANP");
/*  97 */         String codCnaeAnpParc = rs.getString("AD_CNAE_ANP");
/*  98 */         String codPaisAnp = rs.getString("COD_PAIS_ANP");
/*  99 */         String licencaImportacao = rs.getString("LICENCA_IMPORTACAO_LI");
/* 100 */         String declaracaoImportacao = rs.getString("NUM_DELCARACAO_IMPORTACAO_DI");
/* 101 */         String nroNFOper = rs.getString("NRONF");
/* 102 */         String codSerieNfAnp = rs.getString("COD_SERIE_NF");
/* 103 */         String datOperComercial = rs.getString("DTENTSAI");
/* 104 */         String codTipTarifaServ = rs.getString("COD_TIP_TAR_SERV");
/* 105 */         String caracteristicaInativo = rs.getString("CARACTERISTICA_INATIVO");
/* 106 */         String metodoInativo = rs.getString("METODO_INATIVO");
/* 107 */         String modalidadeFrete = rs.getString("MODALIDADE_FRETE");
/* 108 */         String nroDocQualidade = rs.getString("VALOR_CARACTERISTICA");
/* 109 */         String codProdResultante = rs.getString("CODPROD_RESULTANTE_ANP");
/* 110 */         String vlrUnit = rs.getString("VLRUNIT");
/* 111 */         String recepienteGlp = rs.getString("RECIPIENTE_GLP");
/* 112 */         String chaveNfeCte = rs.getString("CHAVE_NFE_CTE");
/* 113 */         BigDecimal operacao = rs.getBigDecimal("OPERACAO");
/* 114 */         BigDecimal codOperTotalizador = rs.getBigDecimal("OPER_TOTALIZADOR");
/* 115 */         saveAD_DETSIMULARSIMP(codSimSimp, codEmpAnp, referenciaArq, codInstalacao1, codInstalacao2, codProdAnp, String.valueOf(qtdUnPadrao.intValue()), String.valueOf(qtdKg.intValue()), codModal, codOleoduto, cgc_cpf, codCidAnp, codCnaeAnpParc, codPaisAnp, licencaImportacao, declaracaoImportacao, nroNFOper, codSerieNfAnp, datOperComercial, codTipTarifaServ, caracteristicaInativo, metodoInativo, modalidadeFrete, nroDocQualidade, codProdResultante, vlrUnit, recepienteGlp, chaveNfeCte, codOperTotalizador, operacao, null, null, null, null);
/*     */       } 
/*     */       
/* 118 */       rs.close();
/*     */     } 
/*     */     
/* 121 */     return null;
/*     */   }
/*     */   
/*     */   private static BigDecimal tryResolveCodSimSimp(ContextoAcao ctx, BigDecimal fromParam) {
/* 125 */     if (fromParam != null) {
/* 126 */       return fromParam;
/*     */     }
/*     */     try {
/* 129 */       Object o = ctx.getParam("CODSIMSIM");
/* 130 */       if (o instanceof BigDecimal) {
/* 131 */         return (BigDecimal)o;
/*     */       }
/*     */       
/* 134 */       if (o != null) {
/* 135 */         return new BigDecimal(o.toString());
/*     */       }
/* 137 */     } catch (Throwable throwable) {}
/*     */ 
/*     */     
/*     */     try {
/* 141 */       Registro[] regs = ctx.getLinhas();
/* 142 */       if (regs != null && regs.length > 0) {
/* 143 */         Object o = regs[0].getCampo("CODSIMSIM");
/* 144 */         if (o instanceof BigDecimal) {
/* 145 */           return (BigDecimal)o;
/*     */         }
/*     */         
/* 148 */         if (o != null) {
/* 149 */           return new BigDecimal(o.toString());
/*     */         }
/*     */       } 
/* 152 */     } catch (Throwable throwable) {}
/*     */ 
/*     */     
/* 155 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void saveAD_DETSIMULARSIMP(BigDecimal codSimulaSimp, String codEmpAnp, String referenciaArq, String codInstalacao1, String codInstalacao2, String codProdAnp, String qtdUnPadrao, String qtdKg, String codModal, String codOleoduto, String cgc_cpf, String codCidAnp, String codCnaeAnpParc, String codPaisAnp, String licencaImportacao, String declaracaoImportacao, String nroNFOper, String codSerieNfAnp, String datOperComercial, String codTipTarifaServ, String caracteristicaInativo, String metodoInativo, String modalidadeFrete, String nroDocQualidade, String codProdResultante, String vlrUnit, String recepienteGlp, String chaveNfeCte, BigDecimal codOperTotalizador, BigDecimal operacao, BigDecimal nunota, String seq, String codProd, String idiProc) throws Exception {
/* 160 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/* 161 */     DynamicVO simp = (DynamicVO)dwf.getDefaultValueObjectInstance("AD_DETSIMULARSIMP");
/* 162 */     simp.setProperty("CODSIMSIM", codSimulaSimp);
/* 163 */     simp.setProperty("CODEMP_ANP", codEmpAnp);
/* 164 */     simp.setProperty("MESREFERENCIA", referenciaArq.replace("-", ""));
/* 165 */     simp.setProperty("CODOPER", operacao);
/* 166 */     simp.setProperty("CODINSTALACAO1", codInstalacao1);
/* 167 */     simp.setProperty("CODINSTALACAO2", codInstalacao2);
/* 168 */     simp.setProperty("CODPROD_ANP", codProdAnp);
/* 169 */     simp.setProperty("QTD_PROD_UN_ANP", qtdUnPadrao);
/* 170 */     simp.setProperty("QTD_PROD_OPERADO_KG", qtdKg);
/* 171 */     simp.setProperty("COD_MODAL_MOVIMENTACAO", codModal);
/* 172 */     simp.setProperty("COD_OLEODUTO_TRANSPORTE", codOleoduto);
/* 173 */     simp.setProperty("CNPJ_PARC", cgc_cpf);
/* 174 */     simp.setProperty("CODCID_ANP", codCidAnp);
/* 175 */     simp.setProperty("COD_ATIV_PARC_ANP", codCnaeAnpParc);
/* 176 */     simp.setProperty("CODPAIS_ANP", codPaisAnp);
/* 177 */     simp.setProperty("NUM_LICEN_IMPORT", licencaImportacao);
/* 178 */     simp.setProperty("NUM_DECLARACAO_IMPORT", declaracaoImportacao);
/* 179 */     simp.setProperty("NUMNF", nroNFOper);
/* 180 */     simp.setProperty("SERIE_NF", codSerieNfAnp);
/* 181 */     simp.setProperty("DAT_OPER_COMERCIAL", datOperComercial);
/* 182 */     simp.setProperty("COD_TIP_TARIFA_SERVICO", codTipTarifaServ);
/* 183 */     simp.setProperty("CARACTERISTICA", caracteristicaInativo);
/* 184 */     simp.setProperty("METODO", metodoInativo);
/* 185 */     simp.setProperty("MODALIDADE_FRETE", modalidadeFrete);
/* 186 */     simp.setProperty("NUM_DOC_QUALIDADE", nroDocQualidade);
/* 187 */     simp.setProperty("COD_OPER_RESULTANTE", codProdResultante);
/* 188 */     simp.setProperty("VLRUNIT_NF", vlrUnit);
/* 189 */     simp.setProperty("RECEPIENTE_GLP", recepienteGlp);
/* 190 */     simp.setProperty("CAHVE_NFE_CTE", chaveNfeCte);
/* 191 */     simp.setProperty("OPERTOTALIZADOR", codOperTotalizador);
/* 192 */     simp.setProperty("NUNOTA", nunota);
/* 193 */     simp.setProperty("SEQUENCIA", seq);
/* 194 */     simp.setProperty("CODPROD", codProd);
/* 195 */     simp.setProperty("IDIPROC", idiProc);
/* 196 */     dwf.createEntity("AD_DETSIMULARSIMP", (EntityVO)simp);
/*     */   }
/*     */   
/*     */   private static byte[] joinWithNewline(List<String> lines, String charset) throws Exception {
/* 200 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 202 */     for (int i = 0; i < lines.size(); i++) {
/* 203 */       if (i > 0) {
/* 204 */         sb.append('\n');
/*     */       }
/*     */       
/* 207 */       sb.append(lines.get(i));
/*     */     } 
/*     */     
/* 210 */     return sb.toString().getBytes((charset != null) ? charset : "ISO-8859-1");
/*     */   }
/*     */   
/*     */   private void assertRequiredAliases(String opCode, ResultSet rs, Set<String> required) throws Exception {
/* 214 */     if (required != null && !required.isEmpty()) {
/* 215 */       ResultSetMetaData md = rs.getMetaData();
/* 216 */       Set<String> present = new HashSet<>();
/*     */       
/* 218 */       for (int i = 1; i <= md.getColumnCount(); i++) {
/* 219 */         String lbl = md.getColumnLabel(i);
/* 220 */         if (lbl == null || lbl.trim().isEmpty()) {
/* 221 */           lbl = md.getColumnName(i);
/*     */         }
/*     */         
/* 224 */         if (lbl != null) {
/* 225 */           present.add(lbl.trim().toUpperCase());
/*     */         }
/*     */       } 
/*     */       
/* 229 */       Set<String> missing = new HashSet<>(required);
/* 230 */       missing.removeAll(present);
/* 231 */       if (!missing.isEmpty()) {
/* 232 */         StringBuilder sb = new StringBuilder();
/* 233 */         sb.append("Faltam aliases obrigatórios para a operação ").append(opCode).append(": ");
/* 234 */         boolean first = true;
/*     */         
/* 236 */         for (Iterator<String> var10 = missing.iterator(); var10.hasNext(); first = false) {
/* 237 */           String s = var10.next();
/* 238 */           if (!first) {
/* 239 */             sb.append(", ");
/*     */           }
/*     */           
/* 242 */           sb.append(s);
/*     */         } 
/*     */         
/* 245 */         throw new IllegalStateException(sb.toString());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String joinLines(List<String> lines) {
/* 251 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 253 */     for (int i = 0; i < lines.size(); i++) {
/* 254 */       if (i > 0) {
/* 255 */         sb.append('\n');
/*     */       }
/*     */       
/* 258 */       sb.append("- ").append(lines.get(i));
/*     */     } 
/*     */     
/* 261 */     return sb.toString();
/*     */   }
/*     */   private Integer tryCountRows(NativeSqlHelper helper, String sql, int codEmp, Date dtIni, Date dtFim, int mesRef, int anoRef) throws Exception {
/*     */     Integer var11;
/* 265 */     NativeSql ns = helper.newNativeSql();
/* 266 */     ns.appendSql("SELECT COUNT(*) AS CNT FROM ( ");
/* 267 */     ns.appendSql(sql);
/* 268 */     ns.appendSql(" ) X");
/* 269 */     ns.setNamedParameter("CODEMP", new BigDecimal(codEmp));
/* 270 */     if (dtIni != null) {
/* 271 */       ns.setNamedParameter("DTINI", new Date(dtIni.getTime()));
/*     */     }
/*     */     
/* 274 */     if (dtFim != null) {
/* 275 */       ns.setNamedParameter("DTFIM", new Date(dtFim.getTime()));
/*     */     }
/*     */     
/* 278 */     ns.setNamedParameter("MES_REFERENCIA", new BigDecimal(mesRef));
/* 279 */     ns.setNamedParameter("ANO_REFERENTE", new BigDecimal(anoRef));
/*     */     
/*     */     try {
/* 282 */       ns.setNamedParameter("REFERENCIA", (dtIni != null) ? new Date(dtIni.getTime()) : null);
/* 283 */     } catch (Throwable throwable) {}
/*     */ 
/*     */     
/* 286 */     ResultSet rs = null;
/*     */ 
/*     */ 
/*     */     
/* 290 */     try { rs = ns.executeQuery();
/* 291 */       if (rs.next()) {
/* 292 */         Integer integer = Integer.valueOf(rs.getInt(1));
/* 293 */         return integer;
/*     */       }
/*     */        }
/*     */     finally
/*     */     
/* 298 */     { if (rs != null)
/*     */         try {
/* 300 */           rs.close();
/* 301 */         } catch (Exception exception) {}  }  if (rs != null) try { rs.close(); } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 307 */     return var11;
/*     */   }
/*     */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\erp\service\SimpExportServiceOnline.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */