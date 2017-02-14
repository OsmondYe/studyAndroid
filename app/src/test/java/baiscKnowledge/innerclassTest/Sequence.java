package baiscKnowledge.innerclassTest;

/**
 * Created by oye on 2/14/2017.
 */

public class Sequence {
    private Object[] itmes;
    private int next = 0;

    public Sequence(int size) {
        itmes = new Object[size];
    }

    public void add(Object x) {
        if (next < itmes.length) {
            itmes[next++] = x;
        }
    }

    private class SequenceSelector implements Selector {
        private int i = 0;

        @Override
        public boolean end() {
            return i == itmes.length;
        }

        @Override
        public Object current() {
            return itmes[i];
        }

        @Override
        public void next() {
            if (i < itmes.length) {
                i++;
            }
        }
    }

    public Selector selector(){
        //内部类 一定是通过外部类以某种方式创建出来的，所以内部类会隐藏的维护一个指向外部类的指针
        return new SequenceSelector();
    }

}
