import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class BigFileClient {
    private static int PORT = 9000;
    public static void main(String[] args) {
        try {
            var socket = new MulticastSocket(PORT);
            var address = InetAddress.getByName("225.1.1.1");
            socket.joinGroup(address);
            var file = new File("result");
            var fos = new FileOutputStream(file);
            while(true) {
                var firstByte = new byte[1];
                var firstPack = new DatagramPacket(firstByte,1);
                socket.receive(firstPack);
                var start = firstPack.getLength();
                var isComplete = false;
                while(start == 0) {
                    var inputByte = new byte[60000];
                    var pack = new DatagramPacket(inputByte,60000);
                    socket.receive(pack);
                    System.out.printf("Get File");
                    if(pack.getLength() == 0) {
                        isComplete = true;
                        break;
                    }
                    fos.write(pack.getData(),0,pack.getLength());
                }
                if(isComplete) {
                    break;
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
