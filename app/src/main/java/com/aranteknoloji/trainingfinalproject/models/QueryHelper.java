package com.aranteknoloji.trainingfinalproject.models;

public class QueryHelper {

    private static String query = "";

    public static void setQuery(String query) {
        QueryHelper.query = query;
    }

    public static String getQuery() {
        return query;
    }

    public static String getQueryWithPlus() {
        return query.replaceAll(" ", "+") + "+in+US";
    }
}
