/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

/**
 *
 * @author Nick
 */
public class StartScreen extends AbstractAppState {
    
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
	
	Game game = new Game();
        stateManager.attach(game);
	
    }
    
    @Override
    public void update(float tpf) {
    
    }
}
