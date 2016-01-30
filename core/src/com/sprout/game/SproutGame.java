package com.sprout.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.IOException;
import java.util.ArrayList;

public class SproutGame extends Game implements InputProcessor {

    public static GameLogic gl;

    private Texture circleTexture;
    private Texture edgeTexture;
    private Texture stateTexture;

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


    @Override
    public void create() {

        gl = new GameLogic(3);

        soundSelected = Gdx.audio.newSound(Gdx.files.internal("select.wav"));
        soundUnselected = Gdx.audio.newSound(Gdx.files.internal("unselect.wav"));
        addEdge = Gdx.audio.newSound(Gdx.files.internal("add-edge.wav"));
        creatingEdge = Gdx.audio.newSound(Gdx.files.internal("MENU_Pick.wav"));

        circleTexture = new Texture(Gdx.files.internal("circle-white-2.png"));
        edgeTexture = new Texture(Gdx.files.internal("circle-white-2.png"));
        stateTexture = new Texture(Gdx.files.internal("256.png"));

        backgroundTexture = new Texture("background.jpg");

        spriteBatch = new SpriteBatch();

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        spriteBatch.begin();
        renderBackground();
        drawEdges();
        drawTextureCircles();
        renderState();
        spriteBatch.end();

    }

    public void renderState() {
        spriteBatch.setColor(Color.RED);
        if (gl.getEdges().size() % 2 == 0) {
            spriteBatch.setColor(Color.GREEN);
        }
        spriteBatch.draw(stateTexture,0, 0, TEXTURE_SIZE, TEXTURE_SIZE);
    }

    public void renderBackground() {
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.draw(backgroundTexture, 0, 0, backgroundMoveX, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        if (button == Input.Buttons.LEFT && (gl.getEdges().size() % 2 == 0)) {
            float posX = screenX;
            float posY = Gdx.graphics.getHeight() - screenY;
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
        }

        if (button == Input.Buttons.RIGHT && (gl.getEdges().size() % 2 == 0)) {
            for (PointElement p : gl.getPoints()) {
                if (p.isSelected()) {
                    p.setSelected(false);
                    long id = soundUnselected.play(1.0f);
                    selected.remove(p);
                    edgePointSequence.clear();
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
