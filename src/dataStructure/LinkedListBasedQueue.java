package dataStructure;


import dataStructuresInterfaces.ILinkedBased;
import dataStructuresInterfaces.IQueue;

/**
 * @author SHIKO
 *
 */
public class LinkedListBasedQueue implements IQueue, ILinkedBased {
	/**
	 *
	 * @author SHIKO
	 *
	 */
	private static class Node {
		/**
		 *
		 */
		private Object element;
		/**
		 *
		 */
		private Node next;

		/**
		 *
		 */
		public Node() {
			this(null, null);
		}

		/**
		 *
		 * @param x .
		 * @param n .
		 */
		public Node(final Object x, final Node n) {
			element = x;
			next = n;
		}
		/**
		 *
		 * @param x .
		 */
		public void setElement(final Object x) {
			element = x;
		}

		/**
		 *
		 * @return element .
		 */
		public Object getElement() {
			return element;
		}

		/**
		 *
		 * @param n .
		 */
		public void setNext(final Node n) {
			next = n;
		}

		/**
		 *
		 * @return next .
		 */
		public Node getnext() {
			return next;
		}

	}

	/**
	 *
	 */
	private Node front;
	/**
	 *
	 */
	private Node rear;
	/**
	 *
	 */
	private int size = 0;

	@Override
	public void enqueue(final Object item) {
		if (size == 0) {
			rear = new Node(item, null);
			front = rear;
			size++;

		} else {
			Node addedNode = new Node(item, null);
			rear.setNext(addedNode);
			rear = addedNode;
			size++;
		}
	}

	@Override
	public Object dequeue() {
		if (size == 0) {
			throw new RuntimeException();
		} else if (size == 1) {
			Object element = front.getElement();
			front = front.getnext();
			rear = front;
			size--;
			return element;

		}
		Object element = front.getElement();
		front = front.getnext();
		size--;
		return element;

	}

	@Override
	public boolean isEmpty() {

		return (size == 0);
	}

	@Override
	public int size() {

		return size;
	}
}
