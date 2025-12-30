/*     */ package br.com.sattva.lubmaster.simp.erp.store;
/*     */ 
/*     */ import br.com.sankhya.extensions.actionbutton.ContextoAcao;
/*     */ import br.com.sankhya.jape.EntityFacade;
/*     */ import br.com.sankhya.modelcore.util.EntityFacadeFactory;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.math.BigDecimal;
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
/*     */ public final class SimpFileStore
/*     */ {
/*  23 */   private static final Logger LOG = Logger.getLogger(SimpFileStore.class.getName());
/*     */   
/*     */   public String persistViaLegado(ContextoAcao ctx, String fileName, byte[] rawData, String mimeType, BigDecimal codSimSimp, String mesReferenteStr, BigDecimal anoReferente) throws Exception {
/*     */     Object pkObj;
/*  27 */     if (rawData == null || rawData.length == 0) {
/*  28 */       throw new IllegalArgumentException("Bytes do arquivo vazios para persistência.");
/*     */     }
/*  30 */     if (mimeType == null || mimeType.trim().isEmpty()) {
/*  31 */       mimeType = "text/plain";
/*     */     }
/*     */     
/*  34 */     BigDecimal mesReferente = null;
/*     */     try {
/*  36 */       if (mesReferenteStr != null && mesReferenteStr.trim().length() > 0) {
/*  37 */         mesReferente = new BigDecimal(mesReferenteStr.trim());
/*     */       }
/*  39 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*  42 */     byte[] payload = addLegacyPlaceholder(rawData, fileName, mimeType, codSimSimp, mesReferenteStr, anoReferente);
/*  43 */     LOG.info("[SIMP] Persist payload bytes=" + payload.length + " | name=" + fileName + " | mime=" + mimeType + 
/*  44 */         " | mes=" + mesReferenteStr + " | ano=" + anoReferente + " | pai=" + ((codSimSimp != null) ? codSimSimp.toPlainString() : "(null)"));
/*     */     
/*  46 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/*     */ 
/*     */     
/*  49 */     Class<?> cls = Class.forName("processamento.processarExportarArquivo");
/*  50 */     Method[] methods = cls.getDeclaredMethods();
/*     */ 
/*     */     
/*  53 */     for (int i = 0; i < methods.length; i++) {
/*  54 */       Method mm = methods[i];
/*  55 */       if ("saveAD_DETSIMULARSIMP".equals(mm.getName())) {
/*  56 */         LOG.info("[SIMP] Overload disponível: " + formatSig(mm));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  61 */     Method chosen = null;
/*  62 */     String signature = null;
/*     */     
/*     */     int j;
/*  65 */     for (j = 0; j < methods.length && chosen == null; j++) {
/*  66 */       Method m = methods[j];
/*  67 */       if ("saveAD_DETSIMULARSIMP".equals(m.getName())) {
/*  68 */         Class[] pt = m.getParameterTypes();
/*  69 */         if (pt.length == 6 && 
/*  70 */           EntityFacade.class.isAssignableFrom(pt[0]) && 
/*  71 */           String.class.equals(pt[1]) && 
/*  72 */           byte[].class.equals(pt[2]) && 
/*  73 */           BigDecimal.class.isAssignableFrom(pt[3]) && 
/*  74 */           BigDecimal.class.isAssignableFrom(pt[4]) && 
/*  75 */           BigDecimal.class.isAssignableFrom(pt[5])) {
/*  76 */           chosen = m; signature = "(EF,String,byte[],BD,BD,BD)";
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*  81 */     if (chosen == null) {
/*  82 */       for (j = 0; j < methods.length; j++) {
/*  83 */         Method m = methods[j];
/*  84 */         if ("saveAD_DETSIMULARSIMP".equals(m.getName())) {
/*  85 */           Class[] pt = m.getParameterTypes();
/*  86 */           if (pt.length == 5 && 
/*  87 */             EntityFacade.class.isAssignableFrom(pt[0]) && 
/*  88 */             BigDecimal.class.isAssignableFrom(pt[1]) && 
/*  89 */             byte[].class.equals(pt[2]) && 
/*  90 */             BigDecimal.class.isAssignableFrom(pt[3]) && 
/*  91 */             BigDecimal.class.isAssignableFrom(pt[4])) {
/*  92 */             chosen = m; signature = "(EF,BD,byte[],BD,BD)";
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*  98 */     if (chosen == null) {
/*  99 */       for (j = 0; j < methods.length; j++) {
/* 100 */         Method m = methods[j];
/* 101 */         if ("saveAD_DETSIMULARSIMP".equals(m.getName())) {
/* 102 */           Class[] pt = m.getParameterTypes();
/* 103 */           if (pt.length == 5 && 
/* 104 */             EntityFacade.class.isAssignableFrom(pt[0]) && 
/* 105 */             BigDecimal.class.isAssignableFrom(pt[1]) && 
/* 106 */             String.class.equals(pt[2]) && 
/* 107 */             byte[].class.equals(pt[3]) && 
/* 108 */             BigDecimal.class.isAssignableFrom(pt[4])) {
/* 109 */             chosen = m; signature = "(EF,BD,String,byte[],BD)";
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 115 */     if (chosen == null)
/* 116 */       for (j = 0; j < methods.length; j++) {
/* 117 */         Method m = methods[j];
/* 118 */         if ("saveAD_DETSIMULARSIMP".equals(m.getName())) {
/* 119 */           Class[] pt = m.getParameterTypes();
/* 120 */           if (pt.length == 5 && 
/* 121 */             EntityFacade.class.isAssignableFrom(pt[0]) && 
/* 122 */             BigDecimal.class.isAssignableFrom(pt[1]) && 
/* 123 */             byte[].class.equals(pt[2]) && 
/* 124 */             String.class.equals(pt[3]) && 
/* 125 */             BigDecimal.class.isAssignableFrom(pt[4])) {
/* 126 */             chosen = m; signature = "(EF,BD,byte[],String,BD)";
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }  
/* 131 */     if (chosen == null) {
/* 132 */       String msg = "Nenhum overload esperado de saveAD_DETSIMULARSIMP encontrado. Abortando para não criar cabeçalho indevido.";
/* 133 */       LOG.severe("[SIMP] " + msg);
/* 134 */       throw new IllegalStateException(msg);
/*     */     } 
/*     */     
/* 137 */     chosen.setAccessible(true);
/* 138 */     LOG.info("[SIMP] Usando overload legado " + signature + " para salvar detalhe. CODSIMSIM pai=" + (
/* 139 */         (codSimSimp != null) ? codSimSimp.toPlainString() : "(null)"));
/*     */     
/* 141 */     Object inst = cls.newInstance();
/*     */ 
/*     */ 
/*     */     
/* 145 */     if ("(EF,String,byte[],BD,BD,BD)".equals(signature)) {
/*     */       
/* 147 */       pkObj = chosen.invoke(inst, new Object[] { EntityFacadeFactory.getDWFFacade(), fileName, payload, mesReferente, anoReferente, codSimSimp });
/* 148 */     } else if ("(EF,BD,byte[],BD,BD)".equals(signature)) {
/*     */       
/* 150 */       pkObj = chosen.invoke(inst, new Object[] { EntityFacadeFactory.getDWFFacade(), codSimSimp, payload, mesReferente, anoReferente });
/* 151 */     } else if ("(EF,BD,String,byte[],BD)".equals(signature)) {
/*     */       
/* 153 */       pkObj = chosen.invoke(inst, new Object[] { EntityFacadeFactory.getDWFFacade(), codSimSimp, fileName, payload, anoReferente });
/* 154 */     } else if ("(EF,BD,byte[],String,BD)".equals(signature)) {
/*     */       
/* 156 */       pkObj = chosen.invoke(inst, new Object[] { EntityFacadeFactory.getDWFFacade(), codSimSimp, payload, fileName, anoReferente });
/*     */     } else {
/* 158 */       throw new IllegalStateException("Assinatura não tratada: " + signature);
/*     */     } 
/*     */     
/* 161 */     BigDecimal pk = (pkObj instanceof BigDecimal) ? (BigDecimal)pkObj : (
/* 162 */       (pkObj != null) ? new BigDecimal(pkObj.toString()) : null);
/*     */     
/* 164 */     if (pk == null || pk.signum() <= 0) {
/* 165 */       String msg = "Persistência LEGADA retornou PK nulo/<=0. Verifique placeholder e parâmetros.";
/* 166 */       LOG.severe("[SIMP] " + msg);
/* 167 */       throw new IllegalStateException(msg);
/*     */     } 
/*     */     
/* 170 */     String out = pk.toPlainString();
/* 171 */     LOG.info("[SIMP] Persistência OK. CODSIMSIM=" + out);
/* 172 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] addLegacyPlaceholder(byte[] arquivo, String nomeArquivo, String mimeType, BigDecimal codSimSimp, String mesStr, BigDecimal ano) throws Exception {
/* 177 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/* 178 */     BufferedOutputStream bf = new BufferedOutputStream(out);
/*     */     
/* 180 */     StringBuilder json = new StringBuilder();
/* 181 */     json.append("__start_fileinformation__")
/* 182 */       .append("{\"name\":\"").append(nomeArquivo).append("\",")
/* 183 */       .append("\"size\":").append(arquivo.length).append(",")
/* 184 */       .append("\"type\":\"").append(mimeType).append("\"");
/*     */     
/* 186 */     if (codSimSimp != null)
/*     */     {
/* 188 */       json.append(",\"CODSIMSIM\":").append(codSimSimp.toPlainString())
/* 189 */         .append(",\"codSimSimp\":").append(codSimSimp.toPlainString())
/* 190 */         .append(",\"codPai\":").append(codSimSimp.toPlainString())
/* 191 */         .append(",\"parentId\":").append(codSimSimp.toPlainString());
/*     */     }
/* 193 */     if (mesStr != null) {
/* 194 */       json.append(",\"MES_REFERENCIA\":\"").append(mesStr).append("\"");
/*     */     }
/* 196 */     if (ano != null) {
/* 197 */       json.append(",\"ANO_REFERENTE\":").append(ano.toPlainString());
/*     */     }
/*     */     
/* 200 */     json.append("}")
/* 201 */       .append("__end_fileinformation__");
/*     */     
/* 203 */     bf.write(json.toString().getBytes("ISO-8859-1"));
/* 204 */     bf.write(arquivo);
/* 205 */     bf.flush();
/* 206 */     bf.close();
/* 207 */     return out.toByteArray();
/*     */   }
/*     */   
/*     */   private static String formatSig(Method m) {
/* 211 */     StringBuilder sb = (new StringBuilder(m.getName())).append('(');
/* 212 */     Class[] pt = m.getParameterTypes();
/* 213 */     for (int i = 0; i < pt.length; i++) {
/* 214 */       if (i > 0) sb.append(','); 
/* 215 */       sb.append(pt[i].getSimpleName());
/*     */     } 
/* 217 */     return sb.append(')').toString();
/*     */   }
/*     */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\erp\store\SimpFileStore.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */