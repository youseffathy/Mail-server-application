package mailServerInterfaces;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ServerFileSystem.Email;
import ServerFileSystem.User;
import dataStructure.linkedList;

/**
 * @author Abdelrahman Youssef
 *
 */
/**
 * @author Abdelrahman Youssef
 *
 */
/**
 * @author Abdelrahman Youssef
 *
 */
/**
 * @author Abdelrahman Youssef
 *
 */
public interface IServerFolder {
    /**
     * @param dir the directory to create or open the server ends with the server name
     * @throws Exception IOException for file handling
     * the function creates the server if it doesn't exist and calls one of the two functions:
     *      setIndexFile: which sets the index file of the server which contains the users data
     *                    it creates the file for the first time
     *      loadIndexFile: the function reads the index file when the server starts
     *                     and add it to the JSON object index.
     */
    public void startServer(final String dir) throws Exception;
    /**
     * @return number of users on th;e server
     */
    public int getUsersNumber();
    /**
     * @return a string array containing the users in format : 'name : password'
     */
    public String[] getUsersInfo();
    /**
     * @param u User type object to be added to the server
     * @return boolean added successfuly or not
     * @throws IOException
     */
    public boolean addUser(User u) throws IOException;
    /**
     * create class of User with username 
     * @param username u 
     * @param folderFrom f
     * @param folderTo ft
     * @param emailnum en
     * @return moveEmail in User class
     * @throws Exception
     */
	public boolean moveEmail(String username,
	                         String folderFrom,
	                         String folderTo,
	                         String emailnum) throws Exception ;
	/**
	 * check if user logging is exsit or not .
	 * get name and password of user from User class .
	 * @param User class u .
	 * @return true if it is already exist , else return false .
	 */
	public boolean login(User u) ;
	/**
	 * recaive an email to a user.
	 * @param Email class e .
	 * @return true if user exist , else return false .
	 * @throws Exception .
	 */
	public boolean recieveFile(Email e) throws Exception ;
	/**
	 * load jason object of user .
	 * @param n username .
	 * @return JsonObject of user .
	 */
	public JSONObject loadUserIndex(String n);
	/**
	 * return jason object of folder in a user .
	 * @param username u .	
	 * @param foldername f .
	 * @return JsonObject of folder of user
	 */
	public JSONObject loadfolderindex(String username, String foldername);
	/**
	 * create User class with name username .
	 * @param username u .
	 * @param foldername f.
	 * @param emailnum e.
	 * @return load email method of load email .
	 */
	public Email loadEmail(String username, String foldername,String emailnum);
	/**
	 * call move method and move email from foldername to trash .
	 * @param username u .
	 * @param foldername f .
	 * @param emailnum e .
	 * @return moveEmail method from foldername to trash .
	 * @throws Exception .
	 */
	public boolean deleteEmail(String username, String foldername,String emailnum) throws Exception ;
	/**
	
	 * @param userName u .
	 * @param folderName f .
	 * @return deleteFolder method in user class .
	 * @throws Exception
	 */
	public boolean deleteFolder(String userName, String folderName) throws Exception ;

	/**
	 * create User class with username .
	 * @param userName u .
	 * @param filterName f .
	 * @return
	 */
	public boolean createFilter(String userName, String filterName);
	/**
	 * create User class with username .
	 * @param username u
	 * @return emtyTrash method in USer class .
	 * @throws Exception
	 */
	public boolean emptyTrash(String username) throws Exception;
	/**
	 * create User class with username .
	 * @param username u .
	 * @param foldername f .
	 * @param emailnum e .
	 * @return starEmail method in user .
	 */
	public boolean starEmail(String username, String foldername,String emailnum);
	/**
	* create User class with username .
	 * @param username u .
	 * @param foldername f .
	 * @param emailnum e .
	 * @return unStarEmail method in user .
	 */
	public boolean removeStarEmail(String username, String foldername,String emailnum);
	/**
	 * create User class with username .
	 * @param username u .
	 * @param foldername f .
	 * @param toFoldername ft .
	 * @return renameFolder method in user .
	 */
	public boolean renameFolder(String username ,String foldername , String toFoldername);
	/**
	 * create User class with username . 
	 * @param username fu .
	 * @param foldername f .
	 * @param emailNum e .
	 * @param priority p .
	* @return setPriority method in user .
	 */
	public boolean setPriority(String username, String foldername, String emailNum, String priority);
	/**
	 * create User class with username .
	 * search in emails folr to search .
	 * @param username u .
	 * @param searchNum s1 .
	 * @param toSearch s2 .
	 * @return JSEON Object with eamils found .
	 */
	public JSONObject search(String username , String searchNum , String toSearch) ;
	/**
  	 *create User class with username .
 	 * @param userName
	 * @param folderName
	 * @param emailNum
	 * @return markEmailRead method in user .
	 */
	public boolean markEmailRead(String userName, String folderName, String emailNum);
	/**
	 *create User class with username .
	 * @param userName u .
	 * @param folderName f .
	  * @return markAllEmailsRead method in user .
	 */
	public boolean markAllEmailsRead (String userName,String folderName);
	/**
	 *create User class with username . 
	 * @param userName u .
	 * @return getContacts method in user .
	 */
	public String getContacts(String userName) ;
	/**
	 *create User class with username . 
	 * @param userName
	 * @param contact
	 * @return addContact method in user .
	 */
	public boolean addContact(String userName, String contact);
	/**
	 *create User class with username . 
	 * @param userName
	 * @param contact
	 * @return rmoveContactaddContact method in user .
	 */
	public boolean removeContact(String userName, String contact);
	/**
	 *create User class with username . 
	 *@param userName
	 * @param folderName
	 * @param emailNum
	  * @return reportSpam method in user .
	 */
	public boolean reportSpam(String userName, String folderName, String emailNum);
    
}
