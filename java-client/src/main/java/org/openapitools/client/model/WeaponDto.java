/*
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.client.model.ItemTypeDto;
import org.openapitools.client.model.MaterialDto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * WeaponDto
 */
@JsonPropertyOrder({
  WeaponDto.JSON_PROPERTY_DESCRIPTION,
  WeaponDto.JSON_PROPERTY_EFFECT,
  WeaponDto.JSON_PROPERTY_ID,
  WeaponDto.JSON_PROPERTY_MAXIMUM_STACK_SIZE,
  WeaponDto.JSON_PROPERTY_MINIMUM_STACK_SIZE,
  WeaponDto.JSON_PROPERTY_NAME,
  WeaponDto.JSON_PROPERTY_NOTE,
  WeaponDto.JSON_PROPERTY_RARITY,
  WeaponDto.JSON_PROPERTY_REQUIREMENT,
  WeaponDto.JSON_PROPERTY_SUBTYPE,
  WeaponDto.JSON_PROPERTY_TIER,
  WeaponDto.JSON_PROPERTY_TYPE,
  WeaponDto.JSON_PROPERTY_VENDOR_PRICE,
  WeaponDto.JSON_PROPERTY_DAMAGE,
  WeaponDto.JSON_PROPERTY_DICE,
  WeaponDto.JSON_PROPERTY_HIT,
  WeaponDto.JSON_PROPERTY_INITIATIVE,
  WeaponDto.JSON_PROPERTY_MATERIAL,
  WeaponDto.JSON_PROPERTY_UPGRADE_SLOTS
})
@JsonTypeName("Weapon")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public class WeaponDto {
  public static final String JSON_PROPERTY_DESCRIPTION = "description";
  private String description;

  public static final String JSON_PROPERTY_EFFECT = "effect";
  private String effect;

  public static final String JSON_PROPERTY_ID = "id";
  private String id;

  public static final String JSON_PROPERTY_MAXIMUM_STACK_SIZE = "maximumStackSize";
  private Integer maximumStackSize;

  public static final String JSON_PROPERTY_MINIMUM_STACK_SIZE = "minimumStackSize";
  private Integer minimumStackSize;

  public static final String JSON_PROPERTY_NAME = "name";
  private String name;

  public static final String JSON_PROPERTY_NOTE = "note";
  private String note;

  /**
   * Gets or Sets rarity
   */
  public enum RarityEnum {
    UNKNOWN("UNKNOWN"),
    
    COMMON("COMMON"),
    
    RARE("RARE"),
    
    EPIC("EPIC"),
    
    LEGENDARY("LEGENDARY"),
    
    GODLIKE("GODLIKE");

    private String value;

    RarityEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RarityEnum fromValue(String value) {
      for (RarityEnum b : RarityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_RARITY = "rarity";
  private RarityEnum rarity;

  public static final String JSON_PROPERTY_REQUIREMENT = "requirement";
  private String requirement;

  public static final String JSON_PROPERTY_SUBTYPE = "subtype";
  private ItemTypeDto subtype;

  public static final String JSON_PROPERTY_TIER = "tier";
  private Integer tier;

  public static final String JSON_PROPERTY_TYPE = "type";
  private ItemTypeDto type;

  public static final String JSON_PROPERTY_VENDOR_PRICE = "vendorPrice";
  private Integer vendorPrice;

  public static final String JSON_PROPERTY_DAMAGE = "damage";
  private Integer damage;

  public static final String JSON_PROPERTY_DICE = "dice";
  private String dice;

  public static final String JSON_PROPERTY_HIT = "hit";
  private Integer hit;

  public static final String JSON_PROPERTY_INITIATIVE = "initiative";
  private Float initiative;

  public static final String JSON_PROPERTY_MATERIAL = "material";
  private MaterialDto material;

  public static final String JSON_PROPERTY_UPGRADE_SLOTS = "upgradeSlots";
  private Integer upgradeSlots;

  public WeaponDto() {
  }

  public WeaponDto description(String description) {
    
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_DESCRIPTION)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getDescription() {
    return description;
  }


  @JsonProperty(JSON_PROPERTY_DESCRIPTION)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setDescription(String description) {
    this.description = description;
  }


  public WeaponDto effect(String effect) {
    
    this.effect = effect;
    return this;
  }

   /**
   * Get effect
   * @return effect
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_EFFECT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getEffect() {
    return effect;
  }


  @JsonProperty(JSON_PROPERTY_EFFECT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEffect(String effect) {
    this.effect = effect;
  }


  public WeaponDto id(String id) {
    
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getId() {
    return id;
  }


  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setId(String id) {
    this.id = id;
  }


  public WeaponDto maximumStackSize(Integer maximumStackSize) {
    
    this.maximumStackSize = maximumStackSize;
    return this;
  }

   /**
   * Get maximumStackSize
   * @return maximumStackSize
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_MAXIMUM_STACK_SIZE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getMaximumStackSize() {
    return maximumStackSize;
  }


  @JsonProperty(JSON_PROPERTY_MAXIMUM_STACK_SIZE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setMaximumStackSize(Integer maximumStackSize) {
    this.maximumStackSize = maximumStackSize;
  }


  public WeaponDto minimumStackSize(Integer minimumStackSize) {
    
    this.minimumStackSize = minimumStackSize;
    return this;
  }

   /**
   * Get minimumStackSize
   * @return minimumStackSize
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_MINIMUM_STACK_SIZE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getMinimumStackSize() {
    return minimumStackSize;
  }


  @JsonProperty(JSON_PROPERTY_MINIMUM_STACK_SIZE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setMinimumStackSize(Integer minimumStackSize) {
    this.minimumStackSize = minimumStackSize;
  }


  public WeaponDto name(String name) {
    
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_NAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getName() {
    return name;
  }


  @JsonProperty(JSON_PROPERTY_NAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setName(String name) {
    this.name = name;
  }


  public WeaponDto note(String note) {
    
    this.note = note;
    return this;
  }

   /**
   * Get note
   * @return note
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_NOTE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getNote() {
    return note;
  }


  @JsonProperty(JSON_PROPERTY_NOTE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setNote(String note) {
    this.note = note;
  }


  public WeaponDto rarity(RarityEnum rarity) {
    
    this.rarity = rarity;
    return this;
  }

   /**
   * Get rarity
   * @return rarity
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_RARITY)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public RarityEnum getRarity() {
    return rarity;
  }


  @JsonProperty(JSON_PROPERTY_RARITY)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setRarity(RarityEnum rarity) {
    this.rarity = rarity;
  }


  public WeaponDto requirement(String requirement) {
    
    this.requirement = requirement;
    return this;
  }

   /**
   * Get requirement
   * @return requirement
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_REQUIREMENT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getRequirement() {
    return requirement;
  }


  @JsonProperty(JSON_PROPERTY_REQUIREMENT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setRequirement(String requirement) {
    this.requirement = requirement;
  }


  public WeaponDto subtype(ItemTypeDto subtype) {
    
    this.subtype = subtype;
    return this;
  }

   /**
   * Get subtype
   * @return subtype
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_SUBTYPE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public ItemTypeDto getSubtype() {
    return subtype;
  }


  @JsonProperty(JSON_PROPERTY_SUBTYPE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSubtype(ItemTypeDto subtype) {
    this.subtype = subtype;
  }


  public WeaponDto tier(Integer tier) {
    
    this.tier = tier;
    return this;
  }

   /**
   * Get tier
   * @return tier
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_TIER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getTier() {
    return tier;
  }


  @JsonProperty(JSON_PROPERTY_TIER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setTier(Integer tier) {
    this.tier = tier;
  }


  public WeaponDto type(ItemTypeDto type) {
    
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_TYPE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public ItemTypeDto getType() {
    return type;
  }


  @JsonProperty(JSON_PROPERTY_TYPE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setType(ItemTypeDto type) {
    this.type = type;
  }


  public WeaponDto vendorPrice(Integer vendorPrice) {
    
    this.vendorPrice = vendorPrice;
    return this;
  }

   /**
   * Get vendorPrice
   * @return vendorPrice
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_VENDOR_PRICE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getVendorPrice() {
    return vendorPrice;
  }


  @JsonProperty(JSON_PROPERTY_VENDOR_PRICE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setVendorPrice(Integer vendorPrice) {
    this.vendorPrice = vendorPrice;
  }


  public WeaponDto damage(Integer damage) {
    
    this.damage = damage;
    return this;
  }

   /**
   * Get damage
   * @return damage
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_DAMAGE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getDamage() {
    return damage;
  }


  @JsonProperty(JSON_PROPERTY_DAMAGE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setDamage(Integer damage) {
    this.damage = damage;
  }


  public WeaponDto dice(String dice) {
    
    this.dice = dice;
    return this;
  }

   /**
   * Get dice
   * @return dice
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_DICE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getDice() {
    return dice;
  }


  @JsonProperty(JSON_PROPERTY_DICE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setDice(String dice) {
    this.dice = dice;
  }


  public WeaponDto hit(Integer hit) {
    
    this.hit = hit;
    return this;
  }

   /**
   * Get hit
   * @return hit
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_HIT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getHit() {
    return hit;
  }


  @JsonProperty(JSON_PROPERTY_HIT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setHit(Integer hit) {
    this.hit = hit;
  }


  public WeaponDto initiative(Float initiative) {
    
    this.initiative = initiative;
    return this;
  }

   /**
   * Get initiative
   * @return initiative
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_INITIATIVE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Float getInitiative() {
    return initiative;
  }


  @JsonProperty(JSON_PROPERTY_INITIATIVE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setInitiative(Float initiative) {
    this.initiative = initiative;
  }


  public WeaponDto material(MaterialDto material) {
    
    this.material = material;
    return this;
  }

   /**
   * Get material
   * @return material
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_MATERIAL)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public MaterialDto getMaterial() {
    return material;
  }


  @JsonProperty(JSON_PROPERTY_MATERIAL)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setMaterial(MaterialDto material) {
    this.material = material;
  }


  public WeaponDto upgradeSlots(Integer upgradeSlots) {
    
    this.upgradeSlots = upgradeSlots;
    return this;
  }

   /**
   * Get upgradeSlots
   * @return upgradeSlots
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_UPGRADE_SLOTS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getUpgradeSlots() {
    return upgradeSlots;
  }


  @JsonProperty(JSON_PROPERTY_UPGRADE_SLOTS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setUpgradeSlots(Integer upgradeSlots) {
    this.upgradeSlots = upgradeSlots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WeaponDto weapon = (WeaponDto) o;
    return Objects.equals(this.description, weapon.description) &&
        Objects.equals(this.effect, weapon.effect) &&
        Objects.equals(this.id, weapon.id) &&
        Objects.equals(this.maximumStackSize, weapon.maximumStackSize) &&
        Objects.equals(this.minimumStackSize, weapon.minimumStackSize) &&
        Objects.equals(this.name, weapon.name) &&
        Objects.equals(this.note, weapon.note) &&
        Objects.equals(this.rarity, weapon.rarity) &&
        Objects.equals(this.requirement, weapon.requirement) &&
        Objects.equals(this.subtype, weapon.subtype) &&
        Objects.equals(this.tier, weapon.tier) &&
        Objects.equals(this.type, weapon.type) &&
        Objects.equals(this.vendorPrice, weapon.vendorPrice) &&
        Objects.equals(this.damage, weapon.damage) &&
        Objects.equals(this.dice, weapon.dice) &&
        Objects.equals(this.hit, weapon.hit) &&
        Objects.equals(this.initiative, weapon.initiative) &&
        Objects.equals(this.material, weapon.material) &&
        Objects.equals(this.upgradeSlots, weapon.upgradeSlots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, effect, id, maximumStackSize, minimumStackSize, name, note, rarity, requirement, subtype, tier, type, vendorPrice, damage, dice, hit, initiative, material, upgradeSlots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WeaponDto {\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    effect: ").append(toIndentedString(effect)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    maximumStackSize: ").append(toIndentedString(maximumStackSize)).append("\n");
    sb.append("    minimumStackSize: ").append(toIndentedString(minimumStackSize)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    requirement: ").append(toIndentedString(requirement)).append("\n");
    sb.append("    subtype: ").append(toIndentedString(subtype)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    vendorPrice: ").append(toIndentedString(vendorPrice)).append("\n");
    sb.append("    damage: ").append(toIndentedString(damage)).append("\n");
    sb.append("    dice: ").append(toIndentedString(dice)).append("\n");
    sb.append("    hit: ").append(toIndentedString(hit)).append("\n");
    sb.append("    initiative: ").append(toIndentedString(initiative)).append("\n");
    sb.append("    material: ").append(toIndentedString(material)).append("\n");
    sb.append("    upgradeSlots: ").append(toIndentedString(upgradeSlots)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

