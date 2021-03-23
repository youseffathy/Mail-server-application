package mailServerInterfaces;

import java.io.IOException;

import ServerFileSystem.Email;

public interface IEmailFolder {
    /**
     * @param n folder's name
     */
    public void setName(String n);
    /**
     * @param directory folder's parent directory
     */
    public void setDir(String directory);
    /**
     * @throws IOException for files handling
     * the function creates the folder from the begining
     * it also creates the index.json file inside it
     */
    public void createFolders() throws IOException;
    /**
     * @return the number of the emails in a folder
     */
    public int getEmailsNum();
    /**
     * @param e email to receive
     * @return true or false received of failed
     */
    public int receiveEmail (Email e);
}
