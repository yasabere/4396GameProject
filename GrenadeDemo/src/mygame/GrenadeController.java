/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.controls.AnalogListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Yaw Asabere
 */
public class GrenadeController  extends AbstractControl implements AnalogListener  {
    
    private Main main;
    private Grenade grenade;
    private int timer = 1000;
    private boolean live = true;
    
    public GrenadeController(Grenade grenade){
        this.main = grenade.main;
        this.grenade = grenade;
        InitPhysics();
    }
    
    private PhysicsSpace getPhysicsSpace(){
        return main.bullet.getPhysicsSpace();
    }
    
    private void InitPhysics(){
        RigidBodyControl phySmall = new RigidBodyControl(10.0f); // Dynamic: mass > 0
        this.grenade.geo_grenade.addControl(phySmall);
        main.bullet.getPhysicsSpace().add(phySmall);
    }
    
    protected void controlUpdate(float tpf) {
        
        if (live){
            if (timer > 0){
                timer -= 1;
            }
            else {
                live = false;
                //this.grenade = null;
                //this = null;
                
                Material mat = new Material(main.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                mat.setColor("Color", ColorRGBA.Red);
                grenade.setMaterial(mat);
                
                makeShape();
                
                System.out.println("explosion");
            }
        }
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onAnalog(String name, float value, float tpf) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void makeShape(){
        
        //get grenade position
        Vector3f grenadePos = grenade.getLocalTranslation();
        
        Material mat = new Material(main.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        
        Geometry cannon = new Geometry("ground", new Box(2f, 2f, 2f));
        cannon.setMaterial(mat);
        cannon.setLocalTranslation(grenadePos);
        main.getRootNode().attachChild(cannon);
        //create object set object at the position of the grenade
        //apply physics to object
        
    }
}
