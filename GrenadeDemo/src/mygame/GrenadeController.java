/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.PhysicsSpace;

/**
 *
 * @author Yaw Asabere
 */
public class GrenadeController {
    
    private final Main main;
    
    public GrenadeController(Main main){
        this.main = main;
        
        
    }
    
    private PhysicsSpace getPhysicsSpace(){
        return main.bullet.getPhysicsSpace();
    }
}
