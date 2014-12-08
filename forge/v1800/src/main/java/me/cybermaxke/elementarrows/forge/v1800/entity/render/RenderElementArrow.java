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
package me.cybermaxke.elementarrows.forge.v1800.entity.render;

import me.cybermaxke.elementarrows.common.arrow.Arrows;
import me.cybermaxke.elementarrows.common.arrow.ElementArrow;
import me.cybermaxke.elementarrows.forge.v1800.entity.EntityElementArrow;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public class RenderElementArrow extends RenderArrow {
	private final static ResourceLocation[] resources = new ResourceLocation[Short.MAX_VALUE];

	public RenderElementArrow(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float f1, float f2) {
		this.doRender((EntityArrow) entity, x, y, z, f1, f2);
	}

	@Override
	public void doRender(EntityArrow entity, double x, double y, double z, float f1, float f2) {
		super.doRender(entity, x, y, z, f1, f2);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return this.getEntityTexture((EntityArrow) entity);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityArrow entity) {
		short data = ((EntityElementArrow) entity).getElementData();

		if (data != 0) {
			if (resources[data] == null) {
				ElementArrow arrow = Arrows.find(data);

				if (arrow != null) {
					String texture = arrow.getTexture();

					if (texture != null) {
						int index = texture.indexOf(':');

						String part0;
						String part1;

						if (index < 0) {
							part0 = "minecraft";
							part1 = texture;
						} else {
							part0 = texture.substring(0, index);
							part1 = texture.substring(index + 1, texture.length());
						}

						resources[data] = new ResourceLocation(part0 + ":textures/entity/" + part1);
						return resources[data];
					}
				}

				return super.getEntityTexture(entity);
			} else {
				return resources[data];
			}
		} else {
			return super.getEntityTexture(entity);
		}
	}

}