package dataStructure;

/**
 * @author Muhammad Salah
 *
 */
public class StackNode {
    /**
     *
     */
    private Object data = null;
    /**
     *
     */
    private StackNode prev = null;
    /**
     *
     */
    StackNode() {
    }
    /**
     * @param d data
     */
    StackNode(final Object d) {
        this.data = d;
    }
    /**
     * @param d data
     */
    public void setData(final Object d) {
        this.data = d;
    }
    /**
     * @param l prev
     */
    public void setprev(final StackNode l) {
        this.prev = l;
    }
    /**
     * @return data
     */
    public Object getData() {
        return this.data;
    }
    /**
     * @return prev
     */
    public StackNode getprev() {
        return this.prev;
    }
}
