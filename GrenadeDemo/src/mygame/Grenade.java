/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

/**
 *
 * @author Yaw Asabere
 */
public class Grenade extends Node {
    
    Main main;
    Geometry geo_grenade;
  
    public Grenade(Main main){
        
        this.main = main;
      
        this.initMaterials();
        
        main.getRootNode().attachChild(geo_grenade);
    }
    
    private void initMaterials() {
        Material mat = new Material(main.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        geo_grenade.setMaterial(mat);
    }
}
