package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
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
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * test
 * @author 
 */
public class Main extends SimpleApplication implements MouseListener {

  
  Game game;
  private Spatial level_1;
  Node loadedNode;
  BulletAppState bullet;
  RigidBodyControl scenePhys;
  Material mat1, magenta;
  Geometry doorOne, doorTwo, colShape1, colShape2, colShape3;
  Geometry key1pt_1, key1pt_2, key1pt_3, key1pt_4, key2pt_1, key2pt_2, key2pt_3;
  Light alarm_light;

  public Player player;
  int currentX, currentY, oldX, oldY;
    Geometry geomBox, geo;
    CustomMesh mesh;
    JFrame frame;
    JPanel panel, panel2, panel3;
    JLabel label1;
    JButton send, backspace, delete;
    ArrayList verts = new ArrayList();
    boolean meshOn = false;
    int radius = 3;
    Vector3f[] vertices;

    
    public static void main(String[] args) {

       
       Main app = new Main();
       app.start();
       
        
    }

    @Override
    public void simpleInitApp() {
        
        initCrosshairs();
        initLights();
        initMaterial();
        bullet = new BulletAppState();
        stateManager.attach(bullet);
        
        initGeometries();
        player = new Player(cam, bullet, this);
        player.setUpKeys();
        
        StartScreen ss = new StartScreen();
        stateManager.attach(ss);
        
        }
    
    @Override
    public void simpleUpdate(float tpf) {
        player.update(tpf);
        

            }
    
    public void initGui() {
        
        frame = new JFrame();
        frame.setPreferredSize(new Dimension(320, 520));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //background panel
        panel = new JPanel();
        panel.setBackground(Color.gray);
        //panel that collects points
        panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(285, 400));
        panel2.setBackground(Color.black);
        panel2.addMouseListener(this);
        //cracked screen panel
        panel3 = new JPanel();
        panel3.setPreferredSize(new Dimension(285, 400));
        BufferedImage myPicture;
        try {
            myPicture = ImageIO.read(new File("C:\\Users\\Matt\\Desktop\\crack.png"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            panel3.add(picLabel);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        panel3.setOpaque(false);
        panel2.add(panel3);
        
        verts = new ArrayList();
        
        JButton send = new JButton();
        send.setIcon(new ImageIcon("C:\\Users\\Matt\\Desktop\\send.gif"));
        send.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                initMesh();
                frame.dispose();
            }
        });
        JButton delete = new JButton();
        delete.setIcon(new ImageIcon("C:\\Users\\Matt\\Desktop\\delete.gif"));
        delete.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                 verts.clear();
                 panel2.repaint();
                 
            }
        });
        JButton backspace = new JButton();
        backspace.setIcon(new ImageIcon("C:\\Users\\Matt\\Desktop\\backspace.gif"));
        backspace.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                 verts.remove(verts.size()-1);
            }
        });

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 4;
        panel.add(panel2,c);
        c.gridx = 1;
        c.gridy = 1;
        panel.add(backspace, c);
        c.gridx = 1;
        c.gridy = 2;
        panel.add(send,c);
        c.gridx = 1;
        c.gridy = 3;
        panel.add(delete,c);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        
        
        setDisplayFps(true);
        setDisplayStatView(false);
        frame.setLocationRelativeTo(null);
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
	
        Vector3f[] vertices = new Vector3f[verts.size()];
        
        for(int i = 0; i < verts.size(); i++)
            vertices[i] = (Vector3f)verts.get(i);
            
        
        mesh = new CustomMesh(vertices, .5f, true);
        
        Geometry geo = new Geometry("mesh", mesh); 
        //geo.scale(.5f);
        geo.setLocalTranslation(7, 3, 3);
	Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	mat.setBoolean("VertexColor", true);
