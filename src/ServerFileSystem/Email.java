
/**
 *
 */
package ServerFileSystem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import mailServerInterfaces.IEmail;

/**
 * @author SHIKO
 *
 */
public class Email implements IEmail {
    /**
     * the priority of the email
     */
    String priority = null;
    /**
     * is the starred or not
     */
    String starred = null;
    /**
     * read or not
     */
    String read = null;
    /**
     * email subject
     */
    private String subject = null;
    /**
     * email auther
     */
    private String auther = null;
    /**
     * email destination
     */
    private String to = null;
    /**
     * email folder
     */
    private String folder = null;
    /**
     * email directory
     */
    private String dir = null;
    /**
     * email number in the folder
     */
    private String num = null;
    /**
     * email body
     */
    private String body = null;
    /**
     * number of attachments
     */
    private int attachments = 0;
    /**
     *
     */
    private String date = null;
    /**
     * email json object (will contain the whole email)
     */
    JSONObject email = null;

    public String getFrom() {
        return auther;
    }
    public boolean getRead() {
        return Boolean.valueOf(read);
    }
    public boolean getStarred() {
        return Boolean.valueOf(starred);

    }
    public String getPriority() {
        return priority;
    }
    public String getDate() {
        return date;
    }
    @Override
    public void setPriority(final String p) {
        priority = p;
    }
    @Override
    public void setRead(final boolean b) {
        read = String.valueOf(b);
    }
    @Override
    public void setStarred(final boolean b) {
        starred = String.valueOf(b);
    }
    @Override
    public void setSubject(final String s) {
        subject = s;
    }
    @Override
    public void setAuther(final String s) {
        auther = s;
    }
    @Override
    public void setTo(final String s) {
        to = s;
    }
    @Override
    public void setFolder(final String s) {
        folder = s;
    }
    @Override
    public void setDir(final String s) {
        dir = s;
    }
    @Override
    public void setAttachments(final int b) {
        attachments = b;
    }
    @Override
    public void setBody(final String b) {
        body = b;
    }
    @Override
    public void setEmailnum(final String n) {
        num = n;
    }
    @Override
    public String getSubject() {
        return subject;
    }
    @Override
    public void setEmail(JSONObject o) {
    	email = o;
    	setPrivateVariablesFromJSON();
    }
    @Override
    public String getTo() {
        return to;
    }


    @Override
    public void newEmail(final String sub,
            final String from,
            final String dis,
            final String b,
            final String prio) {
        Date d = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E, y-M-d 'at' h:m a");

        date = dateFormatter.format(d);

	    subject = sub;
	    auther = from;
	    to = dis;
	    body = b;
	    priority = prio;
	    email = new JSONObject();
	    email.put("sub", subject);
	    email.put("auther", auther);
	    email.put("to", to);
	    email.put("attachments", String.valueOf(attachments));
	    email.put("body", b);
	    email.put("date", date);
	    email.put("priority", priority);
	    email.put("starred", String.valueOf(false));
	    email.put("read", String.valueOf(false));


	}
    @Override
	public void addAttachment(final byte[] f, final String n) {
	    JSONArray a = toJSONArray(f);
	    attachments++;
	    email.put(String.valueOf(attachments) , n);
	    email.put(n, a);
	    email.put("attachments", String.valueOf(attachments));
	}
	@Override
	public void loadEmailtoJson() {
	    File f = new File(dir + "/" + num + ".json");
	    JSONParser p = new JSONParser();
	    try {
	    	FileReader fr = new FileReader(f);
            email = (JSONObject) p.parse(fr);
            fr.close();
            setPrivateVariablesFromJSON();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	@Override
	public byte[] toByteArray() {
		File f = new File(dir + "/" + num + ".json");
        byte[] mybytes = new byte[(int)f.length()];
		try {
			FileInputStream fis = new FileInputStream(f);
	        BufferedInputStream bis = new BufferedInputStream(fis);
	        bis.read(mybytes,0,mybytes.length);
	        bis.close();
	        fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return mybytes;
	}
	@Override
	public void StoreEmail() throws Exception {

	    File f = new File(dir + "/" + num + ".json");

	    f.createNewFile();


	    FileWriter file = new FileWriter(f);
        file.write(email.toString());
        
        file.flush();
        file.close();

	}
	/**
	 * the function is used to set the private variables from the json object
	 * usually will be called when loading the email from file
	 * or recieving the email
	 */
	private void setPrivateVariablesFromJSON() {
	    subject = (String) email.get("sub");
	    auther = (String) email.get("auther");
	    to = (String) email.get("to");
	    attachments = Integer.valueOf((String) email.get("attachments"));
	    body = (String) email.get("body");
	    date = (String) email.get("date");
	    priority = (String) email.get("priority");
	    starred = (String)email.get("starred");
	    read = (String) email.get("read");
	}
	/**
	 * @param f byte array
	 * @return JSON byte array
	 */
	private JSONArray toJSONArray(final byte[] f) {
	    JSONArray a = new JSONArray();
	    for (int i = 0; i < f.length; i++) {
	        a.add(String.valueOf(f[i]));
	    }
	    return a;
	}
	/**
     * the function is used to update the email file with the json data
     */
    public void updateEmailFile() {
        try {
            FileWriter file = new FileWriter(dir + "/" + num + ".json");
            file.write(email.toString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteEmail() {
    	File file = new File(dir+"/"+num+".json");


	        if(file.delete()){

           return true;
        }
        return false;
	}
    public boolean setPriorityy(String priority) {
    	email.put("priority", priority);
    	updateEmailFile();
    	return true ;
    }
    public boolean starOrUnstarEmail(boolean b) {
        if(b) {
            email.put("starred", "true");
        }else {
            email.put("starred", "false");
        }
        updateEmailFile();
        return true ;
        
    }
    public boolean markAsRead() {
        loadEmailtoJson();
        email.put("read", String.valueOf(true));
        updateEmailFile();
        return true;
    }
}