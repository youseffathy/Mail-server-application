package serverClient;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.StringTokenizer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ServerFileSystem.Email;
import ServerFileSystem.ServerFolder;
import ServerFileSystem.User;
import dataStructure.DoubleLinkedList;


/**
 * @author Muhammad Salah
 *
 */
public class Request{
    JSONObject req = null;
    JSONObject response = null;
    JSONParser parser = new JSONParser();
    DataInputStream dis = null;
    DataOutputStream dos = null;
    DoubleLinkedList users = null;
    Socket skt = null;
    String name = null;
    public void setName(String name) {
        this.name = name;
    }
    public void setskt(Socket s) {
        skt = s;
    }
    public void setUsers(DoubleLinkedList u) {
        users = u;
    }
    public Request(DataInputStream di, DataOutputStream d) {
        dis = di;
        dos = d;
    }
    /******************************* client side Functions *********************************/
    public String reqRegister(String name, String pass) {
        req = new JSONObject();
        req.put("name", name);
        req.put("password", pass);
        req.put("reqNum", "1");
        return req.toString();
    }
    public String loginClient(String name, String pass) {
        req = new JSONObject();
        req.put("name", name);
        req.put("password", pass);
        req.put("reqNum", "2");
        return req.toString();
    }
    public String reqClose() {
        req = new JSONObject();
        req.put("reqNum", "0");
        return req.toString();
    }
    public boolean loginResponse(String s) throws ParseException {
    	req = (JSONObject) parser.parse(s);
    	return (boolean)req.get("res");

    }

    public String getFoldersReq (String name ) {
        req = new JSONObject();
        req.put("name", name);

        req.put("reqNum", "3");
        return req.toString();
    }

    public String createFolderReq(String userName, String folderName) {
        req = new JSONObject();
        req.put("name", userName);
        req.put("folderName", folderName);
        req.put("reqNum", "4");
        return req.toString();
    }
    public String deleteFolderReq(String userName, String folderName) {
        req = new JSONObject();
        req.put("reqNum", "6");
        req.put("userName", userName);
        req.put("folderName", folderName);
        return req.toString();
    }
    public boolean deleteFolderRes(String res) throws ParseException {
        req = (JSONObject) parser.parse(res);
        return (boolean)req.get("res");
    }
    public String getEmailsInFolderRequest (String name , String foldername) {
        req = new JSONObject();
        req.put("name", name);
        req.put("folderName", foldername);
        req.put("reqNum", "5");
        return req.toString();
    }
    public JSONObject EmailsResponse(String s) throws ParseException {
        JSONParser p = new JSONParser();
        return (JSONObject) p.parse(s);
    }

