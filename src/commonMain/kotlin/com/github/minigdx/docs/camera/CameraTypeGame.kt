package com.github.minigdx.docs.camera

import com.dwursteisen.minigdx.scene.api.relation.ObjectType
import com.github.dwursteisen.minigdx.GameContext
import com.github.dwursteisen.minigdx.GameScreen
import com.github.dwursteisen.minigdx.Seconds
import com.github.dwursteisen.minigdx.ecs.Engine
import com.github.dwursteisen.minigdx.ecs.components.CameraComponent
import com.github.dwursteisen.minigdx.ecs.entities.Entity
import com.github.dwursteisen.minigdx.ecs.entities.EntityFactory
import com.github.dwursteisen.minigdx.ecs.events.Event
import com.github.dwursteisen.minigdx.ecs.systems.EntityQuery
import com.github.dwursteisen.minigdx.ecs.systems.System
import com.github.dwursteisen.minigdx.file.Texture
import com.github.dwursteisen.minigdx.file.get
import com.github.dwursteisen.minigdx.game.Game
import com.github.dwursteisen.minigdx.graph.GraphScene
import com.github.dwursteisen.minigdx.imgui.ImGuiSystem
import com.github.dwursteisen.minigdx.input.Key
import com.github.minigdx.docs.quick.start.Cube
import com.github.minigdx.imgui.WidgetBuilder

class ChangeCameraType : Event

class CameraTypeSystem(gameScreen: GameScreen) : System(EntityQuery.of(CameraComponent::class)) {

    override fun update(delta: Seconds, entity: Entity) = Unit

    override fun update(delta: Seconds) {
        if (input.isKeyJustPressed(Key.SPACE)) {
            emit(ChangeCameraType())
        }
    }

    private val perspective = CameraComponent(
        gameScreen,
        CameraComponent.Type.PERSPECTIVE,
        far = 100f,
        near = 0.1f,
        fov = 25f,
        scale = 0.0f
    )

    private val orthographic = CameraComponent(
        gameScreen,
        CameraComponent.Type.ORTHOGRAPHIC,
        far = 100f,
        near = 0.1f,
        scale = 15f
    )

    override fun onEvent(event: Event, entityQuery: EntityQuery?) = when (event) {
        is ChangeCameraType -> entities.forEach { entity ->
            val camera = entity.get(CameraComponent::class)
            val cameraType = camera.type

            when (cameraType) {
                CameraComponent.Type.PERSPECTIVE -> {
                    entity.remove(camera)
                    entity.add(orthographic)
                }
                CameraComponent.Type.ORTHOGRAPHIC -> {
                    entity.remove(camera)
                    entity.add(perspective)
                }
            }
        }
        else -> Unit
    }
}

class CameraGUI : ImGuiSystem() {

    private val cameras by interested(EntityQuery.Companion.of(CameraComponent::class))

    override fun gui(builder: WidgetBuilder<Texture>) {
        builder.verticalContainer(width = 0.5f) {
            cameras.forEach {
                button(label = it.get(CameraComponent::class).type.toString()) {
                    emit(ChangeCameraType())
                }
            }
        }
    }
}

// tag::loading[]
class CameraTypeGame(override val gameContext: GameContext) : Game {

    private val scene by gameContext.fileHandler.get<GraphScene>("cube.protobuf") // <1>

    override fun createEntities(entityFactory: EntityFactory) { // <2>
        scene.nodes.forEach { node ->
            val entity = entityFactory.createFromNode(node)

            if (node.type == ObjectType.MODEL) {
                entity.add(Cube())
            }
        }
    }

    override fun createSystems(engine: Engine): List<System> {
        return listOf(CameraTypeSystem(gameContext.gameScreen), CameraGUI())
    }
}
// end::loading[]
