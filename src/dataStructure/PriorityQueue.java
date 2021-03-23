package dataStructure;

import dataStructuresInterfaces.IPriorityQueue;


/**
 * @author SHIKO
 *
 */
public class PriorityQueue implements IPriorityQueue {
    /**
     *
     */
    private int size = 0;
    /**
     *
     */
    private DNode head = new DNode(null, null, null, 0);
    /**
     *
     */
    private DNode tail = new DNode(null, head, null, 0);

    /**
     *
     */
    public PriorityQueue() {
        head.setnextLink(tail);
    }

    /**
     * @author SHIKO
     *
     */
    private class DNode {
        /**
         *
         */
        private Object element;
        /**
         *
         */
        private DNode previousLink;
        /**
         *
         */
        private DNode nextLink;
        /**
         *
         */
        private int key = 0;
        /**
         * @param e e
         * @param p a
         * @param n f
         * @param k g
         */
        DNode(final Object e, final DNode p,
                final DNode n, final int k) {
            key = k;
            element = e;
            previousLink = p;
            nextLink = n;
        }
        /**
         * @param x d
         */
        public void setObject(final Object x) {
            element = x;
        }
        /**
         * @param n n
         */
        public void setPreviousLink(final DNode n) {
            previousLink = n;
        }
        /**
         * @param n hhj
         */
        public void setnextLink(final DNode n) {
            nextLink = n;
        }
        /**
         * @return jjj
         */
        public Object getObject() {
            return element;
        }
        /**
         * @return gg
         */
        public DNode getPreviousLink() {
            return previousLink;
        }
        /**
         * @return ggg
         */
        public DNode getNextLink() {
            return nextLink;
        }
    }
    /**
     *
     */
    @Override
    public void insert(final Object item, final int key) {
        if (key <= 0) {
            throw new RuntimeException();
        }
        DNode i = head.nextLink;
        DNode j = tail.previousLink;
        while (i.key <= key && i.key != 0
            && j.key != 0 && j.key > key) {
            i = i.nextLink;
            j = j.previousLink;
        }
        if (i.key > key) {
            DNode addednode = new DNode(
                item, i.previousLink, i, key);
            i.previousLink.setnextLink(addednode);
            i.previousLink = addednode;
        } else if (j.key <= key) {
            DNode addednode = new DNode(item, j, j.nextLink, key);
            j.nextLink.setPreviousLink(addednode);
            j.setnextLink(addednode);
        }
        size++;
    }
    /**
     *
     */
    @Override
    public Object removeMin() {
        if (size == 0) {
            throw new RuntimeException();
        }
        DNode i = head.nextLink;
        i.nextLink.setPreviousLink(head);
        head.setnextLink(i.nextLink);
        size--;
        return i.getObject();
    }
    /**
     *
     */
    @Override
    public Object min() {
        DNode i = head.nextLink;
        if (size == 0) {
            throw new RuntimeException();
        }
        return i.getObject();
    }
    /**
     *
     */
    @Override
    public boolean isEmpty() {
        return (size == 0);
    }
    /**
     *
     */
    @Override
    public int size() {
        return size;
    }
}
