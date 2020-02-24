import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item>{
    private Item[] arr;
    private int sz;

    public RandomizedQueue(){
        arr = (Item[])new Object[1];
        sz = 0;
    }
    public boolean isEmpty(){
        return sz == 0;
    }
    public int size(){
        return sz;
    }
    public void enqueue(Item item){
        if(item == null)
            throw new IllegalArgumentException();
        if(sz == arr.length){
            resize(2 * arr.length);
        }
        arr[sz++] = item;
    }
    public Item dequeue(){
        if(isEmpty())
            throw new java.util.NoSuchElementException();
        int idx = StdRandom.uniform(sz);
        Item res = arr[idx];
        arr[idx] = arr[sz - 1];
        arr[sz - 1] = null;
        --sz;
        if(sz > 0 && sz <= arr.length / 4){
            resize(arr.length / 2);
        }
        return res;
    }
    public Item sample(){
        if(isEmpty())
            throw new java.util.NoSuchElementException();
        return arr[StdRandom.uniform(sz)];
    }
    public Iterator<Item> iterator(){
        return new RdQueueIter();
    }

    private class RdQueueIter implements Iterator<Item>{
        private Item[] copy;
        private int idx;
        RdQueueIter(){
            copy = (Item[]) new Object[sz];
            for(int i = 0; i < sz; ++i){
                copy[i] = arr[i];
            }
            StdRandom.shuffle(copy);
            idx = 0;
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
        public boolean hasNext(){
            return idx != copy.length;
        }
        public Item next(){
            if(idx == copy.length)
                throw new java.util.NoSuchElementException();
            return copy[idx++];
        }
    }

    private void resize(int n){
        Item[] oldarr = arr;
        arr = (Item[]) new Object[n];
        for(int i = 0; i < sz; ++i){
            arr[i] = oldarr[i];
        }
    }


    public static void main(String[] args){
        // RandomizedQueue<Integer> rdq = new RandomizedQueue<Integer>();
        // System.out.println(rdq.isEmpty());
        // rdq.enqueue(10);
        // rdq.enqueue(-1);
        // rdq.enqueue(430);
        // System.out.println(rdq.sample());
        // Iterator<Integer> it = rdq.iterator();
        // while(it.hasNext()){
        //     System.out.println(it.next());
        // }
        // it.remove();
    }
}