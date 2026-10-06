import java.util.List;

import DAO.GameDAO;
import DAO.GameDAOJDBC;
import entities.Game;

public class Application {

	public static void main(String[] args) {

		GameDAO dao = new GameDAOJDBC();
		List<Game> games = dao.findAll();
		
		for (Game g: games) {
			System.out.println(g);
		}

	}

}
