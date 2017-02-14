package baiscKnowledge.innerclassTest;

/**
 * Created by oye on 2/14/2017.
 */

public class Parcel4 {
    private class PContents implements Contents{
        private int i=11;
        @Override
        public int value() {
            return i;
        }
    }

    protected class PDestination implements Destination{
        private String label;

        public PDestination(String whereTo) {
            this.label = whereTo;
        }

        @Override
        public String readLabel() {
            return label;
        }
    }

    public Destination destination(String s){
        return new PDestination(s);
    }

    public Contents contents(){
        return new PContents();
    }
}
