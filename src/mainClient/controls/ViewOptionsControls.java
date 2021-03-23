package mainClient.controls;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import dataStructure.HBoxStackQuickSort;
import dataStructure.PriorityQueue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import mainClient.ClientFirstTry;
import serverClient.Request;

/**
 * @author Muhammad Salah
 *
 */
public class ViewOptionsControls {
    /**
     * the controls class of the Layout
     */
    EmailLayoutControls c;
    /**
     * the main class of the Client
     */
    ClientFirstTry x;

    /**
     * @param z the controls class of the Layout
     * @param x the main class of the Client
     */
    public ViewOptionsControls(EmailLayoutControls z ,ClientFirstTry x) {
        c = z;
        this.x = x;
    }

    /**
     * refresh button
     */
    @FXML
    private Button refresh;
    /**
     * mark all as read
     */
    @FXML
    private Button allRead;
    /**
     * MenuButton of sorting
     */
    @FXML
    private MenuButton sort;
    /**
     * priority sort option
     */
    @FXML
    private MenuItem prioritySort;
    /**
     * sort by date option
     */
    @FXML
    private MenuItem dateSort;
    /**
     * sort by the author menu option
     */
    @FXML
    private MenuItem fromSort;
    /**
     * initailize the controls of the Unselected emails
     */
    @FXML
    private void initialize() {
        prioritySort.setOnAction((event) -> {
            prioritySort();
        });
        dateSort.setOnAction((event) -> {
            c.emailsloader(c.getfname());
        });
        
        refresh.setOnAction((event) -> {
            c.emailsloader(c.getfname());
            c.foldersLoader();
        });
        fromSort.setOnAction((event) -> {
            quickSortFrom();
        });
        allRead.setOnAction((event) -> {
            try {
                Request r = new Request(null, null);
                
                InetAddress i = InetAddress.getByName(x.getIP());
                Socket skt = new Socket();
                skt.connect(new InetSocketAddress(i, x.getPort()), 1000);
                DataInputStream dis = new DataInputStream(skt.getInputStream());
                DataOutputStream dos =
                        new DataOutputStream(skt.getOutputStream());
                String req = r.markAllAsReadReq(x.getUsername(), c.getfname());
                dos.writeUTF(req);
                String res = dis.readUTF();
                boolean b = r.deleteFolderRes(res);
                skt.close();
                if(b) {
                    c.emailsloader(c.getfname());
                    c.foldersLoader(); 
                }else {
                    throw new Exception();
                }
                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    /**
     * @param e HBox of the email that contains the email data
     * @return the number of the priority
     */
    private int getPriorityOfEmail(HBox e) {
        String prio = ((MenuButton)e.getChildren().get(7)).getText();
        if(prio.equals("Very Important")) {
            return 1;
        } else if (prio.equals("Important")) {
            return 2;
        } else {
            return 3;
        }
    }
    /**
     * sort the emails by priority 1, 2, or 3
     */
    private void prioritySort() {
        ObservableList<HBox> emails = c.getEmails();
        PriorityQueue pq = new PriorityQueue();
        for(int i = 0; i < emails.size(); i++) {
            HBox em = emails.get(i);
            //System.out.println(((Label)em.getChildren().get(4)).getText());
            int prio = getPriorityOfEmail(em);
            pq.insert(em, prio);
        }
        
        emails.clear();
        while(!pq.isEmpty()) {
            HBox em = (HBox) pq.min();
            emails.add(em);
            pq.removeMin();
        }
    }
    /**
     * sor the emails by the author using quick sort algorithm
     */
    private void quickSortFrom() {
        ObservableList<HBox> emails = c.getEmails();
        HBox[] arr = new HBox[emails.size()];
        for(int i = 0; i < arr.length; i++) {
            arr[i] = emails.get(i);
        }
        emails.clear();
        HBoxStackQuickSort hq = new HBoxStackQuickSort();
        hq.quickSort(arr);
        for(int i = 0; i < arr.length; i++) {
            emails.add(arr[i]);
        }
    }
}
