package com.example.myexpense;

public class ExpenseDetails {

    int image;
    String text;

    public ExpenseDetails(int image, String text) {
        this.image = image;
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public String getText() {
        return text;
    }
}
