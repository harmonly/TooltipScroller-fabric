package io.github.harmonly.tooltipscroller.mixin;

import io.github.harmonly.tooltipscroller.ClientTooltipScroller;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Shadow
    protected Slot focusedSlot;

    @Inject(method = "drawMouseoverTooltip", at = @At("HEAD"))
    public void renderTooltip(MatrixStack matrices, int x, int y, CallbackInfo ci) {
        Slot slot = ClientTooltipScroller.slot;
        if (slot == null || slot != this.focusedSlot) {
            ClientTooltipScroller.reset();
            ClientTooltipScroller.slot = this.focusedSlot;
        }
    }
}
