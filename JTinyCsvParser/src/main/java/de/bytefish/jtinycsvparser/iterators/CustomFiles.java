/*
 * Copyright (c) 2012, 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package de.bytefish.jtinycsvparser.iterators;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class CustomFiles {

    public static Stream<String> lines(Path path, Charset cs) throws IOException {
        // Use the good splitting spliterator if:
        // 1) the path is associated with the default file system;
        // 2) the character set is supported; and
        // 3) the file size is such that all bytes can be indexed by int values
        //    (this limitation is imposed by ByteBuffer)
        if (path.getFileSystem() == FileSystems.getDefault() && FileChannelLinesSpliterator.SUPPORTED_CHARSET_NAMES.contains(cs.name())) {
            FileChannel fc = FileChannel.open(path, StandardOpenOption.READ);

            Stream<String> fcls = createFileChannelLinesStream(fc, cs);
            if (fcls != null) {
                return fcls;
            }
            fc.close();
        }
        // Invoke the old implementation:
        return Files.lines(path, cs);
    }


    private static Stream<String> createFileChannelLinesStream(FileChannel fc, Charset cs) throws IOException {
        try {
            // Obtaining the size from the FileChannel is much faster
            // than obtaining using path.toFile().length()
            long length = fc.size();
            if (length <= Integer.MAX_VALUE) {
                Spliterator<String> s = new FileChannelLinesSpliterator(fc, cs, 0, (int) length);
                return StreamSupport.stream(s, false)
                        .onClose(asUncheckedRunnable(fc));
            }
        } catch (Error | RuntimeException | IOException e) {
            try {
                fc.close();
            } catch (IOException ex) {
                try {
                    e.addSuppressed(ex);
                } catch (Throwable ignore) {
                }
            }
            throw e;
        }
        return null;
    }

    private static Runnable asUncheckedRunnable(Closeable c) {
        return () -> {
            try {
                c.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }
}