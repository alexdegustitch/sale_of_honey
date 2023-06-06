package com.example.medenjaci.util;

public class CartItem {
    private int imageResource;
    private String name, part1, part2;

    public CartItem(int imageResource, String name, String part1, String part2) {
        this.imageResource = imageResource;
        this.name = name;
        this.part1 = part1;
        this.part2 = part2;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPart1() {
        return part1;
    }

    public void setPart1(String part1) {
        this.part1 = part1;
    }

    public String getPart2() {
        return part2;
    }

    public void setPart2(String part2) {
        this.part2 = part2;
    }
}
