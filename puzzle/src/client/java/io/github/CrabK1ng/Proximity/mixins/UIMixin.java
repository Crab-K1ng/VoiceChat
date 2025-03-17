package io.github.CrabK1ng.Proximity.mixins;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.gamestates.PauseMenu;
import finalforeach.cosmicreach.rendering.GameTexture;
import finalforeach.cosmicreach.ui.UI;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.ui.widgets.CRButton;
import io.github.CrabK1ng.Proximity.Constants;
import io.github.CrabK1ng.Proximity.Proximity;
//import io.github.CrabK1ng.Proximity.VoiceMenu;
import io.github.CrabK1ng.Proximity.VoiceMenu;
import org.checkerframework.checker.units.qual.Current;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static finalforeach.cosmicreach.gamestates.GameState.currentGameState;
import static io.github.CrabK1ng.Proximity.ProximityControls.openVoiceMenu;
import static io.github.CrabK1ng.Proximity.ProximityControls.toggleMute;

@Mixin(UI.class)
public abstract class UIMixin {
    @Shadow
    private Viewport uiViewport;

    @Inject(method = "render", at = @At("HEAD"))
    private void drawIcon(CallbackInfo ci) {
        if (UI.renderUI && Proximity.drawIcon) {
            Texture micOn = GameTexture.load("proximity:mic.png").get();
            Texture micOff = GameTexture.load("proximity:mic_off.png").get();
            Sprite statusIcon = new Sprite(micOn);
            if (!Proximity.lineOpen) {statusIcon.setTexture(micOff);}

            statusIcon.flip(false,true);
            statusIcon.setPosition(-uiViewport.getWorldWidth()/2+3, (uiViewport.getWorldHeight()/2) -32 -3);

            UI.batch.setProjectionMatrix(this.uiViewport.getCamera().combined);
            UI.batch.begin();
            Proximity.initText();
            statusIcon.draw(UI.batch);
            UI.batch.end();
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void onKey(CallbackInfo ci) {
        if (toggleMute.isJustPressed()) {
            Proximity.toggleMic();
        }
        if (openVoiceMenu.isJustPressed()) {
            //Proximity.toggleMenu();
        }
        if (Proximity.menuOpen && !(currentGameState instanceof VoiceMenu)) {
            if (InGame.getLocalPlayer() != null) {
                try {
                    GameState.switchToGameState(new VoiceMenu());
                } catch (Exception e) {
                    Constants.LOGGER.info(e);
                }
            }
        }
        if (!Proximity.menuOpen && currentGameState instanceof VoiceMenu) {
            if (InGame.getLocalPlayer() != null) {
                try {
                    VoiceMenu.switchToGameState(new InGame());
                } catch (Exception e) {
                    Constants.LOGGER.info(e);
                }
            }
        }
    }
}