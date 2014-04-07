package mygame;

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
import java.awt.


/**
 *
 * @author tud47465
 */
public class PointDisplay extends JPanel {
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background image each time the panel is repainted.
        g.drawImage(backgroundImage, 0, 0, null);

        // Draw a little square at where the mouse was clicked.
        g.fillRect(pointClicked.x, pointClicked.y, 1, 1);
    }
}
