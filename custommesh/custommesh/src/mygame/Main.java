package mygame;
/**
 * This is a template that provides a start setup for many projects.
 * It provides initialization of all major components:
 *          initAppScreen(app);         
 *          initGui();
 *          initMaterials();
 *          initLightandShadow();
 *          initGeometries();
 *          initCam();
 */
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import java.awt.Dimension;
import java.awt.Toolkit;
import com.jme3.scene.mesh.*;
import com.jme3.scene.Mesh;
import com.jme3.util.BufferUtils;
import com.jme3.scene.VertexBuffer.*;
import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Main extends SimpleApplication implements MouseListener {
    public static Material gold, magenta;
    Geometry geomBox;
    CustomMesh mesh;
    JFrame frame;
    JPanel pane;
    ArrayList verts = new ArrayList();
    boolean meshOn;

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
       // initMesh();
        initCam();
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (!frame.isShowing())
            initMesh();
    }

    // -------------------------------------------------------------------------
    // Initialization Methods
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

    private void initMesh() {
        /*
        Vector3f [] vertices = new Vector3f[8];
        
        vertices[0] = new Vector3f(2,2,0);
        vertices[1] = new Vector3f(4,1,0);
        vertices[2] = new Vector3f(7,0,0);
	vertices[3] = new Vector3f(8,3,0);
        vertices[4] = new Vector3f(6,5,0);
        vertices[5] = new Vector3f(4,3,0);
        vertices[6] = new Vector3f(3,5,0);
	vertices[7] = new Vector3f(1,4,0);*/
       
        if (meshOn)
            return;
        
        Vector3f[] vertices = new Vector3f[verts.size()];
        
        for(int i = 0; i < verts.size(); i++) {
            vertices[i] = (Vector3f)verts.get(i);
            System.out.println(vertices[i].x + "\t" + vertices[i].y);
            System.out.println(i);
        }
        
        mesh = new CustomMesh(vertices, 2, true);
        
        
        Geometry geo = new Geometry("mesh", mesh); 
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setBoolean("VertexColor", true);
	//mat.getAdditionalRenderState().setWireframe(true); 

        geo.setMaterial(mat);
        rootNode.attachChild(geo);
        
        meshOn = true;

    }
    
    private void initMaterials() {
        gold = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        gold.setBoolean("UseMaterialColors", true);
        gold.setColor("Ambient", ColorRGBA.Red);
        gold.setColor("Diffuse", ColorRGBA.Green);
        gold.setColor("Specular", ColorRGBA.Gray);
        gold.setFloat("Shininess", 4f); // shininess from 1-128

        magenta = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        magenta.setBoolean("UseMaterialColors", true);
	magenta.setColor("Ambient", ColorRGBA.Gray);
        magenta.setColor("Diffuse", ColorRGBA.Blue);
        magenta.setColor("Specular", ColorRGBA.Red);
        magenta.setFloat("Shininess", 2f); // shininess from 1-128
    }

    private void initGui() {
       
        frame = new JFrame();
        frame.setPreferredSize(new Dimension(500, 500));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        pane = new JPanel();
        pane.setBounds(10, 10, 300, 300);
        pane.addMouseListener(this);
        frame.add(pane);
        
        frame.pack();
        frame.setVisible(true);
        
        
        setDisplayFps(true);
        setDisplayStatView(false);
    }

    private void initLightandShadow() {
        // Light1: white, directional
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.7f, -1.3f, -0.9f)).normalizeLocal());
        sun.setColor(ColorRGBA.Gray);
        rootNode.addLight(sun);

        // Light 2: Ambient, gray
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.7f, 0.7f, 0.7f, 1.0f));
        rootNode.addLight(ambient);

        // SHADOW
        // the second parameter is the resolution. Experiment with it! (Must be a power of 2)
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 512, 1);
        dlsr.setLight(sun);
        viewPort.addProcessor(dlsr);
    }

    private void initGeometries() {
        // Materials must be initialized firs
	
        // Ground
        Box box = new Box(8f, 2f, 8f);
        geomBox = new Geometry("ground", box);
        geomBox.setMaterial(magenta);
        geomBox.setLocalTranslation(0, -2f, 0);
        rootNode.attachChild(geomBox);

        // define shadow behavior
        geomBox.setShadowMode(ShadowMode.Receive);
    }

    private void initCam() {
        flyCam.setEnabled(true);
        flyCam.setMoveSpeed(40);
        cam.setLocation(new Vector3f(3f, 15f, 15f));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
    }
    
    
    public void mouseExited(MouseEvent m) {
        
    }
    public void mouseEntered(MouseEvent m) {
        
    }
    public void mouseReleased(MouseEvent m) {
        verts.add(new Vector3f(m.getX() / 100f, (frame.getHeight()- m.getY() - 40) / 100f, 0));
        frame.paint(null);
    }
    public void mousePressed(MouseEvent m) {
        
    }
    public void mouseClicked(MouseEvent m) {
        
        
    }
    
    
}
