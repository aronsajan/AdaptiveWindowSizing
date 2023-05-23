package com.storageservice.filestorageservice;

import java.io.IOException;
import java.io.InputStream;

public class StreamBuffer {
    private int maxChunkSize;

    private int totalDataRead = 0;

    private int bufferSize;

    private byte[] readBuffer;

    private InputStream inputStream;

    public StreamBuffer(int maxChunkSize, int bufferSize, InputStream inputStream){
        this.maxChunkSize = maxChunkSize;
        this.bufferSize = bufferSize;
        this.inputStream = inputStream;
    }

    public int buffer() throws IOException {
        this.readBuffer = new byte[this.bufferSize];
        System.out.println("Buffer Size : "+this.bufferSize);
        System.gc();
        return this.buffer(this.readBuffer, this.inputStream, this.maxChunkSize, this.bufferSize);
    }

    public void updateBufferSize(int newBufferSize){
        if(bufferSize>1000) {
            this.bufferSize = newBufferSize;
        } else {
            this.bufferSize = 1000;
        }
    }

    public int getTotalDataRead(){
        return this.totalDataRead;
    }


    public byte[] getBuffer(){
        return this.readBuffer;
    }

    private int buffer(byte[] buffer, InputStream dataStream, int chunkSize, int bufferLimit) throws IOException {
        int readOffset = 0;
        int dataRead = 0;
        while(this.proceedNextRead(readOffset, chunkSize, bufferLimit) && (dataRead = dataStream.read(buffer, readOffset, chunkSize))>0){
            readOffset+=dataRead;
            this.totalDataRead+=dataRead;
        }
        return readOffset;
    }

    private boolean proceedNextRead(int readPtr, int expectedChunkOffset, int bufferLimit){
        return readPtr+expectedChunkOffset < bufferLimit;
    }
}
