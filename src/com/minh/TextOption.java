package com.minh;

import java.awt.*;

public class TextOption {
    private String fieldName;
    private String text;
    private Color textColor;
    private String textAlign;

    public TextOption(String fieldName, String text, Color textColor, String textAlign) {
        this.fieldName = fieldName;
        this.text = text;
        this.textColor = textColor;
        this.textAlign = textAlign;
    }

    public String getFieldName() {
        return fieldName;
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
