package br.com.argo.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;

import br.com.argo.service.ServiceCorpoEmailQualidade;
import br.com.argo.service.ServiceHistoricoEnvio;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;

public class ControllerPrincipalQualidade implements AcaoRotinaJava {

	@Override
	public void doAction(ContextoAcao ctx) throws Exception {
		// TODO Auto-generated method stub
		Registro[] linhas = ctx.getLinhas();
	    JdbcWrapper jdbc = JapeFactory.getEntityFacade().getJdbcWrapper();
	    SessionHandle hnd = JapeSession.open(); // Sempre feche depois
	    ServiceCorpoEmailQualidade ServiceEmail = new ServiceCorpoEmailQualidade();
	    ServiceHistoricoEnvio ServiceArmazenarEnvio  = new ServiceHistoricoEnvio ();
	    try {
	    	// Datas globais (do parâmetro)
	        Timestamp dthoraentrada = (Timestamp) ctx.getParam("DTENTRADA");
	        Timestamp dthorasaida = (Timestamp) ctx.getParam("DTSAIDA");
	    	// Construir a tabela HTML com os dados das notas
	        StringBuilder tabelaHtml = new StringBuilder();
	        tabelaHtml.append("<table>")
            .append("<tr>")
            .append("<th>Nro. Único Pallet</th>")
            .append("<th>Nro. Pallet</th>")
            .append("<th>Local</th>")
            .append("<th>Status Qualidade</th>")
            .append("<th>Status </th>")
            .append("<th>Observação Qualidade Pallet </th>")
            .append("<th>Calibre </th>")
            .append("<th>Qtd. Caixas Pallet </th>")
            .append("<th>P.A. Gerados </th>")
            .append("</tr>");
	    	for (Registro registro : linhas) {
	    		
	    		BigDecimal nUnico = (BigDecimal) registro.getCampo("NROUNICO");
	    		String palett = (String) registro.getCampo("NROPALLET");
	    		BigDecimal codlocal = (BigDecimal) registro.getCampo("CODLOCAL");
	    		BigDecimal StatusQualidade = (BigDecimal) registro.getCampo("CODSTATUS");
	    		String statusSigla = (String) registro.getCampo("STATUS");
	    		String obsqualidade = (String) registro.getCampo("OBSQUALIDADE");
	    		String calibre = (String) registro.getCampo("CALIBRE");
	    		BigDecimal QtdCaixaPallet = (BigDecimal) registro.getCampo("QTDCAIXASPLT");
	    		String PAGerados = (String) registro.getCampo("PAGERADOS");
//	    		OBSQUALIDADE CALIBRE QTDCAIXASPLT  AD_STATUSPLT  CODSTATUS Status Pallet P=Pendente;F=Finalizado
	    		String Status;

	    		switch (statusSigla) {
	    		    case "A":
	    		        Status = "Aberto";
	    		        break;
	    		    case "F":
	    		        Status = "Finalizado";
	    		        break;
	    		    case "D":
	    		        Status = "Desmontado";
	    		        break;
	    		    case "R":
	    		        Status = "Reprocessado";
	    		        break;
	    		    default:
	    		        Status = "Desconhecido";
	    		}
	    		//buscar o local 
	    		JapeWrapper locDAO = JapeFactory.dao("LocalFinanceiro");
				DynamicVO localVO = locDAO.findByPK(codlocal);
				String nomeLocal = localVO.asString("DESCRLOCAL");
				
				//buscar o Cadastro Status Pallet
	    		JapeWrapper statusDAO = JapeFactory.dao("AD_STATUSPLT");
				DynamicVO statusVO = statusDAO.findByPK(StatusQualidade);
				String nomeStatus = statusVO.asString("DESCRICAO");
				
	    		tabelaHtml.append("<tr>")
                .append("<td>").append(nUnico).append("</td>")
                .append("<td>").append(palett).append("</td>")
                .append("<td>").append(nomeLocal).append("</td>")
                .append("<td>").append(nomeStatus).append("</td>")
                .append("<td>").append(Status).append("</td>")
                .append("<td>").append(obsqualidade).append("</td>")
                .append("<td>").append(calibre).append("</td>")
                .append("<td>").append(QtdCaixaPallet).append("</td>")
                .append("<td>").append(PAGerados).append("</td>")
                .append("</tr>");
	    		ServiceArmazenarEnvio.atualizarEnvio( nUnico,palett,codlocal,StatusQualidade,
	                    Status,obsqualidade,calibre,QtdCaixaPallet,PAGerados,dthoraentrada,dthorasaida,nomeStatus
	                    
	                );
	    	}
	    	 tabelaHtml.append("</table>");
	    	
	    	 ServiceEmail.CorpoEmailStatusQualidade(ctx, tabelaHtml.toString(),dthoraentrada,dthorasaida);
	    	 
		} catch (Exception e) {
			// TODO: handle exception
			   ctx.mostraErro("Erro ao enviar o e-mail na classe principal: " + e.getMessage());
		        e.printStackTrace();
		}
	}

}
