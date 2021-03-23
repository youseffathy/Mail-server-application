package ServerFileSystem;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import dataStructure.linkedList;
import mailServerInterfaces.IServerFolder;

/**
 * @author Muhammad Salah
 *
 */
public class ServerFolder implements IServerFolder {
	/**
	 * index file
	 */
	private File indexFile = null;
	/**
	 * server directory/name
	 */
	private String name = null;
	/**
	 * index json object {"usersNumber", 2} {1, "name1 : password1"} {2, "name2
	 * : Password2"} so we need to split the user's data by " : "
	 */
	private JSONObject index = null;

	@Override
	public void startServer(final String dir) throws Exception {
		name = dir;
		File f = new File(dir);
		if (!f.exists()) {
			f.mkdirs();
			indexFile = new File(f, "index.json");
			indexFile.createNewFile();
			setIndexFile();
		} else {
			indexFile = new File(f, "index.json");
			if (!indexFile.exists()) {
				indexFile.createNewFile();
				setIndexFile();
			} else {
				loadIndexFile();
			}
		}
	}

	@Override
	public int getUsersNumber() {

			return Integer.valueOf((String) index.get("usersNumber"));


	}

	@Override
	public String[] getUsersInfo() {
		int n = getUsersNumber();
		String[] users = new String[n];
		for (int i = 0; i < n; i++) {
			users[i] = (String) index.get(String.valueOf(i + 1));
		}

		return users;
	}

	/**
	 * sets the index file for the first time
	 */
	
	private void setIndexFile() {
		index = new JSONObject();
		index.put("usersNumber", String.valueOf(0));
		// z3lana 3shan haga tanya
		updateIndex();
	}

