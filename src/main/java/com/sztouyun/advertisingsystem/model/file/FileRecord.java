package com.sztouyun.advertisingsystem.model.file;

import com.sztouyun.advertisingsystem.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
public class FileRecord extends BaseModel {
    @Column(nullable = false, length=200)
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
