package com.jumpdontdie.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.jumpdontdie.Constants.PIXELS_IN_METER;

public class PlayerEntity extends Actor {

    // Para crear un actor necesitamos saber
    // 1.- Su textura.
    // 2.- Su mundo para saber donde dibujarlo.
    // 3.- El body del player
    // 4.- El Fixture del player

    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;

    // Para este juegos:
    // 5.- Cuándo está vivo.
    // 6.- Cuándo está saltando.

    private boolean alive = true;
    private boolean jumping = false;
    private boolean mustJump = true;

    public PlayerEntity(World world, Texture texture, Vector2 position){
        this.world = world;
        this.texture = texture;

        // Crear Body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        // Crear Fixture
        PolygonShape playerShape = new PolygonShape();
        playerShape.setAsBox(0.5f,0.5f);
        fixture = body.createFixture(playerShape,1);
        fixture.setUserData("player");
        playerShape.dispose();

        setSize(PIXELS_IN_METER,PIXELS_IN_METER); // 1m*1m = 9Opx*90px

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - 0.5f)* PIXELS_IN_METER,
                    (body.getPosition().y - 0.5f)* PIXELS_IN_METER);
        batch.draw(texture, getX(), getY(),getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        // Iniciar un salto si hemos tocado la pantalla
        if(Gdx.input.justTouched() || mustJump){
            mustJump = false;
            jump();
        }
        // Hacer avanzar al jugador si esta vivo
        if(alive){
            float speedY = body.getLinearVelocity().y;
            body.setLinearVelocity(5,speedY);
        }
    }

    public void jump(){
        if(!jumping && !alive){
            jumping = true;
            Vector2 position = body.getPosition();
            body.applyLinearImpulse(0,6,position.x, position.y, true);
        }
    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    // GETTER AND SETTETS

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public boolean isMustJump() {
        return mustJump;
    }

    public void setMustJump(boolean mustJump) {
        this.mustJump = mustJump;
    }
}
