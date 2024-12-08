package io.github.CrabK1ng.VoiceChat.mixins;

import finalforeach.cosmicreach.gamestates.InGame;
import io.github.CrabK1ng.VoiceChat.ClientInitializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGame.class)
public class InGameMixin {
    @Inject(method = "onSwitchTo", at = @At("TAIL"))
    public void onSwitchTo(CallbackInfo ci) {
        ClientInitializer.captureAudio();
    }
}
