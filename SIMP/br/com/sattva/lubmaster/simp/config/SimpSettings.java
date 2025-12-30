/*    */ package br.com.sattva.lubmaster.simp.config;
/*    */ 
/*    */ import java.util.Date;
/*    */ 
/*    */ public final class SimpSettings {
/*    */   private final int codEmp;
/*    */   private final Date dtIni;
/*    */   private final Date dtFim;
/*    */   private final String outDir;
/*    */   private final String charset;
/*    */   private final boolean debug;
/*    */   private final String matrixVersion;
/*    */   private final boolean offline;
/*    */   
/*    */   public int getCodEmp() {
/* 16 */     return this.codEmp;
/* 17 */   } public Date getDtIni() { return this.dtIni; }
/* 18 */   public Date getDtFim() { return this.dtFim; }
/* 19 */   public String getOutDir() { return this.outDir; }
/* 20 */   public String getCharset() { return this.charset; }
/* 21 */   public boolean isDebug() { return this.debug; }
/* 22 */   public String getMatrixVersion() { return this.matrixVersion; } public boolean isOffline() {
/* 23 */     return this.offline;
/*    */   }
/*    */ 
/*    */   
/*    */   public static SimpSettings fromArgs(String[] args) {
/* 28 */     return (new Builder())
/* 29 */       .codEmp(1)
/* 30 */       .period(new Date(System.currentTimeMillis() - 604800000L), new Date())
/* 31 */       .outDir("C:/tmp")
/* 32 */       .charset("ISO-8859-1")
/* 33 */       .debug(true)
/* 34 */       .matrixVersion("v1_11")
/* 35 */       .offline(false)
/* 36 */       .build();
/*    */   }
/*    */   
/*    */   public static final class Builder {
/*    */     private int codEmp;
/*    */     private Date dtIni;
/*    */     private Date dtFim;
/*    */     private String outDir;
/* 44 */     private String charset = "ISO-8859-1";
/*    */     private boolean debug;
/* 46 */     private String matrixVersion = "v1_11";
/*    */     private boolean offline;
/*    */     
/* 49 */     public Builder codEmp(int v) { this.codEmp = v; return this; }
/* 50 */     public Builder period(Date ini, Date fim) { this.dtIni = ini; this.dtFim = fim; return this; }
/* 51 */     public Builder outDir(String v) { this.outDir = v; return this; }
/* 52 */     public Builder charset(String v) { this.charset = v; return this; }
/* 53 */     public Builder debug(boolean v) { this.debug = v; return this; }
/* 54 */     public Builder matrixVersion(String v) { this.matrixVersion = v; return this; } public Builder offline(boolean v) {
/* 55 */       this.offline = v; return this;
/*    */     }
/*    */     public SimpSettings build() {
/* 58 */       return new SimpSettings(this.codEmp, this.dtIni, this.dtFim, this.outDir, this.charset, this.debug, this.matrixVersion, this.offline, null);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private SimpSettings(int codEmp, Date dtIni, Date dtFim, String outDir, String charset, boolean debug, String mv, boolean offline) {
/* 64 */     this.codEmp = codEmp;
/* 65 */     this.dtIni = dtIni;
/* 66 */     this.dtFim = dtFim;
/* 67 */     this.outDir = outDir;
/* 68 */     this.charset = charset;
/* 69 */     this.debug = debug;
/* 70 */     this.matrixVersion = mv;
/* 71 */     this.offline = offline;
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\config\SimpSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */