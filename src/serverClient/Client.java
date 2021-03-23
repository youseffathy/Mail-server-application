package serverClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private static String Ip = null;
	String username = null;

	public void setIp(String s) {
		Ip = s;
	}

	public void setUsername(String s) {
		username = s;
	}

	public static void main(String[] args) throws IOException {

		try
        {
            Scanner scn = new Scanner(System.in);

            // getting localhost ip
            InetAddress ip = InetAddress.getByName("192.168.43.109");

            // establish the connection with server port 9999
            System.out.println(ip);

            Socket s = new Socket(ip, 5051);

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            Request req = new Request(null, null);


            // the following loop performs the exchange of
            // information between client and client handler
        /*    while (true)
            {

                String tosend = scn.nextLine();

                if(tosend.equals("1")) {
                	String name = scn.nextLine();
                    String password = scn.nextLine();
                    String arr = req.reqRegister(name, password);
                    dos.writeUTF(arr);

                }
                if(tosend.equals("0")) {
                    System.out.println("55555");
                    String arr = req.reqClose();
                    System.out.println(new String(arr));
                    dos.writeUTF(arr);
                    System.out.println("ssss");
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }


                */






                // printing date or time as requested by client
                String received = dis.readUTF();

                System.out.println(received);
           /* }*/

            // closing resources
            scn.close();
            dis.close();
            dos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
	}

}
