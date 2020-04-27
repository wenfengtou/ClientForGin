package com.wenfengtou.clientforgin.Util;

import android.view.View;

import com.wenfengtou.clientforgin.proxy.WindowManagerGlabalViews;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class HookUtil {

    public static void hookWindowManagerGlobal() {
        Class<?> cls = null;
        try {
            cls = Class.forName("android.view.WindowManagerGlobal");
            Method method = cls.getMethod("getInstance");
            Object invoke = method.invoke(null);
            Field field = cls.getDeclaredField("mViews");
            field.setAccessible(true);
            ArrayList<View> views = (ArrayList<View>) field.get(invoke);
            WindowManagerGlabalViews windowManagerGlabalViews = new WindowManagerGlabalViews(views);
            field.set(invoke, windowManagerGlabalViews);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }  catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }
}
