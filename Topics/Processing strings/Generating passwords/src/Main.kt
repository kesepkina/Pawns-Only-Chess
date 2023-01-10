import kotlin.random.Random

fun main() {
    // write your code here
    val num = (readln().split(" ").map { it.toInt() }).toMutableList()
    var pass = ""
    num.add(3, num[3] - (num[0] + num[1] + num[2]))
    val random = Random
    while(pass.length < num[4]) {
        val i = random.nextInt(0, 4)
        if (num[i] > 0) {
            val nextSymb = when (i) {
                0 -> ('A'..'Z').random()
                1 -> ('a'..'z').random()
                2 -> ('0'..'9').random()
                else -> ('A'..'z').random()
            }
            if (pass.isEmpty() || pass.last() != nextSymb) {
                pass += nextSymb
                num[i]--
            }
        }
    }
    print(pass)
}