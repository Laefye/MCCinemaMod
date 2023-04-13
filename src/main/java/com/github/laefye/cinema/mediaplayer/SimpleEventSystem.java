package com.github.laefye.cinema.mediaplayer;

import java.util.ArrayList;
import java.util.List;

public class SimpleEventSystem {
    private final ArrayList<Object> _events = new ArrayList<>();

    public SimpleEventSystem() {

    }

    public void add(Object element) {
        _events.add(element);
    }

    public List<Object> get(Class clazz) {
        var list = new ArrayList<>();
        for (var event : _events) {
            if (clazz.isAssignableFrom(event.getClass())) {
                list.add(event);
            }
        }
        return list;
    }
}
