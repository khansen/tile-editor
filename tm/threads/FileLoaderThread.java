/*
*
*    Copyright (C) 2003 Kent Hansen.
*
*    This file is part of Tile Manipulator.
*
*    Tile Manipulator is free software; you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation; either version 2 of the License, or
*    (at your option) any later version.
*
*    Tile Manipulator is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*/

package tm.threads;

import java.io.*;

/**
*
* Thread for reading a file into a buffer.
*
**/

public class FileLoaderThread extends ProgressThread {

    private static final int CHUNK_SIZE = 16384;
    BufferedInputStream bis=null;
    private int bytesLeft;
    private byte[] contents;
    private IOException failure;

    public FileLoaderThread(File file) throws OutOfMemoryError, FileNotFoundException {
        super();
        try {
            contents = new byte[(int)file.length()];
        }
        catch (OutOfMemoryError e) {
            throw e;
        }
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw e;
        }
        bytesLeft = contents.length;
        this.setPriority(NORM_PRIORITY);
    }

    public int getPercentageCompleted() {
        if (contents.length == 0) return 100;
        int result = (int)((long)(((long)contents.length - (long)bytesLeft) * 100) / (long)contents.length);
        return result;
    }

    public void run() {
        try {
            while (bytesLeft > 0) {
                int chunkSize = Math.min(bytesLeft, CHUNK_SIZE);
                int offset = contents.length - bytesLeft;
                int bytesRead = bis.read(contents, offset, chunkSize);
                if (bytesRead < 0) {
                    throw new EOFException("Unexpected end of file.");
                }
                if (bytesRead == 0) {
                    throw new IOException("Read returned 0 bytes without reaching EOF.");
                }
                bytesLeft -= bytesRead;
                Thread.yield();
            }
        }
        catch (IOException e) {
            failure = e;
            bytesLeft = 0;
        }
        finally {
            try {
                bis.close();
            } catch (Exception e) { }
            // done loading data
        }
    }

    public IOException getFailure() {
        return failure;
    }

    public byte[] getContents() {
        return contents;
    }

    public void killContentsRef() {
        contents = null;
    }

}
