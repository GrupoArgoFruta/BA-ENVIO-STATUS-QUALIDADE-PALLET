package br.com.argo.service;

import java.math.BigDecimal;
import java.sql.Timestamp;

import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.MGEModelException;

public class ServiceHistoricoEnvio {
	
	public void atualizarEnvio(BigDecimal nUnico, String palett, BigDecimal codlocal, BigDecimal statusQualidade, String status, 
			String obsqualidade, String calibre, BigDecimal qtdCaixaPallet, String pAGerados, 
			Timestamp dthoraentrada, Timestamp dthorasaida, String nomeStatus, String nomeLocal, Integer qtdpastilhas
			,Integer qtdativadores, Integer qtdpallet, String obs, String descricao, String localTratado,
			String usuarioLogadoNome, BigDecimal usuarioLogadoID, Timestamp dataAtual
			) throws MGEModelException {
		// TODO Auto-generated method stub
		
		JapeSession.SessionHandle hnd = null;
		JapeWrapper pedDAO = JapeFactory.dao("AD_HISTSTATUSPALLET");
//		DynamicEntityNames
		try {
			
			hnd = JapeSession.open(); // Abertura da sessão do JapeSession
			// CONVERSÕES NECESSÁRIAS
	        BigDecimal qtdPastilhasBD = qtdpastilhas != null ?  new BigDecimal(qtdpastilhas) : BigDecimal.ZERO;
	        BigDecimal qtdAtivadoresBD = qtdativadores != null ? new BigDecimal(qtdativadores) :BigDecimal.ZERO;
	        BigDecimal qtdPalletBD = qtdpallet != null ? new BigDecimal(qtdpallet) : BigDecimal.ZERO;
	        
			DynamicVO statusVo = pedDAO.create()
				.set("NROUNICO",nUnico)	
				.set("NROPALLET",palett)
				.set("CODLOCAL",codlocal)
				.set("CODSTATUS",statusQualidade)
				.set("STATUS",status)
				.set("OBSQUALIDADE",obsqualidade)
				.set("CALIBRE",calibre)
				.set("QTDCAIXASPLT",qtdCaixaPallet)
				.set("PAGERADOS",pAGerados)
				.set("DTENTRADA",dthoraentrada)
				.set("DTSAIDA",dthorasaida)
				.set("DESCSTATUS",nomeStatus)
				.set("DESCLOCAL",nomeLocal)
				.set("QTDPASTILHA",qtdPastilhasBD)
				.set("QTDATIVADORES",qtdAtivadoresBD)
				.set("QTDPALLET",qtdPalletBD)
				.set("OBS",obs)
				.set("DESCSERVICO",descricao)
				.set("LOCATRATADO",localTratado)
				.set("NOMEUSU",usuarioLogadoNome)
				.set("CODUSU",usuarioLogadoID)
				.set("DATATUAL",dataAtual)
				.save();  	
			
		} catch (Exception e) {
			MGEModelException.throwMe(e);
		} finally {
			JapeSession.close(hnd);
		}
		
	}

}
