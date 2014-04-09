package mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

public class Player implements ActionListener{
    Main main;
    private Node pNode;
    private CharacterControl pControl;
    private Camera cam;
    private Vector3f walkDir, viewDir, pLoc;
    private boolean forward, backward, left, right;
    InputManager inputManager;
    
    public Player(Camera cam, BulletAppState bullet, Main main){
        forward = backward = left = right = false;
        this.main = main;
        inputManager = main.getInputManager();
        
        pNode = new Node("Player Node");
        pControl = new CharacterControl(new CapsuleCollisionShape(1.0f, 1.8f), .1f);
        pControl.setPhysicsLocation(new Vector3f(0f, 5f, 0f));
        pControl.setGravity(10f);
        pControl.setJumpSpeed(10f);
        pNode.addControl(pControl);
        main.getRootNode().attachChild(pNode);
        bullet.getPhysicsSpace().add(pControl);
        
        this.cam = cam;
       
        walkDir = new Vector3f(0f, 0f, 0f);
        pLoc = new Vector3f(0f, 0f, 0f);
        viewDir = new Vector3f(0f, 0f, 0f);
        
        pControl.warp(new Vector3f(0f, 5f, 0f));
    }
    
    public void setUpKeys() {
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
    inputManager.addMapping("Warp", new KeyTrigger(KeyInput.KEY_K));
    inputManager.addMapping("PickUp", new KeyTrigger(KeyInput.KEY_E));
    inputManager.addListener(this, "Left");
    inputManager.addListener(this, "Right");
    inputManager.addListener(this, "Forward");
    inputManager.addListener(this, "Backward");
    inputManager.addListener(this, "Jump");
    inputManager.addListener(this, "Warp");
    inputManager.addListener(this, "PickUp");
  }
    
    public void onAction(String binding, boolean isPressed, float tpf) {
    if (binding.equals("Left")) {
      if (isPressed){left = true;}
      else { left = false; }
    } 
    else if (binding.equals("Right")) {
      if (isPressed){right = true;}
      else { right = false; }
    } 
    else if (binding.equals("Forward")) {
      if (isPressed){forward = true;}
      else { forward = false; }
    } 
    else if (binding.equals("Backward")) {
      if (isPressed){backward = true;}
      else { backward = false; } 
    } 
    else if (binding.equals("Jump")) {
      if (isPressed) { pControl.jump(); }
    }
    else if (binding.equals("Warp")){
        if (isPressed) { pControl.warp(new Vector3f(0f, 3f, 0f)); }
    }
  }
    
    public void update(float tpf){
        Vector3f camDir = cam.getDirection().mult(0.2f);
        Vector3f camLeft = cam.getLeft().mult(0.2f);
        camDir.y = 0;
        camLeft.y = 0;
        viewDir.set(camDir);
        walkDir.set(0f, 0f, 0f);
        if (left) {
            walkDir.addLocal(camLeft);
        }
        else if (right) {
            walkDir.addLocal(camLeft.negate());
        }
        if (forward) {
            walkDir.addLocal(camDir);
        }
        else if (backward) {
            walkDir.addLocal(camDir.negate());
        }
        pControl.setWalkDirection(walkDir);
        pControl.setViewDirection(viewDir);
        
        pLoc.set(pNode.getLocalTranslation());
        cam.setLocation(pLoc);
    }
    
}
    

