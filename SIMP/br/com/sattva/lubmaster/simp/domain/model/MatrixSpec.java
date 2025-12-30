/*    */ package br.com.sattva.lubmaster.simp.domain.model;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ public final class MatrixSpec {
/*    */   public String version;
/*    */   
/*    */   public static MatrixSpec defaultMatrix(String version) {
/* 11 */     MatrixSpec m = new MatrixSpec();
/* 12 */     m.version = (version != null) ? version : "fallback";
/*    */ 
/*    */     
/* 15 */     List<SimpField> order = new ArrayList<>();
/* 16 */     order.add(SimpField.CONTADOR_SEQUENCIAL);
/* 17 */     order.add(SimpField.AGENTE_REGULADO_INFORMANTE);
/* 18 */     order.add(SimpField.MES_REFERENCIA_MMAAAA);
/* 19 */     order.add(SimpField.CODIGO_OPERACAO);
/* 20 */     order.add(SimpField.CODIGO_INSTALACAO_1);
/* 21 */     order.add(SimpField.CODIGO_INSTALACAO_2);
/* 22 */     order.add(SimpField.CODIGO_PRODUTO_OPERADO);
/* 23 */     order.add(SimpField.QTD_UN_MEDIDA_ANP);
/* 24 */     order.add(SimpField.QTD_PRODUTO_KG);
/* 25 */     order.add(SimpField.CODIGO_MODAL);
/* 26 */     order.add(SimpField.CODIGO_VEICULO);
/* 27 */     order.add(SimpField.IDENTIFICACAO_TERCEIRO);
/* 28 */     order.add(SimpField.CODIGO_MUNICIPIO);
/* 29 */     order.add(SimpField.CODIGO_ATIVIDADE_ECONOMICA);
/* 30 */     order.add(SimpField.CODIGO_PAIS);
/* 31 */     order.add(SimpField.LICENCA_IMPORTACAO_LI);
/* 32 */     order.add(SimpField.DECLARACAO_IMPORTACAO_DI);
/* 33 */     order.add(SimpField.NUMERO_NF);
/* 34 */     order.add(SimpField.CODIGO_SERIE_NF);
/* 35 */     order.add(SimpField.DATA_NF);
/* 36 */     order.add(SimpField.CODIGO_SERVICO_ACORDADO);
/* 37 */     order.add(SimpField.CODIGO_CARACTERISTICA_FISICO_QUIM);
/* 38 */     order.add(SimpField.CODIGO_METODO);
/* 39 */     order.add(SimpField.MODALIDADE_FRETE);
/* 40 */     order.add(SimpField.VALOR_CARACTERISTICA_PRECO_RS_KG);
/* 41 */     order.add(SimpField.CODIGO_PROD_OPER_RESULTANTE);
/* 42 */     order.add(SimpField.VALOR_UNITARIO);
/* 43 */     order.add(SimpField.RECIPIENTE_GLP);
/* 44 */     order.add(SimpField.CHAVE_ACESSO_NFE);
/* 45 */     m.fieldsOrder = order;
/*    */ 
/*    */     
/* 48 */     List<FieldSpec> base = new ArrayList<>();
/* 49 */     base.add(fs(SimpField.CONTADOR_SEQUENCIAL, 10, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 50 */     base.add(fs(SimpField.AGENTE_REGULADO_INFORMANTE, 10, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 51 */     base.add(fs(SimpField.MES_REFERENCIA_MMAAAA, 6, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 52 */     base.add(fs(SimpField.CODIGO_OPERACAO, 7, "X", 0, ' ', "L", FieldRule.OBRIGATORIO, null));
/* 53 */     base.add(fs(SimpField.CODIGO_INSTALACAO_1, 7, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 54 */     base.add(fs(SimpField.CODIGO_INSTALACAO_2, 7, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 55 */     base.add(fs(SimpField.CODIGO_PRODUTO_OPERADO, 9, "N", 0, '0', "R", FieldRule.OBRIGATORIO, null));
/* 56 */     base.add(fs(SimpField.QTD_UN_MEDIDA_ANP, 15, "N", 2, '0', "R", FieldRule.OPCIONAL, null));
/* 57 */     base.add(fs(SimpField.QTD_PRODUTO_KG, 15, "N", 2, '0', "R", FieldRule.OBRIGATORIO, null));
/* 58 */     base.add(fs(SimpField.CODIGO_MODAL, 1, "X", 0, ' ', "L", FieldRule.OBRIGATORIO, null));
/* 59 */     base.add(fs(SimpField.CODIGO_VEICULO, 7, "X", 0, ' ', "L", FieldRule.OPCIONAL, null));
/* 60 */     base.add(fs(SimpField.IDENTIFICACAO_TERCEIRO, 14, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 61 */     base.add(fs(SimpField.CODIGO_MUNICIPIO, 7, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 62 */     base.add(fs(SimpField.CODIGO_ATIVIDADE_ECONOMICA, 5, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 63 */     base.add(fs(SimpField.CODIGO_PAIS, 4, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 64 */     base.add(fs(SimpField.LICENCA_IMPORTACAO_LI, 10, "X", 0, ' ', "L", FieldRule.OPCIONAL, null));
/* 65 */     base.add(fs(SimpField.DECLARACAO_IMPORTACAO_DI, 10, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 66 */     base.add(fs(SimpField.NUMERO_NF, 7, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 67 */     base.add(fs(SimpField.CODIGO_SERIE_NF, 2, "X", 0, ' ', "L", FieldRule.OPCIONAL, null));
/* 68 */     base.add(fs(SimpField.DATA_NF, 8, "D", 0, '0', "R", FieldRule.OBRIGATORIO, null));
/* 69 */     base.add(fs(SimpField.CODIGO_SERVICO_ACORDADO, 1, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 70 */     base.add(fs(SimpField.CODIGO_CARACTERISTICA_FISICO_QUIM, 3, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 71 */     base.add(fs(SimpField.CODIGO_METODO, 3, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 72 */     base.add(fs(SimpField.MODALIDADE_FRETE, 2, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 73 */     base.add(fs(SimpField.VALOR_CARACTERISTICA_PRECO_RS_KG, 10, "N", 2, '0', "R", FieldRule.OPCIONAL, null));
/* 74 */     base.add(fs(SimpField.CODIGO_PROD_OPER_RESULTANTE, 9, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/* 75 */     base.add(fs(SimpField.VALOR_UNITARIO, 7, "N", 2, '0', "R", FieldRule.OPCIONAL, null));
/* 76 */     base.add(fs(SimpField.RECIPIENTE_GLP, 2, "X", 0, ' ', "L", FieldRule.OPCIONAL, null));
/* 77 */     base.add(fs(SimpField.CHAVE_ACESSO_NFE, 44, "N", 0, '0', "R", FieldRule.OPCIONAL, null));
/*    */     
/* 79 */     Map<String, List<FieldSpec>> map = new HashMap<>();
/* 80 */     map.put("DEFAULT", base);
/*    */     
/* 82 */     map.put("1011001", base);
/* 83 */     map.put("1011009", base);
/*    */     
/* 85 */     m.rulesByOperation = map;
/* 86 */     return m;
/*    */   }
/*    */   public List<SimpField> fieldsOrder; public Map<String, List<FieldSpec>> rulesByOperation;
/*    */   private static FieldSpec fs(SimpField f, int len, String type, int scale, char pad, String align, FieldRule rule, String cond) {
/* 90 */     FieldSpec x = new FieldSpec();
/* 91 */     x.field = f; x.length = len; x.type = type; x.scale = scale;
/* 92 */     x.pad = pad; x.align = align; x.rule = rule; x.condition = cond;
/* 93 */     return x;
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\domain\model\MatrixSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */