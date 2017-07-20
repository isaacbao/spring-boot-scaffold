package io.github.isaacbao.scaffold.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;

/**
 * 实体的基类，所有实体都需要继承该类，实现hashcode equals的基础方法,实现validateable接口
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public abstract class BaseEntity implements Validateable, Serializable {


    public abstract String getId();
    public abstract Date getCreateDate();
    public abstract Date getUpdateDate();

    public boolean equals(Object o) {
        return !(o == null || this.getClass() != o.getClass()) && (o == this || this.getId().equals(((BaseEntity) o).getId()));
    }

    public int hashCode() {
        return this.getId().hashCode();
    }

    public String toString() {
        return this.getClass().getSimpleName() + ",id:" + this.getId();
    }

    /**
     * 加载hibernate延迟加载的数据
     */
    public void loadLazyInitField() {
        Class clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            try {
                Method me = fieldGetMethod(clazz, f.getName());
                Object o = me.invoke(this);
                if (o instanceof Collection) {
                    ((Collection) o).size();
                } else if (o instanceof BaseEntity) {
                    ((BaseEntity) o).getId();
                }
            } catch (NoSuchMethodException e) {
                System.out.println(e.getClass().getName() + ":" + e.getMessage());
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    protected Method fieldGetMethod(Class clazz, String s) throws NoSuchMethodException {
        char[] fieldChar = s.toCharArray();
        if (fieldChar[0] > 96 && fieldChar[0] < 123)
            fieldChar[0] -= 32;
        s = new String(fieldChar);
        try {
            String getMethod = "get" + s;
            return clazz.getMethod(getMethod);
        } catch (NoSuchMethodException e) {
            String getMethod = "is" + s;
            return clazz.getMethod(getMethod);
        }
    }

    /**
     * hibernate validator 校验字段合法性
     */
    public void validate() {
        validate(this);
    }

}

