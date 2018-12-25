package com.example.haya.callplus;

import com.example.haya.callplus.beans.Contact;
import com.google.gson.Gson;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Gson gson = new Gson();
        System.out.print(gson.toJson(new Contact("haya","n","123","256","222","as","as")));
    }
}