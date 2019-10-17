/**
 * This class extends the Groovy language kernel by the arithmetic operations with a fraction.
 * This class is dependent on class "PrimeNumbers.groovy", since it provides the prime numbers required for prime number decomposition.
 * @author  Joshua Joost
 * @version 1.0
 * @since   2019-10-17
 */
class Bruch implements Comparable{
    BigDecimal counter // Zähler
    BigDecimal denominator // Nenner

    Bruch(BigDecimal counter = 1, BigDecimal denominator = 1) {
        if(denominator == 0){
            throw new IllegalArgumentException("Nenner darf nicht 0 sein!")
        }

        this.counter = counter
        this.denominator = denominator

        // denominator is unsigned
        if (denominator < 0) {
            this.denominator = -denominator
            this.counter = -counter
        }
    }

    // ------------ print operations
    String toString() {
        if (this.counter == 0) {
            return 0
        } else if (this.counter == this.denominator) {
            return 1
        } else if(this.denominator == 1){
            return this.counter
        }

        "$counter/$denominator"
    }

    // ------------ calc operations
    Bruch plus(Bruch b) {
        Bruch extendedFraction = expandFraction(this, b)
        Bruch extendedFractionB = expandFraction(b, this)

        shortenFracture(new Bruch(extendedFraction.counter + extendedFractionB.counter, extendedFraction.denominator))
    }

    Bruch plus(BigDecimal b) {
        plus(new Bruch(b * this.denominator, this.denominator))
    }

    Bruch minus(Bruch b) {
        Bruch extendedFraction = expandFraction(this, b)
        Bruch extendedFractionB = expandFraction(b, this)

        shortenFracture(new Bruch(extendedFraction.counter - extendedFractionB.counter, extendedFraction.denominator))
    }

    Bruch minus(BigDecimal b) {
        minus(new Bruch(b * this.denominator, this.denominator))
    }

    Bruch multiply(Bruch b) {
        shortenFracture(new Bruch(this.counter * b.counter, this.denominator * b.denominator))
    }

    Bruch multiply(BigDecimal b) {
        multiply(new Bruch(b))
    }

    Bruch div(Bruch b) {
        shortenFracture(new Bruch(this.counter * b.denominator, this.denominator * b.counter))
    }

    Bruch div(BigDecimal b) {
        div(new Bruch(b))
    }

    Bruch negative(){
        new Bruch(-this.counter, this.denominator)
    }

    Bruch positive(){
        new Bruch(this.counter, this.denominator)
    }

    Bruch power(BigDecimal power){
        if(power >= 0) {
            new Bruch(this.counter**power, this.denominator**power)
        } else {
            new Bruch(this.denominator**-power, this.counter**-power)
        }
    }

    // ------------ compare operations
    Boolean equals(Bruch comparedFraction){
        Bruch thisShortend = shortenFracture()
        Bruch comparedFractionShortend = shortenFracture(comparedFraction)

        thisShortend.counter == comparedFractionShortend.counter && thisShortend.denominator == comparedFractionShortend.denominator
    }

    @Override
    int compareTo(Object o){
        Bruch comparedFraction = o as Bruch

        Bruch thisExpanded = expandFraction(this, comparedFraction)
        Bruch comparedFractionExpanded = expandFraction(comparedFraction, this)

        thisExpanded.counter <=> comparedFractionExpanded.counter
    }

    // ------------ typecast operations
    Object asType(Class targetClass){
        switch(targetClass){
            case BigDecimal:
                return new BigDecimal(this.counter / this.denominator)
            default:
                super.asType(targetClass)
        }
    }

    // ------------ fracture operations
    // Bruch erweitern
    Bruch expandFraction(Bruch extended, Bruch byThisOne) {
        new Bruch(extended.counter * byThisOne.denominator, extended.denominator * byThisOne.denominator)
    }

    // Bruch kürzen
    Bruch shortenFracture() {
        shortenFracture(new Bruch(this.counter, this.denominator))
    }

    Bruch shortenFracture(Bruch fracture) {
        Bruch result = new Bruch(fracture.counter, fracture.denominator)
        BigDecimal[] primeDivisables = dividers(result)

        while (primeDivisables.size() > 0) {
            result.counter /= primeDivisables[-1]
            result.denominator /= primeDivisables[-1]

            primeDivisables = dividers(result)
        }

        result
    }

    // Teiler finden
    BigDecimal[] dividers() {
        dividers(new Bruch(this.counter, this.denominator))
    }

    BigDecimal[] dividers(Bruch fracture) {
        BigDecimal[] primeDivisables = []

        //Add new prime numbers if necessary
        BigDecimal[] newAddedPrimeNumbers = []

        if(PrimeNumbers.getPrimeNumbers().size() < 1){
            PrimeNumbers.addPrimeNumbers(1)
            newAddedPrimeNumbers += PrimeNumbers.getPrimeNumbers()[-1]
            println("New Prime Numbers added: $newAddedPrimeNumbers")
        }
        if((PrimeNumbers.getPrimeNumbers()[-1] < fracture.counter / 2) || (PrimeNumbers.getPrimeNumbers()[-1] < fracture.denominator / 2)) {
            while((PrimeNumbers.getPrimeNumbers()[-1] < fracture.counter / 2) || (PrimeNumbers.getPrimeNumbers()[-1] < fracture.denominator / 2)){
                PrimeNumbers.addPrimeNumbers(1)
                newAddedPrimeNumbers += PrimeNumbers.getPrimeNumbers()[-1]
            }
            println("New Prime Numbers added: $newAddedPrimeNumbers")
        }

        // find prime numbers for prime decomposition
        PrimeNumbers.getPrimeNumbers().each {
            if (fracture.counter < it && fracture.denominator < it) {
                return primeDivisables
            }
            if (((fracture.counter as int) % (it as int) == 0) && ((fracture.denominator as int) % (it as int) == 0)) {
                primeDivisables += it
            }
        }

        primeDivisables
    }

}