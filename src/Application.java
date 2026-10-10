import java.util.List;

import DAO.GameDAO;
import DAO.GameDAOJDBC;
import entities.Game;
import enums.Gender;
import enums.Plataform;
import enums.Status;

public class Application {

	public static void main(String[] args) {

		GameDAO dao = new GameDAOJDBC();
		
		
		Game newGame = new Game(null, "Toy Story 2", List.of(Gender.ROYALE), List.of(Plataform.PLAYSTATION_3), "Muito bom jogo", Status.JOGANDO);
		
		dao.insert(newGame);
		
		
		System.out.println("--------------------------");
		
		List<Game> games = dao.findAll();
		
		for (Game g: games) {
			System.out.println(g);
		}
		
		System.out.println("--------------------------");
		
		Game gameForId = dao.findById(1L);
		System.out.println(gameForId);

	}

}
