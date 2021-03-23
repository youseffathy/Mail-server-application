package dataStructure;
import dataStructuresInterfaces.ILinkedList;
import java.util.Comparator;

/**
 * @author Muhammad Salah
 *
 */
public class DoubleLinkedList implements ILinkedList {
    /**
     *
     */
    private DNode head = new DNode();
    /**
    *
    */
    private DNode tail = new DNode();
    /**
     *
     */
    private int size = 0;
    /**
     *
     */
    public DoubleLinkedList() {
        head.setNext(tail);
        tail.setPrev(head);
    }
    /**
     * @param index ahaha
     * @return ahahah
     */
    private DNode getBack(final int index) {
        DNode current = tail;
        for (int i = size - 1; i >= index; i--) {
            current = current.getPrev();
        }
        return current;
    }
    /**
     * @param index ahaha
     * @return ahah
     */
    private DNode getFront(final int index) {
        DNode current = head;
        for (int i = 0; i <= index; i++) {
            current = current.getNext();
        }
        return current;
    }
    /**
     *
     */
    @Override
    public void add(final int index, final Object element) {
        if (index > size || index < 0) {
            throw new RuntimeException();
        } else {
            DNode n = null;
            if (index < size / 2) {
                n = getFront(index);
            } else {
                n = getBack(index);
            }
            DNode o = new DNode(element);
            n.getPrev().setNext(o);
            o.setPrev(n.getPrev());
            n.setPrev(o);
            o.setNext(n);
            size++;
        }
    }

    /**
    *
    */
    @Override
    public void add(final Object element) {
            DNode o = new DNode(element);
            tail.getPrev().setNext(o);
            o.setPrev(tail.getPrev());
            o.setNext(tail);
            tail.setPrev(o);
            size++;
    }

    /**
     *
     */
    @Override
    public Object get(final int index) {
        if (index >= size || index < 0) {
            throw new RuntimeException();
        } else {
            DNode n = null;
            if (index < size / 2) {
                n = getFront(index);
            } else {
                n = getBack(index);
            }
            return n.getObj();
        }
    }
    /**
    *
    */
    @Override
    public void set(final int index, final Object element) {
        if (index >= size) {
            throw new RuntimeException();
        } else {
            DNode n = null;
            if (index < size / 2) {
                n = getFront(index);
            } else {
                n = getBack(index);
            }
            n.setObj(element);
        }
    }

    /**
    *
    */
    @Override
    public void clear() {
        head.setNext(tail);
        tail.setPrev(head);
        size = 0;
    }
    /**
     *
     */
    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    /**
    *
    */
    @Override
    public void remove(final int index) {
        if (index >= size) {
            throw new RuntimeException();
        } else {
            DNode n = null;
            if (index < size / 2) {
                n = getFront(index).getPrev();
            } else {
                n = getBack(index).getPrev();
            }
            n.getNext().getNext().setPrev(n);
            n.setNext(n.getNext().getNext());
            size--;
        }
    }

    /**
     *
     */
    @Override
    public int size() {
        return size;
    }

    /**
     *
     */
    @Override
    public ILinkedList sublist(final int fromIndex, final int toIndex) {
        if (fromIndex >= size || fromIndex < 0) {
            throw new RuntimeException();
        }
        if (toIndex >= size || toIndex < 0) {
            throw new RuntimeException();
        }
        if (fromIndex > toIndex) {
            throw new RuntimeException();
        }
        DoubleLinkedList s = new DoubleLinkedList();
        DNode n = null;
        if (fromIndex < size / 2) {
            n = getFront(fromIndex);
        } else {
            n = getBack(fromIndex);
        }
        s.add(n.getObj());
        for (int i = fromIndex + 1; i <= toIndex; i++) {
            n = n.getNext();
            s.add(n.getObj());
        }
        return s;
    }

    /**
     *
     */
    @Override
    public boolean contains(final Object o) {
        DNode current = head;
        for (int i = 0; i < size; i++) {
            current = current.getNext();
            if (current.getObj().equals(o)) {
                return true;
            }
        }
        return false;
    }
    /**
     * @param e object
     * @param comp to compare
     */
    public void addSorted(final Object e, final Comparator comp) {
        if (size == 0) {
            add(e);
        } else {
            DNode current = head.getNext();
            while (current != tail && comp.compare(current.getObj(), e) == 1) {
                current = current.getNext();
            }
            DNode o = new DNode(e);
            current.getPrev().setNext(o);
            o.setPrev(current.getPrev());
            o.setNext(current);
            current.setPrev(o);
            size++;
        }
    }
}
