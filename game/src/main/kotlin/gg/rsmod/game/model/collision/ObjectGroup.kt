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
package gg.rsmod.game.model.collision

/**
 * The group of an object, which indicates its general class (e.g. if it's a wall, or a floor decoration).
 *
 * @author Major
 * @author Scu11
 */
enum class ObjectGroup(val value: Int) {

    /**
     * The wall object group, which may block a tile.
     */
    WALL(0),

    /**
     * The wall decoration object group, which never blocks a tile.
     */
    WALL_DECORATION(1),

    /**
     * The interactable object group, for objects that can be clicked and interacted with.
     */
    INTERACTABLE_OBJECT(2),

    /**
     * The ground decoration object group, which may block a tile.
     */
    GROUND_DECORATION(3);
}