import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item>{
    private Node first;     // 头
    private Node last;      // 尾
    private int sz;         // 大小
    public Deque(){
        first = null;
        last = null;
        sz = 0;
    }
    public boolean isEmpty(){
        return sz == 0;
    }
    public int size(){
        return sz;
    }
    public void addFirst(Item item){
        if(item == null)
            throw new IllegalArgumentException();
        Node oldfirst = first;
        first = new Node(item);
        first.prev = null;
        first.next = oldfirst;
        if(isEmpty()){
            last = first;
        }else{
            oldfirst.prev = first;
        }
        ++sz;
    }
    public void addLast(Item item){
        if(item == null)
            throw new IllegalArgumentException();
        Node oldlast = last;
        last = new Node(item);
        last.next = null;
        last.prev = oldlast;
        if(isEmpty()){
            first = last;
        }else{
            oldlast.next = last;
        }
        ++sz;
    }
    public Item removeFirst(){
        if(isEmpty())
            throw new NoSuchElementException();
        --sz;
        if(sz == 0){
            Item res = first.val;
            first = null;
            last = null;
            return res;
        }
        Node oldfirst = first;
        first = first.next;
        first.prev = null;
        return oldfirst.val;
    }
    public Item removeLast(){
        if(isEmpty())
            throw new NoSuchElementException();
        --sz;
        if(sz == 0){
            Item res = last.val;
            first = null;
            last = null;
            return res;
        }
        Node oldlast = last;
        last = last.prev;
        last.next = null;
        return oldlast.val;
    }

    public Iterator<Item> iterator(){
        return new DequeIter();
    }

    private class DequeIter implements Iterator<Item>{
        private Node cur = first;
        public void remove(){
            throw new UnsupportedOperationException();
        }
        public boolean hasNext(){
            return cur != null;
        }
        public Item next(){
            if(cur == null)
                throw new NoSuchElementException();
            Item res = cur.val;
            cur = cur.next;
            return res;
        }
    }

    private class Node {
        Node prev;
        Node next;
        Item val;
        Node(Item v) {
            prev = null;
            next = null;
            val = v;
        }
    }

    public static void main(String[] args) {

    }
}