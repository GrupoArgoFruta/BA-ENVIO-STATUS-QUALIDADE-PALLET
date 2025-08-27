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
			Timestamp dthoraentrada, Timestamp dthorasaida, String nomeStatus) throws MGEModelException {
		// TODO Auto-generated method stub
		
		JapeSession.SessionHandle hnd = null;
		JapeWrapper pedDAO = JapeFactory.dao("AD_HISTSTATUSPALLET");
//		DynamicEntityNames
		try {
			
			hnd = JapeSession.open(); // Abertura da sessão do JapeSession
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
				
				.save();  	
			
		} catch (Exception e) {
			MGEModelException.throwMe(e);
		} finally {
			JapeSession.close(hnd);
		}
		
	}

}
