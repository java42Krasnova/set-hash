package telran.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class HashSet<T> extends AbstractSet<T> {
	private static final int DEFAULT_ARRAYLENGTH = 16;
	private static final float FACTOR = (float) 0.75;
	LinkedList<T> hashTable[];

	@SuppressWarnings("unchecked")
	public HashSet(int arrayLength) {
		hashTable = new LinkedList[arrayLength];
	}

	public HashSet() {
		this(DEFAULT_ARRAYLENGTH);
	}

	@Override
	public boolean add(T obj) {
		boolean res = false;
		if (!contains(obj)) {
			res = true;
			size++;
			if (size > FACTOR * hashTable.length) {
				recreateHashTable();
			}
			int index = getHashTableIndex(obj);
			if(hashTable[index] == null) {
				hashTable[index] = new LinkedList<>();
			}
			hashTable[index].add(obj);			
		}

		return res;
	}

	private void recreateHashTable() {
HashSet<T> tmpSet = new HashSet<>(hashTable.length*2);	
	for(LinkedList<T> backet: hashTable) {
		if(backet != null) {
			for (T obj: backet) {
				tmpSet.add(obj);
			}
		}
	}
	hashTable = tmpSet.hashTable;
	tmpSet = null;
	}

	private int getHashTableIndex(T obj) {
		int hashCode = obj.hashCode();
		int res = Math.abs(hashCode)%hashTable.length;
		return res;
	}

	@Override
	public T remove(T pattern) {
		int index = getHashTableIndex(pattern);
		T res = null;
		if(hashTable[index] != null){
			int indObj = hashTable[index].indexOf(pattern);
			if (indObj >= 0) {
				res = hashTable[index].remove(indObj);
			}
			size--;
		}
		return res;
	}

	@Override
	public boolean contains(T pattern) {
		boolean res = false;
		int htInd = getHashTableIndex(pattern);
		if(hashTable[htInd] != null) {
		  res = hashTable[htInd].contains(pattern);
		}
		 return res;
	}

	@Override
	public Iterator<T> iterator() {
		return new HashSetIterator<T>();
	}
	private class HashSetIterator<T> implements Iterator<T>
	{
		Iterator<T> arrIterators[];
		int currentIndex = -1 ;
		int indPrevIterator;
		boolean isWasNext = false;
		
		public HashSetIterator() {
			 fillIteratorsArray();
		}
		
		@SuppressWarnings("unchecked")
		private void fillIteratorsArray() {
			arrIterators = new Iterator[hashTable.length];
			for(int i=0; i < hashTable.length; i++) {
				if(hashTable[i]!= null) {
				arrIterators[i] = (Iterator<T>) hashTable[i].iterator();
				if(currentIndex<0) { 
					currentIndex = i;
				}	
				}
		}
		}

		@Override
		public boolean hasNext() {
		return currentIndex < arrIterators.length && arrIterators[currentIndex].hasNext();
		}

		@Override
		public T next() {//TODO done
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			T res = arrIterators[currentIndex].next();
			indPrevIterator = currentIndex;
			findNextllIterator();
			isWasNext = true;
			return res;
		}
		
		private void findNextllIterator() {
		if(	!arrIterators[currentIndex].hasNext()) {
			while(++currentIndex < arrIterators.length && 
			(arrIterators[currentIndex]==null||
					!arrIterators[currentIndex].hasNext())){
			}
		}
		}

		@Override
		public void remove() {//TODO done
			if(!isWasNext) {
				throw new IllegalStateException();
			}
			arrIterators[indPrevIterator].remove();
			isWasNext = false;
			size--;
		}
	}
}
