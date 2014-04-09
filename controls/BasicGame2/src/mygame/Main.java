package mygame;
 
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
 
/**
 * Example 9 - How to make walls and floors solid.
 * This collision code uses Physics and a custom Action Listener.
 * @author normen, with edits by Zathras
 */
public class Main extends SimpleApplication {
 
  private Spatial sceneModel;
  private BulletAppState bullet;
  private RigidBodyControl landscape;
  public Player player;
 
 
  public static void main(String[] args) {
    Main app = new Main();
    app.start();
  }
 
  public void simpleInitApp() {
    /** Set up Physics */
    bullet = new BulletAppState();
    stateManager.attach(bullet);
    //bulletAppState.getPhysicsSpace().enableDebug(assetManager);
 
    // We re-use the flyby camera for rotation, while positioning is handled by physics
    viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
    flyCam.setMoveSpeed(100);
    setUpLight();
    initCrossHairs();
    
    player = new Player(cam, bullet, this);
    player.setUpKeys();
 
    // We load the scene from the zip file and adjust its size.
    assetManager.registerLocator("town.zip", ZipLocator.class);
    sceneModel = assetManager.loadModel("main.scene");
    sceneModel.setLocalScale(2f);
 
    // We set up collision detection for the scene by creating a
    // compound collision shape and a static RigidBodyControl with mass zero.
    CollisionShape sceneShape =
            CollisionShapeFactory.createMeshShape((Node) sceneModel);
    landscape = new RigidBodyControl(sceneShape, 0);
    sceneModel.addControl(landscape);

 
    // We attach the scene and the player to the rootnode and the physics space,
    // to make them appear in the game world.
    rootNode.attachChild(sceneModel);
    bullet.getPhysicsSpace().add(landscape);
    
    //create test ball
    Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    Sphere sphere = new Sphere(16, 16, .5f);
    Geometry ballGeometry = new Geometry("Soccer ball", sphere);
    ballGeometry.setMaterial(material);
    ballGeometry.setLocalTranslation(0, 2, -2);
    //RigidBodyControl automatically uses Sphere collision shapes when attached to single geometry with sphere mesh
    ballGeometry.addControl(new RigidBodyControl(.001f));
    ballGeometry.getControl(RigidBodyControl.class).setRestitution(1);
    rootNode.attachChild(ballGeometry);
    bullet.getPhysicsSpace().add(ballGeometry);
  }
 
  private void setUpLight() {
    // We add light so we see the scene
    AmbientLight al = new AmbientLight();
    al.setColor(ColorRGBA.White.mult(1.3f));
    rootNode.addLight(al);
 
    DirectionalLight dl = new DirectionalLight();
    dl.setColor(ColorRGBA.White);
    dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
    rootNode.addLight(dl);
  }
  
  protected void initCrossHairs() {
    setDisplayStatView(false);
    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    BitmapText ch = new BitmapText(guiFont, false);
    ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
    ch.setText("+"); // crosshairs
    ch.setLocalTranslation( // center
      settings.getWidth() / 2 - ch.getLineWidth()/2, settings.getHeight() / 2 + ch.getLineHeight()/2, 0);
    guiNode.attachChild(ch);
  }

 @Override
    public void simpleUpdate(float tpf) {
        player.update(tpf);
    }

}