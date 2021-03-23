package ServerFileSystem;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import dataStructure.DoubleLinkedList;
import dataStructure.linkedList;
import mailServerInterfaces.IUser;

/**
 * @author Muhammad Salah
 *
 */
public class User implements IUser {
    private String[] folders =
            { "Inbox", "Trash", "Sent", "Drafts", "Spam", "Stared" };
    /**
     * user name
     */
    private String name = null;
    /**
     * user passwrod
     */
    private String password = null;
    /**
     * directory of the parent folder
     */
    private String dir = null;
    /**
     * index json object the index json object needs to be modified to hold the
     * data in the form: {foldersNum : 0} {1: "foldername" , "foldername" :
     * "emailsNum"} for now it doesn't have the number of email option
     */
    private JSONObject index = null;
    /**
     * filters json object it contains the data as: {"filtersNum", 1} {1,
     * "folderName"} new filters added with the function createNewFilter
     */
    private JSONObject filters = null;
    private DoubleLinkedList contacts = null;

    public JSONObject getIndex() {
        return index;

    }

    @Override
    public void setDir(String directory) {
        dir = directory;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setName(final String n) {
        name = n;
    }

    @Override
    public void setPassword(final String p) {
        password = p;
    }

    @Override
    public void addUser() throws IOException {
        File f = new File(dir + "/" + name);
        f.mkdirs();
        File x = new File(f, "index.json");
        x.createNewFile();
        File filters = new File(dir + "/" + name);
        f.mkdirs();
        File y = new File(f, "filters.json");
        y.createNewFile();
        File d = new File(dir + "/" + name);
        d.mkdirs();
        File g = new File(f, "contacts.json");
        x.createNewFile();
        createIndex();
        createEssentialFolders();

    }

    @Override
    public void login() {
        loadIndexFile();
    }

    @Override
    public boolean createNewFilter(final String FilterName) {
        try {
            loadFiltersFile();
            loadIndexFile();
            if (filters.containsValue(FilterName)) {
                return false;
            }
            int filtersnum =
                    Integer.valueOf((String) filters.get("filtersnum"));
            filtersnum++;
            filters.put("filtersnum", String.valueOf(filtersnum));
            filters.put(String.valueOf(filtersnum), FilterName);
            filters.put(FilterName, String.valueOf(0));
            EmailFolder emailfolder = new EmailFolder();
            emailfolder.setName(FilterName);
            emailfolder.setDir(dir + "/" + name);
            emailfolder.createFolders();
            int foldersnum = Integer.valueOf((String) index.get("foldersNum"));
            foldersnum++;
            index.put("foldersNum", String.valueOf(foldersnum));
            index.put(String.valueOf(foldersnum), FilterName);
            index.put(FilterName, String.valueOf(0));
            updateFilters();
            updateIndex();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public void addSent(Email e) {
        loadIndexFile();
        EmailFolder ef = new EmailFolder();
        ef.setDir(dir + "/" + name);
        ef.setName("Sent"); // set the name of email folder to inbox as default
                            // email folder
        ef.receiveEmail(e); // send the email to the inbox
        int num = Integer.valueOf((String) index.get("Sent"));
        num++;
        index.put("Sent", String.valueOf(num));
        String contact = e.getTo();
        addContact(contact);
        updateIndex();
    }

    @Override
    public void receiveEmail(Email e) {
        loadFiltersFile(); // load the filter folders of the user if exists
        loadIndexFile();
        EmailFolder ef = new EmailFolder();
        ef.setDir(dir + "/" + name); // set the directory of the email folder
                                     // with "servername/usename"
        Filter f = new Filter();
        f.setFilters(filters); // set the filters of the user if exists
        String n = f.receiveEmail(e.getSubject(), e.getFrom()); // get the filter to which
        if(n == null) {
            ef.setName("Inbox");
            ef.receiveEmail(e);
            int num = Integer.valueOf((String) index.get("Inbox"));
            num++;
            index.put("Inbox", String.valueOf(num));
        } else if(n.equals("Spam")) {
            ef.setName(n);// set an email folder name to the filter name
            ef.receiveEmail(e);// send the email to the filter folder
            int num = Integer.valueOf((String) index.get(n));
            num++;
            index.put(n, String.valueOf(num));
        }else {
            ef.setName(n);// set an email folder name to the filter name
            ef.receiveEmail(e);// send the email to the filter folder
            int num = Integer.valueOf((String) index.get(n));
            num++;
            index.put(n, String.valueOf(num));
            num = Integer.valueOf((String) filters.get(n));
            num++;
            filters.put(n, String.valueOf(num));
        }
        String contact = e.getFrom();
        addContact(contact);
        updateIndex();
    }

    public String getContacts() {
        JSONObject obj = new JSONObject();
        JSONParser p = new JSONParser();
        try {
            FileReader fr =
                    new FileReader(dir + "/" + name + "/" + "contacts.json");
            obj = (JSONObject) p.parse(fr);
            fr.close();
            return obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addContact(String contact) {
        JSONObject obj = new JSONObject();
        JSONParser p = new JSONParser();
        try {
            FileReader fr =
                    new FileReader(dir + "/" + name + "/" + "contacts.json");
            obj = (JSONObject) p.parse(fr);
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (obj.containsValue(contact)) {
            return;
        }
        int contactNum = Integer.valueOf((String) obj.get("contacts"));
        contactNum++;
        obj.put(String.valueOf(contactNum), contact);
        obj.put("contacts", String.valueOf(contactNum));
        updateContacts(obj);
    }
    public void removeContact(String contact) {
        JSONObject obj = new JSONObject();
        JSONParser p = new JSONParser();
        try {
            FileReader fr =
                    new FileReader(dir + "/" + name + "/" + "contacts.json");
            obj = (JSONObject) p.parse(fr);
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int n = Integer.valueOf((String)obj.get("contacts"));
        boolean flag = false;
        for(int i=1;i<=n;i++) {

        		if(flag && i!=1) {
        			obj.put(String.valueOf(i-1), obj.get(String.valueOf(i)));
        		}
        		if(obj.get(String.valueOf(i)).equals(contact)) {
        			obj.remove(String.valueOf(i));
        			flag = true;
        		}

        }
        obj.remove(String.valueOf(n));
        n--;
        obj.put("contacts", String.valueOf(n));
        updateContacts(obj);
    }
    /**
     * the function is used to load the index file to the index object when a
     * user logs in
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
     * @throws IOException
     *             for file handling the function creates the essential folders
     *             once a new user is created such as inbox, trash ....
     */
    private void createEssentialFolders() throws IOException {

        for (int i = 0; i < folders.length; i++) {
            EmailFolder emailfolder = new EmailFolder();
            emailfolder.setName(folders[i]);
            emailfolder.setDir(dir + "/" + name);
            emailfolder.createFolders();
            index.put(String.valueOf(i), folders[i]);
            index.put(folders[i], String.valueOf(0));
            // z3lana 3shan haga tanya
        }
        index.put("foldersNum", String.valueOf(folders.length - 1));
        updateIndex();
        // z3lana 3shan haga tanya
    }

    /**
     * the function writes the index object to the index.json file
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
     * the function is used to update the filters.json file with the filters
     * object
     */
    private void updateFilters() {
        try {
            FileWriter file =
                    new FileWriter(dir + "/" + name + "/" + "filters.json");
            file.write(filters.toString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * it's used to load the filters from the file to the filters object
     */
    public void loadFiltersFile() {
        JSONParser p = new JSONParser();
        try {
            FileReader fr =
                    new FileReader(dir + "/" + name + "/" + "filters.json");
            filters = (JSONObject) p.parse(fr);
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * the function sets and creates new index json object then it store it in a
     * index.json file it also do the same for the filters object
     */
    private void createIndex() {
        index = new JSONObject();

        index.put("foldersNum", String.valueOf(0));

        filters = new JSONObject();
        filters.put("filtersnum", String.valueOf(0));
        filters.put("spamFilters", "money, buy, win");
        filters.put("spamUsers", "");
        // z3lana 3shan haga tanya
        JSONObject cont = new JSONObject();
        cont.put("contacts", String.valueOf(0));
        updateContacts(cont);
        updateIndex();
        updateFilters();
    }

    public void reportSpam(String folderName, String emailNum) {
        loadFiltersFile();
        String spamFilters = (String) filters.get("spamFilters");
        Email e = loadEmailFormove(folderName, emailNum);
        spamFilters += ", " + e.getSubject();
        String spamUsers = (String) filters.get("spamUsers");
        spamUsers += ", " + e.getFrom();
        filters.put("spamFilters", spamFilters);
        filters.put("spamUsers", spamUsers);
        updateFilters();
        moveEmail(folderName, "Spam", emailNum);
    }

    public void updateContacts(JSONObject o) {
        try {
            FileWriter file =
                    new FileWriter(dir + "/" + name + "/" + "contacts.json");
            file.write(o.toString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param n
     *            name of the user
     * @return JSONObject contains emails (from - to - subject - date -
     *         emailnumber)
     */
    public JSONObject loadfolderindex(String n) {
        EmailFolder folder = new EmailFolder();
        folder.setName(n);
        folder.setDir(dir + "/" + name);
        folder.loadIndexFile();
        JSONObject o = folder.getIndex();
        return o;

    }

    public Email loadEmail(String foldername, String emailnum) {
        EmailFolder folder = new EmailFolder();
        folder.setName(foldername);
        folder.setDir(dir + "/" + name);
        folder.loadIndexFile();
        return folder.loadEmail(emailnum);
    }

    public Email loadEmailFormove(String foldername, String emailnum) {
        EmailFolder folder = new EmailFolder();
        folder.setName(foldername);
        folder.setDir(dir + "/" + name);
        folder.loadIndexFile();
        return folder.loadEmailForMove(emailnum);
    }

    public boolean deleteEmail(String foldername, String emailnum)
            throws Exception {
        loadIndexFile();
        loadFiltersFile();
        EmailFolder folder = new EmailFolder();
        folder.setName(foldername);
        folder.setDir(dir + "/" + name);
        int num = Integer.valueOf((String) index.get(foldername));
        num--;
        index.put(foldername, String.valueOf(num));
        int n;
        if (filters.containsKey(foldername)) {
            n = Integer.valueOf((String) filters.get(foldername));
            n--;
            filters.put(foldername, String.valueOf(n));
        }
        updateIndex();
        updateFilters();
        return folder.deleteEmail(emailnum);
    }

    /**
     *
     * @param folderFrom
     * @param folderTo
     * @param emailnum
     * @return
     * @throws Exception
     */
    public boolean moveEmail(String folderFrom, String folderTo,
            String emailnum) {

        try {

            loadIndexFile();
            loadFiltersFile();

            byte[] email = loadEmailFormove(folderFrom, emailnum).toByteArray();

            deleteEmail(folderFrom, emailnum);
            EmailFolder folder = new EmailFolder();
            folder.setName(folderTo);
            folder.setDir(dir + "/" + name);
            String s = new String(email);
            JSONParser j = new JSONParser();
            JSONObject o = new JSONObject();
            o = (JSONObject) j.parse(s);
            Email e = new Email();
            e.setEmail(o);
            folder.receiveEmail(e);
            int num = Integer.valueOf((String) index.get(folderTo));
            num++;
            index.put(folderTo, String.valueOf(num));
            int n;

            if (filters.containsKey(folderTo)) {
                n = Integer.valueOf((String) filters.get(folderTo));
                n++;
                filters.put(folderTo, String.valueOf(n));
            }
            updateIndex();
            updateFilters();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean deleteFolder(String folderName) throws Exception {
        loadFiltersFile();
        loadIndexFile();
        if (!filters.containsValue(folderName)) {
            return false;
        }
        EmailFolder ef = new EmailFolder();
        ef.setDir(dir + "/" + name);
        ef.setName(folderName);
        int nFilters = Integer.valueOf((String) filters.get("filtersnum"));
        int nfolders = Integer.valueOf((String) index.get("foldersNum"));
        String s = null;

        for (int i = 6; i <= nfolders; i++) {
            s = (String) index.get(String.valueOf(i));
            if (s.equals(folderName)) {
                index.remove(String.valueOf(i));
                index.remove(s);
                updateFoldersNum(i);
                break;
            }
        }
        boolean b = false;
        if (s != null) {
            b = ef.deleteFolder();
            for (int i = 1; i <= nFilters; i++) {
                s = (String) filters.get(String.valueOf(i));
                if (s.equals(folderName)) {
                    filters.remove(String.valueOf(i));
                    filters.remove(s);
                    updateFiltersNum(i);
                    break;
                }
            }
        }
        updateFilters();
        updateIndex();
        return b;
    }

    public boolean emptyTrash() throws Exception {
        EmailFolder folder = new EmailFolder();
        folder.setName("Trash");
        folder.setDir(dir + "/" + name);
        folder.loadIndexFile();
        int num = folder.emptyTrash();
        index.put("Trash", String.valueOf(num));
        updateIndex();
        return true;
    }

    public boolean starEmail(String foldername, String emailnum) {
        EmailFolder folder = new EmailFolder();
        folder.setName(foldername);
        folder.setDir(dir + "/" + name);
        folder.loadIndexFile();

        byte[] a = folder.loadEmailForMove(emailnum).toByteArray();
        String s = folder.GetEmailFromIndex(emailnum, true);
        s = foldername + " : " + emailnum + " : " + s;
        EmailFolder folder2 = new EmailFolder();
        folder2.setName("Stared");
        folder2.setDir(dir + "/" + name);
        folder2.loadIndexFile();
        return folder2.setStar(s, emailnum);

    }

    public boolean removeStarEmail(String foldername, String emailnum) {

        EmailFolder folder = new EmailFolder();
        folder.setName(foldername);
        folder.setDir(dir + "/" + name);
        folder.loadIndexFile();
        String s = folder.GetEmailFromIndex(emailnum, false);
        s = foldername + " : " + emailnum + " : " + s;
        EmailFolder folder2 = new EmailFolder();
        folder2.setName("Stared");
        folder2.setDir(dir + "/" + name);
        folder2.loadIndexFile();
        return folder2.removeStar(s, emailnum);
    }

    /*
     * to rename filter file
     */
    public boolean renameFolder(String foldername, String toFoldername) {
        loadFiltersFile();
        loadIndexFile();
        File f = new File(dir + "/" + name + "/" + foldername);
        File f2 = new File(dir + "/" + name + "/" + toFoldername);
        if (f.exists()) {
            if (f.renameTo(f2)) {
                int nFilters =
                        Integer.valueOf((String) filters.get("filtersnum"));
                int nfolders =
                        Integer.valueOf((String) index.get("foldersNum"));
                String s = null;
                for (int i = 6; i <= nfolders; i++) {
                    s = (String) index.get(String.valueOf(i));

                    if (s.equals(foldername)) {

                        String q = (String) index.get(s);
                        index.remove(s);
                        index.put(toFoldername, q);
                        index.put(String.valueOf(i), toFoldername);
                        break;
                    }
                }
                for (int i = 1; i <= nFilters; i++) {
                    s = (String) filters.get(String.valueOf(i));
                    if (s.equals(foldername)) {
                        filters.put(String.valueOf(i), toFoldername);
                        String q = (String) filters.get(s);
                        filters.put(toFoldername, q);
                        filters.remove(foldername);
                        break;
                    }
                }
                updateFilters();
                updateIndex();
                return true;
            }
        }
        updateFilters();
        updateIndex();
        return false;
    }

    public boolean setPriority(String foldername, String emailNum,
            String priority) {
        EmailFolder folder = new EmailFolder();
        folder.setName(foldername);
        folder.setDir(dir + "/" + name);
        folder.loadIndexFile();
        return folder.setPriority(emailNum, priority);
    }

    private void updateFoldersNum(int x) throws Exception {
        int n = Integer.valueOf((String) index.get("foldersNum"));
        for (int i = x; i < n; i++) {
            index.put(String.valueOf(i), index.get(String.valueOf(i + 1)));
        }
        index.remove(String.valueOf(n));
        index.put("foldersNum", String.valueOf(n - 1));
    }

    private void updateFiltersNum(int x) throws Exception {
        int n = Integer.valueOf((String) filters.get("filtersnum"));
        for (int i = x; i < n; i++) {
            filters.put(String.valueOf(i), filters.get(String.valueOf(i + 1)));
        }
        filters.remove(String.valueOf(n));
        filters.put("filtersnum", String.valueOf(n - 1));
    }

    public linkedList search(String searchNum, String toSearch) {
        int n = Integer.valueOf((String) index.get("foldersNum"));
        linkedList s = new linkedList();
        for (int i = 0; i <= n; i++) {

            EmailFolder folder = new EmailFolder();
            if (((String) index.get(String.valueOf(i))).equals("stared")) {
                continue;
            }
            folder.setDir(dir + "/" + name);
            folder.setName((String) index.get(String.valueOf(i)));
            folder.loadIndexFile();
            linkedList temp = folder.search(searchNum, toSearch);
            for (int k = 0; k < temp.size; k++) {
                s.add(temp.get(k));
            }

        }
        return s;
    }

    public boolean markEmailRead(String folderName, String emailNum) {
        EmailFolder ef = new EmailFolder();
        ef.setName(folderName);
        ef.setDir(dir + "/" + name);
        ef.loadIndexFile();
        return ef.markEmailRead(emailNum);
    }

    public boolean markAllEmailsRead(String folderName) {
        EmailFolder ef = new EmailFolder();
        ef.setName(folderName);
        ef.setDir(dir + "/" + name);
        ef.loadIndexFile();
        return ef.markAllEmailsRead();
    }
}
