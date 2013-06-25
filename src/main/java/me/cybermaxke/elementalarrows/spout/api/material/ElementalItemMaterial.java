/**
 * 
 * This software is part of the ElementalArrows
 * 
 * ElementalArrows is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * any later version.
 * 
 * ElementalArrows is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ElementalArrows. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package me.cybermaxke.elementalarrows.spout.api.material;

import org.spout.api.math.Vector2;
import org.spout.vanilla.material.item.VanillaItemMaterial;

public class ElementalItemMaterial extends VanillaItemMaterial {

	public ElementalItemMaterial(String name, VanillaItemMaterial parent, int data) {
		this(name, parent, data, null);
	}

	public ElementalItemMaterial(String name, VanillaItemMaterial parent, int data, Vector2 pos) {
		super(name, parent.getMinecraftId(), data, parent, pos);
	}
}