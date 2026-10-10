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
		
		/*
		Game newGame = new Game(null, "Toy Story 2", List.of(Gender.ROYALE), List.of(Plataform.PLAYSTATION_3), "Muito bom jogo", Status.JOGANDO);
		
		dao.insert(newGame);
		*/
		
		Game newGame2 = new Game(null, "Toy Story Racer", List.of(Gender.ADVENTURE,Gender.RACING), List.of(Plataform.PLAYSTATION, Plataform.SEGA_SATURN), "Muito bom jogo", Status.JOGANDO);
		
		dao.update(1L,newGame2);
		
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
