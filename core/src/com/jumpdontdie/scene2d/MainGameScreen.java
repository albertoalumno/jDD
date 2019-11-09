package com.jumpdontdie.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jumpdontdie.BaseScreen;
import com.jumpdontdie.MainGame;
import com.jumpdontdie.scene2d.ActorJugador;
import com.jumpdontdie.scene2d.ActorPinchos;

public class MainGameScreen extends BaseScreen {

    private Stage stage;

    private ActorJugador jugador;
    private ActorPinchos pinchos;

    private Texture texturaJugador, texturaPinchos;

    private TextureRegion regionPinchos;


    public MainGameScreen(MainGame game) {
        super(game);
        texturaJugador = new Texture("pelota64x64.png");
        texturaPinchos = new Texture("pinchos128x128.png");
        regionPinchos = new TextureRegion(texturaPinchos, 0,64,128, 64);
    }


    @Override
    public void show() {

        stage = new Stage();
        stage.setDebugAll(true);                    // Marca los bordes de los objetos (bordea cada actor)

        jugador = new ActorJugador(texturaJugador);
        pinchos = new ActorPinchos(regionPinchos);

        stage.addActor(jugador);
        stage.addActor(pinchos);

        jugador.setPosition(20,100);
        pinchos.setPosition(400,100);
    }

    @Override
    public void hide() {
        stage.dispose();
    }

    // Metodo que se ejecuta 30-60 veces por segundo
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f,0.5f,08f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();

        comprobarColisiones();                      //Comporbar siempre antes de dibujar y despues de actualizar

        stage.draw();
    }

    @Override
    public void dispose() {
        texturaJugador.dispose();
    }


    private void comprobarColisiones(){
        if(jugador.isAlive() && jugador.getX() + jugador.getWidth() > pinchos.getX()){
            System.out.println("Colision!!!");
            jugador.setAlive(false);
        }
    }


}
