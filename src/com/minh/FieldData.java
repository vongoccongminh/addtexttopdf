package com.minh;

import java.awt.*;

public class FieldData {
    private String name;
    private String text;
    private Color textColor;
    private String textAlign;

    public FieldData(String name, String text, Color textColor, String textAlign) {
        this.name = name;
        this.text = text;
        this.textColor = textColor;
        this.textAlign = textAlign;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public Color getTextColor() {
        return textColor;
    }

    public String getTextAlign() {
        return textAlign;
    }
}
