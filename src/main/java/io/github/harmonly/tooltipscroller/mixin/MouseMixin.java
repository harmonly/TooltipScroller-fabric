package io.github.harmonly.tooltipscroller.mixin;

import io.github.harmonly.tooltipscroller.Type;
import io.github.harmonly.tooltipscroller.ClientTooltipScroller;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onMouseScroll",
            at = @At("HEAD"))
    public void onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (vertical > 0) ClientTooltipScroller.scroll(Type.UP, 20);
        else ClientTooltipScroller.scroll(Type.DOWN, 20);
    }
}