	/**
	 * loads the index file to the json object index.
	 */
	private void loadIndexFile() {
		JSONParser p = new JSONParser();
		try {
			FileReader fr = new FileReader(indexFile);
			index = (JSONObject) p.parse(fr);
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 */
	@Override
	public boolean addUser(User u) throws IOException {
		String n = u.getName();
		String p = u.getPassword();
		if (existUser(n)) {
			return false;
		}
		String user = n + " : " + p;
		int num = getUsersNumber();
		num++;
		index.put(String.valueOf(num), user);
		// z3lana 3shan haga tanya
		index.put("usersNumber",String.valueOf(num));
		// z3lana 3shan haga tanya
		u.setDir(name);
		u.addUser();
		updateIndex();
		return true;
	}
	@Override
	public boolean moveEmail(String username,
	                         String folderFrom,
	                         String folderTo,
	                         String emailnum) throws Exception {
	    User u1 = new User();
        u1.setName(username);
        u1.setDir(name);
        u1.loadIndexFile();
        return u1.moveEmail(folderFrom, folderTo, emailnum);
	}
	private void updateIndex() {
		try {
			FileWriter file = new FileWriter(indexFile);
			file.write(index.toString());
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean existUser(String n) {
		String[] users = getUsersInfo();
		for (int i = 0; i < users.length; i++) {
			String x = users[i].split(" : ")[0];
			if (n.equals(x)) {
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean login(User u) {
		String n = u.getName();
		String p = u.getPassword();
		return index.containsValue(n + " : " + p);

	}
	@Override
	public boolean recieveFile(Email e) throws Exception {
		if (existUser(e.getTo())) {
			User u = new User();
			u.setName(e.getTo()); // set the user the email is sent to
			u.setDir(name); // set the folder name of the sever
			u.receiveEmail(e); // set the email in the user inbox folder and
			User sender = new User();
			sender.setName(e.getFrom());
			sender.setDir(name);
			sender.addSent(e);
			return true;
		} else {
			return false;
		}
	}

	
	@Override
	public JSONObject loadUserIndex(String n) {
		User u1 = new User();
		u1.setName(n);
		u1.setDir(name);
		u1.loadIndexFile();
		JSONObject userfoldersData = u1.getIndex();
		return userfoldersData;
	}
	@Override
	public JSONObject loadfolderindex(String username, String foldername) {
		User u1 = new User();
		u1.setName(username);
		u1.setDir(name);
		u1.loadIndexFile();
		return u1.loadfolderindex(foldername);

	}
	@Override
	public Email loadEmail(String username, String foldername,String emailnum) {
		User u1 = new User();
		u1.setName(username);
		u1.setDir(name);
		u1.loadIndexFile();
		return u1.loadEmail(foldername, emailnum);

	}
	@Override
	public boolean deleteEmail(String username, String foldername,String emailnum) throws Exception {

		return moveEmail(username, foldername, "Trash", emailnum);

	}

	@Override
	public boolean deleteFolder(String userName, String folderName) throws Exception {
	    User u = new User();
	    u.setDir(name);
	    u.setName(userName);
	    return u.deleteFolder(folderName);
	}
	@Override
	public boolean createFilter(String userName, String filterName) {
	    User u = new User();
	    u.setName(userName);
	    u.setDir(name);	    return u.createNewFilter(filterName);

	}

	@Override
	public boolean emptyTrash(String username) throws Exception {
		User u1 = new User();
		u1.setName(username);
		u1.setDir(name);
		u1.loadIndexFile();
		return u1.emptyTrash();
	}
	@Override
	public boolean starEmail(String username, String foldername,String emailnum) {
		User u1 = new User();
		u1.setName(username);
		u1.setDir(name);
		u1.loadIndexFile();
		return u1.starEmail(foldername,emailnum);
	}
	@Override
	public boolean removeStarEmail(String username, String foldername,String emailnum) {
		User u1 = new User();
		u1.setName(username);
		u1.setDir(name);
		u1.loadIndexFile();
		return u1.removeStarEmail(foldername,emailnum);
	}
	@Override
	public boolean renameFolder(String username ,String foldername , String toFoldername) {
		User u1 = new User();
		u1.setName(username);
		u1.setDir(name);
		u1.loadIndexFile();
		return u1.renameFolder(foldername , toFoldername);


	}
	@Override
	public boolean setPriority(String username, String foldername, String emailNum, String priority) {
		User u1 = new User();
		u1.setName(username);
		u1.setDir(name);
		u1.loadIndexFile();
		return u1.setPriority(foldername, emailNum, priority);

	}
	@Override
	public JSONObject search(String username , String searchNum , String toSearch) {
		User u1 = new User();
		u1.setName(username);
		u1.setDir(name);
		u1.loadIndexFile();
		linkedList s = new linkedList();
		s = u1.search(searchNum ,toSearch);
		if(s.isEmpty()) {
			return null ;
		}
		JSONObject o = new JSONObject();
		o.put("emailNum",String.valueOf(s.size()));
		for(int i = 1; i <= s.size(); i++) {
			o.put(String.valueOf(i), s.get(i-1));
		}
		return o ;
	}
	@Override
	public boolean markEmailRead(String userName, String folderName, String emailNum) {
		User u = new User();
		u.setName(userName);
		u.setDir(name);
		u.loadIndexFile();
		return u.markEmailRead(folderName, emailNum);
	}
	@Override
	public boolean markAllEmailsRead (String userName,String folderName) {
	    User u = new User();
        u.setName(userName);
        u.setDir(name);
        u.loadIndexFile();
       return u.markAllEmailsRead(folderName);
	}
	@Override
	public String getContacts(String userName) {
	    User u = new User();
        u.setName(userName);
        u.setDir(name);
        return u.getContacts();
	}
	@Override
	public boolean addContact(String userName, String contact) {
	    User u = new User();
        u.setName(userName);
        u.setDir(name);
        u.addContact(contact);
        return true;
	}
	@Override
	public boolean removeContact(String userName, String contact) {
	    User u = new User();
        u.setName(userName);
        u.setDir(name);
        u.removeContact(contact);
        return true;
	}
	@Override
	public boolean reportSpam(String userName, String folderName, String emailNum) {
	    User u = new User();
        u.setName(userName);
        u.setDir(name);
        u.reportSpam(folderName, emailNum);
        return true;
	}
}
