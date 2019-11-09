package com.jumpdontdie.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jumpdontdie.Constants;

import static com.jumpdontdie.Constants.PIXELS_IN_METER;

public class FloorEntity extends Actor {

    private Texture floor, overfloor;
    private World world;
    private Body body;
    private Fixture fixture;

    /**
     *
     * @param world
     * @param floor
     * @param overfloor
     * @param x dónde está el borde izquierdo del suelo (en metros)
     * @param y dónde está el borde superior del suelo (en metros)
     * @param width anchura del suelo en metros
     */
    public FloorEntity(World world, Texture floor, Texture overfloor, float x, float y, float width){
        this.world = world;
        this.floor = floor;
        this.overfloor = overfloor;

        // Coloco el sielo en la posicion que le corresponde
        BodyDef def = new BodyDef();
        def.position.set(x + width / 2, y - 0.5f);
        body = world.createBody(def);

        // Le doy forma a la caja
        PolygonShape floorShape = new PolygonShape();
        floorShape.setAsBox(width / 2, 0.5f);
        fixture = body.createFixture(floorShape, 1);
        fixture.setUserData("floor");
        floorShape.dispose();

        setSize(width * PIXELS_IN_METER, PIXELS_IN_METER);
        setPosition((x - width / 2) * PIXELS_IN_METER,(y - 1) * PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Render both textures.
        batch.draw(floor, getX(), getY(), getWidth(), getHeight());
        batch.draw(overfloor, getX(), getY() + 0.9f * getHeight(), getWidth(), 0.1f * getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

}
