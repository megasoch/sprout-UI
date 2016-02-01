package com.sprout.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.io.IOException;

/**
 * Created by megasoch on 31.01.2016.
 */
public class PauseScreen implements Screen, InputProcessor {

    private SpriteBatch spriteBatch;
    SproutGame game;
    GameLogic gl;
    private Texture backgroundTexture;
    ShapeRenderer shapeRenderer;
    BitmapFont font;


    public PauseScreen(SproutGame game, GameLogic gl) {
        this.game = game;
        this.gl = gl;
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        backgroundTexture = new Texture("background.jpg");
        font = new BitmapFont();
        font.setColor(Color.BLUE);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        spriteBatch.begin();
        renderBackground();
        renderMenuText();
        spriteBatch.end();
        renderMenu();
    }

    public void renderBackground() {
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.draw(backgroundTexture, 0, 0, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void renderMenuText() {
        font.draw(spriteBatch, "Return to game", 150, 400, 350, 1, false);
        font.draw(spriteBatch, "Defeat", 150, 250, 350, 1, false);
    }

    public void renderMenu() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(150, 350, 350, 100);
        shapeRenderer.rect(150, 200, 350, 100);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT && game.getScreen().getClass().equals(PauseScreen.class)) {
            float posX = screenX;
            float posY = Gdx.graphics.getHeight() - screenY;

//            //resume game
//            if (posX > 150 && posX < 500 && posY > 350 && posY < 450) {
//                try {
//                    game.setScreen(new GameScreen(game, gl));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

            //go to menu
            if (posX > 150 && posX < 500 && posY > 200 && posY < 300) {
                game.setScreen(new MenuScreen(game, null));
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
