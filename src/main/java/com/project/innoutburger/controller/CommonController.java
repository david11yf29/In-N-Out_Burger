package com.project.innoutburger.controller;

import com.project.innoutburger.common.R;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/*
* 文件上傳下載
* */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${innoutburger.path}")
    private String basePath;

    /*
    * 文件上傳
    * @param file
    * @return
    * */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        // file 是一個臨時文件, 需要轉存到指定位置, 否則本次請求後臨時文件就會刪除
        log.info(file.toString());

        // 原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 使用 UUID 重新生成文件名稱, 防止文件名稱重複造成文件覆蓋
        String fileName = UUID.randomUUID().toString() + suffix;

        // 創建一個目錄對象
        File dir = new File(basePath);
        // 判斷當前目錄是否存在
        if(!dir.exists()) {
            // 不存在直接創建一個
            dir.mkdirs();
        }

        try {
            // 將臨時文件轉存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }

    /*
    * 文件下載
    * @param name
    * @param response
    * */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        try {
            // 輸入流, 通過輸入讀取文件內容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            // 輸出流, 通過輸出流將文件寫回瀏覽器, 在瀏覽器即可展示圖片
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            // 關閉資源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
