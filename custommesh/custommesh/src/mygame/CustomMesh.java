/*
 * NEEDS TO BE I.NEXT AVAILABLE VERTEX, NOT I+1 AND I+2!!!
 * BASICALLY DO WHAT THE ITERATE METHOD DOES, EVEN WHEN SIMPLY CHECKING FOR VERTICES TO CONSTRUCT TRIANGLES
 * LINKED LIST?! OR JUST ADD NEXT METHOD..?
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
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
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 *** Generates a mesh, given the vertices of one side and a depth
 * 
 * @param verts - Vector3f[], the vertices of one side of the mesh
 * @param depth - float. the depths of the mesh
 * @param vertColor - boolean, true for random vertex coloration
 * 
 * @author Nick Killion
 */
public class CustomMesh extends Mesh {
    
    protected Vector3f[] frontVerts;
    protected Vector3f[] backVerts;
    protected int[] triangleIndicesArray;
    protected ArrayList triangleIndices;
    protected ArrayList dog;
    protected int length; //num of verts per side
    protected float depth;
    protected boolean vertColor;
    protected float[] colorArray;
    
    public CustomMesh(Vector3f[] verts, float depth, boolean vertColor) {
        	
        frontVerts = verts.clone();
        length = verts.length;
	backVerts = new Vector3f[length];
        triangleIndices = new ArrayList();
	this.depth = depth;
	this.vertColor = vertColor;
        
        simplify();
        generateBackSideVerts();
        generateFrontTriangles();
	generateBackTriangles();
	generateSideTriangles();
	
	if(vertColor) {
	    colorArray = new float[length * 8];
	    colorVerts();
	}
        finalizeMesh();
    }
    
    private void simplify(){
        //2168 deer project?! simplification
    }
    
    private void generateBackSideVerts() {
        
        //generate back side vertices
        for (int j = 0; j < length; j++)
            backVerts[j] = new Vector3f(frontVerts[j].x, frontVerts[j].y, frontVerts[j].z + depth);
    }
    
    //dog-ear method
    private void generateFrontTriangles() {
        
        //arraylist of verts for dogear - keeps track of removed vertices
        dog = new ArrayList();
        
        //the two lines incident to the vertex
        Vector2f sideOne, sideTwo;
        float sideOneX, sideOneY, sideTwoX, sideTwoY;
        
        int i = 0;
	
        while (dog.size() < (length - 3))  //end when only three vertices left (this is last triangle)
        {
	    //get indices
	    int next = iterate(i, 1);
	    int nextnext = iterate(i, 2);
	    
	    //get vectors
	    sideOneX = frontVerts[next].x - frontVerts[i].x;
	    sideOneY = frontVerts[next].y - frontVerts[i].y;
	    sideTwoX = frontVerts[nextnext].x - frontVerts[next].x;
	    sideTwoY = frontVerts[nextnext].y - frontVerts[next].y;            
	    sideOne = new Vector2f(sideOneX, sideOneY);
	    sideTwo = new Vector2f(sideTwoX, sideTwoY);

	    float res = (sideOne.x * sideTwo.y) - (sideTwo.x * sideOne.y);

	    if (res > 0) {   //if left hand turn
		addTriangle(nextnext, next, i);
		dog.add(next);
	    }
	    i = iterate(i, 1);
        }
	//final triangle
	addTriangle(iterate(i, 2), iterate(i,1), i);
        
    }
    
    private void generateBackTriangles() {
	
	int numTriangleVerts = triangleIndices.size();
	int v1, v2, v3;
	
	for (int i = 0; i < numTriangleVerts; i += 3) {
	    
	    v1 = length + (Integer) triangleIndices.get(i);
	    v2 = length + (Integer) triangleIndices.get(i+1);
	    v3 = length + (Integer) triangleIndices.get(i+2);
	    
	    addTriangle(v3, v2, v1);
	}
    }
    
    private void colorVerts() {
	Random rand = new Random();
	
	for (int i = 0; i < colorArray.length; i++)
	    colorArray[i] = rand.nextFloat();
    }
    
    
   private void generateSideTriangles() {
	for (int i = 0; i < length-1; i++) {
	    addTriangle(i, i+1, i+1+length);
	    addTriangle(i+1+length, i+length, i);
	}
	addTriangle(length-1, 0, length);
	addTriangle(length, length*2-1, length-1);
    }
    
   //returns index of unremoved vertex, numIterations from i. modulo to allow cyclical triangulation
    private int iterate(int i, int numIterations) {
	
	for (int j = 0; j < numIterations; j++)
	    while (dog.contains(++i % length)) {}

	if (i >= length)
	    i = i % length;
	
	return i;
    }
    
    
    //adds triangle to triangleIndices
    private void addTriangle(int v1, int v2, int v3) {
        triangleIndices.add(v1);
        triangleIndices.add(v2);
        triangleIndices.add(v3);
    }
    
    private void finalizeMesh() {
	
	int i;
	
	//combine frontVerts and backVerts into one array
	Vector3f[] verts = new Vector3f[length * 2];
	for (i = 0; i < length; i++)
	    verts[i] = frontVerts[i];
	for(i = length; i < verts.length; i++)
	    verts[i] = backVerts[i - length];
	
	//get triangle indices from arraylist to array
	triangleIndicesArray = new int[triangleIndices.size()];
	for (i = 0; i < triangleIndices.size(); i++)
	    triangleIndicesArray[i] = (Integer) triangleIndices.get(i);
	
	setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(verts));
        setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(triangleIndicesArray));
	if(vertColor)
	    setBuffer(Type.Color, 4, colorArray);
	updateBound();
    }
    
}
