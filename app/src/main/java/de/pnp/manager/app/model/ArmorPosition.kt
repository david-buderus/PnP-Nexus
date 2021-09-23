package de.pnp.manager.app.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import de.pnp.manager.model.character.data.IArmorPosition

enum class ArmorPosition : IArmorPosition {
    head, upperBody, arm, legs;
}