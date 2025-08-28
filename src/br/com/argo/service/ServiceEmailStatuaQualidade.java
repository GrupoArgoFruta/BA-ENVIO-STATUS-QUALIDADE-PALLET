package br.com.argo.service;

import java.math.BigDecimal;

import com.sankhya.util.BigDecimalUtil;

import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.util.DynamicEntityNames;

public class ServiceEmailStatuaQualidade {
	public  void enviarEmail(String titulo, String mensagem) throws Exception {
		SessionHandle hnd = null;
		try {
			hnd = JapeSession.open();
			JapeWrapper ordemServicoDAO = JapeFactory.dao(DynamicEntityNames.FILA_MSG);
			ordemServicoDAO.create()
			.set("EMAIL", "expedicao@argofruta.com")// envia para esse email expedicao@argofruta.com
			.set("CODCON", BigDecimal.ZERO)
			.set("STATUS", "Pendente")
			.set("TIPOENVIO", "E")
			.set("MAXTENTENVIO", BigDecimalUtil.valueOf(3))
			.set("ASSUNTO", titulo)
			.set("MENSAGEM", mensagem.toCharArray())
			.save();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JapeSession.close(hnd);
		}
	}

}
