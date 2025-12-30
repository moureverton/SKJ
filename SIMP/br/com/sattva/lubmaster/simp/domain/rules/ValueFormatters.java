/*     */ package br.com.sattva.lubmaster.simp.domain.rules;
/*     */ import br.com.sattva.lubmaster.simp.config.SimpSettings;
/*     */ import br.com.sattva.lubmaster.simp.domain.model.FieldSpec;
/*     */ import br.com.sattva.lubmaster.simp.domain.model.MatrixSpec;
/*     */ import br.com.sattva.lubmaster.simp.domain.model.SimpField;
/*     */ import java.math.BigDecimal;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class ValueFormatters {
/*  15 */   private final SimpleDateFormat dfOut = new SimpleDateFormat("ddMMyyyy");
/*  16 */   private final SimpleDateFormat dfIso = new SimpleDateFormat("yyyy-MM-dd");
/*  17 */   private final SimpleDateFormat dfCompact = new SimpleDateFormat("yyyyMMdd");
/*     */ 
/*     */   
/*     */   public ValueFormatters(SimpSettings cfg) {}
/*     */ 
/*     */   
/*     */   public ValueFormatters() {}
/*     */   
/*     */   public String format(FieldSpec spec, Object v) {
/*     */     String s;
/*  27 */     if ("D".equals(spec.type)) {
/*  28 */       Date d = null;
/*  29 */       if (v instanceof Date) { d = (Date)v; }
/*  30 */       else if (v != null) { d = parseDate(v.toString()); }
/*  31 */        s = (d == null) ? "" : this.dfOut.format(d);
/*  32 */     } else if ("N".equals(spec.type)) {
/*     */       
/*  34 */       int implied = (spec.scale > 0) ? spec.scale : 2;
/*  35 */       s = toImpliedDecimal(v, implied);
/*     */     } else {
/*  37 */       s = (v == null) ? "" : v.toString();
/*     */     } 
/*     */ 
/*     */     
/*  41 */     if (s.length() < spec.length) {
/*  42 */       int padLen = spec.length - s.length();
/*  43 */       StringBuilder sb = new StringBuilder(spec.length);
/*  44 */       if ("R".equals(spec.align)) {
/*  45 */         for (int j = 0; j < padLen; ) { sb.append(spec.pad); j++; }
/*  46 */          sb.append(s);
/*  47 */         return sb.toString();
/*     */       } 
/*  49 */       sb.append(s);
/*  50 */       for (int i = 0; i < padLen; ) { sb.append(spec.pad); i++; }
/*  51 */        return sb.toString();
/*     */     } 
/*     */     
/*  54 */     return (s.length() > spec.length) ? s.substring(0, spec.length) : s;
/*     */   }
/*     */   private Date parseDate(String s) {
/*     */     
/*  58 */     try { return this.dfIso.parse(s); } catch (Exception exception) { 
/*  59 */       try { return this.dfCompact.parse(s); } catch (Exception exception1)
/*  60 */       { return null; }
/*     */        }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private String toImpliedDecimal(Object v, int implied) {
/*  67 */     if (v == null) return "";
/*     */     
/*  69 */     if (v instanceof BigDecimal) { bd = (BigDecimal)v; }
/*  70 */     else { bd = new BigDecimal(v.toString().replace(",", ".")); }
/*  71 */      if (implied < 0) implied = 2; 
/*  72 */     BigDecimal bd = bd.setScale(implied, 4);
/*  73 */     String raw = bd.toPlainString();
/*  74 */     int dot = raw.indexOf('.');
/*  75 */     if (dot >= 0) raw = String.valueOf(raw.substring(0, dot)) + raw.substring(dot + 1);
/*     */     
/*  77 */     raw = raw.replace(",", "");
/*     */     
/*  79 */     if (raw.startsWith("-"))
/*     */     {
/*  81 */       raw = raw.substring(1);
/*     */     }
/*  83 */     return raw;
/*     */   }
/*     */ 
/*     */   
/*     */   public static FieldSpec specFor(MatrixSpec m, String opCode, SimpField f) {
/*  88 */     List<FieldSpec> list = (m.rulesByOperation != null) ? (List<FieldSpec>)m.rulesByOperation.get(opCode) : null;
/*  89 */     if (list == null || list.isEmpty()) list = (m.rulesByOperation != null) ? (List<FieldSpec>)m.rulesByOperation.get("DEFAULT") : null; 
/*  90 */     if (list == null || list.isEmpty()) return null; 
/*  91 */     for (int i = 0; i < list.size(); i++) {
/*  92 */       FieldSpec fs = list.get(i);
/*  93 */       if (fs != null && fs.field == f) return fs; 
/*     */     } 
/*  95 */     return null;
/*     */   }
/*     */   
/*     */   public static Map<SimpField, FieldSpec> specMapFor(MatrixSpec m, String opCode) {
/*  99 */     Map<SimpField, FieldSpec> map = new HashMap<>();
/* 100 */     if (m.fieldsOrder == null) return map;
/*     */     
/* 102 */     List<SimpField> missing = new ArrayList<>();
/* 103 */     for (int i = 0; i < m.fieldsOrder.size(); i++) {
/* 104 */       SimpField f = m.fieldsOrder.get(i);
/* 105 */       FieldSpec fs = specFor(m, opCode, f);
/* 106 */       if (fs == null) missing.add(f); 
/* 107 */       map.put(f, fs);
/*     */     } 
/* 109 */     if (!missing.isEmpty()) {
/* 110 */       throw new IllegalStateException(
/* 111 */           "MatrixSpec sem definição para os campos " + missing + 
/* 112 */           " na operação '" + opCode + "'. Verifique rulesByOperation (ou DEFAULT) do v1_11.json.");
/*     */     }
/*     */     
/* 115 */     return map;
/*     */   }
/*     */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\domain\rules\ValueFormatters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */