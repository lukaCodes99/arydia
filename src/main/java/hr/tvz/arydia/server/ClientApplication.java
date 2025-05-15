package hr.tvz.arydia.server;

import hr.tvz.arydia.server.model.CharacterType;
import hr.tvz.arydia.server.model.GameState;
import hr.tvz.arydia.server.model.OpenWorld;
import hr.tvz.arydia.server.model.Player;
import hr.tvz.arydia.server.thread.PlayerThread;
import hr.tvz.arydia.server.util.WorldGenerationUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientApplication extends Application {

    public static GameState gameState;
    public static String playerName;
    public static String gameChoice;
    private CharacterType whoAmI; //možda će trebat biti statički ali ćemo vidjeti
    private static Stage primaryStage;
//TODO obavetno napraviti close everyhing
    //TODO sada treba napraviti ekran koji će prije spajanja imati 2 gumba i poslati jedan brzinski usernme i hoće li novu ili staru igru
    //nakon toga server nam posluži igru i idemo dalje, video je https://www.youtube.com/watch?v=gLfuZrrfKes otprijlike 35. minuta
    //izgleda da bi to mogao u main controlleru ali vidjet cemo, možda bolje napraviti jedan kontroler prije njega!




    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("welcome-screen.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 800);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void startGame(String choice, String name) throws IOException {
        gameChoice = choice;
        playerName = name;
        Socket socket = new Socket("localhost", 1989);
        PlayerThread playerThread = new PlayerThread(socket, choice, name);
        playerThread.connectToServer();
    }

    public static void main(String[] args) throws IOException {
        //OpenWorld openWorld = WorldGenerationUtil.generateOpenWorld();
        //List<Player> players = new ArrayList<>();
        //gameState = new GameState(players, openWorld, CharacterType.PLAYER_ONE);
//        Socket socket = new Socket("localhost", 1989);
//        PlayerThread playerThread = new PlayerThread(socket);
//        playerThread.connectToServer();

        launch();
    }

    public void setWhoAmI(List<Player> players) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                this.whoAmI = player.getPlayerType();
                break;
            }
        }
    }
    public CharacterType getWhoAmI() {
        return whoAmI;
    }

    public static void closeWelcomeScreen() {
        if (primaryStage != null) {
            primaryStage.close();
        }
    }
    public String getPlayerName() {
        return playerName;
    }
}