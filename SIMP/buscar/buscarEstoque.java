/*      */ package buscar;
/*      */ 
/*      */ import br.com.sankhya.jape.dao.JdbcWrapper;
/*      */ import java.math.BigDecimal;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ 
/*      */ 
/*      */ 
/*      */ public class buscarEstoque
/*      */ {
/*      */   public static ResultSet validacao(JdbcWrapper jdbcWrapper, String referencia) throws Exception {
/*   13 */     String sql = "SELECT \r\nCODSIMSIM, \r\nSTATUS , \r\n(LPAD(MES_REFERENCIA,2,0) || '-' || ANO_REFRENCIA) AS REFERENCIA \r\nFROM AD_SIMULARSIMP \r\nWHERE STATUS IN ('1','2') \r\nAND LPAD(MES_REFERENCIA,2,0) || '-' || ANO_REFRENCIA = '" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*   19 */       referencia + "'";
/*   20 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/*   21 */     ResultSet query = pstm.executeQuery();
/*   22 */     return query;
/*      */   }
/*      */ 
/*      */   
/*      */   public static ResultSet estoqueInicial(JdbcWrapper jdbcWrapper, String referencia, String codEmp) throws Exception {
/*   27 */     String sql = "SELECT \r\n\r\n--4\tC�digo da Opera��o\r\nCOALESCE(ESTDET.CODOPER,(SELECT INTEIRO FROM TSIPAR WHERE CHAVE = 'OPERPADESTINICI')) AS OPERACAO,\r\n\r\n--2\tAgente Regulado Informante\r\nCOALESCE((SELECT AD_CODEMP_ANP FROM TSIEMP WHERE CODEMP = COALESCE(ESTDET.CODEMP,1)),'0') AS AD_CODEMP_ANP,\r\n\r\n--Referencia - M�s de Refer�ncia (MMAAAA),\r\n\r\n\r\n--5\tC�digo da Instala��o 1\r\nCOALESCE((SELECT AD_CODINSTALACAO_ANP FROM TSIEMP WHERE CODEMP = COALESCE(ESTDET.CODEMP,1)),'0') AS COD_INST_EMP,\r\n\r\n--6\tC�digo da Instala��o 2\r\n'0' AS COD_INST_PAR,\r\n\r\n--7\tC�digo do Produto Operado\r\nCOALESCE(ESTDET.CODANP,'0') AS CODPROD_ANP,\r\n\r\n--8\tQuantidade do Produto Operado na Unidade de Medida Oficial ANP\r\nCOALESCE(ESTDET.QTDE,0) AS QTD_UN_PADRAO,\r\n\r\n--9\tQuantidade do Produto Operado em quilogramas (kg)\r\nCOALESCE(ESTDET.QTDE,0) AS QTD_CONVERTIDO_KG,\r\n\r\n--10\tC�digo do Modal Utilizado na Movimenta��o\r\n'1' AS CODMODAL_SIMP,--C�digo do Modal Utilizado na Movimenta��o\r\n\r\n--11 C�digo do oleoduto de transporte\r\n'0' AS COD_OLEODUTO,\r\n\r\n--12 Identifica��o do Terceiro Envolvido na Opera��o\r\n'0' AS CNPJ_PARC,\r\n\r\n--13 C�digo do Munic�pio (Origem/Destino)\r\n'0' AS CODCID_ANP,\r\n\r\n--14 C�digo de Atividade Econ�mica do Terceiro\r\n'0' AS AD_CNAE_ANP,\r\n\r\n--15 C�digo do Pa�s (Origem/Destino)\r\n'0' AS COD_PAIS_ANP,--C�digo ANP Pais,\r\n\r\n--16 N�mero da Licen�a de Importa��o (LI)\r\n'0' AS LICENCA_IMPORTACAO_LI,\r\n\r\n--17 N�mero da Declara��o de Importa��o (DI)\r\n'0' AS NUM_DELCARACAO_IMPORTACAO_DI,\r\n\r\n--18 N�mero da Nota Fiscal da Opera��o Comercial -\r\n'0' AS NRONF,\r\n\r\n--19 C�digo da S�rie da Nota Fiscal da Opera��o Comercial\r\n'0' AS COD_SERIE_NF,\r\n\r\n--20 Data da Opera��o Comercial (DDMMAAAA)\r\n'0' AS DTENTSAI,\r\n\r\n--21 C�digo do Tipo de Tarifa de Servi�o (Oleodutos e Terminais de combust�veis l�quidos)\r\n'0' AS COD_TIP_TAR_SERV,\r\n\r\n--22 CARACTERISTICA Inativo - preencher com zeros\r\n'0' AS CARACTERISTICA_INATIVO,\r\n\r\n--23 METODO Inativo - preencher com zeros\r\n'0' AS METODO_INATIVO,\r\n\r\n--24 Modalidade do frete\r\n0 AS MODALIDADE_FRETE,\r\n\r\n--25 N�mero do Documento da Qualidade / Quando o ARI for Distribuidor de Produtos Asf�lticos considerar-se-� o campo como Pre�o (R$/kg)\r\n'0' AS VALOR_CARACTERISTICA,\r\n\r\n--26 C�digo do Produto/Opera��o Resultante\r\n0 AS CODPROD_RESULTANTE_ANP,\r\n\r\n--27\tValor unit�rio (nota fiscal)\r\n0 AS VLRUNIT,\r\n\r\n--28 Recipiente de GLP\r\n'0' AS RECIPIENTE_GLP,\r\n\r\n--29\tChave de acesso da NF-e ou do CT-e\r\n'0' AS CHAVE_NFE_CTE,\r\n\r\n\r\n\r\n0 AS OPER_TOTALIZADOR\r\n\r\nFROM AD_IMPORTADORESTOQUESIMPCAB ESTCAB\r\nINNER JOIN AD_IMPORTADORESTOQUESIMPDET ESTDET ON ESTCAB.CODIMPDOCCAB = ESTDET.CODIMPDOCCAB\r\nWHERE TO_DATE('01'||'/'||(LPAD(ESTCAB.MES,2,0)||'/'||ESTCAB.ANO),'dd/mm/yyyy') = (SELECT MAX(TO_DATE('01'||'/'||(LPAD(CAB.MES,2,0)||'/'||CAB.ANO),'dd/mm/yyyy')) FROM AD_IMPORTADORESTOQUESIMPCAB CAB WHERE CAB.MES IS NOT NULL AND CAB.ANO IS NOT NULL)\r\nAND COALESCE(CODEMP,1) = " + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  120 */       codEmp;
/*  121 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/*  122 */     ResultSet query = pstm.executeQuery();
/*  123 */     return query;
/*      */   }
/*      */ 
/*      */   
/*      */   public static ResultSet estoqueInicialProdMovimentados(JdbcWrapper jdbcWrapper, String referencia, String codEmp) throws Exception {
/*  128 */     String sql = "SELECT DISTINCT\r\n\r\n--4\tC�digo da Opera��o\r\n(SELECT INTEIRO FROM TSIPAR WHERE CHAVE = 'OPERPADESTINICI') AS OPERACAO,\r\n\r\n--2\tAgente Regulado Informante\r\nCOALESCE(F.AD_CODEMP_ANP,'0') AS AD_CODEMP_ANP,\r\n\r\n--3 Referencia - M�s de Refer�ncia (MMAAAA),\r\n\r\n\r\n--5\tC�digo da Instala��o 1\r\nCOALESCE(F.AD_CODINSTALACAO_ANP,'0') AS COD_INST_EMP,\r\n\r\n--6\tC�digo da Instala��o 2\r\n'0' AS COD_INST_PAR,\r\n\r\n--7\tC�digo do Produto Operado\r\nTO_CHAR(COALESCE(E.CODANP,0)) AS CODPROD_ANP,\r\n\r\n\r\n--8\tQuantidade do Produto Operado na Unidade de Medida Oficial ANP\r\n0 AS QTD_UN_PADRAO,\r\n\r\n--9\tQuantidade do Produto Operado em quilogramas (kg)\r\n0 AS QTD_CONVERTIDO_KG,\r\n\r\n--10\tC�digo do Modal Utilizado na Movimenta��o\r\n'1' AS CODMODAL_SIMP,--C�digo do Modal Utilizado na Movimenta��o\r\n\r\n--11 C�digo do oleoduto de transporte\r\n'0' AS COD_OLEODUTO,\r\n\r\n--12 Identifica��o do Terceiro Envolvido na Opera��o\r\n'0' AS CNPJ_PARC,\r\n\r\n--13 C�digo do Munic�pio (Origem/Destino)\r\n'0' AS CODCID_ANP,\r\n\r\n--14 C�digo de Atividade Econ�mica do Terceiro\r\n'0' AS AD_CNAE_ANP,\r\n\r\n--15 C�digo do Pa�s (Origem/Destino)\r\n'0' AS COD_PAIS_ANP,--C�digo ANP Pais,\r\n\r\n--16 N�mero da Licen�a de Importa��o (LI)\r\n'0' AS LICENCA_IMPORTACAO_LI,\r\n\r\n--17 N�mero da Declara��o de Importa��o (DI)\r\n'0' AS NUM_DELCARACAO_IMPORTACAO_DI,\r\n\r\n--18 N�mero da Nota Fiscal da Opera��o Comercial -\r\n'0' AS NRONF,\r\n\r\n--19 C�digo da S�rie da Nota Fiscal da Opera��o Comercial\r\n'0' AS COD_SERIE_NF,\r\n\r\n--20 Data da Opera��o Comercial (DDMMAAAA)\r\n'0' AS DTENTSAI,\r\n\r\n--21 C�digo do Tipo de Tarifa de Servi�o (Oleodutos e Terminais de combust�veis l�quidos)\r\n'0' AS COD_TIP_TAR_SERV,\r\n\r\n--22 CARACTERISTICA Inativo - preencher com zeros\r\n'0' AS CARACTERISTICA_INATIVO,\r\n\r\n--23 METODO Inativo - preencher com zeros\r\n'0' AS METODO_INATIVO,\r\n\r\n--24 Modalidade do frete\r\n0 AS MODALIDADE_FRETE,\r\n\r\n--25 N�mero do Documento da Qualidade / Quando o ARI for Distribuidor de Produtos Asf�lticos considerar-se-� o campo como Pre�o (R$/kg)\r\n'0' AS VALOR_CARACTERISTICA,\r\n\r\n--26 C�digo do Produto/Opera��o Resultante\r\n0 AS CODPROD_RESULTANTE_ANP,\r\n\r\n--27\tValor unit�rio (nota fiscal)\r\nA.vlrunit AS VLRUNIT,\r\n\r\n--28 Recipiente de GLP\r\n'0' AS RECIPIENTE_GLP,\r\n\r\n--29\tChave de acesso da NF-e ou do CT-e\r\n'0' AS CHAVE_NFE_CTE,\r\n\r\n0 AS OPER_TOTALIZADOR\r\n\r\nFROM TGFITE A\r\nINNER JOIN TGFCAB B ON A.NUNOTA = B.NUNOTA\r\nINNER JOIN TGFTOP C ON B.CODTIPOPER = C.CODTIPOPER AND DHALTER = (SELECT MAX(DHALTER) FROM TGFTOP TOP WHERE C.CODTIPOPER = TOP.CODTIPOPER)\r\nINNER JOIN TGFPAR D ON B.CODPARC = D.CODPARC\r\nINNER JOIN TGFPRO E ON A.CODPROD = E.CODPROD\r\nINNER JOIN TSIEMP F ON B.CODEMP = F.CODEMP\r\nWHERE COALESCE(C.AD_ATUALIZA_EST_SIMP,'N') = 'S'\r\nAND COALESCE(E.AD_GERA_SIMP,'N') = 'S'\r\nAND TO_CHAR(B.DTNEG,'mm-yyyy') = '" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  225 */       referencia + "'\r\n" + 
/*  226 */       "AND B.CODEMP = " + codEmp + " \r\n" + 
/*  227 */       "AND CODANP NOT IN(SELECT \r\n" + 
/*  228 */       "COALESCE(ESTDET.CODANP,'0') AS CODPROD_ANP\r\n" + 
/*  229 */       "FROM AD_IMPORTADORESTOQUESIMPCAB ESTCAB\r\n" + 
/*  230 */       "INNER JOIN AD_IMPORTADORESTOQUESIMPDET ESTDET ON ESTCAB.CODIMPDOCCAB = ESTDET.CODIMPDOCCAB\r\n" + 
/*  231 */       "WHERE TO_DATE('01'||'/'||(LPAD(ESTCAB.MES,2,0)||'/'||ESTCAB.ANO),'dd/mm/yyyy') = (SELECT MAX(TO_DATE('01'||'/'||(LPAD(CAB.MES,2,0)||'/'||CAB.ANO),'dd/mm/yyyy')) FROM AD_IMPORTADORESTOQUESIMPCAB CAB WHERE CAB.MES IS NOT NULL AND CAB.ANO IS NOT NULL)\r\n" + 
/*  232 */       "AND COALESCE(CODEMP,1) = " + codEmp + "\r\n" + 
/*  233 */       "AND ESTDET.CODANP IS NOT NULL)";
/*  234 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/*  235 */     ResultSet query = pstm.executeQuery();
/*  236 */     return query;
/*      */   }
/*      */ 
/*      */   
/*      */   public static ResultSet movimentacoesPRODNEWS(JdbcWrapper jdbcWrapper, String referencia, String codEmp) throws Exception {
/*  241 */     String sql = "SELECT \r\nIDIPROC,NUNOTA,\r\nLISTAGG(SEQUENCIA,',') AS SEQUENCIA,\r\nLISTAGG(CODPROD,',') AS CODPROD,\r\nAD_TEM_COLETA,\r\nOPER_PAD_DISP_COLETA,\r\nCLASS_OPERACAO,\r\nOPERACAO,\r\nAD_CODEMP_ANP,\r\nCOD_INST_EMP,\r\nCOD_INST_PAR,\r\nCODPROD_ANP,\r\nSUM(TO_NUMBER(COALESCE(QTD_UN_PADRAO,0))) AS QTD_UN_PADRAO,\r\nSUM(TO_NUMBER(COALESCE(QTD_CONVERTIDO_KG,0))) AS QTD_CONVERTIDO_KG,\r\nCODMODAL_SIMP,\r\nCOD_OLEODUTO,\r\nCNPJ_PARC,\r\n(CASE WHEN COALESCE(CNPJ_PARC,'0') = '0' AND COALESCE(AD_TEM_COLETA,'N') = 'N' THEN '0' ELSE CODCID_ANP END) AS CODCID_ANP,\r\nAD_CNAE_ANP,\r\nCOD_PAIS_ANP,\r\nLICENCA_IMPORTACAO_LI,\r\nNUM_DELCARACAO_IMPORTACAO_DI,\r\nNRONF,\r\nCOD_SERIE_NF,\r\nDTENTSAI,\r\nCOD_TIP_TAR_SERV,\r\nCARACTERISTICA_INATIVO,\r\nMETODO_INATIVO,\r\nMODALIDADE_FRETE,\r\nVALOR_CARACTERISTICA,\r\nCODPROD_RESULTANTE_ANP,\r\nVLRUNIT,\r\nRECIPIENTE_GLP,\r\nCHAVE_NFE_CTE,\r\nOPER_TOTALIZADOR\r\nFROM(\r\nSELECT \r\nA.NUNOTA,\r\nA.SEQUENCIA,\r\nA.CODPROD,\r\nCOALESCE(E.AD_TEM_COLETA,'N') AS AD_TEM_COLETA,\r\n(SELECT INTEIRO FROM TSIPAR WHERE CHAVE = 'OPERDISPCOLETA') AS OPER_PAD_DISP_COLETA,\r\nCASE WHEN(A.USOPROD = 'M') THEN (SELECT CLASSE FROM AD_OPERACAOANP OPER WHERE OPER.CODOPER = C.AD_CODOPERANPMP) ELSE \r\n\t(SELECT CLASSE FROM AD_OPERACAOANP OPER WHERE OPER.CODOPER = C.AD_CODOPERANP) END AS CLASS_OPERACAO,\r\n\t\t\t\r\n--4\tC�digo da Opera��o\r\n\tCASE WHEN(A.USOPROD = 'M') THEN C.AD_CODOPERANPMP\r\n     ELSE \t(CASE WHEN C.AD_CODOPERANP IS NOT NULL THEN C.AD_CODOPERANP ELSE C.AD_CODOPERANPAGENTENREGULADO END)END  AS OPERACAO,\r\n\r\n--2\tAgente Regulado Informante\r\nCOALESCE(F.AD_CODEMP_ANP,'0') AS AD_CODEMP_ANP,\r\n\r\n--3 Referencia - M�s de Refer�ncia (MMAAAA),\r\n\r\n--5\tC�digo da Instala��o 1\r\nCOALESCE(F.AD_CODINSTALACAO_ANP,'0') AS COD_INST_EMP,\r\n\r\n--6\tC�digo da Instala��o 2\r\nCOALESCE(D.AD_CODINSTALACAO_ANP,'0') AS COD_INST_PAR,\r\n\r\n--7\tC�digo do Produto Operado\r\nTO_CHAR(COALESCE(E.CODANP,0)) AS CODPROD_ANP,\r\n\r\n--8\tQuantidade do Produto Operado na Unidade de Medida Oficial ANP\r\nCOALESCE((CASE WHEN E.CODVOL IN ('LT','KG') THEN A.QTDNEG\r\nWHEN E.PESOLIQ != 0 THEN E.PESOLIQ * QTDNEG\r\nWHEN  E.CODVOL != 'LT' AND E.PESOLIQ = 0 AND EXISTS (SELECT * FROM TGFVOA VOA WHERE VOA.CODVOL ='LT' AND A.CODPROD = VOA.CODPROD) THEN (SELECT (CASE WHEN DIVIDEMULTIPLICA = 'M' THEN ((A.QTDNEG*QUANTIDADE)*MULTIPVLR) ELSE TRUNC(((A.QTDNEG/QUANTIDADE)*MULTIPVLR),3) END)  FROM TGFVOA VOA WHERE VOA.CODVOL ='LT' AND A.CODPROD = VOA.CODPROD)\r\nWHEN  E.CODVOL != 'KG' AND E.PESOLIQ = 0 AND EXISTS (SELECT * FROM TGFVOA VOA WHERE VOA.CODVOL ='KG' AND A.CODPROD = VOA.CODPROD) THEN (SELECT (CASE WHEN DIVIDEMULTIPLICA = 'M' THEN ((A.QTDNEG*QUANTIDADE)*MULTIPVLR) ELSE TRUNC(((A.QTDNEG/QUANTIDADE)*MULTIPVLR),3) END)  FROM TGFVOA VOA WHERE VOA.CODVOL ='KG' AND A.CODPROD = VOA.CODPROD)\r\nELSE A.QTDNEG END),0) AS QTD_UN_PADRAO,\r\n\r\n--9\tQuantidade do Produto Operado em quilogramas (kg)\r\nCOALESCE((CASE WHEN E.CODVOL IN ('LT','KG') THEN A.QTDNEG\r\nWHEN E.PESOLIQ != 0 THEN E.PESOLIQ * QTDNEG\r\nWHEN  E.CODVOL != 'LT' AND E.PESOLIQ = 0 AND EXISTS (SELECT * FROM TGFVOA VOA WHERE VOA.CODVOL ='LT' AND A.CODPROD = VOA.CODPROD) THEN (SELECT (CASE WHEN DIVIDEMULTIPLICA = 'M' THEN ((A.QTDNEG*QUANTIDADE)*MULTIPVLR) ELSE TRUNC(((A.QTDNEG/QUANTIDADE)*MULTIPVLR),3) END)  FROM TGFVOA VOA WHERE VOA.CODVOL ='LT' AND A.CODPROD = VOA.CODPROD)\r\nWHEN  E.CODVOL != 'KG' AND E.PESOLIQ = 0 AND EXISTS (SELECT * FROM TGFVOA VOA WHERE VOA.CODVOL ='KG' AND A.CODPROD = VOA.CODPROD) THEN (SELECT (CASE WHEN DIVIDEMULTIPLICA = 'M' THEN ((A.QTDNEG*QUANTIDADE)*MULTIPVLR) ELSE TRUNC(((A.QTDNEG/QUANTIDADE)*MULTIPVLR),3) END)  FROM TGFVOA VOA WHERE VOA.CODVOL ='KG' AND A.CODPROD = VOA.CODPROD)\r\nELSE A.QTDNEG END),0) AS QTD_CONVERTIDO_KG,\r\n\r\n--10\tC�digo do Modal Utilizado na Movimenta��o\r\n--COALESCE((SELECT CODMODAL_SIMP FROM AD_MODALMOVSIMP MODAL WHERE MODAL.CODMODMOV = B.AD_CODMODMOV),'0') AS CODMODAL_SIMP,--C�digo do Modal Utilizado na Movimenta��o\r\n'1' AS CODMODAL_SIMP,--C�digo do Modal Utilizado na Movimenta��o\r\n\r\n--11 C�digo do oleoduto de transporte\r\n'0' AS COD_OLEODUTO,\r\n\r\n--12 Identifica��o do Terceiro Envolvido na Opera��o\r\n(CASE WHEN D.AD_CODINSTALACAO_ANP IS NULL THEN D.CGC_CPF ELSE '0' END) AS CNPJ_PARC,\r\n\r\n--13 C�digo do Munic�pio (Origem/Destino)\r\n(CASE WHEN  (D.AD_CODINSTALACAO_ANP IS NULL OR COALESCE(E.AD_TEM_COLETA,'N') = 'S') THEN COALESCE((SELECT CID.AD_CODCID_ANP FROM TSICID CID WHERE CID.CODCID = D.CODCID),'0') ELSE '0' END) AS CODCID_ANP,\r\n\r\n--14 C�digo de Atividade Econ�mica do Terceiro\r\n(CASE WHEN  (D.AD_CODINSTALACAO_ANP IS NULL AND D.CGC_CPF IS NOT NULL) THEN COALESCE(D.AD_CNAE_ANP,'0') ELSE '0' END) AS AD_CNAE_ANP,\r\n\r\n--15 C�digo do Pa�s (Origem/Destino)\r\n(CASE WHEN (SELECT CODOPER_ANP FROM AD_OPERACAOANP ANP WHERE ANP.CODOPER = C.AD_CODOPERANP) IN ('2011001', '2012001') THEN (SELECT AD_CODPAI_ANP FROM TSICID CID\r\nINNER JOIN TSIUFS UF ON CID.UF = UF.CODUF\r\nINNER JOIN TSIPAI PAI ON UF.CODPAIS = PAI.CODPAIS\r\nWHERE D.CODCID = CID.CODCID) ELSE '0' END) AS COD_PAIS_ANP,--C�digo ANP Pais,\r\n\r\n--16 N�mero da Licen�a de Importa��o (LI)\r\nCOALESCE(B.AD_LICENCA_IMPORTACAO_LI,'0') AS LICENCA_IMPORTACAO_LI,\r\n\r\n--17 N�mero da Declara��o de Importa��o (DI)\r\nCOALESCE(AD_NUM_DELCARACAO_IMPORT_DI,'0') AS NUM_DELCARACAO_IMPORTACAO_DI,\r\n\r\n--18 N�mero da Nota Fiscal da Opera��o Comercial -\r\n'0' AS NRONF,\r\n\r\n--19 C�digo da S�rie da Nota Fiscal da Opera��o Comercial\r\n(CASE WHEN (CASE WHEN B.CHAVENFE IS NOT NULL THEN B.CHAVENFE ELSE B.CHAVECTE END) IS NULL THEN COALESCE((SELECT SER.COD_SIMP FROM AD_SERIENFOPERSIMP SER WHERE SER.CODSEROPER = B.AD_CODSEROPER),'0') ELSE '0' END) AS COD_SERIE_NF,\r\n\r\n--20 Data da Opera��o Comercial/NF-E (DDMMAAAA)\r\n(CASE WHEN B.DTENTSAI IS NOT NULL THEN TO_CHAR(B.DTENTSAI, 'DDMMYYYY') ELSE '0' END) AS DTENTSAI,\r\n\r\n--21 C�digo do Tipo de Tarifa de Servi�o (Oleodutos e Terminais de combust�veis l�quidos)\r\n'0' AS COD_TIP_TAR_SERV,\r\n\r\n--22 CARACTERISTICA Inativo - preencher com zeros\r\n'0' AS CARACTERISTICA_INATIVO,\r\n\r\n--23 METODO Inativo - preencher com zeros\r\n'0' AS METODO_INATIVO,\r\n\r\n--24 Modalidade do frete\r\n(CASE WHEN CIF_FOB = 'C' THEN 10\r\n    WHEN CIF_FOB = 'F' THEN\t11\r\n    WHEN CIF_FOB = 'T' THEN\t12\r\n    WHEN CIF_FOB = 'R' THEN\t13\r\n    WHEN CIF_FOB = 'D' THEN\t14\r\n    WHEN CIF_FOB = 'S' THEN\t19\r\n    ELSE 19 END)AS MODALIDADE_FRETE,\r\n\r\n--25 N�mero do Documento da Qualidade / Quando o ARI for Distribuidor de Produtos Asf�lticos considerar-se-� o campo como Pre�o (R$/kg)\r\n'0' AS VALOR_CARACTERISTICA,\r\n\r\n--26 C�digo do Produto/Opera��o Resultante\r\nCOALESCE(E.CODANP,0) AS CODPROD_RESULTANTE_ANP,\r\n\r\n--27\tValor unit�rio (nota fiscal)\r\n\tCOALESCE(A.VLRUNIT,100) AS VLRUNIT, --COALESCE(A.VLRUNIT,0)\r\n\r\n--28 Recipiente de GLP\r\n'0' AS RECIPIENTE_GLP,\r\n\r\n--29 Chave de acesso da NF-e ou do CT-e\r\n(CASE WHEN B.CHAVENFE IS NOT NULL THEN B.CHAVENFE ELSE B.CHAVECTE END) AS CHAVE_NFE_CTE,\r\n\r\n(CASE WHEN D.AD_CODINSTALACAO_ANP IS NOT NULL THEN C.AD_CODOPERTOTALIZADORANP ELSE AD_CODOPERANPAGENTENREGULADOTOT END) AS OPER_TOTALIZADOR, b.IDIPROC\r\n\r\n\r\nFROM TGFITE A\r\nINNER JOIN TGFCAB B ON A.NUNOTA = B.NUNOTA\r\nINNER JOIN TGFTOP C ON B.CODTIPOPER = C.CODTIPOPER AND DHALTER = (SELECT MAX(DHALTER) FROM TGFTOP TOP WHERE C.CODTIPOPER = TOP.CODTIPOPER)\r\nINNER JOIN TGFPAR D ON B.CODPARC = D.CODPARC\r\nINNER JOIN TGFPRO E ON A.CODPROD = E.CODPROD\r\nINNER JOIN TSIEMP F ON B.CODEMP = F.CODEMP\r\nWHERE COALESCE(C.AD_ATUALIZA_EST_SIMP,'N') = 'S'\r\nAND COALESCE(E.AD_GERA_SIMP,'N') = 'S'\r\nAND B.STATUSNOTA  = 'L' \r\nAND B.TIPMOV = 'F' \r\nAND TO_CHAR(B.DTNEG,'mm-yyyy') = '" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  401 */       referencia + "'\r\n" + 
/*  402 */       "AND B.CODEMP = " + codEmp + "\r\n" + 
/*  403 */       " AND (CASE WHEN (E.USOPROD = '2') THEN '1' ELSE \r\n" + 
/*  404 */       "           CASE WHEN(E.USOPROD = 'V') then '2' else (CASE WHEN(A.USOPROD = 'M') THEN (SELECT CLASSE FROM AD_OPERACAOANP OPER WHERE OPER.CODOPER = C.AD_CODOPERANPMP) ELSE \r\n" + 
/*  405 */       "    \t(SELECT CLASSE FROM AD_OPERACAOANP OPER WHERE OPER.CODOPER = C.AD_CODOPERANP) END) END end) = \r\n" + 
/*  406 */       "      (CASE WHEN(A.USOPROD = 'M') THEN (SELECT CLASSE FROM AD_OPERACAOANP OPER WHERE OPER.CODOPER = C.AD_CODOPERANPMP) ELSE \r\n" + 
/*  407 */       "    \t(SELECT CLASSE FROM AD_OPERACAOANP OPER WHERE OPER.CODOPER = C.AD_CODOPERANP) END)" + 
/*  408 */       "\r\n" + 
/*  409 */       "AND A.NUNOTA || '-' || A.SEQUENCIA NOT IN (SELECT \r\n" + 
/*  410 */       "                                            VAR.NUNOTAORIG || '-' || VAR.SEQUENCIAORIG\r\n" + 
/*  411 */       "                                            FROM TGFVAR VAR\r\n" + 
/*  412 */       "                                            INNER JOIN TGFCAB CAB ON VAR.NUNOTA = CAB.NUNOTA\r\n" + 
/*  413 */       "                                            INNER JOIN TGFTOP TOP ON CAB.CODTIPOPER = TOP.CODTIPOPER AND DHALTER = (SELECT MAX(TOP2.DHALTER) FROM TGFTOP TOP2 WHERE TOP.CODTIPOPER = TOP2.CODTIPOPER) \r\n" + 
/*  414 */       "                                            WHERE COALESCE(TOP.AD_CANCELA_SIMP,'N') = 'S')\r\n" + 
/*  415 */       ")\r\n" + 
/*  416 */       "GROUP BY \r\n" + 
/*  417 */       "NUNOTA,\r\n" + 
/*  418 */       "AD_TEM_COLETA,\r\n" + 
/*  419 */       "OPER_PAD_DISP_COLETA,\r\n" + 
/*  420 */       "CLASS_OPERACAO,\r\n" + 
/*  421 */       "OPERACAO,\r\n" + 
/*  422 */       "AD_CODEMP_ANP,\r\n" + 
/*  423 */       "COD_INST_EMP,\r\n" + 
/*  424 */       "COD_INST_PAR,\r\n" + 
/*  425 */       "CODPROD_ANP,\r\n" + 
/*  426 */       "CODMODAL_SIMP,\r\n" + 
/*  427 */       "COD_OLEODUTO,\r\n" + 
/*  428 */       "CNPJ_PARC,\r\n" + 
/*  429 */       "CODCID_ANP,\r\n" + 
/*  430 */       "AD_CNAE_ANP,\r\n" + 
/*  431 */       "COD_PAIS_ANP,\r\n" + 
/*  432 */       "LICENCA_IMPORTACAO_LI,\r\n" + 
/*  433 */       "NUM_DELCARACAO_IMPORTACAO_DI,\r\n" + 
/*  434 */       "NRONF,\r\n" + 
/*  435 */       "COD_SERIE_NF,\r\n" + 
/*  436 */       "DTENTSAI,\r\n" + 
/*  437 */       "COD_TIP_TAR_SERV,\r\n" + 
/*  438 */       "CARACTERISTICA_INATIVO,\r\n" + 
/*  439 */       "METODO_INATIVO,\r\n" + 
/*  440 */       "MODALIDADE_FRETE,\r\n" + 
/*  441 */       "VALOR_CARACTERISTICA,\r\n" + 
/*  442 */       "CODPROD_RESULTANTE_ANP,\r\n" + 
/*  443 */       "VLRUNIT,\r\n" + 
/*  444 */       "RECIPIENTE_GLP,\r\n" + 
/*  445 */       "CHAVE_NFE_CTE,\r\n" + 
/*  446 */       "OPER_TOTALIZADOR,IDIPROC";
/*      */     
/*  448 */     String sql1 = "WITH\r\n-- Parâmetros centralizados (substitua pelos binds do seu framework)\r\nPARMS AS (\r\n  SELECT '" + 
/*      */ 
/*      */       
/*  451 */       referencia + "' AS MES_REF, " + codEmp + " AS CODEMP FROM DUAL\r\n" + 
/*  452 */       "),\r\n" + 
/*  453 */       "\r\n" + 
/*  454 */       "-- TGFTOP mais recente por operação\r\n" + 
/*  455 */       "TOP_LATEST AS (\r\n" + 
/*  456 */       "  SELECT t.CODTIPOPER, t.DHALTER\r\n" + 
/*  457 */       "  FROM (\r\n" + 
/*  458 */       "    SELECT CODTIPOPER, MAX(DHALTER) AS DHALTER\r\n" + 
/*  459 */       "    FROM TGFTOP\r\n" + 
/*  460 */       "    GROUP BY CODTIPOPER\r\n" + 
/*  461 */       "  ) x\r\n" + 
/*  462 */       "  JOIN TGFTOP t\r\n" + 
/*  463 */       "    ON t.CODTIPOPER = x.CODTIPOPER\r\n" + 
/*  464 */       "   AND t.DHALTER   = x.DHALTER\r\n" + 
/*  465 */       "),\r\n" + 
/*  466 */       "\r\n" + 
/*  467 */       "-- Tabela de operações ANP (principal e para MP)\r\n" + 
/*  468 */       "OP_ANP AS (\r\n" + 
/*  469 */       "  SELECT CODOPER, CLASSE, CODOPER_ANP\r\n" + 
/*  470 */       "  FROM AD_OPERACAOANP\r\n" + 
/*  471 */       "),\r\n" + 
/*  472 */       "-- Parâmetro único (evita subquery repetida)\r\n" + 
/*  473 */       "PARAM_COLETA AS (\r\n" + 
/*  474 */       "  SELECT INTEIRO AS OPER_PAD_DISP_COLETA\r\n" + 
/*  475 */       "  FROM TSIPAR\r\n" + 
/*  476 */       "  WHERE CHAVE = 'OPERDISPCOLETA'\r\n" + 
/*  477 */       "),\r\n" + 
/*  478 */       "\r\n" + 
/*  479 */       "-- Conversões por volume para LT e KG (uma linha por produto/unidade)\r\n" + 
/*  480 */       "VOA_LT AS (\r\n" + 
/*  481 */       "  SELECT CODPROD, QUANTIDADE, MULTIPVLR, DIVIDEMULTIPLICA\r\n" + 
/*  482 */       "  FROM TGFVOA\r\n" + 
/*  483 */       "  WHERE CODVOL = 'LT'\r\n" + 
/*  484 */       "),\r\n" + 
/*  485 */       "VOA_KG AS (\r\n" + 
/*  486 */       "  SELECT CODPROD, QUANTIDADE, MULTIPVLR, DIVIDEMULTIPLICA\r\n" + 
/*  487 */       "  FROM TGFVOA\r\n" + 
/*  488 */       "  WHERE CODVOL = 'KG'\r\n" + 
/*  489 */       "),\r\n" + 
/*  490 */       "\r\n" + 
/*  491 */       "-- Cidade/UF/País do parceiro para calcular COD_PAIS_ANP quando aplicável\r\n" + 
/*  492 */       "PARC_GEO AS (\r\n" + 
/*  493 */       "  SELECT\r\n" + 
/*  494 */       "    p.CODPARC,\r\n" + 
/*  495 */       "    p.CODCID,\r\n" + 
/*  496 */       "    p.CGC_CPF,\r\n" + 
/*  497 */       "    p.AD_CNAE_ANP,\r\n" + 
/*  498 */       "    p.AD_CODINSTALACAO_ANP,\r\n" + 
/*  499 */       "    cid.AD_CODCID_ANP,\r\n" + 
/*  500 */       "    pai.AD_CODPAI_ANP\r\n" + 
/*  501 */       "  FROM TGFPAR p\r\n" + 
/*  502 */       "  LEFT JOIN TSICID cid ON cid.CODCID = p.CODCID\r\n" + 
/*  503 */       "  LEFT JOIN TSIUFS uf  ON uf.CODUF  = cid.UF\r\n" + 
/*  504 */       "  LEFT JOIN TSIPAI pai ON pai.CODPAIS = uf.CODPAIS\r\n" + 
/*  505 */       "),\r\n" + 
/*  506 */       "\r\n" + 
/*  507 */       "-- Empresas (códigos ANP)\r\n" + 
/*  508 */       "EMP_ANP AS (\r\n" + 
/*  509 */       "  SELECT CODEMP, COALESCE(AD_CODEMP_ANP,'0') AS AD_CODEMP_ANP,\r\n" + 
/*  510 */       "         COALESCE(AD_CODINSTALACAO_ANP,'0') AS COD_INST_EMP\r\n" + 
/*  511 */       "  FROM TSIEMP\r\n" + 
/*  512 */       "),\r\n" + 
/*  513 */       "\r\n" + 
/*  514 */       "-- Séries (quando não há chave NFe/CTe)\r\n" + 
/*  515 */       "SERIE_SIMP AS (\r\n" + 
/*  516 */       "  SELECT CODSEROPER, COD_SIMP\r\n" + 
/*  517 */       "  FROM AD_SERIENFOPERSIMP\r\n" + 
/*  518 */       "),\r\n" + 
/*  519 */       "\r\n" + 
/*  520 */       "-- Itens que devem ser cancelados no SIMP (para exclusão)\r\n" + 
/*  521 */       "CANCELA_SIMP AS (\r\n" + 
/*  522 */       "  SELECT VAR.NUNOTAORIG || '-' || VAR.SEQUENCIAORIG AS CHAVE\r\n" + 
/*  523 */       "  FROM TGFVAR VAR\r\n" + 
/*  524 */       "  JOIN TGFCAB CAB        ON CAB.NUNOTA     = VAR.NUNOTA\r\n" + 
/*  525 */       "  JOIN TOP_LATEST TL     ON TL.CODTIPOPER  = CAB.CODTIPOPER\r\n" + 
/*  526 */       "  JOIN TGFTOP TOP        ON TOP.CODTIPOPER = TL.CODTIPOPER AND TOP.DHALTER = TL.DHALTER\r\n" + 
/*  527 */       "  WHERE COALESCE(TOP.AD_CANCELA_SIMP,'N') = 'S'\r\n" + 
/*  528 */       "),\r\n" + 
/*  529 */       "\r\n" + 
/*  530 */       "-- Base unificada com todos os cálculos linha a linha (sem agregação)\r\n" + 
/*  531 */       "BASE AS (\r\n" + 
/*  532 */       "  SELECT\r\n" + 
/*  533 */       "    B.IDIPROC,\r\n" + 
/*  534 */       "    A.NUNOTA,\r\n" + 
/*  535 */       "    A.SEQUENCIA,\r\n" + 
/*  536 */       "    A.CODPROD,\r\n" + 
/*  537 */       "\r\n" + 
/*  538 */       "    COALESCE(PROD.AD_TEM_COLETA,'N') AS AD_TEM_COLETA,\r\n" + 
/*  539 */       "    (SELECT OPER_PAD_DISP_COLETA FROM PARAM_COLETA) AS OPER_PAD_DISP_COLETA,\r\n" + 
/*  540 */       "\r\n" + 
/*  541 */       "    -- Joins para descobrir classe/operacao considerando material produzido (USOPROD='M')\r\n" + 
/*  542 */       "    CASE\r\n" + 
/*  543 */       "      WHEN A.USOPROD = 'M' THEN OP_MP.CLASSE\r\n" + 
/*  544 */       "      ELSE OP.CLASSE\r\n" + 
/*  545 */       "    END AS CLASS_OPERACAO,\r\n" + 
/*  546 */       "\r\n" + 
/*  547 */       "    CASE\r\n" + 
/*  548 */       "      WHEN A.USOPROD = 'M' THEN TOPER.AD_CODOPERANPMP\r\n" + 
/*  549 */       "      ELSE COALESCE(TOPER.AD_CODOPERANP, TOPER.AD_CODOPERANPAGENTENREGULADO)\r\n" + 
/*  550 */       "    END AS OPERACAO,\r\n" + 
/*  551 */       "\r\n" + 
/*  552 */       "    EMP.AD_CODEMP_ANP,\r\n" + 
/*  553 */       "    EMP.COD_INST_EMP,\r\n" + 
/*  554 */       "\r\n" + 
/*  555 */       "    COALESCE(PG.AD_CODINSTALACAO_ANP,'0') AS COD_INST_PAR,\r\n" + 
/*  556 */       "\r\n" + 
/*  557 */       "    TO_CHAR(COALESCE(PROD.CODANP,0)) AS CODPROD_ANP,\r\n" + 
/*  558 */       "\r\n" + 
/*  559 */       "    -- Quantidade padrão / em KG (a lógica é idêntica no original)\r\n" + 
/*  560 */       "    /* Conversão comum encapsulada: */\r\n" + 
/*  561 */       "    CASE\r\n" + 
/*  562 */       "      WHEN PROD.CODVOL IN ('LT','KG') THEN A.QTDNEG\r\n" + 
/*  563 */       "      WHEN PROD.PESOLIQ <> 0          THEN PROD.PESOLIQ * A.QTDNEG\r\n" + 
/*  564 */       "      WHEN PROD.CODVOL <> 'LT' AND PROD.PESOLIQ = 0 AND VOA_LT.CODPROD IS NOT NULL THEN\r\n" + 
/*  565 */       "        CASE WHEN VOA_LT.DIVIDEMULTIPLICA = 'M'\r\n" + 
/*  566 */       "             THEN (A.QTDNEG * VOA_LT.QUANTIDADE) * VOA_LT.MULTIPVLR\r\n" + 
/*  567 */       "             ELSE TRUNC(((A.QTDNEG / VOA_LT.QUANTIDADE) * VOA_LT.MULTIPVLR), 3)\r\n" + 
/*  568 */       "        END\r\n" + 
/*  569 */       "      WHEN PROD.CODVOL <> 'KG' AND PROD.PESOLIQ = 0 AND VOA_KG.CODPROD IS NOT NULL THEN\r\n" + 
/*  570 */       "        CASE WHEN VOA_KG.DIVIDEMULTIPLICA = 'M'\r\n" + 
/*  571 */       "             THEN (A.QTDNEG * VOA_KG.QUANTIDADE) * VOA_KG.MULTIPVLR\r\n" + 
/*  572 */       "             ELSE TRUNC(((A.QTDNEG / VOA_KG.QUANTIDADE) * VOA_KG.MULTIPVLR), 3)\r\n" + 
/*  573 */       "        END\r\n" + 
/*  574 */       "      ELSE A.QTDNEG\r\n" + 
/*  575 */       "    END AS QTD_UN_PADRAO,\r\n" + 
/*  576 */       "\r\n" + 
/*  577 */       "    CASE\r\n" + 
/*  578 */       "      WHEN PROD.CODVOL IN ('LT','KG') THEN A.QTDNEG\r\n" + 
/*  579 */       "      WHEN PROD.PESOLIQ <> 0          THEN PROD.PESOLIQ * A.QTDNEG\r\n" + 
/*  580 */       "      WHEN PROD.CODVOL <> 'LT' AND PROD.PESOLIQ = 0 AND VOA_LT.CODPROD IS NOT NULL THEN\r\n" + 
/*  581 */       "        CASE WHEN VOA_LT.DIVIDEMULTIPLICA = 'M'\r\n" + 
/*  582 */       "             THEN (A.QTDNEG * VOA_LT.QUANTIDADE) * VOA_LT.MULTIPVLR\r\n" + 
/*  583 */       "             ELSE TRUNC(((A.QTDNEG / VOA_LT.QUANTIDADE) * VOA_LT.MULTIPVLR), 3)\r\n" + 
/*  584 */       "        END\r\n" + 
/*  585 */       "      WHEN PROD.CODVOL <> 'KG' AND PROD.PESOLIQ = 0 AND VOA_KG.CODPROD IS NOT NULL THEN\r\n" + 
/*  586 */       "        CASE WHEN VOA_KG.DIVIDEMULTIPLICA = 'M'\r\n" + 
/*  587 */       "             THEN (A.QTDNEG * VOA_KG.QUANTIDADE) * VOA_KG.MULTIPVLR\r\n" + 
/*  588 */       "             ELSE TRUNC(((A.QTDNEG / VOA_KG.QUANTIDADE) * VOA_KG.MULTIPVLR), 3)\r\n" + 
/*  589 */       "        END\r\n" + 
/*  590 */       "      ELSE A.QTDNEG\r\n" + 
/*  591 */       "    END AS QTD_CONVERTIDO_KG,\r\n" + 
/*  592 */       "\r\n" + 
/*  593 */       "    '1' AS CODMODAL_SIMP,\r\n" + 
/*  594 */       "    '0' AS COD_OLEODUTO,\r\n" + 
/*  595 */       "\r\n" + 
/*  596 */       "    CASE WHEN PG.AD_CODINSTALACAO_ANP IS NULL THEN PG.CGC_CPF ELSE '0' END AS CNPJ_PARC,\r\n" + 
/*  597 */       "\r\n" + 
/*  598 */       "    CASE\r\n" + 
/*  599 */       "      WHEN (PG.AD_CODINSTALACAO_ANP IS NULL OR COALESCE(PROD.AD_TEM_COLETA,'N') = 'S')\r\n" + 
/*  600 */       "           THEN COALESCE(PG.AD_CODCID_ANP,'0')\r\n" + 
/*  601 */       "      ELSE '0'\r\n" + 
/*  602 */       "    END AS CODCID_ANP,\r\n" + 
/*  603 */       "\r\n" + 
/*  604 */       "    CASE\r\n" + 
/*  605 */       "      WHEN PG.AD_CODINSTALACAO_ANP IS NULL AND PG.CGC_CPF IS NOT NULL\r\n" + 
/*  606 */       "           THEN COALESCE(PG.AD_CNAE_ANP,'0')\r\n" + 
/*  607 */       "      ELSE '0'\r\n" + 
/*  608 */       "    END AS AD_CNAE_ANP,\r\n" + 
/*  609 */       "\r\n" + 
/*  610 */       "    CASE\r\n" + 
/*  611 */       "      WHEN OP.CODOPER_ANP IN ('2011001','2012001') THEN COALESCE(PG.AD_CODPAI_ANP,'0')\r\n" + 
/*  612 */       "      ELSE '0'\r\n" + 
/*  613 */       "    END AS COD_PAIS_ANP,\r\n" + 
/*  614 */       "\r\n" + 
/*  615 */       "    COALESCE(B.AD_LICENCA_IMPORTACAO_LI,'0') AS LICENCA_IMPORTACAO_LI,\r\n" + 
/*  616 */       "    COALESCE(B.AD_NUM_DELCARACAO_IMPORT_DI,'0') AS NUM_DELCARACAO_IMPORTACAO_DI,\r\n" + 
/*  617 */       "\r\n" + 
/*  618 */       "    '0' AS NRONF,\r\n" + 
/*  619 */       "\r\n" + 
/*  620 */       "    CASE\r\n" + 
/*  621 */       "      WHEN COALESCE(B.CHAVENFE, B.CHAVECTE) IS NULL\r\n" + 
/*  622 */       "        THEN COALESCE(SER.COD_SIMP,'0')\r\n" + 
/*  623 */       "      ELSE '0'\r\n" + 
/*  624 */       "    END AS COD_SERIE_NF,\r\n" + 
/*  625 */       "\r\n" + 
/*  626 */       "    CASE WHEN B.DTENTSAI IS NOT NULL THEN TO_CHAR(B.DTENTSAI,'DDMMYYYY') ELSE '0' END AS DTENTSAI,\r\n" + 
/*  627 */       "\r\n" + 
/*  628 */       "    '0' AS COD_TIP_TAR_SERV,\r\n" + 
/*  629 */       "    '0' AS CARACTERISTICA_INATIVO,\r\n" + 
/*  630 */       "    '0' AS METODO_INATIVO,\r\n" + 
/*  631 */       "\r\n" + 
/*  632 */       "    CASE\r\n" + 
/*  633 */       "      WHEN B.CIF_FOB = 'C' THEN 10\r\n" + 
/*  634 */       "      WHEN B.CIF_FOB = 'F' THEN 11\r\n" + 
/*  635 */       "      WHEN B.CIF_FOB = 'T' THEN 12\r\n" + 
/*  636 */       "      WHEN B.CIF_FOB = 'R' THEN 13\r\n" + 
/*  637 */       "      WHEN B.CIF_FOB = 'D' THEN 14\r\n" + 
/*  638 */       "      WHEN B.CIF_FOB = 'S' THEN 19\r\n" + 
/*  639 */       "      ELSE 19\r\n" + 
/*  640 */       "    END AS MODALIDADE_FRETE,\r\n" + 
/*  641 */       "\r\n" + 
/*  642 */       "    '0' AS VALOR_CARACTERISTICA,\r\n" + 
/*  643 */       "\r\n" + 
/*  644 */       "    COALESCE(PROD.CODANP,0) AS CODPROD_RESULTANTE_ANP,\r\n" + 
/*  645 */       "    COALESCE(A.VLRUNIT,100) AS VLRUNIT,\r\n" + 
/*  646 */       "    '0' AS RECIPIENTE_GLP,\r\n" + 
/*  647 */       "\r\n" + 
/*  648 */       "    COALESCE(B.CHAVENFE, B.CHAVECTE) AS CHAVE_NFE_CTE,\r\n" + 
/*  649 */       "\r\n" + 
/*  650 */       "    CASE\r\n" + 
/*  651 */       "      WHEN PG.AD_CODINSTALACAO_ANP IS NOT NULL THEN TOPER.AD_CODOPERTOTALIZADORANP\r\n" + 
/*  652 */       "      ELSE TOPER.AD_CODOPERANPAGENTENREGULADOTOT\r\n" + 
/*  653 */       "    END AS OPER_TOTALIZADOR\r\n" + 
/*  654 */       "\r\n" + 
/*  655 */       "  FROM TGFITE A\r\n" + 
/*  656 */       "  JOIN TGFCAB B          ON B.NUNOTA     = A.NUNOTA\r\n" + 
/*  657 */       "  JOIN TOP_LATEST TL     ON TL.CODTIPOPER= B.CODTIPOPER\r\n" + 
/*  658 */       "  JOIN TGFTOP TOPER      ON TOPER.CODTIPOPER = TL.CODTIPOPER AND TOPER.DHALTER = TL.DHALTER\r\n" + 
/*  659 */       "  JOIN TGFPRO PROD       ON PROD.CODPROD = A.CODPROD\r\n" + 
/*  660 */       "  JOIN EMP_ANP EMP       ON EMP.CODEMP   = B.CODEMP\r\n" + 
/*  661 */       "  JOIN PARC_GEO PG       ON PG.CODPARC   = B.CODPARC\r\n" + 
/*  662 */       "  LEFT JOIN OP_ANP OP    ON OP.CODOPER   = TOPER.AD_CODOPERANP\r\n" + 
/*  663 */       "  LEFT JOIN OP_ANP OP_MP ON OP_MP.CODOPER= TOPER.AD_CODOPERANPMP\r\n" + 
/*  664 */       "  LEFT JOIN VOA_LT       ON VOA_LT.CODPROD = A.CODPROD\r\n" + 
/*  665 */       "  LEFT JOIN VOA_KG       ON VOA_KG.CODPROD = A.CODPROD\r\n" + 
/*  666 */       "  LEFT JOIN SERIE_SIMP SER ON SER.CODSEROPER = B.AD_CODSEROPER\r\n" + 
/*  667 */       "  JOIN PARMS P ON 1=1\r\n" + 
/*  668 */       "  WHERE COALESCE(TOPER.AD_ATUALIZA_EST_SIMP,'N') = 'S'\r\n" + 
/*  669 */       "    AND COALESCE(PROD.AD_GERA_SIMP,'N') = 'S'\r\n" + 
/*  670 */       "    AND B.STATUSNOTA = 'L'\r\n" + 
/*  671 */       "    AND TO_CHAR(B.DTNEG,'mm-yyyy') = P.MES_REF\r\n" + 
/*  672 */       "    AND B.CODEMP = P.CODEMP\r\n" + 
/*  673 */       "),\r\n" + 
/*  674 */       "\r\n" + 
/*  675 */       "-- Filtro 1 (equivalente ao primeiro SELECT do UNION ALL)\r\n" + 
/*  676 */       "F1 AS (\r\n" + 
/*  677 */       "  SELECT *\r\n" + 
/*  678 */       "  FROM BASE X\r\n" + 
/*  679 */       "  WHERE EXISTS (\r\n" + 
/*  680 */       "          SELECT 1 FROM TGFITE A2 WHERE A2.NUNOTA = X.NUNOTA AND A2.SEQUENCIA = X.SEQUENCIA\r\n" + 
/*  681 */       "        )\r\n" + 
/*  682 */       "    AND X.NUNOTA || '-' || X.SEQUENCIA NOT IN (SELECT CHAVE FROM CANCELA_SIMP)\r\n" + 
/*  683 */       "    AND (\r\n" + 
/*  684 */       "          CASE\r\n" + 
/*  685 */       "            WHEN (SELECT USOPROD FROM TGFPRO WHERE CODPROD = X.CODPROD) = '2' THEN '1'\r\n" + 
/*  686 */       "            WHEN (SELECT USOPROD FROM TGFPRO WHERE CODPROD = X.CODPROD) = 'V' THEN '2'\r\n" + 
/*  687 */       "            ELSE X.CLASS_OPERACAO\r\n" + 
/*  688 */       "          END\r\n" + 
/*  689 */       "        )\r\n" + 
/*  690 */       "        =\r\n" + 
/*  691 */       "        X.CLASS_OPERACAO\r\n" + 
/*  692 */       "    AND EXISTS (\r\n" + 
/*  693 */       "          SELECT 1\r\n" + 
/*  694 */       "          FROM TGFCAB B1\r\n" + 
/*  695 */       "          WHERE B1.NUNOTA = X.NUNOTA\r\n" + 
/*  696 */       "            AND B1.TIPMOV = 'F'\r\n" + 
/*  697 */       "        )\r\n" + 
/*  698 */       "),\r\n" + 
/*  699 */       "\r\n" + 
/*  700 */       "-- Filtro 2 (equivalente ao segundo SELECT do UNION ALL)\r\n" + 
/*  701 */       "F2 AS (\r\n" + 
/*  702 */       "  SELECT b.*\r\n" + 
/*  703 */       "  FROM BASE b\r\n" + 
/*  704 */       "  JOIN TGFCAB c ON c.NUNOTA = b.NUNOTA\r\n" + 
/*  705 */       "  WHERE c.CODTIPOPER IN (1666,1667)\r\n" + 
/*  706 */       ")\r\n" + 
/*  707 */       "\r\n" + 
/*  708 */       "-- Agregações finais (duas saídas unidas)\r\n" + 
/*  709 */       "SELECT\r\n" + 
/*  710 */       "  0 AS IDIPROC,\r\n" + 
/*  711 */       "  0 AS NUNOTA,\r\n" + 
/*  712 */       "  LISTAGG(SEQUENCIA, ',') WITHIN GROUP (ORDER BY SEQUENCIA) AS SEQUENCIA,\r\n" + 
/*  713 */       "  LISTAGG(CODPROD , ',') WITHIN GROUP (ORDER BY CODPROD)  AS CODPROD,\r\n" + 
/*  714 */       "  AD_TEM_COLETA,\r\n" + 
/*  715 */       "  OPER_PAD_DISP_COLETA,\r\n" + 
/*  716 */       "  CLASS_OPERACAO,\r\n" + 
/*  717 */       "  OPERACAO,\r\n" + 
/*  718 */       "  AD_CODEMP_ANP,\r\n" + 
/*  719 */       "  COD_INST_EMP,\r\n" + 
/*  720 */       "  COD_INST_PAR,\r\n" + 
/*  721 */       "  CODPROD_ANP,\r\n" + 
/*  722 */       "  CASE WHEN SUM(TO_NUMBER(COALESCE(QTD_UN_PADRAO,0))) BETWEEN 0 AND 1 THEN ceil(SUM(TO_NUMBER(COALESCE(QTD_UN_PADRAO,0)))) ELSE SUM(TO_NUMBER(COALESCE(QTD_UN_PADRAO,0))) END AS QTD_UN_PADRAO,\r\n" + 
/*  723 */       "  CASE WHEN SUM(TO_NUMBER(COALESCE(QTD_CONVERTIDO_KG,0))) BETWEEN 0 AND 1 THEN CEIL(SUM(TO_NUMBER(COALESCE(QTD_CONVERTIDO_KG,0)))) ELSE SUM(TO_NUMBER(COALESCE(QTD_CONVERTIDO_KG,0))) END AS QTD_CONVERTIDO_KG,\r\n" + 
/*  724 */       "  CODMODAL_SIMP,\r\n" + 
/*  725 */       "  COD_OLEODUTO,\r\n" + 
/*  726 */       "  CNPJ_PARC,\r\n" + 
/*  727 */       "  CASE WHEN COALESCE(CNPJ_PARC,'0')='0' AND COALESCE(AD_TEM_COLETA,'N')='N' THEN '0' ELSE CODCID_ANP END AS CODCID_ANP,\r\n" + 
/*  728 */       "  AD_CNAE_ANP,\r\n" + 
/*  729 */       "  COD_PAIS_ANP,\r\n" + 
/*  730 */       "  LICENCA_IMPORTACAO_LI,\r\n" + 
/*  731 */       "  NUM_DELCARACAO_IMPORTACAO_DI,\r\n" + 
/*  732 */       "  0 AS NRONF,\r\n" + 
/*  733 */       "  COD_SERIE_NF,\r\n" + 
/*  734 */       "  '00000000' AS DTENTSAI,\r\n" + 
/*  735 */       "  COD_TIP_TAR_SERV,\r\n" + 
/*  736 */       "  CARACTERISTICA_INATIVO,\r\n" + 
/*  737 */       "  METODO_INATIVO,\r\n" + 
/*  738 */       "  MODALIDADE_FRETE,\r\n" + 
/*  739 */       "  VALOR_CARACTERISTICA,\r\n" + 
/*  740 */       "  CODPROD_RESULTANTE_ANP,\r\n" + 
/*  741 */       "  VLRUNIT,\r\n" + 
/*  742 */       "  RECIPIENTE_GLP,\r\n" + 
/*  743 */       "  CHAVE_NFE_CTE,\r\n" + 
/*  744 */       "  OPER_TOTALIZADOR\r\n" + 
/*  745 */       "FROM F1\r\n" + 
/*  746 */       "GROUP BY\r\n" + 
/*  747 */       "  AD_TEM_COLETA, OPER_PAD_DISP_COLETA, CLASS_OPERACAO, OPERACAO,\r\n" + 
/*  748 */       "  AD_CODEMP_ANP, COD_INST_EMP, COD_INST_PAR, CODPROD_ANP, CODMODAL_SIMP, COD_OLEODUTO,\r\n" + 
/*  749 */       "  CNPJ_PARC, CODCID_ANP, AD_CNAE_ANP, COD_PAIS_ANP, LICENCA_IMPORTACAO_LI,\r\n" + 
/*  750 */       "  NUM_DELCARACAO_IMPORTACAO_DI, COD_SERIE_NF, COD_TIP_TAR_SERV,\r\n" + 
/*  751 */       "  CARACTERISTICA_INATIVO, METODO_INATIVO, MODALIDADE_FRETE, VALOR_CARACTERISTICA,\r\n" + 
/*  752 */       "  CODPROD_RESULTANTE_ANP, VLRUNIT, RECIPIENTE_GLP, CHAVE_NFE_CTE, OPER_TOTALIZADOR\r\n" + 
/*  753 */       "\r\n" + 
/*  754 */       "UNION ALL\r\n" + 
/*  755 */       "\r\n" + 
/*  756 */       "SELECT\r\n" + 
/*  757 */       "  0 AS IDIPROC,\r\n" + 
/*  758 */       "  0 AS NUNOTA,\r\n" + 
/*  759 */       "  LISTAGG(SEQUENCIA, ',') WITHIN GROUP (ORDER BY SEQUENCIA) AS SEQUENCIA,\r\n" + 
/*  760 */       "  LISTAGG(CODPROD , ',') WITHIN GROUP (ORDER BY CODPROD)  AS CODPROD,\r\n" + 
/*  761 */       "  AD_TEM_COLETA,\r\n" + 
/*  762 */       "  OPER_PAD_DISP_COLETA,\r\n" + 
/*  763 */       "  CLASS_OPERACAO,\r\n" + 
/*  764 */       "  OPERACAO,\r\n" + 
/*  765 */       "  AD_CODEMP_ANP,\r\n" + 
/*  766 */       "  COD_INST_EMP,\r\n" + 
/*  767 */       "  COD_INST_PAR,\r\n" + 
/*  768 */       "  CODPROD_ANP,\r\n" + 
/*  769 */       "  SUM(TO_NUMBER(COALESCE(QTD_UN_PADRAO,0)))     AS QTD_UN_PADRAO,\r\n" + 
/*  770 */       "  SUM(TO_NUMBER(COALESCE(QTD_CONVERTIDO_KG,0))) AS QTD_CONVERTIDO_KG,\r\n" + 
/*  771 */       "  CODMODAL_SIMP,\r\n" + 
/*  772 */       "  COD_OLEODUTO,\r\n" + 
/*  773 */       "  CNPJ_PARC,\r\n" + 
/*  774 */       "  CASE WHEN COALESCE(CNPJ_PARC,'0')='0' AND COALESCE(AD_TEM_COLETA,'N')='N' THEN '0' ELSE CODCID_ANP END AS CODCID_ANP,\r\n" + 
/*  775 */       "  AD_CNAE_ANP,\r\n" + 
/*  776 */       "  COD_PAIS_ANP,\r\n" + 
/*  777 */       "  LICENCA_IMPORTACAO_LI,\r\n" + 
/*  778 */       "  NUM_DELCARACAO_IMPORTACAO_DI,\r\n" + 
/*  779 */       "  0 AS NRONF,\r\n" + 
/*  780 */       "  COD_SERIE_NF,\r\n" + 
/*  781 */       "  '00000000' AS DTENTSAI,\r\n" + 
/*  782 */       "  COD_TIP_TAR_SERV,\r\n" + 
/*  783 */       "  CARACTERISTICA_INATIVO,\r\n" + 
/*  784 */       "  METODO_INATIVO,\r\n" + 
/*  785 */       "  MODALIDADE_FRETE,\r\n" + 
/*  786 */       "  VALOR_CARACTERISTICA,\r\n" + 
/*  787 */       "  CODPROD_RESULTANTE_ANP,\r\n" + 
/*  788 */       "  VLRUNIT,\r\n" + 
/*  789 */       "  RECIPIENTE_GLP,\r\n" + 
/*  790 */       "  CHAVE_NFE_CTE,\r\n" + 
/*  791 */       "  OPER_TOTALIZADOR\r\n" + 
/*  792 */       "FROM F2\r\n" + 
/*  793 */       "GROUP BY\r\n" + 
/*  794 */       "  AD_TEM_COLETA, OPER_PAD_DISP_COLETA, CLASS_OPERACAO, OPERACAO,\r\n" + 
/*  795 */       "  AD_CODEMP_ANP, COD_INST_EMP, COD_INST_PAR, CODPROD_ANP, CODMODAL_SIMP, COD_OLEODUTO,\r\n" + 
/*  796 */       "  CNPJ_PARC, CODCID_ANP, AD_CNAE_ANP, COD_PAIS_ANP, LICENCA_IMPORTACAO_LI,\r\n" + 
/*  797 */       "  NUM_DELCARACAO_IMPORTACAO_DI, COD_SERIE_NF, COD_TIP_TAR_SERV,\r\n" + 
/*  798 */       "  CARACTERISTICA_INATIVO, METODO_INATIVO, MODALIDADE_FRETE, VALOR_CARACTERISTICA,\r\n" + 
/*  799 */       "  CODPROD_RESULTANTE_ANP, VLRUNIT, RECIPIENTE_GLP, CHAVE_NFE_CTE, OPER_TOTALIZADOR";
/*      */ 
/*      */ 
/*      */     
/*  803 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql1);
/*  804 */     ResultSet query = pstm.executeQuery();
/*  805 */     return query;
/*      */   }
/*      */ 
/*      */   
/*      */   public static ResultSet movimentacoes(JdbcWrapper jdbcWrapper, String referencia, String codEmp) throws Exception {
/*  810 */     String sql = "WITH\r\nPARMS AS (\r\n  SELECT '" + 
/*      */       
/*  812 */       referencia + "' AS MES_REF, " + codEmp + " AS CODEMP FROM DUAL\r\n" + 
/*  813 */       "),\r\n" + 
/*  814 */       "TOP_LATEST AS (\r\n" + 
/*  815 */       "  SELECT t.CODTIPOPER, t.DHALTER\r\n" + 
/*  816 */       "  FROM (\r\n" + 
/*  817 */       "    SELECT CODTIPOPER, MAX(DHALTER) AS DHALTER\r\n" + 
/*  818 */       "    FROM TGFTOP\r\n" + 
/*  819 */       "    GROUP BY CODTIPOPER\r\n" + 
/*  820 */       "  ) x\r\n" + 
/*  821 */       "  JOIN TGFTOP t\r\n" + 
/*  822 */       "    ON t.CODTIPOPER = x.CODTIPOPER\r\n" + 
/*  823 */       "   AND t.DHALTER   = x.DHALTER\r\n" + 
/*  824 */       "),\r\n" + 
/*  825 */       "PARAM_COLETA AS (\r\n" + 
/*  826 */       "  SELECT INTEIRO AS OPER_PAD_DISP_COLETA\r\n" + 
/*  827 */       "  FROM TSIPAR\r\n" + 
/*  828 */       "  WHERE CHAVE = 'OPERDISPCOLETA'\r\n" + 
/*  829 */       "),\r\n" + 
/*  830 */       "VOA_LT AS (\r\n" + 
/*  831 */       "  SELECT CODPROD, QUANTIDADE, MULTIPVLR, DIVIDEMULTIPLICA\r\n" + 
/*  832 */       "  FROM TGFVOA\r\n" + 
/*  833 */       "  WHERE CODVOL = 'LT'\r\n" + 
/*  834 */       "),\r\n" + 
/*  835 */       "VOA_KG AS (\r\n" + 
/*  836 */       "  SELECT CODPROD, QUANTIDADE, MULTIPVLR, DIVIDEMULTIPLICA\r\n" + 
/*  837 */       "  FROM TGFVOA\r\n" + 
/*  838 */       "  WHERE CODVOL = 'KG'\r\n" + 
/*  839 */       "),\r\n" + 
/*  840 */       "PARC_GEO AS (\r\n" + 
/*  841 */       "  SELECT\r\n" + 
/*  842 */       "    p.CODPARC,\r\n" + 
/*  843 */       "    p.CODCID,\r\n" + 
/*  844 */       "    p.CGC_CPF,\r\n" + 
/*  845 */       "    p.AD_CNAE_ANP,\r\n" + 
/*  846 */       "    p.AD_CODINSTALACAO_ANP,\r\n" + 
/*  847 */       "    cid.AD_CODCID_ANP,\r\n" + 
/*  848 */       "    pai.AD_CODPAI_ANP\r\n" + 
/*  849 */       "  FROM TGFPAR p\r\n" + 
/*  850 */       "  LEFT JOIN TSICID cid ON cid.CODCID = p.CODCID\r\n" + 
/*  851 */       "  LEFT JOIN TSIUFS uf  ON uf.CODUF  = cid.UF\r\n" + 
/*  852 */       "  LEFT JOIN TSIPAI pai ON pai.CODPAIS = uf.CODPAIS\r\n" + 
/*  853 */       "),\r\n" + 
/*  854 */       "EMP_ANP AS (\r\n" + 
/*  855 */       "  SELECT CODEMP,\r\n" + 
/*  856 */       "         COALESCE(AD_CODEMP_ANP, '0') AS AD_CODEMP_ANP,\r\n" + 
/*  857 */       "         COALESCE(AD_CODINSTALACAO_ANP, '0') AS COD_INST_EMP\r\n" + 
/*  858 */       "  FROM TSIEMP\r\n" + 
/*  859 */       "),\r\n" + 
/*  860 */       "SERIE_SIMP AS (\r\n" + 
/*  861 */       "  SELECT CODSEROPER, COD_SIMP\r\n" + 
/*  862 */       "  FROM AD_SERIENFOPERSIMP\r\n" + 
/*  863 */       "),\r\n" + 
/*  864 */       "OP_ANP AS (\r\n" + 
/*  865 */       "  SELECT CODOPER, CLASSE, CODOPER_ANP\r\n" + 
/*  866 */       "  FROM AD_OPERACAOANP\r\n" + 
/*  867 */       "),\r\n" + 
/*  868 */       "CANCELA_SIMP AS (\r\n" + 
/*  869 */       "  SELECT VAR.NUNOTAORIG || '-' || VAR.SEQUENCIAORIG AS CHAVE\r\n" + 
/*  870 */       "  FROM TGFVAR VAR\r\n" + 
/*  871 */       "  JOIN TGFCAB CAB   ON CAB.NUNOTA    = VAR.NUNOTA\r\n" + 
/*  872 */       "  JOIN TOP_LATEST T ON T.CODTIPOPER  = CAB.CODTIPOPER\r\n" + 
/*  873 */       "  JOIN TGFTOP TOP   ON TOP.CODTIPOPER= T.CODTIPOPER AND TOP.DHALTER = T.DHALTER\r\n" + 
/*  874 */       "  WHERE COALESCE(TOP.AD_CANCELA_SIMP, 'N') = 'S'\r\n" + 
/*  875 */       "),\r\n" + 
/*  876 */       "BASE AS (\r\n" + 
/*  877 */       "  SELECT\r\n" + 
/*  878 */       "    A.NUNOTA,\r\n" + 
/*  879 */       "    A.SEQUENCIA,\r\n" + 
/*  880 */       "    A.CODPROD,\r\n" + 
/*  881 */       "    COALESCE(PROD.AD_TEM_COLETA, 'N') AS AD_TEM_COLETA,\r\n" + 
/*  882 */       "    (SELECT OPER_PAD_DISP_COLETA FROM PARAM_COLETA) AS OPER_PAD_DISP_COLETA,\r\n" + 
/*  883 */       "    OP.CLASSE AS CLASS_OPERACAO,\r\n" + 
/*  884 */       "    CASE WHEN PG.AD_CODINSTALACAO_ANP IS NOT NULL\r\n" + 
/*  885 */       "         THEN TOPER.AD_CODOPERANP\r\n" + 
/*  886 */       "         ELSE TOPER.AD_CODOPERANPAGENTENREGULADO\r\n" + 
/*  887 */       "    END AS OPERACAO,\r\n" + 
/*  888 */       "    EMP.AD_CODEMP_ANP,\r\n" + 
/*  889 */       "    EMP.COD_INST_EMP,\r\n" + 
/*  890 */       "    COALESCE(PG.AD_CODINSTALACAO_ANP, '0') AS COD_INST_PAR,\r\n" + 
/*  891 */       "    TO_CHAR(COALESCE(PROD.CODANP, 0)) AS CODPROD_ANP,\r\n" + 
/*  892 */       "    CASE\r\n" + 
/*  893 */       "      WHEN PROD.CODVOL IN ('LT','KG') THEN A.QTDNEG\r\n" + 
/*  894 */       "      WHEN PROD.PESOLIQ <> 0          THEN PROD.PESOLIQ * A.QTDNEG\r\n" + 
/*  895 */       "      WHEN PROD.CODVOL <> 'LT' AND PROD.PESOLIQ = 0 AND VOA_LT.CODPROD IS NOT NULL THEN\r\n" + 
/*  896 */       "        CASE WHEN VOA_LT.DIVIDEMULTIPLICA = 'M'\r\n" + 
/*  897 */       "             THEN (A.QTDNEG * VOA_LT.QUANTIDADE) * VOA_LT.MULTIPVLR\r\n" + 
/*  898 */       "             ELSE TRUNC(((A.QTDNEG / VOA_LT.QUANTIDADE) * VOA_LT.MULTIPVLR), 3)\r\n" + 
/*  899 */       "        END\r\n" + 
/*  900 */       "      WHEN PROD.CODVOL <> 'KG' AND PROD.PESOLIQ = 0 AND VOA_KG.CODPROD IS NOT NULL THEN\r\n" + 
/*  901 */       "        CASE WHEN VOA_KG.DIVIDEMULTIPLICA = 'M'\r\n" + 
/*  902 */       "             THEN (A.QTDNEG * VOA_KG.QUANTIDADE) * VOA_KG.MULTIPVLR\r\n" + 
/*  903 */       "             ELSE TRUNC(((A.QTDNEG / VOA_KG.QUANTIDADE) * VOA_KG.MULTIPVLR), 3)\r\n" + 
/*  904 */       "        END\r\n" + 
/*  905 */       "      ELSE A.QTDNEG\r\n" + 
/*  906 */       "    END AS QTD_UN_PADRAO,\r\n" + 
/*  907 */       "    CASE\r\n" + 
/*  908 */       "      WHEN PROD.CODVOL IN ('LT','KG') THEN A.QTDNEG\r\n" + 
/*  909 */       "      WHEN PROD.PESOLIQ <> 0          THEN PROD.PESOLIQ * A.QTDNEG\r\n" + 
/*  910 */       "      WHEN PROD.CODVOL <> 'LT' AND PROD.PESOLIQ = 0 AND VOA_LT.CODPROD IS NOT NULL THEN\r\n" + 
/*  911 */       "        CASE WHEN VOA_LT.DIVIDEMULTIPLICA = 'M'\r\n" + 
/*  912 */       "             THEN (A.QTDNEG * VOA_LT.QUANTIDADE) * VOA_LT.MULTIPVLR\r\n" + 
/*  913 */       "             ELSE TRUNC(((A.QTDNEG / VOA_LT.QUANTIDADE) * VOA_LT.MULTIPVLR), 3)\r\n" + 
/*  914 */       "        END\r\n" + 
/*  915 */       "      WHEN PROD.CODVOL <> 'KG' AND PROD.PESOLIQ = 0 AND VOA_KG.CODPROD IS NOT NULL THEN\r\n" + 
/*  916 */       "        CASE WHEN VOA_KG.DIVIDEMULTIPLICA = 'M'\r\n" + 
/*  917 */       "             THEN (A.QTDNEG * VOA_KG.QUANTIDADE) * VOA_KG.MULTIPVLR\r\n" + 
/*  918 */       "             ELSE TRUNC(((A.QTDNEG / VOA_KG.QUANTIDADE) * VOA_KG.MULTIPVLR), 3)\r\n" + 
/*  919 */       "        END\r\n" + 
/*  920 */       "      ELSE A.QTDNEG\r\n" + 
/*  921 */       "    END AS QTD_CONVERTIDO_KG,\r\n" + 
/*  922 */       "    '1' AS CODMODAL_SIMP,\r\n" + 
/*  923 */       "    '0' AS COD_OLEODUTO,\r\n" + 
/*  924 */       "    CASE WHEN PG.AD_CODINSTALACAO_ANP IS NULL THEN PG.CGC_CPF ELSE '0' END AS CNPJ_PARC,\r\n" + 
/*  925 */       "    CASE\r\n" + 
/*  926 */       "      WHEN (PG.AD_CODINSTALACAO_ANP IS NULL OR COALESCE(PROD.AD_TEM_COLETA,'N') = 'S')\r\n" + 
/*  927 */       "           THEN COALESCE(PG.AD_CODCID_ANP,'0')\r\n" + 
/*  928 */       "      ELSE '0'\r\n" + 
/*  929 */       "    END AS CODCID_ANP,\r\n" + 
/*  930 */       "    CASE\r\n" + 
/*  931 */       "      WHEN (PG.AD_CODINSTALACAO_ANP IS NULL AND PG.CGC_CPF IS NOT NULL)\r\n" + 
/*  932 */       "           THEN COALESCE(PG.AD_CNAE_ANP,'0')\r\n" + 
/*  933 */       "      ELSE '0'\r\n" + 
/*  934 */       "    END AS AD_CNAE_ANP,\r\n" + 
/*  935 */       "    CASE\r\n" + 
/*  936 */       "      WHEN OP.CODOPER_ANP IN ('2011001','2012001') THEN COALESCE(PG.AD_CODPAI_ANP,'0')\r\n" + 
/*  937 */       "      ELSE '0'\r\n" + 
/*  938 */       "    END AS COD_PAIS_ANP,\r\n" + 
/*  939 */       "    COALESCE(B.AD_LICENCA_IMPORTACAO_LI, '0') AS LICENCA_IMPORTACAO_LI,\r\n" + 
/*  940 */       "    COALESCE(B.AD_NUM_DELCARACAO_IMPORT_DI, '0') AS NUM_DELCARACAO_IMPORTACAO_DI,\r\n" + 
/*  941 */       "    '0' AS NRONF,\r\n" + 
/*  942 */       "    CASE\r\n" + 
/*  943 */       "      WHEN COALESCE(B.CHAVENFE, B.CHAVECTE) IS NULL\r\n" + 
/*  944 */       "        THEN COALESCE(SER.COD_SIMP,'0')\r\n" + 
/*  945 */       "      ELSE '0'\r\n" + 
/*  946 */       "    END AS COD_SERIE_NF,\r\n" + 
/*  947 */       "    CASE WHEN B.DTENTSAI IS NOT NULL THEN TO_CHAR(B.DTENTSAI,'DDMMYYYY') ELSE '0' END AS DTENTSAI,\r\n" + 
/*  948 */       "    '0' AS COD_TIP_TAR_SERV,\r\n" + 
/*  949 */       "    '0' AS CARACTERISTICA_INATIVO,\r\n" + 
/*  950 */       "    '0' AS METODO_INATIVO,\r\n" + 
/*  951 */       "    CASE\r\n" + 
/*  952 */       "      WHEN B.CIF_FOB = 'C' THEN 10\r\n" + 
/*  953 */       "      WHEN B.CIF_FOB = 'F' THEN 11\r\n" + 
/*  954 */       "      WHEN B.CIF_FOB = 'T' THEN 12\r\n" + 
/*  955 */       "      WHEN B.CIF_FOB = 'R' THEN 13\r\n" + 
/*  956 */       "      WHEN B.CIF_FOB = 'D' THEN 14\r\n" + 
/*  957 */       "      WHEN B.CIF_FOB = 'S' THEN 19\r\n" + 
/*  958 */       "      ELSE 19\r\n" + 
/*  959 */       "    END AS MODALIDADE_FRETE,\r\n" + 
/*  960 */       "    '0' AS VALOR_CARACTERISTICA,\r\n" + 
/*  961 */       "    COALESCE(PROD.CODANP, 0) AS CODPROD_RESULTANTE_ANP,\r\n" + 
/*  962 */       "    COALESCE(A.VLRUNIT, 100) AS VLRUNIT,\r\n" + 
/*  963 */       "    '0' AS RECIPIENTE_GLP,\r\n" + 
/*  964 */       "    COALESCE(B.CHAVENFE, B.CHAVECTE) AS CHAVE_NFE_CTE,\r\n" + 
/*  965 */       "    CASE\r\n" + 
/*  966 */       "      WHEN PG.AD_CODINSTALACAO_ANP IS NOT NULL THEN TOPER.AD_CODOPERTOTALIZADORANP\r\n" + 
/*  967 */       "      ELSE TOPER.AD_CODOPERANPAGENTENREGULADOTOT\r\n" + 
/*  968 */       "    END AS OPER_TOTALIZADOR\r\n" + 
/*  969 */       "  FROM TGFITE A\r\n" + 
/*  970 */       "  JOIN TGFCAB B          ON B.NUNOTA     = A.NUNOTA\r\n" + 
/*  971 */       "  JOIN TOP_LATEST TL     ON TL.CODTIPOPER= B.CODTIPOPER\r\n" + 
/*  972 */       "  JOIN TGFTOP TOPER      ON TOPER.CODTIPOPER = TL.CODTIPOPER AND TOPER.DHALTER = TL.DHALTER\r\n" + 
/*  973 */       "  JOIN TGFPRO PROD       ON PROD.CODPROD = A.CODPROD\r\n" + 
/*  974 */       "  JOIN EMP_ANP EMP       ON EMP.CODEMP   = B.CODEMP\r\n" + 
/*  975 */       "  JOIN PARC_GEO PG       ON PG.CODPARC   = B.CODPARC\r\n" + 
/*  976 */       "  LEFT JOIN OP_ANP OP    ON OP.CODOPER   = TOPER.AD_CODOPERANP\r\n" + 
/*  977 */       "  LEFT JOIN VOA_LT       ON VOA_LT.CODPROD = A.CODPROD\r\n" + 
/*  978 */       "  LEFT JOIN VOA_KG       ON VOA_KG.CODPROD = A.CODPROD\r\n" + 
/*  979 */       "  LEFT JOIN SERIE_SIMP SER ON SER.CODSEROPER = B.AD_CODSEROPER\r\n" + 
/*  980 */       "  JOIN PARMS P ON 1=1\r\n" + 
/*  981 */       "  WHERE COALESCE(TOPER.AD_ATUALIZA_EST_SIMP, 'N') = 'S'\r\n" + 
/*  982 */       "    AND COALESCE(PROD.AD_GERA_SIMP, 'N') = 'S'\r\n" + 
/*  983 */       "    AND B.STATUSNOTA = 'L'\r\n" + 
/*  984 */       "    AND B.TIPMOV <> 'F'\r\n" + 
/*  985 */       "    AND TO_CHAR(B.DTNEG, 'mm-yyyy') = P.MES_REF\r\n" + 
/*  986 */       "    AND B.CODEMP = P.CODEMP\r\n" + 
/*  987 */       "    AND COALESCE(B.STATUSNFE, 'R') = CASE WHEN B.TIPMOV = 'C' THEN COALESCE(B.STATUSNFE,'R') ELSE 'A' END\r\n" + 
/*  988 */       "    AND (A.NUNOTA || '-' || A.SEQUENCIA) NOT IN (SELECT CHAVE FROM CANCELA_SIMP)\r\n" + 
/*  989 */       ")\r\n" + 
/*  990 */       "SELECT\r\n" + 
/*  991 */       "  '0' AS NUNOTA,\r\n" + 
/*  992 */       "  LISTAGG(SEQUENCIA, ',') WITHIN GROUP (ORDER BY SEQUENCIA) AS SEQUENCIA,\r\n" + 
/*  993 */       "  LISTAGG(CODPROD , ',') WITHIN GROUP (ORDER BY CODPROD)  AS CODPROD,\r\n" + 
/*  994 */       "  AD_TEM_COLETA,\r\n" + 
/*  995 */       "  OPER_PAD_DISP_COLETA,\r\n" + 
/*  996 */       "  CLASS_OPERACAO,\r\n" + 
/*  997 */       "  OPERACAO,\r\n" + 
/*  998 */       "  AD_CODEMP_ANP,\r\n" + 
/*  999 */       "  COD_INST_EMP,\r\n" + 
/* 1000 */       "  COD_INST_PAR,\r\n" + 
/* 1001 */       "  CODPROD_ANP,\r\n" + 
/* 1002 */       "  SUM(TO_NUMBER(COALESCE(QTD_UN_PADRAO, 0)))     AS QTD_UN_PADRAO,\r\n" + 
/* 1003 */       "  SUM(TO_NUMBER(COALESCE(QTD_CONVERTIDO_KG, 0))) AS QTD_CONVERTIDO_KG,\r\n" + 
/* 1004 */       "  CODMODAL_SIMP,\r\n" + 
/* 1005 */       "  COD_OLEODUTO,\r\n" + 
/* 1006 */       "  CNPJ_PARC,\r\n" + 
/* 1007 */       "  CASE WHEN COALESCE(CNPJ_PARC,'0')='0' AND COALESCE(AD_TEM_COLETA,'N')='N' THEN '0' ELSE CODCID_ANP END AS CODCID_ANP,\r\n" + 
/* 1008 */       "  AD_CNAE_ANP,\r\n" + 
/* 1009 */       "  COD_PAIS_ANP,\r\n" + 
/* 1010 */       "  LICENCA_IMPORTACAO_LI,\r\n" + 
/* 1011 */       "  NUM_DELCARACAO_IMPORTACAO_DI,\r\n" + 
/* 1012 */       "  NRONF,\r\n" + 
/* 1013 */       "  COD_SERIE_NF,\r\n" + 
/* 1014 */       "  DTENTSAI,\r\n" + 
/* 1015 */       "  COD_TIP_TAR_SERV,\r\n" + 
/* 1016 */       "  CARACTERISTICA_INATIVO,\r\n" + 
/* 1017 */       "  METODO_INATIVO,\r\n" + 
/* 1018 */       "  MODALIDADE_FRETE,\r\n" + 
/* 1019 */       "  VALOR_CARACTERISTICA,\r\n" + 
/* 1020 */       "  CODPROD_RESULTANTE_ANP,\r\n" + 
/* 1021 */       "  MAX(VLRUNIT) AS VLRUNIT,\r\n" + 
/* 1022 */       "  RECIPIENTE_GLP,\r\n" + 
/* 1023 */       "  CHAVE_NFE_CTE,\r\n" + 
/* 1024 */       "  OPER_TOTALIZADOR\r\n" + 
/* 1025 */       "FROM BASE\r\n" + 
/* 1026 */       "GROUP BY\r\n" + 
/* 1027 */       "  AD_TEM_COLETA,\r\n" + 
/* 1028 */       "  OPER_PAD_DISP_COLETA,\r\n" + 
/* 1029 */       "  CLASS_OPERACAO,\r\n" + 
/* 1030 */       "  OPERACAO,\r\n" + 
/* 1031 */       "  AD_CODEMP_ANP,\r\n" + 
/* 1032 */       "  COD_INST_EMP,\r\n" + 
/* 1033 */       "  COD_INST_PAR,\r\n" + 
/* 1034 */       "  CODPROD_ANP,\r\n" + 
/* 1035 */       "  CODMODAL_SIMP,\r\n" + 
/* 1036 */       "  COD_OLEODUTO,\r\n" + 
/* 1037 */       "  CNPJ_PARC,\r\n" + 
/* 1038 */       "  CODCID_ANP,\r\n" + 
/* 1039 */       "  AD_CNAE_ANP,\r\n" + 
/* 1040 */       "  COD_PAIS_ANP,\r\n" + 
/* 1041 */       "  LICENCA_IMPORTACAO_LI,\r\n" + 
/* 1042 */       "  NUM_DELCARACAO_IMPORTACAO_DI,\r\n" + 
/* 1043 */       "  NRONF,\r\n" + 
/* 1044 */       "  COD_SERIE_NF,\r\n" + 
/* 1045 */       "  DTENTSAI,\r\n" + 
/* 1046 */       "  COD_TIP_TAR_SERV,\r\n" + 
/* 1047 */       "  CARACTERISTICA_INATIVO,\r\n" + 
/* 1048 */       "  METODO_INATIVO,\r\n" + 
/* 1049 */       "  MODALIDADE_FRETE,\r\n" + 
/* 1050 */       "  VALOR_CARACTERISTICA,\r\n" + 
/* 1051 */       "  CODPROD_RESULTANTE_ANP,\r\n" + 
/* 1052 */       "  --VLRUNIT,\r\n" + 
/* 1053 */       "  RECIPIENTE_GLP,\r\n" + 
/* 1054 */       "  CHAVE_NFE_CTE,\r\n" + 
/* 1055 */       "  OPER_TOTALIZADOR";
/* 1056 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/* 1057 */     ResultSet query = pstm.executeQuery();
/* 1058 */     return query;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void producao(JdbcWrapper jdbcWrapper, String referencia, String codEmp, BigDecimal codSimSim) throws Exception {
/* 1063 */     String sql = "INSERT INTO AD_DETSIMULARSIMP (CODSIMSIM, CODDETSIMSIMP, IDIPROC, CODEMP_ANP, MESREFERENCIA, CODOPER, CODINSTALACAO1, CODINSTALACAO2, CODPROD_ANP, QTD_PROD_UN_ANP, QTD_PROD_OPERADO_KG, COD_MODAL_MOVIMENTACAO, \r\nCOD_OLEODUTO_TRANSPORTE, CNPJ_PARC, CODCID_ANP, COD_ATIV_PARC_ANP, CODPAIS_ANP, NUM_LICEN_IMPORT, NUM_DECLARACAO_IMPORT, NUMNF, SERIE_NF, DAT_OPER_COMERCIAL, COD_TIP_TARIFA_SERVICO, CARACTERISTICA, \r\nMETODO, MODALIDADE_FRETE, NUM_DOC_QUALIDADE, COD_OPER_RESULTANTE, VLRUNIT_NF, RECEPIENTE_GLP, CAHVE_NFE_CTE, OPERTOTALIZADOR) \r\n(SELECT\r\n" + 
/*      */ 
/*      */ 
/*      */       
/* 1067 */       codSimSim + " AS CODSIMSIM,\r\n" + 
/* 1068 */       "(SELECT COALESCE(MAX(CODDETSIMSIMP),0) FROM AD_DETSIMULARSIMP WHERE CODSIMSIM = " + codSimSim + ")+ROWNUM AS CODDETSIMSIMP,\r\n" + 
/* 1069 */       "--NUNOTA,\r\n" + 
/* 1070 */       "IDIPROC,\r\n" + 
/* 1071 */       "AD_CODEMP_ANP AS CODEMP_ANP,\r\n" + 
/* 1072 */       "'" + referencia.replace("-", "") + "' AS MESREFERENCIA,\r\n" + 
/* 1073 */       "OPERACAO AS CODOPER,\r\n" + 
/* 1074 */       "COD_INST_EMP AS CODINSTALACAO1,\r\n" + 
/* 1075 */       "COD_INST_PAR AS CODINSTALACAO2,\r\n" + 
/* 1076 */       "CODPROD_ANP AS CODPROD_ANP,\r\n" + 
/* 1077 */       "QTD_UN_PADRAO AS QTD_PROD_UN_ANP,\r\n" + 
/* 1078 */       "QTD_CONVERTIDO_KG AS QTD_PROD_OPERADO_KG,\r\n" + 
/* 1079 */       "CODMODAL_SIMP AS COD_MODAL_MOVIMENTACAO,\r\n" + 
/* 1080 */       "COD_OLEODUTO AS COD_OLEODUTO_TRANSPORTE,\r\n" + 
/* 1081 */       "CNPJ_PARC AS CNPJ_PARC,\r\n" + 
/* 1082 */       "CODCID_ANP AS CODCID_ANP,\r\n" + 
/* 1083 */       "AD_CNAE_ANP AS COD_ATIV_PARC_ANP,\r\n" + 
/* 1084 */       "COD_PAIS_ANP AS CODPAIS_ANP,\r\n" + 
/* 1085 */       "REPLACE(REPLACE(LICENCA_IMPORTACAO_LI,'-',''),'/','') AS NUM_LICEN_IMPORT,\r\n" + 
/* 1086 */       "REPLACE(REPLACE(NUM_DELCARACAO_IMPORTACAO_DI,'-',''),'/','')  AS NUM_DECLARACAO_IMPORT,\r\n" + 
/* 1087 */       "NRONF AS NUMNF,\r\n" + 
/* 1088 */       "COD_SERIE_NF AS SERIE_NF,\r\n" + 
/* 1089 */       "DTENTSAI AS DAT_OPER_COMERCIAL,\r\n" + 
/* 1090 */       "COD_TIP_TAR_SERV AS COD_TIP_TARIFA_SERVICO,\r\n" + 
/* 1091 */       "CARACTERISTICA_INATIVO AS CARACTERISTICA,\r\n" + 
/* 1092 */       "METODO_INATIVO AS METODO,\r\n" + 
/* 1093 */       "MODALIDADE_FRETE AS MODALIDADE_FRETE,\r\n" + 
/* 1094 */       "VALOR_CARACTERISTICA AS NUM_DOC_QUALIDADE,\r\n" + 
/* 1095 */       "CODPROD_RESULTANTE_ANP AS COD_OPER_RESULTANTE,\r\n" + 
/* 1096 */       "VLRUNIT AS VLRUNIT_NF,\r\n" + 
/* 1097 */       "RECIPIENTE_GLP AS RECEPIENTE_GLP,\r\n" + 
/* 1098 */       "CHAVE_NFE_CTE AS CAHVE_NFE_CTE,\r\n" + 
/* 1099 */       "OPER_TOTALIZADOR AS OPERTOTALIZADOR\r\n" + 
/* 1100 */       "FROM(SELECT\r\n" + 
/* 1101 */       "LISTAGG(NUNOTA,',') AS NUNOTA,\r\n" + 
/* 1102 */       "IDIPROC AS IDIPROC,\r\n" + 
/* 1103 */       "CODPROD,\r\n" + 
/* 1104 */       "AD_CODEMP_ANP,\r\n" + 
/* 1105 */       "OPERACAO,\r\n" + 
/* 1106 */       "COD_INST_EMP,\r\n" + 
/* 1107 */       "COD_INST_PAR,\r\n" + 
/* 1108 */       "CODPROD_ANP,\r\n" + 
/* 1109 */       "ROUND(SUM(QTD_UN_PADRAO),0) AS QTD_UN_PADRAO,\r\n" + 
/* 1110 */       "ROUND(SUM(QTD_CONVERTIDO_KG),0) AS QTD_CONVERTIDO_KG,\r\n" + 
/* 1111 */       "CODMODAL_SIMP,\r\n" + 
/* 1112 */       "COD_OLEODUTO,\r\n" + 
/* 1113 */       "COALESCE(CNPJ_PARC,'0') AS CNPJ_PARC,\r\n" + 
/* 1114 */       "CODCID_ANP,\r\n" + 
/* 1115 */       "AD_CNAE_ANP,\r\n" + 
/* 1116 */       "COD_PAIS_ANP,\r\n" + 
/* 1117 */       "LICENCA_IMPORTACAO_LI,\r\n" + 
/* 1118 */       "NUM_DELCARACAO_IMPORTACAO_DI,\r\n" + 
/* 1119 */       "NRONF,\r\n" + 
/* 1120 */       "COD_SERIE_NF,\r\n" + 
/* 1121 */       "MAX(DTENTSAI) AS DTENTSAI,\r\n" + 
/* 1122 */       "COD_TIP_TAR_SERV,\r\n" + 
/* 1123 */       "CARACTERISTICA_INATIVO,\r\n" + 
/* 1124 */       "METODO_INATIVO,\r\n" + 
/* 1125 */       "MODALIDADE_FRETE,\r\n" + 
/* 1126 */       "VALOR_CARACTERISTICA,\r\n" + 
/* 1127 */       "CODPROD_RESULTANTE_ANP,\r\n" + 
/* 1128 */       "VLRUNIT,\r\n" + 
/* 1129 */       "RECIPIENTE_GLP,\r\n" + 
/* 1130 */       "CHAVE_NFE_CTE,\r\n" + 
/* 1131 */       "OPER_TOTALIZADOR\r\n" + 
/* 1132 */       "FROM (SELECT \r\n" + 
/* 1133 */       "A.NUNOTA,\r\n" + 
/* 1134 */       "A.IDIPROC,\r\n" + 
/* 1135 */       "G.CODPROD,\r\n" + 
/* 1136 */       "--2\tAgente Regulado Informante\r\n" + 
/* 1137 */       "COALESCE(F.AD_CODEMP_ANP,'0') AS AD_CODEMP_ANP,\r\n" + 
/* 1138 */       "\r\n" + 
/* 1139 */       "--3 Referencia - M�s de Refer�ncia (MMAAAA),\r\n" + 
/* 1140 */       "\r\n" + 
/* 1141 */       "--4\tC�digo da Opera��o\r\n" + 
/* 1142 */       "C.AD_CODOPERANP AS OPERACAO,\r\n" + 
/* 1143 */       "\r\n" + 
/* 1144 */       "--5\tC�digo da Instala��o 1\r\n" + 
/* 1145 */       "COALESCE(F.AD_CODINSTALACAO_ANP,'0') AS COD_INST_EMP,\r\n" + 
/* 1146 */       "\r\n" + 
/* 1147 */       "--6\tC�digo da Instala��o 2\r\n" + 
/* 1148 */       "COALESCE(D.AD_CODINSTALACAO_ANP,'0') AS COD_INST_PAR,\r\n" + 
/* 1149 */       "\r\n" + 
/* 1150 */       "--7\tC�digo do Produto Operado\r\n" + 
/* 1151 */       "TO_CHAR(COALESCE(G.CODANP,0)) AS CODPROD_ANP,\r\n" + 
/* 1152 */       "\r\n" + 
/* 1153 */       "--8\tQuantidade do Produto Operado na Unidade de Medida Oficial ANP\r\n" + 
/* 1154 */       "COALESCE((CASE WHEN E.CODVOL IN ('LT','KG') THEN A.QTDNEG\r\n" + 
/* 1155 */       "WHEN E.PESOLIQ != 0 THEN E.PESOLIQ * QTDNEG\r\n" + 
/* 1156 */       "WHEN  E.CODVOL != 'LT' AND E.PESOLIQ = 0 AND EXISTS (SELECT * FROM TGFVOA VOA WHERE VOA.CODVOL ='LT' AND A.CODPROD = VOA.CODPROD) THEN (SELECT (CASE WHEN DIVIDEMULTIPLICA = 'M' THEN ((A.QTDNEG*QUANTIDADE)*MULTIPVLR) ELSE TRUNC(((A.QTDNEG/QUANTIDADE)*MULTIPVLR),3) END)  FROM TGFVOA VOA WHERE VOA.CODVOL ='LT' AND A.CODPROD = VOA.CODPROD)\r\n" + 
/* 1157 */       "WHEN  E.CODVOL != 'KG' AND E.PESOLIQ = 0 AND EXISTS (SELECT * FROM TGFVOA VOA WHERE VOA.CODVOL ='KG' AND A.CODPROD = VOA.CODPROD) THEN (SELECT (CASE WHEN DIVIDEMULTIPLICA = 'M' THEN ((A.QTDNEG*QUANTIDADE)*MULTIPVLR) ELSE TRUNC(((A.QTDNEG/QUANTIDADE)*MULTIPVLR),3) END)  FROM TGFVOA VOA WHERE VOA.CODVOL ='KG' AND A.CODPROD = VOA.CODPROD)\r\n" + 
/* 1158 */       "ELSE A.QTDNEG END),0) AS QTD_UN_PADRAO,\r\n" + 
/* 1159 */       "\r\n" + 
/* 1160 */       "--9\tQuantidade do Produto Operado em quilogramas (kg)\r\n" + 
/* 1161 */       "COALESCE((CASE WHEN E.CODVOL IN ('LT','KG') THEN A.QTDNEG\r\n" + 
/* 1162 */       "WHEN E.PESOLIQ != 0 THEN E.PESOLIQ * QTDNEG\r\n" + 
/* 1163 */       "WHEN  E.CODVOL != 'LT' AND E.PESOLIQ = 0 AND EXISTS (SELECT * FROM TGFVOA VOA WHERE VOA.CODVOL ='LT' AND A.CODPROD = VOA.CODPROD) THEN (SELECT (CASE WHEN DIVIDEMULTIPLICA = 'M' THEN ((A.QTDNEG*QUANTIDADE)*MULTIPVLR) ELSE TRUNC(((A.QTDNEG/QUANTIDADE)*MULTIPVLR),3) END)  FROM TGFVOA VOA WHERE VOA.CODVOL ='LT' AND A.CODPROD = VOA.CODPROD)\r\n" + 
/* 1164 */       "WHEN  E.CODVOL != 'KG' AND E.PESOLIQ = 0 AND EXISTS (SELECT * FROM TGFVOA VOA WHERE VOA.CODVOL ='KG' AND A.CODPROD = VOA.CODPROD) THEN (SELECT (CASE WHEN DIVIDEMULTIPLICA = 'M' THEN ((A.QTDNEG*QUANTIDADE)*MULTIPVLR) ELSE TRUNC(((A.QTDNEG/QUANTIDADE)*MULTIPVLR),3) END)  FROM TGFVOA VOA WHERE VOA.CODVOL ='KG' AND A.CODPROD = VOA.CODPROD)\r\n" + 
/* 1165 */       "ELSE A.QTDNEG END),0) AS QTD_CONVERTIDO_KG,\r\n" + 
/* 1166 */       "\r\n" + 
/* 1167 */       "--10\tC�digo do Modal Utilizado na Movimenta��o\r\n" + 
/* 1168 */       "'0' AS CODMODAL_SIMP,--C�digo do Modal Utilizado na Movimenta��o\r\n" + 
/* 1169 */       "\r\n" + 
/* 1170 */       "--11 C�digo do oleoduto de transporte\r\n" + 
/* 1171 */       "'0' AS COD_OLEODUTO,\r\n" + 
/* 1172 */       "\r\n" + 
/* 1173 */       "--12 Identifica��o do Terceiro Envolvido na Opera��o\r\n" + 
/* 1174 */       "(CASE WHEN D.AD_CODINSTALACAO_ANP IS NULL THEN D.CGC_CPF ELSE '0' END) AS CNPJ_PARC,\r\n" + 
/* 1175 */       "\r\n" + 
/* 1176 */       "--13 C�digo do Munic�pio (Origem/Destino)\r\n" + 
/* 1177 */       "(CASE WHEN  (D.AD_CODINSTALACAO_ANP IS NULL AND D.CGC_CPF IS NOT NULL) THEN COALESCE((SELECT AD_CODCID_ANP FROM TSICID WHERE CODCID = D.CODCID),'0') ELSE '0' END) AS CODCID_ANP,\r\n" + 
/* 1178 */       "\r\n" + 
/* 1179 */       "--14 C�digo de Atividade Econ�mica do Terceiro\r\n" + 
/* 1180 */       "(CASE WHEN  (D.AD_CODINSTALACAO_ANP IS NULL AND D.CGC_CPF IS NOT NULL) THEN COALESCE(D.AD_CNAE_ANP,'0') ELSE '0' END) AS AD_CNAE_ANP,\r\n" + 
/* 1181 */       "\r\n" + 
/* 1182 */       "--15 C�digo do Pa�s (Origem/Destino)\r\n" + 
/* 1183 */       "(CASE WHEN (SELECT CODOPER_ANP FROM AD_OPERACAOANP ANP WHERE ANP.CODOPER = C.AD_CODOPERANP) IN ('2011001', '2012001') THEN (SELECT AD_CODPAI_ANP FROM TSICID CID\r\n" + 
/* 1184 */       "INNER JOIN TSIUFS UF ON CID.UF = UF.CODUF\r\n" + 
/* 1185 */       "INNER JOIN TSIPAI PAI ON UF.CODPAIS = PAI.CODPAIS\r\n" + 
/* 1186 */       "WHERE D.CODCID = CID.CODCID) ELSE '0' END) AS COD_PAIS_ANP,--C�digo ANP Pais,\r\n" + 
/* 1187 */       "\r\n" + 
/* 1188 */       "--16 N�mero da Licen�a de Importa��o (LI)\r\n" + 
/* 1189 */       "COALESCE(B.AD_LICENCA_IMPORTACAO_LI,'0') AS LICENCA_IMPORTACAO_LI,\r\n" + 
/* 1190 */       "\r\n" + 
/* 1191 */       "--17 N�mero da Declara��o de Importa��o (DI)\r\n" + 
/* 1192 */       "COALESCE(AD_NUM_DELCARACAO_IMPORT_DI,'0') AS NUM_DELCARACAO_IMPORTACAO_DI,\r\n" + 
/* 1193 */       "\r\n" + 
/* 1194 */       "--18 N�mero da Nota Fiscal da Opera��o Comercial -\r\n" + 
/* 1195 */       "'0' AS NRONF,\r\n" + 
/* 1196 */       "\r\n" + 
/* 1197 */       "--19 C�digo da S�rie da Nota Fiscal da Opera��o Comercial\r\n" + 
/* 1198 */       "(CASE WHEN (CASE WHEN B.CHAVENFE IS NOT NULL THEN B.CHAVENFE ELSE B.CHAVECTE END) IS NULL THEN COALESCE((SELECT SER.COD_SIMP FROM AD_SERIENFOPERSIMP SER WHERE SER.CODSEROPER = B.AD_CODSEROPER),'0') ELSE '0' END) AS COD_SERIE_NF,\r\n" + 
/* 1199 */       "\r\n" + 
/* 1200 */       "--20 Data da Opera��o Comercial/NF-E (DDMMAAAA)\r\n" + 
/* 1201 */       "(CASE WHEN B.DTENTSAI IS NOT NULL THEN TO_CHAR(B.DTENTSAI, 'DDMMYYYY') ELSE '0' END) AS DTENTSAI,\r\n" + 
/* 1202 */       "\r\n" + 
/* 1203 */       "--21 C�digo do Tipo de Tarifa de Servi�o (Oleodutos e Terminais de combust�veis l�quidos)\r\n" + 
/* 1204 */       "'0' AS COD_TIP_TAR_SERV,\r\n" + 
/* 1205 */       "\r\n" + 
/* 1206 */       "--22 CARACTERISTICA Inativo - preencher com zeros\r\n" + 
/* 1207 */       "'0' AS CARACTERISTICA_INATIVO,\r\n" + 
/* 1208 */       "\r\n" + 
/* 1209 */       "--23 METODO Inativo - preencher com zeros\r\n" + 
/* 1210 */       "'0' AS METODO_INATIVO,\r\n" + 
/* 1211 */       "\r\n" + 
/* 1212 */       "--24 Modalidade do frete\r\n" + 
/* 1213 */       "0 AS MODALIDADE_FRETE,\r\n" + 
/* 1214 */       "\r\n" + 
/* 1215 */       "--25 N�mero do Documento da Qualidade / Quando o ARI for Distribuidor de Produtos Asf�lticos considerar-se-� o campo como Pre�o (R$/kg)\r\n" + 
/* 1216 */       "'0' AS VALOR_CARACTERISTICA,\r\n" + 
/* 1217 */       "\r\n" + 
/* 1218 */       "--26 C�digo do Produto/Opera��o Resultante\r\n" + 
/* 1219 */       "'0' AS CODPROD_RESULTANTE_ANP,\r\n" + 
/* 1220 */       "\r\n" + 
/* 1221 */       "--27\tValor unit�rio (nota fiscal)\r\n" + 
/* 1222 */       "VLRUNIT AS VLRUNIT, --COALESCE(A.VLRUNIT,0)\r\n" + 
/* 1223 */       "\r\n" + 
/* 1224 */       "--28 Recipiente de GLP\r\n" + 
/* 1225 */       "'0' AS RECIPIENTE_GLP,\r\n" + 
/* 1226 */       "\r\n" + 
/* 1227 */       "--29 Chave de acesso da NF-e ou do CT-e\r\n" + 
/* 1228 */       "(CASE WHEN B.CHAVENFE IS NOT NULL THEN B.CHAVENFE ELSE B.CHAVECTE END) AS CHAVE_NFE_CTE,\r\n" + 
/* 1229 */       "\r\n" + 
/* 1230 */       "\r\n" + 
/* 1231 */       "C.AD_CODOPERTOTALIZADORANP AS OPER_TOTALIZADOR\r\n" + 
/* 1232 */       "\r\n" + 
/* 1233 */       "\r\n" + 
/* 1234 */       "FROM (SELECT \r\n" + 
/* 1235 */       "IDIPROC,\r\n" + 
/* 1236 */       "NUNOTA,\r\n" + 
/* 1237 */       "CODPROD AS PA,\r\n" + 
/* 1238 */       "CODVOL AS CODVOL_PA,\r\n" + 
/* 1239 */       "QTDNEG AS QTD_PA,\r\n" + 
/* 1240 */       "MP_FINAL AS CODPROD,\r\n" + 
/* 1241 */       "CODVOL_FINAL AS CODVOL,\r\n" + 
/* 1242 */       "SUM(QTDMP_FINAL) AS QTDNEG,VLRUNIT\r\n" + 
/* 1243 */       "FROM (\r\n" + 
/* 1244 */       "SELECT \r\n" + 
/* 1245 */       "MP.IDIPROC, \r\n" + 
/* 1246 */       "OPE.IDIATV, \r\n" + 
/* 1247 */       "OPE.SEQROPE,\r\n" + 
/* 1248 */       "OPE.NUAPO,\r\n" + 
/* 1249 */       "OPE.NUNOTA,\r\n" + 
/* 1250 */       "ITE.CODPROD,ite.vlrunit,\r\n" + 
/* 1251 */       "ITE.CODVOL,\r\n" + 
/* 1252 */       "ITE.QTDNEG,\r\n" + 
/* 1253 */       "MP.CODPROD AS CODPROD_MP1,\r\n" + 
/* 1254 */       "MP.CODVOL AS CODVOL_MP1,\r\n" + 
/* 1255 */       "MP.QTDNEG AS QTDNEG_MP1, \r\n" + 
/* 1256 */       "--PI.CODPRODPI AS PI1,\r\n" + 
/* 1257 */       "MP.CONTROLE AS CONTROLE_MP1,\r\n" + 
/* 1258 */       "MPPI.CODPRODMP AS CODPROD_MP2,\r\n" + 
/* 1259 */       "MPPI.CONTROLEMP AS CONTROLE_MP2,\r\n" + 
/* 1260 */       "MPPI.CODVOL AS CODVOL_MP2,\r\n" + 
/* 1261 */       "--MPPARAUM,\r\n" + 
/* 1262 */       "MPPI.MPPARAUM*MP.QTDNEG AS QTD_MP2,\r\n" + 
/* 1263 */       "\r\n" + 
/* 1264 */       "MPPI2.CODPRODMP AS CODPROD_MP3,\r\n" + 
/* 1265 */       "MPPI2.CONTROLEMP AS CONTROLE_MP3,\r\n" + 
/* 1266 */       "MPPI2.CODVOL AS CODVOL_MP3,\r\n" + 
/* 1267 */       "--MPPARAUM,\r\n" + 
/* 1268 */       "MPPI2.MPPARAUM*(MPPI.MPPARAUM*MP.QTDNEG) AS QTD_MP3,\r\n" + 
/* 1269 */       "\r\n" + 
/* 1270 */       "COALESCE(MPPI2.CODPRODMP, MPPI.CODPRODMP, MP.CODPROD) AS MP_FINAL,\r\n" + 
/* 1271 */       "COALESCE(MPPI2.CODVOL, MPPI.CODVOL, MP.CODVOL) AS CODVOL_FINAL,\r\n" + 
/* 1272 */       "COALESCE((MPPI2.MPPARAUM*(MPPI.MPPARAUM*MP.QTDNEG)), (MPPI.MPPARAUM*MP.QTDNEG), MP.QTDNEG) AS QTDMP_FINAL\r\n" + 
/* 1273 */       "FROM TPRROPE OPE \r\n" + 
/* 1274 */       "INNER JOIN TPRIATV IATV ON OPE.IDIATV = IATV.IDIATV \r\n" + 
/* 1275 */       "INNER JOIN TGFITE ITE ON ITE.NUNOTA = OPE.NUNOTA \r\n" + 
/* 1276 */       "LEFT JOIN TPRIPROC IPROC ON IPROC.IDIPROC =  IATV.IDIPROC \r\n" + 
/* 1277 */       "INNER JOIN (SELECT \r\n" + 
/* 1278 */       "    OPE.IDIATV, SEQROPE,OPE.NUAPO,OPE.NUNOTA,QTDNEG,IATV.IDIPROC,ITE.CODPROD,ITE.CONTROLE, ITE.CODVOL\r\n" + 
/* 1279 */       "    FROM \r\n" + 
/* 1280 */       "    TPRROPE OPE \r\n" + 
/* 1281 */       "    INNER JOIN TPRIATV IATV ON OPE.IDIATV = IATV.IDIATV \r\n" + 
/* 1282 */       "    INNER JOIN TGFITE ITE ON ITE.NUNOTA = OPE.NUNOTA\r\n" + 
/* 1283 */       "    WHERE ITE.USOPROD = 'M' /*AND IATV.IDIPROC = 343*/)MP ON MP.IDIPROC = IATV.IDIPROC AND MP.NUNOTA = OPE.NUNOTA \r\n" + 
/* 1284 */       "    \r\n" + 
/* 1285 */       "LEFT JOIN TPRLPI PI ON PI.CODPRODPI = MP.CODPROD AND  PI.CODPRODPA = ITE.CODPROD AND PI.IDPROC = IPROC.IDPROC \r\n" + 
/* 1286 */       "\r\n" + 
/* 1287 */       "LEFT JOIN (SELECT\r\n" + 
/* 1288 */       "    ITE.CODPROD,\r\n" + 
/* 1289 */       "    MP.CODPROD AS CODPRODMP, \r\n" + 
/* 1290 */       "    ITE.CONTROLE, \r\n" + 
/* 1291 */       "    MP.CONTROLE CONTROLEMP, \r\n" + 
/* 1292 */       "    ITE.QTDNEG, \r\n" + 
/* 1293 */       "    MP.QTDNEG QTDNEGMP,\r\n" + 
/* 1294 */       "    ITE.NUNOTA,\r\n" + 
/* 1295 */       "    (MP.QTDNEG/ITE.QTDNEG) AS MPPARAUM,\r\n" + 
/* 1296 */       "    ITE.CODVOL\r\n" + 
/* 1297 */       "    FROM TGFITE ITE \r\n" + 
/* 1298 */       "    INNER JOIN TGFCAB CAB ON CAB.NUNOTA = ITE.NUNOTA \r\n" + 
/* 1299 */       "    LEFT JOIN (SELECT \r\n" + 
/* 1300 */       "        ITE.CODPROD,\r\n" + 
/* 1301 */       "        ITE.CONTROLE,\r\n" + 
/* 1302 */       "        ITE.QTDNEG,\r\n" + 
/* 1303 */       "        ITE.NUNOTA\r\n" + 
/* 1304 */       "        FROM TGFITE ITE \r\n" + 
/* 1305 */       "        INNER JOIN TGFCAB CAB ON CAB.NUNOTA = ITE.NUNOTA\r\n" + 
/* 1306 */       "        WHERE  TIPMOV = 'F'  AND USOPROD = 'M')MP ON MP.NUNOTA = ITE.NUNOTA\r\n" + 
/* 1307 */       "    WHERE CAB.TIPMOV = 'F'  AND ITE.USOPROD = 'V')MPPI ON MPPI.CODPROD = PI.CODPRODPI AND MPPI.CONTROLE = MP.CONTROLE \r\n" + 
/* 1308 */       "\r\n" + 
/* 1309 */       "LEFT JOIN (SELECT\r\n" + 
/* 1310 */       "    ITE.CODPROD,\r\n" + 
/* 1311 */       "    MP.CODPROD AS CODPRODMP, \r\n" + 
/* 1312 */       "    ITE.CONTROLE, \r\n" + 
/* 1313 */       "    MP.CONTROLE CONTROLEMP, \r\n" + 
/* 1314 */       "    ITE.QTDNEG, \r\n" + 
/* 1315 */       "    MP.QTDNEG QTDNEGMP,\r\n" + 
/* 1316 */       "    ITE.NUNOTA,\r\n" + 
/* 1317 */       "    (MP.QTDNEG/ITE.QTDNEG) AS MPPARAUM,\r\n" + 
/* 1318 */       "    ITE.CODVOL\r\n" + 
/* 1319 */       "    FROM TGFITE ITE \r\n" + 
/* 1320 */       "    INNER JOIN TGFCAB CAB ON CAB.NUNOTA = ITE.NUNOTA \r\n" + 
/* 1321 */       "    LEFT JOIN (SELECT \r\n" + 
/* 1322 */       "        ITE.CODPROD,\r\n" + 
/* 1323 */       "        ITE.CONTROLE,\r\n" + 
/* 1324 */       "        ITE.QTDNEG,\r\n" + 
/* 1325 */       "        ITE.NUNOTA\r\n" + 
/* 1326 */       "        FROM TGFITE ITE \r\n" + 
/* 1327 */       "        INNER JOIN TGFCAB CAB ON CAB.NUNOTA = ITE.NUNOTA\r\n" + 
/* 1328 */       "        WHERE  TIPMOV = 'F'  AND USOPROD = 'M')MP ON MP.NUNOTA = ITE.NUNOTA\r\n" + 
/* 1329 */       "    WHERE CAB.TIPMOV = 'F'  AND ITE.USOPROD = 'V')MPPI2 ON MPPI2.CODPROD = MPPI.CODPRODMP AND MPPI2.CONTROLE = MPPI.CONTROLEMP\r\n" + 
/* 1330 */       "\r\n" + 
/* 1331 */       "WHERE IATV.IDIPROC = MP.IDIPROC\r\n" + 
/* 1332 */       "AND ITE.USOPROD = 'V' )\r\n" + 
/* 1333 */       "GROUP BY \r\n" + 
/* 1334 */       "IDIPROC,\r\n" + 
/* 1335 */       "NUNOTA,\r\n" + 
/* 1336 */       "CODPROD,\r\n" + 
/* 1337 */       "CODVOL,\r\n" + 
/* 1338 */       "QTDNEG,\r\n" + 
/* 1339 */       "CODVOL_FINAL,\r\n" + 
/* 1340 */       "MP_FINAL,vlrunit) A \r\n" + 
/* 1341 */       "INNER JOIN TGFCAB B ON A.NUNOTA = B.NUNOTA\r\n" + 
/* 1342 */       "INNER JOIN TGFTOP C ON B.CODTIPOPER = C.CODTIPOPER AND DHALTER = (SELECT MAX(DHALTER) FROM TGFTOP TOP WHERE C.CODTIPOPER = TOP.CODTIPOPER)\r\n" + 
/* 1343 */       "INNER JOIN TGFPAR D ON B.CODPARC = D.CODPARC\r\n" + 
/* 1344 */       "INNER JOIN TGFPRO E ON A.CODPROD = E.CODPROD\r\n" + 
/* 1345 */       "INNER JOIN TSIEMP F ON B.CODEMP = F.CODEMP\r\n" + 
/* 1346 */       "INNER JOIN TGFPRO G ON A.PA = G.CODPROD\r\n" + 
/* 1347 */       "WHERE COALESCE(C.AD_ATUALIZA_EST_SIMP,'N') = 'S'\r\n" + 
/* 1348 */       "AND COALESCE(E.AD_GERA_SIMP,'N') = 'S'\r\n" + 
/* 1349 */       "AND COALESCE(G.AD_GERA_SIMP,'N') = 'S'\r\n" + 
/* 1350 */       "AND TO_CHAR(B.DTNEG,'mm-yyyy') = '" + referencia + "'\r\n" + 
/* 1351 */       "AND B.CODEMP = " + codEmp + " \r\n" + 
/* 1352 */       "ORDER BY A.IDIPROC)\r\n" + 
/* 1353 */       "GROUP BY \r\n" + 
/* 1354 */       "IDIPROC,\r\n" + 
/* 1355 */       "CODPROD,\r\n" + 
/* 1356 */       "AD_CODEMP_ANP,\r\n" + 
/* 1357 */       "OPERACAO,\r\n" + 
/* 1358 */       "COD_INST_EMP,\r\n" + 
/* 1359 */       "COD_INST_PAR,\r\n" + 
/* 1360 */       "CODPROD_ANP,\r\n" + 
/* 1361 */       "CODMODAL_SIMP,\r\n" + 
/* 1362 */       "COD_OLEODUTO,\r\n" + 
/* 1363 */       "CNPJ_PARC,\r\n" + 
/* 1364 */       "CODCID_ANP,\r\n" + 
/* 1365 */       "AD_CNAE_ANP,\r\n" + 
/* 1366 */       "COD_PAIS_ANP,\r\n" + 
/* 1367 */       "LICENCA_IMPORTACAO_LI,\r\n" + 
/* 1368 */       "NUM_DELCARACAO_IMPORTACAO_DI,\r\n" + 
/* 1369 */       "NRONF,\r\n" + 
/* 1370 */       "COD_SERIE_NF,\r\n" + 
/* 1371 */       "--DTENTSAI,\r\n" + 
/* 1372 */       "COD_TIP_TAR_SERV,\r\n" + 
/* 1373 */       "CARACTERISTICA_INATIVO,\r\n" + 
/* 1374 */       "METODO_INATIVO,\r\n" + 
/* 1375 */       "MODALIDADE_FRETE,\r\n" + 
/* 1376 */       "VALOR_CARACTERISTICA,\r\n" + 
/* 1377 */       "CODPROD_RESULTANTE_ANP,\r\n" + 
/* 1378 */       "VLRUNIT,\r\n" + 
/* 1379 */       "RECIPIENTE_GLP,\r\n" + 
/* 1380 */       "CHAVE_NFE_CTE,\r\n" + 
/* 1381 */       "OPER_TOTALIZADOR))";
/* 1382 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/* 1383 */     pstm.execute();
/*      */   }
/*      */   
/*      */   public static void update(JdbcWrapper jdbcWrapper, String update) throws Exception {
/* 1387 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(update);
/* 1388 */     pstm.executeUpdate();
/* 1389 */     pstm.close();
/*      */   }
/*      */   
/*      */   public static ResultSet gerarArquivo(JdbcWrapper jdbcWrapper, BigDecimal codSimulaSimp) throws Exception {
/* 1393 */     String sql = "SELECT \r\nROWNUM,\r\nCOALESCE(CODEMP_ANP,'0') AS CODEMP_ANP,\r\nCOALESCE(MESREFERENCIA,'0') AS MESREFERENCIA,\r\nCOALESCE((SELECT OPER.CODOPER_ANP FROM AD_OPERACAOANP OPER WHERE OPER.CODOPER = A.CODOPER),'0') AS CODOPERACAO,\r\nCOALESCE(CODINSTALACAO1,'0') AS CODINSTALACAO1,\r\nCOALESCE(CODINSTALACAO2,'0') AS CODINSTALACAO2,\r\nCOALESCE(CODPROD_ANP,'0') AS CODPROD_ANP,\r\nCOALESCE(QTD_PROD_UN_ANP,'0') AS QTD_PROD_UN_ANP,\r\nCOALESCE(QTD_PROD_OPERADO_KG,'0') AS QTD_PROD_OPERADO_KG,\r\nCOALESCE(COD_MODAL_MOVIMENTACAO,'0') AS COD_MODAL_MOVIMENTACAO,\r\nCOALESCE(COD_OLEODUTO_TRANSPORTE,'0') AS COD_OLEODUTO_TRANSPORTE,\r\nCOALESCE(CNPJ_PARC,'0') AS CNPJ_PARC,\r\nCOALESCE(CODCID_ANP,'0') AS CODCID_ANP,\r\nCOALESCE(COD_ATIV_PARC_ANP,'0') AS COD_ATIV_PARC_ANP,\r\nCOALESCE(CODPAIS_ANP,'0') AS CODPAIS_ANP,\r\nCOALESCE(NUM_LICEN_IMPORT,'0') AS NUM_LICEN_IMPORT,\r\nCOALESCE(NUM_DECLARACAO_IMPORT,'0') AS NUM_DECLARACAO_IMPORT,\r\nCOALESCE(NUMNF,'0') AS NUMNF,\r\nCOALESCE(SERIE_NF,'0') AS SERIE_NF,\r\nCOALESCE(DAT_OPER_COMERCIAL,'0') AS DAT_OPER_COMERCIAL,\r\nCOALESCE(COD_TIP_TARIFA_SERVICO,'0') AS COD_TIP_TARIFA_SERVICO,\r\nCOALESCE(CARACTERISTICA,'0') AS CARACTERISTICA,\r\nCOALESCE(METODO,'0') AS METODO,\r\nCOALESCE(MODALIDADE_FRETE,'0') AS MODALIDADE_FRETE,\r\nCOALESCE(NUM_DOC_QUALIDADE,'0') AS NUM_DOC_QUALIDADE,\r\nCOALESCE(COD_OPER_RESULTANTE,'0') AS COD_OPER_RESULTANTE,\r\nCOALESCE(VLRUNIT_NF,'0') AS VLRUNIT_NF,\r\nCOALESCE(RECEPIENTE_GLP,'0') AS RECEPIENTE_GLP,\r\nCOALESCE(CAHVE_NFE_CTE,'0') AS CAHVE_NFE_CTE,\r\n(SELECT COUNT(*) FROM AD_DETSIMULARSIMP DET WHERE DET.CODSIMSIM = A.CODSIMSIM) AS CONTADOR\r\nFROM AD_DETSIMULARSIMP A\r\nWHERE CODSIMSIM = " + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1425 */       codSimulaSimp + "\r\n" + 
/* 1426 */       "ORDER BY CODDETSIMSIMP";
/* 1427 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/* 1428 */     ResultSet query = pstm.executeQuery();
/* 1429 */     return query;
/*      */   }
/*      */ 
/*      */   
/*      */   public static ResultSet buscarEstoqueExistente(JdbcWrapper jdbcWrapper, BigDecimal codSimulaSimp) throws Exception {
/* 1434 */     String sql = "SELECT CODESTSIMP FROM AD_ESTOQUESIMP WHERE CODSIMSIM = " + codSimulaSimp;
/* 1435 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/* 1436 */     ResultSet query = pstm.executeQuery();
/* 1437 */     return query;
/*      */   }
/*      */ 
/*      */   
/*      */   public static ResultSet buscarEstoqueFinal(JdbcWrapper jdbcWrapper, BigDecimal codSimulaSimp) throws Exception {
/* 1442 */     String sql = "SELECT\r\n(SELECT CODEMP FROM TSIEMP EMP WHERE A.CODEMP_ANP = EMP.AD_CODEMP_ANP) AS CODEMP,\r\nCOALESCE(A.CODPROD_ANP,'0') AS CODPROD_ANP,\r\n(SELECT DISTINCT DESCRANP FROM TGFPRO PRO WHERE A.CODPROD_ANP = PRO.CODANP AND ROWNUM <=1 ) AS DESCR_PROD,\r\nCOALESCE(A.QTD_PROD_UN_ANP,'0') AS QTD_PROD_UN_ANP,\r\nB.MES_REFERENCIA,\r\nB.ANO_REFRENCIA,\r\n(SELECT INTEIRO FROM TSIPAR WHERE CHAVE = 'OPERPADESTINICI') AS OPER_INI\r\nFROM AD_DETSIMULARSIMP A\r\nINNER JOIN AD_SIMULARSIMP B ON A.CODSIMSIM = B.CODSIMSIM\r\nWHERE A.CODSIMSIM = " + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1452 */       codSimulaSimp + "\r\n" + 
/* 1453 */       "AND A.CODOPER = (SELECT INTEIRO FROM TSIPAR WHERE CHAVE = 'OPERPADESTFINAL')\r\n" + 
/* 1454 */       "ORDER BY A.CODDETSIMSIMP";
/* 1455 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/* 1456 */     ResultSet query = pstm.executeQuery();
/* 1457 */     return query;
/*      */   }
/*      */   
/*      */   public static void producaoMp(JdbcWrapper jdbcWrapper, String referencia, String codEmp, BigDecimal codSimSim) throws Exception {
/* 1461 */     String sql = "INSERT INTO AD_DETSIMULARSIMP (CODSIMSIM, CODDETSIMSIMP, NUNOTA, IDIPROC, CODEMP_ANP, MESREFERENCIA, CODOPER, CODINSTALACAO1, CODINSTALACAO2, CODPROD_ANP, QTD_PROD_UN_ANP, QTD_PROD_OPERADO_KG, COD_MODAL_MOVIMENTACAO, \r\nCOD_OLEODUTO_TRANSPORTE, CNPJ_PARC, CODCID_ANP, COD_ATIV_PARC_ANP, CODPAIS_ANP, NUM_LICEN_IMPORT, NUM_DECLARACAO_IMPORT, NUMNF, SERIE_NF, DAT_OPER_COMERCIAL, COD_TIP_TARIFA_SERVICO, CARACTERISTICA, \r\nMETODO, MODALIDADE_FRETE, NUM_DOC_QUALIDADE, COD_OPER_RESULTANTE, VLRUNIT_NF, RECEPIENTE_GLP, CAHVE_NFE_CTE, OPERTOTALIZADOR) \r\n(SELECT\r\n" + 
/*      */ 
/*      */ 
/*      */       
/* 1465 */       codSimSim + " AS CODSIMSIM,\r\n" + 
/* 1466 */       "(SELECT COALESCE(MAX(CODDETSIMSIMP),0) FROM AD_DETSIMULARSIMP WHERE CODSIMSIM = " + codSimSim + ")+ROWNUM AS CODDETSIMSIMP,\r\n" + 
/* 1467 */       "NUNOTA,\r\n" + 
/* 1468 */       "IDIPROC,\r\n" + 
/* 1469 */       "AD_CODEMP_ANP AS CODEMP_ANP,\r\n" + 
/* 1470 */       "'" + referencia.replace("-", "") + "' AS MESREFERENCIA,\r\n" + 
/* 1471 */       "OPERACAO AS CODOPER,\r\n" + 
/* 1472 */       "COD_INST_EMP AS CODINSTALACAO1,\r\n" + 
/* 1473 */       "COD_INST_PAR AS CODINSTALACAO2,\r\n" + 
/* 1474 */       "CODPROD_ANP AS CODPROD_ANP,\r\n" + 
/* 1475 */       "QTD_UN_PADRAO AS QTD_PROD_UN_ANP,\r\n" + 
/* 1476 */       "QTD_CONVERTIDO_KG AS QTD_PROD_OPERADO_KG,\r\n" + 
/* 1477 */       "CODMODAL_SIMP AS COD_MODAL_MOVIMENTACAO,\r\n" + 
/* 1478 */       "COD_OLEODUTO AS COD_OLEODUTO_TRANSPORTE,\r\n" + 
/* 1479 */       "CNPJ_PARC AS CNPJ_PARC,\r\n" + 
/* 1480 */       "CODCID_ANP AS CODCID_ANP,\r\n" + 
/* 1481 */       "AD_CNAE_ANP AS COD_ATIV_PARC_ANP,\r\n" + 
/* 1482 */       "COD_PAIS_ANP AS CODPAIS_ANP,\r\n" + 
/* 1483 */       "REPLACE(REPLACE(LICENCA_IMPORTACAO_LI,'-',''),'/','') AS NUM_LICEN_IMPORT,\r\n" + 
/* 1484 */       "REPLACE(REPLACE(NUM_DELCARACAO_IMPORTACAO_DI,'-',''),'/','')  AS NUM_DECLARACAO_IMPORT,\r\n" + 
/* 1485 */       "NRONF AS NUMNF,\r\n" + 
/* 1486 */       "COD_SERIE_NF AS SERIE_NF,\r\n" + 
/* 1487 */       "DTENTSAI AS DAT_OPER_COMERCIAL,\r\n" + 
/* 1488 */       "COD_TIP_TAR_SERV AS COD_TIP_TARIFA_SERVICO,\r\n" + 
/* 1489 */       "CARACTERISTICA_INATIVO AS CARACTERISTICA,\r\n" + 
/* 1490 */       "METODO_INATIVO AS METODO,\r\n" + 
/* 1491 */       "MODALIDADE_FRETE AS MODALIDADE_FRETE,\r\n" + 
/* 1492 */       "VALOR_CARACTERISTICA AS NUM_DOC_QUALIDADE,\r\n" + 
/* 1493 */       "CODPROD_RESULTANTE_ANP AS COD_OPER_RESULTANTE,\r\n" + 
/* 1494 */       "VLRUNIT AS VLRUNIT_NF,\r\n" + 
/* 1495 */       "RECIPIENTE_GLP AS RECEPIENTE_GLP,\r\n" + 
/* 1496 */       "CHAVE_NFE_CTE AS CAHVE_NFE_CTE,\r\n" + 
/* 1497 */       "OPER_TOTALIZADOR AS OPERTOTALIZADOR\r\n" + 
/* 1498 */       "FROM(\r\n" + 
/* 1499 */       "SELECT\r\n" + 
/* 1500 */       "NUNOTA,\r\n" + 
/* 1501 */       "IDIPROC AS IDIPROC,\r\n" + 
/* 1502 */       "AD_CODEMP_ANP,\r\n" + 
/* 1503 */       "OPERACAO,\r\n" + 
/* 1504 */       "COD_INST_EMP,\r\n" + 
/* 1505 */       "COD_INST_PAR,\r\n" + 
/* 1506 */       "CODPROD_ANP,\r\n" + 
/* 1507 */       "case when(ROUND(SUM(QTD_UN_PADRAO),0) = 0) then 1 else  ROUND(SUM(QTD_UN_PADRAO),0) end AS QTD_UN_PADRAO,\r\n" + 
/* 1508 */       "case when(ROUND(SUM(QTD_CONVERTIDO_KG),0) = 0) then 1 else  ROUND(SUM(QTD_CONVERTIDO_KG),0) end as  QTD_CONVERTIDO_KG,\r\n" + 
/* 1509 */       "CODMODAL_SIMP,\r\n" + 
/* 1510 */       "COD_OLEODUTO,\r\n" + 
/* 1511 */       "CNPJ_PARC,\r\n" + 
/* 1512 */       "CODCID_ANP,\r\n" + 
/* 1513 */       "AD_CNAE_ANP,\r\n" + 
/* 1514 */       "COD_PAIS_ANP,\r\n" + 
/* 1515 */       "LICENCA_IMPORTACAO_LI,\r\n" + 
/* 1516 */       "NUM_DELCARACAO_IMPORTACAO_DI,\r\n" + 
/* 1517 */       "NRONF,\r\n" + 
/* 1518 */       "COD_SERIE_NF,\r\n" + 
/* 1519 */       "DTENTSAI,\r\n" + 
/* 1520 */       "COD_TIP_TAR_SERV,\r\n" + 
/* 1521 */       "CARACTERISTICA_INATIVO,\r\n" + 
/* 1522 */       "METODO_INATIVO,\r\n" + 
/* 1523 */       "MODALIDADE_FRETE,\r\n" + 
/* 1524 */       "VALOR_CARACTERISTICA,\r\n" + 
/* 1525 */       "CODPROD_RESULTANTE_ANP,\r\n" + 
/* 1526 */       "VLRUNIT,\r\n" + 
/* 1527 */       "RECIPIENTE_GLP,\r\n" + 
/* 1528 */       "CHAVE_NFE_CTE,\r\n" + 
/* 1529 */       "OPER_TOTALIZADOR\r\n" + 
/* 1530 */       "FROM (\r\n" + 
/* 1531 */       "SELECT \r\n" + 
/* 1532 */       "A.NUNOTA,\r\n" + 
/* 1533 */       "A.IDIPROC,\r\n" + 
/* 1534 */       "--2\tAgente Regulado Informante\r\n" + 
/* 1535 */       "COALESCE(F.AD_CODEMP_ANP,'0') AS AD_CODEMP_ANP,\r\n" + 
/* 1536 */       "\r\n" + 
/* 1537 */       "--3 Referencia - M�s de Refer�ncia (MMAAAA),\r\n" + 
/* 1538 */       "\r\n" + 
/* 1539 */       "--4\tC�digo da Opera��o\r\n" + 
/* 1540 */       "C.AD_CODOPERANPMP AS OPERACAO,\r\n" + 
/* 1541 */       "\r\n" + 
/* 1542 */       "--5\tC�digo da Instala��o 1\r\n" + 
/* 1543 */       "COALESCE(F.AD_CODINSTALACAO_ANP,'0') AS COD_INST_EMP,\r\n" + 
/* 1544 */       "\r\n" + 
/* 1545 */       "--6\tC�digo da Instala��o 2\r\n" + 
/* 1546 */       "COALESCE(D.AD_CODINSTALACAO_ANP,'0') AS COD_INST_PAR,\r\n" + 
/* 1547 */       "\r\n" + 
/* 1548 */       "--7\tC�digo do Produto Operado\r\n" + 
/* 1549 */       "TO_CHAR(COALESCE(E.CODANP,0)) AS CODPROD_ANP,\r\n" + 
/* 1550 */       "\r\n" + 
/* 1551 */       "--8\tQuantidade do Produto Operado na Unidade de Medida Oficial ANP\r\n" + 
/* 1552 */       "COALESCE((CASE WHEN E.CODVOL IN ('LT','KG') THEN A.QTDNEG\r\n" + 
/* 1553 */       "WHEN E.PESOLIQ != 0 THEN E.PESOLIQ * QTDNEG\r\n" + 
/* 1554 */       "WHEN  E.CODVOL != 'LT' AND E.PESOLIQ = 0 AND EXISTS (SELECT * FROM TGFVOA VOA WHERE VOA.CODVOL ='LT' AND A.CODPROD = VOA.CODPROD) THEN (SELECT (CASE WHEN DIVIDEMULTIPLICA = 'M' THEN ((A.QTDNEG*QUANTIDADE)*MULTIPVLR) ELSE TRUNC(((A.QTDNEG/QUANTIDADE)*MULTIPVLR),3) END)  FROM TGFVOA VOA WHERE VOA.CODVOL ='LT' AND A.CODPROD = VOA.CODPROD)\r\n" + 
/* 1555 */       "WHEN  E.CODVOL != 'KG' AND E.PESOLIQ = 0 AND EXISTS (SELECT * FROM TGFVOA VOA WHERE VOA.CODVOL ='KG' AND A.CODPROD = VOA.CODPROD) THEN (SELECT (CASE WHEN DIVIDEMULTIPLICA = 'M' THEN ((A.QTDNEG*QUANTIDADE)*MULTIPVLR) ELSE TRUNC(((A.QTDNEG/QUANTIDADE)*MULTIPVLR),3) END)  FROM TGFVOA VOA WHERE VOA.CODVOL ='KG' AND A.CODPROD = VOA.CODPROD)\r\n" + 
/* 1556 */       "ELSE A.QTDNEG END),0) AS QTD_UN_PADRAO,\r\n" + 
/* 1557 */       "\r\n" + 
/* 1558 */       "--9\tQuantidade do Produto Operado em quilogramas (kg)\r\n" + 
/* 1559 */       "COALESCE((CASE WHEN E.CODVOL IN ('LT','KG') THEN A.QTDNEG\r\n" + 
/* 1560 */       "WHEN E.PESOLIQ != 0 THEN E.PESOLIQ * QTDNEG\r\n" + 
/* 1561 */       "WHEN  E.CODVOL != 'LT' AND E.PESOLIQ = 0 AND EXISTS (SELECT * FROM TGFVOA VOA WHERE VOA.CODVOL ='LT' AND A.CODPROD = VOA.CODPROD) THEN (SELECT (CASE WHEN DIVIDEMULTIPLICA = 'M' THEN ((A.QTDNEG*QUANTIDADE)*MULTIPVLR) ELSE TRUNC(((A.QTDNEG/QUANTIDADE)*MULTIPVLR),3) END)  FROM TGFVOA VOA WHERE VOA.CODVOL ='LT' AND A.CODPROD = VOA.CODPROD)\r\n" + 
/* 1562 */       "WHEN  E.CODVOL != 'KG' AND E.PESOLIQ = 0 AND EXISTS (SELECT * FROM TGFVOA VOA WHERE VOA.CODVOL ='KG' AND A.CODPROD = VOA.CODPROD) THEN (SELECT (CASE WHEN DIVIDEMULTIPLICA = 'M' THEN ((A.QTDNEG*QUANTIDADE)*MULTIPVLR) ELSE TRUNC(((A.QTDNEG/QUANTIDADE)*MULTIPVLR),3) END)  FROM TGFVOA VOA WHERE VOA.CODVOL ='KG' AND A.CODPROD = VOA.CODPROD)\r\n" + 
/* 1563 */       "ELSE A.QTDNEG END),0)AS QTD_CONVERTIDO_KG,\r\n" + 
/* 1564 */       "\r\n" + 
/* 1565 */       "--10\tC�digo do Modal Utilizado na Movimenta��o\r\n" + 
/* 1566 */       "'0' AS CODMODAL_SIMP,--C�digo do Modal Utilizado na Movimenta��o\r\n" + 
/* 1567 */       "\r\n" + 
/* 1568 */       "--11 C�digo do oleoduto de transporte\r\n" + 
/* 1569 */       "'0' AS COD_OLEODUTO,\r\n" + 
/* 1570 */       "\r\n" + 
/* 1571 */       "--12 Identifica��o do Terceiro Envolvido na Opera��o\r\n" + 
/* 1572 */       "(CASE WHEN D.AD_CODINSTALACAO_ANP IS NULL THEN D.CGC_CPF ELSE '0' END) AS CNPJ_PARC,\r\n" + 
/* 1573 */       "\r\n" + 
/* 1574 */       "--13 C�digo do Munic�pio (Origem/Destino)\r\n" + 
/* 1575 */       "(CASE WHEN  (D.AD_CODINSTALACAO_ANP IS NULL AND D.CGC_CPF IS NOT NULL) THEN COALESCE((SELECT AD_CODCID_ANP FROM TSICID WHERE CODCID = D.CODCID),'0') ELSE '0' END) AS CODCID_ANP,\r\n" + 
/* 1576 */       "\r\n" + 
/* 1577 */       "--14 C�digo de Atividade Econ�mica do Terceiro\r\n" + 
/* 1578 */       "(CASE WHEN  (D.AD_CODINSTALACAO_ANP IS NULL AND D.CGC_CPF IS NOT NULL) THEN COALESCE(D.AD_CNAE_ANP,'0') ELSE '0' END) AS AD_CNAE_ANP,\r\n" + 
/* 1579 */       "\r\n" + 
/* 1580 */       "--15 C�digo do Pa�s (Origem/Destino)\r\n" + 
/* 1581 */       "(CASE WHEN (SELECT CODOPER_ANP FROM AD_OPERACAOANP ANP WHERE ANP.CODOPER = C.AD_CODOPERANP) IN ('2011001', '2012001') THEN (SELECT AD_CODPAI_ANP FROM TSICID CID\r\n" + 
/* 1582 */       "INNER JOIN TSIUFS UF ON CID.UF = UF.CODUF\r\n" + 
/* 1583 */       "INNER JOIN TSIPAI PAI ON UF.CODPAIS = PAI.CODPAIS\r\n" + 
/* 1584 */       "WHERE D.CODCID = CID.CODCID) ELSE '0' END) AS COD_PAIS_ANP,--C�digo ANP Pais,\r\n" + 
/* 1585 */       "\r\n" + 
/* 1586 */       "--16 N�mero da Licen�a de Importa��o (LI)\r\n" + 
/* 1587 */       "COALESCE(B.AD_LICENCA_IMPORTACAO_LI,'0') AS LICENCA_IMPORTACAO_LI,\r\n" + 
/* 1588 */       "\r\n" + 
/* 1589 */       "--17 N�mero da Declara��o de Importa��o (DI)\r\n" + 
/* 1590 */       "COALESCE(AD_NUM_DELCARACAO_IMPORT_DI,'0') AS NUM_DELCARACAO_IMPORTACAO_DI,\r\n" + 
/* 1591 */       "\r\n" + 
/* 1592 */       "--18 N�mero da Nota Fiscal da Opera��o Comercial -\r\n" + 
/* 1593 */       "'0' AS NRONF,\r\n" + 
/* 1594 */       "\r\n" + 
/* 1595 */       "--19 C�digo da S�rie da Nota Fiscal da Opera��o Comercial\r\n" + 
/* 1596 */       "(CASE WHEN (CASE WHEN B.CHAVENFE IS NOT NULL THEN B.CHAVENFE ELSE B.CHAVECTE END) IS NULL THEN COALESCE((SELECT SER.COD_SIMP FROM AD_SERIENFOPERSIMP SER WHERE SER.CODSEROPER = B.AD_CODSEROPER),'0') ELSE '0' END) AS COD_SERIE_NF,\r\n" + 
/* 1597 */       "\r\n" + 
/* 1598 */       "--20 Data da Opera��o Comercial/NF-E (DDMMAAAA)\r\n" + 
/* 1599 */       "(CASE WHEN B.DTENTSAI IS NOT NULL THEN TO_CHAR(B.DTENTSAI, 'DDMMYYYY') ELSE '0' END) AS DTENTSAI,\r\n" + 
/* 1600 */       "\r\n" + 
/* 1601 */       "--21 C�digo do Tipo de Tarifa de Servi�o (Oleodutos e Terminais de combust�veis l�quidos)\r\n" + 
/* 1602 */       "'0' AS COD_TIP_TAR_SERV,\r\n" + 
/* 1603 */       "\r\n" + 
/* 1604 */       "--22 CARACTERISTICA Inativo - preencher com zeros\r\n" + 
/* 1605 */       "'0' AS CARACTERISTICA_INATIVO,\r\n" + 
/* 1606 */       "\r\n" + 
/* 1607 */       "--23 METODO Inativo - preencher com zeros\r\n" + 
/* 1608 */       "'0' AS METODO_INATIVO,\r\n" + 
/* 1609 */       "\r\n" + 
/* 1610 */       "--24 Modalidade do frete\r\n" + 
/* 1611 */       "0 AS MODALIDADE_FRETE,\r\n" + 
/* 1612 */       "\r\n" + 
/* 1613 */       "--25 N�mero do Documento da Qualidade / Quando o ARI for Distribuidor de Produtos Asf�lticos considerar-se-� o campo como Pre�o (R$/kg)\r\n" + 
/* 1614 */       "'0' AS VALOR_CARACTERISTICA,\r\n" + 
/* 1615 */       "\r\n" + 
/* 1616 */       "--26 C�digo do Produto/Opera��o Resultante\r\n" + 
/* 1617 */       "G.CODANP AS CODPROD_RESULTANTE_ANP,\r\n" + 
/* 1618 */       "\r\n" + 
/* 1619 */       "--27\tValor unit�rio (nota fiscal)\r\n" + 
/* 1620 */       "100 AS VLRUNIT, --COALESCE(A.VLRUNIT,0)\r\n" + 
/* 1621 */       "\r\n" + 
/* 1622 */       "--28 Recipiente de GLP\r\n" + 
/* 1623 */       "'0' AS RECIPIENTE_GLP,\r\n" + 
/* 1624 */       "\r\n" + 
/* 1625 */       "--29 Chave de acesso da NF-e ou do CT-e\r\n" + 
/* 1626 */       "(CASE WHEN B.CHAVENFE IS NOT NULL THEN B.CHAVENFE ELSE B.CHAVECTE END) AS CHAVE_NFE_CTE,\r\n" + 
/* 1627 */       "\r\n" + 
/* 1628 */       "\r\n" + 
/* 1629 */       "C.AD_CODOPERTOTALIZADORANPMP AS OPER_TOTALIZADOR\r\n" + 
/* 1630 */       "\r\n" + 
/* 1631 */       "\r\n" + 
/* 1632 */       "FROM (SELECT \r\n" + 
/* 1633 */       "IDIPROC,\r\n" + 
/* 1634 */       "NUNOTA,\r\n" + 
/* 1635 */       "CODPROD AS PA,\r\n" + 
/* 1636 */       "CODVOL AS CODVOL_PA,\r\n" + 
/* 1637 */       "QTDNEG AS QTD_PA,\r\n" + 
/* 1638 */       "MP_FINAL AS CODPROD,\r\n" + 
/* 1639 */       "CODVOL_FINAL AS CODVOL,\r\n" + 
/* 1640 */       "SUM(QTDMP_FINAL) AS QTDNEG\r\n" + 
/* 1641 */       "FROM (\r\n" + 
/* 1642 */       "SELECT \r\n" + 
/* 1643 */       "MP.IDIPROC, \r\n" + 
/* 1644 */       "OPE.IDIATV, \r\n" + 
/* 1645 */       "OPE.SEQROPE,\r\n" + 
/* 1646 */       "OPE.NUAPO,\r\n" + 
/* 1647 */       "OPE.NUNOTA,\r\n" + 
/* 1648 */       "ITE.CODPROD,\r\n" + 
/* 1649 */       "ITE.CODVOL,\r\n" + 
/* 1650 */       "ITE.QTDNEG,\r\n" + 
/* 1651 */       "MP.CODPROD AS CODPROD_MP1,\r\n" + 
/* 1652 */       "MP.CODVOL AS CODVOL_MP1,\r\n" + 
/* 1653 */       "MP.QTDNEG AS QTDNEG_MP1, \r\n" + 
/* 1654 */       "--PI.CODPRODPI AS PI1,\r\n" + 
/* 1655 */       "MP.CONTROLE AS CONTROLE_MP1,\r\n" + 
/* 1656 */       "MPPI.CODPRODMP AS CODPROD_MP2,\r\n" + 
/* 1657 */       "MPPI.CONTROLEMP AS CONTROLE_MP2,\r\n" + 
/* 1658 */       "MPPI.CODVOL AS CODVOL_MP2,\r\n" + 
/* 1659 */       "--MPPARAUM,\r\n" + 
/* 1660 */       "MPPI.MPPARAUM*MP.QTDNEG AS QTD_MP2,\r\n" + 
/* 1661 */       "\r\n" + 
/* 1662 */       "MPPI2.CODPRODMP AS CODPROD_MP3,\r\n" + 
/* 1663 */       "MPPI2.CONTROLEMP AS CONTROLE_MP3,\r\n" + 
/* 1664 */       "MPPI2.CODVOL AS CODVOL_MP3,\r\n" + 
/* 1665 */       "--MPPARAUM,\r\n" + 
/* 1666 */       "MPPI2.MPPARAUM*(MPPI.MPPARAUM*MP.QTDNEG) AS QTD_MP3,\r\n" + 
/* 1667 */       "\r\n" + 
/* 1668 */       "COALESCE(MPPI2.CODPRODMP, MPPI.CODPRODMP, MP.CODPROD) AS MP_FINAL,\r\n" + 
/* 1669 */       "COALESCE(MPPI2.CODVOL, MPPI.CODVOL, MP.CODVOL) AS CODVOL_FINAL,\r\n" + 
/* 1670 */       "COALESCE((MPPI2.MPPARAUM*(MPPI.MPPARAUM*MP.QTDNEG)), (MPPI.MPPARAUM*MP.QTDNEG), MP.QTDNEG) AS QTDMP_FINAL\r\n" + 
/* 1671 */       "FROM TPRROPE OPE \r\n" + 
/* 1672 */       "INNER JOIN TPRIATV IATV ON OPE.IDIATV = IATV.IDIATV \r\n" + 
/* 1673 */       "INNER JOIN TGFITE ITE ON ITE.NUNOTA = OPE.NUNOTA \r\n" + 
/* 1674 */       "LEFT JOIN TPRIPROC IPROC ON IPROC.IDIPROC =  IATV.IDIPROC \r\n" + 
/* 1675 */       "INNER JOIN (SELECT \r\n" + 
/* 1676 */       "    OPE.IDIATV, SEQROPE,OPE.NUAPO,OPE.NUNOTA,QTDNEG,IATV.IDIPROC,ITE.CODPROD,ITE.CONTROLE, ITE.CODVOL\r\n" + 
/* 1677 */       "    FROM \r\n" + 
/* 1678 */       "    TPRROPE OPE \r\n" + 
/* 1679 */       "    INNER JOIN TPRIATV IATV ON OPE.IDIATV = IATV.IDIATV \r\n" + 
/* 1680 */       "    INNER JOIN TGFITE ITE ON ITE.NUNOTA = OPE.NUNOTA\r\n" + 
/* 1681 */       "    WHERE ITE.USOPROD = 'M' /*AND IATV.IDIPROC = 343*/)MP ON MP.IDIPROC = IATV.IDIPROC AND MP.NUNOTA = OPE.NUNOTA \r\n" + 
/* 1682 */       "    \r\n" + 
/* 1683 */       "LEFT JOIN TPRLPI PI ON PI.CODPRODPI = MP.CODPROD AND  PI.CODPRODPA = ITE.CODPROD AND PI.IDPROC = IPROC.IDPROC \r\n" + 
/* 1684 */       "\r\n" + 
/* 1685 */       "LEFT JOIN (SELECT\r\n" + 
/* 1686 */       "    ITE.CODPROD,\r\n" + 
/* 1687 */       "    MP.CODPROD AS CODPRODMP, \r\n" + 
/* 1688 */       "    ITE.CONTROLE, \r\n" + 
/* 1689 */       "    MP.CONTROLE CONTROLEMP, \r\n" + 
/* 1690 */       "    ITE.QTDNEG, \r\n" + 
/* 1691 */       "    MP.QTDNEG QTDNEGMP,\r\n" + 
/* 1692 */       "    ITE.NUNOTA,\r\n" + 
/* 1693 */       "    (MP.QTDNEG/ITE.QTDNEG) AS MPPARAUM,\r\n" + 
/* 1694 */       "    ITE.CODVOL\r\n" + 
/* 1695 */       "    FROM TGFITE ITE \r\n" + 
/* 1696 */       "    INNER JOIN TGFCAB CAB ON CAB.NUNOTA = ITE.NUNOTA \r\n" + 
/* 1697 */       "    LEFT JOIN (SELECT \r\n" + 
/* 1698 */       "        ITE.CODPROD,\r\n" + 
/* 1699 */       "        ITE.CONTROLE,\r\n" + 
/* 1700 */       "        ITE.QTDNEG,\r\n" + 
/* 1701 */       "        ITE.NUNOTA\r\n" + 
/* 1702 */       "        FROM TGFITE ITE \r\n" + 
/* 1703 */       "        INNER JOIN TGFCAB CAB ON CAB.NUNOTA = ITE.NUNOTA\r\n" + 
/* 1704 */       "        WHERE  TIPMOV = 'F'  AND USOPROD = 'M')MP ON MP.NUNOTA = ITE.NUNOTA\r\n" + 
/* 1705 */       "    WHERE CAB.TIPMOV = 'F'  AND ITE.USOPROD = 'V')MPPI ON MPPI.CODPROD = PI.CODPRODPI AND MPPI.CONTROLE = MP.CONTROLE \r\n" + 
/* 1706 */       "\r\n" + 
/* 1707 */       "LEFT JOIN (SELECT\r\n" + 
/* 1708 */       "    ITE.CODPROD,\r\n" + 
/* 1709 */       "    MP.CODPROD AS CODPRODMP, \r\n" + 
/* 1710 */       "    ITE.CONTROLE, \r\n" + 
/* 1711 */       "    MP.CONTROLE CONTROLEMP, \r\n" + 
/* 1712 */       "    ITE.QTDNEG, \r\n" + 
/* 1713 */       "    MP.QTDNEG QTDNEGMP,\r\n" + 
/* 1714 */       "    ITE.NUNOTA,\r\n" + 
/* 1715 */       "    (MP.QTDNEG/ITE.QTDNEG) AS MPPARAUM,\r\n" + 
/* 1716 */       "    ITE.CODVOL\r\n" + 
/* 1717 */       "    FROM TGFITE ITE \r\n" + 
/* 1718 */       "    INNER JOIN TGFCAB CAB ON CAB.NUNOTA = ITE.NUNOTA \r\n" + 
/* 1719 */       "    LEFT JOIN (SELECT \r\n" + 
/* 1720 */       "        ITE.CODPROD,\r\n" + 
/* 1721 */       "        ITE.CONTROLE,\r\n" + 
/* 1722 */       "        ITE.QTDNEG,\r\n" + 
/* 1723 */       "        ITE.NUNOTA\r\n" + 
/* 1724 */       "        FROM TGFITE ITE \r\n" + 
/* 1725 */       "        INNER JOIN TGFCAB CAB ON CAB.NUNOTA = ITE.NUNOTA\r\n" + 
/* 1726 */       "        WHERE  TIPMOV = 'F'  AND USOPROD = 'M')MP ON MP.NUNOTA = ITE.NUNOTA\r\n" + 
/* 1727 */       "    WHERE CAB.TIPMOV = 'F'  AND ITE.USOPROD = 'V')MPPI2 ON MPPI2.CODPROD = MPPI.CODPRODMP AND MPPI2.CONTROLE = MPPI.CONTROLEMP\r\n" + 
/* 1728 */       "\r\n" + 
/* 1729 */       "WHERE IATV.IDIPROC = MP.IDIPROC\r\n" + 
/* 1730 */       "AND ITE.USOPROD = 'V' )\r\n" + 
/* 1731 */       "GROUP BY \r\n" + 
/* 1732 */       "IDIPROC,\r\n" + 
/* 1733 */       "NUNOTA,\r\n" + 
/* 1734 */       "CODPROD,\r\n" + 
/* 1735 */       "CODVOL,\r\n" + 
/* 1736 */       "QTDNEG,\r\n" + 
/* 1737 */       "CODVOL_FINAL,\r\n" + 
/* 1738 */       "MP_FINAL) A \r\n" + 
/* 1739 */       "INNER JOIN TGFCAB B ON A.NUNOTA = B.NUNOTA\r\n" + 
/* 1740 */       "INNER JOIN TGFTOP C ON B.CODTIPOPER = C.CODTIPOPER AND DHALTER = (SELECT MAX(DHALTER) FROM TGFTOP TOP WHERE C.CODTIPOPER = TOP.CODTIPOPER)\r\n" + 
/* 1741 */       "INNER JOIN TGFPAR D ON B.CODPARC = D.CODPARC\r\n" + 
/* 1742 */       "INNER JOIN TGFPRO E ON A.CODPROD = E.CODPROD\r\n" + 
/* 1743 */       "INNER JOIN TSIEMP F ON B.CODEMP = F.CODEMP\r\n" + 
/* 1744 */       "INNER JOIN TGFPRO G ON A.PA = G.CODPROD\r\n" + 
/* 1745 */       "WHERE COALESCE(C.AD_ATUALIZA_EST_SIMP,'N') = 'S'\r\n" + 
/* 1746 */       "AND COALESCE(E.AD_GERA_SIMP,'N') = 'S'\r\n" + 
/* 1747 */       "AND COALESCE(G.AD_GERA_SIMP,'N') = 'S'\r\n" + 
/* 1748 */       "AND TO_CHAR(B.DTNEG,'mm-yyyy') = '" + referencia + "'\r\n" + 
/* 1749 */       "AND B.CODEMP = " + codEmp + " \r\n" + 
/* 1750 */       "ORDER BY A.IDIPROC)\r\n" + 
/* 1751 */       "GROUP BY \r\n" + 
/* 1752 */       "NUNOTA,\r\n" + 
/* 1753 */       "IDIPROC,\r\n" + 
/* 1754 */       "AD_CODEMP_ANP,\r\n" + 
/* 1755 */       "OPERACAO,\r\n" + 
/* 1756 */       "COD_INST_EMP,\r\n" + 
/* 1757 */       "COD_INST_PAR,\r\n" + 
/* 1758 */       "CODPROD_ANP,\r\n" + 
/* 1759 */       "CODMODAL_SIMP,\r\n" + 
/* 1760 */       "COD_OLEODUTO,\r\n" + 
/* 1761 */       "CNPJ_PARC,\r\n" + 
/* 1762 */       "CODCID_ANP,\r\n" + 
/* 1763 */       "AD_CNAE_ANP,\r\n" + 
/* 1764 */       "COD_PAIS_ANP,\r\n" + 
/* 1765 */       "LICENCA_IMPORTACAO_LI,\r\n" + 
/* 1766 */       "NUM_DELCARACAO_IMPORTACAO_DI,\r\n" + 
/* 1767 */       "NRONF,\r\n" + 
/* 1768 */       "COD_SERIE_NF,\r\n" + 
/* 1769 */       "DTENTSAI,\r\n" + 
/* 1770 */       "COD_TIP_TAR_SERV,\r\n" + 
/* 1771 */       "CARACTERISTICA_INATIVO,\r\n" + 
/* 1772 */       "METODO_INATIVO,\r\n" + 
/* 1773 */       "MODALIDADE_FRETE,\r\n" + 
/* 1774 */       "VALOR_CARACTERISTICA,\r\n" + 
/* 1775 */       "CODPROD_RESULTANTE_ANP,\r\n" + 
/* 1776 */       "VLRUNIT,\r\n" + 
/* 1777 */       "RECIPIENTE_GLP,\r\n" + 
/* 1778 */       "CHAVE_NFE_CTE,\r\n" + 
/* 1779 */       "OPER_TOTALIZADOR))\r\n";
/*      */     
/* 1781 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/* 1782 */     pstm.execute();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResultSet totalizar1021(JdbcWrapper jdbcWrapper, BigDecimal codSimulaSimp) throws Exception {
/* 1788 */     String qry = "SELECT\r\n    DET.CODSIMSIM\r\n    --, DET.CODDETSIMSIMP\r\n    , DET.CODEMP_ANP\r\n    , DET.MESREFERENCIA\r\n    , DET.CODOPER\r\n    , OPER.CODOPER_ANP\r\n    , DET.CODINSTALACAO1\r\n    , DET.CODINSTALACAO2\r\n    , DET.CODPROD_ANP\r\n    , SUM(DET.QTD_PROD_UN_ANP) AS QTD_PROD_UN_ANP\r\n    , SUM(DET.QTD_PROD_OPERADO_KG) AS QTD_PROD_OPERADO_KG\r\nFROM AD_DETSIMULARSIMP DET\r\nJOIN AD_OPERACAOANP OPER ON DET.CODOPER = OPER.CODOPER\r\nWHERE DET.CODSIMSIM = " + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1802 */       codSimulaSimp + "\r\n" + 
/* 1803 */       "AND OPER.CODOPER_ANP LIKE '1021%'\r\n" + 
/* 1804 */       "GROUP BY DET.CODSIMSIM\r\n" + 
/* 1805 */       "    , DET.CODEMP_ANP\r\n" + 
/* 1806 */       "    , DET.MESREFERENCIA\r\n" + 
/* 1807 */       "    , DET.CODOPER\r\n" + 
/* 1808 */       "    , OPER.CODOPER_ANP\r\n" + 
/* 1809 */       "    , DET.CODINSTALACAO1\r\n" + 
/* 1810 */       "    , DET.CODINSTALACAO2\r\n" + 
/* 1811 */       "    , DET.CODPROD_ANP";
/*      */     
/* 1813 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(qry);
/* 1814 */     ResultSet query = pstm.executeQuery();
/* 1815 */     return query;
/*      */   }
/*      */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\buscar\buscarEstoque.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */