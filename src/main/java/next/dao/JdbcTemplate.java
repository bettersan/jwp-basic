package next.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import core.jdbc.ConnectionManager;
import next.model.User;

public class JdbcTemplate {
    public void update(String sql, Object[] parameters) throws DataAccessException{
        try (Connection con = ConnectionManager.getConnection(); 
        		PreparedStatement pstmt = con.prepareStatement(sql)) {
        	for(int i = 0; i < parameters.length; i++) {
        		pstmt.setObject(i + 1 , parameters[i]);
        	}
            pstmt.executeUpdate();
        } catch (SQLException e) {
        	throw new DataAccessException();
        }
    }
    
    @SuppressWarnings("rawtypes")
    public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws SQLException{
    	
    	Connection con = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	try {
    		con = ConnectionManager.getConnection();
    		pstmt = con.prepareStatement(sql);
    		pss.setValues(pstmt);
    		
    		rs = pstmt.executeQuery();
    		
    		List<T> result = new ArrayList<T>();
    		while(rs.next()) {
    			result.add(rowMapper.mapRow(rs, 0));
    		}
    		return result;
    		
    	}finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
    	}
		
    }
    
    @SuppressWarnings("rawtypes")
    public <T> T queryForObject(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws SQLException{
    	
    	List<T> result = query(sql, pss, rowMapper);
    	if(result.isEmpty()) {
    		return null;
    	}
    	return result.get(0);
    }
}
