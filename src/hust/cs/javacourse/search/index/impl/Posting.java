package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Posting extends AbstractPosting {

    public Posting(){

    }

    public Posting(int docId, int freq, List<Integer> positions){
        super(docId, freq, positions);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AbstractPosting)
            return this.docId == ((AbstractPosting) obj).getDocId() && this.freq == ((AbstractPosting)obj).getFreq() && this.positions.containsAll(((AbstractPosting)obj).getPositions()) && ((AbstractPosting)obj).getPositions().containsAll(this.positions);
        return false;
    }

    @Override
    public String toString() {
        return "Posting{" +
                "docId=" + docId +
                "\nfreq=" + freq +
                "\npositions=" + positions +
                '}' + '\n';
    }

    @Override
    public int getDocId() {
        return this.docId;
    }

    @Override
    public void setDocId(int docId) {
        this.docId = docId;
    }

    @Override
    public int getFreq() {
        return  freq;
    }

    @Override
    public void setFreq(int freq) {
        this.freq = freq;
    }

    @Override
    public List<Integer> getPositions() {
        return this.positions;
    }

    @Override
    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    @Override
    public int compareTo(AbstractPosting o) {
        return this.docId - o.getDocId();
    }

    @Override
    public void sort() {
        this.positions.sort(Comparator.naturalOrder());
    }

    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(this.docId);
            out.writeObject(this.freq);
            out.writeObject(this.positions);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @SuppressWarnings("unchecked")
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            this.docId = (int)in.readObject();
            this.freq = (int)in.readObject();
            this.positions = (List<Integer>)in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