//        mat.getAdditionalRenderState().setWireframe(true); 

        geo.setMaterial(mat);
	geo.setShadowMode(RenderQueue.ShadowMode.Cast);
                
        geo.addControl(new RigidBodyControl(.05f));
        geo.getControl(RigidBodyControl.class).setRestitution(1);
        rootNode.attachChild(geo);
        bullet.getPhysicsSpace().add(geo);
        meshOn = true;

    }
    
    public void initGeometries(){
               
        
        //level_3 Key1 Collision Shapes
//          Cylinder cyl1 = new Cylinder(15,15,.01f,.5f);
//          key1pt_1 = new Geometry("ground", cyl1);
//          key1pt_1.rotate(0f, 90.0f * FastMath.DEG_TO_RAD, 0f);
//          key1pt_1.setMaterial(magenta);
//          key1pt_1.setLocalTranslation(20.625f, 2f,8.35f);
//          rootNode.attachChild(key1pt_1);
//          
//          Cylinder cyl2 = new Cylinder(15,15,.01f,.5f);
//          key1pt_2 = new Geometry("ground", cyl2);
//          key1pt_2.rotate(0f, 90.0f * FastMath.DEG_TO_RAD, 0f);
//          key1pt_2.setMaterial(magenta);
//          key1pt_2.setLocalTranslation(20.625f, 2f,8.75f);
//          rootNode.attachChild(key1pt_2);
//          
//          Cylinder cyl3 = new Cylinder(15,15,.01f,.5f);
//          key1pt_3 = new Geometry("ground", cyl3);
//          key1pt_3.rotate(0f, 90.0f * FastMath.DEG_TO_RAD, 0f);
//          key1pt_3.setMaterial(magenta);
//          key1pt_3.setLocalTranslation(20.625f, 2.35f,8.35f);
//          rootNode.attachChild(key1pt_3);
//          
//          Cylinder cyl4 = new Cylinder(15,15,.01f,.5f);
//          key1pt_4 = new Geometry("ground", cyl4);
//          key1pt_4.rotate(0f, 90.0f * FastMath.DEG_TO_RAD, 0f);
//          key1pt_4.setMaterial(magenta);
//          key1pt_4.setLocalTranslation(20.625f, 2.35f,8.75f);
//          rootNode.attachChild(key1pt_4);
        
                //level_3 Key2 Collision Shapes 
        
     
          Cylinder cyl1 = new Cylinder(15,15,.01f,.5f);
          key2pt_1 = new Geometry("ground", cyl1);
          key2pt_1.rotate(0f, 90.0f * FastMath.DEG_TO_RAD, 0f);
          key2pt_1.setMaterial(magenta);
          key2pt_1.setLocalTranslation(4.8f, 6.2f,8.54f);
          rootNode.attachChild(key2pt_1);
          
          Cylinder cyl2 = new Cylinder(15,15,.01f,.5f);
          key2pt_2 = new Geometry("ground", cyl2);
          key2pt_2.rotate(0f, 90.0f * FastMath.DEG_TO_RAD, 0f);
          key2pt_2.setMaterial(magenta);
          key2pt_2.setLocalTranslation(4.8f, 5.92f,8.47f);
          rootNode.attachChild(key2pt_2);
          
          Cylinder cyl3 = new Cylinder(15,15,.01f,.5f);
          key2pt_3 = new Geometry("ground", cyl3);
          key2pt_3.rotate(0f, 90.0f * FastMath.DEG_TO_RAD, 0f);
          key2pt_3.setMaterial(magenta);
          key2pt_3.setLocalTranslation(4.8f, 5.79f,8.69f);
          rootNode.attachChild(key2pt_3);
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
        
        public void drawCircle(int x, int y, Object source, boolean fill) {
        if(source instanceof JPanel) {
            Graphics g = ((JComponent) source).getGraphics();
            g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
            g.setColor(Color.red);
            if (fill) {
                g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
            }
        } // else ignore
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

            public void mouseExited(MouseEvent m) {
        
    }
    public void mouseEntered(MouseEvent m) {
        
    }
    public void mouseReleased(MouseEvent m) {
        verts.add(new Vector3f(m.getX() / 100f, (frame.getHeight()- m.getY() - 40) / 100f, 0));
        drawCircle(m.getX(), m.getY(), panel2,true);
 
        }
    
    
    public void mousePressed(MouseEvent m) {
      
    }
    public void mouseClicked(MouseEvent m) {
        
        
    }
}
