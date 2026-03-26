package comp3170.week5.sceneobjects;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;

import comp3170.SceneObject;
import comp3170.InputManager;

public class Camera extends SceneObject {

	private float zoom = 6.0f;   // smaller zoom so flowers are clearly visible
	private Matrix4f projectionMatrix = new Matrix4f();

	private int width = 800;
	private int height = 800;

	public Camera() {
		rebuildProjection();
	}

	private void rebuildProjection() {
		float aspect = (float) width / (float) height;

		float halfHeight = zoom;
		float halfWidth = zoom * aspect;

		projectionMatrix.identity().ortho(
			-halfWidth, halfWidth,
			-halfHeight, halfHeight,
			-10.0f, 10.0f
		);
	}

	public void resize(int w, int h) {
		width = w;
		height = h;
		rebuildProjection();
	}

	public Matrix4f GetViewMatrix(Matrix4f dest) {
		return dest.set(getMatrix()).invert();
	}

	public Matrix4f GetProjectionMatrix(Matrix4f dest) {
		return dest.set(projectionMatrix);   // NOT invert
	}

	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(GLFW_KEY_UP)) {
			zoom -= 3.0f * deltaTime;
			if (zoom < 1.5f) zoom = 1.5f;
			rebuildProjection();
		}

		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			zoom += 3.0f * deltaTime;
			rebuildProjection();
		}
	}
}