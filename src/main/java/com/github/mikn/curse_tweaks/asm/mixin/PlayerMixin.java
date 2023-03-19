package com.github.mikn.curse_tweaks.asm.mixin;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.mikn.curse_tweaks.CurseTweaks;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "tick()V", at = @At("RETURN"), cancellable = true)
    private void inject(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        var slots = player.getArmorSlots();
        int curseCounts = 0;
        for(ItemStack itemStack: slots) {
            for(CompoundTag tag:getList(itemStack.getEnchantmentTags())) {
                CurseTweaks.LOGGER.error(tag);
            }
        }
    }
    
    private List<CompoundTag> getList(ListTag listTag) {
        List<CompoundTag> list = new ArrayList<CompoundTag>();
        for(int i = 0; i < listTag.size(); i++) {
            list.add(listTag.getCompound(i));
        }
        return list;
    }
}
