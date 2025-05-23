package hr.tvz.arydia.server.server;

import hr.tvz.arydia.server.model.CharacterType;
import hr.tvz.arydia.server.model.GameState;
import hr.tvz.arydia.server.model.OpenWorld;
import hr.tvz.arydia.server.model.Player;
import hr.tvz.arydia.server.thread.GetLastGameStateThread;
import hr.tvz.arydia.server.thread.SaveNewGameStateThread;
import hr.tvz.arydia.server.util.WorldGenerationUtil;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {

    private ServerSocket serverSocket;
    private int numberOfPlayers;
    public static final int PORT = 1989;
    private static final GameState gameState = new GameState();
    private ClientHandler playerOneHandler;
    private ClientHandler playerTwoHandler;

    public GameServer(){
        numberOfPlayers = 0;
        try{
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
        }
        catch (IOException e){
            System.out.println("Error starting server: " + e.getMessage());
        }
    }

    public void acceptConnections(){
        boolean accepting = true;
        while (accepting){
            try{
                while(numberOfPlayers < 2){
                    Socket clientSocket = serverSocket.accept();
                    numberOfPlayers++;
                    //new Thread(new ClientHandler(clientSocket, gameState, numberOfPlayers)).start();
                    ClientHandler handler = new ClientHandler(clientSocket, gameState, numberOfPlayers);
                    if (numberOfPlayers == 1) {
                        playerOneHandler = handler;
                    } else {
                        playerTwoHandler = handler;
                    }
                    new Thread(handler).start();
                }
            }
            catch (IOException e){
                accepting = false;
                System.out.println("Error accepting connection: " + e.getMessage());
            }
        }
    }

    //lakse mi je da dijelim neke varijable ovako za ios i oos nego da ih stalno otvaram i zatvaram
    //a i bolje je jer se javi AC greska
    private class ClientHandler implements Runnable{
        private final Socket clientSocket;
        private GameState gameState;
        private final int playerNumber;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        boolean initalBroadcast = true;

        public ClientHandler(Socket clientSocket, GameState gameState, int playerNumber){
            this.clientSocket = clientSocket;
            this.gameState = gameState;
            this.playerNumber = playerNumber;

            try{
                 // Initialize input and output streams for the client
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.flush(); // Important!
                ois = new ObjectInputStream(clientSocket.getInputStream());
            }
            catch (IOException e){
                System.out.println("Error initializing streams: " + e.getMessage());
            }
        }


        //TODO prilagoditi .equals da se ne šalje kad ne treba, možda čak bi trebalo i recreate napraviti prije usporedbe!
        @Override
        public void run() {
            try{
                System.out.println("Player " + playerNumber + " connected.");
                String firstContact = (String) ois.readObject();
                String gameStateChoice = firstContact.split(",")[0];
                String playerName = firstContact.split(",")[1];


                if ("new".equalsIgnoreCase(gameStateChoice)) {
                    newWorldInThread(playerName);
//                    oos.writeObject(gameState);
//                    oos.flush();
                    broadcastGameState();
                } else if ("load".equalsIgnoreCase(gameStateChoice)) {
                    if (new File("gameState/gameState.dat").exists()) {
                        GetLastGameStateThread getLastGameStateThread = new GetLastGameStateThread();
                        Thread starter = new Thread(getLastGameStateThread);
                        starter.start();
                        gameState = getLastGameStateThread.getLastGameState();
                        gameState.getPlayers().get(playerNumber-1).setName(playerName); //u slučaju da promijeni ime, ostalo mu je sve isto
                        System.out.println("Player " + playerNumber + " loaded an existing game.");
                    } else {
                        newWorldInThread(playerName);
                        System.out.println("No saved game found. A new game has been created.");
                    }
                } else {
                    oos.writeObject("Invalid choice. Disconnecting...");
                    oos.flush();
                    clientSocket.close();
                    return;
                }



                while (!clientSocket.isClosed()) {
//                    System.out.println("trying to read object1111");
//                    gameState = (GameState) ois.readObject();
//                    //GameState newGameState = (GameState) ois.readObject();
//                    System.out.println("trying to read object2222");
//
//                    //gameState = newGameState;
//                    broadcastGameState();
                    try {
                        Object receivedObject = ois.readObject();
                        if (receivedObject instanceof GameState newGameState) {
                            gameState = newGameState;
                            System.out.println("Received game state: " + gameState);
                            broadcastGameState();
                        } else {
                            System.out.println("Received unknown object: " + receivedObject);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Error receiving data from client: " + e.getMessage());
                    }


                }
            }
            catch (IOException | ClassNotFoundException e){
                System.out.println("Error handling client: " + e);
            }
        }

        private void newWorldInThread(String playerName){
            OpenWorld openWorld = WorldGenerationUtil.generateOpenWorld();
            Player newPlayer = Player.getNewPlayer(playerName, playerNumber);
            gameState.setOpenWorld(openWorld);
            gameState.addPlayer(newPlayer);
            gameState.setPlayerTurn(CharacterType.PLAYER_ONE);

            SaveNewGameStateThread saveNewGameStateThread = new SaveNewGameStateThread(gameState);
            Thread starter = new Thread(saveNewGameStateThread);
            starter.start();
        }
        private int broadcastCounter = 0;
        private void broadcastGameState() {
//            if(initalBroadcast) {
//                System.out.println("first broadcast nothing happens");
//                initalBroadcast = false;
//                return;
//            }
            try {
                broadcastCounter++;
                System.out.println("Broadcast counter: " + broadcastCounter);
                if (broadcastCounter == 3) {
                    gameState.changePlayerTurn();
                    broadcastCounter = 0;
                }
                if (playerOneHandler != null) {
                    System.out.println("Broadcasting to player one");
                    playerOneHandler.oos.writeObject(gameState);
                    playerOneHandler.oos.flush();
                }
                if (playerTwoHandler != null) {
                    System.out.println("Broadcasting to player two");
                    playerTwoHandler.oos.writeObject(gameState);
                    playerTwoHandler.oos.flush();
                }
            } catch (IOException e) {
                System.out.println("Error broadcasting game state: " + e.getMessage());
            }
        }
    }

    //TODO sljedeće bi trebalo napraviti da je while petlja i onda već mislim da bi mogao prijeći na klijenta koji će morati upisati ime i reći što želi, prije toga nema spajanja! --done
    //TODO vidjeti jel možda treba imena threadova da ih znamo razlikovati iako mislim da ne treba --- DONE napravljena 2 client handlera
    //TODO KATASTROFA, NE PUNI SE DOBRO STATE!!!! PRAZNI SU CONTAINERI, TREBA VIDJETI PUNJENJE --- DONE dobro se pune ali ne mogu se serijalizirati tako da se mora napraviti čitanje
    //TODO povezati da klijen zna tko je on! player 1 ili player 2 --- done
    //TODO napraviti kad se primi novi game state da se broadcasta! --dobro se BC-a za sad ali divlja jedno triggera drugo u krug, dobro se broadcasta ali ne kad se spoji igrač novi!

    public static void main(String[] args){
        GameServer gameServer = new GameServer();
        gameServer.acceptConnections();
    }

}
