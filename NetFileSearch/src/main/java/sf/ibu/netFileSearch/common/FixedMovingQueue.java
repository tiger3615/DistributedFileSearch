package sf.ibu.netFileSearch.common;

import java.util.ArrayList;

public class FixedMovingQueue<T> {
	private Entry<T> head;
	private Entry<T> tail;
	private int size;
	private int currentSize;

	public FixedMovingQueue(int size) {
		this.size = size;
	}

	public void put(T realObj) {
		Entry<T> entry = new Entry<T>(realObj);
		if (head == null) {
			head = tail = entry;
		} else {
			tail.setNext(entry);
			tail = tail.getNext();
		}
		++currentSize;
		while ( currentSize > size) {
			head=head.getNext();
			currentSize--;
		}
	}
	public ArrayList<T> toList(){
		ArrayList<T> list=new ArrayList<T>();
		//we cannot change the original pointer. so copy one!
		Entry<T> headCopied=head;
		while(headCopied!=null) {
			list.add(headCopied.getReal());
			headCopied=headCopied.getNext();
		}
		return list;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public class Entry<T> {
		private Entry<T> next;
		private T real;

		public Entry(T realObj) {
			real = realObj;
		}

		public Entry<T> getNext() {
			return next;
		}

		public void setNext(Entry<T> next) {
			this.next = next;
		}

		public T getReal() {
			return real;
		}

		public void setReal(T real) {
			this.real = real;
		}

	}
}
