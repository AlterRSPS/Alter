import org.alter.plugins.content.items.food.Food
import org.alter.plugins.content.items.consumables.food.Foods

Food.values.forEach { food ->
    onItemOption(item = food.item, option = "eat") {
        if (!Foods.canEat(player, food)) {
            return@onItemOption
        }

        val inventorySlot = player.getInteractingItemId()
        if (player.inventory.remove(item = food.item, beginSlot = inventorySlot).hasSucceeded()) {
            Foods.eat(player, food)
            if (food.replacement != -1) {
                player.inventory.add(item = food.replacement, beginSlot = inventorySlot)
            }
        }
    }
}
