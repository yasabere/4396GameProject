/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author tub97573
 */
public class Field extends Node {
    
    Main main;
    Geometry geo;
    Material mat1;
    FieldController controller;
    
    public Field(Main main){
     this.main = main;
    
     initGeometries();
     initMaterials();
     
     this.controller = new FieldController(this);
     this.addControl(this.controller);
     
    }
    
    private void initMaterials() {
        mat1 = new Material(main.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");


        mat1.setColor("Color", ColorRGBA.DarkGray);
        //mat1.setFloat("Shininess", 4f); // shininess from 1-128

        geo.setMaterial(mat1);
    }
    
    private void initGeometries(){
        Box box = new Box(10f, .1f, 10f);
        geo = new Geometry("floor", box);
        this.attachChild(geo);
    }
    
}
