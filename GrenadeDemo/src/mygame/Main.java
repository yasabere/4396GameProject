package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    BulletAppState bullet;
    private ArrayList<Grenade> grenades = new ArrayList<Grenade>();
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        bullet = new BulletAppState();
        bullet.setDebugEnabled(true);
        
        stateManager.attach(bullet);
        
        Grenade test = new Grenade(this);
        rootNode.attachChild(test);
        
        Field field = new Field(this);
        field.setLocalTranslation(0, -10f, 0);
        rootNode.attachChild(field);
        
        System.out.println("test");
    }

    @Override
    public void simpleUpdate(float tpf) {
        for (Grenade g : grenades){
            g.controlUpdate(tpf);
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
