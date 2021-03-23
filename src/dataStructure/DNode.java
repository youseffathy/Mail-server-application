package dataStructure;

/**
 * @author Muhammad Salah
 *
 */
public class DNode {
    /**
     *
     */
    private Object obj = null;
    /**
     *
     */
    private DNode next = null;
    /**
    *
    */
   private DNode prev = null;
   /**
    *
    */
   DNode() {
   }
   /**
    * @param o set object constructor
    */
   DNode(final Object o) {
       this.obj = o;
   }
    /**
     * @param o set object of Dnode
     */
    public void setObj(final Object o) {
        this.obj = o;
    }
    /**
     * @return object of Dnode
     */
    public Object getObj() {
        return this.obj;
    }
    /**
     * @param n to set next dnode
     */
    public void setNext(final DNode n) {
        this.next = n;
    }
    /**
     * @return next Dnode
     */
    public DNode getNext() {
        return this.next;
    }
    /**
     * @param n to set prev dnode
     */
    public void setPrev(final DNode n) {
        this.prev = n;
    }
    /**
     * @return prev dnode
     */
    public DNode getPrev() {
        return this.prev;
    }
}
