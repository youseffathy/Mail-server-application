package ServerFileSystem;


public class test {

	public static void main(String[] args) throws Exception {
		ServerFolder server = new ServerFolder();
		server.startServer("server");
		Email e = new Email();


		/*
		 * String s = new String(server.loadEmail("yousef", "inbox", "1"));
		 * System.out.println(s);
		 *
		 */


	/*	System.out.println(server.loadUserIndex("yousef"));
		System.out.println(server.loadfolderindex("yousef", "Inbox"));
	*/

		/*
		 * System.out.println(server.loadUserIndex("yousef"));
		 * System.out.println(server.loadfolderindex("yousef", "Inbox"));
		 */



		  User u = new User(); u.setDir("server"); u.setName("yousef");
		  u.setPassword("1234"); server.addUser(u); User u2 = new User();
		  u2.setDir("server"); u2.setName("salah"); u2.setPassword("1234");
		  server.addUser(u2); User u3 = new User(); u3.setDir("server");
		  u3.setName("hekma"); u3.setPassword("12345"); server.addUser(u3);

		  e.newEmail("t1", "salah", "yousef", "y1","0");

		  server.recieveFile(e); e.newEmail("t2", "salah", "yousef", "y2","0");
		  server.recieveFile(e); e.newEmail("t3", "salah", "yousef", "y3","0");




		  server.recieveFile(e);

		  server.createFilter("yousef", "ggggggg");
		  server.createFilter("yousef", "sssssss");




		  server.moveEmail("yousef","Inbox", "Trash", "1");


		  //server.deleteFolder("yousef", "ggggggg");
		  server.starEmail("yousef", "Inbox", "1");



		  /*server.starEmail("yousef", "Inbox", "1");
		 *
		 *
		 * server.starEmail("yousef", "Inbox", "2");
		 *
		 *
		 * server.starEmail("yousef", "Inbox", "3");
		 * System.out.println(server.renameFolder("yousef", "ggggggg",
		 * "al a7a a7a a7a")); System.out.println(server.renameFolder("yousef",
		 * "al a7a a7a a7a", "al"));
		 *
		 * server.removeStarEmail("yousef", "Inbox", "2");
		 */

		/* User u = new User();
		  u.setDir("server");
		   u.setName("yousef");
		  u.setPassword("1234");
		   server.addUser(u);
		   User u2 = new User();
		  u2.setDir("server");
		   u2.setName("salah");
		    u2.setPassword("1234");
		  server.addUser(u2);
		   User u3 = new User();
		   u3.setDir("server");
		  u3.setName("hekma");
		   u3.setPassword("12345");
		   server.addUser(u3);*/
		  e.newEmail("t1", "salah", "yousef", "y1","0");
		  server.recieveFile(e);
		  e.newEmail("t2", "salah", "yousef", "y2","0");
		  server.recieveFile(e);
		  e.newEmail("t3", "salah", "yousef", "y3","0");
		  server.recieveFile(e);
		//  server.createFilter("yousef", "ggggggg");
	//	  server.createFilter("yousef", "sssssss");
		//  server.moveEmail("yousef","Inbox", "Trash", "1");
		//  server.starEmail("yousef", "Inbox", "1");
	//	  server.starEmail("yousef", "Inbox", "1");
		//  server.starEmail("yousef", "Inbox", "2");
		//server.emptyTrash("yousef");

		//  System.out.println(server.renameFolder("yousef", "ggggggg", "al a7a a7a a7a"));
		//  System.out.println(server.renameFolder("yousef", "al a7a a7a a7a", "al"));
	}

}