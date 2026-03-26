package comp3170.week5.sceneobjects;

import static org.lwjgl.opengl.GL41.*;

import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Flower extends SceneObject {
	
	private static final String VERTEX_SHADER = "vertex.glsl";
	private static final String FRAGMENT_SHADER = "fragment.glsl";
	private Shader shader;
	
	private final float HEIGHT = 1.0f;
	private final float WIDTH = 0.1f;
	private Vector3f colour = new Vector3f(0f, 0.5f, 0f);

	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;

	private FlowerHead head;
	private SceneObject headPivot;

	private float swayTime = 0.0f;
	private float swayPhase = 0.0f;

	private float baseX = 0.0f;
	private float baseY = 0.0f;
	private boolean baseStored = false;

	public Flower(int nPetals) {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);		
	
		vertices = new Vector4f[] {
			new Vector4f(-WIDTH / 2, 0, 0, 1),
			new Vector4f( WIDTH / 2, 0, 0, 1),
			new Vector4f(-WIDTH / 2, HEIGHT, 0, 1),
			new Vector4f( WIDTH / 2, HEIGHT, 0, 1),
		};

		vertexBuffer = GLBuffers.createBuffer(vertices);
		
	    indices = new int[] {
	    	0, 1, 2,
	    	3, 2, 1,
		};
		    
		indexBuffer = GLBuffers.createIndexBuffer(indices);

		headPivot = new SceneObject();
		headPivot.setParent(this);
		headPivot.getMatrix().translate(0.0f, HEIGHT, 0.0f);

		head = new FlowerHead(nPetals, new Vector3f(1.0f, 0.9f, 0.2f));
		head.setParent(headPivot);

		Random random = new Random();
		swayPhase = random.nextFloat() * (float)(Math.PI * 2.0);
	}
	
	public void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		shader.setUniform("u_mvpMatrix", mvpMatrix);
	    shader.setAttribute("a_position", vertexBuffer);
	    shader.setUniform("u_colour", colour);	    
	    
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
	    glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);		
	}
	
	public void update(float dt) {
		if (!baseStored) {
			baseX = getMatrix().m30();
			baseY = getMatrix().m31();
			baseStored = true;
		}

		swayTime += dt;
		float angle = 0.18f * (float)Math.sin(2.0f * swayTime + swayPhase);

		getMatrix().identity()
			.translate(baseX, baseY, 0.0f)
			.rotateZ(angle);

		head.update(dt);
	}
}