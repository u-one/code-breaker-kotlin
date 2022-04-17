import java.lang.NumberFormatException
import java.security.SecureRandom

interface CodeGenerator {
    fun generate(): Int
}

class DefaultCodeGenerator(val bound: Int) : CodeGenerator {
    override fun generate(): Int {
        val secureRandom = SecureRandom()
        return secureRandom.nextInt(bound)
    }
}

interface CodeInputer {
    fun input() : Int
}

class DefaultCodeInputer : CodeInputer {
    override fun input(): Int {
        for(i in 1..3) {
            val str = readLine()
            println("input: $str")
            try {
                val number = str?.toInt()
                println("num: $number")
                return number!!
            } catch (e: NumberFormatException) {
                println("Error: $e : $str")
            }
        }
        throw Exception("Abort: could not input valid number")
    }
}

class CodeBreaker(codeGenerator: CodeGenerator,
                  val codeInputer: CodeInputer,
                  val maxRetry: Int = 100) {
    val code = codeGenerator.generate()
    var turn = 0

    fun start() :Boolean {
        for (i in 1..maxRetry) {
            turn = i
            val estimated = codeInputer.input()
            if (evaluate(code, estimated)) {
                return true
            }
        }
        return false
    }
    fun evaluate(code: Int, estimated: Int) : Boolean {
        val diff = code - estimated
        return when {
            diff == 0 -> {
                println("Correct!!!")
                true
            }
            diff > 0 -> {
                println("[$turn] Smaller")
                false
            }
            diff < 0 -> {
                println("[$turn] Larger")
                false
            }
            else -> {
                false
            }
        }
    }
}

fun main(args: Array<String>) {
    println("Hello World!")

    val codeGenerator = DefaultCodeGenerator(10000)
    val codeInputer = DefaultCodeInputer()
    val codeBreaker = CodeBreaker(codeGenerator, codeInputer)
    codeBreaker.start()

    // Try adding program arguments at Run/Debug configuration
    println("Program arguments: ${args.joinToString()}")
}

