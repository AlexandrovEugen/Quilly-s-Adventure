package com.game.quillyjumper.ai

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.game.quillyjumper.ecs.component.*

interface EntityState : State<Entity> {
    val aniType: AnimationType
    val loopAni: Boolean

    override fun enter(entity: Entity) {
        entity.aniCmp.run {
            animationType = aniType
            loopAnimation = loopAni
            animationTime = 0f
        }
        entity.stateCmp.stateTime = 0f
    }

    override fun exit(entity: Entity) {
    }

    override fun update(entity: Entity) {
    }

    override fun onMessage(entity: Entity, telegram: Telegram): Boolean = false
}

enum class DefaultState(override val aniType: AnimationType, override val loopAni: Boolean = true) : EntityState {
    NONE(AnimationType.IDLE),
    DEATH(AnimationType.DEATH, false) {
        override fun enter(entity: Entity) {
            super.enter(entity)
            with(entity.moveCmp) {
                order = MoveOrder.NONE
                lockMovement = true
            }
            entity.jumpCmp.order = JumpOrder.NONE
            entity.attackCmp.order = AttackOrder.NONE
        }

        override fun update(entity: Entity) {
            if (entity.aniCmp.isAnimationFinished()) {
                entity.statsCmp.alive = false
            }
        }
    };
}