package com.github.mikn.curse_tweaks.asm.mixin;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
        int bindingCurseCounts = 0;
        int vanishmentCurseCounts = 0;
        for(ItemStack itemStack: slots) {
            for(CompoundTag tag:getList(itemStack.getEnchantmentTags())) {
                if(tag.get("id").getAsString().equals("minecraft:binding_curse")) {
                    bindingCurseCounts++;
                } else if(tag.get("id").getAsString().equals("minecraft:vanishing_curse")) {
                    vanishmentCurseCounts++;
                }
            }
        }
        if(bindingCurseCounts > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED,1, bindingCurseCounts-1),player);
        }
        if(vanishmentCurseCounts > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,1, vanishmentCurseCounts-1),player);
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
