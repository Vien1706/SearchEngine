package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PostingList extends AbstractPostingList {

    public PostingList(){

    }

    @Override
    public void add(AbstractPosting posting) {
        for(AbstractPosting i:list)
            if(i.equals(posting))
                return;
        this.list.add(posting);
    }

    @Override
    public String toString() {
        return "PostingList{" +
                "\nlist=" + list +
                '}' + '\n';
    }

    @Override
    public void add(List<AbstractPosting> postings) {
        for(AbstractPosting i:postings)
            this.add(i);
    }

    @Override
    public AbstractPosting get(int index) {//!!
        return this.list.get(index);
    }

    @Override
    public int indexOf(AbstractPosting posting) {//!!
        return this.list.indexOf(posting);
    }

    @Override
    public int indexOf(int docId) {
        int i = 0;
        for(AbstractPosting p:list) {
            if (p.getDocId() == docId)
                return i;
            i++;
        }
        return -1;
    }

    @Override
    public boolean contains(AbstractPosting posting) {
        return this.list.contains(posting);
    }

    @Override
    public void remove(int index) {
        this.list.remove(index);
    }

    @Override
    public void remove(AbstractPosting posting) {
        this.list.remove(posting);
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public void clear() {
        this.list.clear();
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public void sort() {
        list.sort(Comparator.comparing(AbstractPosting::getDocId));
    }

    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(this.list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            this.list = (List<AbstractPosting>) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
