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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by megasoch on 30.01.2016.
 */
public class JoinScreen implements Screen, InputProcessor {

    private SpriteBatch spriteBatch;
    SproutGame game;
    GameLogic gl;
    private Texture backgroundTexture;
    ShapeRenderer shapeRenderer;
    BitmapFont font;
    List<Integer> createdGames;

    public JoinScreen(SproutGame game, GameLogic gl) throws IOException {
        this.game = game;
        this.gl = gl;
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        backgroundTexture = new Texture("background.jpg");
        font = new BitmapFont();
        font.setColor(Color.BLUE);
        createdGames = game.getClient().getGames();
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
        for (int i = 0; i < Math.min(10, createdGames.size()); i++) {
            font.draw(spriteBatch, createdGames.get(i).toString(), 150, 530 - i * 50, 350, 1, false);
        }
        font.draw(spriteBatch, "Menu", 25, 55, 100, 1, false);
    }

    public void renderMenu() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        for (int i = 0; i < Math.min(10, createdGames.size()); i++) {
            shapeRenderer.rect(150, 500 - i * 50, 350, 50);
        }
        shapeRenderer.rect(25, 25, 100, 50);
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
        if (button == Input.Buttons.LEFT && game.getScreen().getClass().equals(JoinScreen.class)) {
            int posX = screenX;
            int posY = Gdx.graphics.getHeight() - screenY;

            //join game
            if (posX > 150 && posX < 500) {
                int id =  ((550 - posY) / 50);
                if(id >= 0 && id < createdGames.size()) {
                    System.out.println("Choosed : " + id);
                    try {
                        game.getClient().joinGame(createdGames.get(id));
                        game.setScreen(new GameScreen(game, gl, false));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            //menu
            if (posX > 25 && posX < 125 && posY > 25 && posY < 75) {
                game.setScreen(new MenuScreen(game, gl));
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
