/**
 * This file is part of ElementalArrows.
 * 
 * Copyright (c) 2014, Cybermaxke
 * 
 * ElementalArrows is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ElementalArrows is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ElementalArrows. If not, see <http://www.gnu.org/licenses/>.
 */
package me.cybermaxke.elementarrows.common.entity;

import me.cybermaxke.elementarrows.common.math.Vector;
import me.cybermaxke.elementarrows.common.source.Source;

public interface EntityProjectile extends Entity {

	/**
	 * Sets the heading of the projectile.
	 * 
	 * @param direction the direction
	 * @param speed the speed
	 * @param spread the spread
	 */
	void setHeading(Vector direction, float speed, float spread);

	/**
	 * Sets the heading of the projectile using the rotation of the shooter entity.
	 * 
	 * @param entity the entity
	 * @param speed the speed
	 * @param spread the spread
	 */
	void setHeading(EntityLiving entity, float speed, float spread);

	/**
	 * Gets the source the projectile has been shot by.
	 * 
	 * @return the source
	 */
	Source getSource();

	/**
	 * Sets the source the projectile has been shot by.
	 * 
	 * @param source the source
	 */
	void setSource(Source source);

}