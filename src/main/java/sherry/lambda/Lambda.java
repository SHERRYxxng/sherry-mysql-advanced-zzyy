package sherry.lambda;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @Description:
 * @Author: SHERRY
 * @email: <a href="mailto:SherryTh743779@gmail.com">TianHai</a>
 * @Date: 2023/6/17 10:05
 */
public class Lambda {
    public static void main(String[] args) {

        Function function = new Function() {
            @Override
            public Object apply(Object o) {
                return "笨蛋";
            }
        };
        System.out.println(function.apply("a"));


        //因为new 出来的对象是函数式接口,接口中只有一个方法因此可以直接提取那唯一一个方法的参数
        Function functionlambda = (Object x) -> {
            return "笨蛋";
        };
        System.out.println(functionlambda.apply("a"));
        //因为参数只有一个所以可以省略参数的类型{},由于方法内的语句只有一条所以可以省略,由于是return所以又能省略
        Function function1 = o -> "笨蛋";
        System.out.println(function1.apply("a"));



        Consumer consumer=new Consumer() {
            @Override
            public void accept(Object o) {
                System.out.println("笨蛋");
            }
        };
        consumer.accept("Hello World");
        Consumer consumer1=(Object o)->{System.out.println("笨蛋");};
        consumer.accept("Hello World");
        Consumer consumer2=o->System.out.println("笨蛋");
        consumer.accept("Hello World");


        Predicate predicate=new Predicate() {
            @Override
            public boolean test(Object o) {
                if(o.equals("春天")){
                    return true;
                }
                return false;
            }
        };
        System.out.println(predicate.test("夏天"));
        Predicate predicate1=o->o.equals("春天");
        System.out.println(predicate1.test("春天"));
    }

}
