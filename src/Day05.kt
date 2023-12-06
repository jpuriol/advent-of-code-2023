fun main() {

    fun part1(input: List<String>): Int {
        return 0
    }


    fun parseMap(input: List<String>): Pair<MapLongRange, Int> {
        val map = mutableMapOf<LongRange, LongRange>()

        var lines = 0

        for (line in input) {
            if (line.isBlank())
                break
            lines++

            val (destinationStart, sourceStart, length) =
                line.split(" ").map { it.toLong() }

            map[sourceStart..sourceStart + length] = destinationStart..destinationStart + length
        }


        return Pair(map, lines)
    }

    data class Almanac(
        val seeds: List<LongRange>,
        val seedToSoil: MapLongRange,
        val soilToFertilizer: MapLongRange,
        val fertilizerToWater: MapLongRange,
        val waterToLight: MapLongRange,
        val lightToTemperature: MapLongRange,
        val temperatureToHumidity: MapLongRange,
        val humidityToLocation: MapLongRange,
    )

    fun Almanac(input: List<String>): Almanac {

        val seeds = input[0].split(" ")
            .mapNotNull { it.toLongOrNull() }
            .pairAdjacentElements()
            .map { (start, length) -> start..<start + length }

        var seedToSoil: MapLongRange = emptyMap()
        var soilToFertilizer: MapLongRange = emptyMap()
        var fertilizerToWater: MapLongRange = emptyMap()
        var waterToLight: MapLongRange = emptyMap()
        var lightToTemperature: MapLongRange = emptyMap()
        var temperatureToHumidity: MapLongRange = emptyMap()
        var humidityToLocation: MapLongRange = emptyMap()

        var linesToSkip = 1

        for (i in input.indices) {
            if (linesToSkip < 0) {
                continue
            }

            val line = input[i]
            when (line) {
                "seed-to-soil map:" -> {
                    val (map, lines) = parseMap(input.subList(i + 1, input.size))
                    seedToSoil = map
                    linesToSkip = i + lines
                }

                "soil-to-fertilizer map:" -> {
                    val (map, lines) = parseMap(input.subList(i + 1, input.size))
                    soilToFertilizer = map
                    linesToSkip = i + lines
                }

                "fertilizer-to-water map:" -> {
                    val (map, lines) = parseMap(input.subList(i + 1, input.size))
                    fertilizerToWater = map
                    linesToSkip = i + lines
                }

                "water-to-light map:" -> {
                    val (map, lines) = parseMap(input.subList(i + 1, input.size))
                    waterToLight = map
                    linesToSkip = i + lines
                }

                "light-to-temperature map:" -> {
                    val (map, lines) = parseMap(input.subList(i + 1, input.size))
                    lightToTemperature = map
                    linesToSkip = i + lines
                }

                "temperature-to-humidity map:" -> {
                    val (map, lines) = parseMap(input.subList(i + 1, input.size))
                    temperatureToHumidity = map
                    linesToSkip = i + lines
                }

                "humidity-to-location map:" -> {
                    val (map, lines) = parseMap(input.subList(i + 1, input.size))
                    humidityToLocation = map
                    linesToSkip = i + lines
                }
            }

        }


        return Almanac(
            seeds = seeds,
            seedToSoil = seedToSoil,
            soilToFertilizer = soilToFertilizer,
            fertilizerToWater = fertilizerToWater,
            waterToLight = waterToLight,
            lightToTemperature = lightToTemperature,
            temperatureToHumidity = temperatureToHumidity,
            humidityToLocation = humidityToLocation,
        )

    }

    fun part2(input: List<String>): Long {
        val almanac = Almanac(input)

        val locations = almanac.humidityToLocation.values.asSequence().flatMap { it.asSequence() }
            .sorted()

        println("locations: ${locations.toList()}")

        for (location in locations) {

            val humidity = almanac.humidityToLocation.entries.find {
                location in it.value
            }?.let { entry ->
                mapValuesInRanges(location, entry.value, entry.key)
            } ?: continue

            val temperature = almanac.temperatureToHumidity.entries.find {
                humidity in it.value
            }?.let { entry ->
                mapValuesInRanges(humidity, entry.value, entry.key)
            } ?: continue


            val light = almanac.lightToTemperature.entries.find {
                temperature in it.value
            }?.let { entry ->
                mapValuesInRanges(temperature, entry.value, entry.key)
            } ?: continue

            val water = almanac.fertilizerToWater.entries.find {
                light in it.value
            }?.let { entry ->
                mapValuesInRanges(light, entry.value, entry.key)
            } ?: continue


            val fertilizer = almanac.soilToFertilizer.entries.find {
                water in it.value
            }?.let { entry ->
                mapValuesInRanges(water, entry.value, entry.key)
            } ?: continue

            val seed = almanac.seedToSoil.entries.find {
                fertilizer in it.value
            }?.let { entry ->
                mapValuesInRanges(fertilizer, entry.value, entry.key)
            }

            if (seed != null) {
                return location
            }
        }

        return 0
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    //  part1(input).println()
    part2(input).println()
}

typealias MapLongRange = Map<LongRange, LongRange>

fun List<Long>.pairAdjacentElements(): List<Pair<Long, Long>> {
    if (size % 2 != 0) {
        error("Input list must have an even number of elements.")
    }

    val result = mutableListOf<Pair<Long, Long>>()

    for (i in indices step 2) {
        val firstElement = this[i]
        val secondElement = this[i + 1]
        result.add(Pair(firstElement, secondElement))
    }

    return result
}

fun mapValuesInRanges(value: Long, range1: LongRange, range2: LongRange): Long? {
    val normalizedValue = value - range1.first

    return range2.first + normalizedValue
}