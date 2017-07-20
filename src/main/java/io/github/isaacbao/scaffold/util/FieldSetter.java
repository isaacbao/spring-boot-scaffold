package io.github.isaacbao.scaffold.util;

import io.github.isaacbao.scaffold.system.annotation.MetaField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


/**
 * @author gzfu
 */
public class FieldSetter {

    static final Logger logger = LogManager.getLogger();

    private FieldSetter() {
    }

    /**
     * 对于hibernate延迟加载的对象，必须手动传递class对象，使用setFields(Object obj, Map<String, String> pm,Class objectClass)方法
     * 使用否则由于hibernate的代理机制，无法正确获取对象的class，导致无法设值
     *
     * @param pm 要往object里set的值
     */
    public static Object setFields(Object obj, Map<String, String> pm) {
        return setFields(obj, pm, null, null);
    }

    /**
     * @param pm 要往object里set的值
     * @param objectClass 对于hibernate延迟加载的对象，必须手动传递class对象，否则由于hibernate的代理机制，
     *                    无法正确获取对象的class，导致无法设值
     */
    public static Object setFields(Object obj, Map<String, String> pm, Class objectClass) {
        return setFields(obj, pm, null, objectClass);
    }

    public static Object setFields(Object obj, Map<String, String> pm, String[] keepFiled) {
        return setFields(obj, pm, keepFiled, null);
    }

    public static Object setFields(Object obj, Map<String, String> pm, String[] keepFiled, Class objectClass) {
        if (obj == null || pm == null)
            return obj;
        Class<?> cl = objectClass == null ? obj.getClass() : objectClass;
        Field[] fields = cl.getDeclaredFields();
        if (keepFiled == null)
            keepFiled = new String[0];

        List<String> keepList = new ArrayList(Arrays.asList(keepFiled));
        keepList.add("id");
        for (int j = 0; j < fields.length; j++) {
            fields[j].setAccessible(true);
            try {
                String fieldName = fields[j].getName();
                if (!keepList.contains(fieldName)) {

                    // 对于标注了MetaField的字段跳过
                    boolean meta = false;
                    for (Annotation a : fields[j].getDeclaredAnnotations()) //getAnnotations 不会运行多次
                        if (a instanceof MetaField)
                            meta = true;
                    if (meta) continue;

                    Class<?> type = fields[j].getType();
                    char[] fieldChar = fieldName.toCharArray();
                    if (fieldChar[0] > 96 && fieldChar[0] < 123)
                        fieldChar[0] -= 32;

                    String setMethod = "set" + new String(fieldChar);

                    logger.debug("set method" + setMethod + ", fieldtype:"+type.getName());
                    //对于枚举字段，调用对象的String参数的枚举setter方法
                    Method me = cl.getMethod(setMethod, type);
                    if (type.isEnum())
                        me = cl.getMethod(setMethod, String.class);
                    String value = pm.get(fieldName);

                    if (value != null) {
                        if (type.equals(String.class) || type.isEnum())
                            me.invoke(obj, getString(value));

                        if ((type.equals(int.class) || type.equals(Integer.class)) && !StringUtil.isEmpty(value))
                            me.invoke(obj, getInt(value));

                        if (type.equals(Date.class) && !StringUtil.isEmpty(value))
                            me.invoke(obj, TimeUtils.getDate(value));

                        if (type.equals(long.class) && !StringUtil.isEmpty(value))
                            me.invoke(obj, getLong(value));

                        if (type.equals(float.class) && !StringUtil.isEmpty(value))
                            me.invoke(obj, getFloat(value));

                        if (type.equals(boolean.class) && !StringUtil.isEmpty(value))
                            me.invoke(obj, getBoolean(value));

                        if (type.equals(double.class) && !StringUtil.isEmpty(value))
                            me.invoke(obj, getDouble(value));

                        if (type.equals(UUID.class) && !StringUtil.isEmpty(value))
                            me.invoke(obj, getUUID(value));
                    }
                }
            } catch (NumberFormatException e) {
                logger.error("", e);
                throw new IllegalArgumentException("number fromat failed:" + e.getMessage());
            } catch (NoSuchMethodException e) {
                logger.error(e.getClass().getName() + e.getMessage());
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("", e);
            }
        }
        return obj;
    }

    /**
     * 去除参数中的script style img a 标签，防止脚本注入
     *
     */
    private static String getString(String value) {
//        RegexUtils.containsPattern("(?i)</?\\s*(script|style|link|img|a)[^>]*>",value);
        return value.replaceAll("(?i)</?\\s*(script|style|link|img|a)[^>]*>", "");
    }

    private static UUID getUUID(String value) {
        return UUID.fromString(value);
    }

    static int getInt(String value) {
        return Integer.parseInt(value);
    }

    static long getLong(String value) {
        return Long.parseLong(value);
    }

    static float getFloat(String value) {
        return Float.parseFloat(value);
    }

    static double getDouble(String value) {
        return Double.parseDouble(value);
    }

    private static Object getBoolean(String value) {
        return Boolean.parseBoolean(value);
    }

}
