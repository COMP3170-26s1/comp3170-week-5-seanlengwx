package comp3170.week5;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;
import comp3170.InputManager;
import comp3170.SceneObject;

import comp3170.week5.sceneobjects.*;

public class Scene extends SceneObject {
	private Camera camera;
	private List<Flower> flowers;
	
	public Scene() {
		flowers = new ArrayList<>();

		camera = new Camera();
		camera.setParent(this);

		createFlower(new Vector4f(0.0f,0.0f,0.f,1.0f));		
	}
	
	public Camera sceneCam() {
		return camera;
	}
	
	public void createFlower(Vector4f position) {
		Flower flower = new Flower(20);
		flower.setParent(this);	
		flower.getMatrix().translate(position.x, position.y, 0.0f);
		flowers.add(flower);
	}

	public void update(InputManager input, float dt) {
		camera.update(input, dt);

		for (Flower flower : flowers) {
			flower.update(dt);
		}
	}
}