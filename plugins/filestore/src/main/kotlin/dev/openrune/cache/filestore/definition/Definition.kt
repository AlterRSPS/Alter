package dev.openrune.cache.filestore.definition

import dev.openrune.serialization.Rscm

interface Definition {
    var id: Rscm
    var inherit : Rscm
}