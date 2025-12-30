/*    */ package br.com.sattva.simp.sql;
/*    */ 
/*    */ import br.com.sankhya.jape.sql.NativeSql;
/*    */ import java.math.BigDecimal;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SimpQueryFactory
/*    */ {
/*    */   public static void montarConsultaComTotalizador(NativeSql sql, BigDecimal codSimsim) {
/* 30 */     sql.appendSql(
/* 31 */         "WITH BASE AS (\n  SELECT\n      A.CODEMP_ANP,\n      A.MESREFERENCIA,\n      OPER.CODOPER_ANP            AS CODOPERACAO,\n      A.CODINSTALACAO1,\n      A.CODPROD_ANP,\n      A.QTD_PROD_UN_ANP,\n      /* --- Demais colunas do layout preenchidas com '0' --- */\n      '0' AS CODINSTALACAO2,\n      '0' AS CODMODAL_SIMP,\n      '0' AS COD_OLEODUTO,\n      '0' AS CNPJ_PARC,\n      '0' AS CODCID_ANP,\n      '0' AS AD_CNAE_ANP,\n      '0' AS COD_PAIS_ANP,\n      '0' AS LICENCA_IMPORTACAO_LI,\n      '0' AS NUM_DECLARACAO_IMPORTACAO_DI,\n      '0' AS NRONF,\n      '0' AS COD_SERIE_NF,\n      '0' AS DTENTSAI,\n      '0' AS COD_TIP_TAR\n  FROM AD_DETSIMULARSIMP A\n  JOIN AD_OPERACAOANP OPER ON A.CODOPER = OPER.CODOPER\n  WHERE A.CODSIMSIM = :CODSIMSIM\n),\nTOT AS (\n  SELECT\n      b.CODEMP_ANP,\n      b.MESREFERENCIA,\n      SUBSTR(b.CODOPERACAO,1,4) || '998' AS CODOPERACAO,\n      b.CODINSTALACAO1,\n      b.CODPROD_ANP,\n      SUM(b.QTD_PROD_UN_ANP) AS QTD_PROD_UN_ANP,\n      /* --- Repete colunas do layout com '0' --- */\n      '0' AS CODINSTALACAO2,\n      '0' AS CODMODAL_SIMP,\n      '0' AS COD_OLEODUTO,\n      '0' AS CNPJ_PARC,\n      '0' AS CODCID_ANP,\n      '0' AS AD_CNAE_ANP,\n      '0' AS COD_PAIS_ANP,\n      '0' AS LICENCA_IMPORTACAO_LI,\n      '0' AS NUM_DECLARACAO_IMPORTACAO_DI,\n      '0' AS NRONF,\n      '0' AS COD_SERIE_NF,\n      '0' AS DTENTSAI,\n      '0' AS COD_TIP_TAR\n  FROM BASE b\n  WHERE SUBSTR(b.CODOPERACAO,1,4) IN (\n      '1011','1012','1021','1022','1041','1042','1051','1052',\n      '1061','1062','2011','2012','2021','2022'\n  )\n  GROUP BY\n      b.CODEMP_ANP,\n      b.MESREFERENCIA,\n      SUBSTR(b.CODOPERACAO,1,4),\n      b.CODINSTALACAO1,\n      b.CODPROD_ANP\n)\nSELECT * FROM BASE\nUNION ALL\nSELECT * FROM TOT\nORDER BY MESREFERENCIA, CODEMP_ANP, CODOPERACAO, CODINSTALACAO1, CODPROD_ANP\n");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 98 */     sql.setNamedParameter("CODSIMSIM", codSimsim);
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMPSATTVA.jar!\br\com\sattva\simp\sql\SimpQueryFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */