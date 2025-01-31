onItemEquip("item.mind_shield") {
    player.queue {
        player.animate(-1)
        player.graphic(-1)
        player.animate(3996, 3)
        player.graphic(809, 90, 3)
    }
}
