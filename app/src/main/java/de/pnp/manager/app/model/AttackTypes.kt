package de.pnp.manager.app.model

import de.pnp.manager.model.character.data.IAttackTypes

enum class AttackTypes : IAttackTypes {
    head, upperBody, legs, arm, ignoreArmor, direct;
}