package de.pnp.manager.component.item;

import java.util.Objects;

/*
 * TODO Auto generate matching items
 *  - Ingot
 *  - Ore
 *  - Nugget
 */
public class Material {

  protected String name;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Material material = (Material) o;
    return Objects.equals(name, material.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
