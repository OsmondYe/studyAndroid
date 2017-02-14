package baiscKnowledge.innerclassTest;

import baiscKnowledge.InnerClassTest;

/**
 * Created by oye on 2/14/2017.
 */
public class Parcel1 {
    class Contents {
        private int i = 11;

        public int value() {
            return i;
        }
    }

    class Destination {
        private String label;

        Destination(String whereTo) {
            this.label = whereTo;
        }

        String readLabel() {
            return label;
        }
    }

    public void ship(String dest) {
        Contents c = new Contents();
        Destination d = new Destination(dest);
        System.out.println(d.readLabel());
    }
}
