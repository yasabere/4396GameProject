/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.controls.AnalogListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author owner
 */
public class FieldController extends AbstractControl implements AnalogListener  {
    
    Field field;
    Main main;
    
    public FieldController(Field field){
        this.field = field;
        this.main = field.main;
        
        InitPhysics();
    }
    
    private void InitPhysics(){
        RigidBodyControl phySmall = new RigidBodyControl(1f); // Dynamic: mass > 0
        this.field.addControl(phySmall);
        main.bullet.getPhysicsSpace().add(phySmall);
    }

    @Override
    protected void controlUpdate(float tpf) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onAnalog(String name, float value, float tpf) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
