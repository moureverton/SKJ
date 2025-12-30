/*     */ package buscar;
/*     */ 
/*     */ import br.com.sankhya.jape.dao.JdbcWrapper;
/*     */ import java.math.BigDecimal;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class buscarTotalizadores
/*     */ {
/*     */   public static ResultSet validarcaoTotalizadorExiste(JdbcWrapper jdbcWrapper, BigDecimal codSimulaSimp, String codEmpAnp, String mesRefrencia, String instalacao, String codProdAnp, BigDecimal codOperTotalizadorAnp) throws Exception {
/*  48 */     String sql = "SELECT \r\n\r\nA.OPERTOTALIZADOR,\r\nB.DESCRICAO,\r\nB.CODOPER_ANP,\r\nB.CLASSE\r\nFROM AD_DETSIMULARSIMP A\r\nLEFT JOIN AD_OPERACAOANP B ON A.CODOPER = B.CODOPER\r\nWHERE A.CODSIMSIM = " + 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  56 */       codSimulaSimp + "\r\n" + 
/*  57 */       "AND CODEMP_ANP = '" + codEmpAnp + "'\r\n" + 
/*  58 */       "AND MESREFERENCIA = '" + mesRefrencia + "'\r\n" + 
/*  59 */       "AND CODINSTALACAO1 = '" + instalacao + "'\r\n" + 
/*  60 */       "AND CODPROD_ANP = '" + codProdAnp + "'\r\n" + 
/*  61 */       "AND A.CODOPER = " + codOperTotalizadorAnp;
/*  62 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/*  63 */     ResultSet query = pstm.executeQuery();
/*  64 */     return query;
/*     */   }
/*     */   
/*     */   public static ResultSet buscarOperacaoAnp(JdbcWrapper jdbcWrapper, BigDecimal codOperTotalizadorAnp) throws Exception {
/*  68 */     String sql = "SELECT CODOPER_ANP FROM AD_OPERACAOANP WHERE CODOPER = " + codOperTotalizadorAnp;
/*  69 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/*  70 */     ResultSet query = pstm.executeQuery();
/*  71 */     return query;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ResultSet totalizadorPorOperacao(JdbcWrapper jdbcWrapper, BigDecimal codSimulaSimp, String codEmpAnp, String mesRefrencia, String instalacao, String codProdAnp) throws Exception {
/*  76 */     String sql = "SELECT \r\n(SELECT CODOPER FROM AD_OPERACAOANP OPER WHERE OPER.TIPO= T.TIPO AND OPER.FINALIDADE = T.FINALIDADE AND OPER.CLASSE = T.CLASSE AND OPER.SEQ = 998) AS OPER_TOT,\r\nT.* \r\nFROM (SELECT \r\nA.CODEMP_ANP,\r\nA.MESREFERENCIA,\r\nA.CODINSTALACAO1,\r\nA.CODPROD_ANP,\r\nSUM(TO_NUMBER(COALESCE(A.QTD_PROD_UN_ANP,'0'))) AS QTD_PROD_UN_ANP,\r\nSUM(TO_NUMBER(COALESCE(A.QTD_PROD_OPERADO_KG,'0'))) AS QTD_PROD_OPERADO_KG,\r\nSUM(TO_NUMBER(COALESCE(A.QTD_PROD_UN_ANP,'0'))*cast(replace(A.VLRUNIT_NF,'.',',') as numeric(10,6))) AS VLR_PROD_UN_ANP,B.TIPO,\r\nB.FINALIDADE,\r\nB.CLASSE\r\nFROM AD_DETSIMULARSIMP A\r\nINNER JOIN AD_OPERACAOANP B ON A.CODOPER = B.CODOPER\r\nWHERE A.CODSIMSIM = " + 
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
/*  91 */       codSimulaSimp + "\r\n" + 
/*  92 */       "AND OPERTOTALIZADOR > 0\r\n" + 
/*  93 */       "AND CODEMP_ANP = '" + codEmpAnp + "'\r\n" + 
/*  94 */       "AND MESREFERENCIA = '" + mesRefrencia + "'\r\n" + 
/*  95 */       "AND CODINSTALACAO1 = '" + instalacao + "'\r\n" + 
/*  96 */       "AND CODPROD_ANP = '" + codProdAnp + "'\r\n" + 
/*  97 */       "GROUP BY \r\n" + 
/*  98 */       "A.CODEMP_ANP,\r\n" + 
/*  99 */       "A.MESREFERENCIA,\r\n" + 
/* 100 */       "A.CODINSTALACAO1,\r\n" + 
/* 101 */       "A.CODPROD_ANP,\r\n" + 
/* 102 */       "B.TIPO,\r\n" + 
/* 103 */       "B.FINALIDADE,\r\n" + 
/* 104 */       "B.CLASSE) T";
/* 105 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/* 106 */     ResultSet query = pstm.executeQuery();
/* 107 */     return query;
/*     */   }
/*     */   
/*     */   public static ResultSet totalizadorPorOperacaoAgendado(JdbcWrapper jdbcWrapper) throws Exception {
/* 111 */     String sql = "SELECT \r\nT.* \r\nFROM (SELECT \r\n(SELECT CODOPER FROM AD_OPERACAOANP OPER WHERE OPER.TIPO= B.TIPO AND OPER.FINALIDADE = B.FINALIDADE AND OPER.CLASSE = B.CLASSE AND OPER.SEQ = 998) AS OPER_TOT,\r\nA.CODSIMSIM,\r\nA.CODEMP_ANP,\r\nA.MESREFERENCIA,\r\nA.CODINSTALACAO1,\r\nA.CODPROD_ANP,\r\nSUM(TO_NUMBER(COALESCE(A.QTD_PROD_UN_ANP,'0'))) AS QTD_PROD_UN_ANP,\r\nSUM(TO_NUMBER(COALESCE(A.QTD_PROD_OPERADO_KG,'0'))) AS QTD_PROD_OPERADO_KG,\r\nSUM(TO_NUMBER(COALESCE(A.QTD_PROD_UN_ANP,'0'))*cast(replace(A.VLRUNIT_NF,'.',',') as numeric(10,6))) AS VLR_PROD_UN_ANP,B.TIPO,\r\nB.FINALIDADE,\r\nB.CLASSE\r\nFROM AD_DETSIMULARSIMP A\r\nINNER JOIN AD_OPERACAOANP B ON A.CODOPER = B.CODOPER\r\nINNER JOIN  AD_SIMULARSIMP C ON A.CODSIMSIM = C.CODSIMSIM\r\nWHERE OPERTOTALIZADOR > 0\r\nAND C.STATUS IN ('0','1')\r\nGROUP BY \r\nA.CODSIMSIM,\r\nA.CODEMP_ANP,\r\nA.MESREFERENCIA,\r\nA.CODINSTALACAO1,\r\nA.CODPROD_ANP,\r\nB.TIPO,\r\nB.FINALIDADE,\r\nB.CLASSE) T\r\nWHERE\r\n(\r\nT.QTD_PROD_UN_ANP || '-' || T.QTD_PROD_OPERADO_KG  != (SELECT \r\n            DET.QTD_PROD_UN_ANP || '-' || DET.QTD_PROD_OPERADO_KG\r\n            FROM AD_DETSIMULARSIMP DET\r\n            WHERE T.CODSIMSIM = DET.CODSIMSIM\r\n            AND  T.OPER_TOT = DET.CODOPER \r\n            AND T.CODPROD_ANP = DET.CODPROD_ANP\r\n            AND T.CODEMP_ANP = DET.CODEMP_ANP\r\n            AND T.MESREFERENCIA = DET.MESREFERENCIA\r\n            AND T.CODINSTALACAO1 = DET.CODINSTALACAO1)\r\nOR NOT EXISTS  (SELECT \r\n            DET.QTD_PROD_UN_ANP || '-' || DET.QTD_PROD_OPERADO_KG\r\n            FROM AD_DETSIMULARSIMP DET\r\n            WHERE T.CODSIMSIM = DET.CODSIMSIM\r\n            AND  T.OPER_TOT = DET.CODOPER \r\n            AND T.CODPROD_ANP = DET.CODPROD_ANP\r\n            AND T.CODEMP_ANP = DET.CODEMP_ANP\r\n            AND T.MESREFERENCIA = DET.MESREFERENCIA\r\n            AND T.CODINSTALACAO1 = DET.CODINSTALACAO1)\r\n)\r\nAND ROWNUM <=100";
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/* 162 */     ResultSet query = pstm.executeQuery();
/* 163 */     return query;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ResultSet totalizadorGeral(JdbcWrapper jdbcWrapper, BigDecimal codSimulaSimp, String codEmpAnp, String mesRefrencia, String instalacao, String codProdAnp) throws Exception {
/* 169 */     String sql = "SELECT \r\n(SELECT CODOPER FROM AD_OPERACAOANP OPER WHERE OPER.TIPO= '4' AND OPER.FINALIDADE = '1' AND OPER.CLASSE = T.CLASSE AND OPER.SEQ = 998) AS OPER_TOT,\r\nT.* \r\nFROM (SELECT \r\nA.CODEMP_ANP,\r\nA.MESREFERENCIA,\r\nA.CODINSTALACAO1,\r\nA.CODPROD_ANP,\r\nSUM(TO_NUMBER(COALESCE(A.QTD_PROD_UN_ANP,'0'))) AS QTD_PROD_UN_ANP,\r\nSUM(TO_NUMBER(COALESCE(A.QTD_PROD_OPERADO_KG,'0'))) AS QTD_PROD_OPERADO_KG,\r\n,SUM(TO_NUMBER(COALESCE(A.QTD_PROD_UN_ANP,'0'))*cast(replace(A.VLRUNIT_NF,'.',',') as numeric(10,6))) AS VLR_PROD_UN_ANP,B.CLASSE\r\nFROM AD_DETSIMULARSIMP A\r\nINNER JOIN AD_OPERACAOANP B ON A.CODOPER = B.CODOPER\r\nWHERE A.CODSIMSIM = " + 
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
/* 182 */       codSimulaSimp + "\r\n" + 
/* 183 */       "AND OPERTOTALIZADOR > 0\r\n" + 
/* 184 */       "AND CODEMP_ANP = '" + codEmpAnp + "'\r\n" + 
/* 185 */       "AND MESREFERENCIA = '" + mesRefrencia + "'\r\n" + 
/* 186 */       "AND CODINSTALACAO1 = '" + instalacao + "'\r\n" + 
/* 187 */       "AND CODPROD_ANP = '" + codProdAnp + "'\r\n" + 
/* 188 */       "AND TO_NUMBER(COALESCE(B.CLASSE,'0')) > 0\r\n" + 
/* 189 */       "GROUP BY \r\n" + 
/* 190 */       "A.CODEMP_ANP,\r\n" + 
/* 191 */       "A.MESREFERENCIA,\r\n" + 
/* 192 */       "A.CODINSTALACAO1,\r\n" + 
/* 193 */       "A.CODPROD_ANP,\r\n" + 
/* 194 */       "B.CLASSE) T";
/* 195 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/* 196 */     ResultSet query = pstm.executeQuery();
/* 197 */     return query;
/*     */   }
/*     */   public static ResultSet totalizadorGeralAgendado(JdbcWrapper jdbcWrapper) throws Exception {
/* 200 */     String sql = "SELECT \r\n*\r\nFROM (SELECT \r\n(SELECT CODOPER FROM AD_OPERACAOANP OPER WHERE OPER.TIPO= '4' AND OPER.FINALIDADE = '1' AND OPER.CLASSE = B.CLASSE AND OPER.SEQ = 998) AS OPER_TOT,\r\nA.CODSIMSIM,\r\nA.CODEMP_ANP,\r\nA.MESREFERENCIA,\r\nA.CODINSTALACAO1,\r\nA.CODPROD_ANP,\r\nSUM(TO_NUMBER(COALESCE(A.QTD_PROD_UN_ANP,'0'))) AS QTD_PROD_UN_ANP,\r\nSUM(TO_NUMBER(COALESCE(A.QTD_PROD_OPERADO_KG,'0'))) AS QTD_PROD_OPERADO_KG,\r\nSUM(TO_NUMBER(COALESCE(A.QTD_PROD_UN_ANP,'0'))*cast(replace(A.VLRUNIT_NF,'.',',') as numeric(10,6))) AS VLR_PROD_UN_ANP,B.CLASSE\r\nFROM AD_DETSIMULARSIMP A\r\nINNER JOIN AD_OPERACAOANP B ON A.CODOPER = B.CODOPER\r\nINNER JOIN  AD_SIMULARSIMP C ON A.CODSIMSIM = C.CODSIMSIM\r\nWHERE \r\nOPERTOTALIZADOR > 0\r\nAND TO_NUMBER(COALESCE(B.CLASSE,'0')) > 0\r\nAND C.STATUS IN ('0','1')\r\nGROUP BY \r\nA.CODSIMSIM,\r\nA.CODEMP_ANP,\r\nA.MESREFERENCIA,\r\nA.CODINSTALACAO1,\r\nA.CODPROD_ANP,\r\nB.CLASSE) T\r\nWHERE\r\n(\r\nT.QTD_PROD_UN_ANP || '-' || T.QTD_PROD_OPERADO_KG  != (SELECT \r\n            DET.QTD_PROD_UN_ANP || '-' || DET.QTD_PROD_OPERADO_KG\r\n            FROM AD_DETSIMULARSIMP DET\r\n            WHERE T.CODSIMSIM = DET.CODSIMSIM\r\n            AND  T.OPER_TOT = DET.CODOPER \r\n            AND T.CODPROD_ANP = DET.CODPROD_ANP\r\n            AND T.CODEMP_ANP = DET.CODEMP_ANP\r\n            AND T.MESREFERENCIA = DET.MESREFERENCIA\r\n            AND T.CODINSTALACAO1 = DET.CODINSTALACAO1)\r\nOR NOT EXISTS  (SELECT \r\n            DET.QTD_PROD_UN_ANP || '-' || DET.QTD_PROD_OPERADO_KG\r\n            FROM AD_DETSIMULARSIMP DET\r\n            WHERE T.CODSIMSIM = DET.CODSIMSIM\r\n            AND  T.OPER_TOT = DET.CODOPER \r\n            AND T.CODPROD_ANP = DET.CODPROD_ANP\r\n            AND T.CODEMP_ANP = DET.CODEMP_ANP\r\n            AND T.MESREFERENCIA = DET.MESREFERENCIA\r\n            AND T.CODINSTALACAO1 = DET.CODINSTALACAO1)\r\n)\r\nAND ROWNUM <=100";
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
/*     */ 
/*     */     
/* 248 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/* 249 */     ResultSet query = pstm.executeQuery();
/* 250 */     return query;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ResultSet buscarEstoqueInicial(JdbcWrapper jdbcWrapper, BigDecimal codSimulaSimp, String codEmpAnp, String mesRefrencia, String instalacao, String codProdAnp) throws Exception {
/* 255 */     String sql = "SELECT \r\n(SELECT INTEIRO FROM TSIPAR WHERE CHAVE = 'OPERPADESTFINAL') AS OPER_TOT,\r\nA.CODEMP_ANP,\r\nA.MESREFERENCIA,\r\nA.CODINSTALACAO1,\r\nA.CODPROD_ANP,\r\nTO_NUMBER(COALESCE(A.QTD_PROD_UN_ANP,'0')) AS QTD_PROD_UN_ANP,\r\nTO_NUMBER(COALESCE(A.QTD_PROD_OPERADO_KG,'0')) AS QTD_PROD_OPERADO_KG,\r\nB.CLASSE\r\nFROM AD_DETSIMULARSIMP A\r\nINNER JOIN AD_OPERACAOANP B ON A.CODOPER = B.CODOPER\r\nWHERE A.CODSIMSIM = " + 
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
/* 266 */       codSimulaSimp + "\r\n" + 
/* 267 */       "AND CODEMP_ANP = '" + codEmpAnp + "'\r\n" + 
/* 268 */       "AND MESREFERENCIA = '" + mesRefrencia + "'\r\n" + 
/* 269 */       "AND CODINSTALACAO1 = '" + instalacao + "'\r\n" + 
/* 270 */       "AND CODPROD_ANP = '" + codProdAnp + "'\r\n" + 
/* 271 */       "AND A.CODOPER = (SELECT INTEIRO FROM TSIPAR WHERE CHAVE = 'OPERPADESTINICI')";
/* 272 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/* 273 */     ResultSet query = pstm.executeQuery();
/* 274 */     return query;
/*     */   }
/*     */   
/*     */   public static ResultSet buscarEstoqueInicialAgendado(JdbcWrapper jdbcWrapper) throws Exception {
/* 278 */     String sql = "SELECT \r\n(SELECT INTEIRO FROM TSIPAR WHERE CHAVE = 'OPERPADESTFINAL') AS OPER_TOT,\r\nA.CODSIMSIM,\r\nA.CODEMP_ANP,\r\nA.MESREFERENCIA,\r\nA.CODINSTALACAO1,\r\nA.CODPROD_ANP,\r\nTO_NUMBER(COALESCE(A.QTD_PROD_UN_ANP,'0')) AS QTD_PROD_UN_ANP,\r\nTO_NUMBER(COALESCE(A.QTD_PROD_OPERADO_KG,'0')) AS QTD_PROD_OPERADO_KG,\r\nB.CLASSE,\r\nA.CODOPER\r\nFROM AD_DETSIMULARSIMP A\r\nINNER JOIN AD_OPERACAOANP B ON A.CODOPER = B.CODOPER\r\nINNER JOIN  AD_SIMULARSIMP C ON A.CODSIMSIM = C.CODSIMSIM\r\nWHERE C.STATUS IN ('0','1')\r\nAND A.CODOPER = (SELECT INTEIRO FROM TSIPAR WHERE CHAVE = 'OPERPADESTINICI')";
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
/* 294 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/* 295 */     ResultSet query = pstm.executeQuery();
/* 296 */     return query;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ResultSet entradasaida(JdbcWrapper jdbcWrapper, BigDecimal codSimulaSimp, String codEmpAnp, String mesRefrencia, String instalacao, String codProdAnp, String classe) throws Exception {
/* 301 */     String sql = "SELECT \r\nA.CODEMP_ANP,\r\nA.MESREFERENCIA,\r\nA.CODINSTALACAO1,\r\nA.CODPROD_ANP,\r\nSUM(TO_NUMBER(COALESCE(A.QTD_PROD_UN_ANP,'0'))) AS QTD_PROD_UN_ANP,\r\nSUM(TO_NUMBER(COALESCE(A.QTD_PROD_OPERADO_KG,'0'))) AS QTD_PROD_OPERADO_KG,\r\nSUM(TO_NUMBER(COALESCE(A.QTD_PROD_UN_ANP,'0'))*cast(replace(A.VLRUNIT_NF,'.',',') as numeric(10,6))) AS VLR_PROD_UN_ANP,B.CLASSE\r\nFROM AD_DETSIMULARSIMP A\r\nINNER JOIN AD_OPERACAOANP B ON A.CODOPER = B.CODOPER\r\nWHERE A.CODSIMSIM = " + 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 311 */       codSimulaSimp + "\r\n" + 
/* 312 */       "AND OPERTOTALIZADOR > 0\r\n" + 
/* 313 */       "AND CODEMP_ANP = '" + codEmpAnp + "'\r\n" + 
/* 314 */       "AND MESREFERENCIA = '" + mesRefrencia + "'\r\n" + 
/* 315 */       "AND CODINSTALACAO1 = '" + instalacao + "'\r\n" + 
/* 316 */       "AND CODPROD_ANP = '" + codProdAnp + "'\r\n" + 
/* 317 */       "AND B.CLASSE = '" + classe + "'\r\n" + 
/* 318 */       "GROUP BY \r\n" + 
/* 319 */       "A.CODEMP_ANP,\r\n" + 
/* 320 */       "A.MESREFERENCIA,\r\n" + 
/* 321 */       "A.CODINSTALACAO1,\r\n" + 
/* 322 */       "A.CODPROD_ANP,\r\n" + 
/* 323 */       "B.CLASSE";
/* 324 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/* 325 */     ResultSet query = pstm.executeQuery();
/* 326 */     return query;
/*     */   }
/*     */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\buscar\buscarTotalizadores.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */