package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * AbstractIndex的具体实现类
 */
public class Index extends AbstractIndex {
    /**
     * 返回索引的字符串表示
     *
     * @return 索引的字符串表示
     */
    @Override
    public String toString() {
        return "Index{" +
                "docIdToDocPathMapping=" + docIdToDocPathMapping +
                "\ntermToPostingListMapping=" + termToPostingListMapping +
                '}';
    }
    /**
     * 添加文档到索引，更新索引内部的HashMap
     *
     * @param document ：文档的AbstractDocument子类型表示
     */
    @Override
    public void addDocument(AbstractDocument document) {
        //docIdToDocPathMapping: <文档编号docId, 文档路径docPath>
        if(document == null) {
            return;
        }
        if (!this.docIdToDocPathMapping.containsKey(document.getDocId())) {
            this.docIdToDocPathMapping.put(document.getDocId(), document.getDocPath());
        }
        /**/
        for (int k = 0; k < document.getTupleSize(); k++) {
            //termToPostingListMapping: <term(单词、出现次数、出现位置), Posting列表(文档编号、出现次数、出现位置的列表)>
            AbstractTermTuple tt = document.getTuple(k);
            if (this.termToPostingListMapping.containsKey(tt.term)) {
                //map中包含当前term(单词)
                AbstractPostingList poslist = this.termToPostingListMapping.get(tt.term);
                int index = poslist.indexOf(document.getDocId());
                if (index >= 0) {
                    //map中包含当前term(单词),且是当前文档
                    AbstractPosting o = poslist.get(index);
                    o.setFreq(o.getFreq() + 1);
                    o.getPositions().add(tt.curPos);
                } else {
                    //虽然map中包含当前term(单词),但该单词属于别的文档
                    AbstractPosting p = new Posting();
                    p.setDocId(document.getDocId());
                    p.getPositions().add(tt.curPos);
                    p.setFreq(tt.freq);
                    poslist.add(p);
                }
            } else {
                //map中没有当前term(单词)
                AbstractPosting p = new Posting();
                p.setDocId(document.getDocId());
                p.setFreq(tt.freq);
                p.getPositions().add(tt.curPos);
                PostingList pl = new PostingList();
                pl.add(p);
                this.termToPostingListMapping.put(tt.term, pl);
            }
        }
    }

    /**
     * <pre>
     * 从索引文件里加载已经构建好的索引.内部调用FileSerializable接口方法readObject即可
     * @param file ：索引文件
     * </pre>
     */
    @Override
    public void load(File file) throws IOException {
        ObjectInputStream ois;
        ois = new ObjectInputStream(new FileInputStream(new File(String.valueOf(file))));
        this.readObject(ois);
    }

    /**
     * <pre>
     * 将在内存里构建好的索引写入到文件. 内部调用FileSerializable接口方法writeObject即可
     * @param file ：写入的目标索引文件
     * </pre>
     */
    @Override
    public void save(File file) throws IOException {
        ObjectOutputStream oos;
        oos = new ObjectOutputStream(new FileOutputStream(new File(String.valueOf(file))));
        this.writeObject(oos);
    }

    /**
     * 返回指定单词的PostingList
     *
     * @param term : 指定的单词
     * @return ：指定单词的PostingList;如果索引字典没有该单词，则返回null
     */
    @Override
    public AbstractPostingList search(AbstractTerm term) {
        return termToPostingListMapping.get(term);
    }

    /**
     * 返回索引的字典.字典为索引里所有单词的并集
     *
     * @return ：索引中Term列表
     */
    @Override
    public Set<AbstractTerm> getDictionary() {
        return this.termToPostingListMapping.keySet();
    }

    /**
     * <pre>
     * 对索引进行优化，包括：
     *      对索引里每个单词的PostingList按docId从小到大排序
     *      同时对每个Posting里的positions从小到大排序
     * 在内存中把索引构建完后执行该方法
     * </pre>
     */
    @Override
    public void optimize() {
        for(AbstractPostingList postingList: termToPostingListMapping.values()){
            postingList.sort();
            for(AbstractPosting posting: postingList.getList()){
                posting.sort();
            }
        }

    }

    /**
     * 根据docId获得对应文档的完全路径名
     *
     * @param docId ：文档id
     * @return : 对应文档的完全路径名
     */
    @Override
    public String getDocName(int docId) {
        return this.docIdToDocPathMapping.get(docId);
    }

    /**
     * 写到二进制文件
     *
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(docIdToDocPathMapping);
            out.writeObject(termToPostingListMapping);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从二进制文件读
     *
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            this.docIdToDocPathMapping = (Map<Integer, String>)in.readObject();
            this.termToPostingListMapping = (Map<AbstractTerm, AbstractPostingList>)in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
