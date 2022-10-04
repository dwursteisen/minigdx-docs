package com.github.minigdx.docs.quick.start

import com.dwursteisen.minigdx.scene.api.relation.ObjectType
import com.github.dwursteisen.minigdx.GameContext
import com.github.dwursteisen.minigdx.ecs.Engine
import com.github.dwursteisen.minigdx.ecs.entities.EntityFactory
import com.github.dwursteisen.minigdx.ecs.systems.System
import com.github.dwursteisen.minigdx.file.get
import com.github.dwursteisen.minigdx.game.Game
import com.github.dwursteisen.minigdx.graph.GraphScene

// tag::cubegame[]
class CubeGame(override val gameContext: GameContext) : Game {

    private val scene by gameContext.fileHandler.get<GraphScene>("cube.protobuf") // <1>

    override fun createEntities(entityFactory: EntityFactory) {
        // Create all entities needed at startup
        // The scene is the node graph that can be updated in Blender
        scene.nodes.forEach { node ->
            // Create an entity using all information from this node (model, position, camera, ...)
            // <2>
            val entity = entityFactory.createFromNode(node)

            // The node is the cube from the Blender file
            if (node.type == ObjectType.MODEL) {
                // Mark this entity as being a cube
                entity.add(Cube())
            }
        }
    }

    override fun createSystems(engine: Engine): List<System> {
        // Create all systems used by the game
        // <3>
        return listOf(RotatingCubeSystem())
    }
}
// end::cubegame[]
