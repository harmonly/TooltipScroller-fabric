package io.github.harmonly.tooltipscroller.mixin;

import io.github.harmonly.tooltipscroller.ClientTooltipScroller;
import io.github.harmonly.tooltipscroller.Type;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Shadow
    public int height;

    @Inject(method = "keyPressed",
            at = @At("HEAD"))
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!ClientTooltipScroller.rendering) return;
        if (ClientTooltipScroller.KEY_UP.matchesKey(keyCode, scanCode))
            ClientTooltipScroller.scroll(Type.UP, 10);
        if (ClientTooltipScroller.KEY_DOWN.matchesKey(keyCode, scanCode))
            ClientTooltipScroller.scroll(Type.DOWN, 10);
    }

    @Inject(method = "renderTooltipFromComponents",
            at = @At("HEAD")
    )
    private void doScroll(MatrixStack matrices, List<TooltipComponent> components, int x, int y, CallbackInfo ci) {
        Slot slot = ClientTooltipScroller.slot;
        if (slot != null && slot.hasStack())
            ClientTooltipScroller.height = (components.size() == 1) ? (12 + 3) : (12 + 4 + 2 + (components.size() - 1) * 10);
        else ClientTooltipScroller.reset();
        ClientTooltipScroller.screenHeight = this.height;
        ClientTooltipScroller.rendering = true;
    }

    @ModifyVariable(method = "renderTooltipFromComponents",
            ordinal = 5,
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;push()V",
                    shift = At.Shift.BEFORE
            )
    )
    private int modifyYAxis(int y) {
        Slot slot = ClientTooltipScroller.slot;
        if (slot == null || !slot.hasStack()) return y;
        int screenHeight = this.height;
        int height = ClientTooltipScroller.height;
        if (height < screenHeight) {
            ClientTooltipScroller.reset();
            if (y + height - 4 > screenHeight) y = screenHeight - height;
        } else y = ClientTooltipScroller.Y_OFFSET;
        return y;
    }

    @Inject(method = "close", at = @At("HEAD"))
    public void onClose(CallbackInfo info) {
        ClientTooltipScroller.reset();
    }
}
