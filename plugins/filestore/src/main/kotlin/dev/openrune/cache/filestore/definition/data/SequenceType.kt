package dev.openrune.cache.filestore.definition.data

import dev.openrune.cache.filestore.definition.Definition
import dev.openrune.cache.filestore.definition.Sound
import dev.openrune.cache.filestore.definition.SoundData

data class SequenceType(
    override var id: Int = -1,
    var frameIDs: MutableList<Int>? = null,
    var chatFrameIds: MutableList<Int>? = null,
    var frameDelays: MutableList<Int>? = null,
    var soundEffects: MutableList<SoundData?> = emptyList<SoundData>().toMutableList(),
    var frameStep: Int = -1,
    var interleaveLeave: MutableList<Int>? = null,
    var stretches: Boolean = false,
    var forcedPriority: Int = 5,
    var leftHandItem: Int = -1,
    var rightHandItem: Int = -1,
    var maxLoops: Int = 99,
    var precedenceAnimating: Int = -1,
    var priority: Int = -1,
    var skeletalId: Int = -1,
    var skeletalRangeBegin: Int = -1,
    var skeletalRangeEnd: Int = -1,
    var replyMode: Int = 2,
    var rangeBegin : Int = 0,
    var rangeEnd : Int = 0,
    var sounds: MutableMap<Int, SoundData> = emptyMap<Int, SoundData>().toMutableMap(),
    var mask: MutableList<Boolean>? = null,
    //Custom
    override var inherit: Int = -1
) : Definition, Sound {
    var lengthInCycles = 0
    val cycleLength: Int get() = lengthInCycles
}