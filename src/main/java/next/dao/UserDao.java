
package next.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import next.model.User;

public class UserDao {
	
	public void insert(User user) throws SQLException{
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "INSERT INTO USERS VALEUS(?,?,?,?)";
		jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
	}

	
    public User findByUserId(String userId){
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
		return jdbcTemplate.queryForObject(sql, (ResultSet rs) -> {
			return new User(
					rs.getString("userId"),
					rs.getString("password"),
					rs.getString("name"),
					rs.getString("email"));
		}, userId);
    }
	
	
	
	public void update(User user) throws SQLException{
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
		jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
	}
    
    	
    

    public List<User> findAll() throws SQLException {
    	SelectJdbcTemplate jdbcTemplate = new SelectJdbcTemplate() {
    		void setValues(PreparedStatement pstmt) throws SQLException{
    			
    		}
    		@Override
    		Object mapRow(ResultSet rs) throws SQLException{
    			return  new User(rs.getString("userId"),rs.getString("password"),rs.getString("name"),rs.getString("email"));
    		}
    	};
    	String sql = "SELECT userId, password, name, email FROM USERS";
    	return (List<User>)jdbcTemplate.query(sql);
    }


}
