package com.sprout.game;

import com.badlogic.gdx.*;

import java.io.IOException;

public class SproutGame extends Game {

    public Client client;

    public SproutGame() throws IOException {
        client = new Client();
    }

    public void create() {
        System.out.println("In sproutgame create");
        this.setScreen(new MenuScreen(this, null));
    }

    @Override
    public void render() {
        super.render();
    }

    public void dispose() {
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
