package at.game.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;

public class AbstractSystem extends EntitySystem {
	protected ImmutableArray<Entity>	entities;
}
