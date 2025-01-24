import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.interfaces.bank.BankTabs

onCommand("emptybank", Privilege.DEV_POWER, description = "Empty your bank") {
    player.bank.removeAll()
    for (i in 1..9) {
        player.setVarbit(BankTabs.BANK_TAB_ROOT_VARBIT + i, 0)
    }
}
