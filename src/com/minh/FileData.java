package com.minh;

import java.util.List;

public class FileData {
    List<FieldData> fieldData;

    public FileData(List<FieldData> fieldData) {
        this.fieldData = fieldData;
    }

    public List<FieldData> getFieldData() {
        return fieldData;
    }
}
