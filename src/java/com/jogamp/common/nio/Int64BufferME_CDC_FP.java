/*
 * Copyright (c) 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN
 * MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed or intended for use
 * in the design, construction, operation or maintenance of any nuclear
 * facility.
 */
package com.jogamp.common.nio;

import com.jogamp.common.os.Platform;
import java.nio.*;

/**
 * @author Sven Gothel
 * @author Michael Bien
 */
final class Int64BufferME_CDC_FP extends Int64Buffer {

    private IntBuffer pb;

    Int64BufferME_CDC_FP(ByteBuffer bb) {
        super(bb);
        this.pb = bb.asIntBuffer();

        capacity = bb.capacity() / elementSize();

        position = 0;
        backup = new long[capacity];
    }

    public final long get(int idx) {
        if (0 > idx || idx >= capacity) {
            throw new IndexOutOfBoundsException();
        }
        idx = idx << 1; // 8-byte to 4-byte offset
        long lo = 0x00000000FFFFFFFFL & ((long) pb.get(idx));
        long hi = 0x00000000FFFFFFFFL & ((long) pb.get(idx + 1));
        if (Platform.isLittleEndian()) {
            return hi << 32 | lo;
        }
        return lo << 32 | hi;
    }

    public final AbstractLongBuffer put(int idx, long v) {
        if (0 > idx || idx >= capacity) {
            throw new IndexOutOfBoundsException();
        }
        backup[idx] = v;
        idx = idx << 1; // 8-byte to 4-byte offset
        int lo = (int) ((v) & 0x00000000FFFFFFFFL);
        int hi = (int) ((v >> 32) & 0x00000000FFFFFFFFL);
        if (Platform.isLittleEndian()) {
            pb.put(idx, lo);
            pb.put(idx + 1, hi);
        } else {
            pb.put(idx, hi);
            pb.put(idx + 1, lo);
        }
        return this;
    }
}
