package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.font.BitmapText;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * test
 * @author 
 */
public class Main extends SimpleApplication {

  

  private Spatial level_1;
  Node loadedNode;
  BulletAppState bullet;
  RigidBodyControl scenePhys;
  Material mat1, magenta;
  Geometry doorOne, doorTwo, colShape1, colShape2, colShape3;
  Light alarm_light;
  FogFilter fog;
  public Player player;

    
    public static void main(String[] args) {

       
       Main app = new Main();
       app.start();
        
    }

    @Override
    public void simpleInitApp() {
        
        initCrosshairs();
   
        initLights();
        //initCam();
        initMaterial();
        bullet = new BulletAppState();
        stateManager.attach(bullet);
        player = new Player(cam, bullet, this);
        player.setUpKeys();
        initGeometries();
        
        
        //Alarm sound
//        AudioNode alarmAudio = new AudioNode(assetManager, "Sounds/alarm.ogg");
//        alarmAudio.setPositional(false); 
//        alarmAudio.setDirectional(false);
//        alarmAudio.setVolume(1);
//        alarmAudio.setLooping(true);
//        rootNode.attachChild(alarmAudio);
//        alarmAudio.play();
        
        //Fire sound for level_2
//        AudioNode fireAudio = new AudioNode(assetManager, "Sounds/fire.ogg");
//        fireAudio.setPositional(true); 
//        fireAudio.setLocalTranslation(13,-2.9f,6f);
//        fireAudio.setDirectional(false);
//        fireAudio.setVolume(1);
//        fireAudio.setLooping(true);
//        rootNode.attachChild(fireAudio);
//        fireAudio.play();
               
//        String userHome = System.getProperty("user.home" );
//        assetManager.registerLocator( userHome, FileLocator.class);
//        Node level_1 = (Node)assetManager.loadModel("Scenes/level_1.j3o");
//        //level_1.addControl(new RigidBodyControl(0.0f));
//
//        level_1.setName("loaded node");
//        level_1.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
//        //rootNode.attachChild( level_1);
//        level_1.scale(2);
//        
        Spatial level_1 = assetManager.loadModel("Scenes/level_1.j3o");
        level_1.scale(2);
        rootNode.attachChild(level_1);
        
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) level_1);
        scenePhys = new RigidBodyControl(sceneShape, 0);
        level_1.addControl(scenePhys);
        
        rootNode.attachChild(level_1);
        bullet.getPhysicsSpace().add(scenePhys);
//        

        //Alarm Light for level_1
        PointLight alarm_light = new PointLight();
        alarm_light.setColor(ColorRGBA.Red);
        alarm_light.setRadius(30f);
        alarm_light.setPosition(new Vector3f(1, 11.5f,6 ));
        rootNode.addLight(alarm_light);
        
        //Alarm Light for level_2
//        PointLight alarm_light = new PointLight();
//        alarm_light.setColor(ColorRGBA.Red);
//        alarm_light.setRadius(30f);
//        alarm_light.setPosition(new Vector3f(1, 8,6.4f ));
//        rootNode.addLight(alarm_light);
        
        //Alarm Light for level_3 ***POSITION
//          PointLight alarm_light = new PointLight();
//          alarm_light.setColor(ColorRGBA.Red);
//          alarm_light.setRadius(30f);
//          alarm_light.setPosition(new Vector3f(1, 8,4f ));
//          rootNode.addLight(alarm_light);
        
        //Second Alarm Light for level_3  ***POSITION
//          PointLight alarm_light2 = new PointLight();
//          alarm_light2.setColor(ColorRGBA.Red);
//          alarm_light2.setRadius(30f);
//          alarm_light2.setPosition(new Vector3f(25f, 3.7f, 8.8f));
//          rootNode.addLight(alarm_light2);
        
        
        //particles for level_2
//        ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
//        fire.setLocalTranslation(new Vector3f(13,-2.9f,6f));
//        Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
//        mat_red.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
//        fire.setMaterial(mat_red);
//        fire.setImagesX(2); 
//        fire.setImagesY(2); // 2x2 texture animation
//        fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));   // red
//        fire.setStartColor(new ColorRGBA(1f, .5f, 0f, 0.5f)); // yellow
//        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 3, 0));
//        fire.setStartSize(10f);
//        fire.setEndSize(.1f);
//        fire.setGravity(0, 0, 0);
//        fire.setLowLife(.5f);
//        fire.setHighLife(3f);
//        fire.getParticleInfluencer().setVelocityVariation(0.3f);   
//        rootNode.attachChild(fire);

        //Fog Effect
        FilterPostProcessor fpp=new FilterPostProcessor(assetManager);
        fog=new FogFilter();
        fog.setFogColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
        fog.setFogDistance(155);
        fog.setFogDensity(2f);
        fpp.addFilter(fog);
        viewPort.addProcessor(fpp);
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        player.update(tpf);
    }

    public void initGeometries(){
        //level_3 door #1
//        Box box = new Box(.1f, 1.9f, 1.7f);
//        doorOne = new Geometry("ground", box);
//        doorOne.setMaterial(magenta);
//        doorOne.setLocalTranslation(20.4f, 1.8f,6.2f);
//        rootNode.attachChild(doorOne);
        
        //level_3 door #1
//        Box box2 = new Box(.1f, 2.2f, 1.7f);
//        doorTwo = new Geometry("ground", box2);
//        doorTwo.setMaterial(magenta);
//        doorTwo.setLocalTranslation(4.8f, 5.4f,6.2f);
//        rootNode.attachChild(doorTwo);
        
        //Collision Shape #1
          Box box = new Box(.1f, 2.2f, 1.7f);
          colShape1 = new Geometry("ground", box);
          colShape1.setMaterial(magenta);
          colShape1.setLocalTranslation(4.8f, 5.4f,6.2f);
          rootNode.attachChild(colShape1);
        //Collision Shape #2
        
        //Collision Shape #3
    }
    
    protected void initCrosshairs() {
        setDisplayStatView(false);
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation( // center
                settings.getWidth() / 2 - ch.getLineWidth() / 2, settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
    }
        
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
        public void initLights() {
            
          //white ambient
//        AmbientLight ambient = new AmbientLight();
//        ambient.setColor(ColorRGBA.White);
//        rootNode.addLight(ambient);
        
            //white directional
            DirectionalLight sun = new DirectionalLight();
            sun.setDirection((new Vector3f(-0.3f, -0.4f, -0.5f)).normalizeLocal());
            sun.setColor(ColorRGBA.White);
            rootNode.addLight(sun);

        //shadow
//        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 1024, 2);
//        dlsr.setLight(sun);
//        viewPort.addProcessor(dlsr); 
        
    }
        
        public void initMaterial() {
             magenta = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        magenta.setBoolean("UseMaterialColors", true);
	magenta.setColor("Ambient", ColorRGBA.Gray);
        magenta.setColor("Diffuse", ColorRGBA.Blue);
        magenta.setColor("Specular", ColorRGBA.Red);
        magenta.setFloat("Shininess", 2f); // shininess from 1-128
        }
        
        public void initCam() {
            flyCam.setEnabled(true);
            flyCam.setMoveSpeed(40);
            cam.setLocation(new Vector3f(25, 3, 6));
            cam.lookAt(cam.getLocation(), Vector3f.UNIT_X);
        }
        
}
