/**
 * Copyright (c) 2010-2011 Graham Edgecombe
 * Copyright (c) 2011-2016 Major <major.emrs@gmail.com> and other apollo contributors
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package gg.rsmod.net.packet

/**
 * Represents the order of bytes in a [DataType] when [DataType.getBytes] `> 1`.
 *
 * @author Graham
 */
enum class DataOrder {

    /**
     * Most significant byte to least significant byte.
     */
    BIG,

    /**
     * Also known as the V2 order.
     */
    INVERSE_MIDDLE,

    /**
     * Least significant byte to most significant byte.
     */
    LITTLE,

    /**
     * Also known as the V1 order.
     */
    MIDDLE,

    /**
     * Used exclusively for [DataType.BYTES] written in reverse order
     */
    REVERSED

}