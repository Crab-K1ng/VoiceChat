package io.github.CrabK1ng.Proximity.mixins;

import finalforeach.cosmicreach.BlockGame;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import io.github.CrabK1ng.Proximity.Proximity;
import io.github.CrabK1ng.Proximity.VoiceMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static finalforeach.cosmicreach.gamestates.GameState.currentGameState;
import static io.github.CrabK1ng.Proximity.ProximityControls.openVoiceMenu;
import static io.github.CrabK1ng.Proximity.ProximityControls.toggleMute;

@Mixin(BlockGame.class)
public abstract class BlockGameMixin {
    @Inject(method = "render", at = @At("HEAD"))            //// Control hooking
    public void onKey(CallbackInfo ci) {
        if (toggleMute.isJustPressed() && currentGameState instanceof InGame) {
            Proximity.toggleMic();
        }
        if (openVoiceMenu.isJustPressed()) {
            if (currentGameState instanceof VoiceMenu) {
                GameState.switchToGameState(GameState.IN_GAME);
            } else if (currentGameState instanceof InGame) {
                GameState.switchToGameState(new VoiceMenu(false));
            }
        }
    }
}