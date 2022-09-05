package gg.rsmod.plugins.content.skills.woodcutting

import gg.rsmod.plugins.content.skills.woodcutting.Woodcutting.Tree

private val TREES = setOf(
    Tree(TreeType.TREE, obj = 3648, trunk = 3649), //Dying Tree
    Tree(TreeType.ACHEY, obj = 2023, trunk = 3371), //Achey
    Tree(TreeType.TREE, obj = 1278, trunk = 1342),
    Tree(TreeType.TREE, obj = 1276, trunk = 1342),
    Tree(TreeType.TREE, obj = 1286, trunk = 1351), // Dead tree
    Tree(TreeType.TREE, obj = 1282, trunk = 1347), // Dead tree
    Tree(TreeType.TREE, obj = 1383, trunk = 1358), // Dead tree
    Tree(TreeType.TREE, obj = 1289, trunk = 1353), // Dead tree
    Tree(TreeType.TREE, obj = 2091, trunk = 1342), // Evergreen
    Tree(TreeType.WILLOW, obj = 10819, trunk = 8489), // Willow
    Tree(TreeType.WILLOW, obj = 10829, trunk = 9471), // Willow
    Tree(TreeType.WILLOW, obj = 10831, trunk = 9471), // Willow
    Tree(TreeType.WILLOW, obj = 10833, trunk = 9471), // Willow
    Tree(TreeType.OAK, obj = 10820, trunk = 8468), // Oak
    Tree(TreeType.OAK, obj = 4533, trunk = 8468), // Oak
    Tree(TreeType.OAK, obj = 4540, trunk = 8468), // Oak
    Tree(TreeType.OAK, obj = 9734, trunk = 8468), // Oak
    Tree(TreeType.OAK, obj = 8463, trunk = 8468), // Oak
    Tree(TreeType.OAK, obj = 8464, trunk = 8468), // Oak
    Tree(TreeType.OAK, obj = 8465, trunk = 8468), // Oak
    Tree(TreeType.OAK, obj = 8466, trunk = 8468), // Oak
    Tree(TreeType.OAK, obj = 8467, trunk = 8468), // Oak
    Tree(TreeType.OAK, obj = 1751, trunk = 1356), //Oak
    Tree(TreeType.MAPLE, obj = 10832, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 36681, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 36682, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 40754, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 40755, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 4535, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 4674, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 5126, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 8435, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 8436, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 8437, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 8438, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 8439, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 8440, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 8441, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 8442, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 8443, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 8444, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 8445, trunk = 9712), // Maple
    Tree(TreeType.MAPLE, obj = 1759, trunk = 9712), //Maple
    Tree(TreeType.HOLLOW, obj = 1752, trunk = 4061), //Hollow
    Tree(TreeType.MAHOGANY, obj = 9034, trunk = 9035), //Mahogany
    Tree(TreeType.TEAK, obj = 9036, trunk = 9037), //Teak
    Tree(TreeType.YEW, obj = 1753, trunk = 9714),
    Tree(TreeType.YEW, obj = 1754, trunk = 9714),
    Tree(TreeType.YEW, obj = 10822, trunk = 9714),

    Tree(TreeType.MAGIC, obj = 1761, trunk = 9713), //Magic
    Tree(TreeType.REDWOOD, obj = 29668, trunk = 29669),
    Tree(TreeType.REDWOOD, obj = 29670, trunk = 29671)
)

TREES.forEach { tree ->
    on_obj_option(obj = tree.obj, option = 1) {
        val obj = player.getInteractingGameObj()
        player.queue {
            Woodcutting.chopDownTree(this, obj, tree.type, tree.trunk)
        }
    }
}