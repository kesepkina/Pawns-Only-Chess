fun main() {
    val answer = readln()
    // write your code here
    val reg = "I can'?t? do my homework on time!".toRegex()
    print(answer.matches(reg))
}