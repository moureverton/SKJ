/*     */ package br.com.sattva.simp.actions;
/*     */ 
/*     */ import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
/*     */ import br.com.sankhya.extensions.actionbutton.ContextoAcao;
/*     */ import br.com.sankhya.extensions.actionbutton.Registro;
/*     */ import br.com.sankhya.jape.EntityFacade;
/*     */ import br.com.sankhya.jape.dao.JdbcWrapper;
/*     */ import br.com.sankhya.jape.sql.NativeSql;
/*     */ import br.com.sankhya.jape.vo.DynamicVO;
/*     */ import br.com.sankhya.jape.vo.EntityVO;
/*     */ import br.com.sankhya.modelcore.util.EntityFacadeFactory;
/*     */ import java.math.BigDecimal;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ public class ActBuscaDados
/*     */   implements AcaoRotinaJava
/*     */ {
/*     */   public void doAction(ContextoAcao ctx) throws Exception {
/*  24 */     long t0 = System.nanoTime();
/*     */     
/*  26 */     BigDecimal codEmp = getParamAsBigDecimal(ctx, "CODEMP");
/*  27 */     Timestamp referencia = (Timestamp)ctx.getParam("REFERENCIA");
/*  28 */     if (codEmp == null) {
/*  29 */       throw new IllegalArgumentException("Parâmetro obrigatório ausente: CODEMP.");
/*     */     }
/*  31 */     if (referencia == null) {
/*  32 */       throw new IllegalArgumentException("Parâmetro obrigatório ausente: REFERENCIA.");
/*     */     }
/*  34 */     String referenciaStr = formatMonthYear(referencia);
/*     */     
/*  36 */     Registro[] linhas = ctx.getLinhas();
/*  37 */     if (linhas == null || linhas.length == 0) {
/*  38 */       throw new IllegalArgumentException("Nenhuma linha selecionada em AD_SIMULARSIMP.");
/*     */     }
/*     */     
/*  41 */     JdbcWrapper jdbc = null;
/*     */     try {
/*  43 */       jdbc = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
/*     */       
/*  45 */       Map<String, String> operacoes = carregarOperacoesAtivas(jdbc);
/*  46 */       if (operacoes.isEmpty()) {
/*  47 */         throw new IllegalStateException("Nenhuma operação ativa com consulta encontrada em AD_OPERACAOANP.");
/*     */       }
/*     */       
/*  50 */       Stats stats = new Stats(null); byte b; int i; Registro[] arrayOfRegistro;
/*  51 */       for (i = (arrayOfRegistro = linhas).length, b = 0; b < i; ) { Registro linha = arrayOfRegistro[b];
/*  52 */         BigDecimal codSimsim = getCampoAsBigDecimal(linha, "CODSIMSIM");
/*  53 */         if (codSimsim == null) {
/*  54 */           throw new IllegalArgumentException("Linha selecionada sem CODSIMSIM (PK da AD_SIMULARSIMP).");
/*     */         }
/*  56 */         processarLinha(ctx, jdbc, codSimsim, codEmp, referenciaStr, operacoes, stats);
/*     */         b++; }
/*     */       
/*  59 */       long t1 = System.nanoTime();
/*  60 */       String resumo = montarResumo(stats, t0, t1);
/*  61 */       ctx.setMensagemRetorno(resumo);
/*     */     }
/*  63 */     catch (Exception e) {
/*  64 */       String msg = "Falha ao gerar dados em AD_DETSIMULARSIMP: " + e.getMessage();
/*  65 */       throw new Exception(msg, e);
/*     */     } finally {
/*  67 */       if (jdbc != null) {
/*  68 */         try { jdbc.closeSession(); } catch (Exception exception) {}
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void processarLinha(ContextoAcao ctx, JdbcWrapper jdbc, BigDecimal codSimsim, BigDecimal codEmp, String referenciaStr, Map<String, String> operacoes, Stats stats) throws Exception {
/*  75 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/*  76 */     stats.linhasPorOperacao.clear();
/*     */     
/*  78 */     for (Map.Entry<String, String> op : operacoes.entrySet()) {
/*  79 */       String codOperAnp = op.getKey();
/*  80 */       String sqlConsulta = op.getValue();
/*     */       
/*  82 */       int inseridosOp = 0;
/*     */       
/*  84 */       NativeSql ns = new NativeSql(jdbc);
/*  85 */       ns.appendSql(sqlConsulta);
/*  86 */       ns.setNamedParameter("REFERENCIA", referenciaStr);
/*  87 */       ns.setNamedParameter("CODEMP", codEmp);
/*     */       
/*  89 */       ResultSet rs = null;
/*     */       try {
/*  91 */         rs = ns.executeQuery();
/*  92 */         while (rs.next()) {
/*  93 */           DynamicVO novo = (DynamicVO)dwf.getDefaultValueObjectInstance("AD_DETSIMULARSIMP");
/*  94 */           novo.setProperty("CODSIMSIM", codSimsim);
/*     */           
/*  96 */           setIfHas(rs, novo, "CODEMP_ANP");
/*  97 */           setIfHas(rs, novo, "MESREFERENCIA");
/*  98 */           setIfHas(rs, novo, "CODOPER", true);
/*  99 */           setIfHas(rs, novo, "CODINSTALACAO1");
/* 100 */           setIfHas(rs, novo, "CODINSTALACAO2");
/* 101 */           setIfHas(rs, novo, "CODPROD_ANP");
/* 102 */           setIfHas(rs, novo, "QTD_PROD_UN_ANP");
/* 103 */           setIfHas(rs, novo, "QTD_PROD_OPERADO_KG");
/* 104 */           setIfHas(rs, novo, "COD_MODAL_MOVIMENTACAO");
/* 105 */           setIfHas(rs, novo, "COD_OLEODUTO_TRANSPORTE");
/* 106 */           setIfHas(rs, novo, "CNPJ_PARC");
/* 107 */           setIfHas(rs, novo, "CODCID_ANP");
/* 108 */           setIfHas(rs, novo, "COD_ATIV_PARC_ANP");
/* 109 */           setIfHas(rs, novo, "CODPAIS_ANP");
/* 110 */           setIfHas(rs, novo, "NUM_LICEN_IMPORT");
/* 111 */           setIfHas(rs, novo, "NUM_DECLARACAO_IMPORT");
/* 112 */           setIfHas(rs, novo, "NUMNF");
/* 113 */           setIfHas(rs, novo, "SERIE_NF");
/* 114 */           setIfHas(rs, novo, "DAT_OPER_COMERCIAL");
/* 115 */           setIfHas(rs, novo, "COD_TIP_TARIFA_SERVICO");
/* 116 */           setIfHas(rs, novo, "CARACTERISTICA");
/* 117 */           setIfHas(rs, novo, "METODO");
/* 118 */           setIfHas(rs, novo, "MODALIDADE_FRETE");
/* 119 */           setIfHas(rs, novo, "NUM_DOC_QUALIDADE");
/* 120 */           setIfHas(rs, novo, "COD_OPER_RESULTANTE");
/* 121 */           setIfHas(rs, novo, "VLRUNIT_NF");
/* 122 */           setIfHas(rs, novo, "RECEPIENTE_GLP");
/* 123 */           setIfHas(rs, novo, "CHAVE_NFE_CTE");
/* 124 */           setIfHas(rs, novo, "OPERTOTALIZADOR", true);
/* 125 */           setIfHas(rs, novo, "NUNOTA", true);
/* 126 */           setIfHas(rs, novo, "SEQUENCIA");
/* 127 */           setIfHas(rs, novo, "CODPROD");
/* 128 */           setIfHas(rs, novo, "IDIPROC");
/*     */           
/* 130 */           dwf.createEntity("AD_DETSIMULARSIMP", (EntityVO)novo);
/* 131 */           inseridosOp++;
/*     */         } 
/*     */       } finally {
/* 134 */         if (rs != null) try { rs.close(); } catch (Exception exception) {}
/*     */       
/*     */       } 
/* 137 */       if (inseridosOp > 0) {
/* 138 */         stats.totalInseridos += inseridosOp;
/*     */       }
/* 140 */       stats.operacoesProcessadas++;
/* 141 */       incrementCount(stats.linhasPorOperacao, codOperAnp, inseridosOp);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Map<String, String> carregarOperacoesAtivas(JdbcWrapper jdbc) throws Exception {
/* 146 */     Map<String, String> ops = new LinkedHashMap<>();
/* 147 */     NativeSql ns = new NativeSql(jdbc);
/* 148 */     ns.appendSql("SELECT CODOPER_ANP, CONSULTA FROM AD_OPERACAOANP WHERE ATIVA = 'S' AND CONSULTA IS NOT NULL ORDER BY 1");
/*     */     
/* 150 */     ResultSet rs = null;
/*     */     try {
/* 152 */       rs = ns.executeQuery();
/* 153 */       while (rs.next()) {
/* 154 */         String codOperAnp = rs.getString("CODOPER_ANP");
/* 155 */         String consulta = rs.getString("CONSULTA");
/* 156 */         if (codOperAnp != null && consulta != null && !consulta.trim().isEmpty()) {
/* 157 */           ops.put(codOperAnp, consulta);
/*     */         }
/*     */       } 
/*     */     } finally {
/* 161 */       if (rs != null) try { rs.close(); } catch (Exception exception) {} 
/*     */     } 
/* 163 */     return ops;
/*     */   }
/*     */   
/*     */   private void setIfHas(ResultSet rs, DynamicVO vo, String col) {
/* 167 */     setIfHas(rs, vo, col, false);
/*     */   }
/*     */   
/*     */   private void setIfHas(ResultSet rs, DynamicVO vo, String col, boolean numerico) {
/*     */     try {
/* 172 */       rs.findColumn(col);
/* 173 */       if (numerico) {
/* 174 */         BigDecimal val = null;
/*     */         
/* 176 */         try { String s = rs.getString(col).replace("-", "");
/* 177 */           if (s != null && s.trim().length() > 0) {
/* 178 */             val = new BigDecimal(s.trim());
/*     */           } }
/* 180 */         catch (NumberFormatException nf) { 
/* 181 */           try { val = rs.getBigDecimal(col); } catch (Exception exception) {} }
/*     */         
/* 183 */         vo.setProperty(col, val);
/*     */       } else {
/* 185 */         String s = rs.getString(col).replace("-", "");
/* 186 */         vo.setProperty(col, s);
/*     */       } 
/* 188 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private BigDecimal getParamAsBigDecimal(ContextoAcao ctx, String nome) {
/* 194 */     Object v = ctx.getParam(nome);
/* 195 */     if (v instanceof BigDecimal) return (BigDecimal)v; 
/* 196 */     if (v instanceof Number) return new BigDecimal(((Number)v).toString()); 
/* 197 */     if (v instanceof String) {
/* 198 */       String s = ((String)v).trim();
/* 199 */       if (s.isEmpty()) return null;  
/* 200 */       try { return new BigDecimal(s); } catch (Exception exception) {}
/*     */     } 
/* 202 */     return null;
/*     */   }
/*     */   
/*     */   private BigDecimal getCampoAsBigDecimal(Registro r, String nomeCampo) {
/* 206 */     Object v = r.getCampo(nomeCampo);
/* 207 */     if (v instanceof BigDecimal) return (BigDecimal)v; 
/* 208 */     if (v instanceof Number) return new BigDecimal(((Number)v).toString()); 
/* 209 */     if (v instanceof String) {
/* 210 */       String s = ((String)v).trim();
/* 211 */       if (s.isEmpty()) return null;  
/* 212 */       try { return new BigDecimal(s); } catch (Exception exception) {}
/*     */     } 
/* 214 */     return null;
/*     */   }
/*     */   
/*     */   public static String formatMonthYear(Timestamp timestamp) {
/* 218 */     if (timestamp == null) return null; 
/* 219 */     SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
/* 220 */     return sdf.format(timestamp);
/*     */   }
/*     */   
/*     */   private String montarResumo(Stats stats, long t0, long t1) {
/* 224 */     StringBuilder sb = new StringBuilder();
/* 225 */     sb.append("Geração concluída com sucesso.\n");
/* 226 */     sb.append("Operações processadas: ").append(stats.operacoesProcessadas).append('\n');
/* 227 */     sb.append("Linhas inseridas: ").append(stats.totalInseridos).append('\n');
/* 228 */     if (!stats.linhasPorOperacao.isEmpty()) {
/* 229 */       sb.append("Detalhe por operação (CODOPER_ANP -> linhas):\n");
/* 230 */       for (Map.Entry<String, Integer> e : stats.linhasPorOperacao.entrySet()) {
/* 231 */         sb.append(" - ").append(e.getKey()).append(" -> ").append(e.getValue()).append('\n');
/*     */       }
/*     */     } 
/* 234 */     long ms = (t1 - t0) / 1000000L;
/* 235 */     sb.append("Tempo total: ").append(ms).append(" ms");
/* 236 */     return sb.toString();
/*     */   }
/*     */   private static class Stats { private Stats() {}
/*     */     
/* 240 */     int operacoesProcessadas = 0;
/* 241 */     int totalInseridos = 0;
/* 242 */     Map<String, Integer> linhasPorOperacao = new LinkedHashMap<>(); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void incrementCount(Map<String, Integer> map, String key, int add) {
/* 247 */     Integer cur = map.get(key);
/* 248 */     if (cur == null) cur = Integer.valueOf(0); 
/* 249 */     map.put(key, Integer.valueOf(cur.intValue() + add));
/*     */   }
/*     */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMPSATTVA.jar!\br\com\sattva\simp\actions\ActBuscaDados.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */