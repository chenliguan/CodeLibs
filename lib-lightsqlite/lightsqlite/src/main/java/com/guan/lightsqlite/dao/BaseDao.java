package com.guan.lightsqlite.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.guan.lightsqlite.annotion.DbFiled;
import com.guan.lightsqlite.annotion.DbTable;
import com.guan.lightsqlite.helper.Condition;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
public abstract class BaseDao<T> implements IBaseDao<T> {
    // 持有数据库操作类的引用
    private SQLiteDatabase database;
    // 保证实例化一次
    private boolean isInit = false;
    // 持有操作数据库表所对应的java类型,eg：UserBean
    private Class<T> entityClass;
    /**
     * 维护数据库列表名与成员变量的映射关系
     * key   ---> 数据库列表名
     * value ---> 成员变量，Field
     */
    private HashMap<String, Field> relationMap;

    private String tableName;

    public String getTableName() {
        return tableName;
    }

    /**
     * 创建表
     */
    protected abstract String createTable();

    /**
     * 实例化
     */
    public synchronized boolean init(Class<T> entity, SQLiteDatabase sqLiteDatabase) {
        if (!isInit) {
            entityClass = entity;
            database = sqLiteDatabase;
            // 通过反射机制获取(DbTable)注解元素的值
            if (entity.getAnnotation(DbTable.class) == null) {
                // 取类名（user）
                tableName = entity.getClass().getSimpleName();
            } else {
                // 取注解名字（tb_user）
                tableName = entity.getAnnotation(DbTable.class).value();
            }
            if (!database.isOpen()) {
                return false;
            }
            if (!TextUtils.isEmpty(createTable())) {
                database.execSQL(createTable());
            }

            initMappingRelations();

            isInit = true;
        }
        return isInit;
    }

    /**
     * 初始化映射关系
     */
    private void initMappingRelations() {
        relationMap = new HashMap<>();
        // 第一条数据  查0个数据
        String sql = "select * from " + this.tableName + " limit 1 , 0";
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(sql, null);
            // 数据库表的列名数组
            String[] columnNames = cursor.getColumnNames();
            // 获取entityClass所有公有的成员变量数组
            Field[] variablesFileds = entityClass.getFields();
            for (Field filed : variablesFileds) {
                filed.setAccessible(true);
            }

            // 开始找对应关系
            // 遍历数据库表的列名数组
            for (String colmunName : columnNames) {
                Field variablesFiled = null;
                // 遍历成员变量数组
                for (Field field : variablesFileds) {
                    String fieldName = null;
                    // 通过反射机制获取成员变量注解元素的名字
                    if (field.getAnnotation(DbFiled.class) == null) {
                        fieldName = field.getName();
                    } else {
                        fieldName = field.getAnnotation(DbFiled.class).value();
                    }
                    // 如果数据库表的列名 == 成员变量的注解名字,找到了对应关系
                    if (colmunName.equals(fieldName)) {
                        // 赋值成员变量(java.lang.String com.example.administrator.dongnaosqlite.model.UserBean.name)
                        variablesFiled = field;
                        break;
                    }
                }
                // 找到了对应关系
                if (variablesFiled != null) {
                    // 给映射表添加关系，key:数据库表的列名、value:成员变量
                    relationMap.put(colmunName, variablesFiled);
                }
            }
        } catch (Exception ignored) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 插入
     */
    @Override
    public Long insert(T entity) {
        Map<String, String> valueMap = getColumnValueMap(entity);
        ContentValues contentValues = getContentValues(valueMap);
        return database.insert(tableName, null, contentValues);
    }

    /**
     * 查询
     */
    @Override
    public List<T> query(T where) {
        return query(where, null, null, null);
    }

    @Override
    public List<T> query(T where, String orderBy, Integer startIndex, Integer limit) {
        String limitString = null;
        if (startIndex != null && limit != null) {
            limitString = startIndex + " , " + limit;
        }

        Map valueMap = getColumnValueMap(where);
        Condition condition = new Condition(valueMap);
        Cursor cursor = database.query(tableName, null, condition.getWhereClause(),
                condition.getWhereArgs(), null, null, orderBy, limitString);
        List<T> result = getQueryResult(cursor, where);
        cursor.close();
        return result;
    }

    /**
     * 获取查询结果
     */
    private List<T> getQueryResult(Cursor cursor, T where) {
        ArrayList items = new ArrayList();
        // 查询的对象
        Object item;

        while (cursor.moveToNext()) {
            try {
                item = where.getClass().newInstance();
                // 遍历映射集合(relationMap)的key：数据库表列名(colmunName)
                for (Object object : relationMap.entrySet()) {
                    Map.Entry entry = (Map.Entry) object;
                    // 得到数据库表列名
                    String colomunName = (String) entry.getKey();
                    // 然后以列名拿到：列名在游标的位置
                    Integer colmunIndex = cursor.getColumnIndex(colomunName);

                    // 获取relationMap里面key对应的value值field：成员变量(Field对象)
                    Field field = (Field) entry.getValue();
                    Class type = field.getType();
                    if (colmunIndex != -1) {
                        if (type == String.class) {
                            // 反射方式赋值（native方法），相当于item.setValue(cursor.getString(colmunIndex));
                            field.set(item, cursor.getString(colmunIndex));
                        } else if (type == Double.class) {
                            field.set(item, cursor.getDouble(colmunIndex));
                        } else if (type == Integer.class) {
                            field.set(item, cursor.getInt(colmunIndex));
                        } else if (type == Long.class) {
                            field.set(item, cursor.getLong(colmunIndex));
                        } else if (type == byte[].class) {
                            field.set(item, cursor.getBlob(colmunIndex));
                        } else {
                            continue;
                        }
                    }
                }
                items.add(item);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return items;
    }

    /**
     * 删除
     */
    @Override
    public int delete(T where) {
        Map valueMap = getColumnValueMap(where);
        Condition condition = new Condition(valueMap);
        return database.delete(tableName, condition.getWhereClause(), condition.getWhereArgs());
    }

    /**
     * 更新
     */
    @Override
    public int update(T entity, T where) {
        int reslut = -1;
        Map valueMap = getColumnValueMap(entity);
        // 将条件对象转换成map
        Map whereClauseMap = getColumnValueMap(where);

        Condition condition = new Condition(whereClauseMap);
        ContentValues contentValues = getContentValues(valueMap);
        reslut = database.update(tableName, contentValues, condition.getWhereClause(), condition.getWhereArgs());
        return reslut;
    }

    /**
     * 将entity拥有的成员变量转换成这样的map集合：key:数据库表的列名、value:成员变量的值，如：tb_name---->"张三"。
     */
    private Map<String, String> getColumnValueMap(T entity) {
        HashMap<String, String> result = new HashMap<>();
        /**
         * 遍历映射集合(relationMap)的values：成员变量(Filed)
         */
        for (Field field : relationMap.values()) {
            String filedKey = null;
            String filedValue = null;
            // 通过反射机制获取成员变量注解元素的名字
            if (field.getAnnotation(DbFiled.class) != null) {
                // 取注解名（user_name）
                filedKey = field.getAnnotation(DbFiled.class).value();
            } else {
                // 取成员变量名（user）
                filedKey = field.getName();
            }
            try {
                if (field.get(entity) == null) {
                    continue;
                }
                // 获取成员变量的值
                filedValue = field.get(entity).toString();

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            // (relationMap)数据库列表名已对应成员变量，成员变量注解名==数据库表的列名
            // 给map集合添加关系，key:数据库表的列名(成员变量注解名)、value:成员变量的值
            result.put(filedKey, filedValue);
        }

        return result;
    }

    /**
     * 将columnValueMap集合（数据库表-->成员变量的值）转换成ContentValues
     */
    private ContentValues getContentValues(Map<String, String> columnValueMap) {
        ContentValues contentValues = new ContentValues();
        Set keys = columnValueMap.keySet();
        for (String key : (Iterable<String>) keys) {
            String value = columnValueMap.get(key);
            if (value != null) {
                contentValues.put(key, value);
            }
        }

        return contentValues;
    }

}
