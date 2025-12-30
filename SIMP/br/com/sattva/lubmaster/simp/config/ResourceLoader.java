/*     */ package br.com.sattva.lubmaster.simp.config;
/*     */ import br.com.sattva.lubmaster.simp.domain.model.FieldRule;
/*     */ import br.com.sattva.lubmaster.simp.domain.model.FieldSpec;
/*     */ import br.com.sattva.lubmaster.simp.domain.model.MatrixSpec;
/*     */ import br.com.sattva.lubmaster.simp.domain.model.SimpField;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class ResourceLoader {
/*     */   public ResourceLoader(SimpSettings cfg) {
/*  17 */     this.cfg = cfg;
/*     */   } private final SimpSettings cfg;
/*     */   public String readText(String path) {
/*  20 */     BufferedReader br = null;
/*     */     try {
/*  22 */       InputStream is = getStream(path);
/*  23 */       if (is == null) return null; 
/*  24 */       br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
/*  25 */       StringBuilder sb = new StringBuilder();
/*     */       String line;
/*  27 */       while ((line = br.readLine()) != null) {
/*  28 */         sb.append(line).append('\n');
/*     */       }
/*  30 */       return sb.toString();
/*  31 */     } catch (Exception e) {
/*  32 */       throw new RuntimeException("Falha ao ler recurso: " + path, e);
/*     */     } finally {
/*  34 */       if (br != null) try { br.close(); } catch (Exception exception) {} 
/*     */     } 
/*     */   }
/*     */   
/*     */   public MatrixSpec readMatrix(String path) {
/*  39 */     String json = readText(path);
/*  40 */     if (json != null) {
/*     */       
/*     */       try {
/*  43 */         Class<?> gsonCls = Class.forName("com.google.gson.Gson");
/*  44 */         Object gson = gsonCls.newInstance();
/*  45 */         Method fromJson = gsonCls.getMethod("fromJson", new Class[] { String.class, Class.class });
/*  46 */         MatrixSpec spec = (MatrixSpec)fromJson.invoke(gson, new Object[] { json, MatrixSpec.class });
/*  47 */         if (spec != null) return spec; 
/*  48 */       } catch (Throwable throwable) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  53 */     return defaultMatrix(this.cfg.getMatrixVersion());
/*     */   }
/*     */   
/*     */   private InputStream getStream(String path) throws Exception {
/*  57 */     String p = path;
/*  58 */     if (p == null) return null; 
/*  59 */     if (!p.startsWith("/")) p = "/" + p;
/*     */     
/*  61 */     InputStream is = ResourceLoader.class.getResourceAsStream(p);
/*  62 */     if (is != null) return is;
/*     */ 
/*     */     
/*  65 */     File f = new File(path);
/*  66 */     if (f.exists() && f.isFile()) return new FileInputStream(f); 
/*  67 */     return null;
/*     */   }
/*     */   
/*     */   private MatrixSpec defaultMatrix(String version) {
/*  71 */     MatrixSpec m = new MatrixSpec();
/*  72 */     m.version = (version != null) ? version : "fallback";
/*     */     
/*  74 */     m.fieldsOrder = new ArrayList();
/*  75 */     addOrder(m, new SimpField[] { SimpField.CONTADOR_SEQUENCIAL, SimpField.AGENTE_REGULADO_INFORMANTE, SimpField.MES_REFERENCIA_MMAAAA, 
/*  76 */           SimpField.CODIGO_OPERACAO, SimpField.CODIGO_INSTALACAO_1, SimpField.CODIGO_INSTALACAO_2, 
/*  77 */           SimpField.CODIGO_PRODUTO_OPERADO, SimpField.QTD_UN_MEDIDA_ANP, SimpField.QTD_PRODUTO_KG, 
/*  78 */           SimpField.CODIGO_MODAL, SimpField.CODIGO_VEICULO, SimpField.IDENTIFICACAO_TERCEIRO, 
/*  79 */           SimpField.CODIGO_MUNICIPIO, SimpField.CODIGO_ATIVIDADE_ECONOMICA, SimpField.CODIGO_PAIS, 
/*  80 */           SimpField.LICENCA_IMPORTACAO_LI, SimpField.DECLARACAO_IMPORTACAO_DI, SimpField.NUMERO_NF, 
/*  81 */           SimpField.CODIGO_SERIE_NF, SimpField.DATA_NF, SimpField.CODIGO_SERVICO_ACORDADO, 
/*  82 */           SimpField.CODIGO_CARACTERISTICA_FISICO_QUIM, SimpField.CODIGO_METODO, SimpField.MODALIDADE_FRETE, 
/*  83 */           SimpField.VALOR_CARACTERISTICA_PRECO_RS_KG, SimpField.CODIGO_PROD_OPER_RESULTANTE, 
/*  84 */           SimpField.VALOR_UNITARIO, SimpField.RECIPIENTE_GLP, SimpField.CHAVE_ACESSO_NFE });
/*     */     
/*  86 */     List<FieldSpec> base = new ArrayList<>();
/*     */     
/*  88 */     base.add(fs(SimpField.CONTADOR_SEQUENCIAL, 10, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/*  89 */     base.add(fs(SimpField.AGENTE_REGULADO_INFORMANTE, 10, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/*  90 */     base.add(fs(SimpField.MES_REFERENCIA_MMAAAA, 6, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/*  91 */     base.add(fs(SimpField.CODIGO_OPERACAO, 7, "X", 0, ' ', "L", FieldRule.OBRIGATORIO, null));
/*  92 */     base.add(fs(SimpField.CODIGO_INSTALACAO_1, 7, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/*  93 */     base.add(fs(SimpField.CODIGO_INSTALACAO_2, 7, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/*  94 */     base.add(fs(SimpField.CODIGO_PRODUTO_OPERADO, 9, "N", 0, '0', "R", FieldRule.OBRIGATORIO, null));
/*  95 */     base.add(fs(SimpField.QTD_UN_MEDIDA_ANP, 15, "N", 2, '0', "R", FieldRule.OPCIONAL, null));
/*  96 */     base.add(fs(SimpField.QTD_PRODUTO_KG, 15, "N", 2, '0', "R", FieldRule.OBRIGATORIO, null));
/*  97 */     base.add(fs(SimpField.CODIGO_MODAL, 1, "X", 0, ' ', "L", FieldRule.OBRIGATORIO, null));
/*  98 */     base.add(fs(SimpField.CODIGO_VEICULO, 7, "X", 0, ' ', "L", FieldRule.OPCIONAL, null));
/*  99 */     base.add(fs(SimpField.IDENTIFICACAO_TERCEIRO, 14, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 100 */     base.add(fs(SimpField.CODIGO_MUNICIPIO, 7, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 101 */     base.add(fs(SimpField.CODIGO_ATIVIDADE_ECONOMICA, 5, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 102 */     base.add(fs(SimpField.CODIGO_PAIS, 4, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 103 */     base.add(fs(SimpField.LICENCA_IMPORTACAO_LI, 10, "X", 0, ' ', "L", FieldRule.OPCIONAL, null));
/* 104 */     base.add(fs(SimpField.DECLARACAO_IMPORTACAO_DI, 10, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 105 */     base.add(fs(SimpField.NUMERO_NF, 7, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 106 */     base.add(fs(SimpField.CODIGO_SERIE_NF, 2, "X", 0, ' ', "L", FieldRule.OPCIONAL, null));
/* 107 */     base.add(fs(SimpField.DATA_NF, 8, "D", 0, '0', "R", FieldRule.OBRIGATORIO, null));
/* 108 */     base.add(fs(SimpField.CODIGO_SERVICO_ACORDADO, 1, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 109 */     base.add(fs(SimpField.CODIGO_CARACTERISTICA_FISICO_QUIM, 3, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 110 */     base.add(fs(SimpField.CODIGO_METODO, 3, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 111 */     base.add(fs(SimpField.MODALIDADE_FRETE, 2, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 112 */     base.add(fs(SimpField.VALOR_CARACTERISTICA_PRECO_RS_KG, 10, "N", 2, '0', "R", FieldRule.OPCIONAL, null));
/* 113 */     base.add(fs(SimpField.CODIGO_PROD_OPER_RESULTANTE, 9, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 114 */     base.add(fs(SimpField.VALOR_UNITARIO, 7, "N", 2, '0', "R", FieldRule.OPCIONAL, null));
/* 115 */     base.add(fs(SimpField.RECIPIENTE_GLP, 2, "X", 0, ' ', "L", FieldRule.OPCIONAL, null));
/* 116 */     base.add(fs(SimpField.CHAVE_ACESSO_NFE, 44, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/*     */     
/* 118 */     m.rulesByOperation = new HashMap<>();
/* 119 */     m.rulesByOperation.put("DEFAULT", base);
/*     */     
/* 121 */     m.rulesByOperation.put("1011001", base);
/* 122 */     m.rulesByOperation.put("1011009", base);
/*     */     
/* 124 */     return m;
/*     */   }
/*     */   
/*     */   private void addOrder(MatrixSpec m, SimpField... fs) {
/* 128 */     for (int i = 0; i < fs.length; ) { m.fieldsOrder.add(fs[i]); i++; }
/*     */   
/*     */   }
/*     */   private static FieldSpec fs(SimpField f, int len, String type, int scale, char pad, String align, FieldRule rule, String cond) {
/* 132 */     FieldSpec x = new FieldSpec();
/* 133 */     x.field = f; x.length = len; x.type = type; x.scale = scale;
/* 134 */     x.pad = pad; x.align = align; x.rule = rule; x.condition = cond;
/* 135 */     return x;
/*     */   }
/*     */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\config\ResourceLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */