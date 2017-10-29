package com.guan.lightsqlite.helper;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * 封装条件--值语句
 */
public class Condition {
    /**
     * 查询条件
     * where子句：name=? and password =?
     */
    private String whereClause;
    /**
     * 查询条件的值
     */
    private String[] whereArgs;

    /**
     * Condition
     * @param columnValueMap 列表名--列表值映射
     */
    public Condition(Map<String, String> columnValueMap) {
        ArrayList whereValues = new ArrayList();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(" 1=1 ");
        Set keys = columnValueMap.keySet();
        for (Object key1 : keys) {
            String key = (String) key1;
            String value = columnValueMap.get(key);

            if (value != null) {
                // 拼接条件查询语句：1=1 and name =? and password=?
                stringBuilder.append(" and " + key + " =?");
                // ? ---> value
                whereValues.add(value);
            }
        }
        this.whereClause = stringBuilder.toString();
        this.whereArgs = (String[]) whereValues.toArray(new String[whereValues.size()]);
    }

    public String[] getWhereArgs() {
        return whereArgs;
    }

    public String getWhereClause() {
        return whereClause;
    }
}
