package echo.driver.testgameopengl.render

import android.opengl.GLES20

class Shader {

    companion object {
        const val VERTEX_SHADER_CODE = """
            uniform mat4 uMVPMatrix; // Матрица модели-вида-проекции
            attribute vec4 vPosition; // Позиция вершины
            attribute vec2 aTexCoord; // Текстурные координаты
            
            varying vec2 vTexCoord; // Передача текстурных координат в фрагментный шейдер
            
            void main() {
                gl_Position = uMVPMatrix * vPosition;
                vTexCoord = aTexCoord;
            }
        """
        const val FRAGMENT_SHADER_CODE = """
            precision mediump float;
            
            uniform vec4 uColor;         // Цвет (если текстура не используется)
            uniform sampler2D uTexture;  // Текстура
            uniform bool useTexture;     // Флаг: использовать текстуру или цвет
            
            varying vec2 vTexCoord;      // Текстурные координаты из вершинного шейдера
            
            void main() {
                if (useTexture) {
                    gl_FragColor = texture2D(uTexture, vTexCoord); // Используем текстуру
                } else {
                    gl_FragColor = uColor; // Используем цвет
                }
            }
        """

        const val VERTEX_SHADER_CODE_OLD = """
            uniform mat4 uMVPMatrix;
            attribute vec4 vPosition;
            void main() {
                gl_Position = uMVPMatrix * vPosition;
            }
        """
        const val FRAGMENT_SHADER_CODE_OLD = """
            precision mediump float;
            uniform vec4 uColor;
            void main() {
                gl_FragColor = uColor;
            }
        """
    }

    var vertexShader: Int = 0
    var fragmentShader: Int = 0

    private fun loadShader(type: Int, shaderCode: String): Int =
        GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }

    fun initShader() {
        vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER_CODE)
        fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE)
    }

    fun initOldShader() {
        vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER_CODE_OLD)
        fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE_OLD)
    }
}