
/**
 * @author Fritz <frikkipafi@gmail.com>
 */
on_button(AccountTab.BUTTON_ID, AccountTab.UPGRADE_NOW_ID){
    //player.openUrl("https://github.com/AlterRSPS/Alter")
    player.message("Placeholder for upgrade now button")
}
on_button(AccountTab.BUTTON_ID, AccountTab.BENEFITS_ID){
    //player.openUrl("https://github.com/AlterRSPS/Alter")
    player.message("Placeholder for benefits button")
}
on_button(AccountTab.BUTTON_ID, AccountTab.INBOX_ID){
    //player.openUrl("https://github.com/AlterRSPS/Alter")
    player.message("Placeholder for inbox button")
}
on_button(AccountTab.BUTTON_ID, AccountTab.NAME_CHANGER_ID) {
    player.openInterface(interfaceId = 589, dest = InterfaceDestination.TAB_AREA)
    player.setComponentText(interfaceId = 589, component = 6, text = "Next free change:")
    player.setComponentText(interfaceId = 589, component = 7, text = "Now!") // Make this a method to pull last updated date from your database, return that date, or "Now!"
    player.setInterfaceEvents(interfaceId = 589, component = 18, range = 0..9, setting = 0)
    player.setVarbit(5605, 1)
}
/*
on_button(AccountTab.BUTTON_ID, AccountTab.POLL_ID){
    player.openUrl("https://github.com/AlterRSPS/Alter")
    player.message("Placeholder for poll button")
}*/
on_button(AccountTab.BUTTON_ID, AccountTab.HISTORY_ID){
    //player.openUrl("https://github.com/AlterRSPS/Alter")
    player.message("Placeholder for history button")
}
on_button(AccountTab.BUTTON_ID, AccountTab.NEWS_ID){
    //player.openUrl("https://github.com/AlterRSPS/Alter")
    player.message("Placeholder for news button")
}
on_button(AccountTab.BUTTON_ID, AccountTab.ARCHIVE_ID){
    //player.openUrl("https://github.com/AlterRSPS/Alter")
    player.message("Placeholder for archive button")
}
on_button(AccountTab.BUTTON_ID, AccountTab.WEBSITE_ID){
    //player.openUrl("https://github.com/AlterRSPS/Alter")
    player.message("Placeholder for website button")
}
on_button(AccountTab.BUTTON_ID, AccountTab.GE_ID){
    //player.openUrl("https://github.com/AlterRSPS/Alter")
    player.message("Placeholder for ge button")
}
on_button(AccountTab.BUTTON_ID, AccountTab.WIKI_ID){
    //player.openUrl("https://github.com/AlterRSPS/Alter")
    player.message("Placeholder for wiki button")
}
on_button(AccountTab.BUTTON_ID, AccountTab.SUPPORT_ID){
    //player.openUrl("https://github.com/AlterRSPS/Alter")
    player.message("Placeholder for support button")
}
on_button(AccountTab.BUTTON_ID, AccountTab.HISCORE_ID){
    //player.openUrl("https://github.com/AlterRSPS/Alter")
    player.message("Placeholder for hiscore button")
}
on_button(AccountTab.BUTTON_ID, AccountTab.MERCH_ID){
    //player.openUrl("https://github.com/AlterRSPS/Alter")
    player.message("Placeholder for merch button")
}