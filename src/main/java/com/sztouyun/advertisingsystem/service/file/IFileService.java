package com.sztouyun.advertisingsystem.service.file;


import java.io.InputStream;

public interface IFileService {
    String upload(InputStream inputStream ,Long size ,String contentType ,String fileName);

    void delete(String fileName);
}
