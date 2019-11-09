package com.jumpdontdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Box2DScreen extends BaseScreen {

    private World world;
    private Box2DDebugRenderer renderer;
    private OrthographicCamera camera;

    private Body minijoeBody, sueloBody, pinchoBody;
    private Fixture minijoeFixture, sueloFixture, pinchoFixture;

    private boolean debeSaltar, joeSaltando, joeVivo = true;

    public Box2DScreen(MainGame game) { super(game); }

    @Override
    public void show() {

        world = new World(new Vector2(0,-10),true);
        renderer = new Box2DDebugRenderer();
        //camera = new OrthographicCamera(7.11f, 4);
        camera = new OrthographicCamera(16, 9);
        camera.translate(0,1);// Subir la camara 1m para que el suelo esté más abajo

        // Contactos (colisiones)
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                // Cuando el jugador caiga al suelo se producirá un contacto entre 2 fixtures
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if((fixtureA.getUserData().equals("player") && fixtureB.equals("floor") ||
                        fixtureA.getUserData().equals("floor") && fixtureB.equals("player"))){
                    if (Gdx.input.isTouched()){
                        debeSaltar = true;
                    }
                    joeSaltando = false;
                }

                if((fixtureA.getUserData().equals("player") && fixtureB.equals("spike") ||
                        fixtureA.getUserData().equals("spike") && fixtureB.equals("player"))){
                   joeVivo = false;
                }
//                if(fixtureA == minijoeFixture && fixtureB == sueloFixture){
//                    if (Gdx.input.isTouched()){
//                        debeSaltar = true;
//                    }
//                    joeSaltando = false;
//                }
//                if(fixtureA == sueloFixture && fixtureB == minijoeFixture){
//                    joeSaltando = false;
//                }

            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = fixtureB = contact.getFixtureB();

                if(fixtureA == minijoeFixture && fixtureB == sueloFixture){
                    joeSaltando = true;
                }
                if(fixtureA == sueloFixture && fixtureB == minijoeFixture){
                    joeSaltando = true;
                }

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        // BODYS
        minijoeBody = world.createBody(createJoeBodyDef());
        sueloBody = world.createBody(createSueloBodyDef());
        pinchoBody = world.createBody(createPinchoBodyDef(6f));

        // Jugador minijoe
        PolygonShape minijoeShape = new PolygonShape();
        minijoeShape.setAsBox(0.5f,0.5f); // OJO! Se pasa el tamaño en metros!! estamos dando 1m de caja (se pasan medio ancho y medio alto, no el ancho y el alto)
        minijoeFixture = minijoeBody.createFixture(minijoeShape,1);
        minijoeShape.dispose();

        // Suelo
        PolygonShape sueloShape = new PolygonShape();
        sueloShape.setAsBox(500,1);         // 1km de ancho y 2m de alto
        sueloFixture = sueloBody.createFixture(sueloShape,1);
        sueloShape.dispose();

        // Pinchos
        pinchoFixture = createPinchoFixture(pinchoBody);

        // UserData
        minijoeFixture.setUserData("player");
        sueloFixture.setUserData("floor");
        pinchoFixture.setUserData("spike");

    }

    @Override
    public void dispose() {
        minijoeBody.destroyFixture(minijoeFixture);
        sueloBody.destroyFixture(sueloFixture);
        pinchoBody.destroyFixture(pinchoFixture);
        world.destroyBody(minijoeBody);
        world.destroyBody(sueloBody);
        world.destroyBody(pinchoBody);
        world.dispose();
        renderer.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(debeSaltar){
            saltar();
            debeSaltar = false;
        }

        if(Gdx.input.justTouched() && !joeSaltando){
            debeSaltar = true;
        }

        if(joeVivo){
            float velocidadY = minijoeBody.getLinearVelocity().y;
            minijoeBody.setLinearVelocity(8,velocidadY);
        }

        world.step(delta, 6,2); // velocityIterations cuanto mas alto mejor se simula la gravedad pero mayor gasto de CPU
        camera.update(); // Necesario actualizar la camara
        renderer.render(world, camera.combined);
    }

    private BodyDef createJoeBodyDef() {
        BodyDef def = new BodyDef();
        def.position.set(0,0);                         // Posicion en que se situará (a 10metros de alto)
        def.type = BodyDef.BodyType.DynamicBody;
        return def;
    }

    private BodyDef createSueloBodyDef(){
        BodyDef def = new BodyDef();
        def.position.set(0,-1);                        // -1 porque -> LIBGDX para Android - Tutorial 25 - Como Crear el Suelo
        return def;
    }

    private BodyDef createPinchoBodyDef(float x){
        BodyDef def = new BodyDef();
        def.position.set(x,0.5f);
        return def;
    }

    private Fixture createPinchoFixture(Body pinchoBody){
        Vector2[] vertices = new Vector2[3];
        vertices[0] = new Vector2(-0.5f, -0.5f);
        vertices[1] = new Vector2(0.5f, -0.5f);
        vertices[2] = new Vector2(0, 0.5f);

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);
        Fixture fix = pinchoBody.createFixture(shape,1);
        shape.dispose();
        return fix;
    }

    private void saltar(){
        Vector2 position = minijoeBody.getPosition();
        minijoeBody.applyLinearImpulse(0,5, position.x, position.y,true);
    }
    // CAMARA
    //camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    //camera = new OrthographicCamera(32,18);
    // Si  pensamos el cuerpo que queremos dibujar mide 1 metro, y lo queremos pintar en una pantalla
    // que va a representar 4m necesitamos hacer una operacion para pasarle los parametros a la camara
    // Sabemos que el alto son 4m (porque así lo queremos simular) ¿Cuanto debe ser el ancho?
    // Regla de 3:
    // - Resolucion de la pantalla: 640px / 360px = 16:9
    // - Alto simulado: 4m
    // - ¿Ancho? =  640 x 4 / 360 (16 x 4 / 9) = 7.11
}
