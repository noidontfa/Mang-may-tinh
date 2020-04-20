import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class BigFileServer {
    private static int PORT = 9000;
    public static void main(String[] args) {
        try {
            var socket = new DatagramSocket();
            var address = InetAddress.getByName("225.1.1.1");
            var sc = new Scanner(System.in);
            System.out.printf("Nhap Ten FIle: ");
            var filename = sc.nextLine();

            while(true) {
                var file = new File(filename);
                var fileLength = (int)file.length();
                var fis = new FileInputStream(file);
                var fileByte = new byte[fileLength];
                fis.read(fileByte);

                var firstByte = new byte[0];
                var firstPack = new DatagramPacket(firstByte,0 , address, PORT);
                socket.send(firstPack);
                System.out.println("Goi dau tien");
                var fileAmount = fileLength / 60000;
                if(fileLength % 60000 != 0 )
                    fileAmount++;
                var midByte = new byte[60000];
                for(var i =0; i < fileAmount -1; i++) {
                    for(var j = 0; j< 60000; j++) {
                        midByte[j] = fileByte[i * 60000 + j];
                    }
                    var midPack = new DatagramPacket(midByte,60000 , address, PORT);
                    socket.send(midPack);
                    System.out.println("Goi thu" + (i + 1));
                }
                var endByte = new byte[60000];
                var endAmount = fileLength - (fileAmount - 1) * 60000;
                for (var y = 0; y < endAmount; y++) {
                    endByte[y] = fileByte[(fileAmount - 1) * 60000 + y];
                }

                var endPack = new DatagramPacket(endByte,60000 , address, PORT);
                socket.send(endPack);
                System.out.println("Goi cuoi");

                Thread.sleep(1000);


            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }
}
