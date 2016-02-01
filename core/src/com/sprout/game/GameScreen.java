package com.sprout.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by megasoch on 30.01.2016.
 */
public class GameScreen implements Screen, InputProcessor {

    public static GameLogic gl;
    SproutGame game;

    private Texture circleTexture;
    private Texture edgeTexture;
    private Texture stateTexture;
    private Texture menuTexture;

    public static boolean MYMOVE;

    private final float TEXTURE_SIZE = 30;
    private final float EDGE_DISTANCE = 7;
    private final float EDGE_WIDTH = 10;
    private int backgroundMoveX = 0;
    private ArrayList<PointElement> selected = new ArrayList<PointElement>(0);
    private ArrayList<PointElement> edgePointSequence = new ArrayList<PointElement>(0);
    private boolean firstTouch = false;

    SpriteBatch spriteBatch;

    public static Texture backgroundTexture;

    Sound soundSelected;
    Sound soundUnselected;
    Sound addEdge;
    Sound creatingEdge;

    public GameScreen(SproutGame game, GameLogic gl, boolean myMove) throws IOException {

        this.MYMOVE = myMove;
        game.getClient().start();
        this.game = game;

        if (gl == null) {
            this.gl = new GameLogic(3);
        } else {
            this.gl = gl;
        }

        soundSelected = Gdx.audio.newSound(Gdx.files.internal("select.wav"));
        soundUnselected = Gdx.audio.newSound(Gdx.files.internal("unselect.wav"));
        addEdge = Gdx.audio.newSound(Gdx.files.internal("add-edge.wav"));
        creatingEdge = Gdx.audio.newSound(Gdx.files.internal("MENU_Pick.wav"));

        circleTexture = new Texture(Gdx.files.internal("circle-white-2.png"));
        edgeTexture = new Texture(Gdx.files.internal("circle-white-2.png"));
        stateTexture = new Texture(Gdx.files.internal("256.png"));
        menuTexture = new Texture(Gdx.files.internal("menu.png"));

        backgroundTexture = new Texture("background.jpg");

        spriteBatch = new SpriteBatch();

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        spriteBatch.begin();
        renderBackground();
        drawEdges();
        drawTextureCircles();
        renderState();
        renderMenuButton();
        spriteBatch.end();

    }

    public void renderState() {
        spriteBatch.setColor(Color.RED);
        if (MYMOVE) {
            spriteBatch.setColor(Color.GREEN);
        }
        spriteBatch.draw(stateTexture, 0, 0, TEXTURE_SIZE, TEXTURE_SIZE);
    }

