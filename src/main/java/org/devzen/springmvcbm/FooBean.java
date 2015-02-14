package org.devzen.springmvcbm;

/**
 * User: matianyi
 * Date: 13-7-29
 * Time: 下午11:55
 */
public class FooBean {
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    private String name;

    private boolean gender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public FooBean(Integer count, String name, boolean gender) {
        this.count = count;
        this.name = name;
        this.gender = gender;
    }

    public FooBean() {
    }
}
