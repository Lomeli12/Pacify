package net.lomeli.pacify;

import net.lomeli.pacify.commands.CommandPacify;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Pacify.MOD_ID, name = Pacify.MOD_NAME, version = Pacify.MOD_VER, acceptableRemoteVersions = "*")
public class Pacify {
    public static final String MOD_ID = "pacify";
    public static final String MOD_NAME = "Pacify";
    public static final String MOD_VER = "1.0.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandPacify());
    }

    @SubscribeEvent
    public void attackedEvent(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        if (damageFromPlayer(source)) {
            EntityPlayer player = (EntityPlayer) getSourceOfDamage(source);
            if (player != null && isPlayerPacified(player)) event.setCanceled(true);
        }
    }

    public static boolean isPlayerPacified(EntityPlayer player) {
        NBTTagCompound persistant = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        return persistant != null ? persistant.getBoolean(MOD_ID) : false;
    }

    public static void setPacifiedState(EntityPlayer player, boolean flag) {
        NBTTagCompound persistant = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        if (persistant == null) persistant = new NBTTagCompound();
        persistant.setBoolean(MOD_ID, flag);
        player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, persistant);
    }

    boolean damageFromPlayer(DamageSource source) {
        return (source != null && (source.getDamageType() == "player" || source.getSourceOfDamage() instanceof EntityPlayer || source.getEntity() instanceof EntityPlayer));
    }

    Entity getSourceOfDamage(DamageSource source) {
        return source != null ? (source.isProjectile() ? source.getEntity() : source.getSourceOfDamage()) : null;
    }
}
