package com.jju.feng.anti_theft.domain;

/**
 * =============================================================================
 */
public class AntivirusInfo {
    private String md5;
    private String name;

    public AntivirusInfo() {
    }

    public AntivirusInfo(String md5, String name) {
        this.md5 = md5;
        this.name = name;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AntivirusInfo{" +
                "md5='" + md5 + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
