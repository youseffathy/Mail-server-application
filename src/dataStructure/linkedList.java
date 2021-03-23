package dataStructure;

class sNode {
	private Object element;
	private sNode link;

	public sNode() {
		element = null;
		link = null;
	}

	public sNode(Object x, sNode n) {
		element = x;
		link = n;
	}

	public void setObject(Object x) {
		element = x;
	}

	public void setLink(sNode n) {
		link = n;
	}

	public Object getObject() {
		return element;
	}

	public sNode getLink() {
		return link;
	}
}

public class linkedList  {

	private sNode head = null;
	public int size = 0;
	int i;

	
	public void add(int index, Object element) {
		sNode n = new sNode(element, null);
		sNode st1 = head;
		if (index <= size && index >= 0) {
			if (index == 0) {
				n.setLink(head);
				head = n;
			} else {

				for (i = 0; i < size; i++) {
					if (i == index - 1) {
						sNode temp = st1.getLink();
						st1.setLink(n);
						n.setLink(temp);
						break;
					}
					st1 = st1.getLink();
				}
			}
			size++;

		} else {
			throw new RuntimeException();
		}
	}

	public void add(Object element) {
		sNode n = new sNode(element, null);
		sNode st1 = head;
		if (size == 0) {
			head = n;
		} else {
			for (i = 0; i < size; i++) {
				if (i == size - 1) {
					sNode temp = st1.getLink();
					st1.setLink(n);
					n.setLink(temp);
				}
				st1 = st1.getLink();
			}
		}
		size++;
	}

	public Object get(int index) {
		sNode st1 = head;
		if (index < size && index >= 0) {
			for (i = 0; i < size; i++) {
				if (i == index) {
					return st1.getObject();
				}
				st1 = st1.getLink();

			}
		} else {
			throw new RuntimeException();
		}
		return null;

	}


	public void set(int index, Object element) {
		sNode st1 = head;
		if (index < size && index >= 0) {
			for (i = 0; i < size; i++) {
				if (i == index) {
					st1.setObject(element);
				}
				st1 = st1.getLink();
			}

		} else {
			throw new RuntimeException();
		}
	}

	
	public void clear() {
		head = null;
		size = 0;

	}

		
	public boolean isEmpty() {

		return (size == 0);
	}

	
	public void remove(int index) {
		sNode st1 = head;
		if (index < size && index >= 0) {
			if (index == 0) {
				head = head.getLink();
			}
			for (i = 0; i < size; i++) {
				if (i == index - 1) {
					st1.setLink(st1.getLink().getLink());
					break;
				}
				st1 = st1.getLink();

			}
			size--;

		} else {
			throw new RuntimeException();
		}
	}

	
	public int size() {

		return size;
	}

	
	public linkedList sublist(int fromIndex, int toIndex) {
		if (fromIndex >= 0 && fromIndex < size && toIndex >= 0 && toIndex < size) {
			linkedList sublist = new linkedList();
			sNode st1 = head;
			for (i = 0; i <= toIndex; i++) {
				if (i >= fromIndex && i <= toIndex) {
					sublist.add(st1.getObject());
				}
				st1 = st1.getLink();
			}
			return sublist;
		} else {
			throw new RuntimeException();
		}
	}

	public boolean contains(Object o) {
		sNode st1 = head;
		for (i = 0; i < size; i++) {
			if (st1.getObject().equals(o)) {
				return true;
			}
			st1 = st1.getLink();
		}
		return false;
	}

}
