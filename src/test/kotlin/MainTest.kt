import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.whenever

class MainTest {

    @Nested
    inner class DefaultCodeGeneratorTest {
        private val generator = DefaultCodeGenerator(10)
        @Test
        fun oTest() {
            assertEquals(true, generator.generate() < 10)
        }
    }

    @Nested
    class CodeBreakerTest {
        @ParameterizedTest
        @MethodSource("evaluateTestSource")
        fun evaluateTest(code: Int, estimated: Int, expected: Boolean) {
            val generator : CodeGenerator = mock()
            val inputer : CodeInputer = mock()
            val codeBreaker = CodeBreaker(generator, inputer)

            assertEquals(expected, codeBreaker.evaluate(code, estimated))
        }

        companion object {
            @JvmStatic
            fun evaluateTestSource() = listOf(
               arguments(5, 4, false),
               arguments(5, 6, false),
               arguments(5, 5, true),
            )
        }

        @Test
        fun startThenReachesLimitTest() {
            val generator : CodeGenerator = mock()
            val inputer : CodeInputer = mock()

            whenever(generator.generate()).thenReturn(5)

            inputer.stub {
                on { input() } doReturn 6
            }

            val codeBreaker = CodeBreaker(generator, inputer)

            assertEquals(false, codeBreaker.start())
        }

        @Test
        fun startThenSuccessTest() {
            val generator : CodeGenerator = mock()
            val inputer : CodeInputer = mock()

            generator.stub {
                on { generate() } doReturn 5
            }
            inputer.stub {
                on { input() }
                    .doReturn(6)
                    .doReturn(4)
                    .doReturn(5)
            }

            val codeBreaker = CodeBreaker(generator, inputer)

            assertEquals(true, codeBreaker.start())
        }
    }
}