package org.alter.plugins.content.objects.crates

private val CRATES =
    setOf(
        "object.crate_354",
        "object.crate_355",
        "object.crate_356",
        "object.crate_357",
        "object.crate_358",
        "object.crate_366",
        "object.crate_1990",
        "object.crate_1999",
        "object.crate_2064",
    )

CRATES.forEach { crate ->
    onObjOption(obj = crate, option = "search") {
        player.message("You search the crate but find nothing.")
    }
}
