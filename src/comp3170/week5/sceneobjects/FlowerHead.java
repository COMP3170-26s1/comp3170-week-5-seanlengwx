package comp3170.week5.sceneobjects;

import static org.lwjgl.opengl.GL41.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class FlowerHead extends SceneObject {
	
	private static final String VERTEX_SHADER = "vertex.glsl";
	private static final String FRAGMENT_SHADER = "fragment.glsl";
	private Shader shader;

	private Vector3f petalColour = new Vector3f(1.0f,1.0f,1.0f);

	private Vector4f[] vertices;
	private int vertexBuffer;

	private float angle = 0.0f;

	public FlowerHead(int nPetals, Vector3f colour) {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);		
		petalColour = colour;

		float innerRadius = 0.15f;
		float outerRadius = 0.35f;

		vertices = new Vector4f[2 * nPetals + 2];
		vertices[0] = new Vector4f(0, 0, 0, 1);

		for (int i = 0; i <= 2 * nPetals; i++) {
			float theta = (float)(i * Math.PI / nPetals);
			float radius = (i % 2 == 0) ? outerRadius : innerRadius;

			float x = radius * (float)Math.cos(theta);
			float y = radius * (float)Math.sin(theta);

			vertices[i + 1] = new Vector4f(x, y, 0, 1);
		}

		vertexBuffer = GLBuffers.createBuffer(vertices);
	}

	public void update(float dt) {
		angle += dt;
		getMatrix().identity().rotateZ(angle);
	}

	public void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_colour", petalColour);

		glDrawArrays(GL_TRIANGLE_FAN, 0, vertices.length);
	}
}