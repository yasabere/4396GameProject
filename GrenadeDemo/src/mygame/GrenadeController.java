/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;

/**
 *
 * @author Yaw Asabere
 */
public class GrenadeController {
    
    private final Main main;
    private int timer = 0;
    
    public GrenadeController(Main main){
        this.main = main;
    }
    
    private PhysicsSpace getPhysicsSpace(){
        return main.bullet.getPhysicsSpace();
    }
    
    protected void controlUpdate(float tpf) {
        
        if (timer > 0){
            timer -= 1;
        }
        else {
            
        }
     
    }
}
