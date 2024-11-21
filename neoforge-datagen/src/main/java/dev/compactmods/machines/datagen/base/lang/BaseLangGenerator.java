package dev.compactmods.machines.datagen.base.lang;

import dev.compactmods.machines.api.CompactMachines;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseLangGenerator extends LanguageProvider {

    private final String locale;

    public BaseLangGenerator(PackOutput packOutput, String locale) {
        super(packOutput, CompactMachines.MOD_ID, locale);
        this.locale = locale;
    }

    protected String getMachineTranslation() {
        return "Compact Machine";
    }

    @Override
    protected void addTranslations() {}

    protected void addCreativeTab(ResourceLocation id, String translation) {
        add(Util.makeDescriptionId("itemGroup", id), translation);
    }

    protected void advancement(ResourceLocation adv, String title, String desc) {
        add(Util.makeDescriptionId("advancement", adv), title);
        add(Util.makeDescriptionId("advancement", adv) + ".desc", desc != null ? desc : "");
    }
}
