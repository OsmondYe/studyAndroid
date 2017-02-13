package baiscKnowledge;

import org.junit.Test;

/**
 * Created by oye on 2/13/2017.
 */

public class InnerClassTest {

    public class Parcel1 {
        class Contents {
            private int i=11;

            public int value() {
                return i;
            }
        }
        class Destination {
            private String label;
            Destination(String whereTo){
                this.label =whereTo;
            }
            String readLabel(){
                return label;
            }
        }

        public void ship(String dest){
            Contents c = new Contents();
            Destination d = new Destination(dest);
            System.out.println(d.readLabel());
        }
    }

    public class Parcel2 {
        class Contents {
            private int i=11;

            public int value() {
                return i;
            }
        }
        class Destination {
            private String label;
            Destination(String whereTo){
                this.label =whereTo;
            }
            String readLabel(){
                return label;
            }
        }

        public void ship(String dest){
            Contents c = new Contents();
            Destination d = new Destination(dest);
            System.out.println(d.readLabel());
        }

        public Destination to(String s){
            return new Destination(s);
        }
        public Contents contents(){
            return new Contents();
        }

    }

    @Test
    public void test_basic() throws Exception {
        // for parcel1
        Parcel1 p1 = new Parcel1();
        p1.ship("china");
        // for parcel2
        // 每一个内部类都有一个隐含的this$n 指向其外部类，根据调试信息里面的@后面的数字可以看出来
        Parcel2 p2 = new Parcel2();
        Parcel2.Contents c = p2.contents();
        Parcel2.Destination d = p2.to("China");

        System.out.println("end this case");
    }

    @Test
    public void test_linkToOuterClass() throws Exception {
        // inner class has all access rights to its outer class
    }
}
