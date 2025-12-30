/*    */ package br.com.sattva.lubmaster.simp.config;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ public final class OperationCatalog {
/*    */   public static final class OpInfo { public final String code;
/*    */     
/*    */     public OpInfo(String code, String desc, String group) {
/* 13 */       this.code = code; this.descricao = desc; this.group = group;
/*    */     }
/*    */     public final String descricao;
/*    */     public final String group; }
/* 17 */   private final Map<String, OpInfo> byCode = new HashMap<>();
/* 18 */   private final Map<String, List<String>> groupToCodes = new HashMap<>();
/*    */   
/*    */   public static OperationCatalog load(ResourceLoader loader, String pathOrNull) {
/* 21 */     String csv = (pathOrNull != null) ? loader.readText(pathOrNull) : loader.readText("./config/Opercoes.csv");
/* 22 */     if (csv == null || csv.trim().isEmpty()) {
/* 23 */       throw new IllegalStateException("Operacoes.csv não encontrado. Esperado em /config/Operacoes.csv (classpath) ou forneça caminho absoluto.");
/*    */     }
/* 25 */     return parse(csv);
/*    */   }
/*    */   
/*    */   private static OperationCatalog parse(String csv) {
/* 29 */     OperationCatalog cat = new OperationCatalog();
/* 30 */     BufferedReader br = null;
/*    */     
/* 32 */     try { br = new BufferedReader(new StringReader(csv));
/*    */       
/* 34 */       boolean first = true; String line;
/* 35 */       while ((line = br.readLine()) != null) {
/* 36 */         line = line.trim();
/* 37 */         if (line.length() == 0)
/*    */           continue; 
/* 39 */         if (first) { first = false; if (line.toLowerCase().contains("código") || line.toLowerCase().contains("codigo"))
/* 40 */             continue;  }  String[] parts = line.split(";", -1);
/* 41 */         if (parts.length < 3)
/* 42 */           continue;  String code = parts[0].trim();
/* 43 */         String desc = parts[1].trim();
/* 44 */         String group = parts[2].trim();
/* 45 */         OpInfo info = new OpInfo(code, desc, group);
/* 46 */         cat.byCode.put(code, info);
/* 47 */         List<String> list = cat.groupToCodes.get(group);
/* 48 */         if (list == null) { list = new ArrayList<>(); cat.groupToCodes.put(group, list); }
/* 49 */          list.add(code);
/*    */       } 
/* 51 */       return cat; }
/* 52 */     catch (Exception e)
/* 53 */     { throw new RuntimeException("Falha ao parsear Operacoes.csv", e); }
/*    */     finally { 
/* 55 */       try { if (br != null) br.close();  } catch (Exception exception) {} }
/*    */   
/*    */   }
/*    */   public Collection<OpInfo> all() {
/* 59 */     return this.byCode.values();
/*    */   }
/*    */   public boolean isAggregator(String code) {
/* 62 */     return (code != null && code.endsWith("998"));
/*    */   }
/*    */   
/*    */   public String groupOf(String code) {
/* 66 */     OpInfo i = this.byCode.get(code);
/* 67 */     return (i != null) ? i.group : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> nonAggregatorsInGroup(String group) {
/* 72 */     List<String> list = this.groupToCodes.get(group);
/* 73 */     List<String> out = new ArrayList<>();
/* 74 */     if (list != null)
/* 75 */       for (int i = 0; i < list.size(); i++) {
/* 76 */         String c = list.get(i);
/* 77 */         if (!isAggregator(c)) out.add(c);
/*    */       
/*    */       }  
/* 80 */     return out;
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\config\OperationCatalog.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */