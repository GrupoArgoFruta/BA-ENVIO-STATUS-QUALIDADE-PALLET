package br.com.argo.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.argo.service.ServiceCorpoEmailQualidade;
import br.com.argo.service.ServiceHistoricoEnvio;
import br.com.argo.util.Ultilitarios;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.auth.AuthenticationInfo;
import br.com.sankhya.ws.ServiceContext;
import repository.ParametrosQualidadeDTO;

public class ControllerPrincipalQualidade implements AcaoRotinaJava {

	@Override
	public void doAction(ContextoAcao ctx) throws Exception {
		// TODO Auto-generated method stub
		Registro[] linhas = ctx.getLinhas();
	    JdbcWrapper jdbc = JapeFactory.getEntityFacade().getJdbcWrapper();
	    SessionHandle hnd = JapeSession.open(); // Sempre feche depois
	    ServiceCorpoEmailQualidade ServiceEmail = new ServiceCorpoEmailQualidade();
	    ServiceHistoricoEnvio ServiceArmazenarEnvio  = new ServiceHistoricoEnvio ();
	    Ultilitarios util = new Ultilitarios();
	    String usuarioLogadoNome = ((AuthenticationInfo) ServiceContext.getCurrent().getAutentication()).getUsuVO().getNOMEUSU();
		BigDecimal usuarioLogadoID = ((AuthenticationInfo) ServiceContext.getCurrent().getAutentication()).getUserID();
		Timestamp dataAtual = new Timestamp(new Date().getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String dataHoraAtualFormatada = sdf.format(dataAtual);
	    try {
	    	// meus parâmetro  globais 
	        Timestamp dthoraentrada = (Timestamp) ctx.getParam("DTENTRADA");
	        Timestamp dthorasaida = (Timestamp) ctx.getParam("DTSAIDA");
	        Integer qtdpastilhas    = (Integer) ctx.getParam("QTDPASTILHA");
        	Integer qtdativadores   = (Integer) ctx.getParam("QTDATIVADORES");
        	Integer qtdpallet       = (Integer) ctx.getParam("QTDPALLET");
        	String obs = (String) ctx.getParam("OBS");
        	String codstatus = (String) ctx.getParam("CODSTATUS");
        	String localTratamento = (String) ctx.getParam("LOCATRATAMENTO");
        	String descricaoStatusParam = "Serviço não informado"; // valor padrão
        	String localTratado;
        	switch (localTratamento) {
        	    case "A":
        	        localTratado = "ANTECAMARA";
        	        break;
        	    case "C":
        	        localTratado = "CONTEINER FIXO";
        	        break;
        	    case "CM":
        	        localTratado = "CAMARA 4";
        	        break;
        	    case "CA":
        	        localTratado = "CONTEINER ARGOLOGISTICA";
        	        break;
        	    case "CF":
        	        localTratado = "CARRETA FAMOSA";
        	        break;
        	    default:
        	        localTratado = (localTratamento != null ? localTratamento.toUpperCase() : "DESCONHECIDO");
        	}
            // Só busca no banco se o parâmetro foi informado
        	BigDecimal codigostatus = null;
            if (codstatus != null && !codstatus.trim().isEmpty()) {
                try {
                	 codigostatus = new BigDecimal(codstatus); // <<<< instancia aqui
                    JapeWrapper servicoDAO = JapeFactory.dao("AD_STATUSPLT");
                    DynamicVO servicoVO = servicoDAO.findByPK(codigostatus);

                    if (servicoVO != null) {
                        descricaoStatusParam = servicoVO.asString("DESCRICAO");
                    } else {
                    	descricaoStatusParam = "Serviço não encontrado";
                    }
                } catch (Exception e) {
                	descricaoStatusParam = "Serviço inválido";
                }
            }
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
	    		// variaveis 
	    		BigDecimal nUnico = (BigDecimal) registro.getCampo("NROUNICO");
	    		String palett = (String) registro.getCampo("NROPALLET");
	    		BigDecimal codlocal = (BigDecimal) registro.getCampo("CODLOCAL");
	    		BigDecimal StatusQualidade = (BigDecimal) registro.getCampo("CODSTATUS");
	    		String statusSigla = (String) registro.getCampo("STATUS");
	    		String obsqualidade = (String) registro.getCampo("OBSQUALIDADE");
	    		String calibre = (String) registro.getCampo("CALIBRE");
	    		BigDecimal QtdCaixaPallet = (BigDecimal) registro.getCampo("QTDCAIXASPLT");
	    		String PAGerados = (String) registro.getCampo("PAGERADOS");
	    		Timestamp Dtlqualidade = (Timestamp) registro.getCampo("DHALTERQUALIDADE");
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
                .append("<td>").append(descricaoStatusParam).append("</td>")
                .append("<td>").append(Status).append("</td>")
                .append("<td>").append(obs).append("</td>")
                .append("<td>").append(calibre).append("</td>")
                .append("<td>").append(QtdCaixaPallet).append("</td>")
                .append("<td>").append(PAGerados).append("</td>")
                .append("</tr>");
	    		
	    		// serviço que envia os histoticos para armazenar em uma tabela AD_HISTSTATUSPALLET 
	    		ServiceArmazenarEnvio.atualizarEnvio( nUnico,palett,codlocal,codigostatus,Status,obsqualidade,calibre,QtdCaixaPallet,PAGerados,dthoraentrada,dthorasaida,descricaoStatusParam
	            ,nomeLocal,qtdpastilhas,qtdativadores,qtdpallet,obs,descricaoStatusParam,localTratado,usuarioLogadoNome,usuarioLogadoID,dataAtual);
	    		
	    		//atualiza o status e a obs na tabela AD_MONTPALLET
	    		util.AtualizarStatus(codigostatus, obs, nUnico, ctx);
	    		// grava histórico do status anterior
	    		util.insertStatusAnterios(nUnico, StatusQualidade, obsqualidade, dataAtual, usuarioLogadoID);
	    		
	    	}
	    	    tabelaHtml.append("</table>");
	    	    //serviço que envia as informação para email
	    	    ServiceEmail.CorpoEmailStatusQualidade(ctx, tabelaHtml.toString(),dthoraentrada,dthorasaida,
	    	    qtdpastilhas,qtdativadores,qtdpallet,obs,descricaoStatusParam,localTratado);
	    	 
		} catch (Exception e) {
			// TODO: handle exception
			   ctx.mostraErro("Erro ao enviar o e-mail na classe principal: " + e.getMessage());
		        e.printStackTrace();
		}
	}

}


//P_NROUNICO







