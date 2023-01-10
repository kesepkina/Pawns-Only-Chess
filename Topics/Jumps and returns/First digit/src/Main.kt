fun main() {
    // put your code here
    for ( i in readln()) {
        if (i.isDigit()) {
            print(i)
            break
        }
    }
}