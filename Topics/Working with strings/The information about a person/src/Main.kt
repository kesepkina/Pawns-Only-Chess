import java.util.*

fun main() {
    // put your code here
    val scanner = Scanner(System.`in`)
    val firstName = scanner.next()
    val lastName = scanner.next()
    val age = scanner.nextInt()
    print("${firstName.first()}. $lastName, $age years old")
}