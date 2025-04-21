package io.github.CrabK1ng.Proximity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import finalforeach.cosmicreach.BlockGame;
import finalforeach.cosmicreach.ClientZoneLoader;
import finalforeach.cosmicreach.TickRunner;
import finalforeach.cosmicreach.gamestates.*;
import finalforeach.cosmicreach.io.ChunkSaver;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.rendering.shaders.SpriteBatchShader;
import finalforeach.cosmicreach.settings.GraphicsSettings;
import finalforeach.cosmicreach.settings.INumberSetting;
import finalforeach.cosmicreach.settings.types.IntSetting;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.GameStyles;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import finalforeach.cosmicreach.ui.actions.AlignXAction;
import finalforeach.cosmicreach.ui.actions.AlignYAction;
import finalforeach.cosmicreach.ui.screens.CustomScreen;
import finalforeach.cosmicreach.ui.widgets.CRButton;
import finalforeach.cosmicreach.ui.widgets.CRSlider;

import java.text.NumberFormat;

import static io.github.CrabK1ng.Proximity.Proximity.lineOpen;

public class VoiceMenu extends GameState implements IGameStateInWorld {
    final String on = Lang.get("on_state");
    final String off = Lang.get("off_state");

    ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
    NinePatch bg9Patch = GameStyles.containerBackground9Patch;
//    style.background = new NinePatchDrawable(bg9Patch);
//    style.background.setLeftWidth(0.0F);
//    style.background.setRightWidth(0.0F);style.background.setBottomHeight(0.0F);
//    style.background.setTopHeight(0.0F);
//    style.background.setMinWidth(0.0F);
//    style.background.setMinHeight(0.0F);
//    style.knobBefore = new NinePatchDrawable(GameStyles.container9Patch);

//    ProgressBar volumeBar = new ProgressBar(0f, 100f, 1f, true, CustomScreen.) {};


    boolean cursorCaught;
    private final NumberFormat percentFormat = Lang.getPercentFormatter();

    @Override
    public void onSwitchTo()
    {
        super.onSwitchTo();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void switchAwayTo(GameState gameState)
    {
        TickRunner.INSTANCE.continueTickThread();
        super.switchAwayTo(gameState);
        Gdx.input.setInputProcessor(null);
    }

    private CRSlider createSettingsCRSlider(final INumberSetting setting, final String prefix, float min, float max, float stepSize, final NumberFormat valueTextFormat) {
        CRSlider slider = new CRSlider((String)null, min, max, stepSize, false) {
            protected void onChangeEvent(ChangeListener.ChangeEvent event) {
                float currentValue = this.getValue();
                setting.setValue(currentValue);
                String formattedValue;
                if (valueTextFormat == null) {
                    if (setting instanceof IntSetting) {
                        formattedValue = "" + (int)currentValue;
                    } else {
                        formattedValue = "" + currentValue;
                    }
                } else {
                    formattedValue = valueTextFormat.format((double)currentValue);
                }

                this.setText(prefix + formattedValue);
            }
        };
        slider.setWidth(250.0F);
        slider.setValue(setting.getValueAsFloat());
        return slider;
    }

    public VoiceMenu(boolean cursorCaught) {
        this.cursorCaught = cursorCaught;
        Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    }

    public void create() {
        super.create();
        TickRunner.INSTANCE.pauseThread();
        if (ClientZoneLoader.currentInstance != null) {
            ClientZoneLoader.currentInstance.requestSave();
        }

        Gdx.input.setCursorCatched(false);

        super.create();
        Table table = new Table();
        table.setFillParent(true);
        this.stage.addActor(table);
        System.gc();

        //close button
        CRButton closeButton = new CRButton("Close") {
            public void onClick() {
                super.onClick();
                GameState.switchToGameState(GameState.IN_GAME);
            }
        };
        closeButton.addAction(new AlignXAction(1, 0.5F));
        closeButton.addAction(new AlignYAction(1, 0.5F, 40.0F));
        closeButton.setSize(275.0F, 35.0F);
        this.stage.addActor(closeButton);

        //mic volume slider
        CRSlider soundSlider = this.createSettingsCRSlider(Proximity.micVolume, "Mic Volume: ", 0.0F, 2.0F, 0.01F, this.percentFormat);
        soundSlider.addAction(new AlignXAction(1, 0.5F));
        soundSlider.addAction(new AlignYAction(1, 0.5F, -10.0F));
        soundSlider.setSize(275.0F, 35.0F);
        this.stage.addActor(soundSlider);


        //mic button
        CRButton micButton = new CRButton() {
            public void onClick() {
                super.onClick();
                Proximity.toggleMic();
                this.updateText();
            }

            public void updateText() {
                String string = "Mic: "/*Lang.get("difficultyButton")*/;
                this.setText(string + ((lineOpen) ? VoiceMenu.this.on : VoiceMenu.this.off));
            }
        };

        micButton.onClick();
        micButton.onClick();  //update text

        micButton.addAction(new AlignXAction(1, 0.5F));
        micButton.addAction(new AlignYAction(1, 0.5F, -60.0F));
        micButton.setSize(275.0F, 35.0F);
        this.stage.addActor(micButton);

        //icon button
        CRButton iconButton = new CRButton("iconButton") {
            public void onClick() {
                super.onClick();
                Proximity.toggleIcon();
                this.updateText();
            }

            public void updateText() {
                String string = "Icon: "/*Lang.get("difficultyButton")*/;
                this.setText(string + ((Proximity.drawIcon) ? VoiceMenu.this.on : VoiceMenu.this.off));
            }
        };
        iconButton.onClick();
        iconButton.onClick();   //update text

        iconButton.addAction(new AlignXAction(1, 0.5F));
        iconButton.addAction(new AlignYAction(1, 0.5F, -100.0F));
        iconButton.setSize(275.0F, 35.0F);
        this.stage.addActor(iconButton);

        //volume progress bar
//        volumeBar.addAction(new AlignXAction(-1, 0.5F));
//        volumeBar.addAction(new AlignYAction(-1, 0.5F, -100.0F));
//        this.stage.addActor(volumeBar);

        //other stuff
        PerspectiveCamera worldCamera = new PerspectiveCamera(GraphicsSettings.fieldOfView.getValue(), (float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
        worldCamera.near = 0.1F;
        worldCamera.far = 1000.0F;
        PerspectiveCamera skyCamera = new PerspectiveCamera(GraphicsSettings.fieldOfView.getValue(), (float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
        skyCamera.near = 0.1F;
        skyCamera.far = 2500.0F;
        System.gc();
    }

    public void resize(int width, int height) {
        super.resize(width, height);
        IN_GAME.resize(width, height);
    }

    public void render() {
        super.render();
        if (!this.firstFrame && Gdx.input.isKeyJustPressed(111)) {
            TickRunner.INSTANCE.continueTickThread();
            Proximity.toggleMenu();
            switchToGameState(IN_GAME);
        }

        this.stage.act();
        ScreenUtils.clear(0.1F, 0.1F, 0.2F, 1.0F, true);
        Gdx.gl.glEnable(2929);
        Gdx.gl.glDepthFunc(513);
        Gdx.gl.glEnable(2884);
        Gdx.gl.glCullFace(1029);
        Gdx.gl.glEnable(3042);
        Gdx.gl.glBlendFunc(770, 771);
        IN_GAME.render();
        Gdx.gl.glCullFace(1028);
//        volumeBar.setValue(Proximity.micLevel);
        this.stage.draw();
        Gdx.gl.glEnable(2884);
        Gdx.gl.glCullFace(1029);
        Gdx.gl.glDepthFunc(519);
    }
}
