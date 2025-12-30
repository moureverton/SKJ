/*     */ package br.com.sattva.lubmaster.simp.domain.mapping;
/*     */ import br.com.sattva.lubmaster.simp.config.ResourceLoader;
/*     */ import br.com.sattva.lubmaster.simp.domain.model.Operation;
/*     */ import br.com.sattva.lubmaster.simp.domain.model.SimpField;
/*     */ import br.com.sattva.lubmaster.simp.domain.model.SimpRecord;
/*     */ import java.lang.reflect.Method;
/*     */ import java.math.BigDecimal;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class FixtureLoader {
/*     */   public FixtureLoader(SimpSettings cfg) {
/*  16 */     this.loader = new ResourceLoader(cfg);
/*     */   } private final ResourceLoader loader;
/*     */   public List<SimpRecord> load(Operation op) throws Exception {
/*  19 */     String path = "/fixtures/" + op.code() + ".json";
/*  20 */     String json = this.loader.readText(path);
/*  21 */     if (json != null) {
/*     */       
/*     */       try {
/*  24 */         Class<?> gsonCls = Class.forName("com.google.gson.Gson");
/*  25 */         Object gson = gsonCls.newInstance();
/*  26 */         Method fromJson = gsonCls.getMethod("fromJson", new Class[] { String.class, Class.class });
/*  27 */         Map parsed = (Map)fromJson.invoke(gson, new Object[] { json, Map.class });
/*  28 */         Object recs = parsed.get("records");
/*  29 */         if (recs instanceof List) {
/*  30 */           return toRecords(op, (List)recs);
/*     */         }
/*  32 */       } catch (Throwable throwable) {}
/*     */     }
/*     */ 
/*     */     
/*  36 */     return fallback(op);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<SimpRecord> toRecords(Operation op, List list) throws Exception {
/*  42 */     List<SimpRecord> out = new ArrayList<>();
/*  43 */     for (int i = 0; i < list.size(); i++) {
/*  44 */       Object o = list.get(i);
/*  45 */       if (o instanceof Map) {
/*  46 */         Map m = (Map)o;
/*     */         
/*  48 */         SimpRecord r = new SimpRecord(op.code());
/*     */ 
/*     */         
/*  51 */         put(r, SimpField.CONTADOR_SEQUENCIAL, m.get("CONTADOR_SEQUENCIAL"));
/*  52 */         put(r, SimpField.AGENTE_REGULADO_INFORMANTE, m.get("AGENTE_REGULADO_INFORMANTE"));
/*  53 */         put(r, SimpField.MES_REFERENCIA_MMAAAA, m.get("MES_REFERENCIA_MMAAAA"));
/*  54 */         put(r, SimpField.CODIGO_OPERACAO, m.get("CODIGO_OPERACAO"));
/*  55 */         put(r, SimpField.CODIGO_INSTALACAO_1, m.get("CODIGO_INSTALACAO_1"));
/*  56 */         put(r, SimpField.CODIGO_INSTALACAO_2, m.get("CODIGO_INSTALACAO_2"));
/*  57 */         put(r, SimpField.CODIGO_PRODUTO_OPERADO, m.get("CODIGO_PRODUTO_OPERADO"));
/*  58 */         put(r, SimpField.QTD_UN_MEDIDA_ANP, m.get("QUANTIDADE_UNITARIA_MEDIDA_ANP"));
/*  59 */         put(r, SimpField.QTD_PRODUTO_KG, m.get("QUANTIDADE_DE_PRODUTO_EM_KG"));
/*  60 */         put(r, SimpField.CODIGO_MODAL, m.get("CODIGO_DO_MODAL"));
/*  61 */         put(r, SimpField.CODIGO_VEICULO, m.get("CODIGO_DO_VEICULO"));
/*  62 */         put(r, SimpField.IDENTIFICACAO_TERCEIRO, m.get("IDENTIFICACAO_DO_TERCEIRO"));
/*  63 */         put(r, SimpField.CODIGO_MUNICIPIO, m.get("CODIGO_DO_MUNICIPIO"));
/*  64 */         put(r, SimpField.CODIGO_ATIVIDADE_ECONOMICA, m.get("CODIGO_DA_ATIVIDADE_ECONOMICA"));
/*  65 */         put(r, SimpField.CODIGO_PAIS, m.get("CODIGO_DO_PAIS"));
/*  66 */         put(r, SimpField.LICENCA_IMPORTACAO_LI, m.get("LI_LICENCA_DE_IMPORTACAO"));
/*  67 */         put(r, SimpField.DECLARACAO_IMPORTACAO_DI, m.get("DI_DECLARACAO_DE_IMPORTACAO"));
/*  68 */         put(r, SimpField.NUMERO_NF, m.get("NUMERO_DA_NOTA_FISCAL"));
/*  69 */         put(r, SimpField.CODIGO_SERIE_NF, m.get("CODIGO_DA_SERIE_DA_NF"));
/*  70 */         put(r, SimpField.DATA_NF, m.get("DATA_DA_NOTA_FISCAL"));
/*  71 */         put(r, SimpField.CODIGO_SERVICO_ACORDADO, m.get("CODIGO_DO_SERVICO_ACORDADO"));
/*  72 */         put(r, SimpField.CODIGO_CARACTERISTICA_FISICO_QUIM, m.get("CODIGO_DA_CARACTERISTICA_FISICO_QUIMICA"));
/*  73 */         put(r, SimpField.CODIGO_METODO, m.get("CODIGO_DO_METODO"));
/*  74 */         put(r, SimpField.MODALIDADE_FRETE, m.get("MODALIDADE_DO_FRETE"));
/*  75 */         put(r, SimpField.VALOR_CARACTERISTICA_PRECO_RS_KG, m.get("VALOR_ENCONTRADO_DA_CARACTERISTICA_PRECO_RS_KG"));
/*  76 */         put(r, SimpField.CODIGO_PROD_OPER_RESULTANTE, m.get("CODIGO_DO_PRODUTO_OPER_RESULTANTE"));
/*  77 */         put(r, SimpField.VALOR_UNITARIO, m.get("VALOR_UNITARIO"));
/*  78 */         put(r, SimpField.RECIPIENTE_GLP, m.get("RECIPIENTE_GLP"));
/*  79 */         put(r, SimpField.CHAVE_ACESSO_NFE, m.get("CHAVE_DE_ACESSO_NF_E_DANFE"));
/*     */ 
/*     */         
/*  82 */         applySynonyms(m, r);
/*     */ 
/*     */         
/*  85 */         if (r.get(SimpField.CODIGO_OPERACAO) == null) {
/*  86 */           r.put(SimpField.CODIGO_OPERACAO, op.code());
/*     */         }
/*     */         
/*  89 */         out.add(r);
/*     */       } 
/*  91 */     }  return out;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void applySynonyms(Map m, SimpRecord r) throws Exception {
/*  97 */     if (m.containsKey("QTD_UN_KG") && r.get(SimpField.QTD_PRODUTO_KG) == null)
/*  98 */       r.put(SimpField.QTD_PRODUTO_KG, toBigDecimalSafe(m.get("QTD_UN_KG"))); 
/*  99 */     if (m.containsKey("COD_MODAL_SIMP") && r.get(SimpField.CODIGO_MODAL) == null)
/* 100 */       r.put(SimpField.CODIGO_MODAL, String.valueOf(m.get("COD_MODAL_SIMP"))); 
/* 101 */     if (m.containsKey("CNPJ_PARC") && r.get(SimpField.IDENTIFICACAO_TERCEIRO) == null)
/* 102 */       r.put(SimpField.IDENTIFICACAO_TERCEIRO, onlyDigits(String.valueOf(m.get("CNPJ_PARC")))); 
/*     */   } private String onlyDigits(String s) {
/* 104 */     return (s == null) ? null : s.replaceAll("\\D+", "");
/*     */   }
/*     */   private void put(SimpRecord r, SimpField f, Object v) throws Exception {
/* 107 */     if (v == null) { r.put(f, null);
/*     */       return; }
/*     */     
/* 110 */     if (f == SimpField.DATA_NF) {
/* 111 */       r.put(f, parseDate(v.toString()));
/*     */       
/*     */       return;
/*     */     } 
/* 115 */     if (f == SimpField.QTD_UN_MEDIDA_ANP || 
/* 116 */       f == SimpField.QTD_PRODUTO_KG || 
/* 117 */       f == SimpField.VALOR_CARACTERISTICA_PRECO_RS_KG || 
/* 118 */       f == SimpField.VALOR_UNITARIO) {
/* 119 */       r.put(f, toBigDecimalSafe(v));
/*     */       
/*     */       return;
/*     */     } 
/* 123 */     r.put(f, v.toString());
/*     */   }
/*     */   
/*     */   private BigDecimal toBigDecimalSafe(Object v) {
/* 127 */     if (v instanceof BigDecimal) return (BigDecimal)v; 
/* 128 */     return new BigDecimal(v.toString().replace(",", "."));
/*     */   }
/*     */   
/* 131 */   private static final SimpleDateFormat ISO = new SimpleDateFormat("yyyy-MM-dd");
/* 132 */   private static final SimpleDateFormat COMPACT = new SimpleDateFormat("yyyyMMdd");
/*     */   private Date parseDate(String s) {
/*     */     
/* 135 */     try { return ISO.parse(s); } catch (Exception exception) { 
/* 136 */       try { return COMPACT.parse(s); } catch (Exception exception1)
/*     */       
/* 138 */       { return new Date(); }
/*     */        }
/*     */   
/*     */   }
/*     */   
/*     */   private List<SimpRecord> fallback(Operation op) {
/* 144 */     List<SimpRecord> list = new ArrayList<>();
/*     */     
/* 146 */     Date hoje = new Date();
/* 147 */     SimpleDateFormat mmaaaa = new SimpleDateFormat("MMyyyy");
/*     */     
/* 149 */     SimpRecord r = new SimpRecord(op.code());
/*     */ 
/*     */     
/* 152 */     r.put(SimpField.CODIGO_OPERACAO, op.code());
/* 153 */     r.put(SimpField.CONTADOR_SEQUENCIAL, "1");
/* 154 */     r.put(SimpField.AGENTE_REGULADO_INFORMANTE, "1234567890");
/* 155 */     r.put(SimpField.MES_REFERENCIA_MMAAAA, mmaaaa.format(hoje));
/*     */     
/* 157 */     r.put(SimpField.CODIGO_INSTALACAO_1, "1234567");
/* 158 */     r.put(SimpField.CODIGO_INSTALACAO_2, "0000000");
/* 159 */     r.put(SimpField.CODIGO_PRODUTO_OPERADO, "271012500");
/*     */ 
/*     */     
/* 162 */     r.put(SimpField.QTD_UN_MEDIDA_ANP, new BigDecimal("1.00"));
/* 163 */     r.put(SimpField.QTD_PRODUTO_KG, new BigDecimal("1234.56"));
/*     */     
/* 165 */     r.put(SimpField.CODIGO_MODAL, "1");
/* 166 */     r.put(SimpField.CODIGO_VEICULO, "ABC1234");
/* 167 */     r.put(SimpField.IDENTIFICACAO_TERCEIRO, "12345678000199");
/* 168 */     r.put(SimpField.CODIGO_MUNICIPIO, "3550308");
/* 169 */     r.put(SimpField.CODIGO_ATIVIDADE_ECONOMICA, "46711");
/* 170 */     r.put(SimpField.CODIGO_PAIS, "0105");
/*     */     
/* 172 */     r.put(SimpField.LICENCA_IMPORTACAO_LI, "LI12345678");
/* 173 */     r.put(SimpField.DECLARACAO_IMPORTACAO_DI, "1234567890");
/*     */     
/* 175 */     r.put(SimpField.NUMERO_NF, "1234567");
/* 176 */     r.put(SimpField.CODIGO_SERIE_NF, "1A");
/* 177 */     r.put(SimpField.DATA_NF, hoje);
/*     */     
/* 179 */     r.put(SimpField.CODIGO_SERVICO_ACORDADO, "1");
/* 180 */     r.put(SimpField.CODIGO_CARACTERISTICA_FISICO_QUIM, "001");
/* 181 */     r.put(SimpField.CODIGO_METODO, "001");
/* 182 */     r.put(SimpField.MODALIDADE_FRETE, "01");
/*     */     
/* 184 */     r.put(SimpField.VALOR_CARACTERISTICA_PRECO_RS_KG, new BigDecimal("3.45"));
/* 185 */     r.put(SimpField.CODIGO_PROD_OPER_RESULTANTE, "271019320");
/* 186 */     r.put(SimpField.VALOR_UNITARIO, new BigDecimal("4.50"));
/*     */     
/* 188 */     r.put(SimpField.RECIPIENTE_GLP, "N");
/* 189 */     r.put(SimpField.CHAVE_ACESSO_NFE, makeNfeKey44());
/*     */     
/* 191 */     list.add(r);
/* 192 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   private String makeNfeKey44() {
/* 197 */     StringBuilder sb = new StringBuilder(44);
/* 198 */     for (int i = 0; i < 44; i++) {
/* 199 */       sb.append((char)(48 + i % 10));
/*     */     }
/* 201 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\domain\mapping\FixtureLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */