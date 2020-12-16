package com.honey.mediaserv;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("media")
public class MediaController {
    @GetMapping("test")
    public String test(){
        return "All fine keep going";
    }
    @GetMapping("{filename}")
    public ResponseEntity<StreamingResponseBody> mediaGet(@PathVariable String filename) {
        String path = "/home/leonaxico/Downloads/";
        path = path.concat(filename);
        System.out.println(path);
        File video = new File(path);
        StreamingResponseBody stream = out -> {
            out.write(loadFile(video));
        };
        return new ResponseEntity(stream, HttpStatus.OK);
    }
    private byte[] loadFile(File file) throws IOException {
        FileInputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        return bytes;
    }
}
