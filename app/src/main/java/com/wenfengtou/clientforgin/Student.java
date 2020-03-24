package com.wenfengtou.clientforgin;

import java.io.IOException;
import java.io.Serializable;

public class Student extends Person implements Serializable {
    private static final long serialVersionUID = 1573998989139402737L;
    String school;


    // 反序列化
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        // 恢复性别信息
        school = in.readUTF();
        name = in.readUTF();
        age = in.readInt();
    }


    public Student(String name, int age, String school) {
        this.name = name;
        this.age = age;
        this.school = school;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", school='" + school + '\'' +
                '}';
    }
}
