package dataStructure;

import dataStructuresInterfaces.IStack;

/**
 * @author Muhammad Salah
 *
 */
public class Stack implements IStack {
    /**
     *
     */
    private StackNode top = null;
    /**
     *
     */
    private int size = 0;
    /**
     *
     */
    @Override
    public Object pop() {
        if (size == 0) {
            throw new RuntimeException();
        }
        Object data = top.getData();
        top = top.getprev();
        size--;
        return data;
    }
   /**
    *
    */
    @Override
    public Object peek() {
        if (size == 0) {
            throw new RuntimeException();
        }
        return top.getData();
    }
   /**
    *
    */
    @Override
    public void push(final Object element) {
        StackNode n = new StackNode(element);
        n.setprev(top);
        top = n;
        size++;
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
    public int size() {
        return size;
    }
}
