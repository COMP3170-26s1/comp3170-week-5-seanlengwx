package comp3170.week5;

import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL41.*;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import comp3170.OpenGLException;
import comp3170.IWindowListener;
import comp3170.ShaderLibrary;
import comp3170.Window;
import comp3170.InputManager;

import java.io.File;
import java.io.IOException;

public class Week5 implements IWindowListener {
	private Window window;
	private int width = 800;
	private int height = 800;

	final private File DIRECTORY = new File("src/comp3170/week5/shaders/"); 

	private InputManager input;
	private long oldTime;
	
	private Scene scene;

	public Week5()  throws OpenGLException {		
		window = new Window("Flower field", width, height, this);
		window.setResizable(true);
	    window.run();
	}

	public void init() {
		input = new InputManager(window);		
		oldTime = System.currentTimeMillis();
		
		new ShaderLibrary(DIRECTORY);
		scene = new Scene();
		scene.sceneCam().resize(width, height);
	}
	
	private Vector2i position = new Vector2i();
		
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;

		if (input.wasMouseClicked()) {
			input.getCursorPos(position);

			float xNDC = (2.0f * position.x) / width - 1.0f;
			float yNDC = 1.0f - (2.0f * position.y) / height;

			scene.sceneCam().GetProjectionMatrix(projectionMatrix);
			scene.sceneCam().GetViewMatrix(viewMatrix);

			Matrix4f pv = new Matrix4f(projectionMatrix).mul(viewMatrix);
			Matrix4f invPV = new Matrix4f(pv).invert();

			Vector4f worldPos = new Vector4f(xNDC, yNDC, 0.0f, 1.0f);
			invPV.transform(worldPos);

			scene.createFlower(worldPos);
		}
		
		scene.update(input, deltaTime);
		input.clear();
	}

	private Matrix4f viewMatrix  = new Matrix4f();
	private Matrix4f projectionMatrix  = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();
	
	public void draw() {
		update();
	
		glClearColor(87.0f/255.0f, 60.0f/255.0f, 23.0f/255.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);		
		
		scene.sceneCam().GetViewMatrix(viewMatrix);
		scene.sceneCam().GetProjectionMatrix(projectionMatrix);

		mvpMatrix.set(projectionMatrix).mul(viewMatrix);

		scene.draw(mvpMatrix);
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		glViewport(0,0,width,height);
		scene.sceneCam().resize(width, height);
	}

	@Override
	public void close() {
	}

	public static void main(String[] args) throws IOException, OpenGLException {
		new Week5();
	}
}