package org.alter.plugins.content.objects.sacks

private val SACKS = setOf("object.sacks_365")

SACKS.forEach { sack ->
    onObjOption(obj = sack, option = "search") {
        player.message("There's nothing interesting in these sacks.")
    }
}
