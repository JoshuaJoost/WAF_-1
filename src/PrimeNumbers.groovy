/**
 * Manages all prime numbers.
 * Class "Bruch.groovy" accesses the prime numbers for prime number decomposition.
 * @author  Joshua Joost
 * @version 1.0
 * @since   2019-10-17
 */
class PrimeNumbers {
    // If further prime numbers are needed, they are generated dynamically at runtime when method "addPrimeNumbers" is called.
    static BigDecimal[] primeNumbers = []

    /**
     * Adds further prime numbers to the array "primeNumbers". The search continues from the currently largest prime number.
     * @param howMuchPrimeNumbers Add n more prime numbers to the array "primeNumbers"
     */
    static void addPrimeNumbers(BigDecimal howMuchPrimeNumbers) {
        if (howMuchPrimeNumbers > 0) {
            BigDecimal endSize = primeNumbers.size() + howMuchPrimeNumbers
            Boolean isPrimeNumber

            BigDecimal startNumber = primeNumbers.size() == 0 ? 1 : primeNumbers[-1]
            for (int i = startNumber + 1; primeNumbers.size() < endSize; i++) {
                isPrimeNumber = true

                for (int j = 2; j <= i / 2; j++) {
                    if (i % j == 0) {
                        isPrimeNumber = false
                    }
                }

                if (isPrimeNumber) {
                    primeNumbers += i
                }
            }

        }
    }
}