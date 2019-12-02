package NetworkPart.Broadcast;


import NetworkPart.GlobalController;
import me.ippolitov.fit.snakes.SnakesProto;

import java.io.IOException;
import java.net.*;
import java.util.List;

public class MulticastController extends Thread {
    private final int multicastPort = 9192;
    private InetAddress multicastAddress;

    private MulticastSocket multicastSocket;

    public MulticastController(){
        try {
            multicastAddress =  InetAddress.getByName("239.192.0.4");
            multicastSocket = new MulticastSocket(multicastPort);
            multicastSocket.setInterface(GlobalController.getInstance().getIP());
            multicastSocket.setSoTimeout(1000);
            multicastSocket.joinGroup(multicastAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        byte[] buf = new byte[4096];
        DatagramPacket dp = new DatagramPacket(buf,buf.length);
        SnakesProto.GameMessage.AnnouncementMsg message;
        System.out.println("**Сокет начал работу**");
        while(true){
            try {
                multicastSocket.receive(dp);
                message = SnakesProto.GameMessage.AnnouncementMsg.parseFrom(dp.getData());
                System.out.println(message.getCanJoin());
                SnakesProto.GameConfig config = message.getConfig();
                System.out.println("ширина поля: " + config.getWidth());
                System.out.println("высота поля: " + config.getHeight());
                System.out.println("статичное количество еды: " + config.getFoodStatic());
                System.out.println("количество еды на каждого игрока :" + config.getFoodPerPlayer());
                System.out.println("задержка: " + config.getDelayMs());
                System.out.println("вероятность: " + config.getDeadFoodProb());
                //где еще 2???
                SnakesProto.GamePlayers g;
                g = message.getPlayers();
                List<SnakesProto.GamePlayer> list = g.getPlayersList();
                for(int i = 0; i < list.size(); i++){
                    System.out.println("Имя игрока" + (i+1) +" "+ list.get(i).getName());
                }
            }catch (SocketTimeoutException e){

            }
            catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("------------------------");
        }
    }
}
