package baiscKnowledge;

import android.database.DefaultDatabaseErrorHandler;

import org.junit.Test;

import baiscKnowledge.innerclassTest.ClassInInterface;
import baiscKnowledge.innerclassTest.Contents;
import baiscKnowledge.innerclassTest.Destination;
import baiscKnowledge.innerclassTest.DotNew;
import baiscKnowledge.innerclassTest.DotThis;
import baiscKnowledge.innerclassTest.Parcel1;
import baiscKnowledge.innerclassTest.Parcel2;
import baiscKnowledge.innerclassTest.Parcel4;
import baiscKnowledge.innerclassTest.Parcel5;
import baiscKnowledge.innerclassTest.ParcelNestedClass;
import baiscKnowledge.innerclassTest.Selector;
import baiscKnowledge.innerclassTest.Sequence;
import baiscKnowledge.innerclassTest.Warpping;

/**
 * Created by oye on 2/13/2017.
 */

public class InnerClassTest {

    @Test
    public void test_parcel1() throws Exception {
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
        Sequence sequence = new Sequence(10);
        for (int i = 0; i < 10 ; i++) {
            sequence.add(Integer.toString(i));
        }

        Selector selector = sequence.selector();

        while (!selector.end()){
            System.out.println(selector.current().toString());
            selector.next();
        }
    }

    @Test
    public void test_dotThis() throws Exception {
        DotThis dotThis = new DotThis();
        DotThis.Inner inner = dotThis.inner();
        inner.outer().f();
    }

    @Test
    public void test_dotNew() throws Exception {
        DotNew dotNew = new DotNew();
        //一个新的语法，dotNew.new  创建对象dotNew的内部对象
        DotNew.Inner ndi= dotNew.new Inner();
        //DotNew.Inner ndi2= new DotNew.Inner() 内部类对象只能依赖外部类而存在，所以必须以外部类的对象的身份去创建
    }

    @Test
    public void test_Parcel4WithInterfaceHide() throws Exception {
        Parcel4 p4 = new Parcel4();
        Contents c = p4.contents();
        Destination d = p4.destination("china");
    }

    @Test
    public void test_Parcel5() throws Exception {
        Parcel5 p5 = new Parcel5();
        Destination d = p5.destination("china");
        Contents c =p5.contents();
        Warpping w = p5.warpping(10);
        int i =w.value();
    }

    @Test
    public void test_NestedClass() throws Exception {

        ParcelNestedClass p =new ParcelNestedClass();
        Contents c=ParcelNestedClass.contents();
        Destination d = ParcelNestedClass.destination();

        //
        new ClassInInterface.Test().howy();

    }
}
