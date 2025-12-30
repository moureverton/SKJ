/*     */ package br.com.sattva.lubmaster.simp.domain.mapping;
/*     */ import br.com.sattva.lubmaster.simp.config.SimpSettings;
/*     */ import br.com.sattva.lubmaster.simp.domain.model.Operation;
/*     */ import br.com.sattva.lubmaster.simp.domain.model.SimpField;
/*     */ import br.com.sattva.lubmaster.simp.domain.model.SimpRecord;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.math.BigDecimal;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public final class MappingRegistry {
/*     */   public MappingRegistry(SimpSettings cfg) {
/*  22 */     this.cfg = cfg;
/*  23 */     this.fixtures = new FixtureLoader(cfg);
/*     */   }
/*     */   private final SimpSettings cfg;
/*     */   private final FixtureLoader fixtures;
/*     */   
/*     */   public Map<String, Object> defaultParams(SimpSettings cfgArg) {
/*  29 */     SimpSettings c = (cfgArg != null) ? cfgArg : this.cfg;
/*  30 */     Map<String, Object> p = new HashMap<>();
/*  31 */     if (c != null) {
/*  32 */       p.put("codEmp", Integer.valueOf(c.getCodEmp()));
/*  33 */       p.put("dtIni", c.getDtIni());
/*  34 */       p.put("dtFim", c.getDtFim());
/*  35 */       p.put("matrixVersion", c.getMatrixVersion());
/*     */     } 
/*  37 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<SimpRecord> mockRecords(Operation op, SimpSettings cfgArg) throws Exception {
/*  43 */     return this.fixtures.load(op);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpRecord mapRow(Operation op, ResultSet rs) throws Exception {
/*  49 */     SimpRecord r = new SimpRecord(op.code());
/*  50 */     r.put(SimpField.CODIGO_OPERACAO, op.code());
/*     */ 
/*     */     
/*  53 */     r.put(SimpField.CONTADOR_SEQUENCIAL, digits(anyS(rs, "CONTADOR_SEQUENCIAL")));
/*  54 */     r.put(SimpField.AGENTE_REGULADO_INFORMANTE, digits(anyS(rs, "AGENTE_REGULADO_INFORMANTE")));
/*  55 */     r.put(SimpField.MES_REFERENCIA_MMAAAA, digits(anyS(rs, "MES_REFERENCIA_MMAAAA")));
/*  56 */     r.put(SimpField.CODIGO_INSTALACAO_1, digits(anyS(rs, "CODIGO_INSTALACAO_1")));
/*  57 */     r.put(SimpField.CODIGO_INSTALACAO_2, digits(anyS(rs, "CODIGO_INSTALACAO_2")));
/*  58 */     r.put(SimpField.CODIGO_PRODUTO_OPERADO, digits(anyS(rs, "CODIGO_PRODUTO_OPERADO", "CODPROD_ANP")));
/*  59 */     r.put(SimpField.CODIGO_MODAL, anyS(rs, "CODIGO_MODAL", "CODMODAL_SIMP"));
/*  60 */     r.put(SimpField.CODIGO_VEICULO, anyS(rs, "CODIGO_VEICULO"));
/*  61 */     r.put(SimpField.IDENTIFICACAO_TERCEIRO, digits(anyS(rs, "IDENTIFICACAO_TERCEIRO", "CNPJ_PARC")));
/*  62 */     r.put(SimpField.CODIGO_MUNICIPIO, digits(anyS(rs, "CODIGO_MUNICIPIO")));
/*  63 */     r.put(SimpField.CODIGO_ATIVIDADE_ECONOMICA, digits(anyS(rs, "CODIGO_ATIVIDADE_ECONOMICA")));
/*  64 */     r.put(SimpField.CODIGO_PAIS, digits(anyS(rs, "CODIGO_PAIS")));
/*  65 */     r.put(SimpField.LICENCA_IMPORTACAO_LI, anyS(rs, "LICENCA_IMPORTACAO_LI"));
/*  66 */     r.put(SimpField.DECLARACAO_IMPORTACAO_DI, digits(anyS(rs, "DECLARACAO_IMPORTACAO_DI", "DI")));
/*  67 */     r.put(SimpField.NUMERO_NF, digits(anyS(rs, "NUMERO_NF")));
/*  68 */     r.put(SimpField.CODIGO_SERIE_NF, anyS(rs, "CODIGO_SERIE_NF", "SERIE_NF"));
/*  69 */     r.put(SimpField.CODIGO_SERVICO_ACORDADO, digits(anyS(rs, "CODIGO_SERVICO_ACORDADO")));
/*  70 */     r.put(SimpField.CODIGO_CARACTERISTICA_FISICO_QUIM, digits(anyS(rs, "CODIGO_CARACTERISTICA_FISICO_QUIM")));
/*  71 */     r.put(SimpField.CODIGO_METODO, digits(anyS(rs, "CODIGO_METODO")));
/*  72 */     r.put(SimpField.MODALIDADE_FRETE, digits(anyS(rs, "MODALIDADE_FRETE")));
/*  73 */     r.put(SimpField.CODIGO_PROD_OPER_RESULTANTE, digits(anyS(rs, "CODIGO_PROD_OPER_RESULTANTE")));
/*  74 */     r.put(SimpField.RECIPIENTE_GLP, anyS(rs, "RECIPIENTE_GLP"));
/*  75 */     r.put(SimpField.CHAVE_ACESSO_NFE, digits(anyS(rs, "CHAVE_ACESSO_NFE", "CHAVE_NFE", "CHAVE_DE_ACESSO")));
/*     */ 
/*     */     
/*  78 */     r.put(SimpField.DATA_NF, anyD(rs, "DATA_NF", "DTNF", "DTNEG"));
/*     */ 
/*     */     
/*  81 */     r.put(SimpField.QTD_UN_MEDIDA_ANP, anyBD(rs, "QTD_UN_MEDIDA_ANP", "QTD_UN_ANP"));
/*  82 */     r.put(SimpField.QTD_PRODUTO_KG, anyBD(rs, "QTD_PRODUTO_KG", "QTD_CONVERTIDO_KG"));
/*  83 */     r.put(SimpField.VALOR_CARACTERISTICA_PRECO_RS_KG, anyBD(rs, "VALOR_CARACTERISTICA_PRECO_RS_KG", "PRECO_RS_KG"));
/*  84 */     r.put(SimpField.VALOR_UNITARIO, anyBD(rs, "VALOR_UNITARIO", "VLR_UNIT"));
/*     */     
/*  86 */     return r;
/*     */   }
/*     */   
/*     */   private String s(ResultSet rs, String col) {
/*     */     
/*  91 */     try { String v = rs.getString(col); return (v != null) ? v.trim() : null; } catch (Exception e) { return null; }
/*     */      } private String anyS(ResultSet rs, String c1) {
/*  93 */     return s(rs, c1);
/*     */   } private String anyS(ResultSet rs, String c1, String c2) {
/*  95 */     String v = s(rs, c1); return (v != null) ? v : s(rs, c2);
/*     */   }
/*     */   private String anyS(ResultSet rs, String c1, String c2, String c3) {
/*  98 */     String v = s(rs, c1); if (v != null) return v;  v = s(rs, c2); return (v != null) ? v : s(rs, c3);
/*     */   }
/*     */   
/*     */   private BigDecimal n(ResultSet rs, String col) { 
/* 102 */     try { Object o = rs.getObject(col);
/* 103 */       if (o == null) return null; 
/* 104 */       if (o instanceof BigDecimal) return (BigDecimal)o; 
/* 105 */       String t = o.toString().replace(",", ".").trim();
/* 106 */       if (t.length() == 0) return null; 
/* 107 */       return new BigDecimal(t); }
/* 108 */     catch (Exception e) { return null; }
/*     */      } private BigDecimal anyBD(ResultSet rs, String c1) {
/* 110 */     return n(rs, c1);
/*     */   } private BigDecimal anyBD(ResultSet rs, String c1, String c2) {
/* 112 */     BigDecimal v = n(rs, c1); return (v != null) ? v : n(rs, c2);
/*     */   }
/*     */   private Date d(ResultSet rs, String col) { 
/* 115 */     try { Timestamp ts = rs.getTimestamp(col); if (ts != null) return new Date(ts.getTime());  } catch (Exception exception) {}
/* 116 */     String txt = s(rs, col);
/* 117 */     if (txt == null || txt.length() == 0) return null; 
/* 118 */     SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
/* 119 */     SimpleDateFormat f2 = new SimpleDateFormat("yyyyMMdd");
/* 120 */     SimpleDateFormat f3 = new SimpleDateFormat("ddMMyyyy"); 
/* 121 */     try { return f1.parse(txt); } catch (Exception exception) { 
/* 122 */       try { return f2.parse(txt); } catch (Exception exception1) { 
/* 123 */         try { return f3.parse(txt); } catch (Exception exception2)
/* 124 */         { return null; }  }
/*     */        }
/* 126 */      } private Date anyD(ResultSet rs, String c1) { return d(rs, c1); }
/*     */    private Date anyD(ResultSet rs, String c1, String c2) {
/* 128 */     Date v = d(rs, c1); return (v != null) ? v : d(rs, c2);
/*     */   }
/*     */   private Date anyD(ResultSet rs, String c1, String c2, String c3) {
/* 131 */     Date v = d(rs, c1); if (v != null) return v;  v = d(rs, c2); return (v != null) ? v : d(rs, c3);
/*     */   } private String digits(String s) {
/* 133 */     return (s == null) ? null : s.replaceAll("\\D+", "");
/*     */   } public List<SimpRecord> emptyList() {
/* 135 */     return new ArrayList<>();
/*     */   }
/*     */   
/*     */   public List<SimpRecord> mapResultSet(String opCode, ResultSet rs) throws Exception {
/* 139 */     List<SimpRecord> out = new ArrayList<>();
/*     */     
/* 141 */     ResultSetMetaData md = rs.getMetaData();
/* 142 */     int colCount = md.getColumnCount();
/*     */ 
/*     */     
/* 145 */     Class<?> cls = SimpRecord.class;
/* 146 */     Method factory = null;
/*     */     try {
/* 148 */       factory = cls.getMethod("of", new Class[] { String.class });
/* 149 */     } catch (Throwable t) {
/* 150 */       factory = null;
/*     */     } 
/*     */     
/* 153 */     Method put = null;
/*     */     try {
/* 155 */       put = cls.getMethod("put", new Class[] { String.class, String.class });
/* 156 */     } catch (Throwable t) {
/*     */       try {
/* 158 */         put = cls.getMethod("set", new Class[] { String.class, String.class });
/* 159 */       } catch (Throwable t2) {
/* 160 */         put = null;
/*     */       } 
/*     */     } 
/*     */     
/* 164 */     Constructor<?> ctor = null;
/* 165 */     if (factory == null) {
/*     */       try {
/* 167 */         ctor = cls.getConstructor(new Class[] { String.class });
/* 168 */       } catch (Throwable t) {
/* 169 */         ctor = null;
/*     */       } 
/*     */     }
/*     */     
/* 173 */     while (rs.next()) {
/*     */       SimpRecord rec;
/* 175 */       if (factory != null) {
/* 176 */         rec = (SimpRecord)factory.invoke(null, new Object[] { opCode });
/* 177 */       } else if (ctor != null) {
/* 178 */         rec = (SimpRecord)ctor.newInstance(new Object[] { opCode });
/*     */       } else {
/*     */         
/* 181 */         rec = (SimpRecord)cls.newInstance();
/*     */         try {
/* 183 */           Method setOp = cls.getMethod("setOpCode", new Class[] { String.class });
/* 184 */           setOp.invoke(rec, new Object[] { opCode });
/* 185 */         } catch (Throwable throwable) {}
/*     */       } 
/*     */       
/* 188 */       for (int i = 1; i <= colCount; i++) {
/* 189 */         String alias = md.getColumnLabel(i);
/* 190 */         if (alias == null || alias.trim().isEmpty()) alias = md.getColumnName(i); 
/* 191 */         String val = rs.getString(i);
/* 192 */         if (alias != null && put != null) {
/* 193 */           put.invoke(rec, new Object[] { alias, val });
/*     */         }
/*     */       } 
/* 196 */       out.add(rec);
/*     */     } 
/* 198 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getRequiredAliases(String opCode) {
/* 203 */     Set<String> out = new HashSet<>();
/*     */     
/*     */     try {
/* 206 */       Object rules = null;
/*     */       try {
/* 208 */         Field fRules = getClass().getDeclaredField("rules");
/* 209 */         fRules.setAccessible(true);
/* 210 */         rules = fRules.get(this);
/* 211 */       } catch (Throwable ignore) {
/* 212 */         rules = null;
/*     */       } 
/*     */       
/* 215 */       Object matrix = null;
/* 216 */       if (rules != null) {
/*     */         try {
/* 218 */           Method m = rules.getClass().getMethod("getMatrix", new Class[0]);
/* 219 */           matrix = m.invoke(rules, new Object[0]);
/* 220 */         } catch (Throwable ignore) {
/*     */           
/*     */           try {
/* 223 */             Field fMatrix = getClass().getDeclaredField("matrix");
/* 224 */             fMatrix.setAccessible(true);
/* 225 */             matrix = fMatrix.get(this);
/* 226 */           } catch (Throwable ignore2) {
/* 227 */             matrix = null;
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         
/*     */         try {
/* 233 */           Field fMatrix = getClass().getDeclaredField("matrix");
/* 234 */           fMatrix.setAccessible(true);
/* 235 */           matrix = fMatrix.get(this);
/* 236 */         } catch (Throwable ignore) {
/* 237 */           matrix = null;
/*     */         } 
/*     */       } 
/*     */       
/* 241 */       if (matrix == null) {
/* 242 */         return Collections.emptySet();
/*     */       }
/*     */ 
/*     */       
/* 246 */       Object lineSpec = null;
/*     */       try {
/* 248 */         Method getLineSpec = matrix.getClass().getMethod("getLineSpec", new Class[] { String.class });
/* 249 */         lineSpec = getLineSpec.invoke(matrix, new Object[] { opCode });
/* 250 */       } catch (Throwable ignore) {
/* 251 */         return Collections.emptySet();
/*     */       } 
/* 253 */       if (lineSpec == null) {
/* 254 */         return Collections.emptySet();
/*     */       }
/*     */ 
/*     */       
/* 258 */       Object fieldsList = null;
/*     */       try {
/* 260 */         Method fields = lineSpec.getClass().getMethod("fields", new Class[0]);
/* 261 */         fieldsList = fields.invoke(lineSpec, new Object[0]);
/* 262 */       } catch (Throwable ignore) {
/* 263 */         return Collections.emptySet();
/*     */       } 
/* 265 */       if (!(fieldsList instanceof List)) {
/* 266 */         return Collections.emptySet();
/*     */       }
/*     */       
/* 269 */       List<?> list = (List)fieldsList;
/* 270 */       for (int i = 0; i < list.size(); i++) {
/* 271 */         Object fieldSpec = list.get(i);
/* 272 */         if (fieldSpec != null) {
/*     */           try {
/* 274 */             Method name = fieldSpec.getClass().getMethod("name", new Class[0]);
/* 275 */             Object n = name.invoke(fieldSpec, new Object[0]);
/* 276 */             if (n != null) {
/* 277 */               String s = n.toString().trim();
/* 278 */               if (!s.isEmpty()) {
/* 279 */                 out.add(s.toUpperCase());
/*     */               }
/*     */             } 
/* 282 */           } catch (Throwable throwable) {}
/*     */         }
/*     */       } 
/*     */       
/* 286 */       return out.isEmpty() ? Collections.<String>emptySet() : out;
/*     */     }
/* 288 */     catch (Throwable t) {
/*     */       
/* 290 */       return Collections.emptySet();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\domain\mapping\MappingRegistry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */