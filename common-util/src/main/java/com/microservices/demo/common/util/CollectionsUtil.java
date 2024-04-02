package com.microservices.demo.common.util;

import java.util.ArrayList;
import java.util.List;

// Singleton pattern -> Initialization on-demand holder provides thread-safe and lazy approach
public class CollectionsUtil {

    // Construtor private para criar um Singleton
    private CollectionsUtil() {
    }

    // Inner class que cria uma instância de CollectionsUtil.class
    private static class CollectionsUtilHolder {
        static final CollectionsUtil INSTANCE = new CollectionsUtil();
    }

    public static CollectionsUtil getInstance() {
        return CollectionsUtilHolder.INSTANCE;
    }

    // pode ser utilizado com qualquer tipo, pois utilizamos tipos genéricos aqui
    public <T> List<T> getListFromIterable(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

}
