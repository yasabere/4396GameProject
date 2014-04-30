package mygame;

/**
 * This is a template that provides a start setup for many projects. It provides
 * initialization of all major components: initAppScreen(app); initGui();
 * initMaterials(); initLightandShadow(); initGeometries(); initCam();
 */
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

import com.jme3.system.AppSettings;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Main extends SimpleApplication implements ActionListener {

    public static Material mat1, mat2;
    Geometry geomGround;
    BulletAppState bullet;
    VehicleControl vehicle;
    private final float accelerationForce = 1000.0f;
    private final float brakeForce = 100.0f;
    private float steeringValue = 0;
    private float accelerationValue = 0;
    private Vector3f jumpForce = new Vector3f(0, 3000, 0);

    public static void main(String[] args) {
        Main app = new Main();
        initAppScreen(app);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        initGui();
        initMaterials();
        initLightandShadow();
        initGeometries();
        initCam();
        initPhysics();
        initKeys();
        initVehicle();
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    // -------------------------------------------------------------------------
    private static void initAppScreen(SimpleApplication app) {
        AppSettings aps = new AppSettings(true);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        screen.width *= 0.75;
        screen.height *= 0.75;
        aps.setResolution(screen.width, screen.height);
        app.setSettings(aps);
        app.setShowSettings(false);
    }

    private void initMaterials() {
        mat1 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat1.setBoolean("UseMaterialColors", true);
        mat1.setColor("Ambient", ColorRGBA.Red);
        mat1.setColor("Diffuse", ColorRGBA.Green);
        mat1.setColor("Specular", ColorRGBA.Gray);
        mat1.setFloat("Shininess", 4f); // shininess from 1-128

        mat2 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat2.setBoolean("UseMaterialColors", true);
        mat2.setColor("Ambient", ColorRGBA.Gray);
        mat2.setColor("Diffuse", ColorRGBA.Blue);
        mat2.setColor("Specular", ColorRGBA.Red);
        mat2.setFloat("Shininess", 2f); // shininess from 1-128
    }

    private void initGui() {
        setDisplayFps(true);
        setDisplayStatView(false);
    }

    private void initLightandShadow() {
        // Light1: white, directional
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.7f, -0.3f, -0.9f)).normalizeLocal());
        sun.setColor(ColorRGBA.Gray);
        rootNode.addLight(sun);

        // Light 2: Ambient, gray
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.7f, 0.7f, 0.7f, 1.0f));
        rootNode.addLight(ambient);

        // SHADOW
        // the second parameter is the resolution. Experiment with it! (Must be a power of 2)
        
    }

    private void initGeometries() {
        // Ground
        Box box = new Box(20f, 1f, 20f);
        geomGround = new Geometry("ground", box);
        geomGround.setMaterial(mat2);
        geomGround.setLocalTranslation(0, -5f, 0);
        rootNode.attachChild(geomGround);
        geomGround.rotate(0,0,10f*FastMath.DEG_TO_RAD);

        // define shadow behavior
        geomGround.setShadowMode(ShadowMode.Receive);
    }

    private void initCam() {
        flyCam.setEnabled(true);
        cam.setLocation(new Vector3f(3f, 3f, 25f));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
    }

    private void initPhysics() {
        // initialize the physics engine
        bullet = new BulletAppState();
        stateManager.attach(bullet);
        //bullet.setDebugEnabled(true);

        // add the ground
        RigidBodyControl phyGround = new RigidBodyControl(0.0f); // static: mass = 0
        geomGround.addControl(phyGround);
        bullet.getPhysicsSpace().add(phyGround);

    }

    private void initKeys() {
        inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Reset", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addListener(this, "Lefts");
        inputManager.addListener(this, "Rights");
        inputManager.addListener(this, "Ups");
        inputManager.addListener(this, "Downs");
        inputManager.addListener(this, "Space");
        inputManager.addListener(this, "Reset");
    }

    // how to initialize a pysical vehicle
    // see also: http://hub.jmonkeyengine.org/wiki/doku.php/jme3:advanced:vehicles  
    private void initVehicle() {
        // the compound is the entire car: chassis and tires
        CompoundCollisionShape compoundShape = new CompoundCollisionShape();
        // the box is the chassis
        Box meshChassis = new Box(1.2f, 0.5f, 2.4f);
        Geometry geomChassis = new Geometry("chassis", meshChassis);
        geomChassis.setMaterial(mat1);
        BoxCollisionShape collChassis = new BoxCollisionShape(new Vector3f(1.2f, 0.5f, 2.4f));
        compoundShape.addChildShape(collChassis, new Vector3f(0, 1, 0));

        // the central Node: the vehicle Node.
        // to this node, the VehicleControl is added, which itself takes the
        // CompoundShape as input.
        // VEHICLECONTROL is the core of the physics simulation! It is part
        // of the bullet package.
        Node vehicleNode = new Node("vehicleNode");
        vehicle = new VehicleControl(compoundShape, 400);
        vehicleNode.addControl(vehicle);
        vehicleNode.attachChild(geomChassis);
        geomChassis.setLocalTranslation(0, 1, 0);

        // Configure the VehicleControl
        // these values come from the demo website.
        float stiffness = 60.0f;//200=f1 car
        float compValue = .3f; //(should be lower than damp)
        float dampValue = .4f;
        vehicle.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
        vehicle.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
        vehicle.setSuspensionStiffness(stiffness);
        vehicle.setMaxSuspensionForce(10000.0f);

        // add the wheels
        Vector3f wheelDirection = new Vector3f(0, -1, 0);
        Vector3f wheelAxle = new Vector3f(-1f, 0, 0);
        float radius = 0.5f;
        float restLength = 0.3f;
        float yOff = 0.5f;
        float xOff = 1.5f;
        float zOff = 2f;

        Cylinder wheelMesh = new Cylinder(16, 16, radius, radius * 0.6f, true);

        Node node1 = new Node("wheel 1 node");
        Geometry wheels1 = new Geometry("wheel 1", wheelMesh);
        node1.attachChild(wheels1);
        wheels1.rotate(0, FastMath.HALF_PI, 0);
        wheels1.setMaterial(mat1);
        vehicle.addWheel(node1, new Vector3f(-xOff, yOff, zOff),
                wheelDirection, wheelAxle, restLength, radius, true);

        Node node2 = new Node("wheel 2 node");
        Geometry wheels2 = new Geometry("wheel 2", wheelMesh);
        node2.attachChild(wheels2);
        wheels2.rotate(0, FastMath.HALF_PI, 0);
        wheels2.setMaterial(mat1);
        vehicle.addWheel(node2, new Vector3f(xOff, yOff, zOff),
                wheelDirection, wheelAxle, restLength, radius, true);

        Node node3 = new Node("wheel 3 node");
        Geometry wheels3 = new Geometry("wheel 3", wheelMesh);
        node3.attachChild(wheels3);
        wheels3.rotate(0, FastMath.HALF_PI, 0);
        wheels3.setMaterial(mat1);
        vehicle.addWheel(node3, new Vector3f(-xOff, yOff, -zOff),
                wheelDirection, wheelAxle, restLength, radius, false);

        Node node4 = new Node("wheel 4 node");
        Geometry wheels4 = new Geometry("wheel 4", wheelMesh);
        node4.attachChild(wheels4);
        wheels4.rotate(0, FastMath.HALF_PI, 0);
        wheels4.setMaterial(mat1);
        vehicle.addWheel(node4, new Vector3f(xOff, yOff, -zOff),
                wheelDirection, wheelAxle, restLength, radius, false);

        // add everything to the scene graph
        vehicleNode.attachChild(node1);
        vehicleNode.attachChild(node2);
        vehicleNode.attachChild(node3);
        vehicleNode.attachChild(node4);
        rootNode.attachChild(vehicleNode);

        // and add it to the physics engine
        bullet.getPhysicsSpace().add(vehicle);
    }

    //  STEERING
    public void onAction(String binding, boolean down, float tpf) {
        if (binding.equals("Lefts")) {
            if (down) {
                steeringValue += .5f;
            } else {
                steeringValue += -.5f;
            }
            vehicle.steer(steeringValue);
        } else if (binding.equals("Rights")) {
            if (down) {
                steeringValue += -.5f;
            } else {
                steeringValue += .5f;
            }
            vehicle.steer(steeringValue);
        } else if (binding.equals("Ups")) {
            if (down) {
                accelerationValue += accelerationForce;
            } else {
                accelerationValue -= accelerationForce;
            }
            vehicle.accelerate(accelerationValue);
        } else if (binding.equals("Downs")) {
            if (down) {
                vehicle.brake(brakeForce);
            } else {
                vehicle.brake(0f);
            }
        } else if (binding.equals("Space")) {
            if (down) {
                vehicle.applyImpulse(jumpForce, Vector3f.ZERO);
            }
        } else if (binding.equals("Reset")) {
            if (down) {
                System.out.println("Reset");
                vehicle.setPhysicsLocation(Vector3f.ZERO);
                vehicle.setPhysicsRotation(new Matrix3f());
                vehicle.setLinearVelocity(Vector3f.ZERO);
                vehicle.setAngularVelocity(Vector3f.ZERO);
                vehicle.resetSuspension();
            } else {
            }
        }
    }
}
