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
* Thread for writing a buffer to a file.
*
**/

public class FileSaverThread extends ProgressThread {

    private static final int CHUNK_SIZE = 16384;
    private RandomAccessFile raf=null;
    private int bytesLeft;
    private byte[] contents;
    private IOException failure;

    public FileSaverThread(byte[] contents, File file)
        throws FileNotFoundException, IOException {
        super();
        this.contents = contents;
        try {
            raf = new RandomAccessFile(file, "rw");
            raf.setLength(0);
            raf.seek(0);
        }
        catch (FileNotFoundException e) {
            throw e;
        }
        catch (IOException e) {
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
                raf.write(contents, offset, chunkSize);
                bytesLeft -= chunkSize;
                Thread.yield();
            }
        }
        catch (IOException e) {
            failure = e;
            bytesLeft = 0;
        }
        finally {
            try {
                raf.close();
            } catch (Exception e) { }
            // done saving data
        }
    }

    public IOException getFailure() {
        return failure;
    }

}
