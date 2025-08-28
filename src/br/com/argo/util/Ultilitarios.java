package br.com.argo.util;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Map;

import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class Ultilitarios {
	
	public void AtualizarStatus(BigDecimal codigostatus, String obs, BigDecimal nUnico, ContextoAcao ctx) throws Exception {
	    SessionHandle hnd = JapeSession.open();
	    hnd.setFindersMaxRows(-1);
	    EntityFacade entity = EntityFacadeFactory.getDWFFacade();
	    JdbcWrapper jdbc = entity.getJdbcWrapper();
	    jdbc.openSession();
	   
	    try {
	        NativeSql sql = new NativeSql(jdbc);
	        sql.appendSql("UPDATE AD_MONTPALLET SET CODSTATUS = :CODSTATUS, DHALTERQUALIDADE = SYSDATE, OBSQUALIDADE = :OBSQUALIDADE WHERE NROUNICO = :NROUNICO");
	        sql.setNamedParameter("CODSTATUS", codigostatus);
	        sql.setNamedParameter("OBSQUALIDADE", obs);
	        sql.setNamedParameter("NROUNICO", nUnico);
	        sql.executeUpdate();

	        
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new Exception("Erro ao executar Atualiza status: " + e.getMessage());
	    } finally {
	        JdbcWrapper.closeSession(jdbc);
	        JapeSession.close(hnd);
	    }
	}
	
	public void insertStatusAnterios(BigDecimal nUnico, BigDecimal StatusQualidade, String obsqualidade,
			Timestamp Dtlqualidade, BigDecimal usuarioLogadoID) throws Exception {
		EntityFacade entityFacade = EntityFacadeFactory.getDWFFacade();
		JdbcWrapper jdbc = entityFacade.getJdbcWrapper();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			jdbc.openSession();
			String sqlSeq = "SELECT NVL(MAX(SEQUENCIA),0) + 1 AS PROX_SEQ " + "FROM AD_LOGSTATUSPLT WHERE NROUNICO = ?";
			pstmt = jdbc.getPreparedStatement(sqlSeq);
			pstmt.setBigDecimal(1, nUnico);
			rs = pstmt.executeQuery();

			int proxSeq = 1;
			if (rs.next()) {
				proxSeq = rs.getInt("PROX_SEQ");
			}
			rs.close();
			pstmt.close();
			String sqlUpdate = "INSERT INTO AD_LOGSTATUSPLT "
					+ "(NROUNICO, SEQUENCIA, CODSTATUS, OBSERVACAO, DHALTER, CODUSU) " + "VALUES (?, ?, ?, ?, ?, ?)";
			pstmt = jdbc.getPreparedStatement(sqlUpdate);
			pstmt.setBigDecimal(1, nUnico);
			pstmt.setInt(2, proxSeq);
			pstmt.setBigDecimal(3, StatusQualidade);
			pstmt.setString(4, obsqualidade);
			pstmt.setTimestamp(5, Dtlqualidade);
			pstmt.setBigDecimal(6, usuarioLogadoID);
			pstmt.executeUpdate();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}

			if (jdbc != null) {
				jdbc.closeSession();
			}
		}
	}

}
