package com.ericlam.mc.mvvm.exampletest;

import org.jetbrains.annotations.TestOnly;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@TestOnly
public class TestMapLimit {

    public static void main(String[] args) {

        LinkedList<String> list = new LinkedList<>();
        push(list, "123");
        push(list, "456");
        push(list, "789");
        push(list, "abc");
        push(list, "xyz");
        push(list, "efg");



    }

    static void push(LinkedList<String> list, String msg){
        if (list.size() == 2){
            list.removeFirst();
        }
        list.add(msg);
        System.out.println(list);
    }

}
