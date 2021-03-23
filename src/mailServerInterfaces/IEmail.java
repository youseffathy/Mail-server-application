package mailServerInterfaces;

import org.json.simple.JSONObject;

/**
 * @author Muhammad Salah
 *
 */
public interface IEmail {
    /**
     * @param p priority of the email
     */

    public void setPriority(final String p);
    /**
     * @return the subject of the email
     */
    public String getSubject();
    /**
     * @param b read or not yet
     */
    public void setRead(final boolean b);
    /**
     * @param b starred or not
     */
    public void setStarred(final boolean b);
    /**
     * @param f file read as byte array
     * the function adds the byte array of the attachment to the JSON object.
     * the function increases the attachments variable in the JSON Object
     */
    public void addAttachment(final byte[] f, final String n);
    /**
     * the function reads the email.json file and parse it into the json object email
     */
    public void loadEmailtoJson();
    /**
     * @return the email json object as a byte array
     * the function converst the whole email to a byte array.
     * which will help us to send it over sockets
     */
    public byte[] toByteArray();
    /**
     * @throws Exception for file handling
     * the function stores the email json object in the .json file located in the folder containing the email
     */
    public void StoreEmail() throws Exception;
    /**
     * @param sub subject of the email
     * @param from sender
     * @param dis destination 
     * @param b the text body of the email
     */
    public void newEmail(final String sub,
            final String from,
            final String dis,
            final String b,
            final String prio);
    /**
     * @param n the email number
     * Emails will be stored in the folder by numbers
     * to avoid conflicts between to emails with the same subject
     */
    public void setEmailnum(final String n);
    /**
     * @param b the email text body
     */
    public void setBody(final String b);
    /**
     * @param b number of attachments in the email
     */
    public void setAttachments(final int b);
    /**
     * @param s the directory to store the email (parent directory)
     */
    public void setDir(final String s);
    /**
     * @param s the folder the email stored in
     */
    public void setFolder(final String s);
    /**
     * @param s the destination
     */
    public void setTo(final String s);
    /**
     * @param s the auther
     */
    public void setAuther(final String s);
    /**
     * @param s subject
     */
    public void setSubject(final String s);
    /**
     * @param o JSON Object
     */
    public void setEmail(JSONObject o);
    /**
     * @return To destination of the email
     */
    public String getTo();
}
