package mailServerInterfaces;

import java.io.IOException;

import ServerFileSystem.Email;

public interface IUser {
    /**
     * @param directory the parent folder directory
     */
    public void setDir(String directory);
    /**
     * @return the user name
     */
    public String getName();
    /**
     * @return the user password
     */
    public String getPassword();
    /**
     * @param n sets the user name
     */
    public void setName(final String n);
    /**
     * @param p sets the user password
     */
    public void setPassword(final String p);
    /**
     * @throws IOException creates a new user with all it's folders and index files
     */
    public void addUser() throws IOException;
    /**
     * used to load the essential files of a user to the user object to be able to deal with it
     */
    public void login();
    /**
     * @param name filter name to compare with the subject of the incoming emails
     * @throws IOException for file handling
     */
    public boolean createNewFilter(final String name) throws IOException;
    /**
     * @param e email to receive
     */
    public void receiveEmail(Email e);
}
