/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.ui.Picture;

/**
 *
 * @author Nick
 */
public class StartScreen extends AbstractAppState implements ActionListener {
    
    Main main;
    AppStateManager asm;
    BitmapText text;
        
    public void onAction(String name, boolean isPressed, float tpf) {
        
        if (name.equals("start")) {
            startGame();
        }
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
	main = (Main) app;
        asm = stateManager;
	//Game game = new Game();
        //stateManager.attach(game);
        
        Picture pic = new Picture("HUD Picture");
        pic.setImage(main.getAssetManager(), "Interface/resources/background.png", true);
        pic.setWidth(1024);
        pic.setHeight(600);
        pic.setPosition(0, 0);
        main.getGuiNode().attachChild(pic);
        //init input
        InputManager inputManager = main.getInputManager();
        inputManager.addMapping("start", new KeyTrigger(KeyInput.KEY_B));
        inputManager.addListener(this, "start"); 
	
    }
    
    public void startGame() {
        Game game = new Game();
        asm.detach(this);
        main.clearJMonkey(main);
        asm.attach(game);
        
        
    }
    
    @Override
    public void update(float tpf) {
    
    }
}
