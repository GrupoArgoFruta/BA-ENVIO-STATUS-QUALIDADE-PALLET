package br.com.argo.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.modelcore.MGEModelException;

public class DeleteHistoricoEnvio implements AcaoRotinaJava{

	@Override
	public void doAction(ContextoAcao ctx) throws Exception {
	    Registro[] linhas = ctx.getLinhas();
	    JdbcWrapper jdbc = JapeFactory.getEntityFacade().getJdbcWrapper();
	    SessionHandle hnd = JapeSession.open(); // Sempre feche depois
	    List<String> numerosPallets = new ArrayList<>();

	    try {
	        for (Registro registro : linhas) {
	            BigDecimal codigopk = (BigDecimal) registro.getCampo("CODIGOHIST");
	            BigDecimal nUnico = (BigDecimal) registro.getCampo("NROUNICO");

	            // Adiciona apenas o número do pallet
	            numerosPallets.add(nUnico.toString());

	            // Chamada para deletar o item
	            deletarPallet(codigopk);
	        }

	        if (!numerosPallets.isEmpty()) {
	            StringBuilder mensagemSucesso = new StringBuilder();
	            mensagemSucesso.append("<!DOCTYPE html><html><body>")
	                    .append("<div style='display: flex; align-items: center;'>")
	                    .append("<img src='https://cdn-icons-png.flaticon.com/256/189/189677.png' style='width:23px; height:23px; margin-right:5px;'>")
	                    .append("<p style='color:Black; font-family:verdana; font-size:15px; margin: 0;'><b>Dados  excluídos</b></p>")
	                    .append("</div>")
	                    .append("<p style='font-family:Sans-serif; color:Black;font-size:13px; margin: 1;'>Os pallets deletados foram:<br>");

	            for (String pallet : numerosPallets) {
	                mensagemSucesso.append(" Pallet ").append(pallet).append("<br>");
	            }

	            mensagemSucesso.append("</p></body></html>");

	            ctx.setMensagemRetorno(mensagemSucesso.toString());

	        } else {
	            StringBuilder mensagemErro = new StringBuilder();
	            mensagemErro.append("<!DOCTYPE html><html><body>")
	                    .append("<p style='color:red; font-family:verdana; font-size:15px;'>")
	                    .append("<img src='https://cdn-icons-png.flaticon.com/512/4201/4201973.png' style='width:23px; height:23px; vertical-align:middle; margin-right:5px;'>")
	                    .append("Nenhum pallet encontrado para exclusão.")
	                    .append("</p></body></html>");

	            ctx.setMensagemRetorno(mensagemErro.toString());
	        }

	    } catch (Exception e) {
	        ctx.mostraErro("Erro ao processar o botão de ação [Deletar histórico de pallet]: " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        JapeSession.close(hnd);
	    }
	}

	private void deletarPallet(BigDecimal codigopk) throws SQLException, MGEModelException {
	    JdbcWrapper jdbc = JapeFactory.getEntityFacade().getJdbcWrapper();
	    NativeSql sql = new NativeSql(jdbc);

	    try {
	        sql.appendSql("DELETE FROM AD_HISTSTATUSPALLET WHERE CODIGOHIST = :CODIGOHIST");
	        sql.setNamedParameter("CODIGOHIST", codigopk);
	        sql.executeUpdate();
	    } catch (Exception e) {
	        MGEModelException.throwMe(e);
	    } finally {
	        NativeSql.releaseResources(sql);
	    }
	}



}
