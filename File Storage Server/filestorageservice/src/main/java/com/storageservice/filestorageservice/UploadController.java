package com.storageservice.filestorageservice;

import com.storageservice.filestorageservice.resourceassistant.ResourceAssistant;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class UploadController {
    @RequestMapping(value = "/streamdata", method = RequestMethod.PUT)
    public void streamuploader(HttpServletRequest request) throws IOException {
        int maxChunkSize = 100;
        int defaultBufferSize = 1*1024*1024;
        StreamBuffer streamBuffer = new StreamBuffer(maxChunkSize, defaultBufferSize, request.getInputStream());
        ResourceAssistant resAssistant  = new ResourceAssistant(2000, 4, streamBuffer);
        int dataRead = 0;
        resAssistant.startMonitoring();
        while((dataRead = streamBuffer.buffer())>0){
            //pass
        }
        resAssistant.stopMonitoring();
        System.out.println("Completed");
    }
}
