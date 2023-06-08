package de.pnp.manager.component.character;

import de.pnp.manager.component.attributes.EPrimaryAttribute;
import java.util.List;

public class Talent {

  private final String name;

  private final String group;

  private final EPrimaryAttribute firstAttribute;

  private final EPrimaryAttribute secondAttribute;

  private final EPrimaryAttribute thirdAttribute;


  public Talent(String name, String group, EPrimaryAttribute firstAttribute,
      EPrimaryAttribute secondAttribute, EPrimaryAttribute thirdAttribute) {
    this.name = name;
    this.group = group;
    this.firstAttribute = firstAttribute;
    this.secondAttribute = secondAttribute;
    this.thirdAttribute = thirdAttribute;
  }

  public String getName() {
    return name;
  }

  public String getGroup() {
    return group;
  }

  public EPrimaryAttribute getFirstAttribute() {
    return firstAttribute;
  }

  public EPrimaryAttribute getSecondAttribute() {
    return secondAttribute;
  }

  public EPrimaryAttribute getThirdAttribute() {
    return thirdAttribute;
  }

  public List<EPrimaryAttribute> getAttributes() {
    return List.of(getFirstAttribute(), getSecondAttribute(), getThirdAttribute());
  }
}
