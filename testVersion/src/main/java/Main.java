import NetworkPart.GlobalController;
import me.ippolitov.fit.snakes.SnakesProto;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Main {

    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(5555, GlobalController.getInstance().getIP());

        SnakesProto.GamePlayer.Builder gmB = SnakesProto.GamePlayer.newBuilder();
        gmB.setName("ВАСИЛИЙ");
        gmB.setId(1);
        gmB.setIpAddress(GlobalController.getInstance().getIP().toString());
        gmB.setPort(5555);
        gmB.setScore(4);
        gmB.setRole(SnakesProto.NodeRole.NORMAL);

        SnakesProto.GamePlayer player = gmB.build();

        SnakesProto.GameConfig.Builder gcB = SnakesProto.GameConfig.newBuilder();
        gcB.setWidth(10);
        gcB.setHeight(20);
        gcB.setFoodStatic(1);
        gcB.setFoodPerPlayer(2);
        gcB.setPingDelayMs(1000);
        gcB.setDeadFoodProb((float) 0.3);

        SnakesProto.GameConfig config = gcB.build();

        SnakesProto.GameMessage.AnnouncementMsg.Builder amB = SnakesProto.GameMessage.AnnouncementMsg.newBuilder();

        SnakesProto.GamePlayers.Builder gpB = SnakesProto.GamePlayers.newBuilder();
        gpB.addPlayers(player);

        amB.setPlayers(gpB.build());
        amB.setConfig(config);
        amB.setCanJoin(true);

        SnakesProto.GameMessage.AnnouncementMsg message = amB.build();

        SnakesProto.GameMessage.Builder gm = SnakesProto.GameMessage.newBuilder();

        System.out.println(message.hasConfig());
        System.out.println(message.hasPlayers());
        System.out.println(message.hasCanJoin());

        gm.setMsgSeq(1);
       // gm.setSenderId(1);
       // gm.setReceiverId(2);

        gm.setAnnouncement(amB);

        SnakesProto.GameMessage msg = gm.build();


        InetAddress inetAddress = InetAddress.getByName("239.192.0.4");
        DatagramPacket dp = new DatagramPacket(msg.toByteArray(), msg.toByteArray().length,inetAddress,9192);

       // System.out.println(Arrays.toString(dp.getData()));
       // System.out.println(dp.getLength());

        SnakesProto.GameMessage.parseFrom(msg.toByteArray());
        while(true){
            socket.send(dp);
            System.out.println("--отправили--");
            Thread.sleep(1000);
        }
    }

}
