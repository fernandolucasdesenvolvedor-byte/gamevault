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

			while (rs1.next()) {
				Long id = rs1.getLong("id");
				String name = rs1.getString("name");
				String note = rs1.getString("note");
				Status status = Status.valueOf(rs1.getString("status"));

				ps = conn.prepareStatement(
						"SELECT genders.name AS name FROM genders WHERE genders.id IN (SELECT gender_id FROM games INNER JOIN game_gender ON game_gender.game_id = games.id WHERE games.id = ?)");
				ps.setLong(1, id);
				rs2 = ps.executeQuery();
				List<Gender> genders = new ArrayList<>();

				while (rs2.next()) {
					String nameGender = rs2.getString("name");
					genders.add(Gender.valueOf(nameGender));
				}

				ps = conn.prepareStatement(
						"SELECT plataforms.name FROM plataforms WHERE plataforms.id IN (SELECT plataform_id FROM games INNER JOIN game_plataform ON game_plataform.game_id = games.id WHERE games.id = ?)");
				ps.setLong(1, id);
				rs2 = ps.executeQuery();
				List<Plataform> plataforms = new ArrayList<>();

				while (rs2.next()) {
					String namePlataforms = rs2.getString("name");
					plataforms.add(Plataform.valueOf(namePlataforms));
				}

				games.add(new Game(id, name, genders, plataforms, note, status));
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

		return games;
	}

	@Override
	public Game findById(Long id) {

		Connection conn = DB.getConnection();
		PreparedStatement ps = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		Game game = null;

		try {

			ps = conn.prepareStatement("SELECT * FROM games WHERE id = ?");
			ps.setLong(1, id);
			rs1 = ps.executeQuery();

			while (rs1.next()) {
				Long idGame = rs1.getLong("id");
				String nameGame = rs1.getString("name");
				String noteGame = rs1.getString("note");
				Status statusGame = Status.valueOf(rs1.getString("status"));

				ps = conn.prepareStatement(
						"SELECT genders.name FROM games INNER JOIN game_gender ON game_gender.game_id = games.id INNER JOIN genders ON genders.id = game_gender.gender_id WHERE games.id = ?");
				ps.setLong(1, id);
				rs2 = ps.executeQuery();
				List<Gender> genders = new ArrayList<>();

				while (rs2.next()) {
					Gender gender = Gender.valueOf(rs2.getString("name"));
					genders.add(gender);
				}

				ps = conn.prepareStatement(
						"SELECT plataforms.name FROM games INNER JOIN game_plataform ON game_plataform.game_id = games.id INNER JOIN plataforms ON plataforms.id = game_plataform.plataform_id WHERE games.id = ?");
				ps.setLong(1, id);
				rs2 = ps.executeQuery();
				List<Plataform> plataforms = new ArrayList<>();

				while (rs2.next()) {
					Plataform plataform = Plataform.valueOf(rs2.getString("name"));
					plataforms.add(plataform);
				}

				game = new Game(idGame, nameGame, genders, plataforms, noteGame, statusGame);
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

		return game;
	}

	@Override
	public void insert(Game game) {

		Connection conn = DB.getConnection();

		try {

			PreparedStatement ps = conn.prepareStatement("INSERT INTO games (name,note,status) VALUES (?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, game.getName());
			ps.setString(2, game.getNote());
			ps.setString(3, game.getStatus().toString());
			ps.executeUpdate();
			ResultSet rsDoID = ps.getGeneratedKeys();

			if(rsDoID.next()) {
				Long id = rsDoID.getLong(1);
				game.setId(id);
			}
			

			//////////////////////////////////////////////

			List<Gender> gendersAll = new ArrayList<>();
			List<Gender> genders = new ArrayList<>();
			List<Gender> gendersNotExist = new ArrayList<>();
			PreparedStatement ps2 = conn.prepareStatement("SELECT name FROM genders");
			ResultSet rs = ps2.executeQuery();

			while (rs.next()) {
				gendersAll.add(Gender.valueOf(rs.getString("name")));
			}

			gendersNotExist = game.getGenders().stream().filter(g -> !gendersAll.contains(g)).toList();

			List<Plataform> plataformsAll = new ArrayList<>();
			List<Plataform> plataforms = new ArrayList<>();
			List<Plataform> plataformsNotExist = new ArrayList<>();
			PreparedStatement ps3 = conn.prepareStatement("SELECT name FROM plataforms");
			rs = ps3.executeQuery();

			while (rs.next()) {
				plataformsAll.add(Plataform.valueOf(rs.getString("name")));
			}

			plataformsNotExist = game.getPlataforms().stream().filter(p -> !plataformsAll.contains(p)).toList();

			//////////////////////////////////////////////

			gendersNotExist.forEach(gn -> {
				try {
					PreparedStatement ps4 = conn.prepareStatement("INSERT INTO genders (name) VALUES (?)");
					ps4.setString(1, gn.name());
					ps4.executeUpdate();

				} catch (SQLException e) {
					throw new DbException(e.getMessage());
				}
			});

			List<Integer> idsGenders = new ArrayList<>();
			ps2 = conn.prepareStatement("SELECT name FROM genders");
			rs = ps2.executeQuery();
			gendersAll.clear();
			while(rs.next()) {
				gendersAll.add(Gender.valueOf(rs.getString("name")));
			}
			
			genders = game.getGenders().stream().filter(g -> gendersAll.contains(g)).toList();
			genders.forEach(g -> {
				try {
					PreparedStatement ps5 = conn.prepareStatement("SELECT * FROM genders WHERE name = ?");
					ps5.setString(1, g.name());
					ResultSet rs1 = ps5.executeQuery();

					while (rs1.next()) {
						idsGenders.add(rs1.getInt("id"));
					}

				} catch (SQLException e) {
					throw new DbException(e.getMessage());
				}
			});
			

			idsGenders.forEach(i -> {
				try {
					PreparedStatement ps6 = conn
							.prepareStatement("INSERT INTO game_gender (game_id, gender_id) VALUES (?,?)");
					ps6.setLong(1, game.getId());
					ps6.setInt(2, i);
					ps6.execute();

				} catch (SQLException e) {
					throw new DbException(e.getMessage());
				}
			});
			
			///////

			plataformsNotExist.forEach(pn -> {
				try {
					PreparedStatement ps4 = conn.prepareStatement("INSERT INTO plataforms (name) VALUES (?)");
					ps4.setString(1, pn.name());
					ps4.executeUpdate();

				} catch (SQLException e) {
					throw new DbException(e.getMessage());
				}
			});

			List<Integer> idsPlataforms = new ArrayList<>();
			ps2 = conn.prepareStatement("SELECT name FROM plataforms");
			rs = ps2.executeQuery();
			plataformsAll.clear();
			
			while (rs.next()) {
				plataformsAll.add(Plataform.valueOf(rs.getString("name")));
			}
			
			
			plataforms = game.getPlataforms().stream().filter(p -> plataformsAll.contains(p)).toList();
			plataforms.forEach(p -> {
				try {
					PreparedStatement ps5 = conn.prepareStatement("SELECT * FROM plataforms WHERE name = ?");
					ps5.setString(1, p.name());
					ResultSet rs1 = ps5.executeQuery();

					while (rs1.next()) {
						idsPlataforms.add(rs1.getInt("id"));
					}

				} catch (SQLException e) {
					throw new DbException(e.getMessage());
				}
			});

			idsPlataforms.forEach(i -> {
				try {
					PreparedStatement ps6 = conn
							.prepareStatement("INSERT INTO game_plataform (game_id, plataform_id) VALUES (?,?)");
					ps6.setLong(1, game.getId());
					ps6.setInt(2, i);
					ps6.execute();

				} catch (SQLException e) {
					throw new DbException(e.getMessage());
				}
			});
			
			///

		} catch (SQLException e) {
			System.out.println("Foi Aqui");
			e.printStackTrace();
			throw new DbException(e.getMessage());
		}

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
