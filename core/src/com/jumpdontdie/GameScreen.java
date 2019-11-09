package com.jumpdontdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.jumpdontdie.entities.FloorEntity;
import com.jumpdontdie.entities.PlayerEntity;
import com.jumpdontdie.entities.SpikeEntity;

import java.util.ArrayList;
import java.util.List;


public class GameScreen extends BaseScreen {

    private Stage stage;
    private World world;
    private PlayerEntity player;

    private List<FloorEntity> floorList = new ArrayList<FloorEntity>();
    private List<SpikeEntity> spikeList = new ArrayList<SpikeEntity>();

    public GameScreen(MainGame game) {
        super(game);
        stage = new Stage(new FitViewport(640, 360));
        world = new World(new Vector2(0, -10), true);
        world.setContactListener(new ContactListener() {

            private boolean areCollided(Contact contact, Object userA, Object userB){
                return (contact.getFixtureA().getUserData().equals(userA) && contact.getFixtureB().getUserData().equals(userB)) ||
                        (contact.getFixtureA().getUserData().equals(userB) && contact.getFixtureB().getUserData().equals(userA));
            }
            @Override
            public void beginContact(Contact contact) {
                if(areCollided(contact, "player", "floor")){
                    player.setJumping(false);
                    if(Gdx.input.isTouched()){      // seguir saltando si se mantiene pulsada la pantalla
                        player.setMustJump(true);
                    }
                }
                if(areCollided(contact, "player", "spike")){
                    player.setAlive(false);
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

    }

    @Override
    public void show() {
        Texture playerTexture = game.getAssetManeger().get("player.png");
        Texture floorTexture = game.getAssetManeger().get("floor.png");
        Texture overfloorTexture = game.getAssetManeger().get("overfloor.png");
        Texture spikeTexture = game.getAssetManeger().get("spike.png");
        player = new PlayerEntity(world, playerTexture, new Vector2(1.5f,1.5f)); //Posicion incial

        floorList.add(new FloorEntity(world, floorTexture, overfloorTexture, 0, 1, 1000));
        spikeList.add(new SpikeEntity(world, spikeTexture, 6,1)); // 6metros en una base de 1

        stage.addActor(player);
        for(FloorEntity floor : floorList){
            stage.addActor(floor);
        }
        for(SpikeEntity spike : spikeList){
            stage.addActor(spike);
        }
    }

    @Override
    public void hide() {
        player.detach();
        player.remove();
        for(FloorEntity floor : floorList){
            floor.detach();
            floor.remove();
        }
        for(SpikeEntity spike : spikeList){
            spike.detach();
            spike.remove();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f,0.5f,08f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        world.step(delta, 6,2);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
    }
}
