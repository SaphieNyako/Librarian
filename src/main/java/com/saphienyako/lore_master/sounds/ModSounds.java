package com.saphienyako.lore_master.sounds;

import com.saphienyako.lore_master.LoreMasterMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, LoreMasterMod.MOD_ID);

    public static final RegistryObject<SoundEvent> BELLSNICKEL_AMBIANCE = registerSoundEvents("bellsnickel_ambiance");
    public static final RegistryObject<SoundEvent> BELLSNICKEL_HURT = registerSoundEvents("bellsnickel_hurt");
    public static final RegistryObject<SoundEvent> BELLSNICKEL_DEATH = registerSoundEvents("bellsnickel_death");
    public static final RegistryObject<SoundEvent> BELLSNICKEL_TRADE = registerSoundEvents("bellsnickel_trade");

    public static final RegistryObject<SoundEvent> BELLSNICKEL_INTERACT = registerSoundEvents("bellsnickel_interact");
    public static final RegistryObject<SoundEvent> BELLSNICKEL_SUMMON_01 = registerSoundEvents("bellsnickel_summon_01");
    public static final RegistryObject<SoundEvent> BELLSNICKEL_SUMMON_02 = registerSoundEvents("bellsnickel_summon_02");
    public static final RegistryObject<SoundEvent> BELLSNICKEL_SUMMON_03 = registerSoundEvents("bellsnickel_summon_03");
    public static final RegistryObject<SoundEvent> BELLSNICKEL_SECURITY = registerSoundEvents("bellsnickel_security");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(LoreMasterMod.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
