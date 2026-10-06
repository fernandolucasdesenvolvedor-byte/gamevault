import java.util.List;

import entities.Game;
import enums.Gender;
import enums.Plataform;
import enums.Status;

public class Application {

	public static void main(String[] args) {
		Game game = new Game(1L,"Game 01",List.of(Gender.ACTION,Gender.ADVENTURE),List.of(Plataform.NINTENDO),"Isso é Bom",Status.ABANDONADO);
		System.out.println(game);

	}

}
