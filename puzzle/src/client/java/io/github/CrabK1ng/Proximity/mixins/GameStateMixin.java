package io.github.CrabK1ng.Proximity.mixins;

import finalforeach.cosmicreach.gamestates.GameState;
import io.github.CrabK1ng.Proximity.ClientInitializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameState.class)
public class GameStateMixin {
    @Inject(method = "onSwitchTo", at = @At("TAIL"))
    public void onSwitchTo(CallbackInfo ci) {
        ClientInitializer.captureAudio();
    }
}