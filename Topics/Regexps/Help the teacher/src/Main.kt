fun main() {
    val report = readLine()!!
    //write your code here.
    val regexp = ". wrong answers?".toRegex()
    print(report.matches(regexp))
}