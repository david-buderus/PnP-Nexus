package de.pnp.manager.app.model

import de.pnp.manager.model.character.data.IAttackTypes

enum class AttackTypes : IAttackTypes {
    HEAD, UPPER_BODY, LEGS, ARM, IGNORE_ARMOR, DIRECT;
}