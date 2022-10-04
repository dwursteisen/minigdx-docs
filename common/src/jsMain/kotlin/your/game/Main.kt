package your.game

import com.github.dwursteisen.minigdx.GameApplicationBuilder
import com.github.dwursteisen.minigdx.GameConfiguration
import com.github.dwursteisen.minigdx.GameContext
import com.github.dwursteisen.minigdx.GameScreenConfiguration
import com.github.dwursteisen.minigdx.game.Game
import com.github.minigdx.docs.camera.CameraTypeGame
import com.github.minigdx.docs.quick.start.CubeGame
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.Element
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.get

val gamesFactory: Map<String, (gameContext: GameContext) -> Game> = mapOf(
    "quickstart" to { CubeGame(it) },
    "camera" to { CameraTypeGame(it) },
    "cube" to { MyGame(it) },
    "anotherCube" to { MyGame(it) }
)

fun main() {
    val canvas = document.getElementsByTagName("canvas")
    (0 until canvas.length).map { canvas[it]!! }
        .forEach {
            runGame(it, gamesFactory[it.getAttribute("property")]!!)
        }
}

private fun runGame(
    canvas: Element,
    gameFactory: (gameContext: GameContext) -> Game
) {
    // Get the actual root path and compute the root path to let the game load the resources from
    // the correct URL.
    // This portion may need to be customized regarding the service where the game is deployed (itch.io, ...)
    var rootPath = window.location.protocol + "//" + window.location.host + window.location.pathname
    rootPath = rootPath.replace("index.html", "")


    GameApplicationBuilder(
        gameConfigurationFactory = {
            GameConfiguration(
                // Configure how the game will be rendered in the canvas
                gameScreenConfiguration = GameScreenConfiguration.WithRatio(16f / 9f),
                // What canvas to use to render the game
                canvas = canvas as HTMLCanvasElement,
                // What root path to use. It's use so minigdx can access to resources with the correct URL.
                rootPath = rootPath,
                // Is debug information should be displayed? (hitbox, ...)
                debug = false,
                // The name of your game
                gameName = "My Game - running in a browser"
            )
        },
        // Creation of your game
        gameFactory = gameFactory
    ).start() // ! Don't forget to call this method to start your game!
}
