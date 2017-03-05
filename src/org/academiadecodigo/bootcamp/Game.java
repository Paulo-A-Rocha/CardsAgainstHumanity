package org.academiadecodigo.bootcamp;

import java.net.Socket;
import java.util.Iterator;

/**
 * Created by codecadet on 02/03/17.
 */
public class Game {
    private WhiteDeck whiteDeck;
    private BlackDeck blackDeck;
    private int[] sockets = new int[5];
    private Server server;
    int gameOver;


    public void start(int winsAt) {
        whiteDeck = new WhiteDeck();
        whiteDeck.makeDeck();
        blackDeck = new BlackDeck();
        blackDeck.makeDeck();
        gameOver = winsAt;
        server = new Server();
        server.start();

    }

    //metodos q vao acontecer durante a ronda(nao sei se os thread.sleeps funcionam!) FALTAM LA MERDAS PELO MEIO MAS TENHO
// DE METER ESTA MERDA A TRABALHAR PARA VER SE PERCEBO COMO AS RESOLVER***NAO VAI COMPILAR, ESTOU TAO FODIDO
    private void startRound() {
        String whoIsTheCzar = "";
        System.out.println("comecou o round");
        if (server.getMap().size() == 5) {
            //faz um deck

            System.out.println("Vou atribuir um czar");
            //escolhe o czar, cada vez q passa faz it.next e muda para o proximo
            Iterator<String> it = server.getMap().values().iterator();

            //envia mensagem para o player q vai ser o czar
            //TODO o cliente(FRED) tem de lidar com esta string
            whoIsTheCzar = it.next();
            server.sendToPlayer(">isCzar ", whoIsTheCzar);

            //manda 10 cartas brancas para todos
            for (Socket player : server.getMap().keySet()) {
                server.sendToPlayer(">white " + (whiteDeck.giveCard(10)), server.getMap().get(player));
            }

            //manda mais uma carta branca a cada um (a cada ronda, tb a contar com a primeira
            for (Socket player : server.getMap().keySet()) {
                server.sendToPlayer(">white " + (whiteDeck.giveCard(1)), server.getMap().get(player));
            }

            //manda a todos uma carta preta
                server.sendToAll(">black " + (blackDeck.giveBlackCard(1)));
                //Todo os clientes têm de apagar os comandos, fred!


            //esperar que todos os jogadores escolham a carta
            //nullpointer
            if (server.getTable().size() != 4) {
                try {
                    Thread.sleep(120000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //ver as escolhas dos jogadores
            for (String values : server.getMap().values()) {
                server.sendToAll(">white " + (server.getMap().values()));
            }

            //enviar as escolhas de cada jogador para o czar
            server.sendToPlayer(">white " + server.getTable().values(), whoIsTheCzar);

            //esperar que o czar faça a escolha
            //qnd o czar escolhe guardo essa carta, retiro da mesa e o jogo avança
            while (server.getTable().size() != 3) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            //TODO atribuir um ponto ao vencedor: papel e caneta acho q tenho de pegar na carta escolhida,
            //TODO ver de quem é e aumentar um ponto ao dono dela,-> solve that and... profit!!!

            //if(playerscore(ta mal!!) != gameOver){
            startRound();
        }

        //TODO enviar o winner aos players
    }


    /*
    -ver se estão 5 jogadores -> metodo do server DONE

    começar o jogo (metodo start):DONE
    ----atribuir um czar DONE
    -dar cartas aos jogadores, *ta no round, pareceu me melhor DONE

    -começar ronda:
    ----escolher uma carta preta do deck e enviar para todos,DONE
    ----bloquear a escolha de cartas do czar, ACHO Q TEM DE SER NO PLAYER
    esperar que todos os jogadores escolham uma carta, --MIGHT BE DONE
    ----ver as escolhas de todos os jogadores
    TODO----enviar as escolhas de cada jogador para o czar,
    TODO----esperar que o czar faça uma escolha,
    TODO----atribuir um ponto ao dono da carta escolhida,
    TODO----mostrar um score a todos os jogadores,


    ----fim do round.DONE
    -novo round até alguem ter um score de x,DONE
    TODO-mostrar a todos quem é o winner.
     */
    public static void main(String[] args) {
        Game g = new Game();
        g.start(1);
        System.out.println("Estou depois do start e antes do round");
        g.startRound();
    }

}
