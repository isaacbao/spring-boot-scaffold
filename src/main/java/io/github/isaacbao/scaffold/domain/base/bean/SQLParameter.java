package io.github.isaacbao.scaffold.domain.base.bean;

/**
 * 就是一个用来放mysql查询参数的java bean
 * 注意：一个name只能对一个value，不要出现一条sql语句中有同一个name对多个value的情况
 * Created by rongyang_lu on 2017/7/10.
 */
public class SQLParameter {
    private String name;
    private Object value;

    public SQLParameter(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "SQLParameter{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
