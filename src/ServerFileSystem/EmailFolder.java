package ServerFileSystem;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import dataStructure.linkedList;
import mailServerInterfaces.IEmailFolder;

/**
 * @author SHIKO
 *
 */
public class EmailFolder implements IEmailFolder {
    /**
     * the directory of the parent
     */
    private String dir = null;
    /**
     * the index file that contains the emails for now it only stores the number
     * of the emails but in needs to be modified to contain the data like:
     * {"emails number", 1} {1, "Subject : from : date : read : priority :
     * starred "}
     */
    private JSONObject index = null;
    /**
     * folder name
     */
    private String name = null;

    public JSONObject getIndex() {
        return index;
    }

    @Override
    public void setName(String n) {
        name = n;
    }

    @Override
    public void setDir(String directory) {
        dir = directory;
    }

    @Override
    public void createFolders() throws IOException {

        File f = new File(dir + "/" + name);
        f.mkdirs();
        File x = new File(f, "index.json");
        x.createNewFile();
        createIndex();

    }

    /**
     * creates the index file that contains email
     */
    private void createIndex() {
        index = new JSONObject();
        index.put("EmailsNum", String.valueOf(0));
        try {
            FileWriter file =
                    new FileWriter(dir + "/" + name + "/" + "index.json");
            file.write(index.toString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getEmailsNum() {

        return Integer.valueOf((String) index.get("EmailsNum"));

    }
    @Override
    public int receiveEmail(Email e) {
        int n = addEmailToIndex(e); // number of existing emails in the folders
        e.setDir(dir + "/" + name);
        /*
         * set the directory of the email folder which will receive the email
         */
        e.setFolder(name); // set the name of the folder of the email
        e.setEmailnum(String.valueOf(n));

        try {

            e.StoreEmail();
            /*
             * email name and the email contents in serverfolder class
             */
            updateIndex();
            /*
             * save the index file of the email folder after updating
             */
            return n;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return n;
    }
    /**
     * updates the index file with the data in the json index object
     */
    private void updateIndex() {
        try {
            FileWriter file =
                    new FileWriter(dir + "/" + name + "/" + "index.json");
            file.write(index.toString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * this method store the data of the email in the emailfolder index
     * (subject-from-to-read-priority-starred)
     *
     * @param e
     *            received email
     * @return email number
     */
    private int addEmailToIndex(Email e) {
        String s = e.getSubject() + " : " + e.getFrom() + " : " + e.getDate()
                + " : " + e.getRead() + " : " + e.getPriority() + " : "
                + e.getStarred();
        loadIndexFile(); // load the index file
        int n = getEmailsNum(); // get the number of existing emails previosly
        n++; // increment of numbers of emails in the folder
        index.put("EmailsNum", String.valueOf(n)); // update the number of
                                                   // emails in the folder
        index.put(String.valueOf(n), s);
        return n;
    }

    /**
     * loads the index json object from the index.json file
     */
    public void loadIndexFile() {
        JSONParser p = new JSONParser();
        try {
            FileReader fr =
                    new FileReader(dir + "/" + name + "/" + "index.json");
            index = (JSONObject) p.parse(fr);
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param emailnum
     * @return
     */
    public Email loadEmail(String emailnum) {
        loadIndexFile();
        markRead(emailnum);
        Email email = new Email();
        email.setEmailnum(emailnum);
        email.setDir(dir + "/" + name);
        email.loadEmailtoJson();
        updateIndex();
        return email;

    }
    public Email loadEmailForMove(String emailnum) {
        loadIndexFile();
        Email email = new Email();
        email.setEmailnum(emailnum);
        email.setDir(dir + "/" + name);
        email.loadEmailtoJson();
        updateIndex();
        return email;

    }
    public boolean deleteEmail(String emailnum) throws Exception {
        loadIndexFile();
        Email email = new Email();
        email.setEmailnum(emailnum);
        email.setDir(dir + "/" + name);
        boolean deleted = email.deleteEmail();
        updateEmailNum(emailnum);
        updateIndex();
        return deleted;
    }

    private void updateEmailNum(String emailnum) throws Exception {
        int n = getEmailsNum();
        for (int i = Integer.valueOf(emailnum); i < n; i++) {
            Email e = new Email();
            e.setDir(dir + "/" + name);
            e.setEmailnum(String.valueOf(i + 1));
            e.loadEmailtoJson();
            e.setEmailnum(String.valueOf(i));
            e.StoreEmail();
            index.put(String.valueOf(i), index.get(String.valueOf(i + 1)));
        }
        index.remove(String.valueOf(n));
        Email e1 = new Email();
        e1.setEmailnum(String.valueOf(n));
        e1.setDir(dir + "/" + name);
        e1.deleteEmail();
        index.put("EmailsNum", String.valueOf(n - 1));
        updateIndex();
    }

    public int emptyTrash() throws Exception {
        loadIndexFile();
        Date d = new Date();
        SimpleDateFormat dateFormatter =
                new SimpleDateFormat("E, y-M-d 'at' h:m a");
        String date = dateFormatter.format(d);
        String currentDate = String.valueOf(d.getTime());
        int n = getEmailsNum();
        int num = 0;
        if (n > 0) {
            long x = 259200000;// 30 day milliseconds 259200000
            for (int i = 0; i < n; i++) {
                String[] temp = ((String) index.get(String.valueOf(i + 1)))
                        .split(" : ");
                DateFormat format = new SimpleDateFormat("E, y-M-d 'at' h:m a",
                        Locale.ENGLISH);
                Date date1 = format.parse(temp[2]);
                String emailDate = String.valueOf(date1.getTime());
                if ((Long.valueOf(currentDate) / 10
                        - Long.valueOf(emailDate) / 10) >= x) {
                    deleteEmail(String.valueOf(i + 1));
                    n--;
                    i--;
                    num++;
                }
            }

            return n;
        }
        return n;

    }

    // used to star email
    public String GetEmailFromIndex(String emailnum, boolean x) {
        if (x) {
            Email e = new Email();
            e.setDir(dir + "/" + name);
            e.setEmailnum(emailnum);
            e.loadEmailtoJson();
            e.starOrUnstarEmail(true);          
            String s = splitAndChange((String) index.get(emailnum),
                    String.valueOf(true), 5, " : ");
            index.put(emailnum, s);
        } else {
            Email e = new Email();
            e.setDir(dir + "/" + name);
            e.setEmailnum(emailnum);
            e.loadEmailtoJson();
            e.starOrUnstarEmail(false);          
            String s = splitAndChange((String) index.get(emailnum),
                    String.valueOf(false), 5, " : ");
            index.put(emailnum, s);
        }
        updateIndex();
        String s = (String) index.get(emailnum);

        return s;

    }

    public boolean setStar(String s, String emailnum) {
        int n = getEmailsNum();
        n++;
        index.put("EmailsNum", String.valueOf(n));
        index.put(String.valueOf(n), s);
        updateIndex();
        return true;
    }

    public boolean removeStar(String s1, String emailnum) {
        String s = splitAndChange(s1, String.valueOf(true), 7, " : ");

        int key = 0;
        int i = 0;
        int n = getEmailsNum();
        for (i = 1; i <= n; i++) {
            if (s.equals((index.get(String.valueOf(i))))) {
                n--;
                key = i;
                index.remove(String.valueOf(i));
                index.put("EmailsNum", String.valueOf(n));
                break;
            }
        }
        for (i = key; i <= n; i++) {
            String temp = (String) index.get(String.valueOf(i + 1));
            index.put(String.valueOf(i), temp);
        }
        index.remove(String.valueOf(n + 1));
        updateIndex();

        return true;
    }

    public boolean deleteFolder() {
        File file = new File(dir + "/" + name);
        String[] entries = file.list();
        for (String s : entries) {
            File currentFile = new File(file.getPath(), s);
            currentFile.delete();
        }
        return file.delete();
    }

    public boolean setPriority(String emailNum, String priority) {
        String s = splitAndChange((String) index.get(emailNum), priority, 4,
                " : ");
        index.put(emailNum, s);
        updateIndex();
        Email e = new Email();
        e.setDir(dir + "/" + name);
        e.setEmailnum(emailNum);
        e.loadEmailtoJson();

        return e.setPriorityy(priority);
    }

    private String splitAndChange(String s, String change, int index,
            String regex) {
        String[] arr = s.split(regex);
        arr[index] = change;
        String s1 = "";
        for (int i = 0; i < arr.length; i++) {
            s1 += arr[i];
            if (i < arr.length - 1) {
                s1 += " : ";
            }
        }
        return s1;

    }

    public linkedList search(String searchNum, String toSearch) {
        int n = getEmailsNum();
        linkedList s = new linkedList();
        for (int i = 1; i <= n; i++) {
           
            String[] arr = ((String) index.get(String.valueOf(i))).split(" : ");

            if (arr[Integer.valueOf(searchNum)].equals(toSearch)) {
                String e = name + " : " + Integer.valueOf(i) + " : " + arr[0]
                        + " : " + arr[1] + " : " + arr[2] + " : " + arr[3]
                        + " : " + arr[4] + " : " + arr[5];
                s.add(e);
            }
        }
        return s;
    }

    private void markRead(String eNum) {
        String e = (String) index.get(eNum);
        String[] v = e.split(" : ");
        v[3] = String.valueOf(true);
        StringBuilder ss = new StringBuilder();
        ss.append(v[0]);
        for (int count = 1; count < v.length; count++) {
            ss.append(" : ");
            ss.append(v[count]);
        }
        index.put(eNum, ss.toString());
        Email email = new Email();
        email.setEmailnum(eNum);
        email.setDir(dir + "/" + name);
        email.markAsRead();
    }

    public boolean markEmailRead(String emailNum) {
        markRead(emailNum);
        updateIndex();
        return true;
    }

    public boolean markAllEmailsRead() {
        int n = getEmailsNum();
        for (int i = 1; i <= n; i++) {
            markEmailRead(String.valueOf(i));
        }
        return true;
    }
}
