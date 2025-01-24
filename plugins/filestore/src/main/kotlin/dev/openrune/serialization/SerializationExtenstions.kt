package dev.openrune.serialization

import kotlinx.serialization.Serializable

typealias ListRscm = @Serializable(with  = RscmList::class) MutableList<Int>
typealias Rscm = @Serializable(with  = RscmString::class) Int