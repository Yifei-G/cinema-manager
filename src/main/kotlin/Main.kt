import java.lang.Exception

fun main(args: Array<String>) {
    // write your code here
    println("Enter the number of rows:")
    val cinemaRows = readln().toInt()
    println("Enter the number of seats in each row:")
    val cinemaSeats = readln().toInt()
    println()
    var cinemaMatrix = mutableListOf<List<String>>()
    var purchasedTickets = 0
    var currentIncome = 0

    //construct the matrix for further processing...
    for (row in 0..cinemaRows) {
        val rowList = mutableListOf<String>()
        var seatInfo = ""
        for (seat in 1.. cinemaSeats) {
            if (row == 0) seatInfo+="$seat " else seatInfo+="S "
        }
        rowList.add(seatInfo)
        cinemaMatrix.add(rowList)
    }


    var operation = generateOperationMenu()
    var termination = false

    while(!termination) {
        when (operation) {
            1 -> {
                generateCinemaUI(cinemaMatrix)
                operation = generateOperationMenu()
            }
            2 -> {
                println()
                println("Enter a row number:")
                val rowNumber = readln().toInt()
                println("Enter a seat number in that row:")
                val seatNumber = readln().toInt()
                println()

                try {
                    val maxRow = cinemaMatrix.lastIndex
                    val maxSeat = cinemaMatrix[0][0].split(" ").toList().lastIndex

                    if ((rowNumber > 0 && seatNumber > 0) && (rowNumber <= maxRow && seatNumber <= maxSeat)) {
                        cinemaMatrix = bookSeat(cinemaMatrix, rowNumber, seatNumber)
                        val income = buyTickets(rowNumber, cinemaRows, cinemaSeats)
                        currentIncome += income
                        purchasedTickets += 1
                        operation = generateOperationMenu()
                    } else {
                        throw IndexOutOfBoundsException("Wrong input!")
                    }
                } catch(e: Exception) {
                    println(e.message)
                    operation = 2
                }
            }
            3 -> {
                showStatistics(cinemaRows, cinemaSeats, purchasedTickets, currentIncome)
                operation = generateOperationMenu()
            }
            0 -> termination = true
            else -> println("Unknown operation")
        }
    }

}

private fun generateOperationMenu (): Int {
    println("1. Show the seats")
    println("2. Buy a ticket")
    println("3. Statistics")
    println("0. Exit")

    return readln().toInt()
}

private fun generateCinemaUI(cinema:  MutableList<List<String>>) {
    println("Cinema:")
    for (row in cinema.indices) {
        if (row == 0) {
            println("  ${cinema[row].joinToString()}")
        } else {
            println("$row ${cinema[row].joinToString()}")
        }
    }
    println()
}

private fun calculateTicketPrice(roomSize: Int, cinemaRows: Int, rowNumber: Int): Int {
    val ticketPrice = if (roomSize < 60) {
        10
    } else {
        val numberOfFirstHalfRows = cinemaRows / 2
        if (rowNumber <= numberOfFirstHalfRows) 10 else 8

    }
    return ticketPrice
}

private fun calculateTotalIncome(roomSize: Int, cinemaRows: Int, cinemaSeats: Int): Int {
    val totalIncome = if (roomSize < 60) {
        10 * roomSize
    } else {
        val numberOfFirstHalfRows = cinemaRows / 2
        numberOfFirstHalfRows * 10 * cinemaSeats  + (cinemaRows - numberOfFirstHalfRows) * 8 * cinemaSeats
    }
    return totalIncome
}


private fun bookSeat(cinema:  MutableList<List<String>>, rowNumber: Int, seatNumber: Int): MutableList<List<String>> {
    val seatStatus = cinema[rowNumber][0].split(' ').toMutableList()[seatNumber - 1]
    if (seatStatus == "S") {
        for (row in cinema.indices) {
            if (row == rowNumber) {
                val targetRow = cinema[row][0].split(' ').toMutableList()
                targetRow[seatNumber - 1] = "B"
                cinema[row] = arrayListOf(targetRow.joinToString(" "))
                break
            }
        }
    } else {
        throw Exception("That ticket has already been purchased!\n")
    }

    return cinema
}

private fun buyTickets(rowNumber: Int, cinemaRows: Int, cinemaSeats: Int): Int {

    val roomSize = cinemaRows * cinemaSeats
    val ticketPrice = calculateTicketPrice(roomSize, cinemaRows, rowNumber)
    println()
    println("Ticket price: $${ticketPrice}")
    println()
    return ticketPrice
}

private fun showStatistics(cinemaRows: Int, cinemaSeats: Int, purchasedTickets: Int, currentIncome: Int) {

    val roomSize = cinemaRows * cinemaSeats
    val result = (purchasedTickets.toDouble() / roomSize.toDouble()) * 100
    val formatPercentage = "%.2f".format(result)
    val totalIncome = calculateTotalIncome (roomSize, cinemaRows, cinemaSeats)
    println()
    println("Number of purchased tickets: $purchasedTickets")
    println("Percentage: ${formatPercentage}%")
    println("Current income: $${currentIncome}")
    println("Total income: $${totalIncome}")
    println()
}