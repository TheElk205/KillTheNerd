package at.game.managers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.MySkin;

public class AssetManager implements IResourceRetriever {
	private TextureAtlas							textureAtlas;
	private final HashMap<String, ParticleEffect>	particleEffects	= new HashMap<String, ParticleEffect>();
	private final HashMap<String, TextureAtlas>		animations		= new HashMap<String, TextureAtlas>();

	public AssetManager() {
		this.loadAssets();
	}

	private void loadAssets() {
		final FileHandle atlasHandle = Gdx.files.internal("../core/assets/OverlapDemo/assets/orig/pack/pack.atlas");// "OverlapDemo/assets/orig/pack/pack.atlas"
		final FileHandle rainDropHandle = Gdx.files.internal("../core/assets/OverlapDemo/particles/rain");// "OverlapDemo/particles/rainDrop"

		this.textureAtlas = new TextureAtlas(atlasHandle);

		final ParticleEffect rain = new ParticleEffect();
		rain.load(rainDropHandle, this.textureAtlas);
		this.particleEffects.put("rainDrop", rain);

		this.animations.put("sheep", new TextureAtlas(Gdx.files.internal("../core/assets/OverlapDemo/assets/orig/sprite-animations/sheep/sheep.atlas")));
	}

	@Override
	public BitmapFont getBitmapFont(final String arg0, final int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParticleEffect getParticleEffect(final String name) {
		return this.particleEffects.get(name);
	}

	@Override
	public ProjectInfoVO getProjectVO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileHandle getSCMLFile(final String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SceneVO getSceneVO(final String arg0) {
		// TODO Auto-generated method stub
		return new SceneVO();
	}

	@Override
	public TextureAtlas getSkeletonAtlas(final String arg0) {
		return this.textureAtlas;
	}

	@Override
	public FileHandle getSkeletonJSON(final String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MySkin getSkin() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextureAtlas getSpriteAnimation(final String name) {
		return this.animations.get(name);
	}

	@Override
	public TextureRegion getTextureRegion(final String name) {
		return this.textureAtlas.findRegion(name);
	}

}
