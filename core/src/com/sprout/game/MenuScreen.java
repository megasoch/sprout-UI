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
 * Created by megasoch on 30.01.2016.
 */
public class MenuScreen implements Screen, InputProcessor {

    private SpriteBatch spriteBatch;
    SproutGame game;
    public GameLogic gl;
    private Texture backgroundTexture;
    ShapeRenderer shapeRenderer;
    BitmapFont font;


    public MenuScreen(SproutGame game, GameLogic gl) {
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
        font.draw(spriteBatch, "Create game", 150, 450, 350, 1, false);
        font.draw(spriteBatch, "Join game", 150, 300, 350, 1, false);
        font.draw(spriteBatch, "Exit", 150, 150, 350, 1, false);
    }

    public void renderMenu() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(150, 100, 350, 100);
        shapeRenderer.rect(150, 250, 350, 100);
        shapeRenderer.rect(150, 400, 350, 100);
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
        if (button == Input.Buttons.LEFT && game.getScreen().getClass().equals(MenuScreen.class)) {
            float posX = screenX;
            float posY = Gdx.graphics.getHeight() - screenY;

            //exit
            if (posX > 150 && posX < 500 && posY > 100 && posY < 200) {
                Gdx.app.exit();
            }

            //join
            if (posX > 150 && posX < 500 && posY > 250 && posY < 350) {
                try {
                    game.setScreen(new JoinScreen(game, gl));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //create
            if (posX > 150 && posX < 500 && posY > 400 && posY < 500) {
                try {
                    game.getClient().createGame();
                    game.setScreen(new GameScreen(game, gl, true));
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
