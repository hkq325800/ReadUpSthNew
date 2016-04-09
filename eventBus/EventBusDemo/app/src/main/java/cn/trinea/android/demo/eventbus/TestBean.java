package cn.trinea.android.demo.eventbus;

/**
 * Created by Administrator on 2016/3/28 0028.
 */
public class TestBean {
    public String name = "1";
    public int count = 1;
    public Double dou = 0.01;
    public TestBean test;

    public TestBean(TestBean test){
        this.test = test;
    }
}
