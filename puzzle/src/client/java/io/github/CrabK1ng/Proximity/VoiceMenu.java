package io.github.CrabK1ng.Proximity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.rendering.GameTexture;
import finalforeach.cosmicreach.settings.GraphicsSettings;
import finalforeach.cosmicreach.ui.actions.AlignXAction;
import finalforeach.cosmicreach.ui.actions.AlignYAction;
import finalforeach.cosmicreach.ui.widgets.CRButton;
import finalforeach.cosmicreach.world.Sky;

public class VoiceMenu extends GameState {
    public static Texture micOn = GameTexture.load("proximity:mic.png").get();
    public static Texture micOff = GameTexture.load("proximity:mic_off.png").get();
    private PerspectiveCamera skyCamera;

    public VoiceMenu() {
    }

    public void create() {
        super.create();
        Table table = new Table();
        table.setFillParent(true);
        this.stage.addActor(table);
        System.gc();
        CRButton startButton = new CRButton(Lang.get("startButton")) {
            public void onClick() {
                super.onClick();
                GameState.switchToGameState(new InGame());
            }
        };
        startButton.addAction(new AlignXAction(1, 0.5F));
        startButton.addAction(new AlignYAction(1, 0.5F, 40.0F));
        startButton.setSize(275.0F, 35.0F);
        this.stage.addActor(startButton);
        CRButton optionsButton = new CRButton(Lang.get("optionsButton")) {
            public void onClick() {
                super.onClick();
                Proximity.toggleIcon();
            }
        };
        optionsButton.addAction(new AlignXAction(1, 0.5F));
        optionsButton.addAction(new AlignYAction(1, 0.5F, -95.0F));
        optionsButton.setSize(275.0F, 35.0F);
        this.stage.addActor(optionsButton);

        PerspectiveCamera worldCamera = new PerspectiveCamera(GraphicsSettings.fieldOfView.getValue(), (float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
        worldCamera.near = 0.1F;
        worldCamera.far = 1000.0F;
        this.skyCamera = new PerspectiveCamera(GraphicsSettings.fieldOfView.getValue(), (float)Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight());
        this.skyCamera.near = 0.1F;
        this.skyCamera.far = 2500.0F;
        CRButton smallLangButton = new CRButton() {
            public void onClick() {
                super.onClick();
                Proximity.toggleMic();
            }
        };
        smallLangButton.addAction(new AlignXAction(8, 1.0F, -58.0F));
        smallLangButton.addAction(new AlignYAction(4, 0.0F, 60.0F));
        smallLangButton.add(new Image(micOn));
        smallLangButton.setSize(50.0F, 50.0F);
        this.stage.addActor(smallLangButton);
    }

    public void onSwitchTo() {
        super.onSwitchTo();
        Gdx.input.setInputProcessor(new InputMultiplexer(new InputProcessor[]{this.stage, new InputAdapter() {
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return true;
            }
        }}));
    }

    public void switchAwayTo(GameState gameState) {
        super.switchAwayTo(gameState);
        Gdx.input.setInputProcessor((InputProcessor)null);
    }

    public void render() {
        if (Proximity.menuOpen) {
            super.render();
            this.stage.act();
            ScreenUtils.clear(0.0F, 0.0F, 0.0F, 1.0F, true);
            Gdx.gl.glEnable(2929);
            Gdx.gl.glDepthFunc(513);
            Gdx.gl.glEnable(2884);
            Gdx.gl.glCullFace(1029);
            Gdx.gl.glEnable(3042);
            Gdx.gl.glBlendFunc(770, 771);
            Sky.SPACE_DAY.drawSky(this.skyCamera);
            this.skyCamera.rotate(Vector3.Z, Gdx.graphics.getDeltaTime() * 0.25F);
            Gdx.gl.glActiveTexture(33984);
            Gdx.gl.glBindTexture(3553, 0);
            Gdx.gl.glCullFace(1028);
            this.stage.draw();
            Gdx.gl.glEnable(2884);
            Gdx.gl.glCullFace(1029);
            Gdx.gl.glDepthFunc(519);
        }
    }
}