    public JSONObject foldersResponse(String s) throws ParseException {
        JSONParser p = new JSONParser();
        return (JSONObject) p.parse(s);
    }
    public String starUnstarEmailReq(String star,String userName, String folderName, String emailnum) {
    	req = new JSONObject();
    	 req.put("name", userName);
    	 req.put("starred", star);
         req.put("folderName", folderName);
         req.put("emailnum",emailnum);
         req.put("reqNum", "8");
         return req.toString();
    }
    public String renameFolderReq(String userName, String folderName, String newFolder) {
        req = new JSONObject();
        req.put("name", userName);
        req.put("folderName", folderName);
        req.put("newFolder", newFolder);
        req.put("reqNum", "7");
        return req.toString();
    }
    public String newFolderReq(String userName, String folderName) {
        req = new JSONObject();
        req.put("name", userName);
        req.put("folderName", folderName);
        req.put("reqNum", "7");
        return req.toString();
    }
    public String setPriorityReq(String priority,String userName, String folderName, String emailnum) {
    	req = new JSONObject();
   	 	req.put("name", userName);
    	req.put("folderName", folderName);
        req.put("emailnum",emailnum);
        req.put("priority", priority);
        req.put("reqNum", "10");
        return req.toString();
	}
    public boolean newFolderRes(String res) throws ParseException {
        req = (JSONObject) parser.parse(res);
        return (boolean)req.get("res");
    }
    public boolean renameFolderRes(String res) throws ParseException {
        req = (JSONObject) parser.parse(res);
        return (boolean)req.get("res");
    }
    public boolean starUnstarEmailRes(String res) throws ParseException {
        req = (JSONObject) parser.parse(res);
        return (boolean)req.get("res");
    }
    public boolean setPriorityRes(String res) throws ParseException {
        req = (JSONObject) parser.parse(res);
        return (boolean)req.get("res");
    }
    public String sendEmailReq(String to, String sub, String body, String userName, String attachNum) {
        req = new JSONObject();
        req.put("reqNum", "11");
        req.put("to", to);
        req.put("sub", sub);
        req.put("body", body);
        req.put("name", userName);
        req.put("attach", attachNum);
        return req.toString();
    }
    public String laodEmailreq(String username , String foldername , String emailnum) {
    	req = new JSONObject();
        req.put("reqNum", "12");
        req.put("name", username);
        req.put("folderName", foldername);
        req.put("emailNum",emailnum );
        return req.toString();
    }
    public String SendAttachReq(File f) throws Exception {
        byte [] mybytearray  = new byte [(int)f.length()];
        FileInputStream fis = new FileInputStream(f);
        BufferedInputStream bis = new BufferedInputStream(fis);
        bis.read(mybytearray,0,mybytearray.length);
        String s = new String(mybytearray);
        return s;
    }
    public String markAllAsReadReq(String username , String foldername) {
        req = new JSONObject();
        req.put("reqNum", "13");
        req.put("name", username);
        req.put("folderName", foldername);
        return req.toString();
    }
    public String DeleteEmailReq(String username , String foldername, String emailNum) {
        req = new JSONObject();
        req.put("reqNum", "14");
        req.put("name", username);
        req.put("folderName", foldername);
        req.put("emailNum", emailNum);
        return req.toString();
    }
    public String moveEmailReq(String username , String foldername,String folderTo, String emailNum) {
        req = new JSONObject();
        req.put("reqNum", "15");
        req.put("name", username);
        req.put("folderName", foldername);
        req.put("emailNum", emailNum);
        req.put("folderTo",folderTo);
        return req.toString();
    }
    public String getContactsReq(String userName) {
        req = new JSONObject();
        req.put("reqNum", "16");
        req.put("name", userName);
        return req.toString();
    }
    public JSONObject getContactsRes(String res) {
        JSONObject contacts = new JSONObject();
        try {
            contacts = (JSONObject) parser.parse(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contacts;
    }
    public String newContactReq(String userName, String contact) {
        req = new JSONObject();
        req.put("reqNum", "17");
        req.put("name", userName);
        req.put("contact", contact);
        return req.toString();
    }
    public String reportSpamReq(String userName, String folderName, String emailNum) {
        req = new JSONObject();
        req.put("reqNum", "18");
        req.put("name", userName);
        req.put("folderName", folderName);
        req.put("emailNum", emailNum);
        return req.toString();
    }
    public String removeContactReq(String userName, String contact) {
        req = new JSONObject();
        req.put("reqNum", "19");
        req.put("name", userName);
        req.put("contact", contact);
        return req.toString();
    }

    /******************************* Server side Functions *********************************/
    public void recieveReq(String r) throws ParseException {
        if(r == "exit") {
            return;
        }
        req = (JSONObject) parser.parse(r);
    }
    /**
     * this  function checks  the request number and the user name
     * and password of user and  set response to true if the user name and
     * password matches
     * @param s
     * @return
     * @throws Exception
     */
    public String response() throws Exception {
    	int reqnum = Integer.valueOf((String) req.get("reqNum"));
    	switch (reqnum) {
		case 1:
			return register();
		case 2:
			return login();
		case 3:
		    String x = loaduserdata();
			return x;
		case 4:
		    return createNewFolder();
		case 5:
		    return loadFoldersIndex();
		case 6:
		    return deleteFolder();
		case 7:
		    return renameFolder();
		case 8:
			return starunstarEmail();
		case 9:
		    return newFolder();
		case 10:
			return setPriority();
		case 11:
		    return recieveEmail();
		case 12:
			return returnEmail();
		case 13:
		    return markAllEmailsRead();
		case 14:
		    return DeleteEmail();
		case 15:
		    return moveEmail();
		case 16:
		    return getContacts();
		case 17:
		    return newContact();
		case 18:
		    return reportSpam();
		case 19:
			return removeContact();
		default:
			break;
		}
    	return null;
    }

	private  String loaduserdata() throws Exception {
		ServerFolder server = new ServerFolder();
		server.startServer("server");
		server.emptyTrash((String)req.get("name"));
		return (server.loadUserIndex((String)req.get("name"))).toString();
	}


    private String register() throws Exception {
    	ServerFolder server = new ServerFolder();
		server.startServer("server");
		User u = new User();
		u.setName((String)req.get("name"));
		u.setPassword((String)req.get("password"));

		response = new JSONObject();
		response.put("res", server.addUser(u));
		return response.toString();
    }
    private String login() throws Exception {
    		ServerFolder server = new ServerFolder();
    		server.startServer("server");
    		User u = new User();
    		u.setName((String)req.get("name"));
    		u.setPassword((String)req.get("password"));
    		server.emptyTrash((String)req.get("name"));
    		response = new JSONObject();
    		response.put("res", server.login(u));
    		return response.toString();


	}
    private String createNewFolder() {
        boolean b = false;
        try {
            ServerFolder server = new ServerFolder();
            server.startServer("server");
            String userName = (String) req.get("name");
            String folderName = (String) req.get("folderName");
            server.emptyTrash((String)req.get("name"));
            b = server.createFilter(userName, folderName);
        } catch(Exception e) {
            b = false;
        }
        response = new JSONObject();
        response.put("res", b);
        return response.toString();
    }
    public String[] getRegReq() {
        String[] s = {(String) req.get("name") , (String) req.get("password")};
        return s;
    }
    private String loadFoldersIndex() throws Exception {
        ServerFolder server = new ServerFolder();
        server.startServer("server");
        server.emptyTrash((String)req.get("name"));
        return (server.loadfolderindex((String)req.get("name"),(String)req.get("folderName"))).toString();
    }
    private String deleteFolder() throws Exception {
        boolean b = false;
        try {
            String fname = (String) req.get("folderName");
            String userName = (String) req.get("userName");
            ServerFolder server = new ServerFolder();
            server.startServer("server");
            server.emptyTrash((String)req.get("name"));
            b = server.deleteFolder(userName, fname);
        } catch(Exception e) {
            b = false;
        }
        response = new JSONObject();
        response.put("res", b);
        return response.toString();
    }
    private String renameFolder() {
        boolean b = false;
        try {
            String fname = (String) req.get("folderName");
            String userName = (String) req.get("name");
            String newFolder = (String) req.get("newFolder");
            ServerFolder server = new ServerFolder();
            server.startServer("server");
            server.emptyTrash((String)req.get("name"));
            b = server.renameFolder(userName, fname, newFolder);
        } catch(Exception e) {
            b = false;
        }
        response = new JSONObject();
        response.put("res", b);

        return response.toString();
    }
    private String starunstarEmail() {
    	boolean  b = false;
    	try {
    		 String fname = (String) req.get("folderName");
             String userName = (String) req.get("name");
             String eNum = (String) req.get("emailnum");
             String star = (String) req.get("starred");
             ServerFolder server = new ServerFolder();
             server.startServer("server");

             server.emptyTrash((String)req.get("name"));
             if(star.equals("false")) {
            	 b = server.starEmail(userName, fname, eNum);

             } else {
            	 b = server.removeStarEmail(userName, fname, eNum);
             }

		} catch (Exception e) {
			b = false;
		}
    	response = new JSONObject();
    	response.put("res", b);
        return response.toString();

    }
    private String newFolder() {
        boolean b = false;
        try {
            String folderName = (String) req.get("folderName");
            String userName = (String) req.get("name");
            ServerFolder server = new ServerFolder();
            server.startServer("server");
            server.emptyTrash((String)req.get("name"));
            server.createFilter(userName, folderName);
            b = true;
        } catch (Exception e) {
            b = false;
        }
        response = new JSONObject();
        response.put("res", b);
        return response.toString();
    }
    private String setPriority() {
    	boolean b = false;
    	try {
    		String fname = (String) req.get("folderName");
            String userName = (String) req.get("name");
            String eNum = (String) req.get("emailnum");
            String priority =  (String) req.get("priority");
            ServerFolder server = new ServerFolder();
            server.startServer("server");
            server.emptyTrash((String)req.get("name"));
            b = server.setPriority(userName, fname, eNum, priority);
            if(!b) {
            	throw new Exception();
            }
		} catch (Exception e) {

		}
    	response = new JSONObject();
        response.put("res", b);
        return response.toString();
	}
    private String recieveEmail() {
        try {
            String to = (String) req.get("to");
            String userName = (String) req.get("name");
            String sub = (String) req.get("sub");
            String body =  (String) req.get("body");
            int attach = Integer.valueOf((String) req.get("attach"));
            Email e = new Email();
            e.newEmail(sub, userName, to, body, "3");
            dos.writeUTF("okay");
            for(int i = 0; i < attach; i++) {
                String fileName = dis.readUTF();

                String size = dis.readUTF();
                int filesize = Integer.valueOf(size);
                byte[] file = new byte[filesize];

                if(filesize>0) {
                	dis.readFully(file, 0, filesize);
                    e.addAttachment(file, fileName);
                } else {
                	throw new Exception();
                }

            }
            ServerFolder server = new ServerFolder();
            server.startServer("server");
            server.emptyTrash((String)req.get("name"));
            boolean bool = server.recieveFile(e);
            response = new JSONObject();
            response.put("res", bool);
            return response.toString();
        } catch (Exception e) {
            boolean bool = false;
            response = new JSONObject();
            response.put("res", bool);
            return response.toString();
        }
    }
    private String returnEmail() {
    	try {
    		String fname = (String) req.get("folderName");
            String userName = (String) req.get("name");
            String eNum = (String) req.get("emailNum");
            ServerFolder server = new ServerFolder();
            server.startServer("server");
            server.emptyTrash((String)req.get("name"));
            Email e = server.loadEmail(userName, fname, eNum);
            byte[] mybytes = e.toByteArray();
            dos.writeUTF(String.valueOf(mybytes.length));
            dos.write(mybytes);
            response = new JSONObject();
            response.put("res",true);
            return response.toString();
		} catch (Exception e) {
			boolean bool = false;
            response = new JSONObject();
            response.put("res", bool);
            return response.toString();
		}


    }
    private String markAllEmailsRead() {
        try {
            String fname = (String) req.get("folderName");
            String userName = (String) req.get("name");
            ServerFolder server = new ServerFolder();
            server.startServer("server");
            server.emptyTrash((String)req.get("name"));
            server.markAllEmailsRead(userName, fname);
            response = new JSONObject();
            response.put("res",true);
            return response.toString();
        } catch (Exception e) {
            boolean bool = false;
            response = new JSONObject();
            response.put("res", bool);
            return response.toString();
        }
    }
    private String DeleteEmail() {
        try {
            String fname = (String) req.get("folderName");
            String userName = (String) req.get("name");
            String emailNum = (String) req.get("emailNum");
            ServerFolder server = new ServerFolder();
            server.startServer("server");
            server.emptyTrash((String)req.get("name"));
            boolean bool = server.deleteEmail(userName, fname, emailNum);
            if(!bool) {
                throw new Exception();
            } else {
                response = new JSONObject();
                response.put("res", bool);
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new JSONObject();
            response.put("res", false);
            return response.toString();
        }

    }
    private String moveEmail() {
        try {
            String fname = (String) req.get("folderName");
            String userName = (String) req.get("name");
            String emailNum = (String) req.get("emailNum");
            String folderTo = (String) req.get("folderTo");
            ServerFolder server = new ServerFolder();
            server.startServer("server");
            server.emptyTrash((String)req.get("name"));
            boolean bool = server.moveEmail(userName, fname, folderTo, emailNum);
            if(!bool) {
                throw new Exception();
            } else {
                response = new JSONObject();
                response.put("res", bool);
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new JSONObject();
            response.put("res", false);
            return response.toString();
        }

    }
    private String getContacts() {
        try {
            String userName = (String) req.get("name");;
            ServerFolder server = new ServerFolder();
            server.startServer("server");
            server.emptyTrash((String)req.get("name"));
            String res = server.getContacts(userName);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private String newContact() {
        try {
            String userName = (String) req.get("name");
            String contactName = (String) req.get("contact");
            ServerFolder server = new ServerFolder();
            server.startServer("server");
            server.emptyTrash((String)req.get("name"));
            boolean bool = server.addContact(userName, contactName);
            if(!bool) {
                throw new Exception();
            } else {
                response = new JSONObject();
                response.put("res", bool);
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new JSONObject();
            response.put("res", false);
            return response.toString();
        }
    }
    private String reportSpam() {
        try {
            String userName = (String) req.get("name");
            String folderName = (String) req.get("folderName");
            String emailNum = (String) req.get("emailNum");
            ServerFolder server = new ServerFolder();
            server.startServer("server");
            server.emptyTrash((String)req.get("name"));
            boolean bool = server.reportSpam(userName, folderName, emailNum);
            if(!bool) {
                throw new Exception();
            } else {
                response = new JSONObject();
                response.put("res", bool);
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new JSONObject();
            response.put("res", false);
            return response.toString();
        }
    }
    private String removeContact() {
    	try {
            String userName = (String) req.get("name");
            String contactName = (String) req.get("contact");
            ServerFolder server = new ServerFolder();
            server.startServer("server");
            server.emptyTrash((String)req.get("name"));
            boolean bool = server.removeContact(userName, contactName);
            if(!bool) {
                throw new Exception();
            } else {
                response = new JSONObject();
                response.put("res", bool);
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new JSONObject();
            response.put("res", false);
            return response.toString();
        }
	}
    private String startChat() {
        while (true) {
            try {
                String received = dis.readUTF();
                System.out.println(received);
                if(received.equals("logout")){
                    break;
                }
                // break the string into message and recipient part
                StringTokenizer st = new StringTokenizer(received, "#");
                String MsgToSend = st.nextToken();
                String recipient = st.nextToken();
                // search for the recipient in the connected devices list.
                // ar is the vector storing client of active users
                for (int i = 0; i < users.size(); i++) {
                    ClientHandler1 mc = (ClientHandler1) users.get(i);
                    if (mc.name.equals(recipient) && mc.isLoggedin==true) {
                        mc.dos.writeUTF(this.name+" : "+MsgToSend);
                        break;
                    }
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return "Done";
    }
}
