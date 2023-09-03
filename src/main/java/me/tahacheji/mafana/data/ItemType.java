package me.tahacheji.mafana.data;

public enum ItemType {

    SWORD("Sword"),
    TOOL("Tool"),
    BOW("Bow"),
    ITEM("Item"),
    BOOTS("Boots"),
    ARMOR("Armor"),
    LEGGGINGS("Leggings"),
    CHESTPLATE("Chestplate"),
    HELMET("Helmet"),
    SPELL("Spell"),
    STAFF("Staff"),
    MATERIAL( "Material");


    private final String lore;

    ItemType(String lore) {
        this.lore = lore;
    }

    public String getLore() {
        return lore;
    }




}
