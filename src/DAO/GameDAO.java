package DAO;

import java.util.List;

import entities.Game;

public interface GameDAO {

	public List<Game> findAll();
	
	public Game findById(Long id);
	
	public void insert();
	
	public void update(Game game);
	
	public void delete(Long id);
}
