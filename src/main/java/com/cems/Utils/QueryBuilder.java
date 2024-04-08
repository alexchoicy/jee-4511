package com.cems.Utils;

import java.util.ArrayList;

public class QueryBuilder {
    private ArrayList<String> columns;
    private ArrayList<String> tables;
    private ArrayList<String> conditions;
    private ArrayList<String> options;

    public QueryBuilder() {
        columns = new ArrayList<>();
        tables = new ArrayList<>();
        conditions = new ArrayList<>();
        options = new ArrayList<>();
    }

    public QueryBuilder select(String column) {
        columns.add(column);
        return this;
    }

    public QueryBuilder from(String table) {
        tables.add(table);
        return this;
    }

    public QueryBuilder where(String condition) {
        conditions.add(condition);
        return this;
    }
    public QueryBuilder options(String column) {
        options.add(column);
        return this;
    }

    public String build() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        for (int i = 0; i < columns.size(); i++) {
            query.append(columns.get(i));
            if (i < columns.size() - 1) {
                query.append(", ");
            }
        }
        query.append(" FROM ");
        for (int i = 0; i < tables.size(); i++) {
            query.append(tables.get(i));
            if (i < tables.size() - 1) {
                query.append(", ");
            }
        }
        if (!conditions.isEmpty()) {
            query.append(" WHERE ");
            for (int i = 0; i < conditions.size(); i++) {
                query.append(conditions.get(i));
                if (i < conditions.size() - 1) {
                    query.append(" AND ");
                }
            }
        }
        query.append(" ");
        for (int i = 0; i < options.size(); i++) {
            query.append(options.get(i));
            if (i < options.size() - 1) {
                query.append(", ");
            }
        }
        return query.toString();
    }

}