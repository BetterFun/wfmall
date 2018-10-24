package com.cug.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Administrator on 2018/10/8 0008.
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