    public void renderBackground() {
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.draw(backgroundTexture, 0, 0, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void renderMenuButton() {
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.draw(menuTexture, 600, 0, 50, 50);
    }

    private void drawTextureCircles() {
        for (PointElement p : gl.getPoints()) {
            spriteBatch.setColor(Color.BLUE);
            if (p.isSelected()) {
                spriteBatch.setColor(Color.GREEN);
            }
            float x = p.getX() - TEXTURE_SIZE / 2;
            float y = p.getY() - TEXTURE_SIZE / 2;
            spriteBatch.draw(circleTexture, x, y, TEXTURE_SIZE, TEXTURE_SIZE);
        }
    }

    private void drawEdges() {
        spriteBatch.setColor(Color.BROWN);
        for (EdgeElement e : gl.getEdges()) {
            for (int i = e.getPoints().size() - 1; i > 0; i--) {
                float xLength = e.getPoints().get(i - 1).getX() - e.getPoints().get(i).getX();
                float yLength = e.getPoints().get(i - 1).getY() - e.getPoints().get(i).getY();
                float k = yLength / xLength;
                double xStep = Math.signum(xLength) * EDGE_DISTANCE / Math.sqrt(k * k + 1);
                double yStep = k * xStep;
                int steps = Math.round(xLength / (float) xStep);
                for (int j = 0; j < steps; j++) {
                    float x = e.getPoints().get(i).getX() + (float) (j * xStep) - EDGE_WIDTH / 2;
                    float y = e.getPoints().get(i).getY() + (float) (j * yStep) - EDGE_WIDTH / 2;

                    spriteBatch.draw(edgeTexture, x, y, EDGE_WIDTH, EDGE_WIDTH);
                }
            }
        }

        for (int i = edgePointSequence.size() - 1; i > 0; i--) {
            float xLength = edgePointSequence.get(i - 1).getX() - edgePointSequence.get(i).getX();
            float yLength = edgePointSequence.get(i - 1).getY() - edgePointSequence.get(i).getY();
            float k = yLength / xLength;
            double xStep = Math.signum(xLength) * EDGE_DISTANCE / Math.sqrt(k * k + 1);
            double yStep = k * xStep;
            int steps = Math.round(xLength / (float) xStep);
            for (int j = 0; j < steps; j++) {
                float x = edgePointSequence.get(i).getX() + (float) (j * xStep) - EDGE_WIDTH / 2;
                float y = edgePointSequence.get(i).getY() + (float) (j * yStep) - EDGE_WIDTH / 2;

                spriteBatch.draw(edgeTexture, x, y, EDGE_WIDTH, EDGE_WIDTH);
            }
        }

    }

    private void addNewPoint() throws IOException, ClassNotFoundException {
        float center = Intersection.sequenceLength(edgePointSequence) / 2;
        for (int i = 0; i < edgePointSequence.size() - 1; i++) {
            PointElement a = edgePointSequence.get(i);
            PointElement b = edgePointSequence.get(i + 1);
            if (center - Intersection.length(a, b) > 0) {
                center -= Intersection.length(a, b);
            } else {
                float k = center / Intersection.length(a, b);
                float x = b.getX() - a.getX();
                float y = b.getY() - a.getY();
                PointElement p = new PointElement(a.getX() + x * k, a.getY() + y * k);
                p.setPower(2);
                gl.addPoint(p);
                return;
            }
        }
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
        boolean edgeAdded = false;
        if (button == Input.Buttons.LEFT && (MYMOVE) && game.getScreen().getClass().equals(GameScreen.class)) {
            float posX = screenX;
            float posY = Gdx.graphics.getHeight() - screenY;

//            if (posX > 600 && posX < 650 && posY > 0 && posY < 50) {
//                game.setScreen(new PauseScreen(game, gl));
//            }
            for (PointElement p : gl.getPoints()) {
                if (Math.abs(posX - p.getX()) < TEXTURE_SIZE / 2 && Math.abs(posY - p.getY()) < TEXTURE_SIZE / 2 && p.isUsable()) {
                    p.setSelected(true);
                    if (edgePointSequence.size() <= 1) {
                        firstTouch = true;
                        if (!selected.contains(p)) {
                            if (selected.size() == 1) {
                                if (!Intersection.hasIntersection(p, gl.getEdges(), edgePointSequence)) {
                                    selected.add(p);
                                    edgePointSequence.add(p);
                                    long id = soundSelected.play(1.0f);
                                } else {
                                    p.setSelected(false);
                                }
                            } else {
                                selected.add(p);
                                edgePointSequence.add(p);
                                long id = soundSelected.play(1.0f);
                            }
                        }

                    } else {
                        if (selected.contains(p)) {
                            if (p.getPower() < 2) {
                                selected.add(p);
                                edgePointSequence.add(p);
                                long id = soundSelected.play(1.0f);
                            }
                        } else {
                            selected.add(p);
                            edgePointSequence.add(p);
                            long id = soundSelected.play(1.0f);
                        }
                    }
                    break;
                }
            }
            if (selected.size() == 2 && selected.get(0).isUsable() && selected.get(1).isUsable() && edgePointSequence.size() > 1) {
                if (selected.get(0).equals(selected.get(1))) {
                    if (selected.get(0).getPower() < 2) {
                        try {
                            addNewPoint();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        gl.addEdge(edgePointSequence);
                        edgeAdded = true;
                        selected.get(0).increasePower();
                        selected.get(1).increasePower();
                        selected.get(0).setSelected(false);
                        edgePointSequence.clear();
                        selected.clear();
                        long id = addEdge.play(1.0f);
                    }
                } else {
                    try {
                        addNewPoint();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    gl.addEdge(edgePointSequence);
                    edgeAdded = true;
                    selected.get(0).increasePower();
                    selected.get(1).increasePower();
                    selected.get(0).setSelected(false);
                    selected.get(1).setSelected(false);
                    edgePointSequence.clear();
                    selected.clear();
                    long id = addEdge.play(1.0f);
                }
            }
            if (selected.size() == 1) {
                if (firstTouch) {
                    firstTouch = false;
                } else {
                    PointElement p = new PointElement(posX, posY);
                    if (!Intersection.hasIntersection(p, gl.getEdges(), edgePointSequence)) {
                        edgePointSequence.add(p);
                        long id = creatingEdge.play(1.0f);
                    }
                }
            }
            try {
                if(edgeAdded){
                    game.getClient().communicate(gl);
                    MYMOVE = !MYMOVE;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (button == Input.Buttons.RIGHT && (MYMOVE) && game.getScreen().getClass().equals(GameScreen.class)) {
            for (PointElement p : gl.getPoints()) {
                if (p.isSelected()) {
                    p.setSelected(false);
                    long id = soundUnselected.play(1.0f);
                    selected.remove(p);
                    edgePointSequence.clear();
                }
            }
//            try {
//                game.getClient().communicate(gl);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
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

    @Override
    public void show() {

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
}
