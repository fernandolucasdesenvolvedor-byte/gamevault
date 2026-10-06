package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import entities.Game;
import enums.Gender;
import enums.Plataform;
import enums.Status;

public class GameDAOJDBC implements GameDAO {

	@Override
	public List<Game> findAll() {

		Connection conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		List<Game> games = new ArrayList<>();
		
		try {
			
			conn = DB.getConnection();
			st = conn.createStatement();
			rs1 = st.executeQuery("SELECT * FROM games");
			
			while(rs1.next()) {
				Long id = rs1.getLong("id");
				String name = rs1.getString("name");
				String note = rs1.getString("note");
				Status status = Status.valueOf(rs1.getString("status"));
				
				ps = conn.prepareStatement("SELECT genders.name AS name FROM genders WHERE genders.id IN (SELECT gender_id FROM games INNER JOIN game_gender ON game_gender.game_id = games.id WHERE games.id = ?)");
				ps.setLong(1, id);
				rs2 = ps.executeQuery();
				List<Gender> genders = new ArrayList<>();
				
				
				while(rs2.next()) {
					String nameGender = rs2.getString("name");
					genders.add(Gender.valueOf(nameGender));
				}
				
				ps = conn.prepareStatement("SELECT plataforms.name FROM plataforms WHERE plataforms.id IN (SELECT plataform_id FROM games INNER JOIN game_plataform ON game_plataform.game_id = games.id WHERE games.id = ?)");
				ps.setLong(1, id);
				rs2 = ps.executeQuery();
				List<Plataform> plataforms = new ArrayList<>();
				
				
				while(rs2.next()) {
					String namePlataforms = rs2.getString("name");
					plataforms.add(Plataform.valueOf(namePlataforms));
				}
				
				games.add(new Game(id,name,genders,plataforms,note,status));
			}
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		
		return games;
	}

	@Override
	public Game findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Game game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

}
